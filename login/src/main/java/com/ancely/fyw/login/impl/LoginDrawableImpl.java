package com.ancely.fyw.login.impl;

import com.ancely.fyw.common.LoginCall;
import com.ancely.fyw.login.R;

import con.ancely.fyw.annotation.apt.ARouter;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.login.impl
 *  @文件名:   LoginDrawableImpl
 *  @创建者:   fanlelong
 *  @创建时间:  2019/8/21 11:44 AM
 *  @描述：    TODO
 */
@ARouter(path = "/login/getDrawable")
public class LoginDrawableImpl implements LoginCall {
    @Override
    public int getDrawable() {
        return R.drawable.login_ic_mouse_black_24dp;
    }
}
