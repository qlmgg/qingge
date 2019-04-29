package com.qingge.post.service;


import com.google.common.base.Strings;
import com.qingge.post.bean.api.group.GroupCreateModel;
import com.qingge.post.bean.api.group.GroupMemberAddModel;
import com.qingge.post.bean.api.group.GroupMemberUpdateModel;
import com.qingge.post.bean.base.ResponseModel;
import com.qingge.post.bean.card.ApplyCard;
import com.qingge.post.bean.card.GroupCard;
import com.qingge.post.bean.card.GroupMemberCard;
import com.qingge.post.bean.db.*;
import com.qingge.post.factory.GroupFactory;
import com.qingge.post.factory.PushFactory;
import com.qingge.post.factory.UniversityFactory;
import com.qingge.post.factory.UserFactory;
import org.omg.CORBA.UNKNOWN;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Path("/group")
public class GroupService extends BaseService {
    /**
     * 创建群
     *
     * @param model 基本参数
     * @return 群信息
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<GroupCard> create(GroupCreateModel model) {
        if (!GroupCreateModel.check(model)) {
            return ResponseModel.buildParameterError();
        }

        // 创建者
        User creator = getSelf();
        // 创建者并不在列表中
        //  改; 创建时可以就社长一人
//        model.getUsers().remove(creator.getId());
        if (model.getUsers().size() == 0) {
            return ResponseModel.buildParameterError();
        }

        // 检查是否已有
        if (GroupFactory.findByName(model.getName()) != null) {
            return ResponseModel.buildHaveNameError();
        }
        //群成员
        List<User> users = new ArrayList<>();
        for (String s : model.getUsers()) {
            User user = UserFactory.findById(s);
            if (user == null)
                continue;
            users.add(user);
        }
        // 没有一个成员
        if (users.size() == 0) {
            return ResponseModel.buildParameterError();
        }
        //找到创建人所在的学校
        University university = UniversityFactory.findById(creator.getUniversityId());
        if (university == null)
            return ResponseModel.buildParameterError();

        Group group = GroupFactory.create(creator, university, model, users);
        if (group == null) {
            // 服务器异常
            return ResponseModel.buildServiceError();
        }

        // 拿管理员的信息（自己的信息）
        GroupMember creatorMember = GroupFactory.getMember(creator.getId(), group.getId());
        if (creatorMember == null) {
            // 服务器异常
            return ResponseModel.buildNotFoundGroupError("测试异常,这是第二个");
        }

        // 拿到群的成员，给所有的群成员发送信息，已经被添加到群的信息
        Set<GroupMember> members = GroupFactory.getMembers(group);
        if (members == null) {
            // 服务器异常
            return ResponseModel.buildNotFoundGroupError("测试异常,这是第三个");
        }

        members = members.stream()
                .filter(groupMember -> !groupMember.getId().equalsIgnoreCase(creatorMember.getId()))
                .collect(Collectors.toSet());

        // 开始发起推送
        PushFactory.pushJoinGroup(members);

        return ResponseModel.buildOk(new GroupCard(creatorMember));
    }

    /**
     * 申请加入群
     * 此时会创建一个加入的申请，并写入表；然后会给管理员发送消息
     * 管理员同意，其实就是调用添加成员的接口把对应的用户添加进去
     *
     * @param groupId 要加入的群的id
     * @return 申请的信息
     */
    @POST
    @Path("/applyJoin/{groupId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<ApplyCard> applyAddGroup(@PathParam("groupId") String groupId) {
        if (Strings.isNullOrEmpty(groupId)) {
            return ResponseModel.buildParameterError();
        }
        User self = getSelf();

        //是否在群中
        GroupMember member = GroupFactory.getMember(self.getId(),groupId);
        if (member !=null)
            return ResponseModel.buildHaveGroupMemberError("当前用户已经在群中了");

        Apply apply = GroupFactory.applyAddGroup(self, groupId);
        if (apply == null)
            return ResponseModel.buildServiceError();
        //TODO 发起一个推送给管理员
        return ResponseModel.buildOk(new ApplyCard(apply));
    }

