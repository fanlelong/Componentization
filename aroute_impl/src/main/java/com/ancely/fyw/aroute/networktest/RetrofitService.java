package com.ancely.fyw.aroute.networktest;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.aroute.network
 *  @文件名:   RetrofitService
 *  @创建者:   fanlelong
 *  @创建时间:  2019/11/12 10:36 AM
 *  @描述：    TODO
 */
public interface RetrofitService {

    @GET
    Observable<String> get(@Url String url, @QueryMap Map<String, Object> params);

    @GET
    Observable<String> get(@Url String url, @Query(value = "username") String username);

    @FormUrlEncoded
    @POST
    Observable<String> post(@Url String url, @FieldMap Map<String, Object> params);


    @Streaming
    @GET
    Observable<String> download(@Url String url, @QueryMap Map<String, Object> params);

    @Multipart
    @POST
    Observable<String> upload(@Url String url, @Part MultipartBody.Part part);

    //原始数据
    @POST
    Observable<String> postRaw(@Url String url, @Body RequestBody requestBody);
}
