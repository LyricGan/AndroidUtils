package com.lyric.android.app.base;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.lyric.android.app.R;
import com.lyric.android.app.view.TitleBar;
import com.lyric.android.app.widget.swipe.SwipeBackActivityBase;
import com.lyric.android.app.widget.swipe.SwipeBackActivityHelper;
import com.lyric.android.app.widget.swipe.SwipeBackLayout;
import com.lyric.android.library.utils.ViewUtils;

/**
 * @author lyricgan
 * @description
 * @time 2016/5/26 13:59
 */
public abstract class BaseCompatActivity extends BaseActivity implements SwipeBackActivityBase {
    private SwipeBackActivityHelper mSwipeHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (isSwipeBackEnable()) {
            mSwipeHelper = new SwipeBackActivityHelper(this);
            mSwipeHelper.onActivityCreate();
            setSwipeBackEnable(true);
            getSwipeBackLayout().setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        setContentView(getLayoutInflater().inflate(layoutResID, null));
    }

    @Override
    public void setContentView(View view) {
        TitleBar titleBar = new TitleBar(this);
        titleBar.setLeftDrawable(R.drawable.icon_back);
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        onTitleCreated(titleBar);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(titleBar, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.addView(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        super.setContentView(linearLayout);
    }

    public abstract void onTitleCreated(TitleBar titleBar);

    protected boolean isSwipeBackEnable() {
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (isSwipeBackEnable()) {
            mSwipeHelper.onPostCreate();
        }
    }

    @Override
    public View findViewById(int id) {
        View view = super.findViewById(id);
        if (isSwipeBackEnable()) {
            if (view == null && mSwipeHelper != null) {
                return mSwipeHelper.findViewById(id);
            }
        }
        return view;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        if (isSwipeBackEnable()) {
            return mSwipeHelper.getSwipeBackLayout();
        }
        return null;
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        if (isSwipeBackEnable()) {
            getSwipeBackLayout().setEnableGesture(enable);
        }
    }

    @Override
    public void finishActivity() {
        ViewUtils.convertActivityToTranslucent(this);
        getSwipeBackLayout().finishActivity();
    }

    @Override
    public void onBackPressed() {
        if (isSwipeBackEnable()) {
            finishActivity();
            return;
        }
        super.onBackPressed();
    }

}
