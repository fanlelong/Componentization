package com.ancely.fyw.aroute.base;

import android.arch.lifecycle.ViewModel;

/*
 *  @项目名：  ShareAndroid
 *  @包名：    com.ancely.mvp
 *  @文件名:   BaseModel
 *  @创建者:   fanlelong
 *  @创建时间:  2019/7/25 1:53 PM
 *  @描述：    TODO
 */
public abstract class BaseModel<P extends BasePresenter, CONTRACT> extends ViewModel {

    protected P mPresenter;

    public abstract CONTRACT getContract();

    public void setPresenter(P p) {
        mPresenter = p;
    }
}
