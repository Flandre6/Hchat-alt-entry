package h.Hchat.hooks.items.securemessage

import de.robv.android.xposed.XC_MethodHook
import h.Hchat.dexkit.DexMethodCache
import h.Hchat.event.Events
import h.Hchat.hooks.api.core.WeChatApis
import h.Hchat.hooks.core.BaseFeature
import h.Hchat.hooks.core.DexInstallScheduler
import h.Hchat.hooks.core.FeatureContext
import h.Hchat.hooks.core.HookRegistry
import h.Hchat.hooks.api.model.WeChatMessageTypes
import h.Hchat.preferences.HchatStorage
import h.Hchat.utils.KavaReflector
import org.luckypray.dexkit.query.FindMethod
import org.luckypray.dexkit.query.matchers.MethodMatcher
import java.lang.reflect.Method
import java.lang.reflect.Modifier

/** Injects the WeChat secure-message marker before outgoing supported messages are stored. */
class SendSecureMessageFeature : BaseFeature() {
    @Volatile private var installed = false
    @Volatile private var mergeInstalled = false
    @Volatile private var emojiSourceInstalled = false
    @Volatile private var emojiDispatchInstalled = false
    @Volatile private var emojiCheckInstalled = false
    private var prefs: android.content.SharedPreferences? = null
    private lateinit var methodPrefs: android.content.SharedPreferences
    @Volatile private var markerLogged = false
    @Volatile private var markerFailureLogged = false
    @Volatile private var hookMissLogged = false
    @Volatile private var emojiSourceTriggeredLogged = false
    @Volatile private var emojiDispatchTriggeredLogged = false
    @Volatile private var emojiCheckTriggeredLogged = false
    @Volatile private var unsupportedEmojiVersionLogged = false

    override fun featureId(): String = SecureMessageSettings.SEND_ID
    override fun name(): String = "安全消息"

    override fun onFeatureInit(context: FeatureContext) {
        registerSettingsProvider(SendSecureMessageSettingsProvider())
    }

    override fun onFeatureInstall(context: FeatureContext) {
        prefs = HchatStorage.preferences(context.hostContext(), SecureMessageSettings.SEND_PREFS)
        methodPrefs = DexMethodCache.prefs(context.hostContext(), "Hchat_secure_message_method_cache")
        logInfo("安全消息功能已初始化，等待 DexKit")
        schedule(context)
        subscribe(Events.DexReady::class.java) { schedule(context) }
    }

    private fun schedule(context: FeatureContext) {
        DexInstallScheduler.schedule(featureId(), name()) { installHook(context) }
    }

