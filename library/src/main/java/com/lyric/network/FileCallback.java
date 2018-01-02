package com.lyric.network;

/**
 * 文件进度回调接口
 * @author lyricgan
 * @date 17/12/30 下午9:54
 */
public interface FileCallback {

    void onProgress(long contentLength, long currentBytes, boolean isFinished);
}
