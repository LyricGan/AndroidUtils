package com.lyric.android.app.base;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.lyric.android.app.R;
import com.lyric.android.app.widget.dialog.LoadingDialog;
import com.lyric.android.app.widget.swipe.SwipeBackActivityBase;
import com.lyric.android.app.widget.swipe.SwipeBackActivityHelper;
import com.lyric.android.app.widget.swipe.SwipeBackLayout;
import com.lyric.android.library.utils.BuildVersionUtils;
import com.lyric.android.library.utils.ViewUtils;

/**
 * 基类Activity
 * @author ganyu
 * @time 2016/5/26 13:59
 */
public abstract class BaseActivity extends FragmentActivity implements OnClickListener, IBaseListener, Constants, SwipeBackActivityBase {
    private boolean mDestroy = false;
    private LoadingDialog mLoadingDialog;
    private SwipeBackActivityHelper mSwipeHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        onPrepareCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        if (isInjectStatusBar()) {
            injectStatusBar();
        }
        if (isSwipeBackEnable()) {
            mSwipeHelper = new SwipeBackActivityHelper(this);
            mSwipeHelper.onActivityCreate();
            setSwipeBackEnable(true);
            getSwipeBackLayout().setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        }
        onViewCreate(savedInstanceState);
    }

    @Override
    public abstract void onViewCreate(Bundle savedInstanceState);

    @Override
    public void onPrepareCreate(Bundle savedInstanceState) {
    }

    @Override
    public void onViewClick(View v) {
    }

    @Override
    public void onClick(View v) {
        if (ViewUtils.isFastOperated()) {
            return;
        }
        onViewClick(v);
    }

    @Override
    protected void onResume() {
        mDestroy = false;
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mDestroy = true;
        super.onDestroy();
    }

    protected boolean isDestroy() {
        if (BuildVersionUtils.hasJellyBean()) {
            return isDestroyed();
        }
        return mDestroy;
    }

    protected void showLoadingDialog() {
        showLoadingDialog("");
    }

    protected void showLoadingDialog(CharSequence message) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
        }
        mLoadingDialog.setMessage(message);
        mLoadingDialog.show();
    }

    protected void hideLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    protected boolean isInjectStatusBar() {
        return false;
    }

    protected void injectStatusBar() {
        ViewUtils.setStatusBarColor(this, ContextCompat.getColor(BaseApp.getContext(), R.color.color_title_bar_bg));
    }

    protected boolean isSwipeBackEnable() {
        return false;
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

    protected <T extends View> T findViewWithId(int id) {
        T view = (T) super.findViewById(id);
        if (isSwipeBackEnable()) {
            if (view == null && mSwipeHelper != null) {
                return (T) mSwipeHelper.findViewById(id);
            }
        }
        return view;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        if (mSwipeHelper != null) {
            return mSwipeHelper.getSwipeBackLayout();
        }
        return null;
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        if (getSwipeBackLayout() != null) {
            getSwipeBackLayout().setEnableGesture(enable);
        }
    }

    @Override
    public void finishActivity() {
        if (getSwipeBackLayout() != null) {
            ViewUtils.convertActivityToTranslucent(this);
            getSwipeBackLayout().finishActivity();
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
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (MotionEvent.ACTION_DOWN == ev.getAction()) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] location = {0, 0};
            v.getLocationInWindow(location);
            int left = location[0];
            int top = location[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom);
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    private void hideKeyboard(IBinder token) {
        if (token == null) {
            return;
        }
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
    }

}