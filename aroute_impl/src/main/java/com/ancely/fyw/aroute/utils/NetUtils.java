package com.ancely.fyw.aroute.utils;
/*
 *  网络相关工具类
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.ancely.fyw.aroute.manager.NetWorkManager;
import com.ancely.fyw.aroute.networks.impl.NetType;


public class NetUtils {
    /**
     * 判断网络是否连接
     *
     * @param context 上下文
     * @return true: 有网; false:无网;
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivity) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                return info.getState() == NetworkInfo.State.CONNECTED;
            }
        }
        return false;
    }

    public static boolean isRealWifi(Context context) {

        Runtime runtime = Runtime.getRuntime();
        try {
            Process p = runtime.exec("ping -c 3 www.baidu.com");
            int ret = p.waitFor();
            return ret == 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static NetType getNetType() {
        ConnectivityManager cm = (ConnectivityManager) NetWorkManager.getInstance().getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return NetType.NONE;
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null) {
            return NetType.NONE;
        }
        int type = networkInfo.getType();
        if (type == ConnectivityManager.TYPE_MOBILE) {
            if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
                return NetType.CMNET;
            } else {
                return NetType.CMWAP;
            }
        } else if (type == ConnectivityManager.TYPE_WIFI) {
            return NetType.WIFI;
        }
        return NetType.NONE;
    }
}