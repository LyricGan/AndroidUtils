package com.lyric.utils;

import android.app.Activity;

import java.util.Stack;

/**
 * Activity管理类：用于Activity管理和应用程序退出
 * 
 * @author lyric
 * @created 2014-8-6
 * 
 */
public class ActivityStackUtils {
	private static Stack<Activity> mActivityStack;
	private static final ActivityStackUtils mInstance = new ActivityStackUtils();

	private ActivityStackUtils() {
	}

	public static ActivityStackUtils getInstance() {
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

    public void remove(Activity activity) {
        if (mActivityStack != null) {
            mActivityStack.remove(activity);
        }
    }

	/**
	 * 获取当前Activity（栈顶Activity）
	 */
	public Activity getCurrentActivity() {
		if (mActivityStack == null || mActivityStack.isEmpty()) {
			return null;
		}
		return mActivityStack.lastElement();
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
	 * @param cls Class
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
}
