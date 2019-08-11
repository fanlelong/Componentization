package com.ancely.fyw.aroute.manager;

import android.content.Context;
import android.text.TextUtils;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.aroute.manager
 *  @文件名:   RouterManager
 *  @创建者:   fanlelong
 *  @创建时间:  2019/8/11 10:30 PM
 *  @描述：    TODO
 */
public class RouterManager {
    private static RouterManager sInstance;

    public static RouterManager getInstance() {
        if (sInstance == null) {
            synchronized (RouterManager.class) {
                if (sInstance == null) {
                    sInstance = new RouterManager();
                }
                
            }
        }
        return sInstance;
    }

    /**
     * @param path 路由路径
     * @return Bundle管理类
     */
    public BundleManager build(String path) {
        if (TextUtils.isEmpty(path) || !path.startsWith("/")) {
            throw new IllegalArgumentException("规范path1: /app/MainActivity");
        }
        return new BundleManager();
    }

    public Object navigation(Context context, BundleManager bundleManager, int code) {
        return null;
    }
}
