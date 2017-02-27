package com.lyric.android.app.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.lyric.android.app.R;
import com.lyric.android.app.base.BaseCompatActivity;
import com.lyric.android.app.view.TitleBar;
import com.lyric.android.app.widget.praiseview.PraisePopupWindow;

/**
 * 点赞动画页面
 * @author lyricgan
 * @time 2017/2/27 14:56
 */
public class PraiseActivity extends BaseCompatActivity {
    private PraisePopupWindow mPraisePopupWindow;

    @Override
    public void onTitleCreated(TitleBar titleBar) {
        titleBar.setText("PraiseView");
    }

    @Override
    public void onViewCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_praise);
        mPraisePopupWindow = new PraisePopupWindow(this);
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

    public void reset(View view) {
        ((ImageView) findViewById(R.id.good)).setImageResource(R.mipmap.good);
        ((ImageView) findViewById(R.id.good2)).setImageResource(R.mipmap.good);
        ((ImageView) findViewById(R.id.collection)).setImageResource(R.mipmap.collection);
        ((ImageView) findViewById(R.id.bookmark)).setImageResource(R.mipmap.bookmark);
        mPraisePopupWindow.reset();
    }
}
