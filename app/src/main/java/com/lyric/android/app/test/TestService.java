package com.lyric.android.app.test;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.lyric.android.app.IServiceAidlInterface;
import com.lyric.android.library.utils.LogUtils;

public class TestService extends Service {
    private ServiceBinder mServiceBinder;

    public TestService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (mServiceBinder == null) {
            mServiceBinder = new ServiceBinder();
        }
        return mServiceBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    class ServiceBinder extends IServiceAidlInterface.Stub {

        @Override
        public void execute(String tag) throws RemoteException {
            LogUtils.e("TestService", "tag");
        }
    }
}
