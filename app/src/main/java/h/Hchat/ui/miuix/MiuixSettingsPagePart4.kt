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

internal object PluginMarketHistoryUi {
    @Composable
    fun HistoryList(
        versions: List<PluginMarketHistoryVersion>,
        loading: Boolean,
        error: String,
        installingVersionId: String?,
        enabled: Boolean,
        onRetry: () -> Unit,
        onInstall: (PluginMarketHistoryVersion) -> Unit
    ) {
        Text(
            text = "历史版本",
            color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 14.dp)
        )
        when {
            loading -> EmptyText("正在加载历史版本...")
            error.isNotBlank() -> {
                EmptyText(error)
                TextButton(
                    text = "重新加载",
                    onClick = onRetry,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.textButtonColorsPrimary()
                )
            }
            versions.isEmpty() -> EmptyText("暂无历史版本")
            else -> versions.forEachIndexed { index, version ->
                if (index > 0) InsetDivider(start = 0.dp)
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = buildString {
                                append(version.versionName.ifBlank { "未命名版本" })
                                if (index == 0) append("（最新）")
                            },
                            color = MiuixTheme.colorScheme.onSurface,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "${formatPluginMarketTime(version.createdAt)} · ${formatBytes(version.totalSize)}",
                            color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "更新说明：${version.releaseNotes.ifBlank { "暂无更新说明" }}",
                            color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 3.dp)
                        )
                    }
                    TextButton(
                        text = if (installingVersionId == version.versionId) "读取中" else "安装",
                        onClick = { onInstall(version) },
                        enabled = enabled && installingVersionId == null,
                        colors = ButtonDefaults.textButtonColorsPrimary()
                    )
                }
            }
        }
    }

    private fun formatBytes(size: Long): String = when {
        size >= 1024L * 1024L -> String.format(Locale.US, "%.1f MiB", size / (1024.0 * 1024.0))
        size >= 1024L -> String.format(Locale.US, "%.1f KiB", size / 1024.0)
        else -> "$size B"
    }
}

internal val pluginMarketBeijingTimeFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
        .withZone(ZoneId.of("Asia/Shanghai"))

