package com.qingge.yangsong.factory.presenter.contact;


import com.qingge.yangsong.factory.model.card.UserCard;
import com.qingge.yangsong.factory.presenter.BaseContract;

/**
 * 关注的接口定义
 *
 */
public interface FollowContract {
    // 任务调度者
    interface Presenter extends BaseContract.Presenter {
        // 关注一个人
        void follow(String id);
    }

    interface View extends BaseContract.View<Presenter> {
        // 成功的情况下返回一个用户的信息
        void onFollowSucceed(UserCard userCard);
    }
}
