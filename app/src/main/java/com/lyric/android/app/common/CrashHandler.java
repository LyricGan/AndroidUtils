package com.lyric.android.app.common;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;

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
 * crash handler
 *
 * @author Lyric Gan
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String CRASH_REPORTER_EXTENSION = ".cr";
    private static final String VERSION_NAME = "version_name";
    private static final String VERSION_CODE = "version_code";
    private static final String STACK_TRACE = "stack_trace";

    private Context mContext;
    private Thread.UncaughtExceptionHandler mHandler;

    private Properties mCrashProperties = new Properties();

    private CrashHandler(Context context) {
        this.mContext = context.getApplicationContext();
        this.mHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    public static void init(Context context) {
        CrashHandler handler = new CrashHandler(context);
        Thread.setDefaultUncaughtExceptionHandler(handler);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Context context = mContext;
        collectCrashDeviceInfo(context);

        String fileName = saveCrashFile(context, ex);
        if (!TextUtils.isEmpty(fileName)) {
            sendCrashReportFiles(context);
            return;
        }

        if (mHandler != null) {
            mHandler.uncaughtException(thread, ex);
        }
    }

    private void collectCrashDeviceInfo(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (packageInfo != null) {
                mCrashProperties.put(VERSION_NAME, packageInfo.versionName);
                mCrashProperties.put(VERSION_CODE, packageInfo.versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
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

    private void sendCrashReportFiles(Context context) {
        String[] crashReportFiles = getCrashReportFiles(context);
        if (crashReportFiles != null && crashReportFiles.length > 0) {
            TreeSet<String> sortedFiles = new TreeSet<>(Arrays.asList(crashReportFiles));
            for (String fileName : sortedFiles) {
                File crashFile = new File(context.getFilesDir(), fileName);
                sendCrashReportFile(crashFile);
            }
        }
    }

    public void sendCrashReportFile(File file) {

    }
}