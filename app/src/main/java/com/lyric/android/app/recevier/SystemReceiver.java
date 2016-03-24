package com.lyric.android.app.recevier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.lyric.android.library.utils.LogUtils;

/**
 * @author lyric
 * @description
 * @time 2016/3/24 14:23
 */
public class SystemReceiver extends BroadcastReceiver implements IntentFlags {
    private static final String TAG = SystemReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (APP_START.equals(action)) {
            LogUtils.d(TAG, "app start");
        }
    }
}
