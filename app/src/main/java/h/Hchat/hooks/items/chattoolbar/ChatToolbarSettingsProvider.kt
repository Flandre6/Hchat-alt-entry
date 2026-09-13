package h.Hchat.hooks.items.chattoolbar

import h.Hchat.ui.FeatureSettingsProvider
import h.Hchat.ui.SimpleFeatureSettingsProvider

class ChatToolbarSettingsProvider : SimpleFeatureSettingsProvider(
    ChatToolbarFeature.ID,
    "聊天工具栏",
    "在聊天输入框上方显示微信常用工具和快捷回复",
    FeatureSettingsProvider.CATEGORY_ENHANCE
)
