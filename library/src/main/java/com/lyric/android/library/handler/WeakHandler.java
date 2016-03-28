package com.lyric.android.library.handler;

import android.os.Handler;
import android.os.Looper;

import java.lang.ref.WeakReference;

/**
 * @author lyric
 * @description
 * @time 2016/3/28 16:21
 */
public class WeakHandler<T> extends Handler {
    private final WeakReference<T> mReferenceObject;

    public WeakHandler(T object) {
        this.mReferenceObject = new WeakReference<T>(object);
    }

    public WeakHandler(T object, Looper looper) {
        super(looper);
        this.mReferenceObject = new WeakReference<T>(object);
    }

    public T get() {
        return mReferenceObject.get();
    }

}
