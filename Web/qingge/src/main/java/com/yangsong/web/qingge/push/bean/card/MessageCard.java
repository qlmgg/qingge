package com.yangsong.web.qingge.push.bean.card;

import com.google.gson.annotations.Expose;
import com.yangsong.web.qingge.push.bean.db.Message;

import java.time.LocalDateTime;

public class MessageCard {
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


    public MessageCard(Message message) {
        this.id = message.getId();
        this.content = message.getContent();
        this.attach = message.getAttach();
        this.createAt = message.getCreateAt();
        this.senderId = message.getSenderId();
    }
}
