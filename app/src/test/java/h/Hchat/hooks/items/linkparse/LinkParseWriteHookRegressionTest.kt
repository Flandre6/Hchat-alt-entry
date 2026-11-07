package h.Hchat.hooks.items.linkparse

import android.content.Context
import h.Hchat.hooks.api.model.WeChatMessage
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class LinkParseWriteHookRegressionTest {
    class Native(val field_status: Int, val field_isSend: Int, val field_talker: String = "filehelper")
    class Host { fun write(id: Long, message: Native, notify: Boolean): Int = 1 }
    private fun msg(id: Long = 42) = WeChatMessage(id, 11, 1, 2, 1, System.currentTimeMillis(), "filehelper", "https://v.douyin.com/_WFxKTl9V78/", "", "", "", 0)

    @Test fun captureRequiresEnabledFeatureAndValidWriteArguments() {
        val native = Native(2, 1)
        assertNull(LinkParseWriteGate.capture(false, arrayOf<Any?>(42L, native, true)))
        assertNull(LinkParseWriteGate.capture(true, arrayOf<Any?>(0L, native, true)))
        assertNull(LinkParseWriteGate.capture(true, arrayOf<Any?>(42L, null, true)))
        assertEquals(42L, LinkParseWriteGate.capture(true, arrayOf<Any?>(42L, native, true))?.id)
    }

    @Test fun successfulWriteObservationKeepsHostResultAndClaimsDeduplicate() {
        val prefs = RuntimeEnvironment.getApplication().getSharedPreferences("link-hook-r2", Context.MODE_PRIVATE)
        prefs.edit().clear().commit()
        val claims = MessageClaims(prefs)
        val native = Native(2, 1)
        val capture = LinkParseWriteGate.capture(true, arrayOf<Any?>(42L, native, true))!!
        var accepted = 0
        var lookups = 0
        var observerErrors = 0
        val accept: (WeChatMessage) -> Unit = { if (claims.claim("filehelper:${it.msgId}", Long.MAX_VALUE, 1)) accepted++ }
        val hostResult = Host().write(42L, native, true)
        LinkParseWriteGate.observe(hostResult, capture, { id -> lookups++; msg(id) }, accept, { observerErrors++ })
        LinkParseWriteGate.observe(hostResult, capture, { id -> lookups++; msg(id) }, accept, { observerErrors++ })
        assertEquals(1, hostResult)
        assertEquals(2, lookups)
        assertEquals(1, accepted)
        assertEquals(0, observerErrors)
    }

    @Test fun disabledAndNonSuccessfulWritesDoNotQueryOrAccept() {
        val native = Native(2, 1)
        val disabled = LinkParseWriteGate.capture(false, arrayOf<Any?>(42L, native, true))
        val capture = LinkParseWriteGate.capture(true, arrayOf<Any?>(42L, native, true))
        var lookups = 0
        var accepted = 0
        val lookup: (Long) -> WeChatMessage? = { lookups++; msg(it) }
        val accept: (WeChatMessage) -> Unit = { accepted++ }
        LinkParseWriteGate.observe(1, disabled, lookup, accept, {})
        LinkParseWriteGate.observe(0, capture, lookup, accept, {})
        LinkParseWriteGate.observe(null, capture, lookup, accept, {})
        assertEquals(0, lookups)
        assertEquals(0, accepted)
        assertTrue("a successful database update remains independently eligible", LinkParseEntryPolicy.acceptDatabase(false, true, true, 2, 2))
    }

    @Test fun observerErrorsAreContainedAndHostExceptionsStillPropagate() {
        val capture = LinkParseWriteGate.capture(true, arrayOf<Any?>(42L, Native(2, 1), true))!!
        var errorCount = 0
        LinkParseWriteGate.observe(1, capture, { throw IllegalStateException("lookup") }, { fail("must not accept") }, { errorCount++ })
        assertEquals(1, errorCount)

        val error = IllegalStateException("host failure")
        try {
            val hostCall: () -> Int = { throw error }
            val result = hostCall()
            LinkParseWriteGate.observe(result, capture, { fail("lookup must not occur"); null }, { fail("must not accept") }, { fail("host exception is not observer error") })
            fail("host error must propagate")
        } catch (actual: IllegalStateException) {
            // The host method's own exception remains visible to its caller.
            assertSame(error, actual)
        }
    }
}
