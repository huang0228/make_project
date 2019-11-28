package com.sam.demo.util;

import android.widget.ImageView;

import com.sam.demo.MyApplication;
import com.sam.demo.R;
import com.sam.demo.widget.GlideCircleTransform;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.io.File;

/**
 * ================================================
 * 爱代码，不爱BUG
 * 作    者：赵腾飞
 * 版    本：1.0.1
 * 创建日期：2018/4/17
 * 描    述：Glide管理类
 * 修订历史：
 * ================================================
 */

public class GlideManager {
   private static RequestManager gliderRequestManager= Glide.with(MyApplication.AppContent);

    /**
     * 展示图片
     * @param url
     * @param imageView
     */
    public static void displayImage(String url, ImageView imageView) {
        if(url.startsWith("http")){
            displayImageByUrl(url,imageView);
        }else {
            displayImageByFilePath(url,imageView);

        }


    }
    private static void displayImageByUrl(String url, ImageView imageView) {

        gliderRequestManager
                .load(url)
                .error(R.mipmap.ic_tolerant)
                .into(imageView);
    }

    private static void displayImageByFilePath(String filePath, ImageView imageView) {
        File file=new File(filePath);
        gliderRequestManager
                .load(file)
                .error(R.mipmap.ic_tolerant)
                .into(imageView);
    }

    /**
     * 展示圆形图片
     * @param url
     * @param imageView
     */
    public static void displayCircleImage(String url, ImageView imageView) {
        gliderRequestManager
                .load(url)//
                .error(R.mipmap.ic_tolerant)
                .centerCrop()
                .transform(new GlideCircleTransform(MyApplication.AppContent))
                .into(imageView);
    }

    /**
     * add by sam.huang
     * @param url
     * @param imageView
     */
    // 加载圆形网络图片
    public static void displayGlideCircleImage(String url, final ImageView imageView) {
        gliderRequestManager
                .load(url)
                .error(R.mipmap.ic_tolerant)
                .crossFade()
                .bitmapTransform(new GlideCircleTransform(MyApplication.getInstance()))
                .into(imageView);
    }
}
