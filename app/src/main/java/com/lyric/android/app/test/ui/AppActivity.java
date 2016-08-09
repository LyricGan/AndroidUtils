package com.lyric.android.app.test.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lyric.android.app.R;

/**
 * @author lyricgan
 * @description
 * @time 2016/8/9 14:12
 */
public abstract class AppActivity extends BaseAppActivity {

    protected abstract BaseFragment getFirstFragment();

    protected void handleIntent(Intent intent) {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        if (getIntent() != null) {
            handleIntent(getIntent());
        }
        if (savedInstanceState == null) {
            // 避免重复添加Fragment
            if (getSupportFragmentManager().getFragments() == null) {
                BaseFragment firstFragment = getFirstFragment();
                if (firstFragment != null) {
                    addFragment(firstFragment);
                }
            }
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_test_base;
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.fragment_content;
    }
}
