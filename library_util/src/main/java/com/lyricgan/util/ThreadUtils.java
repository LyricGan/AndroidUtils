package com.lyricgan.util;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

/**
 * 线程工具类
 * @author Lyric Gan
 */
public class ThreadUtils {
    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());

    private ThreadUtils() {
    }

    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static void runMainThread(Runnable command) {
        if (isMainThread()) {
            command.run();
        } else {
            MAIN_HANDLER.post(command);
        }
    }

    public static void runBackgroundThread(Runnable command) {
        if (isMainThread()) {
            AsyncTask.SERIAL_EXECUTOR.execute(command);
        } else {
            command.run();
        }
    }
}
