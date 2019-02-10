package com.qingge.post.factory;

import com.qingge.post.bean.db.Post;
import com.qingge.post.bean.db.University;
import com.qingge.post.utils.Hib;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class UniversityFactory {
    //通过id找到大学
    public static University findById(String id){
        return Hib.query(session -> session.get(University.class, id));
    }

    //查询大学里的帖子
    public static List<Post> posts(University university){
        List<Post> msg = new ArrayList<>();
        return Hib.query(session -> {
            session.load(university,university.getId());//因为要用到懒加载的数据  所以这儿需要进行加载一次
            Set<Post> posts = university.getMessages();
            msg.addAll(posts);

            return msg;
        });
    }
}
