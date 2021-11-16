package com.lyricgan.library.network;

import java.io.IOException;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 网络请求响应回调接口
 * @author Lyric Gan
 * @since 2017/12/28 11:04
 */
public abstract class HttpResponseCallback<T> implements HttpCallback {

    @Override
    public void onResponse(HttpRequest httpRequest, HttpResponse httpResponse) {
        Response response = httpResponse.getResponse();
        ResponseBody responseBody = response.body();
        if (responseBody == null) {
            onFailure(httpRequest, new IOException("request failed, response is null"));
            return;
        }
        T result = parseResponse(responseBody);
        if (result == null) {
            onFailure(httpRequest, new IOException("response parse error " + responseBody.toString()));
            return;
        }
        onResponse(httpRequest, result);
    }

    @Override
    public void onCancel(HttpRequest httpRequest) {
    }

    public abstract T parseResponse(ResponseBody responseBody);

    public abstract void onResponse(HttpRequest httpRequest, T result);
}
