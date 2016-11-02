package com.lyric.android.app.test;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Base64;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.textservice.TextServicesManager;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TableLayout;

import com.lyric.android.app.base.BaseApp;
import com.lyric.android.app.utils.Preferences;
import com.lyric.android.library.logger.Loggers;
import com.lyric.android.library.utils.FileUtils;
import com.lyric.android.library.utils.ImageUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;

import dalvik.system.DexClassLoader;

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
        static final Test INSTANCE = new Test();
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

        testEncode();
    }

    public void test() {
        Preferences.getInstance().clear();

        Activity activity;
        Service service;
        BroadcastReceiver broadcastReceiver;
        ContentProvider contentProvider;

        LinearLayout linearLayout;
        RelativeLayout relativeLayout;
        FrameLayout frameLayout;
        TableLayout tableLayout;
        AbsoluteLayout absoluteLayout;

        Intent intent;
        IntentFilter intentFilter;
        TextServicesManager textServicesManager;
        ActivityManager activityManager;
        PackageManager packageManager;
        Binder binder;
        ClassLoader classLoader;
        DexClassLoader dexClasLoader;
        Handler handler;
        Message message;
        Thread thread;
        HandlerThread handlerThread;
        Looper looper;
        Context context;

        InvocationHandler invocationHandler;
        Proxy proxy;

        View view;
        MotionEvent motionEvent;
        VelocityTracker velocityTracker;
        GestureDetector gestureDetector;
        Scroller scroller;
        ViewConfiguration viewConfiguration;

        Iterator iterator;
        Stack stack;

        EnumSet enumSet;
        EnumMap enumMap;

        HashMap hashMap;
        HashSet hashSet;
        Hashtable hashtable;

        TreeSet treeSet;
        TreeMap treeMap;

        ArrayList arrayList;
        LinkedList linkedList;
    }

    public void testEncode() {
        List<String> paths = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            paths.add(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "TestBase64.png");
        }
        String value = encodeBitmap(paths);
        Loggers.e("value:" + value);

        decode(value);
    }

    public String encodeBitmap(List<String> paths) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < paths.size(); i++) {
            String path = paths.get(i);
//            Bitmap bitmap = ImageUtils.getCompressBitmap(path, 300, 300);
            Bitmap bitmap = ImageUtils.decodeBitmap(path);
            try {
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, output);
                byte[] bytes = output.toByteArray();
                String data = Base64.encodeToString(bytes, Base64.DEFAULT);
                if (i == paths.size() - 1) {
                    stringBuffer.append(data);
                } else {
                    stringBuffer.append(data).append(",");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bitmap != null) {
                    bitmap.recycle();
                }
            }
        }
        return stringBuffer.toString();
    }

    public void decode(String bitmapString) {
        byte[] s = Base64.decode(bitmapString, Base64.DEFAULT);
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(filePath, "TestBase64_" + System.currentTimeMillis() + ".png");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
            }
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(s, 0, s.length);
        } catch (FileNotFoundException e) {

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
