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

    public static ResponseEntity doGet(String url, Map<String, String> params) {
        return doGet(url, params, HttpConstants.UTF_8);
    }

    public static ResponseEntity doGet(String url, Map<String, String> params, String encode) {
        ResponseEntity responseEntity = new ResponseEntity();
        if (TextUtils.isEmpty(url)) {
            LogUtils.e(HttpConstants.HTTP_TAG, "Request url can not be null.");
            responseEntity.responseCode = HttpConstants.URL_NULL;
            return responseEntity;
        }
//        url = ParamsUtils.buildSpecialGetUrl(url, params, encode);
        url = ParamsUtils.buildGetUrl(url, params, encode);
        responseEntity.url = url;
        HttpURLConnection urlConnection = null;
        try {
            URL requestUrl = new URL(url);
            urlConnection = (HttpURLConnection) requestUrl.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.setConnectTimeout(HttpConstants.CONNECTION_TIMEOUT);
            urlConnection.setReadTimeout(HttpConstants.SOCKET_TIMEOUT);

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

    public static ResponseEntity doPost(String url, Map<String, String> params) {
        return doPost(url, params, HttpConstants.UTF_8);
    }

    public static ResponseEntity doPost(String url, Map<String, String> params, String encode) {
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
            urlConnection = (HttpURLConnection) requestUrl.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setConnectTimeout(HttpConstants.CONNECTION_TIMEOUT);
            urlConnection.setReadTimeout(HttpConstants.SOCKET_TIMEOUT);

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

    private static String process(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
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
