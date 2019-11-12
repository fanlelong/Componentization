package com.ancely.fyw.aroute.model.bean;


public class RequestErrBean {
    public String msg;
    public int code;
    public int flag;

    public RequestErrBean(int code, String msg, int flag) {
        this.msg = msg;
        this.code = code;
        this.flag = flag;
    }
}
