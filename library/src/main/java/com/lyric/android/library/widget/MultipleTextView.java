package com.lyric.android.library.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author lyric
 * @description
 * @time 16/3/10
 */
public class MultipleTextView extends TextView {

    public MultipleTextView(Context context) {
        super(context);
    }

    public MultipleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MultipleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public MultipleTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

}
