package com.lyric.android.app.test;

import com.lyric.android.app.test.network.DataLoader;
import com.lyric.android.app.test.network.ResponseCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lyricgan
 * @time 2016/6/22 14:04
 */
public class DataApi {
    // 引用聚合测试数据
    private static final String TEST_URL = "http://v.juhe.cn/toutiao/index";
    private static volatile DataApi mInstance;

    private DataApi() {
    }

    public static DataApi getInstance() {
        if (mInstance == null) {
            synchronized (DataApi.class) {
                if (mInstance == null) {
                    mInstance = new DataApi();
                }
            }
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
