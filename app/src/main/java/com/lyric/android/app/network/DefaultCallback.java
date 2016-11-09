package com.lyric.android.app.network;

/**
 * @author lyric
 * @description 默认回调类
 * @time 2016/6/22 16:00
 */
public abstract class DefaultCallback<T> implements ResponseCallback<T> {

    @Override
    public void onSuccess(T response) {
    }

    @Override
    public void onFailed(ResponseError error) {
    }
}
