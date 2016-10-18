package com.lyric.android.app.test;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.textservice.TextServicesManager;

import com.lyric.android.app.base.BaseApp;
import com.lyric.android.app.utils.Preferences;
import com.lyric.android.library.logger.Loggers;
import com.lyric.android.library.utils.FileUtils;

import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import dalvik.system.DexClassLoader;
import rx.android.plugins.RxAndroidSchedulersHook;

/**
 * @author lyricgan
 * @description
 * @time 2016/7/25 13:51
 */
public class Test {
    private ArrayList<byte[]> mLeakyContainer = new ArrayList<>();

    private Test() {
    }

    private static final class TestHolder {
        public static final Test INSTANCE = new Test();
    }

    public static Test getInstance() {
        return TestHolder.INSTANCE;
    }

    public void execute() {
        ExecutorsTest executorsTest = new ExecutorsTest();
        executorsTest.start();
    }

    public void registerBroadcast(Context context, BroadcastReceiver receiver) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(IntentFlags.ACTION_TEST);
        filter.addAction(IntentFlags.ACTION_TEST_DEFAULT);
        context.registerReceiver(receiver, filter);
    }

    public void unregisterBroadcast(Context context, BroadcastReceiver receiver) {
        context.unregisterReceiver(receiver);
    }

    public void sendBroadcast(Context context) {
        Intent intent = new Intent(IntentFlags.ACTION_TEST_DEFAULT);
        context.sendBroadcast(intent);
    }

    public void printMemoryInfo(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        int memory = activityManager.getMemoryClass();
        int largeMemory = activityManager.getLargeMemoryClass();
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(info);

        Loggers.e("memory:" + memory + ",largeMemory:" + largeMemory);
    }

    public void newLeaky() {
        byte[] bytes = new byte[100 * 1024 * 1024];
        mLeakyContainer.add(bytes);
    }

    /**
     * 获取当前应用APK路径
     * @param context 上下文
     * @return 当前应用APK路径
     */
    public String extract(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        return applicationInfo.sourceDir;
    }

    public void initialize() {
        Loggers.init().setMethodCount(2).setLevelFull();
        Loggers.e("sourceDir:" + extract(BaseApp.getContext()));

        File bitmapFile = FileUtils.getCacheDir(BaseApp.getContext(), "bitmap");
        if (bitmapFile != null) {
            Loggers.e("file path:" + bitmapFile.getPath());
        }
        File file = FileUtils.getCacheDir(BaseApp.getContext(), "file");
        if (file != null) {
            Loggers.e("file path:" + file.getPath());
        }
    }

    public void test() {
        Iterator iterator;
        Stack stack;

        Preferences.getInstance().clear();

        Service service;
        TextServicesManager textServicesManager;
        ActivityManager activityManager;
        PackageManager packageManager;
        Binder binder;
        ClassLoader classLoader;
        DexClassLoader dexClassLoader;
        Handler handler;
        Message message;
        Looper looper;
        Context context;
        RxAndroidSchedulersHook rxAndroidSchedulersHook;

        InvocationHandler invocationHandler;
        Proxy proxy;

        View view;
    }
}
