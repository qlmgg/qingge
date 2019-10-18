package com.qingge.yangsong.factory.data.message;


import com.qingge.yangsong.factory.data.DbDataSource;
import com.qingge.yangsong.factory.model.db.Message;

/**
 * 消息的数据源定义，他的实现是：MessageRepository
 * 关注的对象是Message表
 *
 */
public interface MessageDataSource extends DbDataSource<Message> {
}
