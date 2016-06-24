package com.lyric.android.library.utils;

import android.os.Looper;

/**
 * @author lyric
 * @description
 * @time 2016/6/24 11:14
 */
public class ThreadUtils {

    private ThreadUtils() {
    }

    /**
     * 判断是否在主线程
     * @return boolean
     */
    public static boolean isMainThread() {
        return (Looper.myLooper() == Looper.getMainLooper());
    }
}
