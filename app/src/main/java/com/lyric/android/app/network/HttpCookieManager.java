package com.lyric.android.app.network;

import android.text.TextUtils;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

/**
 * @author lyricgan
 * @since 2018/9/18 下午2:08
 */
public class HttpCookieManager {
    private OkHttpClient mHttpClient;

    public HttpCookieManager(OkHttpClient httpClient) {
        this.mHttpClient = httpClient;
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
