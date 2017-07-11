package com.lyric.android.app.test.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class TestRemoteService extends Service {
    private TestRemoteServiceBinder mRemoteServiceBinder;

    @Override
    public void onCreate() {
        super.onCreate();
        mRemoteServiceBinder = new TestRemoteServiceBinder();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mRemoteServiceBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
