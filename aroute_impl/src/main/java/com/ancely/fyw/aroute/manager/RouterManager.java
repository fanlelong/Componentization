package com.ancely.fyw.aroute.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;
import android.widget.Toast;

import com.ancely.fyw.aroute.core.ARouteLoadGroup;
import com.ancely.fyw.aroute.core.ARouteLoadPath;

import con.ancely.fyw.annotation.apt.bean.RouteBean;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.aroute.manager
 *  @文件名:   RouterManager
 *  @创建者:   fanlelong
 *  @创建时间:  2019/8/11 10:30 PM
 *  @描述：    路由管理类
 */
public class RouterManager {
    private String mGroup;
    private String mPath;
    private static RouterManager sInstance;

    private LruCache<String, ARouteLoadGroup> mGroupLruCache; //组缓存
    private LruCache<String, ARouteLoadPath> mPathLruCache; // 跳转地址缓存
    private static final String GROUP_FILE_PREFFIX_NAME = "ARoute$$Group$$";

    private RouterManager() {
        mGroupLruCache = new LruCache<>(163);//最大条目为163
        mPathLruCache = new LruCache<>(163);//最大条目为163
    }

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
        mGroup = subFromPath2Group(path);
        mPath = path;
        return new BundleManager();
    }


    public BundleManager finish(Activity activity) {

        return new BundleManager(true);
    }

    private String subFromPath2Group(String path) {
        if (path.lastIndexOf("/") == 0) {
            throw new IllegalArgumentException("规范path2: /app/MainActivity");
        }
        String finalGroup = path.substring(1, path.indexOf("/", 1));

        if (TextUtils.isEmpty(finalGroup)) {
            throw new IllegalArgumentException("规范path3: /app/MainActivity");
        }
        return finalGroup;
    }

    /**
     * 跳转
     *
     * @param context       上下文
     * @param bundleManager 传参管理类
     * @param code          requestCode or responseCode
     * @return 跨模块用
     */
    public Object navigation(Context context, BundleManager bundleManager, int code) {
        if (bundleManager.isFinish()) {
            Intent intent = new Intent();
            if ((bundleManager.getBundle() != null)) intent.putExtras(bundleManager.getBundle());
            ((Activity) context).setResult(code, intent);
            ((Activity) context).finish();
            return null;
        }

        String groupClassName = "com.ancely.fyw.modular.apt." + GROUP_FILE_PREFFIX_NAME + mGroup;
        Log.e("ancely>>> :", groupClassName);
        try {
            //读取group的类文件 懒加载方式 如果只打开app只只加载app里的
            ARouteLoadGroup loadGroup = mGroupLruCache.get(mGroup);
            if (loadGroup == null) {
                //加载APT  groutp类文件 如: ARouter$$Group$$app

                Class<?> clazz = Class.forName(groupClassName);
                loadGroup = (ARouteLoadGroup) clazz.newInstance();
                mGroupLruCache.put(mGroup, loadGroup);
            }

            //判空
            if (loadGroup.loadGroup().isEmpty()) {
                throw new RuntimeException("arouter group 加载失败");
            }
            //读取path
            ARouteLoadPath loadPath = mPathLruCache.get(mPath);
            if (loadPath == null) {
                //通过group加载接口,去获取path加载接口
                Class<? extends ARouteLoadPath> clazz = loadGroup.loadGroup().get(mGroup);

                if (clazz != null) loadPath = clazz.newInstance();
                if (loadPath != null) mPathLruCache.put(mPath, loadPath);
            }

            if (loadPath != null) {
                if (loadPath.loadPath().isEmpty()) {
                    throw new RuntimeException("arouter path 加载失败");
                }
                RouteBean routeBean = loadPath.loadPath().get(mPath);
                if (routeBean != null) {
                    //类型判断
                    switch (routeBean.getType()) {
                        case ACTIVITY:
                            Intent intent = new Intent(context, routeBean.getClazz());
                            intent.putExtras(bundleManager.getBundle());

                            if (bundleManager.isResult()) {
                                ((Activity) context).setResult(code, intent);
                            }

                            if (code > 0) {//跳转的时候需要回调
                                ((Activity) context).startActivityForResult(intent, code, bundleManager.getBundle());

                            } else {
                                context.startActivity(intent, bundleManager.getBundle());
                            }
                            break;
                        case CALL:
                            //返回的是一个接口的实现类
                            return routeBean.getClazz().newInstance();

                        case FRAGMENT:
                            Fragment fragment = (Fragment) routeBean.getClazz().newInstance();
                            fragment.setArguments(bundleManager.getBundle());
                            return fragment;
                        default:

                            break;
                    }
                } else {
                    Toast.makeText(context, "功能暂未开放", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
