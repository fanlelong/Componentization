package com.ancely.fyw.aroute.networks;

import android.annotation.SuppressLint;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.util.Log;

import com.ancely.fyw.aroute.networks.impl.NetType;
import com.ancely.fyw.aroute.utils.NetUtils;



@SuppressLint("NewApi")
public class NetworkCallbackImpl extends ConnectivityManager.NetworkCallback {
    private final NetChangeImpl netChange;
    private NetType mNetType;

    public NetworkCallbackImpl(NetChangeImpl netChange) {
        mNetType = NetType.NONE;
        this.netChange = netChange;

    }

    @Override
    public void onAvailable(Network network) {
        super.onAvailable(network);
        Log.e("NetworkCallbackImpl", "网络已经连接");
        mNetType=NetUtils.getNetType();
        netChange.post(mNetType);
    }


    @Override
    public void onLost(Network network) {
        super.onLost(network);
        Log.e("NetworkCallbackImpl", "网络已经中断");
        mNetType=NetUtils.getNetType();
        netChange.post(mNetType);
    }

    @Override
    public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities);
        if (mNetType==NetUtils.getNetType()) {
            return;
        }
        if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.e("NetworkCallbackImpl", "网络发生改变, 类型为 WIFI");
                netChange.post(NetType.WIFI);
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {//
                Log.e("NetworkCallbackImpl", "网络发生改变, 类型为 移动数据");
                netChange.post(NetType.CMNET);
            } else {
                Log.e("NetworkCallbackImpl", "网络发生改变, 类型为 其它");
            }
        }
    }
}
