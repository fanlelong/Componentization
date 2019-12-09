package com.ancely.fyw.photo.bean;

import java.util.List;

/*
 *  @项目名：  PhotoFile
 *  @包名：    com.pick.photo.bean
 *  @文件名:   FloderBean
 *  @创建者:   fanlelong
 *  @创建时间:  2018/12/13 下午2:05
 *  @描述：    TODO
 */
public class FloderBean {
    public List<String> imags;
    private String dir; //当前文件夹的路径
    private String firstImgPath;
    private String name;
    private int count;
    public String firstImg;
    public String parentName;
    public String getDir() {
        return dir;
    }

    public String getName() {
        return name;
    }

    public void setDir(String dir) {
        this.dir = dir;
        int lastIndexOf = this.dir.indexOf("/");
        this.name = this.dir.substring(lastIndexOf);

    }

    public String getFirstImgPath() {
        return firstImgPath;
    }

    public void setFirstImgPath(String firstImgPath) {
        this.firstImgPath = firstImgPath;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
