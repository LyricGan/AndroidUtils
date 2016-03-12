package com.lyric.android.library.widget.WebView;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * @author lyric
 * @description
 * @time 2016/3/12 17:19
 */
public class MultipleWebView extends WebView {
    public MultipleWebView(Context context) {
        super(context);
    }

    public MultipleWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MultipleWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MultipleWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
