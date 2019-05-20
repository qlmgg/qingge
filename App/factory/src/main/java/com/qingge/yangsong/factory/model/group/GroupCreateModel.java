package com.qingge.yangsong.factory.model.group;

import java.util.Set;

public class GroupCreateModel {
    private String name;
    private String desc;
    private String picture;
    private Set<String> users;

    public GroupCreateModel() {
    }

    public GroupCreateModel(String name, String desc, String url, Set<String> users) {
        this.desc = desc;
        this.name = name;
        this.picture = url;
        this.users = users;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Set<String> getUsers() {
        return users;
    }

    public void setUsers(Set<String> users) {
        this.users = users;
    }
}
