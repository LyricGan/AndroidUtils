package com.lyric.android.app.api;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.lyric.android.app.BaseApplication;
import com.lyric.android.app.api.net.Request;
import com.lyric.android.app.api.net.RequestError;
import com.lyric.android.app.api.net.RequestQueue;
import com.lyric.android.app.api.net.Response;
import com.lyric.android.app.api.net.core.StringRequest;
import com.lyric.android.app.api.net.core.Volley;
import com.lyric.android.app.constants.Constants;
import com.lyric.android.library.utils.MD5Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ganyu
 * @description API接口
 * @time 2016/1/27 15:28
 */
public abstract class Api implements ApiPath, Constants {
    protected RequestQueue mRequestQueue;

    public Api() {
        this(BaseApplication.getContext());
    }

    public Api(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
    }

    /**
     * 封装请求参数
     * @param params 请求参数
     * @param encode 编码格式
     * @return 请求参数
     */
    public String buildParams(Map<String, String> params, String encode) {
        StringBuilder stringBuilder = new StringBuilder();
        if (TextUtils.isEmpty(encode)) {
            encode = Encode.UTF_8;
        }
        // 判断参数是否为空
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                try {
                    if (TextUtils.isEmpty(entry.getValue())) {
                        stringBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                    } else {
                        stringBuilder.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), encode)).append("&");
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            if (stringBuilder.length() > 0) {
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 封装请求地址
     * @param url 请求地址
     * @param params 请求参数
     * @return 请求地址
     */
    public String buildUrl(String url, String params) {
        if (!TextUtils.isEmpty(params)) {
            // 判断地址是否带参数
            if (url.contains("?") || url.contains("&")) {
                url = url + "&" + params;
            } else {
                url = url + "?" + params;
            }
        }
        return url;
    }

    /**
     * 创建默认参数
     * @return 默认参数
     */
    public Map<String, String> buildDefaultParams() {
        Map<String, String> defaultParams = new HashMap<>();
        long timestamp = System.currentTimeMillis();
        defaultParams.put("access", MD5Utils.getMD5(String.valueOf(timestamp), MD5Utils.CASE_LOWER) + timestamp);

        return defaultParams;
    }

    public void request(String url, Map<String, String> params, int flag, Handler handler) {
        this.request(url, Request.Method.GET, params, flag, handler);
    }

    public void request(String url, Map<String, String> params, int flag, Handler handler, boolean shouldCache) {
        this.request(url, Request.Method.GET, params, flag, handler, shouldCache);
    }

    public void request(String url, int method, Map<String, String> params, int flag, Handler handler) {
        this.request(url, method, params, flag, handler, true);
    }

    /**
     * 发起网络请求
     * @param url 请求地址
     * @param method 请求方式
     * @param params 请求参数
     * @param flag 请求标识
     * @param handler handler
     * @param shouldCache 是否缓存
     */
    public void request(String url, int method, final Map<String, String> params, final int flag, final Handler handler, boolean shouldCache) {
        StringRequest request = null;
        if (method == Request.Method.POST) {
            request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Message msg = handler.obtainMessage(flag, response);
                    msg.arg1 = ResponseCode.SUCCESS;
                    handler.sendMessage(msg);
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(RequestError error) {
                    Message msg = handler.obtainMessage(flag, error.getMessage());
                    msg.arg1 = ResponseCode.ERROR;
                    handler.sendMessage(msg);
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws RequestError {
                    return params;
                }
            };
        } else {
            url = buildUrl(url, buildParams(params, Encode.UTF_8));
            request = new StringRequest(url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Message msg = handler.obtainMessage(flag, response);
                    msg.arg1 = ResponseCode.SUCCESS;
                    handler.sendMessage(msg);
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(RequestError error) {
                    Message msg = handler.obtainMessage(flag, error.getMessage());
                    msg.arg1 = ResponseCode.ERROR;
                    handler.sendMessage(msg);
                }
            });
        }
        request.setTag(flag);
        request.setShouldCache(shouldCache);
        mRequestQueue.add(request);
    }

    /**
     * 取消请求
     * @param tag 标记
     */
    public void cancel(Object tag) {
        if (tag == null) {
            return;
        }
        mRequestQueue.cancelAll(tag);
    }
}
