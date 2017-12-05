package com.lyric.android.app.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import java.util.Random;

/**
 * 通知工具类
 * @author lyricgan
 * @date 2017/9/30 15:02
 */
public class NotificationUtils {

    private NotificationUtils() {
    }

    public static Notification build(Context context, CharSequence title, CharSequence content, Intent intent) {
        return build(context, title, content, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static Notification build(Context context, CharSequence title, CharSequence content, int requestCode, Intent intent, int flags) {
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(context, requestCode, intent, flags);
        return build(context, title, content, true, null, pendingIntent);
    }

    public static Notification build(Context context, CharSequence title, CharSequence content, PendingIntent pendingIntent) {
        return build(context, title, content, true, null, pendingIntent);
    }

    public static Notification build(Context context, CharSequence title, CharSequence content, boolean autoCancel, RemoteViews remoteViews, PendingIntent pendingIntent) {
        NotificationCompat.Builder builder = defaultBuilder(context, title, content);
        builder.setContentIntent(pendingIntent);
        builder.setTicker(content);
        builder.setAutoCancel(autoCancel);
        if (remoteViews != null) {
            builder.setContent(remoteViews);
        }
        builder.setWhen(System.currentTimeMillis());
        builder.setShowWhen(true);
        return builder.build();
    }

    public static NotificationCompat.Builder defaultBuilder(Context context, CharSequence title, CharSequence content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(title);
        builder.setContentText(content);
        return builder;
    }

    public static void notify(Context context, Notification notification) {
        notify(context, null, randomNotifyId(), notification);
    }

    public static void notify(Context context, int id, Notification notification) {
        notify(context, null, id, notification);
    }

    public static void notify(Context context, String tag, int id, Notification notification) {
        NotificationManager nm = getNotificationManager(context);
        if (nm != null) {
            nm.notify(tag, id, notification);
        }
    }

    public static int randomNotifyId() {
        return new Random().nextInt(Integer.MAX_VALUE);
    }

    public static void cancel(Context context, int id) {
        cancel(context, null, id);
    }

    public static void cancel(Context context, String tag, int id) {
        NotificationManager nm = getNotificationManager(context);
        if (nm != null) {
            nm.cancel(tag, id);
        }
    }

    public static void cancelAll(Context context) {
        NotificationManager nm = getNotificationManager(context);
        if (nm != null) {
            nm.cancelAll();
        }
    }

    public static NotificationManager getNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }
}
