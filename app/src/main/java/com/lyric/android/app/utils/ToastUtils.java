package com.lyric.android.app.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

/**
 * Toast工具类，用来弹出提示
 * 
 * @author lyricgan
 * @created 2014-3-4
 * 
 */
public class ToastUtils {
	private static Handler mHandler = new Handler(Looper.getMainLooper());
    private static Toast mToast = null;
    private static final Object mSynObject = new Object();

    private ToastUtils() {
    }

    /**
     * Toast发送消息，默认Toast.LENGTH_SHORT
     * @param context Context
     * @param message 消息
     */
    public static void show(Context context, String message) {
        show(context, message, Toast.LENGTH_SHORT);
    }

    /**
     * Toast发送消息，默认Toast.LENGTH_SHORT
     * @param context Context
     * @param messageId 消息文本资源ID
     */
    public static void show(Context context, int messageId) {
        show(context, messageId, Toast.LENGTH_SHORT);
    }

    /**
     * Toast发送消息
     * @param context Context
     * @param messageId 消息文本资源ID
     * @param duration 持续时间
     */
    public static void show(final Context context, final int messageId, final int duration) {
        show(context, context.getString(messageId), duration);
    }

    /**
     * Toast发送消息
     * @param context Context
     * @param message 消息
     * @param duration 持续时间
     */
    public static void show(final Context context, final String message, final int duration) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                synchronized (mSynObject) {
                    if (mToast != null) {
                        mToast.setText(message);
                        mToast.setDuration(duration);
                    } else {
                        mToast = Toast.makeText(context, message, duration);
                    }
                    mToast.show();
                }
            }
        });
    }

    public static void cancel() {
        if (mToast != null) {
            mToast.cancel();
        }
    }
}
