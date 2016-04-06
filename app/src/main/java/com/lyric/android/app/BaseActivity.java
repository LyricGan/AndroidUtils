package com.lyric.android.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * BaseActivity
 */
public abstract class BaseActivity extends Activity implements OnClickListener, IBaseListener {
    private boolean mDestroy = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onInitView(savedInstanceState);
    }

    @Override
    public abstract void onInitView(Bundle savedInstanceState);

    @Override
    public void onWidgetClick(View v) {
    }

    @Override
    public void onResponse(int flag, String response) {
    }

    @Override
    public void onClick(View v) {
        onWidgetClick(v);
    }

    @Override
    protected void onResume() {
        mDestroy = false;
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mDestroy = true;
        super.onDestroy();
    }

    protected boolean isDestroy() {
        return mDestroy;
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
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}