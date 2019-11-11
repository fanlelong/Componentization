package com.ancely.fyw;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.ancely.fyw.aroute.manager.PluginManager;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;


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
        PluginManager.init(this);
        Bugly.init(this, "900029763", false);
//        EventBus.builder().addIndex(new MyEventBusIndex()).installDefaultEventBus();

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
        Beta.installTinker();
    }
}
