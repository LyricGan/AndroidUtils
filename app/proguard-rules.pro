# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\dev_android_studio\android_sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose

-dontwarn android.support.**

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keepattributes Signature
-keepattributes *Annotation*
-keepattributes *JavascriptInterface*

-dontwarn java.**
-dontwarn javax.**
-dontwarn com.**
-dontwarn org.**
-keep class java.** {*;}
-keep class javax.** {*;}
-keep class com.** {*;}
-keep class org.** {*;}

-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.support.v4.app.Fragment
-keep public class com.lyric.android.app.view.**{*;}
-ignorewarning
#实体类
-keep class com.lyric.android.app.mvvm.model.** {*;}

-keepclasseswithmembers class * {# 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity { # 保持自定义控件类不被混淆
    public void *(android.view.View);
}

-keepclassmembers enum * {     # 保持枚举 enum 类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class android.support.v4.** { *; }
-keep interface android.support.v4.app.** { *; }

##autoscrollviewpager
-keep class cn.trinea.android.** { *; }
-keepclassmembers class cn.trinea.android.** { *; }
-dontwarn cn.trinea.android.**

##友盟
-dontwarn com.umeng.**
-dontwarn com.umeng.message.**

#enventBus
-keepclassmembers class ** {
    public void onEvent*(**);
}
# Only required if you use AsyncExecutor
-keepclassmembers class * extends de.greenrobot.event.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

#greendao
-keep class de.greenrobot.** {*;}
# Don't remove any methods that have the @Subscribe annotation
-keepclassmembers class ** {
    @de.greenrobot.event.Subscribe <methods>;
}
#保持greenDao的方法不被混淆
-keepclassmembers class * extends de.greenrobot.dao.AbstractDao {
public static java.lang.String TABLENAME; }
-keep class **$Properties

#shareSDK
-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class **.R$* {*;}
-keep class **.R{*;}
-dontwarn cn.sharesdk.**
-dontwarn **.R$*

#volley
-keep class com.android.volley.** {*;}
-keep class com.android.volley.toolbox.** {*;}
-keep class com.android.volley.Response$* { *; }
-keep class com.android.volley.Request$* { *; }
-keep class com.android.volley.RequestQueue$* { *; }
-keep class com.android.volley.toolbox.HurlStack$* { *; }
-keep class com.android.volley.toolbox.ImageLoader$* { *; }

#gson
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.examples.android.model.** { *; }

#pinyin4j
-keep class com.hpl.sparta.**{*;}
-keep class com.hpl.sparta.xpath.**{*;}
-keep class net.sourceforge.pinyin4j.**{*;}
-keep class net.sourceforge.pinyin4j.format.**{*;}
-keep class pinyindb.**{*;}
#baidumap sdk
-keep class com.baidu.** {*;}
-keep class vi.com.** {*;}
-dontwarn com.baidu.**

#jsoup
-keep class org.jsoup.**{*;}
#httpmine
-keep class org.apache.http.**{*;}
