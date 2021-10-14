package com.ancely.fyw.aroute.core;

import com.ancely.fyw.annotation.apt.bean.RouteBean;

import java.util.Map;


/*
 *  @项目名：  zhujianhua
 *  @包名：    ancely.com.zjh.commmodel.api
 *  @文件名:   ARouteLoadPath
 *  @创建者:   fanlelong
 *  @创建时间:  2019/8/8 3:00 PM
 *  @描述：    路由组Group对应的详细Path加载数据接口
 *  比如: app分组下 对应的哪些类需要加载
 */
public interface ARouteLoadPath{

    /**
     * 加载路由组Group中的path详细数据
     */
    Map<String, RouteBean> loadPath();
}
