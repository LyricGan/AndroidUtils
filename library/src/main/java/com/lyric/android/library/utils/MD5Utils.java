package com.lyric.android.library.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

/**
 * MD5工具类
 * 
 * @author lyric
 * @created 2014-1-11
 * 
 */
public class MD5Utils {
	/** 大写标记 */
	public static final int CASE_UPPER = 1;
	/** 小写标记 */
	public static final int CASE_LOWER = 2;

    private MD5Utils() {
    }
	
	/**
	 * 获取字符串MD5值
	 * @param str 字符串
	 * @param caseFlag 大小写标记
	 * @return 转换过的字符串
	 */
	public static String getMD5(String str, int caseFlag) {
		if (str == null) {
			return null;
		}
		String md5Str = null;
		// 16 进制字符数组
		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			// MD5 的计算结果是128 位的长整数，用字节表示就是16 个字节
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
			if (caseFlag == CASE_UPPER) {
				md5Str = new String(charArray).toUpperCase(locale);
			} else {
				md5Str = new String(charArray).toLowerCase(locale);
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return md5Str;
	}
}
