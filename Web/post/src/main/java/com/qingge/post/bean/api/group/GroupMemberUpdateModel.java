package com.qingge.post.bean.api.group;

import com.google.common.base.Strings;
import com.google.gson.annotations.Expose;
import com.qingge.post.bean.db.GroupMember;

public class GroupMemberUpdateModel {
    //可以修改的信息
    @Expose
    private String alias;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public static boolean check(GroupMemberUpdateModel model) {
        return !(model == null ||
                Strings.isNullOrEmpty(model.alias));
    }


    public GroupMember updateToUMember(GroupMember member) {

        if (!Strings.isNullOrEmpty(alias))
            member.setAlias(alias);

        return member;
    }
}
