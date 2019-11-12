package com.ancely.fyw.aroute.model;


import com.ancely.fyw.aroute.model.bean.ResponseBean;

import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.Disposable;

public interface IBaseModelP<T> {

    void accessSucceed(ResponseBean<T> responseBean, int flag, boolean isShowLoading);

    void accessMoreSuccess(ResponseBean<T> responseBean, int flag, boolean isShowLoading);

    void accessError(int code, String errorMsg, ResponseBean<T> responseBean);

    void unDisposable();

    void disposable(Disposable s);

    ObservableTransformer<T,T> getTransformer();


}
