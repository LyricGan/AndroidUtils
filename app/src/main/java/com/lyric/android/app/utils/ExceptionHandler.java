package com.lyric.android.app.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Properties;
import java.util.TreeSet;

/**
 * 异常处理工具类
 * @author lyricgan
 * @time 2016/8/30 17:29
 */
public class ExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = ExceptionHandler.class.getSimpleName();
    /** 错误报告文件的扩展名 */
    private static final String CRASH_REPORTER_EXTENSION = ".cr";
    private static final String VERSION_NAME = "version_name";
    private static final String VERSION_CODE = "version_code";
    private static final String STACK_TRACE = "stack_trace";

    private Context mContext;
    /** 异常捕获处理对象 */
    private Thread.UncaughtExceptionHandler mHandler;

    // 使用Properties来保存设备的信息和错误堆栈信息
    private Properties mCrashProperties = new Properties();

    public ExceptionHandler(Context context, Thread.UncaughtExceptionHandler handler) {
        if (handler == null) {
            handler = Thread.getDefaultUncaughtExceptionHandler();
        }
        this.mContext = context.getApplicationContext();
        this.mHandler = handler;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (ex == null) {
            return;
        }
        if (handleException(ex, mContext)) {
            ex.printStackTrace();
        } else {
            if (mHandler != null) {
                mHandler.uncaughtException(thread, ex);
            }
        }
    }

    /**
     * 处理异常，收集错误信息并发送错误报告
     * @param ex 异常信息
     * @param context 上下文
     * @return true or false
     */
    private boolean handleException(Throwable ex, Context context) {
        String localizedMessage = ex.getLocalizedMessage();
        String stackTraceMessages = getStackTraceMessage();
        Log.w(TAG, "localizedMessage:" + localizedMessage + ",stackTraceMessages:" + stackTraceMessages);

        collectCrashDeviceInfo(context);
        // 保存错误报告文件
        String fileName = saveCrashFile(context, ex);
        if (!TextUtils.isEmpty(fileName)) {
            sendCrashReportFiles(context);
            return true;
        }
        return false;
    }

    /**
     * 获取堆栈信息
     * @return 获取异常日志堆栈信息
     */
    private String getStackTraceMessage() {
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
            return "[ " + Thread.currentThread().getName() + ": "
                    + stackTraceElement.getFileName() + ":"
                    + stackTraceElement.getLineNumber() + " "
                    + stackTraceElement.getMethodName() + " ]";
        }
        return null;
    }

    /**
     * 收集程序崩溃的设备信息
     * @param context 应用上下文
     */
    private void collectCrashDeviceInfo(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (packageInfo != null) {
                mCrashProperties.put(VERSION_NAME, packageInfo.versionName == null ? "not set" : packageInfo.versionName);
                mCrashProperties.put(VERSION_CODE, packageInfo.versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        // 使用反射来收集设备信息
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                mCrashProperties.put(field.getName(), field.get(null));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 保存错误信息到文件中
     * @param context 上下文
     * @param ex 异常信息
     * @return 异常收集文件名称
     */
    private String saveCrashFile(Context context, Throwable ex) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);

        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        String result = writer.toString();
        printWriter.close();
        mCrashProperties.put(STACK_TRACE, result);
        // 设置文件名称
        String fileName = "crash-" + System.currentTimeMillis() + CRASH_REPORTER_EXTENSION;
        FileOutputStream traceStream = null;
        try {
            traceStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            mCrashProperties.store(traceStream, "trace");
            traceStream.flush();

            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (traceStream != null) {
                try {
                    traceStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 获取错误报告文件名
     * @param context 应用上下文
     * @return 异常文件目录
     */
    private String[] getCrashReportFiles(Context context) {
        File filesDir = context.getFilesDir();
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(CRASH_REPORTER_EXTENSION);
            }
        };
        return filesDir.list(filter);
    }

    /**
     * 上传错误报告文件
     * @param context 应用上下文
     */
    private void sendCrashReportFiles(Context context) {
        String[] crashReportFiles = getCrashReportFiles(context);
        if (crashReportFiles != null && crashReportFiles.length > 0) {
            TreeSet<String> sortedFiles = new TreeSet<>();
            sortedFiles.addAll(Arrays.asList(crashReportFiles));
            for (String fileName : sortedFiles) {
                File crashFile = new File(context.getFilesDir(), fileName);
                sendCrashReportFile(crashFile);
            }
        }
    }

    /**
     * 发送错误报告文件
     * @param file 错误收集文件
     */
    public void sendCrashReportFile(File file) {
        if (file == null || !file.exists()) {
            return;
        }
        Log.d(TAG, "file path:" + file.getPath());
    }
}