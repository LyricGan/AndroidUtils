package com.lrc.baseand.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;

import java.io.File;
import java.util.List;
import java.util.Locale;
/**
 * Android文件工具类
 * 
 * @author ganyu
 *
 */
public class AndroidFileUtils {
	
	/**
	 * 打开文件
	 * @filePath filePath 文件路径
	 * @return
	 */
    public static Intent openFile(String filePath) {
    	// 获取扩展名
        String end = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length()).toLowerCase(Locale.getDefault());
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
            return getAllIntent(filePath);
        }
    }
    
    /**
	 * Android获取用于打开所有文件的Intent
	 * @filePath filePath 文件路径
	 * @return
	 */
    public static Intent getAllIntent(String filePath) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		Uri uri = Uri.fromFile(new File(filePath));
		intent.setDataAndType(uri, "*/*");
		return intent;
    }

    /**
	 * Android获取用于打开APK文件的Intent
	 * @filePath filePath 文件路径
	 * @return
	 */
    public static Intent getApkFileIntent(String filePath) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        return intent;
    }

    /**
	 * Android获取用于打开VIDEO文件的Intent
	 * @filePath filePath 文件路径
	 * @return
	 */
    public static Intent getVideoFileIntent(String filePath) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setDataAndType(uri, "video/*");
        return intent;
    }

    /**
	 * Android获取用于打开AUDIO文件的Intent
	 * @filePath filePath 文件路径
	 * @return
	 */
    public static Intent getAudioFileIntent(String filePath) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setDataAndType(uri, "audio/*");
        return intent;
    }

    /**
	 * Android获取用于打开HTML文件的Intent
	 * @filePath filePath 文件路径
	 * @return
	 */
    public static Intent getHtmlFileIntent(String filePath) {
        Uri uri = Uri.parse(filePath).buildUpon().encodedAuthority("com.android.htmlfileprovider")
                .scheme("content").encodedPath(filePath).build();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "text/html");
        return intent;
    }

    /**
	 * Android获取用于打开图片文件的Intent
	 * @filePath filePath 文件路径
	 * @return
	 */
    public static Intent getImageFileIntent(String filePath) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

    /**
	 * Android获取用于打开PPT文件的Intent
	 * @filePath filePath 文件路径
	 * @return
	 */
    public static Intent getPptFileIntent(String filePath) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        return intent;
    }

    /**
	 * Android获取用于打开Excel文件的Intent
	 * @filePath filePath 文件路径
	 * @return
	 */
    public static Intent getExcelFileIntent(String filePath) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }

    /**
	 * Android获取用于打开Word文件的Intent
	 * @filePath filePath 文件路径
	 * @return
	 */
    public static Intent getWordFileIntent(String filePath) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }

    /**
	 * Android获取用于打开CHM文件的Intent
	 * @filePath filePath 文件路径
	 * @return
	 */
    public static Intent getChmFileIntent(String filePath) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setDataAndType(uri, "application/x-chm");
        return intent;
    }

    /**
	 * Android获取用于打开文本文件的Intent
	 * @filePath filePath 文件路径
	 * @return
	 */
    public static Intent getTextFileIntent(String filePath, boolean paramBoolean) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (paramBoolean) {
            Uri uri1 = Uri.parse(filePath);
            intent.setDataAndType(uri1, "text/plain");
        } else {
            Uri uri2 = Uri.fromFile(new File(filePath));
            intent.setDataAndType(uri2, "text/plain");
        }
        return intent;
    }

    /**
	 * Android获取用于打开PDF文件的Intent
	 * @filePath filePath 文件路径
	 * @return
	 */
    public static Intent getPdfFileIntent(String filePath) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }

    /**
	 * Android获取用于打开ZIP文件的Intent
	 * @filePath filePath 文件路径
	 * @return
	 */
    public static Intent getZipFileIntent(String filePath) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setDataAndType(uri, "application/zip");
        return intent;
    }

    /**
	 * Android获取用于打开RAR文件的Intent
	 * @filePath filePath 文件路径
	 * @return
	 */
    public static Intent getRarFileIntent(String filePath) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setDataAndType(uri, "application/rar");
        return intent;
    }

    /**
     * 判断Intent是否可用
     * @param context 上下文对象
     * @param intent 意图
     * @return
     */
    public static boolean isIntentAvailable(Context context, Intent intent) {
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.GET_ACTIVITIES);
        return list.size() > 0;
    }

    /**
     * 判断文件是否存在
     * @filePath filePath 文件路径
     * @return
     */
    public static boolean isFileExists(String filePath) {
    	if (!TextUtils.isEmpty(filePath)) {
    		File file = new File(filePath);
            if (file.exists()) {
                return true;
            }
    	}
        return false;
    }
    
}
