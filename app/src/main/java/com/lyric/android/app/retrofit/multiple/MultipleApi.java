package com.lyric.android.app.retrofit.multiple;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.lyric.android.app.base.Constants;
import com.lyric.android.app.retrofit.converter.GsonConverterFactory;
import com.lyric.android.app.retrofit.interceptor.HttpLogInterceptor;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * @author lyricgan
 * @description
 * @time 2016/7/28 17:27
 */
public class MultipleApi {
    private static final String BASE_URL = Constants.BASE_URL;
    private static MultipleApi mInstance;
    private static Retrofit.Builder mRetrofitBuilder;

    private MultipleApi() {
        initialize(BASE_URL);
    }

    public static synchronized MultipleApi getInstance() {
        if (mInstance == null) {
            mInstance = new MultipleApi();
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
            builder.addNetworkInterceptor(new HttpLogInterceptor());
        }
        OkHttpClient okHttpClient = builder.build();
        mRetrofitBuilder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(FileConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
    }

    private Retrofit.Builder getRetrofit() {
        return mRetrofitBuilder;
    }

    public <T> T build(Class<T> cls) {
        if (getRetrofit() == null) {
            throw new NullPointerException("initialized failed.");
        }
        return getRetrofit().build().create(cls);
    }

    public <T> T build(Class<T> clazz, OnDownloadCallback callback) {
        OkHttpClient client = addOnDownloadCallback(new OkHttpClient.Builder(), callback).build();
        return getRetrofit().client(client).build().create(clazz);
    }

    public <T> T build(Class<T> clazz, OnUploadCallback callback) {
        OkHttpClient client = addOnUploadCallback(new OkHttpClient.Builder(), callback).build();
        return getRetrofit().client(client).build().create(clazz);
    }

    public OkHttpClient.Builder addOnDownloadCallback(OkHttpClient.Builder builder, final OnDownloadCallback callback) {
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Response originalResponse = chain.proceed(originalRequest);
                List<String> segments = originalRequest.url().pathSegments();
                String filename = segments.get(segments.size() - 1);
                ProgressResponseBody responseBody = new ProgressResponseBody(originalResponse.body(), callback);
                responseBody.setSavePath(originalRequest.header(FileResponseBodyConverter.SAVE_PATH));
                responseBody.setFileName(filename);
                return originalResponse.newBuilder().body(responseBody).build();
            }
        });
        return builder;
    }

    public OkHttpClient.Builder addOnUploadCallback(OkHttpClient.Builder builder, final OnUploadCallback callback) {
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request request = originalRequest.newBuilder()
                        .method(originalRequest.method(), new ProgressRequestBody(originalRequest.body(), callback))
                        .build();
                return chain.proceed(request);
            }
        });
        return builder;
    }

    public interface OnUploadCallback {

        void onProgress(long currentSize, long totalSize, boolean isCompleted);
    }

    public interface OnDownloadCallback {

        void onProgress(long currentSize, long totalSize, boolean isCompleted);
    }
}
