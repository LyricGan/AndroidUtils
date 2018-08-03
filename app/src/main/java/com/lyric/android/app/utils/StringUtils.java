package com.lyric.android.app.utils;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
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

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * 
 * @author lyricgan
 * @created 2015-05-28
 */
public class StringUtils {
    public static final String EMPTY = "";

	/**
     * is null or its length is 0
     * 
     * <pre>
     * isEmpty(null) = true;
     * isEmpty(&quot;&quot;) = true;
     * isEmpty(&quot;  &quot;) = false;
     * </pre>
     * 
     * @param str String
     * @return if string is null or its size is 0, return true, else return false.
     */
    public static boolean isEmpty(String str) {
        return (str == null || str.length() == 0);
    }
	
	/**
     * is null or its length is 0 or it is made by space
     * 
     * <pre>
     * isBlank(null) = true;
     * isBlank(&quot;&quot;) = true;
     * isBlank(&quot;  &quot;) = true;
     * isBlank(&quot;a&quot;) = false;
     * isBlank(&quot;a &quot;) = false;
     * isBlank(&quot; a&quot;) = false;
     * isBlank(&quot;a b&quot;) = false;
     * </pre>
     * 
     * @param str String
     * @return if string is null or its size is 0 or it is made by space, return true, else return false.
     */
    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }
    
    /**
     * 返回字符串是否为空或者为'null'
     * @param str String
     * @return if string is null or "null" or its size is 0
     */
    public static boolean isNull(String str) {
    	return (isEmpty(str) || "NULL".equalsIgnoreCase(str));
    }
    
    /**
     * null string to empty string
     * 
     * <pre>
     * nullStrToEmpty(null) = &quot;&quot;;
     * nullStrToEmpty(&quot;&quot;) = &quot;&quot;;
     * nullStrToEmpty(&quot;aa&quot;) = &quot;aa&quot;;
     * </pre>
     * 
     * @param str String
     * @return
     */
    public static String nullStrToEmpty(String str) {
        return (str == null ? "" : str);
    }
    
    /**
     * capitalize first letter
     * 
     * <pre>
     * capitalizeFirstLetter(null)     =   null;
     * capitalizeFirstLetter("")       =   "";
     * capitalizeFirstLetter("2ab")    =   "2ab"
     * capitalizeFirstLetter("a")      =   "A"
     * capitalizeFirstLetter("ab")     =   "Ab"
     * capitalizeFirstLetter("Abc")    =   "Abc"
     * </pre>
     * 
     * @param str String
     * @return
     */
    public static String capitalizeFirstLetter(String str) {
        if (isEmpty(str)) {
            return str;
        }
        char c = str.charAt(0);
        return (!Character.isLetter(c) || Character.isUpperCase(c)) ? str : new StringBuilder(str.length())
                .append(Character.toUpperCase(c)).append(str.substring(1)).toString();
    }
    
    /**
     * encoded in utf-8
     * 
     * <pre>
     * utf8Encode(null)        =   null
     * utf8Encode("")          =   "";
     * utf8Encode("aa")        =   "aa";
     * utf8Encode("啊啊啊啊")   = "%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A";
     * </pre>
     * 
     * @param str String
     * @return
     */
    public static String utf8Encode(String str) {
        if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("UnsupportedEncodingException occurred. ", e);
            }
        }
        return str;
    }
    
    /**
     * encoded in utf-8, if exception, return defultReturn
     * 
     * @param str String
     * @param defaultReturn default value
     * @return
     */
    public static String utf8Encode(String str, String defaultReturn) {
        if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return defaultReturn;
            }
        }
        return str;
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
        if (isEmpty(href)) {
            return "";
        }

        String hrefReg = ".*<[\\s]*a[\\s]*.*>(.+?)<[\\s]*/a[\\s]*>.*";
        Pattern hrefPattern = Pattern.compile(hrefReg, Pattern.CASE_INSENSITIVE);
        Matcher hrefMatcher = hrefPattern.matcher(href);
        if (hrefMatcher.matches()) {
            return hrefMatcher.group(1);
        }
        return href;
    }
    
    /**
     * process special char in html
     * 
     * <pre>
     * htmlEscapeCharsToString(null) = null;
     * htmlEscapeCharsToString("") = "";
     * htmlEscapeCharsToString("mp3") = "mp3";
     * htmlEscapeCharsToString("mp3&lt;") = "mp3<";
     * htmlEscapeCharsToString("mp3&gt;") = "mp3\>";
     * htmlEscapeCharsToString("mp3&amp;mp4") = "mp3&mp4";
     * htmlEscapeCharsToString("mp3&quot;mp4") = "mp3\"mp4";
     * htmlEscapeCharsToString("mp3&lt;&gt;&amp;&quot;mp4") = "mp3\<\>&\"mp4";
     * </pre>
     * 
     * @param source String
     * @return
     */
    public static String htmlEscapeCharsToString(String source) {
        return StringUtils.isEmpty(source) ? source : source.replaceAll("&lt;", "<").replaceAll("&gt;", ">")
                .replaceAll("&amp;", "&").replaceAll("&quot;", "\"");
    }
    
    /**
     * transform half width char to full width char
     * 
     * <pre>
     * fullWidthToHalfWidth(null) = null;
     * fullWidthToHalfWidth("") = "";
     * fullWidthToHalfWidth(new String(new char[] {12288})) = " ";
     * fullWidthToHalfWidth("！＂＃＄％＆) = "!\"#$%&";
     * </pre>
     * 
     * @param s String
     * @return
     */
    public static String fullWidthToHalfWidth(String s) {
        if (isEmpty(s)) {
            return s;
        }
        char[] source = s.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == 12288) {
                source[i] = ' ';
                // } else if (source[i] == 12290) {
                // source[i] = '.';
            } else if (source[i] >= 65281 && source[i] <= 65374) {
                source[i] = (char)(source[i] - 65248);
            } else {
                source[i] = source[i];
            }
        }
        return new String(source);
    }
    
    /**
     * transform full width char to half width char
     * 
     * <pre>
     * halfWidthToFullWidth(null) = null;
     * halfWidthToFullWidth("") = "";
     * halfWidthToFullWidth(" ") = new String(new char[] {12288});
     * halfWidthToFullWidth("!\"#$%&) = "！＂＃＄％＆";
     * </pre>
     * 
     * @param s String
     * @return
     */
    public static String halfWidthToFullWidth(String s) {
        if (isEmpty(s)) {
            return s;
        }
        char[] source = s.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == ' ') {
                source[i] = (char)12288;
                // } else if (source[i] == '.') {
                // source[i] = (char)12290;
            } else if (source[i] >= 33 && source[i] <= 126) {
                source[i] = (char)(source[i] + 65248);
            } else {
                source[i] = source[i];
            }
        }
        return new String(source);
    }
    
    /**
     * 获取文件后缀名
     * @param fileName 文件名称
     * @return
     */
	public static String getFileSuffix(String fileName) {
		if (!TextUtils.isEmpty(fileName) && fileName.contains(".")) {
			return fileName.substring(fileName.lastIndexOf(".") + 1);
		}
		return "";
	}
	
	/**
	 * 删除文件后缀名
	 * @param fileName 文件名称
	 * @return
	 */
	public static String deleteFileSuffix(String fileName) {
		if (TextUtils.isEmpty(fileName)) {
			return fileName;
		}
		int index = fileName.lastIndexOf(".");
		if (index >= 0) {
			return fileName.substring(0, index);
		}
		return fileName;
	}

    /**
     * 判断字符串是否为手机号
     *
     * @param str 字符串
     * @return 是否为手机号
     */
    public static boolean isMobile(String str) {
        return str.matches("^((1[358][0-9])|(14[57])|(17[0678]))\\d{8}$");
    }

    /**
     * 判断是否为密码，不能包含特殊字符，空格、制表符、中文
     *
     * @param value 字符串，不能包含特殊字符，空格、制表符、中文
     * @return 是否为密码
     */
    public static boolean isPassword(String value) {
        if (TextUtils.isEmpty(value)) {
            return false;
        }
        if ((value.contains(" ") || value.contains("\t") || value.contains("\n") || value.contains("\r")) || hasChineseChar(value)) {
            return false;
        }
        return true;
    }

    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }

    /**
     * 隐藏手机号中间四位
     *
     * @param number 手机号码
     * @return String
     */
    public static String buildHiddenMobile(String number) {
        if (null == number || number.length() < 11) {
            return number;
        }
        return number.substring(0, 3) + "****" + number.substring(7, number.length());
    }

    /**
     * 获取字符串长度
     *
     * @param value 字符串
     * @return 字符串长度
     */
    public static int getCharLength(String value) {
        if (TextUtils.isEmpty(value)) {
            return 0;
        }
        int valueLength = 0;
        String chineseChar = "[\u0391-\uFFE5]";
        // 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
        for (int i = 0; i < value.length(); i++) {
            // 获取一个字符
            String temp = value.substring(i, i + 1);
            // 判断是否为中文字符
            if (temp.matches(chineseChar)) {
                // 中文字符长度为2
                valueLength += 2;
            } else {
                // 非中文字符长度为1
                valueLength += 1;
            }
        }
        return valueLength;
    }

    /**
     * 判断字符串是否包含中文
     *
     * @param value 字符串
     * @return boolean
     */
    public static boolean hasChineseChar(String value) {
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

    /**
     * 判断字符串是否为数字
     * @param str 字符串
     * @return 字符串是否为数字
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断字符串是否为浮点数
     * @param str 字符串
     * @return 字符串是否为浮点数
     */
    public static boolean isFloat(String str) {
        Pattern pattern = Pattern.compile("[0-9]*(\\.?)[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 获取double类型数据保留小数位后的字符串
     * @param value double值
     * @param newScale 保留的小数位
     * @return 保留小数位后的字符串
     */
    public static String getDouble(double value, int newScale) {
        return new BigDecimal(Double.toString(value)).setScale(newScale, BigDecimal.ROUND_DOWN).toPlainString();
    }

    /**
     * 将长整数转换为字节数组
     * @param num 长整数
     * @return 字节数组
     */
    public static byte[] longToBytes(long num) {
        byte[] bytes = new byte[8];
        int len = 8;
        for (byte b : bytes) {
            b = (byte) (num & 0xff);
            bytes[--len] = b;
            num >>= 8;
        }
        return bytes;
    }

    /**
     * 将字符串转换为整型
     * @param value 字符串
     * @param defaultValue 默认值
     * @return
     */
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

    /**
     * 获取拼接后的字符串
     * @param args 字符串参数
     * @return 拼接后的字符串
     */
    public static String combineString(String... args) {
        if (args == null || args.length <= 0) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (String arg : args) {
            builder.append(arg);
        }
        return builder.toString();
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
        StackTraceElement[] stackTraceElementArray = thread.getStackTrace();
        if (stackTraceElementArray == null) {
            return null;
        }
        for (StackTraceElement element : stackTraceElementArray) {
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

}
