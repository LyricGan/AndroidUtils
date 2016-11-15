package com.lyric.android.app.test.deadlock;

/**
 * @author lyricgan
 * @description
 * @time 2016/11/15 15:08
 */
public class DeadLockTest {

    // TODO: 2016/11/15
    public static void test() {
        DeadLock deadLock = new DeadLock();
        Thread1 thread1 = new Thread1(deadLock);
        Thread2 thread2 = new Thread2(deadLock);
        thread1.start();
        thread2.start();
    }
}
