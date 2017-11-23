package com.lyric.android.app.retrofit.interceptor;

import okhttp3.Interceptor;

/**
 * @author lyricgan
 * @description 缓存拦截器帮助类
 * @time 2016/8/2 17:54
 */
public class InterceptorHelper {
    private static final InterceptorHelper INSTANCE = new InterceptorHelper();
    private final Interceptor PARAMS_INTERCEPTOR = new ParamsInterceptor();
    private final Interceptor CACHE_INTERCEPTOR = new CacheInterceptor();

    private InterceptorHelper() {
    }

    public static InterceptorHelper getInstance() {
        return INSTANCE;
    }

    public Interceptor getParamsInterceptor() {
        return PARAMS_INTERCEPTOR;
    }

    public Interceptor getCacheInterceptor() {
        return CACHE_INTERCEPTOR;
    }
}
