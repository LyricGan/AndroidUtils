package com.lyricgan.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import java.lang.ref.WeakReference;

/**
 * Toast工具类
 * @author Lyric Gan
 */
public class ToastUtils {
    private static WeakReference<Toast> sToast;

    private ToastUtils() {
    }

    public static void showToast(Context context, int textId) {
        Toast toast = sToast != null ? sToast.get() : null;
        if (toast == null) {
            toast = Toast.makeText(context, textId, Toast.LENGTH_SHORT);
            sToast = new WeakReference<>(toast);
        }
        toast.setText(textId);
        toast.show();
    }

    public static void showToast(Context context, CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        Toast toast = sToast != null ? sToast.get() : null;
        if (toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            sToast = new WeakReference<>(toast);
        }
        toast.setText(text);
        toast.show();
    }
}