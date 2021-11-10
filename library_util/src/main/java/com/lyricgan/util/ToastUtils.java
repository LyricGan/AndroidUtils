package com.lyricgan.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

/**
 * Toast工具类
 * 
 * @author Lyric Gan
 * 
 */
public class ToastUtils {
	private static Handler mHandler = new Handler(Looper.getMainLooper());
    private static Toast mToast = null;
    private static final Object mSynObject = new Object();

    private ToastUtils() {
    }

    public static void show(Context context, String message) {
        show(context, message, Toast.LENGTH_SHORT);
    }

    public static void show(Context context, int messageId) {
        show(context, messageId, Toast.LENGTH_SHORT);
    }

    public static void show(final Context context, final int messageId, final int duration) {
        show(context, context.getString(messageId), duration);
    }

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
