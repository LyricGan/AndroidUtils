package com.lyric.android.app.network;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Http manager with OkHttp
 * https://github.com/square/okhttp
 * 
 * @author lyricgan
 */
public class HttpManager {
    private OkHttpClient mHttpClient;
    private HttpCookieManager mCookieManager;

    private HttpManager() {
        if (mHttpClient == null) {
            mHttpClient = getDefaultHttpClient();
        }
    }

    private static class HttpManagerHolder {
        private static final HttpManager INSTANCE = new HttpManager();
    }

    public static HttpManager getInstance() {
        return HttpManagerHolder.INSTANCE;
    }

    private OkHttpClient getDefaultHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
    }

    public void setHttpClient(OkHttpClient httpClient) {
        if (httpClient == null) {
            return;
        }
        this.mHttpClient = httpClient;
    }

    public void get(String url, Map<String, String> params, Object tag, boolean isUseCache, HttpCallback callback) {
        get(url, params, null, tag, isUseCache, callback);
    }

    public void get(String url, Map<String, String> params, Map<String, String> headers, Object tag, boolean isUseCache, HttpCallback callback) {
        Request request = HttpRequest.buildGetRequest(url, params, headers, tag, isUseCache);
        new HttpRequest(request, mHttpClient).enqueue(callback);
    }

    public void post(String url, Map<String, String> params, Object tag, HttpCallback callback) {
        post(url, params, null, tag, callback);
    }

    public void post(String url, Map<String, String> params, Map<String, String> headers, Object tag, HttpCallback callback) {
        Request request = HttpRequest.buildPostRequest(url, params, headers, tag);
        new HttpRequest(request, mHttpClient).enqueue(callback);
    }

    public void upload(String url, String name, List<File> files, Map<String, String> params, Map<String, String> headers, Object tag, HttpCallback callback, FileCallback fileCallback) {
        Request request = HttpRequest.buildUploadRequest(url, name, files, params, headers, tag, fileCallback);
        new HttpRequest(request, mHttpClient).enqueue(callback);
    }

    public void cancel(Object tag) {
        OkHttpClient httpClient = mHttpClient;
        if (httpClient == null) {
            return;
        }
        Dispatcher dispatcher = httpClient.dispatcher();
        if (dispatcher == null) {
            return;
        }
        List<Call> queuedCalls = dispatcher.queuedCalls();
        for (Call call : queuedCalls) {
            if (tag == null || tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        List<Call> runningCalls = dispatcher.runningCalls();
        for (Call call : runningCalls) {
            if (tag == null || tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    public void cancelAll() {
        cancel(null);
    }

    public HttpCookieManager getCookieManager() {
        if (mCookieManager == null) {
            mCookieManager = new HttpCookieManager(mHttpClient);
        }
        return mCookieManager;
    }
}
