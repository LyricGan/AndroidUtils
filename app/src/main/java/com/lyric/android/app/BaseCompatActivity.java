package com.lyric.android.app;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.lyric.android.app.view.TitleBar;

/**
 * @author lyric
 * @description
 * @time 2016/5/26 13:59
 */
public abstract class BaseCompatActivity extends BaseActivity {
    private TitleBar mTitleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mTitleBar = new TitleBar(this);
        mTitleBar.setLeftDrawable(R.drawable.icon_back);
        mTitleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        initialize(mTitleBar);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        setContentView(getLayoutInflater().inflate(layoutResID, null));
    }

    @Override
    public void setContentView(View view) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(mTitleBar, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.addView(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        super.setContentView(layout);
    }

    private void initialize(TitleBar titleBar) {
        onTitleCreated(titleBar);
    }

    public abstract void onTitleCreated(TitleBar titleBar);
}
