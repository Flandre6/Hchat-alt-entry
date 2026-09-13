package h.Hchat.hooks.items.chattoolbar

import h.Hchat.event.Events
import h.Hchat.hooks.api.core.WeChatApis
import h.Hchat.hooks.core.BaseFeature
import h.Hchat.hooks.core.DexInstallScheduler
import h.Hchat.hooks.core.FeatureContext

class ChatToolbarFeature : BaseFeature() {
    private var runtime: ChatToolbarRuntime? = null

    override fun featureId(): String = ID

    override fun name(): String = "聊天工具栏"

    override fun onFeatureInit(context: FeatureContext) {
        registerSettingsProvider(ChatToolbarSettingsProvider())
    }

    override fun onFeatureInstall(context: FeatureContext) {
        val installed = ChatToolbarRuntime(context, ::logRuntimeError)
        runtime = installed
        installed.installBaseHooks()
        trackSubscription(
            WeChatApis.lifecycle()?.subscribe { event ->
                if (event.isDestroy) installed.onActivityDestroyed(event.activity)
            }
        )
        scheduleDexInstall()
        subscribe(Events.DexReady::class.java) { scheduleDexInstall() }
    }

    override fun onFeatureDestroy(context: FeatureContext) {
        runtime?.destroy()
        runtime = null
    }

    private fun scheduleDexInstall() {
        DexInstallScheduler.schedule(ID, name(), stage = DexInstallScheduler.Stage.BRIDGE) {
            runtime?.installDexHooks() == true
        }
    }

    private fun logRuntimeError(message: String, throwable: Throwable?) {
        logError(message, throwable)
    }

    companion object {
        const val ID = "chat_toolbar"
    }
}
