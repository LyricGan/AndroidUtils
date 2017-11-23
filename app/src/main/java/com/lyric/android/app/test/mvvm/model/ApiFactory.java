package com.lyric.android.app.test.mvvm.model;

import com.lyric.android.app.retrofit.Api;

/**
 * @author lyricgan
 * @description
 * @time 2016/7/26 15:44
 */
public class ApiFactory {

    private ApiFactory() {
    }

    private static Api getDefault() {
        return Api.getInstance();
    }

    /**
     * 获取用户相关API
     * @return {@link UserApi}
     */
    public static UserApi getUserApi() {
        return getDefault().build(UserApi.class);
    }
}
