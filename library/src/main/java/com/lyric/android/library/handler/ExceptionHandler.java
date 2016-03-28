package com.lyric.android.library.handler;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Looper;
import android.text.TextUtils;

import com.lyric.android.library.utils.LogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Properties;
import java.util.TreeSet;

/**
 * @author ganyu
 * @description 异常处理类
 * @time 16/3/6 下午9:40
 */
public class ExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = ExceptionHandler.class.getSimpleName();
    /** 系统默认的UncaughtException处理类 */
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    /** ExceptionHandler实例 */
    private static ExceptionHandler mInstance;
    /** 程序的Context对象 */
    private Context mContext;

    // 使用Properties来保存设备的信息和错误堆栈信息
    private Properties mCrashProperties = new Properties();
    private static final String VERSION_NAME = "version_name";
    private static final String VERSION_CODE = "version_code";
    private static final String STACK_TRACE = "stack_trace";
    /** 错误报告文件的扩展名 */
    private static final String CRASH_REPORTER_EXTENSION = ".cr";

    private ExceptionHandler() {
    }

    public static synchronized ExceptionHandler getInstance() {
        if (mInstance == null) {
            mInstance = new ExceptionHandler();
        }
        return mInstance;
    }

    /**
     * 初始化，获取系统默认的异常处理器，设置ExceptionHandler为程序的默认处理器
     * @param context 应用上下文
     */
    public void initialize(Context context) {
        mContext = context.getApplicationContext();
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (ex != null && handleException(ex)) {
            ex.printStackTrace();
        } else {
            mDefaultHandler.uncaughtException(thread, ex);
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finishException();
    }

    /**
     * 处理异常，收集错误信息并发送错误报告
     * @param ex 异常信息
     * @return true:如果处理了该异常信息;否则返回false
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return true;
        }
        final String msg = ex.getLocalizedMessage();
        final String name = getStackTraceMessage();
        // 打印异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                if (TextUtils.isEmpty(name)) {
                    LogUtils.e(TAG, "handleException:" + msg);
                } else {
                    LogUtils.e(TAG, "handleException:" + name + "-" + msg);
                }
                Looper.loop();
            }
        }.start();
        // 收集设备信息
        collectCrashDeviceInfo(mContext);
        // 保存错误报告文件
        saveCrashInfoToFile(ex);
        // 发送错误报告到服务器
        sendCrashReportFiles(mContext);

        return true;
    }

    private void finishException() {
        System.exit(0);
    }

    /**
     * 获取堆栈信息
     * @return 获取异常日志堆栈信息
     */
    public String getStackTraceMessage() {
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
     * 上传错误报告文件
     * @param context 应用上下文
     */
    public void sendCrashReportFiles(Context context) {
        String[] crFiles = getCrashReportFiles(context);
        if (crFiles != null && crFiles.length > 0) {
            TreeSet<String> sortedFiles = new TreeSet<>();
            sortedFiles.addAll(Arrays.asList(crFiles));
            for (String fileName : sortedFiles) {
                File crFile = new File(context.getFilesDir(), fileName);
                sendCrashReportFile(crFile);
            }
        }
    }

    /**
     * 发送错误报告到服务器
     * @param file 错误收集文件
     */
    private void sendCrashReportFile(File file) {
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
     * 保存错误信息到文件中
     * @param ex 异常信息
     * @return 异常收集文件名称
     */
    private String saveCrashInfoToFile(Throwable ex) {
        Writer info = new StringWriter();
        PrintWriter printWriter = new PrintWriter(info);
        ex.printStackTrace(printWriter);

        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        String result = info.toString();
        printWriter.close();
        mCrashProperties.put(STACK_TRACE, result);
        try {
            long timestamp = System.currentTimeMillis();
            String fileName = "crash-" + timestamp + CRASH_REPORTER_EXTENSION;
            FileOutputStream trace = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);
            mCrashProperties.store(trace, "trace");
            trace.flush();
            trace.close();

            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 收集程序崩溃的设备信息
     * @param context 应用上下文
     */
    public void collectCrashDeviceInfo(Context context) {
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

}
