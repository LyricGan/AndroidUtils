package com.lyric.android.app.test;

import android.os.Handler;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

/**
 * @author lyricgan
 */
public class MainThreadExecutor implements Executor {
    private Handler mHandler;

    private MainThreadExecutor(Handler handler) {
        this.mHandler = handler;
    }

    @Override
    public void execute(@NonNull Runnable command) {
        if (mHandler != null) {
            mHandler.post(command);
        }
    }
}
