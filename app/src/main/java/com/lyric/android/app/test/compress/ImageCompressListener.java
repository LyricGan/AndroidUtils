package com.lyric.android.app.test.compress;

/**
 * @author lyricgan
 * @description 图片压缩监听事件
 * @time 2016/9/7 15:28
 */
public interface ImageCompressListener {

    void onPreCompress();

    void onPostCompress(ImageCompressResult result);
}
