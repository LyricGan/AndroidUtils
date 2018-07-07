package com.lyric.android.app.ui;

import android.os.Bundle;
import android.view.View;

import com.lyric.android.app.R;
import com.lyric.android.app.common.BaseActivity;
import com.lyric.android.app.common.BaseTitleBar;
import com.lyric.android.app.utils.ViewUtils;
import com.lyric.android.app.widget.TitleBar;
import com.lyric.android.app.widget.swipeback.SwipeBackActivityBase;
import com.lyric.android.app.widget.swipeback.SwipeBackActivityHelper;
import com.lyric.android.app.widget.swipeback.SwipeBackLayout;

/**
 * base activity compat
 *
 * @author lyricgan
 */
public abstract class BaseCompatActivity extends BaseActivity implements SwipeBackActivityBase {
    private SwipeBackActivityHelper mSwipeBackHelper;

    @Override
    protected void onCreateContentViewPrepare() {
        super.onCreateContentViewPrepare();
        if (isSwipeBackEnable()) {
            mSwipeBackHelper = new SwipeBackActivityHelper(this);
            mSwipeBackHelper.onActivityCreate();
            setSwipeBackEnable(true);
            getSwipeBackLayout().setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        }
    }

    @Override
    public final void onCreateTitleBar(BaseTitleBar titleBar, Bundle savedInstanceState) {
        super.onCreateTitleBar(titleBar, savedInstanceState);
        View titleView = titleBar.getTitleView();
        if (titleView instanceof TitleBar) {
            onCreateTitleBar((TitleBar) titleView, savedInstanceState);
        }
    }

    @Override
    public void onClick(View v) {
        if (ViewUtils.isFastOperated(500L)) {
            return;
        }
        super.onClick(v);
    }

    protected void onCreateTitleBar(TitleBar titleBar, Bundle savedInstanceState) {
        titleBar.setLeftDrawable(R.drawable.icon_back);
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    protected boolean isSwipeBackEnable() {
        return false;
    }

    @Override
    public View findViewById(int id) {
        View view = super.findViewById(id);
        if (view == null) {
            if (isSwipeBackEnable()) {
                if (mSwipeBackHelper != null) {
                    return mSwipeBackHelper.findViewById(id);
                }
            }
        }
        return view;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (isSwipeBackEnable()) {
            if (mSwipeBackHelper != null) {
                mSwipeBackHelper.onPostCreate();
            }
        }
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        if (mSwipeBackHelper != null) {
            return mSwipeBackHelper.getSwipeBackLayout();
        }
        return null;
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        SwipeBackLayout swipeBackLayout = getSwipeBackLayout();
        if (swipeBackLayout != null) {
            swipeBackLayout.setEnableGesture(enable);
        }
    }

    @Override
    public void finishActivity() {
        SwipeBackLayout swipeBackLayout = getSwipeBackLayout();
        if (swipeBackLayout != null) {
            ViewUtils.convertActivityToTranslucent(this);
            swipeBackLayout.finishActivity();
        }
    }

    @Override
    public void onBackPressed() {
        if (isSwipeBackEnable()) {
            finishActivity();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void showLoading(CharSequence message, boolean cancelable) {
        super.showLoading(message, cancelable);
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
    }
}
