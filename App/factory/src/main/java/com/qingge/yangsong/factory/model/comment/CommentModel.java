package com.qingge.yangsong.factory.model.comment;

import com.google.gson.annotations.Expose;

import java.util.UUID;

public class CommentModel {
    private String id;
    private String content;//内容
    private String senderId;//发送者id
    private String receiverId;//接收者id
    private String senderName;//发送者名字
    private String commenterPortrait;//发送者头像
    private String attach;//附件
    private String postId;//帖子的id

    public CommentModel() {
        //随机生成一个id
        this.id = UUID.randomUUID().toString();
    }

    //建造者模式
    public static class Builder {
        private CommentModel model;

        public Builder() {
            model = new CommentModel();
        }

        public Builder setSenderIdAndReceiverId(String senderId, String receiverId) {
            model.senderId = senderId;
            model.receiverId = receiverId;
            return this;
        }

        public Builder setSenderNameAndPortrait(String name, String portrait) {
            model.senderName = name;
            model.commenterPortrait = portrait;
            return this;
        }

        public Builder setContentAndPostId(String content, String postId) {
            model.content = content;
            model.postId = postId;
            return this;
        }

        public Builder setAttach(String attach){
            model.attach = attach;
            return this;
        }

        public CommentModel build(){
            return this.model;
        }
    }

}
