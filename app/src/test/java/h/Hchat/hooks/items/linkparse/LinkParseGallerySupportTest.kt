package h.Hchat.hooks.items.linkparse

import org.junit.Assert.assertTrue
import org.junit.Test

class LinkParseGallerySupportTest {
    @Test fun enablesOnlyVerifiedRealDeviceGallerySenders() {
        assertTrue(LinkParseGallerySupport.isVerified(WorkLinkMatcher.Platform.XIAOHONGSHU))
        assertTrue(LinkParseGallerySupport.isVerified(WorkLinkMatcher.Platform.DOUYIN))
    }
}
