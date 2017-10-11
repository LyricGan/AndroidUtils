package com.lyric.android.app.test.network;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author lyric
 * @description
 * @time 2016/6/22 14:01
 */
public class DataLoader<T> {
    private Method mMethod;
    private String mUrl;
    private Map<String, String> mParams;
    private Map<String, File> mFileParams;
    private Type mType;
    private ResponseCallback<T> mCallback;
    private Request<T> mRequest;

    public DataLoader(String url, Map<String, String> params, Type type, ResponseCallback<T> callback) {
        this(Method.POST, url, params, type, callback);
    }

    public DataLoader(Method method, String url, Map<String, String> params, Type type, ResponseCallback<T> callback) {
        this(method, url, params, null, type, callback);
    }

    public DataLoader(Method method, String url, Map<String, String> params, Map<String, File> fileParams, Type type, ResponseCallback<T> callback) {
        this.mMethod = method;
        this.mUrl = url;
        this.mParams = params;
        this.mFileParams = fileParams;
        this.mType = type;
        this.mCallback = callback;
    }

    public void load() {
        load(true);
    }

    public void load(boolean isRefresh) {
        mRequest = new Request<>(mMethod, mUrl, mParams, mFileParams, mType, mCallback);
        mRequest.execute(isRefresh);
    }

    public void cancel() {
        mRequest.cancel();
    }

    public Method getMethod() {
        return this.mMethod;
    }

    public String getUrl() {
        return this.mUrl;
    }

    public Map<String, String> getParams() {
        return this.mParams;
    }
}
