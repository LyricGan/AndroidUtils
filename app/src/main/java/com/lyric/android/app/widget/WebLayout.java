package com.lyric.android.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.lyric.android.app.Constants;
import com.lyric.android.app.R;

/**
 * 自定义WebLayout，包含WebView和ProgressBar
 * @author lyricgan
 * @time 2016/6/23 11:20
 */
public class WebLayout extends RelativeLayout {
    private WebViewCompat mWebView;
    private ProgressBar mProgressLoading;

    public WebLayout(Context context) {
        this(context, null);
    }

    public WebLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WebLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    private void initialize(Context context) {
        View rootView = View.inflate(context, R.layout.view_web_layout, this);
        mWebView = (WebViewCompat) rootView.findViewById(R.id.web_view);
        mProgressLoading = (ProgressBar) rootView.findViewById(R.id.progress_loading);

        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress > 95) {
                    mWebView.setVisibility(View.VISIBLE);
                    mProgressLoading.setVisibility(View.GONE);
                } else {
                    mWebView.setVisibility(View.GONE);
                    mProgressLoading.setVisibility(View.VISIBLE);
                }
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });
        mWebView.setWebDebuggingEnabled(Constants.DEBUG);
    }

    public WebViewCompat getWebView() {
        return mWebView;
    }

    public void loadUrl(String url) {
        mWebView.loadUrl(url);
    }

    public boolean onBackPressed() {
        return mWebView.onBackPressed();
    }

    public void destroy() {
        mWebView.destroy();
    }
}
