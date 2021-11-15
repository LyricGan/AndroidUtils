package com.lyricgan.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

/**
 * Activity工具类
 * @author Lyric Gan
 */
public class ActivityUtils {

    private ActivityUtils() {
    }

    public static boolean isActivityInvalid(Activity activity) {
        if (activity == null || activity.isFinishing()) {
            return true;
        }
        boolean invalid = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            invalid = activity.isDestroyed();
        }
        return invalid;
    }

    public static void finishActivity(Activity activity) {
        if (isActivityInvalid(activity)) {
            return;
        }
        activity.finish();
    }

    public static void startActivity(Context context, Class<? extends Activity> cls) {
        startActivity(context, cls, null);
    }

    public static void startActivity(Context context, Class<? extends Activity> cls, Bundle extras) {
        startActivity(context, cls, extras, 0);
    }

    public static void startActivity(Context context, Class<? extends Activity> cls, Bundle extras, int flags) {
        if (context == null) {
            return;
        }
        Intent intent = getIntent(context, cls, extras, flags);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, String action) {
        Intent intent = new Intent(action);
        try {
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startActivity(Context context, Intent intent) {
        if (context == null || intent == null) {
            return;
        }
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startActivityForResult(Activity activity, String action, int requestCode) {
        Intent intent = new Intent(action);
        try {
            intent.setData(Uri.parse("package:" + activity.getPackageName()));
            activity.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startActivityForResult(Activity activity, Class<? extends Activity> cls, int requestCode) {
        startActivityForResult(activity, cls, requestCode, null);
    }

    public static void startActivityForResult(Activity activity, Class<? extends Activity> cls, int requestCode, Bundle extras) {
        startActivityForResult(activity, cls, requestCode, extras, 0);
    }

    public static void startActivityForResult(Activity activity, Class<? extends Activity> cls, int requestCode, Bundle extras, int flags) {
        if (activity == null) {
            return;
        }
        Intent intent = getIntent(activity, cls, extras, flags);
        activity.startActivityForResult(intent, requestCode);
    }

    public static Intent getIntent(Context context, Class<? extends Activity> cls, Bundle extras) {
        return getIntent(context, cls, extras, 0);
    }

    public static Intent getIntent(Context context, Class<? extends Activity> cls, Bundle extras, int flags) {
        Intent intent = new Intent(context, cls);
        if (extras != null) {
            intent.putExtras(extras);
        }
        if (flags > 0) {
            intent.setFlags(flags);
        }
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        return intent;
    }

    public static Intent getIntent(String url) {
        if (url == null || url.length() == 0) {
            return null;
        }
        try {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            return intent;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
