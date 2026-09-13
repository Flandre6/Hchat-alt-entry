package h.Hchat.hooks.items.inputtoolbar

import android.content.Context
import h.Hchat.preferences.HchatStorage

object InputToolbarSettings {
    const val PREFS_NAME = "Hchat_input_toolbar_config"
    const val KEY_ENABLE = "input_toolbar_enable"
    const val DEFAULT_ENABLE = true

    fun preferences(context: Context) = HchatStorage.preferences(context, PREFS_NAME)

    fun enabled(context: Context): Boolean = preferences(context)
        .getBoolean(KEY_ENABLE, DEFAULT_ENABLE)
}
