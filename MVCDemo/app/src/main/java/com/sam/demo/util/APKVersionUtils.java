package com.sam.demo.util;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * 作者：sam.huang
 * <p>
 * 创建日期：2018/8/24
 * <p>
 * 描述：
 */
public class APKVersionUtils {

    /**
     * 获取当前本地apk的版本
     *
     * @param mContext
     * @return
     */
    public static int getVersionCode(Context mContext) {
        int versionCode = 0;
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = mContext.getPackageManager().
                    getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取版本号名称
     *
     * @param context 上下文
     * @return
     */
    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

    /**
     * 获取包名
     * @param context
     * @return
     */
    public static String getPackageName(Context context)
    {
        String packageName="";
        try {
            packageName=context.getPackageManager().getPackageInfo(context.getPackageName(),0).packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageName;
    }

}
