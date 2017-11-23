package com.lyric.android.app.retrofit;

import com.lyric.android.app.Constants;
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
    private static final long CONNECT_TIMEOUT = 30L;
    private static final long READ_TIMEOUT = 30L;
    private static Api mInstance;
    private static Retrofit mRetrofit;

    private Api() {
    }

    public static synchronized Api getInstance() {
        if (mInstance == null) {
            mInstance = new Api();
        }
        return mInstance;
    }

    private OkHttpClient buildDefaultClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(true);
        if (Constants.DEBUG) {
            builder.addNetworkInterceptor(new HttpLogInterceptor());
        }
        builder.addInterceptor(CacheInterceptorHelper.getInstance().getCacheInterceptor());
        builder.addNetworkInterceptor(CacheInterceptorHelper.getInstance().getCacheNetworkInterceptor());
        return builder.build();
    }

    private void buildRetrofit() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(buildDefaultClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    private Retrofit getRetrofit() {
        if (mRetrofit == null) {
            buildRetrofit();
        }
        return mRetrofit;
    }

    public <T> T build(Class<T> cls) {
        return build(getRetrofit(), cls);
    }

    public <T> T build(Retrofit retrofit, Class<T> cls) {
        if (retrofit == null) {
            throw new NullPointerException("retrofit is null");
        }
        return retrofit.create(cls);
    }
}
