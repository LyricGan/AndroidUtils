package com.lyric.android.app.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import com.lyric.android.app.R;
import com.lyric.android.app.ui.MainActivity;

/**
 * 应用快捷方式工具类
 * @author lyricgan
 * @date 2017/11/23 11:38
 */
public class ShortcutUtils {

    private ShortcutUtils() {
    }

    /**
     * 创建快捷方式
     * @param context 上下文对象
     */
    public void createShortcut(Context context) {
        Intent shortCutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        // 设置属性
        shortCutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getResources().getString(R.string.app_name));
        Intent.ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(context, R.mipmap.ic_launcher);
        shortCutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, iconRes);
        // 设置为不允许重复创建
        shortCutIntent.putExtra("duplicate", false);
        // 设置启动意图
        Intent launchIntent = new Intent(Intent.ACTION_MAIN);
        launchIntent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
        launchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        launchIntent.setClass(context, MainActivity.class);
        // 设置启动程序
        shortCutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launchIntent);
        // 发送广播
        context.sendBroadcast(shortCutIntent);
    }

    /**
     * 删除程序的快捷方式
     *
     * @param context  上下文对象
     * @param activity Activity
     */
    public void deleteShortcut(Context context, Activity activity) {
        Intent shortCutIntent = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
        shortCutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getString(R.string.app_name));
        String appClass = context.getPackageName() + "." + activity.getLocalClassName();
        ComponentName componentName = new ComponentName(context.getPackageName(), appClass);
        shortCutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN).setComponent(componentName));
        context.sendBroadcast(shortCutIntent);
    }

    /**
     * 判断应用快捷方式是否创建
     *
     * @param context 上下文对象
     * @return 应用快捷方式是否创建
     */
    public boolean isShortcutCreated(Context context) {
        boolean isShortCutCreated = false;
        final ContentResolver cr = context.getContentResolver();
        final String AUTHORITY = "com.android.launcher2.settings";
        final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/favorites?notify=true");
        Cursor cursor = cr.query(CONTENT_URI, new String[]{"title", "iconResource"}, "title=?",
                new String[]{context.getString(R.string.app_name)}, null);
        // 判断游标是否为空
        if (cursor != null && cursor.getCount() > 0) {
            isShortCutCreated = true;
            cursor.close();
        }
        return isShortCutCreated;
    }
}
