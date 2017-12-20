package com.lyric.android.app.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.lyric.android.app.R;
import com.lyric.android.app.common.BaseActivity;

public class MainDetailsActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_details;
    }

    @Override
    public void onContentViewInitialize(View view, Bundle savedInstanceState) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("我的课程");
    }
}
