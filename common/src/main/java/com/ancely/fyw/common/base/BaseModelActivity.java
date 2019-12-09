package com.ancely.fyw.common.base;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.ancely.fyw.aroute.base.BaseActivity;
import com.ancely.fyw.aroute.eventbus.EventBus;
import com.ancely.fyw.aroute.manager.Density;
import com.ancely.fyw.aroute.manager.NetWorkManager;
import com.ancely.fyw.aroute.model.ModelP;
import com.ancely.fyw.aroute.model.bean.RequestErrBean;
import com.ancely.fyw.aroute.model.bean.ResponseBean;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.common
 *  @文件名:   BaseModelActivity
 *  @创建者:   fanlelong
 *  @创建时间:  2019/11/13 10:55 AM
 */
public abstract class BaseModelActivity<P extends ModelP<T>, T> extends BaseActivity implements View.OnClickListener,IBaseView<T> {

    public Context mContext;
    protected boolean mIsFirstInto;//是否第一次进入界面
    public boolean isResevierrequest;//是否请求出现错误标记
    private P modelP;

    public abstract P getModelP();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (isFullScreen()) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            if (isFullScreen() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
        super.onCreate(savedInstanceState);


        Density.setDensity(getApplication(),this);
        mContext = this;

        NetWorkManager.getInstance().registerObserver(this);
        if (openEventBus()) {
            EventBus.getDefault().register(this);
        }
        int layoutId = getContentView();
        if (layoutId >= 0) {
            setContentView(layoutId);
        }
        modelP = getModelP();
        initView();

        if (modelP != null) {
            initObserver();
        }

        initEvent();
        initDatas();


    }

    private void initObserver() {
        modelP.getBaseViewModel().getErrorLiveData().observe(this, errBean -> {
            isResevierrequest = true;
            accessError(errBean);
        });
        modelP.getBaseViewModel().getResultLiveData().observe(this, responseBean -> {
            isResevierrequest = false;
            mIsFirstInto = true;
            accessSuccess(responseBean);
        });
        modelP.getBaseViewModel().getMoreLiveData().observe(this, responseBean -> {
            isResevierrequest = false;
            accessMoreSuccess(responseBean);
        });
        modelP.getBaseViewModel().getShowLoadingLiveData().observe(this, this::showloading);
        modelP.getBaseViewModel().getShowLoadingLiveData().observe(this, this::hideLoading);
    }

    protected abstract void initDatas();

    protected abstract void initEvent();

    protected abstract void initView();

    protected abstract int getContentView();



    @Override
    public void onClick(View v) {

    }


    @Override
    public void showloading(int flag) {

    }

    @Override
    public void hideLoading(int flag) {

    }

    @Override
    public void accessError(RequestErrBean errBean) {
    }


    @Override
    public void accessMoreSuccess(ResponseBean<T> responseBean) {
    }


    public boolean isFullScreen() {
        return true;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        NetWorkManager.getInstance().unRegisterObserver(this);
        if (openEventBus()) {
            EventBus.getDefault().unregister(this);
        }
    }

    public boolean openEventBus() {
        return false;
    }
}
