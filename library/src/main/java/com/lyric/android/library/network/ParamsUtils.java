package com.lyric.android.library.network;

import android.text.TextUtils;

import com.lyric.android.library.utils.LogUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author 请求参数工具类
 * @description
 * @time 2016/6/22 16:49
 */
public class ParamsUtils {

    private ParamsUtils() {
    }

    public static String encodeParams(Map<String, String> params, String encode) {
        if (TextUtils.isEmpty(encode)) {
            encode = HttpConstants.UTF_8;
        }
        if (params == null || params.isEmpty()) {
            return "";
        }
        try {
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (TextUtils.isEmpty(key)) {
                    continue;
                }
                if (TextUtils.isEmpty(value)) {
                    builder.append(key).append("=").append(value).append("&");
                } else {
                    builder.append(key).append("=").append(URLEncoder.encode(value, encode)).append("&");
                }
            }
            builder.deleteCharAt(builder.length() - 1);
            return builder.toString();
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public static String buildGetUrl(String url, Map<String, String> params, String encode) {
        String requestParams = encodeParams(params, encode);
        // 判断传递参数是否为空
        if (!TextUtils.isEmpty(requestParams)) {
            // 判断地址是否带参数
            if (url.contains("?") || url.contains("&")) {
                url = url + "&" + requestParams;
            } else {
                url = url + "?" + requestParams;
            }
        }
        return url;
    }

    public static String encodeSpecialParams(Map<String, String> params, String encode) {
        if (TextUtils.isEmpty(encode)) {
            encode = HttpConstants.UTF_8;
        }
        if (params == null || params.isEmpty()) {
            return "";
        }
        try {
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (TextUtils.isEmpty(key)) {
                    continue;
                }
                if (TextUtils.isEmpty(value)) {
                    builder.append(key).append("/").append(value).append("/");
                } else {
                    builder.append(key).append("/").append(URLEncoder.encode(value, encode)).append("/");
                }
            }
            builder.deleteCharAt(builder.length() - 1);
            return builder.toString();
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public static String buildSpecialGetUrl(String url, Map<String, String> params, String encode) {
        String requestParams = "";
        if (params == null || params.isEmpty()) {
            return url;
        }
        if (TextUtils.isEmpty(url)) {
            LogUtils.e(HttpConstants.HTTP_TAG, "Request url can not be null.");
            return "";
        }
        requestParams = encodeSpecialParams(params, encode);
        // 判断传递参数是否为空
        if (!TextUtils.isEmpty(requestParams)) {
            if ("/".equals(url.substring(url.length() - 1))) {
                url = url + requestParams;
            } else {
                url = url + "/" + requestParams;
            }
        }
        return url;
    }
}
