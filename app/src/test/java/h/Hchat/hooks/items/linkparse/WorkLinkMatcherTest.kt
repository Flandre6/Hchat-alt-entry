package h.Hchat.hooks.items.linkparse

import org.junit.Assert.*
import org.junit.Test

class WorkLinkMatcherTest {
    @Test fun findsWorkLinksInShareTextAndPreservesParameters() {
        val match = WorkLinkMatcher.find("看看笔记 https://xhslink.com/o/6A6YKVkqAP2?x=1，复制查看")!!
        assertEquals(WorkLinkMatcher.Platform.XIAOHONGSHU, match.platform)
        assertEquals("https://xhslink.com/o/6A6YKVkqAP2?x=1", match.url)
        assertTrue(match.requiresRedirect)
        assertEquals(WorkLinkMatcher.Platform.DOUYIN, WorkLinkMatcher.find("https://www.douyin.com/note/7616399587141737704")?.platform)
        assertEquals(WorkLinkMatcher.Platform.DOUYIN, WorkLinkMatcher.find("https://www.douyin.com/video/7616399587141737704")?.platform)
    }

    @Test fun recognizesEscapedSchemeFromXhsShareText() {
        val text = "喜多川海梦 可惜这个景已经绝版了 出镜@配角一奈 https\\://xhslink.cn/o/9dORNEMXaP2 复制内容后去小红书"
        val match = WorkLinkMatcher.find(text)!!
        assertEquals(WorkLinkMatcher.Platform.XIAOHONGSHU, match.platform)
        assertEquals("https://xhslink.cn/o/9dORNEMXaP2", match.url)
        assertTrue(match.requiresRedirect)
        assertEquals("https://xhslink.cn/o/9dORNEMXaP2", WorkLinkMatcher.parse("https\\://xhslink.cn/o/9dORNEMXaP2")?.url)
    }

    @Test fun findsFullDouyinShareText() {
        val text = "4.35 不必奔赴动漫世界，因为喜欢的风景，已经住进了我的屏幕 # 动漫壁纸 # 动态壁纸 # 二次元 # 手机壁纸 # 动漫美图 https://v.douyin.com/bg9DH--cvOc/ 复制此链接，打开抖音搜索，直接观看视频！ ChB:/ :2pm 03/22 K@W.Zz"
        val match = WorkLinkMatcher.find(text)!!
        assertEquals(WorkLinkMatcher.Platform.DOUYIN, match.platform)
        assertEquals("https://v.douyin.com/bg9DH--cvOc/", match.url)
    }

    @Test fun ignoresNonWorkAndConfusableHosts() {
        listOf(
            "https://example.com/video/1234567890123456789",
            "https://www.douyin.com/user/1234567890123456789",
            "https://www.xiaohongshu.com/user/profile/6ad9e5320000000014012d15",
            "https://xhslink.com/m/abcdef",
            "https://douyin.com.evil.example/video/7616399587141737704",
            "https://www.douyin.com@evil.example/video/7616399587141737704",
            "https://www.douyin.com:8443/video/7616399587141737704",
            "https://www.douyin.com/video/%37%36%31%36%33%39%39%35%38%37%31%34%31%37%33%37%37%30%34"
        ).forEach { assertNull(it, WorkLinkMatcher.find(it)) }
    }

    @Test fun recognizesDirectWorksButNotTheirHomepages() {
        assertFalse(WorkLinkMatcher.find("https://www.xiaohongshu.com/discovery/item/6ad9e5320000000014012d15?xsec_token=sample")!!.requiresRedirect)
        assertTrue(WorkLinkMatcher.find("https://v.douyin.com/abc123/")!!.requiresRedirect)
    }
}
