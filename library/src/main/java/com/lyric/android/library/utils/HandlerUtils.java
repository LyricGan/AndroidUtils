package com.lyric.android.library.utils;

import android.os.Handler;
import android.os.Looper;

/**
 * @author lyricgan
 * @description Handler工具类
 * @time 2016/8/26 15:19
 */
public class HandlerUtils {
    private static final Handler mHandler = new Handler(Looper.getMainLooper());

    private HandlerUtils() {
    }

    public static boolean isMainThread() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }

    public static void post(Runnable runnable) {
        if (isMainThread()) {
            runnable.run();
        } else {
            mHandler.post(runnable);
        }
    }

    public static void postAtFrontOfQueue(Runnable runnable) {
        mHandler.postAtFrontOfQueue(runnable);
    }

    public static void removeCallbacks(Runnable runnable) {
        mHandler.removeCallbacks(runnable);
    }

    public static void postDelayed(Runnable runnable, long delayMillis) {
        mHandler.postDelayed(runnable, delayMillis);
    }

    public static void removePreviousAndPost(Runnable runnable) {
        mHandler.removeCallbacks(runnable);
        mHandler.post(runnable);
    }

    public static void removePreviousAndPostDelayed(Runnable runnable, long delayMills) {
        mHandler.removeCallbacks(runnable);
        mHandler.postDelayed(runnable, delayMills);
    }
}
