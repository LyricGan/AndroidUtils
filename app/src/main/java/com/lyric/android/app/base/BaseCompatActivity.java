package com.lyric.android.app.base;

import android.view.View;
import android.widget.LinearLayout;

import com.lyric.android.app.R;
import com.lyric.android.app.view.TitleBar;

/**
 * 带标题栏的基类Activity
 * @author ganyu
 * @time 2016/5/26 13:59
 */
public abstract class BaseCompatActivity extends BaseActivity {
    private TitleBar mTitleBar;

    @Override
    public void setContentView(int layoutResID) {
        setContentView(getLayoutInflater().inflate(layoutResID, null));
    }

    @Override
    public void setContentView(View view) {
        mTitleBar = new TitleBar(this);
        mTitleBar.setLeftDrawable(R.drawable.icon_back);
        mTitleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        onTitleCreated(mTitleBar);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(mTitleBar, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.addView(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        super.setContentView(linearLayout);
    }

    protected TitleBar getTitleBar() {
        return mTitleBar;
    }

    protected abstract void onTitleCreated(TitleBar titleBar);

}
