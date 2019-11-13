package com.ancely.fyw.login.modelps;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.ancely.fyw.aroute.bean.HttpResult;
import com.ancely.fyw.aroute.manager.NetWorkManager;
import com.ancely.fyw.aroute.model.BaseViewModel;
import com.ancely.fyw.aroute.model.ModelP;
import com.ancely.fyw.login.LoginApi;
import com.ancely.fyw.login.bean.LoginBean;
import com.ancely.fyw.login.viewmodel.RegisterVM;

import java.util.Map;

import io.reactivex.Observable;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.login.modelps
 *  @文件名:   RegisterModelP
 *  @创建者:   fanlelong
 *  @创建时间:  2019/11/13 2:08 PM
 *  @描述：    TODO
 */
public class RegisterModelP extends ModelP<HttpResult<LoginBean>> {
    public RegisterModelP(@NonNull Fragment fragment) {
        super(fragment);
    }

    @Override
    public Class<? extends BaseViewModel<HttpResult<LoginBean>>> getVMClass() {
        return RegisterVM.class;
    }

    @Override
    protected Observable<HttpResult<LoginBean>> getObservable(Map<String, Object> map, int flag) {
        return NetWorkManager.getInstance().getRetrofit().create(LoginApi.class).register(map);
    }

    @Override
    public void showProgress(int flag) {

    }

    @Override
    public void hideProgress(int flag) {

    }
}
