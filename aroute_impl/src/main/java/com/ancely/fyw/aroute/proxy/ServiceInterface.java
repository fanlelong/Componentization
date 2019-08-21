package com.ancely.fyw.aroute.proxy;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.aroute.proxy
 *  @文件名:   ServiceInterface
 *  @创建者:   fanlelong
 *  @创建时间:  2019/8/21 1:57 PM
 *  @描述：    TODO
 */
public interface ServiceInterface {
    void attach(Service service);

    int onStartCommand(Intent intent, int flags, int startId);

    public IBinder onBind(Intent intent);

    public void onCreate();

    public void onDestroy();

    public boolean onUnbind(Intent intent);
}
