package com.lyric.android.app.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ZoomButtonsController;

import java.lang.reflect.Field;

/**
 * 自定义WebView，继承于{@link WebSecureView}
 * @author lyricgan
 * @time 2016/6/23 10:36
 */
public class WebViewCompat extends WebSecureView {

    public WebViewCompat(Context context) {
        super(context);
    }

    public WebViewCompat(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WebViewCompat(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initWebSettings(WebSettings settings) {
        final String filesDir = getContext().getFilesDir().getPath();
        final String databaseDir = filesDir.substring(0, filesDir.lastIndexOf("/")) + "/databases";
        settings.setAllowFileAccess(false);
        setAllowAccessFromFileUrls(settings, false);
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        }
        settings.setDatabaseEnabled(true);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            settings.setDatabasePath(databaseDir);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setLoadWithOverviewMode(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(false);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            settings.setDisplayZoomControls(false);
        } else {
            setDisplayZoomControlsGone(this);
        }
    }

    private void setDisplayZoomControlsGone(View view) {
        Class classType;
        Field field;
        try {
            classType = WebView.class;
            field = classType.getDeclaredField("mZoomButtonsController");
            field.setAccessible(true);
            ZoomButtonsController controller = new ZoomButtonsController(view);
            controller.getZoomControls().setVisibility(View.GONE);
            field.set(view, controller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setWebDebuggingEnabled(boolean enabled) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 方便WebView在chrome调试
            WebView.setWebContentsDebuggingEnabled(enabled);
        }
    }

    public void setAllowAccessFromFileUrls(WebSettings webSettings, boolean allowed) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.setAllowFileAccessFromFileURLs(allowed);
            webSettings.setAllowUniversalAccessFromFileURLs(allowed);
        }
    }

    public boolean onBackPressed() {
        if (canGoBack()) {
            goBack();
            return false;
        }
        return true;
    }

    public void onDestroy() {
        try {
            ViewParent viewParent = getParent();
            if (viewParent != null) {
                ((ViewGroup) getParent()).removeView(this);
            }
            removeAllViews();
            destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
