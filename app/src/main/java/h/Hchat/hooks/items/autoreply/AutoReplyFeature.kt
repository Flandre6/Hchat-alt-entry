package h.Hchat.hooks.items.autoreply

import h.Hchat.event.Events
import h.Hchat.hooks.api.core.WeChatApis
import h.Hchat.hooks.core.BaseFeature
import h.Hchat.hooks.core.FeatureContext
import h.Hchat.hooks.core.DexInstallScheduler
import h.Hchat.hooks.items.script.ScriptNewFriendHook
import h.Hchat.utils.HLog
import java.util.concurrent.atomic.AtomicBoolean

class AutoReplyFeature : BaseFeature() {
    private val observerSubscribed = AtomicBoolean(false)
    private val friendHookInstalled = AtomicBoolean(false)
    override fun featureId(): String = ID

    override fun name(): String = "自动回复"

    override fun onFeatureInit(context: FeatureContext) {
        registerSettingsProvider(AutoReplySettingsProvider())
    }

    override fun onFeatureInstall(context: FeatureContext) {
        fun installObserver(): Boolean {
            val observe = WeChatApis.message().observe() ?: return false
            // The API may exist before its PB/DB hooks are ready; install is idempotent.
            observe.install()
            if (observerSubscribed.compareAndSet(false, true)) {
                trackSubscription(observe.subscribe { message ->
                    AutoReplyRuntime.handleMessage(context.hostContext(), message)
                })
            }
            if (friendHookInstalled.compareAndSet(false, true)) {
                ScriptNewFriendHook.install(context)
                trackSubscription(ScriptNewFriendHook.subscribe { event ->
                    AutoReplyRuntime.handleNewFriend(context.hostContext(), event.wxid, event.ticket, event.scene)
                })
            }
            return true
        }
        DexInstallScheduler.schedule(
            "auto_reply:observer", "自动回复消息监听", DexInstallScheduler.Stage.BRIDGE
        ) { installObserver() }
        subscribe(Events.DexReady::class.java) {
            DexInstallScheduler.schedule(
                "auto_reply:observer", "自动回复消息监听", DexInstallScheduler.Stage.WARMUP
            ) { installObserver() }
        }
    }

    companion object {
        const val ID = "auto_reply"
        private const val TAG = "[Hchat:AutoReply]"
    }
}
