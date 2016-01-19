package com.lyric.android.library.utils;

import android.util.Log;

import java.util.Hashtable;

/**
 * 日志工具类<br />
 * 格式: TAG:className.methodName(Line:lineNumber), TAG为空时只输出：className.methodName(Line:lineNumber)。 
 * 
 * @author ganyu
 * @created 2014-3-4
 * 
 */
public class LogUtils extends CommonUtils  {
	private static final String TAG = LogUtils.class.getSimpleName();
	/** 是否打印日志 */
	private static boolean DEBUG = false;
	/** 日志等级 */
	private static int sLevel = Log.VERBOSE;
	/** 类名称 */
	private String mClassName;
	private static Hashtable<String, LogUtils> mLoggerTable = new Hashtable<String, LogUtils>();

	private LogUtils(String name) {
		this.mClassName = name;
	}
	
	private static LogUtils getInstance(String className) {
		LogUtils classLogger = (LogUtils) mLoggerTable.get(className);
		if (classLogger == null) {
			classLogger = new LogUtils(className);
			mLoggerTable.put(className, classLogger);
		}
		return classLogger;
	}
	
	/**
	 * 设置是否调试
	 * @param flag 是否打印日志标识
	 */
	public static void setDebug(boolean flag) {
		DEBUG = flag;
	}
	
	/**
	 * 设置调试等级
	 * @param level 调试等级
	 */
	public static void setLevel(int level) {
		sLevel = level;
	}
	
	/**
	 * INFO
	 * @param className
	 * @param obj
	 */
	public static void i(String className, Object obj) {
		LogUtils.getInstance(className).i(obj);
	}
	
	/**
	 * DEBUG
	 * @param className
	 * @param obj
	 */
	public static void d(String className, Object obj) {
		LogUtils.getInstance(className).d(obj);
	}
	
	/**
	 * WARN
	 * @param className
	 * @param obj
	 */
	public static void w(String className, Object obj) {
		LogUtils.getInstance(className).w(obj);
	}
	
	/**
	 * ERROR
	 * @param className
	 * @param obj
	 */
	public static void e(String className, Object obj) {
		LogUtils.getInstance(className).e(obj);
	}
	
	/**
	 * ERROR
	 * @param className
	 * @param exception
	 */
	public static void e(String className, Exception exception) {
		LogUtils.getInstance(className).e(exception);
	}
	
	/**
	 * ERROR
	 * @param className
	 * @param log
	 * @param tr
	 */
	public static void e(String className, String log, Throwable tr) {
		LogUtils.getInstance(className).e(log, tr);
	}
	
	/**
	 * VERBOSE
	 * @param className
	 * @param obj
	 */
	public static void v(String className, Object obj) {
		LogUtils.getInstance(className).v(obj);
	}

	/**
	 * INFO
	 * @param obj
	 */
	private void i(Object obj) {
		if (DEBUG) {
			if (sLevel <= Log.INFO) {
				String stackString = buildStackTrace();
				if (stackString != null) {
					Log.i(TAG, stackString + obj);
				} else {
					Log.i(TAG, obj.toString());
				}
			}
		}
	}

	/**
	 * DEBUG
	 * @param obj
	 */
	private void d(Object obj) {
		if (DEBUG) {
			if (sLevel <= Log.DEBUG) {
				String stackString = buildStackTrace();
				if (stackString != null) {
					Log.d(TAG, stackString + obj);
				} else {
					Log.d(TAG, obj.toString());
				}
			}
		}
	}

	/**
	 * VERBOSE
	 * @param obj
	 */
	private void v(Object obj) {
		if (DEBUG) {
			if (sLevel <= Log.VERBOSE) {
				String stackString = buildStackTrace();
				if (stackString != null) {
					Log.v(TAG, stackString + obj);
				} else {
					Log.v(TAG, obj.toString());
				}
			}
		}
	}

	/**
	 * WARN
	 * @param obj
	 */
	private void w(Object obj) {
		if (DEBUG) {
			if (sLevel <= Log.WARN) {
				String stackString = buildStackTrace();
				if (stackString != null) {
					Log.w(TAG, stackString + obj);
				} else {
					Log.w(TAG, obj.toString());
				}
			}
		}
	}

	/**
	 * ERROR
	 * @param obj
	 */
	private void e(Object obj) {
		if (DEBUG) {
			if (sLevel <= Log.ERROR) {
				String stackString = buildStackTrace();
				if (stackString != null) {
					Log.e(TAG, stackString + obj);
				} else {
					Log.e(TAG, obj.toString());
				}
			}
		}
	}

	/**
	 * ERROR
	 * @param e
	 */
	private void e(Exception e) {
		if (DEBUG) {
			if (sLevel <= Log.ERROR) {
				String stackString = buildStackTrace();
				if (stackString != null) {
					Log.e(TAG, stackString + e);
				} else {
					Log.e(TAG, "error", e);
				}
			}
		}
	}

	/**
	 * ERROR
	 * @param log
	 * @param tr
	 */
	private void e(String log, Throwable tr) {
		if (DEBUG) {
			if (sLevel <= Log.ERROR) {
				String stackString = buildStackTrace();
				if (stackString != null) {
					Log.e(TAG, stackString + log);
				} else {
					Log.e(TAG, "error", tr);
				}
			}
		}
	}
	
	private String buildStackTrace() {
		StackTraceElement[] stackTraceElementArray = Thread.currentThread().getStackTrace();
		if (stackTraceElementArray == null) {
			return null;
		}
		for (StackTraceElement stackTraceElement : stackTraceElementArray) {
			if (stackTraceElement.isNativeMethod()) {
				continue;
			}
			if (stackTraceElement.getClassName().equals(Thread.class.getName())) {
				continue;
			}
			if (stackTraceElement.getClassName().equals(this.getClass().getName())) {
				continue;
			}
			return mClassName + "." + stackTraceElement.getMethodName() + "()"
					+ "[Line:" + stackTraceElement.getLineNumber() + "]";
		}
		return null;
	}
	
}
