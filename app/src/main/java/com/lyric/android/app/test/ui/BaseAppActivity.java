package com.lyric.android.app.test.ui;

import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

/**
 * @author lyricgan
 * @description
 * @time 2016/8/9 14:00
 */
public abstract class BaseAppActivity extends AppCompatActivity {

    protected abstract int getContentViewId();

    protected abstract int getFragmentContentId();

    protected void addFragment(BaseFragment fragment) {
        if (fragment == null) {
            return;
        }
        String fragmentName = fragment.getClass().getSimpleName();
        getSupportFragmentManager().beginTransaction()
                .replace(getContentViewId(), fragment, fragmentName)
                .addToBackStack(fragmentName)
                .commitAllowingStateLoss();
    }

    protected void removeFragment() {
        if (getSupportFragmentManager().getFragments().size() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (getSupportFragmentManager().getFragments().size() == 1) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
