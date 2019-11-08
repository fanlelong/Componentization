package con.ancely.fyw.annotation.apt.bean;

/*
 *  @项目名：  Componentization
 *  @包名：    con.ancely.fyw.annotation.apt.bean
 *  @文件名:   SubScriberInfo
 *  @创建者:   fanlelong
 *  @创建时间:  2019/11/8 2:57 PM
 */
public interface SubscriberInfo {

    //订阅的类,比如Mainactivity
    Class<?> getSubscriberClass();

    //比如Mainactivity中有哪些订阅的方法
    SubscriberMethod[] getSubscriberMethods();
}
