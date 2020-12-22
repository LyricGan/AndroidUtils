package com.lyric.android.app.common;

import android.view.View;

/**
 * @author Lyric Gan
 */
public interface IControllerCallback<E> {

    void onCreateView(View view);

    void onUpdateView(E data);

    void onDestroyView();
}
