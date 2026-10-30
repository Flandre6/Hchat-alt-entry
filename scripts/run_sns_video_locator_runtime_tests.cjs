// Compile the current DexFinder method bodies and cache blocks verbatim with isolated Android/DexKit stubs.
// This exercises the production logic, but does not replace a full Android integration test.
const fs = require('node:fs');
const os = require('node:os');
const path = require('node:path');
const cp = require('node:child_process');
const root = path.resolve(__dirname, '..');
const source = fs.readFileSync(process.env.SNS_FINDER_SOURCE || path.join(root, 'app/src/main/java/h/Hchat/dexkit/DexFinder.java'), 'utf8');
function method(name, access = 'private') {
  const index = source.indexOf(name + ' {');
  const start = source.lastIndexOf('    ' + access + ' ', index);
  if (start < 0 || !source.slice(start, source.indexOf('{', start)).includes(name)) throw new Error(`missing method ${name}`);
  let i = source.indexOf('{', start), depth = 0, end = i;
  for (; end < source.length; end++) { if (source[end] === '{') depth++; if (source[end] === '}' && --depth === 0) break; }
  if (depth !== 0) throw new Error(`unbalanced ${name}`);
  return source.slice(start, end + 1);
}
function section(startText, endText) {
  const start = source.indexOf(startText), end = source.indexOf(endText, start);
  if (start < 0 || end < 0) throw new Error(`missing cache section ${startText}`);
  return source.slice(start, end);
}
const imageLoad = 'snsAddImageMethod = loadMethod("snsAddImageMethod");';
const restore = section(imageLoad, 'snsCommitMethod = loadMethod("snsCommitMethod");').slice(imageLoad.length);
const save = section('putMethod(editor, "snsAddVideoMethod", snsAddVideoMethod);', 'putMethod(editor, "snsCommitMethod", snsCommitMethod);');
const harness = `package h.Hchat.dexkit;
import java.lang.reflect.Method;
import java.util.*;
class DexFinderHarness {
 static final String TAG = "test", CACHE_KEY = "cache.key";
 String runtimeCacheKey = "runtime";
 static final String SNS_VIDEO_LOCATOR_VERSION_KEY = "sns.video.locator.version", SNS_VIDEO_LOCATOR_VERSION = "1";
 boolean snsVideoLocatorResolved, cacheDescriptorInvalid, resolvedAll, failLoad;
 Class<?> snsUploadPackHelperClass, snsUploadManagerClass; ClassLoader classLoader;
 Method snsAddVideoMethod, snsSetContentMethod, snsSetSdkIdMethod, snsSetSdkAppNameMethod, snsAddImageMethod, snsCommitMethod, snsUploadManagerGetterMethod, snsShareAppMsgMethod, snsUploadCheckMethod;
 SharedPreferences cachePrefs = new SharedPreferences(); FakeDex dexKit = new FakeDex();
 String mkMethodUsingStrings(String s) { return s; }
 void logDetail(String s) {}
 boolean ensureSnapshotLoaded() { return resolvedAll; }
 boolean hasSnsUploadApi() { return false; }
 boolean isSnsUploadPackHelperClass(Class<?> c) { return c != null; }
 boolean isSnsUploadManagerClass(Class<?> c) { return c != null; }
 void collectSendTextClassCandidates(List<Class<?>> c, int n, String[][] anchors) {}
 void collectSendTextMethodOwnerCandidates(List<Class<?>> c, int n, String[][] anchors) {}
 String methodName(Method m) { return ""; }
 Method loadMethod(String key) { if (failLoad) { cacheDescriptorInvalid=true; return null; } return cachePrefs.method; }
 void putMethod(SharedPreferences.Editor editor, String key, Method m) { editor.putMethod(key,m); }
 Method findSnsHelperChainMethod(String s) { return null; }
 Method findSnsAddImageMethod(Class<?> c) { return null; }
 Method findSnsCommitMethod(Class<?> c) { return null; }
 Method findSnsUploadManagerGetter(Class<?> c) { return null; }
 Method findSnsShareAppMsgMethod(Class<?> c) { return null; }
 Method findSnsUploadCheckMethod(Class<?> c) { return null; }
 ${method('findSnsAddVideoMethod(Class<?> helperClass)')}
 ${method('isSnsAddVideoMethod(Class<?> helperClass, Method method)')}
 ${method('resolveSnsUploadMethods()')}
 ${method('resolveSnsUploadApi()', 'public')}
 Method locate(Class<?> c) { return findSnsAddVideoMethod(c); }
 void resolve() { resolveSnsUploadMethods(); }
 void resolvePublic() { resolveSnsUploadApi(); }
 void restore() { ${restore} }
 void saveVideo() { SharedPreferences.Editor editor = cachePrefs.edit(); ${save} editor.commit(); }
 static class DexCacheIdentity { static boolean sameRuntime(String a, String b) { return a != null && !a.isEmpty() && a.equals(b); } }
 static class KavaReflector { static Method accessible(Method m) { return m; } }
 static class MethodData { Method method; boolean error; MethodData(Method m) { method=m; } Method getMethodInstance(ClassLoader loader) { if (error) throw new RuntimeException(); return method; } }
 static class FakeDex { Map<String,List<MethodData>> rows = new HashMap<>(); Set<String> failures = new HashSet<>(); int calls;
   List<MethodData> findMethod(String anchor) { calls++; if (failures.contains(anchor)) throw new RuntimeException(); return rows.getOrDefault(anchor, Collections.emptyList()); }
   void both(Method... methods) { List<MethodData> list = new ArrayList<>(); for (Method m: methods) list.add(new MethodData(m)); rows.put("addSightObjectByPath",list); rows.put("produceSightByPath",list); }
 }
 static class SharedPreferences { Map<String,String> values = new HashMap<>(); Method method; int commits; boolean commitSuccess = true;
   SharedPreferences() { values.put("cache.key", "runtime"); }
   String getString(String key, String fallback) { return values.getOrDefault(key,fallback); }
   Editor edit() { return new Editor(this); }
   static class Editor { SharedPreferences prefs; Map<String,String> pending = new HashMap<>(); Method method; boolean setMethod;
     Editor(SharedPreferences p) { prefs=p; }
     Editor putString(String k,String v) { pending.put(k,v); return this; }
     void putMethod(String k,Method m) { method=m; setMethod=true; }
     boolean commit() { if (!prefs.commitSuccess) return false; prefs.values.putAll(pending); if(setMethod) prefs.method=method; prefs.commits++; return true; }
   }
 }
}`;
const tmp = fs.mkdtempSync(path.join(os.tmpdir(), 'sns-video-r1-'));
try {
  const harnessFile = path.join(tmp, 'DexFinderHarness.java');
  fs.writeFileSync(harnessFile, harness);
  const logStub = path.join(tmp, 'HLog.java');
  fs.writeFileSync(logStub, 'package h.Hchat.utils; public class HLog { public static void e(String s, Throwable t) {} }');
  const utility = path.join(root, 'app/src/main/java/h/Hchat/hooks/api/sns/SnsVideoRequestArguments.java');
  const fixture = path.join(root, 'scripts/tests/sns_video_locator/LocatorRegression.java');
  cp.execFileSync('javac', ['-d', tmp, harnessFile, logStub, utility, fixture], {stdio:'inherit'});
  cp.execFileSync('java', ['-cp', tmp, 'h.Hchat.dexkit.LocatorRegression'], {stdio:'inherit'});
} finally { fs.rmSync(tmp, {recursive:true, force:true}); }
