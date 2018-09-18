package com.lyric.android.app.network;

import android.text.TextUtils;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.Dispatcher;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * network manager with OkHttp
 * https://github.com/square/okhttp
 * 
 * @author lyricgan
 */
public class HttpManager {
    private OkHttpClient mHttpClient;

    private HttpManager() {
    }

    private static class HttpManagerHolder {
        private static final HttpManager INSTANCE = new HttpManager();
    }

    public static HttpManager getInstance() {
        return HttpManagerHolder.INSTANCE;
    }

    public void init(OkHttpClient httpClient) {
        if (httpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .addNetworkInterceptor(new StethoInterceptor());

            httpClient = builder.build();
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

    public List<Cookie> getCookies(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        HttpUrl httpUrl = HttpUrl.parse(url);
        if (httpUrl == null) {
            return null;
        }
        return mHttpClient.cookieJar().loadForRequest(httpUrl);
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
        CookieJar cookieJar = mHttpClient.cookieJar();
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
        CookieJar cookieJar = mHttpClient.cookieJar();
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
        CookieJar cookieJar = mHttpClient.cookieJar();
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
