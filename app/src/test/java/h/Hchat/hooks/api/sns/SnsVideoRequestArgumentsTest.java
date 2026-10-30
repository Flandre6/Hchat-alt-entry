package h.Hchat.hooks.api.sns;

import org.junit.Test;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class SnsVideoRequestArgumentsTest {
    public static final class Helpers {
        public boolean four(String path, String thumb, String desc, String md5) { return true; }
        public boolean five(String path, String thumb, String desc, String md5, String extra) { return true; }
        public void wrongReturn(String a, String b, String c, String d, String e) {}
        public boolean wrongType(String a, String b, String c, String d, Object e) { return true; }
        public boolean wrongCount(String a, String b, String c) { return true; }
    }

    public static final class OtherHelper {
        public boolean five(String path, String thumb, String desc, String md5, String extra) { return true; }
    }

    @Test public void supportsLegacyFourAndVerifiedFiveStringLayouts() throws Exception {
        Method four = Helpers.class.getMethod("four", String.class, String.class, String.class, String.class);
        Method five = Helpers.class.getMethod("five", String.class, String.class, String.class,
                String.class, String.class);

        assertTrue(SnsVideoRequestArguments.isCompatibleMethod(Helpers.class, four));
        assertTrue(SnsVideoRequestArguments.isCompatibleMethod(Helpers.class, five));
        assertArrayEquals(new Object[]{"video", "thumb", "caption", "md5"},
                SnsVideoRequestArguments.create(four, "video", "thumb", "caption", "md5"));
        assertArrayEquals(new Object[]{"video", "thumb", "caption", "md5", ""},
                SnsVideoRequestArguments.create(five, "video", "thumb", "caption", "md5"));
    }

    @Test public void rejectsWrongOwnerReturnTypeAndCompleteParameterShape() throws Exception {
        Method five = Helpers.class.getMethod("five", String.class, String.class, String.class,
                String.class, String.class);
        assertFalse(SnsVideoRequestArguments.isCompatibleMethod(OtherHelper.class, five));
        assertFalse(SnsVideoRequestArguments.isCompatibleMethod(Helpers.class,
                Helpers.class.getMethod("wrongReturn", String.class, String.class, String.class,
                        String.class, String.class)));
        assertFalse(SnsVideoRequestArguments.isCompatibleMethod(Helpers.class,
                Helpers.class.getMethod("wrongType", String.class, String.class, String.class,
                        String.class, Object.class)));
        assertFalse(SnsVideoRequestArguments.isCompatibleMethod(Helpers.class,
                Helpers.class.getMethod("wrongCount", String.class, String.class, String.class)));
        assertNull(SnsVideoRequestArguments.create(
                Helpers.class.getMethod("wrongReturn", String.class, String.class, String.class,
                        String.class, String.class), "v", "t", "d", "m"));
    }

    @Test public void rejectsAnInvalidCachedMethodInsteadOfTreatingItAsRestored() throws Exception {
        Method wrongType = Helpers.class.getMethod("wrongType", String.class, String.class,
                String.class, String.class, Object.class);

        assertFalse(SnsVideoRequestArguments.isCompatibleMethod(Helpers.class, wrongType));
    }
}
