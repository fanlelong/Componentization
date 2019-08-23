package com.ancely.fyw.aroute.base;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import java.lang.ref.WeakReference;

/*
 *  @项目名：  ShareAndroid
 *  @包名：    com.ancely.mvp
 *  @文件名:   BasePresenter
 *  @创建者:   fanlelong
 *  @创建时间:  2019/7/25 1:53 PM
 *  @描述：    TODO
 */
public abstract class BasePresenter<VM extends BaseModel, V extends IBaseView, CONTRACT> {
    //    private R mRequest;
    private VM mBaseModel;
    protected V mBaseView;
    private WeakReference<? extends LifecycleOwner> mWeakReference;

    public BasePresenter(@NonNull Fragment fragment, V baseView) {
        mBaseModel = ViewModelProviders.of(fragment).get(getModelClass());
        mBaseView = baseView;
        mBaseModel.setPresenter(this);
        mWeakReference = new WeakReference<>(fragment);
//        NetWorkManager.getInstance().getRequestManagerRetriever().get(fragment, this);
//        mRequest = NetWorkManager.getInstance().getRetrofit().create(getClazz());
        initObserable(mBaseModel);
    }

    public BasePresenter(@NonNull FragmentActivity fragment, V baseView) {
        mBaseModel = ViewModelProviders.of(fragment).get(getModelClass());
        mBaseView = baseView;
        mBaseModel.setPresenter(this);
        mWeakReference = new WeakReference<>(fragment);
//        NetWorkManager.getInstance().getRequestManagerRetriever().get(fragment, this);
//        mRequest = NetWorkManager.getInstance().getRetrofit().create(getClazz());
        initObserable(mBaseModel);
    }

    protected abstract void initObserable(VM baseModel);

    protected abstract Class<VM> getModelClass();

    protected abstract CONTRACT getContract();

    public VM getBaseModel() {
        return mBaseModel;
    }

    public LifecycleOwner getView() {
        if (mWeakReference.get() != null) {
            return mWeakReference.get();
        }
        return null;
    }
}
