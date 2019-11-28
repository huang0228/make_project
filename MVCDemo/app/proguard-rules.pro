# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\sdk\sdk1/tools/proguard/proguard-android.txt
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

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
#Ӧ�ñ�����
-dontwarn com.tencent.**
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep class com.tencent.beaconselfupdate.** {*;}
-keep class com.tencent.hlyyb.** {*;}
-keep class com.tencent.tmapkupdatesdk.** {*;}
-keep class com.tencent.tmassistantbase.** {*;}
-keep class com.tencent.tmdownloader.** {*;}
-keep class com.tencent.tmassistantsdk.** {*;}
-keep class com.tencent.tmselfupdatesdk.** {*;}
-keep class com.tencent.yybsdk.apkpatch.** {*;}
-keep class com.tencent.assistant.sdk.remote.**{public protected private *;}
-keep public interface com.tencent.tmassistantbase.common.download.ITMAssistantDownloadClientListener{*;}
-keep class com.qq.** {*;}

#�������ͻ���
-keep class com.umeng.commonsdk.** {*;}
-dontwarn com.umeng.**
-dontwarn com.taobao.**
-dontwarn anet.channel.**
-dontwarn anetwork.channel.**
-dontwarn org.android.**
-dontwarn org.apache.thrift.**
-dontwarn com.xiaomi.**
-dontwarn com.huawei.**
-dontwarn com.meizu.**
#不混淆Province、City等实体类
-keepattributes InnerClasses,Signature
-keepattributes *Annotation*

-keep class cn.addapp.pickers.entity.** { *;}
#BUGLY
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
#Ӧ�ñ�����
-dontwarn com.tencent.**
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep class com.tencent.beaconselfupdate.** {*;}
-keep class com.tencent.hlyyb.** {*;}
-keep class com.tencent.tmapkupdatesdk.** {*;}
-keep class com.tencent.tmassistantbase.** {*;}
-keep class com.tencent.tmdownloader.** {*;}
-keep class com.tencent.tmassistantsdk.** {*;}
-keep class com.tencent.tmselfupdatesdk.** {*;}
-keep class com.tencent.yybsdk.apkpatch.** {*;}
-keep class com.tencent.assistant.sdk.remote.**{public protected private *;}
-keep public interface com.tencent.tmassistantbase.common.download.ITMAssistantDownloadClientListener{*;}
-keep class com.qq.** {*;}
-ignorewarnings
#友盟
-keep class com.umeng.commonsdk.** {*;}
-dontwarn com.taobao.**
-dontwarn anet.channel.**
-dontwarn anetwork.channel.**
-dontwarn org.android.**
-dontwarn org.apache.thrift.**
-dontwarn com.xiaomi.**
-dontwarn com.huawei.**



-keepattributes *Annotation*
-keep class com.taobao.** {*;}
-keep class com.sam.demo.entity.** {*;}
-keep class org.android.** {*;}
-keep class anet.channel.** {*;}
-keep class com.umeng.** {*;}
-keep class com.xiaomi.** {*;}
-keep class com.huawei.** {*;}
-keep class org.apache.thrift.** {*;}

-keep class com.alibaba.sdk.android.**{*;}
-keep class com.ut.**{*;}
-keep class com.ta.**{*;}

-keep public class **.R$*{
   public static final int *;
}

-dontwarn android.support.**
-keep class android.support.v4.** { *; }

#����ѡ������Log��ӡ���
-assumenosideeffects class android.util.Log {
   public static *** v(...);
   public static *** d(...);
   public static *** i(...);
   public static *** w(...);
 }
# ѡ����
-keepattributes InnerClasses,Signature
-keepattributes *Annotation*

-keep class cn.addapp.pickers.entity.** { *;}
#BUGLY
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}


-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.** { *; }
-keepattributes Signature
#butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

#okgo
#okhttp
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}

#okio
-dontwarn okio.**
-keep class okio.**{*;}

# EventBus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

-keepclasseswithmembers class * {
    public <init>(android.content.Context);}


#地图定位 混淆
 -keep class com.amap.api.location.**{*;}
    -keep class com.amap.api.fence.**{*;}
    -keep class com.autonavi.aps.amapapi.model.**{*;}