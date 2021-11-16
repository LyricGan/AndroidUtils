package com.lyricgan.library.network;

/**
 * 文件进度回调接口
 * @author Lyric Gan
 * @since 17/12/30 下午9:54
 */
public interface FileCallback {

    void onProgress(long contentLength, long currentBytes, boolean isFinished);
}
