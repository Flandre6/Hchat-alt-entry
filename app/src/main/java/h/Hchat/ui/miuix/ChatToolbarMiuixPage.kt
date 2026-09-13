package h.Hchat.ui.miuix

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import h.Hchat.hooks.items.chattoolbar.ChatToolbarItemConfig
import h.Hchat.hooks.items.chattoolbar.ChatToolbarSettings
import h.Hchat.ui.FeatureSettingsProvider
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.Switch
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
internal fun ChatToolbarMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { ChatToolbarSettings.preferences(context) }
    var enabled by remember {
        mutableStateOf(sp.getBoolean(ChatToolbarSettings.KEY_ENABLE, ChatToolbarSettings.DEFAULT_ENABLE))
    }
    var displayMode by remember {
        mutableStateOf(
            sp.getString(ChatToolbarSettings.KEY_DISPLAY_MODE, ChatToolbarSettings.DEFAULT_DISPLAY_MODE)
                ?: ChatToolbarSettings.DEFAULT_DISPLAY_MODE
        )
    }
    var items by remember { mutableStateOf(ChatToolbarSettings.loadItems(sp)) }
    var quickReplies by remember {
        mutableStateOf(ChatToolbarSettings.loadQuickReplies(sp).joinToString("\n"))
    }
    val quickReplyEnabled = items.firstOrNull {
        it.id == ChatToolbarSettings.QUICK_REPLY_ID
    }?.enabled == true
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()

    fun moveItem(index: Int, delta: Int) {
        val target = index + delta
        if (index !in items.indices || target !in items.indices) return
        items = items.toMutableList().also { list ->
            val moved = list.removeAt(index)
            list.add(target, moved)
        }
    }

    PageScaffold(
        title = provider.title(),
        largeTitle = provider.title(),
        scrollBehavior = scrollBehavior,
        onBack = onBack,
        bottomBar = {
            BottomActionBar(
                primaryText = "保存",
                onPrimaryClick = {
                    sp.edit()
                        .putBoolean(ChatToolbarSettings.KEY_ENABLE, enabled)
                        .putString(ChatToolbarSettings.KEY_DISPLAY_MODE, displayMode)
                        .apply()
                    ChatToolbarSettings.saveItems(sp, items)
                    ChatToolbarSettings.saveQuickReplies(
                        sp,
                        quickReplies.lines().map { it.trim() }.filter { it.isNotEmpty() }
                    )
                    Toast.makeText(context, "聊天工具栏设置已保存", Toast.LENGTH_SHORT).show()
                },
                secondaryText = "重置",
                onSecondaryClick = {
                    enabled = ChatToolbarSettings.DEFAULT_ENABLE
                    displayMode = ChatToolbarSettings.DEFAULT_DISPLAY_MODE
                    items = ChatToolbarSettings.defaultItems()
                    quickReplies = ""
                },
                middleText = "取消",
                onMiddleClick = onBack
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
            item { SmallTitle(text = "聊天输入区") }
            item {
                SettingsCard {
                    SwitchRow(
                        checked = enabled,
                        title = "聊天工具栏",
                        summary = "在输入框上方显示当前会话可用的微信工具"
                    ) { enabled = it }
                    if (enabled) {
                        InsetDivider()
                        PopupChoiceRow(
                            title = "显示方式",
                            summary = when (displayMode) {
                                ChatToolbarSettings.DISPLAY_ICON -> "仅显示图标"
                                ChatToolbarSettings.DISPLAY_TEXT -> "仅显示文字"
                                else -> "同时显示图标和文字"
                            },
                            options = listOf(
                                PopupChoice("图标和文字", ChatToolbarSettings.DISPLAY_BOTH),
                                PopupChoice("仅图标", ChatToolbarSettings.DISPLAY_ICON),
                                PopupChoice("仅文字", ChatToolbarSettings.DISPLAY_TEXT)
                            ),
                            currentValue = displayMode,
                            onValueChanged = { displayMode = it }
                        )
                        InsetDivider()
                        InfoRow("工具来源", "按当前聊天的微信原生“+”面板动态显示")
                    }
                }
            }
            if (enabled) {
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "工具与顺序") }
                item {
                    SettingsCard {
                        items.forEachIndexed { index, item ->
                            if (index > 0) InsetDivider()
                            ChatToolbarItemSettingRow(
                                item = item,
                                canMoveUp = index > 0,
                                canMoveDown = index < items.lastIndex,
                                onEnabledChange = { checked ->
                                    items = items.toMutableList().also { list ->
                                        list[index] = item.copy(enabled = checked)
                                    }
                                },
                                onMoveUp = { moveItem(index, -1) },
                                onMoveDown = { moveItem(index, 1) }
                            )
                        }
                    }
                }
            }
            if (enabled && quickReplyEnabled) {
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "快捷回复") }
                item {
                    SettingsCard {
                        InputRow(
                            title = "回复内容",
                            summary = "每行一条，点击工具栏的快捷回复后直接发送",
                            value = quickReplies,
                            minLines = 4,
                            onValueChange = { quickReplies = it }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatToolbarItemSettingRow(
    item: ChatToolbarItemConfig,
    canMoveUp: Boolean,
    canMoveDown: Boolean,
    onEnabledChange: (Boolean) -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit
) {
    val title = ChatToolbarSettings.tool(item.id)?.title ?: item.id
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = MiuixTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = when (item.id) {
                    ChatToolbarSettings.QUICK_REPLY_ID -> "从自定义文本中选择并发送"
                    ChatToolbarSettings.SYSTEM_CAMERA_ID -> "调用微信相册入口的长按拍摄动作"
                    else -> "调用微信原生工具"
                },
                color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                fontSize = 12.sp
            )
        }
        Image(
            imageVector = NavIcons.MoveUp,
            contentDescription = "上移$title",
            colorFilter = ColorFilter.tint(
                MiuixTheme.colorScheme.onSurface.copy(alpha = if (canMoveUp) 1f else 0.25f)
            ),
            modifier = Modifier.size(34.dp)
                .clip(RoundedCornerShape(8.dp))
                .clickable(enabled = canMoveUp, onClick = onMoveUp)
                .padding(7.dp)
        )
        Image(
            imageVector = NavIcons.MoveDown,
            contentDescription = "下移$title",
            colorFilter = ColorFilter.tint(
                MiuixTheme.colorScheme.onSurface.copy(alpha = if (canMoveDown) 1f else 0.25f)
            ),
            modifier = Modifier.size(34.dp)
                .clip(RoundedCornerShape(8.dp))
                .clickable(enabled = canMoveDown, onClick = onMoveDown)
                .padding(7.dp)
        )
        Switch(
            checked = item.enabled,
            onCheckedChange = onEnabledChange
        )
    }
}
