package com.lyric.android.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.lyric.android.app.BaseFragment;
import com.lyric.android.app.R;
import com.lyric.android.app.widget.TitleBar;

/**
 * 基类，用于加载fragment
 * @author lyric
 * @time 2016/5/27 15:37
 */
public class BaseFragmentActivity extends BaseCompatActivity {
    private static final String EXTRA_FRAGMENT_NAME = "fragment_name";
    private Fragment mFragment;

    public static Intent newIntent(Context context, Class<?> fragmentClass) {
        Intent intent = new Intent(context, BaseFragmentActivity.class);
        intent.putExtra(EXTRA_FRAGMENT_NAME, fragmentClass.getName());
        return intent;
    }

    @Override
    public void onViewCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_base_fragment);
        setupViews();
    }

    private void setupViews() {
        Bundle bundle = getIntent().getExtras();
        String fragmentName = bundle.getString(EXTRA_FRAGMENT_NAME);
        mFragment = Fragment.instantiate(this, fragmentName, bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_content, mFragment, fragmentName).commit();
    }

    @Override
    protected void onTitleCreated(TitleBar titleBar) {
    }

    @Override
    public void setTitle(int titleId) {
        if (getTitleBar() != null) {
            getTitleBar().setText(titleId);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        if (getTitleBar() != null) {
            getTitleBar().setText(title);
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