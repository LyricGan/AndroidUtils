package com.lyric.android.app.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import com.lyric.utils.CheckUtils;

/**
 * @author 应用权限控制工具类
 * @description
 * @time 2016/6/24 11:41
 */
public class PermissionUtils {
    public static final String CAMERA = Manifest.permission.CAMERA;
    public static final String READ_CONTACTS = Manifest.permission.READ_CONTACTS;
    public static final String WRITE_CONTACTS = Manifest.permission.WRITE_CONTACTS;
    public static final String READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    public static final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final String RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;

    private PermissionUtils() {
    }

    /**
     * 判断权限是否被拒绝
     * @param context Context
     * @param permission 应用权限
     * @return returns true if permission is denied
     */
    public static boolean check(Context context, String permission) {
        return (PackageManager.PERMISSION_DENIED == ContextCompat.checkSelfPermission(context, permission));
    }

    /**
     * 判断权限是否全部被拒绝
     * @param context Context
     * @param permissions 应用权限
     * @return returns true if permissions is denied
     */
    public static boolean check(Context context, String... permissions) {
        CheckUtils.checkContext(context);
        for (String permission : permissions) {
            if (check(context, permission)) {
                return false;
            }
        }
        return true;
    }
}
