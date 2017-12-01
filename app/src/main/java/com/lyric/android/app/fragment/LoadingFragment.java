package com.lyric.android.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lyric.android.app.R;
import com.lyric.android.app.common.BaseFragment;

/**
 * 加载视图页面
 * @author lyricgan
 * @date 2017/7/25 14:14
 */
public class LoadingFragment extends BaseFragment {

    public static LoadingFragment newInstance() {
        Bundle args = new Bundle();
        LoadingFragment fragment = new LoadingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initExtras(Bundle args) {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_loading;
    }

    @Override
    public void onViewCreate(@Nullable Bundle savedInstanceState) {
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
    }
}
