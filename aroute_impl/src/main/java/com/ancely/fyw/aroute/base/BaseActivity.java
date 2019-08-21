package com.ancely.fyw.aroute.base;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

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


    @Override
    public void attach(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (mActivity == null) {
            super.onCreate(savedInstanceState);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        if (mActivity == null) {
            super.setContentView(layoutResID);
        } else {
            mActivity.setContentView(layoutResID);
        }
    }

    @Override
    public void setContentView(View view) {
        if (mActivity == null) {
            super.setContentView(view);
        } else {
            mActivity.setContentView(view);
        }
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if (mActivity == null) {
            super.setContentView(view, params);
        } else {
            mActivity.setContentView(view, params);
        }
    }

    @Override
    public <T extends View> T findViewById(int id) {
        if (mActivity != null) {
            return mActivity.findViewById(id);
        }
        return super.findViewById(id);
    }


    @Override
    public Intent getIntent() {
        if (mActivity != null) {
            return mActivity.getIntent();
        }
        return super.getIntent();
    }

    @Override
    public ClassLoader getClassLoader() {
        if (mActivity != null) {
            return mActivity.getClassLoader();
        }
        return super.getClassLoader();
    }

    @Override
    public Resources getResources() {
        if (mActivity != null) {
            return mActivity.getResources();
        }
        return super.getResources();
    }

    @Override
    public Window getWindow() {
        if (mActivity != null) {
            return mActivity.getWindow();
        }
        return super.getWindow();
    }

    @Override
    public WindowManager getWindowManager() {
        if (mActivity != null) {
            return mActivity.getWindowManager();
        }
        return super.getWindowManager();
    }

    @Override
    public PackageManager getPackageManager() {
        if (mActivity != null) {
            return mActivity.getPackageManager();
        }
        return super.getPackageManager();
    }

    @Override
    public ApplicationInfo getApplicationInfo() {
        if (mActivity != null) {
            return mActivity.getApplicationInfo();
        }
        return super.getApplicationInfo();
    }

    @NonNull
    @Override
    public LayoutInflater getLayoutInflater() {
        if (mActivity != null) {
            return mActivity.getLayoutInflater();
        }
        return super.getLayoutInflater();
    }

    @Override
    public void startActivity(Intent intent) {
        if (mActivity == null) {
            super.startActivity(intent);
        } else {
            Intent newIntent = new Intent();
            newIntent.putExtra("className", intent.getComponent().getClassName());
            mActivity.startActivity(newIntent);
        }
    }

    @Override
    public ComponentName startService(Intent service) {
        if (mActivity == null) {
            return super.startService(service);
        }
        Intent newService = new Intent();
        newService.putExtra("className", service.getComponent().getClassName());
        return mActivity.startService(newService);
    }

    @Override
    public void onStart() {
        if (mActivity == null) {
            super.onStart();
        }
    }

    @Override
    public void onResume() {
        if (mActivity == null) {
            super.onResume();
        }
    }

    @Override
    public void onPause() {
        if (mActivity == null) {
            super.onPause();
        }
    }

    @Override
    public void onStop() {
        if (mActivity == null) {
            super.onStop();
        }
    }

    @Override
    public void onDestroy() {
        if (mActivity == null) {
            super.onDestroy();
        }
    }
}
