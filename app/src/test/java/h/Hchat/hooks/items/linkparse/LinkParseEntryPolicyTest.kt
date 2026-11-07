package h.Hchat.hooks.items.linkparse

import org.junit.Assert.*
import org.junit.Test

class LinkParseEntryPolicyTest {
    @Test fun ownMessageRequiresExplicitSuccessfulMutation() {
        assertFalse(LinkParseEntryPolicy.acceptDatabase(true, false, true, 1, 1))
        assertFalse(LinkParseEntryPolicy.acceptDatabase(false, true, true, 5, 5))
        assertFalse(LinkParseEntryPolicy.acceptDatabase(false, true, true, 2, null))
        assertFalse(LinkParseEntryPolicy.acceptDatabase(false, true, true, 1, 2))
        assertTrue(LinkParseEntryPolicy.acceptDatabase(false, true, true, 2, 2))
        assertTrue(LinkParseEntryPolicy.acceptDatabase(true, false, true, 2, 2))
    }
    @Test fun incomingIsHandledOnInsertOnly() {
        assertTrue(LinkParseEntryPolicy.acceptDatabase(true, false, false, 0, null))
        assertFalse(LinkParseEntryPolicy.acceptDatabase(false, true, false, 2, 2))
    }
}
