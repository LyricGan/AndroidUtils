package com.lyric.android.app.utils.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * @author lyric
 * @description Handler
 * @time 2016/3/28 16:21
 */
public class WeakHandler<T> extends Handler {
    private final WeakReference<T> mReferenceObject;
    private OnMessageCallback mCallback;

    public WeakHandler(T object) {
        this(object, Looper.getMainLooper());
    }

    public WeakHandler(T object, Looper looper) {
        super(looper);
        this.mReferenceObject = new WeakReference<T>(object);
    }

    public T get() {
        return mReferenceObject.get();
    }

    public void setCallback(OnMessageCallback callback) {
        this.mCallback = callback;
    }

    public void removeCallback() {
        this.mCallback = null;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (mCallback != null) {
            mCallback.callback(msg);
        }
    }

    public interface OnMessageCallback {

        void callback(Message msg);
    }
}
