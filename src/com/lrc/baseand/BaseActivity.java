package com.lrc.baseand;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;

import com.lrc.baseand.constants.AppConstants;
import com.lrc.baseand.utils.ActivityUtils;
import com.lrc.baseand.utils.LogUtils;
import com.lrc.baseand.utils.ToastUtils;

/**
 * Activity基类，继承自 {@link Activity}，实现 {@link OnClickListener} 和 {@link AppConstants} 接口
 * 
 * @author ganyu
 * @created 2015-4-20
 * 
 */
public abstract class BaseActivity extends Activity implements OnClickListener, IBaseListener, AppConstants {
	private static final String TAG = BaseActivity.class.getSimpleName();

	/**
	 * 初始化布局界面
	 * @param savedInstanceState
	 */
	@Override
	public abstract void onInitView(Bundle savedInstanceState);

	/**
	 * 组件点击事件处理
	 * @param v
	 */
	@Override
	public void onWidgetClick(View v) {
	}
	
	/**
	 * 响应网络请求
	 * @param flag 请求标识
	 * @param response 响应字符串
	 */
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
	
	/**
	 * 打印调试日志信息
	 * @param tag
	 * @param message
	 */
	protected void debug(String tag, String message) {
		LogUtils.d(tag, message);
	}
	
	/**
	 * 打印异常日志信息
	 * @param tag
	 * @param message
	 */
	protected void error(String tag, String message) {
		LogUtils.e(tag, message);
	}
	
	/**
	 * Toast提示
	 * @param messageId
	 */
	protected void showShortToast(int messageId) {
		ToastUtils.showShort(this, messageId);
	}
	
	/**
	 * Toast提示
	 * @param message
	 */
	protected void showShortToast(String message) {
		ToastUtils.showShort(this, message);
	}
	
	/**
	 * Toast提示
	 * @param messageId
	 */
	protected void showLongToast(int messageId) {
		ToastUtils.showLong(this, messageId);
	}

	/**
	 * Toast提示
	 * @param message
	 */
	protected void showLongToast(String message) {
		ToastUtils.showLong(this, message);
	}
	
	/**
	 * 跳转Activity
	 * @param cls
	 */
	protected void openActivity(Class<?> cls) {
		openActivity(cls, null);
	}

	/**
	 * 跳转Activity
	 * @param cls
	 * @param bundle
	 */
	protected void openActivity(Class<?> cls, Bundle bundle) {
		Intent intent = new Intent(this, cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
	}

	/**
	 * 跳转Activity
	 * @param action
	 */
	protected void openActivity(String action) {
		Intent intent = new Intent(action);
		startActivity(intent);
	}
	
	/**
	 * 跳转Activity
	 * @param action
	 * @param uri
	 */
	protected void openActivity(String action, Uri uri) {
		Intent intent = new Intent(action, uri);
		startActivity(intent);
	}
	
	/**
	 * 跳转Activity
	 * @param action
	 * @param bundle
	 */
	protected void openActivity(String action, Bundle bundle) {
		Intent intent = new Intent(action);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
	}
	
	/**
	 * 跳转Activity
	 * @param cls
	 * @param bundle
	 * @param requestCode
	 */
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
	
	/**
	 * 跳转startActivityForResult
	 * @param cls
	 * @param requestCode
	 */
	protected void openActivityForResult(Class<?> cls, int requestCode) {
        openActivity(cls, null, requestCode);
    }
	
	/**
	 * 跳转startActivityForResult
	 * @param cls
	 * @param bundle
	 * @param requestCode
	 */
	protected void openActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
		openActivity(cls, bundle, requestCode);
	}

	/**
	 * 隐藏键盘
	 * @param view
	 */
	protected void hideKeyboard(View view) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	protected WeakHandler mBaseHandler = new WeakHandler(this) {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.arg1 == ResponseCode.SUCCESS) {
				onResponse(msg.what, String.valueOf(msg.obj));
			} else {
				String errorString = String.valueOf(msg.obj);
				LogUtils.e(TAG, "errorString:" + errorString);
				onResponse(msg.what, "");
			}
		}
	};
	
	/**
	 * 自定义Handler，继承自 {@link Handler}
	 */
	static class WeakHandler extends Handler {
		WeakReference<Object> mRefObject;
		
		public WeakHandler(Object obj) {
			mRefObject = new WeakReference<Object>(obj);
		}
	}
	
}
