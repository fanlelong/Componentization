package com.ancely.fyw.aroute.manager;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;
import android.os.Build;

import com.ancely.fyw.aroute.networks.NetChangeImpl;
import com.ancely.fyw.aroute.networks.NetworkCallbackImpl;
import com.ancely.fyw.aroute.networks.receiver.NetWorkConnectReceiver;

/*
 *   网络监听管理类
 */
public class NetChangerManager {
    private final NetWorkConnectReceiver mReceiver;
    private Application mApplication;
    private static NetChangerManager instance;
    private NetChangeImpl mNetChange;

    private NetChangerManager() {
        mNetChange = new NetChangeImpl();
        mReceiver = new NetWorkConnectReceiver(mNetChange);
    }

    public Context getApplication() {
        if (mApplication == null) {
            throw new RuntimeException("NetworkManager.getDefault().init没有初始化");
        }
        return mApplication;
    }

    public static NetChangerManager getDefault() {
        if (instance == null) {
            synchronized (NetChangerManager.class) {
                if (instance == null) {
                    instance = new NetChangerManager();
                }
            }
        }
        return instance;
    }

    @SuppressLint("MissingPermission")
    public void init(Application application) {
        mApplication = application;
        //通过广播方式来操作
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        //不通过广播来监听
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ConnectivityManager.NetworkCallback networkCallback = new NetworkCallbackImpl(mNetChange);
            NetworkRequest.Builder builder = new NetworkRequest.Builder();
            NetworkRequest request = builder.build();
            ConnectivityManager cm = (ConnectivityManager) NetWorkManager.getInstance().getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                cm.registerNetworkCallback(request, networkCallback);
            }
        } else {
            mApplication.registerReceiver(mReceiver, filter);
        }
    }

    public void registerObserver(Object object) {
        mNetChange.registerObserver(object);
    }

    public void unRegisterObserver(Object object) {
        mNetChange.unRegisterObserver(object);
    }

    public void unRegisterAllObserver() {
        mNetChange.unRegisterAllObserver();

    }
}
