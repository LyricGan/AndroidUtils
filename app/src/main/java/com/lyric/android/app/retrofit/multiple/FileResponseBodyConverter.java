package com.lyric.android.app.retrofit.multiple;

import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * @author lyricgan
 * @description
 * @time 2016/7/28 17:21
 */
public class FileResponseBodyConverter implements Converter<ResponseBody, File> {
    public static final String SAVE_PATH = "savePath2016050433191";
    static final FileResponseBodyConverter INSTANCE = new FileResponseBodyConverter();

    @Override
    public File convert(ResponseBody value) throws IOException {
        String saveFilePath = getSaveFilePath(value);
        return writeResponseBodyToDisk(value, saveFilePath);
    }

    @Nullable
    private String getSaveFilePath(ResponseBody responseBody) {
        String saveFilePath = null;
        String requestFileName = null;
        try {
            Class clazz = responseBody.getClass();
            Field field = clazz.getDeclaredField("delegate");
            field.setAccessible(true);
            ResponseBody body = (ResponseBody) field.get(responseBody);
            if (body instanceof ProgressResponseBody) {
                ProgressResponseBody prBody = ((ProgressResponseBody) body);
                saveFilePath = prBody.getSavePath();
                requestFileName = prBody.getFileName();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        //请求的文件名为空则根据时间戳生成一个临时文件名
        if (TextUtils.isEmpty(requestFileName)) {
            requestFileName = System.currentTimeMillis() + ".tmp";
        }
        //如果保存路径是一个文件夹,则在后面加上请求文件名
        if (!TextUtils.isEmpty(saveFilePath)) {
            File file = new File(saveFilePath);
            if (file.isDirectory()) {
                saveFilePath = saveFilePath + File.separator + requestFileName;
            }
        }
        //如果保存路径为null则设置默认保存到sdcard根目录
        if (TextUtils.isEmpty(saveFilePath)) {
            saveFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + requestFileName;
        }
        return saveFilePath;
    }

    /**
     * 将文件写入本地
     *
     * @param body http响应体
     * @param path 保存路径
     * @return 保存file
     */
    public static File writeResponseBodyToDisk(ResponseBody body, String path) {
        File futureStudioIconFile = null;
        try {
            futureStudioIconFile = new File(path);
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[4096];
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                }
                outputStream.flush();
                return futureStudioIconFile;
            } catch (IOException e) {
                return futureStudioIconFile;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return futureStudioIconFile;
        }
    }
}

