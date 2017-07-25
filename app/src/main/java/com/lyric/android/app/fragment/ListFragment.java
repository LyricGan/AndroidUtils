package com.lyric.android.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lyric.android.app.R;
import com.lyric.android.app.adapter.RecyclerViewAdapter;
import com.lyric.android.app.base.BaseFragment;

/**
 * 列表页面
 * @author ganyu
 * @date 2017/7/25 14:22
 */
public class ListFragment extends BaseFragment {
    private RecyclerView mRecyclerView;

    public static ListFragment newInstance() {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initExtras(Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.list_fragment;
    }

    @Override
    protected void initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) getRootView();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new RecyclerViewAdapter(getActivity()));
    }
}
