package com.ancely.fyw.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.ancely.fyw.aroute.manager.ParameterManager;
import com.ancely.fyw.aroute.manager.RouterManager;

import con.ancely.fyw.annotation.apt.ARouter;
import con.ancely.fyw.annotation.apt.Parameter;

@ARouter(path = "/login/Login_MainActivity")
public class Login_MainActivity extends AppCompatActivity {

    @Parameter
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ParameterManager.getInstance().loadParameter(this);
        Log.e("componentization", name);
    }

    public void jumpToApp(View view) {
        RouterManager.getInstance().finish(this).withString("call","login_app").navigation(this,20);
    }
}
