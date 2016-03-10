package com.lyric.android.library.widget.ScrollView;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * @author lyric
 * @description
 * @time 16/3/10
 */
public class MultipleScrollView extends ScrollView {

    public MultipleScrollView(Context context) {
        super(context);
    }

    public MultipleScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MultipleScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MultipleScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
