package com.ancely.fyw.aroute;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.ancely.fyw.aroute.proxy.ActivityInterface;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.aroute
 *  @文件名:   BaseActivity
 *  @创建者:   fanlelong
 *  @创建时间:  2019/8/21 1:56 PM
 *  @描述：    TODO
 */
public class BaseActivity extends AppCompatActivity implements ActivityInterface {

    protected Activity mActivity;


    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

    }

    @SuppressLint("MissingSuperCall")

    @Override
    public void onStart() {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onResume() {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onPause() {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onStop() {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onDestroy() {

    }
    
}
