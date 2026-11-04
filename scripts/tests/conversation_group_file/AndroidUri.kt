package android.net
class Uri(val value: String) { val scheme: String? get() = "content"; val lastPathSegment: String? get() = value }
