package com.lrc.baseand.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.lrc.baseand.constants.AppConstants;

import android.text.TextUtils;

/**
 * 网络请求工具类
 * 
 * @author ganyu
 * @created 2013-12-10
 * 
 */
public class HttpUtils implements AppConstants {
	private static final String TAG = HttpUtils.class.getSimpleName();
	/** 请求超时时间 */
	public static final int SOCKET_TIMEOUT = 30 * 1000;
	/** 连接超时时间 */
	public static final int CONNECTION_TIMEOUT = 30 * 1000;
	
	/**
	 * 封装请求参数
	 * @param params 请求参数
	 * @param encode 编码格式
	 * @return
	 */
	public static String getParams(Map<String, String> params, String encode) {
		StringBuffer stringBuffer = new StringBuffer();
		// 判断参数是否为空
		if (params != null && !params.isEmpty()) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				try {
					if (entry.getValue() == null || "".equals(entry.getValue())) {
						stringBuffer.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
					} else {
						stringBuffer.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), encode)).append("&");
					}
				} catch (UnsupportedEncodingException e) {
					LogUtils.e(TAG, "UnsupportedEncodingException:" + e.getMessage());
					stringBuffer = null;
				}
			}
			if (stringBuffer != null && stringBuffer.length() > 0) {
				stringBuffer.deleteCharAt(stringBuffer.length() - 1);
			}
		}
		return stringBuffer.toString();
	}
	
	/**
	 * Get请求
	 * @param url 请求地址
	 * @param params 请求参数
	 * @param encode 编码格式
	 * @return
	 */
	public static String requestGet(String url, Map<String, String> params, String encode) {
		String response = "";
		// 判断地址是否为空
		if (TextUtils.isEmpty(url)) {
			return response;
		}
		// 判断编码是否为空
		if (TextUtils.isEmpty(encode)) {
			encode = HTTP.UTF_8;
		}
		// 获取请求参数
		String requestParams = getParams(params, encode);
		// 判断传递参数是否为空
		if (requestParams != null && !"".equals(requestParams)) {
			// 判断地址是否带参数
			if (url.contains("?") || url.contains("&")) {
				url = url + "&" + requestParams;
			} else {
				url = url + "?" + requestParams;
			}
		}
		LogUtils.d(TAG, "requestGet url:" + url);
		// 设置请求参数（请求超时、连接超时）
		HttpParams httpParams = new BasicHttpParams();
		httpParams.setParameter(HttpConnectionParams.SO_TIMEOUT, SOCKET_TIMEOUT);
		httpParams.setParameter(HttpConnectionParams.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT);
		
		HttpClient httpClient = new DefaultHttpClient(httpParams);
		HttpGet httpGet = new HttpGet(url);
		HttpResponse httpResponse = null;
		try {
			httpResponse = httpClient.execute(httpGet);
			int responseCode = httpResponse.getStatusLine().getStatusCode();
			// 判断请求是否成功
			if (responseCode == HttpStatus.SC_OK) {
				response = EntityUtils.toString(httpResponse.getEntity(), encode);
			}
		} catch (ClientProtocolException e) {
			LogUtils.e(TAG,"ClientProtocolException:" + e.getMessage());
		} catch (IllegalStateException e) {
			LogUtils.e(TAG,"IllegalStateException:" + e.getMessage());
		} catch (IOException e) {
			LogUtils.e(TAG,"IOException:" + e.getMessage());
		}
		return response;
	}
	
	/**
	 * Post请求
	 * @param url 请求地址
	 * @param params 请求参数
	 * @param encode 编码格式
	 * @return
	 */
	public static String requestPost(String url, Map<String, String> params, String encode) {
		String response = "";
		// 判断地址是否为空
		if (TextUtils.isEmpty(url)) {
			return response;
		}
		// 判断编码是否为空
		if (TextUtils.isEmpty(encode)) {
			encode = HTTP.UTF_8;
		}
		// 获取请求参数
		List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
		if (params != null && !params.isEmpty()) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				nameValuePairList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
		}
		try {
			// 设置请求参数
			HttpEntity httpEntity = new UrlEncodedFormEntity(nameValuePairList, encode);
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(httpEntity);
			
			HttpParams httpParams = new BasicHttpParams();
			httpParams.setParameter(HttpConnectionParams.SO_TIMEOUT, 10 * 1000);
			httpParams.setParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 10 * 1000);
			
			HttpClient httpClient = new DefaultHttpClient(httpParams);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			int responseCode = httpResponse.getStatusLine().getStatusCode();
			
			// 判断请求是否成功
			if (responseCode == HttpStatus.SC_OK) {
				response = EntityUtils.toString(httpResponse.getEntity(), encode);
			}
		} catch (UnsupportedEncodingException e) {
			LogUtils.e(TAG,"UnsupportedEncodingException:" + e.getMessage());
		} catch (ClientProtocolException e) {
			LogUtils.e(TAG,"ClientProtocolException:" + e.getMessage());
		} catch (IOException e) {
			LogUtils.e(TAG,"IOException:" + e.getMessage());
		}
		return response;
	}
	
}
