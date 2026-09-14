package h.Hchat.hooks.items.chattoolbar

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.FrameLayout
import android.widget.GridView
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import de.robv.android.xposed.XC_MethodHook
import h.Hchat.dexkit.DexMethodCache
import h.Hchat.hooks.api.core.WeChatApis
import h.Hchat.hooks.core.FeatureContext
import h.Hchat.hooks.core.HookRegistry
import h.Hchat.utils.KavaReflector
import org.luckypray.dexkit.query.FindMethod
import org.luckypray.dexkit.query.matchers.MethodMatcher
import java.lang.ref.WeakReference
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.util.Collections
import java.util.WeakHashMap
import java.util.concurrent.ConcurrentHashMap

class ChatToolbarRuntime(
    private val featureContext: FeatureContext,
    private val logger: (String, Throwable?) -> Unit
) {
    private val mainHandler = Handler(Looper.getMainLooper())
    private val preferences = ChatToolbarSettings.preferences(featureContext.hostContext())
    private val methodCache = DexMethodCache.prefs(
        featureContext.hostContext(),
        "Hchat_chat_toolbar_method_cache"
    )
    private val hookedConstructors = Collections.newSetFromMap(
        ConcurrentHashMap<java.lang.reflect.Constructor<*>, Boolean>()
    )
    private val bindings = Collections.synchronizedMap(WeakHashMap<ViewGroup, ToolbarBinding>())
    private val panelStates = Collections.synchronizedMap(WeakHashMap<View, PanelState>())
    private val loggedFailures = Collections.newSetFromMap(ConcurrentHashMap<String, Boolean>())

    @Volatile
    private var active = true

    @Volatile
    private var baseHooksInstalled = false

    @Volatile
    private var gridHookInstalled = false

    @Volatile
    private var initAppGridMethod: Method? = null

    private val preferenceListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (key == ChatToolbarSettings.KEY_ENABLE ||
            key == ChatToolbarSettings.KEY_DISPLAY_MODE ||
            key == ChatToolbarSettings.KEY_ITEMS ||
            key == ChatToolbarSettings.KEY_QUICK_REPLIES
        ) {
            runOnMain(::refreshBindings)
        }
    }

    @Synchronized
    fun installBaseHooks(): Boolean {
        if (baseHooksInstalled) return true
        val footerClass = KavaReflector.loadClass(CHAT_FOOTER_CLASS, featureContext.hostClassLoader())
            ?: run {
                logOnce("footer_class", "未找到微信聊天输入区类 $CHAT_FOOTER_CLASS")
                return false
            }
        val constructors = KavaReflector.declaredConstructors(footerClass)
        if (constructors.isEmpty()) {
            logOnce("footer_constructor", "未找到微信聊天输入区构造方法")
            return false
        }
        var installed = 0
        constructors.forEach { constructor ->
            if (!hookedConstructors.add(constructor)) {
                installed++
                return@forEach
            }
            val ok = runCatching {
                HookRegistry.get().hook(constructor, object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        (param.thisObject as? ViewGroup)?.let(::attachFooter)
                    }
                })
                true
            }.getOrElse {
                hookedConstructors.remove(constructor)
                logger("聊天工具栏构造 Hook 安装失败: ${constructor.toGenericString()}", it)
                false
            }
            if (ok) installed++
        }
        if (installed == 0) return false
        preferences.registerOnSharedPreferenceChangeListener(preferenceListener)
        baseHooksInstalled = true
        return true
    }

    @Synchronized
    fun installDexHooks(): Boolean {
        if (!active) return false
        val baseReady = installBaseHooks()
        if (gridHookInstalled) return baseReady
        val initMethod = locateInitAppGridMethod() ?: return false
        val measureMethod = locatePanelMeasureMethod() ?: return false
        val installed = runCatching {
            HookRegistry.get().hook(initMethod, object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    panelFromHook(param)?.let { panel ->
                        measureMethod?.let { preparePanelMeasure(panel, it) }
                    }
                }

                override fun afterHookedMethod(param: MethodHookParam) {
                    panelFromHook(param)?.let(::scheduleSnapshot)
                }
            })
            initAppGridMethod = initMethod
            gridHookInstalled = true
            true
        }.getOrElse {
            logger("微信工具面板 Hook 安装失败: ${initMethod.toGenericString()}", it)
            false
        }
        if (installed) runOnMain(::initializeKnownPanels)
        return baseReady && installed
    }

    fun destroy() {
        active = false
        if (baseHooksInstalled) {
            runCatching { preferences.unregisterOnSharedPreferenceChangeListener(preferenceListener) }
        }
        mainHandler.removeCallbacksAndMessages(null)
        runOnMain {
            synchronized(bindings) {
                bindings.values.toList().forEach { binding ->
                    runCatching { (binding.root.parent as? ViewGroup)?.removeView(binding.root) }
                }
                bindings.clear()
            }
            synchronized(panelStates) { panelStates.clear() }
        }
    }

    private fun attachFooter(footer: ViewGroup) {
        if (!active) return
        runOnMain {
            if (!active || bindings.containsKey(footer)) return@runOnMain
            val activity = footer.context.findActivity() ?: run {
                logOnce("footer_activity", "聊天输入区 Context 不是 Activity，无法挂载工具栏")
                return@runOnMain
            }
            val host = findToolbarHost(footer) ?: run {
                logOnce("toolbar_host", "未找到聊天输入框工具栏挂载容器")
                return@runOnMain
            }
            val old = host.findViewWithTag<View>(TOOLBAR_TAG)
            if (old != null) return@runOnMain
            val panel = findViewByClassName(footer, APP_PANEL_CLASS)
            val row = LinearLayout(activity).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
                setPadding(dp(activity, 8), dp(activity, 4), dp(activity, 8), dp(activity, 4))
            }
            val root = HorizontalScrollView(activity).apply {
                tag = TOOLBAR_TAG
                isHorizontalScrollBarEnabled = false
                overScrollMode = View.OVER_SCROLL_NEVER
                isFillViewport = false
                addView(
                    row,
                    FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    )
                )
            }
            val binding = ToolbarBinding(
                activity = WeakReference(activity),
                footer = WeakReference(footer),
                panel = WeakReference<View>(panel),
                root = root,
                row = row
            )
            bindings[footer] = binding
            host.addView(
                root,
                0,
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            )
            root.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
                override fun onViewAttachedToWindow(view: View) {
                    if (active) bindings[footer] = binding
                }

                override fun onViewDetachedFromWindow(view: View) {
                    bindings.remove(footer)
                }
            })
            rebuild(binding)
            panel?.let {
                panelState(it)
                scheduleGridWatchdog(it)
            }
        }
    }

    private fun findToolbarHost(footer: ViewGroup): LinearLayout? {
        (childAtPath(footer, 0, 1) as? LinearLayout)?.let { return it }
        val candidates = ArrayList<LinearLayout>()
        collectViews(footer) { view ->
            if (view is LinearLayout && findViewByClassName(view, APP_PANEL_CLASS) == null) {
                candidates += view
            }
        }
        return candidates.map { candidate ->
            var score = 0
            collectViews(candidate) { view ->
                val name = view.javaClass.name
                if (view is android.widget.EditText || name.contains("EditText", ignoreCase = true)) score += 5
                if (view is android.widget.Button) score += 2
            }
            candidate to score
        }.maxByOrNull { it.second }
            ?.takeIf { (_, score) -> score > 0 }
            ?.first
            ?.takeIf { candidate -> candidate !== footer && candidate.parent != null }
    }

    private fun initializeKnownPanels() {
        synchronized(bindings) {
            bindings.values.toList().forEach { binding ->
                binding.panel.get()?.let { panel ->
                    invokeInitAppGrid(panel)
                    scheduleSnapshot(panel)
                }
            }
        }
    }

    private fun scheduleGridWatchdog(panel: View) {
        mainHandler.postDelayed({
            if (!active || panelState(panel).tools.isNotEmpty()) return@postDelayed
            val gridContainer = childAtPath(panel, 0, 0, 0) as? ViewGroup
            if (gridContainer != null && gridContainer.childCount > 0) {
                scheduleSnapshot(panel)
                return@postDelayed
            }
            invokeInitAppGrid(panel)
            scheduleSnapshot(panel, 250L)
        }, GRID_INIT_WATCHDOG_DELAY_MS)
    }

    private fun invokeInitAppGrid(panel: View) {
        val method = initAppGridMethod ?: return
        runCatching {
            if (Modifier.isStatic(method.modifiers)) {
                KavaReflector.invokeOrThrow(method, null, panel)
            } else {
                KavaReflector.invokeOrThrow(method, panel)
            }
        }.onFailure { logOnce("invoke_init_app_grid", "主动初始化微信工具面板失败", it) }
    }

    private fun scheduleSnapshot(panel: View, delayMs: Long = 0L) {
        if (!active) return
        val action = Runnable { snapshotTools(panel) }
        if (delayMs > 0L) mainHandler.postDelayed(action, delayMs) else panel.post(action)
    }

    private fun snapshotTools(panel: View) {
        if (!active) return
        val state = panelState(panel)
        val elapsed = SystemClock.uptimeMillis() - state.lastSnapshotAt
        if (state.lastSnapshotAt > 0L && elapsed < SNAPSHOT_DEBOUNCE_MS) {
            if (!state.refreshScheduled) {
                state.refreshScheduled = true
                mainHandler.postDelayed({
                    state.refreshScheduled = false
                    snapshotToolsNow(panel)
                }, SNAPSHOT_DEBOUNCE_MS - elapsed)
            }
            return
        }
        snapshotToolsNow(panel)
    }

    private fun snapshotToolsNow(panel: View) {
        val grids = ArrayList<GridView>()
        collectViews(panel) { view -> if (view is GridView) grids += view }
        if (grids.isEmpty()) return
        val result = ArrayList<NativeTool>()
        grids.forEach { grid ->
            val adapter = grid.adapter ?: return@forEach
            val click = grid.onItemClickListener ?: return@forEach
            val longClick = grid.onItemLongClickListener
            for (position in 0 until adapter.count) {
                val itemView = runCatching {
                    val visibleIndex = position - grid.firstVisiblePosition
                    grid.getChildAt(visibleIndex) ?: adapter.getView(position, null, grid)
                }.getOrNull() ?: continue
                val spec = resolveTool(itemView) ?: continue
                val icon = findFirstImageView(itemView)?.drawable?.copyFor(itemView.context)
                result += NativeTool(
                    id = spec.id,
                    title = spec.title,
                    icon = icon,
                    grid = WeakReference(grid),
                    position = position,
                    clickListener = click,
                    longClickListener = longClick
                )
            }
        }
        if (result.isEmpty()) return
        val state = panelState(panel)
        state.tools = result.distinctBy { it.id }
        state.lastSnapshotAt = SystemClock.uptimeMillis()
        refreshBindingsFor(panel)
    }

    private fun resolveTool(itemView: View): ChatToolbarSettings.Tool? {
        val drawable = findFirstImageView(itemView)?.drawable
        resourceNameOf(drawable, itemView)?.let(ChatToolbarSettings::toolForResourceName)?.let { return it }
        return ChatToolbarSettings.toolForHostTitle(findFirstText(itemView))
    }

    private fun resourceNameOf(drawable: Drawable?, itemView: View): String? {
        if (drawable == null) return null
        var type: Class<*>? = drawable.javaClass
        while (type != null && type != Any::class.java) {
            KavaReflector.declaredFields(type).forEach { field ->
                if (field.type != Integer.TYPE || Modifier.isStatic(field.modifiers)) return@forEach
                val value = (KavaReflector.readField(field, drawable) as? Number)?.toInt() ?: return@forEach
                val name = runCatching { itemView.resources.getResourceEntryName(value) }.getOrNull()
                if (ChatToolbarSettings.toolForResourceName(name) != null) return name
            }
            type = type.superclass
        }
        return null
    }

    private fun refreshBindingsFor(panel: View) {
        runOnMain {
            synchronized(bindings) {
                bindings.values.toList()
                    .filter { it.panel.get() === panel }
                    .forEach(::rebuild)
            }
        }
    }

    private fun refreshBindings() {
        synchronized(bindings) {
            bindings.values.toList().forEach(::rebuild)
        }
    }

    private fun rebuild(binding: ToolbarBinding) {
        val activity = binding.activity.get() ?: return
        if (!preferences.getBoolean(ChatToolbarSettings.KEY_ENABLE, ChatToolbarSettings.DEFAULT_ENABLE)) {
            binding.root.visibility = View.GONE
            return
        }
        val configs = ChatToolbarSettings.loadItems(preferences)
        val nativeTools = binding.panel.get()?.let { panelState(it).tools }.orEmpty()
        val nativeById = nativeTools.associateBy { it.id }
        val album = nativeById["album"]
        val row = binding.row
        row.removeAllViews()
        configs.filter { it.enabled }.forEach { config ->
            val spec = ChatToolbarSettings.tool(config.id) ?: return@forEach
            val action: (() -> Unit)? = when (config.id) {
                ChatToolbarSettings.QUICK_REPLY_ID -> ({ showQuickReplies(binding) })
                ChatToolbarSettings.SYSTEM_CAMERA_ID -> album?.longClickListener?.let {
                    { album.invokeLongClick() }
                }
                else -> nativeById[config.id]?.let { tool -> ({ tool.invokeClick() }) }
            }
            if (action != null) {
                row.addView(createToolButton(activity, spec.title, nativeById[config.id]?.icon ?: album?.icon, action))
            }
        }
        binding.root.visibility = if (row.childCount > 0) View.VISIBLE else View.GONE
    }

    private fun createToolButton(
        context: Context,
        title: String,
        icon: Drawable?,
        action: () -> Unit
    ): View {
        val mode = preferences.getString(
            ChatToolbarSettings.KEY_DISPLAY_MODE,
            ChatToolbarSettings.DEFAULT_DISPLAY_MODE
        ) ?: ChatToolbarSettings.DEFAULT_DISPLAY_MODE
        val dark = (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) ==
            Configuration.UI_MODE_NIGHT_YES
        val foreground = if (dark) Color.WHITE else Color.rgb(35, 35, 35)
        val background = if (dark) 0x22FFFFFF else 0x11000000
        val content = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            minimumHeight = dp(context, 40)
            isClickable = true
            isFocusable = true
            setPadding(dp(context, 12), dp(context, 7), dp(context, 12), dp(context, 7))
            this.background = RippleDrawable(
                android.content.res.ColorStateList.valueOf(if (dark) 0x33FFFFFF else 0x22000000),
                ColorDrawable(background),
                null
            )
            setOnClickListener { action() }
        }
        if (mode != ChatToolbarSettings.DISPLAY_TEXT) {
            content.addView(ImageView(context).apply {
                setImageDrawable(icon?.copyFor(context) ?: context.getDrawable(android.R.drawable.ic_menu_send))
                imageTintList = null
                contentDescription = title
            }, LinearLayout.LayoutParams(dp(context, 22), dp(context, 22)))
        }
        if (mode != ChatToolbarSettings.DISPLAY_ICON) {
            content.addView(TextView(context).apply {
                text = title
                textSize = 13f
                setTextColor(foreground)
                gravity = Gravity.CENTER
                if (mode == ChatToolbarSettings.DISPLAY_BOTH) setPadding(dp(context, 6), 0, 0, 0)
            })
        }
        content.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply { marginEnd = dp(context, 8) }
        return content
    }

    private fun showQuickReplies(binding: ToolbarBinding) {
        val activity = binding.activity.get() ?: return
        val footer = binding.footer.get() ?: return
        if (!footer.isAttachedToWindow || WeChatApis.currentActivity()?.currentActivity() !== activity) return
        val replies = ChatToolbarSettings.loadQuickReplies(preferences)
        if (replies.isEmpty()) {
            Toast.makeText(activity, "请先在增强 → 聊天工具栏中添加快捷回复", Toast.LENGTH_SHORT).show()
            return
        }
        AlertDialog.Builder(activity)
            .setTitle("快捷回复")
            .setItems(replies.toTypedArray()) { _, which ->
                val talker = WeChatApis.chatPage()?.currentTalker().orEmpty()
                val text = replies.getOrNull(which).orEmpty()
                val sent = talker.isNotBlank() && text.isNotBlank() &&
                    WeChatApis.message().sender()?.sendText(talker, text) == true
                if (!sent) Toast.makeText(activity, "快捷回复发送失败", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun preparePanelMeasure(panel: View, method: Method) {
        val metrics = panel.resources.displayMetrics
        val fallbackDp = if (metrics.widthPixels < metrics.heightPixels) 215 else 158
        val containerHeight = childAtPath(panel, 0, 0)?.layoutParams?.height?.takeIf { it > 0 }
            ?: dp(panel.context, fallbackDp)
        val height = (containerHeight - dp(panel.context, 22)).coerceAtLeast(1)
        val args = if (Modifier.isStatic(method.modifiers)) {
            arrayOf(panel, metrics.widthPixels, height)
        } else {
            arrayOf(metrics.widthPixels, height)
        }
        val receiver = if (Modifier.isStatic(method.modifiers)) {
            null
        } else if (method.declaringClass.isInstance(panel)) {
            panel
        } else {
            KavaReflector.newInstanceByArgs(method.declaringClass, arrayOf(panel))
                ?: KavaReflector.newInstanceByArgs(method.declaringClass, emptyArray<Any?>())
        }
        if (!Modifier.isStatic(method.modifiers) && receiver == null) return
        runCatching { KavaReflector.invokeOrThrow(method, receiver, *args) }
            .onFailure { logOnce("invoke_panel_measure", "准备微信工具面板测量失败", it) }
    }

    private fun locateInitAppGridMethod(): Method? {
        val runtimeKey = methodCacheKey()
        DexMethodCache.load(methodCache, runtimeKey, featureContext.hostClassLoader(), CACHE_INIT_APP_GRID)
            ?.takeIf(::isInitAppGridMethod)
            ?.let { return it }
        val candidates = runCatching {
            featureContext.dexKitBridge().findMethod(
                FindMethod().apply {
                    matcher(MethodMatcher().apply {
                        declaredClass(APP_PANEL_CLASS)
                        usingEqStrings("MicroMsg.AppPanel", "initAppGrid()")
                    })
                }
            ).mapNotNull { data ->
                runCatching { data.getMethodInstance(featureContext.hostClassLoader()) }.getOrNull()
            }.filter(::isInitAppGridMethod).distinctBy { it.toGenericString() }
        }.getOrElse {
            logger("定位微信工具面板初始化方法失败", it)
            emptyList()
        }
        val method = candidates.singleOrNull()
        if (method != null) {
            DexMethodCache.save(methodCache, runtimeKey, CACHE_INIT_APP_GRID, method)
        } else {
            DexMethodCache.clear(methodCache, runtimeKey, CACHE_INIT_APP_GRID)
            if (candidates.size > 1) logger("微信工具面板初始化方法候选不唯一", null)
        }
        return method
    }

    private fun locatePanelMeasureMethod(): Method? {
        val runtimeKey = methodCacheKey()
        DexMethodCache.load(methodCache, runtimeKey, featureContext.hostClassLoader(), CACHE_PANEL_MEASURE)
            ?.takeIf(::isPanelMeasureMethod)
            ?.let { return it }
        val candidates = runCatching {
            featureContext.dexKitBridge().findMethod(
                FindMethod().apply {
                    searchPackages(CHAT_PACKAGE)
                    matcher(MethodMatcher().apply {
                        usingEqStrings(
                            "MicroMsg.AppPanel",
                            "onMeasure width: %d, heigth:%d, isMeasured:%b, gridWidth:%d, gridHeight:%d"
                        )
                    })
                }
            ).mapNotNull { data ->
                runCatching { data.getMethodInstance(featureContext.hostClassLoader()) }.getOrNull()
            }.filter(::isPanelMeasureMethod).distinctBy { it.toGenericString() }
        }.getOrElse {
            logger("定位微信工具面板测量方法失败", it)
            emptyList()
        }
        val method = candidates.singleOrNull()
        if (method != null) {
            DexMethodCache.save(methodCache, runtimeKey, CACHE_PANEL_MEASURE, method)
        } else {
            DexMethodCache.clear(methodCache, runtimeKey, CACHE_PANEL_MEASURE)
            if (candidates.size > 1) logger("微信工具面板测量方法候选不唯一", null)
        }
        return method
    }

    private fun isInitAppGridMethod(method: Method): Boolean {
        if (method.declaringClass.name != APP_PANEL_CLASS || method.returnType != Void.TYPE ||
            Modifier.isAbstract(method.modifiers)
        ) return false
        val types = method.parameterTypes
        return if (Modifier.isStatic(method.modifiers)) {
            types.size == 1 && types[0].name == APP_PANEL_CLASS
        } else {
            types.isEmpty()
        }
    }

    private fun isPanelMeasureMethod(method: Method): Boolean {
        if (!method.declaringClass.name.startsWith(CHAT_PACKAGE) || method.returnType != Void.TYPE ||
            Modifier.isAbstract(method.modifiers)
        ) return false
        val types = method.parameterTypes
        return if (Modifier.isStatic(method.modifiers)) {
            types.size == 3 && types[0].name == APP_PANEL_CLASS &&
                types[1] == Integer.TYPE && types[2] == Integer.TYPE
        } else {
            types.size == 2 && types.all { it == Integer.TYPE }
        }
    }

    private fun panelFromHook(param: XC_MethodHook.MethodHookParam): View? {
        (param.thisObject as? View)?.takeIf { it.javaClass.name == APP_PANEL_CLASS }?.let { return it }
        return param.args?.firstOrNull { it is View && it.javaClass.name == APP_PANEL_CLASS } as? View
    }

    private fun methodCacheKey(): String =
        DexMethodCache.runtimeKey(featureContext.hostContext(), featureContext.hostClassLoader())
            .takeIf { it.isNotBlank() }
            ?.let { "$it|$CACHE_SCHEMA" }
            .orEmpty()

    private fun panelState(panel: View): PanelState = synchronized(panelStates) {
        panelStates.getOrPut(panel) { PanelState() }
    }

    fun onActivityDestroyed(activity: Activity) {
        runOnMain {
            synchronized(bindings) {
                val targets = bindings.entries
                    .filter { it.value.activity.get() === activity }
                    .map { it.key to it.value }
                targets.forEach { (footer, binding) ->
                    (binding.root.parent as? ViewGroup)?.removeView(binding.root)
                    bindings.remove(footer)
                    binding.panel.get()?.let { panelStates.remove(it) }
                }
            }
        }
    }

    private fun childAtPath(root: View, vararg indexes: Int): View? {
        var current = root
        indexes.forEach { index ->
            val group = current as? ViewGroup ?: return null
            if (index !in 0 until group.childCount) return null
            current = group.getChildAt(index) ?: return null
        }
        return current
    }

    private fun findViewByClassName(root: View, className: String): View? {
        if (root.javaClass.name == className) return root
        val group = root as? ViewGroup ?: return null
        for (index in 0 until group.childCount) {
            findViewByClassName(group.getChildAt(index), className)?.let { return it }
        }
        return null
    }

    private fun findFirstImageView(root: View): ImageView? {
        if (root is ImageView && root.drawable != null) return root
        val group = root as? ViewGroup ?: return null
        for (index in 0 until group.childCount) {
            findFirstImageView(group.getChildAt(index))?.let { return it }
        }
        return null
    }

    private fun findFirstText(root: View): String? {
        if (root is TextView) root.text?.toString()?.trim()?.takeIf { it.isNotEmpty() }?.let { return it }
        val group = root as? ViewGroup ?: return null
        for (index in 0 until group.childCount) {
            findFirstText(group.getChildAt(index))?.let { return it }
        }
        return null
    }

    private fun collectViews(root: View, action: (View) -> Unit) {
        action(root)
        val group = root as? ViewGroup ?: return
        for (index in 0 until group.childCount) collectViews(group.getChildAt(index), action)
    }

    private fun Context.findActivity(): Activity? {
        var current: Context? = this
        while (current is ContextWrapper) {
            if (current is Activity) return current
            current = current.baseContext
        }
        return current as? Activity
    }

    private fun Drawable.copyFor(context: Context): Drawable =
        constantState?.newDrawable(context.resources)?.mutate() ?: mutate()

    private fun dp(context: Context, value: Int): Int =
        (value * context.resources.displayMetrics.density + 0.5f).toInt()

    private fun runOnMain(action: () -> Unit) {
        if (Looper.myLooper() == Looper.getMainLooper()) action() else mainHandler.post(action)
    }

    private fun logOnce(key: String, message: String, throwable: Throwable? = null) {
        if (loggedFailures.add(key)) logger(message, throwable)
    }

    private data class ToolbarBinding(
        val activity: WeakReference<Activity>,
        val footer: WeakReference<ViewGroup>,
        val panel: WeakReference<View>,
        val root: HorizontalScrollView,
        val row: LinearLayout
    )

    private class PanelState {
        var tools: List<NativeTool> = emptyList()
        var lastSnapshotAt: Long = 0L
        var refreshScheduled: Boolean = false
    }

    private data class NativeTool(
        val id: String,
        val title: String,
        val icon: Drawable?,
        val grid: WeakReference<GridView>,
        val position: Int,
        val clickListener: AdapterView.OnItemClickListener,
        val longClickListener: AdapterView.OnItemLongClickListener?
    ) {
        fun invokeClick() {
            val grid = grid.get() ?: return
            val adapter = grid.adapter ?: return
            if (position !in 0 until adapter.count) return
            val visibleIndex = position - grid.firstVisiblePosition
            val itemView = grid.getChildAt(visibleIndex)
                ?: runCatching { adapter.getView(position, null, grid) }.getOrNull()
                ?: return
            clickListener.onItemClick(grid, itemView, position, adapter.getItemId(position))
        }

        fun invokeLongClick() {
            val grid = grid.get() ?: return
            val listener = longClickListener ?: return
            val adapter = grid.adapter ?: return
            if (position !in 0 until adapter.count) return
            val visibleIndex = position - grid.firstVisiblePosition
            val itemView = grid.getChildAt(visibleIndex)
                ?: runCatching { adapter.getView(position, null, grid) }.getOrNull()
                ?: return
            listener.onItemLongClick(grid, itemView, position, adapter.getItemId(position))
        }
    }

    private companion object {
        const val CHAT_FOOTER_CLASS = "com.tencent.mm.pluginsdk.ui.chat.ChatFooter"
        const val APP_PANEL_CLASS = "com.tencent.mm.pluginsdk.ui.chat.AppPanel"
        const val CHAT_PACKAGE = "com.tencent.mm.pluginsdk.ui.chat"
        const val TOOLBAR_TAG = "Hchat:ChatToolbar"
        const val CACHE_SCHEMA = "chat_toolbar_v1"
        const val CACHE_INIT_APP_GRID = "init_app_grid"
        const val CACHE_PANEL_MEASURE = "panel_measure"
        const val GRID_INIT_WATCHDOG_DELAY_MS = 1_500L
        const val SNAPSHOT_DEBOUNCE_MS = 2_000L
    }
}
