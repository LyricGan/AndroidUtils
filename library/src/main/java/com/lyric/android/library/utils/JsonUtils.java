package com.lyric.android.library.utils;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * JSON解析帮助类
 * 
 * @author ganyu
 *
 */
public class JsonUtils {

	private JsonUtils() {
	}
	
	/**
	 * 判断字符串是否为JSONObject
	 * @param json 字符串
	 * @return
	 */
	public static boolean isJsonObject(String json) {
		try {
			JSONObject jsonObject = new JSONObject(json);
			if (jsonObject != null) {
				return true;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 判断字符串是否为JSONArray
	 * @param json 字符串
	 * @return
	 */
	public static boolean isJsonArray(String json) {
		try {
			JSONArray jsonArray = new JSONArray(json);
			if (jsonArray != null) {
				return true;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 判断字符串是否为JSONArray，数组非空
	 * @param json
	 * @return
	 */
	public static boolean isJsonArrayOrEmpty(String json) {
		try {
			JSONArray jsonArray = new JSONArray(json);
			if (jsonArray != null && jsonArray.length() > 0) {
				return true;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 解析JSON字符串中指定的值
	 * @param jsonObject JSON对象
	 * @param key 指定的值
	 * @param defaultValue 默认值，字符串
	 * @see {@link JSONObject#optString(String, String)}
	 * @return
	 */
	public static String getString(JSONObject jsonObject, String key, String defaultValue) {
		if (jsonObject == null || TextUtils.isEmpty(key)) {
			return defaultValue;
		}
		return jsonObject.optString(key, defaultValue);
	}
	
	/**
	 * 解析JSON字符串中指定的值
	 * @param jsonObject JSON对象
	 * @param key 指定的值
	 * @param defaultValue 默认值，整型值
	 * @see {@link JSONObject#optInt(String, int)}
	 * @return
	 */
	public static int getInt(JSONObject jsonObject, String key, int defaultValue) {
		if (jsonObject == null || TextUtils.isEmpty(key)) {
			return defaultValue;
		}
		return jsonObject.optInt(key, defaultValue);
	}
	
	/**
	 * 解析JSON字符串中指定的值
	 * @param jsonObject JSON对象
	 * @param key 指定的值
	 * @param defaultValue 默认值，长整型值
	 * @see {@link JSONObject#optLong(String, long)}
	 * @return
	 */
	public static long getLong(JSONObject jsonObject, String key, long defaultValue) {
		if (jsonObject == null || TextUtils.isEmpty(key)) {
			return defaultValue;
		}
		return jsonObject.optLong(key, defaultValue);
	}
	
	/**
	 * 解析JSON字符串中指定的值
	 * @param jsonObject JSON对象
	 * @param key 指定的值
	 * @param defaultValue 默认值，Boolean值
	 * @see {@link JSONObject#optBoolean(String, boolean)}
	 * @return
	 */
	public static boolean getBoolean(JSONObject jsonObject, String key, boolean defaultValue) {
		if (jsonObject == null || TextUtils.isEmpty(key)) {
			return defaultValue;
		}
		return jsonObject.optBoolean(key, defaultValue);
	}
	
	/**
	 * 解析JSON字符串中指定的值
	 * @param jsonObject JSON对象
	 * @param key 指定的值
	 * @param defaultValue 默认值，Double值
	 * @see {@link JSONObject#optDouble(String, double)}
	 * @return
	 */
	public static double getDouble(JSONObject jsonObject, String key, double defaultValue) {
		if (jsonObject == null || TextUtils.isEmpty(key)) {
			return defaultValue;
		}
		return jsonObject.optDouble(key, defaultValue);
	}
	
	/**
	 * 解析JSON字符串中指定的值
	 * @param jsonObject JSON对象
	 * @param key 指定的值
	 * @see {@link JSONObject#optJSONObject(String)}
	 * @return JSON对象
	 */
	public static JSONObject getJsonObject(JSONObject jsonObject, String key) {
		if (jsonObject == null || TextUtils.isEmpty(key)) {
			return null;
		}
		return jsonObject.optJSONObject(key);
	}
	
	/**
	 * 解析JSON字符串中指定的值
	 * @param jsonObject JSON对象
	 * @param key 指定的值
	 * @see {@link JSONObject#optJSONArray(String)}
	 * @return JSON数组
	 */
	public static JSONArray getJsonArray(JSONObject jsonObject, String key) {
		if (jsonObject == null || TextUtils.isEmpty(key)) {
			return null;
		}
		return jsonObject.optJSONArray(key);
	}
	
}
