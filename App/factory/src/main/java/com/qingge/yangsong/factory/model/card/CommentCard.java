package com.qingge.yangsong.factory.model.card;


import com.qingge.yangsong.factory.utils.DiffUiDataCallback;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class CommentCard implements DiffUiDataCallback.UiDataDiffer<CommentCard> {
    private String id;
    //评论者
    private String senderId;
    //内容
    private String content;
    //评论者名字
    private String senderName;
    //评论者头像
    private String commenterPortrait;
    // 创建时间
    private Date createAt;
    //附件
    private String attach;



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

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getCommenterPortrait() {
        return commenterPortrait;
    }

    public void setCommenterPortrait(String commenterPortrait) {
        this.commenterPortrait = commenterPortrait;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    @Override
    public boolean isSame(CommentCard old) {
        return Objects.equals(id, old.id);
    }

    @Override
    public boolean isUiContentSame(CommentCard old) {
        return true;
    }
}
