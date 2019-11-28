package com.ancely.fyw.compile.utils;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.compile.utils
 *  @文件名:   Constance
 *  @创建者:   fanlelong
 *  @创建时间:  2019/8/9 3:56 PM
 *  @描述：    TODO
 */
public interface Constance {
    //ARouter注解的全路径
    String AROUTE_ANNOTATION_TYPE = "con.ancely.fyw.annotation.apt.ARouter";

    //@Parameter注解全路径
    String PARAMETER_ANNOTATION_TYPE = "con.ancely.fyw.annotation.apt.Parameter";

    //@Subscribe注解全路径
    String SUBSCRIBE_ANNOTATION_TYPE = "con.ancely.fyw.annotation.apt.Subscribe";


    String MODEL_ANNOTATION_TYPE = "con.ancely.fyw.annotation.apt.Model";

    //每个项目的名字
    String PRODECT_NAME = "projectName";

    //路径名,生成path和group
    String APT_PACKAGE = "packageNameAPT";


    //ACTIVITYR 的全类名
    String ACTIVITY_PATH = "android.app.Activity";
    String FRAGMENT_PATH = "android.support.v4.app.Fragment";
    String CALL_PATH = "com.ancely.fyw.aroute.core.Call";
    String STRING = "java.lang.String";
    String ROUTER_MANAGER = "com.ancely.fyw.aroute.manager.RouterManager";

    //接口的全类名
    String AROUTE_GROUP = "com.ancely.fyw.aroute.core.ARouteLoadGroup";
    String AROUTE_PATH = "com.ancely.fyw.aroute.core.ARouteLoadPath";
    String PARAMETER_PATH = "com.ancely.fyw.aroute.core.ParameterLoad";
    String Call_PATH = "com.ancely.fyw.aroute.core.Call";


    //方法名
    String PATH_METHOD_NAME = "loadPath";
    String GROUP_METHOD_NAME = "loadGroup";
    String PARAMETER_METHOD_NAME = "loadParameter";


    //方法参数
    String PARAMETER_LOAD = "target";

    //返回的值
    String PATH_METHOD_RETURN = "pathMap";
    String GROUP_METHOD_RETURN = "groupMap";


    String PATH_FILE_NAME_PREFIX = "ARoute$$Path$$";
    String GROUP_FILE_NAME_PREFIX = "ARoute$$Group$$";
    String PARAMETER_FILE_NAME = "$$Parameter";


    // APT生成类文件所属包名
    String PACKAGE_NAME = "packageName";

    // APT生成类文件的类名
    String CLASS_NAME = "className";

    // 所有的事件订阅方法，生成索引接口
    String SUBSCRIBERINFO_INDEX = "con.ancely.fyw.annotation.apt.SubscriberInfoIndex";

    // 全局属性名
    String FIELD_NAME = "SUBSCRIBER_INDEX";

    // putIndex方法的参数对象名
    String PUTINDEX_PARAMETER_NAME = "info";

    // 加入Map集合方法名
    String PUTINDEX_METHOD_NAME = "putIndex";
    String PUTINDEXS_METHOD_NAME = "putIndexs";

    // getSubscriberInfo方法的参数对象名
    String GETSUBSCRIBERINFO_PARAMETER_NAME = "subscriberClass";

    String GET_SUBSCRIBER_MAPS = "getSubscriberMaps";


    // 通过订阅者对象（MainActivity.class）获取所有订阅方法的方法名
    String GETSUBSCRIBERINFO_METHOD_NAME = "getSubscriberInfo";

}
