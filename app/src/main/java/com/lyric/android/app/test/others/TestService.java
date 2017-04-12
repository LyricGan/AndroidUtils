package com.lyric.android.app.test.others;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.lyric.android.library.utils.LogUtils;

public class TestService extends Service {
    private static final String TAG = TestService.class.getSimpleName();
    private LocaleServiceBinder mLocaleServiceBinder;

    @Override
    public void onCreate() {
        LogUtils.e(TAG, "onCreate()");
        super.onCreate();
        mLocaleServiceBinder = new LocaleServiceBinder();
    }

    @Override
    public IBinder onBind(Intent intent) {
        LogUtils.e(TAG, "onBind()");
        return mLocaleServiceBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.e(TAG, "onStartCommand()");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        LogUtils.e(TAG, "onDestroy()");
        super.onDestroy();
    }

    class LocaleServiceBinder extends Binder {

        public TestService getService() {
            return TestService.this;
        }
    }
}
