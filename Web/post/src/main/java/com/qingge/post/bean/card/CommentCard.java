package com.qingge.post.bean.card;

import com.google.gson.annotations.Expose;
import com.qingge.post.bean.db.Comment;
import com.qingge.post.bean.db.Post;
import com.qingge.post.bean.db.Reply;
import com.qingge.post.bean.db.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class CommentCard {
    @Expose
    private String id;

    //评论者
    @Expose
    private String senderId;
    //评论者名字
    @Expose
    private String senderName;
    //评论者头像
    @Expose
    private String commenterPortrait;
    //内容
    @Expose
    private String content;

    @Expose
    private LocalDateTime createAt;// 创建时间

    //附件
    @Expose
    private String attach;

    public CommentCard(Comment comment) {
        this.id = comment.getId();
        this.senderId = comment.getSenderId();
        this.attach = comment.getAttach();
        this.content = comment.getContent();
        this.createAt = comment.getCreateAt();
        this.senderName = comment.getSenderName();
        this.commenterPortrait = comment.getCommenterPortrait();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getCommenterPortrait() {
        return commenterPortrait;
    }

    public void setCommenterPortrait(String commenterPortrait) {
        this.commenterPortrait = commenterPortrait;
    }
}
