package com.ancely.fyw.network;

import com.ancely.fyw.aroute.networks.okhttp.HttpLoggingInterceptor;
import com.ancely.fyw.aroute.networks.okhttp.ProgressInterceptor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.network
 *  @文件名:   NetWorkApi
 *  @创建者:   fanlelong
 *  @创建时间:  2020/5/17 1:01 AM
 *  @描述：    TODO
 */
public abstract class NetWorkApi implements INetworkRequiredInfo,IEnviroment {
    private String BASE_URL = "https://www.wanandroid.com/";
    private static INetworkRequiredInfo sRequiredInfo;
    private static Map<String, Retrofit> sRetrofitMap = new HashMap<>();
    private OkHttpClient mOkHttpClient;

    public static void init(INetworkRequiredInfo requiredInfo) {
        sRequiredInfo = requiredInfo;
    }

    protected NetWorkApi(String BASE_URL) {
        this.BASE_URL = BASE_URL;
    }

    public Retrofit getRetrofit(Class servicce) {
        Retrofit retrofit = sRetrofitMap.get(BASE_URL + servicce.getName());
        if (retrofit != null) {
            return retrofit;
        }
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(BASE_URL);
        builder.client(getOkhttpClient());
        builder.addConverterFactory(GsonConverterFactory.create());
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        retrofit = builder.build();
        sRetrofitMap.put(BASE_URL + servicce.getName(), retrofit);
        return retrofit;
    }


    private OkHttpClient getOkhttpClient() {
        if (mOkHttpClient != null) {
            return mOkHttpClient;
        }
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor("ancelyOkhttp:");
        httpLoggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        httpLoggingInterceptor.setColorLevel(Level.INFO);
        // 初始化okhttp
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new ProgressInterceptor())
                .addInterceptor(chain -> {
                    //默认添加 Accept-Language
                    //默认添加 User-Agent
                    Request.Builder builder1 = chain.request().newBuilder();
                    Request request = builder1
                            .addHeader("Content-type", "application/json; charset=utf-8")
                            .addHeader("Accept", "application/json")
                            .build();
                    return chain.proceed(request);
                })
                .addInterceptor(httpLoggingInterceptor)
                .readTimeout(60000, TimeUnit.MILLISECONDS)
                .writeTimeout(60000, TimeUnit.MILLISECONDS)
                .connectTimeout(60000, TimeUnit.MILLISECONDS);
        mOkHttpClient = builder.build();
        return mOkHttpClient;

    }

    public <T> ObservableTransformer<T, T> applySchedulers(Observer<T> observer) {

        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                Observable<T> observable = upstream.subscribeOn(Schedulers.io())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .map(getAppErrorHandler()).onErrorResumeNext(new HttpErrorHandler<>());
                observable.subscribe(observer);
                return observable;
            }
        };
    }

    protected abstract Interceptor getInterceptor();

    protected abstract <T> Function<T, T> getAppErrorHandler();
}
