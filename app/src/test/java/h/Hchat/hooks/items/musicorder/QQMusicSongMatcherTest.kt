package h.Hchat.hooks.items.musicorder

import org.junit.Assert.assertEquals
import org.junit.Test

class QQMusicSongMatcherTest {
    @Test fun exactTitleBeatsEarlierLiveVersion() {
        assertEquals(1, QQMusicSongMatcher.selectIndex("晴天", listOf("晴天 Live", "晴天")))
    }

    @Test fun versionSuffixMatchesBaseTitle() {
        assertEquals(1, QQMusicSongMatcher.selectIndex("晴天", listOf("关于晴天", "晴天（Live版）")))
    }

    @Test fun prefixMatchIsUsedWhenThereIsNoExactTitle() {
        assertEquals(1, QQMusicSongMatcher.selectIndex("晴天", listOf("今天天气晴天", "晴天的故事")))
    }

    @Test fun containsMatchIsUsedAfterPrefixMatches() {
        assertEquals(0, QQMusicSongMatcher.selectIndex("晴天", listOf("今天晴天", "睛天")))
    }

    @Test fun caseAndWhitespaceDifferencesAreIgnored() {
        assertEquals(1, QQMusicSongMatcher.selectIndex("  My   Song ", listOf("My Song Live", "MYsong")))
    }

    @Test fun punctuationAndParenthesizedVersionAreIgnoredForBaseMatch() {
        assertEquals(1, QQMusicSongMatcher.selectIndex("Love Story", listOf("Love Story (Taylor's Version)", "love-story [Live]")))
    }

    @Test fun reasonableFuzzyMatchIsUsedAndTiesKeepQqOrder() {
        assertEquals(1, QQMusicSongMatcher.selectIndex("yesterday", listOf("yesterda", "yesterdays")))
    }

    @Test fun noReliableMatchFallsBackToOriginalQqOrder() {
        assertEquals(0, QQMusicSongMatcher.selectIndex("completely unrelated", listOf("Song A", "Song B")))
    }
}
