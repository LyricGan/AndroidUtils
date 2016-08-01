package com.lyric.android.app.test;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

import com.lyric.android.app.IServiceAidlInterface;
import com.lyric.android.library.utils.LogUtils;

public class TestService extends Service {
    private static final String TAG = TestService.class.getSimpleName();
    private RemoteServiceBinder mRemoteServiceBinder;
    private LocaleServiceBinder mLocaleServiceBinder;

    @Override
    public void onCreate() {
        LogUtils.e(TAG, "onCreate()");
        super.onCreate();
        mRemoteServiceBinder = new RemoteServiceBinder();
        mLocaleServiceBinder = new LocaleServiceBinder();
    }

    @Override
    public IBinder onBind(Intent intent) {
        LogUtils.e(TAG, "onBind()");
        return mRemoteServiceBinder;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        LogUtils.e(TAG, "onStart()");
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.e(TAG, "onStartCommand()");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onRebind(Intent intent) {
        LogUtils.e(TAG, "onRebind()");
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogUtils.e(TAG, "onUnbind()");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        LogUtils.e(TAG, "onDestroy()");
        super.onDestroy();
    }

    class RemoteServiceBinder extends IServiceAidlInterface.Stub {

        @Override
        public void execute(String tag) throws RemoteException {
            LogUtils.e(TAG, "tag");
        }
    }

    class LocaleServiceBinder extends Binder {

        public TestService getService() {
            return TestService.this;
        }
    }
}
