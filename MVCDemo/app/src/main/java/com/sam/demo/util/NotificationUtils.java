package com.sam.demo.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.sam.demo.MyApplication;
import com.sam.demo.R;
import com.sam.demo.MainActivity;

/**
 * 通知帮助类
 *
 * by sam.huang
 * date:2018.12.27
 */

public class NotificationUtils {

    public static NotificationManager notificationManager=null;

    public static void showNotification(Context context) {
        notificationManager = (NotificationManager) MyApplication.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);

        Notification.Builder builder1 = new Notification.Builder(context);
        builder1.setSmallIcon(R.mipmap.ic_launcher_round); //设置图标
        builder1.setTicker(MyApplication.getInstance().getResources().getString(R.string.app_name) + "已登录");
        builder1.setContentTitle(MyApplication.getInstance().getResources().getString(R.string.app_name)); //设置标题
        builder1.setContentText(MyApplication.getInstance().getResources().getString(R.string.app_name)); //消息内容
        builder1.setWhen(System.currentTimeMillis()); //发送时间
        builder1.setDefaults(Notification.DEFAULT_LIGHTS|Notification.DEFAULT_SOUND); //设置默认的提示音，灯光
        builder1.setVibrate(new long[]{0});//振动方式
        builder1.setAutoCancel(true);//打开程序后图标消失
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        builder1.setContentIntent(pendingIntent);
        Notification notification1 = builder1.build();
        notificationManager.notify(1, notification1); // 通过通知管理器发送通知
    }


    /**
     * 启动应用后，状态栏的消息通知取消
     */
    public static void cancleNotification()
    {
        if (notificationManager!=null)
        notificationManager.cancel(1);
    }
}
