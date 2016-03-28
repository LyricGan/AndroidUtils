package com.lyric.android.library.listener;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * @author lyric
 * @description
 * @time 2016/3/28 16:46
 */
public abstract class AbsTextWatcher implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public abstract void onTextChanged(CharSequence s, int start, int before, int count);

    @Override
    public void afterTextChanged(Editable s) {
    }

}
