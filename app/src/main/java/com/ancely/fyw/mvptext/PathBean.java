package com.ancely.fyw.mvptext;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.mvptext
 *  @文件名:   PathBean
 *  @创建者:   fanlelong
 *  @创建时间:  2019/11/5 4:01 PM
 *  @描述：    TODO
 */
public class PathBean {
    private String path;//跳转目标字符串key
    private Class clazz;//跳转的目标对象

    public PathBean(String path, Class clazz) {
        this.path = path;
        this.clazz = clazz;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }
}
