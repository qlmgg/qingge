package com.qingge.yangsong.factory.model.db;


import android.util.Log;

import com.qingge.yangsong.factory.model.card.AlbumCard;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Table(database = AppDatabase.class)
public class Post extends BaseDbModel<Post> {
    @PrimaryKey
    private String id;//主键
    @Column
    private String content;// 内容
    @Column
    private String attach;// 附属信息
    @Column
    private Date createAt;// 创建时间
    @Column
    private int fabulousNumber;//点赞量
    @Column
    private int commentNumber; //评论量
    @Column
    private String senderId; //发送者id
    @Column
    private String senderName; //发送者名字
    @Column
    private String senderPortrait; //发送者头像

    private List<AlbumCard> images;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return fabulousNumber == post.fabulousNumber &&
                commentNumber == post.commentNumber &&
                Objects.equals(senderId, post.senderId) &&
                Objects.equals(senderName, post.senderName) &&
                Objects.equals(senderPortrait, post.senderPortrait) &&
                Objects.equals(id, post.id) &&
                isListEqual(images, post.images) &&
                Objects.equals(content, post.content) &&
                Objects.equals(attach, post.attach) &&
                Objects.equals(createAt, post.createAt);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
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

    public List<AlbumCard> getImages() {
        return images;
    }

    public void setImages(List<AlbumCard> images) {
        this.images = images;
    }

    @Override
    public boolean isSame(Post old) {
        return Objects.equals(id, old.id);
    }

    @Override
    public boolean isUiContentSame(Post old) {
        return Objects.equals(this.senderPortrait, old.senderPortrait)
                && Objects.equals(this.attach, old.attach)
                && Objects.equals(this.content, old.content)
                && Objects.equals(this.senderId, old.senderId)
                && Objects.equals(this.senderName, old.senderName)
                && Objects.equals(this.senderPortrait, old.senderPortrait)
                && Objects.equals(this.commentNumber, old.commentNumber)
                && isListEqual(this.images, old.images)
                && Objects.equals(this.fabulousNumber, old.fabulousNumber);
    }

    //对数组中的元素进行对比
    private static boolean isListEqual(List<AlbumCard> newList, List<AlbumCard> oldList) {

        if (newList == oldList)//是不是同一个对象
            return true;
        if (newList == null || oldList == null)//一个为空,不等
            return false;
        if (newList.size() != oldList.size())//先判断大小是否等
            return false;
        if (oldList.size() == 0)//如果list中数据为0 则返回true
            return true;

        for (AlbumCard newCard : newList) {//遍历对比  拿新的第一个和旧的比,找不到就返回false  找到就下一个,同上,
            boolean isSame = false;
            for (AlbumCard oldCard : oldList) {
                isSame = newCard.getAddress().equals(oldCard.getAddress()); //如果找到就结束当前for,
                if (isSame)
                    break;
            }
            if (!isSame) //如果一轮对比完都没找到,就返回false
                return false;
        }

        return true;
    }

}
