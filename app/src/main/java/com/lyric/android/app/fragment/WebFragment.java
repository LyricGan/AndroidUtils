package com.lyric.android.app.fragment;

import android.os.Bundle;
import android.view.View;

import com.lyric.android.app.R;
import com.lyric.android.app.common.BaseFragment;
import com.lyric.android.app.widget.webview.WebLayout;

/**
 * 网页显示页面
 * @author lyricgan
 * @time 2017/6/28 9:45
 */
public class WebFragment extends BaseFragment {
    private WebLayout mWebLayout;

    public static WebFragment newInstance() {
        Bundle args = new Bundle();
        WebFragment fragment = new WebFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    public void onViewInitialize(View view, Bundle savedInstanceState) {
        mWebLayout = findViewWithId(R.id.layout_web);
    }

    @Override
    public void onDataInitialize(Bundle savedInstanceState) {
        super.onDataInitialize(savedInstanceState);
        mWebLayout.loadUrl("https://github.com");
    }

    @Override
    public boolean onBackPressed() {
        if (mWebLayout != null) {
            if (mWebLayout.onBackPressed()) {
                return true;
            }
        }
        return super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWebLayout != null) {
            mWebLayout.destroy();
        }
    }
}
