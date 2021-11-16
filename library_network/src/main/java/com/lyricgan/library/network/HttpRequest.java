package com.lyricgan.library.network;

import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 网络请求包装类
 *
 * @author Lyric Gan
 * @since 2017/12/28 10:30
 */
public class HttpRequest {
    public static final MediaType MEDIA_TYPE_PLAIN = MediaType.parse("text/plain;charset=utf-8");
    public static final MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream");

    private Request request;
    private OkHttpClient httpClient;

    public HttpRequest(Request request, OkHttpClient httpClient) {
        this.request = request;
        this.httpClient = httpClient;
    }

    public Request getRequest() {
        return request;
    }

    public OkHttpClient getHttpClient() {
        return httpClient;
    }

    public void enqueue(final HttpCallback httpCallback) {
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (httpCallback == null) {
                    return;
                }
                httpCallback.onFailure(HttpRequest.this, e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (httpCallback == null) {
                    return;
                }
                if (call.isCanceled()) {
                    httpCallback.onCancel(HttpRequest.this);
                    return;
                }
                if (!response.isSuccessful()) {
                    httpCallback.onFailure(HttpRequest.this, new IOException("request is failed, the response's code is " + response.code()));
                    return;
                }
                httpCallback.onResponse(HttpRequest.this, new HttpResponse(response));
            }
        });
    }

    public static String addParams(String url, Map<String, String> params) {
        if (url == null || params == null || params.isEmpty()) {
            return url;
        }
        Uri.Builder builder = Uri.parse(url).buildUpon();
        Set<String> keys = params.keySet();
        for (String key : keys) {
            builder.appendQueryParameter(key, params.get(key));
        }
        return builder.build().toString();
    }

    public static Headers getHeaders(Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return null;
        }
        Headers.Builder builder = new Headers.Builder();
        Set<String> keys = params.keySet();
        for (String key : keys) {
            builder.add(key, params.get(key));
        }
        return builder.build();
    }

    public static CacheControl cacheControl(boolean isUseCache) {
        CacheControl.Builder builder = new CacheControl.Builder();
        if (isUseCache) {
            builder.onlyIfCached();
        } else {
            builder.noCache();
        }
        return builder.build();
    }

    public static RequestBody buildFileRequestBody(String name, List<File> files, Map<String, String> params, FileCallback fileCallback) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addPart(buildRequestBody(params));
        FileRequestBody fileRequestBody;
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            fileRequestBody = new FileRequestBody(RequestBody.create(MEDIA_TYPE_STREAM, file), fileCallback);
            builder.addFormDataPart(name, file.getName(), fileRequestBody);
        }
        return builder.build();
    }

    public static RequestBody buildStringRequestBody(String content) {
        return RequestBody.create(HttpRequest.MEDIA_TYPE_PLAIN, content);
    }

    public static RequestBody buildRequestBody(@Nullable MediaType contentType, String content) {
        return RequestBody.create(contentType, content);
    }

    public static RequestBody buildRequestBody(final @Nullable MediaType contentType, final File file) {
        return RequestBody.create(contentType, file);
    }

    public static RequestBody buildRequestBody(Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!TextUtils.isEmpty(entry.getKey())) {
                String entryValue = entry.getValue();
                if (TextUtils.isEmpty(entryValue)) {
                    entryValue = "";
                }
                builder.add(entry.getKey(), entryValue);
            }
        }
        return builder.build();
    }

    public static Request buildGetRequest(String url, Map<String, String> params, Map<String, String> headers, Object tag, boolean isUseCache) {
        Request.Builder builder = new Request.Builder();
        return builder.get()
                .url(addParams(url, params))
                .headers(getHeaders(headers))
                .tag(tag)
                .cacheControl(cacheControl(isUseCache))
                .build();
    }

    public static Request buildPostRequest(String url, Map<String, String> params, Map<String, String> headers, Object tag) {
        Request.Builder builder = new Request.Builder();
        return builder.post(buildRequestBody(params))
                .url(url)
                .headers(getHeaders(headers))
                .tag(tag)
                .build();
    }

    public static Request buildPutRequest(String url, Map<String, String> params, Map<String, String> headers, Object tag) {
        Request.Builder builder = new Request.Builder();
        return builder.put(buildRequestBody(params))
                .url(url)
                .headers(getHeaders(headers))
                .tag(tag)
                .build();
    }

    public static Request buildPatchRequest(String url, Map<String, String> params, Map<String, String> headers, Object tag) {
        Request.Builder builder = new Request.Builder();
        return builder.patch(buildRequestBody(params))
                .url(url)
                .headers(getHeaders(headers))
                .tag(tag)
                .build();
    }

    public static Request buildHeadRequest(String url, Map<String, String> headers, Object tag) {
        Request.Builder builder = new Request.Builder();
        return builder.head()
                .url(url)
                .headers(getHeaders(headers))
                .tag(tag)
                .build();
    }

    public static Request buildDeleteRequest(String url, Map<String, String> headers, Object tag) {
        Request.Builder builder = new Request.Builder();
        return builder.delete()
                .url(url)
                .headers(getHeaders(headers))
                .tag(tag)
                .build();
    }

    public static Request buildDeleteRequest(String url, Map<String, String> params, Map<String, String> headers, Object tag) {
        Request.Builder builder = new Request.Builder();
        return builder.delete(buildRequestBody(params))
                .url(url)
                .headers(getHeaders(headers))
                .tag(tag)
                .build();
    }

    public static Request buildUploadRequest(String url, String name, List<File> files, Map<String, String> params, Map<String, String> headers, Object tag, FileCallback fileCallback) {
        Request.Builder builder = new Request.Builder();
        return builder.post(buildFileRequestBody(name, files, params, fileCallback))
                .url(url)
                .headers(getHeaders(headers))
                .tag(tag)
                .build();
    }
}
