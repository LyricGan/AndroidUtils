package com.lyric.android.app.test.service;

import android.os.RemoteException;

import com.lyric.android.app.IServiceAidlInterface;
import com.lyric.android.app.Constants;
import com.lyric.android.library.utils.LogUtils;

/**
 * @author lyricgan
 * @time 2017/7/11 16:40
 */
public class TestRemoteServiceBinder extends IServiceAidlInterface.Stub {

    @Override
    public void execute(String tag) throws RemoteException {
        LogUtils.d(Constants.TAG, "tag:" + tag);
    }
}
