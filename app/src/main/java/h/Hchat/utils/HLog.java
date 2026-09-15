package h.Hchat.utils;

import de.robv.android.xposed.XposedBridge;

/**
 * LSPosed treats Throwable logs as error logs. Use this only for real failures.
 */
public final class HLog {
    private HLog() {
    }

    public static void e(String message) {
        XposedBridge.log(new RuntimeException(message));
    }

    public static void e(String message, Throwable throwable) {
        if (throwable == null) {
            e(message);
            return;
        }
        XposedBridge.log(new RuntimeException(message, throwable));
    }

    /**
     * Informational module log. Keep successful hook/install diagnostics out of
     * LSPosed's error stream while preserving the existing error API above.
     */
    public static void i(String message) {
        if (message == null) return;
        XposedBridge.log(message);
    }
}
