package de.robv.android.xposed
import android.app.Activity
import android.content.Intent
open class XC_MethodHook {
    open fun afterHookedMethod(param: MethodHookParam) {}
    class MethodHookParam(val thisObject: Any, val args: Array<Any?>, private val failure: Boolean) { fun hasThrowable() = failure }
    class Unhook(private val hook: XC_MethodHook) { fun unhook() { if (XposedBridge.failNextUnhook.compareAndSet(true, false)) { error("injected unhook failure") }; XposedBridge.callbacks.remove(hook) } }
}
object XposedBridge {
    val callbacks = java.util.concurrent.CopyOnWriteArrayList<XC_MethodHook>()
    val failNextUnhook = java.util.concurrent.atomic.AtomicBoolean(false)
    fun hookAllMethods(clazz: Class<*>, name: String, callback: XC_MethodHook): Set<XC_MethodHook.Unhook> {
        if (clazz != Activity::class.java) return emptySet()
        callbacks.add(callback)
        return setOf(XC_MethodHook.Unhook(callback))
    }
    fun result(activity: Activity, code: Int, result: Int, data: Intent? = null, failure: Boolean = false) {
        val param = XC_MethodHook.MethodHookParam(activity, arrayOf(code, result, data), failure)
        callbacks.toList().forEach { it.afterHookedMethod(param) }
    }
}
