package com.yangsong.web.qingge.push.factory;

import com.yangsong.web.qingge.push.bean.api.message.PostModel;
import com.yangsong.web.qingge.push.bean.db.Message;
import com.yangsong.web.qingge.push.bean.db.University;
import com.yangsong.web.qingge.push.bean.db.User;
import com.yangsong.web.qingge.push.utils.Hib;

public class MessageFactory {
    //添加帖子
    // 添加一条普通消息
    public static Message add(User sender, University university, PostModel model) {
        Message message = new Message(sender,university,model);
        return save(message);
    }
    private static Message save(Message message) {
        return Hib.query(session -> {
            session.save(message);

            // 写入到数据库
            session.flush();

            // 紧接着从数据库中查询出来
            session.refresh(message);
            return message;
        });
    }
}
