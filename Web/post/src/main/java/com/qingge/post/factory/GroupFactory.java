package com.qingge.post.factory;


import com.qingge.post.bean.db.Group;
import com.qingge.post.bean.db.GroupMember;
import com.qingge.post.bean.db.User;

import java.util.Set;

/**
 * 群数据处理类
 */
public class GroupFactory {
    public static Group findById(String groupId) {
        // TODO 查询一个群
        return null;
    }

    public static Group findById(User user, String groupId) {
        // TODO 查询一个群, 同时该User必须为群的成员，否则返回null
        return null;
    }

    public static Set<GroupMember> getMembers(Group group) {
        // TODO 查询一个群的成员
        return null;
    }
}
