package h.Hchat.update

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import h.Hchat.preferences.HchatStorage
import h.Hchat.utils.HLog
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

/** Hchat GitHub Release 更新检查与系统通知。 */
object HchatUpdateChecker {
    private const val TAG = "[Hchat:Update]"
    private const val RELEASE_API =
        "https://api.github.com/repos/Flandre6/Hchat-alt-entry/releases/latest"
    private const val PREFS_NAME = "hchat_update"
    private const val KEY_LAST_ATTEMPT = "last_attempt_at"
    private const val KEY_LAST_NOTIFIED_TAG = "last_notified_tag"
    private const val CHECK_INTERVAL_MS = 12L * 60L * 60L * 1000L
    private const val CHANNEL_ID = "hchat_module_update"
    private const val CHANNEL_NAME = "Hchat 更新"
    private const val NOTIFICATION_ID = 0x48434154

    private val executor = Executors.newSingleThreadExecutor { runnable ->
        Thread(runnable, "Hchat-UpdateCheck").apply { isDaemon = true }
    }

    data class CheckResult(
        val currentVersion: String,
        val remoteVersion: String? = null,
        val tag: String? = null,
        val notes: String = "",
        val releaseUrl: String? = null,
        val downloadUrl: String? = null,
        val isNewer: Boolean = false,
        val error: String? = null
    )

    /** 设置页使用的同步检查入口，调用方必须放在 IO 调度器。 */
    fun check(currentVersion: String): CheckResult {
        val normalizedCurrent = currentVersion.trim().ifBlank { "0.0.0" }
        var connection: HttpURLConnection? = null
        return try {
            connection = (URL(RELEASE_API).openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                connectTimeout = 8_000
                readTimeout = 8_000
                useCaches = false
                setRequestProperty("Accept", "application/vnd.github+json")
                setRequestProperty("User-Agent", "Hchat-UpdateChecker")
                if (Build.VERSION.SDK_INT >= 26) {
                    setRequestProperty("X-GitHub-Api-Version", "2022-11-28")
                }
            }
            val responseCode = connection.responseCode
            if (responseCode !in 200..299) {
                return CheckResult(normalizedCurrent, error = "GitHub 返回 HTTP $responseCode")
            }
            val body = BufferedReader(InputStreamReader(connection.inputStream, Charsets.UTF_8))
                .use { it.readText() }
            val release = JSONObject(body)
            val tag = release.optString("tag_name").trim().takeIf { it.isNotBlank() }
            val remoteVersion = (tag ?: release.optString("name"))
                .removePrefix("v")
                .trim()
                .takeIf { it.isNotBlank() }
            val notes = release.optString("body").trim().take(8_000)
            val releaseUrl = release.optString("html_url").trim()
                .takeIf { it.isNotBlank() } ?: RELEASE_API
            val downloadUrl = findApkUrl(release)
            CheckResult(
                currentVersion = normalizedCurrent,
                remoteVersion = remoteVersion,
                tag = tag ?: remoteVersion,
                notes = notes,
                releaseUrl = releaseUrl,
                downloadUrl = downloadUrl,
                isNewer = remoteVersion != null && compareVersions(remoteVersion, normalizedCurrent) > 0
            )
        } catch (t: Throwable) {
            HLog.e("$TAG 检查更新失败: ${t.message}", t)
            CheckResult(normalizedCurrent, error = t.message ?: t.javaClass.simpleName)
        } finally {
            connection?.disconnect()
        }
    }

    /** 主进程低频后台检查；不阻塞微信启动线程。 */
    @JvmStatic
    fun scheduleCheck(context: Context, currentVersion: String) {
        val appContext = context.applicationContext ?: context
        val prefs = HchatStorage.preferences(appContext, PREFS_NAME)
        val now = System.currentTimeMillis()
        val lastAttempt = prefs.getLong(KEY_LAST_ATTEMPT, 0L)
        if (now - lastAttempt < CHECK_INTERVAL_MS) return
        prefs.edit().putLong(KEY_LAST_ATTEMPT, now).apply()
        executor.execute {
            val result = check(currentVersion)
            notifyIfNew(appContext, result)
        }
    }

    @Synchronized
    fun notifyIfNew(context: Context, result: CheckResult) {
        if (!result.isNewer || result.tag.isNullOrBlank()) return
        if (Build.VERSION.SDK_INT >= 33 &&
            context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) !=
            android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val prefs = HchatStorage.preferences(context, PREFS_NAME)
        if (prefs.getString(KEY_LAST_NOTIFIED_TAG, "") == result.tag) return
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
            ?: return
        if (Build.VERSION.SDK_INT >= 26) {
            manager.createNotificationChannel(
                NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            )
        }
        val target = result.downloadUrl ?: result.releaseUrl ?: return
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(target)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val flags = PendingIntent.FLAG_UPDATE_CURRENT or
            (if (Build.VERSION.SDK_INT >= 23) PendingIntent.FLAG_IMMUTABLE else 0)
        val pendingIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, intent, flags)
        val icon = context.applicationInfo.icon.takeIf { it != 0 } ?: android.R.drawable.ic_dialog_info
        val summary = result.notes.replace(Regex("\\s+"), " ").trim()
            .ifBlank { "有新版本可下载" }
            .take(180)
        val builder = if (Build.VERSION.SDK_INT >= 26) {
            Notification.Builder(context, CHANNEL_ID)
        } else {
            Notification.Builder(context)
        }
        builder.setSmallIcon(icon)
            .setContentTitle("Hchat 有新版本：${result.remoteVersion ?: result.tag}")
            .setContentText(summary)
            .setStyle(Notification.BigTextStyle().bigText(summary))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setWhen(System.currentTimeMillis())
            .setShowWhen(true)
            .setPriority(Notification.PRIORITY_DEFAULT)
        runCatching {
            manager.notify(NOTIFICATION_ID, builder.build())
            prefs.edit().putString(KEY_LAST_NOTIFIED_TAG, result.tag).apply()
        }.onFailure {
            HLog.e("$TAG 发送更新通知失败: ${it.message}", it)
        }
    }

    fun openDownload(context: Context, result: CheckResult): Boolean {
        val target = result.downloadUrl ?: result.releaseUrl ?: return false
        return runCatching {
            context.startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse(target))
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
            true
        }.getOrElse {
            HLog.e("$TAG 打开下载链接失败: ${it.message}", it)
            false
        }
    }

    private fun findApkUrl(release: JSONObject): String? {
        val assets = release.optJSONArray("assets") ?: return null
        var fallback: String? = null
        for (index in 0 until assets.length()) {
            val asset = assets.optJSONObject(index) ?: continue
            val name = asset.optString("name")
            val url = asset.optString("browser_download_url").trim()
            if (name.endsWith(".apk", ignoreCase = true) && url.startsWith("http")) {
                if (name.contains("alt-entry", ignoreCase = true)) return url
                if (fallback == null) fallback = url
            }
        }
        return fallback
    }

    private fun compareVersions(left: String, right: String): Int {
        val leftParts = versionParts(left)
        val rightParts = versionParts(right)
        val size = maxOf(leftParts.size, rightParts.size)
        for (index in 0 until size) {
            val l = leftParts.getOrElse(index) { 0L }
            val r = rightParts.getOrElse(index) { 0L }
            if (l != r) return l.compareTo(r)
        }
        return 0
    }

    private fun versionParts(value: String): List<Long> =
        Regex("\\d+").findAll(value).mapNotNull { it.value.toLongOrNull() }.toList()
}
