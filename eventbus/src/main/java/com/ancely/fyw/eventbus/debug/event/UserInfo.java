package com.ancely.fyw.eventbus.debug.event;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.eventbus.debug.event
 *  @文件名:   UserInfo
 *  @创建者:   fanlelong
 *  @创建时间:  2019/11/8 2:48 PM
 *  @描述：    TODO
 */
public class UserInfo {
    private String name;
    private int age;

    @Override
    public String toString() {
        return "UserInfo{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public UserInfo(String name, int age) {

        this.name = name;
        this.age = age;
    }
}
