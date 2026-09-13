package h.Hchat.hooks.items.monetgenerator

import h.Hchat.hooks.core.BaseFeature
import h.Hchat.hooks.core.FeatureContext

class MonetModuleGeneratorFeature : BaseFeature() {
    override fun featureId(): String = ID

    override fun name(): String = "莫奈引擎模块生成器"

    override fun onFeatureInit(context: FeatureContext) {
        MonetModuleGeneratorRuntime.attach(context)
        registerSettingsProvider(MonetModuleGeneratorSettingsProvider())
    }

    override fun onFeatureInstall(context: FeatureContext) = Unit

    override fun onFeatureDestroy(context: FeatureContext) {
        MonetModuleGeneratorRuntime.detach(context)
    }

    companion object {
        const val ID = "monet_module_generator"
    }
}
