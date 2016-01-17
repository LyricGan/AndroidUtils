package com.lrc.baseand.bitmap;

import android.content.Context;

/**
 * @author ganyu
 * @description image factory
 * @time 16/1/16 下午11:01
 */
public class ImageFactory {
    private static ImageBuilder mImageBuilder;

    public static ImageBuilder create(Context context) {
        if (mImageBuilder == null) {
            mImageBuilder = new ImageBuilder(context.getApplicationContext());
        }
        return mImageBuilder;
    }
}
