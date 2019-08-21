package com.ancely.fyw.aroute.manager;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.LruCache;

import com.ancely.fyw.aroute.core.ParameterLoad;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.aroute.manager
 *  @文件名:   ParameterManager
 *  @创建者:   fanlelong
 *  @创建时间:  2019/8/12 9:52 AM
 *  @描述：     参数Parameter加载管理器
 */
public class ParameterManager {
    private static ParameterManager sInstance;

    private LruCache<String, ParameterLoad> mCache;
    public static final String FILE_SUFFIX_NAME = "$$Parameter";

    private ParameterManager() {
        mCache = new LruCache<>(163);//最大条目为163
    }

    public static ParameterManager getInstance() {
        if (sInstance == null) {
            synchronized (ParameterManager.class) {
                if (sInstance == null) {
                    sInstance = new ParameterManager();
                }
            }
        }
        return sInstance;
    }

    public void loadParameter(@NonNull Activity activity) {

        String className = activity.getClass().getName();

        ParameterLoad iParameterLoad = mCache.get(className);
        //缓存中没,加入缓存
        try {
            if (iParameterLoad == null) {
                Class<?> clazz = Class.forName(className + FILE_SUFFIX_NAME);
                iParameterLoad  = (ParameterLoad) clazz.newInstance();
                mCache.put(className,iParameterLoad);
            }

            iParameterLoad.loadParameter(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
