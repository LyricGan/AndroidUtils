package com.lyric.android.app.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.lyric.android.app.R;

/**
 * @author lyric
 * @description 默认WebLayout，包含WebView和ProgressBar
 * @time 2016/6/23 11:20
 */
public class DefaultWebLayout extends RelativeLayout implements DefaultWebView.WebCallback {
    private DefaultWebView web_view;
    private ProgressBar progress_loading;

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
        web_view = (DefaultWebView) rootView.findViewById(R.id.web_view);
        progress_loading = (ProgressBar) rootView.findViewById(R.id.progress_loading);

        web_view.setWebCallback(this);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {

    }

    @Override
    public void onPageFinished(WebView view, String url) {

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

    public DefaultWebView getWebView() {
        return web_view;
    }

    public ProgressBar getProgressBar() {
        return progress_loading;
    }

    public String getReceivedTitle() {
        return mReceivedTitle;
    }

    private void setViewLoading() {
        getWebView().setVisibility(View.GONE);
        getProgressBar().setVisibility(View.VISIBLE);
    }

    private void setViewLoadFinished() {
        getWebView().setVisibility(View.VISIBLE);
        getProgressBar().setVisibility(View.GONE);
    }

    public boolean onBackPressed() {
        return getWebView().onBackPressed();
    }

    public void destroy() {
        getWebView().destroy();
    }

    public void loadUrl(String url) {
        getWebView().loadUrl(url);
    }
}
