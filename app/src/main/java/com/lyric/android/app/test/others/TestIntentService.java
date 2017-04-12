package com.lyric.android.app.test.others;

import android.app.IntentService;
import android.content.Intent;

/**
 * @author lyric
 * @description
 * @time 16/7/30
 */
public class TestIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public TestIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
    }
}
