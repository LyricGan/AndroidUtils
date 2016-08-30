package com.lyric.android.app.handler;

/**
 * @author lyricgan
 * @description
 * @time 2016/7/29 14:08
 */
public class SyncPost {
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

    public void waitRun() {
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

    public void waitRun(int time, boolean cancel) {
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
