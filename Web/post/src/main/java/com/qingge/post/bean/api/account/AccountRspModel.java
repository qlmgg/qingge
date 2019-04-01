package com.qingge.post.bean.api.account;

import com.google.gson.annotations.Expose;
import com.qingge.post.bean.card.UserCard;
import com.qingge.post.bean.db.University;
import com.qingge.post.bean.db.User;
import com.qingge.post.factory.UniversityFactory;

public class AccountRspModel {
    // 用户基本信息
    @Expose
    private UserCard userCard;
    // 当前登录的账号
    @Expose
    private String account;
    // 当前登录成功后获取的Token,
    // 可以通过Token获取用户的所有信息
    @Expose
    private String token;
    // 标示是否已经绑定到了设备PushId
    @Expose
    private boolean isBind;

    public AccountRspModel(User user) {
        // 默认无绑定
        this(user, false);
    }

    public AccountRspModel(User user, boolean isBind) {

        String universityId = UniversityFactory.findByName(user.getSchoolName()).getId();

        this.userCard = new UserCard(user,universityId);
        this.account = user.getPhone();
        this.token = user.getToken();
        this.isBind = isBind;
    }


    public UserCard getUser() {
        return userCard;
    }

    public void setUser(UserCard user) {
        this.userCard = user;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isBind() {
        return isBind;
    }

    public void setBind(boolean bind) {
        isBind = bind;
    }
}
