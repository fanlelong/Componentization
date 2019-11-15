package com.ancely.fyw.aroute.model;

/*
 *  viewmodel基类
 */

import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.ancely.fyw.aroute.model.bean.RequestErrBean;
import com.ancely.fyw.aroute.model.bean.ResponseBean;
import com.google.gson.Gson;

public abstract class BaseViewModel<T> extends ViewModel implements IBaseViewModel<T> {

    private MediatorLiveData<ResponseBean<T>> resultLiveData;
    private MediatorLiveData<ResponseBean<T>> moreLiveData;
    private MutableLiveData<RequestErrBean> errorLiveData;
    private MutableLiveData<Integer> showLoadingLiveData;
    private MutableLiveData<Integer> hideLoadingLiveData;
    private final Gson mGson = new Gson();


    public <R> R fromJson(String json, Class<R> classOfT) {
        return mGson.fromJson(json, classOfT);
    }

    @Override
    public MutableLiveData<ResponseBean<T>> getResultLiveData() {
        if (null == resultLiveData) {
            resultLiveData = new MediatorLiveData<>();
        }
        return resultLiveData;
    }


    @Override
    public MutableLiveData<ResponseBean<T>> getMoreLiveData() {
        if (null == moreLiveData) {
            moreLiveData = new MediatorLiveData<>();
        }
        return moreLiveData;
    }

    @Override
    public MutableLiveData<RequestErrBean> getErrorLiveData() {
        if (null == errorLiveData) {
            errorLiveData = new MutableLiveData<>();
        }
        return errorLiveData;
    }

    @Override
    public MutableLiveData<Integer> getShowLoadingLiveData() {
        if (null == showLoadingLiveData) {
            showLoadingLiveData = new MutableLiveData<>();
        }
        return showLoadingLiveData;
    }

    @Override
    public MutableLiveData<Integer> getHideLoadingLiveData() {
        if (null == hideLoadingLiveData) {
            hideLoadingLiveData = new MutableLiveData<>();
        }
        return hideLoadingLiveData;
    }
}
