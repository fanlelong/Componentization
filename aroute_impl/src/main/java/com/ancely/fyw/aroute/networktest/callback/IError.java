package com.ancely.fyw.aroute.networktest.callback;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.aroute.network.callback
 *  @文件名:   IError
 *  @创建者:   fanlelong
 *  @创建时间:  2019/11/12 9:57 AM
 *  @描述：    请求失败
 */
public interface IError {
    void onError(int code,String message);
}
