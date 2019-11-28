package con.ancely.fyw.annotation.apt;


import java.util.Map;

import con.ancely.fyw.annotation.apt.bean.SubscriberInfo;

/**
 * 所有的事件订阅方法，生成索引接口
 */
public interface SubscriberInfoIndex {

    /**
     * 生成索引接口，通过订阅者对象（MainActivity.class）获取所有订阅方法
     *
     * @param subscriberClass 订阅者对象Class，如：MainActivity.class
     * @return 事件订阅方法封装类
     */
    SubscriberInfo getSubscriberInfo(Class<?> subscriberClass);

    Map<Class, SubscriberInfo> getSubscriberMaps();

    void putIndexs(SubscriberInfo info);
}
