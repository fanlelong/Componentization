package com.ancely.fyw.aroute.networktest;

import com.ancely.fyw.aroute.networktest.callback.IError;
import com.ancely.fyw.aroute.networktest.callback.IFailure;
import com.ancely.fyw.aroute.networktest.callback.IRequest;
import com.ancely.fyw.aroute.networktest.callback.ISuccess;
import com.ancely.fyw.aroute.networktest.model.HttpMethod;
import com.ancely.fyw.aroute.networktest.rxbus.databus.RxBus;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.aroute.network.model
 *  @创建者:   fanlelong
 *  @创建时间:  2019/11/12 10:24 AM
 *  @描述：    请求客户端
 */
public class RequestClient {
    private Map<String, Object> params = new HashMap<>();
    private String url;
    private IRequest request;
    private ISuccess success;
    private IError error;
    private IFailure failure;
    private RequestBody requestBody;

    //上传 下载

    File file;
    private String downLoadDir;
    private String extension;
    private String fileName;

    public RequestClient(Builder builder) {
        this.params = builder.params;
        this.url = builder.url;
        this.request = builder.request;
        this.success = builder.success;
        this.error = builder.error;
        this.failure = builder.failure;
        this.requestBody = builder.requestBody;
        this.downLoadDir = builder.downLoadDir;
        this.extension = builder.extension;
        this.fileName = builder.fileName;
    }

    public static Builder create() {
        return new Builder();
    }

    private static class Builder {

        private Map<String, Object> params = new HashMap<>();
        private String url;
        private IRequest request;
        private ISuccess success;
        private IError error;
        private IFailure failure;
        private RequestBody requestBody;

        //上传 下载

        File file;
        private String downLoadDir;
        private String extension;
        private String fileName;

        public Builder params(Map<String, Object> params) {
            this.params = params;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder request(IRequest request) {
            this.request = request;
            return this;
        }

        public Builder iSuccess(ISuccess success) {
            this.success = success;
            return this;
        }

        public Builder iError(IError error) {
            this.error = error;
            return this;
        }

        public Builder iFailure(IFailure failure) {
            this.failure = failure;
            return this;
        }

        public Builder requestBody(RequestBody requestBody) {
            this.requestBody = requestBody;
            return this;
        }

        public Builder file(File file) {
            this.file = file;
            return this;
        }

        public Builder downLoadDir(String downLoadDir) {
            this.downLoadDir = downLoadDir;
            return this;
        }

        public Builder extension(String extension) {
            this.extension = extension;
            return this;
        }

        public Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public RequestClient builder() {
            return new RequestClient(this);
        }
    }

    protected Observable requestAction(HttpMethod httpMethod) {
        RetrofitService retrofitService = RetrofitCreate.getRetrofitService();

        if (request != null) {
            request.onRequestStart();
        }
        Observable<String> observable = null;
        switch (httpMethod) {
            case GET:
                observable = retrofitService.get(url, params);
                break;
            case POST:
                observable = retrofitService.post(url, params);
                break;
        }

        return observable;
    }

    public Observable get() {
        return requestAction(HttpMethod.GET);
    }

    public  void test(){
        RxBus.getInstance().doProcessInvoke(RequestClient.create().builder().get(),this.getClass());
    }
}
