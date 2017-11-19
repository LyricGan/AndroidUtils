package com.lyric.android.app.test.handler;

/**
 * Sync Post
 * @author lyricgan
 * @date 17/11/18 下午8:37
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
