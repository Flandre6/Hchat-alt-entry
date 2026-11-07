package h.Hchat.hooks.items.linkparse

import org.junit.Assert.*
import org.junit.Test

class WorkVideoExtractorTest {
    @Test fun douyinRequiresMatchingVideoAndIgnoresGallery() {
        val id = "7392565659223248165"
        val video = """{"status_code":0,"aweme_detail":{"aweme_id":"$id","aweme_type":0,"video":{"play_addr":{"url_list":["https://v9.douyinvod.com/a.mp4"]}}}}"""
        assertEquals("https://v9.douyinvod.com/a.mp4", WorkVideoExtractor.douyin(video, id))
        assertNull(WorkVideoExtractor.douyin(video, "7457473400202726715"))
        assertNull(WorkVideoExtractor.douyin(video.replace("\"aweme_type\":0", "\"aweme_type\":68,\"images\":[{}]"), id))
        assertNull(WorkVideoExtractor.douyin(video.replace("v9.douyinvod.com", "v9.douyinvod.com.evil.example"), id))
    }

    @Test fun douyinRetainsOnlyTrustedAlternativesInOrder() {
        val id = "7392565659223248165"
        val body = """{"status_code":0,"aweme_detail":{"aweme_id":"$id","aweme_type":0,"video":{"play_addr":{"url_list":["https://a.douyinvod.com/one","https://evil.example/bad","https://b.douyinvod.com/two","https://a.douyinvod.com/one"]}}}}"""
        assertEquals(listOf("https://a.douyinvod.com/one", "https://b.douyinvod.com/two"), WorkVideoExtractor.douyinCandidates(body, id))
    }

    @Test fun xhsUsesOnlyVerifiedH265StreamsAndNeverFallsBackToWatermarkedH264() {
        val id = "6a9a9fe70000000011033fd1"
        val html = """<script>window.__INITIAL_STATE__ = {"unused":undefined,"noteData":{"data":{"noteData":{"noteId":"$id","type":"video","title":"undefined is text","video":{"consumer":{"originVideoKey":"/oversized/original.mp4"},"media":{"stream":{"h264":[{"masterUrl":"https://sns-video-v6.xhscdn.com/watermarked.mp4","streamType":259}],"h265":[{"masterUrl":"http://evil.example/untrusted.mp4","streamType":520},{"masterUrl":"http://sns-video-v6.xhscdn.com:80/insecure.mp4","streamType":520},{"masterUrl":"http://sns-video-v6.xhscdn.com/clean-one.mp4","streamType":520},{"masterUrl":"https://sns-video-v6.xhscdn.com/clean-two.mp4","streamType":520}]}}}}}}};</script>"""
        assertEquals(
            listOf("https://sns-video-v6.xhscdn.com/clean-one.mp4", "https://sns-video-v6.xhscdn.com/clean-two.mp4"),
            WorkVideoExtractor.xiaohongshuCandidates(html, id)
        )
        assertEquals("https://sns-video-v6.xhscdn.com/clean-one.mp4", WorkVideoExtractor.xiaohongshu(html, id))
        assertNull(WorkVideoExtractor.xiaohongshu(html, "6ad9e5320000000014012d15"))
        assertNull(WorkVideoExtractor.xiaohongshu(html.replace("\"type\":\"video\"", "\"type\":\"normal\""), id))
        assertNull(WorkVideoExtractor.xiaohongshu("<html>login</html>", id))

        val streamOnly = html.replace("\"consumer\":{\"originVideoKey\":\"/oversized/original.mp4\"},", "")
        assertEquals("https://sns-video-v6.xhscdn.com/clean-one.mp4", WorkVideoExtractor.xiaohongshu(streamOnly, id))
        val h264Only = """<script>window.__INITIAL_STATE__={"noteData":{"data":{"noteData":{"noteId":"$id","type":"video","video":{"consumer":{"originVideoKey":"/original/hash.mp4"},"media":{"stream":{"h264":[{"masterUrl":"https://sns-video-v6.xhscdn.com/watermarked.mp4","streamType":259}],"h265":[]}}}}}}};</script>"""
        assertNull(WorkVideoExtractor.xiaohongshu(h264Only, id))
    }
}
