package com.sam.demo.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ================================================
 * 爱代码，不爱BUG
 * 作    者：赵腾飞
 * 版    本：1.0.1
 * 创建日期：2018/3/12
 * 描    述：
 * 修订历史：
 * ================================================
 */

public class Utils {
//    /**
//     * 获取设备名称
//     * @return
//     */
//    public static String getDeviceName(){
//        BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
//        return myDevice.getName();
//    }


    /**
     *
     *<p>
     *@description 转换javabean ,将class2中的属性值赋值给class1，如果class1属性有值，则不覆盖
     *</p>
     *@param class1 基准类,被赋值对象
     *@param class2 提供数据的对象
     *@throws Exception
     * @see
     */
    public static void converJavaBean(Object class1, Object class2) {
        Class<?> clazz1 = class1.getClass();
        Class<?> clazz2 = class2.getClass();
        Field[] fields1 = clazz1.getDeclaredFields();
        Field[] fields2 = clazz2.getDeclaredFields();
        for (int i = 0; i < fields1.length; i++) {
            try {
                fields1[i].setAccessible(true);
                fields2[i].setAccessible(true);
                Object obg1 = fields1[i].get(class1);
                Object obg2 = fields2[i].get(class2);
                if (null == fields1[i].get(class1) && null != fields2[i].get(class2)) {
                    fields1[i].set(class1, fields2[i].get(class2));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取状态栏高度
     * @param activity
     * @return > 0 success; <= 0 fail
     */
    public static int getStatusHeight(Activity activity) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView(
        ).getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName(
                        "com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(
                        localClass.getField("status_bar_height").get(
                                localObject).toString());
                statusHeight = activity.getResources(
                ).getDimensionPixelSize(i5);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getDeviceName() {
        return android.os.Build.MODEL;
    }
    /**
     * 获取手机厂商
     *
     * @return  手机厂商
     */
    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 获取设备序列号
     * @return
     */
    public static String getDeviceID(Context mContext) {
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceID=tm.getDeviceId();

        if (StringUtils.isEmpty(deviceID)){
            deviceID= Settings.System.getString(mContext.getContentResolver(), Settings.System.ANDROID_ID);
        }
        return deviceID;
    }
    /**
     * 获取当前手机系统版本号
     *
     * @return  系统版本号
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }
    /**
     * 检测网络是否可用
     *
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isAvailable();
    }
    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    /**
     * 是否强制更新
     * @return
     */
    public static boolean  forceUpdate(Context context,String newVersionName){
        String oldVersionName= getAppVersionName(context);
        oldVersionName.substring(1, oldVersionName.length());
        newVersionName.substring(1, newVersionName.length());
        String olds[]= oldVersionName.split("\\.");
        String news[]= newVersionName.split("\\.");
        return (Integer.valueOf(olds[0])<Integer.valueOf(news[0])||Integer.valueOf(olds[1])<Integer.valueOf(news[1]));
    }

    /**
     * 判断手机号码的正则表达式
     *
     * @param phoneNumber
     * @return
     */
    public static boolean CheckPhoneNum(String phoneNumber) {
        //
       // $pattern = '/^[a-zA-Z0-9]{6,10}$/';

            String pattern = "^[0-9]\\d{5,14}$";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(phoneNumber);
            return m.matches();

    }

    /**add by sam.huang
     * date:2018.08.13
     * 手机号验证
     * @param
     * @return
     */
    public static boolean CheckMobilePhoneNum(String mobileNums) {
        /**
         * 判断字符串是否符合手机号码格式
         * 移动号段: 134,135,136,137,138,139,147,150,151,152,157,158,159,170,178,182,183,184,187,188
         * 联通号段: 130,131,132,145,155,156,170,171,175,176,185,186
         * 电信号段: 133,149,153,170,173,177,180,181,189
         * @param str
         * @return 待检测的字符串
         */
        String telRegex = "^((13[0-9])|(14[0-9])|(15[^4])|(16[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))\\d{8}$";
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else {
            Pattern r = Pattern.compile(telRegex);
            Matcher m = r.matcher(mobileNums);
            return m.matches();
        }
         //return mobileNums.matches(telRegex);
    }



    //邮箱正则
    public static boolean checkEmaile(String emaile){
        /**
         *   ^匹配输入字符串的开始位置
         *   $结束的位置
         *   \转义字符 eg:\. 匹配一个. 字符  不是任意字符 ，转义之后让他失去原有的功能
         *   \t制表符
         *   \n换行符
         *   \\w匹配字符串  eg:\w不能匹配 因为转义了
         *   \w匹配包括字母数字下划线的任何单词字符
         *   \s包括空格制表符换行符
         *   *匹配前面的子表达式任意次
         *   .小数点可以匹配任意字符
         *   +表达式至少出现一次
         *   ?表达式0次或者1次
         *   {10}重复10次
         *   {1,3}至少1-3次
         *   {0,5}最多5次
         *   {0,}至少0次 不出现或者出现任意次都可以 可以用*号代替
         *   {1,}至少1次  一般用+来代替
         *   []自定义集合     eg:[abcd]  abcd集合里任意字符
         *   [^abc]取非 除abc以外的任意字符
         *   |  将两个匹配条件进行逻辑“或”（Or）运算
         *   [1-9] 1到9 省略123456789
         *    邮箱匹配 eg: ^[a-zA-Z_]{1,}[0-9]{0,}@(([a-zA-z0-9]-*){1,}\.){1,3}[a-zA-z\-]{1,}$
         *
         */
        String RULE_EMAIL = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        //正则表达式的模式 编译正则表达式
        Pattern p = Pattern.compile(RULE_EMAIL);
        //正则表达式的匹配器
        Matcher m = p.matcher(emaile);
        //进行正则匹配\
        return m.matches();
    }

    /**
     * 验证身份证号的正则
     *
     * @param idcard
     * @return
     */
    public static boolean CheckIdCard(String idcard) {
        String pattern = "\\d{15}|\\d{17}[0-9Xx]";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(idcard);
        return m.matches();
    }
    /**
     * dp转px
     *
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param context
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

}
