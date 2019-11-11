package com.ancely.fyw.aroute.proxy;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.LayoutInflaterCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ancely.fyw.aroute.manager.PluginManager;
import com.ancely.fyw.aroute.skin.impl.ViewsMatch;
import com.ancely.fyw.aroute.skin.utils.NavigationUtils;
import com.ancely.fyw.aroute.skin.utils.StatusBarUtils;
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
            return mViewInflater.autoMatch();
        }
        return super.onCreateView(parent, name, context, attrs);
    }

    private boolean openChangerSkin() {
        return false;
    }


    @Override
    public Resources getResources() {
        return PluginManager.getInstance().getResources();
    }

    @Override
    public ClassLoader getClassLoader() {
        return PluginManager.getInstance().getClassLoader();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mInterface.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mInterface.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mInterface.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mInterface.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mInterface.onDestroy();
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


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void defaultSkin(int themeColorId) {
        this.skinDynamic(null, themeColorId);
    }

    /**
     * 动态换肤（api限制：5.0版本）
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void skinDynamic(String skinPath, int themeColorId) {
        PluginManager.getInstance().loadPluginPath(skinPath);

        if (themeColorId != 0) {
            int themeColor = PluginManager.getInstance().getColor(themeColorId);
            StatusBarUtils.forStatusBar(this, themeColor);
            NavigationUtils.forNavigation(this, themeColor);
        }

        applyViews(getWindow().getDecorView());
    }

    /**
     * 控件回调监听，匹配上则给控件执行换肤方法
     */
    protected void applyViews(View view) {
        if (view instanceof ViewsMatch) {
            ViewsMatch viewsMatch = (ViewsMatch) view;
            viewsMatch.startChangerSkin();
        }

        if (view instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) view;
            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                applyViews(parent.getChildAt(i));
            }
        }
    }
}
