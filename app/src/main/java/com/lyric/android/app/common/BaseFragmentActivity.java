package com.lyric.android.app.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.lyric.android.app.R;

/**
 * 基类Activity，用于加载fragment
 * @author lyricgan
 * @time 2016/5/27 15:37
 */
public class BaseFragmentActivity extends BaseActivity {
    private static final String EXTRA_FRAGMENT_NAME = "fragment_name";
    private Fragment mFragment;

    public static Intent newIntent(Context context, Class<?> fragmentClass) {
        Intent intent = new Intent(context, BaseFragmentActivity.class);
        intent.putExtra(EXTRA_FRAGMENT_NAME, fragmentClass.getName());
        return intent;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_base_fragment;
    }

    @Override
    public void onContentViewInitialize(View view, Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String fragmentName = bundle.getString(EXTRA_FRAGMENT_NAME);
            Fragment fragment = Fragment.instantiate(this, fragmentName, bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_content, fragment, fragmentName)
                    .commit();
            mFragment = fragment;
        }
    }

    @Override
    public void onBackPressed() {
        if (mFragment != null && mFragment instanceof BaseFragment) {
            if (((BaseFragment) mFragment).onBackPressed()) {
                return;
            }
        }
        super.onBackPressed();
    }
}