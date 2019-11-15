package com.ancely.fyw.aroute.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.ancely.fyw.aroute.R;
import com.ancely.fyw.aroute.manager.PluginManager;
import com.ancely.fyw.aroute.proxy.ActivityInterface;
import com.ancely.fyw.aroute.skin.impl.ViewsMatch;
import com.ancely.fyw.aroute.skin.utils.ActionBarUtils;
import com.ancely.fyw.aroute.skin.utils.NavigationUtils;
import com.ancely.fyw.aroute.skin.utils.PreferencesUtils;
import com.ancely.fyw.aroute.skin.utils.StatusBarUtils;
import com.ancely.fyw.aroute.skin.view.SelfAppCompatViewInflater;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.aroute
 *  @文件名:   BaseActivity
 *  @创建者:   fanlelong
 *  @创建时间:  2019/8/21 1:56 PM
 *  @描述：    Activity基类
 */
public class BaseActivity extends AppCompatActivity implements ActivityInterface {

    protected Activity mActivity;
    private SelfAppCompatViewInflater mViewInflater;
    private Activity mContext;

    @Override
    public void attach(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        if (mActivity == null) {
            if (openChangerSkin()) {
                LayoutInflater inflater = LayoutInflater.from(this);
                LayoutInflaterCompat.setFactory2(inflater, this);
            }
            super.onCreate(savedInstanceState);
        } else {
            if (openChangerSkin()) {
                LayoutInflater inflater = LayoutInflater.from(mActivity);
                LayoutInflaterCompat.setFactory2(inflater, mActivity);
            }
        }
        mContext = mActivity == null ? this : mActivity;
        AppCompatDelegate.setDefaultNightMode(Configuration.UI_MODE_NIGHT_NO);
        boolean isNight = PreferencesUtils.getBoolean(mContext, "isNight");
        if (isNight) {
            setDayNightModel(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            setDayNightModel(AppCompatDelegate.MODE_NIGHT_NO);
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
            return view == null ? super.onCreateView(parent, name, context, attrs) : view;
        }

        return super.onCreateView(parent, name, context, attrs);
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
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        if (mActivity == null) {
            return super.registerReceiver(receiver, filter);
        }
        return mActivity.registerReceiver(receiver, filter);
    }

    @Override
    public void sendBroadcast(Intent intent) {
        if (mActivity == null) {
            super.sendBroadcast(intent);
        } else {
            mActivity.sendBroadcast(intent);
        }
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

    public boolean openChangerSkin() {
        return false;
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
            ActionBarUtils.forActionBar(this, themeColor);
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

    @SuppressLint("ObsoleteSdkInt")
    public void setDayNightModel(@AppCompatDelegate.NightMode int uiModeNightYes) {
        boolean isVersion21 = Build.VERSION.SDK_INT >= 21;
        if (mActivity == null) {
            getDelegate().setLocalNightMode(uiModeNightYes);
            if (isVersion21) {
                StatusBarUtils.forStatusBar(this, getResources().getColor(R.color.colorPrimary));
                ActionBarUtils.forActionBar(this, getResources().getColor(R.color.colorPrimary));
                NavigationUtils.forNavigation(this, getResources().getColor(R.color.colorPrimary));
            }
            View decorView = getWindow().getDecorView();
            applyViews(decorView);
        }
    }
}
