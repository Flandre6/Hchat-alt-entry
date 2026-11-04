package android.content
import android.net.Uri
import java.io.File
import java.io.InputStream

open class Context {
    open val applicationContext: Context get() = this
    val packageManager = PackageManager()
    val contentResolver = ContentResolver()
    val cacheDir: File get() = File(System.getProperty("java.io.tmpdir"))
}
class PackageManager { fun queryIntentActivities(intent: Intent, flags: Int): List<Any> = emptyList() }
class ContentResolver {
    fun openInputStream(uri: Uri): InputStream? = java.io.ByteArrayInputStream("{}".toByteArray())
    fun openOutputStream(uri: Uri, mode: String): java.io.OutputStream? = java.io.ByteArrayOutputStream()
    fun takePersistableUriPermission(uri: Uri, flags: Int) {}
    fun query(uri: Uri, columns: Array<String>, selection: String?, args: Array<String>?, order: String?): Cursor? = null
}
class Cursor : AutoCloseable {
    fun moveToFirst() = false
    fun getColumnIndex(name: String) = -1
    fun getString(index: Int): String? = null
    override fun close() {}
}
class Intent() {
    constructor(action: String) : this()
    constructor(other: Intent) : this()
    var failData = false
    var data: Uri? = null
        get() { if (failData) error("injected result failure"); return field }
    var flags: Int = 0
    var type: String? = null
    fun addCategory(category: String) = this
    fun addFlags(value: Int) = apply { flags = flags or value }
    fun setPackage(value: String) = this
    fun putExtra(key: String, value: String) = this
    companion object {
        const val ACTION_CREATE_DOCUMENT = "create"
        const val EXTRA_TITLE = "title"
        const val FLAG_GRANT_WRITE_URI_PERMISSION = 2
        const val ACTION_OPEN_DOCUMENT = "open"
        const val ACTION_GET_CONTENT = "get"
        const val CATEGORY_OPENABLE = "openable"
        const val FLAG_GRANT_READ_URI_PERMISSION = 1
        const val FLAG_GRANT_PERSISTABLE_URI_PERMISSION = 64
        fun createChooser(intent: Intent, title: String) = intent
    }
}
