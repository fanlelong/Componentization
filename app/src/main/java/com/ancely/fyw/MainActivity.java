package com.ancely.fyw;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.ancely.fyw.aroute.manager.ParameterManager;
import com.ancely.fyw.aroute.manager.RouterManager;
import com.ancely.fyw.common.LoginCall;

import con.ancely.fyw.annotation.apt.ARouter;
import con.ancely.fyw.annotation.apt.Parameter;

@ARouter(path = "/app/MainActivity")
public class MainActivity extends AppCompatActivity {


    @Parameter
    String name;

    @Parameter(name = "/login/getDrawable")
    LoginCall mLoginCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10000);
        setContentView(R.layout.activity_main);
        ParameterManager.getInstance().loadParameter(this);
        ImageView imageView = findViewById(R.id.act_main_iv);
        imageView.setImageResource(mLoginCall.getDrawable());
    }

    public void jumpToOrder(View view) {
        RouterManager.getInstance().build("/login/Login_MainActivity")
                .withResultString("name", "app_login")
                .navigation(this, 20);
    }

    public void jumpToUsercenter(View view) {
        RouterManager.getInstance().build("/usercenter/UserCenter_MainActivity")
                .withResultString("name", "app_usercenter")
                .navigation(this, 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 20 && data != null) {
            String call = data.getStringExtra("call");
            Log.e("componentization", call);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
