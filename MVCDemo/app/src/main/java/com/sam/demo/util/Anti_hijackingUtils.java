package com.sam.demo.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import com.sam.demo.MyApplication;
import com.sam.demo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.sam.demo.mine.SysSettingActivity.currentTime;

/**
 * 作者：sam.huang
 * <p>
 * 创建日期：2018/12/25
 * <p>
 * 描述：
 */
public class Anti_hijackingUtils {

    String packageName="com.autobio.autolink";
    boolean isAppBackground=false;

    /**
     * 用于执行定时任务
     */
    private Timer timer = null;

    /**
     * 用于保存当前任务
     */
    private List<MyTimerTask> tasks = null;

    /**
     * 唯一实例
     */
    private static Anti_hijackingUtils anti_hijackingUtils;

    private Anti_hijackingUtils() {
        // 初始化
        tasks = new ArrayList<MyTimerTask>();
        timer = new Timer();
    }

    /**
     * 获取唯一实例
     *
     * @return 唯一实例
     */
    public static Anti_hijackingUtils getinstance() {
        if (anti_hijackingUtils == null) {
            anti_hijackingUtils = new Anti_hijackingUtils();
        }
        return anti_hijackingUtils;
    }

    /**
     * 在activity的onPause()方法中调用
     *
     * @param activity
     */
    public void onPause(final Activity activity) {
        MyTimerTask task = new MyTimerTask(activity);
        tasks.add(task);
        timer.schedule(task, 1000);
    }

    /**
     * 在activity的onResume()方法中调用
     */
    public void onResume() {
        if (tasks.size() > 0) {
            tasks.get(tasks.size() - 1).setCanRun(false);
            tasks.remove(tasks.size() - 1);
        }
        NotificationUtils.cancleNotification();

    }

    /**
     * 自定义TimerTask类
     */
    class MyTimerTask extends TimerTask {
        /**
         * 任务是否有效
         */
        private boolean canRun = true;
        private Activity activity;

        public void setCanRun(boolean canRun) {
            this.canRun = canRun;
        }

        public MyTimerTask(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void run() {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //首先判断程序是否在运行
                    if (canRun) {
                        //其次判断程序是否在前台
                        if (!isAppOnForeground())
                        {
                            // 程序退到后台，进行风险警告
                            ToastUtil.showToast(activity,MyApplication.getInstance().getResources().getString(R.string.app_name));
                            //NotificationUtils.showNotification(activity);
                            SharedPreferencesUtils.setParam(activity,currentTime,TimeUtils.getCurrentDate().getTime());
                        }
                        tasks.remove(MyTimerTask.this);
                    }
                }
            });
        }
    }


    /**
     * 应用是否在前台运行
     *
     * @return true：在前台运行；false：已经被切到后台了
     */
    public boolean isAppOnForeground() {
        ActivityManager activityManager= (ActivityManager) MyApplication.getInstance().getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses != null) {
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    if (appProcess.processName.equals(packageName)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
