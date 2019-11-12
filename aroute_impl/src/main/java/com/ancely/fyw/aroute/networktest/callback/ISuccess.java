package com.ancely.fyw.aroute.networktest.callback;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.aroute.network.callback
 *  @文件名:   ISuccess
 *  @创建者:   fanlelong
 *  @创建时间:  2019/11/12 10:00 AM
 *  @描述：    请求成功
 */
public interface ISuccess<T> {

    void onSuccess(T response);
}
