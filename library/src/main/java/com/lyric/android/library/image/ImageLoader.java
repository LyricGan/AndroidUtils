package com.lyric.android.library.image;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * @author lyric
 * @description 网络图片加载类
 * @time 2016/6/3 13:23
 */
public class ImageLoader {

    private ImageLoader() {
    }

    public static void load(Context context, String url, ImageView view) {
        Glide.with(context).load(url).into(view);
    }

    public static void load(Context context, Uri uri, ImageView view) {
        Glide.with(context).load(uri).into(view);
    }

    public static void load(Activity activity, String url, ImageView view) {
        Glide.with(activity).load(url).into(view);
    }

    public static void load(FragmentActivity activity, String url, ImageView view) {
        Glide.with(activity).load(url).into(view);
    }

    public static void load(Fragment fragment, String url, ImageView view) {
        Glide.with(fragment).load(url).into(view);
    }

    public static void load(android.app.Fragment fragment, String url, ImageView view) {
        Glide.with(fragment).load(url).into(view);
    }
}
