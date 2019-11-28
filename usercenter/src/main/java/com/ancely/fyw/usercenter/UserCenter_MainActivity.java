package com.ancely.fyw.usercenter;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import com.ancely.fyw.aroute.base.BaseActivity;
import com.ancely.fyw.aroute.eventbus.EventBus;
import com.ancely.fyw.aroute.manager.ParameterManager;
import com.ancely.fyw.aroute.manager.PluginManager;
import com.ancely.fyw.aroute.permissions.PermissionManager;
import com.ancely.fyw.aroute.permissions.listener.PermissionRequest;
import com.ancely.fyw.aroute.proxy.ProxyActivity;
import com.ancely.fyw.aroute.proxy.ProxyService;
import com.ancely.fyw.usercenter.apt.EventBusIndex;

import con.ancely.fyw.annotation.apt.ARouter;
import con.ancely.fyw.annotation.apt.NeedsPermission;
import con.ancely.fyw.annotation.apt.OnNeverAskAgain;
import con.ancely.fyw.annotation.apt.OnPermissionDenied;
import con.ancely.fyw.annotation.apt.OnShowRationale;
import con.ancely.fyw.annotation.apt.Parameter;
import con.ancely.fyw.annotation.apt.Subscribe;
import con.ancely.fyw.annotation.apt.bean.ThreadMode;

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
        EventBus.getDefault().addIndex(new EventBusIndex());
        EventBus.getDefault().register(this);
    }


    // 提示用户为何要开启权限
    @OnShowRationale()
    void showRationaleForCamera(final PermissionRequest request) {
        Log.e("ancely_fyw >>> ", "showRationaleForCamera()");
        new AlertDialog.Builder(this)
                .setMessage("提示用户为何要开启权限")
                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        // 再次执行权限请求
                        request.proceed();
                    }
                })
                .show();
    }

    // 用户选择拒绝时的提示
    @OnPermissionDenied()
    void showDeniedForCamera() {
        Log.e("ancely_fyw >>> ", "showDeniedForCamera()");
    }

    // 用户选择不再询问后的提示
    @OnNeverAskAgain()
    void showNeverAskForCamera() {
        Log.e("ancely_fyw >>> ", "showNeverAskForCamera()");
        new AlertDialog.Builder(this)
                .setMessage("用户选择不再询问后的提示")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        Log.e("ancely_fyw >>> ", "showNeverAskForCamera() >>> Dialog");
                    }
                })
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("ancely_fyw >>> ", "onRequestPermissionsResult()");
        PermissionManager.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    public void permission(View view) {
        PermissionManager.request(this, new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
    }


    @NeedsPermission(code = 1001)
    public void startServices() {
        //加载第三方插件apk
        boolean isSuccess = PluginManager.getInstance().loadPluginPath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/plugin-debug.apk");
        if (!isSuccess) {
            return;
        }
        Intent intent = new Intent(this, ProxyService.class);
        //拿到插件apk的第一个activity的名字
        String serviceName = PluginManager.getInstance().getPackageServiceInfo().services[0].name;
        intent.putExtra("className", serviceName);
        startService(intent);
    }

    public void startServices(View view) {
        PermissionManager.request(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, 1001);
    }

    @NeedsPermission(code = 1002)
    public void startActivitys() {
        boolean isSuccess = PluginManager.getInstance().loadPluginPath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/plugin-debug.apk");
        if (!isSuccess) {
            return;
        }
        Intent intent = new Intent(this, ProxyActivity.class);
        String serviceName = PluginManager.getInstance().getPackageInfo().activities[0].name;
        intent.putExtra("className", serviceName);
        startActivity(intent);
    }

    public void startActivitys(View view) {
        PermissionManager.request(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, 1002);

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true, priority = 10)
    public void sendReceiver(View view) {
        Intent intent = new Intent();
        intent.setAction("com.plugin.text.StaticReceiver");
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
