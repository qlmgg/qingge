package com.qingge.post.factory;

import com.qingge.post.bean.api.post.PostModel;
import com.qingge.post.bean.api.post.WriteCommentModel;
import com.qingge.post.bean.db.Comment;
import com.qingge.post.bean.db.Post;
import com.qingge.post.bean.db.University;
import com.qingge.post.bean.db.User;
import com.qingge.post.utils.Hib;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PostFactory {

    //根据id找到帖子
    public static Post findPostById(String id) {
        return Hib.query(session -> session
                .get(Post.class, id));
    }


    //添加帖子
    // 添加一条普通消息
    public static Post add(User sender, University university, PostModel model) {
        Post post = new Post(sender, university, model);
        return Hib.query(session -> {
            session.save(post);
            return post;
        });
    }

    private static Comment save(Comment comment) {
        return Hib.query(session -> {
            session.save(comment);

            // 写入到数据库
            session.flush();

            // 紧接着从数据库中查询出来
            session.refresh(comment);
            return comment;
        });
    }


    //根据帖子的id拿到它的全部评论
    public static List<Comment> getComment(Post post) {
        List<Comment> comments = new ArrayList<>();
        return Hib.query(session -> {
            Set<Comment> set = post.getComments();
            comments.addAll(set);
            return comments;
        });
    }

    //写一条评论
    public static Comment writeComment(User self, User receiver, Post post, WriteCommentModel model) {
        Comment comment = new Comment(self, receiver, post, model);
        return save(comment);
    }


}
