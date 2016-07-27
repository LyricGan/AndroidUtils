package com.lyric.android.app.widget.webview;

import android.content.Context;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lyric
 * @description js调用接口
 * @time 2016/6/23 14:22
 */
public class JsInterfaceImpl {
    public static final String INTERFACE_NAME = "imagelistner";
    private Context mContext;
    private List<String> mImageUrlList;

    public JsInterfaceImpl(Context context) {
        this.mContext = context;
        this.mImageUrlList = new ArrayList<>();
    }

    @JavascriptInterface
    public void test() {
    }

    /**
     * 添加图片地址
     * @param imageUrl 图片地址
     */
    @JavascriptInterface
    public void addImageUrl(String imageUrl) {
        // 判断图片地址是否为空
        if (!TextUtils.isEmpty(imageUrl)) {
            String replaceUrl = imageUrl.replace("_small", "");
            mImageUrlList.add(replaceUrl);
        }
    }

    @JavascriptInterface
    public void openImage(int imageIndex, String imageUrl) {
        // 判断图片地址是否为空
        if (!TextUtils.isEmpty(imageUrl)) {
            String replaceUrl = imageUrl.replace("_small", "");
            openImageActivity(mContext, replaceUrl, mImageUrlList);
        }
    }

    /**
     * 跳转到图片查看界面
     * @param context Context
     * @param imageUrl 图片地址
     * @param imageUrlList 图片地址列表
     */
    protected void openImageActivity(Context context, String imageUrl, List<String> imageUrlList) {
        if (imageUrlList == null || imageUrlList.size() <= 0) {
            return;
        }
        final int length = imageUrlList.size();
        String[] imageUrlArray = new String[length];
        int imageIndex = 0;
        for (int i = 0; i < length; i++) {
            imageUrlArray[i] = imageUrlList.get(i);
            if (imageUrlArray[i].equals(imageUrl)) {
                imageIndex = i;
            }
        }
        // TODO
    }
}
