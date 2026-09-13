package h.Hchat.hooks.items.inputtoolbar

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import de.robv.android.xposed.XC_MethodHook
import h.Hchat.event.Events
import h.Hchat.hooks.api.core.WeChatApis
import h.Hchat.hooks.core.BaseFeature
import h.Hchat.hooks.core.FeatureContext
import h.Hchat.hooks.core.HookRegistry
import h.Hchat.ui.miuix.VoiceForwardMiuixDialog
import h.Hchat.utils.KavaReflector
import java.util.Collections
import java.util.WeakHashMap

class InputToolbarFeature : BaseFeature() {
    private var runtime: InputToolbarRuntime? = null

    override fun featureId(): String = ID

    override fun name(): String = "输入框工具栏"

    override fun onFeatureInit(context: FeatureContext) {
        // The chat entrance is always available when this feature is installed.
    }

    override fun onFeatureInstall(context: FeatureContext) {
        runtime = InputToolbarRuntime(context.hostContext(), context.hostClassLoader(), ::logError)
        runtime?.install()
        subscribe(Events.DexReady::class.java) { runtime?.install() }
    }

    override fun onFeatureDestroy(context: FeatureContext) {
        runtime?.destroy()
        runtime = null
    }

    companion object {
        const val ID = "input_toolbar"
    }
}

