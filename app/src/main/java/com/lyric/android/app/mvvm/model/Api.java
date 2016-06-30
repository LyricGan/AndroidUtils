package com.lyric.android.app.mvvm.model;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.lyric.android.app.constants.ApiPath;
import com.lyric.android.app.constants.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author lyric
 * @description
 * @time 2016/6/3 11:41
 */
public class Api {
    private static Api mInstance;
    private static Retrofit mRetrofit;

    private Api() {
        initialize(ApiPath.BASE_URL);
    }

    private static synchronized Api getInstance() {
        if (mInstance == null) {
            mInstance = new Api();
        }
        return mInstance;
    }

    private void initialize(String baseUrl) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30, TimeUnit.SECONDS);
        builder.readTimeout(30, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(true);
        if (Constants.DEBUG) {
            builder.addNetworkInterceptor(new StethoInterceptor());
        }
        OkHttpClient okHttpClient = builder.build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    private Retrofit getRetrofit() {
        return mRetrofit;
    }

    private <T> T build(Class<T> cls) {
        if (getRetrofit() == null) {
            throw new NullPointerException("initialized failed.");
        }
        return getRetrofit().create(cls);
    }

    public static UserApi getUserApi() {
        return Api.getInstance().build(UserApi.class);
    }
}
