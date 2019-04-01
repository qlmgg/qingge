package com.qingge.yangsong.factory.data.school;
import com.qingge.yangsong.factory.model.card.UniversityCard;
import com.qingge.yangsong.factory.model.card.UserCard;

/**
 * 大学中心的基本定义
 */
public interface SchoolCenter {
    // 分发处理一堆用户卡片的信息，并更新到数据库
    void dispatch(UniversityCard... cards);
}
