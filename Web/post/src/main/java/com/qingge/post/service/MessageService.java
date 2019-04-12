package com.qingge.post.service;


import com.qingge.post.bean.api.message.MessageCreateModel;
import com.qingge.post.bean.base.ResponseModel;
import com.qingge.post.bean.card.MessageCard;
import com.qingge.post.bean.db.Group;
import com.qingge.post.bean.db.Message;
import com.qingge.post.bean.db.User;
import com.qingge.post.factory.GroupFactory;
import com.qingge.post.factory.MessageFactory;
import com.qingge.post.factory.PushFactory;
import com.qingge.post.factory.UserFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * 消息发送的入口
 *
 */
@Path("/msg")
public class MessageService extends BaseService {

    @GET
    @Path("/test")
    public String test() {
        return "测试成功";
    }

    // 发送一条消息到服务器
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<MessageCard> pushMessage(MessageCreateModel model) {
        if (!MessageCreateModel.check(model)) {
            return ResponseModel.buildParameterError();
        }

        User self = getSelf();

        // 查询是否已经在数据库中有了
        Message message = MessageFactory.findById(model.getId());
        if (message != null) {
            // 正常返回
            return ResponseModel.buildOk(new MessageCard(message));
        }

        if (model.getReceiverType() == Message.RECEIVER_TYPE_GROUP) {
            return pushToGroup(self, model);
        } else {
            return pushToUser(self, model);
        }
    }

    // 发送到人
    private ResponseModel<MessageCard> pushToUser(User sender, MessageCreateModel model) {
        User receiver = UserFactory.findById(model.getReceiverId());
        if (receiver == null) {
            // 没有找到接收者
            return ResponseModel.buildNotFoundUserError("Con't find receiver user");
        }

        if (receiver.getId().equalsIgnoreCase(sender.getId())) {
            // 发送者接收者是同一个人就返回创建消息失败
            return ResponseModel.buildCreateError(ResponseModel.ERROR_CREATE_MESSAGE);
        }

        // 存储数据库
        Message message = MessageFactory.add(sender, receiver, model);

        return buildAndPushResponse(sender, message);
    }

    // 发送到群
    private ResponseModel<MessageCard> pushToGroup(User sender, MessageCreateModel model) {
        // 找群是有权限性质的找
        Group group = GroupFactory.findById(sender, model.getReceiverId());
        if (group == null) {
            // 没有找到接收者群，有可能是你不是群的成员
            return ResponseModel.buildNotFoundUserError("Con't find receiver group");
        }

        // 添加到数据库
        Message message = MessageFactory.add(sender, group, model);

        // 走通用的推送逻辑
        return buildAndPushResponse(sender, message);
    }

    // 推送并构建一个返回信息
    private ResponseModel<MessageCard> buildAndPushResponse(User sender, Message message) {
        if (message == null) {
            // 存储数据库失败
            return ResponseModel.buildCreateError(ResponseModel.ERROR_CREATE_MESSAGE);
        }

        // 进行推送
        PushFactory.pushNewMessage(sender, message);

        // 返回
        return ResponseModel.buildOk(new MessageCard(message));
    }
}
