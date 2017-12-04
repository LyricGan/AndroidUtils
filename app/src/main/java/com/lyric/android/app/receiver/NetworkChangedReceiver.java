package com.lyric.android.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * 网络状态变化广播接收器
 * @author lyricgan
 * @time 2016/5/14 16:00
 */
public class NetworkChangedReceiver extends BroadcastReceiver {
    private static final String TAG = NetworkChangedReceiver.class.getSimpleName();
    private static final IntentFilter NETWORK_INTENT_FILTER = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
    private static NetworkChangedReceiver mInstance;
    private List<NetStateChangedListener> mListeners;

    private NetworkChangedReceiver() {
        mListeners = new ArrayList<>();
    }

    public static NetworkChangedReceiver getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkChangedReceiver();
        }
        return mInstance;
    }

    public void register(Context context) {
        context.registerReceiver(this, NETWORK_INTENT_FILTER);
    }

    public void unregister(Context context) {
        if (mListeners.size() > 0) {
            Log.w(TAG, "there are other listeners, reject this request");
            return;
        }
        context.unregisterReceiver(this);
    }

    public void addNetStateChangeListener(NetStateChangedListener listener) {
        if (listener == null) {
            return;
        }
        if (mListeners.contains(listener)) {
            return;
        }
        mListeners.add(listener);
    }

    public void removeNetStateChangeListener(NetStateChangedListener listener) {
        if (listener == null) {
            return;
        }
        if (mListeners.contains(listener)) {
            mListeners.remove(listener);
        }
    }

    public void clearNetStateChangeListeners() {
        if (mListeners != null && !mListeners.isEmpty()) {
            mListeners.clear();
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        for (NetStateChangedListener listener : mListeners) {
            listener.onChanged(getCurrentNetState(context));
        }
    }

    /**
     * 获取当前网络状态
     * @param context 上下文
     * @return 当前网络状态
     */
    public NetState getCurrentNetState(Context context) {
        if (context == null) {
            return NetState.NET_NO;
        }
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return NetState.NET_NO;
        }
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnectedOrConnecting()) {
            return NetState.NET_NO;
        }
        NetState state;
        switch (networkInfo.getType()) {
            case ConnectivityManager.TYPE_MOBILE:// 移动数据网络
                switch (networkInfo.getSubtype()) {
                    case TelephonyManager.NETWORK_TYPE_GPRS: // 联通2g
                    case TelephonyManager.NETWORK_TYPE_CDMA: // 电信2g
                    case TelephonyManager.NETWORK_TYPE_EDGE: // 移动2g
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        state = NetState.NET_2G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_EVDO_A: // 电信3g
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        state = NetState.NET_3G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:// 4G
                        state = NetState.NET_4G;
                        break;
                    default:// 未知网络，一般不会出现
                        state = NetState.NET_UNKNOWN;
                        break;
                }
                break;
            case ConnectivityManager.TYPE_WIFI:// wifi
                state = NetState.NET_WIFI;
                break;
            default:
                state = NetState.NET_UNKNOWN;// 未知网络
                break;
        }
        return state;
    }

    /**
     * 网络状态，枚举类
     * NET_NO：没有网络, NET_2G:2g网络, NET_3G：3g网络, NET_4G：4g网络, NET_WIFI：wifi, NET_UNKNOWN：未知网络
     */
    public enum NetState {
        NET_NO, NET_2G, NET_3G, NET_4G, NET_WIFI, NET_UNKNOWN
    }

    /**
     * 网络状态变化监听事件
     */
    public interface NetStateChangedListener {

        void onChanged(NetState state);
    }
}