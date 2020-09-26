package com.ancely.fyw.common.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.common.base
 *  @文件名:   BaseFragment
 *  @创建者:   fanlelong
 *  @创建时间:  2020/5/26 3:06 PM
 *  @描述：    TODO
 */
public abstract class BaseFragment extends Fragment {
    protected View mContentView;
    private Context mContext;
    protected boolean isLazyLoad;
    /**
     * 是否已经初始化结束
     */
    protected boolean isPrepare;

    protected boolean isFirstLoad;//是否只加载一次数据
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(setLayoutId(), container, false);
        return mContentView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if (!isLazyLoad) {
            loadData();
        }
        super.onActivityCreated(savedInstanceState);
    }

    protected abstract void loadData();

    public void setLazyLoad(boolean lazyLoad) {
        isLazyLoad = lazyLoad;
    }


    protected abstract void init();

    protected abstract int setLayoutId();

}
