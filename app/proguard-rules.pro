# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile




-keep class com.google.gson.** { *; }
-keep class * extends com.google.gson.TypeAdapter { *; }
-keep class * implements java.lang.reflect.InvocationHandler { *; }
-keep class * extends java.lang.reflect.Proxy { *; }
-keepattributes Signature, Exceptions, InnerClasses
-keepattributes *Annotation*
-keep class * implements java.lang.reflect.InvocationHandler { *; }
-keep interface * { *; }
-keep public class com.com.nsicyber.vinylscan.data.model.response.**  { *; }
-keep public class com.com.nsicyber.vinylscan.domain.**  { *; }
-keepclassmembers class com.com.nsicyber.vinylscan.data.model.response.**  { *; }
-keepclassmembers class com.com.nsicyber.vinylscan.domain.**  { *; }
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response