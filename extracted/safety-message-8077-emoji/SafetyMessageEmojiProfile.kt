package h.Hchat.hooks.items.specialmessage.extracted

/** 表情安全消息所需的宿主混淆类名和方法名。 */
data class SafetyMessageEmojiProfile(
    val sourceOwner: String,
    val emojiScene: String
) {
    companion object {
        fun forVersion(name: String?, code: Long): SafetyMessageEmojiProfile? = when {
            name == "8.0.76" && code == 3140L -> SafetyMessageEmojiProfile(
                sourceOwner = "lq1.d1",
                emojiScene = "o22.y"
            )
            name == "8.0.77" && code == 3160L -> SafetyMessageEmojiProfile(
                sourceOwner = "ft1.d1",
                emojiScene = "k52.y"
            )
            else -> null
        }
    }
}
