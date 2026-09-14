package h.Hchat.hooks.items.specialmessage.extracted

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import h.Hchat.hooks.core.FeatureContext
import h.Hchat.preferences.HchatStorage
import h.Hchat.utils.KavaReflector

/**
 * 从 safety-message-8077.zip 提取的表情安全消息 Hook。
 *
 * 这不是已注册到 FeatureRegistry 的完整功能；调用方需要自行提供 hooks 容器、
 * 错误日志和 sec_msg_node 写回函数。
 */
object EmojiSafetyMessageHooks {
    const val PREFS = "hchat_special_message"

    fun installMediaHook(
        context: FeatureContext,
        profile: SafetyMessageEmojiProfile,
        hostVersion: String,
        hooks: MutableList<XC_MethodHook.Unhook>,
        appendNode: (String, Boolean) -> String,
        logError: (String, Throwable?) -> Unit
    ): Boolean = runCatching {
        val loader = context.hostClassLoader()
        val target = HostReflection.method(
            HostReflection.findClass(profile.sourceOwner, loader),
            "a",
            HostReflection.findClass("com.tencent.mm.storage.e9", loader)
        )
        check(target.returnType == String::class.java)
        val sp = HchatStorage.preferences(context.hostContext(), PREFS)
        hooks += XposedBridge.hookMethod(target, object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam) {
                if (param.hasThrowable() || !sp.getBoolean("emoji", false)) return
                val message = param.args.getOrNull(0) ?: return
                try {
                    if ((HostReflection.callMethod(message, "getType") as? Number)?.toInt() != 47) return
                    val source = param.result as? String ?: ""
                    param.result = appendNode(source, false)
                } catch (error: Throwable) {
                    logError("微信 $hostVersion 表情安全节点注入失败: $target", error)
                }
            }
        })
        true
    }.getOrElse {
        logError("微信 $hostVersion 表情安全链路未就绪", it)
        false
    }

    /**
     * 在表情请求进入网络分发前再写一次 MsgSource，覆盖上传阶段重新构建 MsgSource 的情况。
     * 目标请求是 sendemoji（8077 的 cmdId 为 175）。
     */
    fun installEmojiDispatchHook(
        context: FeatureContext,
        profile: SafetyMessageEmojiProfile,
        hostVersion: String,
        hooks: MutableList<XC_MethodHook.Unhook>,
        appendNode: (String, Boolean) -> String,
        logError: (String, Throwable?) -> Unit
    ): Boolean = runCatching {
        val loader = context.hostClassLoader()
        val scene = HostReflection.findClass(profile.emojiScene, loader)
        val dispatch = HostReflection.method(
            scene,
            "doScene",
            HostReflection.findClass("com.tencent.mm.network.s", loader),
            HostReflection.findClass("com.tencent.mm.modelbase.u0", loader)
        )
        check(dispatch.returnType == Int::class.javaPrimitiveType)
        val envelope = HostReflection.findField(scene, "d").type
        val wrapper = HostReflection.findField(envelope, "a").type
        HostReflection.findField(wrapper, "a")
        val sp = HchatStorage.preferences(context.hostContext(), PREFS)
        hooks += XposedBridge.hookMethod(dispatch, object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam) {
                if (!sp.getBoolean("emoji", false)) return
                try {
                    val request = HostReflection.getObjectField(param.thisObject, "d") ?: return
                    val list = HostReflection.getObjectField(
                        HostReflection.getObjectField(request, "a"), "a"
                    ) ?: return
                    val items = HostReflection.getObjectField(list, "e") as? java.util.LinkedList<*> ?: return
                    val item = items.firstOrNull() ?: return
                    val field = HostReflection.findField(item.javaClass, "p")
                    val source = KavaReflector.readField(field, item)?.toString().orEmpty()
                    check(KavaReflector.writeField(field, item, appendNode(source, false))) {
                        "写入表情请求 MsgSource 失败"
                    }
                } catch (error: Throwable) {
                    logError("微信 $hostVersion 表情请求安全节点注入失败: $dispatch", error)
                }
            }
        })
        true
    }.getOrElse {
        logError("微信 $hostVersion 表情请求分发链路未就绪", it)
        false
    }

    /** 写入或更新 sec_msg_node，保留节点属性、uuid 和未知字段。 */
    fun appendNode(source: String, link: Boolean = false): String {
        val flag = if (link) "1" else ""
        val preview = if (link) 1 else 0
        val node = "<sec_msg_node><sfn>1</sfn><show-h5><![CDATA[]]></show-h5>" +
            "<clip-len>0</clip-len><share-tip-url><![CDATA[]]></share-tip-url>" +
            "<sec-ctrl-flag><![CDATA[$flag]]></sec-ctrl-flag><fold-reduce>0</fold-reduce>" +
            "<media-to-emoji>0</media-to-emoji><block-range>0</block-range>" +
            "<bubble-type>2</bubble-type><preview-type>$preview</preview-type>" +
            "<url-click-type>$preview</url-click-type></sec_msg_node>"
        if (source.isBlank()) return "<msgsource>$node</msgsource>"
        val existing = safetyNodePattern.find(source)
        if (existing != null) {
            val raw = existing.value
            val opening = raw.substringBefore('>') + ">"
            var preserved = if (opening.endsWith("/>")) ""
            else raw.substringAfter('>').substringBeforeLast("</sec_msg_node")
            policyFieldPatterns.forEach { pattern -> preserved = preserved.replace(pattern, "") }
            val policy = node.removePrefix("<sec_msg_node>").removeSuffix("</sec_msg_node>")
            val merged = opening.removeSuffix("/>").let {
                if (opening.endsWith("/>")) "$it>" else it
            } + preserved + policy + "</sec_msg_node>"
            return source.replaceRange(existing.range, merged)
        }
        val close = source.indexOf("</msgsource>")
        return if (close >= 0) source.substring(0, close) + node + source.substring(close)
        else "<msgsource>$source$node</msgsource>"
    }

    private val safetyNodePattern = Regex(
        "<sec_msg_node\\b[^>]*>.*?</sec_msg_node\\s*>|<sec_msg_node\\b[^>]*/>",
        RegexOption.DOT_MATCHES_ALL
    )
    private val policyFieldPatterns = listOf(
        "sfn", "show-h5", "clip-len", "share-tip-url", "sec-ctrl-flag",
        "fold-reduce", "media-to-emoji", "block-range", "bubble-type",
        "preview-type", "url-click-type"
    ).map { field ->
        Regex("<$field\\b[^>]*>.*?</$field\\s*>|<$field\\b[^>]*/>", RegexOption.DOT_MATCHES_ALL)
    }

    private object HostReflection {
        fun findClass(name: String, loader: ClassLoader): Class<*> =
            requireNotNull(KavaReflector.loadClass(name, loader)) { "未找到宿主类 $name" }

        fun method(owner: Class<*>, name: String, vararg types: Class<*>): java.lang.reflect.Method =
            requireNotNull(KavaReflector.findMethodRecursive(owner, name, *types)) {
                "未找到宿主方法 ${owner.name}.$name"
            }

        fun findField(owner: Class<*>, name: String): java.lang.reflect.Field =
            requireNotNull(KavaReflector.findFieldRecursive(owner, name)) {
                "未找到宿主字段 ${owner.name}.$name"
            }

        fun getObjectField(receiver: Any?, name: String): Any? {
            requireNotNull(receiver) { "读取 $name 时宿主对象为空" }
            return KavaReflector.readField(findField(receiver.javaClass, name), receiver)
        }

        fun callMethod(receiver: Any, name: String, vararg args: Any?): Any? =
            KavaReflector.invokeOrThrow(
                requireNotNull(KavaReflector.findCompatibleMethod(receiver.javaClass, name, *args)) {
                    "未找到宿主方法 ${receiver.javaClass.name}.$name"
                },
                receiver,
                *args
            )
    }
}
