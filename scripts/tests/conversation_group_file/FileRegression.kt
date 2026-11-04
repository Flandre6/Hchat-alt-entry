package h.Hchat.hooks.items.conversationgroup
import android.app.Activity
import android.os.Handler
import android.content.Intent
import android.net.Uri
import h.Hchat.hooks.items.conversationtabs.ConversationTabsIconPicker
import h.Hchat.hooks.items.conversationtabs.ConversationTabsIconPickResult
import de.robv.android.xposed.XposedBridge

fun waitFor(predicate: () -> Boolean) {
    val deadline = System.nanoTime() + 5_000_000_000L
    while (!predicate() && System.nanoTime() < deadline) {
        Handler.drain()
        Thread.yield()
    }
    Handler.drain()
    check(predicate())
}

fun testLockOrdering() {
    // 结果交付锁与 Hook 锁反向交错：post 失败时不得在请求锁内调用释放回调。
    val gate = Any()
    val postEntered = java.util.concurrent.CountDownLatch(1)
    val permitPost = java.util.concurrent.CountDownLatch(1)
    val lockActivity = Activity()
    val lockReleased = java.util.concurrent.CountDownLatch(1)
    val lockRequest = ConversationGroupFileRequest.create(lockActivity, { _: String -> }) {
        synchronized(gate) { lockReleased.countDown() }
    }!!
    Handler.onPost = {
        postEntered.countDown()
        check(permitPost.await(5, java.util.concurrent.TimeUnit.SECONDS))
        false
    }
    val posting = Thread { lockRequest.deliver("first") }.apply { isDaemon = true; start() }
    check(postEntered.await(5, java.util.concurrent.TimeUnit.SECONDS))
    val competing = Thread {
        synchronized(gate) {
            permitPost.countDown()
            lockRequest.deliver("second")
        }
    }.apply { isDaemon = true; start() }
    posting.join(5000); competing.join(5000)
    Handler.onPost = null
    check(!posting.isAlive && !competing.isAlive && lockReleased.await(5, java.util.concurrent.TimeUnit.SECONDS))
    check(lockActivity.application.listeners.isEmpty())

}

