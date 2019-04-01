package com.qingge.yangsong.factory.model.card;

import com.qingge.yangsong.factory.model.db.University;
import com.qingge.yangsong.factory.model.db.User;

public class UniversityCard {
    private String id; // Id
    private String name;// 学校名称
    private String desc;// 学校描述
    private String picture;// 学校图片
    private String province; //省
    private String city; //市
    private String county; //县
    private String ownerId;//创建者id

    public University build() {
        University university = new University();
        university.setId(id);
        university.setName(name);
        university.setDesc(desc);
        university.setPicture(picture);
        university.setProvince(province);
        university.setCity(city);
        university.setCounty(county);
        university.setOwnerId(ownerId);
        return university;
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
