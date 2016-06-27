package com.lyric.android.app.network;

import com.lyric.android.library.network.DataLoader;
import com.lyric.android.library.network.ResponseCallback;

import java.util.Map;

/**
 * @author lyric
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

    // 类型,,top(头条，默认),shehui(社会),guonei(国内),guoji(国际),yule(娱乐),tiyu(体育)junshi(军事),keji(科技),caijing(财经),shishang(时尚)
    public DataLoader queryNews(String keys, ResponseCallback<String> callback) {
        Map<String, String> params = BaseApi.getInstance().buildDefaultParams();
        params.put("type", keys);
        DataLoader dataLoader = new DataLoader<>(TEST_URL, params, String.class, callback);
        dataLoader.load();
        return dataLoader;
    }
}
