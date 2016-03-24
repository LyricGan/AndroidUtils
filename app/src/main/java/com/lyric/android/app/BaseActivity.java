package com.lyric.android.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

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
}
