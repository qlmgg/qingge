package com.qingge.post.service;

import com.google.common.base.Strings;
import com.qingge.post.bean.api.post.PostModel;
import com.qingge.post.bean.base.ResponseModel;
import com.qingge.post.bean.card.PostCard;
import com.qingge.post.bean.db.Post;
import com.qingge.post.bean.db.University;
import com.qingge.post.bean.db.User;
import com.qingge.post.factory.PostFactory;
import com.qingge.post.factory.UniversityFactory;
import com.qingge.post.factory.UserFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Path("/post")
public class PostService {
    @GET
    @Path("/test")
    public String test(){
        return "测试成功";
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

        University university = UniversityFactory.findById(id);
        if (university == null) {
            // 没找到，返回没找到大学
            return ResponseModel.buildNotFoundUserError(null);
        }

        List<Post> posts = UniversityFactory.posts(university);
        //转换
        List<PostCard> cards = posts.stream()
                .map(PostCard::new)
                .collect(Collectors.toList());
        return ResponseModel.buildOk(cards);
    }

    //写帖子
    @PUT
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel writePost(PostModel model){
        if (!PostModel.check(model)){
            return ResponseModel.buildParameterError();//返回参数异常
        }
        User sender = UserFactory.findById(model.getSenderId());
        University university = UniversityFactory.findById(model.getUniversityId());
        Post post = PostFactory.add(sender,university,model);
        if (post == null)
            return ResponseModel.buildCreateError(ResponseModel.ERROR_CREATE_MESSAGE);
        return ResponseModel.buildOk();
    }
}
