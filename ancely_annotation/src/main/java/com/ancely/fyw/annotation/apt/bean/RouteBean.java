package com.ancely.fyw.annotation.apt.bean;
import javax.lang.model.element.Element;
public class RouteBean {
    private RouteBean(Builder builder) {
        this.path = builder.path;
        this.element = builder.element;
        this.group = builder.group;
    }

    private RouteBean(Type type, Class<?> clazz, String path, String group) {
        this.path = path;
        this.group = group;
        this.clazz = clazz;
        this.type = type;
    }

    public static RouteBean create(Type type, Class<?> clazz, String path, String group) {
        return new RouteBean(type, clazz, path, group);
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Element getElement() {
        return element;
    }

    public String getGroup() {
        return group;
    }

    public Type getType() {
        return type;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getPath() {
        return path;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    @Override
    public String toString() {
        return "RouteBean{" +
                "type=" + type +
                ", element=" + element +
                ", clazz=" + clazz +
                ", group='" + group + '\'' +
                ", path='" + path + '\'' +
                '}';
    }

    public enum Type {
        ACTIVITY, CALL, FRAGMENT_V4
    }

    //枚举类型
    private Type type;

    //类节点
    private Element element;

    //被ARoute注解的对象
    private Class<?> clazz;

    //路由的组名
    private String group;

    //路由的地址
    private String path;

    public static class Builder {
        //类节点
        private Element element;

        //路由的组名
        private String group;

        //路由的地址
        private String path;

        public Builder setElement(Element element) {
            this.element = element;
            return this;
        }

        public Builder setGroup(String group) {
            this.group = group;
            return this;
        }

        public Builder setPath(String path) {
            this.path = path;
            return this;
        }

        public RouteBean build() {
            if (path == null || path.length() == 0) {
                throw new IllegalArgumentException("path必填,如/app/Mainactivity");
            }
            return new RouteBean(this);
        }
    }


}