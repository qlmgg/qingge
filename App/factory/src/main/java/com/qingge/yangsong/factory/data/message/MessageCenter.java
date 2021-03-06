package com.qingge.yangsong.factory.data.message;


import com.qingge.yangsong.factory.model.card.MessageCard;

/**
 * 消息中心，进行消息卡片的消费
 *
 */
public interface MessageCenter {
    void dispatch(MessageCard... cards);
}
