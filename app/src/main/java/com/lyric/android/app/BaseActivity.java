package com.lyric.android.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.lyric.android.library.utils.ActivityUtils;
import com.lyric.android.library.utils.ToastUtils;


public abstract class BaseActivity extends Activity implements OnClickListener, IBaseListener {

	@Override
	public abstract void onInitView(Bundle savedInstanceState);

	@Override
	public void onWidgetClick(View v) {
	}
	
	@Override
	public void onResponse(int flag, String response) {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ActivityUtils.init().addActivity(this);
		
		onInitView(savedInstanceState);
	}
	
	@Override
	public void onClick(View v) {
		onWidgetClick(v);
	}
	
	protected void showToast(int messageId) {
		ToastUtils.showShort(this, messageId);
	}
	
	protected void showToast(String message) {
		ToastUtils.showShort(this, message);
	}
	
	protected void openActivity(Class<?> cls) {
		openActivity(cls, null);
	}

	protected void openActivity(Class<?> cls, Bundle bundle) {
		Intent intent = new Intent(this, cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
	}

	protected void openActivity(String action) {
		Intent intent = new Intent(action);
		startActivity(intent);
	}
	
	protected void openActivity(String action, Uri uri) {
		Intent intent = new Intent(action, uri);
		startActivity(intent);
	}
	
	protected void openActivity(String action, Bundle bundle) {
		Intent intent = new Intent(action);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
	}

    protected void openActivityForResult(Class<?> cls, int requestCode) {
        openActivity(cls, null, requestCode);
    }

    protected void openActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        openActivity(cls, bundle, requestCode);
    }
	
	protected void openActivity(Class<?> cls, Bundle bundle, int requestCode) {
		Intent intent = new Intent(this, cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		if (requestCode == 0) {
			startActivity(intent);
		} else {
			startActivityForResult(intent, requestCode);
		}
	}
}
