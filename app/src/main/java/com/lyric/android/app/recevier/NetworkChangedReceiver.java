package com.lyric.android.app.recevier;

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
 * @author lyric
 * @description
 * @time 2016/5/14 16:00
 */
public class NetworkChangedReceiver extends BroadcastReceiver {
    private static final String TAG = NetworkChangedReceiver.class.getSimpleName();
    public static IntentFilter sIntentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
    private static NetworkChangedReceiver mInstance;
    private List<NetStateChangedListener> mListeners;

    /**
     * 枚举网络状态
     * NET_NO：没有网络, NET_2G:2g网络, NET_3G：3g网络, NET_4G：4g网络, NET_WIFI：wifi, NET_UNKNOWN：未知网络
     */
    public enum NetState {
        NET_NO, NET_2G, NET_3G, NET_4G, NET_WIFI, NET_UNKNOWN
    }

    public interface NetStateChangedListener {
        void onNetStateChanged(NetState state);
    }

    private NetworkChangedReceiver() {
        mListeners = new ArrayList<NetStateChangedListener>();
    }

    public static NetworkChangedReceiver getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkChangedReceiver();
        }
        return mInstance;
    }

    public void register(Context context) {
        context.registerReceiver(this, sIntentFilter);
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

    public void remoteNetStateChangeListener(NetStateChangedListener listener) {
        if (listener == null) {
            return;
        }
        if (mListeners.contains(listener)) {
            mListeners.remove(listener);
        }
    }

    public void clearNetStateChangeListeners() {
        mListeners.clear();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        for (NetStateChangedListener listener : mListeners) {
            listener.onNetStateChanged(getCurrentNetState(context));
        }
    }

    public NetState getCurrentNetState(Context context) {
        NetState state = NetState.NET_NO;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isConnectedOrConnecting()) {
            switch (ni.getType()) {
                case ConnectivityManager.TYPE_WIFI: {// wifi
                    state = NetState.NET_WIFI;
                }
                    break;
                case ConnectivityManager.TYPE_MOBILE: {// 移动网络
                    switch (ni.getSubtype()) {
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
                        default:// 未知,一般不会出现
                            state = NetState.NET_UNKNOWN;
                    }
                }
                    break;
                default:
                    state = NetState.NET_UNKNOWN;
            }
        }
        return state;
    }

    private NetStateChangedListener mInnerStateChangedListener = new NetStateChangedListener() {
        @Override
        public void onNetStateChanged(NetState state) {
            switch (state) {
                case NET_NO: // 断网
                    break;
                case NET_2G:
                case NET_3G:
                case NET_4G:// 移动数据网络
                    break;
                case NET_WIFI:// wifi
                    break;
                case NET_UNKNOWN:// 未知网络
                    break;
                default:
                    break;
            }
        }
    };
}