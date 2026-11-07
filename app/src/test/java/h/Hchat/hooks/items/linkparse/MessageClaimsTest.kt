package h.Hchat.hooks.items.linkparse

import android.content.Context
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class MessageClaimsTest {
    @Test fun claimsPersistAcrossInstancesAndBoundTheActiveSet() {
        val prefs = RuntimeEnvironment.getApplication().getSharedPreferences("link-claims-test", Context.MODE_PRIVATE)
        prefs.edit().clear().commit()
        val a = MessageClaims(prefs, 2)
        assertTrue(a.claim("one", 200, 100))
        assertFalse(MessageClaims(prefs, 2).claim("one", 200, 100))
        assertTrue(a.claim("two", 200, 100))
        assertFalse(a.claim("three", 200, 100))
        assertTrue(MessageClaims(prefs, 2).claim("three", 300, 201))
    }
    @Test fun malformedHistoryFailsClosed() {
        val prefs = RuntimeEnvironment.getApplication().getSharedPreferences("link-claims-bad-test", Context.MODE_PRIVATE)
        prefs.edit().putString("recent", "not-json").commit()
        assertFalse(MessageClaims(prefs).claim("one", 200, 100))
    }
}
