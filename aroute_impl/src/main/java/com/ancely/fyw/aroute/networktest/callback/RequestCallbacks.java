package com.ancely.fyw.aroute.networktest.callback;

import android.support.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.aroute.network.callback
 *  @文件名:   RequestCallbacks
 *  @创建者:   fanlelong
 *  @创建时间:  2019/11/12 10:01 AM
 *  @描述：    请求回调
 */
public class RequestCallbacks<T> implements Callback<T> {
    private IRequest mIRequest;
    private ISuccess<T> mISuccess;
    private IError mIError;
    private IFailure mIFailure;

    public RequestCallbacks(IRequest IRequest, ISuccess<T> ISuccess, IError IError, IFailure IFailure) {
        mIRequest = IRequest;
        mISuccess = ISuccess;
        mIError = IError;
        mIFailure = IFailure;
    }

    @Override
    public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
        if (response.isSuccessful() && call.isExecuted() && mISuccess != null) {
            mISuccess.onSuccess(response.body());
        } else if (mIError != null) {
            mIError.onError(response.code(), response.message());
        }

        if (mIRequest != null) {
            mIRequest.onRequestEnd();
        }
    }

    @Override
    public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
        if (mIFailure != null) {
            mIFailure.onFailure();
        }

        if (mIRequest != null) {
            mIRequest.onRequestEnd();
        }
    }
}
