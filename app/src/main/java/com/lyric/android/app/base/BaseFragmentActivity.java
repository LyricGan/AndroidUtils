package com.lyric.android.app.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.lyric.android.app.R;

/**
 * @author lyric
 * @description
 * @time 2016/5/27 15:37
 */
public class BaseFragmentActivity extends BaseActivity {
    private static final String EXTRA_FRAGMENT_NAME = "fragment_name";

    public static Intent newIntent(Context context, String fragmentName) {
        Intent intent = new Intent(context, BaseFragmentActivity.class);
        intent.putExtra(EXTRA_FRAGMENT_NAME, fragmentName);
        return intent;
    }

    @Override
    public void onViewCreated(Bundle savedInstanceState) {
        setContentView(R.layout.activity_base_fragment);
        setupViews();
    }

    private void setupViews() {
        Bundle bundle = getIntent().getExtras();
        String fragmentName = bundle.getString(EXTRA_FRAGMENT_NAME);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_content, Fragment.instantiate(this, fragmentName, bundle), fragmentName).commit();
    }
}