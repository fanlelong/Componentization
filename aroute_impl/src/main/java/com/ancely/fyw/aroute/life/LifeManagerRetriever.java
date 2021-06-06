package com.ancely.fyw.aroute.life;

/*
 *  @项目名：  BaseMvp
 *  @包名：    com.ancely.rxjava.life
 *  @创建者:   fanlelong
 *  @创建时间:  2018/8/28 下午1:37
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.ancely.fyw.aroute.model.ModelP;

import java.util.HashMap;
import java.util.Map;

public class LifeManagerRetriever implements Handler.Callback {
    private static final String FRAGMENT_TAG = "com.ancely.rxjava.life";
    private final Map<FragmentManager, LifeFragment> pendingSupportRequestManagerFragments = new HashMap<>();

    private final Handler handler;

    public LifeManagerRetriever() {
        handler = new Handler(Looper.getMainLooper(), this);
    }


    @NonNull
    public LifeManager get(@NonNull FragmentActivity activity, ModelP presenter) {
        assertNotDestroyed(activity);//先判断activity没有销毁

        FragmentManager fm = activity.getSupportFragmentManager();//获取fragment管理

        return supportFragmentGet(activity, fm, isActivityVisible(activity), presenter);
    }

    @NonNull
    public LifeManager get(@NonNull Fragment fragment, ModelP presenter) {
        checkNotNull(fragment.getActivity(), "You cannot start a load on a fragment before it is attached or after it is destroyed");//先判断activity没有销毁

        FragmentManager fm = fragment.getChildFragmentManager();//获取fragment管理

        return supportFragmentGet(fragment.getActivity(), fm, fragment.isVisible(), presenter);
    }

    private static <T> void checkNotNull(@Nullable T arg, @NonNull String message) {
        if (arg == null) {
            throw new NullPointerException(message);
        }
    }

    @NonNull
    private LifeManager supportFragmentGet(@NonNull Context context, @NonNull FragmentManager fm,
                                           boolean isParentVisible, ModelP presenter) {
        //获取到一个隐藏的fragment
        LifeFragment current = getSupportRequestManagerFragment(fm, isParentVisible);

        LifeManager lifeManager = current.getLifeManager();//获取到最终管理类
        if (lifeManager == null) {//没有则创建,并将其设置进去
            lifeManager = new LifeManager(current.getModelLifecycle(), presenter);
            current.setLifeManager(lifeManager);
        } else {
            lifeManager.addPresenter(presenter);
        }
        return lifeManager;
    }

    private static boolean isActivityVisible(Activity activity) {
        return !activity.isFinishing();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static void assertNotDestroyed(@NonNull Activity activity) {
        if (activity.isDestroyed()) {
            throw new IllegalArgumentException("You cannot start a load for a destroyed activity");
        }
    }

    @NonNull
    private LifeFragment getSupportRequestManagerFragment(@NonNull final FragmentManager    fm, boolean isParentVisible) {
        //第一: 通过Tag找
        LifeFragment current = (LifeFragment) fm.findFragmentByTag(FRAGMENT_TAG);
        if (current == null) {
            //第二: 通过集合找,
            current = pendingSupportRequestManagerFragments.get(fm);
            if (current == null) {
                //第三: 都没有,创建
                current = new LifeFragment();
                if (isParentVisible) {//activity是否销毁了
                    current.getModelLifecycle().onStart();
                }
                //将其添加到集合中,
                pendingSupportRequestManagerFragments.put(fm, current);
                fm.beginTransaction().add(current, FRAGMENT_TAG).commitAllowingStateLoss();
                //最后再移除,之所以添加个集合就是为了不重复创建Fragment而出错
                handler.obtainMessage(ID_REMOVE_SUPPORT_FRAGMENT_MANAGER, fm).sendToTarget();
            }
        }
        return current;
    }

    private static final int ID_REMOVE_SUPPORT_FRAGMENT_MANAGER = 2;
    private static final String TAG = "RMRetriever";

    @Override
    public boolean handleMessage(Message message) {
        boolean handled = true;
        Object removed = null;
        Object key = null;
        if (message.what == ID_REMOVE_SUPPORT_FRAGMENT_MANAGER) {
            FragmentManager supportFm = (FragmentManager) message.obj;
            key = supportFm;
            removed = pendingSupportRequestManagerFragments.remove(supportFm);
        } else {
            handled = false;
        }
        if (handled && removed == null && Log.isLoggable(TAG, Log.WARN)) {
            Log.w(TAG, "Failed to remove expected request manager fragment, manager: " + key);
        }
        return handled;
    }
}
