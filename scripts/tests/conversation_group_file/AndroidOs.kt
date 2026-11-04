package android.os
class Bundle
class Looper { companion object { fun getMainLooper() = Looper() } }
class Handler(looper: Looper) {
    fun post(action: Runnable): Boolean {
        val interceptor = onPost
        if (interceptor != null && !interceptor()) return false
        synchronized(queue) { queue.add(action) }; return true
    }
    fun removeCallbacks(action: Runnable) { synchronized(queue) { queue.remove(action) } }
    companion object {
        @Volatile var onPost: (() -> Boolean)? = null
        private val queue = mutableListOf<Runnable>()
        fun drain() { while (true) { val task = synchronized(queue) { if (queue.isEmpty()) null else queue.removeAt(0) } ?: return; task.run() } }
    }
}
