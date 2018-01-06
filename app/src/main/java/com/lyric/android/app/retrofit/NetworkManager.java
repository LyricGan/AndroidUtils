package com.lyric.android.app.retrofit;

import com.lyric.android.app.common.Constants;
import com.lyric.android.app.retrofit.converter.GsonConverterFactory;
import com.lyric.android.app.retrofit.interceptor.CacheInterceptorHelper;
import com.lyric.android.app.retrofit.interceptor.HttpLogInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * 网络请求管理类，基于retrofit
 * @author lyricgan
 * @time 2016/6/3 11:41
 */
public class NetworkManager {
    /** 默认连接超时，30s */
    private static final long CONNECT_TIMEOUT = 30L;
    /** 默认读数据超时，30s */
    private static final long READ_TIMEOUT = 30L;
    /** 默认写数据超时，120s */
    private static final long WRITE_TIMEOUT = 120L;

    private Retrofit mRetrofit;

    private NetworkManager() {
    }

    private static final class NetworkManagerHolder {
        private static final NetworkManager mInstance = new NetworkManager();
    }

    public static NetworkManager getInstance() {
        return NetworkManagerHolder.mInstance;
    }

    private OkHttpClient buildDefaultClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(true);
        if (Constants.DEBUG) {
            builder.addNetworkInterceptor(new HttpLogInterceptor());
        }
        builder.addInterceptor(CacheInterceptorHelper.getInstance().getCacheInterceptor());
        builder.addNetworkInterceptor(CacheInterceptorHelper.getInstance().getCacheNetworkInterceptor());
        return builder.build();
    }

    private Retrofit getDefaultRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(buildDefaultClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public <T> T build(Class<T> cls) {
        return build(getDefaultRetrofit(), cls);
    }

    public <T> T build(Retrofit retrofit, Class<T> cls) {
        if (retrofit == null) {
            retrofit = getDefaultRetrofit();
        }
        this.mRetrofit = retrofit;
        return retrofit.create(cls);
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }
}
