package com.qingge.post.bean.api.post;

import com.google.common.base.Strings;
import com.google.gson.annotations.Expose;

public class WriteCommentModel {
    @Expose
    private String id;
    @Expose
    private String content;
    @Expose
    private String senderId;
    @Expose
    private String receiverId;
    @Expose
    private String senderName;
    @Expose
    private String commenterPortrait;
    @Expose
    private String attach;
    @Expose
    private String postId;

    public static boolean check(WriteCommentModel model) {
        return model != null
                && !Strings.isNullOrEmpty(model.senderName)
                && !Strings.isNullOrEmpty(model.commenterPortrait)
                && !Strings.isNullOrEmpty(model.content)
                && !Strings.isNullOrEmpty(model.senderId)
                && !Strings.isNullOrEmpty(model.receiverId)
                && !Strings.isNullOrEmpty(model.postId);
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

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
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
