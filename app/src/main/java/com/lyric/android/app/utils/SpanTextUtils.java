package com.lyric.android.app.utils;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

/**
 * @author lyricgan
 * @time 2016/5/26 15:57
 */
public class SpanTextUtils {

    private SpanTextUtils() {
    }

    public static SpannableStringBuilder append(StyleText styleText) {
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        String str = stringBuilder.toString();
        stringBuilder.append(styleText.text);
        stringBuilder.setSpan(new ForegroundColorSpan(styleText.textColor), str.length(), str.length() + styleText.text.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (styleText.textSize > 0) {
            stringBuilder.setSpan(new AbsoluteSizeSpan(styleText.textSize * 3), str.length(), str.length() + styleText.text.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return stringBuilder;
    }

    /**
     * 添加指定文字大小和颜色的文本：append(new TextStyle("aaa", 12, Color.BLUE))
     * @param context Context
     * @param styleText {@link StyleText}
     * @return SpannableStringBuilder
     */
    public static SpannableStringBuilder append(Context context, StyleText styleText) {
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        String str = stringBuilder.toString();
        stringBuilder.append(styleText.text);
        stringBuilder.setSpan(new ForegroundColorSpan(styleText.textColor), str.length(), str.length() + styleText.text.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (styleText.textSize > 0) {
            stringBuilder.setSpan(new AbsoluteSizeSpan(DisplayUtils.px2dip(context, styleText.textSize)), str.length(), str.length()
                    + styleText.text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return stringBuilder;
    }

    public static class StyleText {
        String text;
        int textSize;
        int textColor;

        public StyleText(String text, int textSize, int textColor) {
            this.text = text;
            this.textSize = textSize;
            this.textColor = textColor;
        }

        @Override
        public String toString() {
            return text;
        }
    }
}
