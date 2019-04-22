package com.qingge.post.bean.card;

import com.google.gson.annotations.Expose;
import com.qingge.post.bean.db.University;
import com.qingge.post.bean.db.User;
import com.qingge.post.utils.Hib;

import java.time.LocalDateTime;

public class UserCard {
    @Expose
    private String id;
    @Expose
    private String name;
    @Expose
    private String phone;
    @Expose
    private String portrait;
    @Expose
    private String desc;
    @Expose
    private int sex = 0;

    // 用户的学校
    @Expose
    private String schoolName;

    // 用户关注人的数量
    @Expose
    private int follows;

    // 用户粉丝的数量
    @Expose
    private int following;

    // 我与当前User的关系状态，是否已经关注了这个人
    @Expose
    private boolean isFollow;

    // 用户信息最后的更新时间
    @Expose
    private LocalDateTime modifyAt;

    // 用户的学校id
    @Expose
    private String universityId;

    public UserCard(final User user,String universityId) {
        this(user, universityId,false);
    }


    public UserCard(final User user,String universityId,boolean isFollow) {
        this.isFollow = isFollow;
        this.schoolName = user.getSchoolName();
        this.id = user.getId();
        this.name = user.getName();
        this.phone = user.getPhone();
        this.portrait = user.getPortrait();
        this.desc = user.getDescription();
        this.sex = user.getSex();
        this.modifyAt = user.getUpdateAt();
        this.universityId = universityId;

        Hib.queryOnly(session -> {
            session.load(user,user.getId());
            this.follows = user.getFollowers().size();
            this.following = user.getFollowing().size();
        });
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getFollows() {
        return follows;
    }

    public void setFollows(int follows) {
        this.follows = follows;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }

    public LocalDateTime getModifyAt() {
        return modifyAt;
    }

    public void setModifyAt(LocalDateTime modifyAt) {
        this.modifyAt = modifyAt;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getUniversityId() {
        return universityId;
    }

    public void setUniversityId(String universityId) {
        this.universityId = universityId;
    }
}
