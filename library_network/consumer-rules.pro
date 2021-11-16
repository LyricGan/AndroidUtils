# okio begin
-dontwarn okio.**
# okio end

# okhttp
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Gson
-dontwarn sun.misc.**
-dontwarn com.google.gson.**
-keep class sun.misc.Unsafe {*;}
-keep class com.google.gson.** {*;}