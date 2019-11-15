package com.ancely.fyw.login.viewmodel;


import com.ancely.fyw.aroute.bean.HttpResult;
import com.ancely.fyw.aroute.model.BaseViewModel;
import com.ancely.fyw.aroute.model.bean.ResponseBean;
import com.ancely.fyw.login.bean.LoginBean;


public class LoginVM extends BaseViewModel<HttpResult<LoginBean>> {

    @Override
    public void hanlerDataRequestSuccess(ResponseBean<HttpResult<LoginBean>> responseBean) {


    }
}
