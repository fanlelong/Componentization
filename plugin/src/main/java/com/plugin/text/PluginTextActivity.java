package com.plugin.text;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ancely.fyw.aroute.base.BaseActivity;

public class PluginTextActivity extends BaseActivity {
    public static final String ACTION = "com.plugin.text.TextReceitve";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin_text);

        Button button = findViewById(R.id.startPluginService);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(mActivity, TextService.class));
            }
        });

        //注册广播
        findViewById(R.id.regist_broadcast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentFilter infilter = new IntentFilter();
                infilter.addAction(ACTION);
                registerReceiver(new TextReceitve(), infilter);
            }
        });
        //发送广播
        findViewById(R.id.send_broadcast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(ACTION);
                sendBroadcast(intent);
            }
        });

//        PluginNetWorkApi.getInstance().getService(PluginApi.class).login(null)
//                .compose(PluginNetWorkApi.getInstance().applySchedulers(new BaseObserver<String>() {
//                    @Override
//                    public void onSuccess(String s) {
//
//                    }
//
//                    @Override
//                    public void onFailure(Throwable e) {
//
//                    }
//                }));
    }


    @Override
    public boolean openChangerSkin() {
        return true;
    }
}
