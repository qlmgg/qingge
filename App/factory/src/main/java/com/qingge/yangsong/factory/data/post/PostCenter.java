package com.qingge.yangsong.factory.data.post;
import com.qingge.yangsong.factory.model.card.PostCard;

/**
 * 用户中心的基本定义
 */
public interface PostCenter {
    // 分发处理一堆用户卡片的信息，并更新到数据库
    void dispatch(PostCard... cards);
}
