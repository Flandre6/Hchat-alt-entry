// Compile production text-constructor and locator cache methods without Gradle or Android.
const fs = require('node:fs');
const os = require('node:os');
const path = require('node:path');
const { spawnSync } = require('node:child_process');

const root = path.resolve(__dirname, '..');
const output = fs.mkdtempSync(path.join(os.tmpdir(), 'hchat-send-text-compat-'));
process.on('exit', () => fs.rmSync(output, { recursive: true, force: true }));
const dexPath = process.env.HCHAT_DEX_FINDER_SOURCE
    || path.join(root, 'app/src/main/java/h/Hchat/dexkit/DexFinder.java');
const apiPath = path.join(root, 'app/src/main/java/h/Hchat/hooks/api/message/WeChatMessageApi.java');
const dex = fs.readFileSync(dexPath, 'utf8');
const api = fs.readFileSync(apiPath, 'utf8');

function method(source, signature) {
    const start = source.indexOf(signature);
    if (start < 0) return null;
    const open = source.indexOf('{', start);
    let depth = 0;
    for (let i = open; i < source.length; i++) {
        if (source[i] === '{') depth++;
        if (source[i] === '}' && --depth === 0) return source.slice(start, i + 1);
    }
    throw new Error('Unclosed production method: ' + signature);
}

function required(source, signature) {
    const found = method(source, signature);
    if (!found) throw new Error('Missing production method: ' + signature);
    return found;
}

const finder = required(dex, '    private Constructor<?> findSendTextConstructor(');
const exactFinder = required(dex, '    private Constructor<?> findCtorByExactTypes(');
const cacheVersionCheck = required(dex, '    private boolean isSendTextLocatorCacheCurrent(');
const resolveText = required(dex, '    public void resolveSendTextMsg(');
const cacheRestore = method(dex, '    private void restoreSendTextLocatorCacheState(');
const cacheSave = method(dex, '    private void saveSendTextLocatorCache(');
const apiValid = method(dex, '    private boolean isSendTextRequestApiValid(');
const requestBuilder = required(api, '    private Object newMessageRequest(');
const resolveAll = required(dex, '    public synchronized void resolveAll(');
const saveCache = required(dex, '    private void saveCache(');
const loadCache = required(dex, '    private boolean loadCache(');
const availability = required(api, '    public boolean isAvailable(');
function assertSource(condition, label) {
    if (!condition) throw new Error('FAIL ' + label);
}
assertSource(/String\.class,\s*int\.class,\s*int\.class,\s*sourceArgsType,\s*String\.class/.test(finder),
    'six-argument matcher includes trailing String msgSource');
assertSource(/String\.class,\s*int\.class,\s*int\.class,\s*sourceArgsType\s*\)/.test(finder),
    'five-argument constructor remains a compatible fallback');
assertSource(finder.indexOf('Constructor<?> six') < finder.indexOf('return findCtorByExactTypes'),
    'six-argument overload is preferred over five-argument overload');
