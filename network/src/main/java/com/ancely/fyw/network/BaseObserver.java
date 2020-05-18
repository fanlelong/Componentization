package com.ancely.fyw.network;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.network
 *  @文件名:   BaseObserver
 *  @创建者:   fanlelong
 *  @创建时间:  2020/5/17 12:59 AM
 *  @描述：    TODO
 */
public abstract class BaseObserver<T> implements Observer<T> {
    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        onFailure(e);
    }

    @Override
    public void onComplete() {

    }

    public abstract void onSuccess(T t);

    public abstract void onFailure(Throwable e);
}
