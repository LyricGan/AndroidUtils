package com.lyric.android.app.common;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.lyric.android.app.R;

/**
 * base activity
 *
 * @author lyricgan
 */
public abstract class BaseActivity extends AppCompatActivity implements IBaseListener, IMessageProcessor, ILoadingListener {
    protected final String TAG = getClass().getSimpleName();
    private boolean mDestroy = false;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        onCreatePrepare(savedInstanceState);
        super.onCreate(savedInstanceState);
        mHandler = new InnerHandler(this);
        ActivityStackManager.getInstance().add(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            onCreateExtras(bundle);
        }
        onCreateContentViewPrepare();
        int layoutId = getLayoutId();
        if (layoutId > 0) {
            setContentView(layoutId);
        } else {
            setContentView(getContentView());
        }
        View decorView = getWindow().getDecorView();
        View titleView = decorView.findViewById(R.id.title_bar);
        if (titleView != null) {
            if (titleView instanceof Toolbar) {
                onCreateTitleBar((Toolbar) titleView, savedInstanceState);
            } else {
                BaseTitleBar titleBar = new BaseTitleBar(titleView);
                onCreateTitleBar(titleBar, savedInstanceState);
            }
        }
        onCreateContentView(decorView, savedInstanceState);

        onCreateData(savedInstanceState);
    }

    @Override
    public void onCreatePrepare(Bundle savedInstanceState) {
    }

    @Override
    public void onCreateExtras(Bundle bundle) {
    }

    @Override
    public void onCreateTitleBar(BaseTitleBar titleBar, Bundle savedInstanceState) {
    }

    protected void onCreateTitleBar(Toolbar toolbar, Bundle savedInstanceState) {
    }

    protected void onCreateContentViewPrepare() {
    }

    protected View getContentView() {
        return null;
    }

    @Override
    public void onCreateData(Bundle savedInstanceState) {
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public <T extends View> T findViewWithId(int id) {
        return (T) super.findViewById(id);
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
        ActivityStackManager.getInstance().remove(this);
    }

    protected boolean isDestroy() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return isDestroyed();
        }
        return mDestroy;
    }

    @Override
    public void showLoading(CharSequence message) {
        showLoading("", true);
    }

    @Override
    public void showLoading(CharSequence message, boolean cancelable) {
    }

    @Override
    public void hideLoading() {
    }

    protected boolean isAutoHideKeyboard() {
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isAutoHideKeyboard()) {
            if (MotionEvent.ACTION_DOWN == ev.getAction()) {
                View v = getCurrentFocus();
                if (isShouldHideKeyboard(v, ev)) {
                    hideKeyboard(v.getWindowToken());
                }
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
        if (im != null) {
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public Fragment getFragment(Context context, Class<?> fragmentClass, Bundle args) {
        return getFragment(context, fragmentClass.getName(), args);
    }

    public Fragment getFragment(Context context, String fragmentName, Bundle args) {
        Fragment fragment = null;
        try {
            fragment = Fragment.instantiate(context, fragmentName, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fragment;
    }

    public void addFragment(int containerViewId, Fragment fragment, String tag, boolean isAddToBackStack, String name) {
        commitFragment(containerViewId, fragment, tag, isAddToBackStack, name, false);
    }

    public void replaceFragment(int containerViewId, Fragment fragment, String tag, boolean isAddToBackStack, String name) {
        commitFragment(containerViewId, fragment, tag, isAddToBackStack, name, true);
    }

    private void commitFragment(int containerViewId, Fragment fragment, String tag, boolean isAddToBackStack, String name, boolean isReplace) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (isReplace) {
            fragmentTransaction.replace(containerViewId, fragment, tag);
        } else {
            fragmentTransaction.add(containerViewId, fragment, tag);
        }
        if (isAddToBackStack) {
            fragmentTransaction.addToBackStack(name);
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public Handler getHandler() {
        return mHandler;
    }

    @Override
    public void handleMessage(Message msg) {
    }

    private static class InnerHandler extends BaseHandler<IMessageProcessor> {

        InnerHandler(IMessageProcessor object) {
            super(object);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            IMessageProcessor listener = get();
            if (listener != null) {
                listener.handleMessage(msg);
            }
        }
    }
}