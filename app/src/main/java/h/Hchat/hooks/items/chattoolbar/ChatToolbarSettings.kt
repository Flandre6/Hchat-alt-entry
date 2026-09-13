package h.Hchat.hooks.items.chattoolbar

import android.content.Context
import android.content.SharedPreferences
import h.Hchat.preferences.HchatStorage
import org.json.JSONArray

data class ChatToolbarItemConfig(
    val id: String,
    val enabled: Boolean
)

object ChatToolbarSettings {
    const val PREFS_NAME = "Hchat_chat_toolbar_config"
    const val KEY_ENABLE = "enable"
    const val KEY_DISPLAY_MODE = "display_mode"
    const val KEY_ITEMS = "items"
    const val KEY_QUICK_REPLIES = "quick_replies"

    const val DISPLAY_BOTH = "both"
    const val DISPLAY_ICON = "icon"
    const val DISPLAY_TEXT = "text"

    const val QUICK_REPLY_ID = "quick_reply"
    const val SYSTEM_CAMERA_ID = "system_camera"

    const val DEFAULT_ENABLE = false
    const val DEFAULT_DISPLAY_MODE = DISPLAY_BOTH

    data class Tool(
        val id: String,
        val title: String,
        val resourceNames: Set<String> = emptySet(),
        val hostTitles: Set<String> = setOf(title)
    )

    val tools: List<Tool> = listOf(
        Tool(QUICK_REPLY_ID, "快捷回复"),
        Tool("album", "相册", setOf("panel_icon_pic")),
        Tool(SYSTEM_CAMERA_ID, "系统拍摄"),
        Tool("camera", "拍摄", setOf("panel_icon_camera")),
        Tool("video_call", "视频通话", setOf("panel_icon_voip")),
        Tool(
            "voice_call",
            "语音通话",
            setOf("panel_icon_multitalk", "panel_icon_voipvoice"),
            setOf("语音通话", "多人通话")
        ),
        Tool("location", "位置", setOf("panel_icon_location")),
        Tool("red_packet", "红包", setOf("panel_icon_luckymoney")),
        Tool("gift", "礼物", setOf("icons_filled_gift_chatting")),
        Tool("transfer", "转账", setOf("panel_icon_transfer")),
        Tool("voice_input", "语音输入", setOf("panel_icon_voiceinput")),
        Tool("favorite", "收藏", setOf("panel_icon_fav")),
        Tool("solitaire", "接龙", setOf("icons_outlined_continued_form")),
        Tool("contact_card", "个人名片", setOf("panel_icon_friendcard")),
        Tool("file", "文件", setOf("panel_icon_file_explorer")),
        Tool("music", "音乐", setOf("icon_music_filled"))
    )

    private val toolById = tools.associateBy { it.id }
    private val toolByResource = tools
        .flatMap { tool -> tool.resourceNames.map { it to tool } }
        .toMap()
    private val toolByHostTitle = tools
        .flatMap { tool -> tool.hostTitles.map { it to tool } }
        .toMap()

    fun preferences(context: Context): SharedPreferences =
        HchatStorage.preferences(context, PREFS_NAME)

    fun tool(id: String): Tool? = toolById[id]

    fun toolForResourceName(resourceName: String?): Tool? =
        resourceName?.let(toolByResource::get)

    fun toolForHostTitle(title: String?): Tool? =
        title?.trim()?.let(toolByHostTitle::get)

    fun loadItems(preferences: SharedPreferences): List<ChatToolbarItemConfig> {
        val defaults = defaultItems()
        val raw = preferences.getString(KEY_ITEMS, null)?.takeIf { it.isNotBlank() }
            ?: return defaults
        val decoded = runCatching {
            val array = JSONArray(raw)
            buildList {
                for (index in 0 until array.length()) {
                    val value = array.optJSONObject(index) ?: continue
                    val id = value.optString("id").trim()
                    if (id in toolById) {
                        add(ChatToolbarItemConfig(id, value.optBoolean("enabled", true)))
                    }
                }
            }
        }.getOrDefault(emptyList())
        val result = decoded.distinctBy { it.id }.toMutableList()
        defaults.forEach { item ->
            if (result.none { it.id == item.id }) result += item
        }
        return result
    }

    fun saveItems(preferences: SharedPreferences, items: List<ChatToolbarItemConfig>) {
        val normalized = items
            .filter { it.id in toolById }
            .distinctBy { it.id }
            .toMutableList()
        defaultItems().forEach { item ->
            if (normalized.none { it.id == item.id }) normalized += item
        }
        val array = JSONArray()
        normalized.forEach { item ->
            array.put(org.json.JSONObject().apply {
                put("id", item.id)
                put("enabled", item.enabled)
            })
        }
        preferences.edit().putString(KEY_ITEMS, array.toString()).apply()
    }

    fun loadQuickReplies(preferences: SharedPreferences): List<String> {
        val raw = preferences.getString(KEY_QUICK_REPLIES, "").orEmpty()
        if (raw.isBlank()) return emptyList()
        return runCatching {
            val array = JSONArray(raw)
            buildList {
                for (index in 0 until array.length()) {
                    array.optString(index).trim().takeIf { it.isNotEmpty() }?.let { add(it) }
                }
            }
        }.getOrDefault(emptyList())
    }

    fun saveQuickReplies(preferences: SharedPreferences, replies: List<String>) {
        val array = JSONArray()
        replies.map { it.trim() }.filter { it.isNotEmpty() }.distinct().forEach(array::put)
        preferences.edit().putString(KEY_QUICK_REPLIES, array.toString()).apply()
    }

    fun defaultItems(): List<ChatToolbarItemConfig> = tools.map { tool ->
        ChatToolbarItemConfig(
            id = tool.id,
            enabled = tool.id != QUICK_REPLY_ID
        )
    }
}
