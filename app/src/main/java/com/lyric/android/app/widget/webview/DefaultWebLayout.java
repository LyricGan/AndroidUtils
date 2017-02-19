package com.lyric.android.app.widget.webview;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.lyric.android.app.R;

/**
 * 默认WebLayout，包含WebView和ProgressBar
 * @author lyric
 * @time 2016/6/23 11:20
 */
public class DefaultWebLayout extends RelativeLayout implements DefaultWebView.WebCallback {
    private DefaultWebView mWebView;
    private ProgressBar mProgressLoading;

    private String mReceivedTitle;

    public DefaultWebLayout(Context context) {
        this(context, null);
    }

    public DefaultWebLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DefaultWebLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    private void initialize(Context context) {
        View rootView = View.inflate(context, R.layout.view_default_web_layout, this);
        mWebView = (DefaultWebView) rootView.findViewById(R.id.web_view);
        mProgressLoading = (ProgressBar) rootView.findViewById(R.id.progress_loading);

        mWebView.setWebCallback(this);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        addImageClickListener();
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return false;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if (newProgress > 95) {
            setViewLoadFinished();
        } else {
            setViewLoading();
        }
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        mReceivedTitle = title;
    }

    private DefaultWebView getWebView() {
        return mWebView;
    }

    private ProgressBar getProgressBar() {
        return mProgressLoading;
    }

    private void setViewLoading() {
        getWebView().setVisibility(View.GONE);
        getProgressBar().setVisibility(View.VISIBLE);
    }

    private void setViewLoadFinished() {
        getWebView().setVisibility(View.VISIBLE);
        getProgressBar().setVisibility(View.GONE);
    }

    public String getReceivedTitle() {
        return mReceivedTitle;
    }

    public void loadUrl(String url) {
        getWebView().loadUrl(url);
    }

    public void loadData(String data) {
        getWebView().loadData(data, "text/html;charset=UTF-8", null);
    }

    public boolean onBackPressed() {
        return getWebView().onBackPressed();
    }

    public void destroy() {
        getWebView().setWebCallback(null);
        getWebView().onDestroy();
    }

    /**
     * 添加网页图片点击事件
     */
    private void addImageClickListener() {
        getWebView().loadUrl("javascript:(function(){"
                + "var objs = document.getElementsByTagName(\"img\"); "
                + "for(var i=0;i<objs.length;i++){"
                + "window.imagelistner.addImageUrl(objs[i].src);"
                + "objs[i].onclick=function(){"
                + "window.imagelistner.openImage(i, this.src);"
                + "}}"
                + "})()");
    }
}
