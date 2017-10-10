package com.lyric.android.app.test.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;

import java.util.LinkedList;
import java.util.Queue;

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

    private static class SyncPost {
        private Runnable mRunnable;
        private boolean isEnd = false;

        SyncPost(Runnable runnable) {
            this.mRunnable = runnable;
        }

        public void run() {
            if (!isEnd) {
                synchronized (this) {
                    if (!isEnd) {
                        mRunnable.run();
                        isEnd = true;
                        try {
                            this.notifyAll();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        void waitRun() {
            if (!isEnd) {
                synchronized (this) {
                    if (!isEnd) {
                        try {
                            this.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        void waitRun(int time, boolean cancel) {
            if (!isEnd) {
                synchronized (this) {
                    if (!isEnd) {
                        try {
                            this.wait(time);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            if (!isEnd && cancel) {
                                isEnd = true;
                            }
                        }
                    }
                }
            }
        }
    }

    private static class HandlerPoster extends Handler {
        private static final int ASYNC = 0x1;
        private static final int SYNC = 0x2;
        private final Queue<Runnable> mAsyncPool;
        private final Queue<SyncPost> mSyncPool;
        private final int mMaxMillisInsideHandleMessage;
        private boolean isAsyncActive;
        private boolean isSyncActive;

        HandlerPoster(Looper looper, int maxMillisInsideHandleMessage) {
            super(looper);
            this.mMaxMillisInsideHandleMessage = maxMillisInsideHandleMessage;
            mAsyncPool = new LinkedList<>();
            mSyncPool = new LinkedList<>();
        }

        void async(Runnable runnable) {
            synchronized (mAsyncPool) {
                mAsyncPool.offer(runnable);
                if (!isAsyncActive) {
                    isAsyncActive = true;
                    if (!sendMessage(obtainMessage(ASYNC))) {
                        throw new RuntimeException("Could not send handler message");
                    }
                }
            }
        }

        void sync(SyncPost post) {
            synchronized (mSyncPool) {
                mSyncPool.offer(post);
                if (!isSyncActive) {
                    isSyncActive = true;
                    if (!sendMessage(obtainMessage(SYNC))) {
                        throw new RuntimeException("Could not send handler message");
                    }
                }
            }
        }

        void dispose() {
            this.removeCallbacksAndMessages(null);
            this.mAsyncPool.clear();
            this.mSyncPool.clear();
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == ASYNC) {
                boolean rescheduled = false;
                try {
                    long started = SystemClock.uptimeMillis();
                    while (true) {
                        Runnable runnable = mAsyncPool.poll();
                        if (runnable == null) {
                            synchronized (mAsyncPool) {
                                // Check again, this time in synchronized
                                runnable = mAsyncPool.poll();
                                if (runnable == null) {
                                    isAsyncActive = false;
                                    return;
                                }
                            }
                        }
                        runnable.run();
                        long timeInMethod = SystemClock.uptimeMillis() - started;
                        if (timeInMethod >= mMaxMillisInsideHandleMessage) {
                            if (!sendMessage(obtainMessage(ASYNC))) {
                                throw new RuntimeException("Could not send handler message");
                            }
                            rescheduled = true;
                            return;
                        }
                    }
                } finally {
                    isAsyncActive = rescheduled;
                }
            } else if (msg.what == SYNC) {
                boolean rescheduled = false;
                try {
                    long started = SystemClock.uptimeMillis();
                    while (true) {
                        SyncPost post = mSyncPool.poll();
                        if (post == null) {
                            synchronized (mSyncPool) {
                                // Check again, this time in synchronized
                                post = mSyncPool.poll();
                                if (post == null) {
                                    isSyncActive = false;
                                    return;
                                }
                            }
                        }
                        post.run();
                        long timeInMethod = SystemClock.uptimeMillis() - started;
                        if (timeInMethod >= mMaxMillisInsideHandleMessage) {
                            if (!sendMessage(obtainMessage(SYNC))) {
                                throw new RuntimeException("Could not send handler message");
                            }
                            rescheduled = true;
                            return;
                        }
                    }
                } finally {
                    isSyncActive = rescheduled;
                }
            } else {
                super.handleMessage(msg);
            }
        }
    }
}
