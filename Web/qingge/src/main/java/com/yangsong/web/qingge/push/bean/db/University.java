package com.yangsong.web.qingge.push.bean.db;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "TB_UNIVERSITY")
public class University {

        // 这是一个主键
        @Id
        @PrimaryKeyJoinColumn
        // 主键生成存储的类型为UUID，自动生成UUID
        @GeneratedValue(generator = "uuid")
        // 把uuid的生成器定义为uuid2，uuid2是常规的UUID toString
        @GenericGenerator(name = "uuid", strategy = "uuid2")
        // 不允许更改，不允许为null
        @Column(updatable = false, nullable = false)
        private String id;

        // 大学名称
        @Column(nullable = false)
        private String name;

        // 大学描述
        @Column(nullable = false)
        private String description;
        // 图片
        @Column(nullable = false)
        private String picture;

        // 定义为创建时间戳，在创建时就已经写入
        @CreationTimestamp
        @Column(nullable = false)
        private LocalDateTime createAt = LocalDateTime.now();

        // 定义为更新时间戳，在创建时就已经写入
        @UpdateTimestamp
        @Column(nullable = false)
        private LocalDateTime updateAt = LocalDateTime.now();

        // 大学的创建者
        // optional: 可选为false，必须有一个创建者
        // fetch: 加载方式FetchType.EAGER，急加载，
        // 意味着加载群的信息的时候就必须加载owner的信息：联级级别为ALL，所有的更改（更新，删除等）都将进行关系更新
        // cascade
        @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
        @JoinColumn(name = "ownerId")
        private User owner;
        @Column(nullable = false, updatable = false, insertable = false)
        private String ownerId;

        @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
        @LazyCollection(LazyCollectionOption.EXTRA)//懒加载
        @JoinColumn(name = "UniversityId")
        private Set<Message> messages = new HashSet<>();

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }
}