assertSource(/SendTextRequestArguments\.create\(/.test(requestBuilder)
        && /SendTextRequestArguments\.isSupported\(/.test(availability),
    'message API retains the request argument adapter and signature guard');
assertSource(/if \(!isSendTextLocatorCacheCurrent\(\)\)\s*\{\s*resolveSendTextMsg\(\);\s*saveCache\(\);/s.test(resolveAll),
    'stale snapshots use one targeted text re-resolution');
if (cacheSave) {
    assertSource(/saveSendTextLocatorCache\(editor\)/.test(saveCache),
        'snapshot persistence invokes the production text-cache state writer');
} else {
    assertSource(/editor\.putString\(SEND_TEXT_LOCATOR_VERSION_KEY,\s*SEND_TEXT_LOCATOR_VERSION\)/.test(saveCache),
        'pre-fix snapshot writer is the unconditional-version behavior under regression');
}
if (cacheRestore) {
    assertSource(/restoreSendTextLocatorCacheState\(\)/.test(loadCache),
        'snapshot restoration invokes the production text-cache state restorer');
}
const version = dex.match(/SEND_TEXT_LOCATOR_VERSION\s*=\s*"([^"]+)"/);
if (!version) throw new Error('Missing production text locator cache version');
const resolvedKey = (dex.match(/CACHE_SEND_TEXT_LOCATOR_RESOLVED\s*=\s*"([^"]+)"/) || [])[1]
    || 'locator.send.text.resolved';

// The fallback methods deliberately model the pre-fix production statements so this same
// regression suite can first demonstrate the old failure, then execute the production helpers.
const restoreMethod = cacheRestore || `private void restoreSendTextLocatorCacheState() {
        sendTextMsgLocatorResolved = isSendTextLocatorCacheCurrent();
    }`;
const saveMethod = cacheSave || `private void saveSendTextLocatorCache(android.content.SharedPreferences.Editor editor) {
        editor.putString(SEND_TEXT_LOCATOR_VERSION_KEY, SEND_TEXT_LOCATOR_VERSION);
    }`;
const validMethod = apiValid || `private boolean isSendTextRequestApiValid() {
        return sendTextMsgClass != null
                && (sendTextMsgCtorLong != null || sendTextMsgCtorObject != null);
    }`;

const fixtureDir = path.join(output, 'h/Hchat/dexkit');
const utilsDir = path.join(output, 'h/Hchat/utils');
const androidDir = path.join(output, 'android/content');
fs.mkdirSync(fixtureDir, { recursive: true });
fs.mkdirSync(utilsDir, { recursive: true });
fs.mkdirSync(androidDir, { recursive: true });
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
import android.content.SharedPreferences;
import h.Hchat.utils.KavaReflector;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
public final class SendTextRestoreFixture {
    private static final String TAG = "fixture";
    static final String SEND_TEXT_LOCATOR_VERSION_KEY = "send.text.locator.version";
    static final String SEND_TEXT_LOCATOR_VERSION = "${version[1]}";
    static final String CACHE_SEND_TEXT_LOCATOR_RESOLVED = "${resolvedKey}";
    static final String RESOLVED_KEY = CACHE_SEND_TEXT_LOCATOR_RESOLVED;
    static final String VERSION_KEY = SEND_TEXT_LOCATOR_VERSION_KEY;
    public static final String CURRENT_VERSION = SEND_TEXT_LOCATOR_VERSION;
    private final CachePrefs cachePrefs;
    private boolean snapshotLoaded;
    private boolean sendTextMsgLocatorResolved;
    private Class<?> sendTextMsgClass;
    private Constructor<?> sendTextMsgCtorLong;
    private Constructor<?> sendTextMsgCtorObject;
    private int lookupCount;
    private List<Class<?>> nextCandidates = new ArrayList<>();

    public SendTextRestoreFixture(String version) { this.cachePrefs = new CachePrefs(version); }
    ${validMethod}
    ${cacheVersionCheck}
    ${restoreMethod}
    ${saveMethod}
    ${finder}
    ${exactFinder}
    ${resolveText}

    private boolean ensureSnapshotLoaded() { return snapshotLoaded; }
    private void collectSendTextClassCandidates(List<Class<?>> out, int max, String[][] anchors) {
        lookupCount++;
        out.addAll(nextCandidates);
        nextCandidates = new ArrayList<>();
    }
    private void collectSendTextMethodOwnerCandidates(List<Class<?>> out, int max, String[][] anchors) {}
    private void logDetail(String message) {}
    private String methodName(java.lang.reflect.Method method) { return method == null ? "null" : method.getName(); }

    public void setSnapshotLoaded(boolean loaded) { snapshotLoaded = loaded; }
    public void nextCandidates(Class<?>... candidates) {
        nextCandidates = new ArrayList<>();
        for (Class<?> candidate : candidates) nextCandidates.add(candidate);
    }
    public void installRestoredClass(Class<?> owner) {
        sendTextMsgClass = owner;
        sendTextMsgCtorLong = findSendTextConstructor(owner, long.class);
        sendTextMsgCtorObject = findSendTextConstructor(owner, Object.class);
    }
    public void restoreState() { restoreSendTextLocatorCacheState(); }
    public void locate() { resolveSendTextMsg(); }
    public void saveTextState() {
        SharedPreferences.Editor editor = cachePrefs.edit();
        saveSendTextLocatorCache(editor);
        editor.apply();
    }
    public boolean cacheCurrent() { return isSendTextLocatorCacheCurrent(); }
    public boolean locatorResolved() { return sendTextMsgLocatorResolved; }
    public Class<?> textClass() { return sendTextMsgClass; }
    public Constructor<?> longConstructor() { return sendTextMsgCtorLong; }
    public int lookupCount() { return lookupCount; }
    public String savedVersion() { return cachePrefs.getString(VERSION_KEY, ""); }
    public boolean savedResolved() { return cachePrefs.getBoolean(RESOLVED_KEY, false); }
    public void seedCache(String version, boolean resolved) {
        cachePrefs.putString(VERSION_KEY, version);
        cachePrefs.putBoolean(RESOLVED_KEY, resolved);
    }
    public void seedOtherCacheItem(String value) { cachePrefs.putString("other.cache.item", value); }
    public String otherCacheItem() { return cachePrefs.getString("other.cache.item", ""); }

    private static final class CachePrefs implements SharedPreferences {
        private final java.util.Map<String, Object> values = new java.util.HashMap<>();
        CachePrefs(String version) { putString(VERSION_KEY, version); }
        public String getString(String key, String fallback) {
            Object value = values.get(key);
            return value instanceof String ? (String) value : fallback;
        }
        public boolean getBoolean(String key, boolean fallback) {
            Object value = values.get(key);
            return value instanceof Boolean ? (Boolean) value : fallback;
        }
        void putString(String key, String value) { values.put(key, value); }
        void putBoolean(String key, boolean value) { values.put(key, value); }
        public Editor edit() { return new CacheEditor(this); }
    }
    private static final class CacheEditor implements SharedPreferences.Editor {
        private final CachePrefs prefs;
        CacheEditor(CachePrefs prefs) { this.prefs = prefs; }
        public SharedPreferences.Editor putString(String key, String value) { prefs.putString(key, value); return this; }
        public SharedPreferences.Editor putBoolean(String key, boolean value) { prefs.putBoolean(key, value); return this; }
        public void apply() {}
    }
}
`);
fs.writeFileSync(path.join(androidDir, 'SharedPreferences.java'), `
package android.content;
public interface SharedPreferences {
    String getString(String key, String fallback);
    boolean getBoolean(String key, boolean fallback);
    Editor edit();
    interface Editor {
        Editor putString(String key, String value);
        Editor putBoolean(String key, boolean value);
        void apply();
    }
}
`);
fs.writeFileSync(path.join(utilsDir, 'KavaReflector.java'), `
package h.Hchat.utils;
import java.lang.reflect.Constructor;
public final class KavaReflector {
    public static Constructor<?>[] declaredConstructors(Class<?> owner) {
        return owner.getDeclaredConstructors();
    }
}
`);
fs.writeFileSync(path.join(utilsDir, 'HLog.java'), `
package h.Hchat.utils;
public final class HLog {
    public static void e(String message, Throwable error) {}
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
    path.join(utilsDir, 'KavaReflector.java'), path.join(utilsDir, 'HLog.java'),
    path.join(androidDir, 'SharedPreferences.java')]);
run(process.env.JAVA || 'java', ['-cp', classes, 'h.Hchat.hooks.api.message.SendTextRequestArgumentsRegression']);
console.log('Temporary test artifacts removed.');
