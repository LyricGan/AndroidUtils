package com.lyric.android.library.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * @author lyricgan
 * @description
 * @time 2016/1/20 14:41
 */
public class ActivityUtils {
    
    private ActivityUtils() {
    }

    public static void startActivity(Context context, Class<? extends Activity> cls) {
        startActivity(context, cls, null);
    }

    public static void startActivity(Context context, Class<? extends Activity> cls, Bundle bundle) {
        Intent intent = new Intent(context, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(context, intent);
    }

    public static void startActivity(Context context, String action) {
        Intent intent = new Intent(action);
        startActivity(context, intent);
    }

    public static void startActivity(Context context, String action, Uri uri) {
        Intent intent = new Intent(action, uri);
        startActivity(context, intent);
    }

    public static void startActivity(Context context, String action, Bundle bundle) {
        Intent intent = new Intent(action);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(context, intent);
    }

    public static void startActivity(Context context, Intent intent) {
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public static void startActivityForResult(Activity activity, Class<? extends Activity> cls, int requestCode) {
        startActivityForResult(activity, cls, requestCode, null);
    }

    public static void startActivityForResult(Activity activity, Class<? extends Activity> cls, int requestCode, Bundle bundle) {
        Intent intent = new Intent(activity, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(activity, intent, requestCode);
    }

    public static void startActivityForResult(Activity activity, Intent intent, int requestCode) {
        activity.startActivityForResult(intent, requestCode);
    }

    public static void startActivityForResult(Fragment fragment, Class<? extends Activity> cls, int requestCode) {
        startActivityForResult(fragment, cls, requestCode, null);
    }

    public static void startActivityForResult(Fragment fragment, Class<? extends Activity> cls, int requestCode, Bundle bundle) {
        Intent intent = new Intent(fragment.getContext(), cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(fragment, intent, requestCode);
    }

    public static void startActivityForResult(Fragment fragment, Intent intent, int requestCode) {
        fragment.startActivityForResult(intent, requestCode);
    }
}
