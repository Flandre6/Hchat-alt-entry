# 恢复旧版聊天输入框

## 功能范围

设置项位于“设置 → 聊天 → 恢复旧版聊天输入框”，默认关闭。它只控制微信聊天输入栏的原生灰度配置，与“美化 → 输入框提示”是两个独立功能。

当前 Hook 仅对微信主进程的 `versionCode=3220`（8.0.80）安装。开启后，微信下一次创建或重建聊天输入栏时使用原生旧版分支；关闭或其他配置对象会原样调用微信逻辑。修改后重新打开会话，让微信重建输入区。

## 逆向依据

使用 DexSQL 检查了本机微信 8.0.80（3220）、腾讯 CDN 的微信 8.0.49（2600），并查询了 8.0.78 和 8.0.79 的 DEX 字符串。

8.0.80 中，`RepairerConfigInputBarVoiceInput.e()` 返回配置键 `clicfg_chatting_inputbar_voice_input`。`ChatFooter.c1()` 通过 `tc2.e.c(ee5.e):int` 读取该配置；聊天组件 `com.tencent.mm.ui.chatting.component.ld.Q0()` 的 `resetView()` 路径再次读取同一配置，并以 `inputBarVoiceInputEnabled` 记录状态。Hook 只在该配置对象、目标版本和用户开关同时匹配时，将微信原生读取结果设为 `0`。

8.0.49 的 `ChatFooter` 仍使用微信原生 `t8` 输入栏布局，包含 `MMFlexEditText`、微信按钮和 `ChatFooterBottom`，但没有 `clicfg_chatting_inputbar_voice_input` 或 `RepairerConfigInputBarVoiceInput`。8.0.80 的同名布局保留原生编辑框和控件，并增加 `SpeechInputLayout` 等语音输入视图。8.0.78 与 8.0.79 也包含该配置键；本实现因只验证了 8.0.80 的配置读取签名，不会在它们上安装 Hook。

因此实现复用微信已有的旧版输入栏分支，不自行绘制控件，也不改键盘、发送、引用、@、语音、表情或多行文本事件。其他 RepairerConfig 对象（包括 `RepairerConfigUseNewVoice2TxtComponent`）保持原行为。

## 样本

| 样本 | 来源 | SHA-256 |
| --- | --- | --- |
| WeChat 8.0.80 / 3220 | 本机 APK；DexSQL Manifest 确认包名 `com.tencent.mm`、版本 8.0.80、versionCode 3220 | `c35a05d5c13bd43cf1f7ff42c13fab070d21509ab2e233596f776d1afcae52f5` |
| WeChat 8.0.49 / 2600 | [腾讯 CDN APK](https://dldir1.qq.com/weixin/android/weixin8049android2600_0x2800313d_arm64.apk)；DexSQL Manifest 确认版本 | `e8402abfcd88f540d00ddeac451c847deed0d1b162d41fa3b2c6eb86f4c4b5b8` |

## 验证边界

`:app:testDebugUnitTest` 通过（20 项，0 失败）；`:app:compileReleaseKotlin` 与 `:app:compileReleaseJavaWithJavac` 通过，本地未组装 APK。8.0.78 / 8.0.79 仅做 DEX 字符串检查；其他版本未验证。真机上的键盘、发送、引用、@、语音、表情、多行文字、聊天切换和 View 重建仍需在该 Hchat 分支的 8.0.80（3220）构建上验收。
