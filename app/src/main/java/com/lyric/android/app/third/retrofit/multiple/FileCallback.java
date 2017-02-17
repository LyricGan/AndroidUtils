package com.lyric.android.app.third.retrofit.multiple;

/**
 * @author lyricgan
 * @description 文件上传下载回调接口
 * @time 2016/7/29 10:20
 */
public interface FileCallback {

    void onProgress(long currentSize, long totalSize, boolean isCompleted);
}
