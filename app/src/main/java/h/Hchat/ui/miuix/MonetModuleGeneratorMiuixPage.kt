package h.Hchat.ui.miuix

import android.app.Activity
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import h.Hchat.hooks.items.monetgenerator.MonetModuleDocumentBridge
import h.Hchat.hooks.items.monetgenerator.MonetModuleGeneratorRuntime
import h.Hchat.hooks.items.monetgenerator.MonetModuleGeneratorSettings
import h.Hchat.hooks.items.monetgenerator.engine.MonetBubbleStyle
import h.Hchat.hooks.items.monetgenerator.engine.MonetGenerationEvent
import h.Hchat.hooks.items.monetgenerator.engine.MonetGenerationStage
import h.Hchat.hooks.items.monetgenerator.engine.MonetTabStyle
import h.Hchat.hooks.items.monetgenerator.engine.MonetUserScope
import h.Hchat.ui.FeatureSettingsProvider
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
internal fun MonetModuleGeneratorMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val preferences = remember { MonetModuleGeneratorSettings.preferences(context) }
    var bubbleStyle by remember {
        mutableStateOf(
            enumValue(
                preferences.getString(
                    MonetModuleGeneratorSettings.KEY_BUBBLE_STYLE,
                    MonetModuleGeneratorSettings.DEFAULT_BUBBLE_STYLE.name
                ),
                MonetModuleGeneratorSettings.DEFAULT_BUBBLE_STYLE
            )
        )
    }
    var multiSceneCorners by remember {
        mutableStateOf(
            preferences.getBoolean(
                MonetModuleGeneratorSettings.KEY_MULTI_SCENE_CORNERS,
                MonetModuleGeneratorSettings.DEFAULT_MULTI_SCENE_CORNERS
            )
        )
    }
    var tabStyle by remember {
        mutableStateOf(
            enumValue(
                preferences.getString(
                    MonetModuleGeneratorSettings.KEY_TAB_STYLE,
                    MonetModuleGeneratorSettings.DEFAULT_TAB_STYLE.name
                ),
                MonetModuleGeneratorSettings.DEFAULT_TAB_STYLE
            )
        )
    }
    var userScope by remember {
        mutableStateOf(
            enumValue(
                preferences.getString(
                    MonetModuleGeneratorSettings.KEY_USER_SCOPE,
                    MonetModuleGeneratorSettings.DEFAULT_USER_SCOPE.name
                ),
                MonetModuleGeneratorSettings.DEFAULT_USER_SCOPE
            )
        )
    }
    var generationState by remember { mutableStateOf<MonetGeneratorPageState>(MonetGeneratorPageState.Idle) }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    val isRunning = generationState is MonetGeneratorPageState.Running || MonetModuleGeneratorRuntime.isRunning()

    fun generate() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            Toast.makeText(context, "需要 Android 12 或更高版本", Toast.LENGTH_SHORT).show()
            return
        }
        if (MonetModuleGeneratorRuntime.isRunning()) {
            Toast.makeText(context, "已有莫奈模块正在生成", Toast.LENGTH_SHORT).show()
            return
        }
        val activity = context as? Activity
        if (activity == null) {
            Toast.makeText(context, "当前页面无法打开系统文件选择器", Toast.LENGTH_SHORT).show()
            return
        }
        MonetModuleDocumentBridge.launch(
            activity,
            MonetModuleGeneratorRuntime.suggestedFileName()
        ) { target ->
            if (target == null) return@launch
            val initial = MonetGenerationEvent.Progress(
                MonetGenerationStage.LOADING_APKS,
                "准备读取当前微信 APK",
                0,
                1
            )
            generationState = MonetGeneratorPageState.Running(initial)
            val started = MonetModuleGeneratorRuntime.generate(
                output = target,
                options = MonetModuleGeneratorSettings.options(context),
                onEvent = { event ->
                    if (event is MonetGenerationEvent.Progress) {
                        generationState = MonetGeneratorPageState.Running(event)
                    }
                },
                onComplete = { result ->
                    generationState = result.fold(
                        onSuccess = MonetGeneratorPageState::Done,
                        onFailure = {
                            MonetGeneratorPageState.Failed(
                                (generationState as? MonetGeneratorPageState.Running)?.progress?.stage,
                                it.message ?: it.toString()
                            )
                        }
                    )
                }
            )
            if (!started && MonetModuleGeneratorRuntime.isRunning()) {
                generationState = MonetGeneratorPageState.Failed(null, "已有莫奈模块正在生成")
            }
        }
    }

    PageScaffold(
        title = provider.title(),
        largeTitle = provider.title(),
        scrollBehavior = scrollBehavior,
        onBack = onBack,
        bottomBar = {
            BottomActionBar(
                primaryText = if (isRunning) "正在生成" else "生成模块",
                onPrimaryClick = { if (isRunning) {
                    Toast.makeText(context, "请等待当前生成任务完成", Toast.LENGTH_SHORT).show()
                } else {
                    generate()
                } },
                secondaryText = "返回",
                onSecondaryClick = onBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(text = "生成选项") }
            item {
                SettingsCard {
                    PopupChoiceRow(
                        title = "气泡样式",
                        summary = bubbleStyleLabel(bubbleStyle),
                        options = listOf(
                            PopupChoice("现代圆角", MonetBubbleStyle.MODERN.name),
                            PopupChoice("经典微信", MonetBubbleStyle.CLASSIC.name),
                            PopupChoice("Pro 圆角", MonetBubbleStyle.PRO.name)
                        ),
                        currentValue = bubbleStyle.name,
                        enabled = !isRunning,
                        onValueChanged = { value ->
                            bubbleStyle = enumValue(value, MonetModuleGeneratorSettings.DEFAULT_BUBBLE_STYLE)
                            preferences.edit().putString(
                                MonetModuleGeneratorSettings.KEY_BUBBLE_STYLE,
                                bubbleStyle.name
                            ).apply()
                        }
                    )
                    InsetDivider()
                    SwitchRow(
                        checked = multiSceneCorners,
                        title = "多场景圆角",
                        summary = "应用到输入栏、引用区域和支付键盘等界面",
                        enabled = !isRunning,
                        onCheckedChange = {
                            multiSceneCorners = it
                            preferences.edit().putBoolean(
                                MonetModuleGeneratorSettings.KEY_MULTI_SCENE_CORNERS,
                                it
                            ).apply()
                        }
                    )
                    InsetDivider()
                    PopupChoiceRow(
                        title = "底栏样式",
                        summary = if (tabStyle == MonetTabStyle.SOLID) "纯色莫奈" else "半透明莫奈",
                        options = listOf(
                            PopupChoice("纯色莫奈", MonetTabStyle.SOLID.name),
                            PopupChoice("半透明莫奈", MonetTabStyle.BLUR.name)
                        ),
                        currentValue = tabStyle.name,
                        enabled = !isRunning,
                        onValueChanged = { value ->
                            tabStyle = enumValue(value, MonetModuleGeneratorSettings.DEFAULT_TAB_STYLE)
                            preferences.edit().putString(
                                MonetModuleGeneratorSettings.KEY_TAB_STYLE,
                                tabStyle.name
                            ).apply()
                        }
                    )
                    InsetDivider()
                    PopupChoiceRow(
                        title = "Android 用户范围",
                        summary = if (userScope == MonetUserScope.CURRENT) "仅当前用户" else "全部用户",
                        options = listOf(
                            PopupChoice("仅当前用户", MonetUserScope.CURRENT.name),
                            PopupChoice("全部用户", MonetUserScope.ALL.name)
                        ),
                        currentValue = userScope.name,
                        enabled = !isRunning,
                        onValueChanged = { value ->
                            userScope = enumValue(value, MonetModuleGeneratorSettings.DEFAULT_USER_SCOPE)
                            preferences.edit().putString(
                                MonetModuleGeneratorSettings.KEY_USER_SCOPE,
                                userScope.name
                            ).apply()
                        }
                    )
                }
            }

            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "生成状态") }
            item {
                SettingsCard {
                    MonetGenerationStatus(generationState)
                }
            }

            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "安装说明") }
            item {
                SettingsCard {
                    MonetInfoRow(
                        title = "Android 12 及以上",
                        summary = "生成器会读取当前微信 base/split APK，结果只适用于当前微信版本；微信升级后需要重新生成。"
                    )
                    InsetDivider()
                    MonetInfoRow(
                        title = "通过 Root 管理器安装",
                        summary = "生成的 ZIP 可交给 Magisk、KernelSU、APatch 或兼容管理器安装。生成成功不代表已完成设备安装验证。"
                    )
                    if (userScope == MonetUserScope.ALL) {
                        InsetDivider()
                        MonetInfoRow(
                            title = "全部用户范围",
                            summary = "安装脚本会尝试为设备上的全部 Android 用户启用 Overlay，并强制停止对应用户的微信。"
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MonetGenerationStatus(state: MonetGeneratorPageState) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp)) {
        when (state) {
            MonetGeneratorPageState.Idle -> {
                Text("尚未生成", color = MiuixTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
                Text(
                    "点击底部“生成模块”，选择 ZIP 保存位置后开始。",
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    fontSize = 12.sp
                )
            }

            is MonetGeneratorPageState.Running -> {
                Text(
                    stageLabel(state.progress.stage),
                    color = MiuixTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    state.progress.detail,
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    fontSize = 12.sp
                )
                Spacer(Modifier.height(10.dp))
                val completed = state.progress.completed
                val total = state.progress.total
                if (completed != null && total != null) {
                    LinearProgressIndicator(
                        progress = { completed.toFloat() / total.coerceAtLeast(1) },
                        modifier = Modifier.fillMaxWidth(),
                        color = MiuixTheme.colorScheme.primary,
                        trackColor = MiuixTheme.colorScheme.secondaryVariant
                    )
                    Text(
                        "$completed/$total",
                        modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                        color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                        fontSize = 12.sp
                    )
                } else {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        color = MiuixTheme.colorScheme.primary,
                        trackColor = MiuixTheme.colorScheme.secondaryVariant
                    )
                }
            }

            is MonetGeneratorPageState.Done -> {
                Text("生成完成", color = MiuixTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
                Text(
                    "已写入 ${state.result.displayName}，覆盖 ${state.result.resourceCount} 项颜色资源，生成 ${state.result.overlayCount} 个 Overlay APK。",
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    fontSize = 12.sp
                )
            }

            is MonetGeneratorPageState.Failed -> {
                Text("生成失败", color = MiuixTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
                Text(
                    buildString {
                        state.stage?.let { append(stageLabel(it)).append("：") }
                        append(state.message)
                    },
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun MonetInfoRow(title: String, summary: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp)) {
        Text(title, color = MiuixTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
        Text(summary, color = MiuixTheme.colorScheme.onSurfaceVariantSummary, fontSize = 12.sp)
    }
}

private fun bubbleStyleLabel(value: MonetBubbleStyle): String = when (value) {
    MonetBubbleStyle.MODERN -> "现代圆角"
    MonetBubbleStyle.CLASSIC -> "经典微信"
    MonetBubbleStyle.PRO -> "Pro 圆角"
}

private fun stageLabel(value: MonetGenerationStage): String = when (value) {
    MonetGenerationStage.LOADING_APKS -> "读取微信 APK"
    MonetGenerationStage.BUILDING_RESOURCE_GRAPH -> "构建资源图"
    MonetGenerationStage.RESOLVING_ROLES -> "解析资源语义"
    MonetGenerationStage.BUILDING_OVERLAY -> "构建 Overlay"
    MonetGenerationStage.SIGNING -> "签名 Overlay"
    MonetGenerationStage.PACKAGING -> "打包 Root 模块"
}

private inline fun <reified T : Enum<T>> enumValue(value: String?, fallback: T): T {
    return value?.let { runCatching { enumValueOf<T>(it) }.getOrNull() } ?: fallback
}

private sealed interface MonetGeneratorPageState {
    data object Idle : MonetGeneratorPageState
    data class Running(val progress: MonetGenerationEvent.Progress) : MonetGeneratorPageState
    data class Done(val result: MonetModuleGeneratorRuntime.ExportResult) : MonetGeneratorPageState
    data class Failed(val stage: MonetGenerationStage?, val message: String) : MonetGeneratorPageState
}
