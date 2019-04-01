package com.qingge.post.bean.db;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 这是回复表
 */

@Entity
@Table(name = "TB_REPLY")
public class Reply {
    @Id
    @PrimaryKeyJoinColumn
    // 主键生成存储的类型为UUID，自动生成UUID
    @GeneratedValue(generator = "uuid")
    // 把uuid的生成器定义为uuid2，uuid2是常规的UUID toString
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    // 不允许更改，不允许为null
    @Column(updatable = false, nullable = false)
    private String id;


    //此回复的所在评论
    @JoinColumn(name = "commentId")
    @ManyToOne(optional = false,cascade = CascadeType.ALL)
    private Comment comment;
    @Column(nullable = false,insertable = false,updatable = false)
    private String commentId;

    //回复人
    @JoinColumn(name = "senderId")
    //不可选,必须有一个发送者,急加载, 串联级别ALL
    @ManyToOne(optional = false, fetch = FetchType.EAGER , cascade = CascadeType.ALL)
    private User sender;
    @Column(insertable = false,updatable = false,nullable = false)
    private String senderId;

    //被回复人
    @JoinColumn(name = "recerverId")
    //急加载, 串联级别ALL
    @ManyToOne(fetch = FetchType.EAGER , cascade = CascadeType.ALL)
    private User recerver;
    @Column(insertable = false,updatable = false)
    private String recerverId;

    //回复类型
    @Column(nullable = false)
    private int replyType;

    //类容
    @Column(nullable = false)
    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public User getRecerver() {
        return recerver;
    }

    public void setRecerver(User recerver) {
        this.recerver = recerver;
    }

    public String getRecerverId() {
        return recerverId;
    }

    public void setRecerverId(String recerverId) {
        this.recerverId = recerverId;
    }

    public int getReplyType() {
        return replyType;
    }

    public void setReplyType(int replyType) {
        this.replyType = replyType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
