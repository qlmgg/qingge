package com.qingge.post.factory;


import com.qingge.post.bean.api.group.GroupCreateModel;
import com.qingge.post.bean.api.group.GroupMemberUpdateModel;
import com.qingge.post.bean.db.*;
import com.qingge.post.utils.Hib;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 群数据处理类
 */
public class GroupFactory {
    //根据id查一个群
    public static Group findById(String groupId) {
        return Hib.query(session -> session.get(Group.class, groupId));
    }

    //根据名字查一个群
    public static Group findByName(String groupName) {
        return Hib.query(session -> (Group) session.createQuery("from Group where name=:name")
                .setParameter("name", groupName)
                .uniqueResult());
    }

    // 查询一个群, 同时该User必须为群的成员，否则返回null
    public static Group findById(User user, String groupId) {
        return Hib.query(session -> {
            GroupMember member = getMember(user.getId(), groupId);
            if (member != null)
                return member.getGroup();
            return null;
        });
    }

    //拿到群成员
    public static Set<GroupMember> getMembers(Group group) {
        return Hib.query(session -> {
            @SuppressWarnings("unchecked")
            List<GroupMember> members = session.createQuery("from GroupMember where group=:group")
                    .setParameter("group", group)
                    .list();

            return new HashSet<>(members);
        });
    }

    /**
     * 申请加入一个群，
     * 此时会创建一个加入的申请，并写入表；然后会给管理员发送消息
     * 管理员同意，其实就是调用添加成员的接口把对应的用户添加进去
     *
     * @param groupId 群Id
     * @return 申请的信息
     */
    public static Apply applyAddGroup(User self, String groupId) {

        return Hib.query(session -> {
            Apply apply = new Apply();
            apply.setTargetId(groupId);
            apply.setApplicant(self);
            apply.setDescription("我想加入群(以后这个改成model拿取)");
            apply.setType(Apply.TYPE_ADD_GROUP);
            session.save(apply);
            return apply;
        });
    }

    // 创建群
    public static Group create(User creator,University university, GroupCreateModel model, List<User> users) {
        return Hib.query(session -> {

            Group group = new Group(creator,university, model);
            session.save(group);

            GroupMember ownerMember = new GroupMember(creator, group);
            // 设置超级权限，创建者
            ownerMember.setPermissionType(GroupMember.PERMISSION_TYPE_ADMIN_SU);
            // 保存，并没有提交到数据库
            session.save(ownerMember);

            for (User user : users) {
                GroupMember member = new GroupMember(user, group);
                // 保存，并没有提交到数据库
                session.save(member);
            }

            //session.flush();
            //session.load(group, group.getId());

            return group;
        });
    }

    //通过成员id去查成员
    public static GroupMember getMember(String memberId) {
        return Hib.query(session -> session.get(GroupMember.class, memberId));
    }

    //查询一个成员 通过成员userId和groupId
    public static GroupMember getMember(String userId, String groupId) {

        return Hib.query(session -> (GroupMember)
                session.createQuery("from GroupMember where userId=:userId and groupId=:groupId")
                        .setParameter("userId", userId)
                        .setParameter("groupId", groupId)
                        .setMaxResults(1)
                        .uniqueResult()
        );
    }

    public static Set<GroupMember> addMembers(List<User> addUsers, Group group) {

        return Hib.query(session -> {
            Set<GroupMember> groupMembers = new HashSet<>();
            for (User addUser : addUsers) {
                GroupMember member = new GroupMember(addUser, group);
                session.save(member);//保存
                groupMembers.add(member);

            }
            return groupMembers;
        });
    }

    //修改成员信息
    public static GroupMember modifyMember(GroupMember member ) {
        return Hib.query(session -> {
            session.saveOrUpdate(member);
            return member;
        });
    }

    //找群信息(用学校id)
    public static List<Group> findGroupsBySchoolId(String schoolId ) {
            return Hib.query(session -> {
                @SuppressWarnings("unchecked")
                List<Group> groups = session.createQuery("from Group where universityId=:schoolId")
                        .setParameter("schoolId",schoolId)
                        .list();
                return groups;
            });
    }
}
