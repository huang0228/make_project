package com.sam.demo.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.sam.demo.receiver.TimeChangeReceiver;

/**
 * ================================================
 * 爱代码，不爱BUG
 * 描    述：服务启动广播接收器，使得广播接收器可以在程序退出后在后台继续执行，接收系统时间时区修改广播事件，时间变换时间
 * 修订历史：
 * ================================================
 */
public class TimeService extends Service {

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {

        super.onCreate();

        TimeChangeReceiver receiver=new TimeChangeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(receiver, filter);


    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

}