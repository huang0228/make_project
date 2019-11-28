package com.sam.demo.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import com.sam.demo.R;

import me.leolin.shortcutbadger.ShortcutBadger;

public class BadgeIntentService extends IntentService {

    private static final String NOTIFICATION_CHANNEL = "com.autobio.autolink";

    private int notificationId = 0;

    public BadgeIntentService() {
        super("BadgeIntentService");
    }

    private NotificationManager mNotificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            int badgeCount = intent.getIntExtra("badgeCount", 0);

            mNotificationManager.cancel(notificationId);
            notificationId++;

            Notification.Builder builder = new Notification.Builder(getApplicationContext())
                .setContentTitle("")
                .setContentText("")
                .setSmallIcon(R.mipmap.ic_launcher_round);

           /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setupNotificationChannel();

                builder.setChannelId(NOTIFICATION_CHANNEL);
            }*/

            Notification notification = builder.build();
            ShortcutBadger.applyNotification(getApplicationContext(), notification, badgeCount);
            mNotificationManager.notify(notificationId, notification);
        }
    }

   /* @TargetApi(Build.VERSION_CODES.O)
    private void setupNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL, MyApplication.getInstance().getResources().getString(R.string.app_name),
                NotificationManager.IMPORTANCE_DEFAULT);

        mNotificationManager.createNotificationChannel(channel);
    }*/
}