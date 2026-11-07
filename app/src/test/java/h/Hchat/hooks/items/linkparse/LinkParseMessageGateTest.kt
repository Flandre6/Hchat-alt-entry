package h.Hchat.hooks.items.linkparse

import h.Hchat.hooks.api.model.WeChatMessage
import org.junit.Assert.*
import org.junit.Test

class LinkParseMessageGateTest {
    private val now = 1_770_000_000_000L
    private fun msg(type: Int = 1, id: Long = 4, time: Long = now, text: String = "https://v.douyin.com/bg9DH--cvOc/") =
        WeChatMessage(id, 0, type, 0, 1, time, "filehelper", text, "", "", "", 0)

    @Test fun onlyFreshTextWithStableIdentityIsEligible() {
        assertEquals(now, LinkParseMessageGate.eligibleTime(msg(), now))
        assertEquals(now, LinkParseMessageGate.eligibleTime(msg(time = now / 1000), now))
        assertNull(LinkParseMessageGate.eligibleTime(msg(time = now - 30_000), now))
        assertNull(LinkParseMessageGate.eligibleTime(msg(time = now + 5_001), now))
        assertNull(LinkParseMessageGate.eligibleTime(msg(id = 0), now))
        assertNull(LinkParseMessageGate.eligibleTime(msg(type = 3), now))
        assertNull(LinkParseMessageGate.eligibleTime(msg(type = 49), now))
        assertNull(LinkParseMessageGate.eligibleTime(msg(text = "x".repeat(16_385)), now))
    }
}
