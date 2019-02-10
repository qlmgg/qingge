package com.qingge.post.bean.api.account;

import com.google.common.base.Strings;
import com.google.gson.annotations.Expose;

public class LoginModel {
    @Expose
    private String phone;
    @Expose
    private String password;
    @Expose
    private String pushId;

    public static boolean check(LoginModel model){
        return model!=null
                && !Strings.isNullOrEmpty(model.getPhone())
                && !Strings.isNullOrEmpty(model.getPassword());
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }
}
