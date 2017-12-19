package com.lyric.android.app.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lyric.android.app.R;

/**
 * 基类Activity，用于加载fragment
 * @author lyricgan
 * @time 2016/5/27 15:37
 */
public class BaseFragmentActivity extends BaseActivity {
    private static final String EXTRA_FRAGMENT_NAME = "fragment_name";
    private static final String EXTRA_ADD_BACK_STACK = "_add_to_back_stack";
    private static final String EXTRA_NAME = "_name";
    private BaseFragment mFragment;

    public static Intent newIntent(Context context, Class<?> fragmentClass, boolean isAddToBackStack, String name) {
        Intent intent = new Intent(context, BaseFragmentActivity.class);
        intent.putExtra(EXTRA_FRAGMENT_NAME, fragmentClass.getName());
        intent.putExtra(EXTRA_ADD_BACK_STACK, isAddToBackStack);
        intent.putExtra(EXTRA_NAME, name);
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
            boolean isAddToBackStack = bundle.getBoolean(EXTRA_ADD_BACK_STACK);
            String name = bundle.getString(EXTRA_NAME);
            BaseFragment fragment = getFragment(this, fragmentName, bundle);
            if (fragment != null) {
                addFragment(R.id.fragment_content, fragment, fragmentName, isAddToBackStack, name);
            }
            mFragment = fragment;
        }
    }

    @Override
    public void onBackPressed() {
        if (mFragment != null && mFragment.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }
}