package com.lyric.android.app.test;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.lyric.android.app.IServiceAidlInterface;
import com.lyric.android.app.base.Constants;
import com.lyric.android.library.utils.LogUtils;

public class TestRemoteService extends Service {
    private RemoteServiceBinder mRemoteServiceBinder;

    @Override
    public void onCreate() {
        super.onCreate();
        mRemoteServiceBinder = new RemoteServiceBinder();
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

    class RemoteServiceBinder extends IServiceAidlInterface.Stub {

        @Override
        public void execute(String tag) throws RemoteException {
            LogUtils.e(Constants.TAG_DEFAULT, "tag");
        }
    }
}
