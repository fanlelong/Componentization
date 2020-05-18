package com.ancely.pay;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.pay
 *  @文件名:   PayApi
 *  @创建者:   fanlelong
 *  @创建时间:  2020/2/15 3:45 PM
 *  @描述：    TODO
 */
public interface PayApi {
    @POST
    Observable<String> postPay(@Url String url, @Body Map<String, Object> params);
}
