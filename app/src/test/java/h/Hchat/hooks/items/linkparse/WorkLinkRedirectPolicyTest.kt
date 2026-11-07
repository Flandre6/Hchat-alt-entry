package h.Hchat.hooks.items.linkparse

import okhttp3.HttpUrl.Companion.toHttpUrl
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class WorkLinkRedirectPolicyTest {
    private val douyinId = "7616399587141737704"

    @Test fun acceptsOnlyPlatformOwnedRedirectHostsAndKeepsDouyinWorkId() {
        val direct = "https://www.douyin.com/note/$douyinId".toHttpUrl()
        val slides = "https://www.iesdouyin.com/share/slides/$douyinId/".toHttpUrl()
        val intermediate = "https://www.douyin.com/?modal_id=$douyinId".toHttpUrl()
        val unknown = "https://www.douyin.com/".toHttpUrl()
        val foreign = "https://evil.example/?modal_id=$douyinId".toHttpUrl()

        assertTrue(WorkLinkRedirectPolicy.allows(direct, WorkLinkMatcher.Platform.DOUYIN))
        assertTrue(WorkLinkRedirectPolicy.allows(slides, WorkLinkMatcher.Platform.DOUYIN))
        assertTrue(WorkLinkRedirectPolicy.allows(intermediate, WorkLinkMatcher.Platform.DOUYIN))
        assertFalse(WorkLinkRedirectPolicy.allows(foreign, WorkLinkMatcher.Platform.DOUYIN))
        assertEquals(douyinId, WorkLinkRedirectPolicy.workId(direct, WorkLinkMatcher.Platform.DOUYIN))
        assertEquals(douyinId, WorkLinkRedirectPolicy.workId(slides, WorkLinkMatcher.Platform.DOUYIN))
        assertEquals(douyinId, WorkLinkRedirectPolicy.workId(intermediate, WorkLinkMatcher.Platform.DOUYIN))
        assertNull(WorkLinkRedirectPolicy.workId(unknown, WorkLinkMatcher.Platform.DOUYIN))
    }

    @Test fun rejectsCredentialedAndNonStandardPortRedirects() {
        assertFalse(WorkLinkRedirectPolicy.allows("https://www.douyin.com:8443/video/$douyinId".toHttpUrl(), WorkLinkMatcher.Platform.DOUYIN))
        assertFalse(WorkLinkRedirectPolicy.allows("https://user@www.douyin.com/video/$douyinId".toHttpUrl(), WorkLinkMatcher.Platform.DOUYIN))
    }
}
