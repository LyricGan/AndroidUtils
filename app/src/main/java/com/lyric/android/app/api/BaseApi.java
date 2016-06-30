package com.lyric.android.app.api;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lyric
 * @description 网络请求基类
 * @time 2016/6/22 16:59
 */
public class BaseApi {
    private static BaseApi mInstance;

    private BaseApi() {
    }

    public static synchronized BaseApi getInstance() {
        if (mInstance == null) {
            mInstance = new BaseApi();
        }
        return mInstance;
    }

    public Map<String, String> buildDefaultParams() {
        Map<String, String> params = new HashMap<>();
        params.put("device", "android");
        params.put("key", "f909a4cf8e87f8553c95f6d4989d1559");// 聚合数据APP KEY，用来测试
        return params;
    }
}
