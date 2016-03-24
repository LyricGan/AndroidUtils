package com.lyric.android.library.widget.EditText;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * @author lyric
 * @description
 * @time 16/3/10
 */
public class MultipleEditText extends EditText {

    public MultipleEditText(Context context) {
        super(context);
    }

    public MultipleEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MultipleEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public MultipleEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
