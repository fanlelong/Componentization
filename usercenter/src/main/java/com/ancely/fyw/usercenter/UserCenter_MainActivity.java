package com.ancely.fyw.usercenter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.ancely.fyw.aroute.base.BaseActivity;
import com.ancely.fyw.aroute.manager.ParameterManager;
import com.ancely.fyw.aroute.manager.PluginManager;
import com.ancely.fyw.aroute.proxy.ProxyActivity;
import com.ancely.fyw.aroute.proxy.ProxyService;

import con.ancely.fyw.annotation.apt.ARouter;
import con.ancely.fyw.annotation.apt.Parameter;

@ARouter(path = "/usercenter/UserCenter_MainActivity")
public class UserCenter_MainActivity extends BaseActivity {
    @Parameter
    String name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usercenter);
        ParameterManager.getInstance().loadParameter(this);
        Log.e("componentization", name);
    }

    public void startServices(View view) {
        //加载第三方插件apk
        boolean isSuccess = PluginManager.getInstance().loadPath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/plugin-debug.apk");
        if (!isSuccess) {
            return;
        }
        Intent intent = new Intent(this, ProxyService.class);
        //拿到插件apk的第一个activity的名字
        String serviceName = PluginManager.getInstance().getPackageServiceInfo().services[0].name;
        intent.putExtra("className", serviceName);
        startService(intent);
    }

    public void startActivitys(View view) {
        boolean isSuccess = PluginManager.getInstance().loadPath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/plugin-debug.apk");
        if (!isSuccess) {
            return;
        }
        Intent intent = new Intent(this, ProxyActivity.class);
        String serviceName = PluginManager.getInstance().getPackageInfo().activities[0].name;
        intent.putExtra("className", serviceName);
        startActivity(intent);
    }
}
