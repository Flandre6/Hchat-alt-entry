const fs = require('node:fs');
const path = require('node:path');
const os = require('node:os');
const { spawnSync } = require('node:child_process');

const root = path.resolve(__dirname, '..');
const build = fs.readFileSync(path.join(root, 'build.gradle.kts'), 'utf8');
const kotlin = build.match(/id\("org\.jetbrains\.kotlin\.plugin\.compose"\)\s+version\s+"([^"]+)"/);
if (!kotlin) throw new Error('Cannot determine the project Kotlin version');
const output = fs.mkdtempSync(path.join(os.tmpdir(), 'hchat-conversation-file-tests-'));
function run(command, args, timeout = 180000) {
    const result = spawnSync(command, args, { cwd: root, encoding: 'utf8', timeout });
    process.stdout.write(result.stdout || '');
    process.stderr.write(result.stderr || '');
    if (result.error || result.status !== 0) throw result.error || new Error(`Command failed: ${command} (${result.status})`);
}
try {
    // Resolve the compiler and transitive dependencies through the project wrapper; no
    // assumption about a particular machine's Gradle cache or dependency versions.
    fs.writeFileSync(path.join(output, 'settings.gradle'), "rootProject.name = 'conversation-file-regression'\n");
    fs.writeFileSync(path.join(output, 'build.gradle'), `
repositories { mavenCentral(); maven { url = "https://api.xposed.info/" } }
configurations { compiler; xposed }
dependencies {
    compiler 'org.jetbrains.kotlin:kotlin-compiler-embeddable:${kotlin[1]}'
    compiler 'org.jetbrains.kotlin:kotlin-stdlib:${kotlin[1]}'
    compiler 'org.jetbrains:annotations:13.0'
    xposed 'de.robv.android.xposed:api:82'
}
tasks.register('writeXposedClasspath') {
    doLast { file('xposed.txt').text = configurations.xposed.asPath }
}
tasks.register('writeCompilerClasspath') {
    doLast { file('classpath.txt').text = configurations.compiler.asPath }
}
`);
    run(path.join(root, 'gradlew'), ['--no-daemon', '--console=plain', '-q', '-p', output, 'writeCompilerClasspath']);
    const classpath = fs.readFileSync(path.join(output, 'classpath.txt'), 'utf8');
    const artifact = path.join(output, 'file.jar');
    const quick = fs.readFileSync(path.join(root, 'app/src/main/java/h/Hchat/hooks/items/conversationgroup/ConversationGroupQuickDialog.kt'), 'utf8');
    const start = quick.indexOf('private object ConversationGroupDocumentBridge {');
    if (start < 0) throw new Error('Missing document bridge');
    const extracted = path.join(output, 'DocumentBridge.kt');
    fs.writeFileSync(extracted, `package h.Hchat.hooks.items.conversationgroup
import android.app.Activity
import android.content.Context
import android.content.Intent
import h.Hchat.utils.HLog
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
` + quick.slice(start).replace('private object ConversationGroupDocumentBridge', 'internal object ConversationGroupDocumentBridge').replace('private data class DocumentResult(', 'internal data class DocumentResult('));
    const java = process.env.JAVA || 'java';
    run(java, ['-Xmx256m', '-cp', classpath, 'org.jetbrains.kotlin.cli.jvm.K2JVMCompiler',
        '-no-stdlib', '-no-reflect', '-classpath', classpath, '-d', artifact,
        extracted,
        'app/src/main/java/h/Hchat/hooks/items/conversationgroup/ConversationGroupFileRequest.kt',
        'app/src/main/java/h/Hchat/hooks/items/conversationgroup/ConversationGroupSendPicker.kt',
        'app/src/main/java/h/Hchat/hooks/items/conversationtabs/ConversationTabsIconPicker.kt',
        ...fs.readdirSync(path.join(root, 'scripts/tests/conversation_group_file')).filter(f => f.endsWith('.kt')).map(f => 'scripts/tests/conversation_group_file/' + f)
    ]);
    run(java, ['-cp', [artifact, classpath].join(path.delimiter), 'h.Hchat.hooks.items.conversationgroup.FileRegressionKt', ...process.argv.slice(2).filter(arg => arg !== '--real-api')]);
    if (process.argv.includes('--real-api')) {
        const sdk = process.env.ANDROID_HOME || process.env.ANDROID_SDK_ROOT;
        if (!sdk) throw new Error('Set ANDROID_HOME or ANDROID_SDK_ROOT for --real-api');
        const compileSdk = fs.readFileSync(path.join(root, 'app/build.gradle.kts'), 'utf8').match(/compileSdk\s*=\s*(\d+)/);
        if (!compileSdk) throw new Error('Cannot determine compileSdk');
        const platforms = path.join(sdk, 'platforms');
        const match = fs.readdirSync(platforms).find(name => name === `android-${compileSdk[1]}` || name === `android-${compileSdk[1]}.0`);
        const androidJar = match && path.join(platforms, match, 'android.jar');
        if (!androidJar || !fs.existsSync(androidJar)) throw new Error(`Missing Android SDK ${compileSdk[1]} android.jar`);
        run(path.join(root, 'gradlew'), ['--no-daemon', '--console=plain', '-q', '-p', output, 'writeXposedClasspath']);
        const xposed = fs.readFileSync(path.join(output, 'xposed.txt'), 'utf8');
        const realClasspath = [androidJar, xposed, classpath].join(path.delimiter);
        run(java, ['-Xmx256m', '-cp', classpath, 'org.jetbrains.kotlin.cli.jvm.K2JVMCompiler',
            '-no-stdlib', '-no-reflect', '-classpath', realClasspath, '-d', path.join(output, 'real-api.jar'),
            extracted,
            'app/src/main/java/h/Hchat/hooks/items/conversationgroup/ConversationGroupFileRequest.kt',
            'app/src/main/java/h/Hchat/hooks/items/conversationgroup/ConversationGroupSendPicker.kt',
            'app/src/main/java/h/Hchat/hooks/items/conversationtabs/ConversationTabsIconPicker.kt',
            'scripts/tests/conversation_group_file/GroupStore.kt',
            'scripts/tests/conversation_group_file/IconStore.kt',
            'scripts/tests/conversation_group_file/Log.kt'
        ]);
        console.log('Real Android/Xposed API compile passed (document bridge extracted from QuickDialog)');
    }
} finally {
    fs.rmSync(output, { recursive: true, force: true });
}
