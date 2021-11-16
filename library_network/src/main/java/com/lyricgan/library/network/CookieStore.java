package com.lyricgan.library.network;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * cookie存储类
 * @author Lyric Gan
 */
public interface CookieStore {

    void add(HttpUrl url, List<Cookie> cookie);

    List<Cookie> get(HttpUrl url);

    List<Cookie> getCookies();

    boolean remove(HttpUrl url, Cookie cookie);
}
