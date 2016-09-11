package com.lyric.android.app.glide;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.GlideModule;

/**
 * @author lyricgan
 * @description Configure Glide to set desired image quality.
 * @time 2016/9/11 15:00
 */
public class ImageModule implements GlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        boolean isLowRamDevice = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            isLowRamDevice = activityManager.isLowRamDevice();
        }
        builder.setDecodeFormat(isLowRamDevice ? DecodeFormat.PREFER_RGB_565 : DecodeFormat.PREFER_ARGB_8888);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
    }
}
