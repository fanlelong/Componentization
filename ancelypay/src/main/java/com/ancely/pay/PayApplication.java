package com.ancely.pay;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.ancely.fyw.aroute.eventbus.EventBus;
import com.ancely.fyw.aroute.manager.NetWorkManager;
import com.ancely.pay.constans.Constances;
import com.ancely.pay.event.EventBusIndex;
import com.ancely.pay.utils.SharePreferenceHelper;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.pay
 *  @文件名:   PayApplication
 *  @创建者:   fanlelong
 *  @创建时间:  2020/2/15 2:51 PM
 *  @描述：    TODO
 */
public class PayApplication extends Application {
    public static Context mApplication;
    @Override
    public void onCreate() {
        super.onCreate();
        initValized(this);
        NetWorkManager.getInstance().init(Constances.API, this);
    }

    public void initValized(Context application){
        mApplication=application;
        EventBus.getDefault().addIndex(new EventBusIndex());


        String api = SharePreferenceHelper.getString(application, "POST_API");
        if (!TextUtils.isEmpty(api)) {
            Constances.API = api;
        }
        Constances.APPID = SharePreferenceHelper.getString(application, "APPID");

    }
}
