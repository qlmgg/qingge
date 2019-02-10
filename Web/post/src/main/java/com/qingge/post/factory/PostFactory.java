package com.qingge.post.factory;

import com.qingge.post.bean.api.post.PostModel;
import com.qingge.post.bean.db.Post;
import com.qingge.post.bean.db.University;
import com.qingge.post.bean.db.User;
import com.qingge.post.utils.Hib;

public class PostFactory {
    //添加帖子
    // 添加一条普通消息
    public static Post add(User sender, University university, PostModel model) {
        Post post = new Post(sender,university,model);
        return save(post);
    }
    private static Post save(Post post) {
        return Hib.query(session -> {
            session.save(post);

            // 写入到数据库
            session.flush();

            // 紧接着从数据库中查询出来
            session.refresh(post);
            return post;
        });
    }
}
