package com.ancely.fyw.aroute.life;

/*
 *  @项目名：  BaseMvp 
 *  @包名：    com.ancely.rxjava.mvp
 *  @文件名:   Lifecycle
 *  @创建者:   fanlelong
 *  @创建时间:  2018/8/28 上午10:03
 *  @描述：    TODO
 */

import android.support.annotation.NonNull;


public interface Lifecycle {

    void addListener(@NonNull LifecycleListener listener);

    void removeListener(@NonNull LifecycleListener listener);
}
