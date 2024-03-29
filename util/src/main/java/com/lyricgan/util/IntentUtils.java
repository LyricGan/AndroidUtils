package com.lyricgan.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;

import java.io.File;
import java.util.Locale;

/**
 * Intent工具类
 * @author Lyric Gan
 */
public class IntentUtils {

    private IntentUtils() {
    }

    public static Intent getIntent(String action, String uriString) {
        Intent intent = new Intent(action);
        intent.setData(Uri.parse(uriString));
        return intent;
    }

    /**
     * 获取应用安装意图
     * @param uri 安装文件Uri
     * @return 应用安装意图
     */
    public static Intent getInstallIntent(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        return intent;
    }

    public static Intent getDialIntent(String phoneNumber) {
        return getIntent(Intent.ACTION_DIAL, "tel:" + phoneNumber);
    }

    public static Intent getSmsIntent(String smsContent, String phoneNumber) {
        Intent intent = getIntent(Intent.ACTION_VIEW, "smsto:" + phoneNumber);
        intent.putExtra("sms_body", smsContent);
        intent.setType("vnd.android-dir/mms-sms");
        return intent;
    }

    public static Intent getEmailIntent(String emailAddress) {
        return getIntent(Intent.ACTION_SENDTO, "mailto:" + emailAddress);
    }

