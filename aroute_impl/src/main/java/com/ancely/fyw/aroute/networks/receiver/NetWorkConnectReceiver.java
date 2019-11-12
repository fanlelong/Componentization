package com.ancely.fyw.aroute.networks.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ancely.fyw.aroute.networks.NetChangeImpl;
import com.ancely.fyw.aroute.networks.impl.NetType;
import com.ancely.fyw.aroute.utils.NetUtils;


/*
 *  网络监听广播
 */
public class NetWorkConnectReceiver extends BroadcastReceiver {
    private NetType mNetType;
    private NetChangeImpl mNetChange;

    @Override
    public void onReceive(Context context, Intent intent) {

        //通知所有注册的方法,网络发生了变化
        if (mNetType == NetUtils.getNetType()) return;
        mNetType = NetUtils.getNetType();
        mNetChange.post(mNetType);
    }

    public NetWorkConnectReceiver(NetChangeImpl netChange) {
        mNetType = NetType.NONE;
        mNetChange = netChange;
    }

}