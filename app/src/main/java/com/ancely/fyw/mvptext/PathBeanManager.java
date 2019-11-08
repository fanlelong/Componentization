package com.ancely.fyw.mvptext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 *  @描述：    全局路径管理器(根据组模块分类)
 */
public class PathBeanManager {

    /**
     * key 组的唯一标识, value: 组里面需要的一些类的路径
     */
    private static Map<String, List<PathBean>> groupMap = new HashMap<>();

    public static void joinGroup(String groupName, String pathName, Class<?> clazz) {
        List<PathBean> list = groupMap.get(groupName);
        if (list == null) {
            list = new ArrayList<>();
            list.add(new PathBean(pathName, clazz));
            groupMap.put(groupName, list);
        } else {
            for (PathBean pathBean : list) {
                if (!pathName.equals(pathBean.getPath())) {
                    list.add(new PathBean(pathName, clazz));
                    groupMap.put(groupName, list);
                }
            }
        }
    }

    public static Class<?> getTargetClass(String groupName, String pathName) {

        List<PathBean> list = groupMap.get(groupName);
        if (list == null) {
            return null;
        }

        for (PathBean pathBean : list) {
            if (pathName.equalsIgnoreCase(pathBean.getPath())) {
                return pathBean.getClazz();
            }
        }
        return null;
    }
}
