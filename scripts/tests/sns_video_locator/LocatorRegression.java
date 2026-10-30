package h.Hchat.dexkit;
import h.Hchat.hooks.api.sns.SnsVideoRequestArguments;
import java.lang.reflect.Method;
import java.util.Arrays;

public class LocatorRegression {
    static class Helper {
        public boolean four(String a,String b,String c,String d) { return true; }
        public boolean five(String a,String b,String c,String d,String e) { return true; }
        public boolean other(String a,String b,String c,String d) { return true; }
        public void wrong(String a,String b,String c,String d) {}
    }
    static final String KEY = "sns.video.locator.version";
    static int failures;
    interface Check { void run() throws Exception; }
    static void check(String name, Check test) {
        try { test.run(); System.out.println("PASS: " + name); }
        catch (Throwable e) { failures++; System.out.println("FAIL: " + name + ": " + e); }
    }
    static void require(boolean condition, String message) { if (!condition) throw new AssertionError(message); }
    static Method method(String name, int count) throws Exception {
        Class<?>[] types = new Class<?>[count]; Arrays.fill(types, String.class);
        return Helper.class.getDeclaredMethod(name, types);
    }
    static DexFinderHarness finder() { DexFinderHarness f = new DexFinderHarness(); f.snsUploadPackHelperClass=Helper.class; return f; }
    public static void main(String[] args) throws Exception {
        Method four=method("four",4), five=method("five",5), other=method("other",4), wrong=method("wrong",4);
        check("missing helper remains unresolved", () -> {
            DexFinderHarness f=finder(); f.snsVideoLocatorResolved=true; f.snsUploadPackHelperClass=null; f.resolve();
            require(!f.snsVideoLocatorResolved,"resolve marked missing helper successful");
            f.locate(null); require(!f.snsVideoLocatorResolved,"locate marked missing helper successful");
            f.saveVideo(); require(!"1".equals(f.cachePrefs.values.get(KEY)),"saved success version for missing helper");
        });
        check("zero and ambiguous candidates remain unresolved", () -> {
            DexFinderHarness f=finder(); require(f.locate(Helper.class)==null && !f.snsVideoLocatorResolved,"zero candidates marked successful");
            f.dexKit.both(four,other); require(f.locate(Helper.class)==null && !f.snsVideoLocatorResolved,"ambiguous candidates marked successful");
            f.saveVideo(); require(!"1".equals(f.cachePrefs.values.get(KEY)),"saved success version for ambiguity");
        });
        check("ambiguous candidates remain unresolved", () -> {
            DexFinderHarness f=finder(); f.dexKit.both(four,other);
            require(f.locate(Helper.class)==null && !f.snsVideoLocatorResolved,"ambiguous candidates marked successful");
        });
        check("direct null helper remains unresolved", () -> {
            DexFinderHarness f=finder(); f.locate(null);
            require(!f.snsVideoLocatorResolved,"null helper marked successful");
        });
        check("query exception is retryable", () -> {
            DexFinderHarness f=finder(); f.dexKit.both(five); f.dexKit.failures.add("produceSightByPath");
            require(f.locate(Helper.class)==null && !f.snsVideoLocatorResolved,"partial query accepted");
            f.dexKit.failures.clear(); require(f.locate(Helper.class).equals(five) && f.snsVideoLocatorResolved,"explicit retry failed");
        });
        check("method resolution exception remains retryable", () -> {
            DexFinderHarness f=finder(); f.dexKit.both(four);
            f.dexKit.rows.get("addSightObjectByPath").get(0).error=true;
            require(f.locate(Helper.class)==null && !f.snsVideoLocatorResolved,"candidate exception accepted");
            f.dexKit.rows.get("addSightObjectByPath").get(0).error=false;
            require(four.equals(f.locate(Helper.class)) && f.snsVideoLocatorResolved,"candidate retry failed");
        });
        check("old valid four argument cache survives failed migration", () -> {
            DexFinderHarness f=finder(); f.cachePrefs.method=four; f.cachePrefs.values.put("unrelated","keep"); f.restore();
            require(four.equals(f.snsAddVideoMethod),"valid cached method lost in memory");
            require(four.equals(f.cachePrefs.method),"valid cached method overwritten");
            require(!"1".equals(f.cachePrefs.values.get(KEY)),"failed migration marked successful");
            require("keep".equals(f.cachePrefs.values.get("unrelated")),"unrelated cache changed");
        });
        check("failed legacy migration retries and updates only video cache", () -> {
            DexFinderHarness f=finder(); f.cachePrefs.method=four; f.cachePrefs.values.put("unrelated","keep");
            f.restore(); f.resolvedAll=true; f.dexKit.both(five); f.resolvePublic();
            require(five.equals(f.snsAddVideoMethod) && five.equals(f.cachePrefs.method),"explicit retry did not update video cache");
            require("1".equals(f.cachePrefs.values.get(KEY)),"successful retry did not update version");
            require("keep".equals(f.cachePrefs.values.get("unrelated")),"retry changed unrelated cache");
        });
        check("ambiguous and exceptional legacy migration preserves existing method", () -> {
            for (boolean exception : new boolean[]{false,true}) {
                DexFinderHarness f=finder(); f.cachePrefs.method=four; f.cachePrefs.values.put("unrelated","keep");
                f.dexKit.both(four,other); if (exception) f.dexKit.failures.add("produceSightByPath"); f.restore();
                require(four.equals(f.snsAddVideoMethod) && four.equals(f.cachePrefs.method),"cached method lost");
                require(!f.snsVideoLocatorResolved && !"1".equals(f.cachePrefs.values.get(KEY)),"failure marked successful");
                require("keep".equals(f.cachePrefs.values.get("unrelated")),"unrelated cache changed");
            }
        });
        check("failed cache commit does not upgrade version", () -> {
            DexFinderHarness f=finder(); f.cachePrefs.method=four; f.cachePrefs.commitSuccess=false; f.dexKit.both(five); f.restore();
            require(f.snsVideoLocatorResolved && five.equals(f.snsAddVideoMethod),"successful locate lost");
            require(four.equals(f.cachePrefs.method) && !"1".equals(f.cachePrefs.values.get(KEY)),"failed commit changed cache");
        });
        check("explicit retry respects runtime identity", () -> {
            DexFinderHarness f=finder(); f.cachePrefs.method=four; f.restore(); f.resolvedAll=true;
            f.cachePrefs.values.put("cache.key","different"); f.dexKit.both(five); f.resolvePublic();
            require(five.equals(f.snsAddVideoMethod) && four.equals(f.cachePrefs.method),"wrong runtime cache changed");
            require(!"1".equals(f.cachePrefs.values.get(KEY)),"wrong runtime version changed");
        });
        check("legacy cache migrates to unique four and five helpers", () -> {
            for (Method candidate : new Method[]{four,five}) {
                DexFinderHarness f=finder(); f.cachePrefs.values.put("unrelated","keep"); f.dexKit.both(candidate); f.restore();
                require(candidate.equals(f.snsAddVideoMethod) && candidate.equals(f.cachePrefs.method),"method not restored");
                require("1".equals(f.cachePrefs.values.get(KEY)),"version not saved");
                require("keep".equals(f.cachePrefs.values.get("unrelated")),"unrelated cache changed");
                int calls=f.dexKit.calls; f.restore(); f.resolvedAll=true; f.resolvePublic(); require(f.dexKit.calls==calls,"current cache queried again");
            }
        });
        check("unreadable video descriptor does not invalidate other cache entries", () -> {
            DexFinderHarness f=finder(); f.failLoad=true; f.cachePrefs.values.put("unrelated","keep"); f.restore();
            require(!f.cacheDescriptorInvalid && !f.snsVideoLocatorResolved,"video load invalidated whole snapshot");
            require("keep".equals(f.cachePrefs.values.get("unrelated")),"other cache changed");
            f.cacheDescriptorInvalid=true; f.restore(); require(f.cacheDescriptorInvalid,"previous cache error was cleared");
        });
        check("stale resolved state retries invalid method", () -> {
            DexFinderHarness f=finder(); f.resolvedAll=true; f.snsVideoLocatorResolved=true; f.snsAddVideoMethod=wrong;
            f.dexKit.both(five); f.resolvePublic();
            require(five.equals(f.snsAddVideoMethod) && f.snsVideoLocatorResolved,"invalid resolved state blocked retry");
        });
        check("current version with empty method remains retryable", () -> {
            DexFinderHarness f=finder(); f.cachePrefs.values.put(KEY,"1"); f.restore();
            require(f.snsAddVideoMethod==null && !f.snsVideoLocatorResolved,"empty current cache accepted");
            f.resolvedAll=true; f.dexKit.both(five); f.resolvePublic();
            require(five.equals(f.cachePrefs.method) && f.snsVideoLocatorResolved,"empty current cache did not recover");
        });
        check("invalid current cache rejected", () -> {
            DexFinderHarness f=finder(); f.cachePrefs.method=wrong; f.cachePrefs.values.put(KEY,"1"); f.restore();
            require(f.snsAddVideoMethod==null && !f.snsVideoLocatorResolved,"invalid signature accepted");
        });
        check("four and five invocation arguments", () -> {
            require(Arrays.equals(new Object[]{"v","t","d","m"}, SnsVideoRequestArguments.create(four,"v","t","d","m")),"four arguments");
            require(Arrays.equals(new Object[]{"v","t","d","m",""}, SnsVideoRequestArguments.create(five,"v","t","d","m")),"five arguments");
            require(SnsVideoRequestArguments.create(wrong,"v","t","d","m")==null,"invalid arguments accepted");
        });
        if (failures != 0) throw new AssertionError(failures + " regression groups failed");
    }
}
