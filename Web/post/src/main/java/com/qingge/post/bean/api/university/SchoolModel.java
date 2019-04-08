package com.qingge.post.bean.api.university;

import com.google.common.base.Strings;
import com.google.gson.annotations.Expose;

public class SchoolModel {


    // 大学名称
    @Expose
    private String name;

    // 大学描述
    @Expose
    private String description;

    // 图片
    @Expose
    private String picture;

    // 大学的创建者
    @Expose
    private String ownerId;

    // 省
    @Expose
    private String province;

    // 市
    @Expose
    private String city;

    // 县
    @Expose
    private String county;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
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

    // 校验
    public static boolean check(SchoolModel model) {
        return model != null
                && !Strings.isNullOrEmpty(model.province)
                && !Strings.isNullOrEmpty(model.city)
                && !Strings.isNullOrEmpty(model.county)
                && !Strings.isNullOrEmpty(model.name)
                && !Strings.isNullOrEmpty(model.picture)
                && !Strings.isNullOrEmpty(model.ownerId);
    }
}
