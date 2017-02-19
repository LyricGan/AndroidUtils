package com.aspsine.irecyclerview.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.aspsine.irecyclerview.R;

/**
 * @author <a href="mailto:ganyu@medlinker.com">ganyu</a>
 * @version 1.0
 * @description 加载进度条
 * @time 2016/8/16 11:30
 */
public class LoadingProgressBar extends ProgressBar {

    public LoadingProgressBar(Context context) {
        this(context, null);
    }

    public LoadingProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        initialize();
    }

    private void initialize() {
        Drawable drawable = getResources().getDrawable(R.drawable.progressbar_loading);
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            setIndeterminateDrawable(drawable);
        }
    }
}
