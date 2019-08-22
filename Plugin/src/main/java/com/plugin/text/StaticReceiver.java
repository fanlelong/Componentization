package com.plugin.text;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/*
 *  @项目名：  Componentization
 *  @包名：    com.plugin.text
 *  @文件名:   TextReceiver
 *  @创建者:   fanlelong
 *  @创建时间:  2019/8/22 11:33 AM
 *  @描述：    TODO
 */
public class StaticReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "com.plugin.text.StaticReceiver", Toast.LENGTH_SHORT).show();
    }
}
