package com.qingge.yangsong.factory.model.post;

import java.util.List;
import java.util.UUID;

/**
 * Created by White paper on 2019/10/19
 * Describe :
 */
public class CreatePostModel {
    private String id;
    private String content;
    private String attach;
    private String senderId;
    private String senderName;
    private String senderPortrait;
    private String universityId;
    private List<String> pictureList;

    public CreatePostModel(String content, String senderId, String senderName,
                           String senderPortrait, String universityId, List<String> pictureList) {
        this.id = UUID.randomUUID().toString();
        this.content = content;
        this.senderId = senderId;
        this.senderName = senderName;
        this.senderPortrait = senderPortrait;
        this.universityId = universityId;
        this.pictureList = pictureList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getUniversityId() {
        return universityId;
    }

    public void setUniversityId(String universityId) {
        this.universityId = universityId;
    }

    public List<String> getPictureList() {
        return pictureList;
    }

    public void setPictureList(List<String> pictureList) {
        this.pictureList = pictureList;
    }
}
