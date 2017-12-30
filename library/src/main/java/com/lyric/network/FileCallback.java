package com.lyric.network;

import java.io.File;

import okhttp3.ResponseBody;

/**
 * 文件回调接口
 * @author lyricgan
 * @date 17/12/30 下午9:54
 */
public abstract class FileCallback extends BaseCallback<File> {

    @Override
    public File parseResponse(ResponseBody responseBody) {
        return null;
    }

    public abstract void onProgress(long contentLength, long currentBytes, boolean isFinished);
}
