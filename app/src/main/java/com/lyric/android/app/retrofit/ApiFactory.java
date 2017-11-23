package com.lyric.android.app.retrofit;

/**
 * @author lyricgan
 * @description
 * @time 2016/8/2 14:31
 */
public class ApiFactory {

    private ApiFactory() {
    }

    private static Api getDefault() {
        return Api.getInstance();
    }
}
