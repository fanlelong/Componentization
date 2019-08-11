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

    //每个项目的名字
    String PRODECT_NAME = "projectName";

    //路径名,生成path和group
    String APT_PACKAGE = "packageNameAPT";


    //ACTIVITYR 的全类名
    String ACTIVITY_PATH = "android.app.Activity";
    String STRING = "java.lang.string";
    String ROUTER_MANAGER = "com.ancely.fyw.aroute.manager.RouterManager";

    //接口的全类名
    String AROUTE_GROUP = "com.ancely.fyw.aroute.core.ARouteLoadGroup";
    String AROUTE_PATH = "com.ancely.fyw.aroute.core.ARouteLoadPath";
     String PARAMETER_PATH =  "com.ancely.fyw.aroute.core.ParameterLoad";
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


    public static final String PATH_FILE_NAME_PREFIX = "ARoute$$Path$$";
    public static final String GROUP_FILE_NAME_PREFIX = "ARoute$$Group$$";
    public static final String PARAMETER_FILE_NAME = "$$Parameter";

}