private class InputToolbarRuntime(
    private val hostContext: Context,
    private val classLoader: ClassLoader,
    private val logger: (String, Throwable?) -> Unit
) {
    private val mainHandler = Handler(Looper.getMainLooper())
    private val preferences = InputToolbarSettings.preferences(hostContext)
    private val attachedFooters = Collections.synchronizedMap(WeakHashMap<Any, Boolean>())
    private val installedTags = Collections.synchronizedMap(WeakHashMap<View, Boolean>())
    private val preferenceListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (key == InputToolbarSettings.KEY_ENABLE) refreshFooters()
    }

    @Volatile
    private var hookInstalled = false

    init {
        preferences.registerOnSharedPreferenceChangeListener(preferenceListener)
    }

    @Synchronized
    fun install(): Boolean {
        if (hookInstalled) return true
        val footerClass = KavaReflector.loadClass(CHAT_FOOTER_CLASS, classLoader) ?: run {
            logger("未找到 8077 ChatFooter", null)
            return false
        }
        val attachMethod = KavaReflector.declaredMethods(footerClass).singleOrNull {
            it.name == "onAttachedToWindow" &&
                it.returnType == Void.TYPE &&
                it.parameterTypes.isEmpty()
        }
        val targetConstructor = KavaReflector.declaredConstructors(footerClass).firstOrNull {
            it.parameterTypes.size == 3 &&
                it.parameterTypes[0] == Context::class.java &&
                it.parameterTypes[1].name == "android.util.AttributeSet" &&
                it.parameterTypes[2] == Int::class.javaPrimitiveType
        }
        if (attachMethod == null && targetConstructor == null) {
            logger("未找到 8077 ChatFooter 生命周期入口", null)
            return false
        }
        return runCatching {
            targetConstructor?.let { constructor ->
                HookRegistry.get().hook(constructor, object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        bindFooter(param.thisObject)
                    }
                })
            }
            attachMethod?.let { method ->
                HookRegistry.get().hook(method, object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        bindFooter(param.thisObject)
                    }
                })
            }
            hookInstalled = true
            true
        }.getOrElse {
            logger("输入框工具栏 Hook 安装失败", it)
            false
        }
    }

    fun destroy() {
        val views = synchronized(installedTags) { installedTags.keys.toList().also { installedTags.clear() } }
        views.forEach { view ->
            (view.parent as? ViewGroup)?.removeView(view)
        }
        synchronized(attachedFooters) { attachedFooters.clear() }
        preferences.unregisterOnSharedPreferenceChangeListener(preferenceListener)
    }

    private fun bindFooter(footer: Any?) {
        val view = footer as? ViewGroup ?: return
        if (!attachedFooters.putIfAbsent(footer, true).let { it == null }) return
        val task = Runnable {
            if (!view.isAttachedToWindow) return@Runnable
            if (!InputToolbarSettings.enabled(hostContext)) return@Runnable
            addToolbar(view)
        }
        if (Looper.myLooper() == Looper.getMainLooper()) task.run() else mainHandler.post(task)
    }

    private fun addToolbar(footer: ViewGroup) {
        if (!InputToolbarSettings.enabled(hostContext)) return
        if (footer.findViewWithTag<View>(TOOLBAR_TAG) != null) return

        val density = footer.resources.displayMetrics.density
        val toolbar = LinearLayout(footer.context).apply {
            tag = TOOLBAR_TAG
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(2, density), 0, dp(2, density), 0)
            background = roundedBackground(footer.context)
            elevation = dp(2, density).toFloat()
        }
        toolbar.addView(makeAction(footer.context, "骰", "发送指定骰子", GameKind.DICE, density))
        toolbar.addView(makeAction(footer.context, "拳", "发送指定猜拳", GameKind.RPS, density))

        val params = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            dp(36, density),
            Gravity.END or Gravity.CENTER_VERTICAL
        ).apply {
            marginEnd = dp(76, density)
        }
        if (footer !is FrameLayout) return
        footer.addView(toolbar, params)
        installedTags[toolbar] = true
    }

    private fun refreshFooters() {
        val footers = synchronized(attachedFooters) { attachedFooters.keys.toList() }
        mainHandler.post {
            footers.forEach { footer ->
                val view = footer as? ViewGroup ?: return@forEach
                val existing = view.findViewWithTag<View>(TOOLBAR_TAG)
                if (InputToolbarSettings.enabled(hostContext)) {
                    if (existing == null) addToolbar(view)
                } else if (existing != null) {
                    (existing.parent as? ViewGroup)?.removeView(existing)
                    installedTags.remove(existing)
                }
            }
        }
    }

    private fun makeAction(
        context: Context,
        label: String,
        description: String,
        kind: GameKind,
        density: Float
    ): TextView = TextView(context).apply {
        text = label
        textSize = 14f
        gravity = Gravity.CENTER
        setTextColor(Color.DKGRAY)
        isClickable = true
        isFocusable = true
        contentDescription = description
        setPadding(dp(8, density), 0, dp(8, density), 0)
        setOnClickListener { showChoices(it, kind) }
    }

    private fun showChoices(source: View, kind: GameKind) {
        val activity = activityFromContext(source.context) ?: return
        if (activity.isFinishing || activity.isDestroyed) return
        val choices = if (kind == GameKind.DICE) {
            DICE_RESULTS.map { it.label to "" }
        } else {
            RPS_RESULTS.map { it.label to "" }
        }
        VoiceForwardMiuixDialog.showChoices(
            activity = activity,
            title = if (kind == GameKind.DICE) "选择骰子点数" else "选择猜拳结果",
            summary = "从输入框工具栏发送",
            choices = choices,
            onSelected = { index ->
                val result = if (kind == GameKind.DICE) DICE_RESULTS.getOrNull(index) else RPS_RESULTS.getOrNull(index)
                val talker = WeChatApis.chatPage()?.currentTalker().orEmpty()
                if (result == null || talker.isBlank() || WeChatApis.media()?.sendEmoji(talker, result.md5) != true) {
                    Toast.makeText(activity, "游戏表情发送失败", Toast.LENGTH_SHORT).show()
                }
            },
            onDismiss = {}
        )
    }

    private fun activityFromContext(context: Context?): Activity? {
        var current = context
        repeat(8) {
            when (current) {
                is Activity -> return current
                is android.content.ContextWrapper -> current = current.baseContext
                else -> return null
            }
        }
        return null
    }

    private fun roundedBackground(context: Context) = GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        cornerRadius = dp(18, context.resources.displayMetrics.density).toFloat()
        setColor(0xFFF0F0F0.toInt())
    }

    private fun dp(value: Int, density: Float): Int = (value * density + 0.5f).toInt()

    private enum class GameKind { DICE, RPS }

    private data class Result(val label: String, val md5: String)

    companion object {
        private const val CHAT_FOOTER_CLASS = "com.tencent.mm.pluginsdk.ui.chat.ChatFooter"
        private const val TOOLBAR_TAG = "Hchat:InputToolbar"
        private val RPS_RESULTS = listOf(
            Result("剪刀", "514914788fc461e7205bf0b6ba496c49"),
            Result("石头", "f790e342a02e0f99d34b316547f9aeab"),
            Result("布", "091577322c40c05aa3dd701da29d6423")
        )
        private val DICE_RESULTS = (1..6).mapIndexed { index, value ->
            Result("$value 点", listOf(
                "da1c289d4e363f3ce1ff36538903b92f",
                "9e3f303561566dc9342a3ea41e6552a6",
                "dbcc51db2765c1d0106290bae6326fc4",
                "9a21c57defc4974ab5b7c842e3232671",
                "3a8e16d650f7e66ba5516b2780512830",
                "5ba8e9694b853df10b9f2a77b312cc09"
            )[index])
        }
    }
}