    @Synchronized
    private fun installHook(context: FeatureContext): Boolean {
        if (installed && mergeInstalled && emojiHooksReady(context) && emojiCheckInstalled) return true
        val runtimeKey = methodCacheKey(context)
        if (runtimeKey.isBlank()) {
            logError("安全消息安装跳过：微信运行时版本信息未就绪", null)
            return false
        }
        val direct = context.dexFinder().localMessageInsertMethod?.takeIf(::isInsertMethod)
        if (direct != null) {
            DexMethodCache.save(methodPrefs, runtimeKey, SecureMessageSettings.CACHE_INSERT, direct)
            logInfo("安全消息使用微信本地消息插入 API: ${direct.toGenericString()}")
        }
        val insert = direct ?: cachedOrLocate(context, runtimeKey, SecureMessageSettings.CACHE_INSERT, INSERT_ANCHOR, ::isInsertMethod)
            ?: run {
                logError("安全消息入库方法未定位到，微信版本可能不匹配", null)
                return false
            }
        val insertReady = if (installed) true else runCatching {
            HookRegistry.get().hook(insert, object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    if (!enabled()) return
                    val args = param.args ?: return
                    val msg = args.firstOrNull { it != null && isMessageLike(it) } ?: run {
                        if (!hookMissLogged) {
                            hookMissLogged = true
                            logInfo("安全消息 Hook 已触发，但未找到消息参数")
                        }
                        return
                    }
                    if (!isSupportedOutgoingMessage(msg)) return
                    addSecureMarker(msg)
                }
            })
            installed = true
            logInfo("安全消息 Hook 已安装: ${insert.toGenericString()}")
            true
        }.getOrElse {
            logError("安全消息入库Hook安装失败", it)
            false
        }
        val mergeReady = if (mergeInstalled) true else installSourceMergeHooks(insert)
        val emojiReady = installEmojiHooks(context)
        val emojiCheckReady = if (emojiCheckInstalled) true else installEmojiSelfCheckHooks(context, runtimeKey)
        return insertReady && mergeReady && emojiReady && emojiCheckReady
    }

    /**
     * 表情发送会在本地消息入库后重新生成 MsgSource，因此通用入库 Hook 不足以覆盖
     * 最终网络请求。这里按已确认的 8.0.76/8.0.77 链路补写两次标记。
     */
    private fun installEmojiHooks(context: FeatureContext): Boolean {
        val version = WeChatApis.version()?.current()
        val profile = EmojiSecureMessageProfile.forVersion(version?.versionName, version?.versionCode ?: 0L)
        if (profile == null) {
            if (!unsupportedEmojiVersionLogged) {
                unsupportedEmojiVersionLogged = true
                logInfo(
                    "表情安全消息专用链路未启用: 微信 ${version?.displayVersion() ?: "版本未知"} " +
                        "不在已验证映射中"
                )
            }
            return true
        }
        val hostVersion = version?.displayVersion() ?: "未知"
        val sourceReady = emojiSourceInstalled || installEmojiSourceHook(context, profile, hostVersion)
        val dispatchReady = emojiDispatchInstalled || installEmojiDispatchHook(context, profile, hostVersion)
        return sourceReady && dispatchReady
    }

    private fun emojiHooksReady(context: FeatureContext): Boolean {
        val version = WeChatApis.version()?.current()
        val supported = EmojiSecureMessageProfile.forVersion(
            version?.versionName,
            version?.versionCode ?: 0L
        ) != null
        return !supported || (emojiSourceInstalled && emojiDispatchInstalled)
    }

    /**
     * 自发出的表情在本地回显/数据库更新后可能丢失 msgSource，但长按菜单仍会经过
     * 微信的 sec_msg_node 检查。对 type=47 且 isSend=1 的消息做精确兜底，避免自己
     * 可以转发表情、别人却不能转发的不一致行为；反安全消息开关不参与此判断。
     */
    private fun installEmojiSelfCheckHooks(context: FeatureContext, runtimeKey: String): Boolean {
        val candidates = linkedSetOf<Method>()
        DexMethodCache.load(
            methodPrefs,
            runtimeKey,
            context.hostClassLoader(),
            SecureMessageSettings.CACHE_EMOJI_CHECK
        )?.takeIf(::isCheckMethod)?.let(candidates::add)
        candidates += findMethods(context, SECURE_CHECK_ANCHOR).filter(::isCheckMethod)
        if (candidates.isEmpty()) {
            logError("自发表情安全检查兜底入口未定位到", null)
            return true
        }
        if (candidates.size == 1) {
            DexMethodCache.save(
                methodPrefs,
                runtimeKey,
                SecureMessageSettings.CACHE_EMOJI_CHECK,
                candidates.first()
            )
        } else {
            DexMethodCache.clear(methodPrefs, runtimeKey, SecureMessageSettings.CACHE_EMOJI_CHECK)
        }
        var hooked = false
        candidates.take(MAX_CHECK_HOOKS).forEach { method ->
            runCatching {
                HookRegistry.get().hook(method, object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (!enabled()) return
                        val message = param.args?.firstOrNull { it != null && isMessageLike(it) } ?: return
                        if (!isOutgoingEmoji(message)) return
                        if (SecureMessageSource.containsMarker(readMessageSource(message))) return
                        param.result = true
                        if (!emojiCheckTriggeredLogged) {
                            emojiCheckTriggeredLogged = true
                            logInfo("自发表情安全检查兜底已生效: ${method.name}")
                        }
                    }
                })
                hooked = true
            }.onFailure {
                logError("自发表情安全检查 Hook 安装失败: ${method.toGenericString()}", it)
            }
        }
        emojiCheckInstalled = hooked
        if (hooked) logInfo("自发表情安全检查兜底 Hook 已安装: ${candidates.size} 个入口")
        return hooked
    }

    private fun isOutgoingEmoji(message: Any): Boolean {
        val send = readNumber(message, "field_isSend", "isSend", "getIsSend", "getSend")?.toInt()
        if (send != 1) return false
        return readNumber(message, "field_type", "type", "getType", "getMsgType")?.toInt() == EMOJI_TYPE
    }

    private fun isCheckMethod(method: Method): Boolean =
        Modifier.isStatic(method.modifiers) &&
            method.returnType == Boolean::class.javaPrimitiveType &&
            method.parameterCount == 1 &&
            !method.parameterTypes[0].isPrimitive

    private fun installEmojiSourceHook(
        context: FeatureContext,
        profile: EmojiSecureMessageProfile,
        hostVersion: String
    ): Boolean = runCatching {
        val loader = context.hostClassLoader()
        val messageClass = requireNotNull(KavaReflector.loadClass(EMOJI_MESSAGE_CLASS, loader)) {
            "未找到 $EMOJI_MESSAGE_CLASS"
        }
        val owner = requireNotNull(KavaReflector.loadClass(profile.sourceOwner, loader)) {
            "未找到 ${profile.sourceOwner}"
        }
        val target = requireNotNull(KavaReflector.findMethodRecursive(owner, EMOJI_SOURCE_METHOD, messageClass)) {
            "未找到 ${profile.sourceOwner}.$EMOJI_SOURCE_METHOD(${messageClass.name})"
        }
        check(target.returnType == String::class.java) { "表情 MsgSource 方法返回类型不匹配: $target" }
        HookRegistry.get().hook(target, object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam) {
                if (!enabled() || param.hasThrowable()) return
                val message = param.args?.getOrNull(0) ?: return
                if (readNumber(message, "field_type", "type", "getType", "getMsgType")?.toInt() != EMOJI_TYPE) {
                    return
                }
                param.result = SecureMessageSource.addMarker(param.result as? String)
                if (!emojiSourceTriggeredLogged) {
                    emojiSourceTriggeredLogged = true
                    logInfo("表情 MsgSource 安全标记已写入")
                }
            }
        })
        emojiSourceInstalled = true
        logInfo("表情 MsgSource Hook 已安装[$hostVersion]: ${target.toGenericString()}")
        true
    }.getOrElse {
        logError("表情 MsgSource Hook 安装失败[$hostVersion]", it)
        false
    }

    private fun installEmojiDispatchHook(
        context: FeatureContext,
        profile: EmojiSecureMessageProfile,
        hostVersion: String
    ): Boolean = runCatching {
        val loader = context.hostClassLoader()
        val scene = requireNotNull(KavaReflector.loadClass(profile.emojiScene, loader)) {
            "未找到 ${profile.emojiScene}"
        }
        val network = requireNotNull(KavaReflector.loadClass(EMOJI_NETWORK_CLASS, loader)) {
            "未找到 $EMOJI_NETWORK_CLASS"
        }
        val dispatcher = requireNotNull(KavaReflector.loadClass(EMOJI_DISPATCHER_CLASS, loader)) {
            "未找到 $EMOJI_DISPATCHER_CLASS"
        }
        val target = requireNotNull(
            KavaReflector.findMethodRecursive(scene, EMOJI_DISPATCH_METHOD, network, dispatcher)
        ) {
            "未找到 ${profile.emojiScene}.$EMOJI_DISPATCH_METHOD(${network.name},${dispatcher.name})"
        }
        check(target.returnType == Int::class.javaPrimitiveType) { "表情请求分发方法返回类型不匹配: $target" }
        HookRegistry.get().hook(target, object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam) {
                if (!enabled()) return
                try {
                    val request = KavaReflector.readField(param.thisObject, EMOJI_SCENE_REQUEST_FIELD) ?: return
                    val wrapper = KavaReflector.readField(request, EMOJI_REQUEST_WRAPPER_FIELD) ?: return
                    val list = KavaReflector.readField(wrapper, EMOJI_REQUEST_LIST_FIELD) ?: return
                    val items = KavaReflector.readField(list, EMOJI_ITEMS_FIELD) as? java.util.List<*> ?: return
                    val item = items.firstOrNull() ?: return
                    val field = KavaReflector.findFieldRecursive(item.javaClass, EMOJI_SOURCE_FIELD)
                    val source = KavaReflector.readField(field, item) as? String
                    check(KavaReflector.writeField(field, item, SecureMessageSource.addMarker(source))) {
                        "写入 sendemoji 请求 MsgSource 失败"
                    }
                    if (!emojiDispatchTriggeredLogged) {
                        emojiDispatchTriggeredLogged = true
                        logInfo("表情 sendemoji 请求安全标记已写入")
                    }
                } catch (error: Throwable) {
                    logError("表情 sendemoji 请求安全标记注入失败[$hostVersion]", error)
                }
            }
        })
        emojiDispatchInstalled = true
        logInfo("表情 sendemoji Hook 已安装[$hostVersion]: ${target.toGenericString()}")
        true
    }.getOrElse {
        logError("表情 sendemoji Hook 安装失败[$hostVersion]", it)
        false
    }

    /**
     * Media senders assign msgSource before the final local insert. Hook the setter layer
     * as well so the marker reaches the actual image/video/emoji/AppMsg send request.
     * Candidates are derived from the already verified insert argument class; no
     * obfuscated method name is guessed.
     */
    private fun installSourceMergeHooks(insert: Method): Boolean {
        val messageClass = insert.parameterTypes.firstOrNull {
            !it.isPrimitive && it != String::class.java && it.name.startsWith(MESSAGE_PACKAGE)
        } ?: return false
        val candidates = generateSequence<Class<*>>(messageClass) { current -> current.superclass }
            .takeWhile { it != Any::class.java }
            .flatMap { KavaReflector.declaredMethods(it).asSequence() }
            .filter(::isStringSetter)
            .distinctBy { it.toGenericString() }
            .toList()
        if (candidates.isEmpty()) {
            logError("安全消息 msgSource 合并入口未找到: ${messageClass.name}", null)
            return false
        }
        var count = 0
        candidates.forEach { method ->
            runCatching {
                HookRegistry.get().hook(method, object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (!enabled()) return
                        val source = param.args?.getOrNull(0) as? String ?: return
                        if (!looksLikeMsgSource(source)) return
                        val message = param.thisObject ?: return
                        if (!isSupportedOutgoingMessage(message)) return
                        param.args[0] = SecureMessageSource.addMarker(source)
                    }
                })
                count++
            }.onFailure {
                logError("安全消息 msgSource 候选 Hook 安装失败: ${method.toGenericString()}", it)
            }
        }
        mergeInstalled = count > 0
        if (mergeInstalled) logInfo("安全消息 msgSource 合并 Hook 已安装: $count 个候选")
        return mergeInstalled
    }

    private fun enabled(): Boolean = prefs?.getBoolean(SecureMessageSettings.KEY_ENABLE, SecureMessageSettings.DEFAULT_ENABLE) == true

    private fun isMessageLike(value: Any): Boolean =
        readNumber(value, "field_type", "type", "getType", "getMsgType") != null ||
            SOURCE_FIELDS.any { KavaReflector.readField(value, it) != null }

    private fun addSecureMarker(message: Any) {
        val current = readMessageSource(message)
        if (SecureMessageSource.containsMarker(current)) return
        val updated = SecureMessageSource.addMarker(current)
        if (!setMessageSource(message, updated)) {
            if (!markerFailureLogged) {
                markerFailureLogged = true
                logError("安全消息标记注入失败: msgSource 不可写，类型=${message.javaClass.name}", null)
            }
        } else if (!markerLogged) {
            markerLogged = true
            logInfo("安全消息标记已写入消息")
        }
    }

    private fun readMessageSource(message: Any): String {
        for (fieldName in SOURCE_FIELDS) {
            (KavaReflector.readField(message, fieldName) as? String)?.let { return it }
        }
        return (KavaReflector.invokeMethod(message, "getMsgSource") as? String).orEmpty()
    }

    private fun setMessageSource(message: Any, value: String): Boolean {
        for (name in SOURCE_SETTERS) {
            val method = KavaReflector.findCompatibleMethod(message.javaClass, name, value)
            if (KavaReflector.invokeSuccessfully(method, message, value)) return true
        }
        return SOURCE_FIELDS.any { fieldName ->
            KavaReflector.writeField(message, fieldName, value)
        }
    }

    private fun isSupportedOutgoingMessage(message: Any): Boolean {
        val send = readNumber(message, "field_isSend", "isSend", "getIsSend", "getSend")
        if (send?.toInt() != 1) return false
        val type = readNumber(message, "field_type", "type", "getType", "getMsgType")
        if (type == null) return true
        val normalized = WeChatMessageTypes.normalize(type.toInt())
        return normalized == WeChatMessageTypes.TEXT ||
            normalized == WeChatMessageTypes.IMAGE ||
            normalized == WeChatMessageTypes.VIDEO ||
            normalized == VIDEO_COMPAT ||
            normalized == WeChatMessageTypes.EMOJI ||
            normalized == WeChatMessageTypes.APP
    }

    private fun readNumber(receiver: Any, vararg names: String): Number? {
        for (name in names) {
            val value = KavaReflector.readField(receiver, name) ?: KavaReflector.invokeMethod(receiver, name)
            if (value is Number) return value
        }
        return null
    }

    private fun isInsertMethod(method: Method): Boolean {
        if (method.parameterCount !in 1..2) return false
        if (method.returnType != Void.TYPE && method.returnType != Long::class.javaPrimitiveType) return false
        return method.parameterTypes.any { !it.isPrimitive }
    }

    private fun isStringSetter(method: Method): Boolean {
        return !Modifier.isStatic(method.modifiers) &&
            !Modifier.isAbstract(method.modifiers) &&
            method.returnType == Void.TYPE &&
            method.parameterTypes.contentEquals(arrayOf(String::class.java))
    }

    private fun looksLikeMsgSource(value: String): Boolean {
        val trimmed = value.trimStart()
        return trimmed.startsWith("<msgsource", ignoreCase = true) &&
            trimmed.contains("</msgsource>", ignoreCase = true)
    }

    private fun cachedOrLocate(context: FeatureContext, runtimeKey: String, name: String, anchor: String, predicate: (Method) -> Boolean): Method? {
        DexMethodCache.load(methodPrefs, runtimeKey, context.hostClassLoader(), name)?.takeIf(predicate)?.let { return it }
        val candidates = findMethods(context, anchor)
            .filter(predicate).distinctBy { it.toGenericString() }
        val method = candidates.maxByOrNull { candidate ->
            val params = candidate.parameterTypes
            var score = 0
            if (params.size == 2 && params[1] == Boolean::class.javaPrimitiveType) score += 8
            if (params.any { it.name.startsWith("com.tencent.mm.storage.") }) score += 6
            if (params.any { it.name.contains("Msg", true) || it.simpleName.equals("k9", true) }) score += 2
            score
        }
        if (method != null) DexMethodCache.save(methodPrefs, runtimeKey, name, method) else DexMethodCache.clear(methodPrefs, runtimeKey, name)
        return method
    }

    private fun methodCacheKey(context: FeatureContext): String = DexMethodCache.runtimeKey(context.hostContext(), context.hostClassLoader())
        .takeIf { it.isNotBlank() }
        ?.let { "$it|${SecureMessageSettings.CACHE_SCHEMA}" }
        .orEmpty()

    private fun findMethods(context: FeatureContext, anchor: String): List<Method> = runCatching {
        val exact = context.dexKitBridge().findMethod(FindMethod().apply {
            matcher(MethodMatcher().apply { usingEqStrings(anchor) })
        })
        val candidates = if (exact.isNotEmpty()) exact else {
            runCatching {
                context.dexKitBridge().findMethod(FindMethod().apply {
                    matcher(MethodMatcher().apply { usingStrings(anchor) })
                })
            }.getOrDefault(emptyList())
        }
        candidates.mapNotNull { data -> runCatching { data.getMethodInstance(context.hostClassLoader()) }.getOrNull() }
            .distinctBy { it.toGenericString() }
    }.getOrElse {
        logError("安全消息方法定位失败", it)
        emptyList()
    }

    private companion object {
        const val MAX_CHECK_HOOKS = 6
        const val INSERT_ANCHOR = "Error insert message msg:%s talker:%s"
        const val SECURE_CHECK_ANCHOR = ".msgsource.sec_msg_node.sfn"
        val SOURCE_SETTERS = arrayOf("setMsgSource", "setMsgsource", "setSource")
        // 8.0.77 (e9) stores MsgInfo.msgSource in the obfuscated G field.
        val SOURCE_FIELDS = arrayOf("field_msgSource", "msgSource", "G", "g")
        const val VIDEO_COMPAT = 62
        const val MESSAGE_PACKAGE = "com.tencent.mm.storage."
        const val EMOJI_TYPE = 47
        const val EMOJI_MESSAGE_CLASS = "com.tencent.mm.storage.e9"
        const val EMOJI_SOURCE_METHOD = "a"
        const val EMOJI_DISPATCH_METHOD = "doScene"
        const val EMOJI_NETWORK_CLASS = "com.tencent.mm.network.s"
        const val EMOJI_DISPATCHER_CLASS = "com.tencent.mm.modelbase.u0"
        const val EMOJI_SCENE_REQUEST_FIELD = "d"
        const val EMOJI_REQUEST_WRAPPER_FIELD = "a"
        const val EMOJI_REQUEST_LIST_FIELD = "a"
        const val EMOJI_ITEMS_FIELD = "e"
        const val EMOJI_SOURCE_FIELD = "p"
    }
}
