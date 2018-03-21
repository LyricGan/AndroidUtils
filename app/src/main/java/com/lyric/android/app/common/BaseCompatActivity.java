package com.lyric.android.app.common;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.lyric.android.app.AndroidApplication;
import com.lyric.android.app.R;
import com.lyric.android.app.utils.LogUtils;
import com.lyric.android.app.utils.ViewUtils;
import com.lyric.android.app.widget.LoadingDialog;
import com.lyric.android.app.widget.TitleBar;
import com.lyric.android.app.widget.swipeback.SwipeBackActivityBase;
import com.lyric.android.app.widget.swipeback.SwipeBackActivityHelper;
import com.lyric.android.app.widget.swipeback.SwipeBackLayout;
import com.lyric.common.BaseActivity;
import com.lyric.common.BaseTitleBar;

/**
 * 带扩展功能的Activity
 * @author lyricgan
 * @date 2017/12/22 12:57
 */
public abstract class BaseCompatActivity extends BaseActivity implements SwipeBackActivityBase {
    private SwipeBackActivityHelper mSwipeBackHelper;
    private LoadingDialog mLoadingDialog;

    @Override
    protected void onPrepareContentView() {
        super.onPrepareContentView();
        if (isSwipeBackEnable()) {
            mSwipeBackHelper = new SwipeBackActivityHelper(this);
            mSwipeBackHelper.onActivityCreate();
            setSwipeBackEnable(true);
            getSwipeBackLayout().setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        }
        if (isInjectStatusBar()) {
            injectStatusBar();
        }
    }

    @Override
    public final void onTitleBarInitialize(BaseTitleBar titleBar, Bundle savedInstanceState) {
        super.onTitleBarInitialize(titleBar, savedInstanceState);
        View titleView = titleBar.getTitleView();
        if (titleView instanceof TitleBar) {
            onTitleBarInitialize((TitleBar) titleView, savedInstanceState);
        } else if (titleView instanceof Toolbar) {
            onTitleBarInitialize((Toolbar) titleView, savedInstanceState);
        } else {
            LogUtils.d(TAG, "TitleBar is empty...");
        }
    }

    @Override
    public void onClick(View v) {
        if (ViewUtils.isFastOperated()) {
            return;
        }
        super.onClick(v);
    }

    protected void onTitleBarInitialize(TitleBar titleBar, Bundle savedInstanceState) {
        titleBar.setLeftDrawable(R.drawable.icon_back);
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    protected void onTitleBarInitialize(Toolbar toolbar, Bundle savedInstanceState) {
    }

    protected boolean isInjectStatusBar() {
        return false;
    }

    protected void injectStatusBar() {
        ViewUtils.setStatusBarColor(this, ContextCompat.getColor(AndroidApplication.getContext(), R.color.color_title_bar_bg));
    }

    protected boolean isSwipeBackEnable() {
        return false;
    }

    protected SwipeBackActivityHelper getSwipeBackHelper() {
        return mSwipeBackHelper;
    }

    @Override
    public View findViewById(int id) {
        View view = super.findViewById(id);
        if (view == null) {
            if (isSwipeBackEnable()) {
                SwipeBackActivityHelper swipeBackHelper = getSwipeBackHelper();
                if (swipeBackHelper != null) {
                    return swipeBackHelper.findViewById(id);
                }
            }
        }
        return view;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (isSwipeBackEnable()) {
            SwipeBackActivityHelper swipeBackHelper = getSwipeBackHelper();
            if (swipeBackHelper != null) {
                swipeBackHelper.onPostCreate();
            }
        }
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        SwipeBackActivityHelper swipeBackHelper = getSwipeBackHelper();
        if (swipeBackHelper != null) {
            return swipeBackHelper.getSwipeBackLayout();
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
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
        }
        mLoadingDialog.setMessage(message);
        mLoadingDialog.setCancelable(cancelable);
        mLoadingDialog.show();
    }

    @Override
    public void hideLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }
}
