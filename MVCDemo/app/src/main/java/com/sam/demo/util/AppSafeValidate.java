package com.sam.demo.util;

import android.content.Context;
import android.os.Process;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 作者：sam.huang
 * <p>
 * 创建日期：2019/1/9
 * <p>
 * 描述：app应用校验
 */
public class AppSafeValidate {


    /**
     * 根据apk MD5摘要获取对应的哈希值
     * @param context
     * @return
     */
    public static String getApkHashValue(Context context)
    {
        String apkPath = context.getPackageCodePath(); // 获取Apk包存储路径
        try {
            MessageDigest dexDigest = MessageDigest.getInstance("MD5");
            byte[] bytes = new byte[1024];
            int byteCount;
            FileInputStream fis = new FileInputStream(new File(apkPath)); // 读取apk文件
            while ((byteCount = fis.read(bytes)) != -1) {
                dexDigest.update(bytes, 0, byteCount);
            }
           /* BigInteger bigInteger = new BigInteger(1, dexDigest.digest()); // 计算apk文件的哈希值
            String sha = bigInteger.toString(16);*/
           //解决MD5在特殊情况下丢失0的情况
            StringBuffer buf = new StringBuffer("");
            byte b[]=dexDigest.digest();
            int i;
            for (int offset=0;offset<b.length;offset++)
            {
                i=b[offset];
                if (i<0){
                    i+=256;
                }
                if (i<16){
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            fis.close();
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * 通过检查apk包的MD5摘要值来判断代码文件是否被篡改
     *
     * @param orginalMD5 原始Apk包的MD5值
     */
    public static void apkVerifyWithMD5(Context context, String orginalMD5) {
        String apkPath = context.getPackageCodePath(); // 获取Apk包存储路径
        try {
            MessageDigest dexDigest = MessageDigest.getInstance("MD5");
            byte[] bytes = new byte[1024];
            int byteCount;
            FileInputStream fis = new FileInputStream(new File(apkPath)); // 读取apk文件
            while ((byteCount = fis.read(bytes)) != -1) {
                dexDigest.update(bytes, 0, byteCount);
            }
            BigInteger bigInteger = new BigInteger(1, dexDigest.digest()); // 计算apk文件的哈希值
            String sha = bigInteger.toString(16);
            fis.close();
            if (!sha.equals(orginalMD5)) { // 将得到的哈希值与原始的哈希值进行比较校验
                Process.killProcess(Process.myPid()); // 验证失败则退出程序
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过检查classes.dex文件的CRC32摘要值来判断文件是否被篡改
     *
     * @param orginalCRC 原始classes.dex文件的CRC值
     */
    public static void apkVerifyWithCRC(Context context, String orginalCRC) {
        String apkPath = context.getPackageCodePath(); // 获取Apk包存储路径
        try {
            ZipFile zipFile = new ZipFile(apkPath);
            ZipEntry dexEntry = zipFile.getEntry("classes.dex"); // 读取ZIP包中的classes.dex文件
            String dexCRC = String.valueOf(dexEntry.getCrc()); // 得到classes.dex文件的CRC值
            if (!dexCRC.equals(orginalCRC)) { // 将得到的CRC值与原始的CRC值进行比较校验
                Process.killProcess(Process.myPid()); // 验证失败则退出程序
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 通过检查签名文件classes.dex文件的哈希值来判断代码文件是否被篡改
     *
     * @param orginalSHA 原始Apk包的SHA-1值
     */
    public static void apkVerifyWithSHA(Context context, String orginalSHA) {
        String apkPath = context.getPackageCodePath(); // 获取Apk包存储路径
        try {
            MessageDigest dexDigest = MessageDigest.getInstance("SHA-1");
            byte[] bytes = new byte[1024];
            int byteCount;
            FileInputStream fis = new FileInputStream(new File(apkPath)); // 读取apk文件
            while ((byteCount = fis.read(bytes)) != -1) {
                dexDigest.update(bytes, 0, byteCount);
            }
            BigInteger bigInteger = new BigInteger(1, dexDigest.digest()); // 计算apk文件的哈希值
            String sha = bigInteger.toString(16);
            fis.close();
            if (!sha.equals(orginalSHA)) { // 将得到的哈希值与原始的哈希值进行比较校验
                Process.killProcess(Process.myPid()); // 验证失败则退出程序
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
