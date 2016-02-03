package com.lyric.android.app.test;

import com.lyric.android.library.log.AdvanceLogger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author ganyu
 * @description
 * @time 2016/1/27 16:15
 */
public class ExecutorsTest {
    private static final String TAG = "ExecutorsTest";

    public ExecutorsTest() {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
//        executorService = Executors.newCachedThreadPool();
//        executorService = Executors.newSingleThreadExecutor();

//        Thread thread1 = new AddThread("thread1");
//        Thread thread2 = new AddThread("thread2");
//        Thread thread3 = new AddThread("thread3");
//        Thread thread4 = new AddThread("thread4");
//        Thread thread5 = new AddThread("thread5");
//        Thread thread6 = new AddThread("thread6");
//
//        executorService.execute(thread1);
//        executorService.execute(thread2);
//        executorService.execute(thread3);
//        executorService.execute(thread4);
//        executorService.execute(thread5);
//        executorService.execute(thread6);

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 5, 5, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());
        for (int i = 0; i < 20; i++) {
            threadPoolExecutor.execute(new AddThread("thread" + i));

            if (i == 19) {
                threadPoolExecutor.shutdown();
            }
        }
    }

    public static void start() {
        new ExecutorsTest();
    }

    class AddThread extends Thread {
        private String name;

        public AddThread(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            try {
                AdvanceLogger.e(TAG, "add thread run:" + name);
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
