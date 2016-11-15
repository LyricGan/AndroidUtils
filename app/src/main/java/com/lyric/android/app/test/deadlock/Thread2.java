package com.lyric.android.app.test.deadlock;

import com.lyric.android.library.logger.Loggers;

/**
 * @author lyricgan
 * @description
 * @time 2016/11/15 15:06
 */
public class Thread2 extends Thread {
    private DeadLock mDeadLock;

    public Thread2(DeadLock lock) {
        this.mDeadLock = lock;
    }

    @Override
    public void run() {
        try {
            mDeadLock.rightLeft();
        } catch (Exception e) {
            Loggers.e("Exception:" + e.getMessage());
            e.printStackTrace();
        }
    }
}
