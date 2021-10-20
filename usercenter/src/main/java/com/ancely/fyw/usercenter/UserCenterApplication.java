package com.ancely.fyw.usercenter;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.usercenter
 *  @文件名:   UserCenterApplication
 *  @创建者:   admin
 *  @创建时间:  2021/10/20 14:13
 *  @描述：    TODO
 */

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.Keep;

import com.ancely.fyw.annotation.apt.SubscriberInfoIndex;
import com.ancely.fyw.aroute.core.IApplication;
import com.ancely.fyw.aroute.eventbus.EventBus;

@Keep
public class UserCenterApplication implements IApplication {
    @Override
    public void attach(Context context) {
    }

    @Override
    public void onTrimMemory(int level) {
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
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
