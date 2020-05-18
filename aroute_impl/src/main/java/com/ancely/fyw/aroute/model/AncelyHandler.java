package com.ancely.fyw.aroute.model;

import com.ancely.fyw.aroute.loginaspect.MethodBean;
import com.ancely.fyw.aroute.manager.NetWorkManager;
import com.ancely.fyw.aroute.manager.RouterManager;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.aroute.model
 *  @文件名:   AncelyHandler
 *  @创建者:   fanlelong
 *  @创建时间:  2020/5/9 1:21 AM
 *  @描述：    判断是否登陆
 */
public class AncelyHandler implements InvocationHandler {
    public static final String REQUEST_METHOD = "requestDatasFromService";
    public static final String LOGIN_SUCCESS = "loginSuccess";
    private Map<Object, MethodBean> mMethodBeanMap = new HashMap<>();
    private static AncelyHandler sInstance;

    public static AncelyHandler getInstance() {
        if (sInstance == null) {
            synchronized (AncelyHandler.class) {
                if (sInstance == null) {
                    sInstance = new AncelyHandler();
                }
            }
        }
        return sInstance;
    }

    private AncelyHandler() {
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if (REQUEST_METHOD.equals(method.getName())) {
            if (!NetWorkManager.getInstance().isLogin()) {
                mMethodBeanMap.put(args[0], new MethodBean(proxy, method, args));
                navigation();
            }

        } else if (LOGIN_SUCCESS.equals(method.getName())) {
            invokeLastMethod();
        }
        //在这里判断是否是登陆,如果没有登陆,则跳转
        return null;
    }

    private void invokeLastMethod() {
        for (Object proxy : mMethodBeanMap.keySet()) {
            MethodBean methodBean = mMethodBeanMap.get(proxy);
            if (methodBean != null) {
                methodBean.invoke();
            }
        }
    }

    public void removeModelP(ModelP modelP) {
        mMethodBeanMap.remove(modelP);
    }

    public Object navigation() {
        return RouterManager.getInstance().build("/login/LoginActivity").navigation(NetWorkManager.getInstance().getContext(), 0x100);
    }
}
