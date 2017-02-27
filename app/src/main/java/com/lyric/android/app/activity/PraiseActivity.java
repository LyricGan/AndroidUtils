package com.lyric.android.app.activity;

import android.os.Bundle;

import com.lyric.android.app.R;
import com.lyric.android.app.base.BaseCompatActivity;
import com.lyric.android.app.view.TitleBar;

/**
 * 点赞动画页面
 * @author lyricgan
 * @time 2017/2/27 14:56
 */
public class PraiseActivity extends BaseCompatActivity {

    @Override
    public void onTitleCreated(TitleBar titleBar) {

    }

    @Override
    public void onViewCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_praise);
    }
}
