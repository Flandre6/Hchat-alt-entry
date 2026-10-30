package h.Hchat.hooks.api.message;

import h.Hchat.dexkit.SendTextFinderFixture;
import h.Hchat.dexkit.SendTextRestoreFixture;
import java.lang.reflect.Constructor;
import java.util.Arrays;

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

    public static void main(String[] args) throws Exception {
        verifiesLocatorAcceptsFiveAndSixAndPrefersSix();
        verifiesExactParameterOrderAndRejectedSignatures();
        verifiesArgumentValuesAndMsgSourceTail();
        verifiesLocatorCacheVersionAndRestorePath();
        System.out.println("PASS text request compatibility assertions");
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

    private static void verifiesLocatorCacheVersionAndRestorePath() {
        SendTextRestoreFixture legacy = new SendTextRestoreFixture("");
        legacy.restoreCachedSnapshot();
        equal(1, legacy.textResolutionCount(), "legacy snapshot re-runs text locator");
        equal(1, legacy.saveCount(), "legacy snapshot saves upgraded locator result");
        equal(SendTextRestoreFixture.CURRENT_VERSION, legacy.savedVersion(),
                "restored snapshot stores current locator version");

        SendTextRestoreFixture previous = new SendTextRestoreFixture("0");
        previous.restoreCachedSnapshot();
        equal(1, previous.textResolutionCount(), "previous locator version re-runs text locator");

        SendTextRestoreFixture current = new SendTextRestoreFixture(
                SendTextRestoreFixture.CURRENT_VERSION);
        current.restoreCachedSnapshot();
        equal(0, current.textResolutionCount(), "current locator version reuses cached result");
        equal(0, current.saveCount(), "current locator cache is not rewritten");
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
}
