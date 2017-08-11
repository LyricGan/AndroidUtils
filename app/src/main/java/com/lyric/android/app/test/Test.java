package com.lyric.android.app.test;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Base64;
import android.util.SparseArray;
import android.view.View;

import com.lyric.android.app.BaseApp;
import com.lyric.android.app.test.logger.Loggers;
import com.lyric.utils.FileUtils;
import com.lyric.utils.ImageUtils;
import com.lyric.utils.LogUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author lyricgan
 * @description
 * @time 2016/7/25 13:51
 */
public class Test {
    private static final String TAG = Test.class.getSimpleName();
    private ArrayList<byte[]> mLeakyContainer = new ArrayList<>();

    public interface IntentFlags {
        String ACTION_APP_START = "com.intent.action.APP_START";
        String ACTION_TEST = "com.intent.action.TEST";
        String ACTION_TEST_DEFAULT = "com.intent.action.TEST_DEFAULT";
    }

    private Test() {
    }

    private static final class TestHolder {
        private static final Test INSTANCE = new Test();
    }

    public static Test getInstance() {
        return TestHolder.INSTANCE;
    }

    public void execute() {
        start();
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
        // 每次申请100M内存
        byte[] bytes = new byte[100 * 1024 * 1024];
        mLeakyContainer.add(bytes);
    }

    public boolean isLeakyEmpty() {
        return mLeakyContainer == null || mLeakyContainer.isEmpty();
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
                e.printStackTrace();
            }
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(s, 0, s.length);
        } catch (Exception e) {
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

    public static <T extends View> T get(View convertView, int id) {
        if (convertView == null) {
            return null;
        }
        SparseArray<View> viewHolder = (SparseArray<View>) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<>();
            convertView.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = convertView.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }

    public void start() {
//        ExecutorService executorService = Executors.newFixedThreadPool(3);
//        ExecutorService executorService = Executors.newCachedThreadPool();
//        ExecutorService executorService = Executors.newSingleThreadExecutor();

//        Thread thread1 = new AddThread("thread1");
//        Thread thread2 = new AddThread("thread2");
//        Thread thread3 = new AddThread("thread3");
//        Thread thread4 = new AddThread("thread4");
//        Thread thread5 = new AddThread("thread5");
//        Thread thread6 = new AddThread("thread6");
//
//        executorService.execute(thread1);
//        executorService.execute(thread2);
//        executorService.execute(thread3);
//        executorService.execute(thread4);
//        executorService.execute(thread5);
//        executorService.execute(thread6);

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 5, 5, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());
        for (int i = 0; i < 20; i++) {
            threadPoolExecutor.execute(new AddThread("thread" + i));
            if (i == 19) {
                threadPoolExecutor.shutdown();
            }
        }
    }

    private class AddThread extends Thread {
        private String name;

        AddThread(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            try {
                LogUtils.e(TAG, "add thread run:" + name);
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getDouble(double value) {
        return new BigDecimal(Double.toString(value)).setScale(2, BigDecimal.ROUND_DOWN).toPlainString();
    }

    public static String formatDecimal1(double d) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(d);
    }

    public static String formatDecimal2(double d) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(d);
    }

    public static String formatDecimal3(double d) {
        return String.format(Locale.getDefault(), "%.2f", d);
    }

    public static double formatDecimal4(double d) {
        return new BigDecimal(d).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public void test() {
        Loggers.d("classLoader:" + this.getClass().getClassLoader()
                + "\n," + Context.class.getClassLoader());
    }

    /**
     * 设置时区需要配置权限
     * <uses-permission android:name="android.permission.SET_TIME" />
     * <uses-permission android:name="android.permission.SET_TIME_ZONE"/>
     * AlarmManager alarm =(AlarmManager) getSystemService(Context.ALARM_SERVICE);
     * alarm.setTimeZone("Africa/Windhoek");
     * @return
     */
    public TimeZone getTimeZone() {
        TimeZone timeZone = TimeZone.getDefault();
        Loggers.d("id:" + timeZone.getID() + ",displayName:" + timeZone.getDisplayName() + ",rawOffset:" + timeZone.getRawOffset());
        return timeZone;
    }

}
