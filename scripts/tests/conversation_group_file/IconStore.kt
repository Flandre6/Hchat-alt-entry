package h.Hchat.hooks.items.conversationtabs
import android.content.Context
import android.net.Uri
object ConversationTabsIconStore {
    fun isSupportedTabKey(key: String) = true
    fun delete(context: Context, path: String) {}
    fun saveFromUri(context: Context, key: String, uri: Uri): String? = "icon"
}
