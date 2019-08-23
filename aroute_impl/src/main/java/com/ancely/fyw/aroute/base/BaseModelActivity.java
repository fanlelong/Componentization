package com.ancely.fyw.aroute.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.aroute.base
 *  @文件名:   BaseModelActivity
 *  @创建者:   fanlelong
 *  @创建时间:  2019/8/23 10:50 AM
 *  @描述：    TODO
 */
public abstract class BaseModelActivity<P extends BasePresenter> extends BaseActivity
        implements View.OnClickListener {

    protected P mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = getPresetner();

        initView();

        initListener();


        initDatas();


    }

    protected abstract P getPresetner();

    protected abstract void initDatas();

    protected abstract void initView();

    protected abstract void initListener();

    @Override
    public void onClick(View v) {

    }
}
