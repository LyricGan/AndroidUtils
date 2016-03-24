package com.lyric.android.library.widget.WebView;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * @author lyric
 * @description
 * @time 16/3/10
 */
public abstract class DefaultWebView extends WebView {

    public DefaultWebView(Context context) {
        super(context);
    }

    public DefaultWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DefaultWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DefaultWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
