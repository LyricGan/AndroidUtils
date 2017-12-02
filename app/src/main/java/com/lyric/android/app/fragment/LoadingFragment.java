package com.lyric.android.app.fragment;

import android.os.Bundle;
import android.view.View;

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
    public int getLayoutId() {
        return R.layout.fragment_loading;
    }

    @Override
    public void onViewInitialize(View view, Bundle savedInstanceState) {
    }
}
