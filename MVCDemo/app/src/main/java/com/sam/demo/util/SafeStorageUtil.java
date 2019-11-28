package com.sam.demo.util;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;


/**
 * ================================================
 * 爱代码，不爱BUG
 * 作    者：赵腾飞
 * 版    本：1.0.1
 * 创建日期：2018/3/1
 * 描    述： AES加解密工具封装
 * 修订历史：
 * ================================================
 */

public class SafeStorageUtil {
    private static String AESKEY;

    /**
     * 获得应用程序包名
     * @param context
     * @return
     */
    private static String getPackageName(Context context) {
        return context.getPackageName();
    }

    /**
     * 设备IMEI号
     * @param mContext
     * @return
     */
    private static String getDeviceID(Context mContext) {
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    /**
     * 获取唯一标识
     *  @param mContext
     *  @return
     */
    private static String getAppUnique(Context mContext) {
        return getDeviceID(mContext) + getPackageName(mContext);
    }

    private static String getAESKey(Context context) {
        if (!TextUtils.isEmpty(AESKEY))
            return AESKEY;
        //取16位作为密钥key
        AESKEY = HashUtils.getHash(getAppUnique(context), "MD5").substring(0, 16);
        return AESKEY;
    }

    public static String encrypt(Context context, String data) {
        return SafeAESTool.encrypt(getAESKey(context), data);
    }

    public static String decrypt(Context context, String data) {
        return SafeAESTool.decrypt(getAESKey(context), data);
    }
}
