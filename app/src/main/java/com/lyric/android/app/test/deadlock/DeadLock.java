package com.lyric.android.app.test.deadlock;

import com.lyric.android.app.test.logger.Loggers;

/**
 * @author lyricgan
 * @description
 * @time 2016/11/15 15:02
 */
public class DeadLock {
    private final Object left = new Object();
    private final Object right = new Object();

    public void leftRight() throws Exception {
        synchronized (left) {
            Thread.sleep(2000);
            synchronized (right) {
                Loggers.e("left-right");
            }
        }
    }

    public void rightLeft() throws Exception {
        synchronized (right) {
            Thread.sleep(2000);
            synchronized (left) {
                Loggers.e("right-left");
            }
        }
    }
}
