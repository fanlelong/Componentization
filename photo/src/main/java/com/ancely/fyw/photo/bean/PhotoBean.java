package com.ancely.fyw.photo.bean;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.photo.bean
 *  @文件名:   PhotoBean
 *  @创建者:   fanlelong
 *  @创建时间:  2019/12/8 10:40 AM
 *  @描述：    TODO
 */
public class PhotoBean{
    public String path;
    public long time;

    public PhotoBean(String path, long time) {
        this.path = path;
        this.time = time;
    }
}
