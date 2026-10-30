const fs = require('node:fs');
const path = require('node:path');
const assert = require('node:assert/strict');

const root = path.resolve(__dirname, '..');
const read = file => fs.readFileSync(path.join(root, file), 'utf8');
const finder = read('app/src/main/java/h/Hchat/dexkit/DexFinder.java');
const api = read('app/src/main/java/h/Hchat/hooks/api/sns/WeChatSnsApi.kt');
const args = read('app/src/main/java/h/Hchat/hooks/api/sns/SnsVideoRequestArguments.java');

const resolver = finder.slice(
  finder.indexOf('private Method findSnsAddVideoMethod'),
  finder.indexOf('private Method findSnsCommitMethod')
);
assert.match(resolver, /new String\[\]\s*\{\s*"addSightObjectByPath"\s*,\s*"produceSightByPath"\s*\}/);
assert.match(resolver, /LinkedHashSet<Method> candidates/);
assert.match(resolver, /candidates\.size\(\) != 1/);
assert.match(args, /method\.getDeclaringClass\(\) != helperClass/);
assert.match(args, /method\.getReturnType\(\) != boolean\.class/);
assert.match(args, /types\.length != 4 && types\.length != 5/);
assert.match(args, /for \(Class<\?> type : types\) if \(type != String\.class\) return false/);
assert.match(api, /SnsVideoRequestArguments\.create\([\s\S]*?KavaReflector\.invoke\(addVideo, helper, \*it\)/);

const cacheRestoreStart = finder.indexOf('snsUploadPackHelperClass = loadClass("snsUploadPackHelperClass")');
const cacheRestoreEnd = finder.indexOf('snsCommitMethod = loadMethod("snsCommitMethod")', cacheRestoreStart);
assert.notEqual(cacheRestoreStart, -1);
assert.notEqual(cacheRestoreEnd, -1);
const cacheRestore = finder.slice(cacheRestoreStart, cacheRestoreEnd);
assert.match(cacheRestore, /snsAddVideoMethod = loadMethod\("snsAddVideoMethod"\)[\s\S]*?!isSnsAddVideoMethod\(snsUploadPackHelperClass, snsAddVideoMethod\)[\s\S]*?snsAddVideoMethod = null;[\s\S]*?cacheDescriptorInvalid = true;/);

console.log('PASS: four/five-String compatibility, anchored unique candidate, call argument adaptation, and cache signature rejection');
