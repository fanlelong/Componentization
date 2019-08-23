package com.ancely.fyw.aroute.manager;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

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

    private static PluginManager sInstance;

    //第三方插件apk的资源对象
    private Resources mResources;

    //第三方插件apk的类加载器
    private DexClassLoader mClassLoader;

    //上下文
    private Context mContext;

    //插件apk的包信息类,因为我们需要根据包信息名字获取Activity的名字
    private PackageInfo mPackageInfo;
    private PackageInfo mPackageServiceInfo;

    public static PluginManager getInstance() {

        if (sInstance == null) {
            synchronized (PluginManager.class) {
                if (sInstance == null) {
                    sInstance = new PluginManager();
                }
            }
        }
        return sInstance;
    }

    public void setContext(Context context) {
        if (context instanceof Application) {
            mContext = context;
        } else {
            this.mContext = context.getApplicationContext();
        }
    }

    //根据传进来的路径去动态加载第三方插件apk里的资源文件和类加载器
    public boolean loadPath(String path) {
        File pathFile = new File(path);
        if (!pathFile.exists()) {
            return false;
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
        mPackageServiceInfo = packageManager.getPackageArchiveInfo(path, PackageManager.GET_SERVICES);

        return true;
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

    public PackageInfo getPackageServiceInfo() {
        return mPackageServiceInfo;
    }

    public boolean parserApkAction(String path) {
        try {
            File pathFile = new File(path);
            if (!pathFile.exists()) {
                return false;
            }
            File fileDir = mContext.getDir("odex", Context.MODE_PRIVATE);// data/data/包名/odex/


            //parsePackage
            Class<?> packageParserClass = Class.forName("android.content.pm.PackageParser");
            Object packageParser = packageParserClass.newInstance();
            Method parsePackageMethod = packageParserClass.getMethod("parsePackage", File.class, int.class);


            //Package
            Object mPackage = parsePackageMethod.invoke(packageParser, pathFile, PackageManager.GET_ACTIVITIES);
            //Package里的receivers
            Field receiversField = mPackage.getClass().getDeclaredField("receivers");


            ArrayList receivers = (ArrayList) receiversField.get(mPackage);
            PackageManager packageManager = mContext.getPackageManager();
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
                    mContext.registerReceiver(broadcastReceiver, intent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
