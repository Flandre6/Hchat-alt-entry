// Compile the production constructor matcher/argument adapter and exercise stale-cache recovery without Gradle or Android.
const fs = require('node:fs');
const os = require('node:os');
const path = require('node:path');
const { spawnSync } = require('node:child_process');

const root = path.resolve(__dirname, '..');
const output = fs.mkdtempSync(path.join(os.tmpdir(), 'hchat-send-text-compat-'));
process.on('exit', () => fs.rmSync(output, { recursive: true, force: true }));
const dexPath = path.join(root, 'app/src/main/java/h/Hchat/dexkit/DexFinder.java');
const apiPath = path.join(root, 'app/src/main/java/h/Hchat/hooks/api/message/WeChatMessageApi.java');
const dex = fs.readFileSync(dexPath, 'utf8');
const api = fs.readFileSync(apiPath, 'utf8');

function method(source, signature) {
    const start = source.indexOf(signature);
    if (start < 0) throw new Error('Missing production method: ' + signature);
    const open = source.indexOf('{', start);
    let depth = 0;
    for (let i = open; i < source.length; i++) {
        if (source[i] === '{') depth++;
        if (source[i] === '}' && --depth === 0) return source.slice(start, i + 1);
    }
    throw new Error('Unclosed production method: ' + signature);
}

function assertSource(condition, label) {
    if (!condition) throw new Error('FAIL ' + label);
}

const finder = method(dex, '    private Constructor<?> findSendTextConstructor(');
const exactFinder = method(dex, '    private Constructor<?> findCtorByExactTypes(');
const cacheVersionCheck = method(dex, '    private boolean isSendTextLocatorCacheCurrent(');
const resolveAll = method(dex, '    public synchronized void resolveAll(');
const resolveText = method(dex, '    public void resolveSendTextMsg(');
const requestBuilder = method(api, '    private Object newMessageRequest(');
const cacheHitStart = resolveAll.indexOf('        if (loadCache()) {');
const cacheHitEnd = resolveAll.indexOf('        cacheDescriptorInvalid = false;', cacheHitStart);
if (cacheHitStart < 0 || cacheHitEnd < 0) throw new Error('Missing cache restore branch in resolveAll');
const cacheRestoreBranch = resolveAll.slice(cacheHitStart, cacheHitEnd);

assertSource(/String\.class,\s*int\.class,\s*int\.class,\s*sourceArgsType,\s*String\.class/.test(finder),
    'six-argument constructor matcher includes trailing String');
assertSource(/String\.class,\s*int\.class,\s*int\.class,\s*sourceArgsType\s*\)/.test(finder),
    'five-argument constructor matcher remains as fallback');
