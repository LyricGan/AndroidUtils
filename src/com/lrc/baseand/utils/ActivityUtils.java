package com.lrc.baseand.utils;

import java.util.Stack;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

/**
 * Activity管理类：用于Activity管理和应用程序退出
 * 
 * @author ganyu
 * @created 2014-8-6
 * 
 */
public class ActivityUtils {
	private static Stack<Activity> mActivityStack;
	private static ActivityUtils mInstance;

	private ActivityUtils() {
	}

	/**
	 * 单实例 , UI无需考虑多线程同步问题
	 */
	public static ActivityUtils init() {
		if (mInstance == null) {
			mInstance = new ActivityUtils();
		}
		return mInstance;
	}

	/**
	 * 添加Activity到栈
	 */
	public void addActivity(Activity activity) {
		if (mActivityStack == null) {
			mActivityStack = new Stack<Activity>();
		}
		mActivityStack.add(activity);
	}

	/**
	 * 获取当前Activity（栈顶Activity）
	 */
	public Activity getCurrentActivity() {
		if (mActivityStack == null || mActivityStack.isEmpty()) {
			return null;
		}
		Activity activity = mActivityStack.lastElement();
		return activity;
	}

	/**
	 * 获取当前Activity（栈顶Activity） 没有找到则返回null
	 */
	public Activity findActivity(Class<?> cls) {
		Activity activity = null;
		for (Activity aty : mActivityStack) {
			if (aty.getClass().equals(cls)) {
				activity = aty;
				break;
			}
		}
		return activity;
	}

	/**
	 * 结束当前Activity（栈顶Activity）
	 */
	public void finishActivity() {
		Activity activity = mActivityStack.lastElement();
		finishActivity(activity);
	}

	/**
	 * 结束指定的Activity(重载)
	 */
	public void finishActivity(Activity activity) {
		if (activity != null) {
			mActivityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 结束指定的Activity(重载)
	 */
	public void finishActivity(Class<?> cls) {
		for (Activity activity : mActivityStack) {
			if (activity.getClass().equals(cls)) {
				finishActivity(activity);
			}
		}
	}

	/**
	 * 关闭除了指定activity以外的全部activity 如果cls不存在于栈中，则栈全部清空
	 * @param cls
	 */
	public void finishOthersActivity(Class<?> cls) {
		for (Activity activity : mActivityStack) {
			if (!(activity.getClass().equals(cls))) {
				finishActivity(activity);
			}
		}
	}

	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity() {
		for (int i = 0, size = mActivityStack.size(); i < size; i++) {
			if (null != mActivityStack.get(i)) {
				mActivityStack.get(i).finish();
			}
		}
		mActivityStack.clear();
	}

	/**
	 * 退出应用程序
	 * @param context
	 */
	public void exit(Context context) {
		finishAllActivity();
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		activityManager.killBackgroundProcesses(context.getPackageName());
		System.exit(0);
	}
	
}
