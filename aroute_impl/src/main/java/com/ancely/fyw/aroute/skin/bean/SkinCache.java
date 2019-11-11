package com.ancely.fyw.aroute.skin.bean;

import android.content.res.Resources;

import dalvik.system.DexClassLoader;

public class SkinCache {

    private Resources skinResources; // 用于加载皮肤包资源
    private String skinPackageName; // 皮肤包资源所在包名（注：皮肤包不在app内，也不限包名）
    private DexClassLoader classLoader;

    public SkinCache(Resources skinResources, DexClassLoader classLoader, String skinPackageName) {
        this.skinResources = skinResources;
        this.skinPackageName = skinPackageName;
        this.classLoader = classLoader;
    }

    public Resources getSkinResources() {
        return skinResources;
    }

    public String getSkinPackageName() {
        return skinPackageName;
    }

    public DexClassLoader getClassLoader() {
        return classLoader;
    }
}
