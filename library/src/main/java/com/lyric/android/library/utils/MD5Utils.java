package com.lyric.android.library.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

/**
 * MD5工具类
 * 
 * @author lyric
 * @created 2014-1-11
 */
public class Md5Utils {
	/** 大写标记 */
	public static final int CASE_UPPER = 1;
	/** 小写标记 */
	public static final int CASE_LOWER = 2;

    private Md5Utils() {
    }

    public static String getUpper(String value) {
        return getString(value, CASE_UPPER);
    }

    public static String getLower(String value) {
        return getString(value, CASE_LOWER);
    }
	
	/**
	 * 获取字符串MD5值
	 * @param value 字符串
	 * @param flag 大小写标记
	 * @return 转换过的字符串
	 */
	public static String getString(String value, int flag) {
		if (value == null) {
			return null;
		}
		String result = null;
		// 16进制字符数组
		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(value.getBytes());
			// MD5的计算结果是128位的长整数，用字节表示就是16个字节
			byte[] tmp = md.digest();
			char[] charArray = new char[16 * 2];
			// 表示转换结果中对应的字符位置
			int pos = 0;
			for (int i = 0; i < 16; i++) {
				byte ch = tmp[i];
				charArray[pos++] = hexDigits[ch >>> 4 & 0xf];
				charArray[pos++] = hexDigits[ch & 0xf];
			}
			Locale locale = Locale.getDefault();
			if (flag == CASE_UPPER) {
				result = new String(charArray).toUpperCase(locale);
			} else {
				result = new String(charArray).toLowerCase(locale);
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return result;
	}
}
