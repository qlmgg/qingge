package com.qingge.yangsong.factory.model.card;

import com.qingge.yangsong.factory.model.db.BaseDbModel;
import com.qingge.yangsong.factory.model.db.Post;
import com.qingge.yangsong.factory.model.db.University;
import com.qingge.yangsong.factory.model.db.User;

import java.time.LocalDateTime;
import java.util.Date;

public class PostCard{
    private String id; // Id
    private String content;// 内容
    private String attach;// 附件，附属信息
    private Date createAt;// 创建时间
    private String senderId;// 发送者Id
    private String senderName;// 发送者名字
    private String senderPortrait;// 发送者头像
    private String universityId;// 学校Id
    private int fabulousNumber;//点赞量
    private int commentNumber; //评论量


    public Post build(){
        Post post = new Post();
        post.setId(id);
        post.setContent(content);
        post.setAttach(attach);
        post.setCreateAt(createAt);
        post.setSenderId(senderId);
        post.setCommentNumber(commentNumber);
        post.setFabulousNumber(fabulousNumber);
        post.setSenderName(senderName);
        post.setSenderPortrait(senderPortrait);
        return post;
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

    public int getFabulousNumber() {
        return fabulousNumber;
    }

    public void setFabulousNumber(int fabulousNumber) {
        this.fabulousNumber = fabulousNumber;
    }

    public int getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(int commentNumber) {
        this.commentNumber = commentNumber;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
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

}
