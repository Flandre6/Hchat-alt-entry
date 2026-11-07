package h.Hchat.hooks.items.linkparse

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class WorkImageExtractorTest {
    @Test fun douyinUsesOnlyWatermarkFreeOriginalImageCandidatesInOrder() {
        val id = "7616399587141737704"
        val body = """{"status_code":0,"aweme_detail":{"aweme_id":"$id","aweme_type":68,"images":[
            {"download_url_list":["https://p3-pc-sign.douyinpic.com/a-watermarked.webp"],"url_list":["https://p3-pc-sign.douyinpic.com/a-q75.webp","https://p9-pc-sign.douyinpic.com/a-origin.webp"]},
            {"download_url_list":["https://p9-pc-sign.douyinpic.com/b-watermarked.webp"],"url_list":["https://p3-pc-sign.douyinpic.com/b-q75.webp"]}
        ]}}"""
        assertEquals(
            listOf(
                listOf("https://p3-pc-sign.douyinpic.com/a-q75.webp", "https://p9-pc-sign.douyinpic.com/a-origin.webp"),
                listOf("https://p3-pc-sign.douyinpic.com/b-q75.webp")
            ),
            WorkImageExtractor.douyin(body, id)?.map { it.candidates }
        )
        assertNull(WorkImageExtractor.douyin(body, "7392565659223248165"))
        assertFalse(WorkImageExtractor.allowedDouyinImage("https://douyinpic.com.evil.example/a.webp"))
        assertFalse(WorkImageExtractor.allowedDouyinImage("http://p3-pc-sign.douyinpic.com/a.webp"))
    }

    @Test fun douyinRejectsNonAlbumOversizedAndPartialGalleries() {
        val id = "7616399587141737704"
        val item = """{"download_url_list":["https://p3-pc-sign.douyinpic.com/a-watermarked.webp"],"url_list":["https://p3-pc-sign.douyinpic.com/a.webp"]}"""
        val oversized = (0..WorkImageExtractor.MAX_IMAGES).joinToString(",") { item }
        val tooMany = """{"status_code":0,"aweme_detail":{"aweme_id":"$id","aweme_type":68,"images":[$oversized]}}"""
        assertNull(WorkImageExtractor.douyin(tooMany, id))

        val missing = """{"status_code":0,"aweme_detail":{"aweme_id":"$id","aweme_type":68,"images":[{},$item]}}"""
        assertNull(WorkImageExtractor.douyin(missing, id))
        val video = tooMany.replace("\"aweme_type\":68", "\"aweme_type\":0")
        assertNull(WorkImageExtractor.douyin(video, id))
    }

    @Test fun xiaohongshuUsesOriginalFileIdUrlInOrder() {
        val id = "6ad9e5320000000014012d15"
        val images = (0..14).joinToString(",") { index ->
            """{"fileId":"oss-sg/notes_pre_post/image$index","url":"http://sns-webpic-qc.xhscdn.com/low$index!h5_1080jpg"}"""
        }
        val html = """<script>window.__INITIAL_STATE__={"noteData":{"data":{"noteData":{"noteId":"$id","type":"normal","imageList":[$images]}}}};</script>"""
        val resolved = WorkImageExtractor.xiaohongshu(html, id)!!
        assertEquals(15, resolved.size)
        assertEquals(
            "https://sns-img-qc.xhscdn.com/oss-sg/notes_pre_post/image0?imageView2/2/w/1920/format/jpg",
            resolved.first().candidates.single()
        )
        assertTrue(resolved.all { WorkImageExtractor.allowedXhsImage(it.candidates.single()) })
        assertNull(WorkImageExtractor.xiaohongshu(html, "6a9a9fe70000000011033fd1"))
        assertNull(WorkImageExtractor.xiaohongshu(html.replace("\"type\":\"normal\"", "\"type\":\"video\""), id))
    }

    @Test fun xiaohongshuRejectsMissingIdsAndOversizedGalleries() {
        val id = "6ad9e5320000000014012d15"
        val image = """{"fileId":"oss-sg/notes_pre_post/image"}"""
        val oversized = (0..WorkImageExtractor.MAX_IMAGES).joinToString(",") { image }
        val html = """<script>window.__INITIAL_STATE__={"noteData":{"data":{"noteData":{"noteId":"$id","type":"normal","imageList":[$oversized]}}}};</script>"""
        assertNull(WorkImageExtractor.xiaohongshu(html, id))
        assertNull(WorkImageExtractor.xiaohongshu(html.replace("oss-sg/notes_pre_post/image", "../evil"), id))
    }
}
