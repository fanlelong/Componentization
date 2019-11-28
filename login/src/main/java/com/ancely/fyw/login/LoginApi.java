package com.ancely.fyw.login;

import com.ancely.fyw.aroute.bean.HttpResult;
import com.ancely.fyw.login.bean.LoginBean;
import com.ancely.fyw.login.bean.RegisterBean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.login
 *  @文件名:   LoginApi
 *  @创建者:   fanlelong
 *  @创建时间:  2019/11/13 11:21 AM
 *  @描述：    TODO
 */
public interface LoginApi {

    @FormUrlEncoded
    @POST("user/login")
    Observable<HttpResult<LoginBean>> login(@FieldMap Map<String, Object> params);


    @FormUrlEncoded
    @POST("user/register")
    Observable<HttpResult<RegisterBean>> register(@FieldMap Map<String, Object> params);
}
