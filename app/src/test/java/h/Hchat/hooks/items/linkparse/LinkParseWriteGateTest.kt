package h.Hchat.hooks.items.linkparse

import h.Hchat.hooks.api.model.WeChatMessage
import org.junit.Assert.*
import org.junit.Test

class LinkParseWriteGateTest {
    private class Native(val field_status: Int, val field_isSend: Int, val field_talker: String = "filehelper")
    private class Missing
    private fun message(id: Long = 12, status: Int = 2, send: Int = 1) =
        WeChatMessage(id, 0, 1, status, send, System.currentTimeMillis(), "filehelper", "https://v.douyin.com/abc123/", "", "", "", 0)
    private fun capture(id: Long, native: Any) = LinkParseWriteGate.Capture(id, native)

    @Test fun onlyConfirmedOwnRowIsForwardedAndMissingDataFailsClosed() {
        var count = 0
        val accept: (WeChatMessage) -> Unit = { count++ }
        LinkParseWriteGate.observe(1, capture(12, Native(2, 1)), { message() }, accept, {})
        assertEquals(1, count)
        LinkParseWriteGate.observe(1, capture(12, Native(5, 1)), { message() }, accept, {})
        LinkParseWriteGate.observe(1, capture(12, Native(2, 0)), { message() }, accept, {})
        LinkParseWriteGate.observe(1, capture(0, Native(2, 1)), { message() }, accept, {})
        LinkParseWriteGate.observe(1, capture(12, Missing()), { message() }, accept, {})
        LinkParseWriteGate.observe(1, capture(12, Native(2, 1, "")), { message() }, accept, {})
        LinkParseWriteGate.observe(1, capture(12, Native(2, 1, "another-chat")), { message() }, accept, {})
        LinkParseWriteGate.observe(1, capture(12, Native(2, 1)), { null }, accept, {})
        LinkParseWriteGate.observe(1, capture(12, Native(2, 1)), { message(id = 13) }, accept, {})
        LinkParseWriteGate.observe(1, capture(12, Native(2, 1)), { message(status = 5) }, accept, {})
        LinkParseWriteGate.observe(1, capture(12, Native(2, 1)), { message(send = 0) }, accept, {})
        LinkParseWriteGate.observe(0, capture(12, Native(2, 1)), { message() }, accept, {})
        LinkParseWriteGate.observe(null, capture(12, Native(2, 1)), { message() }, accept, {})
        assertEquals(1, count)
    }
}
