package com.ancely.fyw;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import con.ancely.fyw.annotation.apt.ARouter;
import con.ancely.fyw.annotation.apt.Parameter;

@ARouter(path = "/app/MainActivity")
public class MainActivity extends AppCompatActivity {


    @Parameter
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
