package h.Hchat.ui.miuix

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import h.Hchat.BuildConfig
import h.Hchat.hooks.api.core.WeChatApis
import h.Hchat.hooks.items.securemessage.SecureMessageSettings
import h.Hchat.hooks.items.chattime.ChatTimeStyleSettings
import h.Hchat.hooks.items.swipequote.SwipeQuoteSettings
import h.Hchat.hooks.items.transparentavatar.UploadTransparentAvatarSettings
import h.Hchat.preferences.HchatStorage
import h.Hchat.ui.FeatureSettingsProvider
import h.Hchat.update.HchatUpdateChecker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.SmallTitle

@Composable
internal fun SecureMessageMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val isAnti = provider.featureId() == SecureMessageSettings.ANTI_ID
    val prefsName = if (isAnti) SecureMessageSettings.ANTI_PREFS else SecureMessageSettings.SEND_PREFS
    val sp = remember { HchatStorage.preferences(context, prefsName) }
    val title = if (isAnti) "反安全消息" else "安全消息"
    val summary = if (isAnti) {
        "恢复安全消息的普通长按菜单和操作"
    } else {
        "给文字、链接、卡片、图片、视频和表情包添加安全标记"
    }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    PageScaffold(
        title = title,
        largeTitle = title,
        scrollBehavior = scrollBehavior,
        bottomBar = { BottomActionBar("返回", onBack) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(text = "消息安全") }
            item {
                SettingsCard {
                    SwitchRow(
                        sp,
                        SecureMessageSettings.KEY_ENABLE,
                        title,
                        summary,
                        if (isAnti) false else SecureMessageSettings.DEFAULT_ENABLE
                    )
                    InsetDivider()
                    InfoRow("适配范围", "微信 8.0.49–8.0.78；媒体消息按发送链路处理")
                }
            }
        }
    }
}

@Composable
internal fun UploadTransparentAvatarMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { UploadTransparentAvatarSettings.preferences(context) }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    PageScaffold(
        title = provider.title(),
        largeTitle = provider.title(),
        scrollBehavior = scrollBehavior,
        bottomBar = { BottomActionBar("返回", onBack) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(text = "个人头像") }
            item {
                SettingsCard {
                    SwitchRow(
                        sp,
                        UploadTransparentAvatarSettings.KEY_ENABLE,
                        "上传透明头像",
                        "上传头像时使用 PNG 格式保留透明背景",
                        UploadTransparentAvatarSettings.DEFAULT_ENABLE
                    )
                }
            }
        }
    }
}

@Composable
internal fun SwipeQuoteMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, SwipeQuoteSettings.PREFS_NAME) }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    PageScaffold(
        title = provider.title(),
        largeTitle = provider.title(),
        scrollBehavior = scrollBehavior,
        bottomBar = { BottomActionBar("返回", onBack) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(text = "引用") }
            item {
                SettingsCard {
                    SwitchRow(
                        sp,
                        SwipeQuoteSettings.KEY_ENABLE,
                        "左滑引用回复",
                        "左滑消息后调用微信原生引用入口",
                        SwipeQuoteSettings.DEFAULT_ENABLE
                    )
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "复读触发方式") }
            item {
                SettingsCard {
                    SwitchRow(
                        sp,
                        SwipeQuoteSettings.KEY_REPEAT_ENABLE,
                        "右滑复读",
                        "右滑消息后复读到当前聊天",
                        SwipeQuoteSettings.DEFAULT_REPEAT_ENABLE
                    )
                    InsetDivider()
                    SwitchRow(
                        sp,
                        SwipeQuoteSettings.KEY_REPEAT_MENU_ENABLE,
                        "长按菜单复读",
                        "长按消息后点击 +1 复读到当前聊天",
                        SwipeQuoteSettings.DEFAULT_REPEAT_MENU_ENABLE
                    )
                }
            }
        }
    }
}

