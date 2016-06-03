package com.lyric.android.app.volley.net;

/**
 * 响应解析交付封装类，泛型
 *
 * @param <T> 响应数据解析类型
 */
public class Response<T> {
	/** 响应结果 */
    public final T result;
    /** 缓存数据实体 */
    public final Cache.Entry cacheEntry;
    /** 错误信息 */
    public final RequestError error;
    /** 响应结果是否处于将要过期失效的标识 */
    public boolean intermediate = false;

    /** 响应成功回调接口 */
    public interface Listener<T> {
    	/**
    	 * 响应回调方法
    	 * @param response 响应数据
    	 */
        void onResponse(T response);
    }

    /** 响应错误回调接口 */
    public interface ErrorListener {
    	/**
    	 * 响应错误回调方法
    	 * @param error 错误信息，包括错误码和可选的用户可读信息
    	 */
        void onErrorResponse(RequestError error);
    }
    
    /**
     * 构造方法
     * @param result
     * @param cacheEntry
     */
    private Response(T result, Cache.Entry cacheEntry) {
        this.result = result;
        this.cacheEntry = cacheEntry;
        this.error = null;
    }

    /**
     * 构造方法
     * @param error
     */
    private Response(RequestError error) {
        this.result = null;
        this.cacheEntry = null;
        this.error = error;
    }

    /**
     * 返回已解析的响应信息
     * @param result
     * @param cacheEntry
     * @return
     */
    public static <T> Response<T> success(T result, Cache.Entry cacheEntry) {
        return new Response<T>(result, cacheEntry);
    }

    /**
     * 返回错误的响应信息
     * @param error 错误信息，包括错误码和可选的用户可读信息
     * @return
     */
    public static <T> Response<T> error(RequestError error) {
        return new Response<T>(error);
    }

    /**
     * 判断响应是否成功
     * @return
     */
    public boolean isSuccess() {
        return error == null;
    }
}
