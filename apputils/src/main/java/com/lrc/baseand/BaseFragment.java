package com.lrc.baseand;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;

import com.lrc.baseand.BaseActivity.WeakHandler;
import com.lrc.baseand.constants.AppConstants;
import com.lrc.baseand.utils.LogUtils;
import com.lrc.baseand.utils.ToastUtils;

/**
 * Fragment基类，继承自 {@link Fragment}， 实现 {@link AppConstants} 和 {@link OnClickListener} 接口
 * 
 * @author ganyu
 * @created 2015-4-20
 * 
 */
public abstract class BaseFragment extends Fragment implements AppConstants, OnClickListener {
	private static final String TAG = BaseFragment.class.getSimpleName();
	protected Context mContext;

	/**
	 * 初始化布局界面
	 * @param savedInstanceState
	 */
	public abstract void onInitView(Bundle savedInstanceState);

	/**
	 * 组件点击事件处理
	 * @param v
	 */
	public abstract void onWidgetClick(View v);
	
	/**
	 * 响应网络请求
	 * @param flag 请求标识
	 * @param response 响应字符串
	 */
	public abstract void onResponse(int flag, String response);
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		// 初始化界面
		onInitView(savedInstanceState);
	}
	
	@Override
	public void onClick(View v) {
		onWidgetClick(v);
	}
	
	/**
	 * Toast提示
	 * @param messageId
	 */
	protected void showShortToast(int messageId) {
		ToastUtils.showShort(mContext, messageId);
	}
	
	/**
	 * Toast提示
	 * @param message
	 */
	protected void showShortToast(String message) {
		ToastUtils.showShort(mContext, message);
	}
	
	/**
	 * Toast提示
	 * @param messageId
	 */
	protected void showLongToast(int messageId) {
		ToastUtils.showLong(mContext, messageId);
	}

	/**
	 * Toast提示
	 * @param message
	 */
	protected void showLongToast(String message) {
		ToastUtils.showLong(mContext, message);
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
		Intent intent = new Intent(mContext, cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		mContext.startActivity(intent);
	}

	/**
	 * 跳转Activity
	 * @param action
	 */
	protected void openActivity(String action) {
		Intent intent = new Intent(action);
		mContext.startActivity(intent);
	}
	
	/**
	 * 跳转Activity
	 * @param action
	 * @param uri
	 */
	protected void openActivity(String action, Uri uri) {
		Intent intent = new Intent(action, uri);
		mContext.startActivity(intent);
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
		mContext.startActivity(intent);
	}
	
	/**
	 * 跳转Activity
	 * @param cls
	 * @param bundle
	 * @param requestCode
	 */
	protected void openActivity(Class<?> cls, Bundle bundle, int requestCode) {
		Intent intent = new Intent(mContext, cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		if (requestCode == 0) {
			mContext.startActivity(intent);
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
		InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	/**
	 * 消息处理
	 */
	protected WeakHandler mBaseHandler = new WeakHandler(this) {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.arg1 == ResponseCode.SUCCESS) {
				onResponse(msg.what, String.valueOf(msg.obj));
			} else {
				String errorString = String.valueOf(msg.obj);
				LogUtils.d(TAG, "errorString:" + errorString);
				onResponse(msg.what, "");
			}
		}
	};
	
}
