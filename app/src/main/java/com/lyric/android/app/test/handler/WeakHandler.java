package com.lyric.android.app.test.handler;

import android.os.Handler;
import android.os.Looper;

import java.lang.ref.WeakReference;

/**
 * Handler，采用弱引用，防止内存泄漏
 * @author lyricgan
 * @time 2016/3/28 16:21
 */
public class WeakHandler<T> extends Handler {
    private final WeakReference<T> mReference;

    public WeakHandler(T object) {
        this(object, Looper.getMainLooper());
    }

    public WeakHandler(T object, Looper looper) {
        super(looper);
        this.mReference = new WeakReference<>(object);
    }

    public T get() {
        return mReference.get();
    }
}
