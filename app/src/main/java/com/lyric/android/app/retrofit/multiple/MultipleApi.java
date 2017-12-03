package com.lyric.android.app.retrofit.multiple;

import com.lyric.android.app.Constants;
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
 * @time 2016/7/28 17:27
 */
public class MultipleApi {
    private static final String BASE_URL = Constants.BASE_URL;
    private static final long CONNECT_TIMEOUT = 120L;
    private static final long READ_TIMEOUT = 120L;
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
        builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(true);
        if (Constants.DEBUG) {
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

    public <T> T buildDownload(Class<T> clazz, FileCallback callback) {
        OkHttpClient client = addOnDownloadCallback(new OkHttpClient.Builder(), callback).build();
        return getRetrofit().client(client).build().create(clazz);
    }

    public <T> T buildUpload(Class<T> clazz, FileCallback callback) {
        OkHttpClient client = addOnUploadCallback(new OkHttpClient.Builder(), callback).build();
        return getRetrofit().client(client).build().create(clazz);
    }

    private OkHttpClient.Builder addOnDownloadCallback(OkHttpClient.Builder builder, final FileCallback callback) {
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Response originalResponse = chain.proceed(originalRequest);
                List<String> segments = originalRequest.url().pathSegments();
                String filename = segments.get(segments.size() - 1);
                FileResponseBody responseBody = new FileResponseBody(originalResponse.body(), callback);
                responseBody.setFilePath(originalRequest.header(FileResponseBodyConverter.HEADER_FILE_PATH));
                responseBody.setFileName(filename);
                return originalResponse.newBuilder().body(responseBody).build();
            }
        });
        return builder;
    }

    private OkHttpClient.Builder addOnUploadCallback(OkHttpClient.Builder builder, final FileCallback callback) {
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request request = originalRequest.newBuilder()
                        .method(originalRequest.method(), new FileRequestBody(originalRequest.body(), callback))
                        .build();
                return chain.proceed(request);
            }
        });
        return builder;
    }
}
