package com.ancely.fyw.aroute.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
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

    public static UIUtils getInstance(Context context) {
        if (sUIUtils == null) {
            sUIUtils = new UIUtils(context);
        }
        return sUIUtils;
    }

    public static UIUtils getUIUtils() {
        if (sUIUtils == null) {
            throw new RuntimeException("UiUtil not init..");
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
            defaultDisplay.getRealMetrics(realDisplay);

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
}
