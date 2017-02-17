package com.lyric.android.app.utils.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author lyricgan
 * @description
 * @time 2016/7/29 14:07
 */
public class HandlerPoster extends Handler {
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
