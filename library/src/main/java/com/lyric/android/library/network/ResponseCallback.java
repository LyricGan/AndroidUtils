package com.lyric.android.library.network;

/**
 * @author lyric
 * @description 网络请求回调接口
 * @time 2016/6/22 13:43
 */
public interface ResponseCallback<T> {

    void onSuccess(T response);

    void onFailed(ResponseError error);
}
