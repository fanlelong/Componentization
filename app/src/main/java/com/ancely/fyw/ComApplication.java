package com.ancely.fyw;

import android.app.Application;

import com.ancely.fyw.aroute.manager.PluginManager;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw
 *  @文件名:   ComApplication
 *  @创建者:   fanlelong
 *  @创建时间:  2019/8/21 2:37 PM
 *  @描述：    TODO
 */
public class ComApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PluginManager.getInstance().setContext(this);
    }
}
