package com.lyric.android.app.network;

import android.text.TextUtils;

import com.lyric.android.library.utils.LogUtils;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * @author lyric
 * @description
 * @time 2016/6/22 13:36
 */
public class HttpUtils {

    private HttpUtils() {
    }

    public static ResponseEntity get(String url, Map<String, String> params, boolean isRefresh) {
        return get(url, params, HttpConstants.UTF_8, isRefresh);
    }

    public static ResponseEntity get(String url, Map<String, String> params, String encode, boolean isRefresh) {
        ResponseEntity responseEntity = new ResponseEntity();
        if (TextUtils.isEmpty(url)) {
            LogUtils.e(HttpConstants.HTTP_TAG, "Request url can not be null.");
            responseEntity.responseCode = HttpConstants.URL_NULL;
            return responseEntity;
        }
        url = ParamsUtils.buildGetUrl(url, params, encode);
        responseEntity.url = url;
        HttpURLConnection urlConnection = null;
        try {
            URL requestUrl = new URL(url);
            urlConnection = openConnection(requestUrl);
            urlConnection.setRequestMethod("GET");

            String response;
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                response = process(urlConnection.getInputStream());
            } else {
                response = "Request failed.";
            }
            responseEntity.responseCode = urlConnection.getResponseCode();
            responseEntity.response = response;
        } catch (IOException e) {
            responseEntity.responseCode = HttpConstants.EXCEPTION;
            responseEntity.response = e.getMessage();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return responseEntity;
    }

    public static ResponseEntity post(String url, Map<String, String> params, boolean isRefresh) {
        return post(url, params, HttpConstants.UTF_8, isRefresh);
    }

    public static ResponseEntity post(String url, Map<String, String> params, String encode, boolean isRefresh) {
        ResponseEntity responseEntity = new ResponseEntity();
        if (TextUtils.isEmpty(url)) {
            LogUtils.e(HttpConstants.HTTP_TAG, "Request url can not be null.");
            responseEntity.responseCode = HttpConstants.URL_NULL;
            return responseEntity;
        }
        String requestParams = ParamsUtils.encodeParams(params, encode);
        responseEntity.url = url;
        responseEntity.params = requestParams;
        HttpURLConnection urlConnection = null;
        Writer writer = null;
        try {
            URL requestUrl = new URL(url);
            urlConnection = openConnection(requestUrl);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setUseCaches(false);

            OutputStream outputStream = urlConnection.getOutputStream();
            writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            writer.write(requestParams);
            writer.flush();

            String response;
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                response = process(urlConnection.getInputStream());
            } else {
                response = "Request failed.";
            }
            responseEntity.responseCode = urlConnection.getResponseCode();
            responseEntity.response = response;
        } catch (IOException e) {
            responseEntity.responseCode = HttpConstants.EXCEPTION;
            responseEntity.response = e.getMessage();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseEntity;
    }

    private static HttpURLConnection createConnection(URL url) throws IOException {
        return (HttpURLConnection) url.openConnection();
    }

    private static HttpURLConnection openConnection(URL requestUrl) throws IOException {
        HttpURLConnection urlConnection = createConnection(requestUrl);
        urlConnection.setDoOutput(true);
        urlConnection.setConnectTimeout(HttpConstants.CONNECTION_TIMEOUT);
        urlConnection.setReadTimeout(HttpConstants.SOCKET_TIMEOUT);
        return urlConnection;
    }

    private static String process(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 2];// 2k
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        inputStream.close();
        String response = outputStream.toString();
        outputStream.close();
        return response;
    }
}
