package com.yangsong.web.qingge.push.service;

import com.google.common.base.Strings;
import com.yangsong.web.qingge.push.bean.api.message.PostModel;
import com.yangsong.web.qingge.push.bean.base.ResponseModel;
import com.yangsong.web.qingge.push.bean.card.MessageCard;
import com.yangsong.web.qingge.push.bean.db.Message;
import com.yangsong.web.qingge.push.bean.db.University;
import com.yangsong.web.qingge.push.bean.db.User;
import com.yangsong.web.qingge.push.factory.MessageFactory;
import com.yangsong.web.qingge.push.factory.UniversityFactory;
import com.yangsong.web.qingge.push.factory.UserFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Path("/message")
public class MessageService {
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
    public ResponseModel<List<MessageCard>> getPost(@PathParam("id") String id) {
        if (Strings.isNullOrEmpty(id)) {
            // 返回参数异常
            return ResponseModel.buildParameterError();
        }

        University university = UniversityFactory.findById(id);
        if (university == null) {
            // 没找到，返回没找到大学
            return ResponseModel.buildNotFoundUserError(null);
        }

        List<Message> messages = UniversityFactory.posts(university);
        //转换
        List<MessageCard> cards = messages.stream()
                .map(MessageCard::new)
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
        Message message = MessageFactory.add(sender,university,model);
        if (message == null)
            return ResponseModel.buildCreateError(ResponseModel.ERROR_CREATE_MESSAGE);
        return ResponseModel.buildOk();
    }
}
