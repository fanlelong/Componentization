package com.ancely.fyw.aroute.life;

/*
 *  @项目名：  BaseMvp
 *  @包名：    com.ancely.rxjava.mvp
 *  @文件名:   LifeFragment
 *  @创建者:   fanlelong
 *  @创建时间:  2018/8/28 上午10:01
 *  @描述：    无UI的fragment
 */

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;


public class LifeFragment extends Fragment {
    ActivityFragmentLifecycle mLifecycle;

    @Nullable
    private LifeManager mLifeManager;



    public LifeFragment() {
        this(new ActivityFragmentLifecycle());//创建了生命周期管理者
    }

    @VisibleForTesting
    @SuppressLint("ValidFragment")
    public LifeFragment(@NonNull ActivityFragmentLifecycle lifecycle) {
        mLifecycle = lifecycle;
    }

    @NonNull
    ActivityFragmentLifecycle getModelLifecycle() {
        return mLifecycle;
    }

    @Nullable
    public LifeManager getLifeManager() {
        return mLifeManager;
    }

    public void setLifeManager(@Nullable LifeManager lifeManager) {
        mLifeManager = lifeManager;
    }


    @Override
    public void onStart() {
        super.onStart();
        mLifecycle.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mLifecycle.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLifecycle.onDestroy();
    }
}
