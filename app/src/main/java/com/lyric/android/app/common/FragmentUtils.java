package com.lyric.android.app.common;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * @author lyricgan
 * @date 2018/8/16 下午1:01
 */
public class FragmentUtils {

    public static Fragment initFragment(Context context, Class<?> fragmentClass, Bundle args) {
        return initFragment(context, fragmentClass.getName(), args);
    }

    public static Fragment initFragment(Context context, String fragmentName, Bundle args) {
        Fragment fragment = null;
        try {
            fragment = Fragment.instantiate(context, fragmentName, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fragment;
    }

    public static void addFragment(FragmentManager fragmentManager, int containerViewId, Fragment fragment, String tag, boolean isAddToBackStack, String name) {
        FragmentTransaction transaction = fragmentManager.beginTransaction().add(containerViewId, fragment, tag);
        if (isAddToBackStack) {
            transaction.addToBackStack(name);
        }
        transaction.commitAllowingStateLoss();
    }

    public static void replaceFragment(FragmentManager fragmentManager, int containerViewId, Fragment fragment, String tag, boolean isAddToBackStack, String name) {
        FragmentTransaction transaction = fragmentManager.beginTransaction()
                .replace(containerViewId, fragment, tag);
        if (isAddToBackStack) {
            transaction.addToBackStack(name);
        }
        transaction.commitAllowingStateLoss();
    }
}
