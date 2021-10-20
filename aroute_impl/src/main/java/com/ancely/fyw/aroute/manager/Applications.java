package com.ancely.fyw.aroute.manager;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.aroute.manager
 *  @文件名:   Applications
 *  @创建者:   admin
 *  @创建时间:  2021/10/20 14:16
 *  @描述：    TODO
 */

import com.ancely.fyw.aroute.core.IApplication;

import java.util.ArrayList;
import java.util.List;

public class Applications {

    public static List<String> applicationPackageName = new ArrayList<>();
    public static List<IApplication> sApplications = new ArrayList<>();

    static {
        applicationPackageName.add("com.ancely.fyw.ComApplication");
        applicationPackageName.add("com.ancely.fyw.usercenter.UserCenterApplication");
        applicationPackageName.add("com.ancely.fyw.photo.PhotoApplication");
    }

    public static List<String> getApplicationPackageName() {
        return applicationPackageName;
    }

    public static void addApplication(IApplication application) {
        sApplications.add(application);
    }
}
