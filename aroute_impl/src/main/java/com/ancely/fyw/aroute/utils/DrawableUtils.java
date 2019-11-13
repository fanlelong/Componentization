package com.ancely.fyw.aroute.utils;

/*
 *  @项目名：  IntegratedPlatform_android
 *  @包名：    com.vanke.easysale.util
 *  @文件名:   DrawableUtils
 *  @创建者:   fanlelong
 *  @创建时间:  2018/5/15 下午9:56
 *  @描述：    Drable_utils
 */

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.content.ContextCompat;

import com.ancely.fyw.aroute.R;


public class DrawableUtils {

    /**
     * 创建一个矩形圆角的背影
     *
     * @param colorRes 背影颜色资源ID
     * @param radius   背影的圆角度
     */
    public static GradientDrawable creatDrable(Context context, int colorRes, int radius) {
        GradientDrawable normolDrawable = new GradientDrawable();
        normolDrawable.setShape(GradientDrawable.RECTANGLE);
        normolDrawable.setColor(ContextCompat.getColor(context, colorRes));
        normolDrawable.setCornerRadius(SizeUtils.px2dp(context,radius));
        return normolDrawable;
    }

    public static GradientDrawable creatDrableRadiusByE62E3D(Context context) {
        return creatDrable(context, R.color.color_0f66cc, 5);
    }

    public static GradientDrawable creatDrableRadiusByEdbfc3(Context context) {
        return creatDrable(context, R.color.color_0f66cc, 5);
    }

    /**
     * 创建一个矩形圆角的背影 带有边角颜色的
     *
     * @param colorRes       背影颜色资
     * @param radius         背影的圆角度
     * @param strokeWidth    边角宽度
     * @param strokeColorRes 边角颜色
     */
    public static GradientDrawable creatDrable(Context context, int colorRes, int radius, float strokeWidth, int strokeColorRes) {
        GradientDrawable normolDrawable = new GradientDrawable();
        normolDrawable.setShape(GradientDrawable.RECTANGLE);
        normolDrawable.setColor(ContextCompat.getColor(context, colorRes));
        normolDrawable.setCornerRadius(SizeUtils.px2dp(context,radius));
        normolDrawable.setStroke(SizeUtils.px2dp(context,strokeWidth), ContextCompat.getColor(context, strokeColorRes));
        return normolDrawable;
    }

    public static GradientDrawable creatDrableStrokeByE62E3D(Context context) {
        return DrawableUtils.creatDrable(context, R.color.translucent, 6, 1.5f, R.color.color_0f66cc);
    }

    public static GradientDrawable creatDrableStrokeByE0E0E0(Context context) {
        return DrawableUtils.creatDrable(context, R.color.color_ffffff, 3, 1, R.color.color_e0e0e0);
    }

    public static GradientDrawable creatDrableStrokeByDCDCDC(Context context) {
        return DrawableUtils.creatDrable(context, R.color.translucent, 6, 1.5f, R.color.color_dcdcdc);
    }

    public static GradientDrawable creatDrable(Context context, int colorRes, float[] radii) {
        GradientDrawable normolDrawable = new GradientDrawable();
        normolDrawable.setShape(GradientDrawable.RECTANGLE);
        normolDrawable.setColor(ContextCompat.getColor(context, colorRes));
        normolDrawable.setCornerRadii(radii);
        return normolDrawable;
    }

    public static Drawable setStatusDrawable(Context context) {
        GradientDrawable select = creatDrable(context, R.color.color_550f66cc, 5);
        StateListDrawable selectDrawable = new StateListDrawable();
        selectDrawable.addState(new int[]{android.R.attr.state_pressed}, select);
        selectDrawable.addState(new int[]{}, creatDrableRadiusByE62E3D(context));
        return selectDrawable;
    }

    public static Drawable setStatusDrawable(Context context,int normalColor, int radius,int statusColor ,int status){
        GradientDrawable select = creatDrable(context, statusColor, radius);
        GradientDrawable pressed = creatDrable(context, R.color.color_550f66cc, radius);
        GradientDrawable normal = creatDrable(context, normalColor, radius);
        StateListDrawable selectDrawable = new StateListDrawable();
        selectDrawable.addState(new int[]{status,android.R.attr.state_pressed}, pressed);
        selectDrawable.addState(new int[]{status}, select);
        selectDrawable.addState(new int[]{}, normal);
        return selectDrawable;
    }

}
