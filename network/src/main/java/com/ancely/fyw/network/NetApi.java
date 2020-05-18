package com.ancely.fyw.network;

import io.reactivex.Observable;
import retrofit2.http.GET;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.network
 *  @文件名:   NetApi
 *  @创建者:   fanlelong
 *  @创建时间:  2020/5/17 1:03 AM
 *  @描述：    TODO
 */
public interface NetApi {
    @GET
    Observable<String> getString();
}
