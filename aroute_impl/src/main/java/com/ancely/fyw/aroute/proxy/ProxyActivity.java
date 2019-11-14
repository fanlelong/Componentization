package com.ancely.fyw.aroute.proxy;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.ancely.fyw.aroute.manager.PluginManager;
import com.ancely.fyw.aroute.skin.view.SelfAppCompatViewInflater;


/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.aroute.proxy
 *  @文件名:   ProxyActivity
 *  @创建者:   fanlelong
 *  @创建时间:  2019/8/21 2:13 PM
 *  @描述：    插件化 代理类activity
 */
public class ProxyActivity extends Activity {
    private ActivityInterface mInterface;

    private SelfAppCompatViewInflater mViewInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (openChangerSkin()) {
            LayoutInflater inflater = LayoutInflater.from(this);
            LayoutInflaterCompat.setFactory2(inflater, this);
        }

        View decorView = getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(option);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        super.onCreate(savedInstanceState);
        String className = getIntent().getStringExtra("className");
        try {
            Class<?> aClass = PluginManager.getInstance().getClassLoader().loadClass(className);
            Object instance = aClass.newInstance();
            if (instance instanceof ActivityInterface) {
                mInterface = (ActivityInterface) instance;
                mInterface.attach(this);
                Bundle bundle = new Bundle();
                mInterface.onCreate(bundle);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        if (openChangerSkin()) {
            if (mViewInflater == null) {
                mViewInflater = new SelfAppCompatViewInflater(context);
            }
            mViewInflater.setName(name);
            mViewInflater.setAttrs(attrs);
            View view = mViewInflater.autoMatch();
            return view != null ? view : super.onCreateView(parent, name, context, attrs);
        }
        return super.onCreateView(parent, name, context, attrs);
    }

    private boolean openChangerSkin() {
        return false;
    }


    @Override
    public Resources getResources() {
        if (mInterface != null) return PluginManager.getInstance().getResources();
        return super.getResources();
    }

    @Override
    public ClassLoader getClassLoader() {
        if (mInterface != null) return PluginManager.getInstance().getClassLoader();
        return super.getClassLoader();
    }

    @Override
    public AssetManager getAssets() {
        if (mInterface != null) return PluginManager.getInstance().getAssetManager();
        return super.getAssets();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mInterface != null) mInterface.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mInterface != null) mInterface.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mInterface != null) mInterface.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mInterface != null) mInterface.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mInterface != null) mInterface.onDestroy();
    }

    @Override
    public void startActivity(Intent intent) {
        String className = intent.getStringExtra("className");
        Intent intent_one = new Intent(this, ProxyActivity.class);
        //拿到插件apk的第一个activity的名字
        intent_one.putExtra("className", className);
        super.startActivity(intent_one);
    }

    @Override
    public ComponentName startService(Intent service) {
        String className = service.getStringExtra("className");
        Intent intentService = new Intent(this, ProxyService.class);
        intentService.setAction("com.ancely.fyw.aroute.proxy.Proxyservice");
        intentService.putExtra("className", className);
        return super.startService(intentService);
    }

    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {

        String className = receiver.getClass().getName();

        return super.registerReceiver(new ProxyReceiver(className), filter);
    }

    @Override
    public void sendBroadcast(Intent intent) {
        super.sendBroadcast(intent);
    }


}
