package com.lyric.android.app.network;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * cookies存储管理类
 * @author lyricgan
 */
public class CookieStoreImpl implements CookieStore {

    @Override
    public void add(HttpUrl url, List<Cookie> cookie) {
    }

    @Override
    public List<Cookie> get(HttpUrl url) {
        return null;
    }

    @Override
    public List<Cookie> getCookies() {
        return null;
    }

    @Override
    public boolean remove(HttpUrl url, Cookie cookie) {
        return false;
    }
}
