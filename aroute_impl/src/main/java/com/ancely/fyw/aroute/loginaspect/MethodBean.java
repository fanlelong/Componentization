package com.ancely.fyw.aroute.loginaspect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.aroute.loginaspect
 *  @文件名:   MethodBean
 *  @创建者:   fanlelong
 *  @创建时间:  2020/5/9 9:20 PM
 *  @描述：    TODO
 */
public class MethodBean {
    private Object mProxy;
    private Method mMethod;
    private Object[] mArgs;

    public MethodBean(Object proxy, Method method, Object[] args) {
        mProxy = proxy;
        mMethod = method;
        mArgs = args;
    }

    public void invoke() {
        try {
            mMethod.invoke(mProxy, mArgs);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
