package com.lyric.android.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.lyric.android.library.utils.ActivityStackUtils;

public abstract class BaseActivity extends Activity implements OnClickListener, IBaseListener {
    private boolean mDestroy = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ActivityStackUtils.init().addActivity(this);
		
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
        super.onResume();
        mDestroy = false;
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
        super.onDestroy();
        mDestroy = true;
    }

    protected boolean isDestroy() {
        return mDestroy;
    }
}
