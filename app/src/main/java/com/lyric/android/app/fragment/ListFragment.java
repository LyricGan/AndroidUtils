package com.lyric.android.app.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lyric.android.app.R;
import com.lyric.android.app.adapter.RecyclerViewAdapter;
import com.lyric.common.BaseFragment;
import com.lyric.android.app.widget.refresh.GraceRefreshLayout;
import com.lyric.android.app.widget.refresh.OnRefreshListener;

/**
 * 列表页面
 * @author lyricgan
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
    public int getLayoutId() {
        return R.layout.list_fragment;
    }

    @Override
    public void onContentViewInitialize(View view, Bundle savedInstanceState) {
        GraceRefreshLayout refreshLayout = findViewByIdRes(R.id.refresh_layout);
        mRecyclerView = findViewByIdRes(R.id.recycler_view);

        refreshLayout.setAutoLoadMore(true);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh(final GraceRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishRefreshing();
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore(final GraceRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishLoadMore();
                    }
                }, 2000);
            }
        });
    }

    @Override
    public void onDataInitialize(Bundle savedInstanceState) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new RecyclerViewAdapter(getActivity()));
    }
}
