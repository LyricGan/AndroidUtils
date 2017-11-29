package com.lyric.android.app.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lyric.android.app.R;
import com.lyric.android.app.common.BaseFragment;
import com.lyric.android.app.widget.praiseview.PraisePopupWindow;

/**
 * 点赞视图页面
 * @author ganyu
 * @date 2017/7/25 14:30
 */
public class PraiseFragment extends BaseFragment {
    private PraisePopupWindow mPraisePopupWindow;

    public static PraiseFragment newInstance() {
        Bundle args = new Bundle();
        PraiseFragment fragment = new PraiseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initExtras(Bundle args) {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_praise;
    }

    @Override
    protected void initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        findViewById(R.id.good).setOnClickListener(this);
        findViewById(R.id.good2).setOnClickListener(this);
        findViewById(R.id.collection).setOnClickListener(this);
        findViewById(R.id.bookmark).setOnClickListener(this);
        findViewById(R.id.btn_reset).setOnClickListener(this);
        mPraisePopupWindow = new PraisePopupWindow(getActivity());
    }

    @Override
    protected void onViewClick(View v) {
        super.onViewClick(v);
        switch (v.getId()) {
            case R.id.good:
                good(v);
                break;
            case R.id.good2:
                good2(v);
                break;
            case R.id.collection:
                collection(v);
                break;
            case R.id.bookmark:
                bookmark(v);
                break;
            case R.id.btn_reset:
                reset();
                break;
        }
    }

    public void good(View view) {
        ((ImageView) view).setImageResource(R.mipmap.good_checked);
        mPraisePopupWindow.setText("+1");
        mPraisePopupWindow.show(view);
    }

    public void good2(View view) {
        ((ImageView) view).setImageResource(R.mipmap.good_checked);
        mPraisePopupWindow.setImage(getResources().getDrawable(R.mipmap.good_checked));
        mPraisePopupWindow.show(view);
    }

    public void collection(View view) {
        ((ImageView) view).setImageResource(R.mipmap.collection_checked);
        mPraisePopupWindow.setTextInfo("收藏成功", Color.parseColor("#f66467"), 12);
        mPraisePopupWindow.show(view);
    }

    public void bookmark(View view) {
        ((ImageView) view).setImageResource(R.mipmap.bookmark_checked);
        mPraisePopupWindow.setTextInfo("收藏成功", Color.parseColor("#ff941A"), 12);
        mPraisePopupWindow.show(view);
    }

    public void reset() {
        ((ImageView) findViewById(R.id.good)).setImageResource(R.mipmap.good);
        ((ImageView) findViewById(R.id.good2)).setImageResource(R.mipmap.good);
        ((ImageView) findViewById(R.id.collection)).setImageResource(R.mipmap.collection);
        ((ImageView) findViewById(R.id.bookmark)).setImageResource(R.mipmap.bookmark);
        mPraisePopupWindow.reset();
    }
}
