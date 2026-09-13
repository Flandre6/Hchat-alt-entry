package h.Hchat.hooks.items.monetgenerator

import h.Hchat.ui.FeatureSettingsProvider
import h.Hchat.ui.SimpleFeatureSettingsProvider

class MonetModuleGeneratorSettingsProvider : SimpleFeatureSettingsProvider(
    MonetModuleGeneratorFeature.ID,
    "莫奈引擎模块生成器",
    "为当前微信生成 Android 12+ 动态取色 RRO Root 模块",
    FeatureSettingsProvider.CATEGORY_PRACTICAL
)
