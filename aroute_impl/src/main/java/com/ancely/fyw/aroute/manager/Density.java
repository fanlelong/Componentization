package com.ancely.fyw.aroute.manager;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

/*
 *  @文件名:   Density
 *  @创建者:   fanlelong
 * <p>
 * density:屏幕密度->以每一寸有160个像素点为基础 density=1;,如果每一寸有320个像素点的话,那么density=2
 * scaleDensity :字体缩放比例,默认情况下density = scaleDensity;
 * densityDpi:屏幕上每一英寸像素点有多不个,就是160或者320...   Android最终都是以px来显示的
 * 可以看TypeValue里面的public static float applyDimension(int unit, float value, DisplayMetrics metrics)
 */
public class Density {
    private static final float WIDTH = 360;
    private static float appDensity;//屏幕密度
    private static float appScaleDensity;//字体缩放比例,默认appDensity

    public static void setDensity(final Application application, Activity activity) {
        //获取当前App的屏幕显示信息
        DisplayMetrics displayMetrics = application.getResources().getDisplayMetrics();
        if (appDensity == 0) {
            appDensity = displayMetrics.density;
            appScaleDensity = displayMetrics.scaledDensity;
            //添加字体变换监听
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    if (newConfig != null && newConfig.fontScale > 0) {
                        //字体发生更改,重新对appScaleDensity赋值
                        appScaleDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {

                }
            });
        }
        //计算目标值
        float targetDensity;
        if (displayMetrics.widthPixels > displayMetrics.heightPixels) {
            targetDensity = displayMetrics.heightPixels / WIDTH;
        } else {
            targetDensity = displayMetrics.widthPixels / WIDTH;
        }

//        float targetDensity = displayMetrics.widthPixels / WIDTH;
        float targetScaleDensity = targetDensity * (appScaleDensity / appDensity);
        int targetDensityDpi = (int) (targetDensity * 160);

        //替换系统的值
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        dm.density = targetDensity;
        dm.densityDpi = targetDensityDpi;
        dm.scaledDensity = targetScaleDensity;
    }
}
