package com.lyric.android.app.retrofit;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.lyric.android.app.base.Constants;
import com.lyric.android.app.retrofit.adapter.RxJavaCallAdapterFactory;
import com.lyric.android.app.retrofit.converter.GsonConverterFactory;
import com.lyric.android.app.retrofit.interceptor.CacheInterceptorHelper;
import com.lyric.android.app.retrofit.interceptor.HttpLogInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author lyric
 * @description network api
 * @time 2016/6/3 11:41
 */
public class Api {
    private static final String BASE_URL = Constants.BASE_URL;
    private static final long CONNECT_TIMEOUT = 30L;
    private static final long READ_TIMEOUT = 30L;
    private static Api mInstance;
    private static Retrofit mRetrofit;

    private Api() {
        initialize(BASE_URL);
    }

    public static synchronized Api getInstance() {
        if (mInstance == null) {
            mInstance = new Api();
        }
        return mInstance;
    }

    private void initialize(String baseUrl) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(true);
        if (Constants.DEBUG) {
            builder.addNetworkInterceptor(new StethoInterceptor());
            builder.addNetworkInterceptor(new HttpLogInterceptor());
        }
        builder.addInterceptor(CacheInterceptorHelper.getInstance().getCacheInterceptor());
        builder.addNetworkInterceptor(CacheInterceptorHelper.getInstance().getCacheNetworkInterceptor());
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

    public <T> T build(Class<T> cls) {
        if (getRetrofit() == null) {
            throw new NullPointerException("initialized failed.");
        }
        return getRetrofit().create(cls);
    }
}
