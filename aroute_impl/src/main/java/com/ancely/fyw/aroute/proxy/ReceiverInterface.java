package com.ancely.fyw.aroute.proxy;

import android.content.Context;
import android.content.Intent;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.aroute.proxy
 *  @文件名:   ReceiverInterface
 *  @创建者:   fanlelong
 *  @创建时间:  2019/8/22 10:19 AM
 *  @描述：    TODO
 */
public interface ReceiverInterface {
    void onReceive(Context context, Intent intent);
}
