package h.Hchat.hooks.items.conversationgroup
import android.content.Context
object ConversationGroupStore {
    fun accountKey() = "account"
    fun importCurrentAccount(context: Context, json: String, account: String, canceled: () -> Boolean) = Result("ok", true)
    data class Result(val message: String, val success: Boolean)
}
