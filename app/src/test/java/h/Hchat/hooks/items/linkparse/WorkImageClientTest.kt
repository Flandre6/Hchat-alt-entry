package h.Hchat.hooks.items.linkparse

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class WorkImageClientTest {
    @Test fun distinguishesJpegAndWebpSignaturesWithoutConversion() {
        assertEquals(
            WorkImageFormat.JPEG,
            WorkImageFormatSniffer.detect(byteArrayOf(0xff.toByte(), 0xd8.toByte(), 0xff.toByte(), 0xe0.toByte()))
        )
        assertEquals(
            WorkImageFormat.WEBP,
            WorkImageFormatSniffer.detect("RIFF1234WEBPVP8 ".toByteArray(Charsets.US_ASCII))
        )
        assertNull(WorkImageFormatSniffer.detect("RIFF1234WAVE".toByteArray(Charsets.US_ASCII)))
        assertNull(WorkImageFormatSniffer.detect(byteArrayOf(0xff.toByte(), 0xd8.toByte())))
    }
}
