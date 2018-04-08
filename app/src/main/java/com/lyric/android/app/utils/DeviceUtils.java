package com.lyric.android.app.utils;

import android.Manifest;
import android.content.Context;
import android.telephony.TelephonyManager;

import java.util.UUID;

/**
 * 设备相关工具类
 * @author lyricgan
 */
public class DeviceUtils {

    /**
     * 获取设备ID
     * @param context 上下文
     * @return 设备ID
     */
    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null) {
            if (PermissionUtils.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                return tm.getDeviceId();
            }
        }
        return null;
    }

    /**
     * 获取唯一标识ID
     * @return 唯一标识ID
     */
    public static String getUniqueId() {
        return UUID.randomUUID().toString();
    }
}
