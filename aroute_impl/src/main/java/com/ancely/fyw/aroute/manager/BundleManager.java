package com.ancely.fyw.aroute.manager;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

/*
 *  @项目名：  zhujianhua
 *  @包名：    com.arouter.api
 *  @文件名:   BundleManager
 *  @创建者:   fanlelong
 *  @创建时间:  2019/8/9 9:31 PM
 *  @描述：    参数管理
 */
public class BundleManager {
    private Bundle mBundle = new Bundle();

    //是否需要回调的setResult
    private boolean isResult = false;
    private boolean finish;

    public BundleManager() {
    }

    public BundleManager(boolean finish) {
        this.finish = finish;
    }

    public Bundle getBundle() {
        return mBundle;
    }

    public boolean isResult() {
        return isResult;
    }

    public boolean isFinish() {
        return finish;
    }

    public BundleManager withString(@NonNull String key, @NonNull String value) {
        mBundle.putString(key, value);
        return this;
    }

    public BundleManager withResultString(@NonNull String key, @NonNull String value) {
        mBundle.putString(key, value);
        isResult = true;
        return this;
    }

    public BundleManager withBoolean(@NonNull String key, boolean value) {
        mBundle.putBoolean(key, value);
        return this;
    }


    public BundleManager withInt(@NonNull String key, int value) {
        mBundle.putInt(key, value);
        return this;
    }

    public BundleManager withBundle(@NonNull Bundle bundle) {
        mBundle = bundle;
        return this;
    }

    //直接跳转 startActivity
    public Object navigation(Context context) {
        return navigation(context, -1);
    }


    //forResult code 可以是requestCode  也可以是responseCode
    public Object navigation(Context context, int code) {

        return RouterManager.getInstance().navigation(context, this, code);
    }
}
