package com.lyric.android.library.widget.GridView;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @author lyric
 * @description
 * @time 16/3/10
 */
public class MultipleGridView extends GridView {

    public MultipleGridView(Context context) {
        super(context);
    }

    public MultipleGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MultipleGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MultipleGridView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
