# 表情安全消息代码提取

本目录提取自 `safety-message-8077.zip`，用于保留表情安全消息原始迁移依据。正式运行实现已并入 `app/src/main/java/h/Hchat/hooks/items/securemessage/SendSecureMessageFeature.kt`，共用现有“安全消息”功能开关，无需单独注册功能。

## 文件

- `EmojiSafetyMessageHooks.kt`：表情 MsgSource 提供者 Hook、`sendemoji` 分发前 Hook、`sec_msg_node` 写回和反射辅助。
- `SafetyMessageEmojiProfile.kt`：微信 8.0.76 和 8.0.77 的宿主类名、方法名映射。

## 已提取的两条链路

| 微信版本 | 表情 MsgSource 提供者 | 表情请求分发 |
| --- | --- | --- |
| 8.0.76 / 3140 | `lq1.d1.a(com.tencent.mm.storage.e9): String` | `o22.y.doScene(network.s, modelbase.u0): int` |
| 8.0.77 / 3160 | `ft1.d1.a(com.tencent.mm.storage.e9): String` | `k52.y.doScene(network.s, modelbase.u0): int` |

表情消息要求 `getType() == 47`，仅受 `emoji` 开关控制，对所有会话生效。分发 Hook 会读取 `sendemoji` 请求首项的 `p` 字段并覆盖 MsgSource；8077 的请求 cmdId 为 175。

## 接入时需要提供

调用方需要传入 `FeatureContext`、`MutableList<XC_MethodHook.Unhook>`、错误日志函数和节点写回函数；可直接传入 `EmojiSafetyMessageHooks::appendNode`。运行环境还需要项目现有的 `HchatStorage`、`KavaReflector`、Xposed API 和宿主反射辅助。

本目录中的提取副本不参与 APK 编译；正式运行实现仍需在对应微信版本上进行真机 Hook 验证。
