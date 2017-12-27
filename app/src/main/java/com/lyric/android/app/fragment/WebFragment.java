package com.lyric.android.app.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.lyric.android.app.Constants;
import com.lyric.android.app.R;
import com.lyric.common.BaseFragment;
import com.lyric.android.app.widget.WebLayout;

/**
 * 网页显示页面
 * @author lyricgan
 * @time 2017/6/28 9:45
 */
public class WebFragment extends BaseFragment {
    private WebLayout mWebLayout;

    private String mUrl;

    public static WebFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString(Constants.EXTRAS_URL, url);
        WebFragment fragment = new WebFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onExtrasInitialize(Bundle bundle) {
        super.onExtrasInitialize(bundle);
        mUrl = bundle.getString(Constants.EXTRAS_URL);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    public void onContentViewInitialize(View view, Bundle savedInstanceState) {
        mWebLayout = findViewByIdRes(R.id.layout_web);
    }

    @Override
    public void onDataInitialize(Bundle savedInstanceState) {
        super.onDataInitialize(savedInstanceState);
        if (!TextUtils.isEmpty(mUrl)) {
            mWebLayout.loadUrl(mUrl);
        }
    }

    @Override
    public boolean onBackPressed() {
        return (mWebLayout != null && mWebLayout.onBackPressed()) || super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWebLayout != null) {
            mWebLayout.destroy();
        }
    }
}
