package com.lyric.android.app.network;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.Dispatcher;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * network manager with OkHttp
 * https://github.com/square/okhttp
 * 
 * @author lyricgan
 */
public class NetworkManager {
    private OkHttpClient mHttpClient;

    private NetworkManager() {
        mHttpClient = getDefaultHttpClient();
    }

    private static class NetworkManagerHolder {
        private static final NetworkManager INSTANCE = new NetworkManager();
    }

    public static NetworkManager getInstance() {
        return NetworkManagerHolder.INSTANCE;
    }

    public OkHttpClient getDefaultHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        return builder.build();
    }

    public void setHttpClient(OkHttpClient httpClient) {
        if (httpClient == null) {
            httpClient = getDefaultHttpClient();
        }
        this.mHttpClient = httpClient;
    }

    public OkHttpClient getHttpClient() {
        return mHttpClient;
    }

    public OkHttpClient.Builder getHttpClientBuilder() {
        return getHttpClient().newBuilder();
    }

    public void get(String url, Map<String, String> params, Object tag, NetworkCallback callback) {
        get(url, params, tag, false, callback);
    }

    public void get(String url, Map<String, String> params, Object tag, boolean isUseCache, NetworkCallback callback) {
        get(url, params, null, tag, isUseCache, callback);
    }

    public void get(String url, Map<String, String> params, Map<String, String> headers, Object tag, boolean isUseCache, NetworkCallback callback) {
        NetworkRequest networkRequest = new NetworkRequest(NetworkRequest.buildGetRequest(url, params, headers, tag, isUseCache));
        execute(networkRequest, callback);
    }

    public void post(String url, Map<String, String> params, Object tag, NetworkCallback callback) {
        post(url, params, null, tag, callback);
    }

    public void post(String url, Map<String, String> params, Map<String, String> headers, Object tag, NetworkCallback callback) {
        NetworkRequest networkRequest = new NetworkRequest(NetworkRequest.buildPostRequest(url, params, headers, tag));
        execute(networkRequest, callback);
    }

    public void put(String url, Map<String, String> params, Map<String, String> headers, Object tag, NetworkCallback callback) {
        NetworkRequest networkRequest = new NetworkRequest(NetworkRequest.buildPutRequest(url, params, headers, tag));
        execute(networkRequest, callback);
    }

    public void patch(String url, Map<String, String> params, Map<String, String> headers, Object tag, NetworkCallback callback) {
        NetworkRequest networkRequest = new NetworkRequest(NetworkRequest.buildPatchRequest(url, params, headers, tag));
        execute(networkRequest, callback);
    }

    public void head(String url, Map<String, String> headers, Object tag, NetworkCallback callback) {
        NetworkRequest networkRequest = new NetworkRequest(NetworkRequest.buildHeadRequest(url, headers, tag));
        execute(networkRequest, callback);
    }

    public void delete(String url, Map<String, String> headers, Object tag, NetworkCallback callback) {
        NetworkRequest networkRequest = new NetworkRequest(NetworkRequest.buildDeleteRequest(url, headers, tag));
        execute(networkRequest, callback);
    }

    public void delete(String url, Map<String, String> params, Map<String, String> headers, Object tag, NetworkCallback callback) {
        NetworkRequest networkRequest = new NetworkRequest(NetworkRequest.buildDeleteRequest(url, params, headers, tag));
        execute(networkRequest, callback);
    }

    public void upload(String url, String name, List<File> files, Map<String, String> params, Map<String, String> headers, Object tag, NetworkCallback callback, FileCallback fileCallback) {
        NetworkRequest networkRequest = new NetworkRequest(NetworkRequest.buildUploadRequest(url, name, files, params, headers, tag, fileCallback));
        execute(networkRequest, callback);
    }

    public void execute(final NetworkRequest networkRequest, final NetworkCallback callback) {
        if (networkRequest == null) {
            return;
        }
        getHttpClient().newCall(networkRequest.getRequest()).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (callback == null) {
                    return;
                }
                callback.onFailure(networkRequest, e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (callback == null) {
                    return;
                }
                if (call.isCanceled()) {
                    callback.onCancel(networkRequest);
                    return;
                }
                if (!response.isSuccessful()) {
                    callback.onFailure(networkRequest, new IOException("request is failed, the response's code is " + response.code()));
                    return;
                }
                callback.onResponse(networkRequest, new NetworkResponse(response));
            }
        });
    }

    public void cancel(Object tag) {
        OkHttpClient httpClient = getHttpClient();
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

    public List<Cookie> getCookies(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        HttpUrl httpUrl = HttpUrl.parse(url);
        if (httpUrl == null) {
            return null;
        }
        return getHttpClient().cookieJar().loadForRequest(httpUrl);
    }

    public String getCookie(String url, String cookieName) {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(cookieName)) {
            return null;
        }
        List<Cookie> cookies = getCookies(url);
        if (cookies == null || cookies.isEmpty()) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (TextUtils.equals(cookie.name(), cookieName)) {
                return cookie.value();
            }
        }
        return null;
    }

    public void addCookie(String url, Cookie cookie) {
        if (TextUtils.isEmpty(url) || cookie == null) {
            return;
        }
        HttpUrl httpUrl = HttpUrl.parse(url);
        if (httpUrl == null) {
            return;
        }
        CookieJar cookieJar = getHttpClient().cookieJar();
        List<Cookie> cookies = cookieJar.loadForRequest(httpUrl);
        if (cookies == null || cookies.isEmpty()) {
            return;
        }
        if (cookies.contains(cookie)) {
            cookies.remove(cookie);
        }
        cookies.add(cookie);
        cookieJar.saveFromResponse(httpUrl, cookies);
    }

    public void clearCookies(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        HttpUrl httpUrl = HttpUrl.parse(url);
        if (httpUrl == null) {
            return;
        }
        CookieJar cookieJar = getHttpClient().cookieJar();
        List<Cookie> cookies = cookieJar.loadForRequest(httpUrl);
        if (cookies == null || cookies.isEmpty()) {
            return;
        }
        cookies.clear();
        cookieJar.saveFromResponse(httpUrl, cookies);
    }

    public void removeCookie(String url, String cookieName) {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(cookieName)) {
            return;
        }
        HttpUrl httpUrl = HttpUrl.parse(url);
        if (httpUrl == null) {
            return;
        }
        CookieJar cookieJar = getHttpClient().cookieJar();
        List<Cookie> cookies = cookieJar.loadForRequest(httpUrl);
        if (cookies == null || cookies.isEmpty()) {
            return;
        }
        Cookie cookie;
        for (int i = 0; i < cookies.size(); i++) {
            cookie = cookies.get(i);
            if (cookie != null && TextUtils.equals(cookieName, cookie.name())) {
                cookies.remove(cookie);
            }
        }
        cookieJar.saveFromResponse(httpUrl, cookies);
    }
}
