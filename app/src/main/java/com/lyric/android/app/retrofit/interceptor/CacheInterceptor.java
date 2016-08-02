package com.lyric.android.app.retrofit.interceptor;

import com.lyric.android.app.base.App;
import com.lyric.android.library.utils.NetworkUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.CacheControl;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author lyricgan
 * @description 缓存拦截器
 * @time 2016/8/2 17:54
 */
public class CacheInterceptor implements Interceptor {
    private static final String HEADER_CACHE_CONTROL = "Cache-Control";
    private static final String HEADER_PRAGMA = "Pragma";
    private static final String CACHE_CONTROL_NO_NETWORK = "public, only-if-cached, max-stale=2419200";

    private static final List<String> mCacheUrlList;

    static {
        mCacheUrlList = new ArrayList<>();
        mCacheUrlList.add("");
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        HttpUrl httpUrl = request.url();
        String url = httpUrl.toString();
        // 通过url来过滤缓存
        if (mCacheUrlList.contains(url) || !isNetworkConnected()) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        Response originalResponse = chain.proceed(request);
        String cacheControl = request.cacheControl().toString();
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
