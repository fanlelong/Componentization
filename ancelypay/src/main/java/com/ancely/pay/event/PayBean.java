package com.ancely.pay.event;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.pay.event
 *  @文件名:   PayBean
 *  @创建者:   fanlelong
 *  @创建时间:  2020/2/15 3:09 PM
 *  @描述：    TODO
 */
@Entity
public class PayBean {
    private String deviceId;  //主键
    public String payContent;
    public String title;//
    public String time;//推送时间
    public String appId;//推送ID
    public boolean isSuccess;//推送是否成功
    public PayBean(String payContent,String title) {
        this.payContent = payContent;
        this.title = title;
    }
    @Generated(hash = 1204267029)
    public PayBean(String deviceId, String payContent, String title, String time,
            String appId, boolean isSuccess) {
        this.deviceId = deviceId;
        this.payContent = payContent;
        this.title = title;
        this.time = time;
        this.appId = appId;
        this.isSuccess = isSuccess;
    }
    @Generated(hash = 1567866339)
    public PayBean() {
    }
    public String getDeviceId() {
        return this.deviceId;
    }
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    public String getPayContent() {
        return this.payContent;
    }
    public void setPayContent(String payContent) {
        this.payContent = payContent;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getAppId() {
        return this.appId;
    }
    public void setAppId(String appId) {
        this.appId = appId;
    }
    public boolean getIsSuccess() {
        return this.isSuccess;
    }
    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }
}
