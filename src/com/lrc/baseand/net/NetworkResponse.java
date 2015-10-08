package com.lrc.baseand.net;

import org.apache.http.HttpStatus;

import com.lrc.baseand.net.core.Network;

import java.util.Collections;
import java.util.Map;

/**
 * 网络请求响应数据和响应头，从 {@link Network#performRequest(Request)} 中获取
 */
public class NetworkResponse {
	/** HTTP响应状态码 */
    public final int statusCode;
    /** 响应数据字节数组 */
    public final byte[] data;
    /** 响应头 */
    public final Map<String, String> headers;
    /** 服务器返回 304 标识，即返回结果未发生变化 */
    public final boolean notModified;
    /** 网络往返时间，毫秒 */
    public final long networkTimeMs;
    
    /**
     * 构造方法
     * @param statusCode 网络响应状态码
     * @param data 响应数据内容
     * @param headers 响应数据头
     * @param notModified 服务器是否返回304状态码标识，即返回结果是否发生变化
     * @param networkTimeMs 网络往返时间，毫秒
     */
    public NetworkResponse(int statusCode, byte[] data, Map<String, String> headers, boolean notModified, long networkTimeMs) {
        this.statusCode = statusCode;
        this.data = data;
        this.headers = headers;
        this.notModified = notModified;
        this.networkTimeMs = networkTimeMs;
    }

    /**
     * 构造方法
     * @param statusCode 网络响应状态码
     * @param data 响应数据内容
     * @param headers 响应数据头
     * @param notModified 服务器返回304标识，即返回结果未发生变化
     */
    public NetworkResponse(int statusCode, byte[] data, Map<String, String> headers,  boolean notModified) {
        this(statusCode, data, headers, notModified, 0);
    }
    
    /**
     * 构造方法
     * @param data 响应数据内容
     * @param headers 响应数据头
     */
    public NetworkResponse(byte[] data, Map<String, String> headers) {
        this(HttpStatus.SC_OK, data, headers, false, 0);
    }
    
    /**
     * 构造方法
     * @param data 响应数据内容
     */
    public NetworkResponse(byte[] data) {
        this(HttpStatus.SC_OK, data, Collections.<String, String>emptyMap(), false, 0);
    }

}

