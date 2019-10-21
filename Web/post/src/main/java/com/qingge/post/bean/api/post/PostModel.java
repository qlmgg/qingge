package com.qingge.post.bean.api.post;

import com.google.common.base.Strings;
import com.google.gson.annotations.Expose;

import java.util.List;

public class PostModel {
    @Expose
    private String id;
    @Expose
    private String content;
    @Expose
    private String attach;
    @Expose
    private String senderId;
    @Expose
    private String senderName;
    @Expose
    private String senderPortrait;
    @Expose
    private String universityId;
    @Expose
    private List<String> pictureList;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getUniversityId() {
        return universityId;
    }

    public void setUniversityId(String universityId) {
        this.universityId = universityId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderPortrait() {
        return senderPortrait;
    }

    public void setSenderPortrait(String senderPortrait) {
        this.senderPortrait = senderPortrait;
    }

    public List<String> getPictureList() {
        return pictureList;
    }

    public void setPictureList(List<String> pictureList) {
        this.pictureList = pictureList;
    }

    // 校验
    public static boolean check(PostModel model) {
        return model != null
                && !Strings.isNullOrEmpty(model.content)
                && !Strings.isNullOrEmpty(model.senderName)
                && !Strings.isNullOrEmpty(model.senderPortrait)
                && !Strings.isNullOrEmpty(model.senderId)
                && !Strings.isNullOrEmpty(model.universityId);
    }
}
