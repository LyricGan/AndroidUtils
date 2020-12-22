package com.lyric.android.app.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lyric.android.app.R;
import com.lyric.android.app.utils.ActivityUtils;
import com.lyric.android.app.widget.refresh.RefreshLayout;
import com.lyric.android.app.widget.refresh.OnRefreshListener;
import com.lyric.android.app.common.BaseFragment;
import com.lyric.android.app.common.BaseRecyclerAdapter;

/**
 * 列表页面
 * @author Lyric Gan
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
    public void onCreateContentView(View view, Bundle savedInstanceState) {
        RefreshLayout refreshLayout = findViewById(R.id.refresh_layout);
        mRecyclerView = findViewById(R.id.recycler_view);

        refreshLayout.setAutoLoadMore(true);
        refreshLayout.setEnableOverScroll(false);
        refreshLayout.setNestedScrollingEnabled(true);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh(final RefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishRefreshing();
                    }
                }, 2000);
            }

            @Override
            public void onLoadMore(final RefreshLayout refreshLayout) {
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
    public void onCreateData(Bundle savedInstanceState) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new RecyclerViewAdapter(getActivity()));
    }

    private static class RecyclerViewAdapter extends BaseRecyclerAdapter {

        RecyclerViewAdapter(Context context) {
            super(context, R.layout.list_item_card_main);
        }

        @Override
        public void convert(final View itemView, int position, Object item) {
            TextView tvItemTitle = (TextView) itemView.findViewById(R.id.tv_item_title);
            TextView tvItemContent = (TextView) itemView.findViewById(R.id.tv_item_content);

            tvItemTitle.setText("标题");
            tvItemContent.setText("一段内容一段内容一段内容一段内容一段内容一段内容一段内容一段内容一段内容一段内容一段内容一段内容一段内容一段内容一段内容");
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addAnimator(itemView);
                }
            });
        }

        @Override
        public int getItemCount() {
            return 10;
        }

        private void addAnimator(View itemView) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(itemView, "translationZ", 20, 0);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    ActivityUtils.startActivity(getContext(), MainDetailsActivity.class);
                }
            });
            animator.start();
        }
    }
}
