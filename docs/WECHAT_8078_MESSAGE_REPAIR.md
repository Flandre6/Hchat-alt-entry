# WeChat 8.0.78 Message API Repair

## Scope and Status

Date: 2026-09-16. Baseline: a910087d447320581f0c1f33783907daa61b4dd4.
Local remediation for WeChat 8.0.78 (3180), ARM64, with Hchat API 102.
No device execution or server-side acceptance was verified. No commit or push
was performed as part of this repair.

## Artifacts

Original samples were analyzed read-only from the user's 2026-09 message files.

| Artifact | SHA-256 |
| --- | --- |
| weixin8078android3180_0x28004e32_arm64.apk.1 | 41f7dc1f720767fa78fa20dd13ea034b817bbf6ebd23dfd1324c647499c9c1ba |
| LSPosed_20260916_020551(1).zip | 75e4b1bedcdbf422b30b608303ca57ccddcacae9d8fa05cc78439470004893b4 |

## Verified Facts and Remediation

| ID | Evidence | Impact and repair |
| --- | --- | --- |
| E1 | classes12.dex: v51.r0 constructors accept String,String,int,int,long,String or String,String,int,int,Object,String; anchors MicroMsg.NetSceneSendMsg and /cgi-bin/micromsg-bin/newsendmsg | Five-argument-only matching misses the text send API. Recognize both arities, including cached class resolution, and pass an empty trailing msgSource for native assembly. |
| E2 | classes11.dex: com.tencent.mm.storage.f9.Bb(e9,boolean):long; anchor Error insert message getTableByTalker failed. talker:%s; WCDB TableORMOperation.insertOrReplaceObject path | ContentValues-only SQL hooks miss ORM inserts. Observe successful native storage insertions and snapshot the native message through convertTo. Suppress nested SQL callbacks from the same operation. |
| E3 | Observer code discarded all incoming database events whenever an AddMsg candidate appeared available | Availability now requires installed hooks. PB and database input coexist and share atomic server-ID deduplication. Database retry also reinstalls observer subscriptions. |
| E4 | classes12.dex c72.y sendemoji scene; classes11.dex vu1.d1.a(e9):String provider | Update the 8078 emoji profile from old ft1.d1/k52.y names. Existing provider and request hooks consume the corrected profile. |
| E5 | classes3.dex xw3.l.a(String,String,String):void invokes b41.aa.n(String,long):long and com.tencent.mm.storage.f9.yb(e9):long | Discover the timestamp and insert methods from helper invocations. Capture the real row ID during native insertion instead of treating a normal void return as success. |

Logs independently show the unavailable text API, missing old emoji classes,
and unavailable createTime hook. Confidence in these compatibility mismatches
is high; successful end-to-end recovery remains unverified on a device.

## Validation

API 102 Debug Kotlin and Java compilation passed. Host-JVM regression tests
exercise the production observer, not a reimplementation:

- PB before database and database before PB each deliver once.
- Database-only input is delivered even when the PB hook is active.
- Database sender identity is preserved; repeated insertions are suppressed.
- Database updates and historical rows do not trigger new-message actions.
- Distinct server IDs with identical content are both delivered.
- Concurrent claims are atomic; expired identities can be reused.
- Dedup state is bounded to 4096 entries, with constant-time oldest eviction.

Test fixtures are outside Android source sets and never enter the APK.

Reproduce compilation and tests in PowerShell with the local Android SDK set:

```powershell
$env:ANDROID_HOME = 'C:/Users/moci5/AppData/Local/Android/Sdk'
$env:ANDROID_SDK_ROOT = $env:ANDROID_HOME
java -classpath gradle/wrapper/gradle-wrapper.jar org.gradle.wrapper.GradleWrapperMain -I scripts/message-regression.init.gradle :app:messageRegression '-Phchat.modernXposed=true'
git diff --check
```

Offline evidence was obtained with Python and androguard 4.1.4, inspecting
DEX instructions and signatures without executing the APK. The local case
helper reverse_case_lsposed_20260915/inspect_apk.py can reproduce focused dumps:

```powershell
python reverse_case_lsposed_20260915/inspect_apk.py <apk-path> --dex classes12.dex --classes v51.r0 --methods '<init>'
python reverse_case_lsposed_20260915/inspect_apk.py <apk-path> --dex classes11.dex --classes com.tencent.mm.storage.f9 --methods Bb
python reverse_case_lsposed_20260915/inspect_apk.py <apk-path> --dex classes3.dex --classes xw3.l --methods a
```

## Unknowns and Required Device Checks

Shared receiving/sending failures affect red packets, transfers, auto-reply,
schedules, mass messaging, and voice announcements. These fixes do not prove
each downstream feature works. Payment server responses, TTS engine state,
non-text sending, scheduling in the background, and recipient-side secure
emoji behavior still require testing.

PB events without a server ID cannot be reliably correlated with a later
database row that has one; the fallback identity does not guarantee cross-path
deduplication in that case. Subscriber callbacks retain their existing thread
behavior; this is not a complete performance or background-delivery repair.

1. Install a new API 102 build and restart WeChat. Check storage-hook installation,
   observer installation, and first-incoming-source diagnostics in LSPosed.
2. Test one new text message, one auto-reply, one red packet, one transfer, one
   scheduled text, one mass-send recipient, and one TTS announcement separately.
3. Verify a revoked message creates exactly one actual system row, and send an
   emoji to a second account to check its secure-message menu behavior.
