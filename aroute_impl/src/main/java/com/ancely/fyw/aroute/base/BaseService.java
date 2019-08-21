package com.ancely.fyw.aroute.base;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.ancely.fyw.aroute.proxy.ServiceInterface;


/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.aroute.base
 *  @文件名:   BaseService
 *  @创建者:   fanlelong
 *  @创建时间:  2019/8/21 2:12 PM
 *  @描述：    TODO
 */
public class BaseService extends Service implements ServiceInterface {
    protected Service mService;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void attach(Service proxyService) {
        mService = proxyService;
    }

    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return 0;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return mService.onUnbind(intent);
    }
}
