package com.sam.demo.util;

/**
 * 作者：sam.huang
 * <p>
 * 创建日期：2018/9/10
 * <p>
 * 描述：防止双击点击事件
 */
public class DisableDoubleClickUtils {

    private static long lastTimeMillis;
    private static final long MIN_CLICK_INTERVAL = 1000;

    public static boolean isTimeEnabled()
    {
        long currentTimeMills=System.currentTimeMillis();
        if (currentTimeMills-lastTimeMillis>MIN_CLICK_INTERVAL)
        {
            lastTimeMillis=currentTimeMills;
            return true;
        }return false;
    }

}
