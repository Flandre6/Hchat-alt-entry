package h.Hchat.hooks.items.monetgenerator

import android.content.Context
import android.content.SharedPreferences
import android.os.Process
import h.Hchat.hooks.items.monetgenerator.engine.MonetBubbleStyle
import h.Hchat.hooks.items.monetgenerator.engine.MonetGenerationOptions
import h.Hchat.hooks.items.monetgenerator.engine.MonetTabStyle
import h.Hchat.hooks.items.monetgenerator.engine.MonetUserScope
import h.Hchat.preferences.HchatStorage

object MonetModuleGeneratorSettings {
    const val PREFS_NAME = "Hchat_monet_module_generator"
    const val KEY_BUBBLE_STYLE = "bubble_style"
    const val KEY_MULTI_SCENE_CORNERS = "multi_scene_corners"
    const val KEY_TAB_STYLE = "tab_style"
    const val KEY_USER_SCOPE = "user_scope"

    val DEFAULT_BUBBLE_STYLE = MonetBubbleStyle.MODERN
    const val DEFAULT_MULTI_SCENE_CORNERS = true
    val DEFAULT_TAB_STYLE = MonetTabStyle.SOLID
    val DEFAULT_USER_SCOPE = MonetUserScope.CURRENT

    fun preferences(context: Context): SharedPreferences = HchatStorage.preferences(context, PREFS_NAME)

    fun options(context: Context): MonetGenerationOptions {
        val preferences = preferences(context)
        return MonetGenerationOptions(
            bubbleStyle = enumValue(
                preferences.getString(KEY_BUBBLE_STYLE, DEFAULT_BUBBLE_STYLE.name),
                DEFAULT_BUBBLE_STYLE
            ),
            multiSceneCorners = preferences.getBoolean(
                KEY_MULTI_SCENE_CORNERS,
                DEFAULT_MULTI_SCENE_CORNERS
            ),
            tabStyle = enumValue(
                preferences.getString(KEY_TAB_STYLE, DEFAULT_TAB_STYLE.name),
                DEFAULT_TAB_STYLE
            ),
            userScope = enumValue(
                preferences.getString(KEY_USER_SCOPE, DEFAULT_USER_SCOPE.name),
                DEFAULT_USER_SCOPE
            ),
            currentUserId = Process.myUid() / 100000
        )
    }

    private inline fun <reified T : Enum<T>> enumValue(value: String?, fallback: T): T {
        return value?.let { runCatching { enumValueOf<T>(it) }.getOrNull() } ?: fallback
    }
}