fun main(args: Array<String>) {
    if (args.contains("--lock-only")) { testLockOrdering(); println("Lock ordering regression passed"); return }
    val activity = Activity()
    var delivered = 0
    fun launch(): Int {
        ConversationGroupSendPicker.launch(activity, "*/*", "test") { delivered++ }
        return activity.launched.last()
    }
    val first = launch()
    check(XposedBridge.callbacks.size == 1)
    XposedBridge.result(activity, first, Activity.RESULT_CANCELED)
    XposedBridge.result(activity, first, Activity.RESULT_CANCELED)
    Handler.drain()
    check(delivered == 1 && XposedBridge.callbacks.isEmpty() && activity.application.listeners.isEmpty())
    var picked: ConversationGroupSendPicker.PickedFile? = null
    ConversationGroupSendPicker.launch(activity, "*/*", "test") { picked = it }
    XposedBridge.result(activity, activity.launched.last(), Activity.RESULT_OK, Intent().apply { data = Uri("report.txt") })
    waitFor { picked != null }
    check(picked?.displayName == "report.txt" && XposedBridge.callbacks.isEmpty())
    val retired = launch()
    val oldHook = XposedBridge.callbacks.single()
    XposedBridge.failNextUnhook.set(true)
    XposedBridge.result(activity, retired, Activity.RESULT_CANCELED)
    Handler.drain()
    check(delivered == 2 && XposedBridge.callbacks.size == 1)
    val afterRetry = launch()
    check(XposedBridge.callbacks.size == 1)
    oldHook.afterHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam(
        activity, arrayOf(afterRetry, Activity.RESULT_CANCELED, null), false))
    Handler.drain()
    check(delivered == 2 && XposedBridge.callbacks.size == 1)
    XposedBridge.result(activity, afterRetry, Activity.RESULT_CANCELED)
    Handler.drain()
    check(delivered == 3 && XposedBridge.callbacks.isEmpty())
    val failed = launch()
    XposedBridge.result(Activity(), failed, Activity.RESULT_CANCELED, failure = true)
    Handler.drain()
    check(XposedBridge.callbacks.size == 1 && activity.application.listeners.size == 1)
    XposedBridge.result(activity, failed, Activity.RESULT_CANCELED, failure = true)
    Handler.drain()
    check(delivered == 3 && XposedBridge.callbacks.isEmpty() && activity.application.listeners.isEmpty())
    val broken = launch()
    XposedBridge.result(activity, broken, Activity.RESULT_OK, Intent().apply { failData = true })
    Handler.drain()
    check(delivered == 3 && XposedBridge.callbacks.isEmpty() && activity.application.listeners.isEmpty())
    val one = launch()
    val two = launch()
    XposedBridge.result(activity, one, Activity.RESULT_CANCELED)
    Handler.drain()
    check(delivered == 4 && XposedBridge.callbacks.size == 1)
    XposedBridge.result(activity, two, Activity.RESULT_CANCELED)
    Handler.drain()
    check(delivered == 5 && XposedBridge.callbacks.isEmpty())
    val parallelActivity = Activity()
    var parallelDelivered = 0
    val threads = List(8) { Thread {
        ConversationGroupSendPicker.launch(parallelActivity, "*/*", "test") { parallelDelivered++ }
    } }
    threads.forEach(Thread::start)
    threads.forEach { it.join(5000); check(!it.isAlive) }
    check(parallelActivity.launched.size == 8 && XposedBridge.callbacks.size == 1)
    parallelActivity.launched.toList().forEach { XposedBridge.result(parallelActivity, it, Activity.RESULT_CANCELED) }
    Handler.drain()
    check(parallelDelivered == 8 && XposedBridge.callbacks.isEmpty() && parallelActivity.application.listeners.isEmpty())
    launch()
    activity.destroy()
    Handler.drain()
    check(delivered == 5 && XposedBridge.callbacks.isEmpty() && activity.application.listeners.isEmpty())
    val document = Activity()
    var documentResults = 0
    ConversationGroupDocumentBridge.launchImport(document) { documentResults++ }
    val docCode = document.launched.last()
    XposedBridge.result(document, docCode, Activity.RESULT_CANCELED)
    XposedBridge.result(document, docCode, Activity.RESULT_CANCELED)
    Handler.drain()
    check(documentResults == 1 && XposedBridge.callbacks.isEmpty() && document.application.listeners.isEmpty())
    ConversationGroupDocumentBridge.launchExport(document, "{}") { documentResults++ }
    XposedBridge.result(document, document.launched.last(), Activity.RESULT_OK, Intent().apply { data = Uri("export.json") })
    waitFor { documentResults == 2 }
    check(XposedBridge.callbacks.isEmpty())
    ConversationGroupDocumentBridge.launchExport(document, "{}") { documentResults++ }
    XposedBridge.result(document, document.launched.last(), Activity.RESULT_CANCELED, failure = true)
    Handler.drain()
    check(documentResults == 2 && XposedBridge.callbacks.isEmpty() && document.application.listeners.isEmpty())
    ConversationGroupDocumentBridge.launchImport(document) { documentResults++ }
    XposedBridge.result(document, document.launched.last(), Activity.RESULT_OK, Intent().apply { failData = true })
    Handler.drain()
    check(documentResults == 2 && XposedBridge.callbacks.isEmpty() && document.application.listeners.isEmpty())
    ConversationGroupDocumentBridge.launchImport(document) { documentResults++ }
    document.destroy()
    check(documentResults == 2 && XposedBridge.callbacks.isEmpty() && document.application.listeners.isEmpty())
    val iconActivity = Activity()
    var iconResult: ConversationTabsIconPickResult? = null
    ConversationTabsIconPicker.launch(iconActivity, "tab") { iconResult = it }
    XposedBridge.result(iconActivity, iconActivity.launched.last(), Activity.RESULT_CANCELED, failure = true)
    Handler.drain()
    check(iconResult === ConversationTabsIconPickResult.CANCELLED)
    ConversationTabsIconPicker.cancelAll()
    check(XposedBridge.callbacks.isEmpty())
    testLockOrdering()

    // 一个请求正在释放时，另一个请求已登记但仍处于启动中。
    val overlap = Activity()
    var overlapDelivered = 0
    ConversationGroupSendPicker.launch(overlap, "*/*", "test") { overlapDelivered++ }
    XposedBridge.result(overlap, overlap.launched.last(), Activity.RESULT_CANCELED)
    val startEntered = java.util.concurrent.CountDownLatch(1)
    val allowStart = java.util.concurrent.CountDownLatch(1)
    val unregisterEntered = java.util.concurrent.CountDownLatch(1)
    overlap.application.onUnregister = { unregisterEntered.countDown() }
    overlap.beforeStart = {
        startEntered.countDown()
        check(allowStart.await(5, java.util.concurrent.TimeUnit.SECONDS))
    }
    val launching = Thread {
        ConversationGroupSendPicker.launch(overlap, "*/*", "test") { overlapDelivered++ }
    }.apply { isDaemon = true; start() }
    check(startEntered.await(5, java.util.concurrent.TimeUnit.SECONDS))
    val delivering = Thread { Handler.drain() }.apply { isDaemon = true; start() }
    check(unregisterEntered.await(5, java.util.concurrent.TimeUnit.SECONDS))
    allowStart.countDown()
    launching.join(5000); delivering.join(5000)
    check(!launching.isAlive && !delivering.isAlive && overlap.launched.size == 2)
    check(overlapDelivered == 1 && XposedBridge.callbacks.size == 1)
    overlap.beforeStart = null
    XposedBridge.result(overlap, overlap.launched.last(), Activity.RESULT_CANCELED)
    Handler.drain()
    check(overlapDelivered == 2 && XposedBridge.callbacks.isEmpty() && overlap.application.listeners.isEmpty())

    // 回调中重入启动下一次选择时，新请求的 Hook 必须保留。
    val reentrant = Activity()
    var reentered = false
    ConversationGroupSendPicker.launch(reentrant, "*/*", "test") {
        ConversationGroupSendPicker.launch(reentrant, "*/*", "test") { reentered = true }
    }
    XposedBridge.result(reentrant, reentrant.launched.last(), Activity.RESULT_CANCELED)
    Handler.drain()
    check(reentrant.launched.size == 2 && XposedBridge.callbacks.size == 1)
    XposedBridge.result(reentrant, reentrant.launched.last(), Activity.RESULT_CANCELED)
    Handler.drain()
    check(reentered && XposedBridge.callbacks.isEmpty())

    val other = Activity()
    var released = 0
    val discarded = mutableListOf<String>()
    val request = ConversationGroupFileRequest.create(other, { _: String -> error("canceled callback") }, { discarded.add(it) }) { released++ }!!
    request.deliver("queued")
    request.cancel()
    request.deliver("late")
    Handler.drain()
    check(discarded == listOf("queued", "late") && released == 1 && other.application.listeners.isEmpty())
    // 不能中断的任务取消后晚到，结果仍须回收。
    val startedLate = java.util.concurrent.CountDownLatch(1)
    val finishLate = java.util.concurrent.CountDownLatch(1)
    val discardedLate = java.util.concurrent.CountDownLatch(1)
    val late = ConversationGroupFileRequest.create(other, { _: String -> error("late callback") }, {
        check(it == "late-file"); discardedLate.countDown()
    }) {}!!
    late.execute(task = {
        startedLate.countDown()
        while (true) {
            try { finishLate.await(); break } catch (_: InterruptedException) { }
        }
        "late-file"
    }, failure = { throw it })
    check(startedLate.await(5, java.util.concurrent.TimeUnit.SECONDS))
    late.cancel()
    finishLate.countDown()
    check(discardedLate.await(5, java.util.concurrent.TimeUnit.SECONDS))
    check(other.application.listeners.isEmpty())

    // 队列饱和时 failure 回调本身抛错也必须释放请求槽位。
    val workerStarted = java.util.concurrent.CountDownLatch(1)
    val releaseWorker = java.util.concurrent.CountDownLatch(1)
    val worker = ConversationGroupFileRequest.create(other, { _: String -> }) {}!!
    worker.execute(task = {
        workerStarted.countDown()
        while (true) {
            try { releaseWorker.await(); break } catch (_: InterruptedException) { }
        }
        "done"
    }, failure = { throw it })
    check(workerStarted.await(5, java.util.concurrent.TimeUnit.SECONDS))
    worker.cancel()
    val queued = mutableListOf<ConversationGroupFileRequest<String>>()
    try {
        repeat(8) {
            val item = ConversationGroupFileRequest.create(other, { _: String -> }) {}!!
            queued.add(item)
            item.execute(task = { "queued" }, failure = { throw it })
        }
        var rejected = false
        val rejectedRequest = ConversationGroupFileRequest.create(other, { _: String -> }) {}!!
        rejectedRequest.execute(task = { "unexpected" }, failure = { error ->
            rejected = error is java.util.concurrent.RejectedExecutionException
            error("injected rejection handler failure")
        })
        check(rejected && rejectedRequest.canceled.get())
    } finally {
        queued.forEach { it.cancel() }
        releaseWorker.countDown()
    }

    val failureReleased = java.util.concurrent.CountDownLatch(1)
    val failedTask = ConversationGroupFileRequest.create(other, { _: String -> error("unexpected callback") }) { failureReleased.countDown() }!!
    failedTask.execute(task = { error("injected task failure") }, failure = { error("injected failure handler failure") })
    check(failureReleased.await(5, java.util.concurrent.TimeUnit.SECONDS))
    check(failedTask.canceled.get() && other.application.listeners.isEmpty())
    var success: String? = null
    val successful = ConversationGroupFileRequest.create(other, { value: String -> success = value }, { error("delivered value discarded") }) {}!!
    successful.deliver("ok")
    Handler.drain()
    check(success == "ok" && other.application.listeners.isEmpty())
    println("ConversationGroup file request and picker regression passed")
}
