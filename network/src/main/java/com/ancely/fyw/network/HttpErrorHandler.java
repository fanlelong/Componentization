package com.ancely.fyw.network;

import com.ancely.fyw.aroute.networks.CustomException;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.network
 *  @文件名:   HttpErrorHandler
 *  @创建者:   fanlelong
 *  @创建时间:  2020/5/17 1:47 AM
 *  @描述：    TODO
 */
class HttpErrorHandler<T> implements Function<Throwable, Observable<T>> {


    @Override
    public Observable<T> apply(Throwable throwable) throws Exception {
        return Observable.error(CustomException.handleException(throwable));
    }
}