package com.lyric.android.app.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * @author lyricgan
 * @description
 * @time 2016/7/25 13:51
 */
public class Test {

    private Test() {
    }

    private static final class TestHolder {
        public static final Test INSTANCE = new Test();
    }

    public static Test getInstance() {
        return TestHolder.INSTANCE;
    }

    public void execute() {
        ExecutorsTest executorsTest = new ExecutorsTest();
        executorsTest.start();
    }

    public void registerBroadcast(Context context, BroadcastReceiver receiver) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(IntentFlags.ACTION_TEST);
        filter.addAction(IntentFlags.ACTION_TEST_DEFAULT);
        context.registerReceiver(receiver, filter);
    }

    public void unregisterBroadcast(Context context, BroadcastReceiver receiver) {
        context.unregisterReceiver(receiver);
    }

    public void sendBroadcast(Context context) {
        Intent intent = new Intent(IntentFlags.ACTION_TEST_DEFAULT);
        context.sendBroadcast(intent);
    }
}
