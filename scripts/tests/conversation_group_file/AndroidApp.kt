package android.app

import android.content.Context
import android.os.Bundle

open class Application : Context() {
    val listeners: MutableSet<ActivityLifecycleCallbacks> = java.util.concurrent.ConcurrentHashMap.newKeySet()
    fun registerActivityLifecycleCallbacks(callback: ActivityLifecycleCallbacks) { listeners.add(callback) }
    @Volatile var onUnregister: (() -> Unit)? = null
    fun unregisterActivityLifecycleCallbacks(callback: ActivityLifecycleCallbacks) { listeners.remove(callback); onUnregister?.invoke() }
    interface ActivityLifecycleCallbacks {
        fun onActivityCreated(activity: Activity, state: Bundle?)
        fun onActivityStarted(activity: Activity)
        fun onActivityResumed(activity: Activity)
        fun onActivityPaused(activity: Activity)
        fun onActivityStopped(activity: Activity)
        fun onActivitySaveInstanceState(activity: Activity, state: Bundle)
        fun onActivityDestroyed(activity: Activity)
    }
}
open class Activity(val application: Application = Application()) : Context() {
    @Volatile var isDestroyed = false
    @Volatile var isFinishing = false
    override val applicationContext: Context get() = application
    val launched = java.util.concurrent.CopyOnWriteArrayList<Int>()
    @Volatile var beforeStart: (() -> Unit)? = null
    fun startActivityForResult(intent: android.content.Intent, code: Int) { beforeStart?.invoke(); launched.add(code) }
    open fun onActivityResult(code: Int, result: Int, data: android.content.Intent?) {}
    fun destroy() { isDestroyed = true; application.listeners.toList().forEach { it.onActivityDestroyed(this) } }
    companion object { const val RESULT_CANCELED = 0; const val RESULT_OK = -1 }
}
