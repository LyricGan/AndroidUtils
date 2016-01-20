package com.lyric.android.library.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

/**
 * @author ganyu
 * @description
 * @time 2016/1/20 14:41
 */
public class ActivityUtils {
    
    private ActivityUtils() {
    }

    public static void openActivity(Activity activity, Class<?> cls) {
        openActivity(activity, cls, null);
    }

    public static void openActivity(Activity activity, Class<?> cls, Bundle bundle) {
        openActivity(activity, cls, bundle, 0);
    }

    public static void openActivityForResult(Activity activity, Class<?> cls, int requestCode) {
        openActivity(activity, cls, null, requestCode);
    }

    public static void openActivityForResult(Activity activity, Class<?> cls, Bundle bundle, int requestCode) {
        openActivity(activity, cls, bundle, requestCode);
    }

    public static void openActivity(Activity activity, Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent(activity, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (requestCode == 0) {
            activity.startActivity(intent);
        } else {
            activity.startActivityForResult(intent, requestCode);
        }
    }

    public static void openActivity(Activity activity, String action) {
        Intent intent = new Intent(action);
        activity.startActivity(intent);
    }

    public static void openActivity(Activity activity, String action, Uri uri) {
        Intent intent = new Intent(action, uri);
        activity.startActivity(intent);
    }

    public static void openActivity(Activity activity, String action, Bundle bundle) {
        Intent intent = new Intent(action);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        activity.startActivity(intent);
    }
}
