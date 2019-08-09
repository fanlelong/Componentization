package com.ancely.fyw;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import con.ancely.fyw.annotation.apt.ARouter;

@ARouter(path = "/app/MainActivity")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
