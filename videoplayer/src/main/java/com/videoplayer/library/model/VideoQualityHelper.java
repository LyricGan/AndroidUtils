package com.videoplayer.library.model;

import android.text.TextUtils;
import android.util.SparseArray;

/**
 * Created by lenovo on 2016/4/29.
 */
public class VideoQualityHelper {

    static SparseArray<String> dic = new SparseArray<>();

    static {
        dic.put(10, "标清");
        dic.put(11, "高清");
        dic.put(12, "超清");
        dic.put(100, "原画");

    }

    public static String getQualityName(int code) {
        String tmp = dic.get(code);
        if (TextUtils.isEmpty(tmp)) {//默认标清
            tmp = dic.get(10);
        }
        return tmp;
    }
}
