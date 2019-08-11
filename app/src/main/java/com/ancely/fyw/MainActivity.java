package com.ancely.fyw;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ancely.fyw.common.OrderCall;

import con.ancely.fyw.annotation.apt.ARouter;
import con.ancely.fyw.annotation.apt.Parameter;

@ARouter(path = "/app/MainActivity")
public class MainActivity extends AppCompatActivity {


    @Parameter
    String name;

    @Parameter(name = "/order/getDrawable")
    OrderCall mOrderCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
