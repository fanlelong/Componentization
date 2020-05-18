package com.plugin.text;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/*
 *  @项目名：  Componentization
 *  @包名：    com.plugin.text
 *  @文件名:   PluginApi
 *  @创建者:   fanlelong
 *  @创建时间:  2020/5/17 1:25 AM
 *  @描述：    TODO
 */
public interface PluginApi {
    @FormUrlEncoded
    @POST("user/login")
    Observable<String> login(@FieldMap Map<String, Object> params);


    @FormUrlEncoded
    @POST("user/register")
    Observable<String> register(@FieldMap Map<String, Object> params);
}
