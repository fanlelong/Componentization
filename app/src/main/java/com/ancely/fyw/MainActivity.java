package com.ancely.fyw;

import android.Manifest;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.ancely.fyw.aroute.base.BaseActivity;
import com.ancely.fyw.aroute.manager.ParameterManager;
import com.ancely.fyw.aroute.manager.PluginManager;
import com.ancely.fyw.aroute.manager.RouterManager;
import com.ancely.fyw.aroute.skin.utils.PreferencesUtils;
import com.ancely.fyw.common.LoginCall;

import con.ancely.fyw.annotation.apt.ARouter;
import con.ancely.fyw.annotation.apt.Parameter;

@ARouter(path = "/app/MainActivity")
public class MainActivity extends BaseActivity {


    @Parameter
    String name;

    @Parameter(name = "/login/getDrawable")
    LoginCall mLoginCall;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        WindowManager.LayoutParams lp = getWindow().getAttributes();
//        lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER;

        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10000);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(Configuration.UI_MODE_NIGHT_NO);
        boolean isNight = PreferencesUtils.getBoolean(this, "isNight");
        if (isNight) {
            setDayNightModel(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            setDayNightModel(AppCompatDelegate.MODE_NIGHT_NO);
        }


        ParameterManager.getInstance().loadParameter(this);
        ImageView imageView = findViewById(R.id.act_main_iv);
        imageView.setImageResource(mLoginCall.getDrawable());


    }

    public void jumpToOrder(View view) {
        RouterManager.getInstance().build("/login/Login_MainActivity1")
                .withResultString("name", "app_login")
                .navigation(this, 20);
    }

    public void jumpToUsercenter(View view) {
        RouterManager.getInstance().build("/usercenter/UserCenter_MainActivity")
                .withResultString("name", "app_usercenter")
                .navigation(this, 10);
        PluginManager.getInstance().loadPluginPath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/plugin-debug.apk");
        PluginManager.getInstance().parserApkAction(Environment.getExternalStorageDirectory().getAbsolutePath() + "/plugin-debug.apk");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 20 && data != null) {
            String call = data.getStringExtra("call");
            Log.e("componentization", call);
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
        int uiMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (uiMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                setDayNightModel(AppCompatDelegate.MODE_NIGHT_YES);
                PreferencesUtils.putBoolean(this,"isNight",true);
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                setDayNightModel(AppCompatDelegate.MODE_NIGHT_NO);
                PreferencesUtils.putBoolean(this,"isNight",false);
                break;
            default:
                break;
        }
    }



    @Override
    public boolean openChangerSkin() {
        return true;
    }
}
