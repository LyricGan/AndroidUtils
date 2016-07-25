package com.lyric.android.app.network;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lyricgan
 * @description
 * @time 2016/6/22 14:04
 */
public class TestApi {
    private static final String TEST_URL = "http://v.juhe.cn/toutiao/index";
    private static TestApi mInstance;

    private TestApi() {
    }

    public static synchronized TestApi getInstance() {
        if (mInstance == null) {
            mInstance = new TestApi();
        }
        return mInstance;
    }

    public Map<String, String> buildDefaultParams() {
        Map<String, String> params = new HashMap<>();
        params.put("device", "android");
        params.put("key", "f909a4cf8e87f8553c95f6d4989d1559");// 聚合数据APP KEY，用来测试
        return params;
    }

    // 类型,top(头条，默认),shehui(社会),guonei(国内),guoji(国际),yule(娱乐),tiyu(体育)junshi(军事),keji(科技),caijing(财经),shishang(时尚)
    public DataLoader queryNews(String keys, ResponseCallback<String> callback) {
        Map<String, String> params = buildDefaultParams();
        params.put("type", keys);
        DataLoader dataLoader = new DataLoader<>(TEST_URL, params, String.class, callback);
        dataLoader.load();
        return dataLoader;
    }
}