@Composable
internal fun ChatTimeStyleMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, ChatTimeStyleSettings.PREFS_NAME) }
    var mode by remember {
        mutableStateOf(
            ChatTimeStyleSettings.normalizeMode(
                sp.getString(ChatTimeStyleSettings.KEY_MODE, ChatTimeStyleSettings.DEFAULT_MODE)
            )
        )
    }
    var timeFormat by remember {
        mutableStateOf(
            sp.getString(
                ChatTimeStyleSettings.KEY_TIME_FORMAT,
                ChatTimeStyleSettings.DEFAULT_TIME_FORMAT
            ).orEmpty().ifBlank { ChatTimeStyleSettings.DEFAULT_TIME_FORMAT }
        )
    }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    PageScaffold(
        title = provider.title(),
        largeTitle = provider.title(),
        scrollBehavior = scrollBehavior,
        bottomBar = { BottomActionBar("返回", onBack) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(text = "聊天时间") }
            item {
                SettingsCard {
                    PopupChoiceRow(
                        title = "显示方式",
                        summary = chatTimeModeLabel(mode),
                        options = chatTimeModeChoices(),
                        currentValue = mode,
                        onValueChanged = {
                            mode = ChatTimeStyleSettings.normalizeMode(it)
                            sp.edit().putString(ChatTimeStyleSettings.KEY_MODE, mode).apply()
                        }
                    )
                    if (mode == ChatTimeStyleSettings.MODE_CUSTOM ||
                        mode == ChatTimeStyleSettings.MODE_EVERY
                    ) {
                        InsetDivider()
                        InputRow(
                            title = "时间格式",
                            summary = "例如 yyyy-MM-dd HH:mm:ss",
                            value = timeFormat,
                            onValueChange = {
                                timeFormat = it
                                sp.edit().putString(ChatTimeStyleSettings.KEY_TIME_FORMAT, it).apply()
                            }
                        )
                    }
                }
            }
        }
    }
}

private fun chatTimeModeChoices(): List<PopupChoice<String>> = listOf(
    PopupChoice("微信原样", ChatTimeStyleSettings.MODE_ORIGINAL),
    PopupChoice("自定义", ChatTimeStyleSettings.MODE_CUSTOM),
    PopupChoice("每条消息都显示", ChatTimeStyleSettings.MODE_EVERY),
    PopupChoice("隐藏", ChatTimeStyleSettings.MODE_HIDDEN)
)

private fun chatTimeModeLabel(mode: String): String = when (mode) {
    ChatTimeStyleSettings.MODE_CUSTOM -> "自定义格式（微信原生间隔）"
    ChatTimeStyleSettings.MODE_EVERY -> "每条消息都显示时间"
    ChatTimeStyleSettings.MODE_HIDDEN -> "隐藏微信原生聊天时间"
    else -> "保持微信原样（间隔显示）"
}

@Composable
internal fun AboutCard(context: Context) {
    val hostVersion = WeChatApis.version()?.current()?.displayVersion()?.takeIf { it.isNotBlank() } ?: "未知"
    val moduleVersion = BuildConfig.VERSION_NAME.takeIf { it.isNotBlank() } ?: "未知"
    val scope = rememberCoroutineScope()
    var checking by remember { mutableStateOf(false) }
    var result by remember { mutableStateOf<HchatUpdateChecker.CheckResult?>(null) }
    SettingsCard {
        InfoRow(label = "版本", value = moduleVersion)
        InsetDivider()
        InfoRow(label = "宿主", value = hostVersion)
        InsetDivider()
        ActionRow(
            title = "推送更新",
            summary = when {
                checking -> "正在检查 GitHub 最新版本…"
                result?.error != null -> result?.error.orEmpty()
                result?.isNewer == true -> "发现新版本 ${result?.remoteVersion.orEmpty()}，点击下载"
                result != null -> "当前已是最新版本"
                else -> "检查 Hchat-alt-entry 最新 Release"
            },
            onClick = {
                if (checking) return@ActionRow
                checking = true
                scope.launch {
                    val checked = withContext(Dispatchers.IO) {
                        HchatUpdateChecker.check(moduleVersion)
                    }
                    result = checked
                    checking = false
                    if (checked.isNewer) {
                        HchatUpdateChecker.openDownload(context, checked)
                    } else if (checked.error != null) {
                        Toast.makeText(context, checked.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )
        result?.notes?.takeIf { it.isNotBlank() }?.let { notes ->
            InsetDivider()
            InfoRow("更新说明", notes.take(180))
        }
        InsetDivider()
        InfoRow(label = "作者", value = "。。")
    }
}
