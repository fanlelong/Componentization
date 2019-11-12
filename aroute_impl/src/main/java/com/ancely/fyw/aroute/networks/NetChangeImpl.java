package com.ancely.fyw.aroute.networks;

import android.util.Log;

import com.ancely.fyw.aroute.networks.annotation.Net;
import com.ancely.fyw.aroute.networks.bean.NetMethodBean;
import com.ancely.fyw.aroute.networks.impl.NetType;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class NetChangeImpl {

    private Map<Object, List<NetMethodBean>> networkList;

    public NetChangeImpl() {
        networkList = new HashMap<>();
    }

    public void post(NetType netType) {
        Set<Object> set = networkList.keySet();
        for (final Object getter : set) {
            //所有注册的方法
            List<NetMethodBean> methodList = networkList.get(getter);
            if (methodList != null) {
                for (final NetMethodBean methodManager : methodList) {
                    if (methodManager.getType().isAssignableFrom(netType.getClass())) {
                        switch (methodManager.getNetType()) {
                            case AUTO:
                                invoke(methodManager, getter, netType);
                                break;
                            case WIFI:
                                if (netType == NetType.WIFI || netType == NetType.NONE) {
                                    invoke(methodManager, getter, netType);
                                }
                                break;
                            case CMWAP:
                                if (netType == NetType.CMWAP || netType == NetType.NONE) {
                                    invoke(methodManager, getter, netType);
                                }
                                break;
                            case CMNET:
                                if (netType == NetType.CMNET || netType == NetType.NONE) {
                                    invoke(methodManager, getter, netType);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }
    }

    private void invoke(NetMethodBean methodManager, Object getter, NetType netType) {

        if (methodManager.isManThread()) {
            AppExecutors.get().mainThread().execute(() -> {
                try {
                    methodManager.getMethod().invoke(getter, netType);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } else {
            try {
                methodManager.getMethod().invoke(getter, netType);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private List<NetMethodBean> findAnnotationMethod(Object object) {
        List<NetMethodBean> methodManagerList = new ArrayList<>();
        Class<?> clazz = object.getClass();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            //获取方法的注解
            Net net = method.getAnnotation(Net.class);
            if (net == null) {
                continue;
            }
            Type returnType = method.getGenericReturnType();//获取到返回的类型
            if (!"void".equals(returnType.toString())) {
                throw new RuntimeException(method.getName() + "方法必须返回是void");
            }
            //参数机校检
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length != 1) {
                throw new RuntimeException(method.getName() + "方法参数有且只能有一个");
            }
            //过滤掉所有符合要求的方法.才开始添加
            NetMethodBean methodManager = new NetMethodBean(parameterTypes[0], net.netType(), method, net.isManThread());
            methodManagerList.add(methodManager);
        }

        return methodManagerList;
    }

    public void registerObserver(Object object) {
        //获取object类的所有的方法
        List<NetMethodBean> methodList = networkList.get(object);
        if (methodList == null) {//不为空则表示已经注册过
            //开始添加方法
            methodList = findAnnotationMethod(object);
            networkList.put(object, methodList);
        }
    }

    public void unRegisterObserver(Object object) {

        if (!networkList.isEmpty()) {
            networkList.remove(object);
        }
    }

    public void unRegisterAllObserver() {
        if (!networkList.isEmpty()) {
            networkList.clear();
        }
        Log.e("NetworkConnectChanged", "注销所有的监听");
    }
}
