package com.lyric.android.app.utils;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.AnimRes;
import android.support.annotation.InterpolatorRes;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LayoutAnimationController;

/**
 * 动画工具类
 *
 * @author lyricgan
 */
public class AnimationCompatUtils {

    public static long currentAnimationTimeMillis() {
        return AnimationUtils.currentAnimationTimeMillis();
    }

    public static Animation loadAnimation(Context context, @AnimRes int id)
            throws Resources.NotFoundException {
        return AnimationUtils.loadAnimation(context, id);
    }

    public static LayoutAnimationController loadLayoutAnimation(Context context, @AnimRes int id)
            throws Resources.NotFoundException {
        return AnimationUtils.loadLayoutAnimation(context, id);
    }

    public static Animation makeInAnimation(Context c, boolean fromLeft) {
        return AnimationUtils.makeInAnimation(c, fromLeft);
    }

    public static Animation makeOutAnimation(Context c, boolean toRight) {
        return AnimationUtils.makeOutAnimation(c, toRight);
    }

    public static Animation makeInChildBottomAnimation(Context c) {
        return AnimationUtils.makeInChildBottomAnimation(c);
    }

    public static Interpolator loadInterpolator(Context context, @AnimRes @InterpolatorRes int id)
            throws Resources.NotFoundException {
        return AnimationUtils.loadInterpolator(context, id);
    }
}
