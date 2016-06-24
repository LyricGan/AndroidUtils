package com.lyric.android.app.mvvm.model;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * @author lyric
 * @description
 * @time 2016/6/3 11:41
 */
public interface Api {

    class Factory {
        public static <T> T getInstance(String baseUrl, Class<T> cls) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            return retrofit.create(cls);
        }
    }
}
