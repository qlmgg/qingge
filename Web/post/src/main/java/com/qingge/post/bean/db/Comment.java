package com.qingge.post.bean.db;


import com.qingge.post.bean.api.post.WriteCommentModel;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


/**
 * 这是评论表
 */

@Entity
@Table(name = "TB_COMMENT")
public class Comment {
    @Id
    @PrimaryKeyJoinColumn
    // 主键生成存储的类型为UUID，自动生成UUID
//    @GeneratedValue(generator = "uuid")
    // 把uuid的生成器定义为uuid2，uuid2是常规的UUID toString
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    // 不允许更改，不允许为null
    @Column(updatable = false, nullable = false)
    private String id;

    //对应的评论者
    @JoinColumn(name = "senderId")
    //不可选,必须有一个发送者,急加载, 串联级别MERGE
    @ManyToOne(optional = false, fetch = FetchType.EAGER , cascade = CascadeType.MERGE)
    private User sender;

    @Column(insertable = false,updatable = false,nullable = false)
    private String senderId;

    //评论者名字
    @Column
    private String senderName;

    //评论者头像
    @Column
    private String commenterPortrait;

    //对应的接收人
    @JoinColumn(name = "receiverId")
    //不可选,必须有一个发送者,急加载, 串联级别MERGE
    @ManyToOne(optional = false, fetch = FetchType.EAGER , cascade = CascadeType.MERGE)
    private User receiver;

    @Column(insertable = false,updatable = false,nullable = false)
    private String receiverId;

    // 对应的帖子的id
    @JoinColumn(name = "postId")
    @ManyToOne(optional = false, fetch = FetchType.EAGER , cascade = CascadeType.ALL)
    private Post post;
    @Column(insertable = false,updatable = false,nullable = false)
    private String postId;

    // 此评论对应的回复集合
    //懒加载
    @JoinColumn(name = "commentId")
    @LazyCollection(LazyCollectionOption.EXTRA)
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<Reply> replies = new HashSet<>();

    //类容
    @Column(nullable = false)
    private String content;

    //附件
    @Column
    private String attach;

    //创建时间
    @CreationTimestamp
    private LocalDateTime createAt = LocalDateTime.now();


    public Comment(User sender,User receiver , Post post, WriteCommentModel model) {
        this.id = model.getId();
        this.sender = sender;
        this.post = post;
        this.content = model.getContent();
        this.receiver = receiver;
        this.senderName = model.getSenderName();
        this.commenterPortrait = model.getCommenterPortrait();
    }

    public Comment() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public Set<Reply> getReplies() {
        return replies;
    }

    public void setReplies(Set<Reply> replies) {
        this.replies = replies;
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

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
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
