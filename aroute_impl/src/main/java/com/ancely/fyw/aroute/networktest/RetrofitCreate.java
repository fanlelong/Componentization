package com.ancely.fyw.aroute.networktest;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.aroute.network
 *  @文件名:   RetrofitCreate
 *  @创建者:   fanlelong
 *  @创建时间:  2019/11/12 10:29 AM
 *  @描述：    Retrofit创建者
 */
public class RetrofitCreate {

    private final static class RetrofitHolder {
        private final static String BASE_URL = "https://";
        private final static Retrofit RETROFIT = new Retrofit.Builder()
                .baseUrl(BASE_URL).addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(OkhttpHolder.OK_HTTP_CLIENT)
                .build();
    }

    private final static class OkhttpHolder {
        private static final int TIME_OUT = 60;
        private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient.Builder()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .build();
    }


    public static RetrofitService getRetrofitService() {

        return RetrofitHolder.RETROFIT.create(RetrofitService.class);
    }
}
