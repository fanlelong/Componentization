package com.ancely.fyw.login.viewmodel;


import com.ancely.fyw.aroute.bean.HttpResult;
import com.ancely.fyw.aroute.model.BaseViewModel;
import com.ancely.fyw.aroute.model.bean.ResponseBean;
import com.ancely.fyw.login.bean.RegisterBean;


public class RegisterVM extends BaseViewModel<HttpResult<RegisterBean>> {
    @Override
    public void hanlerDataRequestSuccess(ResponseBean<HttpResult<RegisterBean>> t) {

    }
}
