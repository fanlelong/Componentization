package com.ancely.fyw;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.ancely.fyw.app.event.EventBusIndex;
import com.ancely.fyw.aroute.base.BaseActivity;
import com.ancely.fyw.aroute.eventbus.EventBus;
import com.ancely.fyw.aroute.manager.NetWorkManager;


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
        Log.e("ancely1","onCreate time start  "+ (System.currentTimeMillis()- BaseActivity.time));

        super.onCreate();
        Log.e("ancely1","onCreate time end  "+ (System.currentTimeMillis()- BaseActivity.time));

//        Bugly.init(this, "900029763", false);
//        EventBus.builder().addIndex(new MyEventBusIndex()).installDefaultEventBus();
        NetWorkManager.getInstance().init("https://www.wanandroid.com/",this);
        EventBus.getDefault().addIndex(new EventBusIndex());
//        LeakCanary.install(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        BaseActivity.time = System.currentTimeMillis();
        Log.e("ancely1","attachBaseContext time start  "+ (System.currentTimeMillis()- BaseActivity.time));
        super.attachBaseContext(base);
        Log.e("ancely1","attachBaseContext time end  "+ (System.currentTimeMillis()- BaseActivity.time));

        MultiDex.install(base);
//        Beta.installTinker();
    }
}
