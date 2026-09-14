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

@Composable
internal fun WeChatTabletMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, WeChatTabletSettings.PREFS_NAME) }
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
            item { SmallTitle(text = "平板模式") }
            item {
                SettingsCard {
                    SwitchRow(
                        sp,
                        WeChatTabletSettings.KEY_ENABLE,
                        "平板模式",
                        "开启平板模式，退出微信登陆生效",
                        WeChatTabletSettings.DEFAULT_ENABLE
                    )
                }
            }
        }
    }
}

@Composable
internal fun AutoTransferMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, AutoTransferSettings.PREFS_NAME) }
    var templates by remember {
        mutableStateOf(TransferRuleConfig.parseTemplates(sp.getString(TransferRuleConfig.KEY_TEMPLATES, "")))
    }
    var bindings by remember {
        mutableStateOf(TransferRuleConfig.parseBindings(sp.getString(TransferRuleConfig.KEY_BINDINGS, "")))
    }
    var defaultTemplateId by remember {
        mutableStateOf(sp.getString(TransferRuleConfig.KEY_DEFAULT_TEMPLATE_ID, "") ?: "")
    }
    var whitelist by remember { mutableStateOf(sp.getString(AutoTransferSettings.KEY_WHITELIST, "") ?: "") }
    var blacklist by remember { mutableStateOf(sp.getString(AutoTransferSettings.KEY_BLACKLIST, "") ?: "") }
    var listMode by remember { mutableStateOf(sp.getInt(AutoTransferSettings.KEY_MODE, 0)) }
    var delayMs by remember { mutableStateOf(sp.getLong(AutoTransferSettings.KEY_DELAY_MS, 0L).toString()) }
    var delayMode by remember { mutableStateOf(sp.getInt(AutoTransferSettings.KEY_DELAY_MODE, TransferRuleConfig.DELAY_CUSTOM)) }
    var randomMinMs by remember { mutableStateOf(sp.getLong(AutoTransferSettings.KEY_DELAY_RANDOM_MIN, 500L).toString()) }
    var randomMaxMs by remember { mutableStateOf(sp.getLong(AutoTransferSettings.KEY_DELAY_RANDOM_MAX, 3000L).toString()) }
    var amountEnabled by remember { mutableStateOf(sp.getBoolean(AutoTransferSettings.KEY_AMOUNT_ENABLE, false)) }
    var amountCondition by remember { mutableStateOf(sp.getInt(AutoTransferSettings.KEY_AMOUNT_COND, 1)) }
    var amountAction by remember { mutableStateOf(sp.getInt(AutoTransferSettings.KEY_AMOUNT_ACTION, 0)) }
    var amountValue by remember { mutableStateOf(sp.getString(AutoTransferSettings.KEY_AMOUNT_VALUE, "0") ?: "0") }
    var keywords by remember { mutableStateOf(sp.getString(AutoTransferSettings.KEY_KEYWORDS, "") ?: "") }
    var keywordMode by remember { mutableStateOf(sp.getInt(AutoTransferSettings.KEY_KEYWORD_MODE, 0)) }
    var quietEnabled by remember { mutableStateOf(sp.getBoolean(AutoTransferSettings.KEY_QUIET_ENABLE, false)) }
    var quietStart by remember { mutableStateOf(formatRedPacketSecond(sp.getInt(AutoTransferSettings.KEY_QUIET_START_SECOND, 0))) }
    var quietEnd by remember { mutableStateOf(formatRedPacketSecond(sp.getInt(AutoTransferSettings.KEY_QUIET_END_SECOND, 0))) }
    var replySteps by remember { mutableStateOf(loadGlobalTransferReplySteps(sp)) }
    var groupReplySteps by remember { mutableStateOf(loadGlobalGroupTransferReplySteps(sp)) }
    var notifySystemEnabled by remember { mutableStateOf(sp.getBoolean(AutoTransferSettings.KEY_NOTIFY_SYSTEM_ENABLE, false)) }
    var notifyToastEnabled by remember { mutableStateOf(sp.getBoolean(AutoTransferSettings.KEY_NOTIFY_TOAST_ENABLE, false)) }
    var notifySoundEnabled by remember { mutableStateOf(sp.getBoolean(AutoTransferSettings.KEY_NOTIFY_SOUND_ENABLE, false)) }
    var notifySoundMode by remember { mutableStateOf(sp.getInt(AutoTransferSettings.KEY_NOTIFY_SOUND_MODE, AutoTransferSettings.NOTIFY_SOUND_MODE_SYSTEM)) }
    var notifyVibrateEnabled by remember { mutableStateOf(sp.getBoolean(AutoTransferSettings.KEY_NOTIFY_VIBRATE_ENABLE, false)) }
    var notifySoundUri by remember { mutableStateOf(sp.getString(AutoTransferSettings.KEY_NOTIFY_SOUND_URI, "") ?: "") }
    var notifyText by remember { mutableStateOf(sp.getString(AutoTransferSettings.KEY_NOTIFY_TEXT, "已收款 {amount} 元") ?: "") }
    var notifyToastText by remember { mutableStateOf(sp.getString(AutoTransferSettings.KEY_NOTIFY_TOAST_TEXT, "已收款 {amount} 元") ?: "") }
    var announceEnabled by remember { mutableStateOf(sp.getBoolean(AutoTransferSettings.KEY_ANNOUNCE_ENABLE, false)) }
    var announceText by remember { mutableStateOf(sp.getString(AutoTransferSettings.KEY_ANNOUNCE_TEXT, "收到转账 {amount} 元") ?: "") }
    var timeFormat by remember {
        mutableStateOf(
            PaymentTemplateTimeFormatter.normalizePattern(
                sp.getString(AutoTransferSettings.KEY_TIME_FORMAT, AutoTransferSettings.DEFAULT_TIME_FORMAT)
            )
        )
    }
    var receiveAccount by remember {
        mutableStateOf(
            sp.getString(
                AutoTransferSettings.KEY_RECEIVE_ACCOUNT,
                TransferReceiveAccountStore.DEFAULT_KEY
            ) ?: TransferReceiveAccountStore.DEFAULT_KEY
        )
    }
    val receiveAccountOptions = remember { transferReceiveAccountOptions(context) }
    if (receiveAccountOptions.none { it.value == receiveAccount }) {
        receiveAccount = TransferReceiveAccountStore.DEFAULT_KEY
    }
    var picker by remember { mutableStateOf<ContactPickerRequest?>(null) }
    var templateEditor by remember { mutableStateOf<TransferTemplateEditorRequest?>(null) }
    var bindingEditor by remember { mutableStateOf<TransferBindingEditorRequest?>(null) }
    var showTemplates by remember { mutableStateOf(false) }
    var showBindings by remember { mutableStateOf(false) }
    var showBatchApply by remember { mutableStateOf(false) }
    var showReplySteps by remember { mutableStateOf(false) }
    var replyTarget by remember { mutableStateOf(TransferReplyTarget.PRIVATE) }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()

    fun persistRules(
        nextTemplates: List<TransferRuleTemplate> = templates,
        nextBindings: List<TransferRuleBinding> = bindings,
        nextDefault: String = defaultTemplateId
    ) {
        sp.edit()
            .putString(TransferRuleConfig.KEY_TEMPLATES, TransferRuleConfig.encodeTemplates(nextTemplates))
            .putString(TransferRuleConfig.KEY_BINDINGS, TransferRuleConfig.encodeBindings(nextBindings))
            .putString(TransferRuleConfig.KEY_DEFAULT_TEMPLATE_ID, nextDefault)
            .commit()
    }

    val route: AutoTransferRoute = when {
        templateEditor != null -> AutoTransferRoute.TemplateEditor(templateEditor!!)
        bindingEditor != null -> AutoTransferRoute.BindingEditor(bindingEditor!!)
        picker != null -> AutoTransferRoute.ContactPicker(picker!!)
        showReplySteps -> AutoTransferRoute.GlobalReplySteps(replyTarget)
        showTemplates -> AutoTransferRoute.TemplateManager
        showBindings -> AutoTransferRoute.BindingManager
        showBatchApply -> AutoTransferRoute.BatchApply
        else -> AutoTransferRoute.Main
    }

    SettingsRouteTransition(
        targetState = route,
        label = "AutoTransferRouteTransition",
        depthOf = { it.depth() }
    ) { currentRoute ->
        when (currentRoute) {
            is AutoTransferRoute.ContactPicker -> {
                val request = currentRoute.request
                ContactPickerPage(
                    context = context,
                    request = request,
                    onBack = { picker = null },
                    onConfirm = { selected ->
                        request.onValue(formatIds(selected.map { it.id }))
                        picker = null
                    }
                )
            }
            is AutoTransferRoute.TemplateEditor -> TransferTemplateEditorPage(
                context = context,
                request = currentRoute.request,
                onBack = { templateEditor = null },
                onSave = { updated ->
                    val request = currentRoute.request
                    val next = if (request.index in templates.indices) {
                        templates.toMutableList().also { it[request.index] = updated }
                    } else templates + updated
                    templates = next
                    if (defaultTemplateId.isBlank()) defaultTemplateId = updated.id
                    persistRules(nextTemplates = next, nextDefault = defaultTemplateId)
                    templateEditor = null
                },
                onDelete = {
                    val request = currentRoute.request
                    if (request.index in templates.indices) {
                        val deleted = templates[request.index].id
                        templates = templates.toMutableList().also { it.removeAt(request.index) }
                        bindings = bindings.map { if (it.templateId == deleted) it.copy(templateId = "") else it }
                        if (defaultTemplateId == deleted) defaultTemplateId = templates.firstOrNull()?.id.orEmpty()
                        persistRules()
                    }
                    templateEditor = null
                }
            )
            is AutoTransferRoute.BindingEditor -> TransferBindingEditorPage(
                context = context,
                request = currentRoute.request,
                templates = templates,
                onBack = { bindingEditor = null },
                onSave = { updated ->
                    val base = bindings.toMutableList()
                    if (currentRoute.request.index in base.indices) base.removeAt(currentRoute.request.index)
                    bindings = upsertTransferBindings(base, listOf(updated))
                    persistRules(nextBindings = bindings)
                    bindingEditor = null
                },
                onDelete = {
                    if (currentRoute.request.index in bindings.indices) {
                        bindings = bindings.toMutableList().also { it.removeAt(currentRoute.request.index) }
                        persistRules(nextBindings = bindings)
                    }
                    bindingEditor = null
                }
            )
            AutoTransferRoute.TemplateManager -> TransferTemplateListPage(
                templates = templates,
                onBack = { showTemplates = false },
                onOpen = { index, value -> templateEditor = TransferTemplateEditorRequest(index, value, true) },
                onAdd = { templateEditor = TransferTemplateEditorRequest(templates.size, newTransferTemplate(templates.size + 1, sp), false) }
            )
            AutoTransferRoute.BindingManager -> TransferBindingListPage(
                bindings = bindings,
                templates = templates,
                onBack = { showBindings = false },
                onOpen = { index, value -> bindingEditor = TransferBindingEditorRequest(index, value, true) },
                onDeleteBindings = { targets ->
                    val targetIds = targets.mapTo(HashSet()) { it.id }
                    bindings = bindings.filterNot { it.id in targetIds }
                    persistRules(nextBindings = bindings)
                    Toast.makeText(context, "已删除 ${targets.size} 个适用聊天", Toast.LENGTH_SHORT).show()
                },
                onAdd = {
                    picker = ContactPickerRequest(
                        title = "选择适用聊天",
                        mode = ContactPickerMode.BOTH,
                        multiSelect = true,
                        existingValue = "",
                        enableLabels = true,
                        onValue = { value ->
                            val additions = parseIds(value).map { id ->
                                bindings.firstOrNull { it.targetId == id }
                                    ?: TransferRuleBinding(id, id, transferContactLabel(id), false, if (templates.size == 1) templates.first().id else "")
                            }
                            if (additions.size == 1) {
                                val item = additions.first()
                                val index = bindings.indexOfFirst { it.targetId == item.targetId }
                                bindingEditor = TransferBindingEditorRequest(index.takeIf { it >= 0 } ?: bindings.size, item, index >= 0)
                            } else if (additions.isNotEmpty()) {
                                bindings = upsertTransferBindings(bindings, additions)
                                persistRules(nextBindings = bindings)
                            }
                        }
                    )
                }
            )
            AutoTransferRoute.BatchApply -> TransferBatchApplyPage(
                templates = templates,
                bindings = bindings,
                onBack = { showBatchApply = false },
                onPickChats = { templateId ->
                    picker = ContactPickerRequest(
                        title = "批量套用收款模板",
                        mode = ContactPickerMode.BOTH,
                        multiSelect = true,
                        existingValue = formatIds(bindings.filter { it.templateId == templateId }.map { it.targetId }),
                        enableLabels = true,
                        onValue = { value ->
                            val selected = parseIds(value)
                            val retained = bindings.filterNot { it.templateId == templateId && it.targetId !in selected }
                            val additions = selected.map { id ->
                                TransferRuleBinding(id, id, transferContactLabel(id), true, templateId)
                            }
                            bindings = upsertTransferBindings(retained, additions)
                            persistRules(nextBindings = bindings)
                            Toast.makeText(context, "模板已套用到 ${selected.size} 个聊天", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            )
            is AutoTransferRoute.GlobalReplySteps -> RedPacketReplyStepsPage(
                context = context,
                title = if (currentRoute.target == TransferReplyTarget.GROUP) "群聊收款回复" else "私聊收款回复",
                initialSteps = if (currentRoute.target == TransferReplyTarget.GROUP) groupReplySteps else replySteps,
                templateVariables = transferTemplateVariables,
                onBack = { showReplySteps = false },
                onSave = {
                    if (currentRoute.target == TransferReplyTarget.GROUP) groupReplySteps = it else replySteps = it
                    showReplySteps = false
                }
            )
            AutoTransferRoute.Main -> PageScaffold(
                title = provider.title(),
                largeTitle = provider.title(),
                scrollBehavior = scrollBehavior,
                bottomBar = {
                    BottomActionBar(
                        primaryText = "保存设置",
                        onPrimaryClick = save@{
                            if (!PaymentTemplateTimeFormatter.isValidPattern(timeFormat)) {
                                Toast.makeText(context, "时间格式无效", Toast.LENGTH_SHORT).show()
                                return@save
                            }
                            val normalizedTimeFormat = PaymentTemplateTimeFormatter.normalizePattern(timeFormat)
                            val minDelay = randomMinMs.toLongOrNull()?.coerceIn(0L, 600000L) ?: 0L
                            val maxDelay = randomMaxMs.toLongOrNull()?.coerceIn(minDelay, 600000L) ?: minDelay
                            val cleanSteps = cleanRedPacketReplySteps(replySteps)
                            val cleanGroupSteps = cleanRedPacketReplySteps(groupReplySteps)
                            sp.edit()
                                .putString(AutoTransferSettings.KEY_WHITELIST, whitelist)
                                .putString(AutoTransferSettings.KEY_BLACKLIST, blacklist)
                                .putInt(AutoTransferSettings.KEY_MODE, listMode)
                                .putLong(AutoTransferSettings.KEY_DELAY_MS, delayMs.toLongOrNull()?.coerceIn(0L, 600000L) ?: 0L)
                                .putInt(AutoTransferSettings.KEY_DELAY_MODE, delayMode)
                                .putLong(AutoTransferSettings.KEY_DELAY_RANDOM_MIN, minDelay)
                                .putLong(AutoTransferSettings.KEY_DELAY_RANDOM_MAX, maxDelay)
                                .putString(AutoTransferSettings.KEY_AMOUNT_VALUE, amountValue)
                                .putBoolean(AutoTransferSettings.KEY_AMOUNT_ENABLE, amountEnabled)
                                .putInt(AutoTransferSettings.KEY_AMOUNT_COND, amountCondition)
                                .putInt(AutoTransferSettings.KEY_AMOUNT_ACTION, amountAction)
                                .putInt(AutoTransferSettings.KEY_KEYWORD_MODE, keywordMode)
                                .putString(AutoTransferSettings.KEY_KEYWORDS, if (keywordMode == 0) "" else keywords)
                                .putString(AutoTransferSettings.KEY_RECEIVE_ACCOUNT, receiveAccount)
                                .putBoolean(AutoTransferSettings.KEY_QUIET_ENABLE, quietEnabled)
                                .putInt(AutoTransferSettings.KEY_QUIET_START_SECOND, parseRedPacketSecond(quietStart, 0))
                                .putInt(AutoTransferSettings.KEY_QUIET_END_SECOND, parseRedPacketSecond(quietEnd, 0))
                                .putString(AutoTransferSettings.KEY_REPLY_ITEMS, RedPacketRuleConfig.encodeReplySteps(cleanSteps))
                                .putString(AutoTransferSettings.KEY_REPLY_GROUP_ITEMS, RedPacketRuleConfig.encodeReplySteps(cleanGroupSteps))
                                .putBoolean(AutoTransferSettings.KEY_REPLY_ENABLE, cleanSteps.isNotEmpty())
                                .putString(AutoTransferSettings.KEY_REPLY_TEXT, cleanSteps.firstOrNull()?.content.orEmpty())
                                .putBoolean(AutoTransferSettings.KEY_NOTIFY_SYSTEM_ENABLE, notifySystemEnabled)
                                .putBoolean(AutoTransferSettings.KEY_NOTIFY_TOAST_ENABLE, notifyToastEnabled)
                                .putBoolean(AutoTransferSettings.KEY_NOTIFY_SOUND_ENABLE, notifySoundEnabled)
                                .putInt(AutoTransferSettings.KEY_NOTIFY_SOUND_MODE, notifySoundMode)
                                .putBoolean(AutoTransferSettings.KEY_NOTIFY_VIBRATE_ENABLE, notifyVibrateEnabled)
                                .putString(AutoTransferSettings.KEY_NOTIFY_SOUND_URI, notifySoundUri)
                                .putString(AutoTransferSettings.KEY_NOTIFY_TEXT, notifyText)
                                .putString(AutoTransferSettings.KEY_NOTIFY_TOAST_TEXT, notifyToastText)
                                .putBoolean(AutoTransferSettings.KEY_ANNOUNCE_ENABLE, announceEnabled)
                                .putString(AutoTransferSettings.KEY_ANNOUNCE_TEXT, announceText)
                                .putString(AutoTransferSettings.KEY_TIME_FORMAT, normalizedTimeFormat)
                                .apply()
                            replySteps = cleanSteps
                            groupReplySteps = cleanGroupSteps
                            timeFormat = normalizedTimeFormat
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
                    item { SmallTitle(text = "规则") }
                    item {
                        SettingsCard {
                            SwitchRow(sp, AutoTransferSettings.KEY_ENABLE, "自动收款", "自动领取待收款转账", false)
                            InsetDivider()
                            ActionRow("收款规则模板", if (templates.isEmpty()) "暂无模板" else "${templates.size} 个模板") { showTemplates = true }
                            InsetDivider()
                            PopupChoiceRow(
                                title = "默认规则",
                                summary = describeTransferDefault(defaultTemplateId, templates),
                                options = listOf(PopupChoice("旧版全局设置", "")) + templates.map { PopupChoice(it.name, it.id) },
                                currentValue = defaultTemplateId,
                                onValueChanged = {
                                    defaultTemplateId = it
                                    persistRules(nextDefault = it)
                                }
                            )
                            InsetDivider()
                            ActionRow("适用聊天", if (bindings.isEmpty()) "暂无单独配置" else "${bindings.size} 个聊天") { showBindings = true }
                            InsetDivider()
                            ActionRow("批量套用模板", "一次给多个聊天分配同一规则") { showBatchApply = true }
                        }
                    }
                    item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "全局收款") }
                    item {
                        SettingsCard {
                            PopupChoiceRow("收款位置", "使用微信当前账号提供的收款账户", receiveAccountOptions, receiveAccount, onValueChanged = { receiveAccount = it })
                            InsetDivider()
                            SwitchRow(sp, AutoTransferSettings.KEY_REFUND_REJECTED, "拒收时退回", "规则不通过时原路退回", false)
                            InsetDivider()
                            PopupOptionRow("收款延迟", transferDelayModeLabel(delayMode), redPacketDelayModeOptions(true), delayMode, onValueChanged = { delayMode = it })
                            if (delayMode == TransferRuleConfig.DELAY_CUSTOM) {
                                InsetDivider(); NumberInputRow("自定义延迟", "单位 ms", delayMs) { delayMs = it }
                            } else if (delayMode == TransferRuleConfig.DELAY_RANDOM) {
                                InsetDivider(); NumberInputRow("最小延迟", "单位 ms", randomMinMs) { randomMinMs = it }
                                InsetDivider(); NumberInputRow("最大延迟", "单位 ms", randomMaxMs) { randomMaxMs = it }
                            }
                        }
                    }
                    item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "全局过滤") }
                    item {
                        SettingsCard {
                            PopupOptionRow("收款范围", transferListModeLabel(listMode), optionItems("全部接收" to 0, "只接收白名单" to 1, "拒收黑名单" to 2), listMode, onValueChanged = { listMode = it; sp.edit().putInt(AutoTransferSettings.KEY_MODE, it).apply() })
                            if (listMode == 1 || listMode == 2) {
                                InsetDivider()
                                ActionRow(if (listMode == 1) "白名单" else "黑名单", autoReplySelectedIdSummary(if (listMode == 1) whitelist else blacklist)) {
                                    picker = ContactPickerRequest(if (listMode == 1) "选择白名单" else "选择黑名单", ContactPickerMode.BOTH, true, if (listMode == 1) whitelist else blacklist, {
                                        if (listMode == 1) whitelist = it else blacklist = it
                                    }, true)
                                }
                            }
                            InsetDivider()
                            SwitchRow(amountEnabled, "启用金额规则", "按转账金额决定接收或拒收") { amountEnabled = it; sp.edit().putBoolean(AutoTransferSettings.KEY_AMOUNT_ENABLE, it).apply() }
                            if (amountEnabled) {
                                InsetDivider(); PopupOptionRow("金额条件", transferAmountConditionLabel(amountCondition), optionItems("大于" to 0, "小于" to 1, "等于" to 2), amountCondition, onValueChanged = { amountCondition = it; sp.edit().putInt(AutoTransferSettings.KEY_AMOUNT_COND, it).apply() })
                                InsetDivider(); InputRow("金额数值", "单位元，例如 10.5", amountValue) { amountValue = it.filter { ch -> ch.isDigit() || ch == '.' } }
                                InsetDivider(); PopupOptionRow("命中后动作", transferAmountActionLabel(amountAction), optionItems("拒收/忽略" to 0, "仅接收满足条件" to 1), amountAction, onValueChanged = { amountAction = it; sp.edit().putInt(AutoTransferSettings.KEY_AMOUNT_ACTION, it).apply() })
                            }
                            InsetDivider()
                            PopupOptionRow("关键词规则", transferKeywordModeLabel(keywordMode), optionItems("不启用" to 0, "必须包含关键词" to 1, "包含则拒收" to 2), keywordMode, onValueChanged = { keywordMode = it; sp.edit().putInt(AutoTransferSettings.KEY_KEYWORD_MODE, it).apply() })
                            if (keywordMode != 0) {
                                InsetDivider(); InputRow("关键词", "多个关键词用 |、逗号或换行分隔", keywords, minLines = 2) { keywords = it }
                            }
                            InsetDivider()
                            SwitchRow(quietEnabled, "禁收时段", "指定时段内不自动收款") { quietEnabled = it }
                            if (quietEnabled) {
                                InsetDivider(); TimeOfDayPickerRow("开始时间", quietStart) { quietStart = it }
                                InsetDivider(); TimeOfDayPickerRow("结束时间", quietEnd) { quietEnd = it }
                            }
                        }
                    }
                    item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "模板变量") }
                    item {
                        SettingsCard {
                            InputRow(
                                "时间变量格式",
                                "用于 {time}，例如 yyyy-MM-dd HH:mm:ss",
                                timeFormat
                            ) { timeFormat = it }
                        }
                    }
                    item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "收款后回复") }
                    item {
                        SettingsCard {
                            SelectRow("私聊收款回复", describeRedPacketReplySteps(replySteps)) {
                                replyTarget = TransferReplyTarget.PRIVATE
                                showReplySteps = true
                            }
                            InsetDivider()
                            SelectRow("群聊收款回复", describeRedPacketReplySteps(groupReplySteps)) {
                                replyTarget = TransferReplyTarget.GROUP
                                showReplySteps = true
                            }
                        }
                    }
                    item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "成功提醒") }
                    item {
                        TransferNotificationSettingsCard(
                            context, notifySystemEnabled, { notifySystemEnabled = it }, notifyToastEnabled, { notifyToastEnabled = it },
                            notifySoundEnabled, { notifySoundEnabled = it }, notifySoundMode, { notifySoundMode = it; notifySoundUri = "" },
                            notifyVibrateEnabled, { notifyVibrateEnabled = it }, notifySoundUri, { notifySoundUri = it },
                            notifyText, { notifyText = it }, notifyToastText, { notifyToastText = it },
                            announceEnabled, { announceEnabled = it }, announceText, { announceText = it }
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun TransferTemplateListPage(
    templates: List<TransferRuleTemplate>,
    onBack: () -> Unit,
    onOpen: (Int, TransferRuleTemplate) -> Unit,
    onAdd: () -> Unit
) {
    val scrollBehavior = MiuixScrollBehavior()
    PageScaffold(
        title = "收款规则模板",
        largeTitle = "收款规则模板",
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar("新增模板", onAdd, "返回", onBack)
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(text = "模板") }
            item {
                SettingsCard {
                    if (templates.isEmpty()) {
                        EmptyText("暂无模板。新增后可设为默认规则或分配给指定聊天。")
                    } else {
                        templates.forEachIndexed { index, template ->
                            SelectRow(
                                template.name.ifBlank { "模板 ${index + 1}" },
                                describeTransferTemplate(template)
                            ) { onOpen(index, template) }
                            if (index < templates.lastIndex) InsetDivider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun TransferBindingListPage(
    bindings: List<TransferRuleBinding>,
    templates: List<TransferRuleTemplate>,
    onBack: () -> Unit,
    onOpen: (Int, TransferRuleBinding) -> Unit,
    onAdd: () -> Unit,
    onDeleteBindings: (List<TransferRuleBinding>) -> Unit
) {
    val context = LocalContext.current
    var category by remember { mutableStateOf(ConversationRuleCategory.ALL) }
    var query by remember { mutableStateOf("") }
    var batchDeleteMode by remember { mutableStateOf(false) }
    var selectedIds by remember { mutableStateOf<Set<String>>(emptySet()) }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    val lower = query.trim().lowercase(Locale.US)
    val visible = bindings.mapIndexed { index, value -> index to value }.filter { (_, value) ->
        conversationRuleCategoryMatches(value, category) && (
            lower.isBlank() || value.label.lowercase(Locale.US).contains(lower) ||
                value.targetId.lowercase(Locale.US).contains(lower) ||
                templates.firstOrNull { it.id == value.templateId }?.name?.lowercase(Locale.US)?.contains(lower) == true
            )
    }
    val visibleIds = visible.mapTo(LinkedHashSet()) { it.second.id }
    val allVisibleSelected = visibleIds.isNotEmpty() && visibleIds.all { it in selectedIds }
    val selectedBindings = bindings.filter { it.id in selectedIds }
    val scrollBehavior = MiuixScrollBehavior()
    PageScaffold(
        title = "适用聊天",
        largeTitle = "适用聊天",
        scrollBehavior = scrollBehavior,
        bottomBar = {
            if (batchDeleteMode) {
                BottomActionBar(
                    primaryText = "删除所选（${selectedBindings.size}）",
                    onPrimaryClick = {
                        if (selectedBindings.isEmpty()) {
                            Toast.makeText(context, "请先选择适用聊天", Toast.LENGTH_SHORT).show()
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
                    primaryText = "添加聊天",
                    onPrimaryClick = onAdd,
                    secondaryText = "返回",
                    onSecondaryClick = onBack,
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
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item {
                SettingsCard {
                    ConversationRuleCategoryTabs(
                        selected = category,
                        onSelected = { category = it },
                        includeOfficial = false
                    )
                }
            }
            item { SettingsCard { InputRow("搜索聊天", "昵称 / ID / 模板名", query) { query = it } } }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "聊天 · ${visible.size}/${bindings.size} 项") }
            item {
                SettingsCard {
                    if (visible.isEmpty()) {
                        EmptyText(if (bindings.isEmpty()) "暂无适用聊天。" else "没有匹配结果。")
                    } else {
                        visible.forEachIndexed { row, (index, binding) ->
                            if (batchDeleteMode) {
                                OptionChoiceRow(
                                    item = OptionItem(
                                        label = binding.label.ifBlank { binding.targetId },
                                        value = index,
                                        summary = describeTransferBinding(binding, templates)
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
                                    binding.label.ifBlank { binding.targetId },
                                    describeTransferBinding(binding, templates)
                                ) { onOpen(index, binding) }
                            }
                            if (row < visible.lastIndex) InsetDivider()
                        }
                    }
                }
            }
        }
    }
    BatchDeleteConfirmDialog(
        show = showDeleteConfirm,
        message = "将删除已选的 ${selectedBindings.size} 个适用聊天，此操作不可撤销。",
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
internal fun TransferBindingEditorPage(
    context: Context,
    request: TransferBindingEditorRequest,
    templates: List<TransferRuleTemplate>,
    onBack: () -> Unit,
    onSave: (TransferRuleBinding) -> Unit,
    onDelete: () -> Unit
) {
    var enabled by remember(request) { mutableStateOf(request.binding.enabled) }
    var templateId by remember(request) { mutableStateOf(request.binding.templateId) }
    val scrollBehavior = MiuixScrollBehavior()
    PageScaffold(
        title = request.binding.label.ifBlank { request.binding.targetId },
        largeTitle = request.binding.label.ifBlank { request.binding.targetId },
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                "保存聊天",
                {
                    onSave(request.binding.copy(enabled = enabled, templateId = templateId))
                    Toast.makeText(context, "适用聊天已保存", Toast.LENGTH_SHORT).show()
                },
                "返回",
                onBack
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
            item { SmallTitle(text = "聊天") }
            item {
                SettingsCard {
                    InfoRow("ID", request.binding.targetId)
                    InsetDivider()
                    SwitchRow(enabled, "启用自动收款", "关闭后该聊天不会自动收款") { enabled = it }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "模板") }
            item {
                SettingsCard {
                    OptionChoiceRow(
                        OptionItem("跟随默认规则", -1, "使用默认模板或全局设置"),
                        templateId.isBlank()
                    ) { templateId = "" }
                    templates.forEachIndexed { index, template ->
                        InsetDivider()
                        OptionChoiceRow(
                            OptionItem(template.name.ifBlank { "模板 ${index + 1}" }, index, describeTransferTemplate(template)),
                            templateId == template.id
                        ) { templateId = template.id }
                    }
                }
            }
            if (request.canDelete) {
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "操作") }
                item { SettingsCard { ActionRow("移除适用聊天", "移除后恢复默认规则", onDelete) } }
            }
        }
    }
}

@Composable
internal fun TransferBatchApplyPage(
    templates: List<TransferRuleTemplate>,
    bindings: List<TransferRuleBinding>,
    onBack: () -> Unit,
    onPickChats: (String) -> Unit
) {
    val scrollBehavior = MiuixScrollBehavior()
    PageScaffold(
        title = "批量套用模板",
        largeTitle = "批量套用模板",
        scrollBehavior = scrollBehavior,
        bottomBar = { BottomActionBar("返回", onBack) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(text = "选择模板") }
            item {
                SettingsCard {
                    if (templates.isEmpty()) {
                        EmptyText("请先新增收款规则模板。")
                    } else {
                        templates.forEachIndexed { index, template ->
                            val count = bindings.count { it.templateId == template.id }
                            SelectRow(template.name, "$count 个聊天 · ${describeTransferTemplate(template)}") {
                                onPickChats(template.id)
                            }
                            if (index < templates.lastIndex) InsetDivider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun TransferTemplateEditorPage(
    context: Context,
    request: TransferTemplateEditorRequest,
    onBack: () -> Unit,
    onSave: (TransferRuleTemplate) -> Unit,
    onDelete: () -> Unit
) {
    var value by remember(request) { mutableStateOf(request.template) }
    var showReplySteps by remember { mutableStateOf(false) }
    var replyTarget by remember { mutableStateOf(TransferReplyTarget.PRIVATE) }
    var groupReplySteps by remember(request) {
        mutableStateOf(request.template.groupReplySteps ?: request.template.replySteps)
    }
    var picker by remember { mutableStateOf<ContactPickerRequest?>(null) }
    val accountOptions = remember { transferReceiveAccountOptions(context) }
    if (showReplySteps) {
        RedPacketReplyStepsPage(
            context = context,
            title = if (replyTarget == TransferReplyTarget.GROUP) "模板群聊收款回复" else "模板私聊收款回复",
            initialSteps = if (replyTarget == TransferReplyTarget.GROUP) groupReplySteps else value.replySteps,
            templateVariables = transferTemplateVariables,
            onBack = { showReplySteps = false },
            onSave = {
                if (replyTarget == TransferReplyTarget.GROUP) groupReplySteps = it else value = value.copy(replySteps = it)
                showReplySteps = false
            }
        )
        return
    }
    picker?.let { requestPicker ->
        ContactPickerPage(
            context = context,
            request = requestPicker,
            onBack = { picker = null },
            onConfirm = {
                requestPicker.onValue(formatIds(it.map { option -> option.id }))
                picker = null
            }
        )
        return
    }
    val scrollBehavior = MiuixScrollBehavior()
    PageScaffold(
        title = value.name.ifBlank { "收款模板" },
        largeTitle = value.name.ifBlank { "收款模板" },
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                "保存模板",
                {
                    onSave(value.copy(
                        name = value.name.ifBlank { "收款模板" },
                        replySteps = cleanRedPacketReplySteps(value.replySteps),
                        groupReplySteps = cleanRedPacketReplySteps(groupReplySteps)
                    ))
                    Toast.makeText(context, "收款模板已保存", Toast.LENGTH_SHORT).show()
                },
                "返回",
                onBack
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
            item { SmallTitle(text = "模板") }
            item {
                SettingsCard {
                    InputRow("模板名称", "用于默认规则和聊天绑定", value.name) { value = value.copy(name = it) }
                    InsetDivider()
                    SwitchRow(value.enabled, "启用模板", "关闭后使用该模板的聊天不会自动收款") { value = value.copy(enabled = it) }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "收款") }
            item {
                SettingsCard {
                    PopupChoiceRow("收款位置", "选择转账到账账户", accountOptions, value.receiveAccount, onValueChanged = { value = value.copy(receiveAccount = it) })
                    InsetDivider()
                    SwitchRow(value.refundRejected, "拒收时退回", "规则不通过时原路退回") { value = value.copy(refundRejected = it) }
                    InsetDivider()
                    PopupOptionRow("收款延迟", transferDelayModeLabel(value.delayMode), redPacketDelayModeOptions(true), value.delayMode, onValueChanged = { value = value.copy(delayMode = it) })
                    if (value.delayMode == TransferRuleConfig.DELAY_CUSTOM) {
                        InsetDivider(); NumberInputRow("自定义延迟", "单位 ms", value.delayMs.toString()) { value = value.copy(delayMs = it.toLongOrNull()?.coerceIn(0L, 600000L) ?: 0L) }
                    } else if (value.delayMode == TransferRuleConfig.DELAY_RANDOM) {
                        InsetDivider(); NumberInputRow("最小延迟", "单位 ms", value.randomMinMs.toString()) { value = value.copy(randomMinMs = it.toLongOrNull()?.coerceIn(0L, 600000L) ?: 0L) }
                        InsetDivider(); NumberInputRow("最大延迟", "单位 ms", value.randomMaxMs.toString()) { value = value.copy(randomMaxMs = it.toLongOrNull()?.coerceIn(value.randomMinMs, 600000L) ?: value.randomMinMs) }
                    }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "过滤") }
            item {
                SettingsCard {
                    PopupOptionRow("收款范围", transferListModeLabel(value.listMode), optionItems("全部接收" to 0, "只接收白名单" to 1, "拒收黑名单" to 2), value.listMode, onValueChanged = { value = value.copy(listMode = it) })
                    if (value.listMode == 1 || value.listMode == 2) {
                        InsetDivider()
                        val listValue = if (value.listMode == 1) value.whitelist else value.blacklist
                        ActionRow(if (value.listMode == 1) "白名单" else "黑名单", autoReplySelectedIdSummary(listValue)) {
                            picker = ContactPickerRequest(
                                if (value.listMode == 1) "选择白名单" else "选择黑名单",
                                ContactPickerMode.BOTH,
                                true,
                                listValue,
                                { selected -> value = if (value.listMode == 1) value.copy(whitelist = selected) else value.copy(blacklist = selected) },
                                true
                            )
                        }
                    }
                    InsetDivider()
                    SwitchRow(value.amountEnabled, "启用金额规则", "按转账金额决定接收或拒收") { value = value.copy(amountEnabled = it) }
                    if (value.amountEnabled) {
                        InsetDivider(); PopupOptionRow("金额条件", transferAmountConditionLabel(value.amountCondition), optionItems("大于" to 0, "小于" to 1, "等于" to 2), value.amountCondition, onValueChanged = { value = value.copy(amountCondition = it) })
                        InsetDivider(); InputRow("金额数值", "单位元，例如 10.5", value.amountValue) { text -> value = value.copy(amountValue = text.filter { it.isDigit() || it == '.' }) }
                        InsetDivider(); PopupOptionRow("命中后动作", transferAmountActionLabel(value.amountAction), optionItems("拒收/忽略" to 0, "仅接收满足条件" to 1), value.amountAction, onValueChanged = { value = value.copy(amountAction = it) })
                    }
                    InsetDivider()
                    PopupOptionRow("关键词规则", transferKeywordModeLabel(value.keywordMode), optionItems("不启用" to 0, "必须包含关键词" to 1, "包含则拒收" to 2), value.keywordMode, onValueChanged = { value = value.copy(keywordMode = it) })
                    if (value.keywordMode != 0) {
                        InsetDivider(); InputRow("关键词", "多个关键词用 |、逗号或换行分隔", value.keywords, minLines = 2) { value = value.copy(keywords = it) }
                    }
                    InsetDivider()
                    SwitchRow(value.quietEnabled, "禁收时段", "指定时段内不自动收款") { value = value.copy(quietEnabled = it) }
                    if (value.quietEnabled) {
                        InsetDivider(); TimeOfDayPickerRow("开始时间", formatRedPacketSecond(value.quietStartSecond)) { value = value.copy(quietStartSecond = parseRedPacketSecond(it, value.quietStartSecond)) }
                        InsetDivider(); TimeOfDayPickerRow("结束时间", formatRedPacketSecond(value.quietEndSecond)) { value = value.copy(quietEndSecond = parseRedPacketSecond(it, value.quietEndSecond)) }
                    }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "收款后回复") }
            item {
                SettingsCard {
                    SelectRow("私聊收款回复", describeRedPacketReplySteps(value.replySteps)) {
                        replyTarget = TransferReplyTarget.PRIVATE
                        showReplySteps = true
                    }
                    InsetDivider()
                    SelectRow("群聊收款回复", describeRedPacketReplySteps(groupReplySteps)) {
                        replyTarget = TransferReplyTarget.GROUP
                        showReplySteps = true
                    }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "成功提醒") }
            item {
                TransferNotificationSettingsCard(
                    context,
                    value.notifySystemEnabled, { value = value.copy(notifySystemEnabled = it) },
                    value.notifyToastEnabled, { value = value.copy(notifyToastEnabled = it) },
                    value.notifySoundEnabled, { value = value.copy(notifySoundEnabled = it) },
                    value.notifySoundMode, { value = value.copy(notifySoundMode = it, notifySoundUri = "") },
                    value.notifyVibrateEnabled, { value = value.copy(notifyVibrateEnabled = it) },
                    value.notifySoundUri, { value = value.copy(notifySoundUri = it) },
                    value.notifyText, { value = value.copy(notifyText = it) },
                    value.notifyToastText, { value = value.copy(notifyToastText = it) },
                    value.announceEnabled, { value = value.copy(announceEnabled = it) },
                    value.announceText, { value = value.copy(announceText = it) }
                )
            }
            if (request.canDelete) {
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "操作") }
                item { SettingsCard { ActionRow("删除模板", "删除后相关聊天恢复默认规则", onDelete) } }
            }
        }
    }
}

@Composable
internal fun TransferNotificationSettingsCard(
    context: Context,
    system: Boolean,
    onSystem: (Boolean) -> Unit,
    toast: Boolean,
    onToast: (Boolean) -> Unit,
    sound: Boolean,
    onSound: (Boolean) -> Unit,
    soundMode: Int,
    onSoundMode: (Int) -> Unit,
    vibrate: Boolean,
    onVibrate: (Boolean) -> Unit,
    soundUri: String,
    onSoundUri: (String) -> Unit,
    noticeText: String,
    onNoticeText: (String) -> Unit,
    toastText: String,
    onToastText: (String) -> Unit,
    announce: Boolean,
    onAnnounce: (Boolean) -> Unit,
    announceText: String,
    onAnnounceText: (String) -> Unit
) {
    SettingsCard {
        SwitchRow(system, "通知栏提醒", "收款请求成功后显示") { onSystem(it) }
        if (system) {
            InsetDivider(); VariableInputRow("通知栏文案", "支持下方变量", noticeText, transferTemplateVariables, onValueChange = onNoticeText)
        }
        InsetDivider()
        SwitchRow(toast, "浮窗提醒", "收款请求成功后短暂提示") { onToast(it) }
        if (toast) {
            InsetDivider(); VariableInputRow("浮窗文案", "支持下方变量", toastText, transferTemplateVariables, onValueChange = onToastText)
        }
        InsetDivider()
        SwitchRow(sound, "通知铃声", "开启后播放通知铃声") { onSound(it) }
        if (sound) {
            InsetDivider()
            PopupOptionRow(
                "铃声模式",
                if (soundMode == AutoTransferSettings.NOTIFY_SOUND_MODE_CUSTOM) "从文件选择铃声" else "选择系统铃声",
                optionItems("选择系统铃声" to AutoTransferSettings.NOTIFY_SOUND_MODE_SYSTEM, "从文件选择铃声" to AutoTransferSettings.NOTIFY_SOUND_MODE_CUSTOM),
                soundMode,
                onSoundMode
            )
            InsetDivider()
            ActionRow("选择铃声", ringtoneDisplayName(context, soundUri, soundMode)) {
                val activity = context as? Activity
                if (activity == null) {
                    Toast.makeText(context, "当前页面无法打开铃声选择器", Toast.LENGTH_SHORT).show()
                } else {
                    val picked: (String) -> Unit = { uri ->
                        if (uri.isNotBlank()) {
                            onSoundUri(
                                if (soundMode == AutoTransferSettings.NOTIFY_SOUND_MODE_CUSTOM) {
                                    CustomNotificationRuntime.freezeRingtoneUri(context, uri)
                                } else uri
                            )
                        }
                    }
                    if (soundMode == AutoTransferSettings.NOTIFY_SOUND_MODE_CUSTOM) {
                        RingtonePickerBridge.launchFile(activity, picked)
                    } else {
                        RingtonePickerBridge.launchSystem(activity, soundUri, picked)
                    }
                }
            }
        }
        InsetDivider()
        SwitchRow(vibrate, "通知震动", "开启后触发通知震动") { onVibrate(it) }
        InsetDivider()
        SwitchRow(announce, "收款语音播报", "收款请求成功后用系统语音播报") { onAnnounce(it) }
        if (announce) {
            InsetDivider(); VariableInputRow("播报文案", "支持下方变量", announceText, transferTemplateVariables, onValueChange = onAnnounceText)
        }
    }
}

@Composable
internal fun FakeWalletBalanceMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, FakeWalletBalanceSettings.PREFS_NAME) }
    var balanceEnabled by remember {
        mutableStateOf(
            FakeWalletBalanceSettings.isAccountEnabled(
                sp,
                FakeWalletBalanceSettings.KEY_BALANCE_ENABLE
            )
        )
    }
    var lqtEnabled by remember {
        mutableStateOf(
            FakeWalletBalanceSettings.isAccountEnabled(
                sp,
                FakeWalletBalanceSettings.KEY_LQT_ENABLE
            )
        )
    }
    var businessEnabled by remember {
        mutableStateOf(
            FakeWalletBalanceSettings.isAccountEnabled(
                sp,
                FakeWalletBalanceSettings.KEY_BUSINESS_ENABLE
            )
        )
    }
    val storedBalanceAmount = remember {
        sp.getString(
            FakeWalletBalanceSettings.KEY_BALANCE_AMOUNT,
            FakeWalletBalanceSettings.DEFAULT_AMOUNT
        ) ?: FakeWalletBalanceSettings.DEFAULT_AMOUNT
    }
    val storedLqtAmount = remember {
        sp.getString(
            FakeWalletBalanceSettings.KEY_LQT_AMOUNT,
            FakeWalletBalanceSettings.DEFAULT_AMOUNT
        ) ?: FakeWalletBalanceSettings.DEFAULT_AMOUNT
    }
    val storedBusinessAmount = remember {
        sp.getString(FakeWalletBalanceSettings.KEY_BUSINESS_AMOUNT, null)
            ?: storedLqtAmount
    }
    var balanceAmount by remember {
        mutableStateOf(FakeWalletBalanceSettings.normalizeAmount(storedBalanceAmount))
    }
    var lqtAmount by remember {
        mutableStateOf(FakeWalletBalanceSettings.normalizeAmount(storedLqtAmount))
    }
    var businessAmount by remember {
        mutableStateOf(FakeWalletBalanceSettings.normalizeAmount(storedBusinessAmount))
    }
    var balanceMode by remember {
        mutableStateOf(
            FakeWalletBalanceSettings.amountMode(
                sp,
                FakeWalletBalanceSettings.KEY_BALANCE_MODE,
                storedBalanceAmount
            )
        )
    }
    var lqtMode by remember {
        mutableStateOf(
            FakeWalletBalanceSettings.amountMode(
                sp,
                FakeWalletBalanceSettings.KEY_LQT_MODE,
                storedLqtAmount
            )
        )
    }
    var businessMode by remember {
        val fallbackMode = if (!sp.contains(FakeWalletBalanceSettings.KEY_BUSINESS_AMOUNT)) {
            FakeWalletBalanceSettings.amountMode(
                sp,
                FakeWalletBalanceSettings.KEY_LQT_MODE,
                storedLqtAmount
            )
        } else {
            FakeWalletBalanceSettings.DEFAULT_MODE
        }
        mutableStateOf(
            FakeWalletBalanceSettings.amountMode(
                sp,
                FakeWalletBalanceSettings.KEY_BUSINESS_MODE,
                storedBusinessAmount,
                fallbackMode
            )
        )
    }
    val amountModeOptions = remember {
        listOf(
            PopupChoice("固定金额", FakeWalletBalanceSettings.MODE_FIXED),
            PopupChoice("增加金额", FakeWalletBalanceSettings.MODE_INCREASE),
            PopupChoice("减少金额", FakeWalletBalanceSettings.MODE_DECREASE)
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
                    val normalizedBalance = FakeWalletBalanceSettings.normalizeAmount(balanceAmount)
                    val normalizedLqt = FakeWalletBalanceSettings.normalizeAmount(lqtAmount)
                    val normalizedBusiness = FakeWalletBalanceSettings.normalizeAmount(businessAmount)
                    balanceAmount = normalizedBalance
                    lqtAmount = normalizedLqt
                    businessAmount = normalizedBusiness
                    sp.edit()
                        .putString(FakeWalletBalanceSettings.KEY_BALANCE_AMOUNT, normalizedBalance)
                        .putString(FakeWalletBalanceSettings.KEY_LQT_AMOUNT, normalizedLqt)
                        .putString(FakeWalletBalanceSettings.KEY_BUSINESS_AMOUNT, normalizedBusiness)
                        .putString(FakeWalletBalanceSettings.KEY_BALANCE_MODE, balanceMode)
                        .putString(FakeWalletBalanceSettings.KEY_LQT_MODE, lqtMode)
                        .putString(FakeWalletBalanceSettings.KEY_BUSINESS_MODE, businessMode)
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
            item { SmallTitle(text = "显示") }
            item {
                SettingsCard {
                    SwitchRow(
                        checked = balanceEnabled,
                        title = "余额",
                        summary = "固定显示或按真实金额动态增减"
                    ) { enabled ->
                        balanceEnabled = enabled
                        sp.edit().putBoolean(FakeWalletBalanceSettings.KEY_BALANCE_ENABLE, enabled).apply()
                    }
                    InsetDivider()
                    SwitchRow(
                        checked = lqtEnabled,
                        title = "零钱通",
                        summary = "固定显示或按真实金额动态增减"
                    ) { enabled ->
                        lqtEnabled = enabled
                        sp.edit().putBoolean(FakeWalletBalanceSettings.KEY_LQT_ENABLE, enabled).apply()
                    }
                    InsetDivider()
                    SwitchRow(
                        checked = businessEnabled,
                        title = "经营账户",
                        summary = "固定显示或按真实金额动态增减"
                    ) { enabled ->
                        businessEnabled = enabled
                        sp.edit().putBoolean(FakeWalletBalanceSettings.KEY_BUSINESS_ENABLE, enabled).apply()
                    }
                }
            }
            if (balanceEnabled || lqtEnabled || businessEnabled) {
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "金额") }
                item {
                    SettingsCard {
                        if (balanceEnabled) {
                            PopupChoiceRow(
                                title = "余额显示方式",
                                summary = "选择固定显示或按真实余额计算",
                                options = amountModeOptions,
                                currentValue = balanceMode,
                                onValueChanged = { balanceMode = it }
                            )
                            InsetDivider()
                            InputRow(
                                when (balanceMode) {
                                    FakeWalletBalanceSettings.MODE_INCREASE -> "余额增加金额"
                                    FakeWalletBalanceSettings.MODE_DECREASE -> "余额减少金额"
                                    else -> "余额金额"
                                },
                                if (balanceMode == FakeWalletBalanceSettings.MODE_FIXED) {
                                    "直接显示该金额"
                                } else {
                                    "基于微信真实余额计算"
                                },
                                balanceAmount
                            ) { value ->
                                balanceAmount = value.filter { it.isDigit() || it == '.' || it == ',' }
                            }
                        }
                        if (balanceEnabled && (lqtEnabled || businessEnabled)) {
                            InsetDivider()
                        }
                        if (lqtEnabled) {
                            PopupChoiceRow(
                                title = "零钱通显示方式",
                                summary = "选择固定显示或按真实金额计算",
                                options = amountModeOptions,
                                currentValue = lqtMode,
                                onValueChanged = { lqtMode = it }
                            )
                            InsetDivider()
                            InputRow(
                                when (lqtMode) {
                                    FakeWalletBalanceSettings.MODE_INCREASE -> "零钱通增加金额"
                                    FakeWalletBalanceSettings.MODE_DECREASE -> "零钱通减少金额"
                                    else -> "零钱通金额"
                                },
                                if (lqtMode == FakeWalletBalanceSettings.MODE_FIXED) {
                                    "直接显示该金额"
                                } else {
                                    "基于微信真实金额计算"
                                },
                                lqtAmount
                            ) { value ->
                                lqtAmount = value.filter { it.isDigit() || it == '.' || it == ',' }
                            }
                        }
                        if (lqtEnabled && businessEnabled) {
                            InsetDivider()
                        }
                        if (businessEnabled) {
                            PopupChoiceRow(
                                title = "经营账户显示方式",
                                summary = "选择固定显示或按真实金额计算",
                                options = amountModeOptions,
                                currentValue = businessMode,
                                onValueChanged = { businessMode = it }
                            )
                            InsetDivider()
                            InputRow(
                                when (businessMode) {
                                    FakeWalletBalanceSettings.MODE_INCREASE -> "经营账户增加金额"
                                    FakeWalletBalanceSettings.MODE_DECREASE -> "经营账户减少金额"
                                    else -> "经营账户金额"
                                },
                                if (businessMode == FakeWalletBalanceSettings.MODE_FIXED) {
                                    "直接显示该金额"
                                } else {
                                    "基于微信真实金额计算"
                                },
                                businessAmount
                            ) { value ->
                                businessAmount = value.filter { it.isDigit() || it == '.' || it == ',' }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun MemberTitleMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, MemberTitleSettings.PREFS_NAME) }
    var ownerTitle by remember { mutableStateOf(sp.getString(MemberTitleSettings.KEY_OWNER_TITLE, MemberTitleSettings.DEFAULT_OWNER_TITLE) ?: MemberTitleSettings.DEFAULT_OWNER_TITLE) }
    var adminTitle by remember { mutableStateOf(sp.getString(MemberTitleSettings.KEY_ADMIN_TITLE, MemberTitleSettings.DEFAULT_ADMIN_TITLE) ?: MemberTitleSettings.DEFAULT_ADMIN_TITLE) }
    var memberTitle by remember { mutableStateOf(sp.getString(MemberTitleSettings.KEY_MEMBER_TITLE, MemberTitleSettings.DEFAULT_MEMBER_TITLE) ?: MemberTitleSettings.DEFAULT_MEMBER_TITLE) }
    var ownerColor by remember { mutableStateOf(sp.getString(MemberTitleSettings.KEY_OWNER_COLOR, MemberTitleSettings.DEFAULT_OWNER_COLOR) ?: MemberTitleSettings.DEFAULT_OWNER_COLOR) }
    var adminColor by remember { mutableStateOf(sp.getString(MemberTitleSettings.KEY_ADMIN_COLOR, MemberTitleSettings.DEFAULT_ADMIN_COLOR) ?: MemberTitleSettings.DEFAULT_ADMIN_COLOR) }
    var memberColor by remember { mutableStateOf(sp.getString(MemberTitleSettings.KEY_MEMBER_COLOR, MemberTitleSettings.DEFAULT_MEMBER_COLOR) ?: MemberTitleSettings.DEFAULT_MEMBER_COLOR) }
    var customColor by remember { mutableStateOf(sp.getString(MemberTitleSettings.KEY_CUSTOM_COLOR, MemberTitleSettings.DEFAULT_CUSTOM_COLOR) ?: MemberTitleSettings.DEFAULT_CUSTOM_COLOR) }
    var ownerTextColor by remember { mutableStateOf(sp.getString(MemberTitleSettings.KEY_OWNER_TEXT_COLOR, MemberTitleSettings.DEFAULT_TEXT_COLOR) ?: MemberTitleSettings.DEFAULT_TEXT_COLOR) }
    var adminTextColor by remember { mutableStateOf(sp.getString(MemberTitleSettings.KEY_ADMIN_TEXT_COLOR, MemberTitleSettings.DEFAULT_TEXT_COLOR) ?: MemberTitleSettings.DEFAULT_TEXT_COLOR) }
    var memberTextColor by remember { mutableStateOf(sp.getString(MemberTitleSettings.KEY_MEMBER_TEXT_COLOR, MemberTitleSettings.DEFAULT_TEXT_COLOR) ?: MemberTitleSettings.DEFAULT_TEXT_COLOR) }
    var customTextColor by remember { mutableStateOf(sp.getString(MemberTitleSettings.KEY_CUSTOM_TEXT_COLOR, MemberTitleSettings.DEFAULT_TEXT_COLOR) ?: MemberTitleSettings.DEFAULT_TEXT_COLOR) }
    var showMember by remember { mutableStateOf(sp.getBoolean(MemberTitleSettings.KEY_SHOW_MEMBER, MemberTitleSettings.DEFAULT_SHOW_MEMBER)) }
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
                    sp.edit()
                        .putString(MemberTitleSettings.KEY_OWNER_TITLE, MemberTitleStore.cleanTitle(ownerTitle).ifEmpty { MemberTitleSettings.DEFAULT_OWNER_TITLE })
                        .putString(MemberTitleSettings.KEY_ADMIN_TITLE, MemberTitleStore.cleanTitle(adminTitle).ifEmpty { MemberTitleSettings.DEFAULT_ADMIN_TITLE })
                        .putString(MemberTitleSettings.KEY_MEMBER_TITLE, MemberTitleStore.cleanTitle(memberTitle).ifEmpty { MemberTitleSettings.DEFAULT_MEMBER_TITLE })
                        .putString(MemberTitleSettings.KEY_OWNER_COLOR, MemberTitleStore.cleanColorSpec(ownerColor).ifEmpty { MemberTitleSettings.DEFAULT_OWNER_COLOR })
                        .putString(MemberTitleSettings.KEY_ADMIN_COLOR, MemberTitleStore.cleanColorSpec(adminColor).ifEmpty { MemberTitleSettings.DEFAULT_ADMIN_COLOR })
                        .putString(MemberTitleSettings.KEY_MEMBER_COLOR, MemberTitleStore.cleanColorSpec(memberColor).ifEmpty { MemberTitleSettings.DEFAULT_MEMBER_COLOR })
                        .putString(MemberTitleSettings.KEY_CUSTOM_COLOR, MemberTitleStore.cleanColorSpec(customColor).ifEmpty { MemberTitleSettings.DEFAULT_CUSTOM_COLOR })
                        .putString(MemberTitleSettings.KEY_OWNER_TEXT_COLOR, MemberTitleStore.cleanColorSpec(ownerTextColor).ifEmpty { MemberTitleSettings.DEFAULT_TEXT_COLOR })
                        .putString(MemberTitleSettings.KEY_ADMIN_TEXT_COLOR, MemberTitleStore.cleanColorSpec(adminTextColor).ifEmpty { MemberTitleSettings.DEFAULT_TEXT_COLOR })
                        .putString(MemberTitleSettings.KEY_MEMBER_TEXT_COLOR, MemberTitleStore.cleanColorSpec(memberTextColor).ifEmpty { MemberTitleSettings.DEFAULT_TEXT_COLOR })
                        .putString(MemberTitleSettings.KEY_CUSTOM_TEXT_COLOR, MemberTitleStore.cleanColorSpec(customTextColor).ifEmpty { MemberTitleSettings.DEFAULT_TEXT_COLOR })
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
            item { SmallTitle(text = "基础") }
            item {
                SettingsCard {
                    SwitchRow(sp, MemberTitleSettings.KEY_ENABLE, "群员头衔", "在群聊昵称左侧显示身份头衔", MemberTitleSettings.DEFAULT_ENABLE)
                    InsetDivider()
                    SwitchRow(showMember, "显示普通群员", "关闭后只显示群主、管理员和自定义头衔") {
                        showMember = it
                        sp.edit().putBoolean(MemberTitleSettings.KEY_SHOW_MEMBER, it).apply()
                    }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "名称") }
            item {
                SettingsCard {
                    InputRow("群主名称", "最多 8 个字符", ownerTitle) { ownerTitle = it.take(8) }
                    InsetDivider()
                    InputRow("管理员名称", "最多 8 个字符", adminTitle) { adminTitle = it.take(8) }
                    if (showMember) {
                        InsetDivider()
                        InputRow("群员名称", "最多 8 个字符", memberTitle) { memberTitle = it.take(8) }
                    }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "颜色") }
            item {
                SettingsCard {
                    ColorPickerRow("群主颜色", "默认黄色，渐变用 #F59E0B,#FDE047", ownerColor, onReset = { ownerColor = MemberTitleSettings.DEFAULT_OWNER_COLOR }) { ownerColor = it.take(19) }
                    InsetDivider()
                    ColorPickerRow("群主文字颜色", "默认白色，支持渐变", ownerTextColor, onReset = { ownerTextColor = MemberTitleSettings.DEFAULT_TEXT_COLOR }) { ownerTextColor = it.take(19) }
                    InsetDivider()
                    ColorPickerRow("管理员颜色", "默认绿色，渐变用 #22C55E,#14B8A6", adminColor, onReset = { adminColor = MemberTitleSettings.DEFAULT_ADMIN_COLOR }) { adminColor = it.take(19) }
                    InsetDivider()
                    ColorPickerRow("管理员文字颜色", "默认白色，支持渐变", adminTextColor, onReset = { adminTextColor = MemberTitleSettings.DEFAULT_TEXT_COLOR }) { adminTextColor = it.take(19) }
                    if (showMember) {
                        InsetDivider()
                        ColorPickerRow("群员颜色", "默认灰色，渐变用 #64748B,#94A3B8", memberColor, onReset = { memberColor = MemberTitleSettings.DEFAULT_MEMBER_COLOR }) { memberColor = it.take(19) }
                        InsetDivider()
                        ColorPickerRow("群员文字颜色", "默认白色，支持渐变", memberTextColor, onReset = { memberTextColor = MemberTitleSettings.DEFAULT_TEXT_COLOR }) { memberTextColor = it.take(19) }
                    }
                    InsetDivider()
                    ColorPickerRow("自定义默认色", "点击聊天头衔可单独设置每个人的名称和颜色，支持渐变", customColor, onReset = { customColor = MemberTitleSettings.DEFAULT_CUSTOM_COLOR }) { customColor = it.take(19) }
                    InsetDivider()
                    ColorPickerRow("自定义文字默认色", "单人未设置文字颜色时使用，支持渐变", customTextColor, onReset = { customTextColor = MemberTitleSettings.DEFAULT_TEXT_COLOR }) { customTextColor = it.take(19) }
                }
            }
        }
    }
}

@Composable
internal fun GroupNicknameColorMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, GroupNicknameColorSettings.PREFS_NAME) }
    var enabled by remember {
        mutableStateOf(
            sp.getBoolean(GroupNicknameColorSettings.KEY_ENABLE, GroupNicknameColorSettings.DEFAULT_ENABLE)
        )
    }
    var color by remember {
        mutableStateOf(
            sp.getString(GroupNicknameColorSettings.KEY_COLOR, GroupNicknameColorSettings.DEFAULT_COLOR)
                ?: GroupNicknameColorSettings.DEFAULT_COLOR
        )
    }
    var weight by remember {
        mutableStateOf(
            sp.getInt(GroupNicknameColorSettings.KEY_WEIGHT, GroupNicknameColorSettings.DEFAULT_WEIGHT).toString()
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
                    sp.edit()
                        .putString(
                            GroupNicknameColorSettings.KEY_COLOR,
                            GroupNicknameColorStore.cleanColorSpec(color)
                        )
                        .putInt(
                            GroupNicknameColorSettings.KEY_WEIGHT,
                            GroupNicknameColorStore.cleanWeight(
                                weight.toIntOrNull() ?: GroupNicknameColorSettings.DEFAULT_WEIGHT
                            )
                        )
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
            item { SmallTitle(text = "群昵称") }
            item {
                SettingsCard {
                    SwitchRow(enabled, "群昵称自定义颜色", "修改群聊成员昵称的颜色和粗细") {
                        enabled = it
                        sp.edit().putBoolean(GroupNicknameColorSettings.KEY_ENABLE, it).apply()
                    }
                    if (enabled) {
                        InsetDivider()
                        ColorPickerRow(
                            "昵称颜色",
                            "留空跟随微信，渐变用 #F59E0B,#22C55E",
                            color,
                            onReset = { color = GroupNicknameColorSettings.DEFAULT_COLOR }
                        ) { color = it.take(19) }
                        InsetDivider()
                        WeightInputRow("昵称粗细", weight) { weight = it }
                    }
                }
            }
        }
    }
}

@Composable
internal fun RealNameTailMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, RealNameTailSettings.PREFS_NAME) }
    var prefix by remember { mutableStateOf(sp.getString(RealNameTailSettings.KEY_GLOBAL_PREFIX, "") ?: "") }
    var maleText by remember { mutableStateOf(sp.getString(RealNameTailSettings.KEY_GENDER_MALE_TEXT, RealNameTailSettings.DEFAULT_GENDER_MALE_TEXT) ?: RealNameTailSettings.DEFAULT_GENDER_MALE_TEXT) }
    var femaleText by remember { mutableStateOf(sp.getString(RealNameTailSettings.KEY_GENDER_FEMALE_TEXT, RealNameTailSettings.DEFAULT_GENDER_FEMALE_TEXT) ?: RealNameTailSettings.DEFAULT_GENDER_FEMALE_TEXT) }
    var unknownGenderText by remember { mutableStateOf(sp.getString(RealNameTailSettings.KEY_GENDER_UNKNOWN_TEXT, RealNameTailSettings.DEFAULT_GENDER_UNKNOWN_TEXT) ?: RealNameTailSettings.DEFAULT_GENDER_UNKNOWN_TEXT) }
    var tailColor by remember { mutableStateOf(sp.getString(RealNameTailSettings.KEY_TAIL_COLOR, RealNameTailSettings.DEFAULT_TAIL_COLOR) ?: RealNameTailSettings.DEFAULT_TAIL_COLOR) }
    var bracketColor by remember { mutableStateOf(sp.getString(RealNameTailSettings.KEY_BRACKET_COLOR, RealNameTailSettings.DEFAULT_BRACKET_COLOR) ?: RealNameTailSettings.DEFAULT_BRACKET_COLOR) }
    var genderColor by remember { mutableStateOf(sp.getString(RealNameTailSettings.KEY_GENDER_COLOR, RealNameTailSettings.DEFAULT_GENDER_COLOR) ?: RealNameTailSettings.DEFAULT_GENDER_COLOR) }
    var regionColor by remember { mutableStateOf(sp.getString(RealNameTailSettings.KEY_REGION_COLOR, RealNameTailSettings.DEFAULT_REGION_COLOR) ?: RealNameTailSettings.DEFAULT_REGION_COLOR) }
    var tailWeight by remember { mutableStateOf(sp.getInt(RealNameTailSettings.KEY_TAIL_WEIGHT, RealNameTailSettings.DEFAULT_TEXT_WEIGHT).toString()) }
    var bracketWeight by remember { mutableStateOf(sp.getInt(RealNameTailSettings.KEY_BRACKET_WEIGHT, RealNameTailSettings.DEFAULT_TEXT_WEIGHT).toString()) }
    var genderWeight by remember { mutableStateOf(sp.getInt(RealNameTailSettings.KEY_GENDER_WEIGHT, RealNameTailSettings.DEFAULT_TEXT_WEIGHT).toString()) }
    var regionWeight by remember { mutableStateOf(sp.getInt(RealNameTailSettings.KEY_REGION_WEIGHT, RealNameTailSettings.DEFAULT_TEXT_WEIGHT).toString()) }
    var prefixEnabled by remember { mutableStateOf(sp.getBoolean(RealNameTailSettings.KEY_GLOBAL_PREFIX_ENABLE, false)) }
    var showGender by remember { mutableStateOf(sp.getBoolean(RealNameTailSettings.KEY_SHOW_GENDER, RealNameTailSettings.DEFAULT_SHOW_GENDER)) }
    var showRegion by remember { mutableStateOf(sp.getBoolean(RealNameTailSettings.KEY_SHOW_REGION, RealNameTailSettings.DEFAULT_SHOW_REGION)) }
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
                    sp.edit()
                        .putString(RealNameTailSettings.KEY_GLOBAL_PREFIX, prefix.trim().take(8))
                        .putString(RealNameTailSettings.KEY_GENDER_MALE_TEXT, maleText.trim().take(12))
                        .putString(RealNameTailSettings.KEY_GENDER_FEMALE_TEXT, femaleText.trim().take(12))
                        .putString(RealNameTailSettings.KEY_GENDER_UNKNOWN_TEXT, unknownGenderText.trim().take(12))
                        .putString(RealNameTailSettings.KEY_TAIL_COLOR, RealNameTailStore.cleanColorSpec(tailColor))
                        .putString(RealNameTailSettings.KEY_BRACKET_COLOR, RealNameTailStore.cleanColorSpec(bracketColor))
                        .putString(RealNameTailSettings.KEY_GENDER_COLOR, RealNameTailStore.cleanColorSpec(genderColor))
                        .putString(RealNameTailSettings.KEY_REGION_COLOR, RealNameTailStore.cleanColorSpec(regionColor))
                        .putInt(RealNameTailSettings.KEY_TAIL_WEIGHT, RealNameTailStore.cleanWeight(tailWeight.toIntOrNull() ?: RealNameTailSettings.DEFAULT_TEXT_WEIGHT))
                        .putInt(RealNameTailSettings.KEY_BRACKET_WEIGHT, RealNameTailStore.cleanWeight(bracketWeight.toIntOrNull() ?: RealNameTailSettings.DEFAULT_TEXT_WEIGHT))
                        .putInt(RealNameTailSettings.KEY_GENDER_WEIGHT, RealNameTailStore.cleanWeight(genderWeight.toIntOrNull() ?: RealNameTailSettings.DEFAULT_TEXT_WEIGHT))
                        .putInt(RealNameTailSettings.KEY_REGION_WEIGHT, RealNameTailStore.cleanWeight(regionWeight.toIntOrNull() ?: RealNameTailSettings.DEFAULT_TEXT_WEIGHT))
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
            item { SmallTitle(text = "基础") }
            item {
                SettingsCard {
                    SwitchRow(sp, RealNameTailSettings.KEY_ENABLE, "实名尾字", "在群聊昵称后显示已查询到的实名尾字", RealNameTailSettings.DEFAULT_ENABLE)
                    InsetDivider()
                    SwitchRow(sp, RealNameTailSettings.KEY_MESSAGE_QUERY, "消息触发查询", "收到群成员消息后自动补查", RealNameTailSettings.DEFAULT_MESSAGE_QUERY)
                    InsetDivider()
                    SwitchRow(sp, RealNameTailSettings.KEY_VISIBLE_QUERY, "可见成员查询", "聊天页出现未缓存成员时自动补查", RealNameTailSettings.DEFAULT_VISIBLE_QUERY)
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "显示") }
            item {
                SettingsCard {
                    SwitchRow(prefixEnabled, "实名前缀", "用统一前缀替换实名尾字前半部分") {
                        prefixEnabled = it
                        sp.edit().putBoolean(RealNameTailSettings.KEY_GLOBAL_PREFIX_ENABLE, it).apply()
                    }
                    if (prefixEnabled) {
                        InsetDivider()
                        InputRow("前缀内容", "最多 8 个字符，例如 *", prefix) { prefix = it.take(8) }
                    }
                    InsetDivider()
                    SwitchRow(showGender, "显示性别", "在实名尾字后追加联系人性别") {
                        showGender = it
                        sp.edit().putBoolean(RealNameTailSettings.KEY_SHOW_GENDER, it).apply()
                    }
                    if (showGender) {
                        InsetDivider()
                        InputRow("男性文案", "最多 12 个字符，默认 男", maleText) { maleText = it.take(12) }
                        InsetDivider()
                        InputRow("女性文案", "最多 12 个字符，默认 女", femaleText) { femaleText = it.take(12) }
                        InsetDivider()
                        InputRow("未知文案", "最多 12 个字符，留空则不显示", unknownGenderText) { unknownGenderText = it.take(12) }
                    }
                    InsetDivider()
                    SwitchRow(showRegion, "显示地区", "在实名尾字后追加联系人地区") {
                        showRegion = it
                        sp.edit().putBoolean(RealNameTailSettings.KEY_SHOW_REGION, it).apply()
                    }
                    InsetDivider()
                    ColorPickerRow("尾字颜色", "留空跟随昵称，渐变用 #F59E0B,#22C55E", tailColor, onReset = { tailColor = RealNameTailSettings.DEFAULT_TAIL_COLOR }) { tailColor = it.take(19) }
                    InsetDivider()
                    WeightInputRow("尾字粗细", tailWeight) { tailWeight = it }
                    InsetDivider()
                    ColorPickerRow("括号颜色", "留空跟随昵称，渐变用 #F59E0B,#22C55E", bracketColor, onReset = { bracketColor = RealNameTailSettings.DEFAULT_BRACKET_COLOR }) { bracketColor = it.take(19) }
                    InsetDivider()
                    WeightInputRow("括号粗细", bracketWeight) { bracketWeight = it }
                    if (showGender) {
                        InsetDivider()
                        ColorPickerRow("性别颜色", "留空跟随昵称，支持渐变", genderColor, onReset = { genderColor = RealNameTailSettings.DEFAULT_GENDER_COLOR }) { genderColor = it.take(19) }
                        InsetDivider()
                        WeightInputRow("性别粗细", genderWeight) { genderWeight = it }
                    }
                    if (showRegion) {
                        InsetDivider()
                        ColorPickerRow("地区颜色", "留空跟随昵称，支持渐变", regionColor, onReset = { regionColor = RealNameTailSettings.DEFAULT_REGION_COLOR }) { regionColor = it.take(19) }
                        InsetDivider()
                        WeightInputRow("地区粗细", regionWeight) { regionWeight = it }
                    }
                }
            }
        }
    }
}

@Composable
internal fun MessageBlockContactPickerPage(
    context: Context,
    request: MessageBlockContactPickerRequest,
    onBack: () -> Unit,
    onConfirm: (List<MessageBlockContactOption>) -> Unit
) {
    var loading by remember(request) { mutableStateOf(true) }
    var error by remember(request) { mutableStateOf("") }
    var data by remember(request) { mutableStateOf(MessageBlockContactData()) }
    var query by remember(request) { mutableStateOf("") }
    val initialSelectedIds = remember(request) { parseIds(request.existingValue) }
    var selectedIds by remember(request) { mutableStateOf(initialSelectedIds) }
    var contactFilter by remember(request) { mutableStateOf(MessageBlockContactFilter.ALL) }
    var selectedLabel by remember(request) { mutableStateOf<MessageBlockLabelOption?>(null) }
    var selectedConversationGroupId by remember(request) { mutableStateOf("") }
    val scrollBehavior = MiuixScrollBehavior()

    LaunchedEffect(request) {
        loadMessageBlockContacts(context) { result, throwable ->
            loading = false
            if (throwable != null) {
                error = throwable.message ?: "读取联系人失败"
            } else {
                data = result
            }
        }
    }
    LaunchedEffect(contactFilter) {
        if (contactFilter != MessageBlockContactFilter.LABELS) {
            selectedLabel = null
        }
        if (!request.allowOfficialAccounts && contactFilter == MessageBlockContactFilter.OFFICIALS) {
            contactFilter = MessageBlockContactFilter.ALL
        }
    }

    val currentLabel = selectedLabel
    val pageTitle = currentLabel?.let { "标签：${it.name}" } ?: request.title
    val availableContacts = data.contacts.filter {
        request.allowOfficialAccounts || it.kind != MessageBlockContactKind.OFFICIAL
    }
    val conversationGroupFilters = remember(availableContacts) {
        ConversationGroupPickerSupport.filters(context, availableContacts.map { it.contact.id })
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
    val scopedContacts = remember(availableContacts, selectedConversationIds) {
        selectedConversationIds?.let { ids ->
            availableContacts.filter { it.contact.id in ids }
        } ?: availableContacts
    }
    val availableContactIds = scopedContacts.map { it.contact.id }.toSet()
    val bottomLower = query.trim().lowercase(Locale.US)
    val bottomVisibleContacts = scopedContacts.filter { row ->
        val contact = row.contact
        val typeMatched = when (contactFilter) {
            MessageBlockContactFilter.FRIENDS -> row.kind == MessageBlockContactKind.FRIEND
            MessageBlockContactFilter.GROUPS -> row.kind == MessageBlockContactKind.GROUP
            MessageBlockContactFilter.OFFICIALS -> row.kind == MessageBlockContactKind.OFFICIAL
            MessageBlockContactFilter.LABELS -> currentLabel?.contactIds?.contains(contact.id) == true
            MessageBlockContactFilter.ALL -> true
        }
        typeMatched && (
            contact.matchesSearch(bottomLower) ||
                row.labelNames.any { it.lowercase(Locale.US).contains(bottomLower) }
        )
    }
    val bottomShowingLabelList = contactFilter == MessageBlockContactFilter.LABELS && currentLabel == null
    val bottomVisibleContactIds = bottomVisibleContacts.map { it.contact.id }.toSet()
    val bottomVisibleAllSelected = bottomVisibleContactIds.isNotEmpty() && bottomVisibleContactIds.all { selectedIds.contains(it) }
    PageScaffold(
        title = pageTitle,
        largeTitle = pageTitle,
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "保存选择",
                onPrimaryClick = {
                    val picked = availableContacts.filter { selectedIds.contains(it.contact.id) }
                    onConfirm(picked)
                },
                secondaryText = if (currentLabel == null) "返回" else "返回标签",
                onSecondaryClick = {
                    if (currentLabel == null) {
                        onBack()
                    } else {
                        selectedLabel = null
                        query = ""
                    }
                },
                middleText = if (!bottomShowingLabelList && bottomVisibleContactIds.isNotEmpty()) {
                    if (bottomVisibleAllSelected) "取消全选" else "全选"
                } else {
                    null
                },
                onMiddleClick = if (!bottomShowingLabelList && bottomVisibleContactIds.isNotEmpty()) {
                    {
                        selectedIds = if (bottomVisibleAllSelected) {
                            selectedIds - bottomVisibleContactIds
                        } else {
                            selectedIds + bottomVisibleContactIds
                        }
                    }
                } else {
                    null
                }
            )
        }
    ) { padding ->
        val listRoute = if (contactFilter == MessageBlockContactFilter.LABELS && currentLabel == null) {
            MessageBlockContactPickerRoute.Labels
        } else {
            MessageBlockContactPickerRoute.Contacts(contactFilter, currentLabel)
        }
        SettingsRouteTransition(
            targetState = listRoute,
            label = "MessageBlockContactPickerTransition",
            depthOf = { it.depth() },
            horizontalOnSameDepth = false
        ) { route ->
            val lower = query.trim().lowercase(Locale.US)
            val showingLabelList = route is MessageBlockContactPickerRoute.Labels
            val routeFilter = (route as? MessageBlockContactPickerRoute.Contacts)?.filter
                ?: MessageBlockContactFilter.LABELS
            val routeLabel = (route as? MessageBlockContactPickerRoute.Contacts)?.label
            val visibleLabels = data.labels.filter { label ->
                label.contactIds.any { availableContactIds.contains(it) } && (
                    lower.isEmpty() ||
                        label.name.lowercase(Locale.US).contains(lower) ||
                        label.id.lowercase(Locale.US).contains(lower)
                    )
            }.selectedFirst { label -> label.contactIds.any { initialSelectedIds.contains(it) } }
            val visibleContacts = scopedContacts.filter { row ->
                val contact = row.contact
                val typeMatched = when (routeFilter) {
                    MessageBlockContactFilter.FRIENDS -> row.kind == MessageBlockContactKind.FRIEND
                    MessageBlockContactFilter.GROUPS -> row.kind == MessageBlockContactKind.GROUP
                    MessageBlockContactFilter.OFFICIALS -> row.kind == MessageBlockContactKind.OFFICIAL
                    MessageBlockContactFilter.LABELS -> routeLabel?.contactIds?.contains(contact.id) == true
                    MessageBlockContactFilter.ALL -> true
                }
                typeMatched && (
                    contact.matchesSearch(lower) ||
                        row.labelNames.any { it.lowercase(Locale.US).contains(lower) }
                )
            }.selectedFirst { initialSelectedIds.contains(it.contact.id) }
            val visibleContactIds = visibleContacts.map { it.contact.id }.toSet()
            val sectionTitle = when {
                showingLabelList -> "标签"
                routeFilter == MessageBlockContactFilter.LABELS -> "标签成员"
                else -> routeFilter.title
            }
            val sectionCount = if (showingLabelList) visibleLabels.size else visibleContactIds.size
            val routeListState = rememberLazyListState()
            LazyColumn(
                modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
                state = routeListState,
                contentPadding = PaddingValues(
                    top = padding.calculateTopPadding() + 8.dp,
                    bottom = padding.calculateBottomPadding() + 84.dp
                )
            ) {
                item {
                    SettingsCard {
                        MessageBlockContactFilterRow(
                            selected = contactFilter,
                            enableOfficials = request.allowOfficialAccounts,
                            onSelected = {
                                contactFilter = it
                                if (it == MessageBlockContactFilter.LABELS) {
                                    selectedLabel = null
                                }
                                query = ""
                            }
                        )
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
                        InputRow(
                            title = "搜索",
                            summary = if (showingLabelList) "标签名称" else "昵称 / 群聊备注 / wxid",
                            value = query,
                            onValueChange = { query = it }
                        )
                    }
                }
                item {
                    PickerSectionHeader(
                        text = if (sectionCount > 0) "$sectionTitle · $sectionCount 项" else sectionTitle
                    )
                }
                when {
                    loading -> item { SettingsCard { EmptyText("正在载入列表...") } }
                    error.isNotEmpty() -> item { SettingsCard { EmptyText(error) } }
                    showingLabelList -> {
                        if (visibleLabels.isEmpty()) {
                            item { SettingsCard { EmptyText("没有匹配标签") } }
                        } else {
                            visibleLabels.forEach { label ->
                                item {
                                    MessageBlockLabelListCard(
                                        label = label,
                                        onClick = {
                                            selectedLabel = label
                                            query = ""
                                        }
                                    )
                                }
                            }
                        }
                    }
                    visibleContacts.isEmpty() -> item { SettingsCard { EmptyText("没有匹配结果") } }
                    else -> visibleContacts.forEach { row ->
                        item {
                            val option = row.contact
                            MessageBlockContactListCard(
                                row = row,
                                selected = selectedIds.contains(option.id),
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
}

@Composable
internal fun ContactPickerPage(
    context: Context,
    request: ContactPickerRequest,
    onBack: () -> Unit,
    onConfirm: (List<ContactOption>) -> Unit
) {
    var loading by remember(request) { mutableStateOf(true) }
    var error by remember(request) { mutableStateOf("") }
    var contacts by remember(request) { mutableStateOf(emptyList<ContactOption>()) }
    var query by remember(request) { mutableStateOf("") }
    val initialSelectedIds = remember(request) { parseIds(request.existingValue) }
    var selectedIds by remember(request) { mutableStateOf(initialSelectedIds) }
    val scopeSelectionEnabled = request.enableScopeSelection &&
        request.multiSelect &&
        request.mode == ContactPickerMode.FRIENDS
    var scopeTab by remember(request) { mutableStateOf(ContactPickerScopeTab.FRIENDS) }
    var selectedConversationGroupId by remember(request) { mutableStateOf("") }
    var contactFilter by remember(request) {
        mutableStateOf(
            if (request.mode == ContactPickerMode.FRIENDS && request.enableLabels) {
                ContactPickerFilter.FRIENDS
            } else {
                ContactPickerFilter.ALL
            }
        )
    }
    var selectedLabel by remember(request) { mutableStateOf("") }
    val groupLabels = remember(request) {
        if (request.multiSelect && request.enableGroupLabels && request.mode.supportsGroups()) {
            GroupChatLabelStore.load(context)
        } else {
            emptyList()
        }
    }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()

    LaunchedEffect(request) {
        loadContacts(context, request.mode, request.enableLabels) { result, throwable ->
            loading = false
            if (throwable != null) {
                error = throwable.message ?: "读取联系人失败"
            } else {
                contacts = result
            }
        }
    }
    val conversationGroupFilters = remember(contacts) {
        ConversationGroupPickerSupport.filters(context, contacts.map { it.id })
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
    val scopedContacts = remember(contacts, selectedConversationIds) {
        selectedConversationIds?.let { ids -> contacts.filter { it.id in ids } } ?: contacts
    }
    val labels = remember(scopedContacts) {
        scopedContacts.flatMap { it.labels }
            .distinct()
            .sortedWith(compareBy { it.lowercase(Locale.US) })
    }
    val contactLabelScopes = remember(contacts) {
        contacts.asSequence()
            .filter { !it.group && !it.official }
            .flatMap { contact -> contact.labels.asSequence().map { it to contact.id } }
            .groupBy(keySelector = { it.first }, valueTransform = { it.second })
            .map { (name, ids) -> ContactPickerScope(name, ids.toSet()) }
            .sortedBy { it.name.lowercase(Locale.US) }
    }
    val availableGroupIds = remember(scopedContacts) {
        scopedContacts.filter { it.group }.map { it.id }.toSet()
    }
    val selectableGroupLabels = remember(groupLabels, availableGroupIds) {
        groupLabels.mapNotNull { label ->
            val ids = label.groupIds.intersect(availableGroupIds)
            label.copy(groupIds = ids).takeIf { ids.isNotEmpty() }
        }
    }
    val showGroupLabels = request.mode == ContactPickerMode.GROUPS ||
        contactFilter == ContactPickerFilter.GROUPS
    LaunchedEffect(contactFilter, labels) {
        if (contactFilter == ContactPickerFilter.LABELS) {
            if (selectedLabel.isBlank() || !labels.contains(selectedLabel)) {
                selectedLabel = labels.firstOrNull().orEmpty()
            }
        } else if (selectedLabel.isNotBlank()) {
            selectedLabel = ""
        }
    }
    val lower = query.trim().lowercase(Locale.US)
    val visibleContacts = remember(
        scopedContacts,
        contactFilter,
        selectedLabel,
        request.enableLabels,
        lower,
        initialSelectedIds
    ) {
        scopedContacts.asSequence().filter {
            when (contactFilter) {
                ContactPickerFilter.FRIENDS -> !it.group && !it.official
                ContactPickerFilter.GROUPS -> it.group
                ContactPickerFilter.OFFICIALS -> it.official
                ContactPickerFilter.LABELS -> request.enableLabels &&
                    selectedLabel.isNotBlank() &&
                    !it.group &&
                    !it.official &&
                    it.labels.contains(selectedLabel)
                ContactPickerFilter.ALL -> true
            }
        }.filter { it.matchesSearch(lower) }
            .toList()
            .selectedFirst { initialSelectedIds.contains(it.id) }
    }
    val visibleLabelScopes = remember(contactLabelScopes, lower, initialSelectedIds) {
        contactLabelScopes.filter {
            lower.isEmpty() || it.name.lowercase(Locale.US).contains(lower)
        }.selectedFirst { scope -> scope.memberIds.any(initialSelectedIds::contains) }
    }
    val visibleConversationGroupScopes = remember(conversationGroupFilters, lower, initialSelectedIds) {
        conversationGroupFilters.filter {
            lower.isEmpty() || it.name.lowercase(Locale.US).contains(lower)
        }.selectedFirst { scope -> scope.conversationIds.any(initialSelectedIds::contains) }
    }
    val visibleContactIds = remember(visibleContacts) {
        visibleContacts.mapTo(linkedSetOf()) { it.id }
    }
    val bottomVisibleIds = remember(
        scopeSelectionEnabled,
        scopeTab,
        visibleContactIds,
        visibleLabelScopes,
        visibleConversationGroupScopes
    ) {
        when {
            !scopeSelectionEnabled || scopeTab == ContactPickerScopeTab.FRIENDS -> visibleContactIds
            scopeTab == ContactPickerScopeTab.LABELS -> {
                visibleLabelScopes.flatMapTo(linkedSetOf()) { it.memberIds }
            }
            else -> {
                visibleConversationGroupScopes.flatMapTo(linkedSetOf()) { it.conversationIds }
            }
        }
    }
    val bottomVisibleAllSelected = bottomVisibleIds.isNotEmpty() && bottomVisibleIds.all { selectedIds.contains(it) }

    PageScaffold(
        title = request.title,
        largeTitle = request.title,
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = if (request.multiSelect) "保存选择" else request.singleConfirmText,
                onPrimaryClick = {
                    val picked = contacts.filter { selectedIds.contains(it.id) }
                    if (picked.isEmpty() && !request.multiSelect) {
                        Toast.makeText(context, "请选择联系人", Toast.LENGTH_SHORT).show()
                    } else {
                        onConfirm(picked)
                    }
                },
                secondaryText = "返回",
                onSecondaryClick = onBack,
                middleText = if (request.multiSelect && bottomVisibleIds.isNotEmpty()) {
                    if (bottomVisibleAllSelected) "取消全选" else "全选"
                } else {
                    null
                },
                onMiddleClick = if (request.multiSelect && bottomVisibleIds.isNotEmpty()) {
                    {
                        selectedIds = if (bottomVisibleAllSelected) {
                            selectedIds - bottomVisibleIds
                        } else {
                            selectedIds + bottomVisibleIds
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
            if (scopeSelectionEnabled) {
                item {
                    SettingsCard {
                        ContactPickerScopeFilterRow(
                            selected = scopeTab,
                            onSelected = {
                                scopeTab = it
                                contactFilter = ContactPickerFilter.FRIENDS
                                query = ""
                            }
                        )
                    }
                }
            } else if (request.mode == ContactPickerMode.FRIENDS && request.enableLabels) {
                item {
                    SettingsCard {
                        FriendContactFilterRow(
                            selected = contactFilter,
                            onSelected = {
                                contactFilter = it
                                query = ""
                            }
                        )
                    }
                }
            } else if (request.mode == ContactPickerMode.BOTH || request.mode == ContactPickerMode.ALL_CHATS) {
                item {
                    SettingsCard {
                        ContactFilterRow(
                            selected = contactFilter,
                            enableLabels = request.enableLabels,
                            enableOfficials = request.mode == ContactPickerMode.ALL_CHATS,
                            onSelected = {
                                contactFilter = it
                                query = ""
                            }
                        )
                    }
                }
            }
            if (!scopeSelectionEnabled && request.enableLabels && contactFilter == ContactPickerFilter.LABELS) {
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
            if (!scopeSelectionEnabled && conversationGroupChoices.size > 1) {
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
            if (showGroupLabels && selectableGroupLabels.isNotEmpty()) {
                item { PickerSectionHeader(text = "群聊标签") }
                item {
                    SettingsCard {
                        selectableGroupLabels.forEachIndexed { index, label ->
                            val selectedCount = label.groupIds.count { selectedIds.contains(it) }
                            GroupChatLabelPickerRow(
                                label = label,
                                selectedCount = selectedCount,
                                onClick = {
                                    selectedIds = if (selectedCount == label.groupIds.size) {
                                        selectedIds - label.groupIds
                                    } else {
                                        selectedIds + label.groupIds
                                    }
                                }
                            )
                            if (index < selectableGroupLabels.lastIndex) InsetDivider()
                        }
                    }
                }
            }
            item {
                SettingsCard {
                    InputRow(
                        title = "搜索",
                        summary = when {
                            scopeSelectionEnabled && scopeTab == ContactPickerScopeTab.LABELS -> "好友标签名称"
                            scopeSelectionEnabled && scopeTab == ContactPickerScopeTab.CONVERSATION_GROUPS -> "聊天分组名称"
                            else -> "昵称 / 群聊备注 / wxid"
                        },
                        value = query,
                        onValueChange = { query = it }
                    )
                }
            }
            item {
                PickerSectionHeader(
                    text = when {
                        scopeSelectionEnabled && scopeTab == ContactPickerScopeTab.LABELS -> {
                            if (visibleLabelScopes.isEmpty()) "好友标签" else "好友标签 · ${visibleLabelScopes.size} 项"
                        }
                        scopeSelectionEnabled && scopeTab == ContactPickerScopeTab.CONVERSATION_GROUPS -> {
                            if (visibleConversationGroupScopes.isEmpty()) "聊天分组" else "聊天分组 · ${visibleConversationGroupScopes.size} 项"
                        }
                        visibleContacts.isNotEmpty() -> "${contactFilter.title} · ${visibleContacts.size} 项"
                        else -> contactFilter.title
                    }
                )
            }
            when {
                loading -> item { SettingsCard { EmptyText("正在载入列表...") } }
                error.isNotEmpty() -> item { SettingsCard { EmptyText(error) } }
                scopeSelectionEnabled && scopeTab == ContactPickerScopeTab.LABELS -> {
                    if (visibleLabelScopes.isEmpty()) {
                        item { SettingsCard { EmptyText("没有匹配的好友标签") } }
                    } else {
                        item {
                            SettingsCard {
                                visibleLabelScopes.forEachIndexed { index, scope ->
                                    val selectedCount = scope.memberIds.count(selectedIds::contains)
                                    ContactPickerScopeRow(
                                        title = scope.name,
                                        memberCount = scope.memberIds.size,
                                        selectedCount = selectedCount,
                                        memberUnit = "位好友",
                                        onClick = {
                                            selectedIds = if (selectedCount == scope.memberIds.size) {
                                                selectedIds - scope.memberIds
                                            } else {
                                                selectedIds + scope.memberIds
                                            }
                                        }
                                    )
                                    if (index < visibleLabelScopes.lastIndex) InsetDivider()
                                }
                            }
                        }
                    }
                }
                scopeSelectionEnabled && scopeTab == ContactPickerScopeTab.CONVERSATION_GROUPS -> {
                    if (visibleConversationGroupScopes.isEmpty()) {
                        item { SettingsCard { EmptyText("没有匹配的聊天分组") } }
                    } else {
                        item {
                            SettingsCard {
                                visibleConversationGroupScopes.forEachIndexed { index, scope ->
                                    val selectedCount = scope.conversationIds.count(selectedIds::contains)
                                    ContactPickerScopeRow(
                                        title = scope.name,
                                        memberCount = scope.conversationIds.size,
                                        selectedCount = selectedCount,
                                        memberUnit = "位好友",
                                        onClick = {
                                            selectedIds = if (selectedCount == scope.conversationIds.size) {
                                                selectedIds - scope.conversationIds
                                            } else {
                                                selectedIds + scope.conversationIds
                                            }
                                        }
                                    )
                                    if (index < visibleConversationGroupScopes.lastIndex) InsetDivider()
                                }
                            }
                        }
                    }
                }
                visibleContacts.isEmpty() -> item { SettingsCard { EmptyText("没有匹配结果") } }
                else -> items(items = visibleContacts, key = { it.id }) { option ->
                    ContactListCard(
                        option = option,
                        selected = selectedIds.contains(option.id),
                        multiSelect = request.multiSelect,
                        onClick = {
                            selectedIds = if (request.multiSelect) {
                                if (selectedIds.contains(option.id)) {
                                    selectedIds - option.id
                                } else {
                                    selectedIds + option.id
                                }
                            } else {
                                setOf(option.id)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
internal fun FavoritePickerPage(
    request: FavoritePickerRequest,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val favoriteApi = WeChatApis.media()?.favorites()
    val initialCache = remember(request) { favoriteApi?.cachedList() }
    var loading by remember(request) { mutableStateOf(initialCache?.items.isNullOrEmpty()) }
    var error by remember(request) { mutableStateOf("") }
    var favorites by remember(request) { mutableStateOf(initialCache?.items.orEmpty()) }
    var backgroundLoading by remember(request) { mutableStateOf(false) }
    var query by remember(request) { mutableStateOf("") }
    var filter by remember(request) { mutableStateOf(FavoritePickerFilter.ALL) }
    var preview by remember(request) { mutableStateOf<FavoriteMediaPreview?>(null) }
    val initialSelectedIds = remember(request) { parseFavoriteIds(request.existingValue) }
    var selectedIds by remember(request) { mutableStateOf(initialSelectedIds) }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()

    LaunchedEffect(request) {
        loading = favorites.isEmpty()
        error = ""
        val result = withContext(Dispatchers.IO) {
            runCatching {
                val api = favoriteApi
                    ?: throw IllegalStateException("收藏 API 未就绪")
                if (!api.canList()) {
                    throw IllegalStateException("收藏列表不可用")
                }
                var page = h.Hchat.hooks.api.media.WeChatFavoriteApi.FavoritePage(emptyList(), false)
                for (attempt in 0..2) {
                    page = api.openCachedList()
                    if (page.items.isNotEmpty() || attempt == 2) break
                    delay(350L)
                }
                page
            }
        }
        loading = false
        result.onSuccess {
            favorites = it.items
            if (it.hasMore) {
                backgroundLoading = true
                runCatching {
                    while (true) {
                        val page = withContext(Dispatchers.IO) {
                            favoriteApi?.loadNextPage()
                                ?: throw IllegalStateException("收藏 API 未就绪")
                        }
                        favorites = page.items
                        if (!page.hasMore) break
                        delay(FAVORITE_BACKGROUND_BATCH_DELAY_MS)
                    }
                }.onFailure {
                    error = it.message ?: "后台读取收藏失败"
                }
                backgroundLoading = false
            }
        }
            .onFailure {
                error = it.message ?: "读取收藏失败"
                if (favorites.isEmpty()) favorites = emptyList()
                backgroundLoading = false
            }
    }

    val lower = query.trim().lowercase(Locale.US)
    val visible = favorites.filter { item ->
        filter.matches(item.type) && (
            lower.isEmpty() ||
                item.displayTitle().lowercase(Locale.US).contains(lower) ||
                item.displaySummary().lowercase(Locale.US).contains(lower) ||
                item.tags.any { it.lowercase(Locale.US).contains(lower) }
            )
    }.selectedFirst { initialSelectedIds.contains(it.localId.toString()) }
    val visibleIds = visible.map { it.localId.toString() }.toSet()
    val visibleAllSelected = visibleIds.isNotEmpty() && visibleIds.all { selectedIds.contains(it) }
    PageScaffold(
        title = request.title,
        largeTitle = request.title,
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "保存选择",
                onPrimaryClick = {
                    if (!request.multiSelect && selectedIds.isEmpty()) {
                        Toast.makeText(context, "请选择收藏", Toast.LENGTH_SHORT).show()
                    } else {
                        request.onValue(formatFavoriteIds(selectedIds, request.delimiter, request.multiSelect))
                        onBack()
                    }
                },
                secondaryText = "返回",
                onSecondaryClick = onBack,
                middleText = if (request.multiSelect && visibleIds.isNotEmpty()) {
                    if (visibleAllSelected) "取消全选" else "全选"
                } else {
                    null
                },
                onMiddleClick = if (request.multiSelect && visibleIds.isNotEmpty()) {
                    {
                        selectedIds = if (visibleAllSelected) {
                            selectedIds - visibleIds
                        } else {
                            selectedIds + visibleIds
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
            item {
                SettingsCard {
                    InputRow("搜索", "标题 / 来源 / 标签", query) { query = it }
                    FavoritePickerFilterRow(selected = filter, onSelected = { filter = it })
                }
            }
            item {
                PickerSectionHeader(
                    text = if (backgroundLoading) {
                        "${filter.label}收藏 · ${visible.size} 项 · 后台加载中（${favorites.size}）"
                    } else {
                        "${filter.label}收藏 · ${visible.size} / ${favorites.size} 项"
                    }
                )
            }
            when {
                loading -> item { SettingsCard { EmptyText("正在载入收藏...") } }
                error.isNotEmpty() -> item { SettingsCard { EmptyText(error) } }
                visible.isEmpty() && !backgroundLoading -> item {
                    SettingsCard {
                        EmptyText(if (query.isBlank()) "没有可选收藏" else "没有匹配收藏")
                    }
                }
                visible.isEmpty() -> item { SettingsCard { EmptyText("正在后台查找收藏...") } }
                else -> visible.forEach { favorite ->
                    item {
                        val id = favorite.localId.toString()
                        FavoriteListCard(
                            item = favorite,
                            selected = selectedIds.contains(id),
                            multiSelect = request.multiSelect,
                            onPreview = { path -> preview = FavoriteMediaPreview(favorite, path) },
                            onClick = {
                                selectedIds = if (request.multiSelect) {
                                    if (selectedIds.contains(id)) selectedIds - id else selectedIds + id
                                } else {
                                    setOf(id)
                                }
                            }
                        )
                    }
                }
            }
            if (backgroundLoading && visible.isNotEmpty()) {
                item { SettingsCard { EmptyText("正在后台加载更多收藏...") } }
            }
        }
    }
    preview?.let { media ->
        FavoriteMediaPreviewDialog(media = media, onClose = { preview = null })
    }
}

@Composable
internal fun GroupMemberPickerPage(
    context: Context,
    request: GroupMemberPickerRequest,
    onBack: () -> Unit,
    onConfirm: (List<String>) -> Unit
) {
    var groupLoading by remember(request) { mutableStateOf(true) }
    var groupError by remember(request) { mutableStateOf("") }
    var groups by remember(request) { mutableStateOf(emptyList<ContactOption>()) }
    var selectedGroup by remember(request) { mutableStateOf<ContactOption?>(null) }
    var memberLoading by remember(request) { mutableStateOf(false) }
    var memberError by remember(request) { mutableStateOf("") }
    var members by remember(request) { mutableStateOf(emptyList<ContactOption>()) }
    var query by remember(request) { mutableStateOf("") }
    val initialSelectedEntries = remember(request) {
        parseGroupMemberEntries(request.existingValue).filterTo(linkedSetOf()) { entry ->
            request.allowedGroupIds?.let { entry.substringBefore('/') in it } != false
        }
    }
    var selectedEntries by remember(request) { mutableStateOf<Set<String>>(initialSelectedEntries) }
    var selectionEdited by remember(request) { mutableStateOf(initialSelectedEntries.isNotEmpty()) }
    val groupListState = rememberLazyListState()
    val memberListState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()

    LaunchedEffect(request) {
        loadContacts(context, ContactPickerMode.GROUPS) { result, throwable ->
            groupLoading = false
            if (throwable != null) {
                groupError = throwable.message ?: "读取群聊失败"
            } else {
                groups = request.allowedGroupIds?.let { allowed ->
                    result.filter { it.id in allowed }
                } ?: result
            }
        }
    }
    LaunchedEffect(selectedGroup?.id) {
        val group = selectedGroup
        if (group == null) {
            members = emptyList()
            memberError = ""
            memberLoading = false
            return@LaunchedEffect
        }
        memberLoading = true
        memberError = ""
        members = emptyList()
        query = ""
        memberListState.scrollToItem(0)
        loadGroupMembers(context, group) { result, throwable ->
            memberLoading = false
            if (throwable != null) {
                memberError = throwable.message ?: "读取群成员失败"
            } else {
                members = result
            }
        }
    }

    val group = selectedGroup
    PageScaffold(
        title = group?.label ?: request.title,
        largeTitle = group?.label ?: request.title,
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = if (group == null && selectedEntries.isEmpty() && !selectionEdited) "选择群聊" else "保存选择",
                onPrimaryClick = {
                    if (selectedGroup == null && selectedEntries.isEmpty() && !selectionEdited) {
                        Toast.makeText(context, "请先选择群聊", Toast.LENGTH_SHORT).show()
                    } else {
                        onConfirm(selectedEntries.toList())
                    }
                },
                secondaryText = if (group == null) "返回" else "重新选群",
                onSecondaryClick = {
                    if (selectedGroup == null) {
                        onBack()
                    } else {
                        selectedGroup = null
                        query = ""
                    }
                },
                middleText = if (selectedEntries.isEmpty()) null else "清空选择",
                onMiddleClick = if (selectedEntries.isEmpty()) null else {
                    {
                        selectedEntries = emptySet()
                        selectionEdited = true
                    }
                }
            )
        }
    ) { padding ->
        val listRoute = group?.let { GroupMemberPickerRoute.Members(it) } ?: GroupMemberPickerRoute.Groups
        SettingsRouteTransition(
            targetState = listRoute,
            label = "GroupMemberPickerTransition",
            depthOf = { it.depth() }
        ) { route ->
            val routeGroup = (route as? GroupMemberPickerRoute.Members)?.group
            val lower = query.trim().lowercase(Locale.US)
            val rows = if (routeGroup == null) groups else members
            val visible = rows.filter {
                it.matchesSearch(lower)
            }.selectedFirst { option ->
                if (routeGroup == null) {
                    initialSelectedEntries.any { it.substringBefore('/') == option.id }
                } else {
                    initialSelectedEntries.contains(groupMemberEntry(routeGroup.id, option.id))
                }
            }
            val routeListState = if (routeGroup == null) groupListState else memberListState
            LazyColumn(
                modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
                state = routeListState,
                contentPadding = PaddingValues(
                    top = padding.calculateTopPadding() + 8.dp,
                    bottom = padding.calculateBottomPadding() + 84.dp
                )
            ) {
                item {
                    SettingsCard {
                        InputRow(
                            title = "搜索",
                            summary = if (routeGroup == null) "群名称 / 群聊备注 / 群号" else "群昵称 / wxid",
                            value = query,
                            onValueChange = { query = it }
                        )
                    }
                }
                item {
                    SmallTitle(
                        modifier = Modifier.padding(top = 10.dp),
                        text = if (routeGroup == null) "群聊" else "群成员"
                    )
                }
                when {
                    routeGroup == null && groupLoading -> item { SettingsCard { EmptyText("正在载入群聊...") } }
                    routeGroup == null && groupError.isNotEmpty() -> item { SettingsCard { EmptyText(groupError) } }
                    routeGroup != null && memberLoading -> item { SettingsCard { EmptyText("正在载入群成员...") } }
                    routeGroup != null && memberError.isNotEmpty() -> item { SettingsCard { EmptyText(memberError) } }
                    visible.isEmpty() -> item { SettingsCard { EmptyText("没有匹配结果") } }
                    else -> visible.forEach { option ->
                        item {
                            val entry = routeGroup?.let { groupMemberEntry(it.id, option.id) }.orEmpty()
                            ContactListCard(
                                option = option,
                                selected = if (routeGroup == null) false else selectedEntries.contains(entry),
                                multiSelect = routeGroup != null,
                                onClick = {
                                    if (routeGroup == null) {
                                        selectedGroup = option
                                    } else if (selectedEntries.contains(entry)) {
                                        selectedEntries = selectedEntries - entry
                                        selectionEdited = true
                                    } else {
                                        selectedEntries = selectedEntries + entry
                                        selectionEdited = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun OptionPickerPage(
    request: OptionPickerRequest,
    onBack: () -> Unit,
    onSelected: (OptionItem) -> Unit
) {
    val scrollBehavior = MiuixScrollBehavior()
    val listState = rememberLazyListState()
    var selectedValue by remember(request) { mutableStateOf(request.currentValue) }
    PageScaffold(
        title = request.title,
        largeTitle = request.title,
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
            item { SmallTitle(text = "选项") }
            item {
                SettingsCard {
                    request.options.forEachIndexed { index, item ->
                        OptionChoiceRow(
                            item = item,
                            selected = item.value == selectedValue,
                            onClick = {
                                selectedValue = item.value
                                onSelected(item)
                            }
                        )
                        if (index < request.options.lastIndex) InsetDivider()
                    }
                }
            }
        }
    }
}

@Composable
internal fun UnsupportedFeaturePage(provider: FeatureSettingsProvider, onBack: () -> Unit) {
    val scrollBehavior = MiuixScrollBehavior()
    val listState = rememberLazyListState()
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
            item {
                SettingsCard {
                    EmptyText("这个模块暂时没有可配置项")
                }
            }
        }
    }
}

@Composable
internal fun PageScaffold(
    title: String,
    largeTitle: String,
    scrollBehavior: ScrollBehavior,
    onBack: (() -> Unit)? = null,
    topBarActions: @Composable RowScope.() -> Unit = {},
    bottomBar: @Composable ((Backdrop) -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    RegisterSettingsBackHandler(onBack)
    val graphicsLayer = rememberGraphicsLayer()
    val backdrop = rememberLayerBackdrop(graphicsLayer)
    val navigationInset = navigationButtonBottomInset()
    val navigationGap = if (navigationInset > 0.dp) NAVIGATION_BUTTON_EXTRA_GAP else 0.dp
    val bottomAvoidance = navigationInset + navigationGap
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MiuixTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .layerBackdrop(backdrop)
        ) {
            top.yukonga.miuix.kmp.basic.Scaffold(
                topBar = {
                    TopAppBar(
                        title = title,
                        largeTitle = largeTitle,
                        scrollBehavior = scrollBehavior,
                        navigationIcon = {
                            onBack?.let { back ->
                                Box(
                                    modifier = Modifier.size(40.dp).responsiveTap(onClick = back),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        imageVector = NavIcons.Back,
                                        contentDescription = "返回",
                                        colorFilter = ColorFilter.tint(MiuixTheme.colorScheme.onSurface),
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        },
                        actions = topBarActions,
                        defaultWindowInsetsPadding = true
                    )
                },
                bottomBar = {},
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                content = { padding ->
                    content(
                        PaddingValues(
                            top = padding.calculateTopPadding(),
                            bottom = padding.calculateBottomPadding() + bottomAvoidance
                        )
                    )
                }
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = bottomAvoidance)
                .align(Alignment.BottomCenter)
        ) {
            bottomBar?.invoke(backdrop)
        }
    }
}

@Composable
internal fun SettingsCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth().padding(horizontal = 12.dp),
        cornerRadius = 18.dp
    ) {
        content()
    }
}

@Composable
internal fun SwitchRow(
    sp: SharedPreferences,
    key: String,
    title: String,
    summary: String,
    defaultValue: Boolean
) {
    var checked by remember { mutableStateOf(sp.getBoolean(key, defaultValue)) }
    Row(
        modifier = Modifier.fillMaxWidth().clickable {
            checked = !checked
            sp.edit().putBoolean(key, checked).apply()
        }.padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = MiuixTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
            if (summary.isNotBlank()) {
                Text(text = summary, color = MiuixTheme.colorScheme.onSurfaceVariantSummary, fontSize = 12.sp)
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = {
                checked = it
                sp.edit().putBoolean(key, it).apply()
            }
        )
    }
}

@Composable
internal fun SwitchRow(
    checked: Boolean,
    title: String,
    summary: String,
    enabled: Boolean = true,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().then(
            if (enabled) {
                Modifier.clickable { onCheckedChange(!checked) }
            } else {
                Modifier
            }
        ).padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = MiuixTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
            Text(text = summary, color = MiuixTheme.colorScheme.onSurfaceVariantSummary, fontSize = 12.sp)
        }
        Switch(
            checked = checked,
            onCheckedChange = { if (enabled) onCheckedChange(it) }
        )
    }
}

@Composable
internal fun OptionRow(
    sp: SharedPreferences,
    key: String,
    title: String,
    options: List<OptionItem>,
    defaultValue: Int,
    onValueChanged: (Int) -> Unit = {}
) {
    var currentValue by remember {
        mutableStateOf(sp.getInt(key, defaultValue))
    }
    PopupOptionRow(
        title = title,
        summary = options.firstOrNull { it.value == currentValue }?.label
            ?: options.firstOrNull { it.value == defaultValue }?.label
            ?: "",
        options = options,
        currentValue = currentValue,
        onValueChanged = {
            currentValue = it
            onValueChanged(it)
            sp.edit().putInt(key, it).apply()
        }
    )
}

@Composable
internal fun PopupOptionRow(
    title: String,
    summary: String,
    options: List<OptionItem>,
    currentValue: Int,
    onValueChanged: (Int) -> Unit,
    enabled: Boolean = true
) {
    val selectedIndex = options.indexOfFirst { it.value == currentValue }.takeIf { it >= 0 } ?: 0
    val labels = options.map { it.label }
    WindowDropdown(
        items = labels,
        selectedIndex = selectedIndex,
        title = title,
        summary = summary,
        enabled = enabled,
        showValue = true,
        onSelectedIndexChange = { index ->
            options.getOrNull(index)?.let { onValueChanged(it.value) }
        }
    )
}

@Composable
internal fun InputRow(
    title: String,
    summary: String,
    value: String,
    minLines: Int = 1,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp)) {
        Text(text = title, color = MiuixTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
        Text(text = summary, color = MiuixTheme.colorScheme.onSurfaceVariantSummary, fontSize = 12.sp)
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                color = MiuixTheme.colorScheme.onSurface,
                fontSize = 14.sp
            ),
            minLines = minLines,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MiuixTheme.colorScheme.secondaryVariant)
                .padding(horizontal = 12.dp, vertical = 10.dp)
        )
    }
}

@Composable
internal fun PopupChoiceRow(
    title: String,
    summary: String,
    options: List<PopupChoice<String>>,
    currentValue: String,
    onValueChanged: (String) -> Unit,
    enabled: Boolean = true
) {
    val selectedIndex = options.indexOfFirst { it.value == currentValue }.takeIf { it >= 0 } ?: 0
    WindowDropdown(
        items = options.map { it.label },
        selectedIndex = selectedIndex,
        title = title,
        summary = summary,
        enabled = enabled,
        showValue = true,
        onSelectedIndexChange = { index ->
            options.getOrNull(index)?.let { onValueChanged(it.value) }
        }
    )
}

@Composable
internal fun ColorPickerRow(
    title: String,
    summary: String,
    value: String,
    allowGradient: Boolean = true,
    onReset: (() -> Unit)? = null,
    onValueChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val currentValue by rememberUpdatedState(value)
    val colorParts = if (allowGradient) {
        colorSpecParts(value)
    } else {
        MemberTitleStore.cleanColor(value) to ""
    }
    var editEnd by remember { mutableStateOf(colorParts.second.isNotEmpty()) }
    val selectedColor = if (allowGradient && editEnd) colorParts.second.ifEmpty { colorParts.first } else colorParts.first
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().clickable { expanded = !expanded },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
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
                Text(
                    text = if (allowGradient) {
                        "$summary，支持 #RRGGBB / #AARRGGBB / #A,#B 渐变"
                    } else {
                        "$summary，支持 #RRGGBB / #AARRGGBB"
                    },
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    fontSize = 12.sp
                )
            }
            ColorPreviewDot(if (allowGradient) value else colorParts.first)
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = value,
                onValueChange = { onValueChange(it.take(if (allowGradient) 19 else 9)) },
                textStyle = TextStyle(
                    color = MiuixTheme.colorScheme.onSurface,
                    fontSize = 14.sp
                ),
                modifier = Modifier.weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MiuixTheme.colorScheme.secondaryVariant)
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            )
            if (onReset != null) {
                Text(
                    text = "重置",
                    color = MiuixTheme.colorScheme.primary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable(onClick = onReset)
                        .background(MiuixTheme.colorScheme.secondaryVariant)
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                )
            }
        }
        if (expanded) {
            if (allowGradient) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ColorSpecChip(
                        label = "起始色",
                        value = colorParts.first,
                        selected = !editEnd,
                        onClick = { editEnd = false },
                        modifier = Modifier.weight(1f)
                    )
                    ColorSpecChip(
                        label = "结束色",
                        value = colorParts.second,
                        selected = editEnd,
                        onClick = { editEnd = true },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            PsColorPicker(
                value = selectedColor,
                onValueChange = { picked ->
                    val next = if (allowGradient) {
                        val latestParts = colorSpecParts(currentValue)
                        if (editEnd) {
                            composeColorSpec(latestParts.first.ifEmpty { picked }, picked)
                        } else {
                            composeColorSpec(picked, latestParts.second)
                        }
                    } else {
                        picked
                    }
                    onValueChange(next)
                },
                modifier = Modifier.padding(top = if (allowGradient) 12.dp else 8.dp)
            )
            Text(
                text = if (allowGradient) {
                    "先选起始色或结束色，再用色盘取色；清空输入框可恢复默认/跟随昵称"
                } else {
                    "可直接输入颜色值，也可以用色盘取色；清空输入框可恢复默认色"
                },
                color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
internal fun ColorSpecChip(
    label: String,
    value: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (selected) MiuixTheme.colorScheme.primary else MiuixTheme.colorScheme.outline.copy(alpha = 0.45f)
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .border(1.dp, borderColor, RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ColorPreviewDot(value, size = 24.dp)
        Column {
            Text(text = label, color = MiuixTheme.colorScheme.onSurface, fontSize = 12.sp)
            Text(
                text = value.ifEmpty { "未设置" },
                color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
internal fun ColorPreviewDot(value: String, size: Dp = 34.dp) {
    val colorParts = colorSpecParts(value)
    val startColor = composeColorFromHex(colorParts.first) ?: MiuixTheme.colorScheme.secondaryVariant
    val endColor = composeColorFromHex(colorParts.second)
    val shape = RoundedCornerShape(size / 2)
    val colorBackground = if (endColor != null && endColor != startColor) {
        Modifier.background(Brush.horizontalGradient(listOf(startColor, endColor)))
    } else {
        Modifier.background(startColor)
    }
    Box(
        modifier = Modifier
            .size(size)
            .clip(shape)
            .then(colorBackground)
            .border(2.dp, MiuixTheme.colorScheme.outline.copy(alpha = 0.45f), shape),
        contentAlignment = Alignment.Center
    ) {
        if (value.isEmpty()) {
            Text(text = "-", color = MiuixTheme.colorScheme.onSurfaceVariantSummary, fontSize = 15.sp)
        }
    }
}

@Composable
internal fun PsColorPicker(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val selected = colorPickerSelection(value)
    val markerColor = MiuixTheme.colorScheme.onSurface
    val density = LocalDensity.current
    val paletteSize = 228.dp
    val hueBarWidth = 40.dp
    val paletteSizePx = remember(density) { with(density) { paletteSize.roundToPx() } }
    val hueBarWidthPx = remember(density) { with(density) { hueBarWidth.roundToPx() } }
    val paletteBitmap = remember(selected.hue, paletteSizePx) {
        buildSvPaletteBitmap(selected.hue, paletteSizePx)
    }
    val hueBitmap = remember(paletteSizePx, hueBarWidthPx) {
        buildHuePaletteBitmap(hueBarWidthPx, paletteSizePx)
    }
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(
            modifier = Modifier
                .size(paletteSize)
                .pointerInput(selected.hue) {
                    awaitEachGesture {
                        val down = awaitFirstDown(requireUnconsumed = false)
                        onValueChange(
                            colorFromSvOffset(
                                selected.hue,
                                down.position,
                                size.width.toFloat(),
                                size.height.toFloat()
                            )
                        )
                        while (true) {
                            val event = awaitPointerEvent()
                            val change = event.changes.firstOrNull { it.id == down.id } ?: break
                            onValueChange(
                                colorFromSvOffset(
                                    selected.hue,
                                    change.position,
                                    size.width.toFloat(),
                                    size.height.toFloat()
                                )
                            )
                            change.consume()
                            if (!change.pressed) break
                        }
                    }
                }
        ) {
            drawImage(paletteBitmap)
            val marker = Offset(
                selected.saturation * size.width,
                (1f - selected.value) * size.height
            )
            drawCircle(color = markerColor, radius = 9f, center = marker)
            drawCircle(color = Color.White, radius = 5.5f, center = marker)
        }
        Canvas(
            modifier = Modifier
                .padding(start = 14.dp)
                .size(width = hueBarWidth, height = paletteSize)
                .clip(RoundedCornerShape(14.dp))
                .pointerInput(selected.saturation, selected.value) {
                    awaitEachGesture {
                        val down = awaitFirstDown(requireUnconsumed = false)
                        onValueChange(
                            colorFromHueOffset(
                                selected.saturation,
                                selected.value,
                                down.position.y,
                                size.height.toFloat()
                            )
                        )
                        while (true) {
                            val event = awaitPointerEvent()
                            val change = event.changes.firstOrNull { it.id == down.id } ?: break
                            onValueChange(
                                colorFromHueOffset(
                                    selected.saturation,
                                    selected.value,
                                    change.position.y,
                                    size.height.toFloat()
                                )
                            )
                            change.consume()
                            if (!change.pressed) break
                        }
                    }
                }
        ) {
            drawImage(hueBitmap)
            val markerY = (selected.hue / 360f).coerceIn(0f, 1f) * size.height
            drawCircle(color = markerColor, radius = 10f, center = Offset(size.width / 2f, markerY))
            drawCircle(color = Color.White, radius = 6f, center = Offset(size.width / 2f, markerY))
        }
    }
}

internal fun buildSvPaletteBitmap(hue: Float, sizePx: Int): ImageBitmap {
    val safeSize = sizePx.coerceAtLeast(2)
    val bitmap = Bitmap.createBitmap(safeSize, safeSize, Bitmap.Config.ARGB_8888)
    val pixels = IntArray(safeSize * safeSize)
    var index = 0
    for (y in 0 until safeSize) {
        val value = (1f - (y.toFloat() / (safeSize - 1))).coerceIn(0f, 1f)
        for (x in 0 until safeSize) {
            val saturation = (x.toFloat() / (safeSize - 1)).coerceIn(0f, 1f)
            pixels[index++] = AndroidColor.HSVToColor(floatArrayOf(hue, saturation, value))
        }
    }
    bitmap.setPixels(pixels, 0, safeSize, 0, 0, safeSize, safeSize)
    return bitmap.asImageBitmap()
}

internal fun buildHuePaletteBitmap(widthPx: Int, heightPx: Int): ImageBitmap {
    val safeWidth = widthPx.coerceAtLeast(2)
    val safeHeight = heightPx.coerceAtLeast(2)
    val bitmap = Bitmap.createBitmap(safeWidth, safeHeight, Bitmap.Config.ARGB_8888)
    val pixels = IntArray(safeWidth * safeHeight)
    var index = 0
    for (y in 0 until safeHeight) {
        val hue = (y.toFloat() / (safeHeight - 1)).coerceIn(0f, 1f) * 360f
        val color = AndroidColor.HSVToColor(floatArrayOf(hue, 1f, 1f))
        repeat(safeWidth) {
            pixels[index++] = color
        }
    }
    bitmap.setPixels(pixels, 0, safeWidth, 0, 0, safeWidth, safeHeight)
    return bitmap.asImageBitmap()
}

internal fun composeColorFromHex(value: String): Color? {
    val normalized = RealNameTailStore.cleanColor(value)
    if (normalized.isEmpty()) return null
    return runCatching { Color(AndroidColor.parseColor(normalized)) }.getOrNull()
}

internal fun colorSpecParts(value: String): Pair<String, String> {
    val cleaned = MemberTitleStore.cleanColorSpec(value)
    if (cleaned.isEmpty()) return "" to ""
    val parts = cleaned.split(',').take(2)
    return parts.getOrElse(0) { "" } to parts.getOrElse(1) { "" }
}

internal fun composeColorSpec(start: String, end: String): String {
    val first = MemberTitleStore.cleanColor(start)
    val second = MemberTitleStore.cleanColor(end)
    if (first.isEmpty()) return ""
    return if (second.isEmpty() || second == first) first else "$first,$second"
}

@Composable
internal fun WeightInputRow(
    title: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    InputRow(
        title = title,
        summary = "100-900，600 以上显示为加粗",
        value = value,
        onValueChange = { next ->
            onValueChange(next.filter { it.isDigit() }.take(3))
        }
    )
}

internal data class ColorPickerSelection(val hue: Float, val saturation: Float, val value: Float)

internal fun colorPickerSelection(value: String): ColorPickerSelection {
    val normalized = MemberTitleStore.cleanColor(value)
    if (normalized.isEmpty()) return ColorPickerSelection(0f, 1f, 1f)
    return runCatching {
        val hsv = FloatArray(3)
        AndroidColor.colorToHSV(AndroidColor.parseColor(normalized), hsv)
        ColorPickerSelection(hsv[0], hsv[1], hsv[2])
    }.getOrDefault(ColorPickerSelection(0f, 1f, 1f))
}

internal fun colorFromSvOffset(hue: Float, offset: Offset, width: Float, height: Float): String {
    val saturation = (offset.x / width).coerceIn(0f, 1f)
    val value = (1f - (offset.y / height)).coerceIn(0f, 1f)
    val color = AndroidColor.HSVToColor(floatArrayOf(hue, saturation, value))
    return String.format("#%06X", 0xFFFFFF and color)
}

internal fun colorFromHueOffset(saturation: Float, value: Float, y: Float, height: Float): String {
    val hue = (y / height).coerceIn(0f, 1f) * 360f
    val color = AndroidColor.HSVToColor(floatArrayOf(hue, saturation, value))
    return String.format("#%06X", 0xFFFFFF and color)
}

@Composable
internal fun VariableInputRow(
    title: String,
    summary: String,
    value: String,
    variables: List<TemplateVariable>,
    minLines: Int = 1,
    onValueChange: (String) -> Unit
) {
    var fieldValue by remember {
        mutableStateOf(TextFieldValue(value, TextRange(value.length)))
    }
    LaunchedEffect(value) {
        if (value != fieldValue.text) {
            fieldValue = TextFieldValue(value, TextRange(value.length))
        }
    }
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp)) {
        Text(text = title, color = MiuixTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
        if (summary.isNotBlank()) {
            Text(text = summary, color = MiuixTheme.colorScheme.onSurfaceVariantSummary, fontSize = 12.sp)
        }
        BasicTextField(
            value = fieldValue,
            onValueChange = {
                fieldValue = it
                onValueChange(it.text)
            },
            textStyle = TextStyle(
                color = MiuixTheme.colorScheme.onSurface,
                fontSize = 14.sp
            ),
            minLines = minLines,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MiuixTheme.colorScheme.secondaryVariant)
                .padding(horizontal = 12.dp, vertical = 10.dp)
        )
        VariableChipRow(
            variables = variables,
            onInsert = { token ->
                val next = fieldValue.insertToken(token)
                fieldValue = next
                onValueChange(next.text)
            }
        )
    }
}

@Composable
internal fun RedPacketReplyContentEditor(
    context: Context,
    replyMode: Int,
    value: String,
    onValueChange: (String) -> Unit,
    onFavoritePicker: (FavoritePickerRequest) -> Unit,
    templateVariables: List<TemplateVariable> = redPacketReplyTemplateVariables
) {
    when {
        redPacketReplyUsesText(replyMode) -> {
            VariableInputRow(
                title = "回复内容",
                summary = "多个回复用 | 分隔随机选择",
                value = value,
                variables = templateVariables,
                minLines = 2,
                onValueChange = onValueChange
            )
        }
        replyMode == RedPacketRuleConfig.REPLY_XML -> {
            VariableInputRow(
                title = "XML 内容",
                summary = "输入 <msg><appmsg>...</appmsg></msg>，支持变量",
                value = value,
                variables = templateVariables,
                minLines = 4,
                onValueChange = onValueChange
            )
        }
        replyMode == RedPacketRuleConfig.REPLY_FAVORITE -> {
            ActionRow(
                title = redPacketReplyPickerTitle(replyMode),
                summary = redPacketReplyContentSummary(replyMode, value)
            ) {
                onFavoritePicker(
                    FavoritePickerRequest(
                        title = redPacketReplyPickerTitle(replyMode),
                        existingValue = value,
                        onValue = onValueChange,
                        multiSelect = true,
                        delimiter = "|"
                    )
                )
            }
            if (value.isNotBlank()) {
                InsetDivider()
                ActionRow("清空已选收藏", "当前会从该回复类型移除已选收藏") {
                    onValueChange("")
                }
            }
        }
        else -> {
            ActionRow(
                title = redPacketReplyPickerTitle(replyMode),
                summary = redPacketReplyContentSummary(replyMode, value)
            ) {
                val activity = context as? Activity
                if (activity == null) {
                    Toast.makeText(context, "当前页面无法打开文件选择器", Toast.LENGTH_SHORT).show()
                } else {
                    RedPacketReplyFilePickerBridge.launch(activity, replyMode) { paths ->
                        if (paths.isNotEmpty()) {
                            onValueChange(paths.joinToString("|"))
                            Toast.makeText(context, "已选择 ${paths.size} 个文件", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            if (value.isNotBlank()) {
                InsetDivider()
                ActionRow("清空已选文件", "当前会从该回复类型移除已选文件") {
                    onValueChange("")
                }
            }
        }
    }
}

@Composable
internal fun RedPacketReplyStepsPage(
    context: Context,
    title: String,
    initialSteps: List<RedPacketReplyStep>,
    onBack: () -> Unit,
    onSave: (List<RedPacketReplyStep>) -> Unit,
    templateVariables: List<TemplateVariable> = redPacketReplyTemplateVariables
) {
    var steps by remember(initialSteps) {
        mutableStateOf(initialSteps.map { normalizeRedPacketReplyStepForUi(it) })
    }
    var editingIndex by remember { mutableStateOf<Int?>(null) }
    var favoritePicker by remember { mutableStateOf<FavoritePickerRequest?>(null) }
    val listState = rememberLazyListState()

    fun updateStep(index: Int, step: RedPacketReplyStep) {
        if (index !in steps.indices) return
        steps = steps.toMutableList().also { it[index] = normalizeRedPacketReplyStepForUi(step) }
    }

    val currentEditingIndex = editingIndex?.takeIf { it in steps.indices }
    val route = when {
        favoritePicker != null -> RedPacketReplyStepsRoute.FavoritePicker(favoritePicker!!)
        currentEditingIndex != null -> RedPacketReplyStepsRoute.StepEditor(currentEditingIndex)
        else -> RedPacketReplyStepsRoute.Main
    }

    SettingsRouteTransition(
        targetState = route,
        label = "RedPacketReplyStepsRoute",
        depthOf = { it.depth() }
    ) { currentRoute ->
        when (currentRoute) {
            is RedPacketReplyStepsRoute.FavoritePicker -> {
                FavoritePickerPage(
                    request = currentRoute.request,
                    onBack = { favoritePicker = null }
                )
            }
            is RedPacketReplyStepsRoute.StepEditor -> {
                val index = currentRoute.index
                val step = steps[index]
                RedPacketReplyStepEditorPage(
                    context = context,
                    title = "第 ${index + 1} 条回复",
                    step = step,
                    canMoveUp = index > 0,
                    canMoveDown = index < steps.lastIndex,
                    onBack = { editingIndex = null },
                    onStepChange = { updateStep(index, it) },
                    templateVariables = templateVariables,
                    onFavoritePicker = { favoritePicker = it },
                    onMoveUp = {
                        if (index > 0) {
                            steps = steps.toMutableList().also {
                                val current = it.removeAt(index)
                                it.add(index - 1, current)
                            }
                            editingIndex = index - 1
                        }
                    },
                    onMoveDown = {
                        if (index < steps.lastIndex) {
                            steps = steps.toMutableList().also {
                                val current = it.removeAt(index)
                                it.add(index + 1, current)
                            }
                            editingIndex = index + 1
                        }
                    },
                    onDelete = {
                        steps = steps.toMutableList().also { it.removeAt(index) }
                        editingIndex = null
                    }
                )
            }
            RedPacketReplyStepsRoute.Main -> RedPacketReplyStepsListPage(
                title = title,
                steps = steps,
                listState = listState,
                onBack = onBack,
                onSave = { onSave(cleanRedPacketReplySteps(steps)) },
                onOpenStep = { editingIndex = it },
                onAddStep = {
                    val index = steps.size
                    steps = steps + newRedPacketReplyStep(index + 1)
                    editingIndex = index
                },
                onMoveUp = { index ->
                    if (index > 0) {
                        steps = steps.toMutableList().also {
                            val current = it.removeAt(index)
                            it.add(index - 1, current)
                        }
                    }
                },
                onMoveDown = { index ->
                    if (index < steps.lastIndex) {
                        steps = steps.toMutableList().also {
                            val current = it.removeAt(index)
                            it.add(index + 1, current)
                        }
                    }
                },
                onDelete = { index ->
                    if (index in steps.indices) steps = steps.toMutableList().also { it.removeAt(index) }
                }
            )
        }
    }
}

@Composable
internal fun RedPacketReplyStepsListPage(
    title: String,
    steps: List<RedPacketReplyStep>,
    listState: LazyListState,
    onBack: () -> Unit,
    onSave: () -> Unit,
    onOpenStep: (Int) -> Unit,
    onAddStep: () -> Unit,
    onMoveUp: (Int) -> Unit,
    onMoveDown: (Int) -> Unit,
    onDelete: (Int) -> Unit
) {
    val scrollBehavior = MiuixScrollBehavior()
    PageScaffold(
        title = title,
        largeTitle = title,
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "保存回复",
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
            item { SmallTitle(text = "按顺序发送") }
            if (steps.isEmpty()) {
                item {
                    SettingsCard {
                        EmptyText("暂无回复步骤。添加后会按列表顺序依次发送。")
                    }
                }
            } else {
                steps.forEachIndexed { index, step ->
                    item { SmallTitle(modifier = Modifier.padding(top = if (index == 0) 0.dp else 10.dp), text = "第 ${index + 1} 条") }
                    item {
                        SettingsCard {
                            SelectRow(
                                title = redPacketReplyModeLabel(step.mode),
                                summary = redPacketReplyStepSummary(step),
                                onClick = { onOpenStep(index) }
                            )
                            InsetDivider()
                            RedPacketReplyStepActions(
                                canMoveUp = index > 0,
                                canMoveDown = index < steps.lastIndex,
                                onMoveUp = { onMoveUp(index) },
                                onMoveDown = { onMoveDown(index) },
                                onDelete = { onDelete(index) }
                            )
                        }
                    }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "操作") }
            item {
                SettingsCard {
                    ActionRow("新增回复", "添加一条按顺序发送的回复", onAddStep)
                }
            }
        }
    }
}

@Composable
internal fun RedPacketReplyStepEditorPage(
    context: Context,
    title: String,
    step: RedPacketReplyStep,
    canMoveUp: Boolean,
    canMoveDown: Boolean,
    onBack: () -> Unit,
    onStepChange: (RedPacketReplyStep) -> Unit,
    templateVariables: List<TemplateVariable>,
    onFavoritePicker: (FavoritePickerRequest) -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onDelete: () -> Unit
) {
    val scrollBehavior = MiuixScrollBehavior()
    PageScaffold(
        title = title,
        largeTitle = title,
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "完成",
                onPrimaryClick = onBack,
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
            item { SmallTitle(text = "回复内容") }
            item {
                SettingsCard {
                    PopupOptionRow(
                        title = "回复类型",
                        summary = redPacketReplyModeLabel(step.mode),
                        options = redPacketReplyStepModeOptions(),
                        currentValue = step.mode,
                        onValueChanged = { mode ->
                            onStepChange(
                                step.copy(
                                    mode = mode,
                                    content = if (mode == step.mode) {
                                        step.content
                                    } else {
                                        defaultRedPacketReplyContent(mode)
                                    }
                                )
                            )
                        }
                    )
                    InsetDivider()
                    RedPacketReplyContentEditor(
                        context = context,
                        replyMode = step.mode,
                        value = step.content,
                        onValueChange = { onStepChange(step.copy(content = it)) },
                        onFavoritePicker = onFavoritePicker,
                        templateVariables = templateVariables
                    )
                    InsetDivider()
                    NumberInputRow(
                        title = "发送前延迟",
                        summary = "单位 ms，按步骤顺序等待",
                        value = step.delayMs.coerceAtLeast(0L).toString(),
                        onValueChange = { value ->
                            onStepChange(step.copy(delayMs = value.toLongOrNull()?.coerceIn(0L, 600000L) ?: 0L))
                        }
                    )
                    InsetDivider()
                    SwitchRow(
                        checked = step.random,
                        title = "随机追加延迟",
                        summary = "在发送前延迟后随机追加 0-2 秒",
                        onCheckedChange = { onStepChange(step.copy(random = it)) }
                    )
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "操作") }
            item {
                SettingsCard {
                    RedPacketReplyStepActions(
                        canMoveUp = canMoveUp,
                        canMoveDown = canMoveDown,
                        onMoveUp = onMoveUp,
                        onMoveDown = onMoveDown,
                        onDelete = onDelete
                    )
                }
            }
        }
    }
}

@Composable
internal fun RedPacketReplyStepActions(
    canMoveUp: Boolean,
    canMoveDown: Boolean,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextButton(
            text = "上移",
            onClick = { if (canMoveUp) onMoveUp() },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.textButtonColorsPrimary()
        )
        TextButton(
            text = "下移",
            onClick = { if (canMoveDown) onMoveDown() },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.textButtonColorsPrimary()
        )
        TextButton(
            text = "删除",
            onClick = onDelete,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.textButtonColorsPrimary()
        )
    }
}

@Composable
internal fun VariableChipRow(
    variables: List<TemplateVariable>,
    onInsert: (String) -> Unit
) {
    if (variables.isEmpty()) return
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(top = 10.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        variables.forEach { variable ->
            VariableChip(variable = variable, onClick = { onInsert(variable.token) })
        }
    }
}

@Composable
internal fun VariableChip(variable: TemplateVariable, onClick: () -> Unit) {
    var pressed by remember { mutableStateOf(false) }
    val bg = if (pressed) {
        MiuixTheme.colorScheme.primary.copy(alpha = 0.22f)
    } else {
        MiuixTheme.colorScheme.primary.copy(alpha = 0.12f)
    }
    Row(
        modifier = Modifier
            .height(34.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(bg)
            .responsiveTap(
                onClick = onClick,
                onPressedChange = { pressed = it }
            )
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = variable.label,
            color = MiuixTheme.colorScheme.primary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

internal fun TextFieldValue.insertToken(token: String): TextFieldValue {
    val start = selection.start.coerceIn(0, text.length)
    val end = selection.end.coerceIn(0, text.length)
    val left = minOf(start, end)
    val right = maxOf(start, end)
    val nextText = text.replaceRange(left, right, token)
    val cursor = left + token.length
    return TextFieldValue(nextText, TextRange(cursor))
}

@Composable
internal fun NumberInputRow(title: String, summary: String, value: String, onValueChange: (String) -> Unit) {
    InputRow(title, summary, value.filter { it.isDigit() }, onValueChange = { next ->
        onValueChange(next.filter { it.isDigit() })
    })
}

@Composable
internal fun TimeOfDayPickerRow(
    title: String,
    value: String,
    allowEmpty: Boolean = false,
    onValueChange: (String) -> Unit
) {
    var showPicker by remember { mutableStateOf(false) }
    val normalized = normalizeTimeOfDay(value, if (allowEmpty) "" else "00:00:00")
    ActionRow(
        title = title,
        summary = normalized.ifBlank { "不限制" },
        onClick = { showPicker = true }
    )
    if (showPicker) {
        TimeOfDayPickerDialog(
            title = title,
            initialValue = normalized,
            allowEmpty = allowEmpty,
            onDismiss = { showPicker = false },
            onConfirm = {
                onValueChange(it)
                showPicker = false
            }
        )
    }
}

@Composable
internal fun TimeOfDayPickerDialog(
    title: String,
    initialValue: String,
    allowEmpty: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    val initialSeconds = parseTimeOfDaySeconds(initialValue).takeIf { it >= 0 } ?: 0
    var hour by remember(initialValue) { mutableStateOf((initialSeconds / 3600).toString()) }
    var minute by remember(initialValue) { mutableStateOf((initialSeconds / 60 % 60).toString()) }
    var second by remember(initialValue) { mutableStateOf((initialSeconds % 60).toString()) }
    val hourValue = hour.toIntOrNull()
    val minuteValue = minute.toIntOrNull()
    val secondValue = second.toIntOrNull()
    val valid = hourValue != null && hourValue in 0..23 &&
        minuteValue != null && minuteValue in 0..59 &&
        secondValue != null && secondValue in 0..59
    WindowDialog(
        show = true,
        title = title,
        onDismissRequest = onDismiss,
        content = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TimeOfDayNumberField("时", hour, 23, Modifier.weight(1f)) { hour = it }
                    TimeOfDayNumberField("分", minute, 59, Modifier.weight(1f)) { minute = it }
                    TimeOfDayNumberField("秒", second, 59, Modifier.weight(1f)) { second = it }
                }
                Text(
                    text = if (valid) {
                        String.format(
                            Locale.US,
                            "%02d:%02d:%02d",
                            hourValue ?: 0,
                            minuteValue ?: 0,
                            secondValue ?: 0
                        )
                    } else {
                        "请输入有效的时、分、秒"
                    },
                    color = if (valid) MiuixTheme.colorScheme.onSurfaceVariantSummary else Color(0xFFD93025),
                    fontSize = 13.sp,
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                    textAlign = TextAlign.Center
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (allowEmpty) {
                        TextButton(
                            text = "清空",
                            onClick = { onConfirm("") },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.textButtonColorsPrimary()
                        )
                    }
                    TextButton(
                        text = "取消",
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.textButtonColorsPrimary()
                    )
                    TextButton(
                        text = "确定",
                        onClick = {
                            if (valid) {
                                onConfirm(
                                    String.format(
                                        Locale.US,
                                        "%02d:%02d:%02d",
                                        hourValue ?: 0,
                                        minuteValue ?: 0,
                                        secondValue ?: 0
                                    )
                                )
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
internal fun TimeOfDayNumberField(
    label: String,
    value: String,
    max: Int,
    modifier: Modifier,
    onValueChange: (String) -> Unit
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
            fontSize = 12.sp,
            modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
            textAlign = TextAlign.Center
        )
        BasicTextField(
            value = value,
            onValueChange = { next ->
                val digits = next.filter { it.isDigit() }.take(2)
                val number = digits.toIntOrNull()
                if (digits.isEmpty() || (number != null && number <= max)) onValueChange(digits)
            },
            singleLine = true,
            textStyle = TextStyle(
                color = MiuixTheme.colorScheme.onSurface,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MiuixTheme.colorScheme.surfaceVariant)
                .padding(horizontal = 8.dp, vertical = 10.dp)
        )
    }
}

internal fun normalizeTimeOfDay(value: String?, fallback: String): String {
    val seconds = parseTimeOfDaySeconds(value)
    if (seconds < 0) return fallback
    return String.format(
        Locale.US,
        "%02d:%02d:%02d",
        seconds / 3600,
        seconds / 60 % 60,
        seconds % 60
    )
}

internal fun parseTimeOfDaySeconds(value: String?): Int {
    if (value.isNullOrBlank()) return -1
    val parts = value.trim().split(":")
    if (parts.size !in 2..3) return -1
    val hour = parts[0].toIntOrNull() ?: return -1
    val minute = parts[1].toIntOrNull() ?: return -1
    val second = parts.getOrNull(2)?.toIntOrNull() ?: 0
    if (hour !in 0..23 || minute !in 0..59 || second !in 0..59) return -1
    return hour * 3600 + minute * 60 + second
}

@Composable
internal fun MessageBlockContactFilterRow(
    selected: MessageBlockContactFilter,
    enableOfficials: Boolean = true,
    onSelected: (MessageBlockContactFilter) -> Unit
) {
    val items = buildList {
        add(MessageBlockContactFilter.FRIENDS)
        add(MessageBlockContactFilter.GROUPS)
        if (enableOfficials) add(MessageBlockContactFilter.OFFICIALS)
        add(MessageBlockContactFilter.LABELS)
        add(MessageBlockContactFilter.ALL)
    }
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items.forEach { item ->
            ContactFilterChip(
                text = item.label,
                selected = item == selected,
                onClick = { onSelected(item) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
internal fun ConversationRuleCategoryTabs(
    selected: ConversationRuleCategory,
    onSelected: (ConversationRuleCategory) -> Unit,
    includeOfficial: Boolean = true
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        ConversationRuleCategory.entries
            .filter { includeOfficial || it != ConversationRuleCategory.OFFICIAL }
            .forEach { category ->
            ContactFilterChip(
                text = category.label,
                selected = category == selected,
                onClick = { onSelected(category) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
internal fun BatchDeleteConfirmDialog(
    show: Boolean,
    message: String,
    labels: List<String>,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (!show) return
    WindowDialog(
        show = true,
        title = "确认批量删除",
        onDismissRequest = onDismiss,
        content = {
            Column {
                Text(
                    text = buildString {
                        append(message)
                        val names = labels.filter { it.isNotBlank() }.take(6).joinToString("、")
                        if (names.isNotBlank()) append("\n\n").append(names)
                        if (labels.size > 6) append(" 等")
                    },
                    color = MiuixTheme.colorScheme.onSurface,
                    fontSize = 14.sp
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
                        text = "确认删除",
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
internal fun FriendContactFilterRow(
    selected: ContactPickerFilter,
    onSelected: (ContactPickerFilter) -> Unit
) {
    val items = listOf(ContactPickerFilter.FRIENDS, ContactPickerFilter.LABELS)
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items.forEach { item ->
            ContactFilterChip(
                text = item.label,
                selected = item == selected,
                onClick = { onSelected(item) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
internal fun ContactPickerScopeFilterRow(
    selected: ContactPickerScopeTab,
    onSelected: (ContactPickerScopeTab) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        ContactPickerScopeTab.entries.forEach { item ->
            ContactFilterChip(
                text = item.label,
                selected = item == selected,
                onClick = { onSelected(item) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
internal fun ContactPickerScopeRow(
    title: String,
    memberCount: Int,
    selectedCount: Int,
    memberUnit: String,
    onClick: () -> Unit
) {
    var pressed by remember { mutableStateOf(false) }
    val allSelected = memberCount > 0 && selectedCount == memberCount
    Row(
        modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(rememberPressFeedbackColor(pressed))
            .responsiveTap(
                onClick = onClick,
                onPressedChange = { pressed = it }
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = MiuixTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = if (selectedCount == 0) {
                    "$memberCount $memberUnit"
                } else {
                    "已选 $selectedCount / $memberCount $memberUnit"
                },
                color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                fontSize = 12.sp
            )
        }
        SelectionMark(selected = allSelected, multiSelect = true)
    }
}

@Composable
internal fun ContactFilterRow(
    selected: ContactPickerFilter,
    enableLabels: Boolean = false,
    enableOfficials: Boolean = false,
    onSelected: (ContactPickerFilter) -> Unit
) {
    val items = buildList {
        add(ContactPickerFilter.FRIENDS)
        add(ContactPickerFilter.GROUPS)
        if (enableOfficials) add(ContactPickerFilter.OFFICIALS)
        if (enableLabels) add(ContactPickerFilter.LABELS)
        add(ContactPickerFilter.ALL)
    }
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items.forEach { item ->
            ContactFilterChip(
                text = item.label,
                selected = item == selected,
                onClick = { onSelected(item) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
internal fun FavoritePickerFilterRow(
    selected: FavoritePickerFilter,
    onSelected: (FavoritePickerFilter) -> Unit
) {
    val rows = listOf(
        listOf(FavoritePickerFilter.IMAGE, FavoritePickerFilter.TEXT, FavoritePickerFilter.VOICE),
        listOf(FavoritePickerFilter.VIDEO, FavoritePickerFilter.OTHER, FavoritePickerFilter.ALL)
    )
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp)) {
        rows.forEachIndexed { index, filters ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = if (index == 0) 0.dp else 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                filters.forEach { item ->
                    ContactFilterChip(
                        text = item.label,
                        selected = item == selected,
                        onClick = { onSelected(item) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
internal fun ContactLabelFilterRow(
    labels: List<String>,
    selected: String,
    onSelected: (String) -> Unit
) {
    if (labels.isEmpty()) {
        EmptyText("没有好友标签")
        return
    }
    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp).horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        labels.forEach { label ->
            ContactFilterChip(
                text = label,
                selected = label == selected,
                onClick = { onSelected(label) },
                modifier = Modifier.defaultMinSize(minWidth = 70.dp)
            )
        }
    }
}

@Composable
internal fun GroupChatLabelPickerRow(
    label: GroupChatLabel,
    selectedCount: Int,
    onClick: () -> Unit
) {
    var pressed by remember { mutableStateOf(false) }
    val pressFeedbackColor = rememberPressFeedbackColor(pressed)
    val allSelected = selectedCount == label.groupIds.size
    Row(
        modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(pressFeedbackColor)
            .responsiveTap(
                onClick = onClick,
                onPressedChange = { pressed = it }
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label.name,
                color = MiuixTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = if (selectedCount == 0) {
                    "${label.groupIds.size} 个群聊"
                } else {
                    "已选 $selectedCount / ${label.groupIds.size} 个群聊"
                },
                color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                fontSize = 12.sp
            )
        }
        SelectionMark(selected = allSelected, multiSelect = true)
    }
}

@Composable
internal fun ContactFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var pressed by remember { mutableStateOf(false) }
    var visualPressed by remember { mutableStateOf(false) }
    LaunchedEffect(pressed) {
        if (pressed) {
            visualPressed = true
        } else {
            delay(100L)
            visualPressed = false
        }
    }
    val scale by animateFloatAsState(
        targetValue = if (visualPressed) 0.96f else 1f,
        animationSpec = tween(durationMillis = if (pressed) 60 else 145),
        label = "ContactFilterPressScale"
    )
    val background = if (selected) {
        MiuixTheme.colorScheme.primary.copy(alpha = if (visualPressed) 0.88f else 1f)
    } else {
        MiuixTheme.colorScheme.onSurface.copy(alpha = if (visualPressed) 0.18f else 0.06f)
    }
    val textColor = if (selected) {
        Color.White
    } else {
        MiuixTheme.colorScheme.onSurfaceVariantSummary
    }
    Box(
        modifier = modifier
            .height(38.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(RoundedCornerShape(12.dp))
            .background(background)
            .responsiveTap(
                onClick = onClick,
                onPressedChange = { pressed = it }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 14.sp,
            fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            softWrap = false
        )
    }
}

@Composable
internal fun PickerSectionHeader(
    text: String
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(start = 20.dp, top = 10.dp, end = 20.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = text,
            color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
internal fun ActionRow(title: String, summary: String, onClick: () -> Unit) {
    SelectRow(title = title, summary = summary, onClick = onClick)
}

@Composable
internal fun SelectRow(title: String, summary: String, onClick: () -> Unit) {
    var pressed by remember { mutableStateOf(false) }
    val pressFeedbackColor = rememberPressFeedbackColor(pressed)
    Row(
        modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(pressFeedbackColor)
            .responsiveTap(
                onClick = onClick,
                onPressedChange = { pressed = it }
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = MiuixTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
            if (summary.isNotBlank()) {
                Text(text = summary, color = MiuixTheme.colorScheme.onSurfaceVariantSummary, fontSize = 12.sp)
            }
        }
        Text(text = "›", color = MiuixTheme.colorScheme.onSurfaceVariantSummary, fontSize = 22.sp)
    }
}

@Composable
internal fun MessageBlockLabelListCard(
    label: MessageBlockLabelOption,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 3.dp),
        cornerRadius = 14.dp
    ) {
        var pressed by remember { mutableStateOf(false) }
        val pressFeedbackColor = rememberPressFeedbackColor(pressed)
        Row(
            modifier = Modifier.fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(pressFeedbackColor)
                .responsiveTap(
                    onClick = onClick,
                    onPressedChange = { pressed = it }
                )
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MiuixTheme.colorScheme.secondaryVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label.name.take(1).ifEmpty { "签" },
                    color = MiuixTheme.colorScheme.onSecondaryVariant,
                    fontWeight = FontWeight.Bold
                )
            }
            Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                Text(text = label.name, color = MiuixTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
                Text(
                    text = "${label.contactIds.size} 人",
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    fontSize = 12.sp
                )
            }
            Text(text = "›", color = MiuixTheme.colorScheme.onSurfaceVariantSummary, fontSize = 22.sp)
        }
    }
}

@Composable
internal fun MessageBlockContactListCard(
    row: MessageBlockContactOption,
    selected: Boolean,
    onClick: () -> Unit
) {
    val option = row.contact
    val summary = if (row.labelNames.isEmpty()) {
        option.id
    } else {
        option.id + " · " + row.labelNames.joinToString(" / ")
    }
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 3.dp),
        cornerRadius = 14.dp
    ) {
        MessageBlockContactRow(
            option = option,
            summary = summary,
            selected = selected,
            onClick = onClick
        )
    }
}

@Composable
internal fun MessageBlockContactRow(
    option: ContactOption,
    summary: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    var pressed by remember { mutableStateOf(false) }
    val pressFeedbackColor = rememberPressFeedbackColor(pressed)
    Row(
        modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(pressFeedbackColor)
            .responsiveTap(
                onClick = onClick,
                onPressedChange = { pressed = it }
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ContactAvatar(option)
        Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
            Text(text = option.label, color = MiuixTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
            Text(text = summary, color = MiuixTheme.colorScheme.onSurfaceVariantSummary, fontSize = 12.sp)
        }
        SelectionMark(selected = selected, multiSelect = true)
    }
}

@Composable
internal fun ContactListCard(
    option: ContactOption,
    selected: Boolean,
    multiSelect: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 3.dp),
        cornerRadius = 14.dp
    ) {
        ContactRow(
            option = option,
            selected = selected,
            multiSelect = multiSelect,
            onClick = onClick
        )
    }
}

@Composable
internal fun FavoriteListCard(
    item: WeChatFavoriteItem,
    selected: Boolean,
    multiSelect: Boolean,
    onPreview: (String) -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 3.dp),
        cornerRadius = 8.dp
    ) {
        var pressed by remember { mutableStateOf(false) }
        val pressFeedbackColor = rememberPressFeedbackColor(pressed)
        Row(
            modifier = Modifier.fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(pressFeedbackColor)
                .responsiveTap(
                    onClick = onClick,
                    onPressedChange = { pressed = it }
                )
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FavoriteMediaThumbnail(item = item, onPreview = onPreview)
            Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                Text(
                    text = item.displayTitle(),
                    color = MiuixTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = item.displaySummary(),
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    fontSize = 12.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 3.dp)
                )
                if (item.tags.isNotEmpty()) {
                    Text(
                        text = "标签 · ${item.tags.joinToString(" / ")}",
                        color = MiuixTheme.colorScheme.primary,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 3.dp)
                    )
                }
            }
            SelectionMark(selected = selected, multiSelect = multiSelect)
        }
    }
}

@Composable
internal fun FavoriteMediaThumbnail(
    item: WeChatFavoriteItem,
    onPreview: (String) -> Unit
) {
    var previewPath by remember(item.localId) { mutableStateOf("") }
    var thumbnail by remember(item.localId) { mutableStateOf<ImageBitmap?>(null) }
    LaunchedEffect(item.localId, item.type) {
        if (item.type != 2 && item.type != 4) return@LaunchedEffect
        val result = withContext(Dispatchers.IO) {
            val path = WeChatApis.media()?.favorites()?.previewPath(item.localId).orEmpty()
            path to path.takeIf { it.isNotBlank() }
                ?.let { loadFavoriteThumbnail(it, item.type, 320) }
        }
        previewPath = result.first
        thumbnail = result.second
    }
    val previewModifier = if (previewPath.isNotBlank()) {
        Modifier.responsiveTap(onClick = { onPreview(previewPath) })
    } else {
        Modifier
    }
    Box(
        modifier = Modifier.size(56.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MiuixTheme.colorScheme.secondaryVariant)
            .then(previewModifier),
        contentAlignment = Alignment.Center
    ) {
        val image = thumbnail
        if (image != null) {
            Image(
                bitmap = image,
                contentDescription = if (item.type == 4) "预览视频" else "预览图片",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            if (item.type == 4) {
                Box(
                    modifier = Modifier.size(28.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.Black.copy(alpha = 0.56f)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        imageVector = NavIcons.Entertainment,
                        contentDescription = "播放",
                        colorFilter = ColorFilter.tint(Color.White),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        } else {
            Text(
                text = item.typeLabel().take(1).ifEmpty { "藏" },
                color = MiuixTheme.colorScheme.onSecondaryVariant,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}

internal fun loadFavoriteThumbnail(path: String, type: Int, maxDimension: Int): ImageBitmap? {
    if (!File(path).isFile) return null
    return if (type == 4) {
        @Suppress("DEPRECATION")
        ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MINI_KIND)?.asImageBitmap()
    } else {
        decodeFavoriteImage(path, maxDimension)
    }
}

internal fun decodeFavoriteImage(path: String, maxDimension: Int): ImageBitmap? {
    val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    BitmapFactory.decodeFile(path, bounds)
    if (bounds.outWidth <= 0 || bounds.outHeight <= 0) return null
    var sample = 1
    while (bounds.outWidth / sample > maxDimension * 2 || bounds.outHeight / sample > maxDimension * 2) {
        sample *= 2
    }
    return BitmapFactory.decodeFile(path, BitmapFactory.Options().apply { inSampleSize = sample })
        ?.asImageBitmap()
}

@Composable
internal fun FavoriteMediaPreviewDialog(
    media: FavoriteMediaPreview,
    onClose: () -> Unit
) {
    var image by remember(media.path) { mutableStateOf<ImageBitmap?>(null) }
    var videoView by remember(media.path) { mutableStateOf<VideoView?>(null) }
    val previewHeight = (LocalConfiguration.current.screenHeightDp.dp * 0.62f).coerceAtMost(560.dp)
    LaunchedEffect(media.path, media.item.type) {
        if (media.item.type == 2) {
            image = withContext(Dispatchers.IO) { decodeFavoriteImage(media.path, 2048) }
        }
    }
    DisposableEffect(media.path) {
        onDispose { videoView?.stopPlayback() }
    }
    WindowDialog(
        show = true,
        title = "${media.item.typeLabel()}预览",
        onDismissRequest = onClose,
        content = {
            Column(modifier = Modifier.fillMaxWidth()) {
                if (media.item.type == 4) {
                    AndroidView(
                        factory = { context ->
                            VideoView(context).also { view ->
                                val controller = MediaController(context)
                                controller.setAnchorView(view)
                                view.setMediaController(controller)
                                view.setVideoPath(media.path)
                                view.setOnPreparedListener { view.start() }
                                videoView = view
                            }
                        },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 240.dp, max = previewHeight)
                    )
                } else {
                    val bitmap = image
                    if (bitmap == null) {
                        EmptyText("正在载入预览...")
                    } else {
                        Image(
                            bitmap = bitmap,
                            contentDescription = "图片预览",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.fillMaxWidth().heightIn(min = 220.dp, max = previewHeight)
                        )
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

@Composable
internal fun ContactRow(
    option: ContactOption,
    selected: Boolean,
    multiSelect: Boolean,
    onClick: () -> Unit
) {
    var pressed by remember { mutableStateOf(false) }
    val pressFeedbackColor = rememberPressFeedbackColor(pressed)
    Row(
        modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(pressFeedbackColor)
            .responsiveTap(
                onClick = onClick,
                onPressedChange = { pressed = it }
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ContactAvatar(option)
        Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
            Text(text = option.label, color = MiuixTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
            val summary = buildList {
                add(option.id)
                option.labels.takeIf { it.isNotEmpty() }?.let { add(it.joinToString(" / ")) }
                option.extraSummary.takeIf { it.isNotBlank() }?.let(::add)
            }.joinToString(" · ")
            Text(text = summary, color = MiuixTheme.colorScheme.onSurfaceVariantSummary, fontSize = 12.sp)
        }
        SelectionMark(selected = selected, multiSelect = multiSelect)
    }
}

@Composable
internal fun ContactAvatar(option: ContactOption) {
    val context = LocalContext.current
    var bitmap by remember(option.id, option.avatarUrl, option.avatarBackupUrl) { mutableStateOf<ImageBitmap?>(null) }
    LaunchedEffect(option.id, option.avatarUrl, option.avatarBackupUrl) {
        bitmap = loadAvatarBitmap(option.id, option.avatarUrl, option.avatarBackupUrl)
    }
    val image = bitmap
    val avatarBg = if (image == null && option.group) Color(0xFF2E7DFF) else MiuixTheme.colorScheme.secondaryVariant
    val avatarTextColor = if (image == null && option.group) Color.White else MiuixTheme.colorScheme.onSecondaryVariant
    val avatarShape = if (RoundAvatarSettings.enabled(context)) {
        RoundedCornerShape((42f * RoundAvatarSettings.radiusFactor(context)).dp)
    } else {
        RoundedCornerShape(12.dp)
    }
    Box(
        modifier = Modifier.size(42.dp)
            .clip(avatarShape)
            .background(avatarBg),
        contentAlignment = Alignment.Center
    ) {
        if (image != null) {
            Image(
                bitmap = image,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Text(
                text = if (option.group) "群" else option.label.take(1).ifEmpty { "友" },
                color = avatarTextColor,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
internal fun OptionChoiceRow(
    item: OptionItem,
    selected: Boolean,
    onClick: () -> Unit
) {
    var pressed by remember { mutableStateOf(false) }
    val pressFeedbackColor = rememberPressFeedbackColor(pressed)
    Row(
        modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(pressFeedbackColor)
            .responsiveTap(
                onClick = onClick,
                onPressedChange = { pressed = it }
            )
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = item.label, color = MiuixTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
            if (item.summary.isNotEmpty()) {
                Text(text = item.summary, color = MiuixTheme.colorScheme.onSurfaceVariantSummary, fontSize = 12.sp)
            }
        }
        SelectionMark(selected = selected, multiSelect = false)
    }
}

@Composable
internal fun SelectionMark(selected: Boolean, multiSelect: Boolean) {
    Box(
        modifier = Modifier.size(30.dp),
        contentAlignment = Alignment.Center
    ) {
        Checkbox(
            modifier = Modifier.size(22.dp),
            state = if (selected) ToggleableState.On else ToggleableState.Off,
            onClick = null
        )
    }
}

@Composable
internal fun BottomActionBar(
    primaryText: String,
    onPrimaryClick: () -> Unit,
    secondaryText: String? = null,
    onSecondaryClick: (() -> Unit)? = null,
    middleText: String? = null,
    onMiddleClick: (() -> Unit)? = null
) {
    val backAction = when {
        secondaryText == "取消" && onSecondaryClick != null -> onSecondaryClick
        middleText == "取消" && onMiddleClick != null -> onMiddleClick
        primaryText == "取消" -> onPrimaryClick
        secondaryText == "返回" && onSecondaryClick != null -> onSecondaryClick
        middleText == "返回" && onMiddleClick != null -> onMiddleClick
        primaryText == "返回" -> onPrimaryClick
        secondaryText == "关闭" && onSecondaryClick != null -> onSecondaryClick
        middleText == "关闭" && onMiddleClick != null -> onMiddleClick
        primaryText == "关闭" -> onPrimaryClick
        else -> null
    }
    RegisterSettingsBackHandler(backAction)
    Row(
        modifier = Modifier.fillMaxWidth()
            .background(MiuixTheme.colorScheme.background.copy(alpha = 0.92f))
            .padding(start = 12.dp, top = 10.dp, end = 12.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (secondaryText != null && onSecondaryClick != null) {
            BottomBarButton(
                text = secondaryText,
                modifier = Modifier.weight(1f),
                filled = false,
                onClick = onSecondaryClick
            )
        }
        if (middleText != null && onMiddleClick != null) {
            BottomBarButton(
                text = middleText,
                modifier = Modifier.weight(1f),
                filled = false,
                onClick = onMiddleClick
            )
        }
        BottomBarButton(
            text = primaryText,
            modifier = Modifier.weight(1f),
            filled = true,
            onClick = onPrimaryClick
        )
    }
}

@Composable
internal fun navigationButtonBottomInset(): Dp {
    val context = LocalContext.current
    val density = LocalDensity.current
    val gestureNavigation = remember(context) { isGestureNavigationMode(context) }
    if (gestureNavigation) return 0.dp
    val navigationBottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val resourceBottom = remember(context, density) {
        with(density) { navigationBarHeightPx(context).toDp() }
    }
    val bottomInset = if (navigationBottom > resourceBottom) navigationBottom else resourceBottom
    return if (bottomInset >= NAVIGATION_BUTTON_MIN_INSET) bottomInset else 0.dp
}

internal fun navigationBarHeightPx(context: Context): Int {
    val resources = context.resources
    val id = resources.getIdentifier("navigation_bar_height", "dimen", "android")
    return if (id > 0) {
        runCatching { resources.getDimensionPixelSize(id) }.getOrDefault(0)
    } else {
        0
    }
}

internal fun isGestureNavigationMode(context: Context): Boolean {
    val resolver = context.contentResolver
    val androidMode = runCatching {
        Settings.Secure.getInt(resolver, "navigation_mode", -1)
    }.getOrDefault(-1)
    if (androidMode == 2) return true
    val miuiGlobalGesture = runCatching {
        Settings.Global.getInt(resolver, "force_fsg_nav_bar", 0)
    }.getOrDefault(0)
    val miuiSecureGesture = runCatching {
        Settings.Secure.getInt(resolver, "force_fsg_nav_bar", 0)
    }.getOrDefault(0)
    return miuiGlobalGesture == 1 || miuiSecureGesture == 1
}

@Composable
internal fun BottomBarButton(text: String, modifier: Modifier, filled: Boolean, onClick: () -> Unit) {
    val bg = if (filled) MiuixTheme.colorScheme.primary else MiuixTheme.colorScheme.secondaryVariant
    val fg = if (filled) Color.White else MiuixTheme.colorScheme.onSecondaryVariant
    Text(
        modifier = modifier.clip(RoundedCornerShape(14.dp))
            .background(bg)
            .clickable(onClick = onClick)
            .padding(vertical = 13.dp),
        text = text,
        color = fg,
        fontWeight = FontWeight.Medium,
        textAlign = TextAlign.Center
    )
}

@Composable
internal fun StatsCard(sp: SharedPreferences) {
    SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "红包记录")
    SettingsCard {
        InfoRow("已抢红包", "${sp.getInt(RedPacketSettings.KEY_STATS_COUNT, 0)} 个")
        InsetDivider()
        InfoRow("累计金额", String.format("%.2f 元", sp.getInt(RedPacketSettings.KEY_STATS_AMOUNT, 0) / 100.0))
        InsetDivider()
        InfoRow("今日抢到", "${sp.getInt(RedPacketSettings.KEY_STATS_TODAY, 0)} 个")
        InsetDivider()
        InfoRow("失败次数", "${sp.getInt(RedPacketSettings.KEY_STATS_FAILED, 0)} 次")
    }
}

@Composable
internal fun ConfigBackupCard(
    context: Context,
    onImported: () -> Unit
) {
    val activity = context as? Activity
    SettingsCard {
        ActionRow(
            title = "导出配置",
            summary = "",
            onClick = {
                if (activity == null) {
                    Toast.makeText(context, "当前页面无法打开文件管理器", Toast.LENGTH_SHORT).show()
                } else {
                    ConfigImportExportBridge.launchExport(activity)
                }
            }
        )
        InsetDivider()
        ActionRow(
            title = "导入配置",
            summary = "",
            onClick = {
                if (activity == null) {
                    Toast.makeText(context, "当前页面无法打开文件管理器", Toast.LENGTH_SHORT).show()
                } else {
                    ConfigImportExportBridge.launchImport(activity, onImported)
                }
            }
        )
    }
}

@Composable
internal fun GroupLinksCard(context: Context) {
    SettingsCard {
        InfoRow(label = "Telegram Channel", value = "Hchat_ci", onClick = {
            openExternalLink(context, "https://t.me/Hchat_ci")
        })
        InsetDivider()
        InfoRow(label = "Telegram Group", value = "Hchat_Group", onClick = {
            openExternalLink(context, "https://t.me/Hchat_Group")
        })
    }
}

@Composable
internal fun ThanksCard(context: Context) {
    SettingsCard {
        InfoRow(label = "KavaRef", value = "HighCapable", onClick = {
            openExternalLink(context, "https://github.com/HighCapable/KavaRef")
        })
        InsetDivider()
        InfoRow(label = "DexKit", value = "LuckyPray", onClick = {
            openExternalLink(context, "https://github.com/LuckyPray/DexKit")
        })
        InsetDivider()
        InfoRow(label = "FastKV", value = "BillyWei01", onClick = {
            openExternalLink(context, "https://github.com/BillyWei01/FastKV")
        })
        InsetDivider()
        InfoRow(label = "WeChat Pad", value = "lovejiuwu", onClick = {
            openExternalLink(context, "https://github.com/Xposed-Modules-Repo/top.hookvip.wxtablet")
        })
        InsetDivider()
        InfoRow(label = "LSPosed", value = "LSPosed", onClick = {
            openExternalLink(context, "https://github.com/LSPosed/LSPosed")
        })
        InsetDivider()
        InfoRow(label = "Miuix", value = "YuKongA", onClick = {
            openExternalLink(context, "https://github.com/compose-miuix-ui/miuix")
        })
        InsetDivider()
        InfoRow(label = "BeanShell-Android", value = "CopyLibs", onClick = {
            openExternalLink(context, "https://github.com/CopyLibs/BeanShell-Android")
        })
        InsetDivider()
        InfoRow(label = "Silk Codec", value = "YunJavaPro", onClick = {
            openExternalLink(context, "https://github.com/YunJavaPro/Silk-Codec-Android")
        })
    }
}

@Composable
internal fun InfoRow(label: String, value: String, onClick: (() -> Unit)? = null) {
    val modifier = if (onClick != null) {
        Modifier.fillMaxWidth().clickable(onClick = onClick).padding(horizontal = 16.dp, vertical = 15.dp)
    } else {
        Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 15.dp)
    }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = MiuixTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium
        )
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = value, color = MiuixTheme.colorScheme.onSurfaceVariantSummary, textAlign = TextAlign.End)
            if (onClick != null) {
                Text(
                    text = "›",
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    fontSize = 22.sp,
                    modifier = Modifier.padding(start = 6.dp)
                )
            }
        }
    }
}

@Composable
internal fun XiaozhiMcpStatusRow(status: XiaozhiMcpStatus) {
    val dotColor = if (status.connected) Color(0xFF20C05C) else Color(0xFF8C8C8C)
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 15.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "连接状态",
            color = MiuixTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium
        )
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(dotColor)
            )
            Text(
                text = status.label,
                color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                textAlign = TextAlign.End,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

internal fun openExternalLink(context: Context, url: String) {
    runCatching {
        context.startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse(url))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }
}

internal fun extractFirstHttpUrl(text: String): String? {
    val matcher = android.util.Patterns.WEB_URL.matcher(text)
    while (matcher.find()) {
        val url = matcher.group().orEmpty()
        if (url.startsWith("http://", ignoreCase = true) || url.startsWith("https://", ignoreCase = true)) {
            return url
        }
    }
    return null
}

@Composable
internal fun EmptyText(text: String) {
    Text(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 28.dp),
        text = text,
        color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
        textAlign = TextAlign.Center
    )
}

@Composable
internal fun InsetDivider(start: Dp = 16.dp) {
    Box(
        modifier = Modifier
            .padding(start = start)
            .fillMaxWidth()
            .height(Dp.Hairline)
            .background(MiuixTheme.colorScheme.dividerLine)
    )
}

internal class EmbeddedComposeOwner : LifecycleOwner, SavedStateRegistryOwner, ViewModelStoreOwner,
    NavigationEventDispatcherOwner {
    private val lifecycleRegistry = LifecycleRegistry(this)
    private val savedStateRegistryController = SavedStateRegistryController.create(this)
    private val store = ViewModelStore()
    private val navigationDispatcher = NavigationEventDispatcher()
    private val composeRuntime = lazy(LazyThreadSafetyMode.NONE) {
        EmbeddedComposeRuntime(lifecycleRegistry)
    }
    private var restored = false

    override val lifecycle: Lifecycle
        get() = lifecycleRegistry

    override val savedStateRegistry: SavedStateRegistry
        get() = savedStateRegistryController.savedStateRegistry

    override val viewModelStore: ViewModelStore
        get() = store

    override val navigationEventDispatcher: NavigationEventDispatcher
        get() = navigationDispatcher

    fun install(view: View) {
        EmbeddedComposeOwnerInstaller.install(view, this, this, this, this)
    }

    fun clear(view: View) {
        EmbeddedComposeOwnerInstaller.clear(view)
    }

    fun installComposition(view: ComposeView) {
        composeRuntime.value.install(view)
    }

    fun attach() {
        if (!restored) {
            savedStateRegistryController.performRestore(Bundle.EMPTY)
            restored = true
        }
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
        lifecycleRegistry.currentState = Lifecycle.State.STARTED
        lifecycleRegistry.currentState = Lifecycle.State.RESUMED
    }

    fun destroy() {
        if (lifecycleRegistry.currentState != Lifecycle.State.DESTROYED) {
            lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
        }
        if (composeRuntime.isInitialized()) composeRuntime.value.destroy()
        navigationDispatcher.dispose()
        store.clear()
    }
}

internal fun firstTemplate(value: String): String {
    return value.split('|').firstOrNull { it.trim().isNotEmpty() }?.trim() ?: value.trim()
}

internal fun normalizeAntiRecallNoticeText(value: String): String {
    return if (value == AntiRecallSettings.LEGACY_NOTICE_TEXT) {
        AntiRecallSettings.DEFAULT_NOTICE_TEXT
    } else {
        value
    }
}

internal enum class ContactPickerMode {
    FRIENDS,
    GROUPS,
    BOTH,
    ALL_CHATS
}

internal fun ContactPickerMode.supportsGroups(): Boolean {
    return this == ContactPickerMode.GROUPS ||
        this == ContactPickerMode.BOTH ||
        this == ContactPickerMode.ALL_CHATS
}

internal enum class ContactPickerFilter(
    val label: String,
    val title: String
) {
    FRIENDS("好友", "好友"),
    GROUPS("群聊", "群聊"),
    OFFICIALS("公众号", "公众号"),
    LABELS("标签", "标签好友"),
    ALL("全部", "全部")
}

internal enum class ContactPickerScopeTab(val label: String) {
    FRIENDS("好友"),
    LABELS("标签"),
    CONVERSATION_GROUPS("聊天分组")
}

internal data class ContactPickerScope(
    val name: String,
    val memberIds: Set<String>
)

internal enum class ConversationRuleCategory(val label: String) {
    OFFICIAL("公众号"),
    GROUP("群聊"),
    FRIEND("好友"),
    ALL("全部")
}

internal enum class MessageBlockContactKind {
    FRIEND,
    GROUP,
    OFFICIAL
}

internal enum class MessageBlockContactFilter(
    val label: String,
    val title: String
) {
    FRIENDS("好友", "好友"),
    GROUPS("群聊", "群聊"),
    OFFICIALS("公众号", "公众号"),
    LABELS("标签", "标签好友"),
    ALL("全部", "全部")
}

internal data class ContactPickerRequest(
    val title: String,
    val mode: ContactPickerMode,
    val multiSelect: Boolean,
    val existingValue: String,
    val onValue: (String) -> Unit,
    val enableLabels: Boolean = false,
    val enableGroupLabels: Boolean = true,
    val enableScopeSelection: Boolean = false,
    val singleConfirmText: String = "发送"
)

internal enum class AutoMessageForwardRoute {
    MAIN,
    RULES,
    EDITOR,
    CONTACTS,
    MEMBERS,
    TYPES,
    REPLACEMENTS
}

internal enum class AutoMessageForwardContactField {
    SOURCES,
    TARGETS
}

internal data class FavoritePickerRequest(
    val title: String,
    val existingValue: String,
    val onValue: (String) -> Unit,
    val multiSelect: Boolean = true,
    val delimiter: String = "|"
)

internal enum class FavoritePickerFilter(val label: String) {
    IMAGE("图片"),
    TEXT("文字"),
    VOICE("语音"),
    VIDEO("视频"),
    OTHER("其他"),
    ALL("全部");

    fun matches(type: Int): Boolean = when (this) {
        IMAGE -> type == 2
        TEXT -> type == 1
        VOICE -> type == 3
        VIDEO -> type == 4
        OTHER -> type !in setOf(1, 2, 3, 4)
        ALL -> true
    }
}

internal data class FavoriteMediaPreview(
    val item: WeChatFavoriteItem,
    val path: String
)

internal data class MessageBlockContactPickerRequest(
    val title: String,
    val existingValue: String,
    val onValue: (String) -> Unit,
    val allowOfficialAccounts: Boolean = true
)

internal data class GroupMemberPickerRequest(
    val title: String,
    val existingValue: String,
    val onValue: (String) -> Unit,
    val allowedGroupIds: Set<String>? = null
)

internal data class RedPacketTemplateEditorRequest(
    val index: Int,
    val template: RedPacketRuleTemplate,
    val canDelete: Boolean
)

internal data class RedPacketBindingEditorRequest(
    val index: Int,
    val binding: RedPacketRuleBinding,
    val canDelete: Boolean
)

internal data class TransferTemplateEditorRequest(
    val index: Int,
    val template: TransferRuleTemplate,
    val canDelete: Boolean
)

internal data class TransferBindingEditorRequest(
    val index: Int,
    val binding: TransferRuleBinding,
    val canDelete: Boolean
)

internal data class MessageBlockTemplateEditorRequest(
    val index: Int,
    val template: MessageBlockTemplate,
    val canDelete: Boolean
)

internal data class MessageBlockBindingEditorRequest(
    val index: Int,
    val binding: MessageBlockBinding,
    val canDelete: Boolean
)

internal data class MessageBlockBatchBindingEditorRequest(
    val title: String,
    val bindings: List<MessageBlockBinding>
)

internal data class GroupLeaveTemplateEditorRequest(
    val index: Int,
    val template: GroupLeaveReplyTemplate,
    val canDelete: Boolean
)

internal data class MessageBlockTypeOption(
    val key: String,
    val title: String,
    val summary: String
)

internal data class MessageBlockRuleState(
    val typeAll: Boolean,
    val types: Set<String>,
    val textKeywords: String
)

internal data class OptionPickerRequest(
    val title: String,
    val options: List<OptionItem>,
    val currentValue: Int,
    val onSelected: (OptionItem) -> Unit
)

internal data class OptionItem(
    val label: String,
    val value: Int,
    val summary: String = ""
)

internal data class PopupChoice<T>(
    val label: String,
    val value: T
)

internal fun optionItems(vararg items: Pair<String, Int>): List<OptionItem> {
    return items.map { OptionItem(label = it.first, value = it.second) }
}

internal fun ringtoneDisplayName(context: Context, uri: String, mode: Int): String {
    if (uri.isBlank()) return "跟随系统"
    val parsed = try {
        Uri.parse(uri)
    } catch (_: Throwable) {
        null
    }
    if (mode == RedPacketSettings.NOTIFY_SOUND_MODE_CUSTOM) {
        queryDisplayName(context, parsed)?.let { return it }
    }
    return try {
        val ringtone = parsed?.let { RingtoneManager.getRingtone(context, it) }
        ringtone?.getTitle(context)?.takeIf { it.isNotBlank() }
            ?: queryDisplayName(context, parsed)
            ?: "已选择自定义铃声"
    } catch (_: Throwable) {
        queryDisplayName(context, parsed)
            ?: "已选择自定义铃声"
    }
}

internal fun queryDisplayName(context: Context, uri: Uri?): String? {
    if (uri == null) return null
    if ("content".equals(uri.scheme, ignoreCase = true)) {
        try {
            context.contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)
                ?.use { cursor ->
                    if (cursor.moveToFirst()) {
                        val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        if (index >= 0) {
                            return cursor.getString(index)?.takeIf { it.isNotBlank() }
                        }
                    }
                }
        } catch (_: Throwable) {
        }
    }
    return uri.lastPathSegment
        ?.let { Uri.decode(it) }
        ?.substringAfterLast('/')
        ?.substringAfterLast(':')
        ?.takeIf { it.isNotBlank() }
}

internal data class ContactOption(
    val id: String,
    val label: String,
    val group: Boolean,
    val avatarUrl: String,
    val avatarBackupUrl: String,
    val labels: List<String> = emptyList(),
    val official: Boolean = false,
    val extraSummary: String = "",
    val searchAliases: List<String> = emptyList()
)

internal fun ContactOption.matchesSearch(lower: String): Boolean {
    return lower.isEmpty() ||
        label.lowercase(Locale.US).contains(lower) ||
        id.lowercase(Locale.US).contains(lower) ||
        labels.any { it.lowercase(Locale.US).contains(lower) } ||
        extraSummary.lowercase(Locale.US).contains(lower) ||
        searchAliases.any { it.lowercase(Locale.US).contains(lower) }
}

internal data class MessageBlockContactOption(
    val contact: ContactOption,
    val kind: MessageBlockContactKind,
    val labelNames: List<String> = emptyList()
)

internal data class MessageBlockLabelOption(
    val id: String,
    val name: String,
    val contactIds: List<String>
)

internal data class MessageBlockContactData(
    val contacts: List<MessageBlockContactOption> = emptyList(),
    val labels: List<MessageBlockLabelOption> = emptyList()
)

internal data class MessageBlockInitialState(
    val templates: List<MessageBlockTemplate>,
    val bindings: List<MessageBlockBinding>,
    val shouldPersist: Boolean
)

internal fun newRedPacketTemplate(index: Int, sp: SharedPreferences): RedPacketRuleTemplate {
    val replyEnabled = sp.getBoolean(RedPacketSettings.KEY_REPLY_ENABLE, false)
    val storedReplyMode = if (replyEnabled) {
        sp.getInt(RedPacketSettings.KEY_REPLY_TYPE, RedPacketRuleConfig.REPLY_TEXT)
    } else {
        RedPacketRuleConfig.REPLY_OFF
    }
    val replySteps = loadGlobalRedPacketReplySteps(sp)
    val firstReply = replySteps.firstOrNull()
    val legacyReplyText = if (redPacketReplyUsesText(storedReplyMode)) {
        sp.getString(
            RedPacketSettings.KEY_REPLY_TEMPLATES,
            sp.getString(RedPacketSettings.KEY_REPLY_TEXT, "谢谢老板") ?: "谢谢老板"
        ) ?: "谢谢老板"
    } else {
        sp.getString(RedPacketSettings.KEY_REPLY_MEDIA_PATHS, "") ?: ""
    }
    return RedPacketRuleTemplate(
        id = System.currentTimeMillis().toString() + "_" + index,
        name = "模板 $index",
        enabled = false,
        grabMode = sp.getInt(RedPacketSettings.KEY_GRAB_MODE, RedPacketSettings.DEFAULT_GRAB_MODE),
        delayMode = redPacketDelayModeFromPrefs(sp),
        delayMs = redPacketDelayMillisFromPrefs(sp),
        randomMinMs = sp.getInt(RedPacketSettings.KEY_DELAY_RANDOM_MIN, 500).coerceAtLeast(0).toLong(),
        randomMaxMs = sp.getInt(RedPacketSettings.KEY_DELAY_RANDOM_MAX, 3000).coerceAtLeast(0).toLong(),
        skipSelf = sp.getBoolean(RedPacketSettings.KEY_SKIP_SELF, false),
        listMode = 0,
        whitelist = "",
        blacklist = "",
        keywordMode = sp.getInt(RedPacketSettings.KEY_KW_MODE, 0),
        keywords = sp.getString(RedPacketSettings.KEY_KEYWORDS, "") ?: "",
        quietEnabled = false,
        quietStartSecond = 0,
        quietEndSecond = 0,
        replyMode = firstReply?.mode ?: normalizeRedPacketReplyModeForUi(storedReplyMode),
        replyText = firstReply?.content ?: normalizeRedPacketReplyTextForUi(storedReplyMode, legacyReplyText),
        replyDelayMs = firstReply?.delayMs ?: redPacketReplyDelayMillisFromPrefs(sp),
        replyRandom = firstReply?.random ?: sp.getBoolean(RedPacketSettings.KEY_REPLY_RANDOM, false),
        replySteps = replySteps,
        groupReplySteps = loadGlobalGroupRedPacketReplySteps(sp),
        notificationConfigured = true,
        notifySystemEnabled = sp.getBoolean(RedPacketSettings.KEY_NOTIFY_SYSTEM_ENABLE, false),
        notifyToastEnabled = sp.getBoolean(RedPacketSettings.KEY_NOTIFY_TOAST_ENABLE, false),
        notifySoundEnabled = sp.getBoolean(RedPacketSettings.KEY_NOTIFY_SOUND_ENABLE, false),
        notifySoundMode = sp.getInt(RedPacketSettings.KEY_NOTIFY_SOUND_MODE, RedPacketSettings.NOTIFY_SOUND_MODE_SYSTEM),
        notifyVibrateEnabled = sp.getBoolean(RedPacketSettings.KEY_NOTIFY_VIBRATE_ENABLE, false),
        notifySoundUri = sp.getString(RedPacketSettings.KEY_NOTIFY_SOUND_URI, "") ?: "",
        notifyText = sp.getString(RedPacketSettings.KEY_NOTIFY_TEXT, "抢到红包 {amount} 元") ?: "抢到红包 {amount} 元",
        notifyToastText = sp.getString(
            RedPacketSettings.KEY_NOTIFY_TOAST_TEXT,
            sp.getString(RedPacketSettings.KEY_NOTIFY_TEXT, "抢到红包 {amount} 元") ?: "抢到红包 {amount} 元"
        ) ?: "抢到红包 {amount} 元",
        notifyFailedSystemEnabled = sp.getBoolean(RedPacketSettings.KEY_NOTIFY_FAILED_SYSTEM_ENABLE, false),
        notifyFailedToastEnabled = sp.getBoolean(RedPacketSettings.KEY_NOTIFY_FAILED_TOAST_ENABLE, false),
        notifyFailedText = sp.getString(RedPacketSettings.KEY_NOTIFY_FAILED_TEXT, "未抢到红包") ?: "未抢到红包",
        notifyFailedToastText = sp.getString(
            RedPacketSettings.KEY_NOTIFY_FAILED_TOAST_TEXT,
            sp.getString(RedPacketSettings.KEY_NOTIFY_FAILED_TEXT, "未抢到红包") ?: "未抢到红包"
        ) ?: "未抢到红包",
        announceEnabled = sp.getBoolean(RedPacketSettings.KEY_ANNOUNCE_ENABLE, false),
        announceText = sp.getString(RedPacketSettings.KEY_ANNOUNCE_TEXT, "抢到红包 {amount} 元") ?: "抢到红包 {amount} 元"
    )
}

internal fun transferReceiveAccountOptions(context: Context): List<PopupChoice<String>> {
    return listOf(
        PopupChoice("零钱", TransferReceiveAccountStore.DEFAULT_KEY),
        PopupChoice("零钱通", TransferReceiveAccountStore.LQT_KEY),
        PopupChoice("经营账户", TransferReceiveAccountStore.BUSINESS_KEY)
    ) + TransferReceiveAccountStore.list(context)
        .filter { it.available }
        .filterNot {
            val name = it.name.replace(" ", "")
            name == "零钱" || name.contains("零钱通") || name.contains("经营") || name.contains("商户")
        }
        .map { PopupChoice(it.name, it.key) }
}

internal fun loadGlobalTransferReplySteps(sp: SharedPreferences): List<RedPacketReplyStep> {
    val stored = sp.getString(AutoTransferSettings.KEY_REPLY_ITEMS, "").orEmpty()
    if (stored.isNotBlank()) return RedPacketRuleConfig.parseReplySteps(stored).map(::normalizeRedPacketReplyStepForUi)
    if (!sp.getBoolean(AutoTransferSettings.KEY_REPLY_ENABLE, false)) return emptyList()
    return RedPacketRuleConfig.legacyReplySteps(
        RedPacketRuleConfig.REPLY_TEXT,
        sp.getString(AutoTransferSettings.KEY_REPLY_TEXT, "谢谢老板") ?: "谢谢老板",
        1000L,
        false
    )
}

internal fun loadGlobalGroupTransferReplySteps(sp: SharedPreferences): List<RedPacketReplyStep> {
    if (!sp.contains(AutoTransferSettings.KEY_REPLY_GROUP_ITEMS)) {
        return loadGlobalTransferReplySteps(sp)
    }
    return RedPacketRuleConfig.parseReplySteps(
        sp.getString(AutoTransferSettings.KEY_REPLY_GROUP_ITEMS, "") ?: ""
    ).map(::normalizeRedPacketReplyStepForUi)
}

internal fun newTransferTemplate(index: Int, sp: SharedPreferences): TransferRuleTemplate {
    val minDelay = sp.getLong(AutoTransferSettings.KEY_DELAY_RANDOM_MIN, 500L).coerceIn(0L, 600000L)
    val maxDelay = sp.getLong(AutoTransferSettings.KEY_DELAY_RANDOM_MAX, 3000L).coerceIn(minDelay, 600000L)
    return TransferRuleTemplate(
        id = "transfer_${System.currentTimeMillis()}_$index",
        name = "收款模板 $index",
        enabled = true,
        delayMode = sp.getInt(AutoTransferSettings.KEY_DELAY_MODE, TransferRuleConfig.DELAY_CUSTOM),
        delayMs = sp.getLong(AutoTransferSettings.KEY_DELAY_MS, 0L).coerceIn(0L, 600000L),
        randomMinMs = minDelay,
        randomMaxMs = maxDelay,
        receiveAccount = sp.getString(AutoTransferSettings.KEY_RECEIVE_ACCOUNT, TransferReceiveAccountStore.DEFAULT_KEY)
            ?: TransferReceiveAccountStore.DEFAULT_KEY,
        listMode = sp.getInt(AutoTransferSettings.KEY_MODE, 0),
        whitelist = sp.getString(AutoTransferSettings.KEY_WHITELIST, "").orEmpty(),
        blacklist = sp.getString(AutoTransferSettings.KEY_BLACKLIST, "").orEmpty(),
        amountEnabled = sp.getBoolean(AutoTransferSettings.KEY_AMOUNT_ENABLE, false),
        amountCondition = sp.getInt(AutoTransferSettings.KEY_AMOUNT_COND, 1),
        amountValue = sp.getString(AutoTransferSettings.KEY_AMOUNT_VALUE, "0") ?: "0",
        amountAction = sp.getInt(AutoTransferSettings.KEY_AMOUNT_ACTION, 0),
        keywordMode = sp.getInt(AutoTransferSettings.KEY_KEYWORD_MODE, 0),
        keywords = sp.getString(AutoTransferSettings.KEY_KEYWORDS, "").orEmpty(),
        quietEnabled = sp.getBoolean(AutoTransferSettings.KEY_QUIET_ENABLE, false),
        quietStartSecond = sp.getInt(AutoTransferSettings.KEY_QUIET_START_SECOND, 0),
        quietEndSecond = sp.getInt(AutoTransferSettings.KEY_QUIET_END_SECOND, 0),
        refundRejected = sp.getBoolean(AutoTransferSettings.KEY_REFUND_REJECTED, false),
        replySteps = loadGlobalTransferReplySteps(sp),
        groupReplySteps = loadGlobalGroupTransferReplySteps(sp),
        notificationConfigured = true,
        notifySystemEnabled = sp.getBoolean(AutoTransferSettings.KEY_NOTIFY_SYSTEM_ENABLE, false),
        notifyToastEnabled = sp.getBoolean(AutoTransferSettings.KEY_NOTIFY_TOAST_ENABLE, false),
        notifySoundEnabled = sp.getBoolean(AutoTransferSettings.KEY_NOTIFY_SOUND_ENABLE, false),
        notifySoundMode = sp.getInt(AutoTransferSettings.KEY_NOTIFY_SOUND_MODE, AutoTransferSettings.NOTIFY_SOUND_MODE_SYSTEM),
        notifyVibrateEnabled = sp.getBoolean(AutoTransferSettings.KEY_NOTIFY_VIBRATE_ENABLE, false),
        notifySoundUri = sp.getString(AutoTransferSettings.KEY_NOTIFY_SOUND_URI, "").orEmpty(),
        notifyText = sp.getString(AutoTransferSettings.KEY_NOTIFY_TEXT, "已收款 {amount} 元") ?: "已收款 {amount} 元",
        notifyToastText = sp.getString(AutoTransferSettings.KEY_NOTIFY_TOAST_TEXT, "已收款 {amount} 元") ?: "已收款 {amount} 元",
        announceEnabled = sp.getBoolean(AutoTransferSettings.KEY_ANNOUNCE_ENABLE, false),
        announceText = sp.getString(AutoTransferSettings.KEY_ANNOUNCE_TEXT, "收到转账 {amount} 元") ?: "收到转账 {amount} 元"
    )
}

internal fun describeTransferDefault(id: String, templates: List<TransferRuleTemplate>): String {
    if (id.isBlank()) return "旧版全局设置"
    return templates.firstOrNull { it.id == id }?.name ?: "模板不存在，使用全局设置"
}

internal fun describeTransferTemplate(value: TransferRuleTemplate): String {
    val state = if (value.enabled) "启用" else "关闭"
    val delay = when (value.delayMode) {
        TransferRuleConfig.DELAY_RANDOM -> "随机 ${value.randomMinMs}-${value.randomMaxMs}ms"
        TransferRuleConfig.DELAY_CUSTOM -> "延迟 ${value.delayMs}ms"
        else -> "无延迟"
    }
    val amount = if (value.amountEnabled) {
        "金额${transferAmountConditionLabel(value.amountCondition)}${value.amountValue}"
    } else null
    val quiet = if (value.quietEnabled) {
        "禁收 ${formatRedPacketSecond(value.quietStartSecond)}-${formatRedPacketSecond(value.quietEndSecond)}"
    } else null
    return listOfNotNull(
        state,
        delay,
        transferKeywordModeLabel(value.keywordMode),
        amount,
        quiet,
        "私聊 ${describeRedPacketReplySteps(value.replySteps)}",
        "群聊 ${describeRedPacketReplySteps(value.groupReplySteps ?: value.replySteps)}"
    ).joinToString(" · ")
}

internal fun describeTransferBinding(value: TransferRuleBinding, templates: List<TransferRuleTemplate>): String {
    val state = if (value.enabled) "启用" else "关闭"
    val template = templates.firstOrNull { it.id == value.templateId }?.name ?: "跟随默认规则"
    return "$state · $template · ${value.targetId}"
}

internal fun transferContactLabel(id: String): String = runCatching {
    WeChatApis.contact().contacts()?.getDisplayName(id).orEmpty().ifBlank { id }
}.getOrDefault(id)

internal fun upsertTransferBindings(
    current: List<TransferRuleBinding>,
    additions: List<TransferRuleBinding>
): List<TransferRuleBinding> {
    val result = linkedMapOf<String, TransferRuleBinding>()
    current.forEach { if (it.targetId.isNotBlank()) result[it.targetId.trim()] = it.copy(id = it.targetId.trim()) }
    additions.forEach {
        val id = it.targetId.trim()
        if (id.isNotBlank()) result[id] = it.copy(id = id, targetId = id, label = it.label.ifBlank { transferContactLabel(id) })
    }
    return result.values.toList()
}

internal fun transferDelayModeLabel(value: Int): String = when (value) {
    TransferRuleConfig.DELAY_RANDOM -> "随机延迟"
    TransferRuleConfig.DELAY_CUSTOM -> "自定义延迟"
    else -> "无延迟"
}

internal fun transferListModeLabel(value: Int): String = when (value) {
    1 -> "只接收白名单"
    2 -> "拒收黑名单"
    else -> "全部接收"
}

internal fun transferAmountConditionLabel(value: Int): String = when (value) {
    0 -> "大于"
    2 -> "等于"
    else -> "小于"
}

internal fun transferAmountActionLabel(value: Int): String = if (value == 1) "仅接收满足条件" else "拒收/忽略"

internal fun transferKeywordModeLabel(value: Int): String = when (value) {
    1 -> "必须包含关键词"
    2 -> "包含则拒收"
    else -> "不启用"
}

internal fun loadGlobalRedPacketReplySteps(sp: SharedPreferences): List<RedPacketReplyStep> {
    val stored = sp.getString(RedPacketSettings.KEY_REPLY_ITEMS, "") ?: ""
    if (stored.isNotBlank()) {
        return RedPacketRuleConfig.parseReplySteps(stored).map { normalizeRedPacketReplyStepForUi(it) }
    }
    val replyEnabled = sp.getBoolean(RedPacketSettings.KEY_REPLY_ENABLE, false)
    if (!replyEnabled) return emptyList()
    val replyMode = sp.getInt(RedPacketSettings.KEY_REPLY_TYPE, RedPacketRuleConfig.REPLY_TEXT)
    val content = if (redPacketReplyUsesText(replyMode)) {
        sp.getString(
            RedPacketSettings.KEY_REPLY_TEMPLATES,
            sp.getString(RedPacketSettings.KEY_REPLY_TEXT, "谢谢老板") ?: "谢谢老板"
        ) ?: "谢谢老板"
    } else {
        sp.getString(RedPacketSettings.KEY_REPLY_MEDIA_PATHS, "") ?: ""
    }
    return normalizeRedPacketReplyStepsForUi(
        RedPacketRuleConfig.legacyReplySteps(
            replyMode,
            content,
            redPacketReplyDelayMillisFromPrefs(sp),
            sp.getBoolean(RedPacketSettings.KEY_REPLY_RANDOM, false)
        ),
        RedPacketRuleConfig.REPLY_OFF,
        "",
        0L,
        false
    )
}

internal fun loadGlobalGroupRedPacketReplySteps(sp: SharedPreferences): List<RedPacketReplyStep> {
    if (!sp.contains(RedPacketSettings.KEY_REPLY_GROUP_ITEMS)) {
        return loadGlobalRedPacketReplySteps(sp)
    }
    return RedPacketRuleConfig.parseReplySteps(
        sp.getString(RedPacketSettings.KEY_REPLY_GROUP_ITEMS, "") ?: ""
    ).map(::normalizeRedPacketReplyStepForUi)
}

internal fun redPacketDelayMillisFromPrefs(sp: SharedPreferences): Long {
    val value = sp.getInt(RedPacketSettings.KEY_DELAY_VALUE, 0).coerceAtLeast(0)
    return if (sp.getInt(RedPacketSettings.KEY_DELAY_UNIT, 0) == 1) value * 1000L else value.toLong()
}

internal fun redPacketDelayModeFromPrefs(sp: SharedPreferences): Int {
    return when (val storedMode = sp.getInt(RedPacketSettings.KEY_DELAY_MODE, Int.MIN_VALUE)) {
        RedPacketRuleConfig.DELAY_FIXED,
        RedPacketRuleConfig.DELAY_CUSTOM,
        RedPacketRuleConfig.DELAY_RANDOM -> storedMode
        else -> if (redPacketDelayMillisFromPrefs(sp) > 0L) {
            RedPacketRuleConfig.DELAY_CUSTOM
        } else {
            RedPacketRuleConfig.DELAY_FIXED
        }
    }
}

internal fun redPacketReplyDelayMillisFromPrefs(sp: SharedPreferences): Long {
    if (!sp.getBoolean(RedPacketSettings.KEY_REPLY_CUSTOM_ENABLE, false)) return 0L
    val value = sp.getInt(RedPacketSettings.KEY_REPLY_DELAY_VALUE, 1).coerceAtLeast(0)
    return if (sp.getInt(RedPacketSettings.KEY_REPLY_DELAY_UNIT, 1) == 1) value * 1000L else value.toLong()
}

internal fun normalizeRedPacketDelayModeForUi(template: RedPacketRuleTemplate): Int {
    return when (template.delayMode) {
        RedPacketRuleConfig.DELAY_RANDOM -> RedPacketRuleConfig.DELAY_RANDOM
        RedPacketRuleConfig.DELAY_CUSTOM -> RedPacketRuleConfig.DELAY_CUSTOM
        else -> if (template.delayMs > 0L) RedPacketRuleConfig.DELAY_CUSTOM else RedPacketRuleConfig.DELAY_FIXED
    }
}

internal fun redPacketDelayModeLabel(mode: Int): String {
    return when (mode) {
        RedPacketRuleConfig.DELAY_CUSTOM -> "自定义延迟"
        RedPacketRuleConfig.DELAY_RANDOM -> "随机延迟"
        else -> "无延迟"
    }
}

internal fun redPacketDelayModeOptions(includeRandom: Boolean): List<OptionItem> {
    val options = mutableListOf(
        OptionItem("无延迟", RedPacketRuleConfig.DELAY_FIXED),
        OptionItem("自定义延迟", RedPacketRuleConfig.DELAY_CUSTOM)
    )
    if (includeRandom) options += OptionItem("随机延迟", RedPacketRuleConfig.DELAY_RANDOM)
    return options
}

internal fun describeRedPacketDefaultTemplate(
    defaultTemplateId: String,
    templates: List<RedPacketRuleTemplate>
): String {
    if (defaultTemplateId.isBlank()) return "旧版全局设置"
    val template = templates.firstOrNull { it.id == defaultTemplateId } ?: return "模板不存在，当前会回到旧版全局设置"
    return template.name.ifBlank { defaultTemplateId }
}

internal fun describeRedPacketTemplate(template: RedPacketRuleTemplate): String {
    val state = if (template.enabled) "启用" else "关闭"
    val mode = if (template.grabMode == 1) "静默" else "打开页面"
    val delay = when (normalizeRedPacketDelayModeForUi(template)) {
        RedPacketRuleConfig.DELAY_RANDOM -> {
            val minDelay = template.randomMinMs.coerceAtLeast(0L)
            val maxDelay = template.randomMaxMs.coerceAtLeast(minDelay)
            "随机 $minDelay-${maxDelay}ms"
        }
        RedPacketRuleConfig.DELAY_CUSTOM -> "延迟 ${template.delayMs.coerceAtLeast(0L)}ms"
        else -> "无延迟"
    }
    val keyword = when (template.keywordMode) {
        1 -> "只抢关键词"
        2 -> "屏蔽关键词"
        else -> "不限关键词"
    }
    val quiet = if (template.quietEnabled) {
        "禁抢 ${formatRedPacketSecond(template.quietStartSecond)}-${formatRedPacketSecond(template.quietEndSecond)}"
    } else {
        null
    }
    val privateReply = describeRedPacketReplySteps(
        normalizeRedPacketReplyStepsForUi(
            template.replySteps,
            template.replyMode,
            template.replyText,
            template.replyDelayMs,
            template.replyRandom
        )
    )
    val groupReply = describeRedPacketReplySteps(
        template.groupReplySteps
            ?.map(::normalizeRedPacketReplyStepForUi)
            ?: normalizeRedPacketReplyStepsForUi(
                template.replySteps,
                template.replyMode,
                template.replyText,
                template.replyDelayMs,
                template.replyRandom
            )
    )
    val notify = describeRedPacketTemplateNotify(template)
    return listOfNotNull(
        state,
        mode,
        delay,
        keyword,
        quiet,
        "私聊 $privateReply",
        "群聊 $groupReply",
        notify
    ).joinToString(" · ")
}

internal fun describeRedPacketTemplateNotify(template: RedPacketRuleTemplate): String? {
    if (!template.notificationConfigured) return "通知沿用全局"
    val items = buildList {
        if (template.notifySystemEnabled) add("通知栏")
        if (template.notifyToastEnabled) add("浮窗")
        if (template.notifySoundEnabled) add("铃声")
        if (template.notifyVibrateEnabled) add("震动")
        if (template.announceEnabled) add("播报")
        if (template.notifyFailedSystemEnabled || template.notifyFailedToastEnabled) add("未抢到提醒")
    }
    return if (items.isEmpty()) "通知关闭" else items.joinToString("/")
}

internal fun redPacketReplyModeOptions(): List<OptionItem> {
    return listOf(
        OptionItem("不回复", RedPacketRuleConfig.REPLY_OFF, "只抢红包，不发送回复"),
        OptionItem("发送文字", RedPacketRuleConfig.REPLY_TEXT, "抢到后发送文字"),
        OptionItem("发送图片", RedPacketRuleConfig.REPLY_IMAGE, "从系统文件管理器选择图片"),
        OptionItem("发送语音", RedPacketRuleConfig.REPLY_VOICE, "从系统文件管理器选择语音文件"),
        OptionItem("发送视频", RedPacketRuleConfig.REPLY_VIDEO, "从系统文件管理器选择视频"),
        OptionItem("发送表情", RedPacketRuleConfig.REPLY_EMOJI, "从系统文件管理器选择表情文件"),
        OptionItem("发送文件", RedPacketRuleConfig.REPLY_FILE, "从系统文件管理器选择任意文件"),
        OptionItem("发送收藏", RedPacketRuleConfig.REPLY_FAVORITE, "从最近收藏选择"),
        OptionItem("发送 XML", RedPacketRuleConfig.REPLY_XML, "发送 AppMsg/XML 内容")
    )
}

internal fun redPacketReplyStepModeOptions(): List<OptionItem> {
    return redPacketReplyModeOptions().filter { it.value != RedPacketRuleConfig.REPLY_OFF }
}

internal fun redPacketReplyUsesText(replyMode: Int): Boolean {
    return replyMode == RedPacketRuleConfig.REPLY_TEXT ||
        replyMode == RedPacketRuleConfig.REPLY_AT_SENDER
}

internal fun normalizeRedPacketReplyModeForUi(replyMode: Int): Int {
    return if (replyMode == RedPacketRuleConfig.REPLY_AT_SENDER) {
        RedPacketRuleConfig.REPLY_TEXT
    } else {
        replyMode
    }
}

internal fun normalizeRedPacketReplyTextForUi(replyMode: Int, text: String): String {
    if (replyMode != RedPacketRuleConfig.REPLY_AT_SENDER) return text
    if (redPacketReplyContainsAtSenderVariable(text)) return text
    return REDPACKET_AT_SENDER_VARIABLE + text
}

internal fun redPacketReplyContainsAtSenderVariable(text: String): Boolean {
    return REDPACKET_AT_SENDER_VARIABLES.any { text.contains(it) }
}

internal fun normalizeRedPacketReplyStepsForUi(
    steps: List<RedPacketReplyStep>,
    legacyMode: Int,
    legacyText: String,
    legacyDelayMs: Long,
    legacyRandom: Boolean
): List<RedPacketReplyStep> {
    val source = if (steps.isNotEmpty()) {
        steps
    } else {
        RedPacketRuleConfig.legacyReplySteps(legacyMode, legacyText, legacyDelayMs, legacyRandom)
    }
    return source.map { normalizeRedPacketReplyStepForUi(it) }
}

internal fun normalizeRedPacketReplyStepForUi(step: RedPacketReplyStep): RedPacketReplyStep {
    val mode = normalizeRedPacketReplyModeForUi(step.mode)
    val content = normalizeRedPacketReplyTextForUi(step.mode, step.content)
    return step.copy(
        id = step.id.ifBlank { System.currentTimeMillis().toString() },
        mode = mode,
        content = content,
        delayMs = step.delayMs.coerceIn(0L, 600000L)
    )
}

internal fun cleanRedPacketReplySteps(steps: List<RedPacketReplyStep>): List<RedPacketReplyStep> {
    return steps.map { normalizeRedPacketReplyStepForUi(it) }
        .filter { it.mode != RedPacketRuleConfig.REPLY_OFF && it.content.isNotBlank() }
}

internal fun newRedPacketReplyStep(index: Int): RedPacketReplyStep {
    return RedPacketReplyStep(
        id = System.currentTimeMillis().toString() + "_$index",
        mode = RedPacketRuleConfig.REPLY_TEXT,
        content = "谢谢老板",
        delayMs = 0L,
        random = false
    )
}

internal fun defaultRedPacketReplyContent(replyMode: Int): String {
    return when {
        redPacketReplyUsesText(replyMode) -> "谢谢老板"
        replyMode == RedPacketRuleConfig.REPLY_XML -> "<msg><appmsg appid=\"\" sdkver=\"0\"><title>谢谢老板</title></appmsg></msg>"
        else -> ""
    }
}

internal fun redPacketReplyModeLabel(replyMode: Int): String {
    return when (normalizeRedPacketReplyModeForUi(replyMode)) {
        RedPacketRuleConfig.REPLY_TEXT -> "发送文字"
        RedPacketRuleConfig.REPLY_IMAGE -> "发送图片"
        RedPacketRuleConfig.REPLY_VOICE -> "发送语音"
        RedPacketRuleConfig.REPLY_VIDEO -> "发送视频"
        RedPacketRuleConfig.REPLY_EMOJI -> "发送表情"
        RedPacketRuleConfig.REPLY_FILE -> "发送文件"
        RedPacketRuleConfig.REPLY_FAVORITE -> "发送收藏"
        RedPacketRuleConfig.REPLY_XML -> "发送 XML"
        else -> "不回复"
    }
}

internal fun describeRedPacketReplySteps(steps: List<RedPacketReplyStep>): String {
    if (steps.isEmpty()) return "不回复"
    val types = steps.take(3).joinToString("/") { redPacketReplyModeShortLabel(it.mode) }
    val suffix = if (steps.size > 3) "等" else ""
    return "${steps.size} 条回复 · $types$suffix · 顺序发送"
}

internal fun redPacketReplyStepSummary(step: RedPacketReplyStep): String {
    val content = if (redPacketReplyUsesText(step.mode) || step.mode == RedPacketRuleConfig.REPLY_XML) {
        step.content.trim().replace('\n', ' ').ifBlank { "未填写内容" }.take(24)
    } else {
        redPacketReplyContentSummary(step.mode, step.content, withHint = false).removePrefix("已选择：")
    }
    val delay = step.delayMs.coerceAtLeast(0L)
    val delayText = if (delay > 0L) "延迟 ${delay}ms" else "无延迟"
    val randomText = if (step.random) "随机追加" else "固定"
    return "$content · $delayText · $randomText"
}

internal fun redPacketReplyModeShortLabel(replyMode: Int): String {
    return when (normalizeRedPacketReplyModeForUi(replyMode)) {
        RedPacketRuleConfig.REPLY_TEXT -> "文字"
        RedPacketRuleConfig.REPLY_IMAGE -> "图片"
        RedPacketRuleConfig.REPLY_VOICE -> "语音"
        RedPacketRuleConfig.REPLY_VIDEO -> "视频"
        RedPacketRuleConfig.REPLY_EMOJI -> "表情"
        RedPacketRuleConfig.REPLY_FILE -> "文件"
        RedPacketRuleConfig.REPLY_FAVORITE -> "收藏"
        RedPacketRuleConfig.REPLY_XML -> "XML"
        else -> "关闭"
    }
}

internal fun redPacketReplyPickerTitle(replyMode: Int): String {
    return when (replyMode) {
        RedPacketRuleConfig.REPLY_IMAGE -> "选择图片"
        RedPacketRuleConfig.REPLY_VOICE -> "选择语音文件"
        RedPacketRuleConfig.REPLY_VIDEO -> "选择视频"
        RedPacketRuleConfig.REPLY_EMOJI -> "选择表情文件"
        RedPacketRuleConfig.REPLY_FILE -> "选择文件"
        RedPacketRuleConfig.REPLY_FAVORITE -> "选择收藏"
        else -> "选择文件"
    }
}

internal fun redPacketReplyContentSummary(replyMode: Int, value: String, withHint: Boolean = true): String {
    return if (replyMode == RedPacketRuleConfig.REPLY_FAVORITE) {
        favoriteSummary(value)
    } else {
        redPacketReplyFileSummary(value, withHint)
    }
}

internal fun redPacketReplyFileSummary(value: String, withHint: Boolean = true): String {
    val names = RedPacketRuleConfig.splitTokens(value)
        .map { redPacketReplyFileDisplayName(it) }
        .filter { it.isNotBlank() }
    if (names.isEmpty()) return "未选择文件"
    val preview = names.take(3).joinToString("、")
    val suffix = when {
        names.size == 1 -> ""
        names.size > 3 -> " 等 ${names.size} 个文件"
        else -> "，共 ${names.size} 个"
    }
    val summary = "已选择：$preview$suffix"
    return if (withHint) "$summary，点击重新选择" else summary
}

internal fun redPacketReplyFileDisplayName(value: String): String {
    val raw = value.trim()
    if (raw.isBlank()) return ""
    val name = if (raw.startsWith("content://", ignoreCase = true) || raw.startsWith("file://", ignoreCase = true)) {
        runCatching { Uri.parse(raw) }.getOrNull()
            ?.lastPathSegment
            ?.let { Uri.decode(it) }
            ?.substringAfterLast('/')
            ?.substringAfterLast(':')
            .orEmpty()
            .ifBlank { raw.substringAfterLast('/') }
    } else {
        File(raw).name.ifBlank { raw.substringAfterLast('/') }
    }
    val cacheNameMatch = Regex("""^\d{10,}_(.+)$""").matchEntire(name)
    return (cacheNameMatch?.groupValues?.getOrNull(1) ?: name).trim()
}

internal fun describeRedPacketBinding(
    binding: RedPacketRuleBinding,
    templates: List<RedPacketRuleTemplate>
): String {
    val state = if (binding.enabled) "启用" else "关闭"
    val template = templates.firstOrNull { it.id == binding.templateId }
        ?.name
        ?.ifBlank { binding.templateId }
        ?: "跟随默认规则"
    return "$state · $template · ${binding.targetId}"
}

internal fun redPacketBindingMatchesQuery(
    binding: RedPacketRuleBinding,
    templates: List<RedPacketRuleTemplate>,
    lowerQuery: String
): Boolean {
    if (binding.label.lowercase(Locale.US).contains(lowerQuery)) return true
    if (binding.targetId.lowercase(Locale.US).contains(lowerQuery)) return true
    val template = templates.firstOrNull { it.id == binding.templateId }
    return template != null && (
        template.name.lowercase(Locale.US).contains(lowerQuery) ||
            describeRedPacketTemplate(template).lowercase(Locale.US).contains(lowerQuery)
        )
}

internal fun redPacketBindingFromContact(
    row: MessageBlockContactOption,
    templates: List<RedPacketRuleTemplate>,
    existing: RedPacketRuleBinding?
): RedPacketRuleBinding {
    val option = row.contact
    val targetId = option.id.trim()
    return RedPacketRuleBinding(
        id = existing?.id ?: RedPacketRuleConfig.bindingKey(targetId),
        targetId = targetId,
        label = existing?.label?.takeIf { it.isNotBlank() } ?: option.label.ifBlank { targetId },
        enabled = existing?.enabled ?: false,
        templateId = existing?.templateId ?: if (templates.size == 1) templates.first().id else "",
        customRules = false,
        overrideRule = null
    )
}

internal fun findRedPacketBinding(
    bindings: List<RedPacketRuleBinding>,
    targetId: String
): RedPacketRuleBinding? {
    val key = RedPacketRuleConfig.bindingKey(targetId)
    return bindings.firstOrNull { RedPacketRuleConfig.bindingKey(it.targetId) == key }
}

internal fun normalizedRedPacketBinding(binding: RedPacketRuleBinding): RedPacketRuleBinding {
    val targetId = binding.targetId.trim()
    return binding.copy(
        id = RedPacketRuleConfig.bindingKey(targetId),
        targetId = targetId,
        label = binding.label.ifBlank { targetId },
        templateId = binding.templateId.trim(),
        customRules = false,
        overrideRule = null
    )
}

internal fun upsertRedPacketBindings(
    current: List<RedPacketRuleBinding>,
    additions: List<RedPacketRuleBinding>
): List<RedPacketRuleBinding> {
    val result = linkedMapOf<String, RedPacketRuleBinding>()
    current.forEach { binding ->
        val normalized = normalizedRedPacketBinding(binding)
        if (normalized.targetId.isNotBlank()) {
            result[RedPacketRuleConfig.bindingKey(normalized.targetId)] = normalized
        }
    }
    additions.forEach { binding ->
        val normalized = normalizedRedPacketBinding(binding)
        if (normalized.targetId.isBlank()) return@forEach
        val key = RedPacketRuleConfig.bindingKey(normalized.targetId)
        val old = result[key]
        result[key] = if (old == null) {
            normalized
        } else {
            normalized.copy(
                id = old.id,
                enabled = old.enabled,
                label = old.label.ifBlank { normalized.label },
                templateId = old.templateId.ifBlank { normalized.templateId }
            )
        }
    }
    return result.values.toList()
}

internal fun formatRedPacketSecond(value: Int): String {
    val second = value.coerceIn(0, 86399)
    return String.format(
        Locale.US,
        "%02d:%02d:%02d",
        second / 3600,
        second / 60 % 60,
        second % 60
    )
}

internal fun parseRedPacketSecond(value: String, fallback: Int): Int {
    return parseTimeOfDaySeconds(value).takeIf { it >= 0 } ?: fallback.coerceIn(0, 86399)
}

internal fun initialMessageBlockState(sp: SharedPreferences): MessageBlockInitialState {
    val rawTemplates = sp.getString(MessageBlockSettings.KEY_TEMPLATES, "") ?: ""
    val rawBindings = sp.getString(MessageBlockSettings.KEY_BINDINGS, "") ?: ""
    val parsedTemplates = MessageBlockSettings.parseTemplates(rawTemplates)
    val cleanedTemplates = parsedTemplates.map { MessageBlockSettings.clearTemplateTargets(it) }
    val existingBindings = MessageBlockSettings.parseBindings(rawBindings)
    val legacyBindings = if (rawBindings.isBlank()) {
        MessageBlockSettings.legacyBindingsFromTemplates(parsedTemplates)
    } else {
        emptyList()
    }
    val bindings = if (existingBindings.isNotEmpty()) existingBindings else legacyBindings
    val shouldPersist = rawBindings.isBlank() && legacyBindings.isNotEmpty()
    return MessageBlockInitialState(cleanedTemplates, bindings, shouldPersist)
}

internal fun newMessageBlockTemplate(index: Int): MessageBlockTemplate {
    return MessageBlockTemplate(
        id = System.currentTimeMillis().toString() + "_" + index,
        name = "模板 $index",
        enabled = true,
        mode = MessageBlockSettings.MODE_TARGETS,
        targets = "",
        targetGroupMembers = "",
        excludes = "",
        excludeGroupMembers = "",
        typeAll = false,
        types = emptySet(),
        textKeywords = ""
    )
}

internal fun describeMessageBlockTemplate(template: MessageBlockTemplate): String {
    val enabled = if (template.enabled) "启用" else "关闭"
    val type = describeMessageBlockRuleState(
        MessageBlockRuleState(template.typeAll, template.types, template.textKeywords)
    )
    return "$enabled · $type"
}

internal fun describeMessageBlockRuleState(rules: MessageBlockRuleState): String {
    val type = if (rules.typeAll) "所有消息" else {
        val names = messageBlockTypeOptions()
            .filter { rules.types.contains(it.key) }
            .joinToString("、") { it.title }
        names.ifBlank { "未选类型" }
    }
    val keyword = if (rules.textKeywords.isBlank()) "" else "，关键词"
    return "$type$keyword"
}

internal fun messageBlockRuleStateFromTemplates(
    templateIds: Set<String>,
    templates: List<MessageBlockTemplate>
): MessageBlockRuleState {
    val selected = templates.filter { templateIds.contains(it.id) }
    if (selected.isEmpty()) return MessageBlockRuleState(false, emptySet(), "")
    val types = LinkedHashSet<String>()
    selected.forEach { template -> types += template.types }
    val textKeywords = selected.map { it.textKeywords.trim() }
        .filter { it.isNotEmpty() }
        .distinct()
        .joinToString("\n")
    return MessageBlockRuleState(
        typeAll = selected.any { it.typeAll },
        types = types,
        textKeywords = textKeywords
    )
}

internal fun describeMessageBlockBinding(binding: MessageBlockBinding, templates: List<MessageBlockTemplate>): String {
    if (binding.quickBlockAll) return "聊天分组快捷屏蔽 · 所有消息"
    val state = if (binding.enabled) "启用" else "关闭"
    val action = if (binding.action == MessageBlockSettings.ACTION_EXCLUDE) "排除" else "屏蔽"
    val names = templates
        .filter { binding.templateIds.contains(it.id) }
        .joinToString("、") { it.name.ifBlank { it.id } }
        .ifBlank { "未绑定模板" }
    val rules = if (binding.customRules) {
        " · 专属规则：${describeMessageBlockRuleState(MessageBlockRuleState(binding.typeAll, binding.types, binding.textKeywords))}"
    } else {
        ""
    }
    return "$state · $action · $names$rules"
}

internal fun describeMessageBlockDefaultRule(rule: MessageBlockDefaultRule, templates: List<MessageBlockTemplate>): String {
    if (!rule.enabled) return "未启用"
    val names = templates
        .filter { rule.templateIds.contains(it.id) }
        .joinToString("、") { it.name.ifBlank { it.id } }
    val rules = if (rule.customRules) {
        "专属规则：${describeMessageBlockRuleState(MessageBlockRuleState(rule.typeAll, rule.types, rule.textKeywords))}"
    } else {
        names.ifBlank { "未绑定模板" }
    }
    return "启用 · $rules"
}

internal fun messageBlockBindingMatchesQuery(
    binding: MessageBlockBinding,
    templates: List<MessageBlockTemplate>,
    lowerQuery: String
): Boolean {
    if (binding.label.lowercase(Locale.US).contains(lowerQuery)) return true
    if (binding.targetId.lowercase(Locale.US).contains(lowerQuery)) return true
    if (binding.customRules) {
        val rules = describeMessageBlockRuleState(
            MessageBlockRuleState(binding.typeAll, binding.types, binding.textKeywords)
        ).lowercase(Locale.US)
        if (rules.contains(lowerQuery) || binding.textKeywords.lowercase(Locale.US).contains(lowerQuery)) return true
    }
    return templates.any { template ->
        binding.templateIds.contains(template.id) &&
            (
                template.name.lowercase(Locale.US).contains(lowerQuery) ||
                    template.id.lowercase(Locale.US).contains(lowerQuery)
                )
    }
}

internal fun messageBlockBindingTargetSummary(binding: MessageBlockBinding): String {
    val type = if (binding.targetType == MessageBlockSettings.TARGET_GROUP_MEMBER) "群成员" else "联系人 / 群聊 / 公众号"
    return "$type · ${binding.targetId}"
}

internal fun messageBlockBindingFromContact(
    option: ContactOption,
    templates: List<MessageBlockTemplate>,
    existing: MessageBlockBinding?
): MessageBlockBinding {
    val label = option.label.ifBlank { option.id }
    return MessageBlockBinding(
        id = existing?.id ?: MessageBlockSettings.bindingKey(
            MessageBlockSettings.TARGET_CONTACT,
            option.id
        ),
        targetType = MessageBlockSettings.TARGET_CONTACT,
        targetId = option.id,
        label = label,
        enabled = existing?.enabled ?: true,
        action = existing?.action ?: MessageBlockSettings.ACTION_BLOCK,
        templateIds = existing?.templateIds ?: defaultMessageBlockTemplateIds(templates),
        quickBlockAll = existing?.quickBlockAll ?: false
    )
}

internal fun messageBlockBindingFromGroupMember(
    entry: String,
    templates: List<MessageBlockTemplate>,
    existing: MessageBlockBinding?
): MessageBlockBinding {
    val normalized = normalizeGroupMemberEntry(entry) ?: entry.trim()
    return MessageBlockBinding(
        id = existing?.id ?: MessageBlockSettings.bindingKey(
            MessageBlockSettings.TARGET_GROUP_MEMBER,
            normalized
        ),
        targetType = MessageBlockSettings.TARGET_GROUP_MEMBER,
        targetId = normalized,
        label = existing?.label?.takeIf { it.isNotBlank() } ?: normalized,
        enabled = existing?.enabled ?: true,
        action = existing?.action ?: MessageBlockSettings.ACTION_BLOCK,
        templateIds = existing?.templateIds ?: defaultMessageBlockTemplateIds(templates),
        quickBlockAll = existing?.quickBlockAll ?: false
    )
}

internal fun defaultMessageBlockTemplateIds(templates: List<MessageBlockTemplate>): Set<String> {
    return if (templates.size == 1) setOf(templates.first().id) else emptySet()
}

internal fun normalizedMessageBlockBinding(binding: MessageBlockBinding): MessageBlockBinding {
    val targetType = if (binding.targetType == MessageBlockSettings.TARGET_GROUP_MEMBER) {
        MessageBlockSettings.TARGET_GROUP_MEMBER
    } else {
        MessageBlockSettings.TARGET_CONTACT
    }
    val normalizedTarget = if (targetType == MessageBlockSettings.TARGET_GROUP_MEMBER) {
        normalizeGroupMemberEntry(binding.targetId) ?: binding.targetId.trim()
    } else {
        binding.targetId.trim()
    }
    val action = if (binding.action == MessageBlockSettings.ACTION_EXCLUDE) {
        MessageBlockSettings.ACTION_EXCLUDE
    } else {
        MessageBlockSettings.ACTION_BLOCK
    }
    return binding.copy(
        id = MessageBlockSettings.bindingKey(targetType, normalizedTarget),
        targetType = targetType,
        targetId = normalizedTarget,
        action = action,
        templateIds = binding.templateIds.filter { it.isNotBlank() }.toSet(),
        types = binding.types.filter { it.isNotBlank() }.toSet(),
        textKeywords = binding.textKeywords.trim()
    )
}

internal fun findMessageBlockBinding(
    bindings: List<MessageBlockBinding>,
    targetType: String,
    targetId: String
): MessageBlockBinding? {
    val normalizedType = if (targetType == MessageBlockSettings.TARGET_GROUP_MEMBER) {
        MessageBlockSettings.TARGET_GROUP_MEMBER
    } else {
        MessageBlockSettings.TARGET_CONTACT
    }
    val normalizedTarget = if (normalizedType == MessageBlockSettings.TARGET_GROUP_MEMBER) {
        normalizeGroupMemberEntry(targetId) ?: targetId.trim()
    } else {
        targetId.trim()
    }
    return bindings.firstOrNull {
        it.targetType == normalizedType && it.targetId == normalizedTarget
    }
}

internal fun upsertMessageBlockBindings(
    current: List<MessageBlockBinding>,
    additions: List<MessageBlockBinding>
): List<MessageBlockBinding> {
    val result = linkedMapOf<String, MessageBlockBinding>()
    current.forEach { binding ->
        val normalized = normalizedMessageBlockBinding(binding)
        result[MessageBlockSettings.bindingKey(normalized.targetType, normalized.targetId)] = normalized
    }
    additions.forEach { binding ->
        val normalized = normalizedMessageBlockBinding(binding)
        val key = MessageBlockSettings.bindingKey(normalized.targetType, normalized.targetId)
        val old = result[key]
        result[key] = if (old == null) {
            normalized
        } else {
            normalized.copy(
                id = old.id,
                enabled = old.enabled,
                action = old.action,
                templateIds = old.templateIds + normalized.templateIds,
                customRules = old.customRules,
                typeAll = old.typeAll,
                types = old.types,
                textKeywords = old.textKeywords
            )
        }
    }
    return result.values.toList()
}

internal fun messageBlockTypeOptions(): List<MessageBlockTypeOption> {
    return listOf(
        MessageBlockTypeOption(MessageBlockSettings.TYPE_TEXT, "文字", "普通文本消息"),
        MessageBlockTypeOption(MessageBlockSettings.TYPE_QUOTE, "引用消息", "引用文字、图片和其它消息"),
        MessageBlockTypeOption(MessageBlockSettings.TYPE_IMAGE, "图片", "图片消息"),
        MessageBlockTypeOption(MessageBlockSettings.TYPE_VIDEO, "小视频", "视频和短视频消息"),
        MessageBlockTypeOption(MessageBlockSettings.TYPE_VOICE, "语音", "语音消息"),
        MessageBlockTypeOption(MessageBlockSettings.TYPE_LINK, "文章、链接", "文章、链接、文件和接龙类 AppMsg"),
        MessageBlockTypeOption(MessageBlockSettings.TYPE_MUSIC, "音乐", "音乐类 AppMsg"),
        MessageBlockTypeOption(MessageBlockSettings.TYPE_MINI_PROGRAM, "小程序", "小程序卡片"),
        MessageBlockTypeOption(MessageBlockSettings.TYPE_CARD, "名片", "联系人名片"),
        MessageBlockTypeOption(MessageBlockSettings.TYPE_EMOJI, "动画表情", "表情消息"),
        MessageBlockTypeOption(MessageBlockSettings.TYPE_RED_PACKET, "红包", "红包消息"),
        MessageBlockTypeOption(MessageBlockSettings.TYPE_TRANSFER, "转账", "转账消息"),
        MessageBlockTypeOption(MessageBlockSettings.TYPE_VOIP, "视频/语音聊天", "通话邀请和记录"),
        MessageBlockTypeOption(MessageBlockSettings.TYPE_LOCATION, "地图位置", "位置分享消息"),
        MessageBlockTypeOption(MessageBlockSettings.TYPE_SYSTEM, "系统消息", "普通系统提示"),
        MessageBlockTypeOption(MessageBlockSettings.TYPE_PAT, "拍一拍", "拍一拍系统消息"),
        MessageBlockTypeOption(MessageBlockSettings.TYPE_VIDEO_NUMBER, "视频号链接", "视频号分享卡片"),
        MessageBlockTypeOption(MessageBlockSettings.TYPE_UNKNOWN, "未知类型", "微信新增或暂未识别的消息类型")
    )
}

internal fun loadMessageBlockContacts(
    context: Context,
    callback: (MessageBlockContactData, Throwable?) -> Unit
) {
    val api = WeChatApis.contact().contacts()
    if (api == null || !api.isAvailable) {
        callback(MessageBlockContactData(), IllegalStateException("联系人列表不可用"))
        return
    }
    val main = Handler(Looper.getMainLooper())
    Thread({
        try {
            val labelsByUser = linkedMapOf<String, MutableList<String>>()
            val labels = runCatching { api.getContactLabelList() }
                .getOrDefault(emptyList())
                .mapNotNull { label ->
                    val labelId = label.labelId.trim()
                    val labelName = label.labelName.trim().ifBlank { labelId }
                    if (labelId.isBlank() && labelName.isBlank()) return@mapNotNull null
                    val ids = label.userNameList.ifEmpty {
                        if (labelId.isBlank()) emptyList() else runCatching { api.getContactByLabelId(labelId) }.getOrDefault(emptyList())
                    }.map { it.trim() }
                        .filter { it.isNotEmpty() }
                        .distinct()
                    ids.forEach { wxId ->
                        labelsByUser.getOrPut(wxId) { arrayListOf() }.add(labelName)
                    }
                    MessageBlockLabelOption(
                        id = labelId.ifBlank { labelName },
                        name = labelName,
                        contactIds = ids
                    )
                }
                .sortedBy { it.name.lowercase(Locale.US) }

            val byId = linkedMapOf<String, MessageBlockContactOption>()
            fun putContact(contact: WeChatContact?, kind: MessageBlockContactKind, group: Boolean) {
                val option = contact.toOption(group) ?: return
                byId[option.id] = MessageBlockContactOption(
                    contact = option,
                    kind = kind,
                    labelNames = labelsByUser[option.id].orEmpty().distinct()
                )
            }

            api.getPickerContacts().forEach { putContact(it, MessageBlockContactKind.FRIEND, false) }
            api.getPickerGroups().forEach { putContact(it, MessageBlockContactKind.GROUP, true) }
            api.getPickerOfficialAccounts().forEach { putContact(it, MessageBlockContactKind.OFFICIAL, false) }

            val missingLabelIds = labels.flatMap { it.contactIds }
                .distinct()
                .filterNot { byId.containsKey(it) }
            if (missingLabelIds.isNotEmpty()) {
                api.getContactsByIds(missingLabelIds).forEach { contact ->
                    val kind = when {
                        contact.isGroup() -> MessageBlockContactKind.GROUP
                        contact.isOfficialAccount() -> MessageBlockContactKind.OFFICIAL
                        else -> MessageBlockContactKind.FRIEND
                    }
                    putContact(contact, kind, contact.isGroup())
                }
            }

            val contacts = byId.values.sortedWith(
                compareBy<MessageBlockContactOption> { it.kind.ordinal }
                    .thenBy { it.contact.label.lowercase(Locale.US) }
            )
            main.post { callback(MessageBlockContactData(contacts, labels), null) }
        } catch (throwable: Throwable) {
            main.post { callback(MessageBlockContactData(), throwable) }
        }
    }, "HchatMessageBlockContacts").start()
}

internal fun loadContacts(
    context: Context,
    mode: ContactPickerMode,
    includeLabels: Boolean = false,
    callback: (List<ContactOption>, Throwable?) -> Unit
) {
    val api = WeChatApis.contact().contacts()
    if (api == null || !api.isAvailable) {
        callback(emptyList(), IllegalStateException("联系人列表不可用"))
        return
    }
    val main = Handler(Looper.getMainLooper())
    Thread({
        try {
            val rows = mutableListOf<ContactOption>()
            val labelsByUser = linkedMapOf<String, MutableList<String>>()
            if (includeLabels) {
                runCatching { api.getContactLabelList() }.getOrDefault(emptyList()).forEach { label ->
                    val labelName = label.labelName.ifBlank { label.labelId }
                    if (labelName.isBlank()) return@forEach
                    label.userNameList.forEach { wxId ->
                        if (wxId.isNotBlank()) {
                            labelsByUser.getOrPut(wxId) { arrayListOf() }.add(labelName)
                        }
                    }
                }
            }
            if (mode == ContactPickerMode.FRIENDS || mode == ContactPickerMode.BOTH || mode == ContactPickerMode.ALL_CHATS) {
                rows += api.getPickerContacts().mapNotNull { it.toOption(false, labelsByUser[it.wxId].orEmpty()) }
            }
            if (mode == ContactPickerMode.GROUPS || mode == ContactPickerMode.BOTH || mode == ContactPickerMode.ALL_CHATS) {
                rows += api.getPickerGroups().mapNotNull { it.toOption(true) }
            }
            if (mode == ContactPickerMode.ALL_CHATS) {
                rows += api.getPickerOfficialAccounts().mapNotNull { it.toOption(false, official = true) }
            }
            val conversationOrder = WeChatApis.conversations()
                ?.getRecentConversationUsernames(10000)
                .orEmpty()
                .mapIndexed { index, username -> username to index }
                .toMap()
            val sorted = rows.distinctBy { it.id }.sortedWith(
                compareBy<ContactOption> { conversationOrder[it.id] ?: Int.MAX_VALUE }
                    .thenBy { it.group }
                    .thenBy { it.official }
                    .thenBy { it.label.lowercase(Locale.US) }
            )
            main.post { callback(sorted, null) }
        } catch (throwable: Throwable) {
            main.post { callback(emptyList(), throwable) }
        }
    }, "HchatContactPicker").start()
}

internal fun loadGroupMembers(
    context: Context,
    group: ContactOption,
    callback: (List<ContactOption>, Throwable?) -> Unit
) {
    val api = WeChatApis.contact().contacts()
    if (api == null || !api.isAvailable) {
        callback(emptyList(), IllegalStateException("群成员列表不可用"))
        return
    }
    val main = Handler(Looper.getMainLooper())
    Thread({
        try {
            val roomNames = api.getGroupMemberRoomDisplayNames(group.id)
            val rows = api.getGroupMembers(group.id).mapNotNull { contact ->
                val id = contact.wxId.takeIf { it.isNotBlank() } ?: return@mapNotNull null
                ContactOption(
                    id = id,
                    label = roomNames[id]?.takeIf { it.isNotBlank() }
                        ?: contact.displayName().ifEmpty { id },
                    group = false,
                    avatarUrl = contact.avatarUrl,
                    avatarBackupUrl = contact.avatarBackupUrl,
                    searchAliases = listOf(contact.remarkName, contact.nickname, contact.customWxId)
                        .filter { it.isNotBlank() }
                        .distinct()
                )
            }.sortedBy { it.label.lowercase(Locale.US) }
            main.post { callback(rows, null) }
        } catch (throwable: Throwable) {
            main.post { callback(emptyList(), throwable) }
        }
    }, "HchatGroupMemberPicker").start()
}

internal fun WeChatContact?.toOption(
    group: Boolean,
    labels: List<String> = emptyList(),
    official: Boolean = false
): ContactOption? {
    if (this == null || wxId.isNullOrEmpty()) return null
    return ContactOption(
        id = wxId,
        label = pickerDisplayName(group),
        group = group,
        avatarUrl = avatarUrl,
        avatarBackupUrl = avatarBackupUrl,
        labels = labels.distinct(),
        official = official,
        searchAliases = listOf(remarkName, nickname, customWxId)
            .filter { it.isNotBlank() }
            .distinct()
    )
}

internal fun parseIds(value: String): Set<String> {
    return value.split('|').map { it.trim() }.filter { it.isNotEmpty() }.toSet()
}

internal fun formatIds(ids: Iterable<String>): String {
    return ids
        .map { it.trim() }
        .filter { it.isNotEmpty() }
        .distinct()
        .joinToString("|")
}

internal fun parseFavoriteIds(value: String): Set<String> {
    return value.replace(";;;", "|")
        .split("|", ",", "，", "\n", "\r")
        .map { it.trim() }
        .filter { it.isNotEmpty() }
        .toSet()
}

internal fun formatFavoriteIds(ids: Iterable<String>, delimiter: String, multiSelect: Boolean): String {
    val values = ids
        .map { it.trim() }
        .filter { it.isNotEmpty() }
        .distinct()
    if (values.isEmpty()) return ""
    return if (multiSelect) {
        values.joinToString(delimiter)
    } else {
        values.first()
    }
}

internal fun favoriteSummary(value: String): String {
    val count = parseFavoriteIds(value).size
    return if (count == 0) "未选择收藏" else "已选择 $count 个收藏"
}

internal fun <T> List<T>.selectedFirst(isSelected: (T) -> Boolean): List<T> {
    if (size < 2) return this
    val selected = ArrayList<T>()
    val unselected = ArrayList<T>()
    for (value in this) {
        if (isSelected(value)) selected.add(value) else unselected.add(value)
    }
    if (selected.isEmpty() || unselected.isEmpty()) return this
    return selected.apply { addAll(unselected) }
}

internal fun groupMemberEntry(groupId: String, memberId: String): String {
    return "${groupId.trim()}/${memberId.trim()}"
}

internal fun parseGroupMemberEntries(value: String): Set<String> {
    return value.split("|", ",", "，", "\n", "\r")
        .mapNotNull { normalizeGroupMemberEntry(it) }
        .toSet()
}

internal fun formatGroupMemberEntries(entries: Iterable<String>): String {
    return entries
        .mapNotNull { normalizeGroupMemberEntry(it) }
        .distinct()
        .joinToString("|")
}

internal fun normalizeGroupMemberEntry(value: String): String? {
    val token = value.trim()
    if (token.isEmpty()) return null
    val separators = charArrayOf('/', '#', ':', '：')
    val index = separators.map { token.indexOf(it) }
        .filter { it > 0 }
        .minOrNull()
        ?: return null
    val groupId = token.substring(0, index).trim()
    val memberId = token.substring(index + 1).trim()
    if (groupId.isEmpty() || memberId.isEmpty()) return null
    return groupMemberEntry(groupId, memberId)
}

suspend fun loadAvatarBitmap(wxId: String, primary: String, backup: String): ImageBitmap? = withContext(Dispatchers.IO) {
    val sources = avatarSources(wxId, primary, backup)
    if (sources.isEmpty()) {
        null
    } else {
        val cacheKey = sources.joinToString("|")
        val cached = AvatarMemoryCache.cached(cacheKey)
        if (cached.first) {
            cached.second
        } else {
            var bitmap: ImageBitmap? = null
            for (source in sources) {
                bitmap = loadAvatarSource(source)?.asImageBitmap()
                if (bitmap != null) break
            }
            bitmap?.also { AvatarMemoryCache.put(cacheKey, it) }
        }
    }
}

internal fun avatarSources(wxId: String, primary: String, backup: String): List<String> {
    val result = LinkedHashSet<String>()
    if (primary.isNotBlank()) result.add(primary)
    if (backup.isNotBlank()) result.add(backup)
    val local = avatarLocalPath(wxId, hd = false)
    if (!local.isNullOrBlank()) result.add(local)
    val hdLocal = avatarLocalPath(wxId, hd = true)
    if (!hdLocal.isNullOrBlank()) result.add(hdLocal)
    return result.toList()
}

internal fun avatarLocalPath(wxId: String, hd: Boolean): String? {
    if (wxId.isBlank()) return null
    val root = WeChatAvatarPathCache.resolveAvatarRoot() ?: return null
    val hash = md5Lower(wxId) ?: return null
    return buildString(root.length + 64) {
        append(root.trimEnd('/'))
        append('/')
        append(hash.substring(0, 2))
        append('/')
        append(hash.substring(2, 4))
        append("/user_")
        if (hd) append("hd_")
        append(hash)
        append(".png")
    }
}

internal fun md5Lower(value: String): String? {
    return try {
        val digest = MessageDigest.getInstance("MD5").digest(value.toByteArray(Charsets.UTF_8))
        buildString(digest.size * 2) {
            for (b in digest) {
                append(((b.toInt() ushr 4) and 0xF).toString(16))
                append((b.toInt() and 0xF).toString(16))
            }
        }
    } catch (_: Throwable) {
        null
    }
}

internal fun loadAvatarSource(value: String): android.graphics.Bitmap? {
    return try {
        if (value.startsWith("http://") || value.startsWith("https://")) {
            val connection = URL(value).openConnection()
            connection.connectTimeout = 3000
            connection.readTimeout = 3000
            connection.getInputStream().use { BitmapFactory.decodeStream(it) }
        } else {
            val file = File(value)
            if (file.exists()) BitmapFactory.decodeFile(file.absolutePath) else null
        }
    } catch (_: Throwable) {
        null
    }
}

internal fun isDarkMode(context: Context): Boolean {
    val night = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    return night == Configuration.UI_MODE_NIGHT_YES
}

internal const val FAVORITE_BACKGROUND_BATCH_DELAY_MS = 40L
