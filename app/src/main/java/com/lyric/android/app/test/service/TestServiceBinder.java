package com.lyric.android.app.test.service;

import android.os.Binder;

import com.lyric.android.library.logger.Loggers;

/**
 * @author lyricgan
 * @time 2017/7/11 16:09
 */
public class TestServiceBinder extends Binder {

    public void start() {
        Loggers.d("service started.");
    }
}
