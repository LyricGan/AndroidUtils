package com.lyric.android.library.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @author lyric
 * @description
 * @time 16/3/10
 */
public class MultipleImageView extends ImageView {

    public MultipleImageView(Context context) {
        super(context);
    }

    public MultipleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MultipleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public MultipleImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