assertSource(/SendTextRequestArguments\.create\(/.test(requestBuilder),
    'message API routes constructor calls through the argument adapter');
assertSource(/SendTextRequestArguments\.isSupported\(/.test(method(api, '    public boolean isAvailable(')),
    'message API availability rejects unsupported constructor signatures');
assertSource(/if \(!isSendTextLocatorCacheCurrent\(\)\)\s*\{\s*resolveSendTextMsg\(\);\s*saveCache\(\);/s.test(resolveAll),
    'stale snapshot text locator is re-resolved and persisted after cache restore');
assertSource(/if \(snapshotLoaded && isSendTextLocatorCacheCurrent\(\)\) return;[\s\S]*?if \(snapshotLoaded\)\s*\{\s*sendTextMsgClass = null;\s*sendTextMsgCtorLong = null;\s*sendTextMsgCtorObject = null;/s.test(resolveText),
    'stale restored constructor fields are cleared before the new locator runs');
assertSource(/SEND_TEXT_LOCATOR_VERSION_KEY/.test(method(dex, '    private void saveCache(')),
    'successful snapshot persists text locator version');

const version = dex.match(/SEND_TEXT_LOCATOR_VERSION\s*=\s*"([^"]+)"/);
if (!version) throw new Error('Missing production text locator cache version');
const fixtureDir = path.join(output, 'h/Hchat/dexkit');
fs.mkdirSync(fixtureDir, { recursive: true });
fs.writeFileSync(path.join(fixtureDir, 'SendTextFinderFixture.java'), `
package h.Hchat.dexkit;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import h.Hchat.utils.KavaReflector;
public final class SendTextFinderFixture {
    ${finder}
    ${exactFinder}
    public static Constructor<?> locate(Class<?> owner, Class<?> sourceType) {
        return new SendTextFinderFixture().findSendTextConstructor(owner, sourceType);
    }
}
`);
fs.writeFileSync(path.join(fixtureDir, 'SendTextRestoreFixture.java'), `
package h.Hchat.dexkit;
public final class SendTextRestoreFixture {
    static final String SEND_TEXT_LOCATOR_VERSION_KEY = "send.text.locator.version";
    static final String SEND_TEXT_LOCATOR_VERSION = "${version[1]}";
    public static final String CURRENT_VERSION = SEND_TEXT_LOCATOR_VERSION;
    private final CachePrefs cachePrefs;
    private boolean resolvedAll;
    private boolean cacheDescriptorInvalid;
    private int textResolutionCount;
    private int saveCount;
    private final String runtimeCacheKey = "fixture";
    public SendTextRestoreFixture(String version) { this.cachePrefs = new CachePrefs(version); }
    ${cacheVersionCheck}
    private boolean loadCache() { return true; }
    private void resolveSendTextMsg() { textResolutionCount++; }
    private void saveCache() {
        saveCount++;
        cachePrefs.putString(SEND_TEXT_LOCATOR_VERSION_KEY, SEND_TEXT_LOCATOR_VERSION);
    }
    private void logDetail(String message) {}
    private String shortKey(String value) { return value; }
    public void restoreCachedSnapshot() {
        ${cacheRestoreBranch}
    }
    public int textResolutionCount() { return textResolutionCount; }
    public int saveCount() { return saveCount; }
    public String savedVersion() { return cachePrefs.getString(SEND_TEXT_LOCATOR_VERSION_KEY, ""); }
    private static final class CachePrefs {
        private String version;
        CachePrefs(String version) { this.version = version; }
        String getString(String key, String fallback) {
            return SEND_TEXT_LOCATOR_VERSION_KEY.equals(key) && !version.isEmpty() ? version : fallback;
        }
        void putString(String key, String value) {
            if (SEND_TEXT_LOCATOR_VERSION_KEY.equals(key)) version = value;
        }
    }
}
`);
const utilsDir = path.join(output, 'h/Hchat/utils');
fs.mkdirSync(utilsDir, { recursive: true });
fs.writeFileSync(path.join(utilsDir, 'KavaReflector.java'), `
package h.Hchat.utils;
import java.lang.reflect.Constructor;
public final class KavaReflector {
    public static Constructor<?>[] declaredConstructors(Class<?> owner) {
        return owner.getDeclaredConstructors();
    }
}
`);

const source = path.join(root, 'app/src/main/java/h/Hchat/hooks/api/message/SendTextRequestArguments.java');
const regression = path.join(root, 'scripts/tests/send_text_request_compat/SendTextRequestArgumentsRegression.java');
const classes = path.join(output, 'classes');
fs.mkdirSync(classes);
function run(command, args) {
    const result = spawnSync(command, args, { cwd: root, encoding: 'utf8', timeout: 60000 });
    process.stdout.write(result.stdout || '');
    process.stderr.write(result.stderr || '');
    if (result.error || result.status !== 0) {
        throw result.error || new Error(command + ' failed: ' + result.status);
    }
}
run(process.env.JAVAC || 'javac', ['-d', classes, source, regression,
    path.join(fixtureDir, 'SendTextFinderFixture.java'),
    path.join(fixtureDir, 'SendTextRestoreFixture.java'),
    path.join(utilsDir, 'KavaReflector.java')]);
run(process.env.JAVA || 'java', ['-cp', classes, 'h.Hchat.hooks.api.message.SendTextRequestArgumentsRegression']);
console.log('Temporary test artifacts removed.');
