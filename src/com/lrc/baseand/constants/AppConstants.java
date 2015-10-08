package com.lrc.baseand.constants;
/**
 * 应用常量接口
 * 
 * @author ganyu
 * @created 2015-4-20
 * 
 */
public interface AppConstants {
	/**
	 * 编码格式
	 */
	public final class Encode {
		/** UTF-8编码 */
		public static final String UTF_8 = "UTF-8";
		/** GBK编码 */
		public static final String GBK = "GBK";
	}
	
	/**
	 * 响应码
	 */
	public final class ResponseCode {
		/** 响应成功 */
		public static final int SUCCESS = 1;
		/** 响应失败 */
		public static final int ERROR = -1;
	}
	
	/**
	 * 网络请求接口地址
	 */
	public final class Url {
		/** 基础地址 */
		public static final String BASE_URL = "";
	}
	
	/**
	 * 请求标识
	 */
	public final class Flag {
		
	}
	
}
