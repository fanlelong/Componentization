package com.ancely.fyw.usercenter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ancely.fyw.aroute.manager.ParameterManager;

import con.ancely.fyw.annotation.apt.ARouter;
import con.ancely.fyw.annotation.apt.Parameter;

@ARouter(path = "/usercenter/UserCenter_MainActivity")
public class UserCenter_MainActivity extends AppCompatActivity {
    @Parameter
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usercenter);
        ParameterManager.getInstance().loadParameter(this);
        Log.e("componentization", name);
    }
}
