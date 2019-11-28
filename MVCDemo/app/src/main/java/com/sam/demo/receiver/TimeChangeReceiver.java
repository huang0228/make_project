package com.sam.demo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.sam.demo.constants.Common;
import com.sam.demo.util.MyLog;
import com.sam.demo.util.TokenManager;

/**
 * ================================================
 * 创建日期：2018/3/7
 * 描    述：系统时间改变广播接收者事件(时间的自动流失，每一分钟手机会发出一次广播)
 * 修订历史：
 * ================================================
 */
public class TimeChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        MyLog.d("时间发生变化。。。");

        Common.SERVER_TIMES_TAMP+=60*1000;

        TokenManager.timer -= 60;


        //判断token时效性是否小于10分钟，小于10分钟更新token
        if (TokenManager.timer >0&&TokenManager.timer <= 10 * 60) {
            //TokenManager.requestToken(null);
        }
    }

}

