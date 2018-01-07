package com.lyric.android.app.test;

import com.lyric.network.NetworkCallback;
import com.lyric.network.NetworkManager;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lyricgan
 * @date 2018/1/2 17:35
 */
public class Test {

    public static void requestNews(Object tag, NetworkCallback callback) {
        // 类型,top(头条，默认),shehui(社会),guonei(国内),guoji(国际),yule(娱乐),tiyu(体育)junshi(军事),keji(科技),caijing(财经),shishang(时尚)
        final String TEST_URL = "http://v.juhe.cn/toutiao/index";
        Map<String, String> params = new HashMap<>();
        params.put("device", "android");
        params.put("key", "f909a4cf8e87f8553c95f6d4989d1559");
        params.put("type", "top");
        NetworkManager.getInstance().get(TEST_URL, params, tag, callback);
    }
}
