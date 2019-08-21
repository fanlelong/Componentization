package com.ancely.fyw.aroute.proxy;

import android.content.Intent;
import android.os.IBinder;

import com.ancely.fyw.aroute.base.BaseService;
import com.ancely.fyw.aroute.manager.PluginManager;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.aroute.proxy
 *  @文件名:   Proxyservice
 *  @创建者:   fanlelong
 *  @创建时间:  2019/8/21 2:38 PM
 *  @描述：    插件化 服务端
 */
public class ProxyService extends BaseService {
    private ServiceInterface mPluginInterface;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String className = intent.getStringExtra("className");
        try {
            Class<?> aClass = PluginManager.getInstance().getClassLoader().loadClass(className);

            Object service = aClass.newInstance();
            //判断这个对象是否按照我们的标准来
            if (service instanceof ServiceInterface) {
                mPluginInterface = (ServiceInterface) service;
                mPluginInterface.attach(this);
                mPluginInterface.onStartCommand(intent, flags, startId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        mPluginInterface.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return mPluginInterface.onUnbind(intent);
    }
}
