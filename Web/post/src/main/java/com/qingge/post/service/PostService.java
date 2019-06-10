package com.qingge.post.service;

import com.google.common.base.Strings;

import com.qingge.post.bean.api.post.PostModel;
import com.qingge.post.bean.api.post.CommentModel;
import com.qingge.post.bean.base.ResponseModel;
import com.qingge.post.bean.card.AlbumCard;
import com.qingge.post.bean.card.CommentCard;
import com.qingge.post.bean.card.LoadPostCard;
import com.qingge.post.bean.card.PostCard;
import com.qingge.post.bean.db.*;
import com.qingge.post.factory.PostFactory;
import com.qingge.post.factory.UniversityFactory;
import com.qingge.post.factory.UserFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Path("/post")
public class PostService extends BaseService {
    @GET
    @Path("/test")
    public List<AlbumCard> test() {
        //测试 用帖子id查图片

//        PushModel model = new PushModel();
//        model.add(new PushModel.Entity(0,"你好呀!青鸽"));
//
//        PushDispatcher dispatcher = new PushDispatcher();
//        dispatcher.add(getSelf(),model);
//        dispatcher.submit();


        return PostFactory.getAlbumAddress("test26");
    }


    // 分页拿帖子
    @GET
    @Path("{id}|{page}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<LoadPostCard> getPost(@PathParam("id") String id, @PathParam("page") int page) {
        if (Strings.isNullOrEmpty(id)) {
            // 返回参数异常
            return ResponseModel.buildParameterError();
        }
        LoadPostCard loadPostCard = new LoadPostCard();
        loadPostCard.setPageCount(4);

        List<Post> posts = UniversityFactory.findByIdAndPage(id,page);

        //转换
        List<PostCard> cards = posts.stream()
                .map(PostCard::new)
                .collect(Collectors.toList());
        loadPostCard.setCards(cards);
        return ResponseModel.buildOk(loadPostCard);
    }


    // 拿帖子
    @GET
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<PostCard>> getPost(@PathParam("id") String id) {
        if (Strings.isNullOrEmpty(id)) {
            // 返回参数异常
            return ResponseModel.buildParameterError();
        }

//        University university = UniversityFactory.findById(id);
//        if (university == null) {
//            // 没找到，返回没找到学校
//            return ResponseModel.buildNotFoundUserError(null);
//        }

        List<Post> posts = UniversityFactory.postsById(id);

        //转换
        List<PostCard> cards = posts.stream()
                .map(PostCard::new)
                .collect(Collectors.toList());
        return ResponseModel.buildOk(cards);
    }

    //写帖子
    @PUT
    @Path("/write")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel writePost(PostModel model) {
        if (!PostModel.check(model)) {
            return ResponseModel.buildParameterError();//返回参数异常
        }
//TODO 加入图片集合
        User sender = UserFactory.findById(model.getSenderId());

        if (sender == null) {
            return ResponseModel.buildNotFoundUserError("没找到这个用户");
        }
        University university = UniversityFactory.findById(model.getUniversityId());

        if (university == null) {
            return ResponseModel.buildNotFoundUserError("没找到这个学校");
        }

        Post p = PostFactory.findPostById(model.getId());
        if (p != null){
            return ResponseModel.buildNotFoundUserError("已经存在这个帖子了");
        }
        Post post = PostFactory.add(sender, university, model);
        if (post == null)
            return ResponseModel.buildCreateError(ResponseModel.ERROR_CREATE_MESSAGE);
        return ResponseModel.buildOk();
    }

    //点击帖子或者点击帖子下面的评论图片
    @GET
    @Path("/comment/{postId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<CommentCard>> getComment(@PathParam("postId") String postId) {
        if (Strings.isNullOrEmpty(postId)) {
            return ResponseModel.buildParameterError();
        }
        //拿到帖子
        Post post = PostFactory.findPostById(postId);
        //从帖子中拿到评论集合
        List<Comment> comments = PostFactory.getComment(post);
        //转换成card
        List<CommentCard> commentCards = comments.stream()
                .map(CommentCard::new)
                .collect(Collectors.toList());

        return ResponseModel.buildOk(commentCards);
    }

    @PUT
    @Path("/comment/write")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<CommentCard> writeComment(CommentModel model) {
        if (!CommentModel.check(model)) {
            return ResponseModel.buildParameterError();
        }
        User sender = getSelf();
        //拿到评论所对应的帖子
        Post post = PostFactory.findPostById(model.getPostId());
        if (post == null)
            return ResponseModel.buildNotFoundUserError("没找到这个帖子");
        //拿到评论的接收者
        User receiver = UserFactory.findById(model.getReceiverId());
        if (receiver == null)
            return ResponseModel.buildNotFoundUserError("没找到这个用户");
        //写入数据库
        Comment comment = PostFactory.writeComment(sender, receiver, post, model);
        if (comment == null)
            return ResponseModel.buildCreateError(ResponseModel.ERROR_CREATE_COMMENT);
        //写入成功就直接build
        return ResponseModel.buildOk(new CommentCard(comment));

    }

}
