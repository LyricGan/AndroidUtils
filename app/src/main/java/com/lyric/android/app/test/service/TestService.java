package com.lyric.android.app.test.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.lyric.android.app.Constants;
import com.lyric.android.app.utils.LogUtils;

public class TestService extends Service {
    private TestServiceBinder mServiceBinder;

    @Override
    public void onCreate() {
        LogUtils.d(Constants.TAG, "onCreate()");
        super.onCreate();
        mServiceBinder = new TestServiceBinder();
    }

    @Override
    public IBinder onBind(Intent intent) {
        LogUtils.d(Constants.TAG, "onBind()");
        return mServiceBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.d(Constants.TAG, "onStartCommand()");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        LogUtils.d(Constants.TAG, "onDestroy()");
        if (mServiceBinder != null) {
            mServiceBinder = null;
        }
        super.onDestroy();
    }

}
