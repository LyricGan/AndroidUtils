package com.lyric.android.app.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

/**
 * 权限相关工具类
 * @author lyricgan
 */
public class PermissionUtils {

    /**
     * 检查权限是否已被允许
     * @param context 上下文
     * @param permission 应用权限名称
     * @return true or false
     */
    public static boolean checkSelfPermission(@NonNull Context context, @NonNull String permission) {
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }
}
