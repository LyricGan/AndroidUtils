package com.lyric.android.app.test.network;

import android.text.TextUtils;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lyricgan
 * @description dispatcher helper
 * @time 2016/7/25 14:22
 */
public class DispatcherHelper {
    private static final int DEFAULT_THREAD_COUNT = 3;
    private static DispatcherHelper mInstance;
    private ExecutorService mExecutorService;
    private LinkedList<Dispatcher> mDispatchers;

    private DispatcherHelper() {
        mExecutorService = Executors.newFixedThreadPool(DEFAULT_THREAD_COUNT);
        mDispatchers = new LinkedList<>();
    }

    public static DispatcherHelper newFactory() {
        if (mInstance == null) {
            mInstance = new DispatcherHelper();
        }
        return mInstance;
    }

    public void addDispatcher(Dispatcher dispatcher) {
        mDispatchers.add(dispatcher);
    }

    public void removeDispatcher(Dispatcher dispatcher) {
        mDispatchers.remove(dispatcher);
    }

    public void start() {
        if (mDispatchers == null || mDispatchers.isEmpty()) {
            return;
        }
        for (Dispatcher dispatcher : mDispatchers) {
            mExecutorService.execute(dispatcher);
        }
    }

    public void stop() {
        if (mDispatchers == null || mDispatchers.isEmpty()) {
            return;
        }
        for (Dispatcher dispatcher : mDispatchers) {
            dispatcher.cancel();
        }
    }

    public void stop(String tag) {
        if (mDispatchers == null || mDispatchers.isEmpty() || TextUtils.isEmpty(tag)) {
            return;
        }
        for (Dispatcher dispatcher : mDispatchers) {
            if (tag.equals(dispatcher.getTag())) {
                dispatcher.cancel();
            }
        }
    }
}
