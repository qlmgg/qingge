package com.qingge.yangsong.factory.presenter.post;

import com.qingge.yangsong.factory.model.card.CommentCard;

import com.qingge.yangsong.factory.presenter.BaseContract;

public interface PostContract {
    interface View extends BaseContract.RecyclerView<Presenter, CommentCard>{

    }
    interface Presenter extends BaseContract.Presenter{

    }
}
