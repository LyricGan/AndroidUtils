package com.lyric.network;

import java.io.IOException;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 网络请求响应回调接口
 * @author lyricgan
 * @date 2017/12/28 11:04
 */
public abstract class BaseResponseCallback<T> implements NetworkCallback {

    @Override
    public void onResponse(NetworkRequest networkRequest, NetworkResponse networkResponse) {
        Response response = networkResponse.getResponse();
        ResponseBody responseBody = response.body();
        if (responseBody == null) {
            onFailure(networkRequest, new IOException("request failed, response is null"));
            return;
        }
        T result = parseResponse(responseBody);
        if (result == null) {
            onFailure(networkRequest, new IOException("response parse error"));
            return;
        }
        onResponse(networkRequest, result);
    }

    @Override
    public void onCancel(NetworkRequest networkRequest) {
    }

    public abstract T parseResponse(ResponseBody responseBody);

    public abstract void onResponse(NetworkRequest networkRequest, T result);
}
