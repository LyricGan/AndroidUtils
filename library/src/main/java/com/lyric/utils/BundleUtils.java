package com.lyric.utils;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author lyric
 * @description
 * @time 2016/3/22 19:49
 */
public class BundleUtils {
    private final Bundle mBundle = new Bundle();

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

    public Bundle getBundle() {
        return mBundle;
    }

    public <T extends Fragment> T setArguments(T fragment) {
        fragment.setArguments(getBundle());
        return fragment;
    }

    public <T extends android.app.Fragment> T setArguments(T fragment) {
        fragment.setArguments(getBundle());
        return fragment;
    }
}
