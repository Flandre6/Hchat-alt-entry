package h.Hchat.ui.miuix

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ClipData
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Color as AndroidColor
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Bundle
import android.os.Looper
import android.os.Parcelable
import android.os.SystemClock
import android.provider.OpenableColumns
import android.provider.MediaStore
import android.provider.Settings
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.WebView
import android.widget.DatePicker
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.MediaController
import android.widget.TimePicker
import android.widget.Toast
import android.widget.VideoView
import h.Hchat.BuildConfig
import h.Hchat.R
import h.Hchat.crash.CrashReportRuntime
import h.Hchat.crash.CrashReportSettings
import h.Hchat.crash.CrashReportSettingsProvider
import h.Hchat.hooks.api.ui.HchatAgentIconDrawable
import h.Hchat.hooks.items.floatingshortcut.FloatingShortcutFeature
import h.Hchat.hooks.items.floatingshortcut.FloatingShortcutGlyph
import h.Hchat.hooks.items.floatingshortcut.FloatingShortcutGlyphDrawable
import h.Hchat.hooks.items.floatingshortcut.FloatingShortcutGlyphs
import h.Hchat.hooks.items.floatingshortcut.FloatingShortcutIconPickResult
import h.Hchat.hooks.items.floatingshortcut.FloatingShortcutIconPicker
import h.Hchat.hooks.items.floatingshortcut.FloatingShortcutIconStore
import h.Hchat.hooks.items.floatingshortcut.FloatingShortcutItem
import h.Hchat.hooks.items.floatingshortcut.FloatingShortcutRuntime
import h.Hchat.hooks.items.floatingshortcut.FloatingShortcutSettings
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigationevent.NavigationEventDispatcher
import androidx.navigationevent.NavigationEventDispatcherOwner
import androidx.navigationevent.compose.LocalNavigationEventDispatcherOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import h.Hchat.hooks.api.core.WeChatApis
import h.Hchat.hooks.api.media.WeChatFavoriteItem
import h.Hchat.hooks.api.message.WeChatMessageObserveApi
import h.Hchat.hooks.api.model.ContactLabelBean
import h.Hchat.hooks.api.model.WeChatContact
import h.Hchat.hooks.items.audiotransform.AudioTransformFeature
import h.Hchat.hooks.items.audiotransform.AudioTransformCancelledException
import h.Hchat.hooks.items.audiotransform.AudioTransformSegmenter
import h.Hchat.hooks.items.audiotransform.AudioTransformSettings
import h.Hchat.hooks.items.antirecall.AntiRecallFeature
import h.Hchat.hooks.items.antirecall.AntiRecallSettings
import h.Hchat.hooks.items.autoreply.AutoReplyFeature
import h.Hchat.hooks.items.autoreply.AutoReplyRule
import h.Hchat.hooks.items.autoreply.AutoReplyRuntime
import h.Hchat.hooks.items.autoreply.AutoReplySettings
import h.Hchat.hooks.items.autoreply.AutoReplyStep
import h.Hchat.hooks.items.autoreply.XiaozhiMcpStatus
import h.Hchat.hooks.items.autoreply.AutoReplyXiaozhiConfig
import h.Hchat.hooks.items.autoreply.AutoReplyZhiliaConfig
import h.Hchat.hooks.items.autoreply.XiaozhiAgentOption
import h.Hchat.hooks.items.autoreply.XiaozhiConsoleApi
import h.Hchat.hooks.items.autoreply.XiaozhiModelOption
import h.Hchat.hooks.items.autoreply.XiaozhiVoiceOption
import h.Hchat.hooks.items.autooriginal.AutoOriginalImageFeature
import h.Hchat.hooks.items.autooriginal.AutoOriginalImageSettings
import h.Hchat.hooks.items.autovieworiginal.AutoViewOriginalFeature
import h.Hchat.hooks.items.autovieworiginal.AutoViewOriginalSettings
import h.Hchat.hooks.items.transparentavatar.UploadTransparentAvatarFeature
import h.Hchat.hooks.items.transparentavatar.UploadTransparentAvatarSettings
import h.Hchat.hooks.items.atallnotify.AtAllNotificationBlockFeature
import h.Hchat.hooks.items.atallnotify.AtAllNotificationBlockSettings
import h.Hchat.hooks.items.backgroundbeauty.BackgroundBeautyFeature
import h.Hchat.hooks.items.backgroundbeauty.BackgroundBeautyPickResult
import h.Hchat.hooks.items.backgroundbeauty.BackgroundBeautyPicker
import h.Hchat.hooks.items.backgroundbeauty.BackgroundBeautySettings
import h.Hchat.hooks.items.backgroundbeauty.BackgroundBeautyStore
import h.Hchat.hooks.items.automessageforward.AutoMessageForwardFeature
import h.Hchat.hooks.items.automessageforward.AutoMessageForwardRule
import h.Hchat.hooks.items.automessageforward.AutoMessageForwardSettings
import h.Hchat.hooks.items.callmedialimit.CallMediaLimitFeature
import h.Hchat.hooks.items.callmedialimit.CallMediaLimitSettings
import h.Hchat.hooks.items.callmedialimit.CallRingtoneBlockFeature
import h.Hchat.hooks.items.chattime.ChatTimeStyleFeature
import h.Hchat.hooks.items.chattime.ChatTimeStyleSettings
import h.Hchat.hooks.items.conversationgroup.ConversationGroup
import h.Hchat.hooks.items.conversationgroup.ConversationGroupFeature
import h.Hchat.hooks.items.conversationgroup.ConversationGroupPickerSupport
import h.Hchat.hooks.items.conversationgroup.ConversationGroupRuntime
import h.Hchat.hooks.items.conversationgroup.ConversationGroupStore
import h.Hchat.hooks.items.custombottombar.CustomBottomBarFeature
import h.Hchat.hooks.items.custombottombar.FloatingBottomBarSettings
import h.Hchat.hooks.items.customnotify.CustomNotificationFeature
import h.Hchat.hooks.items.customnotify.CustomNotificationRule
import h.Hchat.hooks.items.customnotify.CustomNotificationRuntime
import h.Hchat.hooks.items.customfriendavatar.CustomFriendAvatarFeature
import h.Hchat.hooks.items.customfriendavatar.CustomFriendAvatarPicker
import h.Hchat.hooks.items.customfriendavatar.CustomFriendAvatarSettings
import h.Hchat.hooks.items.customfriendavatar.CustomFriendAvatarStore
import h.Hchat.hooks.items.customnotify.CustomNotificationSettings
import h.Hchat.hooks.items.editmsg.EditMessageFeature
import h.Hchat.hooks.items.editmsg.EditMessageSettings
import h.Hchat.hooks.items.emojisave.EmojiSaveFeature
import h.Hchat.hooks.items.emojisave.EmojiSaveSettings
import h.Hchat.hooks.items.fakelocation.FakeLocationFeature
import h.Hchat.hooks.items.fakelocation.FakeLocationSettings
import h.Hchat.hooks.items.fakescancamera.FakeScanCameraFeature
import h.Hchat.hooks.items.fakescancamera.FakeScanCameraSettings
import h.Hchat.hooks.items.fakevoiceduration.FakeVoiceDurationFeature
import h.Hchat.hooks.items.fakevoiceduration.FakeVoiceDurationSettings
import h.Hchat.hooks.items.forwardlimit.RemoveForwardLimitFeature
import h.Hchat.hooks.items.forwardlimit.RemoveForwardLimitSettings
import h.Hchat.hooks.items.gameemoji.GameEmojiFeature
import h.Hchat.hooks.items.gameemoji.GameEmojiSettings
import h.Hchat.hooks.items.groupleave.GroupLeaveMonitorFeature
import h.Hchat.hooks.items.groupleave.GroupLeaveReplyTemplate
import h.Hchat.hooks.items.groupleave.GroupLeaveReplyTemplateBinding
import h.Hchat.hooks.items.groupleave.GroupLeaveMonitorSettings
import h.Hchat.hooks.items.grouplabel.GroupChatLabel
import h.Hchat.hooks.items.grouplabel.GroupChatLabelFeature
import h.Hchat.hooks.items.grouplabel.GroupChatLabelStore
import h.Hchat.hooks.items.grouplabel.QuickGroupChatLabelFeature
import h.Hchat.hooks.items.grouplabel.QuickGroupChatLabelSettings
import h.Hchat.hooks.items.groupnicknamecolor.GroupNicknameColorSettings
import h.Hchat.hooks.items.groupnicknamecolor.GroupNicknameColorStore
import h.Hchat.hooks.items.grouprename.GroupRenameMonitorFeature
import h.Hchat.hooks.items.grouprename.GroupRenameReplyTemplate
import h.Hchat.hooks.items.grouprename.GroupRenameMonitorSettings
import h.Hchat.hooks.items.grouprename.GroupRenameTemplateBinding
import h.Hchat.hooks.items.hideavatar.HideChatAvatarFeature
import h.Hchat.hooks.items.hideavatar.HideChatAvatarSettings
import h.Hchat.hooks.items.hidemenu.HideChatMenuFeature
import h.Hchat.hooks.items.hidemenu.HideChatMenuSettings
import h.Hchat.hooks.items.hotupdate.DisableHotUpdateFeature
import h.Hchat.hooks.items.hotupdate.DisableHotUpdateSettings
import h.Hchat.hooks.items.hometextcolor.HomeTextColorFeature
import h.Hchat.hooks.items.hometextcolor.HomeTextColorSettings
import h.Hchat.hooks.items.inputhint.InputHintFeature
import h.Hchat.hooks.items.inputhint.InputHintSettings
import h.Hchat.hooks.items.inputhint.InputHintStats
import h.Hchat.hooks.items.inputhint.OutgoingMessageStatsRepository
import h.Hchat.hooks.items.keepalive.WeChatKeepAliveFeature
import h.Hchat.hooks.items.keepalive.WeChatKeepAliveRuntime
import h.Hchat.hooks.items.keepalive.WeChatKeepAliveSettings
import h.Hchat.hooks.items.keywordnotify.KeywordNotificationFeature
import h.Hchat.hooks.items.keywordnotify.KeywordNotificationRuntime
import h.Hchat.hooks.items.keywordnotify.KeywordNotificationSettings
import h.Hchat.hooks.items.keywordnotify.KeywordRule
import h.Hchat.hooks.items.zombiecheck.ZombieCheckController
import h.Hchat.hooks.items.zombiecheck.ZombieCheckActionResult
import h.Hchat.hooks.items.zombiecheck.ZombieCheckFeature
import h.Hchat.hooks.items.zombiecheck.ZombieCheckResultType
import h.Hchat.hooks.items.zombiecheck.ZombieCheckSettings
import h.Hchat.hooks.items.membertitle.MemberTitleFeature
import h.Hchat.hooks.items.membertitle.MemberTitleSettings
import h.Hchat.hooks.items.membertitle.MemberTitleStore
import h.Hchat.hooks.items.messageaffix.MessageAffixFeature
import h.Hchat.hooks.items.messageaffix.MessageAffixSettings
import h.Hchat.hooks.items.messagebubble.MessageBubbleFeature
import h.Hchat.hooks.items.messagebubble.MessageBubblePickResult
import h.Hchat.hooks.items.messagebubble.MessageBubblePicker
import h.Hchat.hooks.items.messagebubble.MessageBubbleSettings
import h.Hchat.hooks.items.messagebubble.MessageBubbleSlot
import h.Hchat.hooks.items.messagebubble.MessageBubbleStore
import h.Hchat.hooks.items.messagebubble.MessageBubbleKind
import h.Hchat.hooks.items.messagetextcolor.MessageTextColorFeature
import h.Hchat.hooks.items.messagetextcolor.MessageTextColorSettings
import h.Hchat.hooks.items.messageblock.MessageBlockBinding
import h.Hchat.hooks.items.messageblock.MessageBlockDefaultRule
import h.Hchat.hooks.items.messageblock.MessageBlockFeature
import h.Hchat.hooks.items.messageblock.MessageBlockSettings
import h.Hchat.hooks.items.messageblock.MessageBlockTemplate
import h.Hchat.hooks.items.messageforward.MessageForwardFeature
import h.Hchat.hooks.items.messageforward.MessageForwardSettings
import h.Hchat.hooks.items.miniprogrambaselib.FakeMiniProgramBaseLibFeature
import h.Hchat.hooks.items.miniprogrambaselib.FakeMiniProgramBaseLibSettings
import h.Hchat.hooks.items.miniprogramsplashad.SkipGlobalMiniProgramSplashAdsFeature
import h.Hchat.hooks.items.miniprogramsplashad.SkipGlobalMiniProgramSplashAdsSettings
import h.Hchat.hooks.items.miniprogramvideoad.SkipMiniProgramVideoAdsFeature
import h.Hchat.hooks.items.miniprogramvideoad.SkipMiniProgramVideoAdsSettings
import h.Hchat.hooks.items.musicorder.QQMusicOrderFeature
import h.Hchat.hooks.items.musicorder.QQMusicOrderSettings
import h.Hchat.hooks.items.moments.OriginalMomentsUploadFeature
import h.Hchat.hooks.items.moments.OriginalMomentsUploadSettings
import h.Hchat.hooks.items.moments.MomentsAutoCommentFeature
import h.Hchat.hooks.items.moments.MomentsAutoCommentSettings
import h.Hchat.hooks.items.moments.MomentsAutoLikeFeature
import h.Hchat.hooks.items.moments.MomentsAutoLikeSettings
import h.Hchat.hooks.items.moments.MomentsAutoForwardFeature
import h.Hchat.hooks.items.moments.MomentsAutoForwardSettings
import h.Hchat.hooks.items.moments.MomentsAutoRefreshFeature
import h.Hchat.hooks.items.moments.MomentsAutoRefreshSettings
import h.Hchat.hooks.items.moments.MomentsBottomDetailFeature
import h.Hchat.hooks.items.moments.MomentsBottomDetailSettings
import h.Hchat.hooks.items.moments.MomentsContactFilterFeature
import h.Hchat.hooks.items.moments.MomentsContactFilterSettings
import h.Hchat.hooks.items.moments.MomentsKeywordBlockFeature
import h.Hchat.hooks.items.moments.MomentsKeywordBlockSettings
import h.Hchat.hooks.items.moments.MomentsPostNotificationFeature
import h.Hchat.hooks.items.moments.MomentsPostNotificationSettings
import h.Hchat.hooks.items.moments.MomentsUploadTailFeature
import h.Hchat.hooks.items.moments.MomentsUploadTailSettings
import h.Hchat.hooks.items.moments.RemoveMomentsAdsFeature
import h.Hchat.hooks.items.moments.RemoveMomentsAdsSettings
import h.Hchat.hooks.items.moments.SnsAntiRecallFeature
import h.Hchat.hooks.items.moments.SnsAntiRecallSettings
import h.Hchat.hooks.items.momentsfake.MomentsFakeCommentSettingsProvider
import h.Hchat.hooks.items.momentsfake.MomentsFakeForwardRuntimeRegistry
import h.Hchat.hooks.items.momentsfake.MomentsFakeForwardSettingsProvider
import h.Hchat.hooks.items.momentsfake.MomentsFakeLikeSettingsProvider
import h.Hchat.hooks.items.momentsfake.MomentsFakeLikeCandidateRepository
import h.Hchat.hooks.items.momentsfake.MomentsFakeInteractionRuntimeRegistry
import h.Hchat.hooks.items.momentsfake.MomentsFakeInteractionSettings
import h.Hchat.hooks.items.multirecall.MultiRecallFeature
import h.Hchat.hooks.items.multirecall.MultiRecallSettings
import h.Hchat.hooks.items.payment.core.AutoRedPacketFeature
import h.Hchat.hooks.items.payment.core.PaymentTemplateTimeFormatter
import h.Hchat.hooks.items.payment.core.RedPacketReplyStep
import h.Hchat.hooks.items.payment.core.RedPacketRuleBinding
import h.Hchat.hooks.items.payment.core.RedPacketRuleConfig
import h.Hchat.hooks.items.payment.core.RedPacketRuleTemplate
import h.Hchat.hooks.items.payment.core.RedPacketSettings
import h.Hchat.hooks.items.payment.fakebalance.FakeWalletBalanceFeature
import h.Hchat.hooks.items.payment.fakebalance.FakeWalletBalanceSettings
import h.Hchat.hooks.items.payment.transfer.AutoTransferFeature
import h.Hchat.hooks.items.payment.transfer.AutoTransferSettings
import h.Hchat.hooks.items.payment.transfer.TransferReceiveAccountStore
import h.Hchat.hooks.items.payment.transfer.TransferRuleBinding
import h.Hchat.hooks.items.payment.transfer.TransferRuleConfig
import h.Hchat.hooks.items.payment.transfer.TransferRuleTemplate
import h.Hchat.hooks.items.patblock.PatBlockFeature
import h.Hchat.hooks.items.patblock.PatBlockSettings
import h.Hchat.hooks.items.profileid.ProfileIdFeature
import h.Hchat.hooks.items.profileid.ProfileIdSettings
import h.Hchat.hooks.items.protobuf.ProtobufPacketFeature
import h.Hchat.hooks.items.protobuf.ProtobufPacketRuntime
import h.Hchat.hooks.items.protobuf.ProtobufPacketSettings
import h.Hchat.hooks.items.quoteclear.QuoteDeleteClearFeature
import h.Hchat.hooks.items.quoteclear.QuoteDeleteClearSettings
import h.Hchat.hooks.items.quickcontactedit.QuickContactEditFeature
import h.Hchat.hooks.items.quickcontactedit.QuickContactEditSettings
import h.Hchat.hooks.items.quickmoments.QuickMomentsFeature
import h.Hchat.hooks.items.quickmoments.QuickMomentsSettings
import h.Hchat.hooks.items.quickread.QuickMarkReadFeature
import h.Hchat.hooks.items.quickread.QuickMarkReadSettings
import h.Hchat.hooks.items.quickterminate.QuickTerminateFeature
import h.Hchat.hooks.items.quickterminate.QuickTerminateSettings
import h.Hchat.hooks.items.realtail.RealNameTailFeature
import h.Hchat.hooks.items.realtail.RealNameTailSettings
import h.Hchat.hooks.items.realtail.RealNameTailStore
import h.Hchat.hooks.items.roundavatar.RoundAvatarFeature
import h.Hchat.hooks.items.roundavatar.RoundAvatarSettings
import h.Hchat.hooks.items.script.ScriptPluginFeature
import h.Hchat.hooks.items.script.ScriptPluginManager
import h.Hchat.hooks.items.script.ScriptPluginRuntime
import h.Hchat.hooks.items.script.ScriptPluginSettings
import h.Hchat.hooks.items.script.market.PluginMarketHistoryVersion
import h.Hchat.hooks.items.script.market.PluginMarketInstaller
import h.Hchat.hooks.items.script.market.PluginMarketException
import h.Hchat.hooks.items.script.market.PluginMarketFile
import h.Hchat.hooks.items.script.market.PluginMarketComment
import h.Hchat.hooks.items.script.market.PluginMarketNotification
import h.Hchat.hooks.items.script.market.PluginMarketPlugin
import h.Hchat.hooks.items.script.market.PluginMarketRepository
import h.Hchat.hooks.items.script.market.PluginMarketReviewStatus
import h.Hchat.hooks.items.script.market.PluginMarketUserIdentity
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentClient
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentAttachment
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentChatMessage
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentConfig
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentCancellation
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentContext
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentDiff
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentDraft
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentEventIds
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentLocalFiles
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentMcpServer
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentRequest
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentResumeState
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentSession
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentSessionStore
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentSettings
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentProfile
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentQuotedMessage
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentSpeech
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentTextMerge
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentToolEvent
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentToolResultStore
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentValidator
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentWorkspaceChange
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentWorkspaceToolConfirmation
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentWorkspaceWriteDecision
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentWorkspaceTools
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentWriter
import h.Hchat.hooks.items.scheduledtask.ScheduledTaskContentItem
import h.Hchat.hooks.items.scheduledtask.ScheduledTaskFeature
import h.Hchat.hooks.items.scheduledtask.ScheduledTaskItem
import h.Hchat.hooks.items.scheduledtask.ScheduledTaskRuntimeCoordinator
import h.Hchat.hooks.items.scheduledtask.ScheduledTaskSettings
import h.Hchat.hooks.items.selectedmessages.SelectedMessageSnapshot
import h.Hchat.hooks.items.selectedmessages.SelectedMessagesFeature
import h.Hchat.hooks.items.securemessage.SecureMessageSettings
import h.Hchat.hooks.items.selectedmessages.SelectedMessagesRuntimeCoordinator
import h.Hchat.hooks.items.selectedmessages.SelectedMessagesSettings
import h.Hchat.hooks.items.settings.PluginAgentEntryProvider
import h.Hchat.hooks.items.settings.SettingsEntrySettings
import h.Hchat.hooks.items.settings.SettingsFeature
import h.Hchat.hooks.items.shortvideo.FinderMediaDownloadFeature
import h.Hchat.hooks.items.shortvideo.FinderMediaDownloadSettings
import h.Hchat.hooks.items.statuslimit.StatusTextLimitFeature
import h.Hchat.hooks.items.statuslimit.StatusTextLimitSettings
import h.Hchat.hooks.items.swipequote.SwipeQuoteFeature
import h.Hchat.hooks.items.swipequote.SwipeQuoteSettings
import h.Hchat.hooks.items.tablet.WeChatTabletFeature
import h.Hchat.hooks.items.tablet.WeChatTabletSettings
import h.Hchat.hooks.items.textspeech.TextSpeechFeature
import h.Hchat.hooks.items.textspeech.TextSpeechEngineCatalog
import h.Hchat.hooks.items.textspeech.TextSpeechSettings
import h.Hchat.hooks.items.textspeech.TextSpeechVoiceCatalog
import h.Hchat.hooks.items.textvoice.TextVoiceFeature
import h.Hchat.hooks.items.textvoice.TextVoiceSettings
import h.Hchat.hooks.items.typingreport.TypingReportBlockFeature
import h.Hchat.hooks.items.typingreport.TypingReportBlockSettings
import h.Hchat.hooks.items.voiceforward.VoiceForwardFeature
import h.Hchat.hooks.items.voiceforward.VoiceForwardSettings
import h.Hchat.hooks.items.hchatextra.GroupMemberHistorySettingsProvider
import h.Hchat.hooks.items.hchatextra.MessageDetailsSettingsProvider
import h.Hchat.hooks.items.hchatextra.RedPacketDetailsSettingsProvider
import h.Hchat.hooks.items.hchatextra.SkipWebRiskSettingsProvider
import h.Hchat.hooks.items.hchatextra.HchatExtraSettings
import h.Hchat.hooks.items.voicepreview.VoicePreviewFeature
import h.Hchat.hooks.items.voicepreview.VoicePreviewSettings
import h.Hchat.media.AudioTransformBridge
import h.Hchat.preferences.HchatStorage
import h.Hchat.preferences.TermsGate
import h.Hchat.ui.FeatureSettingsProvider
import h.Hchat.ui.UIRegistry
import h.Hchat.utils.KavaReflector
import h.Hchat.utils.KeywordReplacementRule
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import org.json.JSONArray
import org.json.JSONObject
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Checkbox
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.ScrollBehavior
import top.yukonga.miuix.kmp.basic.Slider
import top.yukonga.miuix.kmp.extra.WindowDropdown
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.Switch
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.blur.Backdrop
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.blur.rememberLayerBackdrop
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.theme.darkColorScheme
import top.yukonga.miuix.kmp.theme.lightColorScheme
import top.yukonga.miuix.kmp.extra.WindowDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import java.io.File
import java.lang.ref.WeakReference
import java.net.URL
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Calendar
import java.util.LinkedHashSet
import java.util.Locale
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicReference
import kotlin.math.abs
import kotlin.random.Random

internal enum class ZombieCheckSelection {
    TARGETS,
    EXCLUDED,
    DELETE
}

internal data class ZombieCheckDeleteTarget(val wxid: String, val name: String)

internal sealed class ZombieCheckRoute {
    object Main : ZombieCheckRoute()
    data class ContactPicker(
        val selection: ZombieCheckSelection,
        val request: ContactPickerRequest
    ) : ZombieCheckRoute()
}

@Composable
internal fun ZombieCheckMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val settings = remember { ZombieCheckSettings(context) }
    var route by remember { mutableStateOf<ZombieCheckRoute>(ZombieCheckRoute.Main) }
    var targetIds by remember { mutableStateOf(settings.targetIds()) }
    var excludedIds by remember { mutableStateOf(settings.excludedIds()) }
    var pendingDeleteTargets by remember { mutableStateOf(emptyList<ZombieCheckDeleteTarget>()) }

    SettingsRouteTransition(
        targetState = route,
        label = "ZombieCheckRoute",
        depthOf = { if (it is ZombieCheckRoute.Main) 0 else 1 }
    ) { currentRoute ->
        when (currentRoute) {
            ZombieCheckRoute.Main -> ZombieCheckMainPage(
                context = context,
                provider = provider,
                targetIds = targetIds,
                excludedIds = excludedIds,
                pendingDeleteTargets = pendingDeleteTargets,
                onBack = onBack,
                onPickTargets = {
                    route = ZombieCheckRoute.ContactPicker(
                        ZombieCheckSelection.TARGETS,
                        ContactPickerRequest(
                            title = "选择检测好友",
                            mode = ContactPickerMode.FRIENDS,
                            multiSelect = true,
                            existingValue = targetIds.joinToString("|"),
                            onValue = {},
                            enableLabels = true
                        )
                    )
                },
                onPickExcluded = {
                    route = ZombieCheckRoute.ContactPicker(
                        ZombieCheckSelection.EXCLUDED,
                        ContactPickerRequest(
                            title = "选择排除好友",
                            mode = ContactPickerMode.FRIENDS,
                            multiSelect = true,
                            existingValue = excludedIds.joinToString("|"),
                            onValue = {},
                            enableLabels = true
                        )
                    )
                },
                onPickDelete = {
                    val detectedIds = ZombieCheckController.snapshot().results
                        .filter { it.type == ZombieCheckResultType.DEAD }
                        .map { it.wxid }
                        .toSet()
                    route = ZombieCheckRoute.ContactPicker(
                        ZombieCheckSelection.DELETE,
                        ContactPickerRequest(
                            title = "批量删除好友",
                            mode = ContactPickerMode.FRIENDS,
                            multiSelect = true,
                            existingValue = detectedIds.joinToString("|"),
                            onValue = {},
                            enableLabels = true
                        )
                    )
                },
                onDismissDelete = { pendingDeleteTargets = emptyList() },
                onConfirmDelete = { clearRecord, delaySeconds ->
                    val result = ZombieCheckController.deleteFriends(
                        pendingDeleteTargets.map { it.wxid },
                        clearRecord,
                        delaySeconds
                    )
                    pendingDeleteTargets = emptyList()
                    result
                }
            )
            is ZombieCheckRoute.ContactPicker -> ContactPickerPage(
                context = context,
                request = currentRoute.request,
                onBack = { route = ZombieCheckRoute.Main },
                onConfirm = { picked ->
                    val ids = picked.map { it.id }.filter { it.isNotBlank() }.toSet()
                    when (currentRoute.selection) {
                        ZombieCheckSelection.TARGETS -> {
                            targetIds = ids
                            settings.saveTargetIds(ids)
                        }
                        ZombieCheckSelection.EXCLUDED -> {
                            excludedIds = ids
                            settings.saveExcludedIds(ids)
                        }
                        ZombieCheckSelection.DELETE -> {
                            pendingDeleteTargets = picked.map {
                                ZombieCheckDeleteTarget(it.id, it.label.ifBlank { it.id })
                            }
                        }
                    }
                    route = ZombieCheckRoute.Main
                }
            )
        }
    }
}

@Composable
internal fun ZombieCheckMainPage(
    context: Context,
    provider: FeatureSettingsProvider,
    targetIds: Set<String>,
    excludedIds: Set<String>,
    pendingDeleteTargets: List<ZombieCheckDeleteTarget>,
    onBack: () -> Unit,
    onPickTargets: () -> Unit,
    onPickExcluded: () -> Unit,
    onPickDelete: () -> Unit,
    onDismissDelete: () -> Unit,
    onConfirmDelete: (Boolean, Int) -> ZombieCheckActionResult
) {
    val sp = remember { HchatStorage.preferences(context, ZombieCheckSettings.PREFS_NAME) }
    var snapshot by remember { mutableStateOf(ZombieCheckController.snapshot()) }
    var enabled by remember { mutableStateOf(sp.getBoolean(ZombieCheckSettings.KEY_ENABLE, ZombieCheckSettings.DEFAULT_ENABLE)) }
    var autoTag by remember { mutableStateOf(sp.getBoolean(ZombieCheckSettings.KEY_AUTO_TAG, ZombieCheckSettings.DEFAULT_AUTO_TAG)) }
    var labelName by remember { mutableStateOf(sp.getString(ZombieCheckSettings.KEY_LABEL_NAME, ZombieCheckSettings.DEFAULT_LABEL_NAME).orEmpty()) }
    var autoDelete by remember { mutableStateOf(sp.getBoolean(ZombieCheckSettings.KEY_AUTO_DELETE, ZombieCheckSettings.DEFAULT_AUTO_DELETE)) }
    var clearRecord by remember { mutableStateOf(sp.getBoolean(ZombieCheckSettings.KEY_CLEAR_RECORD, ZombieCheckSettings.DEFAULT_CLEAR_RECORD)) }
    var keepAwake by remember { mutableStateOf(sp.getBoolean(ZombieCheckSettings.KEY_KEEP_AWAKE, ZombieCheckSettings.DEFAULT_KEEP_AWAKE)) }
    var minDelay by remember { mutableStateOf(sp.getInt(ZombieCheckSettings.KEY_MIN_DELAY_SECONDS, ZombieCheckSettings.DEFAULT_MIN_DELAY_SECONDS).toString()) }
    var maxDelay by remember { mutableStateOf(sp.getInt(ZombieCheckSettings.KEY_MAX_DELAY_SECONDS, ZombieCheckSettings.DEFAULT_MAX_DELAY_SECONDS).toString()) }
    var timeout by remember { mutableStateOf(sp.getInt(ZombieCheckSettings.KEY_TIMEOUT_SECONDS, ZombieCheckSettings.DEFAULT_TIMEOUT_SECONDS).toString()) }
    var retries by remember { mutableStateOf(sp.getInt(ZombieCheckSettings.KEY_MAX_RETRIES, ZombieCheckSettings.DEFAULT_MAX_RETRIES).toString()) }
    var deleteDelay by remember { mutableStateOf(sp.getInt(ZombieCheckSettings.KEY_DELETE_DELAY_SECONDS, ZombieCheckSettings.DEFAULT_DELETE_DELAY_SECONDS).toString()) }
    var confirmAutoDelete by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()

    LaunchedEffect(Unit) {
        while (true) {
            snapshot = ZombieCheckController.snapshot()
            delay(500L)
        }
    }

    fun saveInt(key: String, value: String, range: IntRange) {
        value.toIntOrNull()?.coerceIn(range.first, range.last)?.let {
            sp.edit().putInt(key, it).apply()
        }
    }

    fun toastResult(result: ZombieCheckActionResult) {
        Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
        snapshot = ZombieCheckController.snapshot()
    }

    val notableResults = snapshot.results.filter { it.type != ZombieCheckResultType.NORMAL }.asReversed()

    PageScaffold(
        title = provider.title(),
        largeTitle = provider.title(),
        scrollBehavior = scrollBehavior,
        bottomBar = { BottomActionBar("返回", onBack) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(text = "检测") }
            item {
                SettingsCard {
                    SwitchRow(
                        checked = enabled,
                        title = "启用僵尸粉检测",
                        summary = "使用微信转账下单接口核验好友关系，不会确认付款",
                        onCheckedChange = {
                            enabled = it
                            sp.edit().putBoolean(ZombieCheckSettings.KEY_ENABLE, it).apply()
                            if (!it && (snapshot.running || snapshot.deleting)) {
                                toastResult(ZombieCheckController.pause())
                            }
                        }
                    )
                    InsetDivider()
                    InfoRow("状态", if (snapshot.ready) snapshot.status else "检测接口定位中")
                    InsetDivider()
                    InfoRow(
                        "进度",
                        "已检测 ${snapshot.checkedCount}/${snapshot.totalCount}，异常 ${snapshot.deadCount}，失败 ${snapshot.unknownCount}"
                    )
                    if (snapshot.deleting || snapshot.deleteTotalCount > 0) {
                        InsetDivider()
                        InfoRow(
                            "删除进度",
                            "已处理 ${snapshot.deleteCompletedCount}/${snapshot.deleteTotalCount}，已提交 ${snapshot.deleteSuccessCount}，失败 ${snapshot.deleteFailureCount}"
                        )
                    }
                    if (snapshot.currentName.isNotBlank()) {
                        InsetDivider()
                        InfoRow("当前好友", snapshot.currentName)
                    }
                    InsetDivider()
                    ActionRow("检测范围", if (targetIds.isEmpty()) "全部好友" else "已选择 ${targetIds.size} 位好友", onPickTargets)
                    InsetDivider()
                    ActionRow("排除好友", if (excludedIds.isEmpty()) "未排除" else "已排除 ${excludedIds.size} 位好友", onPickExcluded)
                    InsetDivider()
                    ActionRow(
                        if (snapshot.running || snapshot.deleting) "停止当前任务" else "开始 / 继续检测",
                        when {
                            snapshot.deleting -> "停止后不再删除剩余好友"
                            snapshot.running -> "暂停后保留当前队列"
                            else -> "按当前范围继续未完成进度"
                        }
                    ) {
                        toastResult(
                            if (snapshot.running || snapshot.deleting) {
                                ZombieCheckController.pause()
                            } else {
                                ZombieCheckController.start()
                            }
                        )
                    }
                    InsetDivider()
                    ActionRow("重置检测进度", "清空断点、结果和运行日志") {
                        toastResult(ZombieCheckController.reset())
                    }
                }
            }

            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "异常处理") }
            item {
                SettingsCard {
                    SwitchRow(
                        checked = autoTag,
                        title = "自动追加标签",
                        summary = "保留好友现有标签后追加指定标签",
                        onCheckedChange = {
                            autoTag = it
                            sp.edit().putBoolean(ZombieCheckSettings.KEY_AUTO_TAG, it).apply()
                        }
                    )
                    if (autoTag) {
                        InsetDivider()
                        InputRow("标签名称", "检测到好友关系异常时追加", labelName) {
                            labelName = it
                            sp.edit().putString(ZombieCheckSettings.KEY_LABEL_NAME, it.trim()).apply()
                        }
                    }
                    InsetDivider()
                    SwitchRow(
                        checked = autoDelete,
                        title = "自动删除异常好友",
                        summary = "高风险操作，默认关闭；暂停检测会取消尚未执行的删除",
                        onCheckedChange = {
                            if (it) {
                                confirmAutoDelete = true
                            } else {
                                autoDelete = false
                                sp.edit().putBoolean(ZombieCheckSettings.KEY_AUTO_DELETE, false).apply()
                            }
                        }
                    )
                    InsetDivider()
                    SwitchRow(
                        checked = clearRecord,
                        title = "同时清理聊天记录",
                        summary = "同时用于自动删除和批量删除",
                        onCheckedChange = {
                            clearRecord = it
                            sp.edit().putBoolean(ZombieCheckSettings.KEY_CLEAR_RECORD, it).apply()
                        }
                    )
                    InsetDivider()
                    NumberInputRow("删除间隔", "自动删除和批量删除的等待秒数，0-300", deleteDelay) {
                        deleteDelay = it
                        saveInt(ZombieCheckSettings.KEY_DELETE_DELAY_SECONDS, it, 0..300)
                    }
                    InsetDivider()
                    ActionRow(
                        "批量删除好友",
                        when {
                            snapshot.deleting -> "正在处理 ${snapshot.deleteCompletedCount}/${snapshot.deleteTotalCount}"
                            snapshot.deadCount > 0 -> "可按标签筛选，默认选中 ${snapshot.deadCount} 位异常好友"
                            else -> "从好友或微信标签中多选后删除"
                        }
                    ) {
                        if (snapshot.running || snapshot.deleting) {
                            Toast.makeText(context, "请先停止当前任务", Toast.LENGTH_SHORT).show()
                        } else {
                            onPickDelete()
                        }
                    }
                    InsetDivider()
                    SwitchRow(
                        checked = keepAwake,
                        title = "检测期间保持 CPU 运行",
                        summary = "使用 WakeLock，长时间检测会增加耗电",
                        onCheckedChange = {
                            keepAwake = it
                            sp.edit().putBoolean(ZombieCheckSettings.KEY_KEEP_AWAKE, it).apply()
                        }
                    )
                }
            }

            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "请求参数") }
            item {
                SettingsCard {
                    NumberInputRow("最小间隔", "单位秒，0-60", minDelay) {
                        minDelay = it
                        saveInt(ZombieCheckSettings.KEY_MIN_DELAY_SECONDS, it, 0..60)
                    }
                    InsetDivider()
                    NumberInputRow("最大间隔", "单位秒，0-120", maxDelay) {
                        maxDelay = it
                        saveInt(ZombieCheckSettings.KEY_MAX_DELAY_SECONDS, it, 0..120)
                    }
                    InsetDivider()
                    NumberInputRow("请求超时", "单位秒，5-60", timeout) {
                        timeout = it
                        saveInt(ZombieCheckSettings.KEY_TIMEOUT_SECONDS, it, 5..60)
                    }
                    InsetDivider()
                    NumberInputRow("超时重试", "每位好友最多重试 0-5 次", retries) {
                        retries = it
                        saveInt(ZombieCheckSettings.KEY_MAX_RETRIES, it, 0..5)
                    }
                }
            }

            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "异常结果 · ${notableResults.size}") }
            item {
                SettingsCard {
                    if (notableResults.isEmpty()) {
                        InfoRow("暂无异常结果", "正常好友不会逐项显示")
                    } else {
                        notableResults.take(50).forEachIndexed { index, result ->
                            if (index > 0) InsetDivider()
                            val resultType = if (result.type == ZombieCheckResultType.DEAD) "好友关系异常" else "检测失败"
                            val resultSummary = if (result.message.isBlank()) resultType else "$resultType · ${result.message}"
                            ZombieCheckResultRow(
                                result.name,
                                resultSummary
                            )
                        }
                    }
                }
            }

            if (snapshot.logs.isNotEmpty()) {
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "运行日志") }
                item {
                    SettingsCard {
                        snapshot.logs.takeLast(12).asReversed().forEachIndexed { index, log ->
                            if (index > 0) InsetDivider()
                            InfoRow("记录", log)
                        }
                    }
                }
            }
        }
    }

    if (confirmAutoDelete) {
        WindowDialog(
            show = true,
            title = "启用自动删除",
            onDismissRequest = { confirmAutoDelete = false },
            content = {
                Column {
                    Text(
                        text = "检测依据来自微信支付接口返回文案。支付风控或服务端文案变化可能产生检测失败，请先小范围验证结果。",
                        color = MiuixTheme.colorScheme.onSurface,
                        fontSize = 14.sp
                    )
                    TextButton(
                        text = "确认启用",
                        onClick = {
                            autoDelete = true
                            sp.edit().putBoolean(ZombieCheckSettings.KEY_AUTO_DELETE, true).apply()
                            confirmAutoDelete = false
                        },
                        modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                        colors = ButtonDefaults.textButtonColorsPrimary()
                    )
                    TextButton(
                        text = "取消",
                        onClick = { confirmAutoDelete = false },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        colors = ButtonDefaults.textButtonColorsPrimary()
                    )
                }
            }
        )
    }

    if (pendingDeleteTargets.isNotEmpty()) {
        WindowDialog(
            show = true,
            title = "确认批量删除",
            onDismissRequest = onDismissDelete,
            content = {
                Column {
                    Text(
                        text = buildString {
                            append("将删除已选的 ${pendingDeleteTargets.size} 位好友")
                            if (clearRecord) append("，并同时清理聊天记录")
                            append("。此操作不可撤销。")
                            val names = pendingDeleteTargets.take(6).joinToString("、") { it.name }
                            if (names.isNotBlank()) append("\n\n$names")
                            if (pendingDeleteTargets.size > 6) append(" 等")
                        },
                        color = MiuixTheme.colorScheme.onSurface,
                        fontSize = 14.sp
                    )
                    TextButton(
                        text = "确认删除",
                        onClick = {
                            val result = onConfirmDelete(
                                clearRecord,
                                deleteDelay.toIntOrNull()?.coerceIn(0, 300)
                                    ?: ZombieCheckSettings.DEFAULT_DELETE_DELAY_SECONDS
                            )
                            toastResult(result)
                        },
                        modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                        colors = ButtonDefaults.textButtonColorsPrimary()
                    )
                    TextButton(
                        text = "取消",
                        onClick = onDismissDelete,
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        colors = ButtonDefaults.textButtonColorsPrimary()
                    )
                }
            }
        )
    }
}

@Composable
internal fun ZombieCheckResultRow(name: String, summary: String) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 15.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = name,
            modifier = Modifier.fillMaxWidth(),
            color = MiuixTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = summary,
            modifier = Modifier.fillMaxWidth(),
            color = MiuixTheme.colorScheme.onSurfaceVariantSummary
        )
    }
}

internal sealed class QQMusicOrderRoute {
    object Main : QQMusicOrderRoute()
    data class ContactPicker(val request: ContactPickerRequest) : QQMusicOrderRoute()
}

@Composable
internal fun QQMusicOrderMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val settings = remember { QQMusicOrderSettings(context) }
    val sp = remember { HchatStorage.preferences(context, QQMusicOrderSettings.PREFS_NAME) }
    var route by remember { mutableStateOf<QQMusicOrderRoute>(QQMusicOrderRoute.Main) }
    var allowedTalkers by remember { mutableStateOf(settings.allowedTalkers()) }

    SettingsRouteTransition(
        targetState = route,
        label = "QQMusicOrderRoute",
        depthOf = { if (it is QQMusicOrderRoute.Main) 0 else 1 }
    ) { currentRoute ->
        when (currentRoute) {
            QQMusicOrderRoute.Main -> QQMusicOrderMainPage(
                provider = provider,
                sp = sp,
                allowedTalkers = allowedTalkers,
                onBack = onBack,
                onPickTalkers = {
                    route = QQMusicOrderRoute.ContactPicker(
                        ContactPickerRequest(
                            title = "允许他人点歌的聊天",
                            mode = ContactPickerMode.BOTH,
                            multiSelect = true,
                            existingValue = allowedTalkers.joinToString("|"),
                            onValue = {},
                            enableLabels = true
                        )
                    )
                }
            )
            is QQMusicOrderRoute.ContactPicker -> ContactPickerPage(
                context = context,
                request = currentRoute.request,
                onBack = { route = QQMusicOrderRoute.Main },
                onConfirm = { picked ->
                    allowedTalkers = picked.map { it.id }.filter { it.isNotBlank() }.toSet()
                    settings.saveAllowedTalkers(allowedTalkers)
                    Toast.makeText(context, "点歌范围已保存", Toast.LENGTH_SHORT).show()
                    route = QQMusicOrderRoute.Main
                }
            )
        }
    }
}

@Composable
internal fun QQMusicOrderMainPage(
    provider: FeatureSettingsProvider,
    sp: SharedPreferences,
    allowedTalkers: Set<String>,
    onBack: () -> Unit,
    onPickTalkers: () -> Unit
) {
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    var customSingerEnabled by remember {
        mutableStateOf(sp.getBoolean(QQMusicOrderSettings.KEY_CUSTOM_SINGER, QQMusicOrderSettings.DEFAULT_CUSTOM_SINGER))
    }
    var triggers by remember {
        mutableStateOf(sp.getString(QQMusicOrderSettings.KEY_TRIGGERS, QQMusicOrderSettings.DEFAULT_TRIGGERS).orEmpty())
    }
    var defaultSinger by remember {
        mutableStateOf(sp.getString(QQMusicOrderSettings.KEY_DEFAULT_SINGER, QQMusicOrderSettings.DEFAULT_SINGER).orEmpty())
    }
    var appId by remember {
        mutableStateOf(sp.getString(QQMusicOrderSettings.KEY_APP_ID, QQMusicOrderSettings.DEFAULT_APP_ID).orEmpty())
    }

    PageScaffold(
        title = provider.title(),
        largeTitle = provider.title(),
        scrollBehavior = scrollBehavior,
        bottomBar = { BottomActionBar("返回", onBack) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(text = "点歌") }
            item {
                SettingsCard {
                    SwitchRow(
                        sp,
                        QQMusicOrderSettings.KEY_ENABLE,
                        "启用 QQ 点歌",
                        "在聊天中响应点歌指令",
                        QQMusicOrderSettings.DEFAULT_ENABLE
                    )
                    InsetDivider()
                    SwitchRow(
                        sp,
                        QQMusicOrderSettings.KEY_INTERCEPT_OWN_COMMAND,
                        "拦截自己的点歌指令",
                        "点击发送后不发送原指令，直接执行点歌",
                        QQMusicOrderSettings.DEFAULT_INTERCEPT_OWN_COMMAND
                    )
                    InsetDivider()
                    SwitchRow(
                        sp,
                        QQMusicOrderSettings.KEY_SEND_AS_CARD,
                        "点歌发送卡片",
                        "开启后发送音乐卡片，可与歌曲语音同时发送",
                        QQMusicOrderSettings.DEFAULT_SEND_AS_CARD
                    )
                    InsetDivider()
                    SwitchRow(
                        sp,
                        QQMusicOrderSettings.KEY_SEND_AS_VOICE,
                        "点歌发送语音",
                        "开启后发送歌曲语音，可与音乐卡片同时发送",
                        QQMusicOrderSettings.DEFAULT_SEND_AS_VOICE
                    )
                    InsetDivider()
                    ActionRow(
                        "允许他人点歌的聊天",
                        if (allowedTalkers.isEmpty()) "未选择" else "已选 ${allowedTalkers.size} 个聊天",
                        onPickTalkers
                    )
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "指令") }
            item {
                SettingsCard {
                    InputRow("触发词", "多个触发词用逗号分隔", triggers) {
                        triggers = it
                        sp.edit().putString(QQMusicOrderSettings.KEY_TRIGGERS, it).commit()
                    }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "音乐卡片") }
            item {
                SettingsCard {
                    SwitchRow(
                        sp,
                        QQMusicOrderSettings.KEY_REPLACE_COVER_WITH_AVATAR,
                        "封面使用点歌人头像",
                        "头像不可用时保留歌曲封面",
                        QQMusicOrderSettings.DEFAULT_REPLACE_COVER_WITH_AVATAR
                    )
                    InsetDivider()
                    SwitchRow(
                        sp,
                        QQMusicOrderSettings.KEY_REPLACE_SINGER_WITH_NICKNAME,
                        "歌手使用点歌人昵称",
                        "昵称不可用时保留原歌手",
                        QQMusicOrderSettings.DEFAULT_REPLACE_SINGER_WITH_NICKNAME
                    )
                    InsetDivider()
                    SwitchRow(
                        checked = customSingerEnabled,
                        title = "自定义 singer",
                        summary = "允许通过 & 指定歌手显示名称",
                        onCheckedChange = {
                            customSingerEnabled = it
                            sp.edit().putBoolean(QQMusicOrderSettings.KEY_CUSTOM_SINGER, it).commit()
                        }
                    )
                    if (customSingerEnabled) {
                        InsetDivider()
                        InputRow("默认 singer", "留空时使用原歌手或点歌人昵称", defaultSinger) {
                            defaultSinger = it
                            sp.edit().putString(QQMusicOrderSettings.KEY_DEFAULT_SINGER, it).commit()
                        }
                    }
                    InsetDivider()
                    InputRow("AppID", "留空时使用 QQ 音乐默认 AppID", appId) {
                        appId = it
                        sp.edit().putString(QQMusicOrderSettings.KEY_APP_ID, it).commit()
                    }
                }
            }
        }
    }
}

@Composable
internal fun KeywordNotificationMainPage(
    context: Context,
    provider: FeatureSettingsProvider,
    sp: SharedPreferences,
    keywords: List<KeywordRule>,
    excludeContacts: Set<String>,
    includeContacts: Set<String>,
    listState: LazyListState,
    onBack: () -> Unit,
    onOpenKeywords: () -> Unit,
    onOpenTemplates: () -> Unit,
    onPickContacts: (Boolean) -> Unit,
    onClearContacts: (Boolean) -> Unit
) {
    val scrollBehavior = MiuixScrollBehavior()
    var filterMode by remember { mutableStateOf(sp.getBoolean(KeywordNotificationSettings.KEY_FILTER_MODE, KeywordNotificationSettings.DEFAULT_FILTER_MODE)) }
    var quietEnabled by remember {
        mutableStateOf(sp.getBoolean(KeywordNotificationSettings.KEY_QUIET, KeywordNotificationSettings.DEFAULT_QUIET))
    }
    var quietStart by remember { mutableStateOf(sp.getString(KeywordNotificationSettings.KEY_QUIET_START, KeywordNotificationSettings.DEFAULT_QUIET_START) ?: KeywordNotificationSettings.DEFAULT_QUIET_START) }
    var quietEnd by remember { mutableStateOf(sp.getString(KeywordNotificationSettings.KEY_QUIET_END, KeywordNotificationSettings.DEFAULT_QUIET_END) ?: KeywordNotificationSettings.DEFAULT_QUIET_END) }
    val lastTime = sp.getLong(KeywordNotificationSettings.KEY_LAST_TIME, 0L)
    val lastKeyword = sp.getString(KeywordNotificationSettings.KEY_LAST_KEYWORD, "").orEmpty()
    val activeContacts = if (filterMode) includeContacts else excludeContacts
    PageScaffold(
        title = provider.title(),
        largeTitle = provider.title(),
        scrollBehavior = scrollBehavior,
        bottomBar = { BottomActionBar("返回", onBack) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(text = "监控") }
            item {
                SettingsCard {
                    SwitchRow(sp, KeywordNotificationSettings.KEY_ENABLE, "启用关键词通知", "收到匹配消息时提醒", KeywordNotificationSettings.DEFAULT_ENABLE)
                    InsetDivider()
                    InfoRow("关键词", "${keywords.size} 个")
                    InsetDivider()
                    InfoRow("上次匹配", if (lastTime > 0) "${KeywordNotificationRuntime.formatLastTime(lastTime)} · $lastKeyword" else "暂无匹配记录")
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "关键词") }
            item {
                SettingsCard {
                    ActionRow("关键词管理", if (keywords.isEmpty()) "未添加关键词" else "${keywords.size} 个关键词") {
                        onOpenKeywords()
                    }
                    InsetDivider()
                    SwitchRow(sp, KeywordNotificationSettings.KEY_ANY_GROUP, "任意关键词-群聊通知", "群聊文字或引用消息都触发", KeywordNotificationSettings.DEFAULT_ANY_GROUP)
                    InsetDivider()
                    SwitchRow(sp, KeywordNotificationSettings.KEY_ANY_PRIVATE, "任意关键词-私聊通知", "私聊文字或引用消息都触发", KeywordNotificationSettings.DEFAULT_ANY_PRIVATE)
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "通知") }
            item {
                SettingsCard {
                    SwitchRow(sp, KeywordNotificationSettings.KEY_NOTIFY, "系统通知", "在通知栏显示提醒", KeywordNotificationSettings.DEFAULT_NOTIFY)
                    InsetDivider()
                    SwitchRow(sp, KeywordNotificationSettings.KEY_TOAST, "Toast 提示", "短暂弹出提示", KeywordNotificationSettings.DEFAULT_TOAST)
                    InsetDivider()
                    SwitchRow(sp, KeywordNotificationSettings.KEY_AT_ME, "@我通知", "群聊有人 @ 我时提醒", KeywordNotificationSettings.DEFAULT_AT_ME)
                    InsetDivider()
                    SwitchRow(sp, KeywordNotificationSettings.KEY_AT_ALL, "@所有人/群公告通知", "命中 @所有人 或群公告时提醒", KeywordNotificationSettings.DEFAULT_AT_ALL)
                    InsetDivider()
                    ActionRow("通知/Toast模板", "留空时使用内置默认模板") {
                        onOpenTemplates()
                    }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "关键词通知") }
            item {
                KeywordNotificationAlertCard(
                    context = context,
                    sp = sp,
                    soundKey = KeywordNotificationSettings.KEY_KEYWORD_NOTIFY_SOUND,
                    vibrateKey = KeywordNotificationSettings.KEY_KEYWORD_NOTIFY_VIBRATE,
                    ringtoneKey = KeywordNotificationSettings.KEY_KEYWORD_NOTIFY_RINGTONE
                )
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "@我通知") }
            item {
                KeywordNotificationAlertCard(
                    context = context,
                    sp = sp,
                    soundKey = KeywordNotificationSettings.KEY_AT_ME_NOTIFY_SOUND,
                    vibrateKey = KeywordNotificationSettings.KEY_AT_ME_NOTIFY_VIBRATE,
                    ringtoneKey = KeywordNotificationSettings.KEY_AT_ME_NOTIFY_RINGTONE
                )
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "@所有人/群公告") }
            item {
                KeywordNotificationAlertCard(
                    context = context,
                    sp = sp,
                    soundKey = KeywordNotificationSettings.KEY_AT_ALL_NOTIFY_SOUND,
                    vibrateKey = KeywordNotificationSettings.KEY_AT_ALL_NOTIFY_VIBRATE,
                    ringtoneKey = KeywordNotificationSettings.KEY_AT_ALL_NOTIFY_RINGTONE
                )
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "生效范围") }
            item {
                SettingsCard {
                    SwitchRow(
                        checked = filterMode,
                        title = if (filterMode) "仅生效模式" else "排除模式",
                        summary = if (filterMode) "只处理名单里的聊天" else "不处理名单里的聊天"
                    ) {
                        filterMode = it
                        sp.edit().putBoolean(KeywordNotificationSettings.KEY_FILTER_MODE, it).apply()
                    }
                    InsetDivider()
                    ActionRow(
                        if (filterMode) "仅生效名单" else "排除名单",
                        if (activeContacts.isEmpty()) "未设置" else "${activeContacts.size} 个聊天"
                    ) {
                        onPickContacts(filterMode)
                    }
                    if (activeContacts.isNotEmpty()) {
                        InsetDivider()
                        ActionRow("清空当前名单", if (filterMode) "清空仅生效名单" else "清空排除名单") {
                            onClearContacts(filterMode)
                        }
                    }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "免打扰") }
            item {
                SettingsCard {
                    SwitchRow(quietEnabled, "启用免打扰", "指定时间内不提醒") {
                        quietEnabled = it
                        sp.edit().putBoolean(KeywordNotificationSettings.KEY_QUIET, it).apply()
                    }
                    if (quietEnabled) {
                        InsetDivider()
                        TimeOfDayPickerRow("开始时间", quietStart) {
                            quietStart = it
                            sp.edit().putString(KeywordNotificationSettings.KEY_QUIET_START, quietStart).apply()
                        }
                        InsetDivider()
                        TimeOfDayPickerRow("结束时间", quietEnd) {
                            quietEnd = it
                            sp.edit().putString(KeywordNotificationSettings.KEY_QUIET_END, quietEnd).apply()
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun KeywordNotificationAlertCard(
    context: Context,
    sp: SharedPreferences,
    soundKey: String,
    vibrateKey: String,
    ringtoneKey: String
) {
    var notifySound by remember(soundKey) {
        mutableStateOf(sp.getBoolean(soundKey, sp.getBoolean(KeywordNotificationSettings.KEY_NOTIFY_SOUND, KeywordNotificationSettings.DEFAULT_NOTIFY_SOUND)))
    }
    var notifyVibrate by remember(vibrateKey) {
        mutableStateOf(sp.getBoolean(vibrateKey, sp.getBoolean(KeywordNotificationSettings.KEY_NOTIFY_VIBRATE, KeywordNotificationSettings.DEFAULT_NOTIFY_VIBRATE)))
    }
    var notifyRingtone by remember(ringtoneKey) {
        mutableStateOf(sp.getString(ringtoneKey, sp.getString(KeywordNotificationSettings.KEY_NOTIFY_RINGTONE, "") ?: "").orEmpty())
    }
    SettingsCard {
        SwitchRow(
            checked = notifyVibrate,
            title = "通知震动",
            summary = "触发该类型通知时震动"
        ) {
            notifyVibrate = it
            sp.edit().putBoolean(vibrateKey, it).apply()
        }
        InsetDivider()
        SwitchRow(
            checked = notifySound,
            title = "通知铃声",
            summary = "触发该类型通知时播放系统或自定义铃声"
        ) {
            notifySound = it
            sp.edit().putBoolean(soundKey, it).apply()
        }
        if (notifySound) {
            InsetDivider()
            ActionRow("选择系统铃声", ringtoneDisplayName(context, notifyRingtone, RedPacketSettings.NOTIFY_SOUND_MODE_SYSTEM)) {
                val activity = context as? Activity
                if (activity == null) {
                    Toast.makeText(context, "当前页面无法打开铃声选择器", Toast.LENGTH_SHORT).show()
                } else {
                    RingtonePickerBridge.launchSystem(activity, notifyRingtone) { picked ->
                        notifyRingtone = CustomNotificationRuntime.freezeRingtoneUri(context, picked)
                        sp.edit().putString(ringtoneKey, notifyRingtone).apply()
                        Toast.makeText(context, "铃声已保存", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            InsetDivider()
            ActionRow("从文件选择铃声", if (notifyRingtone.isBlank()) "未选择" else ringtoneDisplayName(context, notifyRingtone, RedPacketSettings.NOTIFY_SOUND_MODE_CUSTOM)) {
                val activity = context as? Activity
                if (activity == null) {
                    Toast.makeText(context, "当前页面无法打开文件选择器", Toast.LENGTH_SHORT).show()
                } else {
                    RingtonePickerBridge.launchFile(activity) { picked ->
                        notifyRingtone = CustomNotificationRuntime.freezeRingtoneUri(context, picked)
                        sp.edit().putString(ringtoneKey, notifyRingtone).apply()
                        Toast.makeText(context, "铃声已保存", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            if (notifyRingtone.isNotBlank()) {
                InsetDivider()
                ActionRow("清空铃声", "恢复跟随系统") {
                    notifyRingtone = ""
                    sp.edit().putString(ringtoneKey, "").apply()
                    Toast.makeText(context, "铃声已清空", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

@Composable
internal fun KeywordNotificationKeywordListPage(
    keywords: List<KeywordRule>,
    query: String,
    onQueryChange: (String) -> Unit,
    listState: LazyListState,
    onBack: () -> Unit,
    onAdd: () -> Unit,
    onEdit: (KeywordRule) -> Unit,
    onClear: () -> Unit
) {
    val scrollBehavior = MiuixScrollBehavior()
    val lower = query.trim().lowercase(Locale.US)
    val visible = keywords.filter {
        lower.isBlank() || it.keyword.lowercase(Locale.US).contains(lower)
    }
    PageScaffold(
        title = "关键词管理",
        largeTitle = "关键词管理",
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "添加关键词",
                onPrimaryClick = onAdd,
                secondaryText = "返回",
                onSecondaryClick = onBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item {
                SettingsCard {
                    InputRow("搜索", "关键词", query, onValueChange = onQueryChange)
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = if (visible.isEmpty()) "关键词" else "关键词 · ${visible.size} 个") }
            when {
                keywords.isEmpty() -> item { SettingsCard { EmptyText("暂无关键词，点击底部“添加关键词”。") } }
                visible.isEmpty() -> item { SettingsCard { EmptyText("没有匹配结果") } }
                else -> visible.forEach { rule ->
                    item {
                        SettingsCard {
                            ActionRow(rule.keyword, if (rule.wholeWord) "全字匹配" else "模糊匹配") {
                                onEdit(rule)
                            }
                        }
                    }
                }
            }
            if (keywords.isNotEmpty()) {
                item {
                    SettingsCard {
                        ActionRow("清空所有关键词", "移除全部关键词") {
                            onClear()
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun KeywordNotificationKeywordEditorPage(
    initial: KeywordRule?,
    onBack: () -> Unit,
    onSave: (KeywordRule) -> Unit,
    onDelete: (() -> Unit)?
) {
    var keyword by remember(initial) { mutableStateOf(initial?.keyword.orEmpty()) }
    var wholeWord by remember(initial) { mutableStateOf(initial?.wholeWord ?: false) }
    val scrollBehavior = MiuixScrollBehavior()
    PageScaffold(
        title = if (initial == null) "添加关键词" else "编辑关键词",
        largeTitle = if (initial == null) "添加关键词" else "编辑关键词",
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "保存",
                onPrimaryClick = { onSave(KeywordRule(keyword.trim(), wholeWord)) },
                secondaryText = "返回",
                onSecondaryClick = onBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(text = "关键词") }
            item {
                SettingsCard {
                    InputRow("关键词", "输入要监控的关键词", keyword) { keyword = it }
                    InsetDivider()
                    SwitchRow(wholeWord, "全字匹配", "关闭为模糊匹配，包含关键词即可触发") { wholeWord = it }
                }
            }
            if (onDelete != null) {
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "操作") }
                item {
                    SettingsCard {
                        ActionRow("删除关键词", "移除该关键词") {
                            onDelete()
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun KeywordNotificationTemplatePage(
    context: Context,
    sp: SharedPreferences,
    onBack: () -> Unit
) {
    var keywordTitle by remember { mutableStateOf(sp.getString(KeywordNotificationSettings.KEY_KEYWORD_TITLE, "").orEmpty()) }
    var keywordContent by remember { mutableStateOf(sp.getString(KeywordNotificationSettings.KEY_KEYWORD_CONTENT, "").orEmpty()) }
    var keywordToast by remember { mutableStateOf(sp.getString(KeywordNotificationSettings.KEY_KEYWORD_TOAST, "").orEmpty()) }
    var atMeTitle by remember { mutableStateOf(sp.getString(KeywordNotificationSettings.KEY_AT_ME_TITLE, "").orEmpty()) }
    var atMeContent by remember { mutableStateOf(sp.getString(KeywordNotificationSettings.KEY_AT_ME_CONTENT, "").orEmpty()) }
    var atMeToast by remember { mutableStateOf(sp.getString(KeywordNotificationSettings.KEY_AT_ME_TOAST, "").orEmpty()) }
    var atAllTitle by remember { mutableStateOf(sp.getString(KeywordNotificationSettings.KEY_AT_ALL_TITLE, "").orEmpty()) }
    var atAllContent by remember { mutableStateOf(sp.getString(KeywordNotificationSettings.KEY_AT_ALL_CONTENT, "").orEmpty()) }
    var atAllToast by remember { mutableStateOf(sp.getString(KeywordNotificationSettings.KEY_AT_ALL_TOAST, "").orEmpty()) }
    val scrollBehavior = MiuixScrollBehavior()
    fun save() {
        sp.edit()
            .putString(KeywordNotificationSettings.KEY_KEYWORD_TITLE, keywordTitle)
            .putString(KeywordNotificationSettings.KEY_KEYWORD_CONTENT, keywordContent)
            .putString(KeywordNotificationSettings.KEY_KEYWORD_TOAST, keywordToast)
            .putString(KeywordNotificationSettings.KEY_AT_ME_TITLE, atMeTitle)
            .putString(KeywordNotificationSettings.KEY_AT_ME_CONTENT, atMeContent)
            .putString(KeywordNotificationSettings.KEY_AT_ME_TOAST, atMeToast)
            .putString(KeywordNotificationSettings.KEY_AT_ALL_TITLE, atAllTitle)
            .putString(KeywordNotificationSettings.KEY_AT_ALL_CONTENT, atAllContent)
            .putString(KeywordNotificationSettings.KEY_AT_ALL_TOAST, atAllToast)
            .apply()
        Toast.makeText(context, "模板已保存", Toast.LENGTH_SHORT).show()
        onBack()
    }
    PageScaffold(
        title = "自定义文字",
        largeTitle = "自定义文字",
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "保存",
                onPrimaryClick = ::save,
                secondaryText = "返回",
                onSecondaryClick = onBack,
                middleText = "恢复默认",
                onMiddleClick = {
                    keywordTitle = ""
                    keywordContent = ""
                    keywordToast = ""
                    atMeTitle = ""
                    atMeContent = ""
                    atMeToast = ""
                    atAllTitle = ""
                    atAllContent = ""
                    atAllToast = ""
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(text = "关键词通知") }
            item {
                SettingsCard {
                    VariableInputRow("通知标题模板", "默认：${KeywordNotificationSettings.DEFAULT_KEYWORD_TITLE}", keywordTitle, keywordNotificationTemplateVariables) { keywordTitle = it }
                    InsetDivider()
                    VariableInputRow("通知内容模板", "默认：${KeywordNotificationSettings.DEFAULT_KEYWORD_CONTENT}", keywordContent, keywordNotificationTemplateVariables, minLines = 3) { keywordContent = it }
                    InsetDivider()
                    VariableInputRow("Toast 文字模板", "默认：${KeywordNotificationSettings.DEFAULT_KEYWORD_TOAST}", keywordToast, keywordNotificationTemplateVariables) { keywordToast = it }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "@我通知") }
            item {
                SettingsCard {
                    VariableInputRow("通知标题模板", "默认：${KeywordNotificationSettings.DEFAULT_AT_ME_TITLE}", atMeTitle, keywordNotificationTemplateVariables) { atMeTitle = it }
                    InsetDivider()
                    VariableInputRow("通知内容模板", "默认：${KeywordNotificationSettings.DEFAULT_AT_ME_CONTENT}", atMeContent, keywordNotificationTemplateVariables, minLines = 3) { atMeContent = it }
                    InsetDivider()
                    VariableInputRow("Toast 文字模板", "默认：${KeywordNotificationSettings.DEFAULT_AT_ME_TOAST}", atMeToast, keywordNotificationTemplateVariables) { atMeToast = it }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "@所有人/群公告通知") }
            item {
                SettingsCard {
                    VariableInputRow("通知标题模板", "默认：${KeywordNotificationSettings.DEFAULT_AT_ALL_TITLE}", atAllTitle, keywordNotificationTemplateVariables) { atAllTitle = it }
                    InsetDivider()
                    VariableInputRow("通知内容模板", "默认：${KeywordNotificationSettings.DEFAULT_AT_ALL_CONTENT}", atAllContent, keywordNotificationTemplateVariables, minLines = 3) { atAllContent = it }
                    InsetDivider()
                    VariableInputRow("Toast 文字模板", "默认：${KeywordNotificationSettings.DEFAULT_AT_ALL_TOAST}", atAllToast, keywordNotificationTemplateVariables) { atAllToast = it }
                }
            }
        }
    }
}

@Composable
internal fun QuoteDeleteClearMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, QuoteDeleteClearSettings.PREFS_NAME) }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()

    PageScaffold(
        title = provider.title(),
        largeTitle = provider.title(),
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar("返回", onBack)
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(text = "聊天输入框") }
            item {
                SettingsCard {
                    SwitchRow(
                        sp,
                        QuoteDeleteClearSettings.KEY_ENABLE,
                        "删除键清引用",
                        "输入框为空且已引用消息时，按输入法删除键直接取消引用",
                        QuoteDeleteClearSettings.DEFAULT_ENABLE
                    )
                }
            }
        }
    }
}

@Composable
internal fun EmojiSaveMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, EmojiSaveSettings.PREFS_NAME) }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()

    PageScaffold(
        title = provider.title(),
        largeTitle = provider.title(),
        scrollBehavior = scrollBehavior,
        bottomBar = { BottomActionBar("返回", onBack) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(text = "聊天表情") }
            item {
                SettingsCard {
                    SwitchRow(
                        sp,
                        EmojiSaveSettings.KEY_ENABLE,
                        "保存表情",
                        "长按聊天表情后显示保存入口",
                        EmojiSaveSettings.DEFAULT_ENABLE
                    )
                }
            }
        }
    }
}

@Composable
internal fun FakeVoiceDurationMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, FakeVoiceDurationSettings.PREFS_NAME) }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    var enabled by remember {
        mutableStateOf(
            sp.getBoolean(
                FakeVoiceDurationSettings.KEY_ENABLE,
                FakeVoiceDurationSettings.DEFAULT_ENABLE
            )
        )
    }
    var durationSeconds by remember {
        mutableStateOf(
            sp.getInt(
                FakeVoiceDurationSettings.KEY_DURATION_SECONDS,
                FakeVoiceDurationSettings.DEFAULT_DURATION_SECONDS
            ).toString()
        )
    }

    PageScaffold(
        title = provider.title(),
        largeTitle = provider.title(),
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar("返回", onBack)
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(text = "聊天语音") }
            item {
                SettingsCard {
                    SwitchRow(
                        checked = enabled,
                        title = "伪造语音时长",
                        summary = "发送语音时使用自定义显示时长",
                        onCheckedChange = {
                            enabled = it
                            sp.edit().putBoolean(FakeVoiceDurationSettings.KEY_ENABLE, it).apply()
                        }
                    )
                    if (enabled) {
                        InsetDivider()
                        NumberInputRow("显示时长", "单位秒，1-60", durationSeconds) { next ->
                            val value = next.toIntOrNull()
                            durationSeconds = value?.coerceIn(
                                FakeVoiceDurationSettings.MIN_DURATION_SECONDS,
                                FakeVoiceDurationSettings.MAX_DURATION_SECONDS
                            )?.toString() ?: next
                            if (value == null) return@NumberInputRow
                            sp.edit().putInt(
                                FakeVoiceDurationSettings.KEY_DURATION_SECONDS,
                                value.coerceIn(
                                    FakeVoiceDurationSettings.MIN_DURATION_SECONDS,
                                    FakeVoiceDurationSettings.MAX_DURATION_SECONDS
                                )
                            ).apply()
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun VoicePreviewMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, VoicePreviewSettings.PREFS_NAME) }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()

    PageScaffold(
        title = provider.title(),
        largeTitle = provider.title(),
        scrollBehavior = scrollBehavior,
        bottomBar = { BottomActionBar("返回", onBack) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(text = "聊天语音") }
            item {
                SettingsCard {
                    SwitchRow(
                        sp,
                        VoicePreviewSettings.KEY_ENABLE,
                        "语音消息预览",
                        "长按聊天语音后查看时长并播放",
                        VoicePreviewSettings.DEFAULT_ENABLE
                    )
                }
            }
        }
    }
}

@Composable
internal fun FakeMiniProgramBaseLibMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, FakeMiniProgramBaseLibSettings.PREFS_NAME) }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    var enabled by remember {
        mutableStateOf(
            sp.getBoolean(
                FakeMiniProgramBaseLibSettings.KEY_ENABLE,
                FakeMiniProgramBaseLibSettings.DEFAULT_ENABLE
            )
        )
    }
    PageScaffold(
        title = provider.title(),
        largeTitle = provider.title(),
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar("返回", onBack)
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(text = "小程序") }
            item {
                SettingsCard {
                    SwitchRow(
                        checked = enabled,
                        title = "兼容低版本小程序",
                        summary = "伪装启动基础库版本并阻止官方升级页",
                        onCheckedChange = {
                            enabled = it
                            sp.edit().putBoolean(FakeMiniProgramBaseLibSettings.KEY_ENABLE, it).apply()
                        }
                    )
                }
            }
        }
    }
}

@Composable
internal fun SkipMiniProgramVideoAdsMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember {
        HchatStorage.preferences(context, SkipMiniProgramVideoAdsSettings.PREFS_NAME)
    }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    var enabled by remember {
        mutableStateOf(
            sp.getBoolean(
                SkipMiniProgramVideoAdsSettings.KEY_ENABLE,
                SkipMiniProgramVideoAdsSettings.DEFAULT_ENABLE
            )
        )
    }
    PageScaffold(
        title = provider.title(),
        largeTitle = provider.title(),
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar("返回", onBack)
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(text = "小程序") }
            item {
                SettingsCard {
                    SwitchRow(
                        checked = enabled,
                        title = "跳过小程序视频广告",
                        summary = "自动跳过小程序视频广告",
                        onCheckedChange = {
                            enabled = it
                            sp.edit().putBoolean(
                                SkipMiniProgramVideoAdsSettings.KEY_ENABLE,
                                it
                            ).apply()
                        }
                    )
                }
            }
        }
    }
}

@Composable
internal fun SkipGlobalMiniProgramSplashAdsMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember {
        HchatStorage.preferences(context, SkipGlobalMiniProgramSplashAdsSettings.PREFS_NAME)
    }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    var enabled by remember {
        mutableStateOf(
            sp.getBoolean(
                SkipGlobalMiniProgramSplashAdsSettings.KEY_ENABLE,
                SkipGlobalMiniProgramSplashAdsSettings.DEFAULT_ENABLE
            )
        )
    }
    PageScaffold(
        title = provider.title(),
        largeTitle = provider.title(),
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar("返回", onBack)
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(text = "小程序") }
            item {
                SettingsCard {
                    SwitchRow(
                        checked = enabled,
                        title = "跳过全局小程序开屏广告",
                        summary = "阻止所有小程序展示启动开屏广告，修改后需重启微信",
                        onCheckedChange = {
                            enabled = it
                            sp.edit().putBoolean(
                                SkipGlobalMiniProgramSplashAdsSettings.KEY_ENABLE,
                                it
                            ).apply()
                        }
                    )
                }
            }
        }
    }
}

@Composable
internal fun FakeLocationMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, FakeLocationSettings.PREFS_NAME) }
    var enabled by remember {
        mutableStateOf(sp.getBoolean(FakeLocationSettings.KEY_ENABLE, FakeLocationSettings.DEFAULT_ENABLE))
    }
    var latitude by remember {
        mutableStateOf(sp.getString(FakeLocationSettings.KEY_LATITUDE, FakeLocationSettings.DEFAULT_LATITUDE).orEmpty())
    }
    var longitude by remember {
        mutableStateOf(sp.getString(FakeLocationSettings.KEY_LONGITUDE, FakeLocationSettings.DEFAULT_LONGITUDE).orEmpty())
    }
    var showManualInput by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()

    fun saveLocation(lat: Double, lon: Double) {
        latitude = formatFakeLocationCoordinate(lat)
        longitude = formatFakeLocationCoordinate(lon)
        sp.edit()
            .putString(FakeLocationSettings.KEY_LATITUDE, latitude)
            .putString(FakeLocationSettings.KEY_LONGITUDE, longitude)
            .apply()
    }

    PageScaffold(
        title = provider.title(),
        largeTitle = provider.title(),
        scrollBehavior = scrollBehavior,
        bottomBar = { BottomActionBar("返回", onBack) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(text = "定位") }
            item {
                SettingsCard {
                    SwitchRow(
                        checked = enabled,
                        title = "启用虚拟定位",
                        summary = "同时覆盖微信和小程序获取的位置",
                        onCheckedChange = {
                            enabled = it
                            sp.edit().putBoolean(FakeLocationSettings.KEY_ENABLE, it).apply()
                        }
                    )
                    InsetDivider()
                    ActionRow("搜索并选择位置", "打开微信选择位置页，可搜索地点") {
                        val activity = context as? Activity
                        if (activity == null) {
                            Toast.makeText(context, "当前页面无法打开微信地图", Toast.LENGTH_SHORT).show()
                        } else {
                            NativeFakeLocationPickerBridge.launch(activity) { lat, lon ->
                                saveLocation(lat, lon)
                                Toast.makeText(context, "已保存虚拟定位", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    InsetDivider()
                    ActionRow("手动输入经纬度", "纬度 -90~90，经度 -180~180") {
                        showManualInput = true
                    }
                    InsetDivider()
                    InfoRow("当前纬度", latitude) {
                        copyFakeLocationCoordinate(context, "Hchat 纬度", latitude)
                    }
                    InsetDivider()
                    InfoRow("当前经度", longitude) {
                        copyFakeLocationCoordinate(context, "Hchat 经度", longitude)
                    }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "说明") }
            item {
                SettingsCard {
                    InfoRow("生效范围", "微信和小程序定位")
                }
            }
        }
    }

    if (showManualInput) {
        FakeLocationManualInputDialog(
            initialLatitude = latitude,
            initialLongitude = longitude,
            onDismiss = { showManualInput = false },
            onConfirm = { lat, lon ->
                saveLocation(lat, lon)
                showManualInput = false
                Toast.makeText(context, "已保存虚拟定位", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

@Composable
internal fun GameEmojiMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, GameEmojiSettings.PREFS_NAME) }
    var fixedResult by remember {
        mutableStateOf(
            sp.getBoolean(
                GameEmojiSettings.KEY_FIXED_RESULT,
                GameEmojiSettings.DEFAULT_FIXED_RESULT
            )
        )
    }
    var pickBeforeSend by remember {
        mutableStateOf(
            sp.getBoolean(
                GameEmojiSettings.KEY_PICK_BEFORE_SEND,
                GameEmojiSettings.DEFAULT_PICK_BEFORE_SEND
            )
        )
    }
    var diceResult by remember {
        mutableStateOf(
            sp.getInt(GameEmojiSettings.KEY_DICE_RESULT, GameEmojiSettings.DEFAULT_DICE_RESULT)
                .coerceIn(1, 6)
        )
    }
    var rpsResult by remember {
        mutableStateOf(
            sp.getInt(GameEmojiSettings.KEY_RPS_RESULT, GameEmojiSettings.DEFAULT_RPS_RESULT)
                .coerceIn(GameEmojiSettings.RPS_SCISSORS, GameEmojiSettings.RPS_PAPER)
        )
    }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()

    PageScaffold(
        title = provider.title(),
        largeTitle = provider.title(),
        scrollBehavior = scrollBehavior,
        bottomBar = { BottomActionBar("返回", onBack) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(text = "发送方式") }
            item {
                SettingsCard {
                    SwitchRow(
                        checked = fixedResult,
                        title = "使用固定结果",
                        summary = "直接发送下方设置的骰子点数或猜拳结果",
                        onCheckedChange = { checked ->
                            fixedResult = checked
                            if (checked) pickBeforeSend = false
                            sp.edit()
                                .putBoolean(GameEmojiSettings.KEY_FIXED_RESULT, checked)
                                .putBoolean(
                                    GameEmojiSettings.KEY_PICK_BEFORE_SEND,
                                    if (checked) false else pickBeforeSend
                                )
                                .apply()
                        }
                    )
                    InsetDivider()
                    SwitchRow(
                        checked = pickBeforeSend,
                        title = "发送时选择",
                        summary = "每次发送骰子或猜拳前弹出结果选择",
                        onCheckedChange = { checked ->
                            pickBeforeSend = checked
                            if (checked) fixedResult = false
                            sp.edit()
                                .putBoolean(GameEmojiSettings.KEY_PICK_BEFORE_SEND, checked)
                                .putBoolean(
                                    GameEmojiSettings.KEY_FIXED_RESULT,
                                    if (checked) false else fixedResult
                                )
                                .apply()
                        }
                    )
                }
            }
            if (fixedResult) {
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "固定结果") }
                item {
                    SettingsCard {
                        PopupOptionRow(
                            title = "骰子点数",
                            summary = "$diceResult 点",
                            options = (1..6).map { OptionItem("$it 点", it, "") },
                            currentValue = diceResult,
                            onValueChanged = { value ->
                                diceResult = value
                                sp.edit().putInt(GameEmojiSettings.KEY_DICE_RESULT, value).apply()
                            }
                        )
                        InsetDivider()
                        PopupOptionRow(
                            title = "猜拳结果",
                            summary = GameEmojiSettings.rpsLabel(rpsResult),
                            options = listOf(
                                OptionItem("剪刀", GameEmojiSettings.RPS_SCISSORS, ""),
                                OptionItem("石头", GameEmojiSettings.RPS_ROCK, ""),
                                OptionItem("布", GameEmojiSettings.RPS_PAPER, "")
                            ),
                            currentValue = rpsResult,
                            onValueChanged = { value ->
                                rpsResult = value
                                sp.edit().putInt(GameEmojiSettings.KEY_RPS_RESULT, value).apply()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun FakeLocationManualInputDialog(
    initialLatitude: String,
    initialLongitude: String,
    onDismiss: () -> Unit,
    onConfirm: (Double, Double) -> Unit
) {
    var latitude by remember(initialLatitude) { mutableStateOf(initialLatitude) }
    var longitude by remember(initialLongitude) { mutableStateOf(initialLongitude) }
    val parsed = validFakeLocation(latitude.toDoubleOrNull(), longitude.toDoubleOrNull())
    WindowDialog(
        show = true,
        title = "手动输入经纬度",
        onDismissRequest = onDismiss,
        content = {
            Column(modifier = Modifier.fillMaxWidth()) {
                InputRow("纬度", "范围 -90 到 90", latitude) { latitude = cleanCoordinateInput(it) }
                InputRow("经度", "范围 -180 到 180", longitude) { longitude = cleanCoordinateInput(it) }
                if (parsed == null) {
                    Text(
                        text = "请输入有效的纬度和经度",
                        color = Color(0xFFD93025),
                        fontSize = 13.sp,
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        textAlign = TextAlign.Center
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextButton(
                        text = "取消",
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.textButtonColorsPrimary()
                    )
                    TextButton(
                        text = "保存",
                        onClick = { parsed?.let { onConfirm(it.first, it.second) } },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.textButtonColorsPrimary()
                    )
                }
            }
        }
    )
}

internal fun cleanCoordinateInput(value: String): String {
    return value.filterIndexed { index, char ->
        char.isDigit() || char == '.' || (char == '-' && index == 0)
    }
}

internal fun validFakeLocation(latitude: Double?, longitude: Double?): Pair<Double, Double>? {
    if (latitude == null || longitude == null || !latitude.isFinite() || !longitude.isFinite()) return null
    if (latitude !in -90.0..90.0 || longitude !in -180.0..180.0) return null
    return latitude to longitude
}

internal fun parseNativeFakeLocation(text: String): Pair<Double, Double>? {
    val latitude = Regex("""(?:lat|latitude)\s*[:= ]\s*([-+]?[0-9]*\.?[0-9]+)""", RegexOption.IGNORE_CASE)
        .find(text)?.groupValues?.getOrNull(1)?.toDoubleOrNull()
    val longitude = Regex("""(?:lng|lon|longitude)\s*[:= ]\s*([-+]?[0-9]*\.?[0-9]+)""", RegexOption.IGNORE_CASE)
        .find(text)?.groupValues?.getOrNull(1)?.toDoubleOrNull()
    return validFakeLocation(latitude, longitude)
}

internal fun formatFakeLocationCoordinate(value: Double): String {
    return String.format(Locale.US, "%.6f", value)
}

internal fun copyFakeLocationCoordinate(context: Context, label: String, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? android.content.ClipboardManager
    clipboard?.setPrimaryClip(ClipData.newPlainText(label, text))
    Toast.makeText(context, "已复制", Toast.LENGTH_SHORT).show()
}

@Composable
internal fun MessageBubbleMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, MessageBubbleSettings.PREFS_NAME) }
    var enabled by remember {
        mutableStateOf(sp.getBoolean(MessageBubbleSettings.KEY_ENABLE, MessageBubbleSettings.DEFAULT_ENABLE))
    }
    var separateDarkMode by remember {
        mutableStateOf(
            sp.getBoolean(
                MessageBubbleSettings.KEY_SEPARATE_DARK_MODE,
                MessageBubbleSettings.DEFAULT_SEPARATE_DARK_MODE
            )
        )
    }
    var revision by remember { mutableStateOf(0) }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()

    PageScaffold(
        title = provider.title(),
        largeTitle = provider.title(),
        scrollBehavior = scrollBehavior,
        bottomBar = { BottomActionBar("返回", onBack) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(text = "基础") }
            item {
                SettingsCard {
                    SwitchRow(
                        checked = enabled,
                        title = "启用消息气泡",
                        summary = "使用本地图片替换微信原生消息气泡",
                        onCheckedChange = {
                            enabled = it
                            sp.edit().putBoolean(MessageBubbleSettings.KEY_ENABLE, it).apply()
                        }
                    )
                    if (enabled) {
                        InsetDivider()
                        SwitchRow(
                            checked = separateDarkMode,
                            title = "深色模式单独设置",
                            summary = "未设置深色气泡时自动沿用对应浅色气泡",
                            onCheckedChange = {
                                separateDarkMode = it
                                sp.edit()
                                    .putBoolean(MessageBubbleSettings.KEY_SEPARATE_DARK_MODE, it)
                                    .apply()
                            }
                        )
                    }
                }
            }
            if (enabled) {
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "浅色模式") }
                item {
                    SettingsCard {
                        MessageBubbleAssetRow(context, MessageBubbleSlot.LEFT_LIGHT, revision) { revision++ }
                        InsetDivider()
                        MessageBubbleAssetRow(context, MessageBubbleSlot.RIGHT_LIGHT, revision) { revision++ }
                    }
                }
                if (separateDarkMode) {
                    item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "深色模式") }
                    item {
                        SettingsCard {
                            MessageBubbleAssetRow(context, MessageBubbleSlot.LEFT_DARK, revision) { revision++ }
                            InsetDivider()
                            MessageBubbleAssetRow(context, MessageBubbleSlot.RIGHT_DARK, revision) { revision++ }
                        }
                    }
                }
                MessageBubbleKind.values()
                    .filter { it != MessageBubbleKind.GENERAL }
                    .forEach { kind ->
                        item {
                            SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "${kind.displayName}气泡")
                        }
                        item {
                            SettingsCard {
                                if (kind == MessageBubbleKind.SYSTEM) {
                                    MessageBubbleAssetRow(
                                        context,
                                        MessageBubbleSlot.resolve(kind, false, false),
                                        revision
                                    ) { revision++ }
                                } else {
                                    MessageBubbleAssetRow(
                                        context,
                                        MessageBubbleSlot.resolve(kind, false, false),
                                        revision
                                    ) { revision++ }
                                    InsetDivider()
                                    MessageBubbleAssetRow(
                                        context,
                                        MessageBubbleSlot.resolve(kind, true, false),
                                        revision
                                    ) { revision++ }
                                }
                            }
                        }
                        if (separateDarkMode) {
                            item {
                                SettingsCard {
                                    if (kind == MessageBubbleKind.SYSTEM) {
                                        MessageBubbleAssetRow(
                                            context,
                                            MessageBubbleSlot.resolve(kind, false, true),
                                            revision
                                        ) { revision++ }
                                    } else {
                                        MessageBubbleAssetRow(
                                            context,
                                            MessageBubbleSlot.resolve(kind, false, true),
                                            revision
                                        ) { revision++ }
                                        InsetDivider()
                                        MessageBubbleAssetRow(
                                            context,
                                            MessageBubbleSlot.resolve(kind, true, true),
                                            revision
                                        ) { revision++ }
                                    }
                                }
                            }
                        }
                    }
            }
        }
    }
}

@Composable
internal fun MessageBubbleAssetRow(
    context: Context,
    slot: MessageBubbleSlot,
    revision: Int,
    onChanged: () -> Unit
) {
    val selected = remember(slot, revision) { MessageBubbleStore.hasAsset(context, slot) }
    val emptySummary = when {
        slot.kind == MessageBubbleKind.GENERAL && slot.darkMode -> "未选择，沿用对应浅色气泡"
        slot.kind == MessageBubbleKind.GENERAL -> "未选择，保持微信原生气泡"
        slot.darkMode -> "未选择，沿用同类浅色气泡"
        else -> "未选择，保持微信原生气泡"
    }
    ActionRow(
        title = slot.displayName,
        summary = if (selected) "已选择，点击更换或恢复" else emptySummary
    ) {
        val activity = context as? Activity ?: return@ActionRow
        val pickAsset = {
            MessageBubblePicker.launch(activity, slot) { result ->
                when (result) {
                    MessageBubblePickResult.SAVED -> {
                        onChanged()
                        Toast.makeText(context, "${slot.displayName}已更新", Toast.LENGTH_SHORT).show()
                    }
                    MessageBubblePickResult.FAILED -> {
                        Toast.makeText(context, "气泡图片无效或读取失败", Toast.LENGTH_SHORT).show()
                    }
                    MessageBubblePickResult.CANCELLED -> Unit
                }
            }
        }
        if (!selected) {
            pickAsset()
        } else {
            VoiceForwardMiuixDialog.showChoices(
                activity = activity,
                title = slot.displayName,
                summary = "管理当前气泡图片",
                choices = listOf(
                    "更换气泡" to "重新选择一张本地图片",
                    "恢复微信气泡" to "删除当前自定义气泡"
                ),
                onSelected = { choice ->
                    if (choice == 0) {
                        pickAsset()
                    } else if (MessageBubbleStore.remove(context, slot)) {
                        onChanged()
                        Toast.makeText(context, "已恢复微信气泡", Toast.LENGTH_SHORT).show()
                    }
                },
                onDismiss = {}
            )
        }
    }
}

@Composable
internal fun BackgroundBeautyMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, BackgroundBeautySettings.PREFS_NAME) }
    var enabled by remember {
        mutableStateOf(
            sp.getBoolean(
                BackgroundBeautySettings.KEY_ENABLE,
                BackgroundBeautySettings.DEFAULT_ENABLE
            )
        )
    }
    var revision by remember { mutableStateOf(sp.getInt(BackgroundBeautySettings.KEY_REVISION, 0)) }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()

    PageScaffold(
        title = provider.title(),
        largeTitle = provider.title(),
        scrollBehavior = scrollBehavior,
        bottomBar = { BottomActionBar("返回", onBack) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(text = "基础") }
            item {
                SettingsCard {
                    SwitchRow(
                        checked = enabled,
                        title = "启用沉浸式背景",
                        summary = "分别设置聊天和微信四个主页面背景",
                        onCheckedChange = {
                            enabled = it
                            sp.edit().putBoolean(BackgroundBeautySettings.KEY_ENABLE, it).apply()
                        }
                    )
                }
            }
            if (enabled) {
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "聊天") }
                item {
                    SettingsCard {
                        BackgroundBeautyAssetBlock(
                            context = context,
                            sp = sp,
                            slot = BackgroundBeautySettings.Slot.CHAT,
                            revision = revision,
                            onChanged = {
                                revision = bumpBackgroundBeautyRevision(sp)
                            }
                        )
                    }
                }
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "微信首页") }
                item {
                    SettingsCard {
                        val slots = listOf(
                            BackgroundBeautySettings.Slot.WECHAT,
                            BackgroundBeautySettings.Slot.CONTACTS,
                            BackgroundBeautySettings.Slot.DISCOVER,
                            BackgroundBeautySettings.Slot.ME
                        )
                        slots.forEachIndexed { index, slot ->
                            BackgroundBeautyAssetBlock(
                                context = context,
                                sp = sp,
                                slot = slot,
                                revision = revision,
                                onChanged = {
                                    revision = bumpBackgroundBeautyRevision(sp)
                                }
                            )
                            if (index != slots.lastIndex) InsetDivider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun BackgroundBeautyAssetBlock(
    context: Context,
    sp: SharedPreferences,
    slot: BackgroundBeautySettings.Slot,
    revision: Int,
    onChanged: () -> Unit
) {
    val selected = remember(slot, revision) { BackgroundBeautyStore.hasImage(context, slot) }
    var opacity by remember(slot) {
        mutableStateOf(
            BackgroundBeautySettings.opacity(
                sp.getFloat(
                    BackgroundBeautySettings.opacityKey(slot),
                    BackgroundBeautySettings.DEFAULT_OPACITY
                )
            )
        )
    }
    ActionRow(
        title = slot.title,
        summary = if (selected) "已选择，点击更换或恢复" else slot.summary
    ) {
        val activity = context as? Activity ?: return@ActionRow
        val pickImage = {
            BackgroundBeautyPicker.launch(activity, slot) { result ->
                when (result) {
                    BackgroundBeautyPickResult.SAVED -> {
                        onChanged()
                        Toast.makeText(context, "${slot.title}已更新", Toast.LENGTH_SHORT).show()
                    }
                    BackgroundBeautyPickResult.FAILED -> {
                        Toast.makeText(context, "背景图片无效或读取失败", Toast.LENGTH_SHORT).show()
                    }
                    BackgroundBeautyPickResult.CANCELLED -> Unit
                }
            }
        }
        if (!selected) {
            pickImage()
        } else {
            VoiceForwardMiuixDialog.showChoices(
                activity = activity,
                title = slot.title,
                summary = "管理当前背景图片",
                choices = listOf(
                    "更换背景" to "重新选择一张本地图片",
                    "恢复微信背景" to "删除当前自定义背景"
                ),
                onSelected = { choice ->
                    if (choice == 0) {
                        pickImage()
                    } else if (BackgroundBeautyStore.remove(context, slot)) {
                        onChanged()
                        Toast.makeText(context, "已恢复微信背景", Toast.LENGTH_SHORT).show()
                    }
                },
                onDismiss = {}
            )
        }
    }
    if (selected) {
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)) {
            Text(
                text = "背景不透明度 ${(opacity * 100f).toInt()}%",
                color = MiuixTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
            Slider(
                value = opacity,
                onValueChange = { opacity = BackgroundBeautySettings.opacity(it) },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                valueRange = 0.10f..1.0f,
                steps = 17,
                onValueChangeFinished = {
                    sp.edit()
                        .putFloat(
                            BackgroundBeautySettings.opacityKey(slot),
                            BackgroundBeautySettings.opacity(opacity)
                        )
                        .apply()
                },
                showKeyPoints = true,
                keyPoints = listOf(0.1f, 0.25f, 0.5f, 0.75f, 1.0f)
            )
        }
    }
}

internal fun bumpBackgroundBeautyRevision(sp: SharedPreferences): Int {
    val current = sp.getInt(BackgroundBeautySettings.KEY_REVISION, 0)
    val next = if (current == Int.MAX_VALUE) 1 else current + 1
    sp.edit().putInt(BackgroundBeautySettings.KEY_REVISION, next).apply()
    return next
}

@Composable
internal fun MessageTextColorMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, MessageTextColorSettings.PREFS_NAME) }
    var leftLightColor by remember {
        mutableStateOf(
            sp.getString(
                MessageTextColorSettings.KEY_LEFT_LIGHT_COLOR,
                MessageTextColorSettings.DEFAULT_LEFT_LIGHT_COLOR
            ) ?: MessageTextColorSettings.DEFAULT_LEFT_LIGHT_COLOR
        )
    }
    var rightLightColor by remember {
        mutableStateOf(
            sp.getString(
                MessageTextColorSettings.KEY_RIGHT_LIGHT_COLOR,
                MessageTextColorSettings.DEFAULT_RIGHT_LIGHT_COLOR
            ) ?: MessageTextColorSettings.DEFAULT_RIGHT_LIGHT_COLOR
        )
    }
    var leftDarkColor by remember {
        mutableStateOf(
            sp.getString(
                MessageTextColorSettings.KEY_LEFT_DARK_COLOR,
                MessageTextColorSettings.DEFAULT_LEFT_DARK_COLOR
            ) ?: MessageTextColorSettings.DEFAULT_LEFT_DARK_COLOR
        )
    }
    var rightDarkColor by remember {
        mutableStateOf(
            sp.getString(
                MessageTextColorSettings.KEY_RIGHT_DARK_COLOR,
                MessageTextColorSettings.DEFAULT_RIGHT_DARK_COLOR
            ) ?: MessageTextColorSettings.DEFAULT_RIGHT_DARK_COLOR
        )
    }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()

    PageScaffold(
        title = provider.title(),
        largeTitle = provider.title(),
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "保存设置",
                onPrimaryClick = {
                    val leftLight = MessageTextColorSettings.cleanColorSpec(leftLightColor)
                        .ifEmpty { MessageTextColorSettings.DEFAULT_LEFT_LIGHT_COLOR }
                    val rightLight = MessageTextColorSettings.cleanColorSpec(rightLightColor)
                        .ifEmpty { MessageTextColorSettings.DEFAULT_RIGHT_LIGHT_COLOR }
                    val leftDark = MessageTextColorSettings.cleanColorSpec(leftDarkColor)
                        .ifEmpty { MessageTextColorSettings.DEFAULT_LEFT_DARK_COLOR }
                    val rightDark = MessageTextColorSettings.cleanColorSpec(rightDarkColor)
                        .ifEmpty { MessageTextColorSettings.DEFAULT_RIGHT_DARK_COLOR }
                    sp.edit()
                        .putString(MessageTextColorSettings.KEY_LEFT_LIGHT_COLOR, leftLight)
                        .putString(MessageTextColorSettings.KEY_RIGHT_LIGHT_COLOR, rightLight)
                        .putString(MessageTextColorSettings.KEY_LEFT_DARK_COLOR, leftDark)
                        .putString(MessageTextColorSettings.KEY_RIGHT_DARK_COLOR, rightDark)
                        .apply()
                    leftLightColor = leftLight
                    rightLightColor = rightLight
                    leftDarkColor = leftDark
                    rightDarkColor = rightDark
                    Toast.makeText(context, "设置已保存", Toast.LENGTH_SHORT).show()
                },
                secondaryText = "返回",
                onSecondaryClick = onBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(text = "基础") }
            item {
                SettingsCard {
                    SwitchRow(
                        sp,
                        MessageTextColorSettings.KEY_ENABLE,
                        "启用消息文本颜色",
                        "处理聊天里的文本消息和引用消息正文",
                        MessageTextColorSettings.DEFAULT_ENABLE
                    )
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "颜色") }
            item {
                SettingsCard {
                    ColorPickerRow(
                        "左侧浅色",
                        "对方文本消息，浅色模式",
                        leftLightColor,
                        onReset = { leftLightColor = MessageTextColorSettings.DEFAULT_LEFT_LIGHT_COLOR }
                    ) { leftLightColor = it.take(19) }
                    InsetDivider()
                    ColorPickerRow(
                        "右侧浅色",
                        "自己发送文本消息，浅色模式",
                        rightLightColor,
                        onReset = { rightLightColor = MessageTextColorSettings.DEFAULT_RIGHT_LIGHT_COLOR }
                    ) { rightLightColor = it.take(19) }
                    InsetDivider()
                    ColorPickerRow(
                        "左侧深色",
                        "对方文本消息，深色模式",
                        leftDarkColor,
                        onReset = { leftDarkColor = MessageTextColorSettings.DEFAULT_LEFT_DARK_COLOR }
                    ) { leftDarkColor = it.take(19) }
                    InsetDivider()
                    ColorPickerRow(
                        "右侧深色",
                        "自己发送文本消息，深色模式",
                        rightDarkColor,
                        onReset = { rightDarkColor = MessageTextColorSettings.DEFAULT_RIGHT_DARK_COLOR }
                    ) { rightDarkColor = it.take(19) }
                }
            }
        }
    }
}

@Composable
internal fun HideChatAvatarMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, HideChatAvatarSettings.PREFS_NAME) }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()

    PageScaffold(
        title = provider.title(),
        largeTitle = provider.title(),
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar("返回", onBack)
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(text = "聊天头像") }
            item {
                SettingsCard {
                    SwitchRow(
                        sp,
                        HideChatAvatarSettings.KEY_HIDE_SELF,
                        "隐藏自己的头像",
                        "在群聊和私聊中隐藏自己发送消息的头像",
                        HideChatAvatarSettings.DEFAULT_HIDE_SELF
                    )
                    InsetDivider()
                    SwitchRow(
                        sp,
                        HideChatAvatarSettings.KEY_HIDE_OTHER,
                        "隐藏对方的头像",
                        "在群聊和私聊中隐藏对方发送消息的头像",
                        HideChatAvatarSettings.DEFAULT_HIDE_OTHER
                    )
                }
            }
        }
    }
}

@Composable
internal fun RoundAvatarMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, RoundAvatarSettings.PREFS_NAME) }
    var enabled by remember {
        mutableStateOf(sp.getBoolean(RoundAvatarSettings.KEY_ENABLE, RoundAvatarSettings.DEFAULT_ENABLE))
    }
    var radiusFactor by remember {
        mutableStateOf(
            RoundAvatarSettings.normalizeRadiusFactor(
                sp.getFloat(RoundAvatarSettings.KEY_RADIUS_FACTOR, RoundAvatarSettings.DEFAULT_RADIUS_FACTOR)
            )
        )
    }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()

    PageScaffold(
        title = provider.title(),
        largeTitle = provider.title(),
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar("返回", onBack)
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(text = "全局头像") }
            item {
                SettingsCard {
                    SwitchRow(
                        checked = enabled,
                        title = "启用圆角头像",
                        summary = "统一应用到微信界面和通知头像",
                        onCheckedChange = {
                            enabled = it
                            sp.edit().putBoolean(RoundAvatarSettings.KEY_ENABLE, it).apply()
                        }
                    )
                    if (enabled) {
                        InsetDivider()
                        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp)) {
                            Text(
                                text = "圆角弧度 ${RoundAvatarSettings.displayPercent(radiusFactor)}%",
                                color = MiuixTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Medium
                            )
                            Slider(
                                value = radiusFactor,
                                onValueChange = {
                                    radiusFactor = RoundAvatarSettings.normalizeRadiusFactor(it)
                                },
                                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                                valueRange = RoundAvatarSettings.MIN_RADIUS_FACTOR..RoundAvatarSettings.MAX_RADIUS_FACTOR,
                                steps = 39,
                                onValueChangeFinished = {
                                    sp.edit()
                                        .putFloat(
                                            RoundAvatarSettings.KEY_RADIUS_FACTOR,
                                            RoundAvatarSettings.normalizeRadiusFactor(radiusFactor)
                                        )
                                        .apply()
                                },
                                showKeyPoints = true,
                                keyPoints = listOf(0.1f, 0.2f, 0.3f, 0.4f, 0.5f)
                            )
                        }
                    }
                }
            }
        }
    }
}

internal enum class CustomFriendAvatarRoute {
    MAIN,
    CONTACT_PICKER
}

@Composable
internal fun CustomFriendAvatarMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, CustomFriendAvatarSettings.PREFS_NAME) }
    var enabled by remember {
        mutableStateOf(sp.getBoolean(CustomFriendAvatarSettings.KEY_ENABLE, CustomFriendAvatarSettings.DEFAULT_ENABLE))
    }
    var route by remember { mutableStateOf(CustomFriendAvatarRoute.MAIN) }
    var selectedWxid by remember { mutableStateOf("") }
    var contacts by remember { mutableStateOf(emptyList<ContactOption>()) }
    var revision by remember { mutableStateOf(0) }

    LaunchedEffect(revision) {
        loadContacts(context, ContactPickerMode.BOTH, false) { result, _ -> contacts = result }
    }

    SettingsRouteTransition(
        targetState = route,
        label = "custom_friend_avatar_route",
        depthOf = { if (it == CustomFriendAvatarRoute.MAIN) 0 else 1 }
    ) { current ->
        when (current) {
            CustomFriendAvatarRoute.MAIN -> {
                val selected = contacts.firstOrNull { it.id == selectedWxid }
                val configuredIds = remember(revision) { CustomFriendAvatarStore.configuredFriends(context) }
                val configured = contacts.filter { it.id in configuredIds }
                val listState = rememberLazyListState()
                val scrollBehavior = MiuixScrollBehavior()
                PageScaffold(
                    title = provider.title(),
                    largeTitle = provider.title(),
                    scrollBehavior = scrollBehavior,
                    bottomBar = { BottomActionBar("返回", onBack) }
                ) { padding ->
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
                        state = listState,
                        contentPadding = PaddingValues(
                            top = padding.calculateTopPadding() + 8.dp,
                            bottom = padding.calculateBottomPadding() + 84.dp
                        )
                    ) {
                        item { SmallTitle(text = "自定义头像") }
                        item {
                            SettingsCard {
                                SwitchRow(
                                    checked = enabled,
                                    title = "启用自定义头像",
                                    summary = "仅修改本机显示，不会更改微信中的原头像",
                                    onCheckedChange = {
                                        enabled = it
                                        sp.edit().putBoolean(CustomFriendAvatarSettings.KEY_ENABLE, it).apply()
                                    }
                                )
                                if (enabled) {
                                    InsetDivider()
                                    ActionRow(
                                        title = "选择好友或群聊",
                                        summary = selected?.label ?: "选择要设置头像的好友或群聊",
                                        onClick = { route = CustomFriendAvatarRoute.CONTACT_PICKER }
                                    )
                                    if (selected != null) {
                                        InsetDivider()
                                        ActionRow(
                                            title = if (CustomFriendAvatarStore.hasAvatar(context, selected.id)) "更换头像" else "设置头像",
                                            summary = "从系统相册或文件中选择图片",
                                            onClick = {
                                                val activity = context as? Activity
                                                if (activity == null) {
                                                    Toast.makeText(context, "当前页面无法打开图片选择器", Toast.LENGTH_SHORT).show()
                                                } else {
                                                    CustomFriendAvatarPicker.launch(activity, selected.id) { success ->
                                                        Toast.makeText(
                                                            context,
                                                            if (success) "自定义头像已保存" else "头像设置失败",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        if (success) revision++
                                                    }
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                        if (enabled) {
                            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "应用范围") }
                            item {
                                SettingsCard {
                                    SwitchRow(sp, CustomFriendAvatarSettings.KEY_CHAT, "聊天消息", "替换群聊和私聊消息头像", true)
                                    InsetDivider()
                                    SwitchRow(sp, CustomFriendAvatarSettings.KEY_CONVERSATION, "会话列表", "替换微信首页的好友和群聊头像", true)
                                    InsetDivider()
                                    SwitchRow(sp, CustomFriendAvatarSettings.KEY_CONTACTS, "通讯录", "替换通讯录中的好友和群聊头像", true)
                                    InsetDivider()
                                    SwitchRow(sp, CustomFriendAvatarSettings.KEY_PROFILE, "资料页", "替换好友和群聊资料页中的头像", true)
                                    InsetDivider()
                                    SwitchRow(sp, CustomFriendAvatarSettings.KEY_MOMENTS, "朋友圈", "替换朋友圈中的好友头像", true)
                                    InsetDivider()
                                    SwitchRow(sp, CustomFriendAvatarSettings.KEY_OTHER_UI, "其他微信界面", "替换收藏、搜索等界面中的好友和群聊头像", true)
                                    InsetDivider()
                                    SwitchRow(sp, CustomFriendAvatarSettings.KEY_DESKTOP_SHORTCUT, "桌面快捷方式", "替换添加到桌面的好友和群聊头像", true)
                                    InsetDivider()
                                    SwitchRow(sp, CustomFriendAvatarSettings.KEY_NOTIFICATIONS, "消息通知", "替换微信原生通知和 Hchat 通知中的好友和群聊头像", true)
                                    InsetDivider()
                                    SwitchRow(sp, CustomFriendAvatarSettings.KEY_MOMENTS_NOTIFICATIONS, "朋友圈通知", "替换朋友圈发布通知中的好友头像", true)
                                }
                            }
                            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "快捷入口") }
                            item {
                                SettingsCard {
                                    SwitchRow(sp, CustomFriendAvatarSettings.KEY_CONVERSATION_MENU, "会话列表长按菜单", "长按好友或群聊会话时显示设置头像入口，聊天分组内同样生效", true)
                                }
                            }
                            if (configured.isNotEmpty()) {
                                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "已设置头像") }
                                item {
                                    SettingsCard {
                                        configured.forEachIndexed { index, option ->
                                            CustomFriendAvatarRow(context, option, revision) {
                                                selectedWxid = option.id
                                                val activity = context as? Activity
                                                if (activity != null) {
                                                    VoiceForwardMiuixDialog.showChoices(
                                                        activity = activity,
                                                        title = option.label,
                                                        summary = "管理自定义头像",
                                                        choices = listOf(
                                                            "更换头像" to "重新选择一张本地图片",
                                                            "恢复微信头像" to "删除当前自定义头像"
                                                        ),
                                                        onSelected = { choice ->
                                                            if (choice == 0) {
                                                                CustomFriendAvatarPicker.launch(activity, option.id) { success ->
                                                                    if (success) revision++
                                                                }
                                                            } else {
                                                                CustomFriendAvatarStore.remove(context, option.id)
                                                                revision++
                                                            }
                                                        },
                                                        onDismiss = {}
                                                    )
                                                }
                                            }
                                            if (index != configured.lastIndex) InsetDivider()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            CustomFriendAvatarRoute.CONTACT_PICKER -> ContactPickerPage(
                context = context,
                request = ContactPickerRequest(
                    title = "选择好友或群聊",
                    mode = ContactPickerMode.BOTH,
                    multiSelect = false,
                    existingValue = selectedWxid,
                    onValue = {},
                    enableLabels = false,
                    enableGroupLabels = false,
                    singleConfirmText = "确定"
                ),
                onBack = { route = CustomFriendAvatarRoute.MAIN },
                onConfirm = { selected ->
                    selected.firstOrNull()?.let { selectedWxid = it.id }
                    route = CustomFriendAvatarRoute.MAIN
                }
            )
        }
    }
}

@Composable
internal fun CustomFriendAvatarRow(
    context: Context,
    option: ContactOption,
    revision: Int,
    onClick: () -> Unit
) {
    val bitmap = remember(option.id, revision) {
        CustomFriendAvatarStore.loadBitmap(context, option.id)?.asImageBitmap()
    }
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(42.dp).clip(RoundedCornerShape(12.dp))
                .background(MiuixTheme.colorScheme.secondaryVariant),
            contentAlignment = Alignment.Center
        ) {
            if (bitmap != null) {
                Image(bitmap = bitmap, contentDescription = null, modifier = Modifier.fillMaxSize())
            } else {
                Text(if (option.group) "群" else option.label.take(1).ifEmpty { "友" })
            }
        }
        Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
            Text(option.label, color = MiuixTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
            Text("点击更换或恢复微信头像", color = MiuixTheme.colorScheme.onSurfaceVariantSummary, fontSize = 12.sp)
        }
        Text(text = "›", color = MiuixTheme.colorScheme.onSurfaceVariantSummary, fontSize = 22.sp)
    }
}

@Composable
internal fun MessageBlockMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, MessageBlockSettings.PREFS_NAME) }
    val initialState = remember { initialMessageBlockState(sp) }
    var templates by remember {
        mutableStateOf(initialState.templates)
    }
    var bindings by remember {
        mutableStateOf(initialState.bindings)
    }
    var defaultPrivateRule by remember { mutableStateOf(MessageBlockSettings(context).defaultPrivateRule()) }
    var defaultGroupRule by remember { mutableStateOf(MessageBlockSettings(context).defaultGroupRule()) }
    var defaultOfficialRule by remember { mutableStateOf(MessageBlockSettings(context).defaultOfficialRule()) }
    var templateEditor by remember { mutableStateOf<MessageBlockTemplateEditorRequest?>(null) }
    var bindingEditor by remember { mutableStateOf<MessageBlockBindingEditorRequest?>(null) }
    var batchBindingEditor by remember { mutableStateOf<MessageBlockBatchBindingEditorRequest?>(null) }
    var bindingPicker by remember { mutableStateOf<ContactPickerRequest?>(null) }
    var groupMemberBindingPicker by remember { mutableStateOf<GroupMemberPickerRequest?>(null) }
    var defaultRuleEditor by remember { mutableStateOf<MessageBlockDefaultRuleKind?>(null) }
    var showTemplateManager by remember { mutableStateOf(false) }
    var showListManager by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()

    LaunchedEffect(Unit) {
        if (initialState.shouldPersist) {
            sp.edit()
                .putString(MessageBlockSettings.KEY_TEMPLATES, MessageBlockSettings.encodeTemplates(templates))
                .putString(MessageBlockSettings.KEY_BINDINGS, MessageBlockSettings.encodeBindings(bindings))
                .apply()
        }
    }

    val route = when {
        templateEditor != null -> MessageBlockRoute.TemplateEditor(templateEditor!!)
        bindingEditor != null -> MessageBlockRoute.BindingEditor(bindingEditor!!)
        batchBindingEditor != null -> MessageBlockRoute.BatchBindingEditor(batchBindingEditor!!)
        bindingPicker != null -> MessageBlockRoute.ContactPicker(bindingPicker!!)
        groupMemberBindingPicker != null -> MessageBlockRoute.GroupMemberPicker(groupMemberBindingPicker!!)
        defaultRuleEditor != null -> MessageBlockRoute.DefaultRuleEditor(defaultRuleEditor!!)
        showTemplateManager -> MessageBlockRoute.TemplateManager
        showListManager -> MessageBlockRoute.ListManager
        else -> MessageBlockRoute.Main
    }

    SettingsRouteTransition(
        targetState = route,
        label = "MessageBlockRouteTransition",
        depthOf = { it.depth() }
    ) { currentRoute ->
        when (currentRoute) {
            is MessageBlockRoute.TemplateEditor -> {
                val request = currentRoute.request
                MessageBlockTemplatePage(
                    context = context,
                    request = request,
                    onBack = { templateEditor = null },
                    onSave = { updated ->
                        val next = if (request.index in templates.indices) {
                            templates.toMutableList().also { it[request.index] = MessageBlockSettings.clearTemplateTargets(updated) }
                        } else {
                            templates + MessageBlockSettings.clearTemplateTargets(updated)
                        }
                        templates = next
                        sp.edit().putString(MessageBlockSettings.KEY_TEMPLATES, MessageBlockSettings.encodeTemplates(next)).apply()
                        templateEditor = null
                    },
                    onDelete = {
                        if (request.index in templates.indices) {
                            val deletedId = templates[request.index].id
                            val next = templates.toMutableList().also { it.removeAt(request.index) }
                            val nextBindings = bindings.map { binding ->
                                binding.copy(templateIds = binding.templateIds - deletedId)
                            }
                            fun withoutDeletedTemplate(rule: MessageBlockDefaultRule): MessageBlockDefaultRule {
                                val nextIds = rule.templateIds - deletedId
                                return rule.copy(
                                    enabled = rule.enabled && (rule.customRules || nextIds.isNotEmpty()),
                                    templateIds = nextIds
                                )
                            }
                            val nextDefaultPrivate = withoutDeletedTemplate(defaultPrivateRule)
                            val nextDefaultGroup = withoutDeletedTemplate(defaultGroupRule)
                            val nextDefaultOfficial = withoutDeletedTemplate(defaultOfficialRule)
                            templates = next
                            bindings = nextBindings
                            defaultPrivateRule = nextDefaultPrivate
                            defaultGroupRule = nextDefaultGroup
                            defaultOfficialRule = nextDefaultOfficial
                            sp.edit()
                                .putString(MessageBlockSettings.KEY_TEMPLATES, MessageBlockSettings.encodeTemplates(next))
                                .putString(MessageBlockSettings.KEY_BINDINGS, MessageBlockSettings.encodeBindings(nextBindings))
                                .putString(
                                    MessageBlockSettings.KEY_DEFAULT_PRIVATE,
                                    MessageBlockSettings.encodeDefaultRule(nextDefaultPrivate, group = false)
                                )
                                .putString(
                                    MessageBlockSettings.KEY_DEFAULT_GROUP,
                                    MessageBlockSettings.encodeDefaultRule(nextDefaultGroup, group = true)
                                )
                                .putString(
                                    MessageBlockSettings.KEY_DEFAULT_OFFICIAL,
                                    MessageBlockSettings.encodeDefaultRule(
                                        nextDefaultOfficial,
                                        group = false,
                                        official = true
                                    )
                                )
                                .apply()
                        }
                        templateEditor = null
                    }
                )
            }
            is MessageBlockRoute.BindingEditor -> {
                val request = currentRoute.request
                MessageBlockBindingPage(
                    context = context,
                    request = request,
                    templates = templates,
                    onBack = { bindingEditor = null },
                    onSave = { updated ->
                        val normalized = normalizedMessageBlockBinding(updated)
                        val base = if (request.index in bindings.indices) {
                            bindings.toMutableList().also { it.removeAt(request.index) }
                        } else {
                            bindings
                        }
                        val next = upsertMessageBlockBindings(base, listOf(normalized))
                        bindings = next
                        sp.edit().putString(MessageBlockSettings.KEY_BINDINGS, MessageBlockSettings.encodeBindings(next)).apply()
                        bindingEditor = null
                    },
                    onDelete = {
                        if (request.index in bindings.indices) {
                            val next = bindings.toMutableList().also { it.removeAt(request.index) }
                            bindings = next
                            sp.edit().putString(MessageBlockSettings.KEY_BINDINGS, MessageBlockSettings.encodeBindings(next)).apply()
                        }
                        bindingEditor = null
                    }
                )
            }
            is MessageBlockRoute.BatchBindingEditor -> {
                val request = currentRoute.request
                MessageBlockBatchBindingPage(
                    context = context,
                    request = request,
                    templates = templates,
                    onBack = { batchBindingEditor = null },
                    onSave = { updated ->
                        val next = upsertMessageBlockBindings(bindings, updated)
                        bindings = next
                        sp.edit().putString(MessageBlockSettings.KEY_BINDINGS, MessageBlockSettings.encodeBindings(next)).apply()
                        Toast.makeText(context, "已保存 ${updated.size} 个名单项", Toast.LENGTH_SHORT).show()
                        batchBindingEditor = null
                    }
                )
            }
            is MessageBlockRoute.DefaultRuleEditor -> {
                val rule = when (currentRoute.kind) {
                    MessageBlockDefaultRuleKind.PRIVATE -> defaultPrivateRule
                    MessageBlockDefaultRuleKind.GROUP -> defaultGroupRule
                    MessageBlockDefaultRuleKind.OFFICIAL -> defaultOfficialRule
                }
                MessageBlockDefaultRulePage(
                    context = context,
                    rule = rule,
                    templates = templates,
                    onBack = { defaultRuleEditor = null },
                    onSave = { updated ->
                        val normalized = when (currentRoute.kind) {
                            MessageBlockDefaultRuleKind.PRIVATE -> updated.copy(group = false, official = false, label = "默认私聊规则")
                            MessageBlockDefaultRuleKind.GROUP -> updated.copy(group = true, official = false, label = "默认群聊规则")
                            MessageBlockDefaultRuleKind.OFFICIAL -> updated.copy(group = false, official = true, label = "默认公众号规则")
                        }
                        when (currentRoute.kind) {
                            MessageBlockDefaultRuleKind.PRIVATE -> {
                                defaultPrivateRule = normalized
                                MessageBlockSettings(context).saveDefaultPrivateRule(normalized)
                            }
                            MessageBlockDefaultRuleKind.GROUP -> {
                                defaultGroupRule = normalized
                                MessageBlockSettings(context).saveDefaultGroupRule(normalized)
                            }
                            MessageBlockDefaultRuleKind.OFFICIAL -> {
                                defaultOfficialRule = normalized
                                MessageBlockSettings(context).saveDefaultOfficialRule(normalized)
                            }
                        }
                        Toast.makeText(context, "默认规则已保存", Toast.LENGTH_SHORT).show()
                        defaultRuleEditor = null
                    }
                )
            }
            is MessageBlockRoute.ContactPicker -> {
                val request = currentRoute.request
                ContactPickerPage(
                    context = context,
                    request = request,
                    onBack = { bindingPicker = null },
                    onConfirm = { selected ->
                        val additions = selected.map {
                            messageBlockBindingFromContact(
                                option = it,
                                templates = templates,
                                existing = findMessageBlockBinding(bindings, MessageBlockSettings.TARGET_CONTACT, it.id)
                            )
                        }
                        if (additions.size == 1) {
                            val existingIndex = bindings.indexOfFirst {
                                it.targetType == additions.first().targetType &&
                                    it.targetId == additions.first().targetId
                            }
                            bindingEditor = MessageBlockBindingEditorRequest(
                                index = existingIndex.takeIf { it >= 0 } ?: bindings.size,
                                binding = additions.first(),
                                canDelete = existingIndex >= 0
                            )
                        } else if (additions.isNotEmpty()) {
                            batchBindingEditor = MessageBlockBatchBindingEditorRequest(
                                title = "批量添加名单",
                                bindings = additions
                            )
                        }
                        bindingPicker = null
                    }
                )
            }
            is MessageBlockRoute.GroupMemberPicker -> {
                val request = currentRoute.request
                GroupMemberPickerPage(
                    context = context,
                    request = request,
                    onBack = { groupMemberBindingPicker = null },
                    onConfirm = { selected ->
                        val additions = selected.map {
                            messageBlockBindingFromGroupMember(
                                entry = it,
                                templates = templates,
                                existing = findMessageBlockBinding(bindings, MessageBlockSettings.TARGET_GROUP_MEMBER, it)
                            )
                        }
                        if (additions.size == 1) {
                            val binding = additions.first()
                            val existingIndex = bindings.indexOfFirst {
                                it.targetType == binding.targetType &&
                                    it.targetId == binding.targetId
                            }
                            bindingEditor = MessageBlockBindingEditorRequest(
                                index = existingIndex.takeIf { it >= 0 } ?: bindings.size,
                                binding = binding,
                                canDelete = existingIndex >= 0
                            )
                        } else if (additions.isNotEmpty()) {
                            batchBindingEditor = MessageBlockBatchBindingEditorRequest(
                                title = "批量添加群成员",
                                bindings = additions
                            )
                        }
                        groupMemberBindingPicker = null
                    }
                )
            }
            MessageBlockRoute.TemplateManager -> {
                MessageBlockTemplateListPage(
                    templates = templates,
                    onBack = { showTemplateManager = false },
                    onOpenTemplate = { index, template ->
                        templateEditor = MessageBlockTemplateEditorRequest(index, template, canDelete = true)
                    },
                    onAddTemplate = {
                        templateEditor = MessageBlockTemplateEditorRequest(
                            templates.size,
                            newMessageBlockTemplate(templates.size + 1),
                            canDelete = false
                        )
                    }
                )
            }
            MessageBlockRoute.ListManager -> {
                MessageBlockListManagerPage(
                    bindings = bindings,
                    templates = templates,
                    onBack = { showListManager = false },
                    onOpenBinding = { index, binding ->
                        bindingEditor = MessageBlockBindingEditorRequest(index, binding, canDelete = true)
                    },
                    onAddContact = {
                        bindingPicker = ContactPickerRequest(
                            title = "选择名单",
                            mode = ContactPickerMode.ALL_CHATS,
                            multiSelect = true,
                            existingValue = "",
                            onValue = {},
                            enableLabels = true
                        )
                    },
                    onAddGroupMember = {
                        groupMemberBindingPicker = GroupMemberPickerRequest(
                            title = "选择群成员",
                            existingValue = "",
                            onValue = {}
                        )
                    },
                    onDeleteBindings = { targets ->
                        if (targets.isEmpty()) {
                            Toast.makeText(context, "没有可删除的名单", Toast.LENGTH_SHORT).show()
                        } else {
                            val removeKeys = targets.map {
                                MessageBlockSettings.bindingKey(it.targetType, it.targetId)
                            }.toSet()
                            val next = bindings.filterNot {
                                removeKeys.contains(MessageBlockSettings.bindingKey(it.targetType, it.targetId))
                            }
                            bindings = next
                            sp.edit().putString(MessageBlockSettings.KEY_BINDINGS, MessageBlockSettings.encodeBindings(next)).apply()
                            Toast.makeText(context, "已删除 ${targets.size} 个名单项", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
            MessageBlockRoute.Main -> PageScaffold(
        title = provider.title(),
        largeTitle = provider.title(),
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar("返回", onBack)
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(text = "基础") }
            item {
                SettingsCard {
                    SwitchRow(
                        sp,
                        MessageBlockSettings.KEY_ENABLE,
                        "启用屏蔽消息",
                        "总开关开启后，仅命中启用模板的新消息会被拦截",
                        MessageBlockSettings.DEFAULT_ENABLE
                    )
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "管理") }
            item {
                SettingsCard {
                    SelectRow(
                        title = "默认私聊规则",
                        summary = describeMessageBlockDefaultRule(defaultPrivateRule, templates),
                        onClick = { defaultRuleEditor = MessageBlockDefaultRuleKind.PRIVATE }
                    )
                    InsetDivider()
                    SelectRow(
                        title = "默认群聊规则",
                        summary = describeMessageBlockDefaultRule(defaultGroupRule, templates),
                        onClick = { defaultRuleEditor = MessageBlockDefaultRuleKind.GROUP }
                    )
                    InsetDivider()
                    SelectRow(
                        title = "默认公众号规则",
                        summary = describeMessageBlockDefaultRule(defaultOfficialRule, templates),
                        onClick = { defaultRuleEditor = MessageBlockDefaultRuleKind.OFFICIAL }
                    )
                    InsetDivider()
                    SelectRow(
                        title = "模板管理",
                        summary = if (templates.isEmpty()) "暂无模板，进入后添加屏蔽规则" else "${templates.size} 个模板，进入后添加或修改规则",
                        onClick = { showTemplateManager = true }
                    )
                    InsetDivider()
                    SelectRow(
                        title = "名单管理",
                        summary = if (bindings.isEmpty()) "暂无名单，进入后添加好友、群聊、公众号或群成员" else "${bindings.size} 个名单项，进入后分配模板",
                        onClick = { showListManager = true }
                    )
                }
            }
        }
    }
        }
    }
}

@Composable
internal fun MessageBlockTemplateListPage(
    templates: List<MessageBlockTemplate>,
    onBack: () -> Unit,
    onOpenTemplate: (Int, MessageBlockTemplate) -> Unit,
    onAddTemplate: () -> Unit
) {
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    PageScaffold(
        title = "模板管理",
        largeTitle = "模板管理",
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "新增模板",
                onPrimaryClick = onAddTemplate,
                secondaryText = "返回",
                onSecondaryClick = onBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(text = "模板") }
            item {
                SettingsCard {
                    if (templates.isEmpty()) {
                        EmptyText("暂无模板。模板只配置消息类型和关键词，名单在“名单管理”里分配模板。")
                    } else {
                        templates.forEachIndexed { index, template ->
                            SelectRow(
                                title = template.name.ifBlank { "模板 ${index + 1}" },
                                summary = describeMessageBlockTemplate(template),
                                onClick = { onOpenTemplate(index, template) }
                            )
                            if (index < templates.lastIndex) InsetDivider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun MessageBlockListManagerPage(
    bindings: List<MessageBlockBinding>,
    templates: List<MessageBlockTemplate>,
    onBack: () -> Unit,
    onOpenBinding: (Int, MessageBlockBinding) -> Unit,
    onAddContact: () -> Unit,
    onAddGroupMember: () -> Unit,
    onDeleteBindings: (List<MessageBlockBinding>) -> Unit
) {
    val context = LocalContext.current
    var category by remember { mutableStateOf(ConversationRuleCategory.ALL) }
    var query by remember { mutableStateOf("") }
    var batchDeleteMode by remember { mutableStateOf(false) }
    var selectedIds by remember { mutableStateOf<Set<String>>(emptySet()) }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    val lower = query.trim().lowercase(Locale.US)
    val visibleBindings = bindings.mapIndexed { index, binding -> index to binding }
        .filter { (_, binding) ->
            conversationRuleCategoryMatches(binding, category) &&
                (lower.isEmpty() || messageBlockBindingMatchesQuery(binding, templates, lower))
        }
    val listTitle = when {
        bindings.isEmpty() -> "名单"
        lower.isEmpty() -> "名单 · ${bindings.size} 项"
        else -> "名单 · ${visibleBindings.size}/${bindings.size} 项"
    }
    val visibleIds = visibleBindings.mapTo(LinkedHashSet()) { it.second.id }
    val allVisibleSelected = visibleIds.isNotEmpty() && visibleIds.all { it in selectedIds }
    val selectedBindings = bindings.filter { it.id in selectedIds }
    PageScaffold(
        title = "名单管理",
        largeTitle = "名单管理",
        scrollBehavior = scrollBehavior,
        bottomBar = {
            if (batchDeleteMode) {
                BottomActionBar(
                    primaryText = "删除所选（${selectedBindings.size}）",
                    onPrimaryClick = {
                        if (selectedBindings.isEmpty()) {
                            Toast.makeText(context, "请先选择名单", Toast.LENGTH_SHORT).show()
                        } else {
                            showDeleteConfirm = true
                        }
                    },
                    secondaryText = "取消",
                    onSecondaryClick = {
                        batchDeleteMode = false
                        selectedIds = emptySet()
                    },
                    middleText = if (visibleIds.isEmpty()) null else if (allVisibleSelected) "取消全选" else "全选",
                    onMiddleClick = if (visibleIds.isEmpty()) null else {
                        {
                            selectedIds = if (allVisibleSelected) {
                                selectedIds - visibleIds
                            } else {
                                selectedIds + visibleIds
                            }
                        }
                    }
                )
            } else {
                BottomActionBar(
                    primaryText = "返回",
                    onPrimaryClick = onBack,
                    middleText = if (bindings.isEmpty()) null else "批量删除",
                    onMiddleClick = if (bindings.isEmpty()) null else {
                        {
                            batchDeleteMode = true
                            selectedIds = emptySet()
                        }
                    }
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item {
                SettingsCard {
                    ConversationRuleCategoryTabs(
                        selected = category,
                        onSelected = { category = it }
                    )
                }
            }
            item {
                SettingsCard {
                    InputRow(
                        title = "搜索名单",
                        summary = "昵称 / wxid / 群号 / 模板名",
                        value = query,
                        onValueChange = { query = it }
                    )
                }
            }
            if (!batchDeleteMode) {
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "添加") }
                item {
                    SettingsCard {
                        ActionRow("添加好友/群聊/公众号", "批量添加后可一次选择模板") {
                            onAddContact()
                        }
                        InsetDivider()
                        ActionRow("添加群成员", "批量添加后可一次选择模板") {
                            onAddGroupMember()
                        }
                    }
                }
            }
            item { SmallTitle(text = listTitle) }
            item {
                SettingsCard {
                    if (bindings.isEmpty()) {
                        EmptyText("暂无名单。添加好友、群聊、公众号或群成员后，再给名单分配模板。")
                    } else if (visibleBindings.isEmpty()) {
                        EmptyText("没有匹配名单。可按昵称、ID 或模板名搜索。")
                    } else {
                        visibleBindings.forEachIndexed { rowIndex, (index, binding) ->
                            if (batchDeleteMode) {
                                OptionChoiceRow(
                                    item = OptionItem(
                                        label = binding.label.ifBlank { binding.targetId },
                                        value = index,
                                        summary = describeMessageBlockBinding(binding, templates)
                                    ),
                                    selected = binding.id in selectedIds,
                                    onClick = {
                                        selectedIds = if (binding.id in selectedIds) {
                                            selectedIds - binding.id
                                        } else {
                                            selectedIds + binding.id
                                        }
                                    }
                                )
                            } else {
                                SelectRow(
                                    title = binding.label.ifBlank { binding.targetId },
                                    summary = describeMessageBlockBinding(binding, templates),
                                    onClick = { onOpenBinding(index, binding) }
                                )
                            }
                            if (rowIndex < visibleBindings.lastIndex) InsetDivider()
                        }
                    }
                }
            }
        }
    }
    BatchDeleteConfirmDialog(
        show = showDeleteConfirm,
        message = "将删除已选的 ${selectedBindings.size} 个名单项，此操作不可撤销。",
        labels = selectedBindings.map { it.label.ifBlank { it.targetId } },
        onDismiss = { showDeleteConfirm = false },
        onConfirm = {
            val targets = selectedBindings
            showDeleteConfirm = false
            batchDeleteMode = false
            selectedIds = emptySet()
            onDeleteBindings(targets)
        }
    )
}

@Composable
internal fun MessageBlockBatchBindingPage(
    context: Context,
    request: MessageBlockBatchBindingEditorRequest,
    templates: List<MessageBlockTemplate>,
    onBack: () -> Unit,
    onSave: (List<MessageBlockBinding>) -> Unit
) {
    var enabled by remember(request) { mutableStateOf(true) }
    var action by remember(request) { mutableStateOf(MessageBlockSettings.ACTION_BLOCK) }
    var templateIds by remember(request) { mutableStateOf(defaultMessageBlockTemplateIds(templates)) }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    PageScaffold(
        title = request.title,
        largeTitle = request.title,
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "保存名单",
                onPrimaryClick = {
                    if (templateIds.isEmpty()) {
                        Toast.makeText(context, "请先选择模板", Toast.LENGTH_SHORT).show()
                    } else {
                        onSave(
                            request.bindings.map {
                                it.copy(
                                    enabled = enabled,
                                    action = action,
                                    templateIds = templateIds,
                                    customRules = false,
                                    typeAll = false,
                                    types = emptySet(),
                                    textKeywords = ""
                                )
                            }
                        )
                    }
                },
                secondaryText = "返回",
                onSecondaryClick = onBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(text = "批量配置") }
            item {
                SettingsCard {
                    SelectRow(
                        title = "已选择",
                        summary = "${request.bindings.size} 个名单项",
                        onClick = {}
                    )
                    InsetDivider()
                    SwitchRow(
                        checked = enabled,
                        title = "启用名单",
                        summary = "关闭后保留名单但不参与匹配",
                        onCheckedChange = { enabled = it }
                    )
                    InsetDivider()
                    SwitchRow(
                        checked = action == MessageBlockSettings.ACTION_EXCLUDE,
                        title = "排除名单",
                        summary = "开启后这些名单命中时跳过屏蔽",
                        onCheckedChange = {
                            action = if (it) MessageBlockSettings.ACTION_EXCLUDE else MessageBlockSettings.ACTION_BLOCK
                        }
                    )
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "模板") }
            item {
                SettingsCard {
                    if (templates.isEmpty()) {
                        EmptyText("暂无模板。先新增模板，再回来批量添加名单。")
                    } else {
                        templates.forEachIndexed { index, template ->
                            SwitchRow(
                                checked = templateIds.contains(template.id),
                                title = template.name.ifBlank { "模板 ${index + 1}" },
                                summary = describeMessageBlockTemplate(template),
                                onCheckedChange = { checked ->
                                    templateIds = if (checked) {
                                        templateIds + template.id
                                    } else {
                                        templateIds - template.id
                                    }
                                }
                            )
                            if (index < templates.lastIndex) InsetDivider()
                        }
                    }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "名单") }
            item {
                SettingsCard {
                    request.bindings.take(30).forEachIndexed { index, binding ->
                        SelectRow(
                            title = binding.label.ifBlank { binding.targetId },
                            summary = messageBlockBindingTargetSummary(binding),
                            onClick = {}
                        )
                        if (index < minOf(request.bindings.size, 30) - 1) InsetDivider()
                    }
                    if (request.bindings.size > 30) {
                        InsetDivider()
                        EmptyText("还有 ${request.bindings.size - 30} 个名单项未展开显示")
                    }
                }
            }
        }
    }
}

@Composable
internal fun MessageBlockDefaultRulePage(
    context: Context,
    rule: MessageBlockDefaultRule,
    templates: List<MessageBlockTemplate>,
    onBack: () -> Unit,
    onSave: (MessageBlockDefaultRule) -> Unit
) {
    var enabled by remember(rule) { mutableStateOf(rule.enabled) }
    var templateIds by remember(rule) { mutableStateOf(rule.templateIds) }
    var customRules by remember(rule) { mutableStateOf(rule.customRules) }
    var typeAll by remember(rule) { mutableStateOf(rule.typeAll) }
    var types by remember(rule) { mutableStateOf(rule.types) }
    var textKeywords by remember(rule) { mutableStateOf(rule.textKeywords) }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    val title = when {
        rule.official -> "默认公众号规则"
        rule.group -> "默认群聊规则"
        else -> "默认私聊规则"
    }
    val scopeSummary = when {
        rule.official -> "未配置名单的公众号按此规则屏蔽"
        rule.group -> "未配置名单的群聊按此规则屏蔽"
        else -> "未配置名单的私聊按此规则屏蔽"
    }
    fun copySelectedTemplateRules() {
        val rules = messageBlockRuleStateFromTemplates(templateIds, templates)
        typeAll = rules.typeAll
        types = rules.types
        textKeywords = rules.textKeywords
    }

    PageScaffold(
        title = title,
        largeTitle = title,
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "保存规则",
                onPrimaryClick = {
                    if (enabled && !customRules && templateIds.isEmpty()) {
                        Toast.makeText(context, "请先选择模板", Toast.LENGTH_SHORT).show()
                    } else {
                        onSave(
                            rule.copy(
                                enabled = enabled,
                                templateIds = templateIds,
                                customRules = customRules,
                                typeAll = typeAll,
                                types = types,
                                textKeywords = if (customRules && !typeAll && types.contains(MessageBlockSettings.TYPE_TEXT)) textKeywords else ""
                            )
                        )
                    }
                },
                secondaryText = "返回",
                onSecondaryClick = onBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(text = "默认规则") }
            item {
                SettingsCard {
                    SwitchRow(
                        checked = enabled,
                        title = "启用默认规则",
                        summary = scopeSummary,
                        onCheckedChange = { enabled = it }
                    )
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "模板") }
            item {
                SettingsCard {
                    if (templates.isEmpty()) {
                        EmptyText("暂无模板。先新增模板，或开启下方专属规则。")
                    } else {
                        templates.forEachIndexed { index, template ->
                            SwitchRow(
                                checked = templateIds.contains(template.id),
                                title = template.name.ifBlank { "模板 ${index + 1}" },
                                summary = describeMessageBlockTemplate(template),
                                onCheckedChange = { checked ->
                                    templateIds = if (checked) {
                                        templateIds + template.id
                                    } else {
                                        templateIds - template.id
                                    }
                                    if (!customRules) copySelectedTemplateRules()
                                }
                            )
                            if (index < templates.lastIndex) InsetDivider()
                        }
                    }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "规则") }
            item {
                SettingsCard {
                    SwitchRow(
                        checked = customRules,
                        title = "默认专属规则",
                        summary = "开启后不依赖模板，直接按下方类型和关键词屏蔽",
                        onCheckedChange = { checked ->
                            customRules = checked
                            if (checked && !typeAll && types.isEmpty() && textKeywords.isBlank()) {
                                copySelectedTemplateRules()
                            }
                        }
                    )
                    if (customRules) {
                        InsetDivider()
                        ActionRow("套用已选模板规则", "把上方模板的类型和关键词复制到这里") {
                            copySelectedTemplateRules()
                        }
                        InsetDivider()
                        SwitchRow(
                            checked = typeAll,
                            title = "所有消息",
                            summary = "开启后默认范围内全部类型生效",
                            onCheckedChange = {
                                typeAll = it
                                if (it) textKeywords = ""
                            }
                        )
                        if (!typeAll) {
                            messageBlockTypeOptions().forEach { option ->
                                InsetDivider()
                                SwitchRow(
                                    checked = types.contains(option.key),
                                    title = option.title,
                                    summary = option.summary,
                                    onCheckedChange = { checked ->
                                        types = if (checked) {
                                            types + option.key
                                        } else {
                                            if (option.key == MessageBlockSettings.TYPE_TEXT) textKeywords = ""
                                            types - option.key
                                        }
                                    }
                                )
                            }
                            if (types.contains(MessageBlockSettings.TYPE_TEXT)) {
                                InsetDivider()
                                InputRow(
                                    "文字关键词",
                                    "文字和文字引用消息生效；多个用 |、逗号或换行分隔，留空则全部命中",
                                    textKeywords,
                                    minLines = 2
                                ) { textKeywords = it }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun MessageBlockTemplatePage(
    context: Context,
    request: MessageBlockTemplateEditorRequest,
    onBack: () -> Unit,
    onSave: (MessageBlockTemplate) -> Unit,
    onDelete: () -> Unit
) {
    var name by remember(request) { mutableStateOf(request.template.name) }
    var enabled by remember(request) { mutableStateOf(request.template.enabled) }
    var typeAll by remember(request) { mutableStateOf(request.template.typeAll) }
    var types by remember(request) { mutableStateOf(request.template.types) }
    var textKeywords by remember(request) { mutableStateOf(request.template.textKeywords) }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()

    PageScaffold(
        title = name.ifBlank { "模板" },
        largeTitle = name.ifBlank { "模板" },
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "保存模板",
                onPrimaryClick = {
                    onSave(
                        MessageBlockTemplate(
                            id = request.template.id,
                            name = name.trim().ifBlank { "模板 ${request.index + 1}" },
                            enabled = enabled,
                            mode = MessageBlockSettings.MODE_TARGETS,
                            targets = "",
                            targetGroupMembers = "",
                            excludes = "",
                            excludeGroupMembers = "",
                            typeAll = typeAll,
                            types = types,
                            textKeywords = if (!typeAll && types.contains(MessageBlockSettings.TYPE_TEXT)) textKeywords else ""
                        )
                    )
                    Toast.makeText(context, "模板已保存", Toast.LENGTH_SHORT).show()
                },
                secondaryText = "返回",
                onSecondaryClick = onBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(text = "模板") }
            item {
                SettingsCard {
                    InputRow("模板名称", "用于区分不同屏蔽模板", name) { name = it }
                    InsetDivider()
                    SwitchRow(
                        checked = enabled,
                        title = "启用模板",
                        summary = "关闭后使用该模板的名单不会按此规则屏蔽",
                        onCheckedChange = { enabled = it }
                    )
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "类型") }
            item {
                SettingsCard {
                    SwitchRow(
                        checked = typeAll,
                        title = "所有消息",
                        summary = "开启后该模板命中范围内全部类型生效",
                        onCheckedChange = {
                            typeAll = it
                            if (it) textKeywords = ""
                        }
                    )
                    if (!typeAll) {
                        messageBlockTypeOptions().forEach { option ->
                            InsetDivider()
                            SwitchRow(
                                checked = types.contains(option.key),
                                title = option.title,
                                summary = option.summary,
                                onCheckedChange = { checked ->
                                    types = if (checked) {
                                        types + option.key
                                    } else {
                                        if (option.key == MessageBlockSettings.TYPE_TEXT) textKeywords = ""
                                        types - option.key
                                    }
                                }
                            )
                        }
                        if (types.contains(MessageBlockSettings.TYPE_TEXT)) {
                            InsetDivider()
                            InputRow(
                                "文字关键词",
                                "文字和文字引用消息生效；多个用 |、逗号或换行分隔，留空则全部命中",
                                textKeywords,
                                minLines = 2
                            ) { textKeywords = it }
                        }
                    }
                }
            }
            if (request.canDelete) {
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "操作") }
                item {
                    SettingsCard {
                        ActionRow("删除模板", "删除后立即从模板列表移除") {
                            onDelete()
                            Toast.makeText(context, "模板已删除", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun MessageBlockBindingPage(
    context: Context,
    request: MessageBlockBindingEditorRequest,
    templates: List<MessageBlockTemplate>,
    onBack: () -> Unit,
    onSave: (MessageBlockBinding) -> Unit,
    onDelete: () -> Unit
) {
    var enabled by remember(request) { mutableStateOf(request.binding.enabled) }
    var action by remember(request) { mutableStateOf(request.binding.action) }
    var templateIds by remember(request) { mutableStateOf(request.binding.templateIds) }
    var customRules by remember(request) { mutableStateOf(request.binding.customRules) }
    var typeAll by remember(request) { mutableStateOf(request.binding.typeAll) }
    var types by remember(request) { mutableStateOf(request.binding.types) }
    var textKeywords by remember(request) { mutableStateOf(request.binding.textKeywords) }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    val targetTitle = request.binding.label.ifBlank { request.binding.targetId }
    fun copySelectedTemplateRules() {
        val rules = messageBlockRuleStateFromTemplates(templateIds, templates)
        typeAll = rules.typeAll
        types = rules.types
        textKeywords = rules.textKeywords
    }

    PageScaffold(
        title = targetTitle,
        largeTitle = targetTitle,
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "保存名单",
                onPrimaryClick = {
                    onSave(
                        request.binding.copy(
                            enabled = enabled,
                            action = action,
                            templateIds = templateIds,
                            customRules = customRules,
                            typeAll = typeAll,
                            types = types,
                            textKeywords = if (customRules && !typeAll && types.contains(MessageBlockSettings.TYPE_TEXT)) textKeywords else ""
                        )
                    )
                    Toast.makeText(context, "名单已保存", Toast.LENGTH_SHORT).show()
                },
                secondaryText = "返回",
                onSecondaryClick = onBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(text = "名单信息") }
            item {
                SettingsCard {
                    SelectRow(
                        title = targetTitle,
                        summary = messageBlockBindingTargetSummary(request.binding),
                        onClick = {}
                    )
                    InsetDivider()
                    SwitchRow(
                        checked = enabled,
                        title = "启用名单",
                        summary = "关闭后该名单不参与屏蔽消息匹配",
                        onCheckedChange = { enabled = it }
                    )
                    InsetDivider()
                    SwitchRow(
                        checked = action == MessageBlockSettings.ACTION_EXCLUDE,
                        title = "排除名单",
                        summary = "开启后命中该名单时跳过屏蔽；关闭时按下方模板屏蔽",
                        onCheckedChange = {
                            action = if (it) MessageBlockSettings.ACTION_EXCLUDE else MessageBlockSettings.ACTION_BLOCK
                        }
                    )
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "模板") }
            item {
                SettingsCard {
                    if (templates.isEmpty()) {
                        EmptyText("暂无模板。先新增模板，再回到这里选择。")
                    } else {
                        templates.forEachIndexed { index, template ->
                            SwitchRow(
                                checked = templateIds.contains(template.id),
                                title = template.name.ifBlank { "模板 ${index + 1}" },
                                summary = describeMessageBlockTemplate(template),
                                onCheckedChange = { checked ->
                                    templateIds = if (checked) {
                                        templateIds + template.id
                                    } else {
                                        templateIds - template.id
                                    }
                                    if (!customRules) copySelectedTemplateRules()
                                }
                            )
                            if (index < templates.lastIndex) InsetDivider()
                        }
                    }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "规则") }
            item {
                SettingsCard {
                    SwitchRow(
                        checked = customRules,
                        title = "本名单专属规则",
                        summary = if (customRules) {
                            "开启后只按下方勾选屏蔽，不改动模板本身"
                        } else {
                            "关闭时跟随上方模板；开启后可直接给这个名单勾选文字、红包等类型"
                        },
                        onCheckedChange = { checked ->
                            customRules = checked
                            if (checked && !typeAll && types.isEmpty() && textKeywords.isBlank()) {
                                copySelectedTemplateRules()
                            }
                        }
                    )
                    if (customRules) {
                        InsetDivider()
                        ActionRow("套用已选模板规则", "把上方模板的类型和关键词复制到这里") {
                            copySelectedTemplateRules()
                        }
                        InsetDivider()
                        SwitchRow(
                            checked = typeAll,
                            title = "所有消息",
                            summary = "开启后该名单命中范围内全部类型生效",
                            onCheckedChange = {
                                typeAll = it
                                if (it) textKeywords = ""
                            }
                        )
                        if (!typeAll) {
                            messageBlockTypeOptions().forEach { option ->
                                InsetDivider()
                                SwitchRow(
                                    checked = types.contains(option.key),
                                    title = option.title,
                                    summary = option.summary,
                                    onCheckedChange = { checked ->
                                        types = if (checked) {
                                            types + option.key
                                        } else {
                                            if (option.key == MessageBlockSettings.TYPE_TEXT) textKeywords = ""
                                            types - option.key
                                        }
                                    }
                                )
                            }
                            if (types.contains(MessageBlockSettings.TYPE_TEXT)) {
                                InsetDivider()
                                InputRow(
                                    "文字关键词",
                                    "文字和文字引用消息生效；多个用 |、逗号或换行分隔，留空则全部命中",
                                    textKeywords,
                                    minLines = 2
                                ) { textKeywords = it }
                            }
                        }
                    }
                }
            }
            if (request.canDelete) {
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "操作") }
                item {
                    SettingsCard {
                        ActionRow("删除名单", "删除后该名单不再套用模板") {
                            onDelete()
                            Toast.makeText(context, "名单已删除", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun ConversationGroupMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    ConversationGroupSettingsUi.Page(context, provider, onBack)
}

internal object ConversationGroupSettingsUi {
@Composable
fun Page(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    var enabled by remember { mutableStateOf(ConversationGroupStore.isEnabled(context)) }
    var groups by remember { mutableStateOf(ConversationGroupStore.load(context)) }
    var route by remember { mutableStateOf<ConversationGroupRoute>(ConversationGroupRoute.Main) }

    fun reload() {
        groups = ConversationGroupStore.load(context)
        ConversationGroupRuntime.syncAsync(context)
    }

    fun saveGroup(group: ConversationGroup, existing: Boolean): Boolean {
        val duplicate = groups.any {
            it.id != group.id && it.parentId == group.parentId &&
                it.name.equals(group.name.trim(), ignoreCase = true)
        }
        if (duplicate) {
            Toast.makeText(context, "同一层级已存在同名分组", Toast.LENGTH_SHORT).show()
            return false
        }
        val success = if (existing) {
            ConversationGroupStore.updateGroup(context, group)
        } else {
            ConversationGroupStore.addGroup(context, group)
        }
        Toast.makeText(context, if (success) "聊天分组已保存" else "聊天分组保存失败", Toast.LENGTH_SHORT).show()
        if (success) reload()
        return success
    }

    SettingsRouteTransition(
        targetState = route,
        label = "ConversationGroupRoute",
        depthOf = {
            when (it) {
                ConversationGroupRoute.Main -> 0
                is ConversationGroupRoute.Editor -> 1
                is ConversationGroupRoute.AutomaticGrouping -> 1
                is ConversationGroupRoute.ConversationPicker,
                is ConversationGroupRoute.ParentPicker,
                is ConversationGroupRoute.AutomaticPicker -> 2
            }
        }
    ) { currentRoute ->
        when (currentRoute) {
            ConversationGroupRoute.Main -> ConversationGroupListPage(
                title = provider.title(),
                enabled = enabled,
                groups = groups,
                onEnabledChange = {
                    enabled = it
                    ConversationGroupStore.setEnabled(context, it)
                    ConversationGroupRuntime.syncAsync(context)
                },
                onBack = onBack,
                onAdd = {
                    route = ConversationGroupRoute.Editor(
                        ConversationGroupStore.newGroup(parentId = null),
                        existing = false
                    )
                },
                onOpenAutomatic = {
                    val target = groups.firstOrNull { it.automaticGroupingEnabled } ?: groups.firstOrNull()
                    if (target == null) {
                        Toast.makeText(context, "请先新增一个聊天分组", Toast.LENGTH_SHORT).show()
                    } else {
                        route = ConversationGroupRoute.AutomaticGrouping(target)
                    }
                },
                onOpen = { route = ConversationGroupRoute.Editor(it, existing = true) }
            )
            is ConversationGroupRoute.Editor -> ConversationGroupEditorPage(
                context = context,
                group = currentRoute.group,
                groups = groups,
                existing = currentRoute.existing,
                onBack = { route = ConversationGroupRoute.Main },
                onPickParent = {
                    route = ConversationGroupRoute.ParentPicker(it, currentRoute.existing)
                },
                onPickConversations = {
                    route = ConversationGroupRoute.ConversationPicker(it, currentRoute.existing)
                },
                onSave = { draft ->
                    if (saveGroup(draft, currentRoute.existing)) route = ConversationGroupRoute.Main
                },
                onReorder = { action, draft ->
                    val success = ConversationGroupStore.reorderGroup(
                        context,
                        currentRoute.group.id,
                        action
                    )
                    Toast.makeText(
                        context,
                        if (success) "分组顺序已更新" else "分组排序失败",
                        Toast.LENGTH_SHORT
                    ).show()
                    if (success) {
                        reload()
                        ConversationGroupStore.load(context)
                            .firstOrNull { it.id == currentRoute.group.id }
                            ?.let { latest ->
                                route = ConversationGroupRoute.Editor(
                                    draft.copy(order = latest.order),
                                    existing = true
                                )
                            }
                    }
                },
                onDelete = {
                    val success = ConversationGroupStore.deleteGroup(context, currentRoute.group.id)
                    Toast.makeText(context, if (success) "分组已删除" else "分组删除失败", Toast.LENGTH_SHORT).show()
                    if (success) {
                        reload()
                        route = ConversationGroupRoute.Main
                    }
                }
            )
            is ConversationGroupRoute.ConversationPicker -> ConversationGroupConversationPickerPage(
                context = context,
                group = currentRoute.group,
                groups = groups,
                onBack = {
                    route = ConversationGroupRoute.Editor(currentRoute.group, currentRoute.existing)
                },
                onConfirm = { selected ->
                    route = ConversationGroupRoute.Editor(
                        currentRoute.group.copy(conversationIds = selected),
                        currentRoute.existing
                    )
                }
            )
            is ConversationGroupRoute.ParentPicker -> ConversationGroupParentPickerPage(
                group = currentRoute.group,
                groups = groups,
                onBack = {
                    route = ConversationGroupRoute.Editor(currentRoute.group, currentRoute.existing)
                },
                onSelected = { parentId ->
                    route = ConversationGroupRoute.Editor(
                        currentRoute.group.copy(parentId = parentId),
                        currentRoute.existing
                    )
                }
            )
            is ConversationGroupRoute.AutomaticGrouping -> ConversationGroupAutomaticGroupingPage(
                context = context,
                group = currentRoute.group,
                groups = groups,
                onBack = { route = ConversationGroupRoute.Main },
                onTargetChanged = { group ->
                    route = ConversationGroupRoute.AutomaticGrouping(group)
                },
                onPick = { group, kind ->
                    route = ConversationGroupRoute.AutomaticPicker(group, kind)
                },
                onSave = { group ->
                    if (saveGroup(group, existing = true)) {
                        route = ConversationGroupRoute.Main
                    }
                }
            )
            is ConversationGroupRoute.AutomaticPicker -> ConversationGroupAutomaticPickerPage(
                context = context,
                group = currentRoute.group,
                kind = currentRoute.kind,
                onBack = {
                    route = ConversationGroupRoute.AutomaticGrouping(currentRoute.group)
                },
                onConfirm = { selected ->
                    val updated = when (currentRoute.kind) {
                        ConversationGroupAutomaticPickerKind.GROUPS -> {
                            currentRoute.group.copy(automaticGroupIds = selected)
                        }
                        ConversationGroupAutomaticPickerKind.OFFICIAL_INCLUDE -> {
                            currentRoute.group.copy(automaticOfficialIncludeIds = selected)
                        }
                        ConversationGroupAutomaticPickerKind.OFFICIAL_EXCLUDE -> {
                            currentRoute.group.copy(automaticOfficialExcludeIds = selected)
                        }
                    }
                    route = ConversationGroupRoute.AutomaticGrouping(updated)
                }
            )
        }
    }
}

@Composable
internal fun ConversationGroupListPage(
    title: String,
    enabled: Boolean,
    groups: List<ConversationGroup>,
    onEnabledChange: (Boolean) -> Unit,
    onBack: () -> Unit,
    onAdd: () -> Unit,
    onOpenAutomatic: () -> Unit,
    onOpen: (ConversationGroup) -> Unit
) {
    var query by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    val flattened = remember(groups) { flattenConversationGroups(groups) }
    val lower = query.trim().lowercase(Locale.US)
    val visible = flattened.filter { row ->
        lower.isEmpty() || row.group.name.lowercase(Locale.US).contains(lower) ||
            row.path.lowercase(Locale.US).contains(lower)
    }
    PageScaffold(
        title = title,
        largeTitle = title,
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "新增分组",
                onPrimaryClick = onAdd,
                secondaryText = "返回",
                onSecondaryClick = onBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(text = "功能") }
            item {
                SettingsCard {
                    SwitchRow(
                        checked = enabled,
                        title = "聊天分组",
                        summary = if (enabled) "已归拢会话从微信首页隐藏" else "分组配置保留，会话恢复在微信首页显示",
                        onCheckedChange = onEnabledChange
                    )
                    InsetDivider()
                    ActionRow(
                        title = "群聊归拢",
                        summary = "按规则自动归拢群聊和公众号到指定分组"
                    ) { onOpenAutomatic() }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "分组") }
            item {
                SettingsCard {
                    InputRow("搜索", "分组名称 / 所属路径", query) { query = it }
                }
            }
            item {
                SettingsCard {
                    when {
                        groups.isEmpty() -> EmptyText("暂无聊天分组")
                        visible.isEmpty() -> EmptyText("没有匹配的聊天分组")
                        else -> visible.forEachIndexed { index, row ->
                            SelectRow(
                                title = "${"  ".repeat(row.depth)}${row.group.name}",
                                summary = buildString {
                                    append(row.group.conversationIds.size).append(" 个直属会话")
                                    val childCount = groups.count { it.parentId == row.group.id }
                                    if (childCount > 0) append(" · ").append(childCount).append(" 个子分组")
                                    if (row.depth > 0) append("\n").append(row.path)
                                },
                                onClick = { onOpen(row.group) }
                            )
                            if (index < visible.lastIndex) InsetDivider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun ConversationGroupAutomaticGroupingPage(
    context: Context,
    group: ConversationGroup,
    groups: List<ConversationGroup>,
    onBack: () -> Unit,
    onTargetChanged: (ConversationGroup) -> Unit,
    onPick: (ConversationGroup, ConversationGroupAutomaticPickerKind) -> Unit,
    onSave: (ConversationGroup) -> Unit
) {
    var draft by remember(group) { mutableStateOf(group) }
    val labels = remember(context) { GroupChatLabelStore.load(context) }
    val targetChoices = remember(groups) {
        groups.map { item ->
            PopupChoice(item.name, item.id)
        }
    }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    PageScaffold(
        title = "群聊归拢",
        largeTitle = "群聊归拢",
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "保存设置",
                onPrimaryClick = { onSave(draft) },
                secondaryText = "返回",
                onSecondaryClick = onBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(text = "归拢目标") }
            item {
                SettingsCard {
                    PopupChoiceRow(
                        title = "目标聊天分组",
                        summary = "匹配到的会话会自动放入此分组",
                        options = targetChoices,
                        currentValue = group.id,
                        onValueChanged = { id ->
                            groups.firstOrNull { it.id == id }?.let(onTargetChanged)
                        }
                    )
                    InsetDivider()
                    SwitchRow(
                        checked = draft.automaticGroupingEnabled,
                        title = "启用群聊归拢",
                        summary = "关闭后自动归拢的会话立即恢复原位置",
                        onCheckedChange = { draft = draft.copy(automaticGroupingEnabled = it) }
                    )
                }
            }
            if (draft.automaticGroupingEnabled) {
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "群聊规则") }
                item {
                    SettingsCard {
                        SwitchRow(
                            checked = draft.automaticAllGroups,
                            title = "自动归拢所有群聊",
                            summary = "包含普通群和企业微信群"
                        ) { draft = draft.copy(automaticAllGroups = it) }
                        InsetDivider()
                        SwitchRow(
                            checked = draft.automaticNewGroups,
                            title = "自动归拢新群聊",
                            summary = "首次发现的新群聊自动加入目标分组"
                        ) { draft = draft.copy(automaticNewGroups = it) }
                        InsetDivider()
                        ActionRow(
                            title = "选择群聊",
                            summary = if (draft.automaticGroupIds.isEmpty()) {
                                "未选择群聊"
                            } else {
                                "已选择 ${draft.automaticGroupIds.size} 个群聊"
                            }
                        ) { onPick(draft, ConversationGroupAutomaticPickerKind.GROUPS) }
                        InsetDivider()
                        SwitchRow(
                            checked = draft.automaticMutedGroups,
                            title = "自动归拢免打扰群聊",
                            summary = "仅归拢已开启微信消息免打扰的群聊"
                        ) { draft = draft.copy(automaticMutedGroups = it) }
                        InsetDivider()
                        SwitchRow(
                            checked = draft.automaticOwnedGroups,
                            title = "自动归拢自己创建的群聊",
                            summary = "按群资料中的群主账号识别"
                        ) { draft = draft.copy(automaticOwnedGroups = it) }
                        InsetDivider()
                        SwitchRow(
                            checked = draft.automaticEnterpriseGroups,
                            title = "自动归拢企业微信群聊",
                            summary = "仅归拢企业微信互通群"
                        ) { draft = draft.copy(automaticEnterpriseGroups = it) }
                    }
                }
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "群聊标签") }
                item {
                    SettingsCard {
                        if (labels.isEmpty()) {
                            EmptyText("暂无群聊标签")
                        } else {
                            labels.forEachIndexed { index, label ->
                                SwitchRow(
                                    checked = label.id in draft.automaticGroupLabelIds,
                                    title = label.name,
                                    summary = "归拢该标签中的 ${label.groupIds.size} 个群聊"
                                ) { checked ->
                                    draft = draft.copy(
                                        automaticGroupLabelIds = if (checked) {
                                            (draft.automaticGroupLabelIds + label.id).distinct()
                                        } else {
                                            draft.automaticGroupLabelIds.filterNot { it == label.id }
                                        }
                                    )
                                }
                                if (index < labels.lastIndex) InsetDivider()
                            }
                        }
                    }
                }
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "公众号") }
                item {
                    SettingsCard {
                        SwitchRow(
                            checked = draft.automaticOfficialAccounts,
                            title = "自动归拢公众号",
                            summary = "归拢当前账号可识别的公众号会话"
                        ) { draft = draft.copy(automaticOfficialAccounts = it) }
                        InsetDivider()
                        ActionRow(
                            title = "包含公众号",
                            summary = if (draft.automaticOfficialIncludeIds.isEmpty()) {
                                "未额外包含公众号"
                            } else {
                                "已包含 ${draft.automaticOfficialIncludeIds.size} 个公众号"
                            }
                        ) { onPick(draft, ConversationGroupAutomaticPickerKind.OFFICIAL_INCLUDE) }
                        InsetDivider()
                        ActionRow(
                            title = "排除公众号",
                            summary = if (draft.automaticOfficialExcludeIds.isEmpty()) {
                                "未排除公众号"
                            } else {
                                "已排除 ${draft.automaticOfficialExcludeIds.size} 个公众号"
                            }
                        ) { onPick(draft, ConversationGroupAutomaticPickerKind.OFFICIAL_EXCLUDE) }
                    }
                }
            }
        }
    }
}

@Composable
internal fun ConversationGroupAutomaticPickerPage(
    context: Context,
    group: ConversationGroup,
    kind: ConversationGroupAutomaticPickerKind,
    onBack: () -> Unit,
    onConfirm: (List<String>) -> Unit
) {
    val initialSelected = remember(group, kind) {
        when (kind) {
            ConversationGroupAutomaticPickerKind.GROUPS -> group.automaticGroupIds.toSet()
            ConversationGroupAutomaticPickerKind.OFFICIAL_INCLUDE -> {
                group.automaticOfficialIncludeIds.toSet()
            }
            ConversationGroupAutomaticPickerKind.OFFICIAL_EXCLUDE -> {
                group.automaticOfficialExcludeIds.toSet()
            }
        }
    }
    var selectedIds by remember(group, kind) { mutableStateOf(initialSelected) }
    var options by remember(kind) { mutableStateOf(emptyList<ContactOption>()) }
    var loading by remember(kind) { mutableStateOf(true) }
    var error by remember(kind) { mutableStateOf("") }
    var query by remember(kind) { mutableStateOf("") }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    LaunchedEffect(kind) {
        loading = true
        error = ""
        val result = withContext(Dispatchers.IO) {
            runCatching { loadConversationGroupAutomaticOptions(kind) }
        }
        loading = false
        result.onSuccess { options = it }
            .onFailure { error = it.message ?: "读取联系人失败" }
    }
    val visible = options.filter { it.matchesSearch(query.trim().lowercase(Locale.US)) }
        .selectedFirst { it.id in initialSelected }
    val visibleIds = visible.mapTo(linkedSetOf()) { it.id }
    val visibleAllSelected = visibleIds.isNotEmpty() && visibleIds.all(selectedIds::contains)
    PageScaffold(
        title = kind.title,
        largeTitle = kind.title,
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "保存选择",
                onPrimaryClick = { onConfirm(selectedIds.toList()) },
                secondaryText = "返回",
                onSecondaryClick = onBack,
                middleText = if (visibleIds.isEmpty()) null else if (visibleAllSelected) "取消全选" else "全选",
                onMiddleClick = if (visibleIds.isEmpty()) null else {
                    {
                        selectedIds = if (visibleAllSelected) {
                            selectedIds - visibleIds
                        } else {
                            selectedIds + visibleIds
                        }
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item {
                SettingsCard {
                    InputRow("搜索", "名称 / 备注 / wxid", query) { query = it }
                }
            }
            item {
                PickerSectionHeader(
                    text = if (visible.isEmpty()) kind.title else "${kind.title} · ${visible.size} 项"
                )
            }
            when {
                loading -> item { SettingsCard { EmptyText("正在载入...") } }
                error.isNotBlank() -> item { SettingsCard { EmptyText(error) } }
                visible.isEmpty() -> item { SettingsCard { EmptyText(kind.emptyText) } }
                else -> visible.forEach { option ->
                    item {
                        ContactListCard(
                            option = option,
                            selected = option.id in selectedIds,
                            multiSelect = true,
                            onClick = {
                                selectedIds = if (option.id in selectedIds) {
                                    selectedIds - option.id
                                } else {
                                    selectedIds + option.id
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

internal fun loadConversationGroupAutomaticOptions(
    kind: ConversationGroupAutomaticPickerKind
): List<ContactOption> {
    val contacts = WeChatApis.contacts() ?: throw IllegalStateException("联系人列表不可用")
    val rows = when (kind) {
        ConversationGroupAutomaticPickerKind.GROUPS -> {
            contacts.getPickerGroups().mapNotNull { it.toOption(group = true) }
        }
        ConversationGroupAutomaticPickerKind.OFFICIAL_INCLUDE,
        ConversationGroupAutomaticPickerKind.OFFICIAL_EXCLUDE -> {
            contacts.getPickerOfficialAccounts().mapNotNull { it.toOption(group = false, official = true) }
        }
    }
    return rows.distinctBy { it.id }
        .sortedBy { it.label.lowercase(Locale.US) }
}

@Composable
internal fun ConversationGroupEditorPage(
    context: Context,
    group: ConversationGroup,
    groups: List<ConversationGroup>,
    existing: Boolean,
    onBack: () -> Unit,
    onPickParent: (ConversationGroup) -> Unit,
    onPickConversations: (ConversationGroup) -> Unit,
    onSave: (ConversationGroup) -> Unit,
    onReorder: (ConversationGroupStore.ReorderAction, ConversationGroup) -> Unit,
    onDelete: () -> Unit
) {
    var name by remember(group) { mutableStateOf(group.name) }
    var pinned by remember(group) { mutableStateOf(group.pinned) }
    var showDeleteConfirm by remember(group.id) { mutableStateOf(false) }
    var showReorderChoices by remember(group.id) { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    val draft = group.copy(name = name.trim(), pinned = pinned)
    val parentPath = group.parentId?.let { conversationGroupPath(groups, it) }.orEmpty()
    val siblings = remember(groups, group.parentId) {
        groups.filter { it.parentId == group.parentId && it.pinned == group.pinned }
            .sortedBy { it.order }
    }
    val siblingIndex = siblings.indexOfFirst { it.id == group.id }
    PageScaffold(
        title = if (existing) "编辑聊天分组" else "新增聊天分组",
        largeTitle = if (existing) "编辑聊天分组" else "新增聊天分组",
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "保存分组",
                onPrimaryClick = {
                    if (draft.name.isBlank()) {
                        Toast.makeText(context, "请输入分组名称", Toast.LENGTH_SHORT).show()
                    } else {
                        onSave(draft)
                    }
                },
                secondaryText = "返回",
                onSecondaryClick = onBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(text = "分组内容") }
            item {
                SettingsCard {
                    InputRow("分组名称", "同一层级内不能重名", name) { name = it }
                    if (!existing) {
                        InsetDivider()
                        SwitchRow(
                            checked = pinned,
                            title = "主页置顶",
                            summary = "新建后显示在当前层级的置顶分组区域",
                            onCheckedChange = { pinned = it }
                        )
                    }
                    InsetDivider()
                    ActionRow(
                        title = "上级分组",
                        summary = parentPath.ifBlank { "微信首页" }
                    ) { onPickParent(draft) }
                    InsetDivider()
                    ActionRow(
                        title = "会话",
                        summary = if (group.conversationIds.isEmpty()) "未选择会话" else "已选择 ${group.conversationIds.size} 个会话"
                    ) { onPickConversations(draft) }
                }
            }
            if (existing) {
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "操作") }
                item {
                    SettingsCard {
                        if (siblings.size > 1 && siblingIndex >= 0) {
                            ActionRow(
                                title = "分组位置",
                                summary = "当前${if (group.pinned) "置顶" else "普通"}分组第 " +
                                    "${siblingIndex + 1} 个，共 ${siblings.size} 个"
                            ) { showReorderChoices = true }
                            InsetDivider()
                        }
                        ActionRow(
                            title = "删除分组",
                            summary = "子分组移到上一级，直属会话移到上级分组或微信首页"
                        ) { showDeleteConfirm = true }
                    }
                }
            }
        }
    }
    if (showReorderChoices) {
        val actions = buildList {
            if (siblingIndex > 0) {
                add(Triple("移到顶部", "设为当前层级的第一个分组", ConversationGroupStore.ReorderAction.TOP))
                add(Triple("上移", "向前移动一个位置", ConversationGroupStore.ReorderAction.UP))
            }
            if (siblingIndex in 0 until siblings.lastIndex) {
                add(Triple("下移", "向后移动一个位置", ConversationGroupStore.ReorderAction.DOWN))
                add(Triple("移到底部", "设为当前层级的最后一个分组", ConversationGroupStore.ReorderAction.BOTTOM))
            }
        }
        WindowDialog(
            show = true,
            title = "分组位置",
            onDismissRequest = { showReorderChoices = false },
            content = {
                Column {
                    actions.forEachIndexed { index, (title, summary, action) ->
                        SelectRow(title = title, summary = summary) {
                            showReorderChoices = false
                            onReorder(action, draft)
                        }
                        if (index < actions.lastIndex) InsetDivider()
                    }
                }
            }
        )
    }
    if (showDeleteConfirm) {
        WindowDialog(
            show = true,
            title = "删除聊天分组",
            onDismissRequest = { showDeleteConfirm = false },
            content = {
                Column {
                    Text(
                        text = "确定删除“${group.name}”？聊天记录、联系人和群聊不会被删除。",
                        color = MiuixTheme.colorScheme.onSurface,
                        fontSize = 14.sp
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        TextButton(
                            text = "取消",
                            onClick = { showDeleteConfirm = false },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.textButtonColorsPrimary()
                        )
                        TextButton(
                            text = "确认删除",
                            onClick = {
                                showDeleteConfirm = false
                                onDelete()
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.textButtonColorsPrimary()
                        )
                    }
                }
            }
        )
    }
}

@Composable
internal fun ConversationGroupConversationPickerPage(
    context: Context,
    group: ConversationGroup,
    groups: List<ConversationGroup>,
    onBack: () -> Unit,
    onConfirm: (List<String>) -> Unit
) {
    var loading by remember(group.id) { mutableStateOf(true) }
    var error by remember(group.id) { mutableStateOf("") }
    var pickerData by remember(group.id) { mutableStateOf(ConversationGroupPickerData()) }
    var query by remember(group.id) { mutableStateOf("") }
    var contactFilter by remember(group.id) { mutableStateOf(ContactPickerFilter.ALL) }
    var selectedLabel by remember(group.id) { mutableStateOf("") }
    var selectedConversationGroupId by remember(group.id) { mutableStateOf("") }
    val initialSelected = remember(group) { group.conversationIds.toSet() }
    var selectedIds by remember(group) { mutableStateOf(initialSelected) }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()

    LaunchedEffect(group.id, groups) {
        loading = true
        error = ""
        val result = withContext(Dispatchers.IO) {
            runCatching { loadConversationGroupOptions(groups, group.id) }
        }
        loading = false
        result.onSuccess { pickerData = it }
            .onFailure { error = it.message ?: "读取会话列表失败" }
    }
    val conversations = pickerData.options
    val conversationGroupFilters = remember(groups, conversations) {
        ConversationGroupPickerSupport.filters(groups, conversations.map { it.id })
    }
    val conversationGroupChoices = remember(conversationGroupFilters) {
        listOf(PopupChoice("全部会话", "")) + conversationGroupFilters.map {
            PopupChoice(it.name, it.id)
        }
    }
    LaunchedEffect(conversationGroupFilters) {
        if (selectedConversationGroupId.isNotBlank() &&
            conversationGroupFilters.none { it.id == selectedConversationGroupId }
        ) {
            selectedConversationGroupId = ""
        }
    }
    val selectedConversationIds = remember(conversationGroupFilters, selectedConversationGroupId) {
        conversationGroupFilters.firstOrNull { it.id == selectedConversationGroupId }?.conversationIds
    }
    val scopedConversations = remember(conversations, selectedConversationIds) {
        selectedConversationIds?.let { ids -> conversations.filter { it.id in ids } } ?: conversations
    }
    val labels = remember(scopedConversations) {
        scopedConversations.flatMap { it.labels }
            .distinct()
            .sortedWith(compareBy { it.lowercase(Locale.US) })
    }
    LaunchedEffect(contactFilter, labels) {
        if (contactFilter == ContactPickerFilter.LABELS) {
            if (selectedLabel.isBlank() || selectedLabel !in labels) {
                selectedLabel = labels.firstOrNull().orEmpty()
            }
        } else if (selectedLabel.isNotBlank()) {
            selectedLabel = ""
        }
    }
    val lower = query.trim().lowercase(Locale.US)
    val filtered = scopedConversations.filter {
        when (contactFilter) {
            ContactPickerFilter.FRIENDS -> it.id in pickerData.friendIds
            ContactPickerFilter.GROUPS -> it.group
            ContactPickerFilter.OFFICIALS -> it.id in pickerData.officialIds
            ContactPickerFilter.LABELS -> selectedLabel.isNotBlank() &&
                it.id in pickerData.friendIds &&
                selectedLabel in it.labels
            ContactPickerFilter.ALL -> true
        }
    }
    val visible = filtered.filter { it.matchesSearch(lower) }
        .selectedFirst { initialSelected.contains(it.id) }
    val visibleIds = visible.map { it.id }.toSet()
    val visibleAllSelected = visibleIds.isNotEmpty() && visibleIds.all(selectedIds::contains)
    PageScaffold(
        title = "选择分组会话",
        largeTitle = "选择分组会话",
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "保存选择",
                onPrimaryClick = { onConfirm(selectedIds.toList()) },
                secondaryText = "返回",
                onSecondaryClick = onBack,
                middleText = if (visibleIds.isNotEmpty()) {
                    if (visibleAllSelected) "取消全选" else "全选"
                } else null,
                onMiddleClick = if (visibleIds.isNotEmpty()) {
                    {
                        selectedIds = if (visibleAllSelected) selectedIds - visibleIds else selectedIds + visibleIds
                    }
                } else null
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item {
                SettingsCard {
                    ContactFilterRow(
                        selected = contactFilter,
                        enableLabels = true,
                        enableOfficials = true,
                        onSelected = {
                            contactFilter = it
                            query = ""
                        }
                    )
                }
            }
            if (contactFilter == ContactPickerFilter.LABELS) {
                item {
                    SettingsCard {
                        ContactLabelFilterRow(
                            labels = labels,
                            selected = selectedLabel,
                            onSelected = {
                                selectedLabel = it
                                query = ""
                            }
                        )
                    }
                }
            }
            if (conversationGroupChoices.size > 1) {
                item {
                    SettingsCard {
                        PopupChoiceRow(
                            title = "聊天分组",
                            summary = "只显示所选分组及其子分组中的会话",
                            options = conversationGroupChoices,
                            currentValue = selectedConversationGroupId,
                            onValueChanged = {
                                selectedConversationGroupId = it
                                query = ""
                            }
                        )
                    }
                }
            }
            item {
                SettingsCard {
                    InputRow("搜索", "会话名称 / 备注 / wxid", query) { query = it }
                }
            }
            item {
                PickerSectionHeader(
                    text = if (visible.isNotEmpty()) {
                        "${contactFilter.title} · ${visible.size} 项"
                    } else {
                        contactFilter.title
                    }
                )
            }
            when {
                loading -> item { SettingsCard { EmptyText("正在载入会话...") } }
                error.isNotBlank() -> item { SettingsCard { EmptyText(error) } }
                visible.isEmpty() -> item { SettingsCard { EmptyText("没有匹配的会话") } }
                else -> visible.forEach { option ->
                    item {
                        ContactListCard(
                            option = option,
                            selected = selectedIds.contains(option.id),
                            multiSelect = true,
                            onClick = {
                                selectedIds = if (selectedIds.contains(option.id)) {
                                    selectedIds - option.id
                                } else {
                                    selectedIds + option.id
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun ConversationGroupParentPickerPage(
    group: ConversationGroup,
    groups: List<ConversationGroup>,
    onBack: () -> Unit,
    onSelected: (String?) -> Unit
) {
    val excluded = remember(groups, group.id) {
        ConversationGroupStore.descendantIds(groups, group.id) + group.id
    }
    val candidates = remember(groups, excluded) {
        flattenConversationGroups(groups).filterNot { it.group.id in excluded }
    }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    PageScaffold(
        title = "选择上级分组",
        largeTitle = "选择上级分组",
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "微信首页",
                onPrimaryClick = { onSelected(null) },
                secondaryText = "返回",
                onSecondaryClick = onBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(text = "可用分组") }
            item {
                SettingsCard {
                    if (candidates.isEmpty()) {
                        EmptyText("没有其它可用分组")
                    } else {
                        candidates.forEachIndexed { index, row ->
                            SelectRow(
                                title = "${"  ".repeat(row.depth)}${row.group.name}",
                                summary = row.path,
                                onClick = { onSelected(row.group.id) }
                            )
                            if (index < candidates.lastIndex) InsetDivider()
                        }
                    }
                }
            }
        }
    }
}

internal data class FlattenedConversationGroup(
    val group: ConversationGroup,
    val depth: Int,
    val path: String
)

internal fun flattenConversationGroups(groups: List<ConversationGroup>): List<FlattenedConversationGroup> {
    val normalized = ConversationGroupStore.normalize(groups)
    val byParent = normalized.groupBy { it.parentId }
    val result = arrayListOf<FlattenedConversationGroup>()
    val visited = hashSetOf<String>()
    fun visit(parentId: String?, depth: Int, path: List<String>) {
        byParent[parentId].orEmpty().sortedBy { it.order }.forEach { group ->
            if (!visited.add(group.id)) return@forEach
            val nextPath = path + group.name
            result += FlattenedConversationGroup(group, depth, nextPath.joinToString(" / "))
            visit(group.id, depth + 1, nextPath)
        }
    }
    visit(null, 0, emptyList())
    return result
}

internal fun conversationGroupPath(groups: List<ConversationGroup>, groupId: String): String {
    return flattenConversationGroups(groups).firstOrNull { it.group.id == groupId }?.path.orEmpty()
}

internal data class ConversationGroupPickerData(
    val options: List<ContactOption> = emptyList(),
    val friendIds: Set<String> = emptySet(),
    val officialIds: Set<String> = emptySet()
)

internal fun loadConversationGroupOptions(
    groups: List<ConversationGroup>,
    currentGroupId: String
): ConversationGroupPickerData {
    val conversationApi = WeChatApis.conversations() ?: throw IllegalStateException("会话列表不可用")
    val contactApi = WeChatApis.contact().contacts()
    val assignedIds = groups.flatMap { it.conversationIds }.toSet()
    val friendIds = runCatching {
        contactApi?.getPickerContacts().orEmpty()
            .map { it.wxId.trim() }
            .filter { it.isNotBlank() }
            .toSet()
    }.getOrDefault(emptySet())
    val officialIds = runCatching {
        contactApi?.getPickerOfficialAccounts().orEmpty()
            .map { it.wxId.trim() }
            .filter { it.isNotBlank() }
            .toSet()
    }.getOrDefault(emptySet())
    val labelsByUser = linkedMapOf<String, MutableList<String>>()
    runCatching { contactApi?.getContactLabelList().orEmpty() }
        .getOrDefault(emptyList())
        .forEach { label ->
            val labelName = label.labelName.ifBlank { label.labelId }
            if (labelName.isBlank()) return@forEach
            label.userNameList.forEach { wxId ->
                if (wxId.isNotBlank()) {
                    labelsByUser.getOrPut(wxId) { arrayListOf() }.add(labelName)
                }
            }
        }
    val paths = flattenConversationGroups(groups).associate { it.group.id to it.path }
    val ownerLabels = buildMap {
        groups.filterNot { it.id == currentGroupId }.forEach { owner ->
            owner.conversationIds.forEach { talker ->
                paths[owner.id]?.takeIf(String::isNotBlank)?.let { put(talker, "已在：$it") }
            }
        }
    }
    val rows = conversationApi.getRecentConversationUsernames(10000)
    val result = linkedMapOf<String, ContactOption>()
    rows.forEach { username ->
        val talker = username.trim()
        if (talker.isBlank() || ConversationGroupRuntime.isVirtualTalker(talker) ||
            talker in CONVERSATION_GROUP_EXCLUDED_TALKERS
        ) return@forEach
        val contact = runCatching { contactApi?.getContact(talker) }.getOrNull()
        if (contact == null && talker != "filehelper" && talker !in assignedIds) return@forEach
        val group = contact?.isGroup() == true
        result[talker] = ContactOption(
            id = talker,
            label = contact?.pickerDisplayName(group)
                .orEmpty()
                .ifBlank { conversationApi.getConversationTitle(talker).ifBlank { talker } },
            group = group,
            avatarUrl = contact?.avatarUrl.orEmpty(),
            avatarBackupUrl = contact?.avatarBackupUrl.orEmpty(),
            labels = labelsByUser[talker].orEmpty().distinct(),
            official = contact?.isOfficialAccount() == true,
            extraSummary = ownerLabels[talker].orEmpty(),
            searchAliases = listOfNotNull(
                contact?.remarkName,
                contact?.nickname,
                contact?.customWxId,
                talker
            ).filter { it.isNotBlank() }.distinct()
        )
    }
    assignedIds.filterNot(result::containsKey).forEach { talker ->
        val contact = runCatching { contactApi?.getContact(talker) }.getOrNull()
        val group = contact?.isGroup() == true ||
            talker.endsWith("@chatroom") ||
            talker.endsWith("@im.chatroom")
        result[talker] = ContactOption(
            id = talker,
            label = contact?.pickerDisplayName(group)
                .orEmpty()
                .ifBlank { conversationApi.getConversationTitle(talker).ifBlank { talker } },
            group = group,
            avatarUrl = contact?.avatarUrl.orEmpty(),
            avatarBackupUrl = contact?.avatarBackupUrl.orEmpty(),
            labels = labelsByUser[talker].orEmpty().distinct(),
            official = contact?.isOfficialAccount() == true || talker.startsWith("gh_"),
            extraSummary = ownerLabels[talker].orEmpty(),
            searchAliases = listOfNotNull(
                contact?.remarkName,
                contact?.nickname,
                contact?.customWxId,
                talker
            ).filter { it.isNotBlank() }.distinct()
        )
    }
    return ConversationGroupPickerData(
        options = result.values.toList(),
        friendIds = friendIds,
        officialIds = officialIds + result.values.filter { it.official }.map { it.id }
    )
}

internal val CONVERSATION_GROUP_EXCLUDED_TALKERS = setOf(
    "message_fold",
    "conversationboxservice",
    "officialaccounts",
    "appbrand_notify_message",
    "notifymessage",
    "qmessage",
    "floatbottle"
)
}

@Composable
internal fun GroupChatLabelMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    var labels by remember { mutableStateOf(GroupChatLabelStore.load(context)) }
    var route by remember { mutableStateOf<GroupChatLabelRoute>(GroupChatLabelRoute.Main) }

    fun saveLabels(next: List<GroupChatLabel>) {
        labels = next
        GroupChatLabelStore.save(context, next)
    }

    SettingsRouteTransition(
        targetState = route,
        label = "GroupChatLabelRoute",
        depthOf = {
            when (it) {
                GroupChatLabelRoute.Main -> 0
                is GroupChatLabelRoute.Editor -> 1
                is GroupChatLabelRoute.GroupPicker -> 2
            }
        }
    ) { currentRoute ->
        when (currentRoute) {
            GroupChatLabelRoute.Main -> GroupChatLabelListPage(
                title = provider.title(),
                labels = labels,
                onBack = onBack,
                onAdd = { route = GroupChatLabelRoute.Editor(GroupChatLabelStore.newLabel(), false) },
                onOpen = { route = GroupChatLabelRoute.Editor(it, true) }
            )
            is GroupChatLabelRoute.Editor -> GroupChatLabelEditorPage(
                context = context,
                label = currentRoute.label,
                existing = currentRoute.existing,
                onBack = { route = GroupChatLabelRoute.Main },
                onPickGroups = { draft ->
                    route = GroupChatLabelRoute.GroupPicker(draft, currentRoute.existing)
                },
                onSave = { draft ->
                    if (labels.any { it.id != draft.id && it.name.equals(draft.name, ignoreCase = true) }) {
                        Toast.makeText(context, "标签名称已存在", Toast.LENGTH_SHORT).show()
                    } else {
                        val next = if (currentRoute.existing) {
                            labels.map { if (it.id == draft.id) draft else it }
                        } else {
                            labels + draft
                        }
                        saveLabels(next)
                        Toast.makeText(context, "群聊标签已保存", Toast.LENGTH_SHORT).show()
                        route = GroupChatLabelRoute.Main
                    }
                },
                onDelete = {
                    saveLabels(labels.filterNot { it.id == currentRoute.label.id })
                    Toast.makeText(context, "群聊标签已删除", Toast.LENGTH_SHORT).show()
                    route = GroupChatLabelRoute.Main
                }
            )
            is GroupChatLabelRoute.GroupPicker -> ContactPickerPage(
                context = context,
                request = ContactPickerRequest(
                    title = "选择标签群聊",
                    mode = ContactPickerMode.GROUPS,
                    multiSelect = true,
                    existingValue = formatIds(currentRoute.label.groupIds),
                    onValue = {},
                    enableGroupLabels = false
                ),
                onBack = {
                    route = GroupChatLabelRoute.Editor(currentRoute.label, currentRoute.existing)
                },
                onConfirm = { selected ->
                    route = GroupChatLabelRoute.Editor(
                        currentRoute.label.copy(groupIds = selected.map { it.id }.toSet()),
                        currentRoute.existing
                    )
                }
            )
        }
    }
}

@Composable
internal fun GroupChatLabelListPage(
    title: String,
    labels: List<GroupChatLabel>,
    onBack: () -> Unit,
    onAdd: () -> Unit,
    onOpen: (GroupChatLabel) -> Unit
) {
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    PageScaffold(
        title = title,
        largeTitle = title,
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "新增标签",
                onPrimaryClick = onAdd,
                secondaryText = "返回",
                onSecondaryClick = onBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(text = "分类标签") }
            item {
                SettingsCard {
                    if (labels.isEmpty()) {
                        EmptyText("暂无群聊标签")
                    } else {
                        labels.forEachIndexed { index, label ->
                            SelectRow(
                                title = label.name,
                                summary = "${label.groupIds.size} 个群聊",
                                onClick = { onOpen(label) }
                            )
                            if (index < labels.lastIndex) InsetDivider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun GroupChatLabelEditorPage(
    context: Context,
    label: GroupChatLabel,
    existing: Boolean,
    onBack: () -> Unit,
    onPickGroups: (GroupChatLabel) -> Unit,
    onSave: (GroupChatLabel) -> Unit,
    onDelete: () -> Unit
) {
    var name by remember(label) { mutableStateOf(label.name) }
    val groupIds = label.groupIds
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    val draft = label.copy(name = name.trim(), groupIds = groupIds)
    PageScaffold(
        title = if (existing) "编辑群聊标签" else "新增群聊标签",
        largeTitle = if (existing) "编辑群聊标签" else "新增群聊标签",
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "保存标签",
                onPrimaryClick = {
                    if (draft.name.isBlank()) {
                        Toast.makeText(context, "请输入标签名称", Toast.LENGTH_SHORT).show()
                    } else {
                        onSave(draft)
                    }
                },
                secondaryText = "返回",
                onSecondaryClick = onBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(text = "标签") }
            item {
                SettingsCard {
                    InputRow("标签名称", "用于任务和名单选择器", name) { name = it }
                    InsetDivider()
                    ActionRow(
                        title = "标签群聊",
                        summary = if (groupIds.isEmpty()) "未选择群聊" else "已选择 ${groupIds.size} 个群聊"
                    ) {
                        onPickGroups(draft)
                    }
                }
            }
            if (existing) {
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "操作") }
                item {
                    SettingsCard {
                        ActionRow("删除标签", "删除后不会移除原功能中已经保存的群聊", onDelete)
                    }
                }
            }
        }
    }
}

@Composable
internal fun GroupRenameMonitorMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, GroupRenameMonitorSettings.PREFS_NAME) }
    var route by remember { mutableStateOf<GroupRenameRoute>(GroupRenameRoute.Main) }
    var templates by remember {
        mutableStateOf(GroupRenameMonitorSettings.parseTemplates(sp.getString(GroupRenameMonitorSettings.KEY_TEMPLATES, "").orEmpty()))
    }
    var templateBindings by remember {
        mutableStateOf(GroupRenameMonitorSettings.parseBindings(sp.getString(GroupRenameMonitorSettings.KEY_TEMPLATE_BINDINGS, "").orEmpty()))
    }
    var noticeEnabled by remember { mutableStateOf(sp.getBoolean(GroupRenameMonitorSettings.KEY_NOTICE_ENABLE, GroupRenameMonitorSettings.DEFAULT_NOTICE_ENABLE)) }
    var noticeText by remember {
        mutableStateOf(sp.getString(GroupRenameMonitorSettings.KEY_NOTICE_TEXT, GroupRenameMonitorSettings.DEFAULT_NOTICE_TEXT) ?: GroupRenameMonitorSettings.DEFAULT_NOTICE_TEXT)
    }
    var noticeScope by remember {
        mutableStateOf(sp.getString(GroupRenameMonitorSettings.KEY_NOTICE_SCOPE, GroupRenameMonitorSettings.DEFAULT_NOTICE_SCOPE) ?: GroupRenameMonitorSettings.DEFAULT_NOTICE_SCOPE)
    }
    var noticeGroups by remember { mutableStateOf(sp.getString(GroupRenameMonitorSettings.KEY_NOTICE_GROUPS, "").orEmpty()) }
    var sendEnabled by remember { mutableStateOf(sp.getBoolean(GroupRenameMonitorSettings.KEY_SEND_ENABLE, GroupRenameMonitorSettings.DEFAULT_SEND_ENABLE)) }
    var listenGroups by remember { mutableStateOf(sp.getString(GroupRenameMonitorSettings.KEY_LISTEN_GROUPS, "").orEmpty()) }
    var delaySeconds by remember { mutableStateOf(sp.getInt(GroupRenameMonitorSettings.KEY_DELAY_SECONDS, GroupRenameMonitorSettings.DEFAULT_DELAY_SECONDS).toString()) }
    var promptType by remember { mutableStateOf(sp.getString(GroupRenameMonitorSettings.KEY_PROMPT_TYPE, GroupRenameMonitorSettings.DEFAULT_PROMPT_TYPE) ?: GroupRenameMonitorSettings.DEFAULT_PROMPT_TYPE) }
    var bothOrder by remember { mutableStateOf(sp.getString(GroupRenameMonitorSettings.KEY_BOTH_ORDER, GroupRenameMonitorSettings.DEFAULT_BOTH_ORDER) ?: GroupRenameMonitorSettings.DEFAULT_BOTH_ORDER) }
    var text by remember { mutableStateOf(sp.getString(GroupRenameMonitorSettings.KEY_TEXT, GroupRenameMonitorSettings.DEFAULT_TEXT) ?: GroupRenameMonitorSettings.DEFAULT_TEXT) }
    var cardTitle by remember { mutableStateOf(sp.getString(GroupRenameMonitorSettings.KEY_CARD_TITLE, GroupRenameMonitorSettings.DEFAULT_CARD_TITLE) ?: GroupRenameMonitorSettings.DEFAULT_CARD_TITLE) }
    var cardDesc by remember { mutableStateOf(sp.getString(GroupRenameMonitorSettings.KEY_CARD_DESC, GroupRenameMonitorSettings.DEFAULT_CARD_DESC) ?: GroupRenameMonitorSettings.DEFAULT_CARD_DESC) }
    var wxidColor by remember {
        mutableStateOf(
            MemberTitleStore.cleanColor(
                sp.getString(GroupRenameMonitorSettings.KEY_WXID_COLOR, GroupRenameMonitorSettings.DEFAULT_WXID_COLOR)
                    ?.substringBefore(',')
            ).ifEmpty { GroupRenameMonitorSettings.DEFAULT_WXID_COLOR }
        )
    }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()

    SettingsRouteTransition(
        targetState = route,
        label = "GroupRenameRoute",
        depthOf = {
            when (it) {
                GroupRenameRoute.Main -> 0
                GroupRenameRoute.GroupManager,
                GroupRenameRoute.TemplateManager,
                GroupRenameRoute.BatchTemplateBinding,
                is GroupRenameRoute.ContactPicker -> 1
                is GroupRenameRoute.GroupEditor,
                is GroupRenameRoute.TemplateEditor -> 2
            }
        }
    ) { currentRoute ->
        when (currentRoute) {
            is GroupRenameRoute.ContactPicker -> ContactPickerPage(
                context = context,
                request = currentRoute.request,
                onBack = { route = if (currentRoute.fromMain) GroupRenameRoute.Main else GroupRenameRoute.GroupManager },
                onConfirm = { selected ->
                    currentRoute.request.onValue(formatIds(selected.map { it.id }))
                    route = if (currentRoute.fromMain) GroupRenameRoute.Main else GroupRenameRoute.GroupManager
                }
            )
            GroupRenameRoute.GroupManager -> GroupRenameGroupManagerPage(
                context = context,
                listenGroups = listenGroups,
                templates = templates,
                bindings = templateBindings,
                onPickGroups = {
                    route = GroupRenameRoute.ContactPicker(
                        ContactPickerRequest(
                            title = "选择改名监听群",
                            mode = ContactPickerMode.GROUPS,
                            multiSelect = true,
                            existingValue = listenGroups,
                            onValue = { next ->
                                listenGroups = next
                                sp.edit().putString(GroupRenameMonitorSettings.KEY_LISTEN_GROUPS, next).apply()
                            }
                        )
                    )
                },
                onOpenGroup = { groupId, label -> route = GroupRenameRoute.GroupEditor(groupId, label) },
                onOpenTemplates = { route = GroupRenameRoute.TemplateManager },
                onBatchApplyTemplate = { route = GroupRenameRoute.BatchTemplateBinding },
                onDeleteGroups = { targets ->
                    val targetIds = targets.mapTo(HashSet()) { it.id }
                    val nextListenGroups = formatIds(parseIds(listenGroups) - targetIds)
                    val nextBindings = templateBindings.filterNot { it.groupId in targetIds }
                    targetIds.forEach { groupId ->
                        deleteGroupRenameConfiguration(sp, groupId, nextListenGroups, nextBindings)
                    }
                    listenGroups = nextListenGroups
                    templateBindings = nextBindings
                    Toast.makeText(context, "已删除 ${targets.size} 个监听群", Toast.LENGTH_SHORT).show()
                },
                onBack = { route = GroupRenameRoute.Main }
            )
            is GroupRenameRoute.GroupEditor -> GroupRenameGroupEditorPage(
                context = context,
                sp = sp,
                groupId = currentRoute.groupId,
                groupLabel = currentRoute.label,
                templates = templates,
                bindings = templateBindings,
                onBindingsChanged = { next ->
                    templateBindings = next
                    sp.edit().putString(GroupRenameMonitorSettings.KEY_TEMPLATE_BINDINGS, GroupRenameMonitorSettings.encodeBindings(next)).apply()
                },
                onDelete = {
                    listenGroups = formatIds(parseIds(listenGroups) - currentRoute.groupId)
                    templateBindings = templateBindings.filterNot { it.groupId == currentRoute.groupId }
                    deleteGroupRenameConfiguration(sp, currentRoute.groupId, listenGroups, templateBindings)
                    Toast.makeText(context, "群配置已删除", Toast.LENGTH_SHORT).show()
                    route = GroupRenameRoute.GroupManager
                },
                onBack = { route = GroupRenameRoute.GroupManager }
            )
            GroupRenameRoute.TemplateManager -> GroupRenameTemplateListPage(
                templates = templates,
                bindings = templateBindings,
                onBack = { route = GroupRenameRoute.GroupManager },
                onOpenTemplate = { index, template -> route = GroupRenameRoute.TemplateEditor(index, template, true) },
                onAddTemplate = {
                    route = GroupRenameRoute.TemplateEditor(
                        templates.size,
                        newGroupRenameTemplate(templates.size + 1, sp),
                        false
                    )
                }
            )
            is GroupRenameRoute.TemplateEditor -> GroupRenameTemplateEditorPage(
                context = context,
                template = currentRoute.template,
                canDelete = currentRoute.canDelete,
                onBack = { route = GroupRenameRoute.TemplateManager },
                onSave = { updated ->
                    templates = if (currentRoute.index in templates.indices) {
                        templates.toMutableList().also { it[currentRoute.index] = updated }
                    } else {
                        templates + updated
                    }
                    sp.edit().putString(GroupRenameMonitorSettings.KEY_TEMPLATES, GroupRenameMonitorSettings.encodeTemplates(templates)).apply()
                    Toast.makeText(context, "改名模板已保存", Toast.LENGTH_SHORT).show()
                    route = GroupRenameRoute.TemplateManager
                },
                onDelete = {
                    if (currentRoute.index in templates.indices) {
                        val templateId = templates[currentRoute.index].id
                        templates = templates.toMutableList().also { it.removeAt(currentRoute.index) }
                        templateBindings = templateBindings.filterNot { it.templateId == templateId }
                        sp.edit()
                            .putString(GroupRenameMonitorSettings.KEY_TEMPLATES, GroupRenameMonitorSettings.encodeTemplates(templates))
                            .putString(GroupRenameMonitorSettings.KEY_TEMPLATE_BINDINGS, GroupRenameMonitorSettings.encodeBindings(templateBindings))
                            .apply()
                    }
                    route = GroupRenameRoute.TemplateManager
                }
            )
            GroupRenameRoute.BatchTemplateBinding -> GroupRenameBatchTemplateBindingPage(
                context = context,
                listenGroups = listenGroups,
                templates = templates,
                bindings = templateBindings,
                onBack = { route = GroupRenameRoute.GroupManager },
                onSave = { additions ->
                    templateBindings = upsertGroupRenameTemplateBindings(templateBindings, additions)
                    sp.edit().putString(GroupRenameMonitorSettings.KEY_TEMPLATE_BINDINGS, GroupRenameMonitorSettings.encodeBindings(templateBindings)).apply()
                    Toast.makeText(context, "模板已套用到 ${additions.size} 个群", Toast.LENGTH_SHORT).show()
                    route = GroupRenameRoute.GroupManager
                }
            )
            GroupRenameRoute.Main -> PageScaffold(
                title = provider.title(),
                largeTitle = provider.title(),
                scrollBehavior = scrollBehavior,
                bottomBar = {
                    BottomActionBar(
                        primaryText = "保存设置",
                        onPrimaryClick = {
                            val color = MemberTitleStore.cleanColor(wxidColor.substringBefore(','))
                                .ifEmpty { GroupRenameMonitorSettings.DEFAULT_WXID_COLOR }
                            wxidColor = color
                            sp.edit()
                                .putBoolean(GroupRenameMonitorSettings.KEY_NOTICE_ENABLE, noticeEnabled)
                                .putString(GroupRenameMonitorSettings.KEY_NOTICE_TEXT, noticeText)
                                .putString(GroupRenameMonitorSettings.KEY_NOTICE_SCOPE, noticeScope)
                                .putString(GroupRenameMonitorSettings.KEY_NOTICE_GROUPS, noticeGroups)
                                .putBoolean(GroupRenameMonitorSettings.KEY_SEND_ENABLE, sendEnabled)
                                .putString(GroupRenameMonitorSettings.KEY_LISTEN_GROUPS, listenGroups)
                                .putInt(GroupRenameMonitorSettings.KEY_DELAY_SECONDS, delaySeconds.toIntOrNull()?.coerceIn(0, 600) ?: GroupRenameMonitorSettings.DEFAULT_DELAY_SECONDS)
                                .putString(GroupRenameMonitorSettings.KEY_PROMPT_TYPE, promptType)
                                .putString(GroupRenameMonitorSettings.KEY_BOTH_ORDER, bothOrder)
                                .putString(GroupRenameMonitorSettings.KEY_TEXT, text)
                                .putString(GroupRenameMonitorSettings.KEY_CARD_TITLE, cardTitle)
                                .putString(GroupRenameMonitorSettings.KEY_CARD_DESC, cardDesc)
                                .putString(GroupRenameMonitorSettings.KEY_WXID_COLOR, color)
                                .apply()
                            Toast.makeText(context, "改名监控设置已保存", Toast.LENGTH_SHORT).show()
                        },
                        secondaryText = "返回",
                        onSecondaryClick = onBack
                    )
                }
            ) { padding ->
                LazyColumn(
                    modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
                    state = listState,
                    contentPadding = PaddingValues(
                        top = padding.calculateTopPadding() + 8.dp,
                        bottom = padding.calculateBottomPadding() + 84.dp
                    )
                ) {
                    item { SmallTitle(text = "提醒方式") }
                    item {
                        SettingsCard {
                            SwitchRow(noticeEnabled, "插入系统消息", "在对应群聊插入可点击成员资料的改名消息", onCheckedChange = { noticeEnabled = it })
                            InsetDivider()
                            SwitchRow(sendEnabled, "发送改名提醒", "向选择的监听群发送文本或卡片", onCheckedChange = { sendEnabled = it })
                            if (noticeEnabled) {
                                InsetDivider()
                                VariableInputRow(
                                    "系统消息模板",
                                    "wxid变量会生成可点击资料链接",
                                    noticeText,
                                    groupRenameNoticeVariables,
                                    minLines = 3
                                ) { noticeText = it }
                                InsetDivider()
                                PopupChoiceRow(
                                    title = "适用群聊",
                                    summary = systemNoticeScopeLabel(noticeScope, GroupRenameMonitorSettings.NOTICE_SCOPE_SPECIFIC),
                                    options = systemNoticeScopeChoices(
                                        GroupRenameMonitorSettings.NOTICE_SCOPE_ALL,
                                        GroupRenameMonitorSettings.NOTICE_SCOPE_SPECIFIC
                                    ),
                                    currentValue = noticeScope,
                                    onValueChanged = { noticeScope = it }
                                )
                                if (noticeScope == GroupRenameMonitorSettings.NOTICE_SCOPE_SPECIFIC) {
                                    InsetDivider()
                                    ActionRow("选择指定群聊", systemNoticeGroupSummary(noticeGroups)) {
                                        route = GroupRenameRoute.ContactPicker(
                                            request = ContactPickerRequest(
                                                title = "选择系统消息群聊",
                                                mode = ContactPickerMode.GROUPS,
                                                multiSelect = true,
                                                existingValue = noticeGroups,
                                                onValue = { noticeGroups = it }
                                            ),
                                            fromMain = true
                                        )
                                    }
                                }
                                InsetDivider()
                                ColorPickerRow(
                                    "wxid颜色",
                                    "系统消息里的 wxid 链接颜色",
                                    wxidColor,
                                    allowGradient = false,
                                    onReset = { wxidColor = GroupRenameMonitorSettings.DEFAULT_WXID_COLOR }
                                ) { wxidColor = it.take(9) }
                            }
                        }
                    }
                    if (sendEnabled) {
                        item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "发送设置") }
                        item {
                            SettingsCard {
                                ActionRow("监听群与专属设置", groupSelectionSummary(listenGroups)) {
                                    route = GroupRenameRoute.GroupManager
                                }
                                InsetDivider()
                                ActionRow(
                                    "提醒模板管理",
                                    if (templates.isEmpty()) "暂无模板，进入后添加批量配置" else "${templates.size} 个模板，进入后修改或删除"
                                ) {
                                    route = GroupRenameRoute.TemplateManager
                                }
                                InsetDivider()
                                ActionRow(
                                    "批量套用模板",
                                    if (templates.isEmpty()) "先新增模板，再批量绑定监听群" else groupRenameBindingSummary(templateBindings, templates)
                                ) {
                                    route = GroupRenameRoute.BatchTemplateBinding
                                }
                                InsetDivider()
                                NumberInputRow("整体延迟", "单位秒，0-600", delaySeconds) { delaySeconds = it }
                                InsetDivider()
                                PopupChoiceRow(
                                    title = "提示类型",
                                    summary = groupRenamePromptTypeLabel(promptType),
                                    options = groupRenamePromptTypeChoices(),
                                    currentValue = promptType,
                                    onValueChanged = { promptType = it }
                                )
                                if (promptType == GroupRenameMonitorSettings.PROMPT_BOTH) {
                                    InsetDivider()
                                    PopupChoiceRow(
                                        title = "文本+卡片顺序",
                                        summary = groupRenameBothOrderLabel(bothOrder),
                                        options = groupRenameBothOrderChoices(),
                                        currentValue = bothOrder,
                                        onValueChanged = { bothOrder = it }
                                    )
                                }
                            }
                        }
                        if (promptType != GroupRenameMonitorSettings.PROMPT_CARD) {
                            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "文本模板") }
                            item {
                                SettingsCard {
                                    VariableInputRow("改名提醒文本", "多个模板用 || 分隔随机选择", text, groupRenameTemplateVariables, minLines = 3) { text = it }
                                }
                            }
                        }
                        if (promptType != GroupRenameMonitorSettings.PROMPT_TEXT) {
                            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "卡片模板") }
                            item {
                                SettingsCard {
                                    VariableInputRow("卡片标题", "多个模板用 || 分隔随机选择", cardTitle, groupRenameTemplateVariables) { cardTitle = it }
                                    InsetDivider()
                                    VariableInputRow("卡片描述", "多个模板用 || 分隔随机选择", cardDesc, groupRenameTemplateVariables, minLines = 3) { cardDesc = it }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun GroupRenameGroupManagerPage(
    context: Context,
    listenGroups: String,
    templates: List<GroupRenameReplyTemplate>,
    bindings: List<GroupRenameTemplateBinding>,
    onPickGroups: () -> Unit,
    onOpenGroup: (String, String) -> Unit,
    onOpenTemplates: () -> Unit,
    onBatchApplyTemplate: () -> Unit,
    onDeleteGroups: (List<ContactOption>) -> Unit,
    onBack: () -> Unit
) {
    var loading by remember(listenGroups) { mutableStateOf(true) }
    var error by remember(listenGroups) { mutableStateOf("") }
    var groups by remember(listenGroups) { mutableStateOf(emptyList<ContactOption>()) }
    var query by remember { mutableStateOf("") }
    var batchDeleteMode by remember { mutableStateOf(false) }
    var selectedForDelete by remember { mutableStateOf<Set<String>>(emptySet()) }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    val selectedIds = remember(listenGroups) { parseIds(listenGroups).toList() }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()

    LaunchedEffect(listenGroups) {
        loadContacts(context, ContactPickerMode.GROUPS) { result, throwable ->
            loading = false
            if (throwable != null) {
                error = throwable.message ?: "读取群聊失败"
            } else {
                val byId = result.associateBy { it.id }
                groups = selectedIds.map { id ->
                    byId[id] ?: ContactOption(id, id, group = true, avatarUrl = "", avatarBackupUrl = "")
                }
            }
        }
    }
    val lower = query.trim().lowercase(Locale.US)
    val visible = groups.filter { it.matchesSearch(lower) }
    val visibleIds = visible.mapTo(LinkedHashSet()) { it.id }
    val allVisibleSelected = visibleIds.isNotEmpty() && visibleIds.all { it in selectedForDelete }
    val selectedGroups = groups.filter { it.id in selectedForDelete }
    PageScaffold(
        title = "改名监听群",
        largeTitle = "改名监听群",
        scrollBehavior = scrollBehavior,
        bottomBar = {
            if (batchDeleteMode) {
                BottomActionBar(
                    primaryText = "删除所选（${selectedGroups.size}）",
                    onPrimaryClick = {
                        if (selectedGroups.isEmpty()) {
                            Toast.makeText(context, "请先选择监听群", Toast.LENGTH_SHORT).show()
                        } else {
                            showDeleteConfirm = true
                        }
                    },
                    secondaryText = "取消",
                    onSecondaryClick = {
                        batchDeleteMode = false
                        selectedForDelete = emptySet()
                    },
                    middleText = if (visibleIds.isEmpty()) null else if (allVisibleSelected) "取消全选" else "全选",
                    onMiddleClick = if (visibleIds.isEmpty()) null else {
                        {
                            selectedForDelete = if (allVisibleSelected) {
                                selectedForDelete - visibleIds
                            } else {
                                selectedForDelete + visibleIds
                            }
                        }
                    }
                )
            } else {
                BottomActionBar(
                    primaryText = "选择监听群",
                    onPrimaryClick = onPickGroups,
                    secondaryText = "返回",
                    onSecondaryClick = onBack,
                    middleText = if (selectedIds.isEmpty()) null else "批量删除",
                    onMiddleClick = if (selectedIds.isEmpty()) null else {
                        {
                            batchDeleteMode = true
                            selectedForDelete = emptySet()
                        }
                    }
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item {
                SettingsCard {
                    InputRow("搜索监听群", "群名称 / 群聊备注 / 群号", query) { query = it }
                }
            }
            if (!batchDeleteMode) {
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "模板") }
                item {
                    SettingsCard {
                        ActionRow("提醒模板管理", if (templates.isEmpty()) "暂无模板，进入后添加" else "${templates.size} 个模板") {
                            onOpenTemplates()
                        }
                        InsetDivider()
                        ActionRow(
                            "批量套用模板",
                            if (templates.isEmpty()) "先新增模板，再批量绑定监听群" else groupRenameBindingSummary(bindings, templates)
                        ) {
                            onBatchApplyTemplate()
                        }
                    }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "已监听群 · ${selectedIds.size} 项") }
            item {
                SettingsCard {
                    when {
                        loading -> EmptyText("正在载入监听群...")
                        error.isNotEmpty() -> EmptyText(error)
                        selectedIds.isEmpty() -> EmptyText("暂无监听群。点击底部“选择监听群”添加。")
                        visible.isEmpty() -> EmptyText("没有匹配结果")
                        else -> visible.forEachIndexed { index, option ->
                            if (batchDeleteMode) {
                                OptionChoiceRow(
                                    item = OptionItem(
                                        label = option.label.ifBlank { option.id },
                                        value = index,
                                        summary = groupRenameGroupBindingSummary(option.id, bindings, templates)
                                    ),
                                    selected = option.id in selectedForDelete,
                                    onClick = {
                                        selectedForDelete = if (option.id in selectedForDelete) {
                                            selectedForDelete - option.id
                                        } else {
                                            selectedForDelete + option.id
                                        }
                                    }
                                )
                            } else {
                                SelectRow(
                                    title = option.label.ifBlank { option.id },
                                    summary = groupRenameGroupBindingSummary(option.id, bindings, templates),
                                    onClick = { onOpenGroup(option.id, option.label.ifBlank { option.id }) }
                                )
                            }
                            if (index < visible.lastIndex) InsetDivider()
                        }
                    }
                }
            }
        }
    }
    BatchDeleteConfirmDialog(
        show = showDeleteConfirm,
        message = "将删除已选的 ${selectedGroups.size} 个改名监听群及其专属配置，此操作不可撤销。",
        labels = selectedGroups.map { it.label.ifBlank { it.id } },
        onDismiss = { showDeleteConfirm = false },
        onConfirm = {
            val targets = selectedGroups
            showDeleteConfirm = false
            batchDeleteMode = false
            selectedForDelete = emptySet()
            onDeleteGroups(targets)
        }
    )
}

@Composable
internal fun GroupRenameTemplateListPage(
    templates: List<GroupRenameReplyTemplate>,
    bindings: List<GroupRenameTemplateBinding>,
    onBack: () -> Unit,
    onOpenTemplate: (Int, GroupRenameReplyTemplate) -> Unit,
    onAddTemplate: () -> Unit
) {
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    PageScaffold(
        title = "改名提醒模板",
        largeTitle = "改名提醒模板",
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "新增模板",
                onPrimaryClick = onAddTemplate,
                secondaryText = "返回",
                onSecondaryClick = onBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(text = "模板") }
            item {
                SettingsCard {
                    if (templates.isEmpty()) {
                        EmptyText("暂无模板。新增后可批量套用到监听群。")
                    } else {
                        templates.forEachIndexed { index, template ->
                            SelectRow(
                                title = template.name.ifBlank { "模板 ${index + 1}" },
                                summary = "${describeGroupRenameTemplate(template)} · 已绑定 ${bindings.count { it.templateId == template.id }} 个群",
                                onClick = { onOpenTemplate(index, template) }
                            )
                            if (index < templates.lastIndex) InsetDivider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun GroupRenameTemplateEditorPage(
    context: Context,
    template: GroupRenameReplyTemplate,
    canDelete: Boolean,
    onBack: () -> Unit,
    onSave: (GroupRenameReplyTemplate) -> Unit,
    onDelete: () -> Unit
) {
    var name by rememberSaveable(template.id) { mutableStateOf(template.name) }
    var enabled by rememberSaveable(template.id) { mutableStateOf(template.enabled) }
    var delaySeconds by rememberSaveable(template.id) { mutableStateOf(template.delaySeconds.toString()) }
    var promptType by rememberSaveable(template.id) { mutableStateOf(template.promptType) }
    var bothOrder by rememberSaveable(template.id) { mutableStateOf(template.bothOrder) }
    var text by rememberSaveable(template.id) { mutableStateOf(template.text) }
    var cardTitle by rememberSaveable(template.id) { mutableStateOf(template.cardTitle) }
    var cardDesc by rememberSaveable(template.id) { mutableStateOf(template.cardDesc) }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    PageScaffold(
        title = name.ifBlank { "改名提醒模板" },
        largeTitle = "编辑改名模板",
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "保存模板",
                onPrimaryClick = {
                    if (name.isBlank()) {
                        Toast.makeText(context, "请输入模板名称", Toast.LENGTH_SHORT).show()
                    } else {
                        onSave(
                            template.copy(
                                name = name.trim(),
                                enabled = enabled,
                                delaySeconds = delaySeconds.toIntOrNull()?.coerceIn(0, 600) ?: GroupRenameMonitorSettings.DEFAULT_DELAY_SECONDS,
                                promptType = GroupRenameMonitorSettings.normalizePromptType(promptType),
                                bothOrder = GroupRenameMonitorSettings.normalizeBothOrder(bothOrder),
                                text = text,
                                cardTitle = cardTitle,
                                cardDesc = cardDesc
                            )
                        )
                    }
                },
                secondaryText = "返回",
                onSecondaryClick = onBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(text = "模板") }
            item {
                SettingsCard {
                    InputRow("模板名称", "用于列表和批量套用", name) { name = it }
                    InsetDivider()
                    SwitchRow(enabled, "启用模板", "关闭后已绑定群不会发送改名提醒", onCheckedChange = { enabled = it })
                    InsetDivider()
                    NumberInputRow("整体延迟", "单位秒，0-600", delaySeconds) { delaySeconds = it }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "提示设置") }
            item {
                SettingsCard {
                    PopupChoiceRow(
                        title = "提示类型",
                        summary = groupRenamePromptTypeLabel(promptType),
                        options = groupRenamePromptTypeChoices(),
                        currentValue = promptType,
                        onValueChanged = { promptType = it }
                    )
                    if (promptType == GroupRenameMonitorSettings.PROMPT_BOTH) {
                        InsetDivider()
                        PopupChoiceRow(
                            title = "文本+卡片顺序",
                            summary = groupRenameBothOrderLabel(bothOrder),
                            options = groupRenameBothOrderChoices(),
                            currentValue = bothOrder,
                            onValueChanged = { bothOrder = it }
                        )
                    }
                }
            }
            if (promptType != GroupRenameMonitorSettings.PROMPT_CARD) {
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "文本模板") }
                item {
                    SettingsCard {
                        VariableInputRow("改名提醒文本", "多个模板用 || 分隔随机选择", text, groupRenameTemplateVariables, minLines = 3) { text = it }
                    }
                }
            }
            if (promptType != GroupRenameMonitorSettings.PROMPT_TEXT) {
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "卡片模板") }
                item {
                    SettingsCard {
                        VariableInputRow("卡片标题", "多个模板用 || 分隔随机选择", cardTitle, groupRenameTemplateVariables) { cardTitle = it }
                        InsetDivider()
                        VariableInputRow("卡片描述", "多个模板用 || 分隔随机选择", cardDesc, groupRenameTemplateVariables, minLines = 3) { cardDesc = it }
                    }
                }
            }
            if (canDelete) {
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "操作") }
                item {
                    SettingsCard {
                        ActionRow("删除模板", "删除后同时解除所有群绑定") { onDelete() }
                    }
                }
            }
        }
    }
}

@Composable
internal fun GroupRenameBatchTemplateBindingPage(
    context: Context,
    listenGroups: String,
    templates: List<GroupRenameReplyTemplate>,
    bindings: List<GroupRenameTemplateBinding>,
    onBack: () -> Unit,
    onSave: (List<GroupRenameTemplateBinding>) -> Unit
) {
    var loading by remember(listenGroups) { mutableStateOf(true) }
    var error by remember(listenGroups) { mutableStateOf("") }
    var groups by remember(listenGroups) { mutableStateOf(emptyList<ContactOption>()) }
    var query by remember { mutableStateOf("") }
    var selectedTemplateId by remember(templates) { mutableStateOf(templates.firstOrNull()?.id.orEmpty()) }
    var selectedGroups by remember(listenGroups) { mutableStateOf(parseIds(listenGroups)) }
    val selectedIds = remember(listenGroups) { parseIds(listenGroups).toList() }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()

    LaunchedEffect(listenGroups) {
        loadContacts(context, ContactPickerMode.GROUPS) { result, throwable ->
            loading = false
            if (throwable != null) {
                error = throwable.message ?: "读取群聊失败"
            } else {
                val byId = result.associateBy { it.id }
                groups = selectedIds.map { id ->
                    byId[id] ?: ContactOption(id, id, group = true, avatarUrl = "", avatarBackupUrl = "")
                }
            }
        }
    }
    val lower = query.trim().lowercase(Locale.US)
    val visible = groups.filter { it.matchesSearch(lower) }
    val visibleIds = visible.map { it.id }.toSet()
    val allVisibleSelected = visibleIds.isNotEmpty() && visibleIds.all { it in selectedGroups }
    PageScaffold(
        title = "批量套用改名模板",
        largeTitle = "批量套用改名模板",
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "套用模板",
                onPrimaryClick = {
                    when {
                        selectedTemplateId.isBlank() -> Toast.makeText(context, "请先选择模板", Toast.LENGTH_SHORT).show()
                        selectedGroups.isEmpty() -> Toast.makeText(context, "请先选择群", Toast.LENGTH_SHORT).show()
                        else -> {
                            val labels = groups.associate { it.id to it.label.ifBlank { it.id } }
                            onSave(selectedGroups.map { groupId ->
                                GroupRenameTemplateBinding(
                                    groupId,
                                    labels[groupId] ?: bindings.firstOrNull { it.groupId == groupId }?.label ?: groupId,
                                    selectedTemplateId
                                )
                            })
                        }
                    }
                },
                secondaryText = "返回",
                onSecondaryClick = onBack,
                middleText = if (visibleIds.isNotEmpty()) if (allVisibleSelected) "取消全选" else "全选" else null,
                onMiddleClick = if (visibleIds.isNotEmpty()) {
                    {
                        selectedGroups = if (allVisibleSelected) selectedGroups - visibleIds else selectedGroups + visibleIds
                    }
                } else null
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item {
                SettingsCard {
                    PopupChoiceRow(
                        title = "选择模板",
                        summary = templates.firstOrNull { it.id == selectedTemplateId }?.name.orEmpty(),
                        options = templates.map { PopupChoice(it.name.ifBlank { it.id }, it.id) },
                        currentValue = selectedTemplateId,
                        onValueChanged = { selectedTemplateId = it }
                    )
                    InsetDivider()
                    InputRow("搜索监听群", "群名称 / 群聊备注 / 群号", query) { query = it }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "已选择 ${selectedGroups.size} 个群") }
            when {
                loading -> item { SettingsCard { EmptyText("正在载入监听群...") } }
                error.isNotEmpty() -> item { SettingsCard { EmptyText(error) } }
                visible.isEmpty() -> item { SettingsCard { EmptyText("没有匹配结果") } }
                else -> visible.forEach { option ->
                    item {
                        ContactListCard(
                            option = option,
                            selected = option.id in selectedGroups,
                            multiSelect = true,
                            onClick = {
                                selectedGroups = if (option.id in selectedGroups) selectedGroups - option.id else selectedGroups + option.id
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun GroupRenameGroupEditorPage(
    context: Context,
    sp: SharedPreferences,
    groupId: String,
    groupLabel: String,
    templates: List<GroupRenameReplyTemplate>,
    bindings: List<GroupRenameTemplateBinding>,
    onBindingsChanged: (List<GroupRenameTemplateBinding>) -> Unit,
    onDelete: () -> Unit,
    onBack: () -> Unit
) {
    val globalPromptType = sp.getString(GroupRenameMonitorSettings.KEY_PROMPT_TYPE, GroupRenameMonitorSettings.DEFAULT_PROMPT_TYPE)
        ?: GroupRenameMonitorSettings.DEFAULT_PROMPT_TYPE
    val globalBothOrder = sp.getString(GroupRenameMonitorSettings.KEY_BOTH_ORDER, GroupRenameMonitorSettings.DEFAULT_BOTH_ORDER)
        ?: GroupRenameMonitorSettings.DEFAULT_BOTH_ORDER
    val globalText = sp.getString(GroupRenameMonitorSettings.KEY_TEXT, GroupRenameMonitorSettings.DEFAULT_TEXT)
        ?: GroupRenameMonitorSettings.DEFAULT_TEXT
    val globalCardTitle = sp.getString(GroupRenameMonitorSettings.KEY_CARD_TITLE, GroupRenameMonitorSettings.DEFAULT_CARD_TITLE)
        ?: GroupRenameMonitorSettings.DEFAULT_CARD_TITLE
    val globalCardDesc = sp.getString(GroupRenameMonitorSettings.KEY_CARD_DESC, GroupRenameMonitorSettings.DEFAULT_CARD_DESC)
        ?: GroupRenameMonitorSettings.DEFAULT_CARD_DESC
    var promptType by rememberSaveable(groupId) {
        mutableStateOf(sp.getString(GroupRenameMonitorSettings.groupKey(GroupRenameMonitorSettings.KEY_PROMPT_TYPE, groupId), GroupRenameMonitorSettings.MODE_GLOBAL) ?: GroupRenameMonitorSettings.MODE_GLOBAL)
    }
    var bothOrder by rememberSaveable(groupId) {
        mutableStateOf(sp.getString(GroupRenameMonitorSettings.groupKey(GroupRenameMonitorSettings.KEY_BOTH_ORDER, groupId), globalBothOrder) ?: globalBothOrder)
    }
    var text by rememberSaveable(groupId) {
        mutableStateOf(sp.getString(GroupRenameMonitorSettings.groupKey(GroupRenameMonitorSettings.KEY_TEXT, groupId), globalText) ?: globalText)
    }
    var cardTitle by rememberSaveable(groupId) {
        mutableStateOf(sp.getString(GroupRenameMonitorSettings.groupKey(GroupRenameMonitorSettings.KEY_CARD_TITLE, groupId), globalCardTitle) ?: globalCardTitle)
    }
    var cardDesc by rememberSaveable(groupId) {
        mutableStateOf(sp.getString(GroupRenameMonitorSettings.groupKey(GroupRenameMonitorSettings.KEY_CARD_DESC, groupId), globalCardDesc) ?: globalCardDesc)
    }
    val effectivePromptType = if (promptType == GroupRenameMonitorSettings.MODE_GLOBAL) {
        GroupRenameMonitorSettings.normalizePromptType(globalPromptType)
    } else {
        GroupRenameMonitorSettings.normalizePromptType(promptType)
    }
    val boundTemplate = templates.firstOrNull { template ->
        bindings.firstOrNull { it.groupId == groupId }?.templateId == template.id
    }
    val templateOptions = listOf(PopupChoice("不使用模板", "")) + templates.map {
        PopupChoice(it.name.ifBlank { it.id }, it.id)
    }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    PageScaffold(
        title = groupLabel.ifBlank { groupId },
        largeTitle = "专属改名设置",
        scrollBehavior = scrollBehavior,
        bottomBar = {
            if (boundTemplate != null) {
                BottomActionBar("返回", onBack)
            } else {
                BottomActionBar(
                    primaryText = "保存设置",
                    onPrimaryClick = {
                        sp.edit()
                            .putString(GroupRenameMonitorSettings.groupKey(GroupRenameMonitorSettings.KEY_PROMPT_TYPE, groupId), promptType)
                            .putString(GroupRenameMonitorSettings.groupKey(GroupRenameMonitorSettings.KEY_BOTH_ORDER, groupId), bothOrder)
                            .putString(GroupRenameMonitorSettings.groupKey(GroupRenameMonitorSettings.KEY_TEXT, groupId), text)
                            .putString(GroupRenameMonitorSettings.groupKey(GroupRenameMonitorSettings.KEY_CARD_TITLE, groupId), cardTitle)
                            .putString(GroupRenameMonitorSettings.groupKey(GroupRenameMonitorSettings.KEY_CARD_DESC, groupId), cardDesc)
                            .apply()
                        Toast.makeText(context, "专属改名设置已保存", Toast.LENGTH_SHORT).show()
                    },
                    secondaryText = "返回",
                    onSecondaryClick = onBack
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(text = "模板") }
            item {
                SettingsCard {
                    PopupChoiceRow(
                        title = "选择模板",
                        summary = boundTemplate?.let { describeGroupRenameTemplate(it) } ?: "不使用模板，按本群专属设置发送",
                        options = templateOptions,
                        currentValue = boundTemplate?.id.orEmpty(),
                        onValueChanged = { templateId ->
                            val next = if (templateId.isBlank()) {
                                bindings.filterNot { it.groupId == groupId }
                            } else {
                                upsertGroupRenameTemplateBindings(
                                    bindings,
                                    listOf(GroupRenameTemplateBinding(groupId, groupLabel.ifBlank { groupId }, templateId))
                                )
                            }
                            onBindingsChanged(next)
                        }
                    )
                }
            }
            if (boundTemplate == null) {
                item { SmallTitle(text = "提示设置") }
                item {
                    SettingsCard {
                        PopupChoiceRow(
                            title = "提示类型",
                            summary = groupRenamePromptTypeWithGlobalLabel(promptType),
                            options = groupRenamePromptTypeWithGlobalChoices(),
                            currentValue = promptType,
                            onValueChanged = { promptType = it }
                        )
                        if (promptType == GroupRenameMonitorSettings.PROMPT_BOTH) {
                            InsetDivider()
                            PopupChoiceRow(
                                title = "文本+卡片顺序",
                                summary = groupRenameBothOrderLabel(bothOrder),
                                options = groupRenameBothOrderChoices(),
                                currentValue = bothOrder,
                                onValueChanged = { bothOrder = it }
                            )
                        }
                    }
                }
                if (effectivePromptType != GroupRenameMonitorSettings.PROMPT_CARD) {
                    item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "文本模板") }
                    item {
                        SettingsCard {
                            VariableInputRow("改名提醒文本", "本群专属；多个模板用 || 分隔随机选择", text, groupRenameTemplateVariables, minLines = 3) { text = it }
                        }
                    }
                }
                if (effectivePromptType != GroupRenameMonitorSettings.PROMPT_TEXT) {
                    item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "卡片模板") }
                    item {
                        SettingsCard {
                            VariableInputRow("卡片标题", "本群专属；多个模板用 || 分隔随机选择", cardTitle, groupRenameTemplateVariables) { cardTitle = it }
                            InsetDivider()
                            VariableInputRow("卡片描述", "本群专属；多个模板用 || 分隔随机选择", cardDesc, groupRenameTemplateVariables, minLines = 3) { cardDesc = it }
                        }
                    }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "操作") }
            item {
                SettingsCard {
                    if (boundTemplate == null) {
                        ActionRow("恢复全局内容", "跟随全局类型并覆盖本群模板") {
                            promptType = GroupRenameMonitorSettings.MODE_GLOBAL
                            bothOrder = globalBothOrder
                            text = globalText
                            cardTitle = globalCardTitle
                            cardDesc = globalCardDesc
                            sp.edit()
                                .putString(GroupRenameMonitorSettings.groupKey(GroupRenameMonitorSettings.KEY_PROMPT_TYPE, groupId), GroupRenameMonitorSettings.MODE_GLOBAL)
                                .putString(GroupRenameMonitorSettings.groupKey(GroupRenameMonitorSettings.KEY_BOTH_ORDER, groupId), globalBothOrder)
                                .putString(GroupRenameMonitorSettings.groupKey(GroupRenameMonitorSettings.KEY_TEXT, groupId), globalText)
                                .putString(GroupRenameMonitorSettings.groupKey(GroupRenameMonitorSettings.KEY_CARD_TITLE, groupId), globalCardTitle)
                                .putString(GroupRenameMonitorSettings.groupKey(GroupRenameMonitorSettings.KEY_CARD_DESC, groupId), globalCardDesc)
                                .apply()
                        }
                        InsetDivider()
                    }
                    ActionRow("删除当前群", "移除监听和本群专属配置") { onDelete() }
                }
            }
        }
    }
}

internal fun deleteGroupRenameConfiguration(
    sp: SharedPreferences,
    groupId: String,
    listenGroups: String,
    bindings: List<GroupRenameTemplateBinding>
) {
    sp.edit()
        .putString(GroupRenameMonitorSettings.KEY_LISTEN_GROUPS, listenGroups)
        .putString(GroupRenameMonitorSettings.KEY_TEMPLATE_BINDINGS, GroupRenameMonitorSettings.encodeBindings(bindings))
        .remove(GroupRenameMonitorSettings.groupKey(GroupRenameMonitorSettings.KEY_PROMPT_TYPE, groupId))
        .remove(GroupRenameMonitorSettings.groupKey(GroupRenameMonitorSettings.KEY_BOTH_ORDER, groupId))
        .remove(GroupRenameMonitorSettings.groupKey(GroupRenameMonitorSettings.KEY_TEXT, groupId))
        .remove(GroupRenameMonitorSettings.groupKey(GroupRenameMonitorSettings.KEY_CARD_TITLE, groupId))
        .remove(GroupRenameMonitorSettings.groupKey(GroupRenameMonitorSettings.KEY_CARD_DESC, groupId))
        .apply()
}

internal fun groupRenamePromptTypeLabel(value: String): String = when (value) {
    GroupRenameMonitorSettings.PROMPT_CARD -> "卡片"
    GroupRenameMonitorSettings.PROMPT_BOTH -> "文本+卡片"
    else -> "文本"
}

internal fun groupRenamePromptTypeChoices(): List<PopupChoice<String>> = listOf(
    PopupChoice("文本", GroupRenameMonitorSettings.PROMPT_TEXT),
    PopupChoice("文本+卡片", GroupRenameMonitorSettings.PROMPT_BOTH),
    PopupChoice("卡片", GroupRenameMonitorSettings.PROMPT_CARD)
)

internal fun groupRenamePromptTypeWithGlobalLabel(value: String): String {
    return if (value == GroupRenameMonitorSettings.MODE_GLOBAL) "跟随全局" else groupRenamePromptTypeLabel(value)
}

internal fun groupRenamePromptTypeWithGlobalChoices(): List<PopupChoice<String>> {
    return listOf(PopupChoice("跟随全局", GroupRenameMonitorSettings.MODE_GLOBAL)) + groupRenamePromptTypeChoices()
}

internal fun groupRenameBothOrderLabel(value: String): String {
    return if (value == GroupRenameMonitorSettings.BOTH_CARD_FIRST) "先卡片后文本" else "先文本后卡片"
}

internal fun groupRenameBothOrderChoices(): List<PopupChoice<String>> = listOf(
    PopupChoice("先文本后卡片", GroupRenameMonitorSettings.BOTH_TEXT_FIRST),
    PopupChoice("先卡片后文本", GroupRenameMonitorSettings.BOTH_CARD_FIRST)
)

internal fun newGroupRenameTemplate(index: Int, sp: SharedPreferences): GroupRenameReplyTemplate {
    return GroupRenameReplyTemplate(
        id = "rename_${System.currentTimeMillis()}_$index",
        name = "改名模板 $index",
        enabled = true,
        delaySeconds = sp.getInt(GroupRenameMonitorSettings.KEY_DELAY_SECONDS, GroupRenameMonitorSettings.DEFAULT_DELAY_SECONDS),
        promptType = sp.getString(GroupRenameMonitorSettings.KEY_PROMPT_TYPE, GroupRenameMonitorSettings.DEFAULT_PROMPT_TYPE)
            ?: GroupRenameMonitorSettings.DEFAULT_PROMPT_TYPE,
        bothOrder = sp.getString(GroupRenameMonitorSettings.KEY_BOTH_ORDER, GroupRenameMonitorSettings.DEFAULT_BOTH_ORDER)
            ?: GroupRenameMonitorSettings.DEFAULT_BOTH_ORDER,
        text = sp.getString(GroupRenameMonitorSettings.KEY_TEXT, GroupRenameMonitorSettings.DEFAULT_TEXT)
            ?: GroupRenameMonitorSettings.DEFAULT_TEXT,
        cardTitle = sp.getString(GroupRenameMonitorSettings.KEY_CARD_TITLE, GroupRenameMonitorSettings.DEFAULT_CARD_TITLE)
            ?: GroupRenameMonitorSettings.DEFAULT_CARD_TITLE,
        cardDesc = sp.getString(GroupRenameMonitorSettings.KEY_CARD_DESC, GroupRenameMonitorSettings.DEFAULT_CARD_DESC)
            ?: GroupRenameMonitorSettings.DEFAULT_CARD_DESC
    )
}

internal fun upsertGroupRenameTemplateBindings(
    existing: List<GroupRenameTemplateBinding>,
    additions: List<GroupRenameTemplateBinding>
): List<GroupRenameTemplateBinding> {
    val result = LinkedHashMap<String, GroupRenameTemplateBinding>()
    existing.forEach { binding ->
        if (binding.groupId.isNotBlank() && binding.templateId.isNotBlank()) {
            result[binding.groupId] = binding
        }
    }
    additions.forEach { binding ->
        if (binding.groupId.isNotBlank() && binding.templateId.isNotBlank()) {
            result[binding.groupId] = binding
        }
    }
    return result.values.toList()
}

internal fun describeGroupRenameTemplate(template: GroupRenameReplyTemplate): String {
    val type = groupRenamePromptTypeLabel(template.promptType)
    return if (template.enabled) "$type · 延迟 ${template.delaySeconds} 秒" else "已停用"
}

internal fun groupRenameBindingSummary(
    bindings: List<GroupRenameTemplateBinding>,
    templates: List<GroupRenameReplyTemplate>
): String {
    val validIds = templates.map { it.id }.toSet()
    val count = bindings.count { it.templateId in validIds }
    return if (count == 0) "暂无群绑定模板" else "$count 个群已绑定模板"
}

internal fun groupRenameGroupBindingSummary(
    groupId: String,
    bindings: List<GroupRenameTemplateBinding>,
    templates: List<GroupRenameReplyTemplate>
): String {
    val binding = bindings.firstOrNull { it.groupId == groupId } ?: return groupId
    val template = templates.firstOrNull { it.id == binding.templateId } ?: return "$groupId · 模板已失效"
    return "$groupId · ${template.name.ifBlank { template.id }}"
}

@Composable
internal fun GroupLeaveMonitorMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, GroupLeaveMonitorSettings.PREFS_NAME) }
    var groupPicker by remember { mutableStateOf<ContactPickerRequest?>(null) }
    var favoritePicker by remember { mutableStateOf<FavoritePickerRequest?>(null) }
    var showGroupManager by remember { mutableStateOf(false) }
    var showTemplateManager by remember { mutableStateOf(false) }
    var showBatchTemplateBinding by remember { mutableStateOf(false) }
    var groupEditor by remember { mutableStateOf<Pair<String, String>?>(null) }
    var templateEditor by remember { mutableStateOf<GroupLeaveTemplateEditorRequest?>(null) }
    var replyTemplates by remember {
        mutableStateOf(GroupLeaveMonitorSettings.parseTemplates(sp.getString(GroupLeaveMonitorSettings.KEY_REPLY_TEMPLATES, "").orEmpty()))
    }
    var replyTemplateBindings by remember {
        mutableStateOf(GroupLeaveMonitorSettings.parseBindings(sp.getString(GroupLeaveMonitorSettings.KEY_REPLY_TEMPLATE_BINDINGS, "").orEmpty()))
    }
    var leaveNoticeEnabled by remember {
        mutableStateOf(sp.getBoolean(GroupLeaveMonitorSettings.KEY_ENABLE, GroupLeaveMonitorSettings.DEFAULT_ENABLE))
    }
    var leaveNoticeText by remember {
        mutableStateOf(
            sp.getString(
                GroupLeaveMonitorSettings.KEY_LEAVE_NOTICE_TEXT,
                GroupLeaveMonitorSettings.DEFAULT_LEAVE_NOTICE_TEXT
            ) ?: GroupLeaveMonitorSettings.DEFAULT_LEAVE_NOTICE_TEXT
        )
    }
    var inviteNoticeEnabled by remember {
        mutableStateOf(
            sp.getBoolean(
                GroupLeaveMonitorSettings.KEY_INVITE_DETAIL_ENABLE,
                GroupLeaveMonitorSettings.DEFAULT_INVITE_DETAIL_ENABLE
            )
        )
    }
    var inviteNoticeText by remember {
        mutableStateOf(
            sp.getString(
                GroupLeaveMonitorSettings.KEY_INVITE_NOTICE_TEXT,
                GroupLeaveMonitorSettings.DEFAULT_INVITE_NOTICE_TEXT
            ) ?: GroupLeaveMonitorSettings.DEFAULT_INVITE_NOTICE_TEXT
        )
    }
    var noticeScope by remember {
        mutableStateOf(sp.getString(GroupLeaveMonitorSettings.KEY_NOTICE_SCOPE, GroupLeaveMonitorSettings.DEFAULT_NOTICE_SCOPE) ?: GroupLeaveMonitorSettings.DEFAULT_NOTICE_SCOPE)
    }
    var noticeGroups by remember { mutableStateOf(sp.getString(GroupLeaveMonitorSettings.KEY_NOTICE_GROUPS, "").orEmpty()) }
    var wxidColor by remember {
        mutableStateOf(
            MemberTitleStore.cleanColor(
                sp.getString(
                    GroupLeaveMonitorSettings.KEY_WXID_COLOR,
                    GroupLeaveMonitorSettings.DEFAULT_WXID_COLOR
                )?.substringBefore(',')
            ).ifEmpty { GroupLeaveMonitorSettings.DEFAULT_WXID_COLOR }
        )
    }
    var listenGroups by remember {
        mutableStateOf(sp.getString(GroupLeaveMonitorSettings.KEY_LISTEN_GROUPS, "").orEmpty())
    }
    var promptType by remember {
        mutableStateOf(sp.getString(GroupLeaveMonitorSettings.KEY_PROMPT_TYPE, GroupLeaveMonitorSettings.DEFAULT_PROMPT_TYPE) ?: GroupLeaveMonitorSettings.DEFAULT_PROMPT_TYPE)
    }
    var bothOrder by remember {
        mutableStateOf(sp.getString(GroupLeaveMonitorSettings.KEY_BOTH_ORDER, GroupLeaveMonitorSettings.DEFAULT_BOTH_ORDER) ?: GroupLeaveMonitorSettings.DEFAULT_BOTH_ORDER)
    }
    var delaySeconds by remember {
        mutableStateOf(sp.getInt(GroupLeaveMonitorSettings.KEY_DELAY_SECONDS, GroupLeaveMonitorSettings.DEFAULT_DELAY_SECONDS).toString())
    }
    var joinText by remember {
        mutableStateOf(sp.getString(GroupLeaveMonitorSettings.KEY_JOIN_TEXT, GroupLeaveMonitorSettings.DEFAULT_JOIN_TEXT) ?: GroupLeaveMonitorSettings.DEFAULT_JOIN_TEXT)
    }
    var leftText by remember {
        mutableStateOf(sp.getString(GroupLeaveMonitorSettings.KEY_LEFT_TEXT, GroupLeaveMonitorSettings.DEFAULT_LEFT_TEXT) ?: GroupLeaveMonitorSettings.DEFAULT_LEFT_TEXT)
    }
    var joinCardTitle by remember {
        mutableStateOf(sp.getString(GroupLeaveMonitorSettings.KEY_JOIN_CARD_TITLE, GroupLeaveMonitorSettings.DEFAULT_JOIN_CARD_TITLE) ?: GroupLeaveMonitorSettings.DEFAULT_JOIN_CARD_TITLE)
    }
    var joinCardDesc by remember {
        mutableStateOf(sp.getString(GroupLeaveMonitorSettings.KEY_JOIN_CARD_DESC, GroupLeaveMonitorSettings.DEFAULT_JOIN_CARD_DESC) ?: GroupLeaveMonitorSettings.DEFAULT_JOIN_CARD_DESC)
    }
    var leftCardTitle by remember {
        mutableStateOf(sp.getString(GroupLeaveMonitorSettings.KEY_LEFT_CARD_TITLE, GroupLeaveMonitorSettings.DEFAULT_LEFT_CARD_TITLE) ?: GroupLeaveMonitorSettings.DEFAULT_LEFT_CARD_TITLE)
    }
    var leftCardDesc by remember {
        mutableStateOf(sp.getString(GroupLeaveMonitorSettings.KEY_LEFT_CARD_DESC, GroupLeaveMonitorSettings.DEFAULT_LEFT_CARD_DESC) ?: GroupLeaveMonitorSettings.DEFAULT_LEFT_CARD_DESC)
    }
    var mediaOrder by remember {
        mutableStateOf(sp.getString(GroupLeaveMonitorSettings.KEY_MEDIA_ORDER, GroupLeaveMonitorSettings.DEFAULT_MEDIA_ORDER) ?: GroupLeaveMonitorSettings.DEFAULT_MEDIA_ORDER)
    }
    var mediaSequence by remember {
        mutableStateOf(sp.getString(GroupLeaveMonitorSettings.KEY_MEDIA_SEQUENCE, GroupLeaveMonitorSettings.DEFAULT_MEDIA_SEQUENCE) ?: GroupLeaveMonitorSettings.DEFAULT_MEDIA_SEQUENCE)
    }
    var joinImages by remember { mutableStateOf(sp.getString(GroupLeaveMonitorSettings.KEY_JOIN_IMAGE_PATHS, "").orEmpty()) }
    var leftImages by remember { mutableStateOf(sp.getString(GroupLeaveMonitorSettings.KEY_LEFT_IMAGE_PATHS, "").orEmpty()) }
    var joinVoices by remember { mutableStateOf(sp.getString(GroupLeaveMonitorSettings.KEY_JOIN_VOICE_PATHS, "").orEmpty()) }
    var leftVoices by remember { mutableStateOf(sp.getString(GroupLeaveMonitorSettings.KEY_LEFT_VOICE_PATHS, "").orEmpty()) }
    var joinEmojis by remember { mutableStateOf(sp.getString(GroupLeaveMonitorSettings.KEY_JOIN_EMOJI_PATHS, "").orEmpty()) }
    var leftEmojis by remember { mutableStateOf(sp.getString(GroupLeaveMonitorSettings.KEY_LEFT_EMOJI_PATHS, "").orEmpty()) }
    var joinVideos by remember { mutableStateOf(sp.getString(GroupLeaveMonitorSettings.KEY_JOIN_VIDEO_PATHS, "").orEmpty()) }
    var leftVideos by remember { mutableStateOf(sp.getString(GroupLeaveMonitorSettings.KEY_LEFT_VIDEO_PATHS, "").orEmpty()) }
    var joinFiles by remember { mutableStateOf(sp.getString(GroupLeaveMonitorSettings.KEY_JOIN_FILE_PATHS, "").orEmpty()) }
    var leftFiles by remember { mutableStateOf(sp.getString(GroupLeaveMonitorSettings.KEY_LEFT_FILE_PATHS, "").orEmpty()) }
    var joinFavorites by remember { mutableStateOf(sp.getString(GroupLeaveMonitorSettings.KEY_JOIN_FAVORITE_PATHS, "").orEmpty()) }
    var leftFavorites by remember { mutableStateOf(sp.getString(GroupLeaveMonitorSettings.KEY_LEFT_FAVORITE_PATHS, "").orEmpty()) }
    var promptDelay by remember { mutableStateOf(sp.getInt(GroupLeaveMonitorSettings.KEY_PROMPT_DELAY_MS, GroupLeaveMonitorSettings.DEFAULT_PROMPT_DELAY_MS).toString()) }
    var imageDelay by remember { mutableStateOf(sp.getInt(GroupLeaveMonitorSettings.KEY_IMAGE_DELAY_MS, GroupLeaveMonitorSettings.DEFAULT_MEDIA_DELAY_MS).toString()) }
    var voiceDelay by remember { mutableStateOf(sp.getInt(GroupLeaveMonitorSettings.KEY_VOICE_DELAY_MS, GroupLeaveMonitorSettings.DEFAULT_MEDIA_DELAY_MS).toString()) }
    var emojiDelay by remember { mutableStateOf(sp.getInt(GroupLeaveMonitorSettings.KEY_EMOJI_DELAY_MS, GroupLeaveMonitorSettings.DEFAULT_MEDIA_DELAY_MS).toString()) }
    var videoDelay by remember { mutableStateOf(sp.getInt(GroupLeaveMonitorSettings.KEY_VIDEO_DELAY_MS, GroupLeaveMonitorSettings.DEFAULT_MEDIA_DELAY_MS).toString()) }
    var fileDelay by remember { mutableStateOf(sp.getInt(GroupLeaveMonitorSettings.KEY_FILE_DELAY_MS, GroupLeaveMonitorSettings.DEFAULT_MEDIA_DELAY_MS).toString()) }
    var favoriteDelay by remember { mutableStateOf(sp.getInt(GroupLeaveMonitorSettings.KEY_FAVORITE_DELAY_MS, GroupLeaveMonitorSettings.DEFAULT_MEDIA_DELAY_MS).toString()) }
    val listState = rememberLazyListState()
    val groupEditorListStates = remember { mutableMapOf<String, LazyListState>() }
    val scrollBehavior = MiuixScrollBehavior()

    val route = when {
        templateEditor != null -> GroupLeaveRoute.TemplateEditor(templateEditor!!)
        groupEditor != null -> GroupLeaveRoute.GroupEditor(groupEditor!!.first, groupEditor!!.second)
        showBatchTemplateBinding -> GroupLeaveRoute.BatchTemplateBinding
        showTemplateManager -> GroupLeaveRoute.TemplateManager
        favoritePicker != null -> GroupLeaveRoute.FavoritePicker(favoritePicker!!)
        groupPicker != null -> GroupLeaveRoute.ContactPicker(groupPicker!!)
        showGroupManager -> GroupLeaveRoute.GroupManager
        else -> GroupLeaveRoute.Main
    }

    SettingsRouteTransition(
        targetState = route,
        label = "GroupLeaveRouteTransition",
        depthOf = { it.depth() }
    ) { currentRoute ->
        when (currentRoute) {
            is GroupLeaveRoute.ContactPicker -> {
                val request = currentRoute.request
                ContactPickerPage(
                    context = context,
                    request = request,
                    onBack = { groupPicker = null },
                    onConfirm = { selected ->
                        request.onValue(formatIds(selected.map { it.id }))
                        groupPicker = null
                    }
                )
            }
            is GroupLeaveRoute.FavoritePicker -> {
                FavoritePickerPage(
                    request = currentRoute.request,
                    onBack = { favoritePicker = null }
                )
            }
            GroupLeaveRoute.GroupManager -> {
                GroupMemberGroupManagerPage(
                    context = context,
                    listenGroups = listenGroups,
                    templates = replyTemplates,
                    templateBindings = replyTemplateBindings,
                    onBack = { showGroupManager = false },
                    onOpenTemplates = { showTemplateManager = true },
                    onBatchApplyTemplate = { showBatchTemplateBinding = true },
                    onDeleteGroups = { targets ->
                        val targetIds = targets.mapTo(HashSet()) { it.id }
                        val nextListenGroups = formatIds(parseIds(listenGroups) - targetIds)
                        val nextBindings = replyTemplateBindings.filterNot { it.groupId in targetIds }
                        targetIds.forEach { groupId ->
                            deleteGroupMemberConfiguration(sp, groupId, nextListenGroups, nextBindings)
                            groupEditorListStates.remove(groupId)
                        }
                        listenGroups = nextListenGroups
                        replyTemplateBindings = nextBindings
                        Toast.makeText(context, "已删除 ${targets.size} 个监听群", Toast.LENGTH_SHORT).show()
                    },
                    onPickGroups = {
                        groupPicker = ContactPickerRequest(
                            title = "选择监听群",
                            mode = ContactPickerMode.GROUPS,
                            multiSelect = true,
                            existingValue = listenGroups,
                            onValue = { next ->
                                listenGroups = next
                                sp.edit().putString(GroupLeaveMonitorSettings.KEY_LISTEN_GROUPS, next).apply()
                            }
                        )
                    },
                    onOpenGroup = { id, label -> groupEditor = id to label }
                )
            }
            is GroupLeaveRoute.GroupEditor -> {
                GroupMemberGroupEditorPage(
                    context = context,
                    sp = sp,
                    groupId = currentRoute.groupId,
                    groupLabel = currentRoute.label,
                    templates = replyTemplates,
                    templateBindings = replyTemplateBindings,
                    listState = groupEditorListStates.getOrPut(currentRoute.groupId) { LazyListState() },
                    onTemplateBindingsChanged = { next ->
                        replyTemplateBindings = next
                        sp.edit()
                            .putString(GroupLeaveMonitorSettings.KEY_REPLY_TEMPLATE_BINDINGS, GroupLeaveMonitorSettings.encodeBindings(next))
                            .apply()
                    },
                    onDelete = {
                        val nextListenGroups = formatIds(parseIds(listenGroups) - currentRoute.groupId)
                        val nextBindings = replyTemplateBindings.filterNot { it.groupId == currentRoute.groupId }
                        deleteGroupMemberConfiguration(
                            sp,
                            currentRoute.groupId,
                            nextListenGroups,
                            nextBindings
                        )
                        listenGroups = nextListenGroups
                        replyTemplateBindings = nextBindings
                        groupEditorListStates.remove(currentRoute.groupId)
                        Toast.makeText(context, "群配置已删除", Toast.LENGTH_SHORT).show()
                        groupEditor = null
                    },
                    onBack = { groupEditor = null }
                )
            }
            GroupLeaveRoute.TemplateManager -> {
                GroupLeaveTemplateListPage(
                    templates = replyTemplates,
                    bindings = replyTemplateBindings,
                    onBack = { showTemplateManager = false },
                    onOpenTemplate = { index, template ->
                        templateEditor = GroupLeaveTemplateEditorRequest(index, template, canDelete = true)
                    },
                    onAddTemplate = {
                        templateEditor = GroupLeaveTemplateEditorRequest(
                            replyTemplates.size,
                            newGroupLeaveTemplate(replyTemplates.size + 1, sp),
                            canDelete = false
                        )
                    }
                )
            }
            GroupLeaveRoute.BatchTemplateBinding -> {
                GroupLeaveBatchTemplateBindingPage(
                    context = context,
                    listenGroups = listenGroups,
                    templates = replyTemplates,
                    bindings = replyTemplateBindings,
                    onBack = { showBatchTemplateBinding = false },
                    onSave = { additions ->
                        val next = upsertGroupLeaveTemplateBindings(replyTemplateBindings, additions)
                        replyTemplateBindings = next
                        sp.edit()
                            .putString(GroupLeaveMonitorSettings.KEY_REPLY_TEMPLATE_BINDINGS, GroupLeaveMonitorSettings.encodeBindings(next))
                            .apply()
                        Toast.makeText(context, "模板已套用到 ${additions.size} 个群", Toast.LENGTH_SHORT).show()
                        showBatchTemplateBinding = false
                    }
                )
            }
            is GroupLeaveRoute.TemplateEditor -> {
                val request = currentRoute.request
                GroupLeaveTemplateEditorPage(
                    context = context,
                    request = request,
                    onBack = { templateEditor = null },
                    onSave = { updated ->
                        val next = if (request.index in replyTemplates.indices) {
                            replyTemplates.toMutableList().also { it[request.index] = updated }
                        } else {
                            replyTemplates + updated
                        }
                        replyTemplates = next
                        sp.edit()
                            .putString(GroupLeaveMonitorSettings.KEY_REPLY_TEMPLATES, GroupLeaveMonitorSettings.encodeTemplates(next))
                            .apply()
                        Toast.makeText(context, "模板已保存", Toast.LENGTH_SHORT).show()
                        templateEditor = null
                    },
                    onDelete = {
                        if (request.index in replyTemplates.indices) {
                            val deletedId = replyTemplates[request.index].id
                            val nextTemplates = replyTemplates.toMutableList().also { it.removeAt(request.index) }
                            val nextBindings = replyTemplateBindings.filterNot { it.templateId == deletedId }
                            replyTemplates = nextTemplates
                            replyTemplateBindings = nextBindings
                            sp.edit()
                                .putString(GroupLeaveMonitorSettings.KEY_REPLY_TEMPLATES, GroupLeaveMonitorSettings.encodeTemplates(nextTemplates))
                                .putString(GroupLeaveMonitorSettings.KEY_REPLY_TEMPLATE_BINDINGS, GroupLeaveMonitorSettings.encodeBindings(nextBindings))
                                .apply()
                            Toast.makeText(context, "模板已删除", Toast.LENGTH_SHORT).show()
                        }
                        templateEditor = null
                    }
                )
            }
            GroupLeaveRoute.Main -> PageScaffold(
        title = provider.title(),
        largeTitle = provider.title(),
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "保存设置",
                onPrimaryClick = {
                    val cleaned = MemberTitleStore.cleanColor(wxidColor.substringBefore(','))
                        .ifEmpty { GroupLeaveMonitorSettings.DEFAULT_WXID_COLOR }
                    wxidColor = cleaned
                    sp.edit()
                        .putBoolean(GroupLeaveMonitorSettings.KEY_ENABLE, leaveNoticeEnabled)
                        .putString(GroupLeaveMonitorSettings.KEY_LEAVE_NOTICE_TEXT, leaveNoticeText)
                        .putBoolean(GroupLeaveMonitorSettings.KEY_INVITE_DETAIL_ENABLE, inviteNoticeEnabled)
                        .putString(GroupLeaveMonitorSettings.KEY_INVITE_NOTICE_TEXT, inviteNoticeText)
                        .putString(GroupLeaveMonitorSettings.KEY_NOTICE_SCOPE, noticeScope)
                        .putString(GroupLeaveMonitorSettings.KEY_NOTICE_GROUPS, noticeGroups)
                        .putString(GroupLeaveMonitorSettings.KEY_WXID_COLOR, cleaned)
                        .putString(GroupLeaveMonitorSettings.KEY_LISTEN_GROUPS, listenGroups)
                        .putInt(GroupLeaveMonitorSettings.KEY_DELAY_SECONDS, delaySeconds.toIntOrNull()?.coerceIn(0, 600) ?: GroupLeaveMonitorSettings.DEFAULT_DELAY_SECONDS)
                        .putString(GroupLeaveMonitorSettings.KEY_PROMPT_TYPE, promptType)
                        .putString(GroupLeaveMonitorSettings.KEY_BOTH_ORDER, bothOrder)
                        .putString(GroupLeaveMonitorSettings.KEY_JOIN_TEXT, joinText)
                        .putString(GroupLeaveMonitorSettings.KEY_LEFT_TEXT, leftText)
                        .putString(GroupLeaveMonitorSettings.KEY_JOIN_CARD_TITLE, joinCardTitle)
                        .putString(GroupLeaveMonitorSettings.KEY_JOIN_CARD_DESC, joinCardDesc)
                        .putString(GroupLeaveMonitorSettings.KEY_LEFT_CARD_TITLE, leftCardTitle)
                        .putString(GroupLeaveMonitorSettings.KEY_LEFT_CARD_DESC, leftCardDesc)
                        .putString(GroupLeaveMonitorSettings.KEY_MEDIA_ORDER, mediaOrder)
                        .putString(GroupLeaveMonitorSettings.KEY_MEDIA_SEQUENCE, mediaSequence)
                        .putString(GroupLeaveMonitorSettings.KEY_JOIN_IMAGE_PATHS, joinImages)
                        .putString(GroupLeaveMonitorSettings.KEY_LEFT_IMAGE_PATHS, leftImages)
                        .putString(GroupLeaveMonitorSettings.KEY_JOIN_VOICE_PATHS, joinVoices)
                        .putString(GroupLeaveMonitorSettings.KEY_LEFT_VOICE_PATHS, leftVoices)
                        .putString(GroupLeaveMonitorSettings.KEY_JOIN_EMOJI_PATHS, joinEmojis)
                        .putString(GroupLeaveMonitorSettings.KEY_LEFT_EMOJI_PATHS, leftEmojis)
                        .putString(GroupLeaveMonitorSettings.KEY_JOIN_VIDEO_PATHS, joinVideos)
                        .putString(GroupLeaveMonitorSettings.KEY_LEFT_VIDEO_PATHS, leftVideos)
                        .putString(GroupLeaveMonitorSettings.KEY_JOIN_FILE_PATHS, joinFiles)
                        .putString(GroupLeaveMonitorSettings.KEY_LEFT_FILE_PATHS, leftFiles)
                        .putString(GroupLeaveMonitorSettings.KEY_JOIN_FAVORITE_PATHS, joinFavorites)
                        .putString(GroupLeaveMonitorSettings.KEY_LEFT_FAVORITE_PATHS, leftFavorites)
                        .putInt(GroupLeaveMonitorSettings.KEY_PROMPT_DELAY_MS, promptDelay.toIntOrNull()?.coerceAtLeast(0) ?: GroupLeaveMonitorSettings.DEFAULT_PROMPT_DELAY_MS)
                        .putInt(GroupLeaveMonitorSettings.KEY_IMAGE_DELAY_MS, imageDelay.toIntOrNull()?.coerceAtLeast(0) ?: GroupLeaveMonitorSettings.DEFAULT_MEDIA_DELAY_MS)
                        .putInt(GroupLeaveMonitorSettings.KEY_VOICE_DELAY_MS, voiceDelay.toIntOrNull()?.coerceAtLeast(0) ?: GroupLeaveMonitorSettings.DEFAULT_MEDIA_DELAY_MS)
                        .putInt(GroupLeaveMonitorSettings.KEY_EMOJI_DELAY_MS, emojiDelay.toIntOrNull()?.coerceAtLeast(0) ?: GroupLeaveMonitorSettings.DEFAULT_MEDIA_DELAY_MS)
                        .putInt(GroupLeaveMonitorSettings.KEY_VIDEO_DELAY_MS, videoDelay.toIntOrNull()?.coerceAtLeast(0) ?: GroupLeaveMonitorSettings.DEFAULT_MEDIA_DELAY_MS)
                        .putInt(GroupLeaveMonitorSettings.KEY_FILE_DELAY_MS, fileDelay.toIntOrNull()?.coerceAtLeast(0) ?: GroupLeaveMonitorSettings.DEFAULT_MEDIA_DELAY_MS)
                        .putInt(GroupLeaveMonitorSettings.KEY_FAVORITE_DELAY_MS, favoriteDelay.toIntOrNull()?.coerceAtLeast(0) ?: GroupLeaveMonitorSettings.DEFAULT_MEDIA_DELAY_MS)
                        .apply()
                    Toast.makeText(context, "设置已保存", Toast.LENGTH_SHORT).show()
                },
                secondaryText = "返回",
                onSecondaryClick = onBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(text = "系统消息") }
            item {
                SettingsCard {
                    SwitchRow(
                        leaveNoticeEnabled,
                        "退群系统消息",
                        "保留现有退群监控逻辑，检测到成员退出后插入可点击资料页的系统消息",
                        onCheckedChange = {
                            leaveNoticeEnabled = it
                            sp.edit().putBoolean(GroupLeaveMonitorSettings.KEY_ENABLE, it).apply()
                        }
                    )
                    if (leaveNoticeEnabled) {
                        InsetDivider()
                        VariableInputRow(
                            "退群系统消息模板",
                            "wxid变量会生成可点击资料链接",
                            leaveNoticeText,
                            groupLeaveNoticeVariables,
                            minLines = 3
                        ) { leaveNoticeText = it }
                    }
                    InsetDivider()
                    SwitchRow(
                        inviteNoticeEnabled,
                        "邀请详情",
                        "检测到邀请进群后插入邀请者、被邀请者和累计邀请次数",
                        onCheckedChange = {
                            inviteNoticeEnabled = it
                            sp.edit().putBoolean(GroupLeaveMonitorSettings.KEY_INVITE_DETAIL_ENABLE, it).apply()
                        }
                    )
                    if (inviteNoticeEnabled) {
                        InsetDivider()
                        VariableInputRow(
                            "邀请详情系统消息模板",
                            "邀请者和被邀请者wxid会生成可点击资料链接",
                            inviteNoticeText,
                            groupInviteNoticeVariables,
                            minLines = 4
                        ) { inviteNoticeText = it }
                    }
                    if (leaveNoticeEnabled || inviteNoticeEnabled) {
                        InsetDivider()
                        PopupChoiceRow(
                            title = "适用群聊",
                            summary = systemNoticeScopeLabel(noticeScope, GroupLeaveMonitorSettings.NOTICE_SCOPE_SPECIFIC),
                            options = systemNoticeScopeChoices(
                                GroupLeaveMonitorSettings.NOTICE_SCOPE_ALL,
                                GroupLeaveMonitorSettings.NOTICE_SCOPE_SPECIFIC
                            ),
                            currentValue = noticeScope,
                            onValueChanged = { noticeScope = it }
                        )
                        if (noticeScope == GroupLeaveMonitorSettings.NOTICE_SCOPE_SPECIFIC) {
                            InsetDivider()
                            ActionRow("选择指定群聊", systemNoticeGroupSummary(noticeGroups)) {
                                groupPicker = ContactPickerRequest(
                                    title = "选择系统消息群聊",
                                    mode = ContactPickerMode.GROUPS,
                                    multiSelect = true,
                                    existingValue = noticeGroups,
                                    onValue = { noticeGroups = it }
                                )
                            }
                        }
                    }
                    InsetDivider()
                    ColorPickerRow(
                        "wxid颜色",
                        "退群和邀请详情里的 wxid 链接颜色",
                        wxidColor,
                        allowGradient = false,
                        onReset = { wxidColor = GroupLeaveMonitorSettings.DEFAULT_WXID_COLOR }
                    ) {
                        wxidColor = it.take(9)
                    }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "进退群回复") }
            item {
                SettingsCard {
                    SwitchRow(
                        sp,
                        GroupLeaveMonitorSettings.KEY_REPLY_ENABLE,
                        "启用自动回复",
                        "只对下方监听群生效，默认关闭",
                        GroupLeaveMonitorSettings.DEFAULT_REPLY_ENABLE
                    )
                    InsetDivider()
                    SwitchRow(
                        sp,
                        GroupLeaveMonitorSettings.KEY_JOIN_REPLY_ENABLE,
                        "进群回复",
                        "检测到新成员进群后发送欢迎内容",
                        GroupLeaveMonitorSettings.DEFAULT_JOIN_REPLY_ENABLE
                    )
                    InsetDivider()
                    SwitchRow(
                        sp,
                        GroupLeaveMonitorSettings.KEY_LEFT_REPLY_ENABLE,
                        "退群回复",
                        "检测到成员退群后发送退群内容",
                        GroupLeaveMonitorSettings.DEFAULT_LEFT_REPLY_ENABLE
                    )
                    InsetDivider()
                    ActionRow("监听群与专属设置", groupSelectionSummary(listenGroups)) {
                        showGroupManager = true
                    }
                    InsetDivider()
                    ActionRow(
                        "回复模板管理",
                        if (replyTemplates.isEmpty()) "暂无模板，进入后添加批量配置" else "${replyTemplates.size} 个模板，进入后修改或删除"
                    ) {
                        showTemplateManager = true
                    }
                    InsetDivider()
                    ActionRow(
                        "批量套用模板",
                        if (replyTemplates.isEmpty()) "先新增模板，再批量绑定监听群" else groupLeaveTemplateBindingSummary(replyTemplateBindings, replyTemplates)
                    ) {
                        showBatchTemplateBinding = true
                    }
                    InsetDivider()
                    NumberInputRow("整体延迟", "单位秒，0-600", delaySeconds) { delaySeconds = it }
                    InsetDivider()
                    PopupChoiceRow(
                        title = "提示类型",
                        summary = groupPromptTypeLabel(promptType),
                        options = groupPromptTypeChoices(),
                        currentValue = promptType,
                        onValueChanged = {
                            promptType = it
                        }
                    )
                    if (promptType == GroupLeaveMonitorSettings.PROMPT_BOTH) {
                        InsetDivider()
                        PopupChoiceRow(
                            title = "文本+卡片顺序",
                            summary = groupBothOrderLabel(bothOrder),
                            options = groupBothOrderChoices(),
                            currentValue = bothOrder,
                            onValueChanged = {
                                bothOrder = it
                            }
                        )
                    }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "文本模板") }
            item {
                SettingsCard {
                    VariableInputRow(
                        title = "进群文本",
                        summary = "多个模板用 || 分隔随机选择",
                        value = joinText,
                        variables = groupMemberTemplateVariables,
                        minLines = 4,
                        onValueChange = { joinText = it }
                    )
                    InsetDivider()
                    VariableInputRow(
                        title = "退群文本",
                        summary = "多个模板用 || 分隔随机选择",
                        value = leftText,
                        variables = groupMemberTemplateVariables,
                        minLines = 4,
                        onValueChange = { leftText = it }
                    )
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "卡片模板") }
            item {
                SettingsCard {
                    VariableInputRow("进群卡片标题", "支持变量", joinCardTitle, groupMemberTemplateVariables) { joinCardTitle = it }
                    InsetDivider()
                    VariableInputRow("进群卡片描述", "支持变量", joinCardDesc, groupMemberTemplateVariables, minLines = 3) { joinCardDesc = it }
                    InsetDivider()
                    VariableInputRow("退群卡片标题", "支持变量", leftCardTitle, groupMemberTemplateVariables) { leftCardTitle = it }
                    InsetDivider()
                    VariableInputRow("退群卡片描述", "支持变量", leftCardDesc, groupMemberTemplateVariables, minLines = 3) { leftCardDesc = it }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "媒体") }
            item {
                SettingsCard {
                    PopupChoiceRow(
                        title = "媒体顺序",
                        summary = groupMediaOrderLabel(mediaOrder),
                        options = groupMediaOrderChoices(),
                        currentValue = mediaOrder,
                        onValueChanged = {
                            mediaOrder = it
                        }
                    )
                    InsetDivider()
                    InputRow("媒体类型顺序", "英文逗号分隔：image,voice,emoji,video,file,favorite", mediaSequence) { mediaSequence = it }
                    InsetDivider()
                    GroupMemberMediaRow(context, "进群图片", joinImages, RedPacketRuleConfig.REPLY_IMAGE) { joinImages = it }
                    InsetDivider()
                    GroupMemberMediaRow(context, "退群图片", leftImages, RedPacketRuleConfig.REPLY_IMAGE) { leftImages = it }
                    InsetDivider()
                    GroupMemberMediaRow(context, "进群语音", joinVoices, RedPacketRuleConfig.REPLY_VOICE) { joinVoices = it }
                    InsetDivider()
                    GroupMemberMediaRow(context, "退群语音", leftVoices, RedPacketRuleConfig.REPLY_VOICE) { leftVoices = it }
                    InsetDivider()
                    GroupMemberMediaRow(context, "进群表情", joinEmojis, RedPacketRuleConfig.REPLY_EMOJI) { joinEmojis = it }
                    InsetDivider()
                    GroupMemberMediaRow(context, "退群表情", leftEmojis, RedPacketRuleConfig.REPLY_EMOJI) { leftEmojis = it }
                    InsetDivider()
                    GroupMemberMediaRow(context, "进群视频", joinVideos, RedPacketRuleConfig.REPLY_VIDEO) { joinVideos = it }
                    InsetDivider()
                    GroupMemberMediaRow(context, "退群视频", leftVideos, RedPacketRuleConfig.REPLY_VIDEO) { leftVideos = it }
                    InsetDivider()
                    GroupMemberMediaRow(context, "进群文件", joinFiles, RedPacketRuleConfig.REPLY_FILE) { joinFiles = it }
                    InsetDivider()
                    GroupMemberMediaRow(context, "退群文件", leftFiles, RedPacketRuleConfig.REPLY_FILE) { leftFiles = it }
                    InsetDivider()
                    GroupMemberMediaRow(
                        context,
                        "进群收藏",
                        joinFavorites,
                        RedPacketRuleConfig.REPLY_FAVORITE,
                        onValueChange = { joinFavorites = it },
                        onFavoritePicker = { favoritePicker = it }
                    )
                    InsetDivider()
                    GroupMemberMediaRow(
                        context,
                        "退群收藏",
                        leftFavorites,
                        RedPacketRuleConfig.REPLY_FAVORITE,
                        onValueChange = { leftFavorites = it },
                        onFavoritePicker = { favoritePicker = it }
                    )
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "精细延迟") }
            item {
                SettingsCard {
                    NumberInputRow("提示延迟", "单位 ms", promptDelay) { promptDelay = it }
                    InsetDivider()
                    NumberInputRow("图片延迟", "单位 ms", imageDelay) { imageDelay = it }
                    InsetDivider()
                    NumberInputRow("语音延迟", "单位 ms", voiceDelay) { voiceDelay = it }
                    InsetDivider()
                    NumberInputRow("表情延迟", "单位 ms", emojiDelay) { emojiDelay = it }
                    InsetDivider()
                    NumberInputRow("视频延迟", "单位 ms", videoDelay) { videoDelay = it }
                    InsetDivider()
                    NumberInputRow("文件延迟", "单位 ms", fileDelay) { fileDelay = it }
                    InsetDivider()
                    NumberInputRow("收藏延迟", "单位 ms", favoriteDelay) { favoriteDelay = it }
                }
            }
        }
    }
        }
    }
}

@Composable
internal fun GroupMemberGroupManagerPage(
    context: Context,
    listenGroups: String,
    templates: List<GroupLeaveReplyTemplate>,
    templateBindings: List<GroupLeaveReplyTemplateBinding>,
    onBack: () -> Unit,
    onOpenTemplates: () -> Unit,
    onBatchApplyTemplate: () -> Unit,
    onDeleteGroups: (List<ContactOption>) -> Unit,
    onPickGroups: () -> Unit,
    onOpenGroup: (String, String) -> Unit
) {
    var loading by remember(listenGroups) { mutableStateOf(true) }
    var error by remember(listenGroups) { mutableStateOf("") }
    var groups by remember(listenGroups) { mutableStateOf(emptyList<ContactOption>()) }
    var query by remember { mutableStateOf("") }
    var batchDeleteMode by remember { mutableStateOf(false) }
    var selectedForDelete by remember { mutableStateOf<Set<String>>(emptySet()) }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    val selectedIds = remember(listenGroups) { parseIds(listenGroups).toList() }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()

    LaunchedEffect(listenGroups) {
        loadContacts(context, ContactPickerMode.GROUPS) { result, throwable ->
            loading = false
            if (throwable != null) {
                error = throwable.message ?: "读取群聊失败"
            } else {
                val byId = result.associateBy { it.id }
                groups = selectedIds.map { id ->
                    byId[id] ?: ContactOption(id, id, group = true, avatarUrl = "", avatarBackupUrl = "")
                }
            }
        }
    }

    val lower = query.trim().lowercase(Locale.US)
    val visible = groups.filter { it.matchesSearch(lower) }
    val visibleIds = visible.mapTo(LinkedHashSet()) { it.id }
    val allVisibleSelected = visibleIds.isNotEmpty() && visibleIds.all { it in selectedForDelete }
    val selectedGroups = groups.filter { it.id in selectedForDelete }
    PageScaffold(
        title = "监听群",
        largeTitle = "监听群",
        scrollBehavior = scrollBehavior,
        bottomBar = {
            if (batchDeleteMode) {
                BottomActionBar(
                    primaryText = "删除所选（${selectedGroups.size}）",
                    onPrimaryClick = {
                        if (selectedGroups.isEmpty()) {
                            Toast.makeText(context, "请先选择监听群", Toast.LENGTH_SHORT).show()
                        } else {
                            showDeleteConfirm = true
                        }
                    },
                    secondaryText = "取消",
                    onSecondaryClick = {
                        batchDeleteMode = false
                        selectedForDelete = emptySet()
                    },
                    middleText = if (visibleIds.isEmpty()) null else if (allVisibleSelected) "取消全选" else "全选",
                    onMiddleClick = if (visibleIds.isEmpty()) null else {
                        {
                            selectedForDelete = if (allVisibleSelected) {
                                selectedForDelete - visibleIds
                            } else {
                                selectedForDelete + visibleIds
                            }
                        }
                    }
                )
            } else {
                BottomActionBar(
                    primaryText = "选择监听群",
                    onPrimaryClick = onPickGroups,
                    secondaryText = "返回",
                    onSecondaryClick = onBack,
                    middleText = if (selectedIds.isEmpty()) null else "批量删除",
                    onMiddleClick = if (selectedIds.isEmpty()) null else {
                        {
                            batchDeleteMode = true
                            selectedForDelete = emptySet()
                        }
                    }
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item {
                SettingsCard {
                    InputRow("搜索监听群", "群名称 / 群聊备注 / 群号", query) { query = it }
                }
            }
            if (!batchDeleteMode) {
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "模板") }
                item {
                    SettingsCard {
                        ActionRow(
                            "回复模板管理",
                            if (templates.isEmpty()) "暂无模板，进入后添加" else "${templates.size} 个模板"
                        ) {
                            onOpenTemplates()
                        }
                        InsetDivider()
                        ActionRow(
                            "批量套用模板",
                            if (templates.isEmpty()) "先新增模板，再批量绑定监听群" else groupLeaveTemplateBindingSummary(templateBindings, templates)
                        ) {
                            onBatchApplyTemplate()
                        }
                    }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "已监听群 · ${selectedIds.size} 项") }
            item {
                SettingsCard {
                    when {
                        loading -> EmptyText("正在载入监听群...")
                        error.isNotEmpty() -> EmptyText(error)
                        selectedIds.isEmpty() -> EmptyText("暂无监听群。点击底部“选择监听群”添加。")
                        visible.isEmpty() -> EmptyText("没有匹配结果")
                        else -> visible.forEachIndexed { index, option ->
                            if (batchDeleteMode) {
                                OptionChoiceRow(
                                    item = OptionItem(
                                        label = option.label.ifBlank { option.id },
                                        value = index,
                                        summary = groupLeaveGroupBindingSummary(option.id, option.id, templateBindings, templates)
                                    ),
                                    selected = option.id in selectedForDelete,
                                    onClick = {
                                        selectedForDelete = if (option.id in selectedForDelete) {
                                            selectedForDelete - option.id
                                        } else {
                                            selectedForDelete + option.id
                                        }
                                    }
                                )
                            } else {
                                SelectRow(
                                    title = option.label.ifBlank { option.id },
                                    summary = groupLeaveGroupBindingSummary(option.id, option.id, templateBindings, templates),
                                    onClick = { onOpenGroup(option.id, option.label.ifBlank { option.id }) }
                                )
                            }
                            if (index < visible.lastIndex) InsetDivider()
                        }
                    }
                }
            }
        }
    }
    BatchDeleteConfirmDialog(
        show = showDeleteConfirm,
        message = "将删除已选的 ${selectedGroups.size} 个进退群监听群及其专属配置，此操作不可撤销。",
        labels = selectedGroups.map { it.label.ifBlank { it.id } },
        onDismiss = { showDeleteConfirm = false },
        onConfirm = {
            val targets = selectedGroups
            showDeleteConfirm = false
            batchDeleteMode = false
            selectedForDelete = emptySet()
            onDeleteGroups(targets)
        }
    )
}

@Composable
internal fun GroupLeaveTemplateListPage(
    templates: List<GroupLeaveReplyTemplate>,
    bindings: List<GroupLeaveReplyTemplateBinding>,
    onBack: () -> Unit,
    onOpenTemplate: (Int, GroupLeaveReplyTemplate) -> Unit,
    onAddTemplate: () -> Unit
) {
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    PageScaffold(
        title = "回复模板",
        largeTitle = "回复模板",
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "新增模板",
                onPrimaryClick = onAddTemplate,
                secondaryText = "返回",
                onSecondaryClick = onBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(text = "模板") }
            item {
                SettingsCard {
                    if (templates.isEmpty()) {
                        EmptyText("暂无模板。模板保存一整套进退群回复配置，再批量套用到监听群。")
                    } else {
                        templates.forEachIndexed { index, template ->
                            val count = bindings.count { it.templateId == template.id }
                            SelectRow(
                                title = template.name.ifBlank { "模板 ${index + 1}" },
                                summary = "${describeGroupLeaveTemplate(template)} · 已绑定 $count 个群",
                                onClick = { onOpenTemplate(index, template) }
                            )
                            if (index < templates.lastIndex) InsetDivider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun GroupLeaveBatchTemplateBindingPage(
    context: Context,
    listenGroups: String,
    templates: List<GroupLeaveReplyTemplate>,
    bindings: List<GroupLeaveReplyTemplateBinding>,
    onBack: () -> Unit,
    onSave: (List<GroupLeaveReplyTemplateBinding>) -> Unit
) {
    var loading by remember(listenGroups) { mutableStateOf(true) }
    var error by remember(listenGroups) { mutableStateOf("") }
    var groups by remember(listenGroups) { mutableStateOf(emptyList<ContactOption>()) }
    var query by remember { mutableStateOf("") }
    var selectedTemplateId by remember(templates) { mutableStateOf(templates.firstOrNull()?.id.orEmpty()) }
    var selectedGroups by remember(listenGroups) { mutableStateOf(parseIds(listenGroups)) }
    val selectedIds = remember(listenGroups) { parseIds(listenGroups).toList() }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()

    LaunchedEffect(listenGroups) {
        loadContacts(context, ContactPickerMode.GROUPS) { result, throwable ->
            loading = false
            if (throwable != null) {
                error = throwable.message ?: "读取群聊失败"
            } else {
                val byId = result.associateBy { it.id }
                groups = selectedIds.map { id ->
                    byId[id] ?: ContactOption(id, id, group = true, avatarUrl = "", avatarBackupUrl = "")
                }
            }
        }
    }

    val templateOptions = templates.map { template ->
        PopupChoice(template.name.ifBlank { template.id }, template.id)
    }
    val lower = query.trim().lowercase(Locale.US)
    val visible = groups.filter { it.matchesSearch(lower) }
    val visibleIds = visible.map { it.id }.toSet()
    val visibleAllSelected = visibleIds.isNotEmpty() && visibleIds.all { selectedGroups.contains(it) }
    PageScaffold(
        title = "批量套用模板",
        largeTitle = "批量套用模板",
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "套用模板",
                onPrimaryClick = {
                    if (templates.isEmpty()) {
                        Toast.makeText(context, "请先新增模板", Toast.LENGTH_SHORT).show()
                    } else if (selectedTemplateId.isBlank()) {
                        Toast.makeText(context, "请先选择模板", Toast.LENGTH_SHORT).show()
                    } else if (selectedGroups.isEmpty()) {
                        Toast.makeText(context, "请先选择群", Toast.LENGTH_SHORT).show()
                    } else {
                        val labels = groups.associate { it.id to it.label.ifBlank { it.id } }
                        onSave(
                            selectedGroups.map { groupId ->
                                GroupLeaveReplyTemplateBinding(
                                    groupId = groupId,
                                    label = labels[groupId] ?: bindings.firstOrNull { it.groupId == groupId }?.label ?: groupId,
                                    templateId = selectedTemplateId
                                )
                            }
                        )
                    }
                },
                secondaryText = "返回",
                onSecondaryClick = onBack,
                middleText = if (visibleIds.isNotEmpty()) {
                    if (visibleAllSelected) "取消全选" else "全选"
                } else {
                    null
                },
                onMiddleClick = if (visibleIds.isNotEmpty()) {
                    {
                        selectedGroups = if (visibleAllSelected) {
                            selectedGroups - visibleIds
                        } else {
                            selectedGroups + visibleIds
                        }
                    }
                } else {
                    null
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(text = "模板") }
            item {
                SettingsCard {
                    if (templates.isEmpty()) {
                        EmptyText("暂无模板。先进入“回复模板管理”新增模板。")
                    } else {
                        PopupChoiceRow(
                            title = "选择模板",
                            summary = templates.firstOrNull { it.id == selectedTemplateId }?.let { describeGroupLeaveTemplate(it) } ?: "未选择",
                            options = templateOptions,
                            currentValue = selectedTemplateId,
                            onValueChanged = { selectedTemplateId = it }
                        )
                    }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "监听群") }
            item {
                SettingsCard {
                    InputRow("搜索监听群", "群名称 / 群聊备注 / 群号", query) { query = it }
                }
            }
            item {
                SettingsCard {
                    when {
                        loading -> EmptyText("正在载入监听群...")
                        error.isNotEmpty() -> EmptyText(error)
                        selectedIds.isEmpty() -> EmptyText("暂无监听群。先选择需要监听的群。")
                        visible.isEmpty() -> EmptyText("没有匹配结果")
                        else -> visible.forEachIndexed { index, option ->
                            SwitchRow(
                                checked = selectedGroups.contains(option.id),
                                title = option.label.ifBlank { option.id },
                                summary = groupLeaveGroupBindingSummary(option.id, option.id, bindings, templates),
                                onCheckedChange = { checked ->
                                    selectedGroups = if (checked) selectedGroups + option.id else selectedGroups - option.id
                                }
                            )
                            if (index < visible.lastIndex) InsetDivider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun GroupLeaveTemplateEditorPage(
    context: Context,
    request: GroupLeaveTemplateEditorRequest,
    onBack: () -> Unit,
    onSave: (GroupLeaveReplyTemplate) -> Unit,
    onDelete: () -> Unit
) {
    var favoritePicker by remember { mutableStateOf<FavoritePickerRequest?>(null) }
    var name by remember(request) { mutableStateOf(request.template.name) }
    var enabled by remember(request) { mutableStateOf(request.template.enabled) }
    var joinEnabled by remember(request) { mutableStateOf(request.template.joinEnabled) }
    var leftEnabled by remember(request) { mutableStateOf(request.template.leftEnabled) }
    var promptType by remember(request) { mutableStateOf(request.template.promptType) }
    var bothOrder by remember(request) { mutableStateOf(request.template.bothOrder) }
    var joinText by remember(request) { mutableStateOf(request.template.joinText) }
    var leftText by remember(request) { mutableStateOf(request.template.leftText) }
    var joinCardTitle by remember(request) { mutableStateOf(request.template.joinCardTitle) }
    var joinCardDesc by remember(request) { mutableStateOf(request.template.joinCardDesc) }
    var leftCardTitle by remember(request) { mutableStateOf(request.template.leftCardTitle) }
    var leftCardDesc by remember(request) { mutableStateOf(request.template.leftCardDesc) }
    var mediaMode by remember(request) { mutableStateOf(request.template.mediaMode) }
    var mediaOrder by remember(request) { mutableStateOf(request.template.mediaOrder) }
    var mediaSequence by remember(request) { mutableStateOf(request.template.mediaSequence) }
    var joinImages by remember(request) { mutableStateOf(request.template.joinImages) }
    var leftImages by remember(request) { mutableStateOf(request.template.leftImages) }
    var joinVoices by remember(request) { mutableStateOf(request.template.joinVoices) }
    var leftVoices by remember(request) { mutableStateOf(request.template.leftVoices) }
    var joinEmojis by remember(request) { mutableStateOf(request.template.joinEmojis) }
    var leftEmojis by remember(request) { mutableStateOf(request.template.leftEmojis) }
    var joinVideos by remember(request) { mutableStateOf(request.template.joinVideos) }
    var leftVideos by remember(request) { mutableStateOf(request.template.leftVideos) }
    var joinFiles by remember(request) { mutableStateOf(request.template.joinFiles) }
    var leftFiles by remember(request) { mutableStateOf(request.template.leftFiles) }
    var joinFavorites by remember(request) { mutableStateOf(request.template.joinFavorites) }
    var leftFavorites by remember(request) { mutableStateOf(request.template.leftFavorites) }
    var delayMode by remember(request) { mutableStateOf(request.template.delayMode) }
    var promptDelay by remember(request) { mutableStateOf(request.template.promptDelayMs.coerceAtLeast(0).toString()) }
    var imageDelay by remember(request) { mutableStateOf(request.template.imageDelayMs.coerceAtLeast(0).toString()) }
    var voiceDelay by remember(request) { mutableStateOf(request.template.voiceDelayMs.coerceAtLeast(0).toString()) }
    var emojiDelay by remember(request) { mutableStateOf(request.template.emojiDelayMs.coerceAtLeast(0).toString()) }
    var videoDelay by remember(request) { mutableStateOf(request.template.videoDelayMs.coerceAtLeast(0).toString()) }
    var fileDelay by remember(request) { mutableStateOf(request.template.fileDelayMs.coerceAtLeast(0).toString()) }
    var favoriteDelay by remember(request) { mutableStateOf(request.template.favoriteDelayMs.coerceAtLeast(0).toString()) }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()

    SettingsRouteTransition(
        targetState = favoritePicker,
        label = "GroupLeaveTemplateFavoriteRoute",
        depthOf = { if (it == null) 0 else 1 }
    ) { currentFavoritePicker ->
        if (currentFavoritePicker != null) {
            FavoritePickerPage(
                request = currentFavoritePicker,
                onBack = { favoritePicker = null }
            )
        } else PageScaffold(
            title = name.ifBlank { "回复模板" },
            largeTitle = name.ifBlank { "回复模板" },
            scrollBehavior = scrollBehavior,
            bottomBar = {
                BottomActionBar(
                    primaryText = "保存模板",
                    onPrimaryClick = {
                        onSave(
                            GroupLeaveReplyTemplate(
                                id = request.template.id,
                                name = name.trim().ifBlank { "模板 ${request.index + 1}" },
                                enabled = enabled,
                                joinEnabled = joinEnabled,
                                leftEnabled = leftEnabled,
                                promptType = GroupLeaveMonitorSettings.normalizePromptType(promptType),
                                bothOrder = GroupLeaveMonitorSettings.normalizeBothOrder(bothOrder),
                                joinText = joinText,
                                leftText = leftText,
                                joinCardTitle = joinCardTitle,
                                joinCardDesc = joinCardDesc,
                                leftCardTitle = leftCardTitle,
                                leftCardDesc = leftCardDesc,
                                mediaMode = GroupLeaveMonitorSettings.normalizeMediaMode(mediaMode),
                                mediaOrder = GroupLeaveMonitorSettings.normalizeMediaOrder(mediaOrder),
                                mediaSequence = mediaSequence,
                                joinImages = joinImages,
                                leftImages = leftImages,
                                joinVoices = joinVoices,
                                leftVoices = leftVoices,
                                joinEmojis = joinEmojis,
                                leftEmojis = leftEmojis,
                                joinVideos = joinVideos,
                                leftVideos = leftVideos,
                                joinFiles = joinFiles,
                                leftFiles = leftFiles,
                                joinFavorites = joinFavorites,
                                leftFavorites = leftFavorites,
                                delayMode = GroupLeaveMonitorSettings.normalizeMode(delayMode),
                                promptDelayMs = promptDelay.toIntOrNull()?.coerceAtLeast(0) ?: GroupLeaveMonitorSettings.DEFAULT_PROMPT_DELAY_MS,
                                imageDelayMs = imageDelay.toIntOrNull()?.coerceAtLeast(0) ?: GroupLeaveMonitorSettings.DEFAULT_MEDIA_DELAY_MS,
                                voiceDelayMs = voiceDelay.toIntOrNull()?.coerceAtLeast(0) ?: GroupLeaveMonitorSettings.DEFAULT_MEDIA_DELAY_MS,
                                emojiDelayMs = emojiDelay.toIntOrNull()?.coerceAtLeast(0) ?: GroupLeaveMonitorSettings.DEFAULT_MEDIA_DELAY_MS,
                                videoDelayMs = videoDelay.toIntOrNull()?.coerceAtLeast(0) ?: GroupLeaveMonitorSettings.DEFAULT_MEDIA_DELAY_MS,
                                fileDelayMs = fileDelay.toIntOrNull()?.coerceAtLeast(0) ?: GroupLeaveMonitorSettings.DEFAULT_MEDIA_DELAY_MS,
                                favoriteDelayMs = favoriteDelay.toIntOrNull()?.coerceAtLeast(0) ?: GroupLeaveMonitorSettings.DEFAULT_MEDIA_DELAY_MS
                            )
                        )
                    },
                    secondaryText = "返回",
                    onSecondaryClick = onBack
                )
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
                state = listState,
                contentPadding = PaddingValues(
                    top = padding.calculateTopPadding() + 8.dp,
                    bottom = padding.calculateBottomPadding() + 84.dp
                )
            ) {
                item { SmallTitle(text = "模板") }
                item {
                    SettingsCard {
                        InputRow("模板名称", "用于区分不同群回复配置", name) { name = it }
                        InsetDivider()
                        SwitchRow(enabled, "启用模板", "关闭后绑定此模板的群不会按模板回复", onCheckedChange = { enabled = it })
                    }
                }
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "事件") }
                item {
                    SettingsCard {
                        SwitchRow(joinEnabled, "进群回复", "绑定群检测到成员进群时发送", onCheckedChange = { joinEnabled = it })
                        InsetDivider()
                        SwitchRow(leftEnabled, "退群回复", "绑定群检测到成员退群时发送", onCheckedChange = { leftEnabled = it })
                    }
                }
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "提示模板") }
                item {
                    SettingsCard {
                        PopupChoiceRow(
                            title = "提示设置",
                            summary = groupPromptTypeWithGlobalLabel(promptType),
                            options = groupPromptTypeWithGlobalChoices(),
                            currentValue = promptType,
                            onValueChanged = { promptType = it }
                        )
                        if (promptType != GroupLeaveMonitorSettings.MODE_GLOBAL) {
                            if (promptType == GroupLeaveMonitorSettings.PROMPT_BOTH) {
                                InsetDivider()
                                PopupChoiceRow(
                                    title = "文本+卡片顺序",
                                    summary = groupBothOrderLabel(bothOrder),
                                    options = groupBothOrderChoices(),
                                    currentValue = bothOrder,
                                    onValueChanged = { bothOrder = it }
                                )
                            }
                            InsetDivider()
                            VariableInputRow("进群文本", "多个模板用 || 分隔随机选择", joinText, groupMemberTemplateVariables, minLines = 4) { joinText = it }
                            InsetDivider()
                            VariableInputRow("退群文本", "多个模板用 || 分隔随机选择", leftText, groupMemberTemplateVariables, minLines = 4) { leftText = it }
                            InsetDivider()
                            VariableInputRow("进群卡片标题", "支持变量", joinCardTitle, groupMemberTemplateVariables) { joinCardTitle = it }
                            InsetDivider()
                            VariableInputRow("进群卡片描述", "支持变量", joinCardDesc, groupMemberTemplateVariables, minLines = 3) { joinCardDesc = it }
                            InsetDivider()
                            VariableInputRow("退群卡片标题", "支持变量", leftCardTitle, groupMemberTemplateVariables) { leftCardTitle = it }
                            InsetDivider()
                            VariableInputRow("退群卡片描述", "支持变量", leftCardDesc, groupMemberTemplateVariables, minLines = 3) { leftCardDesc = it }
                        }
                    }
                }
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "媒体") }
                item {
                    SettingsCard {
                        PopupChoiceRow(
                            title = "媒体设置",
                            summary = groupMediaModeLabel(mediaMode),
                            options = groupMediaModeChoices(),
                            currentValue = mediaMode,
                            onValueChanged = { mediaMode = it }
                        )
                        if (mediaMode == GroupLeaveMonitorSettings.MODE_CUSTOM) {
                            InsetDivider()
                            PopupChoiceRow(
                                title = "媒体顺序",
                                summary = groupMediaOrderLabel(mediaOrder),
                                options = groupMediaOrderChoices(),
                                currentValue = mediaOrder,
                                onValueChanged = { mediaOrder = it }
                            )
                            InsetDivider()
                            InputRow("媒体类型顺序", "英文逗号分隔：image,voice,emoji,video,file,favorite", mediaSequence) { mediaSequence = it }
                            InsetDivider()
                            GroupMemberMediaRow(context, "进群图片", joinImages, RedPacketRuleConfig.REPLY_IMAGE) { joinImages = it }
                            InsetDivider()
                            GroupMemberMediaRow(context, "退群图片", leftImages, RedPacketRuleConfig.REPLY_IMAGE) { leftImages = it }
                            InsetDivider()
                            GroupMemberMediaRow(context, "进群语音", joinVoices, RedPacketRuleConfig.REPLY_VOICE) { joinVoices = it }
                            InsetDivider()
                            GroupMemberMediaRow(context, "退群语音", leftVoices, RedPacketRuleConfig.REPLY_VOICE) { leftVoices = it }
                            InsetDivider()
                            GroupMemberMediaRow(context, "进群表情", joinEmojis, RedPacketRuleConfig.REPLY_EMOJI) { joinEmojis = it }
                            InsetDivider()
                            GroupMemberMediaRow(context, "退群表情", leftEmojis, RedPacketRuleConfig.REPLY_EMOJI) { leftEmojis = it }
                            InsetDivider()
                            GroupMemberMediaRow(context, "进群视频", joinVideos, RedPacketRuleConfig.REPLY_VIDEO) { joinVideos = it }
                            InsetDivider()
                            GroupMemberMediaRow(context, "退群视频", leftVideos, RedPacketRuleConfig.REPLY_VIDEO) { leftVideos = it }
                            InsetDivider()
                            GroupMemberMediaRow(context, "进群文件", joinFiles, RedPacketRuleConfig.REPLY_FILE) { joinFiles = it }
                            InsetDivider()
                            GroupMemberMediaRow(context, "退群文件", leftFiles, RedPacketRuleConfig.REPLY_FILE) { leftFiles = it }
                            InsetDivider()
                            GroupMemberMediaRow(
                                context,
                                "进群收藏",
                                joinFavorites,
                                RedPacketRuleConfig.REPLY_FAVORITE,
                                onValueChange = { joinFavorites = it },
                                onFavoritePicker = { favoritePicker = it }
                            )
                            InsetDivider()
                            GroupMemberMediaRow(
                                context,
                                "退群收藏",
                                leftFavorites,
                                RedPacketRuleConfig.REPLY_FAVORITE,
                                onValueChange = { leftFavorites = it },
                                onFavoritePicker = { favoritePicker = it }
                            )
                        }
                    }
                }
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "延迟") }
                item {
                    SettingsCard {
                        PopupChoiceRow(
                            title = "精细延迟",
                            summary = groupModeLabel(delayMode),
                            options = groupModeChoices(),
                            currentValue = delayMode,
                            onValueChanged = { delayMode = it }
                        )
                        if (delayMode == GroupLeaveMonitorSettings.MODE_CUSTOM) {
                            InsetDivider()
                            NumberInputRow("提示延迟", "单位 ms", promptDelay) { promptDelay = it }
                            InsetDivider()
                            NumberInputRow("图片延迟", "单位 ms", imageDelay) { imageDelay = it }
                            InsetDivider()
                            NumberInputRow("语音延迟", "单位 ms", voiceDelay) { voiceDelay = it }
                            InsetDivider()
                            NumberInputRow("表情延迟", "单位 ms", emojiDelay) { emojiDelay = it }
                            InsetDivider()
                            NumberInputRow("视频延迟", "单位 ms", videoDelay) { videoDelay = it }
                            InsetDivider()
                            NumberInputRow("文件延迟", "单位 ms", fileDelay) { fileDelay = it }
                            InsetDivider()
                            NumberInputRow("收藏延迟", "单位 ms", favoriteDelay) { favoriteDelay = it }
                        }
                    }
                }
                if (request.canDelete) {
                    item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "操作") }
                    item {
                        SettingsCard {
                            ActionRow("删除模板", "删除后绑定关系也会移除") {
                                onDelete()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun GroupMemberGroupEditorPage(
    context: Context,
    sp: SharedPreferences,
    groupId: String,
    groupLabel: String,
    templates: List<GroupLeaveReplyTemplate>,
    templateBindings: List<GroupLeaveReplyTemplateBinding>,
    listState: LazyListState,
    onTemplateBindingsChanged: (List<GroupLeaveReplyTemplateBinding>) -> Unit,
    onDelete: () -> Unit,
    onBack: () -> Unit
) {
    val title = groupLabel.ifBlank { groupId }
    val boundBinding = templateBindings.firstOrNull { it.groupId == groupId }
    val boundTemplate = templates.firstOrNull { it.id == boundBinding?.templateId }
    val templateOptions = listOf(PopupChoice("不使用模板", "")) + templates.map { template ->
        PopupChoice(template.name.ifBlank { template.id }, template.id)
    }
    var favoritePicker by remember { mutableStateOf<FavoritePickerRequest?>(null) }
    var joinEnabled by rememberSaveable(groupId) {
        mutableStateOf(!parseIds(sp.getString(GroupLeaveMonitorSettings.KEY_JOIN_DISABLED_GROUPS, "").orEmpty()).contains(groupId))
    }
    var leftEnabled by rememberSaveable(groupId) {
        mutableStateOf(!parseIds(sp.getString(GroupLeaveMonitorSettings.KEY_LEFT_DISABLED_GROUPS, "").orEmpty()).contains(groupId))
    }
    val globalJoinText = groupMemberGlobalString(sp, GroupLeaveMonitorSettings.KEY_JOIN_TEXT, GroupLeaveMonitorSettings.DEFAULT_JOIN_TEXT)
    val globalLeftText = groupMemberGlobalString(sp, GroupLeaveMonitorSettings.KEY_LEFT_TEXT, GroupLeaveMonitorSettings.DEFAULT_LEFT_TEXT)
    val globalJoinCardTitle = groupMemberGlobalString(sp, GroupLeaveMonitorSettings.KEY_JOIN_CARD_TITLE, GroupLeaveMonitorSettings.DEFAULT_JOIN_CARD_TITLE)
    val globalJoinCardDesc = groupMemberGlobalString(sp, GroupLeaveMonitorSettings.KEY_JOIN_CARD_DESC, GroupLeaveMonitorSettings.DEFAULT_JOIN_CARD_DESC)
    val globalLeftCardTitle = groupMemberGlobalString(sp, GroupLeaveMonitorSettings.KEY_LEFT_CARD_TITLE, GroupLeaveMonitorSettings.DEFAULT_LEFT_CARD_TITLE)
    val globalLeftCardDesc = groupMemberGlobalString(sp, GroupLeaveMonitorSettings.KEY_LEFT_CARD_DESC, GroupLeaveMonitorSettings.DEFAULT_LEFT_CARD_DESC)
    var promptType by rememberSaveable(groupId) {
        mutableStateOf(sp.getString(groupMemberGroupKey(GroupLeaveMonitorSettings.KEY_PROMPT_TYPE, groupId), GroupLeaveMonitorSettings.MODE_GLOBAL) ?: GroupLeaveMonitorSettings.MODE_GLOBAL)
    }
    var bothOrder by rememberSaveable(groupId) {
        mutableStateOf(sp.getString(groupMemberGroupKey(GroupLeaveMonitorSettings.KEY_BOTH_ORDER, groupId), groupBothOrderGlobal(sp)) ?: groupBothOrderGlobal(sp))
    }
    var joinText by rememberSaveable(groupId) {
        mutableStateOf(groupMemberString(sp, GroupLeaveMonitorSettings.KEY_JOIN_TEXT, groupId, globalJoinText))
    }
    var leftText by rememberSaveable(groupId) {
        mutableStateOf(groupMemberString(sp, GroupLeaveMonitorSettings.KEY_LEFT_TEXT, groupId, globalLeftText))
    }
    var joinCardTitle by rememberSaveable(groupId) {
        mutableStateOf(groupMemberString(sp, GroupLeaveMonitorSettings.KEY_JOIN_CARD_TITLE, groupId, globalJoinCardTitle))
    }
    var joinCardDesc by rememberSaveable(groupId) {
        mutableStateOf(groupMemberString(sp, GroupLeaveMonitorSettings.KEY_JOIN_CARD_DESC, groupId, globalJoinCardDesc))
    }
    var leftCardTitle by rememberSaveable(groupId) {
        mutableStateOf(groupMemberString(sp, GroupLeaveMonitorSettings.KEY_LEFT_CARD_TITLE, groupId, globalLeftCardTitle))
    }
    var leftCardDesc by rememberSaveable(groupId) {
        mutableStateOf(groupMemberString(sp, GroupLeaveMonitorSettings.KEY_LEFT_CARD_DESC, groupId, globalLeftCardDesc))
    }
    var mediaMode by rememberSaveable(groupId) {
        mutableStateOf(sp.getString(groupMemberMediaModeKey(groupId), GroupLeaveMonitorSettings.MODE_GLOBAL) ?: GroupLeaveMonitorSettings.MODE_GLOBAL)
    }
    var mediaOrder by rememberSaveable(groupId) {
        mutableStateOf(groupMemberString(sp, GroupLeaveMonitorSettings.KEY_MEDIA_ORDER, groupId, sp.getString(GroupLeaveMonitorSettings.KEY_MEDIA_ORDER, GroupLeaveMonitorSettings.DEFAULT_MEDIA_ORDER) ?: GroupLeaveMonitorSettings.DEFAULT_MEDIA_ORDER))
    }
    var mediaSequence by rememberSaveable(groupId) {
        mutableStateOf(groupMemberString(sp, GroupLeaveMonitorSettings.KEY_MEDIA_SEQUENCE, groupId, sp.getString(GroupLeaveMonitorSettings.KEY_MEDIA_SEQUENCE, GroupLeaveMonitorSettings.DEFAULT_MEDIA_SEQUENCE) ?: GroupLeaveMonitorSettings.DEFAULT_MEDIA_SEQUENCE))
    }
    var joinImages by rememberSaveable(groupId) { mutableStateOf(sp.getString(groupMemberGroupKey(GroupLeaveMonitorSettings.KEY_JOIN_IMAGE_PATHS, groupId), "").orEmpty()) }
    var leftImages by rememberSaveable(groupId) { mutableStateOf(sp.getString(groupMemberGroupKey(GroupLeaveMonitorSettings.KEY_LEFT_IMAGE_PATHS, groupId), "").orEmpty()) }
    var joinVoices by rememberSaveable(groupId) { mutableStateOf(sp.getString(groupMemberGroupKey(GroupLeaveMonitorSettings.KEY_JOIN_VOICE_PATHS, groupId), "").orEmpty()) }
    var leftVoices by rememberSaveable(groupId) { mutableStateOf(sp.getString(groupMemberGroupKey(GroupLeaveMonitorSettings.KEY_LEFT_VOICE_PATHS, groupId), "").orEmpty()) }
    var joinEmojis by rememberSaveable(groupId) { mutableStateOf(sp.getString(groupMemberGroupKey(GroupLeaveMonitorSettings.KEY_JOIN_EMOJI_PATHS, groupId), "").orEmpty()) }
    var leftEmojis by rememberSaveable(groupId) { mutableStateOf(sp.getString(groupMemberGroupKey(GroupLeaveMonitorSettings.KEY_LEFT_EMOJI_PATHS, groupId), "").orEmpty()) }
    var joinVideos by rememberSaveable(groupId) { mutableStateOf(sp.getString(groupMemberGroupKey(GroupLeaveMonitorSettings.KEY_JOIN_VIDEO_PATHS, groupId), "").orEmpty()) }
    var leftVideos by rememberSaveable(groupId) { mutableStateOf(sp.getString(groupMemberGroupKey(GroupLeaveMonitorSettings.KEY_LEFT_VIDEO_PATHS, groupId), "").orEmpty()) }
    var joinFiles by rememberSaveable(groupId) { mutableStateOf(sp.getString(groupMemberGroupKey(GroupLeaveMonitorSettings.KEY_JOIN_FILE_PATHS, groupId), "").orEmpty()) }
    var leftFiles by rememberSaveable(groupId) { mutableStateOf(sp.getString(groupMemberGroupKey(GroupLeaveMonitorSettings.KEY_LEFT_FILE_PATHS, groupId), "").orEmpty()) }
    var joinFavorites by rememberSaveable(groupId) { mutableStateOf(sp.getString(groupMemberGroupKey(GroupLeaveMonitorSettings.KEY_JOIN_FAVORITE_PATHS, groupId), "").orEmpty()) }
    var leftFavorites by rememberSaveable(groupId) { mutableStateOf(sp.getString(groupMemberGroupKey(GroupLeaveMonitorSettings.KEY_LEFT_FAVORITE_PATHS, groupId), "").orEmpty()) }
    var delayMode by rememberSaveable(groupId) {
        mutableStateOf(sp.getString(groupMemberDelayModeKey(groupId), GroupLeaveMonitorSettings.MODE_GLOBAL) ?: GroupLeaveMonitorSettings.MODE_GLOBAL)
    }
    var promptDelay by rememberSaveable(groupId) { mutableStateOf(groupMemberIntString(sp, GroupLeaveMonitorSettings.KEY_PROMPT_DELAY_MS, groupId, GroupLeaveMonitorSettings.DEFAULT_PROMPT_DELAY_MS)) }
    var imageDelay by rememberSaveable(groupId) { mutableStateOf(groupMemberIntString(sp, GroupLeaveMonitorSettings.KEY_IMAGE_DELAY_MS, groupId, GroupLeaveMonitorSettings.DEFAULT_MEDIA_DELAY_MS)) }
    var voiceDelay by rememberSaveable(groupId) { mutableStateOf(groupMemberIntString(sp, GroupLeaveMonitorSettings.KEY_VOICE_DELAY_MS, groupId, GroupLeaveMonitorSettings.DEFAULT_MEDIA_DELAY_MS)) }
    var emojiDelay by rememberSaveable(groupId) { mutableStateOf(groupMemberIntString(sp, GroupLeaveMonitorSettings.KEY_EMOJI_DELAY_MS, groupId, GroupLeaveMonitorSettings.DEFAULT_MEDIA_DELAY_MS)) }
    var videoDelay by rememberSaveable(groupId) { mutableStateOf(groupMemberIntString(sp, GroupLeaveMonitorSettings.KEY_VIDEO_DELAY_MS, groupId, GroupLeaveMonitorSettings.DEFAULT_MEDIA_DELAY_MS)) }
    var fileDelay by rememberSaveable(groupId) { mutableStateOf(groupMemberIntString(sp, GroupLeaveMonitorSettings.KEY_FILE_DELAY_MS, groupId, GroupLeaveMonitorSettings.DEFAULT_MEDIA_DELAY_MS)) }
    var favoriteDelay by rememberSaveable(groupId) { mutableStateOf(groupMemberIntString(sp, GroupLeaveMonitorSettings.KEY_FAVORITE_DELAY_MS, groupId, GroupLeaveMonitorSettings.DEFAULT_MEDIA_DELAY_MS)) }
    val scrollBehavior = MiuixScrollBehavior()

    SettingsRouteTransition(
        targetState = favoritePicker,
        label = "GroupLeaveFavoriteRoute",
        depthOf = { if (it == null) 0 else 1 }
    ) { currentFavoritePicker ->
        if (currentFavoritePicker != null) {
            FavoritePickerPage(
                request = currentFavoritePicker,
                onBack = { favoritePicker = null }
            )
        } else PageScaffold(
            title = title,
            largeTitle = "群专属设置",
            scrollBehavior = scrollBehavior,
            bottomBar = {
                if (boundTemplate != null) {
                    BottomActionBar("返回", onBack)
                } else {
                    BottomActionBar(
                        primaryText = "保存设置",
                        onPrimaryClick = {
                            sp.edit()
                                .putString(GroupLeaveMonitorSettings.KEY_JOIN_DISABLED_GROUPS, formatIds(updateGroupToggle(sp, GroupLeaveMonitorSettings.KEY_JOIN_DISABLED_GROUPS, groupId, joinEnabled)))
                                .putString(GroupLeaveMonitorSettings.KEY_LEFT_DISABLED_GROUPS, formatIds(updateGroupToggle(sp, GroupLeaveMonitorSettings.KEY_LEFT_DISABLED_GROUPS, groupId, leftEnabled)))
                                .putString(groupMemberGroupKey(GroupLeaveMonitorSettings.KEY_PROMPT_TYPE, groupId), promptType)
                                .putString(groupMemberGroupKey(GroupLeaveMonitorSettings.KEY_BOTH_ORDER, groupId), bothOrder)
                                .putString(groupMemberGroupKey(GroupLeaveMonitorSettings.KEY_JOIN_TEXT, groupId), joinText)
                                .putString(groupMemberGroupKey(GroupLeaveMonitorSettings.KEY_LEFT_TEXT, groupId), leftText)
                                .putString(groupMemberGroupKey(GroupLeaveMonitorSettings.KEY_JOIN_CARD_TITLE, groupId), joinCardTitle)
                                .putString(groupMemberGroupKey(GroupLeaveMonitorSettings.KEY_JOIN_CARD_DESC, groupId), joinCardDesc)
                                .putString(groupMemberGroupKey(GroupLeaveMonitorSettings.KEY_LEFT_CARD_TITLE, groupId), leftCardTitle)
                                .putString(groupMemberGroupKey(GroupLeaveMonitorSettings.KEY_LEFT_CARD_DESC, groupId), leftCardDesc)
                                .putString(groupMemberMediaModeKey(groupId), mediaMode)
                                .putString(groupMemberGroupKey(GroupLeaveMonitorSettings.KEY_MEDIA_ORDER, groupId), mediaOrder)
                                .putString(groupMemberGroupKey(GroupLeaveMonitorSettings.KEY_MEDIA_SEQUENCE, groupId), mediaSequence)
                                .putString(groupMemberGroupKey(GroupLeaveMonitorSettings.KEY_JOIN_IMAGE_PATHS, groupId), joinImages)
                                .putString(groupMemberGroupKey(GroupLeaveMonitorSettings.KEY_LEFT_IMAGE_PATHS, groupId), leftImages)
                                .putString(groupMemberGroupKey(GroupLeaveMonitorSettings.KEY_JOIN_VOICE_PATHS, groupId), joinVoices)
                                .putString(groupMemberGroupKey(GroupLeaveMonitorSettings.KEY_LEFT_VOICE_PATHS, groupId), leftVoices)
                                .putString(groupMemberGroupKey(GroupLeaveMonitorSettings.KEY_JOIN_EMOJI_PATHS, groupId), joinEmojis)
                                .putString(groupMemberGroupKey(GroupLeaveMonitorSettings.KEY_LEFT_EMOJI_PATHS, groupId), leftEmojis)
                                .putString(groupMemberGroupKey(GroupLeaveMonitorSettings.KEY_JOIN_VIDEO_PATHS, groupId), joinVideos)
                                .putString(groupMemberGroupKey(GroupLeaveMonitorSettings.KEY_LEFT_VIDEO_PATHS, groupId), leftVideos)
                                .putString(groupMemberGroupKey(GroupLeaveMonitorSettings.KEY_JOIN_FILE_PATHS, groupId), joinFiles)
                                .putString(groupMemberGroupKey(GroupLeaveMonitorSettings.KEY_LEFT_FILE_PATHS, groupId), leftFiles)
                                .putString(groupMemberGroupKey(GroupLeaveMonitorSettings.KEY_JOIN_FAVORITE_PATHS, groupId), joinFavorites)
                                .putString(groupMemberGroupKey(GroupLeaveMonitorSettings.KEY_LEFT_FAVORITE_PATHS, groupId), leftFavorites)
                                .putString(groupMemberDelayModeKey(groupId), delayMode)
                                .putInt(groupMemberGroupKey(GroupLeaveMonitorSettings.KEY_PROMPT_DELAY_MS, groupId), promptDelay.toIntOrNull()?.coerceAtLeast(0) ?: GroupLeaveMonitorSettings.DEFAULT_PROMPT_DELAY_MS)
                                .putInt(groupMemberGroupKey(GroupLeaveMonitorSettings.KEY_IMAGE_DELAY_MS, groupId), imageDelay.toIntOrNull()?.coerceAtLeast(0) ?: GroupLeaveMonitorSettings.DEFAULT_MEDIA_DELAY_MS)
                                .putInt(groupMemberGroupKey(GroupLeaveMonitorSettings.KEY_VOICE_DELAY_MS, groupId), voiceDelay.toIntOrNull()?.coerceAtLeast(0) ?: GroupLeaveMonitorSettings.DEFAULT_MEDIA_DELAY_MS)
                                .putInt(groupMemberGroupKey(GroupLeaveMonitorSettings.KEY_EMOJI_DELAY_MS, groupId), emojiDelay.toIntOrNull()?.coerceAtLeast(0) ?: GroupLeaveMonitorSettings.DEFAULT_MEDIA_DELAY_MS)
                                .putInt(groupMemberGroupKey(GroupLeaveMonitorSettings.KEY_VIDEO_DELAY_MS, groupId), videoDelay.toIntOrNull()?.coerceAtLeast(0) ?: GroupLeaveMonitorSettings.DEFAULT_MEDIA_DELAY_MS)
                                .putInt(groupMemberGroupKey(GroupLeaveMonitorSettings.KEY_FILE_DELAY_MS, groupId), fileDelay.toIntOrNull()?.coerceAtLeast(0) ?: GroupLeaveMonitorSettings.DEFAULT_MEDIA_DELAY_MS)
                                .putInt(groupMemberGroupKey(GroupLeaveMonitorSettings.KEY_FAVORITE_DELAY_MS, groupId), favoriteDelay.toIntOrNull()?.coerceAtLeast(0) ?: GroupLeaveMonitorSettings.DEFAULT_MEDIA_DELAY_MS)
                                .apply()
                            Toast.makeText(context, "群专属设置已保存", Toast.LENGTH_SHORT).show()
                        },
                        secondaryText = "返回",
                        onSecondaryClick = onBack
                    )
                }
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
                state = listState,
                contentPadding = PaddingValues(
                    top = padding.calculateTopPadding() + 8.dp,
                    bottom = padding.calculateBottomPadding() + 84.dp
                )
            ) {
                item { SmallTitle(text = "模板") }
                item {
                    SettingsCard {
                        if (templates.isEmpty()) {
                            EmptyText("暂无模板。先返回回复模板管理新增模板。")
                        } else {
                            PopupChoiceRow(
                                title = "选择模板",
                                summary = boundTemplate?.let { describeGroupLeaveTemplate(it) }
                                    ?: "不使用模板，配置本群专属设置",
                                options = templateOptions,
                                currentValue = boundTemplate?.id.orEmpty(),
                                onValueChanged = { templateId ->
                                    val next = if (templateId.isBlank()) {
                                        templateBindings.filterNot { it.groupId == groupId }
                                    } else {
                                        upsertGroupLeaveTemplateBindings(
                                            templateBindings,
                                            listOf(GroupLeaveReplyTemplateBinding(groupId, title, templateId))
                                        )
                                    }
                                    onTemplateBindingsChanged(next)
                                    Toast.makeText(
                                        context,
                                        if (templateId.isBlank()) "已取消模板绑定" else "模板已绑定",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            )
                        }
                    }
                }
                if (boundTemplate != null) {
                    item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "群号") }
                    item {
                        SettingsCard {
                            SelectRow(title = title, summary = groupId, onClick = {})
                        }
                    }
                } else {
                    item { SmallTitle(text = "事件") }
            item {
                SettingsCard {
                    SwitchRow(joinEnabled, "进群回复", "该群检测到成员进群时发送", onCheckedChange = { joinEnabled = it })
                    InsetDivider()
                    SwitchRow(leftEnabled, "退群回复", "该群检测到成员退群时发送", onCheckedChange = { leftEnabled = it })
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "提示模板") }
            item {
                SettingsCard {
                    PopupChoiceRow(
                        title = "提示设置",
                        summary = groupPromptTypeWithGlobalLabel(promptType),
                        options = groupPromptTypeWithGlobalChoices(),
                        currentValue = promptType,
                        onValueChanged = {
                            promptType = it
                            sp.edit().putString(groupMemberGroupKey(GroupLeaveMonitorSettings.KEY_PROMPT_TYPE, groupId), it).apply()
                        }
                    )
                    if (promptType != GroupLeaveMonitorSettings.MODE_GLOBAL) {
                        if (promptType == GroupLeaveMonitorSettings.PROMPT_BOTH) {
                            InsetDivider()
                            PopupChoiceRow(
                                title = "文本+卡片顺序",
                                summary = groupBothOrderLabel(bothOrder),
                                options = groupBothOrderChoices(),
                                currentValue = bothOrder,
                                onValueChanged = {
                                    bothOrder = it
                                    sp.edit().putString(groupMemberGroupKey(GroupLeaveMonitorSettings.KEY_BOTH_ORDER, groupId), it).apply()
                                }
                            )
                        }
                        InsetDivider()
                        VariableInputRow("进群文本", "多个模板用 || 分隔随机选择", joinText, groupMemberTemplateVariables, minLines = 4) { joinText = it }
                        InsetDivider()
                        VariableInputRow("退群文本", "多个模板用 || 分隔随机选择", leftText, groupMemberTemplateVariables, minLines = 4) { leftText = it }
                        InsetDivider()
                        ActionRow("随机填充文本", "随机生成本群进群和退群文本") {
                            joinText = groupRandomJoinTexts[Random.nextInt(groupRandomJoinTexts.size)]
                            leftText = groupRandomLeftTexts[Random.nextInt(groupRandomLeftTexts.size)]
                            Toast.makeText(context, "已随机填充本群文本", Toast.LENGTH_SHORT).show()
                        }
                        InsetDivider()
                        ActionRow("恢复全局文本", "用当前全局文本覆盖本群文本") {
                            joinText = globalJoinText
                            leftText = globalLeftText
                            Toast.makeText(context, "已恢复为全局文本", Toast.LENGTH_SHORT).show()
                        }
                        InsetDivider()
                        VariableInputRow("进群卡片标题", "支持变量", joinCardTitle, groupMemberTemplateVariables) { joinCardTitle = it }
                        InsetDivider()
                        VariableInputRow("进群卡片描述", "支持变量", joinCardDesc, groupMemberTemplateVariables, minLines = 3) { joinCardDesc = it }
                        InsetDivider()
                        VariableInputRow("退群卡片标题", "支持变量", leftCardTitle, groupMemberTemplateVariables) { leftCardTitle = it }
                        InsetDivider()
                        VariableInputRow("退群卡片描述", "支持变量", leftCardDesc, groupMemberTemplateVariables, minLines = 3) { leftCardDesc = it }
                        InsetDivider()
                        ActionRow("随机填充卡片", "随机生成本群进群和退群卡片") {
                            joinCardTitle = groupRandomJoinCardTitles[Random.nextInt(groupRandomJoinCardTitles.size)]
                            joinCardDesc = groupRandomJoinCardDescs[Random.nextInt(groupRandomJoinCardDescs.size)]
                            leftCardTitle = groupRandomLeftCardTitles[Random.nextInt(groupRandomLeftCardTitles.size)]
                            leftCardDesc = groupRandomLeftCardDescs[Random.nextInt(groupRandomLeftCardDescs.size)]
                            Toast.makeText(context, "已随机填充本群卡片", Toast.LENGTH_SHORT).show()
                        }
                        InsetDivider()
                        ActionRow("恢复全局卡片", "用当前全局卡片覆盖本群卡片") {
                            joinCardTitle = globalJoinCardTitle
                            joinCardDesc = globalJoinCardDesc
                            leftCardTitle = globalLeftCardTitle
                            leftCardDesc = globalLeftCardDesc
                            Toast.makeText(context, "已恢复为全局卡片", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "媒体") }
            item {
                SettingsCard {
                    PopupChoiceRow(
                        title = "媒体设置",
                        summary = groupMediaModeLabel(mediaMode),
                        options = groupMediaModeChoices(),
                        currentValue = mediaMode,
                        onValueChanged = {
                            mediaMode = it
                            sp.edit().putString(groupMemberMediaModeKey(groupId), it).apply()
                        }
                    )
                    if (mediaMode == GroupLeaveMonitorSettings.MODE_CUSTOM) {
                        InsetDivider()
                        PopupChoiceRow(
                            title = "媒体顺序",
                            summary = groupMediaOrderLabel(mediaOrder),
                            options = groupMediaOrderChoices(),
                            currentValue = mediaOrder,
                            onValueChanged = {
                                mediaOrder = it
                                sp.edit().putString(groupMemberGroupKey(GroupLeaveMonitorSettings.KEY_MEDIA_ORDER, groupId), it).apply()
                            }
                        )
                        InsetDivider()
                        InputRow("媒体类型顺序", "英文逗号分隔：image,voice,emoji,video,file,favorite", mediaSequence) { mediaSequence = it }
                        InsetDivider()
                        GroupMemberMediaRow(context, "进群图片", joinImages, RedPacketRuleConfig.REPLY_IMAGE) { joinImages = it }
                        InsetDivider()
                        GroupMemberMediaRow(context, "退群图片", leftImages, RedPacketRuleConfig.REPLY_IMAGE) { leftImages = it }
                        InsetDivider()
                        GroupMemberMediaRow(context, "进群语音", joinVoices, RedPacketRuleConfig.REPLY_VOICE) { joinVoices = it }
                        InsetDivider()
                        GroupMemberMediaRow(context, "退群语音", leftVoices, RedPacketRuleConfig.REPLY_VOICE) { leftVoices = it }
                        InsetDivider()
                        GroupMemberMediaRow(context, "进群表情", joinEmojis, RedPacketRuleConfig.REPLY_EMOJI) { joinEmojis = it }
                        InsetDivider()
                        GroupMemberMediaRow(context, "退群表情", leftEmojis, RedPacketRuleConfig.REPLY_EMOJI) { leftEmojis = it }
                        InsetDivider()
                        GroupMemberMediaRow(context, "进群视频", joinVideos, RedPacketRuleConfig.REPLY_VIDEO) { joinVideos = it }
                        InsetDivider()
                        GroupMemberMediaRow(context, "退群视频", leftVideos, RedPacketRuleConfig.REPLY_VIDEO) { leftVideos = it }
                        InsetDivider()
                        GroupMemberMediaRow(context, "进群文件", joinFiles, RedPacketRuleConfig.REPLY_FILE) { joinFiles = it }
                        InsetDivider()
                        GroupMemberMediaRow(context, "退群文件", leftFiles, RedPacketRuleConfig.REPLY_FILE) { leftFiles = it }
                        InsetDivider()
                        GroupMemberMediaRow(
                            context,
                            "进群收藏",
                            joinFavorites,
                            RedPacketRuleConfig.REPLY_FAVORITE,
                            onValueChange = { joinFavorites = it },
                            onFavoritePicker = { favoritePicker = it }
                        )
                        InsetDivider()
                        GroupMemberMediaRow(
                            context,
                            "退群收藏",
                            leftFavorites,
                            RedPacketRuleConfig.REPLY_FAVORITE,
                            onValueChange = { leftFavorites = it },
                            onFavoritePicker = { favoritePicker = it }
                        )
                    }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "延迟") }
            item {
                SettingsCard {
                    PopupChoiceRow(
                        title = "精细延迟",
                        summary = groupModeLabel(delayMode),
                        options = groupModeChoices(),
                        currentValue = delayMode,
                        onValueChanged = {
                            delayMode = it
                            sp.edit().putString(groupMemberDelayModeKey(groupId), it).apply()
                        }
                    )
                    if (delayMode == GroupLeaveMonitorSettings.MODE_CUSTOM) {
                        InsetDivider()
                        NumberInputRow("提示延迟", "单位 ms", promptDelay) { promptDelay = it }
                        InsetDivider()
                        NumberInputRow("图片延迟", "单位 ms", imageDelay) { imageDelay = it }
                        InsetDivider()
                        NumberInputRow("语音延迟", "单位 ms", voiceDelay) { voiceDelay = it }
                        InsetDivider()
                        NumberInputRow("表情延迟", "单位 ms", emojiDelay) { emojiDelay = it }
                        InsetDivider()
                        NumberInputRow("视频延迟", "单位 ms", videoDelay) { videoDelay = it }
                        InsetDivider()
                        NumberInputRow("文件延迟", "单位 ms", fileDelay) { fileDelay = it }
                        InsetDivider()
                        NumberInputRow("收藏延迟", "单位 ms", favoriteDelay) { favoriteDelay = it }
                    }
                }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "操作") }
            item {
                SettingsCard {
                    ActionRow("删除当前群", "移除监听、模板绑定和本群专属设置") {
                        onDelete()
                    }
                }
            }
        }
    }
}
}

internal fun deleteGroupMemberConfiguration(
    sp: SharedPreferences,
    groupId: String,
    nextListenGroups: String,
    nextBindings: List<GroupLeaveReplyTemplateBinding>
) {
    val groupKeys = listOf(
        GroupLeaveMonitorSettings.KEY_PROMPT_TYPE,
        GroupLeaveMonitorSettings.KEY_BOTH_ORDER,
        GroupLeaveMonitorSettings.KEY_JOIN_TEXT,
        GroupLeaveMonitorSettings.KEY_LEFT_TEXT,
        GroupLeaveMonitorSettings.KEY_JOIN_CARD_TITLE,
        GroupLeaveMonitorSettings.KEY_JOIN_CARD_DESC,
        GroupLeaveMonitorSettings.KEY_LEFT_CARD_TITLE,
        GroupLeaveMonitorSettings.KEY_LEFT_CARD_DESC,
        GroupLeaveMonitorSettings.KEY_MEDIA_ORDER,
        GroupLeaveMonitorSettings.KEY_MEDIA_SEQUENCE,
        GroupLeaveMonitorSettings.KEY_JOIN_IMAGE_PATHS,
        GroupLeaveMonitorSettings.KEY_LEFT_IMAGE_PATHS,
        GroupLeaveMonitorSettings.KEY_JOIN_VOICE_PATHS,
        GroupLeaveMonitorSettings.KEY_LEFT_VOICE_PATHS,
        GroupLeaveMonitorSettings.KEY_JOIN_EMOJI_PATHS,
        GroupLeaveMonitorSettings.KEY_LEFT_EMOJI_PATHS,
        GroupLeaveMonitorSettings.KEY_JOIN_VIDEO_PATHS,
        GroupLeaveMonitorSettings.KEY_LEFT_VIDEO_PATHS,
        GroupLeaveMonitorSettings.KEY_JOIN_FILE_PATHS,
        GroupLeaveMonitorSettings.KEY_LEFT_FILE_PATHS,
        GroupLeaveMonitorSettings.KEY_JOIN_FAVORITE_PATHS,
        GroupLeaveMonitorSettings.KEY_LEFT_FAVORITE_PATHS,
        GroupLeaveMonitorSettings.KEY_PROMPT_DELAY_MS,
        GroupLeaveMonitorSettings.KEY_IMAGE_DELAY_MS,
        GroupLeaveMonitorSettings.KEY_VOICE_DELAY_MS,
        GroupLeaveMonitorSettings.KEY_EMOJI_DELAY_MS,
        GroupLeaveMonitorSettings.KEY_VIDEO_DELAY_MS,
        GroupLeaveMonitorSettings.KEY_FILE_DELAY_MS,
        GroupLeaveMonitorSettings.KEY_FAVORITE_DELAY_MS
    )
    val joinDisabled = formatIds(
        parseIds(sp.getString(GroupLeaveMonitorSettings.KEY_JOIN_DISABLED_GROUPS, "").orEmpty()) - groupId
    )
    val leftDisabled = formatIds(
        parseIds(sp.getString(GroupLeaveMonitorSettings.KEY_LEFT_DISABLED_GROUPS, "").orEmpty()) - groupId
    )
    val editor = sp.edit()
        .putString(GroupLeaveMonitorSettings.KEY_LISTEN_GROUPS, nextListenGroups)
        .putString(GroupLeaveMonitorSettings.KEY_JOIN_DISABLED_GROUPS, joinDisabled)
        .putString(GroupLeaveMonitorSettings.KEY_LEFT_DISABLED_GROUPS, leftDisabled)
        .putString(
            GroupLeaveMonitorSettings.KEY_REPLY_TEMPLATE_BINDINGS,
            GroupLeaveMonitorSettings.encodeBindings(nextBindings)
        )
        .remove(groupMemberMediaModeKey(groupId))
        .remove(groupMemberDelayModeKey(groupId))
    groupKeys.forEach { key -> editor.remove(groupMemberGroupKey(key, groupId)) }
    sp.all.keys
        .filter { it.startsWith(GroupLeaveMonitorSettings.KEY_INVITE_COUNT_PREFIX + groupId + "|") }
        .forEach { key -> editor.remove(key) }
    editor.apply()
}

@Composable
internal fun GroupMemberMediaRow(
    context: Context,
    title: String,
    value: String,
    replyMode: Int,
    onFavoritePicker: ((FavoritePickerRequest) -> Unit)? = null,
    onValueChange: (String) -> Unit
) {
    ActionRow(title, redPacketReplyContentSummary(replyMode, value)) {
        if (replyMode == RedPacketRuleConfig.REPLY_FAVORITE) {
            onFavoritePicker?.invoke(
                FavoritePickerRequest(
                    title = title,
                    existingValue = value,
                    onValue = onValueChange,
                    multiSelect = true,
                    delimiter = "|"
                )
            )
            return@ActionRow
        }
        val activity = context as? Activity
        if (activity == null) {
            Toast.makeText(context, "当前页面无法打开文件选择器", Toast.LENGTH_SHORT).show()
            return@ActionRow
        }
        RedPacketReplyFilePickerBridge.launch(activity, replyMode) { paths ->
            if (paths.isNotEmpty()) {
                onValueChange(paths.joinToString("|"))
                Toast.makeText(context, "已选择 ${paths.size} 个文件", Toast.LENGTH_SHORT).show()
            }
        }
    }
    if (value.isNotBlank()) {
        InsetDivider()
        ActionRow(
            "清空$title",
            if (replyMode == RedPacketRuleConfig.REPLY_FAVORITE) "移除已选择收藏" else "移除已选择文件"
        ) {
            onValueChange("")
        }
    }
}

internal fun groupSelectionSummary(value: String): String {
    val count = parseIds(value).size
    return if (count == 0) "未选择监听群" else "已选择 $count 个群"
}

internal fun systemNoticeScopeChoices(allValue: String, specificValue: String): List<PopupChoice<String>> = listOf(
    PopupChoice("全部群聊", allValue),
    PopupChoice("指定群聊", specificValue)
)

internal fun systemNoticeScopeLabel(value: String, specificValue: String): String {
    return if (value == specificValue) "指定群聊" else "全部群聊"
}

internal fun systemNoticeGroupSummary(value: String): String {
    val count = parseIds(value).size
    return if (count == 0) "未选择群聊" else "已选择 $count 个群"
}

internal fun describeGroupLeaveTemplate(template: GroupLeaveReplyTemplate): String {
    val join = if (template.joinEnabled) "进群" else ""
    val left = if (template.leftEnabled) "退群" else ""
    val scope = listOf(join, left).filter { it.isNotBlank() }.joinToString("、").ifBlank { "未启用事件" }
    val media = when (template.mediaMode) {
        GroupLeaveMonitorSettings.MEDIA_NONE -> "不发媒体"
        GroupLeaveMonitorSettings.MODE_CUSTOM -> "自定义媒体"
        else -> "跟随全局媒体"
    }
    val delay = when (template.delayMode) {
        GroupLeaveMonitorSettings.MODE_CUSTOM -> "自定义延迟"
        else -> "跟随全局延迟"
    }
    return "$scope · ${groupPromptTypeWithGlobalLabel(template.promptType)} · $media · $delay"
}

internal fun groupLeaveTemplateBindingSummary(
    bindings: List<GroupLeaveReplyTemplateBinding>,
    templates: List<GroupLeaveReplyTemplate>
): String {
    if (templates.isEmpty()) return "暂无模板"
    val count = bindings.size
    return if (count == 0) "暂无群已绑定模板" else "已绑定 $count 个群"
}

internal fun groupLeaveGroupBindingSummary(
    groupId: String,
    defaultLabel: String,
    bindings: List<GroupLeaveReplyTemplateBinding>,
    templates: List<GroupLeaveReplyTemplate>
): String {
    val binding = bindings.firstOrNull { it.groupId == groupId } ?: return "未绑定模板"
    val template = templates.firstOrNull { it.id == binding.templateId } ?: return "模板已不存在"
    return "模板：${template.name.ifBlank { binding.label.ifBlank { defaultLabel } }}"
}

internal fun newGroupLeaveTemplate(index: Int, sp: SharedPreferences): GroupLeaveReplyTemplate {
    return GroupLeaveReplyTemplate(
        id = System.currentTimeMillis().toString() + "_" + index,
        name = "模板 $index",
        enabled = true,
        joinEnabled = sp.getBoolean(GroupLeaveMonitorSettings.KEY_JOIN_REPLY_ENABLE, GroupLeaveMonitorSettings.DEFAULT_JOIN_REPLY_ENABLE),
        leftEnabled = sp.getBoolean(GroupLeaveMonitorSettings.KEY_LEFT_REPLY_ENABLE, GroupLeaveMonitorSettings.DEFAULT_LEFT_REPLY_ENABLE),
        promptType = sp.getString(GroupLeaveMonitorSettings.KEY_PROMPT_TYPE, GroupLeaveMonitorSettings.DEFAULT_PROMPT_TYPE) ?: GroupLeaveMonitorSettings.DEFAULT_PROMPT_TYPE,
        bothOrder = sp.getString(GroupLeaveMonitorSettings.KEY_BOTH_ORDER, GroupLeaveMonitorSettings.DEFAULT_BOTH_ORDER) ?: GroupLeaveMonitorSettings.DEFAULT_BOTH_ORDER,
        joinText = sp.getString(GroupLeaveMonitorSettings.KEY_JOIN_TEXT, GroupLeaveMonitorSettings.DEFAULT_JOIN_TEXT) ?: GroupLeaveMonitorSettings.DEFAULT_JOIN_TEXT,
        leftText = sp.getString(GroupLeaveMonitorSettings.KEY_LEFT_TEXT, GroupLeaveMonitorSettings.DEFAULT_LEFT_TEXT) ?: GroupLeaveMonitorSettings.DEFAULT_LEFT_TEXT,
        joinCardTitle = sp.getString(GroupLeaveMonitorSettings.KEY_JOIN_CARD_TITLE, GroupLeaveMonitorSettings.DEFAULT_JOIN_CARD_TITLE) ?: GroupLeaveMonitorSettings.DEFAULT_JOIN_CARD_TITLE,
        joinCardDesc = sp.getString(GroupLeaveMonitorSettings.KEY_JOIN_CARD_DESC, GroupLeaveMonitorSettings.DEFAULT_JOIN_CARD_DESC) ?: GroupLeaveMonitorSettings.DEFAULT_JOIN_CARD_DESC,
        leftCardTitle = sp.getString(GroupLeaveMonitorSettings.KEY_LEFT_CARD_TITLE, GroupLeaveMonitorSettings.DEFAULT_LEFT_CARD_TITLE) ?: GroupLeaveMonitorSettings.DEFAULT_LEFT_CARD_TITLE,
        leftCardDesc = sp.getString(GroupLeaveMonitorSettings.KEY_LEFT_CARD_DESC, GroupLeaveMonitorSettings.DEFAULT_LEFT_CARD_DESC) ?: GroupLeaveMonitorSettings.DEFAULT_LEFT_CARD_DESC,
        mediaMode = GroupLeaveMonitorSettings.MODE_CUSTOM,
        mediaOrder = sp.getString(GroupLeaveMonitorSettings.KEY_MEDIA_ORDER, GroupLeaveMonitorSettings.DEFAULT_MEDIA_ORDER) ?: GroupLeaveMonitorSettings.DEFAULT_MEDIA_ORDER,
        mediaSequence = sp.getString(GroupLeaveMonitorSettings.KEY_MEDIA_SEQUENCE, GroupLeaveMonitorSettings.DEFAULT_MEDIA_SEQUENCE) ?: GroupLeaveMonitorSettings.DEFAULT_MEDIA_SEQUENCE,
        joinImages = sp.getString(GroupLeaveMonitorSettings.KEY_JOIN_IMAGE_PATHS, "").orEmpty(),
        leftImages = sp.getString(GroupLeaveMonitorSettings.KEY_LEFT_IMAGE_PATHS, "").orEmpty(),
        joinVoices = sp.getString(GroupLeaveMonitorSettings.KEY_JOIN_VOICE_PATHS, "").orEmpty(),
        leftVoices = sp.getString(GroupLeaveMonitorSettings.KEY_LEFT_VOICE_PATHS, "").orEmpty(),
        joinEmojis = sp.getString(GroupLeaveMonitorSettings.KEY_JOIN_EMOJI_PATHS, "").orEmpty(),
        leftEmojis = sp.getString(GroupLeaveMonitorSettings.KEY_LEFT_EMOJI_PATHS, "").orEmpty(),
        joinVideos = sp.getString(GroupLeaveMonitorSettings.KEY_JOIN_VIDEO_PATHS, "").orEmpty(),
        leftVideos = sp.getString(GroupLeaveMonitorSettings.KEY_LEFT_VIDEO_PATHS, "").orEmpty(),
        joinFiles = sp.getString(GroupLeaveMonitorSettings.KEY_JOIN_FILE_PATHS, "").orEmpty(),
        leftFiles = sp.getString(GroupLeaveMonitorSettings.KEY_LEFT_FILE_PATHS, "").orEmpty(),
        joinFavorites = sp.getString(GroupLeaveMonitorSettings.KEY_JOIN_FAVORITE_PATHS, "").orEmpty(),
        leftFavorites = sp.getString(GroupLeaveMonitorSettings.KEY_LEFT_FAVORITE_PATHS, "").orEmpty(),
        delayMode = GroupLeaveMonitorSettings.MODE_CUSTOM,
        promptDelayMs = sp.getInt(GroupLeaveMonitorSettings.KEY_PROMPT_DELAY_MS, GroupLeaveMonitorSettings.DEFAULT_PROMPT_DELAY_MS),
        imageDelayMs = sp.getInt(GroupLeaveMonitorSettings.KEY_IMAGE_DELAY_MS, GroupLeaveMonitorSettings.DEFAULT_MEDIA_DELAY_MS),
        voiceDelayMs = sp.getInt(GroupLeaveMonitorSettings.KEY_VOICE_DELAY_MS, GroupLeaveMonitorSettings.DEFAULT_MEDIA_DELAY_MS),
        emojiDelayMs = sp.getInt(GroupLeaveMonitorSettings.KEY_EMOJI_DELAY_MS, GroupLeaveMonitorSettings.DEFAULT_MEDIA_DELAY_MS),
        videoDelayMs = sp.getInt(GroupLeaveMonitorSettings.KEY_VIDEO_DELAY_MS, GroupLeaveMonitorSettings.DEFAULT_MEDIA_DELAY_MS),
        fileDelayMs = sp.getInt(GroupLeaveMonitorSettings.KEY_FILE_DELAY_MS, GroupLeaveMonitorSettings.DEFAULT_MEDIA_DELAY_MS),
        favoriteDelayMs = sp.getInt(GroupLeaveMonitorSettings.KEY_FAVORITE_DELAY_MS, GroupLeaveMonitorSettings.DEFAULT_MEDIA_DELAY_MS)
    )
}

internal fun upsertGroupLeaveTemplateBindings(
    current: List<GroupLeaveReplyTemplateBinding>,
    additions: List<GroupLeaveReplyTemplateBinding>
): List<GroupLeaveReplyTemplateBinding> {
    val result = linkedMapOf<String, GroupLeaveReplyTemplateBinding>()
    current.forEach { binding ->
        if (binding.groupId.isNotBlank() && binding.templateId.isNotBlank()) {
            result[binding.groupId] = binding.copy(
                groupId = binding.groupId.trim(),
                label = binding.label.trim().ifBlank { binding.groupId.trim() },
                templateId = binding.templateId.trim()
            )
        }
    }
    additions.forEach { binding ->
        if (binding.groupId.isNotBlank() && binding.templateId.isNotBlank()) {
            result[binding.groupId.trim()] = binding.copy(
                groupId = binding.groupId.trim(),
                label = binding.label.trim().ifBlank { binding.groupId.trim() },
                templateId = binding.templateId.trim()
            )
        }
    }
    return result.values.toList()
}

internal fun groupPromptTypeLabel(value: String): String {
    return when (value) {
        GroupLeaveMonitorSettings.PROMPT_CARD -> "卡片"
        GroupLeaveMonitorSettings.PROMPT_BOTH -> "文本+卡片"
        else -> "文本"
    }
}

internal fun groupPromptTypeChoices(): List<PopupChoice<String>> {
    return listOf(
        PopupChoice("文本", GroupLeaveMonitorSettings.PROMPT_TEXT),
        PopupChoice("文本+卡片", GroupLeaveMonitorSettings.PROMPT_BOTH),
        PopupChoice("卡片", GroupLeaveMonitorSettings.PROMPT_CARD)
    )
}

internal fun groupBothOrderLabel(value: String): String {
    return if (value == GroupLeaveMonitorSettings.BOTH_CARD_FIRST) "先卡片后文本" else "先文本后卡片"
}

internal fun groupBothOrderChoices(): List<PopupChoice<String>> {
    return listOf(
        PopupChoice("先文本后卡片", GroupLeaveMonitorSettings.BOTH_TEXT_FIRST),
        PopupChoice("先卡片后文本", GroupLeaveMonitorSettings.BOTH_CARD_FIRST)
    )
}

internal fun groupMediaOrderLabel(value: String): String {
    return when (value) {
        GroupLeaveMonitorSettings.MEDIA_BEFORE -> "先媒体后提示"
        GroupLeaveMonitorSettings.MEDIA_AFTER -> "先提示后媒体"
        else -> "不发送媒体"
    }
}

internal fun groupMediaOrderChoices(): List<PopupChoice<String>> {
    return listOf(
        PopupChoice("不发送媒体", GroupLeaveMonitorSettings.MEDIA_NONE),
        PopupChoice("先提示后媒体", GroupLeaveMonitorSettings.MEDIA_AFTER),
        PopupChoice("先媒体后提示", GroupLeaveMonitorSettings.MEDIA_BEFORE)
    )
}

internal fun groupPromptTypeWithGlobalLabel(value: String): String {
    return when (value) {
        GroupLeaveMonitorSettings.MODE_GLOBAL -> "跟随全局"
        GroupLeaveMonitorSettings.PROMPT_CARD -> "卡片"
        GroupLeaveMonitorSettings.PROMPT_BOTH -> "文本+卡片"
        else -> "文本"
    }
}

internal fun groupPromptTypeWithGlobalChoices(): List<PopupChoice<String>> {
    return listOf(PopupChoice("跟随全局", GroupLeaveMonitorSettings.MODE_GLOBAL)) + groupPromptTypeChoices()
}

internal fun groupModeLabel(value: String): String {
    return if (value == GroupLeaveMonitorSettings.MODE_CUSTOM) "单独设置" else "跟随全局"
}

internal fun groupModeChoices(): List<PopupChoice<String>> {
    return listOf(
        PopupChoice("跟随全局", GroupLeaveMonitorSettings.MODE_GLOBAL),
        PopupChoice("单独设置", GroupLeaveMonitorSettings.MODE_CUSTOM)
    )
}

internal fun groupMediaModeLabel(value: String): String {
    return when (value) {
        GroupLeaveMonitorSettings.MODE_CUSTOM -> "单独设置"
        GroupLeaveMonitorSettings.MEDIA_NONE -> "不发媒体"
        else -> "跟随全局"
    }
}

internal fun groupMediaModeChoices(): List<PopupChoice<String>> {
    return listOf(
        PopupChoice("跟随全局", GroupLeaveMonitorSettings.MODE_GLOBAL),
        PopupChoice("单独设置", GroupLeaveMonitorSettings.MODE_CUSTOM),
        PopupChoice("不发媒体", GroupLeaveMonitorSettings.MEDIA_NONE)
    )
}

internal fun groupMemberGroupKey(key: String, groupId: String): String = "${key}_${groupId}"

internal fun groupMemberMediaModeKey(groupId: String): String = GroupLeaveMonitorSettings.KEY_MEDIA_MODE_PREFIX + groupId

internal fun groupMemberDelayModeKey(groupId: String): String = GroupLeaveMonitorSettings.KEY_DELAY_MODE_PREFIX + groupId

internal fun groupBothOrderGlobal(sp: SharedPreferences): String {
    return sp.getString(
        GroupLeaveMonitorSettings.KEY_BOTH_ORDER,
        GroupLeaveMonitorSettings.DEFAULT_BOTH_ORDER
    ) ?: GroupLeaveMonitorSettings.DEFAULT_BOTH_ORDER
}

internal fun groupMemberGlobalString(
    sp: SharedPreferences,
    key: String,
    fallback: String
): String {
    return sp.getString(key, fallback) ?: fallback
}

internal fun groupMemberString(
    sp: SharedPreferences,
    key: String,
    groupId: String,
    fallback: String
): String {
    return sp.getString(groupMemberGroupKey(key, groupId), null) ?: fallback
}

internal fun groupMemberIntString(
    sp: SharedPreferences,
    key: String,
    groupId: String,
    fallback: Int
): String {
    val global = sp.getInt(key, fallback)
    return sp.getInt(groupMemberGroupKey(key, groupId), global).toString()
}

internal fun updateGroupToggle(
    sp: SharedPreferences,
    key: String,
    groupId: String,
    enabled: Boolean
): Set<String> {
    val disabled = parseIds(sp.getString(key, "").orEmpty()).toMutableSet()
    if (enabled) {
        disabled.remove(groupId)
    } else {
        disabled.add(groupId)
    }
    return disabled
}

internal sealed class TextVoiceRoute {
    object Main : TextVoiceRoute()
    object VoicePicker : TextVoiceRoute()
}

@Composable
internal fun TextVoiceMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, TextVoiceSettings.PREFS_NAME) }
    var route by remember { mutableStateOf<TextVoiceRoute>(TextVoiceRoute.Main) }
    var selectedVoice by remember {
        mutableStateOf(TextVoiceSettings.selectedVoiceKey(context))
    }
    var selectedEngine by remember {
        mutableStateOf(TextVoiceSettings.selectedEngine(context))
    }
    var selectedTtsVoice by remember {
        mutableStateOf(
            sp.getString(TextVoiceSettings.KEY_TTS_VOICE, TextVoiceSettings.DEFAULT_TTS_VOICE)
                ?: TextVoiceSettings.DEFAULT_TTS_VOICE
        )
    }
    SettingsRouteTransition(
        targetState = route,
        label = "TextVoiceRoute",
        depthOf = { if (it is TextVoiceRoute.Main) 0 else 1 }
    ) { currentRoute ->
        when (currentRoute) {
            TextVoiceRoute.Main -> TextVoiceMainPage(
                provider = provider,
                sp = sp,
                selectedVoice = selectedVoice,
                selectedEngine = selectedEngine,
                selectedTtsVoice = selectedTtsVoice,
                onBack = onBack,
                onPickVoice = { route = TextVoiceRoute.VoicePicker },
                onEngineChanged = { value ->
                    selectedEngine = value
                    selectedTtsVoice = TextVoiceSettings.DEFAULT_TTS_VOICE
                    sp.edit()
                        .putString(TextVoiceSettings.KEY_ENGINE, value)
                        .putString(TextVoiceSettings.KEY_TTS_VOICE, TextVoiceSettings.DEFAULT_TTS_VOICE)
                        .apply()
                },
                onTtsVoiceChanged = { value ->
                    selectedTtsVoice = value
                    sp.edit().putString(TextVoiceSettings.KEY_TTS_VOICE, value).apply()
                }
            )
            TextVoiceRoute.VoicePicker -> OptionPickerPage(
                request = OptionPickerRequest(
                    title = "选择在线语音",
                    options = TextVoiceSettings.voiceOptions.mapIndexed { index, option ->
                        OptionItem(option.label, index, option.voiceId)
                    },
                    currentValue = TextVoiceSettings.voiceOptions
                        .indexOfFirst { it.key == selectedVoice }
                        .coerceAtLeast(0),
                    onSelected = {}
                ),
                onBack = { route = TextVoiceRoute.Main },
                onSelected = { item ->
                    val option = TextVoiceSettings.voiceOptions.getOrNull(item.value)
                        ?: return@OptionPickerPage
                    selectedVoice = option.key
                    sp.edit().putString(TextVoiceSettings.KEY_VOICE, option.key).apply()
                }
            )
        }
    }
}

@Composable
internal fun TextVoiceMainPage(
    provider: FeatureSettingsProvider,
    sp: SharedPreferences,
    selectedVoice: String,
    selectedEngine: String,
    selectedTtsVoice: String,
    onBack: () -> Unit,
    onPickVoice: () -> Unit,
    onEngineChanged: (String) -> Unit,
    onTtsVoiceChanged: (String) -> Unit
) {
    val context = LocalContext.current
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    val installedTtsEngines = remember(context) { TextSpeechEngineCatalog.installed(context) }
    val engineOptions = remember(installedTtsEngines, selectedEngine) {
        buildList {
            add(PopupChoice("在线语音", TextVoiceSettings.ENGINE_ONLINE))
            add(PopupChoice("跟随系统默认 TTS", TextVoiceSettings.ENGINE_TTS_PREFIX))
            installedTtsEngines.forEach { option ->
                add(PopupChoice(option.label, TextVoiceSettings.ENGINE_TTS_PREFIX + option.packageName))
            }
            val selectedPackage = TextVoiceSettings.ttsPackage(selectedEngine)
            if (TextVoiceSettings.isTtsEngine(selectedEngine) &&
                selectedPackage.isNotBlank() &&
                installedTtsEngines.none { it.packageName == selectedPackage }
            ) {
                add(PopupChoice("已不可用（$selectedPackage）", selectedEngine))
            }
        }
    }
    var ttsVoiceChoices by remember { mutableStateOf<List<PopupChoice<String>>>(emptyList()) }
    var ttsVoiceLoading by remember { mutableStateOf(false) }
    var ttsVoiceError by remember { mutableStateOf("") }
    var ttsFallbackEnginePackage by remember { mutableStateOf("") }
    val ttsFallbackEngineLabel = remember(installedTtsEngines, ttsFallbackEnginePackage) {
        installedTtsEngines.firstOrNull { it.packageName == ttsFallbackEnginePackage }
            ?.label
            ?: ttsFallbackEnginePackage
    }
    DisposableEffect(context, selectedEngine) {
        val useTts = TextVoiceSettings.isTtsEngine(selectedEngine)
        ttsVoiceLoading = useTts
        ttsVoiceError = ""
        ttsFallbackEnginePackage = ""
        ttsVoiceChoices = emptyList()
        val handle = if (useTts) {
            TextSpeechVoiceCatalog.load(
                context,
                TextVoiceSettings.ttsPackage(selectedEngine)
            ) { result ->
                ttsVoiceChoices = result.options.map { PopupChoice(it.label, it.name) }
                ttsVoiceError = result.error
                ttsFallbackEnginePackage = if (result.usedFallback) {
                    result.activeEnginePackage.ifBlank { "其它可用引擎" }
                } else {
                    ""
                }
                ttsVoiceLoading = false
            }
        } else {
            null
        }
        onDispose { handle?.cancel() }
    }
    val ttsVoiceOptions = remember(ttsVoiceChoices, selectedTtsVoice) {
        buildList {
            add(PopupChoice("跟随引擎默认", TextVoiceSettings.DEFAULT_TTS_VOICE))
            addAll(ttsVoiceChoices)
            if (selectedTtsVoice.isNotBlank() && ttsVoiceChoices.none { it.value == selectedTtsVoice }) {
                add(PopupChoice("已不可用（$selectedTtsVoice）", selectedTtsVoice))
            }
        }
    }
    var sendEnabled by remember {
        mutableStateOf(
            sp.getBoolean(TextVoiceSettings.KEY_SEND_ENABLE, TextVoiceSettings.DEFAULT_SEND_ENABLE)
        )
    }
    var playEnabled by remember {
        mutableStateOf(
            sp.getBoolean(TextVoiceSettings.KEY_PLAY_ENABLE, TextVoiceSettings.DEFAULT_PLAY_ENABLE)
        )
    }
    var speechRate by remember {
        mutableStateOf(
            TextVoiceSettings.normalizeSpeechRate(
                sp.getFloat(TextVoiceSettings.KEY_SPEECH_RATE, TextVoiceSettings.DEFAULT_SPEECH_RATE)
            )
        )
    }
    PageScaffold(
        title = provider.title(),
        largeTitle = provider.title(),
        scrollBehavior = scrollBehavior,
        bottomBar = { BottomActionBar("返回", onBack) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(text = "文本转语音") }
            item {
                SettingsCard {
                    SwitchRow(
                        checked = sendEnabled,
                        title = "文本转换语音模式（发送文字转语音）",
                        summary = "聊天输入 #tts 开启或关闭当前会话，输入 #tts e 使用英文模式"
                    ) {
                        sendEnabled = it
                        sp.edit().putBoolean(TextVoiceSettings.KEY_SEND_ENABLE, it).apply()
                    }
                    InsetDivider()
                    SwitchRow(
                        checked = playEnabled,
                        title = "文字转换语音播放",
                        summary = "长按文字消息后选择“转语音播放[H]”"
                    ) {
                        playEnabled = it
                        sp.edit().putBoolean(TextVoiceSettings.KEY_PLAY_ENABLE, it).apply()
                    }
                    if (sendEnabled || playEnabled) {
                        InsetDivider()
                        PopupChoiceRow(
                            title = "转换引擎",
                            summary = "选择在线语音或设备上已安装的 TTS 引擎",
                            options = engineOptions,
                            currentValue = selectedEngine,
                            onValueChanged = onEngineChanged
                        )
                        InsetDivider()
                        if (TextVoiceSettings.isTtsEngine(selectedEngine)) {
                            PopupChoiceRow(
                                title = "TTS 角色",
                                summary = when {
                                    ttsVoiceLoading -> "正在读取所选引擎的角色"
                                    ttsVoiceError.isNotBlank() -> "读取失败：$ttsVoiceError"
                                    ttsFallbackEnginePackage.isNotBlank() ->
                                        "系统默认不可用，已临时使用 $ttsFallbackEngineLabel"
                                    ttsVoiceChoices.isEmpty() -> "所选引擎未提供可选角色，将跟随引擎默认"
                                    else -> "选择所选 TTS 引擎提供的角色"
                                },
                                options = ttsVoiceOptions,
                                currentValue = selectedTtsVoice,
                                enabled = !ttsVoiceLoading,
                                onValueChanged = onTtsVoiceChanged
                            )
                        } else {
                            ActionRow(
                                title = "在线语音",
                                summary = TextVoiceSettings.voiceLabel(selectedVoice),
                                onClick = onPickVoice
                            )
                        }
                        InsetDivider()
                        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp)) {
                            Text(
                                text = "语速 ${String.format(Locale.ROOT, "%.1f", speechRate)}x",
                                color = MiuixTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Medium
                            )
                            Slider(
                                value = speechRate,
                                onValueChange = {
                                    speechRate = TextVoiceSettings.normalizeSpeechRate(it)
                                },
                                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                                valueRange = TextVoiceSettings.MIN_SPEECH_RATE..TextVoiceSettings.MAX_SPEECH_RATE,
                                steps = 28,
                                onValueChangeFinished = {
                                    sp.edit()
                                        .putFloat(TextVoiceSettings.KEY_SPEECH_RATE, speechRate)
                                        .apply()
                                },
                                showKeyPoints = true,
                                keyPoints = listOf(0.1f, 1.0f, 2.0f, 3.0f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun VoiceForwardMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, VoiceForwardSettings.PREFS_NAME) }
    val legacyDefault = remember { sp.getBoolean(VoiceForwardSettings.KEY_ENABLE, VoiceForwardSettings.DEFAULT_ENABLE) }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()

    PageScaffold(
        title = provider.title(),
        largeTitle = provider.title(),
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar("返回", onBack)
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(text = "聊天语音") }
            item {
                SettingsCard {
                    SwitchRow(
                        sp,
                        VoiceForwardSettings.KEY_CHAT_FORWARD_ENABLE,
                        "聊天语音转发",
                        "长按聊天语音后显示转发入口",
                        legacyDefault
                    )
                    InsetDivider()
                    SwitchRow(
                        sp,
                        VoiceForwardSettings.KEY_CHAT_SAVE_ENABLE,
                        "聊天语音保存",
                        "长按聊天语音后显示保存入口",
                        legacyDefault
                    )
                    InsetDivider()
                    SwitchRow(
                        sp,
                        VoiceForwardSettings.KEY_CHAT_MULTI_FORWARD_ENABLE,
                        "多选语音转发",
                        "多选语音后显示逐条转发入口",
                        legacyDefault
                    )
                    InsetDivider()
                    SwitchRow(
                        sp,
                        VoiceForwardSettings.KEY_CHAT_MULTI_MERGE_ENABLE,
                        "多选语音合并",
                        "多选语音后合成为一条语音，可转发或保存为 MP3",
                        false
                    )
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "收藏语音") }
            item {
                SettingsCard {
                    SwitchRow(
                        sp,
                        VoiceForwardSettings.KEY_FAVORITE_FORWARD_ENABLE,
                        "收藏语音转发",
                        "长按收藏语音后显示转发入口",
                        legacyDefault
                    )
                    InsetDivider()
                    SwitchRow(
                        sp,
                        VoiceForwardSettings.KEY_FAVORITE_SAVE_ENABLE,
                        "收藏语音保存",
                        "长按收藏语音后显示保存入口",
                        legacyDefault
                    )
                }
            }
        }
    }
}

internal object AudioTransformSettingsPage {
    @Composable
    operator fun invoke(
        context: Context,
        provider: FeatureSettingsProvider,
        onBack: () -> Unit
    ) {
    val sp = remember { HchatStorage.preferences(context, AudioTransformSettings.PREFS_NAME) }
    val bridge = remember { AudioTransformBridge() }
    val handler = remember { Handler(Looper.getMainLooper()) }
    val mainListState = rememberLazyListState()
    var route by remember { mutableStateOf<AudioTransformRoute>(AudioTransformRoute.Main) }
    var mode by remember {
        mutableStateOf(
            sp.getInt(
                AudioTransformSettings.KEY_MODE,
                AudioTransformSettings.DEFAULT_MODE
            )
        )
    }
    var inputPath by rememberSaveable { mutableStateOf("") }
    var inputName by rememberSaveable { mutableStateOf("") }
    var selectedTalker by rememberSaveable {
        mutableStateOf(
            sp.getString(AudioTransformSettings.KEY_LAST_TALKER, "").orEmpty()
        )
    }
    var splitEnabled by remember {
        mutableStateOf(
            sp.getBoolean(
                AudioTransformSettings.KEY_SPLIT_ENABLED,
                AudioTransformSettings.DEFAULT_SPLIT_ENABLED
            )
        )
    }
    var splitDurationSeconds by rememberSaveable {
        mutableStateOf(
            sp.getLong(
                AudioTransformSettings.KEY_SPLIT_DURATION_SECONDS,
                AudioTransformSettings.DEFAULT_SPLIT_DURATION_SECONDS
            ).coerceAtLeast(AudioTransformSettings.MIN_SPLIT_DURATION_SECONDS).toString()
        )
    }
    var clipStartMillis by rememberSaveable { mutableStateOf(0L) }
    var clipEndMillis by rememberSaveable { mutableStateOf(0L) }
    val splitCancellation = remember { AtomicBoolean(false) }
    val splitWorker = remember { AtomicReference<Thread?>(null) }
    var running by remember { mutableStateOf(false) }
    var resultText by remember { mutableStateOf("") }

    DisposableEffect(Unit) {
        onDispose {
            splitCancellation.set(true)
            splitWorker.getAndSet(null)?.interrupt()
        }
    }

    fun updateResult(text: String, toast: Boolean = false) {
        resultText = text
        if (toast && text.isNotBlank()) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
    }

    fun startSend() {
        if (running) return
        val sourcePath = inputPath.takeIf { it.isNotBlank() && File(it).isFile }
        if (sourcePath == null) {
            updateResult("请先选择输入文件", toast = true)
            return
        }
        val talker = selectedTalker.trim()
        if (talker.isEmpty()) {
            updateResult("请先选择聊天", toast = true)
            return
        }
        val validate = audioTransformValidateInput(mode, sourcePath, bridge)
        if (validate != null) {
            updateResult(validate, toast = true)
            return
        }
        val shouldSplit = splitEnabled
        val segmentDuration = if (shouldSplit) splitDurationSeconds.toLongOrNull() else null
        val rangeStart = clipStartMillis.coerceAtLeast(0L)
        val rangeEnd = clipEndMillis.takeIf { it > 0L }
        if (shouldSplit) {
            if (segmentDuration == null) {
                updateResult("请输入有效的每段时长", toast = true)
                return
            }
            if (rangeEnd != null && rangeEnd <= rangeStart) {
                updateResult("结束时间必须晚于开始时间", toast = true)
                return
            }
            if (segmentDuration < AudioTransformSettings.MIN_SPLIT_DURATION_SECONDS) {
                updateResult(
                    "每段时长最少为 ${AudioTransformSettings.MIN_SPLIT_DURATION_SECONDS} 秒",
                    toast = true
                )
                return
            }
        }
        val talkerLabel = audioTransformTalkerSummary(talker)
        splitCancellation.set(false)
        running = true
        resultText = if (shouldSplit) "正在准备切割" else "正在发送到 $talkerLabel"
        val worker = Thread({
            var sentCount = 0
            var totalCount = 0
            val message = try {
                if (!shouldSplit) {
                    val ok = runCatching {
                        WeChatApis.media()?.voices()?.send(talker, sourcePath) == true
                    }.getOrDefault(false)
                    if (ok) "已发送到 $talkerLabel" else "发送失败，请稍后重试"
                } else {
                    AudioTransformSegmenter.prepare(
                        bridge = bridge,
                        sourcePath = sourcePath,
                        cacheDir = context.cacheDir,
                        durationSeconds = segmentDuration!!,
                        startMillis = rangeStart,
                        endMillis = rangeEnd,
                        cancelled = splitCancellation,
                        onProgress = { progress ->
                            handler.post {
                                if (!splitCancellation.get()) resultText = progress
                            }
                        }
                    ).use { batch ->
                        totalCount = batch.segments.size
                        batch.segments.forEachIndexed { index, segment ->
                            if (splitCancellation.get()) throw AudioTransformCancelledException()
                            handler.post {
                                if (!splitCancellation.get()) {
                                    resultText = "正在发送 ${index + 1}/$totalCount"
                                }
                            }
                            val sent = WeChatApis.media()?.voices()?.send(
                                talker,
                                segment.file.absolutePath,
                                segment.durationMillis
                            ) == true
                            if (sent) sentCount++
                            if (splitCancellation.get()) throw AudioTransformCancelledException()
                            if (!sent) {
                                throw IllegalStateException("第 ${index + 1} 段发送失败")
                            }
                            if (sentCount < totalCount) {
                                repeat(5) {
                                    if (splitCancellation.get()) throw AudioTransformCancelledException()
                                    Thread.sleep(100L)
                                }
                            }
                        }
                    }
                    "已发送 $totalCount 段到 $talkerLabel"
                }
            } catch (_: AudioTransformCancelledException) {
                if (totalCount > 0) "已取消，已发送 $sentCount/$totalCount 段" else "已取消"
            } catch (throwable: Throwable) {
                if (splitCancellation.get() || throwable is InterruptedException) {
                    if (totalCount > 0) "已取消，已发送 $sentCount/$totalCount 段" else "已取消"
                } else {
                    val detail = throwable.message ?: "未知错误"
                    if (totalCount > 0) {
                        "发送失败，已发送 $sentCount/$totalCount 段：$detail"
                    } else {
                        "切割发送失败：$detail"
                    }
                }
            }
            if (shouldSplit) splitWorker.compareAndSet(Thread.currentThread(), null)
            handler.post {
                running = false
                updateResult(message, toast = true)
            }
        }, "Hchat-AudioTransform-Send")
        if (shouldSplit) splitWorker.set(worker)
        worker.start()
    }

    fun startSave(outputUri: Uri, outputName: String) {
        if (running) return
        val sourcePath = inputPath.takeIf { it.isNotBlank() && File(it).isFile }
        if (sourcePath == null) {
            updateResult("请先选择输入文件", toast = true)
            return
        }
        val validate = audioTransformValidateInput(mode, sourcePath, bridge)
        if (validate != null) {
            updateResult(validate, toast = true)
            return
        }
        running = true
        resultText = "正在转换并保存"
        Thread({
            val sourceFile = File(sourcePath)
            val tempDir = File(context.cacheDir, "audio_transform_output").apply { mkdirs() }
            val tempFile = File(
                tempDir,
                "audio_${System.currentTimeMillis()}${audioTransformOutputExtension(mode)}"
            )
            val result = runCatching {
                val code = when (mode) {
                    AudioTransformSettings.MODE_AUDIO_TO_SILK_SAVE -> {
                        if (bridge.getFileType(sourcePath) == 1) {
                            sourceFile.copyTo(tempFile, overwrite = true)
                            0
                        } else {
                            bridge.autoToSilk(sourcePath, tempFile.absolutePath, AudioTransformBridge.DEFAULT_HZ)
                        }
                    }
                    AudioTransformSettings.MODE_SILK_TO_MP3_SAVE -> {
                        bridge.silkToMp3(sourcePath, tempFile.absolutePath, AudioTransformBridge.DEFAULT_HZ)
                    }
                    AudioTransformSettings.MODE_SILK_TO_M4A_SAVE -> {
                        bridge.silkToM4a(sourcePath, tempFile.absolutePath, AudioTransformBridge.DEFAULT_HZ)
                    }
                    else -> -2
                }
                if (code != 0) {
                    false to "转换失败：${bridge.getErrorMessage(code)}"
                } else if (!tempFile.isFile || tempFile.length() <= 0L) {
                    false to "转换失败：输出文件为空"
                } else if (!copyAudioTransformFileToUri(context, tempFile, outputUri)) {
                    false to "保存失败：无法写入目标文件"
                } else {
                    true to "已保存为 ${outputName.ifBlank { audioTransformSuggestedFileName(inputName, mode) }}"
                }
            }.getOrElse {
                false to "转换异常：${it.message ?: "未知错误"}"
            }
            if (tempFile.exists()) {
                runCatching { tempFile.delete() }
            }
            handler.post {
                running = false
                updateResult(result.second, toast = true)
            }
        }, "Hchat-AudioTransform-Save").start()
    }

    fun launchSavePicker() {
        if (running) return
        val sourcePath = inputPath.takeIf { it.isNotBlank() && File(it).isFile }
        if (sourcePath == null) {
            updateResult("请先选择输入文件", toast = true)
            return
        }
        val activity = context as? Activity
        if (activity == null) {
            updateResult("当前页面无法打开保存选择器", toast = true)
            return
        }
        AudioTransformDocumentBridge.launchOutput(
            activity = activity,
            suggestedFileName = audioTransformSuggestedFileName(inputName, mode),
            mimeType = audioTransformOutputMime(mode)
        ) { uri, name ->
            startSave(uri, name)
        }
    }

    SettingsRouteTransition(
        targetState = route,
        label = "AudioTransformRoute",
        depthOf = { it.depth() }
    ) { currentRoute ->
        when (currentRoute) {
            AudioTransformRoute.Main -> {
                AudioTransformMainPage(
                    provider = provider,
                    mode = mode,
                    inputPath = inputPath,
                    inputName = inputName,
                    talker = selectedTalker,
                    splitEnabled = splitEnabled,
                    splitDurationSeconds = splitDurationSeconds,
                    clipStartMillis = clipStartMillis,
                    clipEndMillis = clipEndMillis,
                    running = running,
                    resultText = resultText,
                    listState = mainListState,
                    onBack = onBack,
                    onModeChange = { next ->
                        mode = next
                        sp.edit().putInt(AudioTransformSettings.KEY_MODE, next).apply()
                    },
                    onPickInput = {
                        val activity = context as? Activity
                        if (activity == null) {
                            updateResult("当前页面无法打开文件选择器", toast = true)
                        } else {
                            AudioTransformDocumentBridge.launchInput(activity) { path, name ->
                                inputPath = path
                                inputName = name
                                clipStartMillis = 0L
                                clipEndMillis = 0L
                                resultText = ""
                            }
                        }
                    },
                    onPickTalker = { route = AudioTransformRoute.ContactPicker },
                    onSplitEnabledChange = { enabled ->
                        splitEnabled = enabled
                        sp.edit().putBoolean(AudioTransformSettings.KEY_SPLIT_ENABLED, enabled).apply()
                    },
                    onSplitDurationChange = { next ->
                        splitDurationSeconds = next
                        next.toLongOrNull()
                            ?.takeIf { it >= AudioTransformSettings.MIN_SPLIT_DURATION_SECONDS }
                            ?.let { duration ->
                                sp.edit().putLong(
                                    AudioTransformSettings.KEY_SPLIT_DURATION_SECONDS,
                                    duration
                                ).apply()
                            }
                    },
                    onClipRangeChange = { source, start, end ->
                        if (inputPath == source) {
                            clipStartMillis = start.coerceAtLeast(0L)
                            clipEndMillis = end.coerceAtLeast(0L)
                        }
                    },
                    onClearInput = {
                        inputPath = ""
                        inputName = ""
                        clipStartMillis = 0L
                        clipEndMillis = 0L
                    },
                    onCancel = {
                        splitCancellation.set(true)
                        splitWorker.get()?.interrupt()
                        resultText = "正在取消"
                    },
                    onStart = {
                        if (mode == AudioTransformSettings.MODE_AUDIO_TO_SILK_SEND) {
                            startSend()
                        } else {
                            launchSavePicker()
                        }
                    }
                )
            }
            AudioTransformRoute.ContactPicker -> {
                ContactPickerPage(
                    context = context,
                    request = ContactPickerRequest(
                        title = "选择发送聊天",
                        mode = ContactPickerMode.ALL_CHATS,
                        multiSelect = false,
                        existingValue = formatIds(listOfNotNull(selectedTalker.takeIf { it.isNotBlank() })),
                        onValue = {}
                    ),
                    onBack = { route = AudioTransformRoute.Main },
                    onConfirm = { selected ->
                        selectedTalker = selected.firstOrNull()?.id.orEmpty()
                        sp.edit().putString(AudioTransformSettings.KEY_LAST_TALKER, selectedTalker).apply()
                        route = AudioTransformRoute.Main
                    }
                )
            }
        }
    }
}

@Composable
internal fun AudioTransformMainPage(
    provider: FeatureSettingsProvider,
    mode: Int,
    inputPath: String,
    inputName: String,
    talker: String,
    splitEnabled: Boolean,
    splitDurationSeconds: String,
    clipStartMillis: Long,
    clipEndMillis: Long,
    running: Boolean,
    resultText: String,
    listState: LazyListState,
    onBack: () -> Unit,
    onModeChange: (Int) -> Unit,
    onPickInput: () -> Unit,
    onPickTalker: () -> Unit,
    onSplitEnabledChange: (Boolean) -> Unit,
    onSplitDurationChange: (String) -> Unit,
    onClipRangeChange: (String, Long, Long) -> Unit,
    onClearInput: () -> Unit,
    onCancel: () -> Unit,
    onStart: () -> Unit
) {
    val scrollBehavior = MiuixScrollBehavior()
    val actionText = if (mode == AudioTransformSettings.MODE_AUDIO_TO_SILK_SEND) "开始发送" else "开始转换"
    val cancellable = running && mode == AudioTransformSettings.MODE_AUDIO_TO_SILK_SEND && splitEnabled
    PageScaffold(
        title = provider.title(),
        largeTitle = provider.title(),
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = actionText,
                onPrimaryClick = onStart,
                secondaryText = if (cancellable) "取消" else "返回",
                onSecondaryClick = if (cancellable) onCancel else onBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(text = "转换方式") }
            item {
                SettingsCard {
                    PopupChoiceRow(
                        title = "操作",
                        summary = audioTransformModeSummary(mode),
                        options = audioTransformModeOptions(),
                        currentValue = mode.toString(),
                        onValueChanged = { next -> onModeChange(next.toIntOrNull() ?: mode) },
                        enabled = !running
                    )
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "文件") }
            item {
                SettingsCard {
                    ActionRow("选择输入文件", audioTransformInputSummary(inputPath, inputName)) {
                        if (!running) onPickInput()
                    }
                    if (inputPath.isNotBlank()) {
                        InsetDivider()
                        AudioTransformPreviewEditor(
                            inputPath = inputPath,
                            inputName = inputName,
                            clipEnabled = mode == AudioTransformSettings.MODE_AUDIO_TO_SILK_SEND &&
                                splitEnabled,
                            clipStartMillis = clipStartMillis,
                            clipEndMillis = clipEndMillis,
                            enabled = !running,
                            onRangeChange = onClipRangeChange
                        )
                        InsetDivider()
                        ActionRow("清空输入文件", "移除当前已选文件") {
                            if (!running) onClearInput()
                        }
                    }
                    if (mode != AudioTransformSettings.MODE_AUDIO_TO_SILK_SEND) {
                        InsetDivider()
                        InfoRow("输出文件", audioTransformSuggestedFileName(inputName, mode))
                    }
                }
            }
            if (mode == AudioTransformSettings.MODE_AUDIO_TO_SILK_SEND) {
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "发送") }
                item {
                    SettingsCard {
                        ActionRow("选择聊天", audioTransformTalkerSummary(talker)) {
                            if (!running) onPickTalker()
                        }
                        InsetDivider()
                        SwitchRow(
                            checked = splitEnabled,
                            title = "裁剪与自动分割",
                            summary = "按选定起止区间裁剪，并按时长依次发送",
                            enabled = !running,
                            onCheckedChange = onSplitEnabledChange
                        )
                        if (splitEnabled) {
                            InsetDivider()
                            NumberInputRow(
                                title = "自动分割（秒）",
                                summary = "单位秒，最少 1 秒",
                                value = splitDurationSeconds,
                                onValueChange = onSplitDurationChange
                            )
                        }
                    }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "状态") }
            item {
                SettingsCard {
                    InfoRow("当前状态", if (running) "处理中" else "待开始")
                    if (resultText.isNotBlank()) {
                        InsetDivider()
                        InfoRow("最近结果", resultText)
                    }
                }
            }
        }
    }
}

@Composable
internal fun AudioTransformPreviewEditor(
    inputPath: String,
    inputName: String,
    clipEnabled: Boolean,
    clipStartMillis: Long,
    clipEndMillis: Long,
    enabled: Boolean,
    onRangeChange: (String, Long, Long) -> Unit
) {
    val context = LocalContext.current
    val currentRangeChange by rememberUpdatedState(onRangeChange)
    val currentClipStart by rememberUpdatedState(clipStartMillis)
    val currentClipEnd by rememberUpdatedState(clipEndMillis)
    val currentClipEnabled by rememberUpdatedState(clipEnabled)
    var playablePath by remember(inputPath) { mutableStateOf("") }
    var previewPreparing by remember(inputPath) { mutableStateOf(true) }
    var previewError by remember(inputPath) { mutableStateOf("") }
    var player by remember(inputPath) { mutableStateOf<MediaPlayer?>(null) }
    var prepared by remember(inputPath) { mutableStateOf(false) }
    var playing by remember(inputPath) { mutableStateOf(false) }
    var durationMillis by remember(inputPath) { mutableStateOf(0) }
    var positionMillis by remember(inputPath) { mutableStateOf(0) }
    var playerError by remember(inputPath) { mutableStateOf("") }

    LaunchedEffect(inputPath, enabled) {
        playablePath = ""
        previewError = ""
        playerError = ""
        if (!enabled) {
            previewPreparing = false
            return@LaunchedEffect
        }
        previewPreparing = true
        var generatedPreview: File? = null
        try {
            val resolved = withContext(Dispatchers.IO) {
                val source = File(inputPath)
                check(source.isFile) { "输入文件不存在" }
                val previewBridge = AudioTransformBridge()
                if (previewBridge.getFileType(inputPath) != 1) {
                    inputPath
                } else {
                    val previewDir = File(context.cacheDir, "audio_transform_preview").apply { mkdirs() }
                    val output = File(
                        previewDir,
                        "preview_${System.currentTimeMillis()}_${System.nanoTime()}.m4a"
                    )
                    generatedPreview = output
                    val code = previewBridge.silkToM4a(
                        inputPath,
                        output.absolutePath,
                        AudioTransformBridge.DEFAULT_HZ
                    )
                    check(code == 0 && output.isFile && output.length() > 0L) {
                        "试听准备失败：${previewBridge.getErrorMessage(code)}"
                    }
                    output.absolutePath
                }
            }
            playablePath = resolved
            previewPreparing = false
            awaitCancellation()
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (throwable: Throwable) {
            previewPreparing = false
            previewError = throwable.message ?: "试听准备失败"
        } finally {
            generatedPreview?.let { runCatching { it.delete() } }
        }
    }

    DisposableEffect(playablePath, enabled) {
        val path = playablePath
        var effectPlayer: MediaPlayer? = null
        prepared = false
        playing = false
        if (path.isNotBlank() && enabled) {
            runCatching {
                MediaPlayer().also { media ->
                    effectPlayer = media
                    media.setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
                    )
                    media.setDataSource(path)
                    media.setOnPreparedListener { preparedPlayer ->
                        if (player !== preparedPlayer) return@setOnPreparedListener
                        val duration = preparedPlayer.duration.coerceAtLeast(0)
                        durationMillis = duration
                        prepared = duration > 0
                        playing = false
                        val maxStart = (duration - 1).coerceAtLeast(0).toLong()
                        val start = currentClipStart.coerceIn(0L, maxStart)
                        val end = currentClipEnd
                            .takeIf { it > start }
                            ?.coerceAtMost(duration.toLong())
                            ?: duration.toLong()
                        positionMillis = start.toInt()
                        runCatching { preparedPlayer.seekTo(positionMillis) }
                        currentRangeChange(inputPath, start, end)
                    }
                    media.setOnCompletionListener { completed ->
                        if (player !== completed) return@setOnCompletionListener
                        playing = false
                        val reset = if (currentClipEnabled) {
                            currentClipStart.coerceIn(
                                0L,
                                completed.duration.coerceAtLeast(0).toLong()
                            ).toInt()
                        } else {
                            0
                        }
                        runCatching { completed.seekTo(reset) }
                        positionMillis = reset
                    }
                    media.setOnErrorListener { failed, _, _ ->
                        if (player !== failed) return@setOnErrorListener true
                        prepared = false
                        playing = false
                        playerError = "当前音频无法试听"
                        true
                    }
                    player = media
                    media.prepareAsync()
                }
            }.onFailure {
                val failedPlayer = effectPlayer
                effectPlayer = null
                if (player === failedPlayer) player = null
                failedPlayer?.let { media ->
                    media.setOnPreparedListener(null)
                    media.setOnCompletionListener(null)
                    media.setOnErrorListener(null)
                    runCatching { media.release() }
                }
                prepared = false
                playing = false
                playerError = "当前音频无法试听"
            }
        }
        onDispose {
            val disposingPlayer = effectPlayer
            effectPlayer = null
            disposingPlayer?.let { media ->
                media.setOnPreparedListener(null)
                media.setOnCompletionListener(null)
                media.setOnErrorListener(null)
                runCatching { if (media.isPlaying) media.stop() }
                runCatching { media.release() }
            }
            if (player === disposingPlayer) player = null
            prepared = false
            playing = false
        }
    }

    LaunchedEffect(enabled, player, playing, prepared, clipEnabled, clipStartMillis, clipEndMillis) {
        val media = player
        if (!enabled && media != null && playing) {
            runCatching { media.pause() }
            playing = false
        }
        while (enabled && prepared && playing && media != null) {
            val current = runCatching { media.currentPosition.coerceAtLeast(0) }
                .getOrDefault(positionMillis)
            val end = if (clipEnabled) {
                clipEndMillis.takeIf { it > clipStartMillis }
                    ?.coerceAtMost(durationMillis.toLong())
                    ?.toInt()
                    ?: durationMillis
            } else {
                durationMillis
            }
            if (end > 0 && current >= end) {
                runCatching { media.pause() }
                val reset = if (clipEnabled) {
                    clipStartMillis.coerceIn(0L, end.toLong()).toInt()
                } else {
                    0
                }
                runCatching { media.seekTo(reset) }
                positionMillis = reset
                playing = false
                break
            }
            positionMillis = current
            delay(80L)
        }
    }

    fun seekTo(target: Int) {
        val media = player ?: return
        if (!prepared || durationMillis <= 0) return
        val maxStart = (durationMillis - 1).coerceAtLeast(0).toLong()
        val rangeStart = clipStartMillis.coerceIn(0L, maxStart).toInt()
        val rangeEnd = clipEndMillis
            .takeIf { it > rangeStart }
            ?.coerceAtMost(durationMillis.toLong())
            ?.toInt()
            ?: durationMillis
        val lowerBound = if (playing && clipEnabled) rangeStart else 0
        val upperBound = if (playing && clipEnabled) rangeEnd else durationMillis
        val next = target.coerceIn(lowerBound, upperBound.coerceAtLeast(lowerBound))
        runCatching { media.seekTo(next) }
        positionMillis = next
    }

    val effectiveClipStart = clipStartMillis.coerceIn(0L, durationMillis.coerceAtLeast(0).toLong())
    val effectiveClipEnd = clipEndMillis
        .takeIf { it > effectiveClipStart }
        ?.coerceAtMost(durationMillis.toLong())
        ?: durationMillis.toLong()
    val statusText = when {
        previewPreparing -> "正在准备试听"
        previewError.isNotBlank() -> previewError
        playerError.isNotBlank() -> playerError
        !enabled -> "处理中"
        !prepared -> "正在加载"
        playing -> "播放中"
        else -> "可试听"
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp)) {
            Text(
                text = inputName.ifBlank { File(inputPath).name.ifBlank { "已选音频" } },
                color = MiuixTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val playEnabled = enabled && prepared
                Box(
                    modifier = Modifier.size(40.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            if (playEnabled) {
                                MiuixTheme.colorScheme.primary
                            } else {
                                MiuixTheme.colorScheme.onSurface.copy(alpha = 0.08f)
                            }
                        )
                        .clickable(enabled = playEnabled) {
                            val media = player ?: return@clickable
                            if (playing) {
                                runCatching { media.pause() }
                                playing = false
                            } else {
                                val start = if (clipEnabled) {
                                    effectiveClipStart.toInt()
                                } else {
                                    0
                                }
                                val end = if (clipEnabled) {
                                    effectiveClipEnd.toInt()
                                } else {
                                    durationMillis
                                }
                                if (positionMillis < start || positionMillis >= end) {
                                    seekTo(start)
                                }
                                runCatching {
                                    media.start()
                                    playing = true
                                }.onFailure {
                                    playerError = "当前音频无法试听"
                                    playing = false
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        imageVector = if (playing) NavIcons.Pause else NavIcons.Play,
                        contentDescription = if (playing) "暂停" else "播放",
                        colorFilter = ColorFilter.tint(
                            if (playEnabled) Color.White else MiuixTheme.colorScheme.onSurfaceVariantSummary
                        ),
                        modifier = Modifier.size(22.dp)
                    )
                }
                Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                    Text(
                        text = "${formatAudioTransformTime(positionMillis.toLong())} / " +
                            formatAudioTransformTime(durationMillis.toLong()),
                        color = MiuixTheme.colorScheme.onSurface,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = statusText,
                        color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Slider(
                value = positionMillis.coerceIn(0, durationMillis.coerceAtLeast(0)).toFloat(),
                onValueChange = { next ->
                    if (enabled && prepared) seekTo(next.toInt())
                },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    .graphicsLayer(alpha = if (enabled && prepared) 1f else 0.45f),
                valueRange = 0f..durationMillis.coerceAtLeast(1).toFloat(),
                steps = 0,
                showKeyPoints = false
            )
            if (clipEnabled && durationMillis > 0) {
                Text(
                    text = "选定区间  ${formatAudioTransformTime(effectiveClipStart)} - " +
                        formatAudioTransformTime(effectiveClipEnd),
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
        if (clipEnabled) {
            InsetDivider()
            ActionRow(
                title = "开始时间",
                summary = if (prepared) {
                    formatAudioTransformTime(effectiveClipStart)
                } else {
                    "等待音频加载"
                }
            ) {
                if (!enabled || !prepared) return@ActionRow
                if (positionMillis.toLong() >= effectiveClipEnd) {
                    Toast.makeText(context, "开始时间必须早于结束时间", Toast.LENGTH_SHORT).show()
                } else {
                    currentRangeChange(inputPath, positionMillis.toLong(), effectiveClipEnd)
                }
            }
            InsetDivider()
            ActionRow(
                title = "结束时间",
                summary = if (prepared) {
                    formatAudioTransformTime(effectiveClipEnd)
                } else {
                    "等待音频加载"
                }
            ) {
                if (!enabled || !prepared) return@ActionRow
                if (positionMillis.toLong() <= effectiveClipStart) {
                    Toast.makeText(context, "结束时间必须晚于开始时间", Toast.LENGTH_SHORT).show()
                } else {
                    currentRangeChange(inputPath, effectiveClipStart, positionMillis.toLong())
                }
            }
            InsetDivider()
            ActionRow("重新设置", "恢复完整音频区间") {
                if (!enabled || !prepared) return@ActionRow
                currentRangeChange(inputPath, 0L, durationMillis.toLong())
                seekTo(0)
            }
        }
    }
}

internal fun formatAudioTransformTime(millis: Long): String {
    val totalSeconds = millis.coerceAtLeast(0L) / 1000L
    val seconds = totalSeconds % 60L
    val minutes = totalSeconds / 60L % 60L
    val hours = totalSeconds / 3600L
    return if (hours > 0L) {
        String.format(Locale.ROOT, "%d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format(Locale.ROOT, "%02d:%02d", minutes, seconds)
    }
}

internal fun audioTransformModeOptions(): List<PopupChoice<String>> = listOf(
    PopupChoice("任意音频转silk保存", AudioTransformSettings.MODE_AUDIO_TO_SILK_SAVE.toString()),
    PopupChoice("任意音频转silk发送", AudioTransformSettings.MODE_AUDIO_TO_SILK_SEND.toString()),
    PopupChoice("Silk 转 MP3 保存", AudioTransformSettings.MODE_SILK_TO_MP3_SAVE.toString()),
    PopupChoice("Silk 转 M4A 保存", AudioTransformSettings.MODE_SILK_TO_M4A_SAVE.toString())
)

internal fun audioTransformModeSummary(mode: Int): String = when (mode) {
    AudioTransformSettings.MODE_AUDIO_TO_SILK_SEND -> "把任意音频转成微信语音并直接发送"
    AudioTransformSettings.MODE_SILK_TO_MP3_SAVE -> "把 Silk 语音导出为 MP3 文件"
    AudioTransformSettings.MODE_SILK_TO_M4A_SAVE -> "把 Silk 语音导出为 M4A 文件"
    else -> "把任意音频转成 Silk 并保存到本地"
}

internal fun audioTransformValidateInput(
    mode: Int,
    inputPath: String,
    bridge: AudioTransformBridge
): String? {
    if (!File(inputPath).isFile) return "输入文件不存在"
    if (mode == AudioTransformSettings.MODE_SILK_TO_MP3_SAVE ||
        mode == AudioTransformSettings.MODE_SILK_TO_M4A_SAVE
    ) {
        val fileType = bridge.getFileType(inputPath)
        if (fileType != 1) return "当前模式只支持 Silk 输入"
    }
    return null
}

internal fun audioTransformSuggestedFileName(inputName: String, mode: Int): String {
    val rawName = inputName.trim().ifBlank { "audio_output" }
    val base = rawName.substringBeforeLast('.').ifBlank { rawName }
    return base + audioTransformOutputExtension(mode)
}

internal fun audioTransformOutputExtension(mode: Int): String = when (mode) {
    AudioTransformSettings.MODE_SILK_TO_MP3_SAVE -> ".mp3"
    AudioTransformSettings.MODE_SILK_TO_M4A_SAVE -> ".m4a"
    else -> ".silk"
}

internal fun audioTransformOutputMime(mode: Int): String = when (mode) {
    AudioTransformSettings.MODE_SILK_TO_MP3_SAVE -> "audio/mpeg"
    AudioTransformSettings.MODE_SILK_TO_M4A_SAVE -> "audio/mp4"
    else -> "application/octet-stream"
}

internal fun audioTransformInputSummary(inputPath: String, inputName: String): String {
    if (inputPath.isBlank()) return "未选择"
    return inputName.ifBlank { File(inputPath).name.ifBlank { "已选择文件" } }
}

internal fun audioTransformTalkerSummary(talker: String): String {
    if (talker.isBlank()) return "未选择"
    val display = runCatching { WeChatApis.contacts()?.getDisplayName(talker) }.getOrNull().orEmpty()
    return if (display.isBlank() || display == talker) talker else "$display · $talker"
}

internal fun copyAudioTransformFileToUri(context: Context, source: File, uri: Uri): Boolean {
    return runCatching {
        context.contentResolver.openOutputStream(uri, "w")?.use { output ->
            source.inputStream().use { input -> input.copyTo(output) }
        } != null
    }.getOrDefault(false)
}
}
