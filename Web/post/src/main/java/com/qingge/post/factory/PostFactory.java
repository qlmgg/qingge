package com.qingge.post.factory;

import com.qingge.post.bean.api.post.PostModel;
import com.qingge.post.bean.api.post.CommentModel;
import com.qingge.post.bean.card.AlbumCard;
import com.qingge.post.bean.db.*;
import com.qingge.post.utils.Hib;

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
            session.flush();
            session.refresh(post);

            if (model.getPictureList()!=null){
                for (String path : model.getPictureList()) {
                    Album album = new Album(path,post,null,sender);
                    session.save(album);
                }
            }

            return post;
        });
    }

    private static Comment save(Comment comment) {
        return Hib.query(session -> {
            session.save(comment);

            // 写入到数据库

//            session.flush();
            // 紧接着从数据库中查询出来
//            session.refresh(comment);
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
    public static Comment writeComment(User self, User receiver, Post post, CommentModel model) {
        Comment comment = new Comment(self, receiver, post, model);
        return save(comment);
    }

    //用帖子id拿到它的图片集合
    public static List<AlbumCard> getAlbumAddress(String postId) {
        List<AlbumCard> cards = new ArrayList<>();

        return Hib.query(session -> {
            @SuppressWarnings("unchecked")
            List<Album> lists = session.createQuery("from Album where postId=:postId")
                    .setParameter("postId", postId)
                    .list();
            for (Album album : lists) {
                cards.add(new AlbumCard(album.getAddress()));
            }
            return cards;
        });

    }

}
