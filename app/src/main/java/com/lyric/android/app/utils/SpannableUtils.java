package com.lyric.android.app.utils;

import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ScaleXSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.text.style.URLSpan;

/**
 * 字符串拼接工具类
 * @author lyricgan
 */
public class SpannableUtils {

    public static SpannableString valueOf(CharSequence source) {
        return SpannableString.valueOf(source);
    }

    public static SpannableString setSubSpan(CharSequence source, Object object, int start, int end, int flags) {
        if (source == null || object == null) {
            return null;
        }
        if (start < 0 || end < 0 || start >= end) {
            return null;
        }
        SpannableString spannableString = valueOf(source);
        spannableString.setSpan(object, start, end, flags);
        return spannableString;
    }

    public static SpannableString setSubCharacterStyle(CharSequence source, CharacterStyle characterStyle, int start, int end, int flags) {
        return setSubSpan(source, characterStyle, start, end, flags);
    }

    /**
     * 获取指定位置缩放后的字符串
     * @param source 源字符串
     * @param scale 缩放倍数
     * @param start 起始位置
     * @param end 结束位置
     * @param flags 例如Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
     * @return 指定位置缩放后的字符串
     */
    public static SpannableString setSubRelativeSizeSpan(CharSequence source, float scale, int start, int end, int flags) {
        return setSubCharacterStyle(source, new RelativeSizeSpan(scale), start, end, flags);
    }

    public static SpannableString setSubAbsoluteSizeSpan(CharSequence source, int size, int start, int end, int flags) {
        return setSubAbsoluteSizeSpan(source, size, false, start, end, flags);
    }

    public static SpannableString setSubAbsoluteSizeSpan(CharSequence source, int size, boolean dip, int start, int end, int flags) {
        return setSubCharacterStyle(source, new AbsoluteSizeSpan(size, dip), start, end, flags);
    }

    public static SpannableString setSubForegroundColorSpan(CharSequence source, int color, int start, int end, int flags) {
        return setSubCharacterStyle(source, new ForegroundColorSpan(color), start, end, flags);
    }

    public static SpannableString setSubBackgroundColorSpan(CharSequence source, int color, int start, int end, int flags) {
        return setSubCharacterStyle(source, new BackgroundColorSpan(color), start, end, flags);
    }

    public static SpannableString setSubStyleSpan(CharSequence source, int style, int start, int end, int flags) {
        return setSubCharacterStyle(source, new StyleSpan(style), start, end, flags);
    }

    public static SpannableString setSubTypefaceSpan(CharSequence source, String family, int start, int end, int flags) {
        return setSubCharacterStyle(source, new TypefaceSpan(family), start, end, flags);
    }

    public static SpannableString setSubURLSpan(CharSequence source, String url, int start, int end, int flags) {
        return setSubCharacterStyle(source, new URLSpan(url), start, end, flags);
    }

    public static SpannableString setSubScaleXSpan(CharSequence source, float proportion, int start, int end, int flags) {
        return setSubCharacterStyle(source, new ScaleXSpan(proportion), start, end, flags);
    }

    public static SpannableString setSubImageSpan(CharSequence source, Drawable drawable, int start, int end, int flags) {
        return setSubImageSpan(source, drawable, ImageSpan.ALIGN_BOTTOM, start, end, flags);
    }

    public static SpannableString setSubImageSpan(CharSequence source, Drawable drawable, int verticalAlignment, int start, int end, int flags) {
        return setSubCharacterStyle(source, new ImageSpan(drawable, verticalAlignment), start, end, flags);
    }

    public static SpannableString setSubClickableSpan(CharSequence source, ClickableSpan clickableSpan, int start, int end, int flags) {
        return setSubCharacterStyle(source, clickableSpan, start, end, flags);
    }
}
