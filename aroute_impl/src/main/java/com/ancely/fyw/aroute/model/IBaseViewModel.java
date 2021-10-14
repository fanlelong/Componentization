package com.ancely.fyw.aroute.model;

import android.arch.lifecycle.MutableLiveData;

import com.ancely.fyw.aroute.model.bean.RequestErrBean;
import com.ancely.fyw.aroute.model.bean.ResponseBean;


interface IBaseViewModel<T> {
    void hanlerDataRequestSuccess(ResponseBean<T> t);

    MutableLiveData<ResponseBean<T>> getResultLiveData();

    MutableLiveData<ResponseBean<T>> getMoreLiveData();

    SingleLiveEvent<RequestErrBean> getErrorLiveData();

    MutableLiveData<Integer> getShowLoadingLiveData();

    MutableLiveData<Integer> getHideLoadingLiveData();
}
