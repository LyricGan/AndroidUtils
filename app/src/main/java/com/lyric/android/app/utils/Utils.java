package com.lyric.android.app.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;

import com.lyric.android.app.R;
import com.lyric.android.app.activity.MainActivity;

import java.util.Iterator;
import java.util.List;

/**
 * @author lyric
 * @description 工具类，封装一部分常用方法
 * @time 16/12/9
 */
public class Utils {

    /**
     * 判断网络是否连接
     * @param context 上下文对象
     * @return 网络是否连接
     */
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * 获取本地号码
     *
     * @param context 上下文对象
     * @return 本地号码
     */
    public static String getLocalNumber(Context context) {
        TelephonyManager tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tManager.getLine1Number();
    }

    /**
     * 获取应用签名信息
     *
     * @param context     上下文对象
     * @param packageName 应用包名
     * @return 应用签名信息
     */
    public static String getSignatureInfo(Context context, String packageName) {
        // 判断应用包名是否为空
        if (packageName == null) {
            return null;
        }
        Signature[] signatureArray = null;
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(PackageManager.GET_SIGNATURES);
        Iterator<PackageInfo> iterator = packageInfoList.iterator();
        String packname;
        while (iterator.hasNext()) {
            PackageInfo packageInfo = iterator.next();
            packname = packageInfo.packageName;
            // 判断包名是否相同
            if (packageName.equals(packname)) {
                signatureArray = packageInfo.signatures;
            }
        }
        // 判断签名数组是否为空
        if (signatureArray != null) {
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < signatureArray.length; i++) {
                buffer.append(signatureArray[i]);
            }
            return buffer.toString();
        }
        return null;
    }

    /**
     * 判断当前网络连接状态
     *
     * @param context 上下文对象
     * @return 网络是否连接
     */
    public static boolean isNetworkConnected(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        // 判断网络信息是否为空
        if (networkInfo != null) {
            return networkInfo.isConnectedOrConnecting();
        }
        return false;
    }

    /**
     * 获取手机运营商
     *
     * @param context 上下文对象
     * @return 手机运营商
     */
    public static String getMobileOperator(Context context) {
        // 获取手机的IMSI码
        TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String sendNumber = null;
        String strIMSI = telManager.getSubscriberId();
        // 判断IMSI码是否为空
        if (strIMSI != null) {
            // 判断号码是否为移动号码
            if (strIMSI.startsWith("46000") || strIMSI.startsWith("46002")) {
                // 因为移动网络编号46000下的IMSI已经用完，所以虚拟了一个46002编号，134/159号段使用了此编号
                sendNumber = "中国移动";
            } else if (strIMSI.startsWith("46001")) {
                sendNumber = "中国联通";
            } else if (strIMSI.startsWith("46003")) {
                sendNumber = "中国电信";
            }
        } else {
            sendNumber = "集团号码";
        }
        return sendNumber;
    }

    /**
     * 创建快捷方式
     */
    public void createShortCut(Context context) {
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
     * @param activity
     */
    public void deleteShortcut(Context context, Activity activity) {
        Intent shortCutIntent = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
        //快捷方式的名称
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
    public boolean isShortCutCreated(Context context) {
        boolean isShortCutCreated = false;
        final ContentResolver cr = context.getContentResolver();
        final String AUTHORITY = "com.android.launcher2.settings";
        final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/favorites?notify=true");
        Cursor cursor = cr.query(CONTENT_URI, new String[]{"title", "iconResource"}, "title=?",
                new String[]{context.getString(R.string.app_name)}, null);
        // 判断游标是否为空
        if (cursor != null && cursor.getCount() > 0) {
            isShortCutCreated = true;
        }
        return isShortCutCreated;
    }
}
