package com.pluginpull;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/*
 *  @项目名：  Chajianhua
 *  @包名：    com.pluginpull
 *  @文件名:   PluginManager
 *  @创建者:   fanlelong
 *  @创建时间:  2019/7/31 10:51 AM
 *  @描述：    动态加载第三方插件apk的资源对象and类加载器
 */
public class PluginManager {
    private PluginManager() {
    }

    private static PluginManager sInstance = new PluginManager();

    //第三方插件apk的资源对象
    private Resources mResources;

    //第三方插件apk的类加载器
    private DexClassLoader mClassLoader;

    //上下文
    private Context mContext;

    //插件apk的包信息类,因为我们需要根据包信息名字获取Activity的名字
    PackageInfo mPackageInfo;

    public static PluginManager getInstance() {
        return sInstance;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    //根据传进来的路径去动态加载第三方插件apk里的资源文件和类加载器
    public void loadPath(String path) {
        File pathFile = new File(path);
        if (!pathFile.exists()) {
            return;
        }
        File fileDir = mContext.getDir("odex", Context.MODE_PRIVATE);// data/data/包名/odex/

        //只有一个classloader   optimizedDire:当前应用的私有存储路径
        //DexClassLoader path: 插件包的路径  optimizedDirectory: 缓存的一个目录
        mClassLoader = new DexClassLoader(path, fileDir.getAbsolutePath(), null, mContext.getClassLoader());

        //加载插件里的布局
        try {
            AssetManager assetManager = AssetManager.class.newInstance();

            //反射获取方法 为了把插件包的路径添加进去
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            //invoke; 第一个是要执行方法的对象,第二个是参数是方法的参数可以是多个
            addAssetPath.invoke(assetManager, path);
            // assetManager:资源的一个管理器 第二个参数和第三个 只是一个配置信息
            mResources = new Resources(assetManager, mContext.getResources().getDisplayMetrics(), mContext.getResources().getConfiguration());

        } catch (Exception e) {
            e.printStackTrace();
        }

        //获取到包管理器(packageManager) 整个系统有只有一个
        PackageManager packageManager = mContext.getPackageManager();

        //通过包管理器获取到传进来的这个路径下的dex文件下的包信息类
        mPackageInfo = packageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);

    }

    public Resources getResources() {
        return mResources;
    }

    public DexClassLoader getClassLoader() {
        return mClassLoader;
    }

    public Context getContext() {
        return mContext;
    }

    public PackageInfo getPackageInfo() {
        return mPackageInfo;
    }
}
