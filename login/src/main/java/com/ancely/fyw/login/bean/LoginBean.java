package com.ancely.fyw.login.bean;

import java.util.List;

/*
 *  @项目名：  ShareAndroid
 *  @包名：    com.ancely.share.bean
 *  @文件名:   LoginBean
 *  @创建者:   fanlelong
 *  @创建时间:  2019/6/2 3:09 PM
 */
public class LoginBean {

    /**
     * admin : false
     * chapterTops : []
     * collectIds : [2890,7562,7565,7559,7558,7564,7570,8108,8099,8202,8046]
     * email :
     * icon :
     * id : 13196
     * password :
     * token :
     * type : 0
     * username : fanlelong
     */

    private boolean admin;
    private String email;
    private String icon;
    private int id;
    private String password;
    private String token;
    private int type;
    private String username;
    private List<?> chapterTops;
    private List<Integer> collectIds;

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<?> getChapterTops() {
        return chapterTops;
    }

    public void setChapterTops(List<?> chapterTops) {
        this.chapterTops = chapterTops;
    }

    public List<Integer> getCollectIds() {
        return collectIds;
    }

    public void setCollectIds(List<Integer> collectIds) {
        this.collectIds = collectIds;
    }

}
