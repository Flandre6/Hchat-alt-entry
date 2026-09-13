package h.Hchat.hooks.items.securemessage

/** 已从目标微信版本确认的表情 MsgSource 与 sendemoji 分发链路。 */
internal data class EmojiSecureMessageProfile(
    val sourceOwner: String,
    val emojiScene: String
) {
    companion object {
        fun forVersion(name: String?, code: Long): EmojiSecureMessageProfile? = when {
            name == "8.0.76" && code == 3140L -> EmojiSecureMessageProfile(
                sourceOwner = "lq1.d1",
                emojiScene = "o22.y"
            )
            name == "8.0.77" && code == 3160L -> EmojiSecureMessageProfile(
                sourceOwner = "ft1.d1",
                emojiScene = "k52.y"
            )
            else -> null
        }
    }
}
