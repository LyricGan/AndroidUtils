package com.lyric.android.app.test.others;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.lyric.android.app.Constants;
import com.lyric.android.app.test.Test;
import com.lyric.android.library.utils.LogUtils;

/**
 * @author lyricgan
 * @description
 * @time 2016/8/2 15:52
 */
public class TestBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Test.IntentFlags.ACTION_APP_START.equals(intent.getAction())) {
            LogUtils.d(Constants.TAG, "App start.");
        } else if (Test.IntentFlags.ACTION_TEST.equals(intent.getAction())) {
            LogUtils.d(Constants.TAG, "Test.");
        } else if (Test.IntentFlags.ACTION_TEST_DEFAULT.equals(intent.getAction())) {
            LogUtils.d(Constants.TAG, "Test default.");
        }
    }
}
