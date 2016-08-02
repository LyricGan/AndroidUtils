package com.lyric.android.app.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.lyric.android.app.base.Constants;
import com.lyric.android.app.network.DefaultCallback;
import com.lyric.android.library.utils.LogUtils;

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

    public void test() {
        ExecutorsTest.start();

        TestApi.getInstance().queryNews("top", new DefaultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                LogUtils.e(Constants.TAG_DEFAULT, "response:" + response);
            }
        });
    }

    public void testRegisterBroadcast(Context context, BroadcastReceiver receiver) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(IntentFlags.ACTION_TEST);
        filter.addAction(IntentFlags.ACTION_TEST_DEFAULT);
        context.registerReceiver(receiver, filter);
    }

    public void testUnregisterBroadcast(Context context, BroadcastReceiver receiver) {
        context.unregisterReceiver(receiver);
    }

    public void testSendBroadcast(Context context) {
        Intent intent = new Intent(IntentFlags.ACTION_TEST_DEFAULT);
        context.sendBroadcast(intent);
    }
}
