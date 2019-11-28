package com.sam.demo.constants;

import android.os.Environment;

/**
 * ================================================
 * 爱代码，不爱BUG
 * 作    者：赵腾飞
 * 版    本：1.0.1
 * 创建日期：2018/3/9
 * 描    述：
 * 修订历史：
 * ================================================
 */

public class Common {
    //token的时效性
    public static int TOKEN_DURATION=0;
    //服务器时间戳
    public static long SERVER_TIMES_TAMP=0;
    //在外部储存缓存数据 (只缓存不重要的数据)
    public static final String APP_SDCAR_PATH=Environment.getExternalStorageDirectory().getPath() + "/com.autobio.autolink";
    public static final String CACHE_DIR = APP_SDCAR_PATH + "/cache";
    //在外部储存下载数据
    public static final String DOWNLOAD_DIR =APP_SDCAR_PATH+ "/download";
}
