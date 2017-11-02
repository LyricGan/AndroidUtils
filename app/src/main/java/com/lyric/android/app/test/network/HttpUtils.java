package com.lyric.android.app.test.network;

import android.text.TextUtils;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
            responseEntity.setResponseCode(HttpConstants.URL_NULL);
            return responseEntity;
        }
        url = ParamsUtils.buildGetUrl(url, params, encode);
        responseEntity.setUrl(url);
        HttpURLConnection urlConnection = null;
        try {
            URL requestUrl = new URL(url);
            urlConnection = openConnection(requestUrl);
            urlConnection.setRequestMethod("GET");

            String response;
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                response = readResponse(urlConnection.getInputStream());
            } else {
                response = "Request failed.";
            }
            responseEntity.setResponseCode(urlConnection.getResponseCode());
            responseEntity.setResponse(response);
        } catch (IOException e) {
            responseEntity.setResponseCode(HttpConstants.EXCEPTION);
            responseEntity.setResponse(e.getMessage());
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
            responseEntity.setResponseCode(HttpConstants.URL_NULL);
            return responseEntity;
        }
        String requestParams = ParamsUtils.encodeParams(params, encode);
        responseEntity.setUrl(url);
        responseEntity.setParams(requestParams);
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
                response = readResponse(urlConnection.getInputStream());
            } else {
                response = "Request failed.";
            }
            responseEntity.setResponseCode(urlConnection.getResponseCode());
            responseEntity.setResponse(response);
        } catch (IOException e) {
            responseEntity.setResponseCode(HttpConstants.EXCEPTION);
            responseEntity.setResponse(e.getMessage());
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

    public static ResponseEntity upload(String url, Map<String, String> params, Map<String, File> fileParams) {
        String BOUNDARY = java.util.UUID.randomUUID().toString();
        String CHARSET = "UTF-8";
        String PREFIX = "--";
        String LINE_END = "\r\n";
        ResponseEntity responseEntity = new ResponseEntity();
        responseEntity.setUrl(url);
        responseEntity.setParams(ParamsUtils.encodeParams(params, HttpConstants.UTF_8));
        try {
            URL requestUrl = new URL(url);
            HttpURLConnection connection = createConnection(requestUrl);
            connection.setConnectTimeout(HttpConstants.CONNECTION_TIMEOUT * 2);
            connection.setReadTimeout(HttpConstants.CONNECTION_TIMEOUT * 2);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("connection", "keep-alive");
            connection.setRequestProperty("Charset", CHARSET);
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + BOUNDARY);
            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            // 首先组拼文本类型的参数
            StringBuilder textParamsBuilder = new StringBuilder();
            if (params != null) {
                for (Map.Entry<String, String> textEntry : params.entrySet()) {
                    textParamsBuilder.append(PREFIX);
                    textParamsBuilder.append(BOUNDARY);
                    textParamsBuilder.append(LINE_END);
                    textParamsBuilder.append("Content-Disposition: form-data; name=\"").append(textEntry.getKey()).append("\"");
                    textParamsBuilder.append(LINE_END);
                    textParamsBuilder.append("Content-Type: text/plain; charset=").append(CHARSET);
                    textParamsBuilder.append(LINE_END);
                    textParamsBuilder.append("Content-Transfer-Encoding: 8bit");
                    textParamsBuilder.append(LINE_END);
                    textParamsBuilder.append(LINE_END);
                    textParamsBuilder.append(textEntry.getValue());
                    textParamsBuilder.append(LINE_END);
                }
                dataOutputStream.write(textParamsBuilder.toString().getBytes());
            }
            InputStream inputStream;
            // 发送文件数据
            if (fileParams != null) {
                StringBuilder fileParamsBuilder;
                for (Map.Entry<String, File> file : fileParams.entrySet()) {
                    fileParamsBuilder = new StringBuilder();
                    fileParamsBuilder.append(PREFIX);
                    fileParamsBuilder.append(BOUNDARY);
                    fileParamsBuilder.append(LINE_END);
                    fileParamsBuilder.append("Content-Disposition: form-data; name=\"").append(file.getKey());
                    fileParamsBuilder.append("\"; filename=\"").append(file.getValue().getName()).append("\"");
                    fileParamsBuilder.append(LINE_END);
                    fileParamsBuilder.append("Content-Type: application/octet-stream; charset=").append(CHARSET);
                    fileParamsBuilder.append(LINE_END);
                    fileParamsBuilder.append(LINE_END);

                    dataOutputStream.write(fileParamsBuilder.toString().getBytes());
                    InputStream is = new FileInputStream(file.getValue());
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = is.read(buffer)) != -1) {
                        dataOutputStream.write(buffer, 0, len);
                    }
                    is.close();
                    dataOutputStream.write(LINE_END.getBytes());
                }
            }
            // 请求结束标志
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
            dataOutputStream.write(end_data);
            dataOutputStream.flush();

            int responseCode = connection.getResponseCode();
            // 判断请求是否成功
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
                int len;
                StringBuilder responseBuilder = new StringBuilder();
                while ((len = inputStream.read()) != -1) {
                    responseBuilder.append((char) len);
                }
                responseEntity.setResponse(responseBuilder.toString());
            }
            responseEntity.setResponseCode(responseCode);
            dataOutputStream.close();
            connection.disconnect();
        } catch (IOException e) {
            responseEntity.setResponseCode(HttpConstants.EXCEPTION);
            responseEntity.setResponse(e.getMessage());
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

    private static String readResponse(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 2];
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
