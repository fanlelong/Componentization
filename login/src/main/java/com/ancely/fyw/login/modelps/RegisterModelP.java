package com.ancely.fyw.login.modelps;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.ancely.fyw.aroute.bean.HttpResult;
import com.ancely.fyw.aroute.manager.NetWorkManager;
import com.ancely.fyw.aroute.model.BaseViewModel;
import com.ancely.fyw.aroute.model.ModelP;
import com.ancely.fyw.login.LoginApi;
import com.ancely.fyw.login.bean.LoginBean;
import com.ancely.fyw.login.bean.RegisterBean;
import com.ancely.fyw.login.viewmodel.RegisterVM;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.login.modelps
 *  @文件名:   RegisterModelP
 *  @创建者:   fanlelong
 *  @创建时间:  2019/11/13 2:08 PM
 *  @描述：    TODO
 */
public class RegisterModelP extends ModelP<HttpResult<RegisterBean>> {
    public RegisterModelP(@NonNull Fragment fragment) {
        super(fragment);
    }

    @Override
    public Class<? extends BaseViewModel<HttpResult<RegisterBean>>> getVMClass() {
        return RegisterVM.class;
    }

    @Override
    protected Observable<HttpResult<RegisterBean>> getObservable(Map<String, Object> map, int flag) {
        return NetWorkManager.getInstance().getRetrofit().create(LoginApi.class).register(map);
    }

    @Override
    public void showProgress(int flag) {
        Disposable subscribe = NetWorkManager.getInstance().getRetrofit()
                .create(LoginApi.class)
                .register(new HashMap<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                //注册完之后,更新注册UI
                .doOnNext(new Consumer<HttpResult<RegisterBean>>() {
                    @Override
                    public void accept(HttpResult<RegisterBean> loginBeanHttpResult) throws Exception {
                        //更新注册UI
                    }
                })
                //马上去请求登陆请求
                .subscribeOn(Schedulers.io())//给上游戏分配io线程
                .flatMap(new Function<HttpResult<RegisterBean>, ObservableSource<HttpResult<LoginBean>>>() {
                    @Override
                    public ObservableSource<HttpResult<LoginBean>> apply(HttpResult<RegisterBean> registerBeanHttpResult) throws Exception {

                        //这里可以拿到注册的信息,然后去执行登录操作
                        Observable<HttpResult<LoginBean>> login = NetWorkManager.getInstance().getRetrofit()
                                .create(LoginApi.class)
                                .login(new HashMap<>());
                        return login;
                    }
                })
                //登陆完之后,开始更新登陆UI
                .observeOn(AndroidSchedulers.mainThread())//把线程切换过来
                .subscribe(new Consumer<HttpResult<LoginBean>>() {
                    @Override
                    public void accept(HttpResult<LoginBean> loginBeanHttpResult) throws Exception {
                        //这里就可以操作登陆成功的操作
                    }
                });


    }

    @Override
    public void hideProgress(int flag) {

    }
}
