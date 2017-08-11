package com.lyric.utils;

import android.util.Log;

import java.util.Hashtable;

/**
 * @author ganyu
 * @description log utils
 * @time 2016/1/19 18:13
 */
public class LogUtils {
    private static final String TAG = LogUtils.class.getSimpleName();
    private static boolean sDebug = false;
    private static int sLevel = Log.VERBOSE;
    private String mClassName;
    private static Hashtable<String, LogUtils> mLoggerTable = new Hashtable<>();

    private LogUtils(String name) {
        this.mClassName = name;
    }

    private static LogUtils getInstance(String className) {
        LogUtils classLogger = mLoggerTable.get(className);
        if (classLogger == null) {
            classLogger = new LogUtils(className);
            mLoggerTable.put(className, classLogger);
        }
        return classLogger;
    }

    public static void setDebug(boolean flag) {
        sDebug = flag;
    }

    public static void setLevel(int level) {
        sLevel = level;
    }

    public static void i(String className, Object obj) {
        getInstance(className).i(obj);
    }

    public static void d(String className, Object obj) {
        getInstance(className).d(obj);
    }

    public static void w(String className, Object obj) {
        getInstance(className).w(obj);
    }

    public static void e(String className, Object obj) {
        getInstance(className).e(obj);
    }

    public static void e(String className, Exception exception) {
        getInstance(className).e(exception);
    }

    public static void e(String className, String log, Throwable tr) {
        getInstance(className).e(log, tr);
    }

    public static void v(String className, Object obj) {
        getInstance(className).v(obj);
    }

    private void i(Object obj) {
        if (sDebug && sLevel <= Log.INFO) {
            String stackString = buildStackTrace();
            if (stackString != null) {
                Log.i(TAG, stackString + obj);
            } else {
                Log.i(TAG, obj.toString());
            }
        }
    }

    private void d(Object obj) {
        if (sDebug && sLevel <= Log.DEBUG) {
            String stackString = buildStackTrace();
            if (stackString != null) {
                Log.d(TAG, stackString + obj);
            } else {
                Log.d(TAG, obj.toString());
            }
        }
    }

    private void v(Object obj) {
        if (sDebug && sLevel <= Log.VERBOSE) {
            String stackString = buildStackTrace();
            if (stackString != null) {
                Log.v(TAG, stackString + obj);
            } else {
                Log.v(TAG, obj.toString());
            }
        }
    }

    private void w(Object obj) {
        if (sDebug && sLevel <= Log.WARN) {
            String stackString = buildStackTrace();
            if (stackString != null) {
                Log.w(TAG, stackString + obj);
            } else {
                Log.w(TAG, obj.toString());
            }
        }
    }

    private void e(Object obj) {
        if (sDebug && sLevel <= Log.ERROR) {
            String stackString = buildStackTrace();
            if (stackString != null) {
                Log.e(TAG, stackString + obj);
            } else {
                Log.e(TAG, obj.toString());
            }
        }
    }

    private void e(Exception e) {
        if (sDebug && sLevel <= Log.ERROR) {
            String stackString = buildStackTrace();
            if (stackString != null) {
                Log.e(TAG, stackString + e);
            } else {
                Log.e(TAG, "error", e);
            }
        }
    }

    private void e(String log, Throwable tr) {
        if (sDebug && sLevel <= Log.ERROR) {
            String stackString = buildStackTrace();
            if (stackString != null) {
                Log.e(TAG, stackString + log);
            } else {
                Log.e(TAG, "error", tr);
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
