package com.plugin.text;

import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.ancely.fyw.aroute.base.BaseService;

/*
 *  @项目名：  Componentization
 *  @包名：    com.plugin.text
 *  @文件名:   TextService
 *  @创建者:   fanlelong
 *  @创建时间:  2019/8/21 3:03 PM
 *  @描述：    TODO
 */
public class TextService extends BaseService {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(mService, "TextService-onStartCommand", Toast.LENGTH_SHORT).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    SystemClock.sleep(3000);
                    Log.e(TextService.class.getSimpleName(), "TextService-onStartCommand");
                }
            }
        }).start();

        return super.onStartCommand(intent, flags, startId);
    }
}
