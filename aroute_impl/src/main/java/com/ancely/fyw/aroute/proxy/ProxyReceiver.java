package com.ancely.fyw.aroute.proxy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ancely.fyw.aroute.manager.PluginManager;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.aroute.proxy
 *  @文件名:   ProxyReceiver
 *  @创建者:   fanlelong
 *  @创建时间:  2019/8/22 9:29 AM
 *  @描述：    代理广播
 */
public class ProxyReceiver extends BroadcastReceiver {

    String mClassName;

    public ProxyReceiver(String className) {
        mClassName = className;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Class<?> aClass = PluginManager.getInstance().getClassLoader().loadClass(mClassName);
            Object receiver = aClass.newInstance();

            if (receiver instanceof ReceiverInterface) {
                ((ReceiverInterface) receiver).onReceive(context, intent);
            }
        } catch (Exception e) {

        }
    }
}
