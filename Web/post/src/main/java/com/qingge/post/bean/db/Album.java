package com.qingge.post.bean.db;


import com.qingge.post.bean.card.AlbumCard;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_ALBUM")
public class Album {
    /*
     * UUID是为了保证id的安全性
     * */
    //这是一个主键
    @Id
    @PrimaryKeyJoinColumn
    //主键生成存储的类型为uuid  generated : 生成
    @GeneratedValue(generator = "uuid")
    //把uuid的生成器定义为uuid2,uuid2是常规的uuid  toString
    //  这个uuid2是对生成的id显示得容易观察
    // generator : 生成器
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    //禁止自己更新,禁止为空
    @Column(updatable = false, nullable = false)
    private String id;

    @Column(nullable = false)
    private String address;

    //所处的帖子
    @ManyToOne
    @JoinColumn(name = "postId")
    private Post post;
    @Column(insertable = false, updatable = false)
    private String postId;

    //所处的群
    @ManyToOne
    @JoinColumn(name = "groupId")
    private Group group;
    @Column(insertable = false, updatable = false)
    private String groupId;

    //所处的用户
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
    @Column(insertable = false, updatable = false)
    private String userId;

    //创建时间
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createAt = LocalDateTime.now();

    public Album() {
    }

    public Album(String address, Post post, Group group, User user) {
        this.address = address;
        this.post = post;
        this.group = group;
        this.user = user;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }


}