    /**
     * 打开文件
     * @param filePath 文件路径
     * @return Intent
     */
    public static Intent getFileIntent(String filePath) {
        // 获取扩展名
        String end = filePath.substring(filePath.lastIndexOf(".") + 1).toLowerCase(Locale.getDefault());
        /* 依扩展名的类型决定MimeType */
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            return getAudioFileIntent(filePath);
        } else if (end.equals("3gp") || end.equals("mp4")) {
            return getAudioFileIntent(filePath);
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg") || end.equals("bmp")) {
            return getImageFileIntent(filePath);
        } else if (end.equals("apk")) {
            return getApkFileIntent(filePath);
        } else if (end.equals("ppt") || end.equals("pptx")) {
            return getPptFileIntent(filePath);
        } else if (end.equals("xls") || end.equals("xlsx")) {
            return getExcelFileIntent(filePath);
        } else if (end.equals("doc") || end.equals("docx")) {
            return getWordFileIntent(filePath);
        } else if (end.equals("pdf")) {
            return getPdfFileIntent(filePath);
        } else if (end.equals("chm")) {
            return getChmFileIntent(filePath);
        } else if (end.equals("txt")) {
            return getTextFileIntent(filePath, false);
        } else if (end.equals("zip")) {
            return getZipFileIntent(filePath);
        } else if (end.equals("rar")) {
            return getRarFileIntent(filePath);
        } else {
            return getDefaultFileIntent(filePath);
        }
    }

    /**
     * 获取用于打开APK文件的Intent
     * @param filePath 文件路径
     * @return Intent
     */
    public static Intent getApkFileIntent(String filePath) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        return intent;
    }

    /**
     * 获取用于打开VIDEO文件的Intent
     * @param filePath 文件路径
     * @return Intent
     */
    public static Intent getVideoFileIntent(String filePath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setDataAndType(uri, "video/*");
        return intent;
    }

    /**
     * 获取用于打开AUDIO文件的Intent
     * @param filePath 文件路径
     * @return Intent
     */
    public static Intent getAudioFileIntent(String filePath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setDataAndType(uri, "audio/*");
        return intent;
    }

    /**
     * 获取用于打开HTML文件的Intent
     * @param filePath 文件路径
     * @return Intent
     */
    public static Intent getHtmlFileIntent(String filePath) {
        Uri uri = Uri.parse(filePath).buildUpon().encodedAuthority("com.android.htmlfileprovider")
                .scheme("content").encodedPath(filePath).build();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "text/html");
        return intent;
    }

    /**
     * 获取用于打开图片文件的Intent
     * @param filePath 文件路径
     * @return Intent
     */
    public static Intent getImageFileIntent(String filePath) {
        Intent intent = getFileInnerIntent();
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

    /**
     * 获取用于打开PPT文件的Intent
     * @param filePath 文件路径
     * @return Intent
     */
    public static Intent getPptFileIntent(String filePath) {
        Intent intent = getFileInnerIntent();
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        return intent;
    }

    /**
     * 获取用于打开Excel文件的Intent
     * @param filePath 文件路径
     * @return Intent
     */
    public static Intent getExcelFileIntent(String filePath) {
        Intent intent = getFileInnerIntent();
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }

    /**
     * 获取用于打开Word文件的Intent
     * @param filePath 文件路径
     * @return Intent
     */
    public static Intent getWordFileIntent(String filePath) {
        Intent intent = getFileInnerIntent();
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }

    /**
     * 获取用于打开CHM文件的Intent
     * @param filePath 文件路径
     * @return Intent
     */
    public static Intent getChmFileIntent(String filePath) {
        Intent intent = getFileInnerIntent();
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setDataAndType(uri, "application/x-chm");
        return intent;
    }

    /**
     * 获取用于打开文本文件的Intent
     * @param filePath 文件路径
     * @return Intent
     */
    public static Intent getTextFileIntent(String filePath, boolean paramBoolean) {
        Intent intent = getFileInnerIntent();
        Uri uri;
        if (paramBoolean) {
            uri = Uri.parse(filePath);
        } else {
            uri = Uri.fromFile(new File(filePath));
        }
        intent.setDataAndType(uri, "text/plain");
        return intent;
    }

    /**
     * 获取用于打开PDF文件的Intent
     * @param filePath 文件路径
     * @return Intent
     */
    public static Intent getPdfFileIntent(String filePath) {
        Intent intent = getFileInnerIntent();
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }

    /**
     * 获取用于打开ZIP文件的Intent
     * @param filePath 文件路径
     * @return Intent
     */
    public static Intent getZipFileIntent(String filePath) {
        Intent intent = getFileInnerIntent();
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setDataAndType(uri, "application/zip");
        return intent;
    }

    /**
     * 获取用于打开RAR文件的Intent
     * @param filePath 文件路径
     * @return Intent
     */
    public static Intent getRarFileIntent(String filePath) {
        Intent intent = getFileInnerIntent();
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setDataAndType(uri, "application/rar");
        return intent;
    }


    public static Intent getDefaultFileIntent(String filePath) {
        Intent intent = getFileInnerIntent();
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setDataAndType(uri, "*/*");
        return intent;
    }

    private static Intent getFileInnerIntent() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    /**
     * 创建快捷方式
     * @param context 上下文对象
     * @param shortcutName 名称
     * @param resourceId 快捷图标
     * @param cls 快捷方式启动类
     */
    public static void createShortcut(Context context, String shortcutName, int resourceId, Class<?> cls) {
        Intent intent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutName);
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, Intent.ShortcutIconResource.fromContext(context, resourceId));
        // 设置为不允许重复创建
        intent.putExtra("duplicate", false);

        Intent launchIntent = new Intent(Intent.ACTION_MAIN);
        launchIntent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
        launchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        launchIntent.setClass(context, cls);
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launchIntent);

        context.sendBroadcast(intent);
    }

    /**
     * 删除快捷方式
     * @param context 上下文对象
     * @param shortcutName 名称
     * @param activity Activity
     */
    public static void deleteShortcut(Context context, String shortcutName, Activity activity) {
        Intent shortCutIntent = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
        shortCutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutName);
        String name = context.getPackageName();
        String appClass = name + "." + activity.getLocalClassName();
        ComponentName componentName = new ComponentName(name, appClass);
        shortCutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN).setComponent(componentName));
        context.sendBroadcast(shortCutIntent);
    }

    /**
     * 判断应用快捷方式是否创建
     * @param context 上下文对象
     * @param shortcutName 名称
     * @return 应用快捷方式是否创建
     */
    public static boolean isShortcutCreated(Context context, String shortcutName) {
        boolean isShortCutCreated = false;
        final ContentResolver cr = context.getContentResolver();
        final String AUTHORITY = "com.android.launcher2.settings";
        final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/favorites?notify=true");
        Cursor cursor = null;
        try {
            cursor = cr.query(CONTENT_URI, new String[]{"title", "iconResource"}, "title=?",
                    new String[]{shortcutName}, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 判断游标是否为空
            if (cursor != null && cursor.getCount() > 0) {
                isShortCutCreated = true;
                cursor.close();
            }
        }
        return isShortCutCreated;
    }
}
