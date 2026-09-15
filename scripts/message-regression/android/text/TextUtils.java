package android.text;

// Minimal JVM fixture for message routing; not an Android implementation.
public final class TextUtils {
    public static boolean isEmpty(CharSequence value) {
        return value == null || value.length() == 0;
    }
}
