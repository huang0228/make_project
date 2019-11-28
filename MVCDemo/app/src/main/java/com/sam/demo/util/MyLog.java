package com.sam.demo.util;

import android.util.Log;

import static com.sam.demo.MyApplication.isDevelop;

/**
 * ================================================
 * 爱代码，不爱BUG
 * 作    者：赵腾飞
 * 版    本：1.0.1
 * 创建日期：2018/3/1
 * 描    述：log工具
 * 修订历史：
 * ================================================
 */
public class MyLog {
    public static void d(String msg) {
        if (isDevelop) {
            Log.i("hgs", msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isDevelop) {
            Log.i(tag, msg);
        }
    }


}
