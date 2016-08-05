package com.lyric.android.app.retrofit.interceptor;

import android.text.TextUtils;

import com.lyric.android.app.base.App;
import com.lyric.android.library.utils.NetworkUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author lyricgan
 * @description 缓存拦截器
 * @time 2016/8/2 17:54
 *  noCache ：不使用缓存，全部走网络
    noStore ： 不使用缓存，也不存储缓存
    onlyIfCached ： 只使用缓存
    maxAge ：设置最大失效时间，失效则不使用
    maxStale ：设置最大失效时间，失效则不使用
    minFresh ：设置最小有效时间，失效则不使用
 */
public class CacheInterceptor implements Interceptor {
    private static final String HEADER_CACHE_CONTROL = "Cache-Control";
    private static final String HEADER_PRAGMA = "Pragma";
    private static final String CACHE_CONTROL_NO_NETWORK = "public, only-if-cached, max-stale=2419200";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String cacheControl = request.cacheControl().toString();
        if (TextUtils.isEmpty(cacheControl)) {
            return chain.proceed(request);
        }
        if (!isNetworkConnected()) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        Response originalResponse = chain.proceed(request);
        if (!isNetworkConnected()) {
            cacheControl = CACHE_CONTROL_NO_NETWORK;
        }
        return originalResponse.newBuilder()
                .header(HEADER_CACHE_CONTROL, cacheControl)
                .removeHeader(HEADER_PRAGMA)
                .build();
    }

    private boolean isNetworkConnected() {
        return NetworkUtils.isNetworkAvailable(App.getContext());
    }
}
