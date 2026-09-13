package h.Hchat.hooks.items.monetgenerator

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import h.Hchat.utils.HLog
import java.lang.ref.WeakReference
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

internal object MonetModuleDocumentBridge {
    private const val TAG = "[Hchat:MonetGenerator]"
    private const val REQUEST_CODE_START = 0x7810
    private const val REQUEST_CODE_END = 0x78ff
    private val nextRequestCode = AtomicInteger(REQUEST_CODE_START)
    private val pending = ConcurrentHashMap<Int, Pending>()
    private val hookedClasses = ConcurrentHashMap.newKeySet<Class<*>>()
    private val destroyHookedClasses = ConcurrentHashMap.newKeySet<Class<*>>()

    fun launch(activity: Activity, suggestedName: String, callback: (ExportTarget?) -> Unit) {
        hookActivityHierarchy(activity.javaClass)
        val requestCode = allocateRequestCode()
        pending[requestCode] = Pending(WeakReference(activity), callback)
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/zip"
            putExtra(Intent.EXTRA_TITLE, suggestedName)
            addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }.preferSystemDocumentsUi(activity)
        runCatching { activity.startActivityForResult(intent, requestCode) }
            .onFailure { error ->
                pending.remove(requestCode)?.deliver(null)
                HLog.e("$TAG 启动模块输出选择器失败: ${error.message}", error)
            }
    }

    private fun hookActivityHierarchy(activityClass: Class<*>) {
        var current: Class<*>? = activityClass
        while (current != null && Activity::class.java.isAssignableFrom(current)) {
            hookActivityResult(current)
            hookActivityDestroy(current)
            current = current.superclass
        }
    }

    private fun hookActivityResult(clazz: Class<*>) {
        if (!hookedClasses.add(clazz)) return
        runCatching {
            XposedBridge.hookAllMethods(clazz, "onActivityResult", object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val requestCode = param.args.getOrNull(0) as? Int ?: return
                    val request = pending[requestCode] ?: return
                    val activity = request.activity.get()
                    if (activity == null) {
                        pending.remove(requestCode, request)
                        return
                    }
                    if (param.thisObject !== activity || !pending.remove(requestCode, request)) return
                    val resultCode = param.args.getOrNull(1) as? Int ?: Activity.RESULT_CANCELED
                    val data = param.args.getOrNull(2) as? Intent
                    val uri = data?.data
                    if (resultCode != Activity.RESULT_OK || uri == null) {
                        request.deliver(null)
                        return
                    }
                    request.deliver(ExportTarget(uri, displayName(activity, uri)))
                }
            })
        }.onFailure { hookedClasses.remove(clazz) }
    }

    private fun hookActivityDestroy(clazz: Class<*>) {
        if (!destroyHookedClasses.add(clazz)) return
        runCatching {
            XposedBridge.hookAllMethods(clazz, "onDestroy", object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val activity = param.thisObject as? Activity ?: return
                    pending.entries.forEach { entry ->
                        val owner = entry.value.activity.get()
                        if (owner == null || owner === activity) pending.remove(entry.key, entry.value)
                    }
                }
            })
        }.onFailure { destroyHookedClasses.remove(clazz) }
    }

    private fun displayName(context: Context, uri: Uri): String {
        val queried = runCatching {
            context.contentResolver.query(
                uri,
                arrayOf(OpenableColumns.DISPLAY_NAME),
                null,
                null,
                null
            )?.use { cursor ->
                if (!cursor.moveToFirst()) return@use null
                val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (index < 0) null else cursor.getString(index)
            }
        }.getOrNull().orEmpty()
        return queried.ifBlank { uri.lastPathSegment.orEmpty() }.ifBlank { "Hchat_Monet_WeChat.zip" }
    }

    private fun allocateRequestCode(): Int {
        repeat(REQUEST_CODE_END - REQUEST_CODE_START + 1) {
            val candidate = nextRequestCode.updateAndGet { current ->
                if (current >= REQUEST_CODE_END) REQUEST_CODE_START else current + 1
            }
            if (!pending.containsKey(candidate)) return candidate
        }
        val oldest = pending.keys.minOrNull() ?: REQUEST_CODE_START
        pending.remove(oldest)?.deliver(null)
        return oldest
    }

    private fun Intent.preferSystemDocumentsUi(context: Context): Intent {
        for (packageName in listOf("com.google.android.documentsui", "com.android.documentsui")) {
            val candidate = Intent(this).setPackage(packageName)
            if (runCatching { context.packageManager.queryIntentActivities(candidate, 0) }
                    .getOrDefault(emptyList()).isNotEmpty()
            ) {
                setPackage(packageName)
                break
            }
        }
        return this
    }

    private data class Pending(
        val activity: WeakReference<Activity>,
        val callback: (ExportTarget?) -> Unit
    ) {
        fun deliver(result: ExportTarget?) {
            val owner = activity.get() ?: return
            owner.runOnUiThread {
                if (!owner.isFinishing && !owner.isDestroyed) callback(result)
            }
        }
    }

    data class ExportTarget(val uri: Uri, val displayName: String)
}
