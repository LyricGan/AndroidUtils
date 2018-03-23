package com.lyric.utils;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * 网络工具类
 *
 * @author lyricgan
 */
public class NetworkUtils {

    public enum NetworkType {
        WIFI, CMNET, CMWAP, NONE_NET
    }

    private NetworkUtils() {
    }

    /**
     * 判断是否有网络连接，需要权限：{@link android.Manifest.permission#ACCESS_NETWORK_STATE}
     *
     * @param context 上下文对象
     * @return true or false
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo[] networkInfoItems = cm.getAllNetworkInfo();
            if (networkInfoItems != null) {
                for (NetworkInfo itemInfo : networkInfoItems) {
                    if (itemInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断手机网络是否可用，需要权限：{@link android.Manifest.permission#ACCESS_NETWORK_STATE}
     *
     * @param context 上下文对象
     * @return true or false
     */
    public static boolean isMobileEnabled(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (networkInfo != null) {
                return networkInfo.isConnectedOrConnecting();
            }
        }
        return false;
    }

    /**
     * 判断Wifi网络是否可用，需要权限：{@link android.Manifest.permission#ACCESS_NETWORK_STATE}
     *
     * @param context 上下文对象
     * @return true or false
     */
    public static boolean isWifiEnabled(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (networkInfo != null) {
                return networkInfo.isConnectedOrConnecting();
            }
        }
        return false;
    }

    /**
     * 判断网络是否为漫游，需要权限：{@link android.Manifest.permission#ACCESS_NETWORK_STATE}
     *
     * @param context 上下文对象
     * @return true or false
     */
    public static boolean isNetworkRoaming(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (info != null && info.getType() == ConnectivityManager.TYPE_MOBILE) {
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                if (tm != null && tm.isNetworkRoaming()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static NetworkType getApnType(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null) {
                switch (networkInfo.getType()) {
                    case ConnectivityManager.TYPE_MOBILE:
                        if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
                            return NetworkType.CMNET;
                        } else {
                            return NetworkType.CMWAP;
                        }
                    case ConnectivityManager.TYPE_WIFI:
                        return NetworkType.WIFI;
                }
            }
        }
        return NetworkType.NONE_NET;
    }

    public static int getNetWorkType(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                switch (networkInfo.getType()) {
                    case ConnectivityManager.TYPE_MOBILE:
                        return ConnectivityManager.TYPE_MOBILE;
                    case ConnectivityManager.TYPE_WIFI:
                        return ConnectivityManager.TYPE_WIFI;
                    default:
                        break;
                }
            }
        }
        return -1;
    }

    /**
     * 判断gps是否打开，需要权限{@link android.Manifest.permission#ACCESS_FINE_LOCATION}
     * @param context 上下文
     * @return true or false
     */
    public static boolean isGpsEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return (locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
    }
}
