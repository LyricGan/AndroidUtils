package com.lyric.android.app.network;

import android.os.Message;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author lyric
 * @description
 * @time 2016/6/22 13:40
 */
public class Request<T> implements HttpHandler.OnMessageCallback {
    private static final int MESSAGE_SUCCESS = 0x01 << 2;
    private static final int MESSAGE_FAILED = 0x02 << 2;
    private Method mMethod;
    private String mUrl;
    private Map<String, String> mParams;
    private Map<String, File> mFileParams;
    private ResponseCallback<T> mCallback;
    private HttpHandler mHandler = new HttpHandler<>(this);
    private Type mType;

    public Request(Method method, String url, Map<String, String> params, Type type, ResponseCallback<T> callback) {
        this(method, url, params, null, type, callback);
    }

    public Request(Method method, String url, Map<String, String> params, Map<String, File> fileParams, Type type, ResponseCallback<T> callback) {
        this.mMethod = method;
        this.mUrl = url;
        this.mParams = params;
        this.mFileParams = fileParams;
        this.mCallback = callback;
        this.mType = type;
        this.mHandler.setCallback(this);
    }

    public ResponseEntity executeSync() {
        return executeSync(true);
    }

    public ResponseEntity executeSync(boolean isRefresh) {
        ResponseEntity responseEntity;
        if (Method.GET == mMethod) {
            responseEntity = HttpUtils.get(mUrl, mParams, isRefresh);
        } else if (Method.POST == mMethod) {
            responseEntity = HttpUtils.post(mUrl, mParams, isRefresh);
        } else if (Method.UPLOAD == mMethod) {
            responseEntity = HttpUtils.upload(mUrl, mParams, mFileParams);
        } else {
            throw new IllegalArgumentException("Request method error.");
        }
        return responseEntity;
    }

    // 未实现线程池管理，待完善...
    public void execute(final boolean isRefresh) {
        String threadName = "execute_net_thread_" + System.currentTimeMillis();
        new Thread(new Runnable() {
            @Override
            public void run() {
                processResponse(executeSync(isRefresh));
            }
        }, threadName).start();
    }

    private void processResponse(ResponseEntity responseEntity) {
        if (responseEntity.isSuccess()) {
            String response = responseEntity.response;
            T result = Converter.getInstance().convert(response, mType);
            Message msg = mHandler.obtainMessage(MESSAGE_SUCCESS);
            msg.obj = result;
            mHandler.sendMessage(msg);
        } else {
            Message msg = mHandler.obtainMessage(MESSAGE_FAILED);
            msg.obj = responseEntity;
            mHandler.sendMessage(msg);
        }
    }

    @Override
    public void callback(Message msg) {
        if (MESSAGE_SUCCESS == msg.what) {// 请求成功
            T object = (T) msg.obj;
            mCallback.onSuccess(object);
        } else if (MESSAGE_FAILED == msg.what) {// 请求失败
            ResponseEntity responseEntity = (ResponseEntity) msg.obj;
            ResponseError error = new ResponseError();
            error.url = responseEntity.url;
            error.params = responseEntity.params;
            error.code = responseEntity.responseCode;
            error.message = responseEntity.response;
            mCallback.onFailed(error);
        }
    }

    // 待实现...
    public void cancel() {

    }
}
