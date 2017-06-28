package com.lyric.android.app.fragment;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lyric.android.app.R;
import com.lyric.android.app.base.BaseFragment;
import com.lyric.android.app.widget.webview.video.VideoWebChromeClient;
import com.lyric.android.app.widget.webview.video.VideoWebView;

/**
 * 用于测试加载视频
 * @author lyricgan
 * @time 2017/6/28 9:45
 */
public class WebFragment extends BaseFragment {
    final String url = "http://h5.jihai8.com/info/20161217/00000000029";
    final String url_2 = "https://v.qq.com/iframe/player.html?vid=o0356fjfl50&tiny=0&auto=0";

    private VideoWebView videoWebView;

    @Override
    protected void initExtras(Bundle savedInstanceState) {
    }

    @Override
    protected View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_web, null);
        videoWebView = (VideoWebView) rootView.findViewById(R.id.web_view);
        initWeb(rootView, videoWebView);

        return rootView;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        videoWebView.loadUrl(url);
    }

    private void initWeb(View rootView, VideoWebView webView) {
        View nonVideoLayout = rootView.findViewById(R.id.nonVideoLayout);
        ViewGroup videoLayout = (ViewGroup) rootView.findViewById(R.id.videoLayout);
        View loadingView = View.inflate(getContext(), R.layout.view_loading_video, null);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setSupportZoom(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        VideoWebChromeClient videoWebChromeClient = new VideoWebChromeClient(nonVideoLayout, videoLayout, loadingView, webView) {
            @Override
            public void onProgressChanged(WebView view, int progress) {
            }
        };
        final Activity activity = getActivity();
        videoWebChromeClient.setOnToggledFullscreen(new VideoWebChromeClient.ToggledFullscreenCallback() {

            @Override
            public void toggledFullscreen(boolean fullscreen) {
                if (fullscreen) {
                    WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
                    attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    activity.getWindow().setAttributes(attrs);
                    if (android.os.Build.VERSION.SDK_INT >= 14) {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
                    }
                } else {
                    WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    activity.getWindow().setAttributes(attrs);
                    if (android.os.Build.VERSION.SDK_INT >= 14) {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                }
            }
        });
        webView.setWebChromeClient(videoWebChromeClient);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }
}
