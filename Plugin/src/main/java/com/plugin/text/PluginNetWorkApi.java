package com.plugin.text;

import com.ancely.fyw.network.NetWorkApi;

import io.reactivex.functions.Function;
import okhttp3.Interceptor;

/*
 *  @项目名：  Componentization
 *  @包名：    com.plugin.text
 *  @文件名:   PluginNetWorkApi
 *  @创建者:   fanlelong
 *  @创建时间:  2020/5/17 2:02 AM
 *  @描述：    TODO
 */
public class PluginNetWorkApi extends NetWorkApi {

    private static PluginNetWorkApi sInstance;

    public static PluginNetWorkApi getInstance() {
        if (sInstance == null) {
            synchronized (PluginNetWorkApi.class) {
                if (sInstance != null) {
                    sInstance = new PluginNetWorkApi();
                }
            }
        }
        return sInstance;
    }

    protected PluginNetWorkApi() {
        super("https://www.tencent.com/");
    }

    @Override
    protected Interceptor getInterceptor() {
        return null;
    }

    @Override
    protected <T> Function<T, T> getAppErrorHandler() {
        return null;
    }

    @Override
    public String getBaseUrlName() {
        return null;
    }

    @Override
    public String getAppVersionCode() {
        return null;
    }

    @Override
    public boolean isDebug() {
        return false;
    }

    public static <T> T getService(Class<T> service) {
        return getInstance().getRetrofit(service).create(service);
    }

    @Override
    public String getRelease() {
        return null;
    }

    @Override
    public String getTest() {
        return null;
    }
}
