package com.ancely.fyw;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.ancely.fyw.aroute.eventbus.EventBus;
import com.ancely.fyw.aroute.manager.ParameterManager;
import com.ancely.fyw.aroute.manager.PluginManager;
import com.ancely.fyw.aroute.manager.RouterManager;
import com.ancely.fyw.aroute.model.ModelP;
import com.ancely.fyw.aroute.model.bean.ResponseBean;
import com.ancely.fyw.aroute.permissions.PermissionManager;
import com.ancely.fyw.aroute.permissions.listener.PermissionRequest;
import com.ancely.fyw.aroute.skin.utils.PreferencesUtils;
import com.ancely.fyw.aroute.utils.LogUtils;
import com.ancely.fyw.common.LoginCall;
import com.ancely.fyw.common.base.BaseModelActivity;
import com.ancely.fyw.login.bean.LoginBean;
import com.ancely.fyw.mvptext.SkinTestActivity;

import java.util.MyHashMap;
import java.util.MyLinkedHashMap;

import con.ancely.fyw.annotation.apt.ARouter;
import con.ancely.fyw.annotation.apt.NeedsPermission;
import con.ancely.fyw.annotation.apt.OnNeverAskAgain;
import con.ancely.fyw.annotation.apt.OnPermissionDenied;
import con.ancely.fyw.annotation.apt.OnShowRationale;
import con.ancely.fyw.annotation.apt.Parameter;
import con.ancely.fyw.annotation.apt.Subscribe;

@ARouter(path = "/app/MainActivity")
public class MainActivity extends BaseModelActivity {

    public static final String TAG = "ancely->>>";
    @Parameter
    String username;

    @Parameter(name = "/login/getDrawable")
    LoginCall mLoginCall;

    private Button mButton;

    @Override
    public ModelP getModelP() {
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        WindowManager.LayoutParams lp = getWindow().getAttributes();
//        lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER;

        super.onCreate(savedInstanceState);
//        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10000);


        ParameterManager.getInstance().loadParameter(this);
        ImageView imageView = findViewById(R.id.act_main_iv);
        imageView.setImageResource(mLoginCall.getDrawable());


    }

    @Override
    protected void initDatas() {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initView() {
        mButton = findViewById(R.id.button);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    public void jumpToLogin(View view) {
        RouterManager.getInstance().build("/login/LoginActivity")
                .navigation(this, 20);
    }

    public void jumpToUsercenter(View view) {
        RouterManager.getInstance().build("/usercenter/UserCenter_MainActivity")
                .withString("name", "app_usercenter")
                .navigation(this, 10);
//        PluginManager.getInstance().loadPluginPath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/plugin-debug.apk");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == 2001 && data != null) {
            String username = data.getStringExtra("username");
            LogUtils.e("ancely_fyw", username);
            mButton.setText(username);
            return;
        }

        Fragment navigation = (Fragment) RouterManager.getInstance().build("/app/TextFragment")
                .withResultString("name", "app_usercenter")
                .navigation(this, 10);
        Bundle arguments = ((Fragment) navigation).getArguments();
        String name = arguments.getString("name");
        String name1 = arguments.getString("name");
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager != null) {
            fragmentManager.beginTransaction().add(R.id.fragment, (Fragment) navigation).commitAllowingStateLoss();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    //日夜间切换
    public void dayOrNight(View view) {
        EventBus.getDefault().postSticky(new View(this));
        int uiMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (uiMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                setDayNightModel(AppCompatDelegate.MODE_NIGHT_YES);
                PreferencesUtils.putBoolean(this, "isNight", true);
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                setDayNightModel(AppCompatDelegate.MODE_NIGHT_NO);
                PreferencesUtils.putBoolean(this, "isNight", false);
                break;
            default:
                break;
        }

        MyHashMap<String,String> maps = new MyHashMap<>();
        maps.put(null,null);
        maps.put("12","aaa");
        maps.put("22","bbb");
        maps.put("32","ccc");
        maps.put("42","ccc");
        maps.put("52","ccc");
        maps.put("62","ccc");
        maps.put("72","ccc");
        maps.put("82","ccc");
        maps.put("92","ccc");
        maps.put("02","ccc");
        maps.put("112","ccc");
        maps.put("122","ccc");
        maps.put("132","ccc");
        maps.put("142","ccc");
        maps.put("152","ccc");
        maps.put("162","ccc");
        String text1 = maps.put("172", "ccc");
        Log.e(TAG,"text1: "+text1);

        String text2 = maps.put("172", "ccc111");
        Log.e(TAG,"text2: "+text2);

        String getTest = maps.get("172");
        Log.e(TAG,"getTest: "+getTest);

        String getNull = maps.get(null);
        Log.e(TAG,"getNull: "+getNull);

        MyLinkedHashMap<String,String> linkedHashMap = new MyLinkedHashMap<>();
        linkedHashMap.put("112","ccc");

    }


    @Override
    public boolean openChangerSkin() {
        return true;
    }


    public void jumpSkinDysn(View view) {
        startActivity(new Intent(this, SkinTestActivity.class));
    }

    @Override
    public void accessSuccess(ResponseBean responseBean) {

    }

    @Override
    public boolean isNeedCheckNetWork() {
        return false;
    }


    @Subscribe
    public void loginEvent(LoginBean loginBean) {
        LogUtils.e("ancely_fyw", loginBean.getUsername());
    }

    @Subscribe
    public void loginSuccess(LoginBean loginBean) {
        LogUtils.e("ancely_fyw", loginBean.getUsername());
    }

    @Override
    public boolean openEventBus() {
        return true;
    }


    @NeedsPermission()
    void showCamera() {
        PluginManager.getInstance().loadPluginPath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/plugin-debug.apk");

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
}
