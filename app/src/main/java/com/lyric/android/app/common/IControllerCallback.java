package com.lyric.android.app.common;

import android.view.View;

/**
 * @author lyricgan
 */
public interface IControllerCallback<T> {

    void onCreateView(View view);

    void onUpdateView(T data);

    void onDestroyView();
}
