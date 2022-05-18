package com.ancely.fyw;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.support.multidex.MultiDex;

import com.ancely.fyw.annotation.apt.SubscriberInfoIndex;
import com.ancely.fyw.aroute.base.BaseActivity;
import com.ancely.fyw.aroute.core.IApplication;
import com.ancely.fyw.aroute.eventbus.EventBus;
import com.ancely.fyw.aroute.manager.Applications;
import com.ancely.fyw.aroute.manager.NetWorkManager;
import com.ancely.fyw.aroute.utils.UIUtils;


/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw
 *  @文件名:   ComApplication
 *  @创建者:   fanlelong
 *  @创建时间:  2019/8/21 2:37 PM
 *  @描述：    TODO
 */
public class ComApplication extends Application implements IApplication {

    @Override
    public void onCreate() {
        super.onCreate();
//        Bugly.init(this, "900029763", false);
        NetWorkManager.getInstance().init("https://www.wanandroid.com/",this);
//        LeakCanary.install(this);
        UIUtils.initApplication(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        BaseActivity.time = System.currentTimeMillis();
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void attach(Context context) {

    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        for (IApplication application : Applications.sApplications) {
            application.onTrimMemory(level);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        for (IApplication application : Applications.sApplications) {
            application.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void addEventBean() {
        try {
            Class<?> aClass = Class.forName(BuildConfig.EVENT_PACKAGEAME+".EventBusIndex");
            Object o = aClass.newInstance();
            if (o instanceof SubscriberInfoIndex) {
                EventBus.getDefault().addIndex((SubscriberInfoIndex) o);
            }
        }catch (Exception ignored){
        }
    }
}
