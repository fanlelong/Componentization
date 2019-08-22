package com.plugin.text;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.ancely.fyw.aroute.proxy.ReceiverInterface;

/*
 *  @项目名：  Componentization
 *  @包名：    com.plugin.text
 *  @文件名:   TextReceitve
 *  @创建者:   fanlelong
 *  @创建时间:  2019/8/22 9:48 AM
 *  @描述：    TODO
 */
public class TextReceitve extends BroadcastReceiver implements ReceiverInterface {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "发送动态广播成功", Toast.LENGTH_SHORT).show();
    }
}
