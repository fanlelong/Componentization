package com.ancely.fyw.aroute.core;

import java.util.Map;

/*
 *  @项目名：  zhujianhua
 *  @包名：    ancely.com.zjh.commmodel.api
 *  @文件名:   ARouteLoadGroup
 *  @创建者:   fanlelong
 *  @创建时间:  2019/8/8 2:57 PM
 *  @描述：   路由组对外提供加载数据接口
 */
public interface ARouteLoadGroup {
    /**
     * 加载路由组Group数据
     */

    Map<String, Class<? extends ARouteLoadPath>> loadGroup();
}
