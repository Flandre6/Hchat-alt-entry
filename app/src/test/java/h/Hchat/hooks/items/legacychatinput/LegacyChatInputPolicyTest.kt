package h.Hchat.hooks.items.legacychatinput

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LegacyChatInputPolicyTest {
    @Test
    fun restoreSwitchDefaultsOff() {
        assertFalse(LegacyChatInputSettings.DEFAULT_ENABLE)
        assertFalse(
            LegacyChatInputPolicy.shouldForceLegacy(
                enabled = LegacyChatInputSettings.DEFAULT_ENABLE,
                versionCode = LegacyChatInputPolicy.TARGET_VERSION_CODE,
                configClassName = LegacyChatInputPolicy.INPUT_BAR_CONFIG_CLASS_NAME
            )
        )
    }

    @Test
    fun onlyEnabledTargetVersionAndInputBarConfigAreOverridden() {
        assertTrue(
            LegacyChatInputPolicy.shouldForceLegacy(
                enabled = true,
                versionCode = LegacyChatInputPolicy.TARGET_VERSION_CODE,
                configClassName = LegacyChatInputPolicy.INPUT_BAR_CONFIG_CLASS_NAME
            )
        )
        assertFalse(
            LegacyChatInputPolicy.shouldForceLegacy(
                enabled = false,
                versionCode = LegacyChatInputPolicy.TARGET_VERSION_CODE,
                configClassName = LegacyChatInputPolicy.INPUT_BAR_CONFIG_CLASS_NAME
            )
        )
        assertFalse(
            LegacyChatInputPolicy.shouldForceLegacy(
                enabled = true,
                versionCode = LegacyChatInputPolicy.TARGET_VERSION_CODE - 20,
                configClassName = LegacyChatInputPolicy.INPUT_BAR_CONFIG_CLASS_NAME
            )
        )
        assertFalse(
            LegacyChatInputPolicy.shouldForceLegacy(
                enabled = true,
                versionCode = LegacyChatInputPolicy.TARGET_VERSION_CODE,
                configClassName = "com.tencent.mm.repairer.config.chatting.RepairerConfigUseNewVoice2TxtComponent"
            )
        )
    }
}
