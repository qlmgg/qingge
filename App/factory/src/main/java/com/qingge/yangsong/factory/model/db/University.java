package com.qingge.yangsong.factory.model.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

import java.util.Date;
import java.util.Objects;

@Table(database = AppDatabase.class)
public class University extends BaseDbModel<University> {
    @PrimaryKey
    private String id; // 学校Id
    @Column
    private String name;// 学校名称
    @Column
    private String desc;// 学校描述
    @Column
    private String picture;// 学校图片
//    @Column
//    private int notifyLevel;// 我在学校中的消息通知级别-对象是我当前登录的账户
    @Column
    private String province; //省
    @Column
    private String city; //市
    @Column
    private String county; //县
    @Column
    private String ownerId; //创建者id

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

//    public int getNotifyLevel() {
//        return notifyLevel;
//    }
//
//    public void setNotifyLevel(int notifyLevel) {
//        this.notifyLevel = notifyLevel;
//    }


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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        University that = (University) o;
        return  Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(desc, that.desc) &&
                Objects.equals(picture, that.picture) &&
                Objects.equals(province, that.province) &&
                Objects.equals(city, that.city) &&
                Objects.equals(county, that.county);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean isSame(University old) {
        return Objects.equals(id, old.id);
    }

    @Override
    public boolean isUiContentSame(University old) {
        //TODO 这儿先暂时这么写  因为这儿我有点懵
        return Objects.equals(this.name, old.name)
                && Objects.equals(this.desc, old.desc)
                && Objects.equals(this.picture, old.picture);
    }
}
