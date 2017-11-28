package com.lyric.android.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lyric.android.app.R;
import com.lyric.android.app.BaseFragment;
import com.lyric.android.app.widget.webview.DefaultWebLayout;

/**
 * 网页显示页面
 * @author lyricgan
 * @time 2017/6/28 9:45
 */
public class WebFragment extends BaseFragment {
    private static final String TEST_URL = "http://www.baidu.com";

    private DefaultWebLayout mWebLayout;

    public static WebFragment newInstance() {
        Bundle args = new Bundle();
        WebFragment fragment = new WebFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initExtras(Bundle args) {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    protected void initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mWebLayout = findViewById(R.id.layout_web);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mWebLayout.loadUrl(TEST_URL);
    }

    @Override
    public boolean onBackPressed() {
        if (!mWebLayout.onBackPressed()) {
            return true;
        }
        return super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWebLayout.destroy();
    }
}
