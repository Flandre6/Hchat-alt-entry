package h.Hchat.hooks.api.message;

import h.Hchat.dexkit.SendTextFinderFixture;
import h.Hchat.dexkit.SendTextRestoreFixture;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class SendTextRequestArgumentsRegression {
    public static final class FiveOnly {
        public FiveOnly(String talker, String content, int type, int flag, long source) {}
        public FiveOnly(String talker, String content, int type, int flag, Object source) {}
    }

    public static final class SixOnly {
        public SixOnly(String talker, String content, int type, int flag, long source, String msgSource) {}
        public SixOnly(String talker, String content, int type, int flag, Object source, String msgSource) {}
    }

    public static final class BothShapes {
        public BothShapes(String talker, String content, int type, int flag, long source) {}
        public BothShapes(String talker, String content, int type, int flag, long source, String msgSource) {}
    }

    public static final class InvalidShapes {
        public InvalidShapes(String talker, String content, int type, String flag, Object source, String msgSource) {}
        public InvalidShapes(String talker, String content, int type, int flag, Object source, Object msgSource) {}
        public InvalidShapes(String talker, String content, int type, int flag, Object source, String msgSource, int extra) {}
    }

    private static final List<String> failures = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        run("locator accepts five and six and prefers six", SendTextRequestArgumentsRegression::verifiesLocatorAcceptsFiveAndSixAndPrefersSix);
        run("exact parameter order and invalid signatures", SendTextRequestArgumentsRegression::verifiesExactParameterOrderAndRejectedSignatures);
        run("argument values and msgSource tail", SendTextRequestArgumentsRegression::verifiesArgumentValuesAndMsgSourceTail);
        run("cold miss does not complete cache", SendTextRequestArgumentsRegression::failedLookupDoesNotMarkCacheResolved);
        run("current version without constructor is invalid", SendTextRequestArgumentsRegression::currentVersionWithoutConstructorIsRejected);
        run("failed stale restore preserves constructor and can retry", SendTextRequestArgumentsRegression::failedStaleRestorePreservesAndRetries);
        run("restored valid cache is reused", SendTextRequestArgumentsRegression::validCacheIsReused);
        run("unrelated cached values survive text-state writes", SendTextRequestArgumentsRegression::unrelatedCacheItemIsPreserved);
        if (!failures.isEmpty()) {
            for (String failure : failures) System.err.println("FAIL " + failure);
            throw new AssertionError(failures.size() + " regression assertion(s) failed");
        }
        System.out.println("PASS text request compatibility and locator cache assertions");
    }

    private static void verifiesLocatorAcceptsFiveAndSixAndPrefersSix() throws Exception {
        Constructor<?> five = SendTextFinderFixture.locate(FiveOnly.class, long.class);
        equal(5, five.getParameterCount(), "legacy five-argument constructor remains supported");

        Constructor<?> six = SendTextFinderFixture.locate(SixOnly.class, Object.class);
        equal(6, six.getParameterCount(), "six-argument constructor is located");
        equal(String.class, six.getParameterTypes()[5], "sixth parameter is String msgSource");

        Constructor<?> preferred = SendTextFinderFixture.locate(BothShapes.class, long.class);
        equal(6, preferred.getParameterCount(), "six-argument form wins when both overloads exist");
    }

    private static void verifiesExactParameterOrderAndRejectedSignatures() throws Exception {
        equal(null, SendTextFinderFixture.locate(InvalidShapes.class, Object.class),
                "wrong parameter order and invalid tails are not located");
        check(!SendTextRequestArguments.isSupported(
                InvalidShapes.class.getConstructor(String.class, String.class, int.class,
                        String.class, Object.class, String.class)), "reversed flag/source order is rejected");
        check(!SendTextRequestArguments.isSupported(
                InvalidShapes.class.getConstructor(String.class, String.class, int.class,
                        int.class, Object.class, Object.class)), "non-String msgSource is rejected");
        check(!SendTextRequestArguments.isSupported(
                InvalidShapes.class.getConstructor(String.class, String.class, int.class,
                        int.class, Object.class, String.class, int.class)), "extra parameter is rejected");
        check(SendTextRequestArguments.create(
                InvalidShapes.class.getConstructor(String.class, String.class, int.class,
                        int.class, Object.class, Object.class), "talker", "body", 1, 2,
                new Object()) == null, "invalid constructor cannot build a request");
    }

    private static void verifiesArgumentValuesAndMsgSourceTail() throws Exception {
        Object sourceArgs = new Object();
        Object[] fiveLong = create(FiveOnly.class.getConstructor(String.class, String.class,
                int.class, int.class, long.class), null);
        array(new Object[]{"talker", "body", 1, 2, 0L}, fiveLong, "five-long order");

        Object[] fiveObject = create(FiveOnly.class.getConstructor(String.class, String.class,
                int.class, int.class, Object.class), sourceArgs);
        array(new Object[]{"talker", "body", 1, 2, sourceArgs}, fiveObject, "five-object order");

        Object[] sixLong = create(SixOnly.class.getConstructor(String.class, String.class,
                int.class, int.class, long.class, String.class), null);
        array(new Object[]{"talker", "body", 1, 2, 0L, null}, sixLong, "six-long msgSource tail");

        Object[] sixObject = create(SixOnly.class.getConstructor(String.class, String.class,
                int.class, int.class, Object.class, String.class), sourceArgs);
        array(new Object[]{"talker", "body", 1, 2, sourceArgs, null}, sixObject, "six-object msgSource tail");

        check(SendTextRequestArguments.create(
                SixOnly.class.getConstructor(String.class, String.class, int.class, int.class,
                        long.class, String.class), "talker", "body", 1, 2, sourceArgs) == null,
                "long source overload rejects non-null sourceArgs");
    }

    private static void failedLookupDoesNotMarkCacheResolved() {
        SendTextRestoreFixture fixture = new SendTextRestoreFixture("");
        fixture.setSnapshotLoaded(false);
        fixture.locate();
        fixture.saveTextState();
        check(!fixture.savedResolved(), "a miss persists resolved=false");
        check(!fixture.cacheCurrent(), "a miss cannot make the text locator current");

        fixture.nextCandidates(SixOnly.class);
        fixture.locate();
        fixture.saveTextState();
        equal(2, fixture.lookupCount(), "failed lookup is retried only on the next explicit locate");
        check(fixture.locatorResolved(), "successful retry marks the locator resolved");
        check(fixture.savedResolved(), "successful retry persists resolved=true");
        check(fixture.cacheCurrent(), "successful retry makes the structurally valid cache current");
    }

    private static void currentVersionWithoutConstructorIsRejected() {
        SendTextRestoreFixture fixture = new SendTextRestoreFixture(SendTextRestoreFixture.CURRENT_VERSION);
        fixture.seedCache(SendTextRestoreFixture.CURRENT_VERSION, true);
        fixture.setSnapshotLoaded(true);
        fixture.restoreState();
        check(!fixture.locatorResolved(), "cache restoration rejects a missing constructor");
        check(!fixture.cacheCurrent(), "version plus resolved marker cannot replace structural validation");
        fixture.locate();
        equal(1, fixture.lookupCount(), "invalid current marker triggers one targeted lookup");
        check(!fixture.locatorResolved(), "failed targeted lookup stays unresolved in memory");
        fixture.saveTextState();
        check(!fixture.savedResolved(), "failed targeted lookup persists unresolved state");
    }

    private static void failedStaleRestorePreservesAndRetries() throws Exception {
        SendTextRestoreFixture fixture = new SendTextRestoreFixture("0");
        fixture.seedCache("0", true);
        fixture.installRestoredClass(FiveOnly.class);
        Constructor<?> original = fixture.longConstructor();
        fixture.setSnapshotLoaded(true);
        fixture.restoreState();
        fixture.locate();
        record(FiveOnly.class.equals(fixture.textClass()),
                "failed stale lookup retains the same-runtime five-arg owner");
        record(original != null && original.equals(fixture.longConstructor()),
                "failed stale lookup retains the valid constructor");
        record(!fixture.locatorResolved(), "failed stale lookup remains eligible for explicit retry");
        record(!fixture.cacheCurrent(), "stale result is not falsely marked current");
        fixture.saveTextState();
        fixture.restoreState();
        record(!fixture.locatorResolved(), "failed stale result restores as unresolved");

        fixture.nextCandidates(BothShapes.class);
        fixture.locate();
        record(fixture.lookupCount() == 2,
                "failed stale restore is queried again on the next explicit locate");
        record(BothShapes.class.equals(fixture.textClass()), "successful retry adopts the newer candidate");
        record(fixture.longConstructor() != null && fixture.longConstructor().getParameterCount() == 6,
                "new six-arg overload outranks legacy five-arg");
        fixture.saveTextState();
        record(fixture.cacheCurrent(), "successful retry persists a valid current descriptor");
    }

    private static void validCacheIsReused() {
        SendTextRestoreFixture fixture = new SendTextRestoreFixture(SendTextRestoreFixture.CURRENT_VERSION);
        fixture.seedCache(SendTextRestoreFixture.CURRENT_VERSION, true);
        fixture.installRestoredClass(FiveOnly.class);
        fixture.setSnapshotLoaded(true);
        fixture.restoreState();
        check(fixture.locatorResolved(), "valid legacy five-arg descriptor restores as complete");
        check(fixture.cacheCurrent(), "valid current five-arg cache is accepted");
        fixture.locate();
        equal(0, fixture.lookupCount(), "valid cache is reused without a targeted query");
    }

    private static void unrelatedCacheItemIsPreserved() {
        SendTextRestoreFixture fixture = new SendTextRestoreFixture("");
        fixture.seedOtherCacheItem("keep");
        fixture.saveTextState();
        equal("keep", fixture.otherCacheItem(), "text-state persistence leaves unrelated cache items alone");
    }

    private interface CheckedAction { void run() throws Exception; }

    private static void run(String name, CheckedAction action) {
        try {
            action.run();
        } catch (Throwable error) {
            failures.add(name + ": " + error.getMessage());
        }
    }

    private static Object[] create(Constructor<?> constructor, Object sourceArgs) {
        return SendTextRequestArguments.create(constructor, "talker", "body", 1, 2, sourceArgs);
    }

    private static void array(Object[] expected, Object[] actual, String label) {
        if (!Arrays.equals(expected, actual)) {
            throw new AssertionError(label + ": expected=" + Arrays.toString(expected)
                    + " actual=" + Arrays.toString(actual));
        }
    }

    private static void equal(Object expected, Object actual, String label) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new AssertionError(label + ": expected=" + expected + " actual=" + actual);
        }
    }

    private static void check(boolean value, String label) {
        if (!value) throw new AssertionError(label);
    }

    private static void record(boolean value, String label) {
        if (!value) failures.add(label);
    }
}
