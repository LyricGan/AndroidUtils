package com.lyric.android.app.retrofit.interceptor;

import android.text.TextUtils;

import com.lyric.android.app.AndroidApplication;
import com.lyric.utils.NetworkUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author lyricgan
 * @description 缓存拦截器帮助类
 * @time 2016/8/2 17:54
 * 配置Cache-Control同时配置{@link #HEADER_USER_CACHE_TYPE}，若只配置Cache-Control则默认使用{@link #TYPE_NETWORK_WITH_CACHE}
 * noCache ：不使用缓存，全部走网络
 * noStore ： 不使用缓存，也不存储缓存
 * onlyIfCached ： 只使用缓存
 * maxAge ：设置最大失效时间，失效则不使用
 * maxStale ：设置最大失效时间，失效则不使用
 * minFresh ：设置最小有效时间，失效则不使用
 */
public class CacheInterceptorHelper {
    private static final String HEADER_CACHE_CONTROL = "Cache-Control";
    private static final String HEADER_PRAGMA = "Pragma";
    private static final String CACHE_CONTROL_ONLY_CACHE = "public, only-if-cached, max-age=2419200";
    private static final String CACHE_CONTROL_NO_CACHE = "public, max-age=0";
    // 自定义缓存类型
    private static final String HEADER_USER_CACHE_TYPE = "User-Cache-Type";
    // 断网情况下，加载缓存，联网情况下，优先加载缓存，默认情况
    public static final String TYPE_NETWORK_WITH_CACHE = "network_with_cache";
    // 断网情况下，加载缓存，联网情况下，只加载网络
    public static final String TYPE_NETWORK_NO_CACHE = "network_no_cache";
    private static final CacheInterceptorHelper INSTANCE = new CacheInterceptorHelper();

    private CacheInterceptorHelper() {
    }

    public static CacheInterceptorHelper getInstance() {
        return INSTANCE;
    }

    public Interceptor getCacheInterceptor() {
        return CACHE_INTERCEPTOR;
    }

    public Interceptor getCacheNetworkInterceptor() {
        return CACHE_NETWORK_INTERCEPTOR;
    }

    private final Interceptor CACHE_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            String originalCacheControl = request.cacheControl().toString();
            if (TextUtils.isEmpty(originalCacheControl)) {
                return chain.proceed(request);
            }
            if (!isNetworkConnected()) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            return chain.proceed(request);
        }
    };

    private boolean isNetworkConnected() {
        return NetworkUtils.isNetworkAvailable(AndroidApplication.getContext());
    }

    private final Interceptor CACHE_NETWORK_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            String originalCacheControl = request.cacheControl().toString();
            String cacheType = request.header(HEADER_USER_CACHE_TYPE);
            if (TextUtils.isEmpty(originalCacheControl)) {
                return chain.proceed(request);
            }
            Response originalResponse = chain.proceed(request);
            String cacheControl = originalCacheControl;
            if (!isNetworkConnected()) {
                cacheControl = CACHE_CONTROL_ONLY_CACHE;
            } else if (TYPE_NETWORK_NO_CACHE.equals(cacheType)) {
                cacheControl = CACHE_CONTROL_NO_CACHE;
            }
            return originalResponse.newBuilder()
                    .header(HEADER_CACHE_CONTROL, cacheControl)
                    .removeHeader(HEADER_PRAGMA)
                    .build();
        }
    };
}
