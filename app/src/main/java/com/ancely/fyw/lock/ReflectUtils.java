package com.ancely.fyw.lock;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.lock
 *  @文件名:   ReflectUtils
 *  @创建者:   admin
 *  @创建时间:  2021/10/28 16:51
 *  @描述：    TODO
 */

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectUtils {
    public static Object mUnsafe = null;
    private static Class<?> sUnsafeClass = null;

    static {
        try {
            sUnsafeClass = Class.forName("sun.misc.Unsafe");
            Method getUnsafeMethod = sUnsafeClass.getMethod("getUnsafe");
            getUnsafeMethod.setAccessible(true);
            mUnsafe = getUnsafeMethod.invoke(null);
        } catch (Exception e) {

        }
    }


    public static long objectFieldOffset(Field field) {
        try {
            Method objectFieldOffset = sUnsafeClass.getDeclaredMethod("objectFieldOffset", Field.class);
            return (long) objectFieldOffset.invoke(mUnsafe, field);
        } catch (Exception e) {
        }
        return 0;
    }

    public static void putObject(Object node, long thread, Object currentThread) {
        try {
            Method putObjectMethod = sUnsafeClass.getDeclaredMethod("putObject", Object.class, long.class, Object.class);
            putObjectMethod.invoke(mUnsafe, node, thread, currentThread);
        } catch (Exception e) {
        }
    }

    public static boolean compareAndSwapObject(Object var1, long var2, Object var4, Object var5) {
        try {
            Method compareAndSwapObjectMethod = sUnsafeClass.getDeclaredMethod("compareAndSwapObject", Object.class, long.class, Object.class, Object.class);
            Object invoke = compareAndSwapObjectMethod.invoke(mUnsafe, var1, var2, var4, var5);
            return (boolean) invoke;
        } catch (Exception e) {
        }
        return false;
    }

    public static boolean compareAndSwapInt(Object var1, long var2, int var4, int var5) {
        try {
            Method compareAndSwapIntMethod = sUnsafeClass.getDeclaredMethod("compareAndSwapInt", Object.class, long.class, int.class, int.class);
            Object invoke = compareAndSwapIntMethod.invoke(mUnsafe, var1, var2, var4, var5);
            return (boolean) invoke;
        } catch (Exception e) {
        }
        return false;
    }
}
