package com.qingge.yangsong.factory.model.account;

/**
 * 注册使用的请求Model
 *
 * @version 1.0.0
 */
public class RegisterModel {
    private String phone;
    private String password;
    private String pushId;
    private String name;
    private String schoolName;


    public RegisterModel(String account, String password,String name, String pushId,String schoolName) {
        this.phone = account;
        this.password = password;
        this.pushId = pushId;
        this.name = name;
        this.schoolName = schoolName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return phone;
    }

    public void setAccount(String account) {
        this.phone = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    @Override
    public String toString() {
        return "RegisterModel{" +
                "account='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", pushId='" + pushId + '\'' +
                '}';
    }
}
