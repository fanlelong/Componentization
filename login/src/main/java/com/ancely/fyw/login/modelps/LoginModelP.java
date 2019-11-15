package com.ancely.fyw.login.modelps;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.ancely.fyw.aroute.bean.HttpResult;
import com.ancely.fyw.aroute.manager.NetWorkManager;
import com.ancely.fyw.aroute.model.BaseViewModel;
import com.ancely.fyw.aroute.model.ModelP;
import com.ancely.fyw.aroute.model.bean.ResponseBean;
import com.ancely.fyw.login.LoginApi;
import com.ancely.fyw.login.bean.LoginBean;
import com.ancely.fyw.login.viewmodel.LoginVM;

import java.util.Map;

import io.reactivex.Observable;


/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.login.modelps
 *  @文件名:   LoginModelP
 *  @创建者:   fanlelong
 *  @创建时间:  2019/11/13 11:16 AM
 */
public class LoginModelP extends ModelP<HttpResult<LoginBean>> {
    public LoginModelP(@NonNull Fragment fragment) {
        super(fragment);
    }

    @Override
    public Class<? extends BaseViewModel<HttpResult<LoginBean>>> getVMClass() {
        return LoginVM.class;
    }

    @Override
    protected Observable<HttpResult<LoginBean>> getObservable(Map<String, Object> map, int flag) {
        return NetWorkManager.getInstance().getRetrofit().create(LoginApi.class).login(map);
    }

    @Override
    public void showProgress(int flag) {

    }

    @Override
    public void hideProgress(int flag) {

    }

    @Override
    public boolean hanlerDataRequestSuccess(ResponseBean<HttpResult<LoginBean>> responseBean) {

        getBaseViewModel().hanlerDataRequestSuccess(responseBean);
        return true;
    }
}
