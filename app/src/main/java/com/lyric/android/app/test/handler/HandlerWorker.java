package com.lyric.android.app.test.handler;

import android.os.Looper;

/**
 * 线程切换处理器
 * @author lyricgan
 * @time 2016/7/29 14:08
 */
public class HandlerWorker {
    private static HandlerPoster mMainPoster = null;

    private static HandlerPoster getMainPoster() {
        if (mMainPoster == null) {
            synchronized (HandlerWorker.class) {
                if (mMainPoster == null) {
                    mMainPoster = new HandlerPoster(Looper.getMainLooper(), 20);
                }
            }
        }
        return mMainPoster;
    }

    /**
     * Asynchronously
     * The child thread asynchronous run relative to the main thread,
     * not blocking the child thread
     *
     * @param runnable Runnable Interface
     */
    public static void runOnMainThreadAsync(Runnable runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
            return;
        }
        getMainPoster().async(runnable);
    }

    /**
     * Synchronously
     * The child thread relative thread synchronization operation,
     * blocking the child thread,
     * thread for the main thread to complete
     *
     * @param runnable Runnable Interface
     */
    public static void runOnMainThreadSync(Runnable runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
            return;
        }
        SyncPost poster = new SyncPost(runnable);
        getMainPoster().sync(poster);
        poster.waitRun();
    }

    /**
     * Synchronously
     * The child thread relative thread synchronization operation,
     * blocking the child thread,
     * thread for the main thread to complete
     * But the child thread just wait for the waitTime long.
     *
     * @param runnable Runnable Interface
     * @param waitTime wait for the main thread run Time
     * @param cancel   on the child thread cancel the runnable task
     */
    public static void runOnMainThreadSync(Runnable runnable, int waitTime, boolean cancel) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
            return;
        }
        SyncPost poster = new SyncPost(runnable);
        getMainPoster().sync(poster);
        poster.waitRun(waitTime, cancel);
    }

    public static void dispose() {
        if (mMainPoster != null) {
            mMainPoster.dispose();
            mMainPoster = null;
        }
    }
}
