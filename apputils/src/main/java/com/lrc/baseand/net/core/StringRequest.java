package com.lrc.baseand.net.core;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;
import org.apache.http.protocol.HTTP;

import com.lrc.baseand.net.Cache;
import com.lrc.baseand.net.NetworkResponse;
import com.lrc.baseand.net.Request;
import com.lrc.baseand.net.Request.Method;
import com.lrc.baseand.net.Response;
import com.lrc.baseand.net.Response.ErrorListener;
import com.lrc.baseand.net.Response.Listener;

/**
 * 网络请求封装类，继承 {@link Request}
 */
public class StringRequest extends Request<String> {
    private final Listener<String> mListener;

    /**
     * 构造方法，创建网络请求
     * @param method 请求方式，使用 {@link Method} 
     * @param url 请求地址
     * @param listener 请求响应监听事件
     * @param errorListener 错误响应监听事件，传入null则不处理
     */
    public StringRequest(int method, String url, Listener<String> listener, ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
    }

    /**
     * 构造方法，使用Get方式创建网络请求
     * @param url 请求地址
     * @param listener 请求响应监听事件
     * @param errorListener 错误响应监听事件，传入null则不处理
     */
    public StringRequest(String url, Listener<String> listener, ErrorListener errorListener) {
        this(Method.GET, url, listener, errorListener);
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }
    
    /**
     * 网络请求头解析类
     */
    static class HttpHeaderParser {
        /**
         * Extracts a {@link Cache.Entry} from a {@link NetworkResponse}.
         * @param response The network response to parse headers from
         * @return a cache entry for the given response, or null if the response is not cacheable.
         */
        public static Cache.Entry parseCacheHeaders(NetworkResponse response) {
            long now = System.currentTimeMillis();
            Map<String, String> headers = response.headers;
            long serverDate = 0;
            long lastModified = 0;
            long serverExpires = 0;
            long softExpire = 0;
            long finalExpire = 0;
            long maxAge = 0;
            long staleWhileRevalidate = 0;
            boolean hasCacheControl = false;
            boolean mustRevalidate = false;
            String serverEtag = null;
            String headerValue;
            headerValue = headers.get("Date");
            if (headerValue != null) {
                serverDate = parseDateAsEpoch(headerValue);
            }
            headerValue = headers.get("Cache-Control");
            if (headerValue != null) {
                hasCacheControl = true;
                String[] tokens = headerValue.split(",");
                for (int i = 0; i < tokens.length; i++) {
                    String token = tokens[i].trim();
                    if (token.equals("no-cache") || token.equals("no-store")) {
                        return null;
                    } else if (token.startsWith("max-age=")) {
                        try {
                            maxAge = Long.parseLong(token.substring(8));
                        } catch (Exception e) {
                        }
                    } else if (token.startsWith("stale-while-revalidate=")) {
                        try {
                            staleWhileRevalidate = Long.parseLong(token.substring(23));
                        } catch (Exception e) {
                        }
                    } else if (token.equals("must-revalidate") || token.equals("proxy-revalidate")) {
                        mustRevalidate = true;
                    }
                }
            }
            headerValue = headers.get("Expires");
            if (headerValue != null) {
                serverExpires = parseDateAsEpoch(headerValue);
            }
            headerValue = headers.get("Last-Modified");
            if (headerValue != null) {
                lastModified = parseDateAsEpoch(headerValue);
            }
            serverEtag = headers.get("ETag");
            // Cache-Control takes precedence over an Expires header, even if both exist and Expires
            // is more restrictive.
            if (hasCacheControl) {
                softExpire = now + maxAge * 1000;
                finalExpire = mustRevalidate ? softExpire : softExpire + staleWhileRevalidate * 1000;
            } else if (serverDate > 0 && serverExpires >= serverDate) {
                // Default semantic for Expire header in HTTP specification is softExpire.
                softExpire = now + (serverExpires - serverDate);
                finalExpire = softExpire;
            }
            Cache.Entry entry = new Cache.Entry();
            entry.data = response.data;
            entry.etag = serverEtag;
            entry.softTtl = softExpire;
            entry.ttl = finalExpire;
            entry.serverDate = serverDate;
            entry.lastModified = lastModified;
            entry.responseHeaders = headers;

            return entry;
        }

        /**
         * Parse date in RFC1123 format, and return its value as epoch
         */
        public static long parseDateAsEpoch(String dateStr) {
            try {
                // Parse date in RFC1123 format if this header contains one
                return DateUtils.parseDate(dateStr).getTime();
            } catch (DateParseException e) {
                // Date in invalid format, fallback to 0
                return 0;
            }
        }

        /**
         * Retrieve a charset from headers
         * @param headers An {@link Map} of headers
         * @param defaultCharset Charset to return if none can be found
         * @return Returns the charset specified in the Content-Type of this header,
         * or the defaultCharset if none can be found.
         */
        public static String parseCharset(Map<String, String> headers, String defaultCharset) {
            String contentType = headers.get(HTTP.CONTENT_TYPE);
            if (contentType != null) {
                String[] params = contentType.split(";");
                for (int i = 1; i < params.length; i++) {
                    String[] pair = params[i].trim().split("=");
                    if (pair.length == 2) {
                        if (pair[0].equals("charset")) {
                            return pair[1];
                        }
                    }
                }
            }
            return defaultCharset;
        }

        /**
         * Returns the charset specified in the Content-Type of this header,
         * or the HTTP default (ISO-8859-1) if none can be found.
         */
        public static String parseCharset(Map<String, String> headers) {
            return parseCharset(headers, HTTP.DEFAULT_CONTENT_CHARSET);
        }
    }
    
}