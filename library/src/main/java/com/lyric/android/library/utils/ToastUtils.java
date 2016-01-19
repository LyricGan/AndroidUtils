package com.lyric.android.library.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

/**
 * Toast工具类，用来弹出提示
 * 
 * @author ganyu
 * @created 2014-3-4
 * 
 */
public class ToastUtils extends CommonUtils {
	private static Handler mHandler = new Handler(Looper.getMainLooper());
	private static Toast mToast = null;
	private static Object mSynObject = new Object();

	/**
	 * Toast发送消息，默认Toast.LENGTH_SHORT
	 * @param context
	 * @param message
	 */
	public static void showShort(final Context context, final String message) {
		showMessage(context, message, Toast.LENGTH_SHORT);
	}

	/**
	 * Toast发送消息，默认Toast.LENGTH_LONG
	 * @param context
	 * @param message
	 */
	public static void showLong(final Context context, final String message) {
		showMessage(context, message, Toast.LENGTH_LONG);
	}

	/**
	 * Toast发送消息，默认Toast.LENGTH_SHORT
	 * @param context
	 * @param messageId
	 */
	public static void showShort(final Context context, final int messageId) {
		showMessage(context, messageId, Toast.LENGTH_SHORT);
	}

	/**
	 * Toast发送消息，默认Toast.LENGTH_LONG
	 * @param context
	 * @param messageId
	 */
	public static void showLong(final Context context, final int messageId) {
		showMessage(context, messageId, Toast.LENGTH_LONG);
	}

	/**
	 * Toast发送消息
	 * @param context
	 * @param messageId
	 * @param duration
	 */
	private static void showMessage(final Context context, final int messageId, final int duration) {
		new Thread(new Runnable() {
			public void run() {
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						synchronized (mSynObject) {
							if (mToast != null) {
								mToast.setText(messageId);
								mToast.setDuration(duration);
							} else {
								mToast = Toast.makeText(context, messageId, duration);
							}
							mToast.show();
						}
					}
				});
			}
		}).start();
	}

	/**
	 * Toast发送消息
	 * @param context
	 * @param message
	 * @param duration
	 */
	private static void showMessage(final Context context, final String message, final int duration) {
		new Thread(new Runnable() {
			public void run() {
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						synchronized (mSynObject) {
							if (mToast != null) {
								mToast.setText(message);
								mToast.setDuration(duration);
							} else {
								mToast = Toast.makeText(context, message, duration);
							}
							mToast.show();
						}
					}
				});
			}
		}).start();
	}

	/**
	 * 关闭当前Toast
	 */
	public static void cancel() {
		if (mToast != null) {
			mToast.cancel();
		}
	}
	
}
