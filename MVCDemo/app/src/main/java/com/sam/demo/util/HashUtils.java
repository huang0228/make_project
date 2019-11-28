package com.sam.demo.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

/**
 * ================================================
 * 爱代码，不爱BUG
 * 作    者：赵腾飞
 * 版    本：1.0.1
 * 创建日期：2018/2/24
 * 描    述：位运算和生成key
 * 修订历史：
 * ================================================
 */

public class HashUtils {
    private static final char[] TARCODE = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    public static String getHash(String data, String type) {
        String str = "";
        if (data != null) {
            try {
                MessageDigest messageDigest = MessageDigest.getInstance(type);
                messageDigest.update(data.getBytes());
                str = handleBytes(messageDigest.digest());
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
        return str;
    }

    //生成key,这一步是重点
    private static String handleBytes(byte[] data) {
        int i = data.length;
        StringBuilder sb = new StringBuilder(i * 2);
        for (int j = 0; j < i; j++) {
            //对每个字节位运算并且和16进制62做&;运算,保证产生的索引在TARCODE的内
            sb.append(TARCODE[(data[j] >> 4 & 0x3D)]);
            sb.append(TARCODE[(data[j] &0x3D)]);
        } return sb.toString();
    }
    public static String sHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
            }
            String result = hexString.toString();
            return result.substring(0, result.length()-1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}