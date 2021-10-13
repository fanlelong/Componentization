package com.ancely.fyw.annotation.apt.bean;

public interface SubscriberInfo {

    //订阅的类,比如Mainactivity
    Class<?> getSubscriberClass();

    //比如Mainactivity中有哪些订阅的方法
    SubscriberMethod[] getSubscriberMethods();
}