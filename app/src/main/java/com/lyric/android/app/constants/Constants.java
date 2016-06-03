package com.lyric.android.app.constants;
/**
 * 应用常量接口
 * 
 * @author ganyu
 * @created 2015-4-20
 * 
 */
public interface Constants {
    boolean DEBUG = true;
    boolean LEAK_DEBUG = false;

	/**
	 * 编码格式
	 */
	final class Encode {
		/** UTF-8编码 */
		public static final String UTF_8 = "UTF-8";
		/** GBK编码 */
		public static final String GBK = "GBK";
	}
	
	/**
	 * 响应码
	 */
	final class ResponseCode {
		/** 响应成功 */
		public static final int SUCCESS = 1;
		/** 响应失败 */
		public static final int ERROR = -1;
	}
}
