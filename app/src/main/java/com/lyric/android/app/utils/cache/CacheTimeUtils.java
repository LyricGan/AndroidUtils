package com.lyric.android.app.utils.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;

/**
 * @author lyricgan
 * @description 时间计算工具类
 * @time 2016/7/22 12:06
 */
public class CacheTimeUtils {
    public static final int TIME_HOUR = 60 * 60;
    public static final int TIME_DAY = TIME_HOUR * 24;
    public static final char SEPARATOR = ' ';

    /**
     * 判断缓存数据是否已到期
     *
     * @param str String
     * @return true or false
     */
    public static boolean isExpire(String str) {
        return isExpire(str.getBytes());
    }

    /**
     * 判断缓存数据是否已到期
     *
     * @param data byte[]
     * @return true or false
     */
    public static boolean isExpire(byte[] data) {
        String[] dateInfo = getDateInfoFromDate(data);
        if (dateInfo != null && dateInfo.length == 2) {
            String expireTimeStr = dateInfo[0];
            while (expireTimeStr.startsWith("0")) {
                expireTimeStr = expireTimeStr.substring(1, expireTimeStr.length());
            }
            long expireTime = Long.valueOf(expireTimeStr);
            long deleteAfter = Long.valueOf(dateInfo[1]);
            if (System.currentTimeMillis() > expireTime + deleteAfter * 1000) {
                return true;
            }
        }
        return false;
    }

    public static String newStringWithDateInfo(int expireTime, String value) {
        return createDateInfo(expireTime) + value;
    }

    public static byte[] newByteArrayWithDateInfo(int expireTime, byte[] data2) {
        byte[] data1 = createDateInfo(expireTime).getBytes();
        byte[] retdata = new byte[data1.length + data2.length];
        System.arraycopy(data1, 0, retdata, 0, data1.length);
        System.arraycopy(data2, 0, retdata, data1.length, data2.length);
        return retdata;
    }

    public static String clearDateInfo(String strInfo) {
        if (strInfo != null && hasDateInfo(strInfo.getBytes())) {
            strInfo = strInfo.substring(strInfo.indexOf(SEPARATOR) + 1, strInfo.length());
        }
        return strInfo;
    }

    public static byte[] clearDateInfo(byte[] data) {
        if (hasDateInfo(data)) {
            return copyOfRange(data, indexOf(data, SEPARATOR) + 1, data.length);
        }
        return data;
    }

    public static boolean hasDateInfo(byte[] data) {
        return data != null && data.length > 15 && data[13] == '-' && indexOf(data, SEPARATOR) > 14;
    }

    public static String[] getDateInfoFromDate(byte[] data) {
        if (hasDateInfo(data)) {
            String saveDate = new String(copyOfRange(data, 0, 13));
            String deleteAfter = new String(copyOfRange(data, 14, indexOf(data, SEPARATOR)));
            return new String[]{saveDate, deleteAfter};
        }
        return null;
    }

    public static int indexOf(byte[] data, char c) {
        for (int i = 0; i < data.length; i++) {
            if (data[i] == c) {
                return i;
            }
        }
        return -1;
    }

    public static byte[] copyOfRange(byte[] original, int from, int to) {
        int newLength = to - from;
        if (newLength < 0)
            throw new IllegalArgumentException(from + " > " + to);
        byte[] copy = new byte[newLength];
        System.arraycopy(original, from, copy, 0, Math.min(original.length - from, newLength));
        return copy;
    }

    public static String createDateInfo(int second) {
        String currentTime = System.currentTimeMillis() + "";
        while (currentTime.length() < 13) {
            currentTime = "0" + currentTime;
        }
        return currentTime + "-" + second + SEPARATOR;
    }

    /*
     * Bitmap → byte[]
     */
    public static byte[] bitmap2Bytes(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /*
     * byte[] → Bitmap
     */
    public static Bitmap bytes2Bitmap(byte[] bytes) {
        if (bytes.length == 0) {
            return null;
        }
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /*
     * Drawable → Bitmap
     */
    public static Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }

    /*
     * Bitmap → Drawable
     */
    public static Drawable bitmap2Drawable(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
        bitmapDrawable.setTargetDensity(bitmap.getDensity());
        return new BitmapDrawable(bitmap);
    }
}
