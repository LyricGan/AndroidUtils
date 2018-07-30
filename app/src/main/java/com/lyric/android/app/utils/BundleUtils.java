package com.lyric.android.app.utils;

import android.os.Bundle;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author lyricgan
 * @time 2016/3/22 19:49
 */
public class BundleUtils {
    private Bundle mBundle;

    private BundleUtils() {
        if (mBundle == null) {
            mBundle = new Bundle();
        } else {
            mBundle.clear();
        }
    }

    private static final class BundleUtilsHolder {
        private static final BundleUtils mInstance = new BundleUtils();
    }

    public static BundleUtils getInstance() {
        return BundleUtilsHolder.mInstance;
    }

    public Bundle getBundle() {
        return mBundle;
    }

    public BundleUtils putInt(String key, int value) {
        mBundle.putInt(key, value);
        return this;
    }

    public BundleUtils putString(String key, String value) {
        mBundle.putString(key, value);
        return this;
    }

    public BundleUtils putLong(String key, long value) {
        mBundle.putLong(key, value);
        return this;
    }

    public BundleUtils putBoolean(String key, boolean value) {
        mBundle.putBoolean(key, value);
        return this;
    }

    public BundleUtils putFloat(String key, float value) {
        mBundle.putFloat(key, value);
        return this;
    }

    public BundleUtils putDouble(String key, double value) {
        mBundle.putDouble(key, value);
        return this;
    }

    public BundleUtils putSerializable(String key, Serializable data) {
        mBundle.putSerializable(key, data);
        return this;
    }

    public BundleUtils putParcelable(String key, Parcelable data) {
        mBundle.putParcelable(key, data);
        return this;
    }

    public BundleUtils putStringArrayList(String key, ArrayList<String> value) {
        mBundle.putStringArrayList(key, value);
        return this;
    }

}