internal fun formatPluginMarketTime(value: String): String {
    val source = value.trim()
    if (source.isEmpty()) return "时间未知"
    val instant = runCatching { Instant.parse(source) }
        .recoverCatching { OffsetDateTime.parse(source, DateTimeFormatter.ISO_DATE_TIME).toInstant() }
        .recoverCatching {
            LocalDateTime.parse(source, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                .toInstant(ZoneOffset.UTC)
        }
        .getOrNull()
        ?: return source
    return pluginMarketBeijingTimeFormatter.format(instant)
}

internal object ScriptPluginAgentUi {

internal data class ScriptPluginAgentPendingWrite(
    val messageIndex: Int,
    val draft: ScriptPluginAgentDraft,
    val diff: String,
    val isNewPlugin: Boolean,
    val confirmsCreation: Boolean,
    val risky: Boolean
)

internal data class ScriptPluginAgentPendingDelete(
    val messageIndex: Int,
    val pluginId: String,
    val pluginName: String
)

internal data class ScriptPluginAgentPendingWorkspaceChange(
    val messageIndex: Int,
    val change: ScriptPluginAgentWorkspaceChange,
    val isNewPlugin: Boolean,
    val hasDeletedPaths: Boolean,
    val risky: Boolean,
    val applyError: String = ""
)

internal class ScriptPluginAgentWorkspaceToolApproval(
    val confirmation: ScriptPluginAgentWorkspaceToolConfirmation
) {
    private val decision = AtomicReference<ScriptPluginAgentWorkspaceWriteDecision?>(null)
    private val latch = CountDownLatch(1)

    fun resolve(value: ScriptPluginAgentWorkspaceWriteDecision) {
        if (decision.compareAndSet(null, value)) latch.countDown()
    }

    fun await(cancellation: ScriptPluginAgentCancellation): ScriptPluginAgentWorkspaceWriteDecision {
        while (!latch.await(200L, TimeUnit.MILLISECONDS)) cancellation.throwIfCancelled()
        cancellation.throwIfCancelled()
        return decision.get() ?: ScriptPluginAgentWorkspaceWriteDecision.CANCEL
    }
}

internal data class ScriptPluginAgentPendingMessage(
    val id: Long,
    val content: String,
    val attachments: List<ScriptPluginAgentAttachment>,
    val quotedMessage: ScriptPluginAgentQuotedMessage?
)

internal data class ScriptPluginAgentRenderedMessage(
    val sourceIndex: Int,
    val message: ScriptPluginAgentChatMessage
)

internal class ScriptPluginAgentSessionRuntimeState(session: ScriptPluginAgentSession) {
    val id = session.id
    val title = mutableStateOf(session.title)
    val createdAt = mutableStateOf(session.createdAt)
    val messages = mutableStateOf(session.messages)
    val draft = mutableStateOf(session.draft)
    val targetPluginId = mutableStateOf(session.targetPluginId)
    val workspaceSessionId = mutableStateOf(session.id)
    val conversationSummary = mutableStateOf(session.conversationSummary)
    val nativeToolHistory = mutableStateOf(session.nativeToolHistory)
    val protocolTranscript = mutableStateOf(session.protocolTranscript)
    val compactedMessageCount = mutableStateOf(session.compactedMessageCount)
    val pinned = mutableStateOf(session.pinned)
    val locked = mutableStateOf(session.locked)
    val sortOrder = mutableStateOf(session.sortOrder)
    val resumeState = mutableStateOf(session.resumeState)
    val messageInput = mutableStateOf("")
    val pendingAttachments = mutableStateOf<List<ScriptPluginAgentAttachment>>(emptyList())
    val pendingQuotedMessage = mutableStateOf<ScriptPluginAgentQuotedMessage?>(null)
    val pendingMessages = mutableStateOf<List<ScriptPluginAgentPendingMessage>>(emptyList())
    val pendingMessagesExpanded = mutableStateOf(true)
    val nextPendingMessageId = mutableStateOf(1L)
    val generating = mutableStateOf(false)
    val generationStartedAt = mutableStateOf(0L)
    val generationHasVisibleReply = mutableStateOf(false)
    val contextCompacting = mutableStateOf(false)
    val contextCompactionStartedAt = mutableStateOf(0L)
    val applyingFileOperation = mutableStateOf(false)
    val pendingWrite = mutableStateOf<ScriptPluginAgentPendingWrite?>(null)
    val pendingDelete = mutableStateOf<ScriptPluginAgentPendingDelete?>(null)
    val pendingWorkspaceChange = mutableStateOf<ScriptPluginAgentPendingWorkspaceChange?>(null)
    val pendingToolApproval = mutableStateOf<ScriptPluginAgentWorkspaceToolApproval?>(null)
    val showPendingWorkspaceDialog = mutableStateOf(false)
    val showPendingWriteDialog = mutableStateOf(false)
    val showPendingDeleteDialog = mutableStateOf(false)
    val showToolApprovalDialog = mutableStateOf(false)
    val activeGenerationId = mutableStateOf("")
    val activeAssistantIndex = mutableStateOf(-1)
    val activeCancellation = mutableStateOf<ScriptPluginAgentCancellation?>(null)
    private val lastSnapshotAt = AtomicLong(session.updatedAt)
    private val checkpointSeq = AtomicLong(session.checkpointSeq)

    fun snapshot(updatedAt: Long = nextSnapshotAt()): ScriptPluginAgentSession {
        val workspaceBelongsToSession = workspaceSessionId.value == id
        return ScriptPluginAgentSession(
            id = id,
            title = title.value,
            createdAt = createdAt.value,
            updatedAt = updatedAt,
            messages = messages.value,
            draft = draft.value.takeIf { workspaceBelongsToSession },
            targetPluginId = targetPluginId.value.takeIf { workspaceBelongsToSession }.orEmpty(),
            conversationSummary = conversationSummary.value,
            nativeToolHistory = nativeToolHistory.value,
            protocolTranscript = protocolTranscript.value,
            compactedMessageCount = compactedMessageCount.value.coerceIn(0, messages.value.size),
            pinned = pinned.value,
            locked = locked.value,
            sortOrder = sortOrder.value,
            resumeState = resumeState.value,
            checkpointSeq = checkpointSeq.incrementAndGet()
        )
    }

    private fun nextSnapshotAt(): Long {
        return lastSnapshotAt.updateAndGet { previous ->
            maxOf(System.currentTimeMillis(), previous + 1L)
        }
    }

    fun isBusy(): Boolean {
        return isWorking() ||
            pendingToolApproval.value != null || pendingWorkspaceChange.value != null ||
            pendingWrite.value != null || pendingDelete.value != null
    }

    fun isWorking(): Boolean {
        return generating.value || contextCompacting.value || applyingFileOperation.value
    }
}

internal class ScriptPluginAgentSessionCheckpointSaver(
    context: Context,
    private val state: ScriptPluginAgentSessionRuntimeState
) {
    private val appContext = context.applicationContext ?: context
    private val handler = Handler(Looper.getMainLooper())
    private var scheduled = false
    private var lastQueuedAt = 0L
    private val saveRunnable = Runnable {
        scheduled = false
        persist()
    }

    fun schedule(force: Boolean = false) {
        val now = SystemClock.uptimeMillis()
        if (force || now - lastQueuedAt >= 600L) {
            handler.removeCallbacks(saveRunnable)
            scheduled = false
            persist()
            return
        }
        if (!scheduled) {
            scheduled = true
            handler.postDelayed(saveRunnable, (600L - (now - lastQueuedAt)).coerceAtLeast(1L))
        }
    }

    private fun persist() {
        if (!ScriptPluginAgentSessionStore.hasConversation(state.messages.value)) return
        lastQueuedAt = SystemClock.uptimeMillis()
        ScriptPluginAgentSessionStore.saveAsync(appContext, state.snapshot())
    }
}

internal object ScriptPluginAgentRuntime {
    private val sessions = java.util.concurrent.ConcurrentHashMap<String, ScriptPluginAgentSessionRuntimeState>()

    @Volatile
    private var resumeSessionId: String? = null

    fun createSession(): ScriptPluginAgentSessionRuntimeState {
        return stateFor(ScriptPluginAgentSessionStore.newSession())
    }

    fun stateFor(session: ScriptPluginAgentSession): ScriptPluginAgentSessionRuntimeState {
        return sessions.computeIfAbsent(session.id) { ScriptPluginAgentSessionRuntimeState(session) }
    }

    fun consumeResumeSession(context: Context): ScriptPluginAgentSessionRuntimeState? {
        val id = resumeSessionId
        resumeSessionId = null
        if (id != null) {
            return sessions[id] ?: ScriptPluginAgentSessionStore.load(context, id)?.let(::stateFor)
        }
        val interrupted = ScriptPluginAgentSessionStore.list(context).firstOrNull { session ->
            val checkpoint = session.resumeState?.takeIf { it.autoOpen } ?: return@firstOrNull false
            session.messages.any { message ->
                message.turnId == checkpoint.turnId &&
                    (message.status == "interrupted" || message.status == "error" ||
                        message.toolEvents.any { it.status == "interrupted" })
            }
        }
        return interrupted?.let(::stateFor)
    }

    fun resumeOnNextEntry(sessionId: String) {
        resumeSessionId = sessionId
    }

    fun snapshots(): List<ScriptPluginAgentSession> {
        return sessions.values
            .map { it.snapshot() }
            .filter { ScriptPluginAgentSessionStore.hasConversation(it.messages) }
    }

    fun isBusy(sessionId: String): Boolean {
        return sessions[sessionId]?.isBusy() == true
    }

    fun workingSessionIds(): Set<String> {
        return sessions.values
            .filter { it.isWorking() }
            .mapTo(LinkedHashSet()) { it.id }
    }

    fun updateMetadata(session: ScriptPluginAgentSession) {
        sessions[session.id]?.let { state ->
            state.title.value = session.title
            state.pinned.value = session.pinned
            state.locked.value = session.locked
            state.sortOrder.value = session.sortOrder
        }
    }

    fun remove(sessionId: String) {
        sessions.remove(sessionId)
        if (resumeSessionId == sessionId) resumeSessionId = null
    }
}

@Composable
fun ScriptPluginAgentWorkspacePage(
    context: Context,
    onBack: () -> Unit,
    onExitHandlerChanged: ((() -> Unit)?) -> Unit
) {
    val initialSessionState = remember(context) {
        ScriptPluginAgentRuntime.consumeResumeSession(context) ?: ScriptPluginAgentRuntime.createSession()
    }
    var activeSessionState by remember { mutableStateOf(initialSessionState) }
    val renderedSessionState = activeSessionState
    val activeSessionId = renderedSessionState.id
    var sessionTitle by renderedSessionState.title
    var sessionMessages by renderedSessionState.messages
    var sessionDraft by renderedSessionState.draft
    var targetPluginId by renderedSessionState.targetPluginId
    var workspaceSessionId by renderedSessionState.workspaceSessionId
    var conversationSummary by renderedSessionState.conversationSummary
    var nativeToolHistory by renderedSessionState.nativeToolHistory
    var protocolTranscript by renderedSessionState.protocolTranscript
    var compactedMessageCount by renderedSessionState.compactedMessageCount
    var sessionPinned by renderedSessionState.pinned
    var sessionLocked by renderedSessionState.locked
    var sessionSortOrder by renderedSessionState.sortOrder
    var resumeState by renderedSessionState.resumeState
    var messageInput by renderedSessionState.messageInput
    var pendingAttachments by renderedSessionState.pendingAttachments
    var pendingQuotedMessage by renderedSessionState.pendingQuotedMessage
    var pendingMessages by renderedSessionState.pendingMessages
    var pendingMessagesExpanded by renderedSessionState.pendingMessagesExpanded
    var nextPendingMessageId by renderedSessionState.nextPendingMessageId
    val initialProfile = remember(context) { ScriptPluginAgentSettings.loadActiveProfile(context) }
    val savedConfig = initialProfile.config
    var activeProfileId by remember { mutableStateOf(initialProfile.id) }
    var activeProfileName by remember { mutableStateOf(initialProfile.name) }
    var apiBase by remember { mutableStateOf(savedConfig.apiBaseUrl) }
    var endpointMode by remember { mutableStateOf(savedConfig.endpointMode) }
    var apiKey by remember { mutableStateOf(savedConfig.apiKey) }
    var model by remember { mutableStateOf(savedConfig.model) }
    var mcpServers by remember { mutableStateOf(savedConfig.mcpServers) }
    var autoCompactEnabled by remember { mutableStateOf(savedConfig.autoCompactEnabled) }
    var webSearchEnabled by remember { mutableStateOf(savedConfig.webSearchEnabled) }
    var workspaceWriteApprovalMode by remember { mutableStateOf(savedConfig.workspaceWriteApprovalMode) }
    var promptCacheMode by remember { mutableStateOf(savedConfig.promptCacheMode) }
    var compactTokenThreshold by remember { mutableStateOf(savedConfig.compactTokenThreshold.toString()) }
    var showHistory by remember { mutableStateOf(false) }
    var showConfig by remember { mutableStateOf(false) }
    var showModelPicker by remember { mutableStateOf(false) }
    var showQuickProfilePicker by remember { mutableStateOf(false) }
    var showQuickOptions by remember { mutableStateOf(false) }
    var generating by renderedSessionState.generating
    var generationStartedAt by renderedSessionState.generationStartedAt
    var generationHasVisibleReply by renderedSessionState.generationHasVisibleReply
    var contextCompacting by renderedSessionState.contextCompacting
    var contextCompactionStartedAt by renderedSessionState.contextCompactionStartedAt
    var applyingFileOperation by renderedSessionState.applyingFileOperation
    var pendingWrite by renderedSessionState.pendingWrite
    var pendingDelete by renderedSessionState.pendingDelete
    var pendingWorkspaceChange by renderedSessionState.pendingWorkspaceChange
    var pendingToolApproval by renderedSessionState.pendingToolApproval
    var showPendingWorkspaceDialog by renderedSessionState.showPendingWorkspaceDialog
    var showPendingWriteDialog by renderedSessionState.showPendingWriteDialog
    var showPendingDeleteDialog by renderedSessionState.showPendingDeleteDialog
    var showToolApprovalDialog by renderedSessionState.showToolApprovalDialog
    var testingConnection by remember { mutableStateOf(false) }
    var activeGenerationId by renderedSessionState.activeGenerationId
    var activeAssistantIndex by renderedSessionState.activeAssistantIndex
    var activeCancellation by renderedSessionState.activeCancellation
    var historyVersion by remember { mutableStateOf(0) }
    var profileVersion by remember { mutableStateOf(0) }
    val sessions = remember(historyVersion, showHistory) {
        if (showHistory) ScriptPluginAgentSessionStore.list(context) else emptyList()
    }
    val profiles = remember(profileVersion) { ScriptPluginAgentSettings.loadProfiles(context) }
    val checkpointSaver = remember(renderedSessionState) {
        ScriptPluginAgentSessionCheckpointSaver(context, renderedSessionState)
    }
    val exitPrepared = remember { AtomicBoolean(false) }

    fun hasPendingConfirmation(): Boolean {
        return pendingToolApproval != null ||
            pendingWorkspaceChange != null ||
            pendingWrite != null ||
            pendingDelete != null
    }

    fun showPendingConfirmation(): Boolean {
        when {
            pendingToolApproval != null -> showToolApprovalDialog = true
            pendingWorkspaceChange != null -> showPendingWorkspaceDialog = true
            pendingWrite != null -> showPendingWriteDialog = true
            pendingDelete != null -> showPendingDeleteDialog = true
            else -> return false
        }
        return true
    }

    fun saveCurrentSession() {
        if (!ScriptPluginAgentSessionStore.hasConversation(sessionMessages)) {
            val emptySessionId = activeSessionId
            ScriptPluginAgentRuntime.remove(emptySessionId)
            Thread({
                runCatching {
                    ScriptPluginAgentSessionStore.discardEmptySession(context, emptySessionId)
                }.onFailure { error ->
                    h.Hchat.utils.HLog.e(
                        "[Hchat:ScriptAgent] 清理空会话失败: $emptySessionId",
                        error
                    )
                }
            }, "Hchat-Agent-Empty-Session-Delete").start()
            return
        }
        ScriptPluginAgentSessionStore.revive(activeSessionId)
        ScriptPluginAgentSessionStore.saveAsync(context, renderedSessionState.snapshot())
        historyVersion++
    }

    fun openSession(session: ScriptPluginAgentSession, saveCurrent: Boolean = true) {
        if (saveCurrent) {
            if (!generating) {
                resumeState = resumeState?.copy(autoOpen = false, updatedAt = System.currentTimeMillis())
            }
            saveCurrentSession()
        }
        activeSessionState = ScriptPluginAgentRuntime.stateFor(session)
        showConfig = false
        showModelPicker = false
        showQuickProfilePicker = false
        showQuickOptions = false
        showHistory = false
    }

    fun newSession(saveCurrent: Boolean = true) {
        if (saveCurrent) {
            if (!generating) {
                resumeState = resumeState?.copy(autoOpen = false, updatedAt = System.currentTimeMillis())
            }
            saveCurrentSession()
        }
        activeSessionState = ScriptPluginAgentRuntime.createSession()
        showConfig = false
        showModelPicker = false
        showQuickProfilePicker = false
        showQuickOptions = false
        showHistory = false
    }

    fun currentConfig(): ScriptPluginAgentConfig {
        return ScriptPluginAgentConfig(
            apiBaseUrl = ScriptPluginAgentSettings.normalizedApiAddress(apiBase, endpointMode),
            apiPath = "",
            apiKey = apiKey.trim(),
            model = model.trim(),
            mcpServers = mcpServers,
            autoCompactEnabled = autoCompactEnabled,
            compactTokenThreshold = compactTokenThreshold.toIntOrNull()
                ?.coerceIn(2_000, 1_000_000)
                ?: ScriptPluginAgentSettings.DEFAULT_COMPACT_TOKEN_THRESHOLD,
            webSearchEnabled = webSearchEnabled,
            workspaceWriteApprovalMode = workspaceWriteApprovalMode,
            promptCacheMode = promptCacheMode,
            endpointMode = endpointMode
        )
    }

    fun endpointError(config: ScriptPluginAgentConfig): String {
        if (ScriptPluginAgentSettings.isValidRequestUrl(config)) return ""
        return if (config.endpointMode == ScriptPluginAgentSettings.ENDPOINT_MODE_CUSTOM_URL) {
            "请填写完整的 HTTP(S) 请求链接"
        } else {
            "API 地址无效"
        }
    }

    fun applyProfile(profile: ScriptPluginAgentProfile) {
        activeProfileId = profile.id
        activeProfileName = profile.name
        apiBase = profile.config.apiBaseUrl
        endpointMode = profile.config.endpointMode
        apiKey = profile.config.apiKey
        model = profile.config.model
        mcpServers = profile.config.mcpServers
        autoCompactEnabled = profile.config.autoCompactEnabled
        webSearchEnabled = profile.config.webSearchEnabled
        workspaceWriteApprovalMode = profile.config.workspaceWriteApprovalMode
        promptCacheMode = profile.config.promptCacheMode
        compactTokenThreshold = profile.config.compactTokenThreshold.toString()
    }

    fun selectAttachments() {
        if (applyingFileOperation) return
        val activity = context as? Activity
        if (activity == null) {
            Toast.makeText(context, "当前页面无法打开文件选择器", Toast.LENGTH_SHORT).show()
            return
        }
        ScriptPluginAgentAttachmentPickerBridge.launch(
            activity,
            ScriptPluginAgentSessionStore.attachmentDir(context, activeSessionId)
        ) { selected ->
            val retained = (pendingAttachments + selected)
                .distinctBy { it.sourceUri.ifBlank { it.path } }
                .take(12)
            val retainedPaths = retained.mapTo(HashSet()) { it.path }
            ScriptPluginAgentSessionStore.cleanupAttachments(
                context,
                selected.filterNot { it.path in retainedPaths }
            )
            pendingAttachments = retained
        }
    }

    fun cancelGeneration() {
        val cancellation = activeCancellation ?: return
        val cancelledTurnId = activeGenerationId
        pendingToolApproval?.resolve(ScriptPluginAgentWorkspaceWriteDecision.CANCEL)
        pendingToolApproval = null
        showToolApprovalDialog = false
        cancellation.cancel()
        resumeState = resumeState?.copy(autoOpen = false, updatedAt = System.currentTimeMillis())
        val indices = if (activeAssistantIndex in sessionMessages.indices) {
            setOf(activeAssistantIndex)
        } else {
            sessionMessages.indices.filter { sessionMessages[it].turnId == activeGenerationId }.toSet()
        }
        if (indices.isNotEmpty()) {
            val interruptedAt = System.currentTimeMillis()
            sessionMessages = sessionMessages.toMutableList().also { messages ->
                indices.forEach { index ->
                    val current = messages[index]
                    messages[index] = current.copy(
                        status = "interrupted",
                        completedAt = interruptedAt,
                        progress = mergeScriptPluginAgentProgress(current.progress, "已中断"),
                        toolEvents = current.toolEvents.map { event ->
                            if (event.status == "running" || event.status == "queued") {
                                event.copy(
                                    status = "interrupted",
                                    progress = "已中断",
                                    finishedAt = interruptedAt
                                )
                            } else {
                                event
                            }
                        }
                    )
                }
            }
            nativeToolHistory = ScriptPluginAgentToolResultStore.rebuildNativeToolHistory(
                context,
                ScriptPluginAgentContext.modelMessagesForTurn(
                    sessionMessages.drop(compactedMessageCount.coerceIn(0, sessionMessages.size)),
                    cancelledTurnId
                )
            )
        }
        checkpointSaver.schedule(force = true)
        activeCancellation = null
        activeGenerationId = ""
        activeAssistantIndex = -1
        generating = false
        contextCompacting = false
        contextCompactionStartedAt = 0L
        generationStartedAt = 0L
        generationHasVisibleReply = false
    }

    fun recordFileOperation(messageIndex: Int, progress: String) {
        val current = sessionMessages.getOrNull(messageIndex) ?: return
        sessionMessages = sessionMessages.toMutableList().also {
            it[messageIndex] = current.copy(
                progress = mergeScriptPluginAgentProgress(current.progress, progress),
                status = "complete"
            )
        }
        saveCurrentSession()
    }

    fun appendWorkspaceStatusMessage(
        messageIndex: Int,
        content: String,
        progress: String = "",
        draftSnapshot: ScriptPluginAgentDraft? = null,
        clearsDraft: Boolean = false
    ) {
        val source = sessionMessages.getOrNull(messageIndex) ?: return
        val now = System.currentTimeMillis()
        sessionMessages = sessionMessages + ScriptPluginAgentChatMessage(
            role = "assistant",
            content = content,
            turnId = source.turnId,
            parentMessageId = source.id,
            phase = "workspace_status",
            progress = progress,
            status = "complete",
            draftSnapshot = draftSnapshot,
            clearsDraft = clearsDraft,
            createdAt = now,
            completedAt = now
        )
        saveCurrentSession()
    }

    fun applyWorkspaceChange(messageIndex: Int, change: ScriptPluginAgentWorkspaceChange) {
        if (applyingFileOperation) return
        applyingFileOperation = true
        recordFileOperation(
            messageIndex,
            if (change.deletePlugin) "正在删除插件: ${change.pluginId}" else "正在提交插件工作区变更"
        )
        Thread({
            val result = ScriptPluginAgentWorkspaceTools.apply(context, change)
            Handler(Looper.getMainLooper()).post {
                applyingFileOperation = false
                result.onSuccess {
                    resumeState = null
                    val nextDraft = change.draft
                    val clearsCurrentDraft = change.deletePlugin &&
                        (sessionDraft?.pluginId.equals(change.pluginId, ignoreCase = true) ||
                            targetPluginId.equals(change.pluginId, ignoreCase = true))
                    if (clearsCurrentDraft) {
                        sessionDraft = null
                        targetPluginId = ""
                    } else if (nextDraft != null) {
                        sessionDraft = nextDraft
                        targetPluginId = nextDraft.pluginId
                    }
                    appendWorkspaceStatusMessage(
                        messageIndex = messageIndex,
                        content = when {
                            change.deletePlugin -> "插件已删除。"
                            change.existed -> "插件修改已提交到真实插件目录。"
                            else -> "插件已创建并写入真实插件目录。"
                        },
                        progress = when {
                            change.deletePlugin -> "已删除插件: ${change.pluginId}"
                            change.existed -> "已更新插件: ${change.pluginId}"
                            else -> "已创建插件: ${change.pluginId}"
                        },
                        draftSnapshot = sessionDraft,
                        clearsDraft = clearsCurrentDraft
                    )
                    Toast.makeText(
                        context,
                        when {
                            change.deletePlugin -> "插件已删除"
                            change.existed -> "插件已更新，当前为禁用状态"
                            else -> "插件已创建，当前为禁用状态"
                        },
                        Toast.LENGTH_LONG
                    ).show()
                }.onFailure { error ->
                    val reason = error.message ?: "未知错误"
                    pendingWorkspaceChange = ScriptPluginAgentPendingWorkspaceChange(
                        messageIndex = messageIndex,
                        change = change,
                        isNewPlugin = !change.existed,
                        hasDeletedPaths = change.deletePlugin || change.deletedPaths.isNotEmpty(),
                        risky = change.warnings.any { it.risky },
                        applyError = reason
                    )
                    showPendingWorkspaceDialog = true
                    appendWorkspaceStatusMessage(
                        messageIndex = messageIndex,
                        content = "插件修改尚未提交：$reason",
                        progress = "提交失败，暂存变更已保留，可直接重试"
                    )
                    Toast.makeText(context, "$reason\n暂存修改已保留，可直接重试", Toast.LENGTH_LONG).show()
                }
            }
        }, "Hchat-Script-Agent-Workspace-Apply").start()
    }

    fun prepareWorkspaceChange(
        messageIndex: Int,
        change: ScriptPluginAgentWorkspaceChange,
        requireConfirmation: Boolean
    ) {
        appendWorkspaceStatusMessage(
            messageIndex = messageIndex,
            content = if (change.deletePlugin) {
                "插件删除操作已暂存，尚未提交。"
            } else {
                "插件修改已暂存，尚未写入真实插件目录。"
            }
        )
        if (!requireConfirmation) {
            applyWorkspaceChange(messageIndex, change)
            return
        }
        val isNewPlugin = !change.existed
        val hasDeletedPaths = change.deletePlugin || change.deletedPaths.isNotEmpty()
        val risky = change.warnings.any { it.risky }
        pendingWorkspaceChange = ScriptPluginAgentPendingWorkspaceChange(
            messageIndex = messageIndex,
            change = change,
            isNewPlugin = isNewPlugin,
            hasDeletedPaths = hasDeletedPaths,
            risky = risky
        )
        showPendingWorkspaceDialog = true
        recordFileOperation(
            messageIndex,
            when {
                change.deletePlugin -> "等待确认删除插件"
                isNewPlugin && risky -> "等待确认创建插件和高风险代码"
                isNewPlugin -> "等待确认创建插件"
                hasDeletedPaths && risky -> "等待确认删除路径和高风险代码"
                hasDeletedPaths -> "等待确认删除插件路径"
                risky -> "等待确认高风险修改"
                else -> "等待确认插件修改"
            }
        )
    }

    fun writeAgentDraft(messageIndex: Int, draft: ScriptPluginAgentDraft, isNewPlugin: Boolean) {
        if (applyingFileOperation) return
        applyingFileOperation = true
        recordFileOperation(messageIndex, if (isNewPlugin) "正在创建插件" else "正在写入插件修改")
        Thread({
            val result = ScriptPluginAgentWriter.save(context, draft, overwrite = !isNewPlugin)
            Handler(Looper.getMainLooper()).post {
                applyingFileOperation = false
                result.onSuccess {
                    resumeState = null
                    sessionDraft = draft
                    targetPluginId = draft.pluginId
                    val current = sessionMessages.getOrNull(messageIndex)
                    if (current != null) {
                        sessionMessages = sessionMessages.toMutableList().also { messages ->
                            messages[messageIndex] = current.copy(
                                progress = mergeScriptPluginAgentProgress(
                                    current.progress,
                                    if (isNewPlugin) "已创建插件: ${draft.pluginId}" else "已更新插件: ${draft.pluginId}"
                                ),
                                status = "complete",
                                draftSnapshot = draft,
                                clearsDraft = false
                            )
                        }
                    }
                    saveCurrentSession()
                    Toast.makeText(
                        context,
                        if (isNewPlugin) "插件已创建，当前为禁用状态" else "插件已更新，当前为禁用状态",
                        Toast.LENGTH_LONG
                    ).show()
                }.onFailure { error ->
                    recordFileOperation(messageIndex, "写入失败: ${error.message ?: "未知错误"}")
                    Toast.makeText(context, error.message ?: "写入插件失败", Toast.LENGTH_LONG).show()
                }
            }
        }, "Hchat-Script-Agent-Write").start()
    }

    fun prepareAgentDraft(
        messageIndex: Int,
        draft: ScriptPluginAgentDraft,
        modifyingExisting: Boolean,
        requireConfirmation: Boolean
    ) {
        val validation = ScriptPluginAgentValidator.validate(draft)
        if (!validation.canSave) {
            val errors = validation.errors.joinToString("；") { it.message }
            recordFileOperation(messageIndex, "静态检查未通过，未写入: $errors")
            Toast.makeText(context, errors, Toast.LENGTH_LONG).show()
            return
        }
        val isNewPlugin = !File(ScriptPluginRuntime.scriptDir(context), draft.pluginId).exists()
        val confirmsCreation = isNewPlugin || !modifyingExisting
        val risky = validation.warnings.any { it.risky }
        if (!requireConfirmation) {
            writeAgentDraft(messageIndex, draft, isNewPlugin)
            return
        }
        pendingWrite = ScriptPluginAgentPendingWrite(
            messageIndex = messageIndex,
            draft = draft,
            diff = sessionMessages.getOrNull(messageIndex)?.diff.orEmpty(),
            isNewPlugin = isNewPlugin,
            confirmsCreation = confirmsCreation,
            risky = risky
        )
        showPendingWriteDialog = true
        recordFileOperation(
            messageIndex,
            when {
                isNewPlugin && risky -> "等待确认创建插件和高风险代码"
                confirmsCreation && risky -> "等待确认替换同名插件和高风险代码"
                isNewPlugin -> "等待确认创建插件"
                confirmsCreation -> "等待确认替换同名插件"
                risky -> "等待确认高风险代码"
                else -> "等待确认插件修改"
            }
        )
    }

    fun deleteAgentPlugin(messageIndex: Int, pluginId: String) {
        if (applyingFileOperation) return
        applyingFileOperation = true
        recordFileOperation(messageIndex, "正在删除插件: $pluginId")
        Thread({
            val result = ScriptPluginAgentWriter.delete(context, pluginId)
            Handler(Looper.getMainLooper()).post {
                applyingFileOperation = false
                result.onSuccess {
                    resumeState = null
                    val clearsCurrentDraft = sessionDraft?.pluginId.equals(pluginId, ignoreCase = true) ||
                        targetPluginId.equals(pluginId, ignoreCase = true)
                    if (clearsCurrentDraft) {
                        sessionDraft = null
                        targetPluginId = ""
                    }
                    val current = sessionMessages.getOrNull(messageIndex)
                    if (current != null) {
                        sessionMessages = sessionMessages.toMutableList().also { messages ->
                            messages[messageIndex] = current.copy(
                                progress = mergeScriptPluginAgentProgress(current.progress, "已删除插件: $pluginId"),
                                status = "complete",
                                draftSnapshot = sessionDraft,
                                clearsDraft = clearsCurrentDraft
                            )
                        }
                    }
                    saveCurrentSession()
                    Toast.makeText(context, "插件已删除", Toast.LENGTH_LONG).show()
                }.onFailure { error ->
                    recordFileOperation(messageIndex, "删除失败: ${error.message ?: "未知错误"}")
                    Toast.makeText(context, error.message ?: "删除插件失败", Toast.LENGTH_LONG).show()
                }
            }
        }, "Hchat-Script-Agent-Delete").start()
    }

    fun preparePluginDelete(messageIndex: Int, requestedId: String, requireConfirmation: Boolean) {
        val plugin = ScriptPluginRuntime.listPlugins(context).firstOrNull {
            it.id.equals(requestedId.trim(), ignoreCase = true)
        }
        if (plugin == null) {
            recordFileOperation(messageIndex, "未找到插件目录，未执行删除")
            Toast.makeText(context, "未找到要删除的插件", Toast.LENGTH_LONG).show()
            return
        }
        if (!requireConfirmation) {
            deleteAgentPlugin(messageIndex, plugin.id)
            return
        }
        pendingDelete = ScriptPluginAgentPendingDelete(
            messageIndex = messageIndex,
            pluginId = plugin.id,
            pluginName = plugin.displayName ?: plugin.name
        )
        showPendingDeleteDialog = true
        recordFileOperation(messageIndex, "等待确认删除插件: ${plugin.id}")
    }

    fun discardResumeWorkspace(state: ScriptPluginAgentResumeState? = resumeState) {
        val checkpoint = state?.workspaceCheckpoint ?: return
        Thread({
            runCatching { ScriptPluginAgentWorkspaceTools.discard(context, checkpoint) }
                .onFailure { error ->
                    h.Hchat.utils.HLog.e("[Hchat:ScriptAgent] 清理恢复工作区失败", error)
                }
        }, "Hchat-Script-Agent-Workspace-Discard").start()
    }

    fun sendMessage(
        content: String = messageInput,
        attachments: List<ScriptPluginAgentAttachment> = pendingAttachments,
        quotedMessage: ScriptPluginAgentQuotedMessage? = pendingQuotedMessage,
        consumeComposer: Boolean = true,
        resumeFrom: ScriptPluginAgentResumeState? = null
    ): Boolean {
        if (showPendingConfirmation()) return false
        if (generating || contextCompacting || applyingFileOperation) return false
        val message = content.trim()
        if (message.isBlank() && attachments.isEmpty()) {
            Toast.makeText(context, "请输入消息", Toast.LENGTH_SHORT).show()
            return false
        }
        val config = currentConfig()
        if (config.apiBaseUrl.isBlank() || config.model.isBlank()) {
            showConfig = true
            Toast.makeText(context, "请先完成 Agent 配置", Toast.LENGTH_SHORT).show()
            return false
        }
        endpointError(config).takeIf { it.isNotBlank() }?.let { error ->
            showConfig = true
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            return false
        }
        if (config.mcpServers.any { it.enabled && it.endpoint.isBlank() }) {
            showConfig = true
            Toast.makeText(context, "请填写已启用 MCP 的 Endpoint", Toast.LENGTH_SHORT).show()
            return false
        }
        ScriptPluginAgentSettings.save(context, config)
        val startedAt = System.currentTimeMillis()
        val resumeSource = resumeFrom?.let { checkpoint ->
            sessionMessages.lastOrNull { it.role == "user" && it.id == checkpoint.sourceUserMessageId }
                ?: sessionMessages.lastOrNull { it.role == "user" && it.turnId == checkpoint.turnId }
        }
        if (resumeFrom != null && resumeSource == null) {
            Toast.makeText(context, "原任务记录已不存在，无法继续", Toast.LENGTH_LONG).show()
            return false
        }
        val runId = resumeFrom?.turnId?.takeIf { it.isNotBlank() }
            ?: java.util.UUID.randomUUID().toString()
        val userMessageId = resumeSource?.id ?: java.util.UUID.randomUUID().toString()
        val nextMessages = if (resumeSource != null) {
            sessionMessages
        } else {
            sessionMessages + ScriptPluginAgentChatMessage(
                role = "user",
                content = message.ifBlank { "请分析附件内容" },
                id = userMessageId,
                turnId = runId,
                phase = "user",
                attachments = attachments,
                quotedMessage = quotedMessage,
                createdAt = startedAt,
                completedAt = startedAt
            )
        }
        if (sessionMessages.isEmpty()) sessionTitle = ScriptPluginAgentSessionStore.titleFrom(nextMessages)
        val assistantIndex = nextMessages.size
        sessionMessages = nextMessages + ScriptPluginAgentChatMessage(
            role = "assistant",
            content = "",
            turnId = runId,
            parentMessageId = userMessageId,
            phase = "assistant",
            progress = "正在连接模型",
            status = "streaming",
            createdAt = startedAt
        )
        if (resumeFrom == null) discardResumeWorkspace()
        resumeState = resumeFrom?.copy(
            turnId = runId,
            sourceUserMessageId = userMessageId,
            autoOpen = true,
            updatedAt = startedAt
        ) ?: ScriptPluginAgentResumeState(
            turnId = runId,
            sourceUserMessageId = userMessageId,
            startedAt = startedAt,
            updatedAt = startedAt
        )
        if (consumeComposer) {
            messageInput = ""
            pendingAttachments = emptyList()
            pendingQuotedMessage = null
        }
        saveCurrentSession()
        val previousSummary = conversationSummary
        val previousProtocolTranscript = protocolTranscript
        val previousCompactedCount = compactedMessageCount.coerceIn(0, nextMessages.size)
        val workspaceBelongsToSession = workspaceSessionId == activeSessionId
        val draftSnapshot = sessionDraft.takeIf { workspaceBelongsToSession }
        val targetSnapshot = targetPluginId.takeIf { workspaceBelongsToSession }.orEmpty()
        val cancellation = ScriptPluginAgentCancellation()
        val liveAssistantIndex = AtomicInteger(assistantIndex)
        activeGenerationId = runId
        activeAssistantIndex = assistantIndex
        activeCancellation = cancellation
        generationStartedAt = startedAt
        generationHasVisibleReply = false
        generating = true
        Thread({
            var requestSummary = previousSummary
            var requestCompactedCount = previousCompactedCount
            var requestProtocolTranscript = previousProtocolTranscript
            var compactProgress = ""
            var compactApplied = false
            fun modelMessagesFrom(startIndex: Int): List<ScriptPluginAgentChatMessage> {
                val modeled = ScriptPluginAgentContext.modelMessagesForTurn(
                    nextMessages.drop(startIndex),
                    runId
                ).filterNot { message ->
                    resumeSource != null && message.role == "assistant" && message.turnId == runId &&
                        (message.status == "error" || message.status == "interrupted")
                }
                val source = resumeSource ?: return modeled
                return if (modeled.any { it.id == source.id }) modeled else listOf(source) + modeled
            }
            val activeMessages = modelMessagesFrom(requestCompactedCount)
            var requestNativeToolHistory = ScriptPluginAgentToolResultStore.rebuildNativeToolHistory(
                context,
                activeMessages
            )
            val estimatedTokensBeforeCompact = ScriptPluginAgentContext.estimateTokens(
                requestSummary,
                activeMessages,
                draftSnapshot,
                requestNativeToolHistory,
                requestProtocolTranscript
            )
            val shouldCompact = resumeFrom == null && config.autoCompactEnabled &&
                estimatedTokensBeforeCompact >= config.compactTokenThreshold
            if (shouldCompact) {
                val compactEnd = (nextMessages.size - 1).coerceAtLeast(requestCompactedCount)
                if (compactEnd > requestCompactedCount) {
                    Handler(Looper.getMainLooper()).post {
                        if (activeGenerationId == runId) {
                            contextCompacting = true
                            contextCompactionStartedAt = System.currentTimeMillis()
                        }
                    }
                    val compactResult = ScriptPluginAgentClient.compact(
                        config = config,
                        previousSummary = requestSummary,
                        messages = nextMessages.subList(requestCompactedCount, compactEnd),
                        currentDraft = draftSnapshot,
                        targetPluginId = targetSnapshot,
                        cancellation = cancellation
                    )
                    compactResult.onSuccess { summary ->
                        val estimatedTokensAfterCompact = ScriptPluginAgentContext.estimateTokens(
                            summary,
                            nextMessages.drop(compactEnd),
                            draftSnapshot,
                            ""
                        )
                        if (estimatedTokensAfterCompact < estimatedTokensBeforeCompact) {
                            requestSummary = summary
                            requestCompactedCount = compactEnd
                            requestNativeToolHistory = ""
                            requestProtocolTranscript = ""
                            compactApplied = true
                            compactProgress = "已自动压缩上下文：$estimatedTokensBeforeCompact → " +
                                "$estimatedTokensAfterCompact Token"
                        } else {
                            compactProgress = "自动压缩未减少上下文，已保留原上下文"
                        }
                    }.onFailure {
                        compactProgress = "自动压缩失败，已保留原上下文"
                    }
                    Handler(Looper.getMainLooper()).post {
                        if (activeGenerationId == runId) {
                            contextCompacting = false
                            contextCompactionStartedAt = 0L
                            if (!cancellation.isCancelled) {
                                Toast.makeText(
                                    context,
                                    if (compactResult.isSuccess) compactProgress
                                    else "自动压缩失败，已使用原上下文",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
            if (cancellation.isCancelled) return@Thread
            val requestMessages = modelMessagesFrom(requestCompactedCount)
            requestNativeToolHistory = ScriptPluginAgentToolResultStore.rebuildNativeToolHistory(
                context,
                requestMessages
            )
            val request = ScriptPluginAgentRequest(
                existing = null,
                messages = requestMessages,
                currentDraft = draftSnapshot,
                targetPluginId = targetSnapshot,
                conversationSummary = requestSummary,
                compactedMessageCount = requestCompactedCount,
                nativeToolHistory = requestNativeToolHistory,
                protocolTranscript = requestProtocolTranscript,
                lockedTaskGoal = resumeFrom?.taskGoal.orEmpty(),
                agentWorkContext = resumeFrom?.workContext.orEmpty().let { previous ->
                    if (resumeFrom == null) previous else listOf(
                        previous,
                        if (resumeFrom.workspaceCheckpoint != null) {
                            "上一轮任务意外中断。客户端将校验并恢复已保存的插件暂存工作区；继续同一目标和执行位置，不得重新执行已成功的工具调用或写入。"
                        } else {
                            "上一轮任务意外中断。继续同一目标并复用已完成的工具结果，不得自动重放已成功的 MCP 或未知副作用操作。"
                        }
                    ).filter { it.isNotBlank() }.joinToString("\n").takeLast(16_000)
                },
                workspaceCheckpoint = resumeFrom?.workspaceCheckpoint,
                sessionId = activeSessionId,
                turnId = runId,
                allowedLocalPaths = ScriptPluginAgentLocalFiles.extractMentionedPaths(
                    nextMessages.asSequence()
                        .filter { it.role == "user" }
                        .joinToString("\n") { it.content }
                ).map { it.path }
            )
            val result = ScriptPluginAgentSessionStore.withMaterializedAttachments(
                context,
                request.messages.flatMap { it.attachments }
            ) {
                ScriptPluginAgentClient.generate(
                    context,
                    config,
                    request,
                    cancellation,
                    { confirmation ->
                        val approval = ScriptPluginAgentWorkspaceToolApproval(confirmation)
                        Handler(Looper.getMainLooper()).post {
                            if (activeGenerationId == runId) {
                                pendingToolApproval = approval
                                showToolApprovalDialog = true
                            } else {
                                approval.resolve(ScriptPluginAgentWorkspaceWriteDecision.CANCEL)
                            }
                        }
                        try {
                            approval.await(cancellation)
                        } finally {
                            Handler(Looper.getMainLooper()).post {
                                if (pendingToolApproval === approval) {
                                    pendingToolApproval = null
                                    showToolApprovalDialog = false
                                }
                            }
                        }
                    }
                ) { update ->
                    Handler(Looper.getMainLooper()).post {
                        if (activeGenerationId != runId) return@post
                        when (update.phase) {
                        "checkpoint" -> {
                            update.resumeState?.let { resumeState = it }
                            update.checkpointNativeToolHistory?.let { nativeToolHistory = it }
                            update.checkpointProtocolTranscript?.let { protocolTranscript = it }
                            update.checkpointConversationSummary?.let { conversationSummary = it }
                            update.checkpointCompactedMessageCount?.let {
                                compactedMessageCount = it.coerceIn(0, sessionMessages.size)
                            }
                            checkpointSaver.schedule(force = true)
                        }

                        "protocol_checkpoint" -> {
                            update.checkpointProtocolTranscript?.let { protocolTranscript = it }
                            checkpointSaver.schedule(force = true)
                        }

                        "assistant_start" -> {
                            val streamId = update.streamId
                            var index = if (streamId.isNotBlank()) {
                                sessionMessages.indexOfLast { message ->
                                    message.role == "assistant" && message.streamId == streamId
                                }
                            } else {
                                liveAssistantIndex.get()
                            }
                            if (index !in sessionMessages.indices) {
                                val placeholderIndex = liveAssistantIndex.get()
                                val placeholder = sessionMessages.getOrNull(placeholderIndex)
                                val canAdoptPlaceholder = placeholderIndex == sessionMessages.lastIndex &&
                                    placeholder?.role == "assistant" &&
                                    placeholder.status == "streaming" &&
                                    placeholder.streamId.isBlank() &&
                                    placeholder.content.isBlank() &&
                                    placeholder.reasoning.isBlank() &&
                                    placeholder.toolEvents.isEmpty()
                                if (canAdoptPlaceholder) {
                                    index = placeholderIndex
                                } else {
                                    val previousIndex = liveAssistantIndex.get()
                                    val previous = sessionMessages.getOrNull(previousIndex)
                                    if (previous != null && previous.role == "assistant" && previous.status == "streaming") {
                                        sessionMessages = sessionMessages.toMutableList().also { messages ->
                                            messages[previousIndex] = previous.copy(
                                                status = "complete",
                                                completedAt = System.currentTimeMillis()
                                            )
                                        }
                                    }
                                }
                            }
                            if (index !in sessionMessages.indices) {
                                sessionMessages = sessionMessages.toMutableList().also { messages ->
                                    messages += ScriptPluginAgentChatMessage(
                                        role = "assistant",
                                        content = "",
                                        turnId = update.turnId,
                                        parentMessageId = update.parentMessageId,
                                        phase = "assistant",
                                        status = "streaming",
                                        streamId = streamId
                                    )
                                }
                                index = sessionMessages.lastIndex
                            }
                            liveAssistantIndex.set(index)
                            activeAssistantIndex = index
                            val assistant = sessionMessages[index]
                            val next = assistant.copy(
                                id = streamId.ifBlank { assistant.id },
                                turnId = update.turnId.ifBlank { assistant.turnId },
                                parentMessageId = update.parentMessageId.ifBlank { assistant.parentMessageId },
                                phase = "assistant",
                                status = "streaming",
                                streamId = streamId.ifBlank { assistant.streamId }
                            )
                            if (next != assistant) sessionMessages = sessionMessages.toMutableList().also {
                                it[index] = next
                            }
                            checkpointSaver.schedule()
                        }

                        "assistant_reset" -> {
                            val index = if (update.streamId.isNotBlank()) {
                                sessionMessages.indexOfLast { message ->
                                    message.role == "assistant" && message.streamId == update.streamId
                                }
                            } else {
                                liveAssistantIndex.get()
                            }
                            val current = sessionMessages.getOrNull(index)
                            if (current != null && current.role == "assistant") {
                                sessionMessages = sessionMessages.toMutableList().also { messages ->
                                    messages.removeAt(index)
                                }
                                liveAssistantIndex.set(-1)
                                activeAssistantIndex = -1
                                checkpointSaver.schedule()
                            }
                        }

                        "working" -> {
                            // Working is a transport state. Keep it out of the assistant message body.
                        }

                        "tool_start", "tool_update" -> {
                            val event = update.toolEvents?.firstOrNull() ?: return@post
                            if (update.phase == "tool_start") {
                                val index = if (update.streamId.isNotBlank()) {
                                    sessionMessages.indexOfLast { message ->
                                        message.role == "assistant" && message.streamId == update.streamId
                                    }
                                } else {
                                    liveAssistantIndex.get()
                                }
                                val current = sessionMessages.getOrNull(index)
                                if (current != null && current.role == "assistant") {
                                    sessionMessages = sessionMessages.toMutableList().also { messages ->
                                        if (index == messages.lastIndex &&
                                            (current.content.isBlank() ||
                                                isScriptPluginAgentControlPreview(current.content)) &&
                                            current.diff.isBlank()
                                        ) {
                                            messages[index] = current.copy(
                                                content = "",
                                                status = "complete",
                                                phase = "assistant_tool_call",
                                                completedAt = System.currentTimeMillis()
                                            )
                                        } else {
                                            messages[index] = current.copy(
                                                status = "complete",
                                                completedAt = System.currentTimeMillis()
                                            )
                                        }
                                    }
                                }
                                liveAssistantIndex.set(-1)
                            }
                            val existingToolIndex = sessionMessages.indexOfLast { message ->
                                message.role == "tool" &&
                                    (message.toolEvents.any { it.id == update.toolEventId } ||
                                        (event.turnId.isNotBlank() &&
                                            message.turnId == event.turnId &&
                                            message.parentMessageId == event.parentAssistantMessageId))
                            }
                            val toolIndex = if (existingToolIndex >= 0) {
                                sessionMessages = sessionMessages.toMutableList().also { messages ->
                                    val current = messages[existingToolIndex]
                                    val nextEvents = current.toolEvents.toMutableList().apply {
                                        val eventIndex = indexOfFirst { it.id == event.id }
                                        if (eventIndex >= 0) set(eventIndex, event) else add(event)
                                    }
                                    val hasPending = nextEvents.any {
                                        it.status == "running" || it.status == "queued"
                                    }
                                    messages[existingToolIndex] = current.copy(
                                        turnId = event.turnId.ifBlank { current.turnId },
                                        parentMessageId = event.parentAssistantMessageId,
                                        phase = "tool",
                                        toolEvents = nextEvents,
                                        status = if (hasPending) {
                                            "streaming"
                                        } else {
                                            "complete"
                                        },
                                        completedAt = if (hasPending) 0L else nextEvents.maxOfOrNull {
                                            it.finishedAt
                                        } ?: event.finishedAt
                                    )
                                }
                                existingToolIndex
                            } else {
                                sessionMessages = sessionMessages + ScriptPluginAgentChatMessage(
                                    role = "tool",
                                    content = "",
                                    id = ScriptPluginAgentEventIds.toolGroup(
                                        event.turnId,
                                        event.parentAssistantMessageId
                                    ),
                                    turnId = event.turnId,
                                    parentMessageId = event.parentAssistantMessageId,
                                    phase = "tool",
                                    toolEvents = listOf(event),
                                    status = if (event.status == "running" || event.status == "queued") {
                                        "streaming"
                                    } else {
                                        "complete"
                                    },
                                    completedAt = event.finishedAt,
                                    streamId = update.streamId
                                )
                                sessionMessages.lastIndex
                            }
                            activeAssistantIndex = toolIndex
                            checkpointSaver.schedule(
                                force = event.status != "running" && event.status != "queued"
                            )
                        }

                        else -> {
                            if (update.reply.isNotBlank()) generationHasVisibleReply = true
                            val index = if (update.streamId.isNotBlank()) {
                                sessionMessages.indexOfLast { message ->
                                    message.role == "assistant" && message.streamId == update.streamId
                                }
                            } else {
                                liveAssistantIndex.get()
                            }
                            if (index !in sessionMessages.indices) return@post
                            val current = sessionMessages[index]
                            val next = current.copy(
                                content = if (update.replyRevision) {
                                    update.reply
                                } else {
                                    mergeScriptPluginAgentReply(current.content, update.reply)
                                },
                                reasoning = if (update.reasoningRevision) {
                                    update.reasoning
                                } else {
                                    ScriptPluginAgentTextMerge.mergeReply(current.reasoning, update.reasoning)
                                },
                                status = "streaming"
                            )
                            if (next != current) sessionMessages = sessionMessages.toMutableList().also {
                                it[index] = next
                            }
                            checkpointSaver.schedule()
                        }
                        }
                    }
                }
            }
            Handler(Looper.getMainLooper()).post {
                if (activeGenerationId != runId) return@post
                generating = false
                contextCompacting = false
                contextCompactionStartedAt = 0L
                generationStartedAt = 0L
                generationHasVisibleReply = false
                activeCancellation = null
                activeGenerationId = ""
                activeAssistantIndex = -1
                pendingToolApproval = null
                showToolApprovalDialog = false
                conversationSummary = requestSummary
                compactedMessageCount = requestCompactedCount
                nativeToolHistory = requestNativeToolHistory
                if (compactApplied && protocolTranscript == previousProtocolTranscript) {
                    protocolTranscript = requestProtocolTranscript
                }
                result.onSuccess { turn ->
                    if (turn.protocolTranscript.isNotBlank()) {
                        protocolTranscript = turn.protocolTranscript
                    }
                    if (turn.nativeToolHistory.isNotBlank()) {
                        nativeToolHistory = turn.nativeToolHistory
                    } else {
                        nativeToolHistory = ScriptPluginAgentToolResultStore.mergeNativeToolHistory(
                            context,
                            requestNativeToolHistory,
                            ScriptPluginAgentContext.modelMessagesForTurn(
                                sessionMessages.drop(requestCompactedCount),
                                runId
                            )
                        )
                    }
                    var finalAssistantIndex = liveAssistantIndex.get()
                    val finalMessage = sessionMessages.getOrNull(finalAssistantIndex)
                    if (finalAssistantIndex !in sessionMessages.indices ||
                        finalMessage?.role != "assistant" ||
                        finalAssistantIndex != sessionMessages.lastIndex
                    ) {
                        sessionMessages = sessionMessages + ScriptPluginAgentChatMessage(
                            role = "assistant",
                            content = "",
                            turnId = runId,
                            parentMessageId = userMessageId,
                            phase = "assistant",
                            status = "streaming"
                        )
                        finalAssistantIndex = sessionMessages.lastIndex
                        liveAssistantIndex.set(finalAssistantIndex)
                    }
                    val previousDraft = sessionDraft
                    val nextDraft = turn.draft?.let { value ->
                        ScriptPluginAgentValidator.normalize(
                            if (turn.targetPluginId.isNotBlank()) value.copy(pluginId = turn.targetPluginId) else value
                        )
                    }
                    val actualDiff = nextDraft?.let { ScriptPluginAgentDiff.between(previousDraft, it) }.orEmpty()
                    val currentMessage = sessionMessages.getOrNull(finalAssistantIndex)
                    val displayedToolEventIds = sessionMessages.asSequence()
                        .flatMap { message -> message.toolEvents.asSequence() }
                        .map { event -> event.id }
                        .toSet()
                    val missingToolEvents = turn.toolEvents.filterNot { event ->
                        event.id in displayedToolEventIds
                    }
                    val completedMessage = (currentMessage ?: ScriptPluginAgentChatMessage("assistant", ""))
                        .copy(
                        content = mergeScriptPluginAgentReply(currentMessage?.content.orEmpty(), turn.reply),
                        progress = listOf(
                            compactProgress.takeIf { compactApplied }.orEmpty(),
                            turn.progress
                        ).fold(currentMessage?.progress.orEmpty()) {
                            current, next -> mergeScriptPluginAgentProgress(current, next)
                        },
                        diff = turn.diff.ifBlank { actualDiff },
                        toolEvents = (currentMessage?.toolEvents.orEmpty() + missingToolEvents)
                            .distinctBy { event -> event.id },
                        status = "complete",
                        completedAt = System.currentTimeMillis(),
                        phase = "assistant",
                        draftSnapshot = null,
                        clearsDraft = false
                    )
                    sessionMessages = sessionMessages.toMutableList().also {
                        if (finalAssistantIndex in it.indices) it[finalAssistantIndex] = completedMessage
                        else it += completedMessage
                    }
                    val awaitsFileOperation =
                        (turn.status.equals("workspace_ready", ignoreCase = true) && turn.workspaceChange != null) ||
                            turn.status.equals("delete", ignoreCase = true) ||
                            (turn.status.equals("ready", ignoreCase = true) && nextDraft != null)
                    if (!awaitsFileOperation) resumeState = null
                    if (turn.title.isNotBlank() && sessionMessages.count { it.role == "user" } <= 1) {
                        sessionTitle = turn.title.take(32)
                    }
                    when {
                        turn.status.equals("workspace_ready", ignoreCase = true) && turn.workspaceChange != null -> {
                            prepareWorkspaceChange(
                                finalAssistantIndex,
                                turn.workspaceChange,
                                workspaceWriteApprovalMode == ScriptPluginAgentSettings.WRITE_APPROVAL_ASK
                            )
                        }
                        turn.status.equals("delete", ignoreCase = true) -> {
                            preparePluginDelete(
                                finalAssistantIndex,
                                turn.targetPluginId,
                                workspaceWriteApprovalMode == ScriptPluginAgentSettings.WRITE_APPROVAL_ASK
                            )
                        }
                        turn.status.equals("ready", ignoreCase = true) && nextDraft != null -> {
                            val modifyingExisting = turn.targetPluginId.isNotBlank() ||
                                sessionDraft?.pluginId.equals(nextDraft.pluginId, ignoreCase = true)
                            prepareAgentDraft(
                                finalAssistantIndex,
                                nextDraft,
                                modifyingExisting,
                                workspaceWriteApprovalMode == ScriptPluginAgentSettings.WRITE_APPROVAL_ASK
                            )
                        }
                        else -> {
                            if (turn.targetPluginId.isNotBlank()) targetPluginId = turn.targetPluginId
                            saveCurrentSession()
                        }
                    }
                }.onFailure { error ->
                    var failureIndex = liveAssistantIndex.get()
                    val failureMessage = sessionMessages.getOrNull(failureIndex)
                    if (failureIndex !in sessionMessages.indices ||
                        failureMessage?.role != "assistant" ||
                        failureIndex != sessionMessages.lastIndex
                    ) {
                        sessionMessages = sessionMessages + ScriptPluginAgentChatMessage(
                            role = "assistant",
                            content = "",
                            turnId = runId,
                            parentMessageId = userMessageId,
                            phase = "assistant",
                            status = "error"
                        )
                        failureIndex = sessionMessages.lastIndex
                    }
                    val currentMessage = sessionMessages.getOrNull(failureIndex)
                    if (currentMessage != null) {
                        sessionMessages = sessionMessages.toMutableList().also { messages ->
                            messages[failureIndex] = currentMessage.copy(
                                status = if (cancellation.isCancellation(error)) "interrupted" else "error",
                                completedAt = System.currentTimeMillis(),
                                progress = mergeScriptPluginAgentProgress(
                                    currentMessage.progress,
                                    if (cancellation.isCancellation(error)) "已中断" else "请求失败"
                                )
                            )
                        }
                    }
                    nativeToolHistory = ScriptPluginAgentToolResultStore.mergeNativeToolHistory(
                        context,
                        requestNativeToolHistory,
                        ScriptPluginAgentContext.modelMessagesForTurn(
                            sessionMessages.drop(requestCompactedCount),
                            runId
                        )
                    )
                    saveCurrentSession()
                    if (!cancellation.isCancellation(error)) {
                        Toast.makeText(context, error.message ?: "请求失败", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }, "Hchat-Script-Agent-Chat").start()
        return true
    }

    fun queueMessage() {
        if (!generating || applyingFileOperation) return
        val message = messageInput.trim()
        if (message.isBlank() && pendingAttachments.isEmpty()) return
        pendingMessages = pendingMessages + ScriptPluginAgentPendingMessage(
            id = nextPendingMessageId++,
            content = message,
            attachments = pendingAttachments,
            quotedMessage = pendingQuotedMessage
        )
        pendingMessagesExpanded = true
        messageInput = ""
        pendingAttachments = emptyList()
        pendingQuotedMessage = null
        Toast.makeText(context, "已加入待发送队列", Toast.LENGTH_SHORT).show()
    }

    fun editPendingMessage(id: Long) {
        val pending = pendingMessages.firstOrNull { it.id == id } ?: return
        pendingMessages = pendingMessages.filterNot { it.id == id }
        messageInput = pending.content
        pendingAttachments = pending.attachments
        pendingQuotedMessage = pending.quotedMessage
    }

    fun sendPendingMessageNow(id: Long) {
        val pending = pendingMessages.firstOrNull { it.id == id } ?: return
        pendingMessages = listOf(pending) + pendingMessages.filterNot { it.id == id }
        if (generating) cancelGeneration()
    }

    fun compactContext() {
        if (generating || contextCompacting || applyingFileOperation) return
        if (showPendingConfirmation()) return
        val start = compactedMessageCount.coerceIn(0, sessionMessages.size)
        val messagesToCompact = sessionMessages.drop(start)
        if (messagesToCompact.isEmpty()) {
            Toast.makeText(context, "没有需要压缩的新上下文", Toast.LENGTH_SHORT).show()
            return
        }
        val config = currentConfig()
        val summarySnapshot = conversationSummary
        val protocolTranscriptSnapshot = protocolTranscript
        val draftSnapshot = sessionDraft
        val targetSnapshot = targetPluginId
        val compactedSize = sessionMessages.size
        val runId = "compact:${java.util.UUID.randomUUID()}"
        val cancellation = ScriptPluginAgentCancellation()
        contextCompacting = true
        contextCompactionStartedAt = System.currentTimeMillis()
        activeGenerationId = runId
        activeAssistantIndex = -1
        activeCancellation = cancellation
        Toast.makeText(context, "正在压缩上下文", Toast.LENGTH_SHORT).show()
        Thread({
            val nativeHistorySnapshot = ScriptPluginAgentToolResultStore.rebuildNativeToolHistory(
                context,
                messagesToCompact
            )
            val estimatedTokensBefore = ScriptPluginAgentContext.estimateTokens(
                summarySnapshot,
                messagesToCompact,
                draftSnapshot,
                nativeHistorySnapshot,
                protocolTranscriptSnapshot
            )
            val result = ScriptPluginAgentClient.compact(
                config = config,
                previousSummary = summarySnapshot,
                messages = messagesToCompact,
                currentDraft = draftSnapshot,
                targetPluginId = targetSnapshot,
                cancellation = cancellation
            )
            Handler(Looper.getMainLooper()).post {
                if (activeGenerationId != runId) return@post
                contextCompacting = false
                contextCompactionStartedAt = 0L
                activeGenerationId = ""
                activeAssistantIndex = -1
                activeCancellation = null
                result.onSuccess { summary ->
                    val estimatedTokensAfter = ScriptPluginAgentContext.estimateTokens(
                        summary,
                        emptyList(),
                        draftSnapshot,
                        ""
                    )
                    if (estimatedTokensAfter < estimatedTokensBefore) {
                        conversationSummary = summary
                        nativeToolHistory = ""
                        protocolTranscript = ""
                        compactedMessageCount = compactedSize.coerceAtMost(sessionMessages.size)
                        saveCurrentSession()
                        Toast.makeText(
                            context,
                            "上下文已压缩：$estimatedTokensBefore → $estimatedTokensAfter Token",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            context,
                            "压缩结果未减少上下文，已保留原上下文",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }.onFailure {
                    Toast.makeText(context, it.message ?: "上下文压缩失败", Toast.LENGTH_LONG).show()
                }
            }
        }, "Hchat-Script-Agent-Compact").start()
    }

    fun saveConfig() {
        val threshold = compactTokenThreshold.toIntOrNull()
        if (threshold == null || threshold !in 2_000..1_000_000) {
            Toast.makeText(context, "自动压缩阈值需为 2000 到 1000000", Toast.LENGTH_SHORT).show()
            return
        }
        val config = currentConfig()
        if (config.apiBaseUrl.isBlank() || config.model.isBlank()) {
            Toast.makeText(context, "API 地址和模型不能为空", Toast.LENGTH_SHORT).show()
            return
        }
        endpointError(config).takeIf { it.isNotBlank() }?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            return
        }
        if (config.mcpServers.any { it.enabled && it.endpoint.isBlank() }) {
            Toast.makeText(context, "已启用 MCP 的 Endpoint 不能为空", Toast.LENGTH_SHORT).show()
            return
        }
        ScriptPluginAgentSettings.save(context, config)
        apiBase = config.apiBaseUrl
        val activeProfile = ScriptPluginAgentSettings.loadActiveProfile(context)
        activeProfileId = activeProfile.id
        activeProfileName = activeProfile.name
        profileVersion++
        showConfig = false
        Toast.makeText(context, "Agent 配置已保存", Toast.LENGTH_SHORT).show()
    }

    fun selectProfile(profile: ScriptPluginAgentProfile) {
        if (profile.id == activeProfileId) return
        val appliesToNextRequest = generating
        ScriptPluginAgentSettings.save(context, currentConfig())
        ScriptPluginAgentSettings.setActiveProfile(context, profile.id)
        applyProfile(ScriptPluginAgentSettings.loadActiveProfile(context))
        profileVersion++
        Toast.makeText(
            context,
            if (appliesToNextRequest) "已切换配置，将用于下一次请求" else "已切换配置",
            Toast.LENGTH_SHORT
        ).show()
    }

    fun setWebSearchEnabled(enabled: Boolean) {
        val nextConfig = currentConfig().copy(webSearchEnabled = enabled)
        webSearchEnabled = enabled
        ScriptPluginAgentSettings.save(context, nextConfig)
        profileVersion++
    }

    fun setMcpServerEnabled(id: String, enabled: Boolean) {
        val nextServers = mcpServers.map { server ->
            if (server.id == id) server.copy(enabled = enabled) else server
        }
        mcpServers = nextServers
        ScriptPluginAgentSettings.save(context, currentConfig().copy(mcpServers = nextServers))
        profileVersion++
    }

    fun setWorkspaceWriteApprovalMode(mode: String) {
        workspaceWriteApprovalMode = mode
        ScriptPluginAgentSettings.save(
            context,
            currentConfig().copy(workspaceWriteApprovalMode = mode)
        )
        profileVersion++
    }

    fun setPromptCacheMode(mode: String) {
        promptCacheMode = mode
        ScriptPluginAgentSettings.save(
            context,
            currentConfig().copy(promptCacheMode = mode)
        )
        profileVersion++
    }

    fun createProfile(name: String) {
        runCatching {
            ScriptPluginAgentSettings.save(context, currentConfig())
            val created = ScriptPluginAgentSettings.createProfile(context, name, currentConfig())
            applyProfile(created)
            profileVersion++
        }.onFailure {
            Toast.makeText(context, it.message ?: "新建配置失败", Toast.LENGTH_SHORT).show()
        }
    }

    fun renameProfile(name: String) {
        runCatching {
            val renamed = ScriptPluginAgentSettings.renameProfile(context, activeProfileId, name)
            activeProfileName = renamed.name
            profileVersion++
        }.onFailure {
            Toast.makeText(context, it.message ?: "重命名失败", Toast.LENGTH_SHORT).show()
        }
    }

    fun deleteActiveProfile() {
        runCatching {
            ScriptPluginAgentSettings.save(context, currentConfig())
            val next = ScriptPluginAgentSettings.deleteProfile(context, activeProfileId)
            applyProfile(next)
            profileVersion++
        }.onFailure {
            Toast.makeText(context, it.message ?: "删除配置失败", Toast.LENGTH_SHORT).show()
        }
    }

    fun testAgentConnection() {
        if (testingConnection) return
        val config = currentConfig()
        if (config.apiBaseUrl.isBlank() || config.model.isBlank()) {
            Toast.makeText(context, "请先填写 API 地址和模型", Toast.LENGTH_SHORT).show()
            return
        }
        endpointError(config).takeIf { it.isNotBlank() }?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            return
        }
        testingConnection = true
        Toast.makeText(context, "正在测试连接", Toast.LENGTH_SHORT).show()
        Thread({
            val result = ScriptPluginAgentClient.testConnection(config)
            Handler(Looper.getMainLooper()).post {
                testingConnection = false
                result.onSuccess {
                    Toast.makeText(context, "连接成功", Toast.LENGTH_SHORT).show()
                }.onFailure {
                    Toast.makeText(context, it.message ?: "连接失败", Toast.LENGTH_LONG).show()
                }
            }
        }, "Hchat-Script-Agent-Test").start()
    }

    fun draftForMessages(messages: List<ScriptPluginAgentChatMessage>): ScriptPluginAgentDraft? {
        return messages.asReversed()
            .firstOrNull { it.draftSnapshot != null || it.clearsDraft }
            ?.draftSnapshot
    }

    fun rebuildNativeHistory(messages: List<ScriptPluginAgentChatMessage>): String {
        return ScriptPluginAgentToolResultStore.rebuildNativeToolHistory(context, messages)
    }

    fun deleteToolResults(messages: List<ScriptPluginAgentChatMessage>) {
        messages.asSequence()
            .flatMap { it.toolEvents.asSequence() }
            .map { it.resultHandle }
            .filter { it.isNotBlank() }
            .distinct()
            .forEach { ScriptPluginAgentToolResultStore.deleteHandle(context, it) }
    }

    fun prepareMessageHistoryChange(
        retainedMessages: List<ScriptPluginAgentChatMessage>,
        removedMessages: List<ScriptPluginAgentChatMessage>,
        actionName: String,
        onReady: (String) -> Boolean
    ) {
        val mutationState = renderedSessionState
        val sourceMessages = mutationState.messages.value
        mutationState.applyingFileOperation.value = true
        Thread({
            val historyResult = runCatching { rebuildNativeHistory(retainedMessages) }
            Handler(Looper.getMainLooper()).post {
                if (mutationState.messages.value !== sourceMessages) {
                    mutationState.applyingFileOperation.value = false
                    Toast.makeText(context, "会话内容已变化，请重试", Toast.LENGTH_SHORT).show()
                    return@post
                }
                historyResult.fold(
                    onSuccess = { restoredHistory ->
                        mutationState.applyingFileOperation.value = false
                        val applied = runCatching { onReady(restoredHistory) }
                            .onFailure { error ->
                                h.Hchat.utils.HLog.e(
                                    "[Hchat:ScriptAgent] ${actionName}失败",
                                    error
                                )
                                Toast.makeText(context, "${actionName}失败，请重试", Toast.LENGTH_SHORT).show()
                            }
                            .getOrDefault(false)
                        if (applied) {
                            val snapshot = mutationState.snapshot()
                            val sessionId = mutationState.id
                            Thread({
                                val saveResult = runCatching {
                                    if (ScriptPluginAgentSessionStore.hasConversation(snapshot.messages)) {
                                        ScriptPluginAgentSessionStore.revive(sessionId)
                                        ScriptPluginAgentSessionStore.save(context, snapshot)
                                    } else {
                                        ScriptPluginAgentSessionStore.delete(context, sessionId)
                                    }
                                }
                                saveResult.onSuccess {
                                    runCatching {
                                        if (removedMessages.isNotEmpty()) deleteToolResults(removedMessages)
                                    }.onFailure { error ->
                                        h.Hchat.utils.HLog.e(
                                            "[Hchat:ScriptAgent] ${actionName}清理工具结果失败",
                                            error
                                        )
                                    }
                                }.onFailure { error ->
                                    h.Hchat.utils.HLog.e(
                                        "[Hchat:ScriptAgent] ${actionName}保存会话失败",
                                        error
                                    )
                                    Handler(Looper.getMainLooper()).post {
                                        Toast.makeText(
                                            context,
                                            "${actionName}已完成，但会话保存失败",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            }, "Hchat-Agent-History-Save").start()
                        }
                    },
                    onFailure = { error ->
                        mutationState.applyingFileOperation.value = false
                        h.Hchat.utils.HLog.e("[Hchat:ScriptAgent] ${actionName}重建历史失败", error)
                        Toast.makeText(context, "${actionName}失败，请重试", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }, "Hchat-Agent-History-Change").start()
    }

    fun removeMessageTree(index: Int): Pair<List<ScriptPluginAgentChatMessage>, List<ScriptPluginAgentChatMessage>> {
        val root = sessionMessages[index]
        val removedIds = linkedSetOf(root.id)
        var changed: Boolean
        do {
            changed = false
            sessionMessages.forEach { message ->
                if (message.id !in removedIds && message.parentMessageId in removedIds) {
                    removedIds += message.id
                    changed = true
                }
            }
        } while (changed)
        return sessionMessages.partition { it.id !in removedIds }
    }

    fun createMessageBranch(index: Int) {
        if (generating || contextCompacting || applyingFileOperation || hasPendingConfirmation() ||
            index !in sessionMessages.indices
        ) return
        val sourceMessages = sessionMessages.take(index + 1)
        val sourceSessionId = activeSessionId
        val sourceTitle = sessionTitle
        val sourceTargetPluginId = targetPluginId
        val branchId = java.util.UUID.randomUUID().toString().replace("-", "")
        applyingFileOperation = true
        Thread({
            val result = runCatching {
                val branchMessages = ScriptPluginAgentToolResultStore.copySessionResults(
                    context,
                    sourceSessionId,
                    branchId,
                    sourceMessages
                )
                val branchDraft = draftForMessages(branchMessages)
                val now = System.currentTimeMillis()
                ScriptPluginAgentSession(
                    id = branchId,
                    title = (sourceTitle.ifBlank { "新对话" } + " 分支").take(32),
                    createdAt = now,
                    updatedAt = now,
                    messages = branchMessages,
                    draft = branchDraft,
                    targetPluginId = branchDraft?.pluginId?.ifBlank { sourceTargetPluginId }
                        ?: sourceTargetPluginId,
                    conversationSummary = "",
                    nativeToolHistory = rebuildNativeHistory(branchMessages),
                    protocolTranscript = "",
                    compactedMessageCount = 0
                ).also { branch -> ScriptPluginAgentSessionStore.save(context, branch) }
            }
            Handler(Looper.getMainLooper()).post {
                applyingFileOperation = false
                result.onSuccess { branch ->
                    openSession(branch)
                    Toast.makeText(context, "已从此处创建分支", Toast.LENGTH_SHORT).show()
                    historyVersion++
                }.onFailure { error ->
                    h.Hchat.utils.HLog.e("[Hchat:ScriptAgent] 创建会话分支失败", error)
                    Toast.makeText(context, error.message ?: "创建分支失败", Toast.LENGTH_LONG).show()
                }
            }
        }, "Hchat-Agent-Create-Branch").start()
    }

    fun editAndResendMessage(index: Int) {
        if (generating || contextCompacting || applyingFileOperation || hasPendingConfirmation() ||
            index !in sessionMessages.indices
        ) return
        val selected = sessionMessages[index]
        if (selected.role != "user") return
        val sourceMessages = sessionMessages
        val retainedMessages = sourceMessages.take(index)
        val removedMessages = sourceMessages.drop(index)
        val restoredDraft = draftForMessages(retainedMessages)
        val previousResumeState = resumeState
        prepareMessageHistoryChange(
            retainedMessages = retainedMessages,
            removedMessages = removedMessages,
            actionName = "编辑重发"
        ) { restoredHistory ->
            sessionMessages = retainedMessages
            sessionDraft = restoredDraft
            conversationSummary = ""
            nativeToolHistory = restoredHistory
            protocolTranscript = ""
            discardResumeWorkspace(previousResumeState)
            resumeState = null
            compactedMessageCount = 0
            messageInput = selected.content
            pendingAttachments = selected.attachments
            pendingQuotedMessage = selected.quotedMessage
            if (retainedMessages.isEmpty()) historyVersion++
            Toast.makeText(context, "已覆盖后续记录并填入原消息", Toast.LENGTH_SHORT).show()
            true
        }
    }

    fun quoteAgentMessage(index: Int) {
        val selected = sessionMessages.getOrNull(index) ?: return
        val quotedContent = selected.content.trim()
        if (quotedContent.isBlank()) {
            Toast.makeText(context, "当前消息没有可引用内容", Toast.LENGTH_SHORT).show()
            return
        }
        pendingQuotedMessage = ScriptPluginAgentQuotedMessage(
            role = selected.role,
            content = quotedContent.take(24_000),
            createdAt = selected.createdAt
        )
    }

    fun regenerateAssistantMessage(index: Int) {
        if (generating || contextCompacting || applyingFileOperation || hasPendingConfirmation() ||
            index !in sessionMessages.indices
        ) return
        val selected = sessionMessages[index]
        val sourceIndex = (index - 1 downTo 0).firstOrNull { sessionMessages[it].role == "user" } ?: return
        val source = sessionMessages[sourceIndex]
        if (selected.role != "assistant") return
        val config = currentConfig()
        if (config.apiBaseUrl.isBlank() || config.model.isBlank()) {
            showConfig = true
            Toast.makeText(context, "请先完成 Agent 配置", Toast.LENGTH_SHORT).show()
            return
        }
        endpointError(config).takeIf { it.isNotBlank() }?.let { error ->
            showConfig = true
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            return
        }
        if (config.mcpServers.any { it.enabled && it.endpoint.isBlank() }) {
            showConfig = true
            Toast.makeText(context, "请填写已启用 MCP 的 Endpoint", Toast.LENGTH_SHORT).show()
            return
        }
        val sourceMessages = sessionMessages
        val retainedMessages = sourceMessages.take(sourceIndex)
        val removedMessages = sourceMessages.drop(sourceIndex)
        val restoredDraft = draftForMessages(retainedMessages)
        val previousResumeState = resumeState
        prepareMessageHistoryChange(
            retainedMessages = retainedMessages,
            removedMessages = removedMessages,
            actionName = "重新生成"
        ) { restoredHistory ->
            sessionMessages = retainedMessages
            sessionDraft = restoredDraft
            targetPluginId = restoredDraft?.pluginId.orEmpty()
            conversationSummary = ""
            nativeToolHistory = restoredHistory
            protocolTranscript = ""
            discardResumeWorkspace(previousResumeState)
            resumeState = null
            compactedMessageCount = 0
            val started = sendMessage(
                content = source.content,
                attachments = source.attachments,
                quotedMessage = source.quotedMessage,
                consumeComposer = false
            )
            if (started) Toast.makeText(context, "正在重新生成", Toast.LENGTH_SHORT).show()
            started
        }
    }

    fun continueInterruptedTask(index: Int) {
        if (generating || contextCompacting || applyingFileOperation || index !in sessionMessages.indices) return
        val selected = sessionMessages[index]
        val turnInterrupted = selected.status == "interrupted" || selected.status == "error" ||
            sessionMessages.any { message ->
                message.turnId == selected.turnId && (
                    message.status == "interrupted" || message.status == "error" ||
                        message.toolEvents.any { it.status == "interrupted" }
                )
            }
        if (!turnInterrupted) return
        val sourceIndex = (index downTo 0).firstOrNull { candidate ->
            val message = sessionMessages[candidate]
            message.role == "user" && (
                selected.turnId.isBlank() || message.turnId == selected.turnId
            )
        } ?: return
        val source = sessionMessages[sourceIndex]
        val savedCheckpoint = resumeState?.takeIf { it.turnId == selected.turnId }
        val hasLaterTurnMessages = sessionMessages.drop(index + 1).any {
            it.turnId == selected.turnId
        }
        if (savedCheckpoint == null && hasLaterTurnMessages) {
            Toast.makeText(context, "该中断记录已经继续处理", Toast.LENGTH_SHORT).show()
            return
        }
        val checkpoint = savedCheckpoint
            ?: ScriptPluginAgentResumeState(
                turnId = selected.turnId,
                sourceUserMessageId = source.id,
                startedAt = source.createdAt,
                updatedAt = System.currentTimeMillis()
            )
        val started = sendMessage(
            content = source.content,
            attachments = emptyList(),
            quotedMessage = null,
            consumeComposer = false,
            resumeFrom = checkpoint
        )
        if (started) Toast.makeText(context, "正在从已保存进度继续", Toast.LENGTH_SHORT).show()
    }

    fun rollbackToMessage(messageId: String) {
        val index = sessionMessages.indexOfFirst { it.id == messageId }
        if (generating || contextCompacting || applyingFileOperation || hasPendingConfirmation() ||
            index < 0
        ) return
        val sourceMessages = sessionMessages
        val retainedMessages = sourceMessages.take(index + 1)
        val removedMessages = sourceMessages.drop(index + 1)
        val restoredDraft = draftForMessages(retainedMessages)
        val rollbackResumeState = resumeState
        prepareMessageHistoryChange(
            retainedMessages = retainedMessages,
            removedMessages = removedMessages,
            actionName = "回滚会话"
        ) { restoredHistory ->
            sessionMessages = retainedMessages
            sessionDraft = restoredDraft
            conversationSummary = ""
            nativeToolHistory = restoredHistory
            protocolTranscript = ""
            discardResumeWorkspace(rollbackResumeState)
            resumeState = null
            compactedMessageCount = 0
            Toast.makeText(context, "已回滚到所选消息", Toast.LENGTH_SHORT).show()
            true
        }
    }

    fun deleteAgentMessage(index: Int) {
        if (generating || contextCompacting || applyingFileOperation || hasPendingConfirmation() ||
            index !in sessionMessages.indices
        ) return
        val hadLaterMessages = index < sessionMessages.lastIndex
        val (remainingMessages, removedMessages) = removeMessageTree(index)
        val restoredDraft = draftForMessages(remainingMessages)
        val nextDraft = if (restoredDraft != null || !hadLaterMessages) restoredDraft else sessionDraft
        val previousResumeState = resumeState
        prepareMessageHistoryChange(
            retainedMessages = remainingMessages,
            removedMessages = removedMessages,
            actionName = "删除消息"
        ) { restoredHistory ->
            sessionMessages = remainingMessages
            sessionDraft = nextDraft
            conversationSummary = ""
            nativeToolHistory = restoredHistory
            protocolTranscript = ""
            discardResumeWorkspace(previousResumeState)
            resumeState = null
            compactedMessageCount = 0
            if (remainingMessages.isEmpty()) {
                historyVersion++
            }
            Toast.makeText(context, "消息已删除", Toast.LENGTH_SHORT).show()
            true
        }
    }

    fun currentSessionSnapshot(updatedAt: Long = System.currentTimeMillis()): ScriptPluginAgentSession {
        return renderedSessionState.snapshot(updatedAt)
    }

    fun persistSession(session: ScriptPluginAgentSession) {
        ScriptPluginAgentSessionStore.save(context, session)
        ScriptPluginAgentRuntime.updateMetadata(session)
        if (session.id == activeSessionId) {
            sessionTitle = session.title
            sessionPinned = session.pinned
            sessionLocked = session.locked
            sessionSortOrder = session.sortOrder
        }
        historyVersion++
    }

    fun renameSession(session: ScriptPluginAgentSession, title: String) {
        val cleanTitle = title.replace(Regex("\\s+"), " ").trim().take(32)
        if (cleanTitle.isBlank()) return
        val source = if (session.id == activeSessionId) {
            currentSessionSnapshot(session.updatedAt)
        } else {
            session
        }
        persistSession(source.copy(title = cleanTitle))
    }

    fun setSessionPinned(session: ScriptPluginAgentSession, pinned: Boolean) {
        val source = if (session.id == activeSessionId) {
            currentSessionSnapshot(session.updatedAt)
        } else {
            session
        }
        val nextOrder = if (pinned) {
            (ScriptPluginAgentSessionStore.list(context).maxOfOrNull { it.sortOrder } ?: source.sortOrder) + 1L
        } else {
            source.sortOrder
        }
        persistSession(source.copy(pinned = pinned, sortOrder = nextOrder))
    }

    fun setSessionLocked(session: ScriptPluginAgentSession, locked: Boolean) {
        val source = if (session.id == activeSessionId) {
            currentSessionSnapshot(session.updatedAt)
        } else {
            session
        }
        persistSession(source.copy(locked = locked))
    }

    fun persistSessionOrder(ordered: List<ScriptPluginAgentSession>) {
        val topOrder = (ordered.maxOfOrNull { it.sortOrder } ?: System.currentTimeMillis()) + ordered.size + 1L
        val reordered = ordered.mapIndexed { index, item -> item.copy(sortOrder = topOrder - index) }
        reordered.firstOrNull { it.id == activeSessionId }?.let { sessionSortOrder = it.sortOrder }
        Thread({
            runCatching {
                reordered.forEach { ScriptPluginAgentSessionStore.save(context, it) }
            }.onSuccess {
                Handler(Looper.getMainLooper()).post { historyVersion++ }
            }.onFailure { error ->
                h.Hchat.utils.HLog.e("[Hchat:ScriptAgent] 保存历史会话排序失败", error)
            }
        }, "Hchat-Agent-Session-Order").start()
    }

    fun deleteSession(session: ScriptPluginAgentSession) {
        if (session.locked) {
            Toast.makeText(context, "请先解锁该对话", Toast.LENGTH_SHORT).show()
            return
        }
        if (ScriptPluginAgentRuntime.isBusy(session.id)) {
            Toast.makeText(context, "该对话正在运行或有待确认操作，请先处理后再删除", Toast.LENGTH_SHORT).show()
            return
        }
        discardResumeWorkspace(
            if (session.id == activeSessionId) resumeState else session.resumeState
        )
        ScriptPluginAgentSessionStore.delete(context, session.id)
        ScriptPluginAgentRuntime.remove(session.id)
        historyVersion++
        if (session.id == activeSessionId) {
            val next = ScriptPluginAgentSessionStore.list(context).firstOrNull()
            if (next == null) newSession(saveCurrent = false) else openSession(next, saveCurrent = false)
        }
    }

    fun prepareForExit() {
        if (!exitPrepared.compareAndSet(false, true)) return
        val hasConversation = ScriptPluginAgentSessionStore.hasConversation(sessionMessages)
        saveCurrentSession()
        if (hasConversation) ScriptPluginAgentRuntime.resumeOnNextEntry(activeSessionId)
    }

    fun exitPage() {
        prepareForExit()
        onBack()
    }

    val latestExitHandler = rememberUpdatedState(newValue = { exitPage() })
    val latestPrepareForExit = rememberUpdatedState(newValue = { prepareForExit() })
    DisposableEffect(onExitHandlerChanged) {
        val handler = { latestExitHandler.value.invoke() }
        onExitHandlerChanged(handler)
        onDispose {
            onExitHandlerChanged(null)
            latestPrepareForExit.value.invoke()
        }
    }

    LaunchedEffect(
        generating,
        applyingFileOperation,
        pendingWrite,
        pendingDelete,
        pendingWorkspaceChange,
        showConfig,
        showModelPicker,
        showQuickProfilePicker,
        showQuickOptions,
        pendingMessages
    ) {
        if (
            !generating &&
            !applyingFileOperation &&
            pendingWrite == null &&
            pendingDelete == null &&
            pendingWorkspaceChange == null &&
            !showConfig &&
            !showModelPicker &&
            !showQuickProfilePicker &&
            !showQuickOptions &&
            pendingMessages.isNotEmpty()
        ) {
            delay(250L)
            val next = pendingMessages.firstOrNull() ?: return@LaunchedEffect
            if (
                sendMessage(
                    content = next.content,
                    attachments = next.attachments,
                    quotedMessage = next.quotedMessage,
                    consumeComposer = false
                )
            ) {
                pendingMessages = pendingMessages.filterNot { it.id == next.id }
            }
        }
    }

    if (showToolApprovalDialog) {
        pendingToolApproval?.let { approval ->
            var alwaysAllow by remember(approval.confirmation.eventId) { mutableStateOf(false) }
            WindowDialog(
                show = true,
                title = approval.confirmation.toolName,
                onDismissRequest = { showToolApprovalDialog = false },
                content = {
                    Column(modifier = Modifier.fillMaxWidth().heightIn(max = 520.dp)) {
                        Column(
                            modifier = Modifier.fillMaxWidth().weight(1f, fill = false)
                                .verticalScroll(rememberScrollState())
                        ) {
                            Text(
                                text = "Agent 正在修改插件 ${approval.confirmation.pluginId}，确认后继续执行。",
                                color = MiuixTheme.colorScheme.onSurface,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "代码差异",
                                color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 12.dp)
                            )
                            ScriptPluginAgentDiffCodeBlock(
                                approval.confirmation.diff.ifBlank { "无代码变化" }
                            )
                        }
                        ScriptPluginAgentAlwaysAllowRow(
                            checked = alwaysAllow,
                            summary = "确认后，本轮后续写入和最终提交不再询问",
                            onCheckedChange = { alwaysAllow = it }
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            TextButton(
                                text = "取消修改",
                                onClick = {
                                    showToolApprovalDialog = false
                                    pendingToolApproval = null
                                    approval.resolve(ScriptPluginAgentWorkspaceWriteDecision.CANCEL)
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.textButtonColorsPrimary()
                            )
                            TextButton(
                                text = "确认并继续",
                                onClick = {
                                    if (alwaysAllow) {
                                        setWorkspaceWriteApprovalMode(
                                            ScriptPluginAgentSettings.WRITE_APPROVAL_ALWAYS_ALLOW
                                        )
                                    }
                                    showToolApprovalDialog = false
                                    pendingToolApproval = null
                                    approval.resolve(
                                        if (alwaysAllow) {
                                            ScriptPluginAgentWorkspaceWriteDecision.ALWAYS_ALLOW
                                        } else {
                                            ScriptPluginAgentWorkspaceWriteDecision.APPROVE_ONCE
                                        }
                                    )
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
    if (showPendingWorkspaceDialog) pendingWorkspaceChange?.let { pending ->
        var alwaysAllow by remember(pending.change.stagingPath) { mutableStateOf(false) }
        fun dismissWorkspaceChange() {
            showPendingWorkspaceDialog = false
            pendingWorkspaceChange = null
            resumeState = null
            Thread({
                ScriptPluginAgentWorkspaceTools.discard(context, pending.change)
            }, "Hchat-Script-Agent-Workspace-Discard").start()
            recordFileOperation(pending.messageIndex, "已取消插件工作区变更")
        }
        WindowDialog(
            show = true,
            title = when {
                pending.change.deletePlugin -> "删除插件"
                pending.isNewPlugin -> "创建插件"
                pending.hasDeletedPaths -> "确认删除路径"
                pending.risky -> "确认高风险修改"
                else -> "确认修改"
            },
            onDismissRequest = { showPendingWorkspaceDialog = false },
            content = {
                Column(modifier = Modifier.fillMaxWidth().heightIn(max = 520.dp)) {
                    Column(
                        modifier = Modifier.fillMaxWidth().weight(1f, fill = false)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = buildString {
                                when {
                                    pending.change.deletePlugin -> append(
                                        "Agent 将删除插件“${pending.change.pluginName}”（${pending.change.pluginId}）。"
                                    )
                                    pending.isNewPlugin -> append(
                                        "Agent 将创建插件“${pending.change.pluginName}”（${pending.change.pluginId}）。"
                                    )
                                    else -> append("Agent 将更新插件 ${pending.change.pluginId}。")
                                }
                                if (!pending.change.deletePlugin) append("提交后插件保持禁用。")
                                if (pending.applyError.isNotBlank()) {
                                    append("\n\n上次提交失败：").append(pending.applyError)
                                    append("\n暂存修改仍在，可直接重试；若目标代码文件已被其它操作修改，请取消后让 Agent 重新读取。")
                                }
                                val summaries = buildList {
                                    if (pending.change.createdPaths.isNotEmpty()) {
                                        add("新增 ${pending.change.createdPaths.size} 项")
                                    }
                                    if (pending.change.modifiedPaths.isNotEmpty()) {
                                        add("修改 ${pending.change.modifiedPaths.size} 项")
                                    }
                                    if (pending.change.deletedPaths.isNotEmpty()) {
                                        add("删除 ${pending.change.deletedPaths.size} 项")
                                    }
                                }
                                if (summaries.isNotEmpty()) {
                                    append("\n\n").append(summaries.joinToString("，"))
                                }
                                val changedPaths = (
                                    pending.change.createdPaths +
                                        pending.change.modifiedPaths +
                                        pending.change.deletedPaths
                                    ).distinct().take(6)
                                if (changedPaths.isNotEmpty()) {
                                    append("\n").append(changedPaths.joinToString("\n") { "- $it" })
                                }
                                if (pending.risky) {
                                    append("\n\n静态检查发现高风险代码：")
                                    append(
                                        pending.change.warnings.filter { it.risky }
                                            .joinToString("；") { it.message }
                                    )
                                }
                            },
                            color = MiuixTheme.colorScheme.onSurface,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "代码差异",
                            color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 12.dp)
                        )
                        ScriptPluginAgentDiffCodeBlock(pending.change.diff.ifBlank { "无代码变化" })
                    }
                    ScriptPluginAgentAlwaysAllowRow(
                        checked = alwaysAllow,
                        summary = "确认后，后续插件修改不再询问",
                        onCheckedChange = { alwaysAllow = it }
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        TextButton(
                            text = "取消",
                            onClick = { dismissWorkspaceChange() },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.textButtonColorsPrimary()
                        )
                        TextButton(
                            text = when {
                                pending.change.deletePlugin -> "确认删除"
                                pending.isNewPlugin -> "确认创建"
                                pending.applyError.isNotBlank() -> "重试提交"
                                else -> "确认提交"
                            },
                            onClick = {
                                if (alwaysAllow) {
                                    setWorkspaceWriteApprovalMode(
                                        ScriptPluginAgentSettings.WRITE_APPROVAL_ALWAYS_ALLOW
                                    )
                                }
                                showPendingWorkspaceDialog = false
                                pendingWorkspaceChange = null
                                applyWorkspaceChange(pending.messageIndex, pending.change)
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.textButtonColorsPrimary()
                        )
                    }
                }
            }
        )
    }
    if (showPendingWriteDialog) pendingWrite?.let { pending ->
        var alwaysAllow by remember(pending.messageIndex, pending.draft.pluginId) { mutableStateOf(false) }
        fun dismissPendingWrite() {
            showPendingWriteDialog = false
            pendingWrite = null
            resumeState = null
            recordFileOperation(
                pending.messageIndex,
                when {
                    pending.isNewPlugin -> "已取消创建插件"
                    pending.confirmsCreation -> "已取消替换同名插件"
                    pending.risky -> "已取消高风险修改"
                    else -> "已取消插件修改"
                }
            )
        }
        WindowDialog(
            show = true,
            title = when {
                pending.isNewPlugin -> "创建插件"
                pending.confirmsCreation -> "替换同名插件"
                pending.risky -> "确认高风险修改"
                else -> "确认修改"
            },
            onDismissRequest = { showPendingWriteDialog = false },
            content = {
                Column(modifier = Modifier.fillMaxWidth().heightIn(max = 520.dp)) {
                    Column(
                        modifier = Modifier.fillMaxWidth().weight(1f, fill = false)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = buildString {
                                if (pending.isNewPlugin) {
                                    append("Agent 将创建插件“${pending.draft.pluginName}”（${pending.draft.pluginId}）。创建后默认禁用。")
                                } else if (pending.confirmsCreation) {
                                    append("目录 ${pending.draft.pluginId} 已存在。继续会把它作为同名插件更新，并保持禁用。")
                                } else {
                                    append("Agent 将直接修改插件 ${pending.draft.pluginId}，写入后插件会保持禁用。")
                                }
                                if (pending.risky) {
                                    append("\n\n静态检查发现网络、反射、Hook、文件删除或进程执行等高风险代码，请确认代码来源和用途。")
                                }
                            },
                            color = MiuixTheme.colorScheme.onSurface,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "代码差异",
                            color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 12.dp)
                        )
                        ScriptPluginAgentDiffCodeBlock(pending.diff.ifBlank { "无代码变化" })
                    }
                    ScriptPluginAgentAlwaysAllowRow(
                        checked = alwaysAllow,
                        summary = "确认后，后续插件修改不再询问",
                        onCheckedChange = { alwaysAllow = it }
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        TextButton(
                            text = "取消",
                            onClick = { dismissPendingWrite() },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.textButtonColorsPrimary()
                        )
                        TextButton(
                            text = when {
                                pending.isNewPlugin -> "确认创建"
                                pending.confirmsCreation -> "确认替换"
                                else -> "确认修改"
                            },
                            onClick = {
                                if (alwaysAllow) {
                                    setWorkspaceWriteApprovalMode(
                                        ScriptPluginAgentSettings.WRITE_APPROVAL_ALWAYS_ALLOW
                                    )
                                }
                                showPendingWriteDialog = false
                                pendingWrite = null
                                writeAgentDraft(pending.messageIndex, pending.draft, pending.isNewPlugin)
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.textButtonColorsPrimary()
                        )
                    }
                }
            }
        )
    }
    if (showPendingDeleteDialog) pendingDelete?.let { pending ->
        var alwaysAllow by remember(pending.messageIndex, pending.pluginId) { mutableStateOf(false) }
        fun dismissPendingDelete() {
            showPendingDeleteDialog = false
            pendingDelete = null
            resumeState = null
            recordFileOperation(pending.messageIndex, "已取消删除插件")
        }
        WindowDialog(
            show = true,
            title = "删除插件",
            onDismissRequest = { showPendingDeleteDialog = false },
            content = {
                Column {
                    Text(
                        text = "确定删除插件“${pending.pluginName}”（${pending.pluginId}）吗？插件目录内的全部文件都会被删除，此操作无法撤销。",
                        color = MiuixTheme.colorScheme.onSurface,
                        fontSize = 13.sp
                    )
                    ScriptPluginAgentAlwaysAllowRow(
                        checked = alwaysAllow,
                        summary = "确认后，后续插件修改不再询问",
                        onCheckedChange = { alwaysAllow = it }
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        TextButton(
                            text = "取消",
                            onClick = { dismissPendingDelete() },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.textButtonColorsPrimary()
                        )
                        TextButton(
                            text = "确认删除",
                            onClick = {
                                if (alwaysAllow) {
                                    setWorkspaceWriteApprovalMode(
                                        ScriptPluginAgentSettings.WRITE_APPROVAL_ALWAYS_ALLOW
                                    )
                                }
                                showPendingDeleteDialog = false
                                pendingDelete = null
                                deleteAgentPlugin(pending.messageIndex, pending.pluginId)
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.textButtonColorsPrimary()
                        )
                    }
                }
            }
        )
    }
    if (showQuickProfilePicker) {
        ScriptPluginAgentQuickProfileDialog(
            profiles = profiles,
            activeProfileId = activeProfileId,
            onSelected = {
                selectProfile(it)
                showQuickProfilePicker = false
            },
            onDismiss = { showQuickProfilePicker = false }
        )
    }
    if (showQuickOptions) {
        ScriptPluginAgentQuickOptionsDialog(
            webSearchEnabled = webSearchEnabled,
            workspaceWriteApprovalMode = workspaceWriteApprovalMode,
            promptCacheMode = promptCacheMode,
            endpointMode = endpointMode,
            mcpServers = mcpServers,
            onWebSearchChanged = { setWebSearchEnabled(it) },
            onWorkspaceWriteApprovalChanged = { setWorkspaceWriteApprovalMode(it) },
            onPromptCacheModeChanged = { setPromptCacheMode(it) },
            onMcpChanged = { id, enabled -> setMcpServerEnabled(id, enabled) },
            onDismiss = { showQuickOptions = false }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            showModelPicker -> ScriptPluginAgentModelPickerPage(
                config = currentConfig(),
                currentModel = model,
                onSelected = {
                    val appliesToNextRequest = generating
                    ScriptPluginAgentSettings.save(context, currentConfig().copy(model = it))
                    model = it
                    showModelPicker = false
                    Toast.makeText(
                        context,
                        if (appliesToNextRequest) "已切换模型，将用于下一次请求" else "已切换模型",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onBack = { showModelPicker = false }
            )
            showConfig -> ScriptPluginAgentConfigPage(
                profiles = profiles,
                activeProfileId = activeProfileId,
                activeProfileName = activeProfileName,
                testingConnection = testingConnection,
                apiBase = apiBase,
                endpointMode = endpointMode,
                apiKey = apiKey,
                model = model,
                mcpServers = mcpServers,
                autoCompactEnabled = autoCompactEnabled,
                compactTokenThreshold = compactTokenThreshold,
                promptCacheMode = promptCacheMode,
                onApiBaseChange = { apiBase = it },
                onEndpointModeChange = { mode ->
                    endpointMode = mode
                    ScriptPluginAgentSettings.presetRequestUrl(mode)
                        .takeIf { it.isNotBlank() }
                        ?.let { apiBase = it }
                },
                onApiKeyChange = { apiKey = it },
                onModelChange = { model = it },
                onMcpServersChange = { mcpServers = it },
                onAutoCompactEnabledChange = { autoCompactEnabled = it },
                onCompactTokenThresholdChange = { compactTokenThreshold = it.filter(Char::isDigit) },
                onPromptCacheModeChange = { promptCacheMode = it },
                onProfileSelected = { selectProfile(it) },
                onCreateProfile = { createProfile(it) },
                onRenameProfile = { renameProfile(it) },
                onDeleteProfile = { deleteActiveProfile() },
                onTestConnection = { testAgentConnection() },
                onOpenModels = { showModelPicker = true },
                onCompact = { compactContext() },
                onSave = { saveConfig() },
                onBack = { showConfig = false }
            )
            else -> ScriptPluginAgentChatPage(
                title = sessionTitle,
                messages = sessionMessages,
                messageInput = messageInput,
                pendingAttachments = pendingAttachments,
                pendingQuotedMessage = pendingQuotedMessage,
                pendingMessages = pendingMessages,
                pendingMessagesExpanded = pendingMessagesExpanded,
                activeProfileName = activeProfileName,
                activeModel = model,
                enabled = !generating && !contextCompacting && !applyingFileOperation &&
                    !hasPendingConfirmation(),
                inputEnabled = !contextCompacting && !applyingFileOperation &&
                    !hasPendingConfirmation(),
                generating = generating,
                contextCompacting = contextCompacting,
                workingStartedAt = if (contextCompacting) contextCompactionStartedAt else generationStartedAt,
                workingLabel = when {
                    contextCompacting && generating -> "正在自动压缩上下文"
                    contextCompacting -> "正在压缩上下文"
                    else -> ""
                },
                showWorking = generating && !generationHasVisibleReply,
                pendingConfirmationLabel = when {
                    pendingToolApproval != null -> "${pendingToolApproval?.confirmation?.toolName}待确认"
                    pendingWorkspaceChange != null -> "最终插件修改待确认"
                    pendingWrite != null -> "最终插件写入待确认"
                    pendingDelete != null -> "删除插件待确认"
                    else -> null
                },
                onMessageInputChange = { messageInput = it },
                onSend = { sendMessage() },
                onQueue = { queueMessage() },
                onCancel = { cancelGeneration() },
                onAttach = { selectAttachments() },
                onRemoveAttachment = { path ->
                    val removed = pendingAttachments.filter { it.path == path }
                    pendingAttachments = pendingAttachments.filterNot { it.path == path }
                    ScriptPluginAgentSessionStore.cleanupAttachments(context, removed)
                },
                onClearQuotedMessage = { pendingQuotedMessage = null },
                onPendingMessagesExpandedChange = { pendingMessagesExpanded = it },
                onEditPendingMessage = { editPendingMessage(it) },
                onDeletePendingMessage = { id ->
                    pendingMessages.firstOrNull { it.id == id }?.let { pending ->
                        ScriptPluginAgentSessionStore.cleanupAttachments(context, pending.attachments)
                    }
                    pendingMessages = pendingMessages.filterNot { it.id == id }
                },
                onSendPendingMessageNow = { sendPendingMessageNow(it) },
                onOpenProfilePicker = { showQuickProfilePicker = true },
                onOpenModelPicker = { showModelPicker = true },
                onOpenQuickOptions = { showQuickOptions = true },
                onOpenPendingConfirmation = { showPendingConfirmation() },
                onCompact = { compactContext() },
                onOpenHistory = { showHistory = true },
                onOpenConfig = {
                    if (!generating && !contextCompacting && !applyingFileOperation &&
                        !showPendingConfirmation()
                    ) {
                        showConfig = true
                    }
                },
                onEditAndResend = { editAndResendMessage(it) },
                onQuoteMessage = { quoteAgentMessage(it) },
                onContinueMessage = { continueInterruptedTask(it) },
                onRegenerateMessage = { regenerateAssistantMessage(it) },
                onRollback = { rollbackToMessage(it) },
                onDeleteMessage = { deleteAgentMessage(it) },
                onCreateBranch = { createMessageBranch(it) },
                onBack = { exitPage() }
            )
        }
        if (showHistory) {
            val workingSessionIds = ScriptPluginAgentRuntime.workingSessionIds()
            val visibleSessions = if (ScriptPluginAgentSessionStore.hasConversation(sessionMessages)) {
                val currentSession = currentSessionSnapshot()
                (sessions.filterNot { it.id == activeSessionId } + currentSession).distinctBy { it.id }
            } else {
                sessions.filterNot { it.id == activeSessionId }
            }
            val runtimeSessions = ScriptPluginAgentRuntime.snapshots()
            ScriptPluginAgentHistoryDrawer(
                sessions = ScriptPluginAgentSessionStore.sorted(
                    (visibleSessions + runtimeSessions).distinctBy { it.id }
                ),
                activeId = activeSessionId,
                workingSessionIds = workingSessionIds,
                enabled = true,
                onNewSession = { newSession() },
                onSelect = { openSession(it) },
                onRename = { session, title -> renameSession(session, title) },
                onCommitOrder = { persistSessionOrder(it) },
                onSetPinned = { session, pinned -> setSessionPinned(session, pinned) },
                onSetLocked = { session, locked -> setSessionLocked(session, locked) },
                onDelete = { deleteSession(it) },
                onClose = { showHistory = false }
            )
        }
    }
}

internal fun promptCacheModeChoices(): List<PopupChoice<String>> = listOf(
    PopupChoice("自动", ScriptPluginAgentSettings.PROMPT_CACHE_AUTO),
    PopupChoice("强制", ScriptPluginAgentSettings.PROMPT_CACHE_FORCE),
    PopupChoice("关闭", ScriptPluginAgentSettings.PROMPT_CACHE_OFF)
)

internal fun promptCacheModeSummary(mode: String, endpointMode: String): String {
    if (endpointMode == ScriptPluginAgentSettings.ENDPOINT_MODE_GEMINI) {
        return "Gemini 不使用 OpenAI 或 Anthropic 的显式缓存字段"
    }
    return when (mode) {
        ScriptPluginAgentSettings.PROMPT_CACHE_FORCE ->
            "向兼容接口发送稳定缓存标识，不支持时自动回退"
        ScriptPluginAgentSettings.PROMPT_CACHE_OFF ->
            "不发送显式缓存字段"
        else -> "官方接口显式缓存，其他接口使用服务端默认策略"
    }
}

@Composable
internal fun ScriptPluginAgentConfigPage(
    profiles: List<ScriptPluginAgentProfile>,
    activeProfileId: String,
    activeProfileName: String,
    testingConnection: Boolean,
    apiBase: String,
    endpointMode: String,
    apiKey: String,
    model: String,
    mcpServers: List<ScriptPluginAgentMcpServer>,
    autoCompactEnabled: Boolean,
    compactTokenThreshold: String,
    promptCacheMode: String,
    onApiBaseChange: (String) -> Unit,
    onEndpointModeChange: (String) -> Unit,
    onApiKeyChange: (String) -> Unit,
    onModelChange: (String) -> Unit,
    onMcpServersChange: (List<ScriptPluginAgentMcpServer>) -> Unit,
    onAutoCompactEnabledChange: (Boolean) -> Unit,
    onCompactTokenThresholdChange: (String) -> Unit,
    onPromptCacheModeChange: (String) -> Unit,
    onProfileSelected: (ScriptPluginAgentProfile) -> Unit,
    onCreateProfile: (String) -> Unit,
    onRenameProfile: (String) -> Unit,
    onDeleteProfile: () -> Unit,
    onTestConnection: () -> Unit,
    onOpenModels: () -> Unit,
    onCompact: () -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit
) {
    var profileDialog by remember { mutableStateOf("") }
    var profileNameDraft by remember(activeProfileName, profileDialog) {
        mutableStateOf(if (profileDialog == "rename") activeProfileName else "")
    }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    val resolvedEndpoint = remember(apiBase, endpointMode, model) {
        ScriptPluginAgentSettings.requestUrl(apiBase, endpointMode, model)
    }

    if (profileDialog.isNotBlank()) {
        WindowDialog(
            show = true,
            title = if (profileDialog == "rename") "重命名配置" else "新建配置",
            onDismissRequest = { profileDialog = "" },
            content = {
                Column {
                    InputRow(
                        title = "配置名称",
                        summary = "用于区分不同服务和模型",
                        value = profileNameDraft,
                        onValueChange = { profileNameDraft = it.take(32) }
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        TextButton(
                            text = "取消",
                            onClick = { profileDialog = "" },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.textButtonColorsPrimary()
                        )
                        TextButton(
                            text = "确认",
                            onClick = {
                                if (profileNameDraft.isNotBlank()) {
                                    if (profileDialog == "rename") {
                                        onRenameProfile(profileNameDraft)
                                    } else {
                                        onCreateProfile(profileNameDraft)
                                    }
                                    profileDialog = ""
                                }
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.textButtonColorsPrimary()
                        )
                    }
                }
            }
        )
    }
    if (showDeleteConfirm) {
        WindowDialog(
            show = true,
            title = "删除配置",
            onDismissRequest = { showDeleteConfirm = false },
            content = {
                Column {
                    Text(
                        text = "确定删除“$activeProfileName”吗？",
                        color = MiuixTheme.colorScheme.onSurface,
                        fontSize = 14.sp
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 14.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        TextButton(
                            text = "取消",
                            onClick = { showDeleteConfirm = false },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.textButtonColorsPrimary()
                        )
                        TextButton(
                            text = "删除",
                            onClick = {
                                showDeleteConfirm = false
                                onDeleteProfile()
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.textButtonColorsPrimary()
                        )
                    }
                }
            }
        )
    }

    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    PageScaffold(
        title = "Agent 配置",
        largeTitle = "Agent 配置",
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "保存配置",
                onPrimaryClick = onSave,
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
            item { SmallTitle(text = "模型配置") }
            item {
                SettingsCard {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 10.dp, end = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "选择模型配置",
                            color = MiuixTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.weight(1f)
                        )
                        TextButton(
                            text = "+ 新建",
                            onClick = {
                                profileNameDraft = ""
                                profileDialog = "create"
                            },
                            minWidth = 0.dp,
                            minHeight = 34.dp,
                            cornerRadius = 10.dp,
                            insideMargin = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            colors = ButtonDefaults.textButtonColorsPrimary()
                        )
                    }
                    PopupChoiceRow(
                        title = "当前配置",
                        summary = activeProfileName,
                        options = profiles.map { PopupChoice(it.name, it.id) },
                        currentValue = activeProfileId,
                        onValueChanged = { id -> profiles.firstOrNull { it.id == id }?.let(onProfileSelected) }
                    )
                    InsetDivider()
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TextButton(
                            text = "重命名",
                            onClick = {
                                profileNameDraft = activeProfileName
                                profileDialog = "rename"
                            },
                            modifier = Modifier.weight(1f),
                            minWidth = 0.dp,
                            minHeight = 34.dp,
                            cornerRadius = 10.dp,
                            insideMargin = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            colors = ButtonDefaults.textButtonColorsPrimary()
                        )
                        TextButton(
                            text = "删除",
                            onClick = { showDeleteConfirm = true },
                            enabled = profiles.size > 1,
                            modifier = Modifier.weight(1f),
                            minWidth = 0.dp,
                            minHeight = 34.dp,
                            cornerRadius = 10.dp,
                            insideMargin = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            colors = ButtonDefaults.textButtonColorsPrimary()
                        )
                        TextButton(
                            text = if (testingConnection) "测试中" else "测试连接",
                            onClick = onTestConnection,
                            enabled = !testingConnection,
                            modifier = Modifier.weight(1f),
                            minWidth = 0.dp,
                            minHeight = 34.dp,
                            cornerRadius = 10.dp,
                            insideMargin = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            colors = ButtonDefaults.textButtonColorsPrimary()
                        )
                    }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "API 设置") }
            item {
                SettingsCard {
                    PopupChoiceRow(
                        title = "接口类型",
                        summary = ScriptPluginAgentSettings.endpointModeLabel(endpointMode),
                        options = listOf(
                            PopupChoice("OpenAI 兼容", ScriptPluginAgentSettings.ENDPOINT_MODE_OPENAI_COMPATIBLE),
                            PopupChoice("OpenAI", ScriptPluginAgentSettings.ENDPOINT_MODE_OPENAI),
                            PopupChoice("DeepSeek", ScriptPluginAgentSettings.ENDPOINT_MODE_DEEPSEEK),
                            PopupChoice("OpenRouter", ScriptPluginAgentSettings.ENDPOINT_MODE_OPENROUTER),
                            PopupChoice("硅基流动", ScriptPluginAgentSettings.ENDPOINT_MODE_SILICONFLOW),
                            PopupChoice("Gemini", ScriptPluginAgentSettings.ENDPOINT_MODE_GEMINI),
                            PopupChoice("Anthropic", ScriptPluginAgentSettings.ENDPOINT_MODE_ANTHROPIC),
                            PopupChoice("自定义请求链接", ScriptPluginAgentSettings.ENDPOINT_MODE_CUSTOM_URL)
                        ),
                        currentValue = endpointMode,
                        onValueChanged = onEndpointModeChange
                    )
                    InsetDivider()
                    InputRow(
                        if (endpointMode == ScriptPluginAgentSettings.ENDPOINT_MODE_CUSTOM_URL) {
                            "请求链接"
                        } else {
                            "API 地址"
                        },
                        if (endpointMode == ScriptPluginAgentSettings.ENDPOINT_MODE_GEMINI) {
                            "自动补全 Gemini 模型与 generateContent 请求路径"
                        } else if (endpointMode == ScriptPluginAgentSettings.ENDPOINT_MODE_ANTHROPIC) {
                            "自动补全 /v1/messages"
                        } else if (endpointMode == ScriptPluginAgentSettings.ENDPOINT_MODE_CUSTOM_URL) {
                            "手动填写完整 URL，不会自动补全或改写"
                        } else {
                            "可填写域名、/v1 地址或完整 /chat/completions 地址"
                        },
                        apiBase,
                        onValueChange = onApiBaseChange
                    )
                    if (resolvedEndpoint.isNotBlank()) {
                        InsetDivider()
                        ScriptPluginAgentEndpointPreview(resolvedEndpoint)
                    }
                    InsetDivider()
                    InputRow("API Key", "留空表示接口不需要密钥", apiKey, onValueChange = onApiKeyChange)
                    InsetDivider()
                    InputRow("模型", "填写服务端可用的模型名称", model, onValueChange = onModelChange)
                    InsetDivider()
                    ActionRow("拉取模型列表", "从当前 API 地址获取") { onOpenModels() }
                    InsetDivider()
                    PopupChoiceRow(
                        title = "提示缓存",
                        summary = promptCacheModeSummary(promptCacheMode, endpointMode),
                        options = promptCacheModeChoices(),
                        currentValue = promptCacheMode,
                        onValueChanged = onPromptCacheModeChange
                    )
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "上下文") }
            item {
                SettingsCard {
                    SwitchRow(
                        checked = autoCompactEnabled,
                        title = "自动压缩上下文",
                        summary = "达到设定阈值后压缩较早对话",
                        onCheckedChange = onAutoCompactEnabledChange
                    )
                    if (autoCompactEnabled) {
                        InsetDivider()
                        InputRow(
                            "压缩阈值",
                            "Token 估算值，范围 2000 到 1000000",
                            compactTokenThreshold,
                            onValueChange = onCompactTokenThresholdChange
                        )
                    }
                    InsetDivider()
                    ActionRow("立即压缩当前会话", "保留本地历史和当前代码草稿") { onCompact() }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "MCP 工具") }
            item {
                SettingsCard {
                    ActionRow("添加 MCP", "可同时启用多个远程 MCP 服务器") {
                        val nextIndex = mcpServers.size + 1
                        onMcpServersChange(
                            mcpServers + ScriptPluginAgentMcpServer(
                                id = java.util.UUID.randomUUID().toString().replace("-", ""),
                                name = "MCP $nextIndex"
                            )
                        )
                    }
                }
            }
            mcpServers.forEachIndexed { index, server ->
                item(key = "agent-mcp-${server.id}") {
                    SettingsCard(modifier = Modifier.padding(top = 8.dp)) {
                        InputRow(
                            "名称",
                            "用于区分工具来源",
                            server.name,
                            onValueChange = { value ->
                                onMcpServersChange(
                                    mcpServers.map { if (it.id == server.id) it.copy(name = value.take(32)) else it }
                                )
                            }
                        )
                        InsetDivider()
                        SwitchRow(
                            checked = server.enabled,
                            title = "启用 ${server.name.ifBlank { "MCP ${index + 1}" }}",
                            summary = if (server.enabled) "此服务器的工具可供 Agent 调用" else "此服务器不会连接或提供工具",
                            onCheckedChange = { enabled ->
                                onMcpServersChange(
                                    mcpServers.map { if (it.id == server.id) it.copy(enabled = enabled) else it }
                                )
                            }
                        )
                        if (server.enabled) {
                            InsetDivider()
                            InputRow(
                                "MCP Endpoint",
                                "例如 https://example.com/mcp",
                                server.endpoint,
                                onValueChange = { value ->
                                    onMcpServersChange(
                                        mcpServers.map { if (it.id == server.id) it.copy(endpoint = value) else it }
                                    )
                                }
                            )
                            InsetDivider()
                            InputRow(
                                "Authorization",
                                "可选，例如 Bearer token",
                                server.authorization,
                                onValueChange = { value ->
                                    onMcpServersChange(
                                        mcpServers.map {
                                            if (it.id == server.id) it.copy(authorization = value) else it
                                        }
                                    )
                                }
                            )
                        }
                        InsetDivider()
                        ActionRow("删除 MCP", "移除此服务器配置") {
                            onMcpServersChange(mcpServers.filterNot { it.id == server.id })
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun ScriptPluginAgentEndpointPreview(endpoint: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)) {
        Text(
            text = "实际请求地址",
            color = MiuixTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = endpoint,
            color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
internal fun ScriptPluginAgentModelPickerPage(
    config: ScriptPluginAgentConfig,
    currentModel: String,
    onSelected: (String) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    var models by remember(config.apiBaseUrl, config.apiKey) { mutableStateOf<List<String>>(emptyList()) }
    var pendingModel by remember(currentModel) { mutableStateOf(currentModel) }
    var query by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var loadError by remember { mutableStateOf("") }
    val visibleModels = remember(models, query) {
        val keyword = query.trim()
        if (keyword.isBlank()) models else models.filter { it.contains(keyword, ignoreCase = true) }
    }

    fun loadModels() {
        if (loading) return
        loading = true
        loadError = ""
        scope.launch {
            val result = withContext(Dispatchers.IO) { ScriptPluginAgentClient.fetchModels(config) }
            loading = false
            result.onSuccess { fetched ->
                models = fetched
                if (pendingModel.isBlank()) pendingModel = fetched.firstOrNull().orEmpty()
                if (fetched.isEmpty()) loadError = "未获取到模型"
            }.onFailure {
                loadError = it.message ?: "拉取模型失败"
            }
        }
    }

    LaunchedEffect(config.apiBaseUrl, config.apiKey) { loadModels() }
    PageScaffold(
        title = "模型选择",
        largeTitle = "模型选择",
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "使用所选模型",
                onPrimaryClick = {
                    if (pendingModel.isBlank()) {
                        Toast.makeText(context, "请先选择模型", Toast.LENGTH_SHORT).show()
                    } else {
                        onSelected(pendingModel)
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
            item {
                SettingsCard {
                    ActionRow(
                        "拉取模型列表",
                        when {
                            loading -> "正在拉取模型"
                            loadError.isNotBlank() -> loadError
                            else -> "已获取 ${models.size} 个模型"
                        }
                    ) { loadModels() }
                    if (models.isNotEmpty()) {
                        InsetDivider()
                        InputRow("搜索", "输入模型名称", query) { query = it }
                    }
                }
            }
            visibleModels.take(300).forEach { modelName ->
                item(key = modelName) {
                    SettingsCard(modifier = Modifier.padding(top = 6.dp)) {
                        ActionRow(
                            title = modelName,
                            summary = when {
                                modelName == pendingModel -> "已选择"
                                modelName == currentModel -> "当前模型"
                                else -> ""
                            }
                        ) { pendingModel = modelName }
                    }
                }
            }
        }
    }
}

internal fun scriptPluginAgentRenderedMessages(
    messages: List<ScriptPluginAgentChatMessage>
): List<ScriptPluginAgentRenderedMessage> {
    val firstToolIndexByTurn = HashMap<String, Int>()
    messages.forEachIndexed { index, message ->
        if (message.role == "tool" && message.turnId.isNotBlank()) {
            firstToolIndexByTurn.putIfAbsent(message.turnId, index)
        }
    }
    val eventsByTurn = messages.asSequence()
        .filter { it.role == "tool" && it.turnId.isNotBlank() }
        .groupBy { it.turnId }
        .mapValues { (_, groups) -> groups.flatMap { it.toolEvents }.distinctBy { it.id } }
    return buildList {
        messages.forEachIndexed { index, message ->
            if (message.role != "tool" || message.turnId.isBlank()) {
                add(ScriptPluginAgentRenderedMessage(index, message))
                return@forEachIndexed
            }
            if (firstToolIndexByTurn[message.turnId] != index) return@forEachIndexed
            val events = eventsByTurn[message.turnId].orEmpty()
            val hasPending = events.any { it.status == "queued" || it.status == "running" }
            val wasInterrupted = events.any { it.status == "interrupted" }
            add(
                ScriptPluginAgentRenderedMessage(
                    sourceIndex = index,
                    message = message.copy(
                        toolEvents = events,
                        status = when {
                            hasPending -> "streaming"
                            wasInterrupted -> "interrupted"
                            else -> "complete"
                        },
                        completedAt = events.maxOfOrNull { it.finishedAt } ?: message.completedAt
                    )
                )
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ScriptPluginAgentChatPage(
    title: String,
    messages: List<ScriptPluginAgentChatMessage>,
    messageInput: String,
    pendingAttachments: List<ScriptPluginAgentAttachment>,
    pendingQuotedMessage: ScriptPluginAgentQuotedMessage?,
    pendingMessages: List<ScriptPluginAgentPendingMessage>,
    pendingMessagesExpanded: Boolean,
    activeProfileName: String,
    activeModel: String,
    enabled: Boolean,
    inputEnabled: Boolean,
    generating: Boolean,
    contextCompacting: Boolean,
    workingStartedAt: Long,
    workingLabel: String,
    showWorking: Boolean,
    pendingConfirmationLabel: String?,
    onMessageInputChange: (String) -> Unit,
    onSend: () -> Unit,
    onQueue: () -> Unit,
    onCancel: () -> Unit,
    onAttach: () -> Unit,
    onRemoveAttachment: (String) -> Unit,
    onClearQuotedMessage: () -> Unit,
    onPendingMessagesExpandedChange: (Boolean) -> Unit,
    onEditPendingMessage: (Long) -> Unit,
    onDeletePendingMessage: (Long) -> Unit,
    onSendPendingMessageNow: (Long) -> Unit,
    onOpenProfilePicker: () -> Unit,
    onOpenModelPicker: () -> Unit,
    onOpenQuickOptions: () -> Unit,
    onOpenPendingConfirmation: () -> Unit,
    onCompact: () -> Unit,
    onOpenHistory: () -> Unit,
    onOpenConfig: () -> Unit,
    onEditAndResend: (Int) -> Unit,
    onQuoteMessage: (Int) -> Unit,
    onContinueMessage: (Int) -> Unit,
    onRegenerateMessage: (Int) -> Unit,
    onRollback: (String) -> Unit,
    onDeleteMessage: (Int) -> Unit,
    onCreateBranch: (Int) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    val imeVisible = WindowInsets.isImeVisible
    var actionMessageId by remember { mutableStateOf<String?>(null) }
    var infoMessageId by remember { mutableStateOf<String?>(null) }
    var showScrollToBottom by remember { mutableStateOf(false) }
    var autoFollowOutput by remember { mutableStateOf(true) }
    var previousRenderedMessageCount by remember { mutableStateOf(-1) }
    val actionMessage = actionMessageId?.let { id -> messages.firstOrNull { it.id == id } }
    val infoMessage = infoMessageId?.let { id -> messages.firstOrNull { it.id == id } }
    val renderedMessages = remember(messages) {
        scriptPluginAgentRenderedMessages(messages)
    }

    fun consumeActionMessageIndex(): Int? {
        val messageId = actionMessageId
        actionMessageId = null
        return messageId?.let { id -> messages.indexOfFirst { it.id == id }.takeIf { it >= 0 } }
    }

    DisposableEffect(context) {
        val window = findAgentActivity(context)?.window
        val originalSoftInputMode = window?.attributes?.softInputMode
        if (window != null && originalSoftInputMode != null) {
            val state = originalSoftInputMode and WindowManager.LayoutParams.SOFT_INPUT_MASK_STATE
            window.setSoftInputMode(state or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }
        onDispose {
            if (window != null && originalSoftInputMode != null) {
                window.setSoftInputMode(originalSoftInputMode)
            }
        }
    }

    if (actionMessage != null) {
        ScriptPluginAgentMessageMenu(
            message = actionMessage,
            onDismiss = { actionMessageId = null },
            onCopy = {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? android.content.ClipboardManager
                clipboard?.setPrimaryClip(ClipData.newPlainText("插件 Agent 消息", actionMessage.content))
                Toast.makeText(context, "消息已复制", Toast.LENGTH_SHORT).show()
                actionMessageId = null
            },
            onReadAloud = {
                ScriptPluginAgentSpeech.speak(context, actionMessage.content)
                Toast.makeText(context, "开始朗读", Toast.LENGTH_SHORT).show()
                actionMessageId = null
            },
            onQuote = {
                val index = consumeActionMessageIndex()
                if (index != null) onQuoteMessage(index)
            },
            onRegenerate = if (actionMessage.role == "assistant" && actionMessage.status != "streaming") {
                {
                    val index = consumeActionMessageIndex()
                    if (index != null) onRegenerateMessage(index)
                }
            } else {
                null
            },
            onEditAndResend = if (actionMessage.role == "user") {
                {
                    val index = consumeActionMessageIndex()
                    if (index != null) onEditAndResend(index)
                }
            } else {
                null
            },
            onRollback = {
                val messageId = actionMessageId
                actionMessageId = null
                if (messageId != null) {
                    val renderedIndex = renderedMessages.indexOfFirst { it.message.id == messageId }
                    if (renderedIndex >= 0) listState.requestScrollToItem(renderedIndex)
                    onRollback(messageId)
                }
            },
            onDelete = {
                val index = consumeActionMessageIndex()
                if (index != null) onDeleteMessage(index)
            },
            onCreateBranch = {
                val index = consumeActionMessageIndex()
                if (index != null) onCreateBranch(index)
            },
            onInfo = {
                infoMessageId = actionMessageId
                actionMessageId = null
            }
        )
    }
    if (infoMessage != null) {
        ScriptPluginAgentMessageInfoDialog(
            message = infoMessage,
            onDismiss = { infoMessageId = null }
        )
    }
    LaunchedEffect(renderedMessages.size) {
        val previousCount = previousRenderedMessageCount
        previousRenderedMessageCount = renderedMessages.size
        if (messages.isNotEmpty()) {
            autoFollowOutput = true
            if (previousCount >= 0 && renderedMessages.size < previousCount) {
                listState.requestScrollToItem(renderedMessages.size)
            } else {
                delay(80L)
                listState.animateScrollToLatest(renderedMessages.size)
            }
        }
    }
    LaunchedEffect(
        messages.lastOrNull()?.content,
        messages.lastOrNull()?.reasoning,
        messages.lastOrNull()?.toolEvents?.size,
        messages.lastOrNull()?.progress
    ) {
        if (messages.isNotEmpty() && autoFollowOutput) {
            delay(40L)
            listState.scrollToLatest(renderedMessages.size)
        }
    }
    LaunchedEffect(listState, messages.isNotEmpty()) {
        snapshotFlow { listState.isScrollInProgress to listState.isNearBottom() }.collect { (scrolling, atBottom) ->
            if (atBottom) autoFollowOutput = true
            else if (scrolling) autoFollowOutput = false
            showScrollToBottom = !atBottom && messages.isNotEmpty()
        }
    }
    LaunchedEffect(imeVisible) {
        if (imeVisible && autoFollowOutput && messages.isNotEmpty()) {
            delay(180L)
            listState.scrollToLatest(renderedMessages.size)
        }
    }
    LaunchedEffect(contextCompacting) {
        if (contextCompacting && !generating && messages.isNotEmpty()) {
            autoFollowOutput = true
            delay(40L)
            listState.animateScrollToLatest(renderedMessages.size)
        }
    }
    PageScaffold(
        title = title.ifBlank { "插件 Agent" },
        largeTitle = "",
        scrollBehavior = scrollBehavior,
        onBack = onBack,
        topBarActions = {
            Image(
                imageVector = NavIcons.Compact,
                contentDescription = "压缩上下文",
                colorFilter = ColorFilter.tint(MiuixTheme.colorScheme.onSurface),
                modifier = Modifier.size(24.dp).responsiveTap(onClick = onCompact)
            )
            Image(
                imageVector = NavIcons.History,
                contentDescription = "会话历史",
                colorFilter = ColorFilter.tint(MiuixTheme.colorScheme.onSurface),
                modifier = Modifier.padding(start = 14.dp).size(24.dp).responsiveTap(onClick = onOpenHistory)
            )
            Image(
                imageVector = NavIcons.Settings,
                contentDescription = "Agent 配置",
                colorFilter = ColorFilter.tint(MiuixTheme.colorScheme.onSurface),
                modifier = Modifier.padding(start = 14.dp).size(24.dp).responsiveTap(onClick = onOpenConfig)
            )
        },
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().imePadding()) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    state = listState,
                    verticalArrangement = Arrangement.Bottom,
                    contentPadding = PaddingValues(
                        top = padding.calculateTopPadding() + 8.dp,
                        bottom = 8.dp
                    )
                ) {
                    if (messages.isEmpty()) {
                        item {
                            Text(
                                text = "开始一个新会话",
                                color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                                fontSize = 14.sp,
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 28.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        renderedMessages.forEach { rendered ->
                            val index = rendered.sourceIndex
                            val message = rendered.message
                            item(key = message.id) {
                                ScriptPluginAgentMessageBlock(
                                    message = message,
                                    showWorking = showWorking && index == messages.lastIndex,
                                    workingStartedAt = workingStartedAt,
                                    workingLabel = workingLabel,
                                    longPressEnabled = enabled && message.role != "tool",
                                    onLongPress = { actionMessageId = message.id },
                                    onContinue = if (
                                        !generating &&
                                        (message.status == "interrupted" || message.status == "error") &&
                                        messages.drop(index + 1).none { it.role == "user" }
                                    ) {
                                        { onContinueMessage(index) }
                                    } else null,
                                    onRetry = if (
                                        !generating && message.role == "assistant" &&
                                        (message.status == "interrupted" || message.status == "error")
                                    ) {
                                        { onRegenerateMessage(index) }
                                    } else null
                                )
                            }
                        }
                        if (contextCompacting && !generating) {
                            item(key = "agent-context-compaction") {
                                Row(
                                    modifier = Modifier.fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 10.dp)
                                ) {
                                    ScriptPluginAgentWorkingIndicator(
                                        startedAt = workingStartedAt,
                                        label = workingLabel
                                    )
                                }
                            }
                        }
                        item(key = "agent-message-tail") {
                            Spacer(modifier = Modifier.height(1.dp))
                        }
                    }
                }
                if (showScrollToBottom && messages.isNotEmpty()) {
                    Image(
                        imageVector = NavIcons.MoveDown,
                        contentDescription = "回到底部",
                        colorFilter = ColorFilter.tint(Color.White),
                        modifier = Modifier.align(Alignment.BottomEnd)
                            .padding(end = 16.dp, bottom = 8.dp)
                            .size(36.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(MiuixTheme.colorScheme.primary)
                            .clickable {
                                showScrollToBottom = false
                                autoFollowOutput = true
                                scope.launch { listState.animateScrollToLatest(renderedMessages.size) }
                            }
                            .padding(7.dp)
                    )
                }
            }
            if (pendingConfirmationLabel != null) {
                ScriptPluginAgentPendingConfirmationBar(
                    label = pendingConfirmationLabel,
                    onClick = onOpenPendingConfirmation
                )
            }
            Box(
                modifier = Modifier.fillMaxWidth()
                    .padding(bottom = padding.calculateBottomPadding())
            ) {
                ScriptPluginAgentComposer(
                    value = messageInput,
                    attachments = pendingAttachments,
                    quotedMessage = pendingQuotedMessage,
                    pendingMessages = pendingMessages,
                    pendingMessagesExpanded = pendingMessagesExpanded,
                    activeProfileName = activeProfileName,
                    activeModel = activeModel,
                    enabled = inputEnabled,
                    generating = generating || contextCompacting,
                    onValueChange = onMessageInputChange,
                    onSend = onSend,
                    onQueue = onQueue,
                    onCancel = onCancel,
                    onAttach = onAttach,
                    onRemoveAttachment = onRemoveAttachment,
                    onClearQuotedMessage = onClearQuotedMessage,
                    onPendingMessagesExpandedChange = onPendingMessagesExpandedChange,
                    onEditPendingMessage = onEditPendingMessage,
                    onDeletePendingMessage = onDeletePendingMessage,
                    onSendPendingMessageNow = onSendPendingMessageNow,
                    onOpenProfilePicker = onOpenProfilePicker,
                    onOpenModelPicker = onOpenModelPicker,
                    onOpenQuickOptions = onOpenQuickOptions
                )
            }
        }
    }
}

@Composable
internal fun ScriptPluginAgentAlwaysAllowRow(
    checked: Boolean,
    summary: String,
    onCheckedChange: (Boolean) -> Unit
) {
    SwitchRow(
        checked = checked,
        title = "始终允许",
        summary = summary,
        onCheckedChange = onCheckedChange
    )
}

@Composable
internal fun ScriptPluginAgentPendingConfirmationBar(label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .background(MiuixTheme.colorScheme.surfaceVariant)
            .responsiveTap(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            imageVector = NavIcons.Modules,
            contentDescription = null,
            colorFilter = ColorFilter.tint(MiuixTheme.colorScheme.primary),
            modifier = Modifier.size(20.dp)
        )
        Column(modifier = Modifier.padding(start = 10.dp).weight(1f)) {
            Text(
                text = "待确认的插件修改",
                color = MiuixTheme.colorScheme.onSurface,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = label,
                color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                fontSize = 11.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Text(
            text = "查看 Diff",
            color = MiuixTheme.colorScheme.primary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(start = 10.dp)
        )
    }
}

internal suspend fun LazyListState.animateScrollToLatest(index: Int) {
    animateScrollToItem(index)
}

internal suspend fun LazyListState.scrollToLatest(index: Int) {
    scrollToItem(index)
}

internal fun LazyListState.isNearBottom(thresholdPx: Int = 120): Boolean {
    val info = layoutInfo
    if (info.totalItemsCount == 0) return true
    val lastVisible = info.visibleItemsInfo.lastOrNull() ?: return true
    if (lastVisible.index < info.totalItemsCount - 1) return false
    return lastVisible.offset + lastVisible.size <= info.viewportEndOffset + thresholdPx
}

@Composable
internal fun ScriptPluginAgentMessageBlock(
    message: ScriptPluginAgentChatMessage,
    showWorking: Boolean,
    workingStartedAt: Long,
    workingLabel: String,
    longPressEnabled: Boolean,
    onLongPress: () -> Unit,
    onContinue: (() -> Unit)?,
    onRetry: (() -> Unit)?
) {
    if (message.phase == "assistant_tool_call" && message.content.isBlank()) return
    val context = LocalContext.current
    val waitingForReply = message.role == "assistant" &&
        message.status == "streaming" &&
        message.content.isBlank()
    if (waitingForReply && !showWorking && message.reasoning.isBlank() && message.toolEvents.isEmpty()) return
    val longPressModifier = Modifier.agentMessageLongPress(
        key = message.id.hashCode().toLong(),
        enabled = longPressEnabled,
        onLongPress = onLongPress
    )
    if (message.role == "user") {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Box(
                modifier = Modifier
                    .then(longPressModifier)
                    .clip(RoundedCornerShape(18.dp))
                    .background(MiuixTheme.colorScheme.primary.copy(alpha = 0.14f))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Column {
                    message.quotedMessage?.let { quoted ->
                        ScriptPluginAgentQuotedMessagePreview(
                            quotedMessage = quoted,
                            maxLines = 3,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    Text(text = message.content, color = MiuixTheme.colorScheme.onSurface, fontSize = 14.sp)
                    if (message.attachments.isNotEmpty()) {
                        Text(
                            text = message.attachments.joinToString("\n") { "附件: ${it.name}" },
                            color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 6.dp)
                        )
                    }
                }
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxWidth().then(longPressModifier)
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            if (waitingForReply) {
                if (showWorking) {
                    ScriptPluginAgentWorkingIndicator(
                        startedAt = workingStartedAt.takeIf { it > 0L } ?: message.createdAt,
                        label = workingLabel
                    )
                }
            } else if (message.role == "tool") {
                Text(
                    text = "工具调用",
                    color = MiuixTheme.colorScheme.primary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            ScriptPluginAgentReasoningGroup(message)
            ScriptPluginAgentToolGroup(message)
            if (message.role != "tool" && message.content.isBlank() && !waitingForReply) {
                Text(
                    text = when (message.status) {
                        "interrupted" -> "已中断，尚未收到回复"
                        else -> "本轮没有返回可显示内容"
                    },
                    color = MiuixTheme.colorScheme.onSurface,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = if (message.toolEvents.isNotEmpty()) 8.dp else 4.dp)
                )
            } else if (message.role != "tool") {
                if (message.status == "streaming") {
                    Text(
                        text = message.content,
                        color = MiuixTheme.colorScheme.onSurface,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                } else {
                    MarkdownUi.Content(
                        context = context,
                        markdown = message.content,
                        contentPadding = PaddingValues(top = if (message.toolEvents.isNotEmpty()) 8.dp else 4.dp),
                        bodyFontSize = 14.sp,
                        onCopyCode = { code ->
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? android.content.ClipboardManager
                            clipboard?.setPrimaryClip(ClipData.newPlainText("Agent 代码", code))
                            Toast.makeText(context, "代码已复制", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
            if (message.status == "interrupted" || message.status == "error") {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (message.status == "interrupted") "已中断" else "请求失败",
                        color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                        fontSize = 12.sp,
                        modifier = Modifier.weight(1f)
                    )
                    if (onContinue != null) {
                        TextButton(
                            text = "继续任务",
                            onClick = onContinue,
                            minWidth = 0.dp,
                            minHeight = 30.dp,
                            insideMargin = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            colors = ButtonDefaults.textButtonColorsPrimary()
                        )
                    }
                    if (onRetry != null) {
                        TextButton(
                            text = "重新开始",
                            onClick = onRetry,
                            minWidth = 0.dp,
                            minHeight = 30.dp,
                            insideMargin = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            colors = ButtonDefaults.textButtonColorsPrimary()
                        )
                    }
                }
            }
            if (message.role != "tool" && message.diff.isNotBlank()) {
                ScriptPluginAgentDiffGroup("message:${message.createdAt}", message.diff)
            }
        }
    }
}

internal fun isScriptPluginAgentControlPreview(content: String): Boolean {
    return content.trim().startsWith("准备调用")
}

@Composable
internal fun ScriptPluginAgentWorkingIndicator(startedAt: Long, label: String = "") {
    var elapsedSeconds by remember(startedAt) {
        mutableStateOf(((System.currentTimeMillis() - startedAt).coerceAtLeast(0L) / 1_000L))
    }
    LaunchedEffect(startedAt) {
        while (true) {
            elapsedSeconds = (System.currentTimeMillis() - startedAt).coerceAtLeast(0L) / 1_000L
            delay(1_000L)
        }
    }
    val elapsedText = if (elapsedSeconds < 60L) {
        "${elapsedSeconds}s"
    } else {
        "${elapsedSeconds / 60L}m ${elapsedSeconds % 60L}s"
    }
    Text(
        text = buildString {
            append("Working (").append(elapsedText).append(')')
            if (label.isNotBlank()) append(" · ").append(label)
        },
        color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium
    )
}

fun findAgentActivity(context: Context): Activity? {
    var current: Context? = context
    while (current is ContextWrapper) {
        if (current is Activity) return current
        current = current.baseContext
    }
    return current as? Activity
}

@Composable
internal fun ScriptPluginAgentReasoningGroup(message: ScriptPluginAgentChatMessage) {
    val context = LocalContext.current
    val reasoning = message.reasoning
    if (reasoning.isEmpty()) return
    var expanded by rememberSaveable(message.createdAt, "reasoning") {
        mutableStateOf(false)
    }
    val rotation by animateFloatAsState(
        targetValue = if (expanded) 90f else 0f,
        animationSpec = tween(220),
        label = "agentReasoningArrow"
    )
    Column(modifier = Modifier.fillMaxWidth().padding(top = 5.dp).animateContentSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(6.dp))
                .responsiveTap(onClick = {
                    expanded = !expanded
                })
                .padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                imageVector = NavIcons.Expand,
                contentDescription = if (expanded) "收起思考过程" else "展开思考过程",
                colorFilter = ColorFilter.tint(MiuixTheme.colorScheme.onSurfaceVariantSummary),
                modifier = Modifier.size(18.dp).graphicsLayer { rotationZ = rotation }
            )
            Text(
                text = if (message.status == "streaming") "思考中" else "思考过程",
                color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 7.dp)
            )
        }
        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn(tween(160)),
            exit = fadeOut(tween(120))
        ) {
            MarkdownUi.Text(
                context = context,
                text = reasoning,
                color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                fontSize = 12.sp,
                modifier = Modifier.fillMaxWidth().padding(start = 25.dp, bottom = 6.dp)
            )
        }
    }
}

@Composable
internal fun ScriptPluginAgentQuotedMessagePreview(
    quotedMessage: ScriptPluginAgentQuotedMessage,
    maxLines: Int,
    modifier: Modifier = Modifier,
    onClear: (() -> Unit)? = null
) {
    Row(
        modifier = modifier.fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(MiuixTheme.colorScheme.onSurface.copy(alpha = 0.05f))
            .padding(start = 8.dp, top = 6.dp, end = if (onClear == null) 8.dp else 4.dp, bottom = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = if (quotedMessage.role == "assistant") "引用 Agent" else "引用用户",
                color = MiuixTheme.colorScheme.primary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = quotedMessage.content,
                color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                fontSize = 11.sp,
                maxLines = maxLines,
                overflow = TextOverflow.Ellipsis
            )
        }
        if (onClear != null) {
            Image(
                imageVector = NavIcons.Close,
                contentDescription = "取消引用",
                colorFilter = ColorFilter.tint(MiuixTheme.colorScheme.onSurfaceVariantSummary),
                modifier = Modifier.padding(start = 6.dp).size(24.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .clickable(onClick = onClear)
                    .padding(5.dp)
            )
        }
    }
}

@Composable
internal fun ScriptPluginAgentToolGroup(message: ScriptPluginAgentChatMessage) {
    val context = LocalContext.current
    if (message.toolEvents.isEmpty()) return
    val streaming = message.status == "streaming"
    var expanded by rememberSaveable(message.createdAt) { mutableStateOf(streaming) }
    var userOverride by rememberSaveable(message.createdAt, "work-override") {
        mutableStateOf<Boolean?>(null)
    }
    var detailEvent by remember(message.createdAt) { mutableStateOf<ScriptPluginAgentToolEvent?>(null) }
    detailEvent?.let { event ->
        ScriptPluginAgentToolEventDialog(
            event = event,
            onCopy = { value ->
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? android.content.ClipboardManager
                clipboard?.setPrimaryClip(ClipData.newPlainText(event.name, value))
                Toast.makeText(context, "已复制", Toast.LENGTH_SHORT).show()
            },
            onDismiss = { detailEvent = null }
        )
    }
    LaunchedEffect(streaming) {
        if (userOverride == null) expanded = streaming
    }
    val rotation by animateFloatAsState(
        targetValue = if (expanded) 90f else 0f,
        animationSpec = tween(250),
        label = "agentWorkArrow"
    )
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 5.dp).animateContentSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(6.dp))
                .responsiveTap(onClick = {
                    expanded = !expanded
                    userOverride = expanded
                })
                .padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                imageVector = NavIcons.Expand,
                contentDescription = if (expanded) "收起" else "展开",
                colorFilter = ColorFilter.tint(MiuixTheme.colorScheme.onSurfaceVariantSummary),
                modifier = Modifier.size(18.dp).graphicsLayer { rotationZ = rotation }
            )
            Text(
                text = "工具调用（${message.toolEvents.size}）",
                color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 7.dp)
            )
        }
        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn(tween(180)),
            exit = fadeOut(tween(140))
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(start = 24.dp, bottom = 6.dp)) {
                message.toolEvents.forEach { event ->
                    ScriptPluginAgentToolEventRow(event) { detailEvent = event }
                }
            }
        }
    }
}

@Composable
internal fun ScriptPluginAgentToolEventRow(
    event: ScriptPluginAgentToolEvent,
    onClick: () -> Unit
) {
    val workspaceWrite = isScriptPluginAgentWorkspaceWriteEvent(event)
    val icon = when (event.kind) {
        "search" -> NavIcons.Search
        "plugin", "workspace" -> NavIcons.Modules
        else -> NavIcons.Compact
    }
    val statusText = when (event.status) {
        "queued" -> "排队"
        "running" -> "进行中"
        "success" -> "完成"
        "interrupted" -> "已中断"
        else -> "失败"
    }
    val durationMs = if (event.startedAt > 0L && event.finishedAt >= event.startedAt) {
        event.finishedAt - event.startedAt
    } else {
        0L
    }
    val durationText = if (durationMs >= 1_000L) {
        "${durationMs / 1_000L}.${(durationMs % 1_000L) / 100}s"
    } else if (durationMs > 0L) {
        "${durationMs}ms"
    } else {
        ""
    }
    val canOpen = if (workspaceWrite) {
        event.diff.isNotBlank()
    } else {
        event.arguments.isNotBlank() || event.diff.isNotBlank() || event.result.isNotBlank()
    }
    Column(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(6.dp))
            .then(if (canOpen) Modifier.responsiveTap(onClick = onClick) else Modifier)
            .padding(vertical = 5.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                imageVector = icon,
                contentDescription = null,
                colorFilter = ColorFilter.tint(MiuixTheme.colorScheme.primary),
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = event.name,
                color = MiuixTheme.colorScheme.primary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 8.dp).weight(1f)
            )
            Text(
                text = listOf(statusText, durationText).filter { it.isNotBlank() }.joinToString(" "),
                color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                fontSize = 11.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        if (workspaceWrite) {
            if (event.progress.isNotBlank() && event.status == "running") {
                Text(
                    text = event.progress,
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth().padding(start = 26.dp, top = 2.dp)
                )
            }
            return@Column
        }
        if (event.arguments.isNotBlank()) {
            Text(
                text = scriptPluginAgentToolPreview(event.arguments),
                color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth().padding(start = 26.dp, top = 2.dp)
            )
        }
        if (event.progress.isNotBlank() && event.status == "running") {
            Text(
                text = event.progress,
                color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                fontSize = 11.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth().padding(start = 26.dp, top = 2.dp)
            )
        }
        if (event.diff.isNotBlank()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(start = 26.dp, top = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    imageVector = NavIcons.Modules,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MiuixTheme.colorScheme.primary),
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "查看 Diff",
                    color = MiuixTheme.colorScheme.primary,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(start = 6.dp)
                )
            }
        }
        if (event.result.isNotBlank()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(start = 26.dp, top = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    imageVector = NavIcons.Back,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MiuixTheme.colorScheme.onSurfaceVariantSummary),
                    modifier = Modifier.size(14.dp).graphicsLayer { rotationZ = 180f }
                )
                Text(
                    text = scriptPluginAgentToolPreview(event.result),
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 6.dp).weight(1f)
                )
            }
        }
    }
}

@Composable
internal fun ScriptPluginAgentToolEventDialog(
    event: ScriptPluginAgentToolEvent,
    onCopy: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var fullResult by remember(event.id) { mutableStateOf<String?>(null) }
    var loadingFullResult by remember(event.id) { mutableStateOf(false) }
    WindowDialog(
        show = true,
        title = event.name,
        onDismissRequest = onDismiss,
        content = {
            Column(
                modifier = Modifier.fillMaxWidth().heightIn(max = 460.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                val workspaceWrite = isScriptPluginAgentWorkspaceWriteEvent(event)
                if (workspaceWrite && event.diff.isNotBlank()) {
                    ScriptPluginAgentDiffCodeBlock(event.diff)
                }
                if (!workspaceWrite && event.arguments.isNotBlank()) {
                    Text(
                        text = "调用参数",
                        color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
                    )
                    MarkdownUi.CodeBlock(event.arguments)
                }
                if (!workspaceWrite && event.diff.isNotBlank()) {
                    Text(
                        text = "代码差异",
                        color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
                    )
                    ScriptPluginAgentDiffCodeBlock(event.diff)
                }
                if (!workspaceWrite && event.result.isNotBlank()) {
                    Text(
                        text = if (event.status == "success") "执行结果" else "执行信息",
                        color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
                    )
                    ScriptPluginAgentToolResultBlock(event, event.result)
                    TextButton(
                        text = "复制结果",
                        onClick = { onCopy(event.result) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.textButtonColorsPrimary()
                    )
                }
                if (!workspaceWrite && event.resultHandle.isNotBlank()) {
                    Text(
                        text = "完整结果已保存（${event.resultLength} 字符）",
                        color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
                    )
                    TextButton(
                        text = if (loadingFullResult) "正在读取完整结果" else "加载完整结果",
                        enabled = !loadingFullResult,
                        onClick = {
                            loadingFullResult = true
                            scope.launch {
                                fullResult = withContext(Dispatchers.IO) {
                                    ScriptPluginAgentToolResultStore.readAll(context, event.resultHandle).getOrNull()
                                }
                                loadingFullResult = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.textButtonColorsPrimary()
                    )
                    fullResult?.let { value ->
                        ScriptPluginAgentToolResultBlock(event, value)
                        TextButton(
                            text = "复制完整结果",
                            onClick = { onCopy(value) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.textButtonColorsPrimary()
                        )
                    }
                }
                TextButton(
                    text = "关闭",
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    colors = ButtonDefaults.textButtonColorsPrimary()
                )
            }
        }
    )
}

internal fun isScriptPluginAgentWorkspaceWriteEvent(event: ScriptPluginAgentToolEvent): Boolean {
    if (event.name == "写入插件文件" || event.name == "修改插件文件") return true
    if (event.kind != "workspace") return false
    val protocolName = event.protocolName.lowercase(Locale.ROOT)
    return protocolName == "write_file" ||
        protocolName == "apply_patch" ||
        protocolName.endsWith(".write_file") ||
        protocolName.endsWith(".apply_patch") ||
        protocolName.endsWith("_write_file") ||
        protocolName.endsWith("_apply_patch")
}

@Composable
internal fun ScriptPluginAgentDiffGroup(stateKey: String, diff: String) {
    var expanded by rememberSaveable(stateKey, "diff") { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (expanded) 90f else 0f,
        animationSpec = tween(250),
        label = "agentDiffArrow"
    )
    Column(modifier = Modifier.fillMaxWidth().padding(top = 6.dp).animateContentSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(6.dp))
                .responsiveTap(onClick = { expanded = !expanded })
                .padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                imageVector = NavIcons.Expand,
                contentDescription = if (expanded) "收起代码变更" else "展开代码变更",
                colorFilter = ColorFilter.tint(MiuixTheme.colorScheme.onSurfaceVariantSummary),
                modifier = Modifier.size(18.dp).graphicsLayer { rotationZ = rotation }
            )
            Text(
                text = "代码变更",
                color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 7.dp)
            )
        }
        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn(tween(180)),
            exit = fadeOut(tween(140))
        ) {
            ScriptPluginAgentDiffCodeBlock(diff)
        }
    }
}

@Composable
internal fun ScriptPluginAgentToolResultBlock(event: ScriptPluginAgentToolEvent, value: String) {
    val isDiffTool = event.name == "查看代码差异" ||
        event.protocolName.contains("show_diff", ignoreCase = true)
    val diff = if (isDiffTool) scriptPluginAgentDiffFromToolResult(value) else null
    if (diff != null) {
        ScriptPluginAgentDiffCodeBlock(diff)
    } else {
        MarkdownUi.CodeBlock(value)
    }
}

internal fun scriptPluginAgentDiffFromToolResult(value: String): String? {
    return runCatching {
        JSONObject(value).optString("diff", "").takeIf { it.isNotBlank() }
    }.getOrNull()
}

@Composable
internal fun ScriptPluginAgentDiffCodeBlock(diff: String) {
    val darkTheme = isSystemInDarkTheme()
    val additionText = if (darkTheme) Color(0xFF7EE787) else Color(0xFF116329)
    val additionBackground = if (darkTheme) Color(0xFF123820) else Color(0xFFE6FFEC)
    val deletionText = if (darkTheme) Color(0xFFFFA198) else Color(0xFFCF222E)
    val deletionBackground = if (darkTheme) Color(0xFF3D1C20) else Color(0xFFFFEBE9)
    val hunkText = if (darkTheme) Color(0xFF79C0FF) else Color(0xFF0969DA)
    val hunkBackground = if (darkTheme) Color(0xFF162A42) else Color(0xFFDDF4FF)
    val neutralText = MiuixTheme.colorScheme.onSurface
    val headerText = MiuixTheme.colorScheme.onSurfaceVariantSummary

    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MiuixTheme.colorScheme.surfaceVariant)
            .horizontalScroll(rememberScrollState())
            .padding(vertical = 6.dp)
    ) {
        diff.lines().forEach { line ->
            val isHeader = line.startsWith("diff --git ") ||
                line.startsWith("--- ") ||
                line.startsWith("+++ ") ||
                line.startsWith("index ") ||
                line.startsWith("new file mode ") ||
                line.startsWith("deleted file mode ") ||
                line.startsWith("similarity index ") ||
                line.startsWith("rename from ") ||
                line.startsWith("rename to ") ||
                line.startsWith("Binary files ") ||
                line.startsWith("Only in ")
            val (textColor, backgroundColor) = when {
                line.startsWith("@@") -> hunkText to hunkBackground
                isHeader -> headerText to Color.Transparent
                line.startsWith("+") -> additionText to additionBackground
                line.startsWith("-") -> deletionText to deletionBackground
                else -> neutralText to Color.Transparent
            }
            Text(
                text = line.ifEmpty { " " },
                color = textColor,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                softWrap = false,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
                    .background(backgroundColor)
                    .padding(horizontal = 10.dp, vertical = 1.dp)
            )
        }
    }
}

internal fun scriptPluginAgentToolPreview(value: String): String {
    return value.replace(Regex("\\s+"), " ").trim()
}

@Composable
internal fun ScriptPluginAgentMessageMenu(
    message: ScriptPluginAgentChatMessage,
    onDismiss: () -> Unit,
    onCopy: () -> Unit,
    onReadAloud: () -> Unit,
    onQuote: () -> Unit,
    onRegenerate: (() -> Unit)?,
    onEditAndResend: (() -> Unit)?,
    onRollback: () -> Unit,
    onDelete: () -> Unit,
    onCreateBranch: () -> Unit,
    onInfo: () -> Unit
) {
    WindowDialog(
        show = true,
        title = "消息操作",
        onDismissRequest = onDismiss,
        content = {
            Column {
                ScriptPluginAgentMessageActionRow(NavIcons.Copy, "复制消息", onCopy)
                InsetDivider()
                ScriptPluginAgentMessageActionRow(NavIcons.Volume, "朗读消息", onReadAloud)
                InsetDivider()
                ScriptPluginAgentMessageActionRow(NavIcons.Quote, "引用消息", onQuote)
                if (onRegenerate != null) {
                    InsetDivider()
                    ScriptPluginAgentMessageActionRow(NavIcons.Refresh, "重新生成", onRegenerate)
                }
                if (onEditAndResend != null) {
                    InsetDivider()
                    ScriptPluginAgentMessageActionRow(NavIcons.Edit, "编辑并重发", onEditAndResend)
                }
                InsetDivider()
                ScriptPluginAgentMessageActionRow(NavIcons.History, "回滚到此处", onRollback)
                InsetDivider()
                ScriptPluginAgentMessageActionRow(NavIcons.Delete, "删除", onDelete)
                InsetDivider()
                ScriptPluginAgentMessageActionRow(NavIcons.Branch, "创建分支", onCreateBranch)
                InsetDivider()
                ScriptPluginAgentMessageActionRow(NavIcons.Info, "信息", onInfo)
                if (message.content.isBlank()) {
                    Text(
                        text = "当前消息没有正文",
                        color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }
    )
}

@Composable
internal fun ScriptPluginAgentMessageActionRow(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(6.dp))
            .responsiveTap(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            imageVector = icon,
            contentDescription = null,
            colorFilter = ColorFilter.tint(MiuixTheme.colorScheme.onSurfaceVariantSummary),
            modifier = Modifier.size(22.dp)
        )
        Text(
            text = text,
            color = MiuixTheme.colorScheme.onSurface,
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 14.dp)
        )
    }
}

@Composable
internal fun ScriptPluginAgentMessageInfoDialog(
    message: ScriptPluginAgentChatMessage,
    onDismiss: () -> Unit
) {
    val status = when (message.status) {
        "streaming" -> "生成中"
        "interrupted" -> "已中断"
        "error" -> "请求失败"
        else -> "已完成"
    }
    WindowDialog(
        show = true,
        title = "消息信息",
        onDismissRequest = onDismiss,
        content = {
            Column {
                InfoRow(
                    "角色",
                    when (message.role) {
                        "user" -> "用户"
                        "tool" -> "工具"
                        else -> "Agent"
                    }
                )
                InsetDivider()
                InfoRow("时间", scheduledTaskTimeText(message.createdAt))
                InsetDivider()
                InfoRow("状态", status)
                InsetDivider()
                InfoRow("字符数", message.content.length.toString())
                InsetDivider()
                InfoRow(
                    "Token 估算",
                    ScriptPluginAgentContext.estimateTokens("", listOf(message), null).toString()
                )
                if (message.attachments.isNotEmpty()) {
                    InsetDivider()
                    InfoRow("附件", message.attachments.size.toString())
                }
                TextButton(
                    text = "关闭",
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    colors = ButtonDefaults.textButtonColorsPrimary()
                )
            }
        }
    )
}

internal fun mergeScriptPluginAgentProgress(current: String, next: String): String {
    val cleanNext = next.trim()
    return cleanNext.ifBlank { current }
}

internal fun mergeScriptPluginAgentReply(current: String, incoming: String): String {
    if (incoming.isBlank()) return current
    return ScriptPluginAgentTextMerge.mergeReply(current, incoming)
}

internal fun Modifier.agentMessageLongPress(
    key: Long,
    enabled: Boolean,
    onLongPress: () -> Unit
): Modifier = pointerInput(key, enabled) {
    if (!enabled) return@pointerInput
    awaitEachGesture {
        val down = awaitFirstDown(
            requireUnconsumed = false,
            pass = PointerEventPass.Initial
        )
        val releasedOrMoved = withTimeoutOrNull(viewConfiguration.longPressTimeoutMillis) {
            while (true) {
                val event = awaitPointerEvent(PointerEventPass.Initial)
                val change = event.changes.firstOrNull { it.id == down.id }
                    ?: return@withTimeoutOrNull true
                if (!change.pressed) return@withTimeoutOrNull true
                if (
                    abs(change.position.x - down.position.x) > viewConfiguration.touchSlop ||
                    abs(change.position.y - down.position.y) > viewConfiguration.touchSlop
                ) {
                    return@withTimeoutOrNull true
                }
            }
            @Suppress("UNREACHABLE_CODE")
            false
        }
        if (releasedOrMoved == null) {
            onLongPress()
            while (true) {
                val event = awaitPointerEvent(PointerEventPass.Initial)
                val change = event.changes.firstOrNull { it.id == down.id }
                change?.consume()
                if (change == null || !change.pressed) break
            }
        }
    }
}

@Composable
internal fun ScriptPluginAgentHistoryDrawer(
    sessions: List<ScriptPluginAgentSession>,
    activeId: String,
    workingSessionIds: Set<String>,
    enabled: Boolean,
    onNewSession: () -> Unit,
    onSelect: (ScriptPluginAgentSession) -> Unit,
    onRename: (ScriptPluginAgentSession, String) -> Unit,
    onCommitOrder: (List<ScriptPluginAgentSession>) -> Unit,
    onSetPinned: (ScriptPluginAgentSession, Boolean) -> Unit,
    onSetLocked: (ScriptPluginAgentSession, Boolean) -> Unit,
    onDelete: (ScriptPluginAgentSession) -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val uiPrefs = remember(context) { HchatStorage.preferences(context, UI_PREFS_NAME) }
    var actionSessionId by remember { mutableStateOf<String?>(null) }
    var renameSessionId by remember { mutableStateOf<String?>(null) }
    var deleteSessionId by remember { mutableStateOf<String?>(null) }
    var draggedSessionId by remember { mutableStateOf<String?>(null) }
    var dragOrderChanged by remember { mutableStateOf(false) }
    var orderedSessions by remember { mutableStateOf(sessions) }
    var showGestureHint by remember {
        mutableStateOf(uiPrefs.getBoolean(KEY_AGENT_HISTORY_GESTURE_HINT, true))
    }
    var titleDraft by remember { mutableStateOf("") }
    val actionSession = actionSessionId?.let { id -> orderedSessions.firstOrNull { it.id == id } }
    val renameSession = renameSessionId?.let { id -> orderedSessions.firstOrNull { it.id == id } }
    val deleteSession = deleteSessionId?.let { id -> orderedSessions.firstOrNull { it.id == id } }

    LaunchedEffect(sessions) {
        if (draggedSessionId == null) orderedSessions = sessions
    }

    fun movedSessions(session: ScriptPluginAgentSession, delta: Int): List<ScriptPluginAgentSession>? {
        val next = orderedSessions.toMutableList()
        val from = next.indexOfFirst { it.id == session.id }
        val to = from + delta
        if (from !in next.indices || to !in next.indices || next[from].pinned != next[to].pinned) return null
        val moved = next.removeAt(from)
        next.add(to, moved)
        return next
    }

    if (renameSession != null) {
        WindowDialog(
            show = true,
            title = "编辑标题",
            onDismissRequest = { renameSessionId = null },
            content = {
                Column {
                    InputRow(
                        title = "对话标题",
                        summary = "最多 32 个字符",
                        value = titleDraft,
                        onValueChange = { titleDraft = it.take(32) }
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        TextButton(
                            text = "取消",
                            onClick = { renameSessionId = null },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.textButtonColorsPrimary()
                        )
                        TextButton(
                            text = "保存",
                            onClick = {
                                if (titleDraft.isNotBlank()) {
                                    onRename(renameSession, titleDraft)
                                    renameSessionId = null
                                }
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.textButtonColorsPrimary()
                        )
                    }
                }
            }
        )
    }
    if (deleteSession != null) {
        WindowDialog(
            show = true,
            title = "删除对话",
            onDismissRequest = { deleteSessionId = null },
            content = {
                Column {
                    Text(
                        text = "确定删除“${deleteSession.title.ifBlank { "新对话" }}”吗？此操作无法撤销。",
                        color = MiuixTheme.colorScheme.onSurface,
                        fontSize = 14.sp
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 14.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        TextButton(
                            text = "取消",
                            onClick = { deleteSessionId = null },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.textButtonColorsPrimary()
                        )
                        TextButton(
                            text = "删除",
                            onClick = {
                                deleteSessionId = null
                                onDelete(deleteSession)
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.textButtonColorsPrimary()
                        )
                    }
                }
            }
        )
    }
    if (actionSession != null) {
        val index = orderedSessions.indexOfFirst { it.id == actionSession.id }
        val canMoveUp = index > 0 && orderedSessions[index - 1].pinned == actionSession.pinned
        val canMoveDown = index in 0 until orderedSessions.lastIndex &&
            orderedSessions[index + 1].pinned == actionSession.pinned
        WindowDialog(
            show = true,
            title = "对话历史",
            onDismissRequest = { actionSessionId = null },
            content = {
                Column {
                    Text(
                        text = actionSession.title.ifBlank { "新对话" },
                        color = MiuixTheme.colorScheme.onSurface,
                        fontSize = 15.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp, bottom = 10.dp),
                        textAlign = TextAlign.Center
                    )
                    ScriptPluginAgentHistoryActionRow(NavIcons.Edit, "编辑标题") {
                        titleDraft = actionSession.title
                        renameSessionId = actionSession.id
                        actionSessionId = null
                    }
                    ScriptPluginAgentHistoryActionRow(NavIcons.MoveUp, "上移", enabled = canMoveUp) {
                        movedSessions(actionSession, -1)?.let { next ->
                            orderedSessions = next
                            onCommitOrder(next)
                        }
                        actionSessionId = null
                    }
                    ScriptPluginAgentHistoryActionRow(NavIcons.MoveDown, "下移", enabled = canMoveDown) {
                        movedSessions(actionSession, 1)?.let { next ->
                            orderedSessions = next
                            onCommitOrder(next)
                        }
                        actionSessionId = null
                    }
                    ScriptPluginAgentHistoryActionRow(
                        NavIcons.Pin,
                        if (actionSession.pinned) "取消置顶" else "置顶聊天"
                    ) {
                        onSetPinned(actionSession, !actionSession.pinned)
                        actionSessionId = null
                    }
                    ScriptPluginAgentHistoryActionRow(
                        if (actionSession.locked) NavIcons.Unlock else NavIcons.Lock,
                        if (actionSession.locked) "解锁聊天" else "锁定聊天"
                    ) {
                        onSetLocked(actionSession, !actionSession.locked)
                        actionSessionId = null
                    }
                    ScriptPluginAgentHistoryActionRow(
                        NavIcons.Delete,
                        if (actionSession.locked) "删除（已锁定）" else "删除",
                        destructive = true,
                        enabled = !actionSession.locked
                    ) {
                        deleteSessionId = actionSession.id
                        actionSessionId = null
                    }
                    TextButton(
                        text = "取消",
                        onClick = { actionSessionId = null },
                        modifier = Modifier.fillMaxWidth().padding(top = 2.dp),
                        colors = ButtonDefaults.textButtonColorsPrimary()
                    )
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.28f)).responsiveTap(onClick = onClose)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth(0.84f)
                .fillMaxHeight()
                .background(MiuixTheme.colorScheme.background)
                .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "对话历史",
                    color = MiuixTheme.colorScheme.onSurface,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )
                TextButton(
                    text = "关闭",
                    onClick = onClose,
                    colors = ButtonDefaults.textButtonColorsPrimary()
                )
            }
            TextButton(
                text = "+  新建对话",
                onClick = onNewSession,
                enabled = enabled,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 4.dp),
                colors = ButtonDefaults.textButtonColorsPrimary()
            )
            if (showGestureHint) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .responsiveTap(onClick = {
                            showGestureHint = false
                            uiPrefs.edit().putBoolean(KEY_AGENT_HISTORY_GESTURE_HINT, false).apply()
                        })
                        .padding(horizontal = 18.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        imageVector = NavIcons.Swipe,
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MiuixTheme.colorScheme.onSurfaceVariantSummary),
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "左右滑动可编辑或删除（点击不再显示）",
                        color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 9.dp)
                    )
                }
            }
            InsetDivider()
            LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(vertical = 8.dp)) {
                orderedSessions.forEach { session ->
                    item(key = session.id) {
                        ScriptPluginAgentHistoryItem(
                            modifier = if (draggedSessionId == session.id) {
                                Modifier
                            } else {
                                Modifier.animateItem(
                                    fadeInSpec = null,
                                    placementSpec = tween(durationMillis = 180),
                                    fadeOutSpec = null
                                )
                            },
                            session = session,
                            selected = session.id == activeId,
                            working = session.id in workingSessionIds,
                            enabled = enabled,
                            onSelect = { onSelect(session) },
                            onLongPress = { actionSessionId = session.id },
                            onSwipeRename = {
                                titleDraft = session.title
                                renameSessionId = session.id
                            },
                            onSwipeDelete = {
                                if (session.locked) {
                                    Toast.makeText(context, "请先解锁该对话", Toast.LENGTH_SHORT).show()
                                } else {
                                    deleteSessionId = session.id
                                }
                            },
                            onDragMove = { delta ->
                                movedSessions(session, delta)?.let { next ->
                                    orderedSessions = next
                                    dragOrderChanged = true
                                    true
                                } ?: false
                            },
                            onDragStateChange = { dragging ->
                                if (dragging) {
                                    draggedSessionId = session.id
                                    dragOrderChanged = false
                                } else if (draggedSessionId == session.id) {
                                    draggedSessionId = null
                                    if (dragOrderChanged) onCommitOrder(orderedSessions)
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
internal fun ScriptPluginAgentWorkingSpinner(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "agentHistoryWorking")
    val spinnerColor = MiuixTheme.colorScheme.primary
    val rotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = LinearEasing)
        ),
        label = "agentHistoryWorkingRotation"
    )
    Canvas(
        modifier = modifier
            .size(18.dp)
            .graphicsLayer { rotationZ = rotation }
    ) {
        drawArc(
            color = spinnerColor,
            startAngle = -90f,
            sweepAngle = 285f,
            useCenter = false,
            style = Stroke(width = 2.dp.toPx())
        )
    }
}

@Composable
internal fun ScriptPluginAgentHistoryItem(
    modifier: Modifier = Modifier,
    session: ScriptPluginAgentSession,
    selected: Boolean,
    working: Boolean,
    enabled: Boolean,
    onSelect: () -> Unit,
    onLongPress: () -> Unit,
    onSwipeRename: () -> Unit,
    onSwipeDelete: () -> Unit,
    onDragMove: (Int) -> Boolean,
    onDragStateChange: (Boolean) -> Unit
) {
    val density = LocalDensity.current
    val actionWidthPx = with(density) { 82.dp.toPx() }
    var offsetX by remember(session.id) { mutableStateOf(0f) }
    var itemHeightPx by remember(session.id) { mutableStateOf(0f) }
    var dragDistanceY by remember(session.id) { mutableStateOf(0f) }
    var dragging by remember(session.id) { mutableStateOf(false) }
    val currentOnDragMove by rememberUpdatedState(onDragMove)
    val currentOnDragStateChange by rememberUpdatedState(onDragStateChange)
    val visualDragY by animateFloatAsState(
        targetValue = dragDistanceY,
        animationSpec = tween(durationMillis = if (dragging) 0 else 170),
        label = "AgentHistoryDragOffset"
    )
    val dragScale by animateFloatAsState(
        targetValue = if (dragging) 1.015f else 1f,
        animationSpec = tween(durationMillis = 120),
        label = "AgentHistoryDragScale"
    )
    Box(
        modifier = modifier
            .zIndex(if (dragging) 1f else 0f)
            .graphicsLayer {
                translationY = visualDragY
                scaleX = dragScale
                scaleY = dragScale
                shadowElevation = if (dragging) with(density) { 8.dp.toPx() } else 0f
                shape = RoundedCornerShape(8.dp)
            }
            .fillMaxWidth().padding(horizontal = 12.dp, vertical = 3.dp)
            .clip(RoundedCornerShape(8.dp))
    ) {
        if (offsetX != 0f) {
            Box(
                modifier = Modifier.matchParentSize(),
                contentAlignment = if (offsetX > 0f) Alignment.CenterStart else Alignment.CenterEnd
            ) {
                Box(
                    modifier = Modifier.fillMaxHeight().sizeIn(minWidth = 82.dp)
                        .background(
                            if (offsetX > 0f) MiuixTheme.colorScheme.primary.copy(alpha = 0.88f)
                            else Color(0xFFD93025)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (offsetX > 0f) "编辑" else "删除",
                        color = Color.White,
                        fontSize = 13.sp
                    )
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth()
                .onSizeChanged { itemHeightPx = it.height.toFloat() }
                .graphicsLayer { translationX = offsetX }
                .background(MiuixTheme.colorScheme.background)
                .background(
                    if (selected) MiuixTheme.colorScheme.primary.copy(alpha = 0.13f)
                    else Color.Transparent
                )
                .pointerInput(session.id, enabled) {
                    if (!enabled) return@pointerInput
                    awaitEachGesture {
                        val down = awaitFirstDown(requireUnconsumed = false)
                        var totalX = 0f
                        var totalY = 0f
                        var horizontal = false
                        while (true) {
                            val event = awaitPointerEvent()
                            val change = event.changes.firstOrNull { it.id == down.id } ?: break
                            totalX += change.position.x - change.previousPosition.x
                            totalY += change.position.y - change.previousPosition.y
                            if (!horizontal && abs(totalX) > viewConfiguration.touchSlop && abs(totalX) > abs(totalY) * 1.25f) {
                                horizontal = true
                            }
                            if (horizontal) {
                                offsetX = totalX.coerceIn(-actionWidthPx, actionWidthPx)
                                change.consume()
                            }
                            if (!change.pressed) break
                        }
                        val action = when {
                            offsetX >= actionWidthPx * 0.58f -> 1
                            offsetX <= -actionWidthPx * 0.58f -> -1
                            else -> 0
                        }
                        offsetX = 0f
                        if (action > 0) onSwipeRename() else if (action < 0) onSwipeDelete()
                    }
                }
                .pointerInput(session.id, enabled) {
                    detectTapGestures(
                        onTap = { if (enabled) onSelect() },
                        onLongPress = { position ->
                            if (enabled && position.x > with(density) { 38.dp.toPx() }) onLongPress()
                        }
                    )
                }
                .padding(horizontal = 14.dp, vertical = 11.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                imageVector = NavIcons.Drag,
                contentDescription = "长按拖动排序",
                colorFilter = ColorFilter.tint(MiuixTheme.colorScheme.onSurfaceVariantSummary),
                modifier = Modifier.size(22.dp)
                    .pointerInput(session.id, enabled, itemHeightPx) {
                        if (!enabled) return@pointerInput
                        detectDragGesturesAfterLongPress(
                            onDragStart = {
                                dragDistanceY = 0f
                                dragging = true
                                currentOnDragStateChange(true)
                            },
                            onDragEnd = {
                                dragging = false
                                dragDistanceY = 0f
                                currentOnDragStateChange(false)
                            },
                            onDragCancel = {
                                dragging = false
                                dragDistanceY = 0f
                                currentOnDragStateChange(false)
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                dragDistanceY += dragAmount.y
                                val step = (itemHeightPx.takeIf { it > 0f }
                                    ?: with(density) { 56.dp.toPx() }) + with(density) { 6.dp.toPx() }
                                while (dragDistanceY <= -step * 0.5f) {
                                    if (currentOnDragMove(-1)) {
                                        dragDistanceY += step
                                    } else {
                                        dragDistanceY = -step * 0.45f
                                        break
                                    }
                                }
                                while (dragDistanceY >= step * 0.5f) {
                                    if (currentOnDragMove(1)) {
                                        dragDistanceY -= step
                                    } else {
                                        dragDistanceY = step * 0.45f
                                        break
                                    }
                                }
                            }
                        )
                    }
            )
            Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                Text(
                    text = session.title.ifBlank { "新对话" },
                    color = MiuixTheme.colorScheme.onSurface,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                val lastUser = session.messages.lastOrNull { it.role == "user" }?.content.orEmpty()
                if (lastUser.isNotBlank() && lastUser != session.title) {
                    Text(
                        text = lastUser,
                        color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 3.dp)
                    )
                }
            }
            if (session.pinned) {
                Image(
                    imageVector = NavIcons.Pin,
                    contentDescription = "已置顶",
                    colorFilter = ColorFilter.tint(MiuixTheme.colorScheme.primary),
                    modifier = Modifier.padding(start = 6.dp).size(17.dp)
                )
            }
            if (session.locked) {
                Image(
                    imageVector = NavIcons.Lock,
                    contentDescription = "已锁定",
                    colorFilter = ColorFilter.tint(MiuixTheme.colorScheme.onSurfaceVariantSummary),
                    modifier = Modifier.padding(start = 6.dp).size(17.dp)
                )
            }
            if (working) {
                ScriptPluginAgentWorkingSpinner(Modifier.padding(start = 8.dp))
            }
        }
    }
}

@Composable
internal fun ScriptPluginAgentHistoryActionRow(
    icon: ImageVector,
    text: String,
    destructive: Boolean = false,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val contentColor = when {
        !enabled -> MiuixTheme.colorScheme.onSurfaceVariantSummary.copy(alpha = 0.45f)
        destructive -> Color(0xFFD93025)
        else -> MiuixTheme.colorScheme.onSurface
    }
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (destructive) Color(0xFFD93025).copy(alpha = 0.08f)
                else MiuixTheme.colorScheme.secondaryVariant
            )
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            imageVector = icon,
            contentDescription = null,
            colorFilter = ColorFilter.tint(contentColor),
            modifier = Modifier.size(22.dp)
        )
        Text(
            text = text,
            color = contentColor,
            fontSize = 15.sp,
            modifier = Modifier.padding(start = 18.dp)
        )
    }
}

@Composable
internal fun ScriptPluginAgentQuickProfileDialog(
    profiles: List<ScriptPluginAgentProfile>,
    activeProfileId: String,
    onSelected: (ScriptPluginAgentProfile) -> Unit,
    onDismiss: () -> Unit
) {
    WindowDialog(
        show = true,
        title = "切换模型配置",
        onDismissRequest = onDismiss,
        content = {
            LazyColumn(modifier = Modifier.fillMaxWidth().heightIn(max = 360.dp)) {
                profiles.forEach { profile ->
                    item(key = profile.id) {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .clickable { onSelected(profile) }
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = profile.name,
                                    color = MiuixTheme.colorScheme.onSurface,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = profile.config.model.ifBlank { "未设置模型" },
                                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                                    fontSize = 12.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            if (profile.id == activeProfileId) {
                                Text(
                                    text = "当前",
                                    color = MiuixTheme.colorScheme.primary,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(start = 12.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
internal fun ScriptPluginAgentQuickOptionsDialog(
    webSearchEnabled: Boolean,
    workspaceWriteApprovalMode: String,
    promptCacheMode: String,
    endpointMode: String,
    mcpServers: List<ScriptPluginAgentMcpServer>,
    onWebSearchChanged: (Boolean) -> Unit,
    onWorkspaceWriteApprovalChanged: (String) -> Unit,
    onPromptCacheModeChanged: (String) -> Unit,
    onMcpChanged: (String, Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    WindowDialog(
        show = true,
        title = "快捷选项",
        onDismissRequest = onDismiss,
        content = {
            Column(modifier = Modifier.fillMaxWidth()) {
                SwitchRow(
                    checked = webSearchEnabled,
                    title = "联网搜索",
                    summary = if (webSearchEnabled) "允许 Agent 按需查询公开资料" else "已关闭，Agent 不会发起搜索",
                    onCheckedChange = onWebSearchChanged
                )
                InsetDivider()
                PopupChoiceRow(
                    title = "插件文件修改确认",
                    summary = if (workspaceWriteApprovalMode == ScriptPluginAgentSettings.WRITE_APPROVAL_ASK) {
                        "每次询问工具写入和最终提交"
                    } else {
                        "始终允许并自动提交"
                    },
                    options = listOf(
                        PopupChoice("每次询问", ScriptPluginAgentSettings.WRITE_APPROVAL_ASK),
                        PopupChoice("始终允许", ScriptPluginAgentSettings.WRITE_APPROVAL_ALWAYS_ALLOW)
                    ),
                    currentValue = workspaceWriteApprovalMode,
                    onValueChanged = onWorkspaceWriteApprovalChanged
                )
                InsetDivider()
                PopupChoiceRow(
                    title = "提示缓存",
                    summary = promptCacheModeSummary(promptCacheMode, endpointMode),
                    options = promptCacheModeChoices(),
                    currentValue = promptCacheMode,
                    onValueChanged = onPromptCacheModeChanged
                )
                if (mcpServers.isNotEmpty()) {
                    InsetDivider()
                    Text(
                        text = "MCP 服务器",
                        color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    mcpServers.forEachIndexed { index, server ->
                        if (index > 0) InsetDivider()
                        SwitchRow(
                            checked = server.enabled,
                            title = server.name.ifBlank { "MCP ${index + 1}" },
                            summary = server.endpoint.ifBlank { "未配置 Endpoint" },
                            onCheckedChange = { onMcpChanged(server.id, it) }
                        )
                    }
                } else {
                    Text(
                        text = "暂无 MCP 服务器，请先在 Agent 配置中添加。",
                        color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                    )
                }
            }
        }
    )
}

@Composable
internal fun ScriptPluginAgentPendingMessagePanel(
    messages: List<ScriptPluginAgentPendingMessage>,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onEdit: (Long) -> Unit,
    onDelete: (Long) -> Unit,
    onSendNow: (Long) -> Unit
) {
    if (messages.isEmpty()) return
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(start = 10.dp, top = 8.dp, end = 10.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(MiuixTheme.colorScheme.secondaryVariant)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().clickable { onExpandedChange(!expanded) }
                .padding(horizontal = 10.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "待发送 ${messages.size}",
                color = MiuixTheme.colorScheme.onSurface,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            Image(
                imageVector = if (expanded) NavIcons.MoveUp else NavIcons.MoveDown,
                contentDescription = if (expanded) "收起待发送消息" else "展开待发送消息",
                colorFilter = ColorFilter.tint(MiuixTheme.colorScheme.onSurfaceVariantSummary),
                modifier = Modifier.size(18.dp)
            )
        }
        if (expanded) {
            LazyColumn(modifier = Modifier.fillMaxWidth().heightIn(max = 150.dp)) {
                messages.forEachIndexed { index, message ->
                    item(key = message.id) {
                        if (index > 0) InsetDivider()
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(start = 10.dp, end = 6.dp, top = 5.dp, bottom = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = buildString {
                                    if (message.quotedMessage != null) append("引用 · ")
                                    append(message.content.ifBlank { "${message.attachments.size} 个附件" })
                                },
                                color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                            ScriptPluginAgentPendingMessageAction(
                                icon = NavIcons.Edit,
                                description = "编辑待发送消息"
                            ) { onEdit(message.id) }
                            ScriptPluginAgentPendingMessageAction(
                                icon = NavIcons.Send,
                                description = "立即发送"
                            ) { onSendNow(message.id) }
                            ScriptPluginAgentPendingMessageAction(
                                icon = NavIcons.Delete,
                                description = "删除待发送消息"
                            ) { onDelete(message.id) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun ScriptPluginAgentPendingMessageAction(
    icon: ImageVector,
    description: String,
    onClick: () -> Unit
) {
    Image(
        imageVector = icon,
        contentDescription = description,
        colorFilter = ColorFilter.tint(MiuixTheme.colorScheme.onSurfaceVariantSummary),
        modifier = Modifier.padding(start = 4.dp).size(26.dp)
            .clip(RoundedCornerShape(4.dp))
            .clickable(onClick = onClick)
            .padding(5.dp)
    )
}

@Composable
internal fun ScriptPluginAgentQuickChoice(
    label: String,
    value: String,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier.height(28.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(MiuixTheme.colorScheme.secondaryVariant)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$label · $value",
            color = if (enabled) {
                MiuixTheme.colorScheme.onSurfaceVariantSummary
            } else {
                MiuixTheme.colorScheme.onSurfaceVariantSummary.copy(alpha = 0.5f)
            },
            fontSize = 11.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        Image(
            imageVector = NavIcons.MoveDown,
            contentDescription = null,
            colorFilter = ColorFilter.tint(MiuixTheme.colorScheme.onSurfaceVariantSummary),
            modifier = Modifier.padding(start = 3.dp).size(14.dp)
        )
    }
}

@Composable
internal fun ScriptPluginAgentComposer(
    value: String,
    attachments: List<ScriptPluginAgentAttachment>,
    quotedMessage: ScriptPluginAgentQuotedMessage?,
    pendingMessages: List<ScriptPluginAgentPendingMessage>,
    pendingMessagesExpanded: Boolean,
    activeProfileName: String,
    activeModel: String,
    enabled: Boolean,
    generating: Boolean,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    onQueue: () -> Unit,
    onCancel: () -> Unit,
    onAttach: () -> Unit,
    onRemoveAttachment: (String) -> Unit,
    onClearQuotedMessage: () -> Unit,
    onPendingMessagesExpandedChange: (Boolean) -> Unit,
    onEditPendingMessage: (Long) -> Unit,
    onDeletePendingMessage: (Long) -> Unit,
    onSendPendingMessageNow: (Long) -> Unit,
    onOpenProfilePicker: () -> Unit,
    onOpenModelPicker: () -> Unit,
    onOpenQuickOptions: () -> Unit
) {
    val canSend = value.isNotBlank() || attachments.isNotEmpty()
    val actionEnabled = enabled && canSend
    val actionBackground = MiuixTheme.colorScheme.primary
    val actionTint = Color.White
    Column(
        modifier = Modifier.fillMaxWidth()
            .background(MiuixTheme.colorScheme.background.copy(alpha = 0.96f))
    ) {
        quotedMessage?.let { quoted ->
            ScriptPluginAgentQuotedMessagePreview(
                quotedMessage = quoted,
                maxLines = 1,
                modifier = Modifier.padding(start = 10.dp, top = 8.dp, end = 10.dp),
                onClear = onClearQuotedMessage
            )
        }
        ScriptPluginAgentPendingMessagePanel(
            messages = pendingMessages,
            expanded = pendingMessagesExpanded,
            onExpandedChange = onPendingMessagesExpandedChange,
            onEdit = onEditPendingMessage,
            onDelete = onDeletePendingMessage,
            onSendNow = onSendPendingMessageNow
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 10.dp, top = 8.dp, end = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            ScriptPluginAgentQuickChoice(
                label = "配置",
                value = activeProfileName.ifBlank { "默认配置" },
                enabled = enabled,
                modifier = Modifier.weight(1f),
                onClick = onOpenProfilePicker
            )
            ScriptPluginAgentQuickChoice(
                label = "模型",
                value = activeModel.ifBlank { "未设置" },
                enabled = enabled,
                modifier = Modifier.weight(1f),
                onClick = onOpenModelPicker
            )
            Image(
                imageVector = NavIcons.Settings,
                contentDescription = "快捷选项",
                colorFilter = ColorFilter.tint(MiuixTheme.colorScheme.onSurfaceVariantSummary),
                modifier = Modifier.size(28.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(MiuixTheme.colorScheme.secondaryVariant)
                    .clickable(enabled = enabled, onClick = onOpenQuickOptions)
                    .padding(6.dp)
            )
        }
        if (attachments.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())
                    .padding(start = 12.dp, top = 8.dp, end = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                attachments.forEach { attachment ->
                    Row(
                        modifier = Modifier.clip(RoundedCornerShape(6.dp))
                            .background(MiuixTheme.colorScheme.secondaryVariant)
                            .padding(start = 8.dp, top = 6.dp, bottom = 6.dp, end = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = attachment.name,
                            color = MiuixTheme.colorScheme.onSurface,
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.sizeIn(maxWidth = 180.dp)
                        )
                        Image(
                            imageVector = NavIcons.Close,
                            contentDescription = "移除附件",
                            colorFilter = ColorFilter.tint(MiuixTheme.colorScheme.onSurfaceVariantSummary),
                            modifier = Modifier.padding(start = 4.dp).size(18.dp)
                                .responsiveTap(onClick = { onRemoveAttachment(attachment.path) })
                        )
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 10.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MiuixTheme.colorScheme.secondaryVariant)
                .padding(start = 8.dp, top = 9.dp, end = 12.dp, bottom = 9.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Image(
                imageVector = NavIcons.Attach,
                contentDescription = "添加附件",
                colorFilter = ColorFilter.tint(MiuixTheme.colorScheme.onSurfaceVariantSummary),
                modifier = Modifier.padding(end = 8.dp, bottom = 1.dp).size(24.dp)
                    .responsiveTap(onClick = onAttach)
            )
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                enabled = enabled,
                minLines = 1,
                maxLines = 4,
                textStyle = TextStyle(color = MiuixTheme.colorScheme.onSurface, fontSize = 14.sp),
                modifier = Modifier.weight(1f).align(Alignment.Top).heightIn(min = 34.dp),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxWidth().heightIn(min = 34.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (value.isBlank()) {
                            Text(
                                text = "输入消息",
                                color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                                fontSize = 14.sp
                            )
                        }
                        innerTextField()
                    }
                }
            )
            if (generating) {
                Image(
                    imageVector = NavIcons.Stop,
                    contentDescription = "停止生成",
                    colorFilter = ColorFilter.tint(Color(0xFFD93025)),
                    modifier = Modifier.padding(start = 6.dp).size(30.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .background(Color.White)
                        .clickable(onClick = onCancel)
                        .padding(7.dp)
                )
            }
            if (!generating || canSend) {
                Image(
                    imageVector = if (generating) NavIcons.Add else NavIcons.Send,
                    contentDescription = if (generating) "加入待发送队列" else "发送",
                    colorFilter = ColorFilter.tint(actionTint),
                    modifier = Modifier.padding(start = 6.dp)
                        .size(34.dp)
                        .clip(RoundedCornerShape(17.dp))
                        .background(actionBackground)
                        .clickable(enabled = actionEnabled) {
                            if (generating) onQueue() else onSend()
                        }
                        .padding(7.dp)
                )
            }
        }
    }
}

}
@Composable
internal fun ClickHintTag() {
    Text(
        text = "单击",
        color = MiuixTheme.colorScheme.primary,
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium,
        maxLines = 1,
        softWrap = false,
        overflow = TextOverflow.Clip,
        modifier = Modifier
            .defaultMinSize(minWidth = 40.dp)
            .clip(RoundedCornerShape(999.dp))
            .background(MiuixTheme.colorScheme.primary.copy(alpha = 0.12f))
            .padding(horizontal = 8.dp, vertical = 2.dp)
    )
}

internal object ScriptPluginSettingsMiuixContent {

@Composable
fun ScriptPluginSettingsContent(
    context: Context,
    onOpenReadme: (ScriptPluginRuntime.ScriptPlugin) -> Unit,
    onOpenMarket: () -> Unit,
    onOpenAgent: () -> Unit,
    onOpenManager: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val sp = remember { HchatStorage.preferences(context, ScriptPluginSettings.PREFS_NAME) }
    val pluginRootPath = remember(context) { ScriptPluginRuntime.scriptDir(context).absolutePath }
    var pluginListVersion by remember { mutableStateOf(0) }
    val managedPlugins = remember(pluginListVersion) { ScriptPluginManager.listForDisplay(context) }
    val plugins = remember(managedPlugins) { managedPlugins.map { it.plugin } }
    val pinnedIds = remember(managedPlugins) {
        managedPlugins.filter { it.pinned }.mapTo(LinkedHashSet()) { it.plugin.id }
    }
    var globalEnabled by remember {
        mutableStateOf(sp.getBoolean(ScriptPluginSettings.KEY_ENABLE, ScriptPluginSettings.DEFAULT_ENABLE))
    }
    var pluginEnabledStates by remember(plugins) {
        mutableStateOf(plugins.associate { it.id to ScriptPluginRuntime.isPluginEnabled(context, it.id) })
    }
    var showPluginRootDialog by remember { mutableStateOf(false) }
    var actionPlugin by remember { mutableStateOf<ScriptPluginRuntime.ScriptPlugin?>(null) }
    var renamePlugin by remember { mutableStateOf<ScriptPluginRuntime.ScriptPlugin?>(null) }
    var deletePlugin by remember { mutableStateOf<ScriptPluginRuntime.ScriptPlugin?>(null) }
    DisposableEffect(context) {
        val subscription = ScriptPluginRuntime.subscribePluginCatalog(context) {
            Handler(Looper.getMainLooper()).post {
                pluginEnabledStates = ScriptPluginRuntime.listPlugins(context)
                    .associate { it.id to ScriptPluginRuntime.isPluginEnabled(context, it.id) }
                globalEnabled = sp.getBoolean(
                    ScriptPluginSettings.KEY_ENABLE,
                    ScriptPluginSettings.DEFAULT_ENABLE
                )
                pluginListVersion++
            }
        }
        onDispose { subscription.unsubscribe() }
    }

    Column {
        SettingsCard {
            PathSwitchRow(
                globalEnabled,
                "插件总开关",
                "启动时自动加载已启用插件\n相关说明:\n请确认插件安全再进行加载,\n否则造成的后果需自行承担。",
                onInfoClick = { showPluginRootDialog = true },
            ) { next ->
                val old = globalEnabled
                globalEnabled = next
                Thread({
                    val result = ScriptPluginRuntime.setGlobalEnabled(context, next)
                    Handler(Looper.getMainLooper()).post {
                        if (result.isFailure) {
                            globalEnabled = old
                            Toast.makeText(
                                context,
                                "切换失败: ${result.exceptionOrNull()?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            pluginListVersion++
                        }
                    }
                }, "Hchat-Script-Global").start()
            }
        }
        if (showPluginRootDialog) {
            ScriptPluginPathDialog(
                context = context,
                title = "插件目录",
                path = pluginRootPath,
                onClose = { showPluginRootDialog = false }
            )
        }
        SettingsCard(modifier = Modifier.padding(top = 10.dp)) {
            ActionRow("插件 Agent", "按需求生成或修改脚本插件") {
                onOpenAgent()
            }
        }
        SettingsCard(modifier = Modifier.padding(top = 10.dp)) {
            ActionRow("在线插件", "浏览、安装或上传社区脚本插件") {
                onOpenMarket()
            }
        }
        SettingsCard(modifier = Modifier.padding(top = 10.dp)) {
            ActionRow("本地插件管理", "排序、置顶、导入、导出或批量管理插件") {
                onOpenManager()
            }
        }
        SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "本地插件(${plugins.size})")
        SettingsCard {
            if (plugins.isEmpty()) {
                Text(
                    text = "暂无插件",
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
                )
            } else {
                plugins.forEachIndexed { index, plugin ->
                    val checked = pluginEnabledStates[plugin.id]
                        ?: ScriptPluginRuntime.isPluginEnabled(context, plugin.id)
                    val summary = buildString {
                        append(plugin.dir.name)
                        append("\n")
                        append("作者: ")
                        append(plugin.author.ifBlank { "未知" })
                        append(" | 更新于: ")
                        append(plugin.updateTime.ifBlank { "未知" })
                    }
                    val title = buildString {
                        append(plugin.displayName ?: "未知")
                        append("(")
                        append(plugin.version.ifBlank { "未知" })
                        append(")")
                    }
                    ScriptPluginRow(
                        checked = checked,
                        title = title,
                        summary = summary,
                        showSettings = ScriptPluginRuntime.canOpenSettings(plugin),
                        onOpenReadme = { onOpenReadme(plugin) },
                        onOpenSettings = {
                            val result = ScriptPluginRuntime.callOpenSettings(plugin.id)
                            if (result.isFailure) {
                                Toast.makeText(
                                    context,
                                    result.exceptionOrNull()?.message ?: "打开设置失败",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        onOpenManager = { actionPlugin = plugin },
                        onCheckedChange = { next ->
                            val oldMap = pluginEnabledStates
                            pluginEnabledStates = pluginEnabledStates + (plugin.id to next)
                            Thread({
                                val result = ScriptPluginRuntime.setPluginEnabled(context, plugin.id, next)
                                Handler(Looper.getMainLooper()).post {
                                    if (result.isFailure) {
                                        pluginEnabledStates = oldMap
                                        Toast.makeText(
                                            context,
                                            "加载[${plugin.displayName ?: "未知"}]失败，已自动关闭",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }, "Hchat-Script-${plugin.id}").start()
                        }
                    )
                    if (index != plugins.lastIndex) InsetDivider()
                }
            }
        }
        actionPlugin?.let { plugin ->
            ScriptPluginActionDialog(
                plugin = plugin,
                pinned = plugin.id in pinnedIds,
                onDismiss = { actionPlugin = null },
                onPinChanged = { pinned ->
                    actionPlugin = null
                    scope.launch {
                        val result = withContext(Dispatchers.IO) {
                            ScriptPluginManager.setPinned(context, listOf(plugin.id), pinned)
                        }
                        result.fold(
                            onSuccess = {
                                pluginListVersion++
                                Toast.makeText(
                                    context,
                                    if (pinned) "已置顶" else "已取消置顶",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            onFailure = { showScriptPluginManagerError(context, it) }
                        )
                    }
                },
                onRename = {
                    actionPlugin = null
                    showAfterDialogDismiss(context) { renamePlugin = plugin }
                },
                onExport = {
                    actionPlugin = null
                    showAfterDialogDismiss(context) {
                        launchScriptPluginExport(
                            context = context,
                            plugins = listOf(plugin)
                        )
                    }
                },
                onDelete = {
                    actionPlugin = null
                    showAfterDialogDismiss(context) { deletePlugin = plugin }
                }
            )
        }
        renamePlugin?.let { plugin ->
            ScriptPluginRenameDialog(
                plugin = plugin,
                onDismiss = { renamePlugin = null },
                onConfirm = { name ->
                    renamePlugin = null
                    scope.launch {
                        val result = withContext(Dispatchers.IO) {
                            ScriptPluginManager.renamePlugin(context, plugin.id, name)
                        }
                        result.fold(
                            onSuccess = {
                                pluginListVersion++
                                Toast.makeText(context, "已重命名", Toast.LENGTH_SHORT).show()
                            },
                            onFailure = { showScriptPluginManagerError(context, it) }
                        )
                    }
                }
            )
        }
        deletePlugin?.let { plugin ->
            ScriptPluginDeleteDialog(
                pluginNames = listOf(plugin.displayName ?: plugin.name.ifBlank { plugin.id }),
                onDismiss = { deletePlugin = null },
                onConfirm = {
                    deletePlugin = null
                    scope.launch {
                        val result = withContext(Dispatchers.IO) {
                            ScriptPluginManager.deletePlugin(context, plugin.id)
                        }
                        result.fold(
                            onSuccess = {
                                pluginListVersion++
                                Toast.makeText(context, "插件已删除", Toast.LENGTH_SHORT).show()
                            },
                            onFailure = { showScriptPluginManagerError(context, it) }
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun ScriptPluginManagerPage(
    context: Context,
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    var query by rememberSaveable { mutableStateOf("") }
    var refreshVersion by remember { mutableStateOf(0) }
    var loading by remember { mutableStateOf(true) }
    var managedPlugins by remember {
        mutableStateOf<List<ScriptPluginManager.ManagedPlugin>>(emptyList())
    }
    var selectedIds by remember { mutableStateOf<Set<String>>(emptySet()) }
    var draggedPluginId by remember { mutableStateOf<String?>(null) }
    var dragOrderChanged by remember { mutableStateOf(false) }
    var actionPlugin by remember { mutableStateOf<ScriptPluginManager.ManagedPlugin?>(null) }
    var renamePlugin by remember { mutableStateOf<ScriptPluginRuntime.ScriptPlugin?>(null) }
    var deletePlugins by remember { mutableStateOf<List<ScriptPluginRuntime.ScriptPlugin>>(emptyList()) }
    var importInspection by remember { mutableStateOf<ScriptPluginManager.ImportInspection?>(null) }
    var importOverwriteIds by remember { mutableStateOf<Set<String>>(emptySet()) }
    var busyText by remember { mutableStateOf("") }
    val pageActive = remember { AtomicBoolean(true) }
    val activeImportSessionId = remember { AtomicReference<String?>(null) }

    LaunchedEffect(refreshVersion) {
        loading = true
        val result = withContext(Dispatchers.IO) {
            runCatching {
                ScriptPluginManager.cleanupStaleDisplayIds(context).getOrThrow()
                ScriptPluginManager.listForDisplay(context)
            }
        }
        result.fold(
            onSuccess = { plugins ->
                managedPlugins = plugins
                selectedIds = selectedIds.intersect(plugins.mapTo(LinkedHashSet()) { it.plugin.id })
            },
            onFailure = { showScriptPluginManagerError(context, it) }
        )
        loading = false
    }
    DisposableEffect(context) {
        val subscription = ScriptPluginRuntime.subscribePluginCatalog(context) {
            Handler(Looper.getMainLooper()).post { refreshVersion++ }
        }
        onDispose { subscription.unsubscribe() }
    }
    DisposableEffect(Unit) {
        pageActive.set(true)
        onDispose {
            pageActive.set(false)
            activeImportSessionId.getAndSet(null)?.let { sessionId ->
                Thread({ ScriptPluginManager.discardImport(context, sessionId) }, "Hchat-Plugin-Import-Cleanup").start()
            }
        }
    }

    val normalizedQuery = query.trim().lowercase(Locale.ROOT)
    val visiblePlugins = managedPlugins.filter { item ->
        val plugin = item.plugin
        normalizedQuery.isEmpty() ||
            plugin.id.lowercase(Locale.ROOT).contains(normalizedQuery) ||
            plugin.name.lowercase(Locale.ROOT).contains(normalizedQuery) ||
            plugin.author.lowercase(Locale.ROOT).contains(normalizedQuery) ||
            plugin.version.lowercase(Locale.ROOT).contains(normalizedQuery)
    }
    val pinnedPlugins = visiblePlugins.filter { it.pinned }
    val normalPlugins = visiblePlugins.filterNot { it.pinned }
    val visibleIds = visiblePlugins.mapTo(LinkedHashSet()) { it.plugin.id }
    val allVisibleSelected = visibleIds.isNotEmpty() && visibleIds.all { it in selectedIds }

    fun refresh() {
        refreshVersion++
    }

    fun setPinned(ids: Set<String>, pinned: Boolean) {
        if (ids.isEmpty() || busyText.isNotEmpty()) return
        busyText = if (pinned) "正在置顶" else "正在取消置顶"
        scope.launch {
            val result = withContext(Dispatchers.IO) {
                ScriptPluginManager.setPinned(context, ids, pinned)
            }
            busyText = ""
            result.fold(
                onSuccess = {
                    refresh()
                    Toast.makeText(
                        context,
                        if (pinned) "已置顶 ${ids.size} 个插件" else "已取消置顶 ${ids.size} 个插件",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onFailure = { showScriptPluginManagerError(context, it) }
            )
        }
    }

    fun commitOrder() {
        val ids = managedPlugins.map { it.plugin.id }
        scope.launch {
            val result = withContext(Dispatchers.IO) {
                ScriptPluginManager.saveDisplayOrder(context, ids)
            }
            result.onFailure { showScriptPluginManagerError(context, it) }
        }
    }

    fun movePlugin(item: ScriptPluginManager.ManagedPlugin, delta: Int): Boolean {
        if (query.isNotBlank()) return false
        val index = managedPlugins.indexOfFirst { it.plugin.id == item.plugin.id }
        val targetIndex = index + delta
        if (index < 0 || targetIndex !in managedPlugins.indices) return false
        if (managedPlugins[targetIndex].pinned != item.pinned) return false
        val next = managedPlugins.toMutableList()
        val moved = next.removeAt(index)
        next.add(targetIndex, moved)
        managedPlugins = next
        return true
    }

    fun launchImportPicker() {
        val activity = context as? Activity
        if (activity == null) {
            Toast.makeText(context, "无法打开文件选择器", Toast.LENGTH_SHORT).show()
            return
        }
        ScriptPluginDocumentBridge.launchImport(activity) { uri ->
            if (!pageActive.get()) return@launchImport
            busyText = "正在检查导入包"
            Thread({
                val result = runCatching {
                    context.contentResolver.openInputStream(uri)?.use { input ->
                        ScriptPluginManager.inspectImport(context, input).getOrThrow()
                    } ?: error("无法读取所选文件")
                }
                if (!pageActive.get()) {
                    result.getOrNull()?.let { inspection ->
                        ScriptPluginManager.discardImport(context, inspection.sessionId)
                    }
                    return@Thread
                }
                Handler(Looper.getMainLooper()).post {
                    if (!pageActive.get()) {
                        result.getOrNull()?.let { inspection ->
                            Thread(
                                { ScriptPluginManager.discardImport(context, inspection.sessionId) },
                                "Hchat-Plugin-Import-Late-Cleanup"
                            ).start()
                        }
                        return@post
                    }
                    busyText = ""
                    result.fold(
                        onSuccess = { inspection ->
                            activeImportSessionId.set(inspection.sessionId)
                            importInspection = inspection
                            importOverwriteIds = emptySet()
                        },
                        onFailure = { showScriptPluginManagerError(context, it) }
                    )
                }
            }, "Hchat-Plugin-Import-Inspect").start()
        }
    }

    PageScaffold(
        title = "本地插件管理",
        largeTitle = "本地插件管理",
        scrollBehavior = scrollBehavior,
        onBack = onBack,
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
            item {
                SearchBarSurface(
                    query = query,
                    placeholder = "搜索本地插件",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    onQueryChange = { query = it }
                )
            }
            item { SmallTitle(modifier = Modifier.padding(top = 6.dp), text = "管理") }
            item {
                SettingsCard {
                    ScriptPluginManagerCommandRow(
                        icon = NavIcons.Import,
                        title = "导入插件",
                        summary = "从 ZIP 文件导入，导入后默认关闭",
                        enabled = busyText.isEmpty(),
                        onClick = ::launchImportPicker
                    )
                    InsetDivider()
                    ScriptPluginSelectionToolbar(
                        selectedCount = selectedIds.size,
                        allVisibleSelected = allVisibleSelected,
                        enabled = visibleIds.isNotEmpty() && busyText.isEmpty(),
                        onSelectAll = {
                            selectedIds = if (allVisibleSelected) {
                                selectedIds - visibleIds
                            } else {
                                selectedIds + visibleIds
                            }
                        },
                        onInvert = {
                            selectedIds = (selectedIds - visibleIds) + (visibleIds - selectedIds)
                        }
                    )
                    if (selectedIds.isNotEmpty()) {
                        InsetDivider()
                        ScriptPluginBatchActions(
                            enabled = busyText.isEmpty(),
                            onPin = { setPinned(selectedIds, true) },
                            onUnpin = { setPinned(selectedIds, false) },
                            onExport = {
                                val selected = managedPlugins.filter { it.plugin.id in selectedIds }.map { it.plugin }
                                launchScriptPluginExport(context, selected)
                            },
                            onDelete = {
                                deletePlugins = managedPlugins
                                    .filter { it.plugin.id in selectedIds }
                                    .map { it.plugin }
                            }
                        )
                    }
                }
            }
            if (busyText.isNotEmpty()) {
                item {
                    Text(
                        text = busyText,
                        color = MiuixTheme.colorScheme.primary,
                        fontSize = 13.sp,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp)
                    )
                }
            }
            when {
                loading -> item { EmptyText("正在加载插件") }
                managedPlugins.isEmpty() -> item { EmptyText("暂无本地插件") }
                visiblePlugins.isEmpty() -> item { EmptyText("没有匹配的插件") }
                else -> {
                    if (pinnedPlugins.isNotEmpty()) {
                        item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "已置顶(${pinnedPlugins.size})") }
                        item {
                            SettingsCard {
                                pinnedPlugins.forEachIndexed { index, item ->
                                    key(item.plugin.id) {
                                        ScriptPluginManagerRow(
                                            modifier = Modifier,
                                            item = item,
                                            selected = item.plugin.id in selectedIds,
                                            dragEnabled = query.isBlank() && busyText.isEmpty(),
                                            onSelectedChange = { selected ->
                                                selectedIds = if (selected) {
                                                    selectedIds + item.plugin.id
                                                } else {
                                                    selectedIds - item.plugin.id
                                                }
                                            },
                                            onOpenActions = { actionPlugin = item },
                                            onDragMove = { delta -> movePlugin(item, delta) },
                                            onDragStateChange = { dragging ->
                                                if (dragging) {
                                                    draggedPluginId = item.plugin.id
                                                    dragOrderChanged = false
                                                } else if (draggedPluginId == item.plugin.id) {
                                                    draggedPluginId = null
                                                    if (dragOrderChanged) commitOrder()
                                                }
                                            },
                                            onOrderChanged = { dragOrderChanged = true }
                                        )
                                        if (index != pinnedPlugins.lastIndex) InsetDivider()
                                    }
                                }
                            }
                        }
                    }
                    if (normalPlugins.isNotEmpty()) {
                        item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "其他插件(${normalPlugins.size})") }
                        item {
                            SettingsCard {
                                normalPlugins.forEachIndexed { index, item ->
                                    key(item.plugin.id) {
                                        ScriptPluginManagerRow(
                                            modifier = Modifier,
                                            item = item,
                                            selected = item.plugin.id in selectedIds,
                                            dragEnabled = query.isBlank() && busyText.isEmpty(),
                                            onSelectedChange = { selected ->
                                                selectedIds = if (selected) {
                                                    selectedIds + item.plugin.id
                                                } else {
                                                    selectedIds - item.plugin.id
                                                }
                                            },
                                            onOpenActions = { actionPlugin = item },
                                            onDragMove = { delta -> movePlugin(item, delta) },
                                            onDragStateChange = { dragging ->
                                                if (dragging) {
                                                    draggedPluginId = item.plugin.id
                                                    dragOrderChanged = false
                                                } else if (draggedPluginId == item.plugin.id) {
                                                    draggedPluginId = null
                                                    if (dragOrderChanged) commitOrder()
                                                }
                                            },
                                            onOrderChanged = { dragOrderChanged = true }
                                        )
                                        if (index != normalPlugins.lastIndex) InsetDivider()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    actionPlugin?.let { item ->
        ScriptPluginActionDialog(
            plugin = item.plugin,
            pinned = item.pinned,
            onDismiss = { actionPlugin = null },
            onPinChanged = { pinned ->
                actionPlugin = null
                setPinned(setOf(item.plugin.id), pinned)
            },
            onRename = {
                actionPlugin = null
                showAfterDialogDismiss(context) { renamePlugin = item.plugin }
            },
            onExport = {
                actionPlugin = null
                showAfterDialogDismiss(context) {
                    launchScriptPluginExport(context, listOf(item.plugin))
                }
            },
            onDelete = {
                actionPlugin = null
                showAfterDialogDismiss(context) { deletePlugins = listOf(item.plugin) }
            }
        )
    }
    renamePlugin?.let { plugin ->
        ScriptPluginRenameDialog(
            plugin = plugin,
            onDismiss = { renamePlugin = null },
            onConfirm = { name ->
                renamePlugin = null
                busyText = "正在重命名"
                scope.launch {
                    val result = withContext(Dispatchers.IO) {
                        ScriptPluginManager.renamePlugin(context, plugin.id, name)
                    }
                    busyText = ""
                    result.fold(
                        onSuccess = {
                            refresh()
                            Toast.makeText(context, "已重命名", Toast.LENGTH_SHORT).show()
                        },
                        onFailure = { showScriptPluginManagerError(context, it) }
                    )
                }
            }
        )
    }
    if (deletePlugins.isNotEmpty()) {
        val pendingDelete = deletePlugins
        ScriptPluginDeleteDialog(
            pluginNames = pendingDelete.map { it.displayName ?: it.name.ifBlank { it.id } },
            onDismiss = { deletePlugins = emptyList() },
            onConfirm = {
                deletePlugins = emptyList()
                busyText = "正在删除插件"
                scope.launch {
                    val result = withContext(Dispatchers.IO) {
                        ScriptPluginManager.deletePlugins(context, pendingDelete.map { it.id })
                    }
                    busyText = ""
                    result.fold(
                        onSuccess = {
                            selectedIds = selectedIds - pendingDelete.mapTo(LinkedHashSet()) { it.id }
                            refresh()
                            Toast.makeText(context, "已删除 ${pendingDelete.size} 个插件", Toast.LENGTH_SHORT).show()
                        },
                        onFailure = { showScriptPluginManagerError(context, it) }
                    )
                }
            }
        )
    }
    importInspection?.let { inspection ->
        ScriptPluginImportDialog(
            inspection = inspection,
            overwriteIds = importOverwriteIds,
            applying = busyText == "正在导入插件",
            onOverwriteChanged = { pluginId, overwrite ->
                importOverwriteIds = if (overwrite) {
                    importOverwriteIds + pluginId
                } else {
                    importOverwriteIds - pluginId
                }
            },
            onDismiss = {
                activeImportSessionId.set(null)
                importInspection = null
                Thread(
                    { ScriptPluginManager.discardImport(context, inspection.sessionId) },
                    "Hchat-Plugin-Import-Discard"
                ).start()
            },
            onConfirm = {
                busyText = "正在导入插件"
                scope.launch {
                    val actions = inspection.conflicts.associateWith { pluginId ->
                        if (pluginId in importOverwriteIds) {
                            ScriptPluginManager.ImportConflictAction.OVERWRITE
                        } else {
                            ScriptPluginManager.ImportConflictAction.SKIP
                        }
                    }
                    val result = withContext(Dispatchers.IO) {
                        ScriptPluginManager.applyImport(context, inspection.sessionId, actions)
                    }
                    busyText = ""
                    result.fold(
                        onSuccess = { imported ->
                            activeImportSessionId.set(null)
                            importInspection = null
                            importOverwriteIds = emptySet()
                            refresh()
                            Toast.makeText(
                                context,
                                "已导入 ${imported.importedPluginIds.size} 个插件，保持关闭状态",
                                Toast.LENGTH_LONG
                            ).show()
                        },
                        onFailure = { showScriptPluginManagerError(context, it) }
                    )
                }
            }
        )
    }
}

@Composable
internal fun ScriptPluginSelectionToolbar(
    selectedCount: Int,
    allVisibleSelected: Boolean,
    enabled: Boolean,
    onSelectAll: () -> Unit,
    onInvert: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = if (selectedCount == 0) "未选择插件" else "已选择 $selectedCount 个",
            color = MiuixTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        TextButton(
            text = if (allVisibleSelected) "取消全选" else "全选",
            enabled = enabled,
            onClick = onSelectAll,
            colors = ButtonDefaults.textButtonColorsPrimary()
        )
        TextButton(
            text = "反选",
            enabled = enabled,
            onClick = onInvert,
            colors = ButtonDefaults.textButtonColorsPrimary()
        )
    }
}

@Composable
internal fun ScriptPluginBatchActions(
    enabled: Boolean,
    onPin: () -> Unit,
    onUnpin: () -> Unit,
    onExport: () -> Unit,
    onDelete: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ScriptPluginCompactAction("置顶", enabled, Modifier.weight(1f), onPin)
            ScriptPluginCompactAction("取消置顶", enabled, Modifier.weight(1f), onUnpin)
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ScriptPluginCompactAction("导出", enabled, Modifier.weight(1f), onExport)
            ScriptPluginCompactAction("删除", enabled, Modifier.weight(1f), onDelete, destructive = true)
        }
    }
}

@Composable
internal fun ScriptPluginCompactAction(
    text: String,
    enabled: Boolean,
    modifier: Modifier,
    onClick: () -> Unit,
    destructive: Boolean = false
) {
    val color = when {
        !enabled -> MiuixTheme.colorScheme.onSurfaceVariantSummary.copy(alpha = 0.45f)
        destructive -> Color(0xFFD93025)
        else -> MiuixTheme.colorScheme.primary
    }
    Box(
        modifier = modifier.height(40.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.10f))
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = color, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
internal fun ScriptPluginManagerCommandRow(
    icon: ImageVector,
    title: String,
    summary: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val color = if (enabled) MiuixTheme.colorScheme.onSurface else {
        MiuixTheme.colorScheme.onSurfaceVariantSummary.copy(alpha = 0.45f)
    }
    Row(
        modifier = Modifier.fillMaxWidth().clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            imageVector = icon,
            contentDescription = null,
            colorFilter = ColorFilter.tint(color),
            modifier = Modifier.size(22.dp)
        )
        Column(modifier = Modifier.weight(1f).padding(start = 14.dp)) {
            Text(text = title, color = color, fontWeight = FontWeight.Medium)
            Text(text = summary, color = MiuixTheme.colorScheme.onSurfaceVariantSummary, fontSize = 12.sp)
        }
    }
}

@Composable
internal fun ScriptPluginManagerRow(
    modifier: Modifier,
    item: ScriptPluginManager.ManagedPlugin,
    selected: Boolean,
    dragEnabled: Boolean,
    onSelectedChange: (Boolean) -> Unit,
    onOpenActions: () -> Unit,
    onDragMove: (Int) -> Boolean,
    onDragStateChange: (Boolean) -> Unit,
    onOrderChanged: () -> Unit
) {
    val plugin = item.plugin
    val density = LocalDensity.current
    var itemHeightPx by remember(plugin.id) { mutableStateOf(0f) }
    var dragDistanceY by remember(plugin.id) { mutableStateOf(0f) }
    var dragging by remember(plugin.id) { mutableStateOf(false) }
    val currentOnDragMove by rememberUpdatedState(onDragMove)
    val currentOnDragStateChange by rememberUpdatedState(onDragStateChange)
    val currentOnOrderChanged by rememberUpdatedState(onOrderChanged)
    val visualDragY by animateFloatAsState(
        targetValue = dragDistanceY,
        animationSpec = tween(durationMillis = if (dragging) 0 else 160),
        label = "ScriptPluginDragOffset"
    )
    Row(
        modifier = modifier.zIndex(if (dragging) 1f else 0f)
            .graphicsLayer {
                translationY = visualDragY
                shadowElevation = if (dragging) with(density) { 7.dp.toPx() } else 0f
                shape = RoundedCornerShape(8.dp)
            }
            .fillMaxWidth()
            .onSizeChanged { itemHeightPx = it.height.toFloat() }
            .clickable { onSelectedChange(!selected) }
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SelectionMark(selected = selected, multiSelect = true)
        Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = plugin.displayName ?: plugin.name.ifBlank { plugin.id },
                    color = MiuixTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
                if (item.pinned) {
                    Image(
                        imageVector = NavIcons.Pin,
                        contentDescription = "已置顶",
                        colorFilter = ColorFilter.tint(MiuixTheme.colorScheme.primary),
                        modifier = Modifier.padding(start = 6.dp).size(16.dp)
                    )
                }
            }
            Text(
                text = buildString {
                    append(plugin.id)
                    append(" · ").append(plugin.author.ifBlank { "未知作者" })
                    append(" · ").append(plugin.version.ifBlank { "未知版本" })
                },
                color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Image(
            imageVector = NavIcons.More,
            contentDescription = "管理插件",
            colorFilter = ColorFilter.tint(MiuixTheme.colorScheme.onSurfaceVariantSummary),
            modifier = Modifier.padding(start = 8.dp).size(32.dp)
                .clip(RoundedCornerShape(8.dp))
                .clickable {
                    onOpenActions()
                }
                .padding(5.dp)
        )
        Image(
            imageVector = NavIcons.Drag,
            contentDescription = if (dragEnabled) "长按拖动排序" else "搜索时不可排序",
            colorFilter = ColorFilter.tint(
                MiuixTheme.colorScheme.onSurfaceVariantSummary.copy(alpha = if (dragEnabled) 1f else 0.35f)
            ),
            modifier = Modifier.padding(start = 4.dp).size(32.dp)
                .pointerInput(plugin.id, dragEnabled, itemHeightPx) {
                    if (!dragEnabled) return@pointerInput
                    detectDragGesturesAfterLongPress(
                        onDragStart = {
                            dragDistanceY = 0f
                            dragging = true
                            currentOnDragStateChange(true)
                        },
                        onDragEnd = {
                            dragging = false
                            dragDistanceY = 0f
                            currentOnDragStateChange(false)
                        },
                        onDragCancel = {
                            dragging = false
                            dragDistanceY = 0f
                            currentOnDragStateChange(false)
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            dragDistanceY += dragAmount.y
                            val step = itemHeightPx.takeIf { it > 0f } ?: with(density) { 62.dp.toPx() }
                            while (dragDistanceY <= -step * 0.5f) {
                                if (currentOnDragMove(-1)) {
                                    dragDistanceY += step
                                    currentOnOrderChanged()
                                } else {
                                    dragDistanceY = -step * 0.45f
                                    break
                                }
                            }
                            while (dragDistanceY >= step * 0.5f) {
                                if (currentOnDragMove(1)) {
                                    dragDistanceY -= step
                                    currentOnOrderChanged()
                                } else {
                                    dragDistanceY = step * 0.45f
                                    break
                                }
                            }
                        }
                    )
                }
                .padding(5.dp)
        )
    }
}

@Composable
internal fun ScriptPluginActionDialog(
    plugin: ScriptPluginRuntime.ScriptPlugin,
    pinned: Boolean,
    onDismiss: () -> Unit,
    onPinChanged: (Boolean) -> Unit,
    onRename: () -> Unit,
    onExport: () -> Unit,
    onDelete: () -> Unit
) {
    WindowDialog(
        show = true,
        title = plugin.displayName ?: plugin.name.ifBlank { plugin.id },
        onDismissRequest = onDismiss,
        content = {
            Column {
                ScriptPluginDialogActionRow(
                    icon = NavIcons.Pin,
                    text = if (pinned) "取消置顶" else "置顶",
                    onClick = { onPinChanged(!pinned) }
                )
                ScriptPluginDialogActionRow(NavIcons.Edit, "重命名", onClick = onRename)
                ScriptPluginDialogActionRow(NavIcons.Export, "导出", onClick = onExport)
                ScriptPluginDialogActionRow(NavIcons.Delete, "删除", destructive = true, onClick = onDelete)
            }
        }
    )
}

@Composable
internal fun ScriptPluginDialogActionRow(
    icon: ImageVector,
    text: String,
    destructive: Boolean = false,
    onClick: () -> Unit
) {
    val color = if (destructive) Color(0xFFD93025) else MiuixTheme.colorScheme.onSurface
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (destructive) Color(0xFFD93025).copy(alpha = 0.08f)
                else MiuixTheme.colorScheme.secondaryVariant
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            imageVector = icon,
            contentDescription = null,
            colorFilter = ColorFilter.tint(color),
            modifier = Modifier.size(22.dp)
        )
        Text(text = text, color = color, fontSize = 15.sp, modifier = Modifier.padding(start = 18.dp))
    }
}

@Composable
internal fun ScriptPluginRenameDialog(
    plugin: ScriptPluginRuntime.ScriptPlugin,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var name by remember(plugin.id) {
        mutableStateOf(plugin.displayName ?: plugin.name.ifBlank { plugin.id })
    }
    WindowDialog(
        show = true,
        title = "重命名插件",
        onDismissRequest = onDismiss,
        content = {
            Column {
                Text(
                    text = "仅修改展示名称，插件目录和 ID 保持不变",
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    fontSize = 12.sp
                )
                BasicTextField(
                    value = name,
                    onValueChange = { name = it.take(100) },
                    singleLine = true,
                    textStyle = TextStyle(color = MiuixTheme.colorScheme.onSurface, fontSize = 15.sp),
                    cursorBrush = SolidColor(MiuixTheme.colorScheme.primary),
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MiuixTheme.colorScheme.secondaryVariant)
                        .padding(horizontal = 12.dp, vertical = 11.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TextButton(
                        text = "取消",
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.textButtonColorsPrimary()
                    )
                    TextButton(
                        text = "确定",
                        onClick = { onConfirm(name.trim()) },
                        enabled = name.trim().isNotEmpty(),
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.textButtonColorsPrimary()
                    )
                }
            }
        }
    )
}

@Composable
internal fun ScriptPluginDeleteDialog(
    pluginNames: List<String>,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    WindowDialog(
        show = true,
        title = if (pluginNames.size == 1) "删除插件" else "批量删除插件",
        onDismissRequest = onDismiss,
        content = {
            Column {
                Text(
                    text = if (pluginNames.size == 1) {
                        "确定删除“${pluginNames.first()}”吗？插件会先停止运行，删除后无法恢复。"
                    } else {
                        "确定删除已选的 ${pluginNames.size} 个插件吗？插件会先停止运行，删除后无法恢复。"
                    },
                    color = MiuixTheme.colorScheme.onSurface,
                    fontSize = 14.sp
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TextButton(
                        text = "取消",
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.textButtonColorsPrimary()
                    )
                    TextButton(
                        text = "删除",
                        onClick = onConfirm,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.textButtonColorsPrimary()
                    )
                }
            }
        }
    )
}

@Composable
internal fun ScriptPluginImportDialog(
    inspection: ScriptPluginManager.ImportInspection,
    overwriteIds: Set<String>,
    applying: Boolean,
    onOverwriteChanged: (String, Boolean) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    WindowDialog(
        show = true,
        title = "导入插件",
        onDismissRequest = { if (!applying) onDismiss() },
        content = {
            Column(modifier = Modifier.fillMaxWidth().heightIn(max = 560.dp)) {
                Text(
                    text = "共 ${inspection.plugins.size} 个插件。已有同 ID 插件默认跳过，可单独选择覆盖。",
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    fontSize = 12.sp
                )
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().weight(1f, fill = false).padding(top = 10.dp)
                ) {
                    items(inspection.plugins, key = { it.pluginId }) { plugin ->
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable(enabled = !applying && plugin.conflict) {
                                    onOverwriteChanged(plugin.pluginId, plugin.pluginId !in overwriteIds)
                                }
                                .padding(horizontal = 10.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = plugin.name.ifBlank { plugin.pluginId },
                                    color = MiuixTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Medium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = if (plugin.conflict) {
                                        if (plugin.pluginId in overwriteIds) "覆盖已有插件" else "跳过已有插件"
                                    } else {
                                        "新增插件"
                                    },
                                    color = if (plugin.conflict && plugin.pluginId in overwriteIds) {
                                        Color(0xFFD93025)
                                    } else {
                                        MiuixTheme.colorScheme.onSurfaceVariantSummary
                                    },
                                    fontSize = 12.sp
                                )
                            }
                            if (plugin.conflict) {
                                SelectionMark(
                                    selected = plugin.pluginId in overwriteIds,
                                    multiSelect = true
                                )
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TextButton(
                        text = "取消",
                        onClick = onDismiss,
                        enabled = !applying,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.textButtonColorsPrimary()
                    )
                    TextButton(
                        text = if (applying) "正在导入" else "导入",
                        onClick = onConfirm,
                        enabled = !applying,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.textButtonColorsPrimary()
                    )
                }
            }
        }
    )
}

internal fun launchScriptPluginExport(
    context: Context,
    plugins: List<ScriptPluginRuntime.ScriptPlugin>
) {
    if (plugins.isEmpty()) {
        Toast.makeText(context, "未选择插件", Toast.LENGTH_SHORT).show()
        return
    }
    val activity = context as? Activity
    if (activity == null) {
        Toast.makeText(context, "无法打开文件选择器", Toast.LENGTH_SHORT).show()
        return
    }
    val fileName = scriptPluginExportFileName(plugins)
    ScriptPluginDocumentBridge.launchExport(activity, fileName) { uri ->
        Thread({
            val result = runCatching {
                context.contentResolver.openOutputStream(uri, "w")?.use { output ->
                    ScriptPluginManager.exportPlugins(context, plugins.map { it.id }, output).getOrThrow()
                } ?: error("无法写入所选文件")
            }
            if (result.isFailure) runCatching { context.contentResolver.delete(uri, null, null) }
            Handler(Looper.getMainLooper()).post {
                result.fold(
                    onSuccess = { exported ->
                        Toast.makeText(
                            context,
                            "已导出 ${exported.exportedPluginIds.size} 个插件",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onFailure = { showScriptPluginManagerError(context, it) }
                )
            }
        }, "Hchat-Plugin-Export").start()
    }
}

internal fun scriptPluginExportFileName(plugins: List<ScriptPluginRuntime.ScriptPlugin>): String {
    val base = if (plugins.size == 1) {
        plugins.first().displayName ?: plugins.first().name.ifBlank { plugins.first().id }
    } else {
        "Hchat_脚本插件_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())}"
    }
    val safe = base.removeSuffix(".zip")
        .replace(Regex("""[\\/:*?\"<>|]"""), "_")
        .trim()
        .ifBlank { "Hchat_脚本插件" }
    return "$safe.zip"
}

internal fun showAfterDialogDismiss(context: Context, action: () -> Unit) {
    val activity = context as? Activity
    if (activity != null) {
        activity.window.decorView.postOnAnimation(action)
    } else {
        Handler(Looper.getMainLooper()).post(action)
    }
}

internal fun showScriptPluginManagerError(context: Context, error: Throwable) {
    h.Hchat.utils.HLog.e("[Hchat:ScriptManager] ${error.message ?: "操作失败"}", error)
    Toast.makeText(context, error.message ?: "操作失败", Toast.LENGTH_LONG).show()
}

@Composable
internal fun PathSwitchRow(
    checked: Boolean,
    title: String,
    summary: String,
    onInfoClick: () -> Unit,
    onCheckedChange: (Boolean) -> Unit
) {
    var pressed by remember { mutableStateOf(false) }
    val pressFeedbackColor = rememberPressFeedbackColor(pressed)
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(pressFeedbackColor)
                .responsiveTap(
                    onClick = onInfoClick,
                    onPressedChange = { pressed = it }
                )
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = title,
                    color = MiuixTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f, fill = false)
                )
                ClickHintTag()
            }
            Text(text = summary, color = MiuixTheme.colorScheme.onSurfaceVariantSummary, fontSize = 12.sp)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}

@Composable
internal fun ScriptPluginRow(
    checked: Boolean,
    title: String,
    summary: String,
    showSettings: Boolean,
    onOpenReadme: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenManager: () -> Unit,
    onCheckedChange: (Boolean) -> Unit
) {
    var pressed by remember { mutableStateOf(false) }
    val pressFeedbackColor = rememberPressFeedbackColor(pressed)
    Row(
        modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(pressFeedbackColor)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
                .responsiveTap(
                    onClick = onOpenReadme,
                    onPressedChange = { pressed = it }
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = title,
                    color = MiuixTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f, fill = false)
                )
                ClickHintTag()
            }
            Text(text = summary, color = MiuixTheme.colorScheme.onSurfaceVariantSummary, fontSize = 12.sp)
        }
        if (showSettings) {
            Image(
                imageVector = NavIcons.Settings,
                contentDescription = "插件设置",
                colorFilter = ColorFilter.tint(MiuixTheme.colorScheme.onSurfaceVariantSummary),
                modifier = Modifier
                    .padding(start = 10.dp, end = 8.dp)
                    .size(22.dp)
                    .responsiveTap(onClick = onOpenSettings)
            )
        }
        Image(
            imageVector = NavIcons.More,
            contentDescription = "管理插件",
            colorFilter = ColorFilter.tint(MiuixTheme.colorScheme.onSurfaceVariantSummary),
            modifier = Modifier
                .padding(start = if (showSettings) 0.dp else 10.dp, end = 8.dp)
                .size(22.dp)
                .responsiveTap(onClick = onOpenManager)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
internal fun ScriptPluginPathDialog(
    context: Context,
    title: String,
    path: String,
    onClose: () -> Unit
) {
    WindowDialog(
        show = true,
        title = title,
        onDismissRequest = onClose,
        content = {
            Column {
                Text(
                    text = path,
                    color = MiuixTheme.colorScheme.onSurface,
                    fontSize = 13.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MiuixTheme.colorScheme.surfaceVariant)
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                )
                TextButton(
                    text = "复制路径",
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? android.content.ClipboardManager
                        clipboard?.setPrimaryClip(ClipData.newPlainText("HchatScriptDir", path))
                        Toast.makeText(context, "已复制", Toast.LENGTH_SHORT).show()
                        onClose()
                    },
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                    colors = ButtonDefaults.textButtonColorsPrimary()
                )
                TextButton(
                    text = "关闭",
                    onClick = onClose,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    colors = ButtonDefaults.textButtonColorsPrimary()
                )
            }
        }
    )
}

@Composable
fun FirstUseAgreementDialog(
    context: Context,
    onCancel: () -> Unit,
    onAccepted: () -> Unit
) {
    var input by remember { mutableStateOf("") }
    var remainingSeconds by remember { mutableStateOf(30) }
    val acceptedText = TermsGate.AGREEMENT_TEXT
    LaunchedEffect(Unit) {
        while (remainingSeconds > 0) {
            delay(1000L)
            remainingSeconds -= 1
        }
    }
    val agreement = remember {
        """
        Hchat 使用协议与免责声明

        1. 本模块为免费模块，仅供个人学习、研究、测试、逆向分析和备份用途使用，不提供任何商业授权、售后承诺或稳定性保证。
        2. 本模块与微信、腾讯及其关联主体无关，不代表官方立场，也不是官方客户端、官方插件或官方服务的一部分。
        3. 禁止倒卖、付费分发、捆绑销售、二次打包收费、引流售卖、以捐赠名义收费，禁止冒充作者、官方渠道或授权代理发布。
        4. 请勿在国内公开平台、群组、论坛、短视频平台、网盘分享页、应用市场或其它公开渠道传播、推广、引流、售卖或组织分发本模块。
        5. 本模块可能会修改微信运行时行为，使用后可能出现功能异常、消息异常、账号风控、限制登录、数据异常、闪退、掉线、模块冲突或其它不可预期问题。
        6. 使用者应自行确认所在地法律法规、平台协议、设备环境和账号风险；因安装、使用、传播、修改、二次分发或与其它模块共存产生的任何后果均由使用者自行承担。
        7. 禁止将本模块用于骚扰、欺诈、刷量、营销轰炸、盗取信息、破坏服务稳定性、绕过平台风控、侵犯他人权益或其它违法违规用途。
        8. 本模块不保证适配所有微信版本、系统版本、设备环境、热更新状态和其它模块共存环境，也不承诺持续维护、及时修复或提供任何形式的服务保障。
        9. 如果你不同意以上任一条款，请点击取消并停止使用本模块。

        如果你理解并接受以上内容，请在下方输入“$acceptedText”后继续使用。
        """.trimIndent()
    }
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val dialogMaxHeight = (screenHeight * 0.78f).coerceAtMost(640.dp)
    val agreementMaxHeight = (screenHeight * 0.42f).coerceIn(220.dp, 430.dp)
    WindowDialog(
        show = true,
        title = "使用协议",
        onDismissRequest = {},
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = dialogMaxHeight)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = agreementMaxHeight)
                        .verticalScroll(rememberScrollState())
                        .clip(RoundedCornerShape(10.dp))
                        .background(MiuixTheme.colorScheme.surfaceVariant)
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = agreement,
                        color = MiuixTheme.colorScheme.onSurface,
                        fontSize = 13.sp,
                        lineHeight = 20.sp
                    )
                }
                Text(
                    text = "请输入“$acceptedText”确认",
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 14.dp)
                )
                BasicTextField(
                    value = input,
                    onValueChange = { input = it },
                    singleLine = true,
                    textStyle = TextStyle(
                        color = MiuixTheme.colorScheme.onSurface,
                        fontSize = 15.sp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MiuixTheme.colorScheme.secondaryVariant)
                        .padding(horizontal = 12.dp, vertical = 11.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TextButton(
                        text = "取消",
                        onClick = onCancel,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.textButtonColorsPrimary()
                    )
                    TextButton(
                        text = if (remainingSeconds > 0) "继续（${remainingSeconds}s）" else "同意并继续",
                        onClick = {
                            if (remainingSeconds > 0) {
                                Toast.makeText(context, "请等待 ${remainingSeconds} 秒后继续", Toast.LENGTH_SHORT).show()
                            } else if (input.trim() == acceptedText) {
                                if (TermsGate.accept(context)) {
                                    onAccepted()
                                } else {
                                    Toast.makeText(context, "协议状态保存失败，请重试", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(context, "请输入“$acceptedText”后继续", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.textButtonColorsPrimary()
                    )
                }
            }
        }
    )
}

@Composable
fun ScriptPluginReadmeDialog(
    context: Context,
    plugin: ScriptPluginRuntime.ScriptPlugin,
    onClose: () -> Unit
) {
    val readme = remember(plugin.id) {
        runCatching {
            val file = File(plugin.dir, "README.md")
            if (file.isFile) file.readText(Charsets.UTF_8) else ""
        }.getOrDefault("")
    }
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val dialogMaxHeight = (screenHeight * 0.78f).coerceAtMost(640.dp)
    val readmeMaxHeight = (screenHeight * 0.58f).coerceIn(240.dp, 520.dp)

    WindowDialog(
        show = true,
        title = plugin.displayName ?: "未知",
        onDismissRequest = onClose,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = dialogMaxHeight)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = readmeMaxHeight)
                        .verticalScroll(rememberScrollState())
                ) {
                    if (readme.isBlank()) {
                        Text(
                            text = "暂无说明",
                            color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                    } else {
                        MarkdownUi.Content(context, readme)
                    }
                }
                TextButton(
                    text = "关闭",
                    onClick = onClose,
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                    colors = ButtonDefaults.textButtonColorsPrimary()
                )
            }
        }
    )
}

}

internal object MarkdownUi {

@Composable
fun Content(
    context: Context,
    markdown: String,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
    bodyFontSize: androidx.compose.ui.unit.TextUnit = 13.sp,
    onCopyCode: ((String) -> Unit)? = null
) {
    val lines = markdown.replace("\r\n", "\n").replace('\r', '\n').lines()
    var inCodeBlock = false
    var inlineState = MarkdownInlineState()
    val codeLines = ArrayList<String>()
    Column(modifier = Modifier.fillMaxWidth().padding(contentPadding)) {
        lines.forEach { rawLine ->
            val line = rawLine.trimEnd()
            if (line.trimStart().startsWith("```")) {
                if (inCodeBlock) {
                    CodeBlock(codeLines.joinToString("\n"), onCopyCode)
                    codeLines.clear()
                    inCodeBlock = false
                } else {
                    inCodeBlock = true
                    codeLines.clear()
                }
                return@forEach
            }
            if (inCodeBlock) {
                codeLines += rawLine
                return@forEach
            }
            val nextState = MarkdownLine(context, line, inlineState, bodyFontSize)
            inlineState = nextState
        }
        if (inCodeBlock && codeLines.isNotEmpty()) {
            CodeBlock(codeLines.joinToString("\n"), onCopyCode)
        }
    }
}

@Composable
internal fun MarkdownLine(
    context: Context,
    line: String,
    inlineState: MarkdownInlineState,
    bodyFontSize: androidx.compose.ui.unit.TextUnit = 13.sp
): MarkdownInlineState {
    val trimmed = line.trim()
    return when {
        trimmed.isBlank() -> {
            Box(modifier = Modifier.height(8.dp))
            inlineState
        }
        trimmed.matches(Regex("""-{3,}|_{3,}|\*{3,}""")) -> {
            Box(
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .height(1.dp)
                    .background(MiuixTheme.colorScheme.outline)
            )
            inlineState
        }
        trimmed.startsWith("#") -> {
            val level = trimmed.takeWhile { it == '#' }.length.coerceIn(1, 6)
            val text = trimmed.drop(level).trim()
            MarkdownTextResult(
                context = context,
                text = text,
                inlineState = inlineState,
                modifier = Modifier.padding(top = if (level <= 2) 10.dp else 8.dp, bottom = 4.dp),
                color = MiuixTheme.colorScheme.onSurface,
                fontSize = when (level) {
                    1 -> 22.sp
                    2 -> 19.sp
                    3 -> 17.sp
                    else -> 15.sp
                },
                fontWeight = FontWeight.SemiBold
            )
        }
        trimmed.startsWith(">") -> {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                Box(
                    modifier = Modifier.padding(end = 8.dp)
                        .size(width = 3.dp, height = 20.dp)
                        .background(MiuixTheme.colorScheme.primary)
                )
                MarkdownTextResult(
                    context = context,
                    text = trimmed.removePrefix(">").trim(),
                    inlineState = inlineState,
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    fontSize = bodyFontSize,
                    modifier = Modifier.weight(1f)
                )
            }
            inlineState
        }
        trimmed.startsWith("- ") || trimmed.startsWith("* ") || trimmed.startsWith("+ ") -> {
            MarkdownBullet(context, trimmed.drop(2).trim(), inlineState, fontSize = bodyFontSize)
        }
        Regex("""^\d+[.)]\s+.*""").matches(trimmed) -> {
            val marker = trimmed.substringBefore(' ').trim()
            MarkdownBullet(context, trimmed.removePrefix(marker).trim(), inlineState, marker, bodyFontSize)
        }
        else -> {
            MarkdownTextResult(
                context = context,
                text = line,
                inlineState = inlineState,
                fontSize = bodyFontSize,
                modifier = Modifier.padding(vertical = 2.dp)
            )
        }
    }
}

@Composable
internal fun MarkdownBullet(
    context: Context,
    text: String,
    inlineState: MarkdownInlineState,
    marker: String = "•",
    fontSize: androidx.compose.ui.unit.TextUnit = 13.sp
): MarkdownInlineState {
    var nextState = inlineState
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
        Text(
            text = marker,
            color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
            fontSize = fontSize,
            modifier = Modifier.padding(end = 8.dp)
        )
        nextState = MarkdownTextResult(
            context = context,
            text = text,
            inlineState = inlineState,
            fontSize = fontSize,
            modifier = Modifier.weight(1f)
        )
    }
    return nextState
}

@Composable
fun CodeBlock(code: String, onCopy: ((String) -> Unit)? = null) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MiuixTheme.colorScheme.surfaceVariant)
    ) {
        if (onCopy != null) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(start = 10.dp, top = 4.dp, end = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "代码",
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    fontSize = 11.sp,
                    modifier = Modifier.weight(1f)
                )
                Image(
                    imageVector = NavIcons.Copy,
                    contentDescription = "复制代码",
                    colorFilter = ColorFilter.tint(MiuixTheme.colorScheme.onSurfaceVariantSummary),
                    modifier = Modifier.size(26.dp).clip(RoundedCornerShape(4.dp))
                        .clickable { onCopy(code) }
                        .padding(6.dp)
                )
            }
        }
        Text(
            text = code,
            color = MiuixTheme.colorScheme.onSurface,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun Text(
    context: Context,
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MiuixTheme.colorScheme.onSurface,
    fontSize: androidx.compose.ui.unit.TextUnit = 13.sp,
    fontWeight: FontWeight? = null
) {
    MarkdownTextResult(context, text, MarkdownInlineState(), modifier, color, fontSize, fontWeight)
}

@Composable
internal fun MarkdownTextResult(
    context: Context,
    text: String,
    inlineState: MarkdownInlineState,
    modifier: Modifier = Modifier,
    color: Color = MiuixTheme.colorScheme.onSurface,
    fontSize: androidx.compose.ui.unit.TextUnit = 13.sp,
    fontWeight: FontWeight? = null
): MarkdownInlineState {
    val primary = MiuixTheme.colorScheme.primary
    val result = remember(text, primary, inlineState.bold) {
        buildMarkdownAnnotatedString(text, primary, inlineState)
    }
    ClickableText(
        text = result.text,
        modifier = modifier,
        style = TextStyle(color = color, fontSize = fontSize, fontWeight = fontWeight),
        onClick = { offset ->
            result.text.getStringAnnotations(MARKDOWN_LINK_TAG, offset, offset)
                .firstOrNull()
                ?.let { annotation -> openMarkdownLink(context, annotation.item) }
        }
    )
    return result.state
}

internal data class MarkdownInlineState(val bold: Boolean = false)

internal data class MarkdownInlineResult(
    val text: AnnotatedString,
    val state: MarkdownInlineState
)

internal fun buildMarkdownAnnotatedString(
    text: String,
    accent: Color,
    initialState: MarkdownInlineState
): MarkdownInlineResult {
    var state = initialState
    val annotated = buildAnnotatedString {
        state = appendInlineMarkdown(text, accent, state)
    }
    return MarkdownInlineResult(annotated, state)
}

internal fun AnnotatedString.Builder.appendInlineMarkdown(
    text: String,
    accent: Color,
    initialState: MarkdownInlineState
): MarkdownInlineState {
    var state = initialState
    var segmentStart = 0
    MARKDOWN_LINK_REGEX.findAll(text).forEach { match ->
        if (match.range.first > segmentStart) {
            state = appendInlineMarkdownSegment(
                text.substring(segmentStart, match.range.first),
                accent,
                state
            )
        }
        val label = match.groupValues[1]
        val url = match.groupValues[2].trim()
        if (label.isNotBlank() && url.isNotBlank()) {
            pushStringAnnotation(MARKDOWN_LINK_TAG, url)
            appendStyledInline(label, accent, state, link = true)
            pop()
        } else {
            append(match.value)
        }
        segmentStart = match.range.last + 1
    }
    if (segmentStart < text.length) {
        state = appendInlineMarkdownSegment(text.substring(segmentStart), accent, state)
    }
    return state
}

internal fun AnnotatedString.Builder.appendInlineMarkdownSegment(
    text: String,
    accent: Color,
    initialState: MarkdownInlineState
): MarkdownInlineState {
    var state = initialState
    var index = 0
    while (index < text.length) {
        when {
            text.startsWith("**", index) -> {
                state = state.copy(bold = !state.bold)
                index += 2
            }
            text[index] == '`' -> {
                val end = text.indexOf('`', index + 1)
                if (end > index) {
                    withStyle(SpanStyle(color = accent, fontFamily = FontFamily.Monospace)) {
                        append(text.substring(index + 1, end))
                    }
                    index = end + 1
                } else {
                    append(text[index])
                    index++
                }
            }
            else -> {
                appendStyledInline(text[index].toString(), accent, state, link = false)
                index++
            }
        }
    }
    return state
}

internal fun AnnotatedString.Builder.appendStyledInline(
    value: String,
    accent: Color,
    state: MarkdownInlineState,
    link: Boolean
) {
    val style = SpanStyle(
        color = if (link) accent else Color.Unspecified,
        fontWeight = when {
            link -> FontWeight.Medium
            state.bold -> FontWeight.SemiBold
            else -> null
        }
    )
    withStyle(style) {
        append(value)
    }
}

internal fun openMarkdownLink(context: Context, url: String) {
    val value = url.trim()
    if (value.isBlank()) return
    runCatching {
        val normalized = if (value.contains("://")) value else "https://$value"
        val uri = Uri.parse(normalized)
        val intent = Intent(Intent.ACTION_VIEW, uri)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}

}
