package com.ancely.fyw.login.debug;

import android.app.Application;

import com.ancely.fyw.aroute.eventbus.EventBus;
import com.ancely.fyw.aroute.manager.NetWorkManager;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.login.debug
 *  @文件名:   LoginApplication
 *  @创建者:   fanlelong
 *  @创建时间:  2019/11/13 11:34 AM
 *  @描述：    TODO
 */
public class LoginApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        NetWorkManager.getInstance().init("https://www.wanandroid.com/", null, this);
    }
}
