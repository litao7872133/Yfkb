package com.yfkk.cardbag.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.yfkk.cardbag.R;

public class NotificationUtils {

    public static void postNotification(Context context, int notifyId, String title, String msg, Bundle bundle, Class activityClass) {
        int channelId = 1;
        Notification.Builder builder;
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(String.valueOf(channelId), "消息推送", NotificationManager.IMPORTANCE_DEFAULT);
            // 设置通知出现时的闪灯（如果 android 设备支持的话）
            channel.enableLights(true);
            manager.createNotificationChannel(channel);
            builder = new Notification.Builder(context, String.valueOf(channelId));
        } else {
            builder = new Notification.Builder(context);
        }
        // 需要跳转指定的页面
        Intent intent = new Intent(context, activityClass);
        for (String key : bundle.keySet()) {
            intent.putExtra(key, bundle.get(key).toString());
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notifyId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setTicker("新消息")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(msg)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true) // 点击取消
                .setContentIntent(pendingIntent);
        manager.cancel(notifyId);
        manager.notify(notifyId, builder.build());
    }

}
