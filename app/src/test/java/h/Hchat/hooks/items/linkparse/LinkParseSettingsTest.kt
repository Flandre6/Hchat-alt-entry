package h.Hchat.hooks.items.linkparse

import h.Hchat.preferences.HchatStorage
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class LinkParseSettingsTest {
    @Test fun ownMessagesBypassWhitelistAndGroupMentionOnlyAppliesToWhitelistedOthers() {
        val context = RuntimeEnvironment.getApplication()
        val prefs = HchatStorage.preferences(context, LinkParseSettings.PREFS_NAME)
        prefs.edit().clear().commit()
        val settings = LinkParseSettings(context)
        val dy = WorkLinkMatcher.Platform.DOUYIN
        val friend = "friend-id"
        val group = "group-id@chatroom"
        assertFalse(settings.allows(dy, friend, false, false, false))
        prefs.edit().putBoolean(LinkParseSettings.KEY_ENABLE, true).putBoolean(LinkParseSettings.KEY_DOUYIN, true).commit()
        assertTrue(settings.allows(dy, "any-chat", true, true, false))
        assertFalse(settings.allows(dy, friend, false, false, false))
        settings.saveAllowedTalkers(setOf(friend, group))
        assertTrue(settings.allows(dy, friend, false, false, false))
        assertTrue(settings.allows(dy, group, true, false, false))
        prefs.edit().putBoolean(LinkParseSettings.KEY_GROUP_AT_ME_ONLY, true).commit()
        assertFalse(settings.allows(dy, group, true, false, false))
        assertTrue(settings.allows(dy, group, true, false, true))
        assertTrue(settings.allows(dy, friend, false, false, false))
        assertTrue(settings.allows(dy, group, true, true, false))
        assertFalse(settings.allows(WorkLinkMatcher.Platform.XIAOHONGSHU, friend, false, false, false))
        prefs.edit().clear().commit()
    }
}
