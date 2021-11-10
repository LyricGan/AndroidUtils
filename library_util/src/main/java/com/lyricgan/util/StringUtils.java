package com.lyricgan.util;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static boolean isEmpty(String str) {
        return (str == null || str.length() == 0);
    }

    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }

    public static boolean isNull(String str) {
    	return (isEmpty(str) || "NULL".equalsIgnoreCase(str));
    }

    /**
     * get innerHtml from href
     * 
     * <pre>
     * getHrefInnerHtml(null)                                  = ""
     * getHrefInnerHtml("")                                    = ""
     * getHrefInnerHtml("mp3")                                 = "mp3";
     * getHrefInnerHtml("&lt;a innerHtml&lt;/a&gt;")                    = "&lt;a innerHtml&lt;/a&gt;";
     * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
     * getHrefInnerHtml("&lt;a&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
     * getHrefInnerHtml("&lt;a href="baidu.com"&gt;innerHtml&lt;/a&gt;")               = "innerHtml";
     * getHrefInnerHtml("&lt;a href="baidu.com" title="baidu"&gt;innerHtml&lt;/a&gt;") = "innerHtml";
     * getHrefInnerHtml("   &lt;a&gt;innerHtml&lt;/a&gt;  ")                           = "innerHtml";
     * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                      = "innerHtml";
     * getHrefInnerHtml("jack&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                  = "innerHtml";
     * getHrefInnerHtml("&lt;a&gt;innerHtml1&lt;/a&gt;&lt;a&gt;innerHtml2&lt;/a&gt;")        = "innerHtml2";
     * </pre>
     * 
     * @param href String
     * @return <ul>
     *         <li>if href is null, return ""</li>
     *         <li>if not match regx, return source</li>
     *         <li>return the last string that match regx</li>
     *         </ul>
     */
    public static String getHrefInnerHtml(String href) {
        if (StringUtils.isEmpty(href)) {
            return "";
        }
        String reg = ".*<[\\s]*a[\\s]*.*>(.+?)<[\\s]*/a[\\s]*>.*";
        Matcher matcher = Pattern.compile(reg, Pattern.CASE_INSENSITIVE).matcher(href);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        return href;
    }

    public static int getCharLength(String value) {
        if (TextUtils.isEmpty(value)) {
            return 0;
        }
        int valueLength = 0;
        String chineseChar = "[\u0391-\uFFE5]";
        // 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
        for (int i = 0; i < value.length(); i++) {
            String temp = value.substring(i, i + 1);
            if (temp.matches(chineseChar)) {
                valueLength += 2;
            } else {
                valueLength += 1;
            }
        }
        return valueLength;
    }

    public static boolean containsChineseChar(String value) {
        if (TextUtils.isEmpty(value)) {
            return false;
        }
        String chineseChar = "[\u0391-\uFFE5]";
        for (int i = 0; i < value.length(); i++) {
            String temp = value.substring(i, i + 1);
            if (temp.matches(chineseChar)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNumber(String str) {
        return Pattern.compile("[0-9]*").matcher(str).matches();
    }

    public static boolean isFloat(String str) {
        return Pattern.compile("[0-9]*(\\.?)[0-9]*").matcher(str).matches();
    }

    public static String getDouble(double value, int newScale) {
        return new BigDecimal(Double.toString(value))
                .setScale(newScale, BigDecimal.ROUND_DOWN).toPlainString();
    }

    public static byte[] longToBytes(long value) {
        byte[] bytes = new byte[8];
        int len = 8;
        for (byte b : bytes) {
            b = (byte) (value & 0xff);
            bytes[--len] = b;
            value >>= 8;
        }
        return bytes;
    }

    public static int parseInt(String value, int defaultValue) {
        if (TextUtils.isEmpty(value)) {
            return defaultValue;
        }
        int valueInt = defaultValue;
        try {
            valueInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            try {
                valueInt = Double.valueOf(value).intValue();
            } catch (NumberFormatException e1) {
                e1.printStackTrace();
            }
        }
        return valueInt;
    }

    public static float parseFloat(String value, float defaultValue) {
        if (TextUtils.isEmpty(value)) {
            return defaultValue;
        }
        float valueFloat = defaultValue;
        try {
            valueFloat = Float.valueOf(value);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return valueFloat;
    }

    public static double parseDouble(String value, double defaultValue) {
        if (TextUtils.isEmpty(value)) {
            return defaultValue;
        }
        double valueDouble = defaultValue;
        try {
            valueDouble = Double.valueOf(value);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return valueDouble;
    }

    public static long parseLong(String value, long defaultValue) {
        if (TextUtils.isEmpty(value)) {
            return defaultValue;
        }
        long valueLong = defaultValue;
        try {
            valueLong = Long.valueOf(value);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return valueLong;
    }

    public static short parseShort(String value, short defaultValue) {
        if (TextUtils.isEmpty(value)) {
            return defaultValue;
        }
        short valueShort = defaultValue;
        try {
            valueShort = Short.valueOf(value);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return valueShort;
    }

    /**
     * 对于浮点数进行格式化
     * @param number 浮点数
     * @param decimalsCount 保留的小数位数
     * @param isRetainInteger 如果是整数是否保留小数点后面的0
     * @return 格式化字符串
     */
    public static String formatDecimals(double number, int decimalsCount, boolean isRetainInteger) {
        String value;
        if (isRetainInteger) {
            value = String.format(Locale.getDefault(), "%1$." + decimalsCount + "f", number);
        } else {
            if (Math.round(number) - number == 0) {
                value = String.valueOf((long) number);
            } else {
                value = String.format(Locale.getDefault(), "%1$." + decimalsCount + "f", number);
            }
        }
        return value;
    }

    public static String combineString(String... strings) {
        if (strings == null || strings.length <= 0) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (String arg : strings) {
            builder.append(arg);
        }
        return builder.toString();
    }

    public static <T> String toString(List<T> list) {
        StringBuilder builder = new StringBuilder();
        for (T item : list) {
            builder.append(item.toString()).append("\n");
        }
        return builder.toString();
    }

    public static <K, V> String toString(Map<K, V> map) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry item : map.entrySet()) {
            builder.append(item.getKey()).append("=").append(item.getValue()).append("\n");
        }
        return builder.toString();
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

    public static SpannableString setSubCharacterStyle(CharSequence source, CharacterStyle characterStyle, int start, int end, int flags) {
        return setSubSpan(source, characterStyle, start, end, flags);
    }

    public static SpannableString setSubSpan(CharSequence source, Object object, int start, int end, int flags) {
        if (source == null || object == null) {
            return null;
        }
        if (start < 0 || end < 0 || start >= end) {
            return null;
        }
        SpannableString spannableString = SpannableString.valueOf(source);
        spannableString.setSpan(object, start, end, flags);
        return spannableString;
    }

    public static SpannableStringBuilder append(String text, int textColor, int textDimensionId) {
        int textSize = Resources.getSystem().getDimensionPixelSize(textDimensionId);
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        String str = stringBuilder.toString();
        stringBuilder.append(text);
        int start = str.length();
        int end = start + text.length();
        stringBuilder.setSpan(new ForegroundColorSpan(textColor), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringBuilder.setSpan(new AbsoluteSizeSpan(textSize), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return stringBuilder;
    }

    /**
     * 关键字高亮变色
     * @param text    文字
     * @param keyword 文字中的关键字
     * @param color   变化的色值
     * @return SpannableString
     */
    public static SpannableString matcherText(String text, String keyword, int color) {
        return matcherText(text, keyword, color, null);
    }

    public static SpannableString matcherText(String text, String keyword, int color, CharacterStyle characterStyle) {
        SpannableString s = new SpannableString(text);
        return matcherIn(s, keyword, color, characterStyle);
    }

    /**
     * 多个关键字高亮变色
     * @param text    文字
     * @param keywords 文字中的关键字数组
     * @param color   变化的色值
     * @return SpannableString
     */
    public static SpannableString matcherText(String text, String[] keywords, int color) {
        return matcherText(text, keywords, color, null);
    }

    public static SpannableString matcherText(String text, String[] keywords, int color, CharacterStyle characterStyle) {
        SpannableString s = new SpannableString(text);
        for (String keyword: keywords) {
            s = matcherIn(s, keyword, color, characterStyle);
        }
        return s;
    }

    private static SpannableString matcherIn(SpannableString s, String keyword, int color, CharacterStyle characterStyle) {
        Pattern p = Pattern.compile(keyword);
        Matcher m = p.matcher(s);
        if (characterStyle == null) {
            characterStyle = new ForegroundColorSpan(color);
        }
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            s.setSpan(characterStyle, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return s;
    }

    public static String getStackTraceMessage(Thread thread, Class<?> cls) {
        StackTraceElement[] stackTraceElements = thread.getStackTrace();
        if (stackTraceElements == null || stackTraceElements.length <= 0) {
            return null;
        }
        for (StackTraceElement element : stackTraceElements) {
            if (element.isNativeMethod()) {
                continue;
            }
            if (element.getClassName().equals(Thread.class.getName())) {
                continue;
            }
            if (element.getClassName().equals(cls.getName())) {
                continue;
            }
            return "[ " + thread.getName() + ": "
                    + element.getFileName() + ":"
                    + element.getLineNumber() + " "
                    + element.getMethodName() + " ]";
        }
        return null;
    }

    /**
     * 输入限制小数点两位
     */
    public static final InputFilter DECIMAL_INPUT_FILTER = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            String destValue = dest.toString();
            if (destValue.contains(".")) {
                String[] values = destValue.split("\\.");
                if (values.length > 1) {
                    String rightValue = values[1];
                    if (rightValue.length() > 1 && (dstart > (destValue.length() - 1))) {
                        source = "";
                    }
                }
            } else if (".".equals(source.toString())) {
                if (dstart < (destValue.length() - 2)) {
                    source = "";
                }
            }
            return source;
        }
    };
}
