package com.ancely.fyw.aroute.manager;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;

import com.ancely.fyw.aroute.skin.bean.SkinCache;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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


    private static PluginManager sInstance;

    //第三方插件apk的资源对象
    private Resources mPluginResources;

    private Resources mAppResources;
    //第三方插件apk的类加载器
    private DexClassLoader mClassLoader;

    //上下文
    private Application mApplication;


    //插件apk的包信息类,因为我们需要根据包信息名字获取Activity的名字
    private PackageInfo mPackageInfo;
    private PackageInfo mPackageServiceInfo;

    private String skinPackageName; // 皮肤包资源所在包名（注：皮肤包不在app内，也不限包名）
    private boolean isDefaultSkin = true; // 应用默认皮肤（app内置）
    private static final String ADD_ASSET_PATH = "addAssetPath"; // 方法名
    private Map<String, SkinCache> cacheSkin;


    /**
     * 单例方法，目的是初始化app内置资源（越早越好，用户的操作可能是：换肤后的第2次冷启动）
     */
    public static void init(Application application) {
        if (sInstance == null) {
            synchronized (PluginManager.class) {
                if (sInstance == null) {
                    sInstance = new PluginManager(application);
                }
            }
        }
    }

    public static PluginManager getInstance() {
        return sInstance;
    }

    private PluginManager(Application application) {
        cacheSkin = new HashMap<>();
        mAppResources = application.getResources();
        mApplication = application;
    }

    //根据传进来的路径去动态加载第三方插件apk里的资源文件和类加载器或者皮肤包
    public boolean loadPluginPath(String pluginPath) {
        File pathFile = new File(pluginPath);
        if (!pathFile.exists()) {
            isDefaultSkin = true;
            return false;
        }

        // 优化：app冷启动、热启动可以取缓存对象
        if (cacheSkin.containsKey(pluginPath)) {
            isDefaultSkin = false;
            SkinCache skinCache = cacheSkin.get(pluginPath);
            if (null != skinCache) {
                mPluginResources = skinCache.getSkinResources();
                skinPackageName = skinCache.getSkinPackageName();
                mClassLoader = skinCache.getClassLoader();
                return true;
            }
        }

        File fileDir = mApplication.getDir("odex", Context.MODE_PRIVATE);// data/data/包名/odex/

        //只有一个classloader   optimizedDire:当前应用的私有存储路径
        //DexClassLoader path: 插件包的路径  optimizedDirectory: 缓存的一个目录
        mClassLoader = new DexClassLoader(pluginPath, fileDir.getAbsolutePath(), null, mApplication.getClassLoader());

        //加载插件里的布局
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            //反射获取方法 为了把插件包的路径添加进去
            Method addAssetPath = assetManager.getClass().getMethod(ADD_ASSET_PATH, String.class);
            //invoke; 第一个是要执行方法的对象,第二个是参数是方法的参数可以是多个

            addAssetPath.invoke(assetManager, pluginPath);
            // assetManager:资源的一个管理器 第二个参数和第三个 只是一个配置信息
            mPluginResources = new Resources(assetManager, mApplication.getResources().getDisplayMetrics(), mApplication.getResources().getConfiguration());

        } catch (Exception e) {
            isDefaultSkin = true;
            e.printStackTrace();
        }

        //获取到包管理器(packageManager) 整个系统有只有一个
        PackageManager packageManager = mApplication.getPackageManager();

        //通过包管理器获取到传进来的这个路径下的dex文件下的包信息类
        mPackageInfo = packageManager.getPackageArchiveInfo(pluginPath, PackageManager.GET_ACTIVITIES);
        mPackageServiceInfo = packageManager.getPackageArchiveInfo(pluginPath, PackageManager.GET_SERVICES);


        skinPackageName = mPackageInfo.packageName;

        // 无法获取皮肤包应用的包名，则加载app内置资源
        isDefaultSkin = TextUtils.isEmpty(skinPackageName);
        if (!isDefaultSkin) {
            cacheSkin.put(pluginPath, new SkinCache(mPluginResources, mClassLoader, skinPackageName));
        }
        Log.e("skinPackageName >>> ", skinPackageName);
        return true;
    }

    public Resources getResources() {
        return mPluginResources;
    }

    public DexClassLoader getClassLoader() {
        return mClassLoader;
    }

    public Context getContext() {
        return mApplication;
    }

    public PackageInfo getPackageInfo() {
        return mPackageInfo;
    }

    public PackageInfo getPackageServiceInfo() {
        return mPackageServiceInfo;
    }

    public boolean parserApkAction(String path) {
        try {
            File pathFile = new File(path);
            if (!pathFile.exists()) {
                return false;
            }
            File fileDir = mApplication.getDir("odex", Context.MODE_PRIVATE);// data/data/包名/odex/


            //parsePackage
            Class<?> packageParserClass = Class.forName("android.content.pm.PackageParser");
            Object packageParser = packageParserClass.newInstance();
            Method parsePackageMethod = packageParserClass.getMethod("parsePackage", File.class, int.class);


            //Package
            Object mPackage = parsePackageMethod.invoke(packageParser, pathFile, PackageManager.GET_ACTIVITIES);
            //Package里的receivers
            Field receiversField = mPackage.getClass().getDeclaredField("receivers");


            ArrayList receivers = (ArrayList) receiversField.get(mPackage);
            PackageManager packageManager = mApplication.getPackageManager();
            PackageInfo receiverInfo = packageManager.getPackageArchiveInfo(path, PackageManager.GET_RECEIVERS);

            for (Object receiver : receivers) {

//                Class<?> mPackageUserStateClass = Class.forName("android.content.pm.PackageUserState");
//                Method generateActivityInfoMethod = packageParserClass.getMethod("generateActivityInfo", receiver.getClass(),
//                        int.class, mPackageUserStateClass, int.class);
//                Class<?> mUserHandleClass = Class.forName("android.os.UserHandle");
//                int useId = (int) mUserHandleClass.getMethod("getCallingUserId").invoke(null);
//
//                ActivityInfo activityInfo = (ActivityInfo) generateActivityInfoMethod.invoke(null, receiver, 0,mPackageUserStateClass.newInstance(), useId);
//                String broastCastName = activityInfo.name;

                Class<?> aClass = PluginManager.getInstance().getClassLoader().loadClass(receiverInfo.receivers[receivers.indexOf(receiver)].name);
                BroadcastReceiver broadcastReceiver = (BroadcastReceiver) aClass.newInstance();


                Class<?> mComponent = Class.forName("android.content.pm.PackageParser$Component");
                Field intentsField = mComponent.getDeclaredField("intents");
                ArrayList<IntentFilter> intents = (ArrayList) intentsField.get(receiver);
                for (IntentFilter intent : intents) {
                    mApplication.registerReceiver(broadcastReceiver, intent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean isDefaultSkin() {
        return isDefaultSkin;
    }


    public int getColor(int resourceId) {
        int ids = getSkinResourceIds(resourceId);
        return isDefaultSkin ? mAppResources.getColor(ids) : mPluginResources.getColor(ids);
    }

    public ColorStateList getColorStateList(int resourceId) {
        int ids = getSkinResourceIds(resourceId);
        return isDefaultSkin ? mAppResources.getColorStateList(ids) : mPluginResources.getColorStateList(ids);
    }

    // mipmap和drawable统一用法（待测）
    private Drawable getDrawableOrMipMap(int resourceId) {
        int ids = getSkinResourceIds(resourceId);
        return isDefaultSkin ? mAppResources.getDrawable(ids) : mPluginResources.getDrawable(ids);
    }

    private String getString(int resourceId) {
        int ids = getSkinResourceIds(resourceId);
        return isDefaultSkin ? mAppResources.getString(ids) : mPluginResources.getString(ids);
    }

    // 返回值特殊情况：可能是color / drawable / mipmap
    public Object getBackgroundOrSrc(int resourceId) {
        // 需要获取当前属性的类型名Resources.getResourceTypeName(resourceId)再判断
        String resourceTypeName = mAppResources.getResourceTypeName(resourceId);

        switch (resourceTypeName) {
            case "color":
                return getColor(resourceId);

            case "mipmap": // drawable / mipmap
            case "drawable":
                return getDrawableOrMipMap(resourceId);
        }
        return null;
    }

    // 获得字体
    public Typeface getTypeface(int resourceId) {
        // 通过资源ID获取资源path，参考：resources.arsc资源映射表
        String skinTypefacePath = getString(resourceId);
        // 路径为空，使用系统默认字体
        if (TextUtils.isEmpty(skinTypefacePath)) return Typeface.DEFAULT;
        return isDefaultSkin ? Typeface.createFromAsset(mAppResources.getAssets(), skinTypefacePath)
                : Typeface.createFromAsset(mPluginResources.getAssets(), skinTypefacePath);
    }

    /**
     * 参考：resources.arsc资源映射表
     * 通过ID值获取资源 Name 和 Type
     *
     * @param resourceId 资源ID值
     * @return 如果没有皮肤包则加载app内置资源ID，反之加载皮肤包指定资源ID
     */
    private int getSkinResourceIds(int resourceId) {
        // 优化：如果没有皮肤包或者没做换肤动作，直接返回app内置资源！
        if (isDefaultSkin) return resourceId;

        // 使用app内置资源加载，是因为内置资源与皮肤包资源一一对应（“netease_bg”, “drawable”）
        String resourceName = mAppResources.getResourceEntryName(resourceId);
        String resourceType = mAppResources.getResourceTypeName(resourceId);

        // 动态获取皮肤包内的指定资源ID
        // getResources().getIdentifier(“netease_bg”, “drawable”, “com.netease.skin.packages”);
        int skinResourceId = mPluginResources.getIdentifier(resourceName, resourceType, skinPackageName);

        // 源码1924行：(0 is not a valid resource ID.)
        isDefaultSkin = skinResourceId == 0;
        return skinResourceId == 0 ? resourceId : skinResourceId;
    }
}
