package com.ancely.fyw.compile.utils;

import java.util.Collection;
import java.util.Map;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.compile.utils
 *  @文件名:   EmptyUtils
 *  @创建者:   fanlelong
 *  @创建时间:  2019/8/9 4:05 PM
 *  @描述：    判空类
 */
public class EmptyUtils {
    public static boolean isEmpty(CharSequence charSequence) {
        return charSequence == null || charSequence.length() == 0;
    }


    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }
}
