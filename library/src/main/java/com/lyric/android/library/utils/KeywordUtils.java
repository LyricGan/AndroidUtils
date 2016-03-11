package com.lyric.android.library.utils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lyric
 * @description
 * @time 2016/3/11 13:56
 */
public class KeywordUtils {

    /**
     * 关键字高亮变色
     * @param text    文字
     * @param keyword 文字中的关键字
     * @param color   变化的色值
     * @return SpannableString
     */
    public static SpannableString matcherText(String text, String keyword, int color) {
        SpannableString s = new SpannableString(text);
        return matcherIn(s, keyword, color);
    }

    /**
     * 多个关键字高亮变色
     * @param text    文字
     * @param keywords 文字中的关键字数组
     * @param color   变化的色值
     * @return SpannableString
     */
    public static SpannableString matcherText(String text, String[] keywords, int color) {
        SpannableString s = new SpannableString(text);
        for (String keyword: keywords) {
            s = matcherIn(s, keyword, color);
        }
        return s;
    }

    private static SpannableString matcherIn(SpannableString s, String keyword, int color) {
        Pattern p = Pattern.compile(keyword);
        Matcher m = p.matcher(s);
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            s.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return s;
    }
}
