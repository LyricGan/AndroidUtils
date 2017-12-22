package com.lyric.common;

/**
 * 加载监听接口
 * @author lyricgan
 * @date 2017/12/22 13:06
 */
public interface ILoadingListener {

    void showLoading(CharSequence message);

    void showLoading(CharSequence message, boolean cancelable);

    void hideLoading();
}
