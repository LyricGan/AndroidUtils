package com.lyric.android.app.net.request;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.lyric.android.app.constants.AppConstants;
import com.lyric.android.app.net.Request;
import com.lyric.android.app.net.RequestError;
import com.lyric.android.app.net.RequestQueue;
import com.lyric.android.app.net.Response;
import com.lyric.android.app.net.core.StringRequest;
import com.lyric.android.app.net.core.Volley;
import com.lyric.android.library.utils.MD5Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 网络请求基类，实现常量接口 {@link AppConstants}
 * 
 * @author ganyu
 * @created 2015-4-20
 * 
 */
public abstract class BaseAPI implements AppConstants {
	protected RequestQueue mRequestQueue;
	
	/**
	 * 构造方法，初始化网络请求队列<br />
	 * 采用的是Google提供的Volley网络框架
	 * @param context 上下文对象
	 */
	public BaseAPI(Context context) {
		mRequestQueue = Volley.newRequestQueue(context);
	}
	
	/**
	 * 封装请求参数
	 * @param params 请求参数
	 * @param encode 编码格式
	 * @return
	 */
	public String buildParams(Map<String, String> params, String encode) {
		StringBuffer stringBuffer = new StringBuffer();
		if (TextUtils.isEmpty(encode)) {
			encode = Encode.UTF_8;
		}
		// 判断参数是否为空
		if (params != null && !params.isEmpty()) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				try {
					if (TextUtils.isEmpty(entry.getValue())) {
						stringBuffer.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
					} else {
						stringBuffer.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), encode)).append("&");
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			if (stringBuffer.length() > 0) {
				stringBuffer.deleteCharAt(stringBuffer.length() - 1);
			}
		}
		return stringBuffer.toString();
	}
	
	/**
	 * 封装请求地址
	 * @param url 请求地址
	 * @param params 请求参数
	 * @return
	 */
	public String buildUrl(String url, String params) {
		if (!TextUtils.isEmpty(params)) {
			// 判断地址是否带参数
			if (url.contains("?") || url.contains("&")) {
				url = url + "&" + params;
			} else {
				url = url + "?" + params;
			}
		}
		return url;
	}
	
	/**
	 * 创建默认参数
	 * @return
	 */
	public Map<String, String> buildDefaultParams() {
		Map<String, String> requestParams = new HashMap<String, String>();
		long timestamp = System.currentTimeMillis();
		String md5String = MD5Utils.getMD5(String.valueOf(timestamp + ""), MD5Utils.CASE_LOWER) + timestamp;
		requestParams.put("access", md5String);
		return requestParams;
	}
	
	/**
	 * 发起网络请求
	 * @param url 请求地址
	 * @param method 请求方式
	 * @param params 请求参数
	 * @param flag 请求标识
	 * @param handler
	 */
	public void request(String url, int method, Map<String, String> params, int flag, Handler handler) {
		this.request(url, method, params, flag, handler, true);
	}
	
	/**
	 * 发起网络请求
	 * @param url 请求地址
	 * @param method 请求方式
	 * @param params 请求参数
	 * @param flag 请求标识
	 * @param handler
	 * @param shouldCache 是否缓存
	 */
	public void request(String url, int method, final Map<String, String> params, final int flag, final Handler handler, boolean shouldCache) {
		StringRequest request = null;
		if (method == Request.Method.POST) {
			request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

				@Override
				public void onResponse(String response) {
					Message msg = handler.obtainMessage(flag, response);
					msg.arg1 = ResponseCode.SUCCESS;
					handler.sendMessage(msg);
				}
			}, new Response.ErrorListener() {

				@Override
				public void onErrorResponse(RequestError error) {
					Message msg = handler.obtainMessage(flag, error.getMessage());
					msg.arg1 = ResponseCode.ERROR;
					handler.sendMessage(msg);
				}
			}) {
				@Override
				protected Map<String, String> getParams() throws RequestError {
					return params;
				}
			};
		} else {
			url = buildUrl(url, buildParams(params, Encode.UTF_8));
			request = new StringRequest(url, new Response.Listener<String>() {
				
				@Override
				public void onResponse(String response) {
					Message msg = handler.obtainMessage(flag, response);
					msg.arg1 = ResponseCode.SUCCESS;
					handler.sendMessage(msg);
				}
			}, new Response.ErrorListener() {
				
				@Override
				public void onErrorResponse(RequestError error) {
					Message msg = handler.obtainMessage(flag, error.getMessage());
					msg.arg1 = ResponseCode.ERROR;
					handler.sendMessage(msg);
				}
			});
		}
		request.setTag(flag);
		request.setShouldCache(shouldCache);
		mRequestQueue.add(request);
	}
	
	/**
	 * 取消请求
	 * <br /> this method exists issue.
	 * @param tag
	 */
	public void cancel(Object tag) {
		if (tag == null) {
			return;
		}
		mRequestQueue.cancelAll(tag);
	}
	
}
