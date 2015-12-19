package com.lrc.baseand.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;

/**
 * 网络工具类
 * 
 * @author ganyu
 *
 */
public class NetworkUtils {
	public static Uri sUri = Uri.parse("content://telephony/carriers");
	
	/**
     * 判断是否有网络连接
     * <br/>
     * 需要权限：{@link android.Manifest.permission#ACCESS_NETWORK_STATE}
     * @param context 上下文对象
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        }
        NetworkInfo[] info = cm.getAllNetworkInfo();
        if (info != null) {
            for (int i = 0; i < info.length; i++) {
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * 判断手机网络是否可用
     * <br/>
     * 需要权限：{@link android.Manifest.permission#ACCESS_NETWORK_STATE}
     * @param context 上下文对象
     * @return
     */
    public static boolean isMobileEnabled(Context context) {
    	ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    	if (cm == null) {
            return false;
        }
    	NetworkInfo networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
    	if (networkInfo != null) {
    		return networkInfo.isConnectedOrConnecting();
    	}
    	return false;
    }
    
    /**
     * 判断Wifi网络是否可用
     * <br/>
     * 需要权限：{@link android.Manifest.permission#ACCESS_NETWORK_STATE}
     * @param context 上下文对象
     * @return
     */
    public static boolean isWifiEnabled(Context context) {
    	ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    	if (cm == null) {
            return false;
        }
    	NetworkInfo networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    	if (networkInfo != null) {
    		return networkInfo.isConnectedOrConnecting();
    	}
    	return false;
    }
    
    /**
     * 判断网络是否为漫游
     * <br/>
     * 需要权限：{@link android.Manifest.permission#ACCESS_NETWORK_STATE}
     * @param context 上下文对象
     * @return
     */
    public static boolean isNetworkRoaming(Context context) {
    	ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    	if (cm == null) {
            return false;
        }
    	NetworkInfo info = cm.getActiveNetworkInfo();
    	if (info != null && info.getType() == ConnectivityManager.TYPE_MOBILE) {
    		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null && tm.isNetworkRoaming()) {
            	return true;
            }
    	}
    	return false;
    }
    
}
