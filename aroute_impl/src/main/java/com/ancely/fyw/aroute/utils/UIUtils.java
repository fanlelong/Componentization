package com.ancely.fyw.aroute.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.lang.reflect.Field;

public class UIUtils {
    //标准值  正常情况下应该保存在配置文件中
    public static final float STANDARD_WIDTH = 750f;
    public static final float STANDARD_HEIGHT = 1334f;

    //实际设备信息
    public static float sDisplayMetricsWidth;
    public static float sDisplayMetricsHeight;
    public static float sDisplayRealHeight;
    public static float sStateBarHeight;

    private static UIUtils sUIUtils;
    private static Application sApplication;

    public static void initApplication(Application application) {
        sApplication = application;
    }

    public static UIUtils getUIUtils() {
        if (sApplication == null) {
            throw new RuntimeException("Please initApplication ...");
        }
        if (sUIUtils == null) {
            sUIUtils = new UIUtils(sApplication);
        }
        return sUIUtils;
    }

    private UIUtils(Context context) {
        //需要得到真机上的宽高值
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        DisplayMetrics realDisplay = new DisplayMetrics();
        if (sDisplayMetricsWidth == 0.0f || sDisplayMetricsHeight == 0.0f) {
            //在这里得到设备的真实值
            Display defaultDisplay = windowManager.getDefaultDisplay();
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                defaultDisplay.getRealMetrics(realDisplay);
            }

            //判断横屏还竖屏
            if (displayMetrics.widthPixels > displayMetrics.heightPixels) {
                sDisplayMetricsWidth = (float) (displayMetrics.heightPixels);
                sDisplayMetricsHeight = (float) (displayMetrics.widthPixels);
                sDisplayRealHeight = (realDisplay.widthPixels > displayMetrics.widthPixels) ? (float) realDisplay.widthPixels : (float) displayMetrics.widthPixels;
            } else {
                sDisplayMetricsWidth = (float) (displayMetrics.widthPixels);
                sDisplayMetricsHeight = (float) (displayMetrics.heightPixels);
                sDisplayRealHeight = (realDisplay.heightPixels > displayMetrics.heightPixels) ? (float) realDisplay.heightPixels : (float) displayMetrics.heightPixels;
            }
            sStateBarHeight = getSystemBarHeight(context);
        }
    }

    public float getHorizontalScaleValue() {
        return ((float) (sDisplayMetricsWidth)) / STANDARD_WIDTH;
    }

    public float getVerticalScaleValue() {
        return ((float) (sDisplayMetricsHeight)) / (STANDARD_HEIGHT - sStateBarHeight);
    }

    /**
     * 用于得到状态框的高度
     */
    public int getSystemBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int height = context.getResources().getDimensionPixelSize(resourceId);
        if (height != -1) {
            return (int) (height / getVerticalScaleValue());
        }
        return getValue(context, "com.android.internal.R$dimen", "system_bar_height", 48);
    }

    private int getValue(Context context, String dimeClass, String system_bar_height, int defaultValue) {

        try {
            Class<?> clz = Class.forName(dimeClass);
            Object object = clz.newInstance();
            Field field = clz.getField(system_bar_height);
            int id = Integer.parseInt(field.get(object).toString());
            return context.getResources().getDimensionPixelSize(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public int getWidth(int width) {
        return Math.round((float) width * sDisplayMetricsWidth / STANDARD_WIDTH);
    }

    public int getHeight(int height) {
        return Math.round((float) height * sDisplayMetricsHeight / (STANDARD_HEIGHT));
    }

    public int getRealHeight(int height) {
        return Math.round((float) height * sDisplayRealHeight / (STANDARD_HEIGHT));
    }


    public void resetUiUtils(Activity activity) {
        Point p = getScreenWidthAndHeightFormActivity(activity);
        sDisplayMetricsWidth = Math.min(p.x, p.y);
        sDisplayMetricsHeight = Math.max(p.x, p.y);
    }

    public Point getScreenWidthAndHeightFormActivity(Activity activity) {
        int screenWidth;
        int screenHeight;
        View decorView = activity.getWindow().getDecorView();
        int decorWidth = decorView.getWidth();
        int widthPixels = activity.getResources().getDisplayMetrics().widthPixels;
        //有些设备获取（decorWidth）这个值会有问题，如果差距大就用DisplayMetrics
        if (decorWidth > widthPixels * 1.5) {
            screenWidth = widthPixels;
        } else if (decorWidth >= 240) {//优先用decorView的宽度
            screenWidth = decorWidth;
        } else {
            screenWidth = Math.max(decorWidth, widthPixels);
        }
        int decorHeight = decorView.getHeight();
        int heightPixels = activity.getResources().getDisplayMetrics().heightPixels;

        //有些设备获取（decorHeight）这个值会有问题，如果差距大就用DisplayMetrics
        if (decorHeight > heightPixels * 1.5) {
            screenHeight = heightPixels;
        } else if (decorHeight >= 320) {//优先用decorView的高度
            screenHeight = decorHeight;
        } else {
            screenHeight = Math.max(decorHeight, heightPixels);
        }
        //如果二个相等取DisplayMetrics宽高
        if (screenWidth == screenHeight) {
            screenWidth = widthPixels;
            screenHeight = heightPixels;
        }
        return new Point(screenWidth, screenHeight);
    }
}
