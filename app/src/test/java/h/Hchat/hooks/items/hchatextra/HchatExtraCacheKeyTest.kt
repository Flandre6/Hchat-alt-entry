package h.Hchat.hooks.items.hchatextra

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class HchatExtraCacheKeyTest {
    @Test
    fun longRuntimeIdentityUsesBoundedIdentityScopedKeys() {
        val runtimeKey = buildString {
            append("com.tencent.mm|8.0.80|3220|")
            repeat(12) { append("patch-metadata-$it/").append("x".repeat(40)).append('|') }
            append("stable-loader")
        }
        val oldKey = "${runtimeKey}_chatting_data_adapter"

        assertTrue(oldKey.toByteArray(Charsets.UTF_8).size > 255)

        val key = HchatExtraCacheKey.forLookup(runtimeKey, "chatting_data_adapter")!!
        assertTrue(key.toByteArray(Charsets.UTF_8).size < 256)
        assertEquals(key, HchatExtraCacheKey.forLookup(runtimeKey, "chatting_data_adapter"))
        assertNotEquals(key, HchatExtraCacheKey.forLookup("$runtimeKey|replacement", "chatting_data_adapter"))
        assertNotEquals(key, HchatExtraCacheKey.forLookup(runtimeKey, "other_lookup"))
    }

    @Test
    fun utf8IdentitiesAndActualPurposesStaySeparate() {
        val runtime = "com.tencent.mm|8.0.80|3220|0x28000050|" + "补丁".repeat(120) + "|patch-a|1700000000000|stable"
        val adapter = HchatExtraCacheKey.forLookup(runtime, "chatting_data_adapter")!!
        val first = HchatExtraCacheKey.forLookup(runtime, "lucky_money_scene_classes_0")!!
        val second = HchatExtraCacheKey.forLookup(runtime, "lucky_money_scene_classes_1")!!
        assertTrue((runtime + "_chatting_data_adapter").toByteArray(Charsets.UTF_8).size > 255)
        listOf(adapter, first, second).forEach { assertTrue(it.toByteArray(Charsets.UTF_8).size <= 255) }
        assertNotEquals(adapter, first)
        assertNotEquals(first, second)
        assertNotEquals(first, HchatExtraCacheKey.forLookup(runtime + "变", "lucky_money_scene_classes_0"))
    }

    @Test
    fun blankRuntimeIdentityDoesNotProduceReusableCacheKey() {
        assertNull(HchatExtraCacheKey.forLookup("", "chatting_data_adapter"))
        assertNull(HchatExtraCacheKey.forLookup("  ", "chatting_data_adapter"))
    }
}
