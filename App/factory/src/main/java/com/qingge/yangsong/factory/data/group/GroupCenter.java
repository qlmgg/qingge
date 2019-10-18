package com.qingge.yangsong.factory.data.group;


import com.qingge.yangsong.factory.model.card.GroupCard;
import com.qingge.yangsong.factory.model.card.GroupMemberCard;

/**
 * 群中心的接口定义
 *
 */
public interface GroupCenter {
    // 群卡片的处理
    void dispatch(GroupCard... cards);

    // 群成员的处理
    void dispatch(GroupMemberCard... cards);
}
