package com.lyric.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式工具类
 * <br/>常用的元字符<br/>
 * <li>. 匹配换行符以外的任意字符</li>
 * <li>\w 匹配字母或数字或下划线或汉字</li>
 * <li>\s 匹配任意的空白符</li>
 * <li>\d 匹配数字</li>
 * <li>\b 匹配单词的开始或结束</li>
 * <li>^ 匹配字符串的开始</li>
 * <li>$ 匹配字符串的结束</li>
 *
 * <br/>常用的限定符<br/>
 * <li>* 重复零次或更多次</li>
 * <li>+ 重复一次或更多次</li>
 * <li>? 重复零次或一次</li>
 * <li>{n} 重复n次</li>
 * <li>{n,} 重复n次或更多次</li>
 * <li>{n,m} 重复n到m次</li>
 *
 * <br/>常用的反义代码<br/>
 * <li>\W 匹配任意不是字母，数字，下划线，汉字的字符</li>
 * <li>\S 匹配任意不是空白符的字符</li>
 * <li>\D 匹配任意非数字的字符</li>
 * <li>\B 匹配不是单词开头或结束的位置</li>
 * <li>[^x] 匹配除了x以外的任意字符</li>
 * <li>[^aeiou] 匹配除了aeiou这几个字母以外的任意字符</li>
 *
 * <br/>常用的分组语法<br/>
 * <li>(exp) 匹配exp，并捕获文本到自动命名的组里</li>
 * <li>(?<name>exp) 匹配exp，并捕获文本到名称为name的组里，也可以写成(?'name'exp)</li>
 * <li>(?:exp) 匹配exp，不捕获匹配的文本，也不给此分组分配组号</li>
 * <li>(?=exp) 匹配exp前面的位置</li>
 * <li>(?<=exp) 匹配exp后面的位置</li>
 * <li>(?!exp) 匹配后面跟的不是exp的位置</li>
 * <li>(?<!exp) 匹配前面不是exp的位置</li>
 *
 * @author lyricgan
 * @date 2017/12/21 14:16
 */
public class RegexUtils {
    /** 正则：手机号（简单） */
    public static final String REGEX_MOBILE_SIMPLE = "^[1]\\d{10}$";
    /**
     * 正则：手机号（精确）
     * <p>移动：134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188</p>
     * <p>联通：130、131、132、145、155、156、175、176、185、186</p>
     * <p>电信：133、153、173、177、180、181、189</p>
     * <p>全球星：1349</p>
     * <p>虚拟运营商：170</p>
     */
    public static final String REGEX_MOBILE_EXACT = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|(147))\\d{8}$";
    /**
     * 正则：电话号码
     */
    public static final String REGEX_TEL = "^0\\d{2,3}[- ]?\\d{7,8}";
    /**
     * 正则：身份证号码15位
     */
    public static final String REGEX_IDCARD15 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
    /**
     * 正则：身份证号码18位
     */
    public static final String REGEX_IDCARD18 = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9Xx])$";
    /**
     * 正则：邮箱
     */
    public static final String REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    /**
     * 正则：URL
     */
    public static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?";
    /**
     * 正则：汉字
     */
    public static final String REGEX_CHZ = "^[\\u4e00-\\u9fa5]+$";
    /**
     * 正则：用户名，取值范围为a-z,A-Z,0-9,"_",汉字，不能以"_"结尾,用户名必须是6-20位
     */
    public static final String REGEX_USERNAME = "^[\\w\\u4e00-\\u9fa5]{6,20}(?<!_)$";
    /**
     * 正则：yyyy-MM-dd格式的日期校验，已考虑平闰年
     */
    public static final String REGEX_DATE = "^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$";
    /**
     * 正则：IP地址
     */
    public static final String REGEX_IP = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";

    private RegexUtils() {
    }

    public static boolean isMatch(String regex, CharSequence value) {
        return isMatch(regex, 0, value);
    }

    public static boolean isMatch(String regex, int flags, CharSequence value) {
        if (TextUtils.isEmpty(regex) || TextUtils.isEmpty(value)) {
            return false;
        }
        Pattern pattern = Pattern.compile(regex, flags);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    public static boolean isIdCard(CharSequence value) {
        return isMatch(REGEX_IDCARD15, value) || isMatch(REGEX_IDCARD18, value);
    }
}
