package com.lyric.android.app.common;

import android.os.Handler;
import android.os.Looper;

import java.lang.ref.WeakReference;

/**
 * Handler，采用弱引用，防止内存泄漏
 * @author lyricgan
 * @time 2016/3/28 16:21
 */
public class BaseHandler<T> extends Handler {
    private WeakReference<T> mReference;

    public BaseHandler(T object) {
        this(object, Looper.getMainLooper());
    }

    public BaseHandler(T object, Looper looper) {
        super(looper);
        this.mReference = new WeakReference<>(object);
    }

    public T get() {
        if (mReference != null) {
            return mReference.get();
        }
        return null;
    }

    public void clear() {
        if (mReference != null) {
            mReference.clear();
        }
    }
}
