package com.ancely.fyw.aroute.core;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.common
 *  @文件名:   IApplication
 *  @创建者:   admin
 *  @创建时间:  2021/10/20 14:10
 *  @描述：    TODO
 */

import android.content.Context;
import android.content.res.Configuration;

public interface IApplication {
    void attach(Context context);

    void onTrimMemory(int level);

    void onConfigurationChanged(Configuration newConfig);

    void addEventBean();
}
