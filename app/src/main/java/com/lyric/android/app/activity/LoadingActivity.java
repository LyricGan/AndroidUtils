package com.lyric.android.app.activity;

import android.os.Bundle;

import com.lyric.android.app.base.BaseCompatActivity;
import com.lyric.android.app.R;
import com.lyric.android.app.view.TitleBar;

/**
 * @author lyric
 * @description
 * @time 2016/5/30 12:38
 */
public class LoadingActivity extends BaseCompatActivity {

    @Override
    public void onTitleCreated(TitleBar titleBar) {
        titleBar.setText("Loading");
    }

    @Override
    public void onViewCreated(Bundle savedInstanceState) {
        setContentView(R.layout.activity_loading);
    }
}
