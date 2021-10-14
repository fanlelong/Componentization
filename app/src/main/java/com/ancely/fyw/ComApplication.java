package com.ancely.fyw;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.ancely.fyw.annotation.apt.SubscriberInfoIndex;
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
        super.onCreate();
//        Bugly.init(this, "900029763", false);
        NetWorkManager.getInstance().init("https://www.wanandroid.com/",this);
        try {
            Class<?> aClass = Class.forName(BuildConfig.EVENT_PACKAGEAME+".EventBusIndex");
            Object o = aClass.newInstance();
            if (o instanceof SubscriberInfoIndex) {
                EventBus.getDefault().addIndex((SubscriberInfoIndex) o);
            }
        }catch (Exception ignored){
        }
//        LeakCanary.install(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        BaseActivity.time = System.currentTimeMillis();
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}