    /**
     * 添加群成员,只有管理员有权限
     *
     * @param model 基本参数
     * @return 添加成员列表
     */
    @POST
    @Path("/{groupId}/member")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<GroupMemberCard>> addGroupMember(@PathParam("groupId") String groupId, GroupMemberAddModel model) {
        if (!GroupMemberAddModel.check(model)) {
            return ResponseModel.buildParameterError();
        }

        User self = getSelf();

        //检查是否存在群
        Group group = GroupFactory.findById(groupId);
        if (group == null) {
            return ResponseModel.buildNotFoundGroupError("没找到这个群");
        }
        //检查自己是否时群成员并且是否有权限
        GroupMember member = GroupFactory.getMember(self.getId(), groupId);
        if (member == null || member.getPermissionType() == GroupMember.NOTIFY_LEVEL_NONE) {
            return ResponseModel.buildNoPermissionError();
        }
        //拿到旧成员,用于通知
        Set<GroupMember> oldMembers = GroupFactory.getMembers(group);
        if (oldMembers == null || oldMembers.size() == 0) {//没有的话那就是服务器出问题了
            ResponseModel.buildServiceError();
        }
        //把上集合的数据的userId存到下oldMemberUserIds集合中
        Set<String> oldMemberUserIds = oldMembers.stream()
                .map(GroupMember::getUserId)
                .collect(Collectors.toSet());

        //要添加的用户
        List<User> addUsers = new ArrayList<>();
        for (String userId : model.getUsers()) {
            User user = UserFactory.findById(userId);
            if (user == null)
                continue;
            //已经在群里面了
            if (oldMemberUserIds.contains(user.getId()))
                continue;
            addUsers.add(user);
        }

        //如果没有要插入的人
        if (addUsers.size() == 0)
            return ResponseModel.buildParameterError();

        //开始添加
        Set<GroupMember> addMembers = GroupFactory.addMembers(addUsers, group);
        if (addMembers == null)
            return ResponseModel.buildServiceError();
        //转换
        List<GroupMemberCard> addMemberCards = addMembers.stream()
                .map(GroupMemberCard::new)  //这就是个循环,把addMembers里面的参数循环new成Card
                .collect(Collectors.toList()); // 添加到addMemberCards中去

        // 通知，两部曲
        // 1.通知新增的成员，你被加入了XXX群
        PushFactory.pushJoinGroup(addMembers);
        // 2.通知群中老的成员，有XXX，XXX加入群
        PushFactory.pushGroupMemberAdd(oldMembers, addMemberCards);

        return ResponseModel.buildOk(addMemberCards);
    }

    /**
     * 修改成员信息  只能自己和群管理可以修改
     *
     * @param model 修改的model
     * @return 当前成员的信息
     */
    @PUT
    @Path("/member/modify/{memberId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<GroupMemberCard> modifyGroupMember(@PathParam("memberId") String memberId, GroupMemberUpdateModel model) {
        if (!GroupMemberUpdateModel.check(model))
            return ResponseModel.buildParameterError();
        User self = getSelf();

        //拿到要修改的成员
        GroupMember member = GroupFactory.getMember(memberId);
        if (member == null)
            return ResponseModel.buildNotFoundGroupMemberError("没找到这个群成员");

        String groupId = member.getGroup().getId();

        //拿请求者去判断是不是群成员
        GroupMember selfMember = GroupFactory.getMember(self.getId(), groupId);

        //如果要修改的成员就是自己,那么允许修改,或者是管理员
        if (!Objects.equals(member.getUser().getId(), self.getId())) {
            if (selfMember == null || selfMember.getPermissionType() == GroupMember.PERMISSION_TYPE_NONE)
                return ResponseModel.buildNoPermissionError();
        }

        member = model.updateToUMember(member);
        member = GroupFactory.modifyMember(member);
        if (member == null)
            return ResponseModel.buildServiceError();

        return ResponseModel.buildOk(new GroupMemberCard(member));
    }


    @GET
    @Path("/search/members/{groupId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<GroupMemberCard>> searchGroupMembers(@PathParam("groupId") String groupId) {
        if (Strings.isNullOrEmpty(groupId)) {
            return ResponseModel.buildParameterError();
        }

        User self = getSelf();

        Group group = GroupFactory.findById(groupId);
        if (group == null) {
            ResponseModel.buildNotFoundGroupError("没找到群");
        }

        // 检查权限
        GroupMember selfMember = GroupFactory.getMember(self.getId(), groupId);
        if (selfMember == null)
            return ResponseModel.buildNoPermissionError();

        Set<GroupMember> members = GroupFactory.getMembers(group);

        if (members == null) {//没有的话那就是服务器出问题了
            ResponseModel.buildServiceError();
        }


        List<GroupMemberCard> memberCards = members.stream()
                .map(GroupMemberCard::new)
                .collect(Collectors.toList());

        return ResponseModel.buildOk(memberCards);
    }

    //用学校id拿群成员
    @GET
    @Path("/search/{schoolId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<GroupCard>> searchGroups(@PathParam("schoolId") String schoolId){
        if (Strings.isNullOrEmpty(schoolId)) {
            return ResponseModel.buildParameterError();
        }

        University university = UniversityFactory.findById(schoolId);
        if (university == null)
            return ResponseModel.buildNotFoundUniversityError("没找到这个学校");

        List<GroupCard> cards = GroupFactory.findGroupsBySchoolId(university.getId()).stream()
                .map(GroupCard::new)
                .collect(Collectors.toList());
        return ResponseModel.buildOk(cards);
    }
}
