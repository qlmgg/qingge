package com.yangsong.web.qingge.push.factory;

import com.yangsong.web.qingge.push.bean.db.Message;
import com.yangsong.web.qingge.push.bean.db.University;
import com.yangsong.web.qingge.push.bean.db.User;
import com.yangsong.web.qingge.push.utils.Hib;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UniversityFactory {
    //通过id找到大学
    public static University findById(String id){
        return Hib.query(session -> session.get(University.class, id));
    }

    //查询大学里的帖子
    public static List<Message> posts(University university){
        List<Message> msg = new ArrayList<>();
       return Hib.query(session -> {
            session.load(university,university.getId());//因为要用到懒加载的数据  所以这儿需要进行加载一次
            Set<Message> messages = university.getMessages();
           msg.addAll(messages);

            return msg;
        });
    }
}
