package com.lyric.android.library.network;

import retrofit.Retrofit;

/**
 * @author lyric
 * @description
 * @time 2016/6/3 11:41
 */
public interface BaseApi {

    class Factory {
        public static BaseApi getInstance(String baseUrl) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .build();
            return retrofit.create(BaseApi.class);
        }
    }
}
