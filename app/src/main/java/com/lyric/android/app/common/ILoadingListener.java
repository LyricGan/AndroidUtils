package com.lyric.android.app.common;

/**
 * 加载监听接口
 * @author Lyric Gan
 */
public interface ILoadingListener {

    void showLoading(CharSequence message);

    void showLoading(CharSequence message, boolean cancelable);

    void hideLoading();
}
