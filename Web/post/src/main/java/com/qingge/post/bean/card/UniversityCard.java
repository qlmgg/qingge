package com.qingge.post.bean.card;

import com.google.gson.annotations.Expose;
import com.qingge.post.bean.db.University;
import com.qingge.post.bean.db.User;

public class UniversityCard {
    @Expose
    private String id; // Id
    @Expose
    private String name;// 学校名称
    @Expose
    private String desc;// 学校描述
    @Expose
    private String picture;// 学校图片
    @Expose
    private String province; //省
    @Expose
    private String city; //市
    @Expose
    private String county; //县
    @Expose
    private String ownerId;//创建者id

    public UniversityCard(final University university) {
        this.id = university.getId();
        this.name = university.getName();
        this.desc = university.getDescription();
        this.picture = university.getPicture();
        this.province = university.getProvince();
        this.city = university.getCity();
        this.county = university.getCounty();
        this.ownerId = university.getOwnerId();


    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
}
