package com.ancely.fyw.eventbus.debug;

import android.app.Application;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.eventbus.debug
 *  @文件名:   EventApplication
 *  @创建者:   fanlelong
 *  @创建时间:  2019/11/8 4:29 PM
 *  @描述：    TODO
 */
public class EventApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        LeakCanary.install(this);
    }
}
