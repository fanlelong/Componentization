package com.ancely.pay.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


/**
 * 操作SharePreference的工具类
 *
 * @author Marvin
 */
public class SharePreferenceHelper {


    /**
     * 得到SharedPreferences
     *
     * @param context
     * @return
     */
    public static SharedPreferences getSharedPreferences(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(
                "ancely_pay", Context.MODE_PRIVATE);
        return preferences;
    }

    /**
     * 得到key对应的boolean值
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean getBoolean(Context context, String key) {
        return getBoolean(context, key, false);
    }

    /**
     * 得到key对应的boolean值
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getBoolean(key, defaultValue);
    }

    /**
     * 写入key的boolean值
     *
     * @param context
     * @param key
     * @return
     */
    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences preferences = getSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * 得到key对应的string值
     *
     * @param context
     * @param key
     * @return
     */
    public static void putString(Context context, String key, String value) {
        SharedPreferences preferences = getSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 写入key的string值
     *
     * @param context
     * @param key
     * @return
     */
    public static String getString(Context context, String key) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getString(key, "");
    }

    /**
     * 得到key对应的int值
     *
     * @param context
     * @param key
     * @return
     */
    public static void putInt(Context context, String key, int value) {
        SharedPreferences preferences = getSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * 写入key的int值
     *
     * @param context
     * @param key
     * @return
     */
    public static int getInt(Context context, String key) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getInt(key, 0);
    }

    public static int getInt(Context context, String key,int deValue) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getInt(key, deValue);
    }
    /**
     * 得到key对应的long值
     *
     * @param context
     * @param key
     * @return
     */
    public static void putLong(Context context, String key, long value) {
        SharedPreferences preferences = getSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * 写入key的long值
     *
     * @param context
     * @param key
     * @return
     */
    public static long getLong(Context context, String key) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getLong(key, 0L);
    }

    /**
     * 移除一个key
     *
     * @param context
     * @param key
     */
    public static void remove(Context context, String key) {
        SharedPreferences preferences = getSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.remove(key);
        editor.commit();
    }

}
