package com.qingge.post.bean.card;

import com.google.gson.annotations.Expose;
import com.qingge.post.bean.db.Post;

import java.time.LocalDateTime;

public class PostCard {
    @Expose
    private String id; // Id
    @Expose
    private String content;// 内容
    @Expose
    private String attach;// 附件，附属信息
    @Expose
    private LocalDateTime createAt;// 创建时间
    @Expose
    private String senderId;// 发送者Id，不为空
    @Expose
    private String senderName;// 发送者名字
    @Expose
    private String senderPortrait;// 发送者头像
    @Expose
    private String universityId;// 学校Id，不为空
    @Expose
    private int comments;// 帖子评论数量
    @Expose
    private int fabulous;// 帖子点赞数量

    public PostCard(Post post) {
        this.id = post.getId();
        this.content = post.getContent();
        this.attach = post.getAttach();
        this.createAt = post.getCreateAt();
        this.universityId = post.getUniversityId();
        this.comments = post.getComments().size();
        this.fabulous = post.getFabulous();
        this.senderId = post.getSenderId();
        this.senderName = post.getSenderName();
        this.senderPortrait = post.getSenderPortrait();
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

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
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

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getFabulous() {
        return fabulous;
    }

    public void setFabulous(int fabulous) {
        this.fabulous = fabulous;
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
