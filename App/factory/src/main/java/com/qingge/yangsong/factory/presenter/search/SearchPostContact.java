package com.qingge.yangsong.factory.presenter.search;

import com.qingge.yangsong.factory.model.db.Post;
import com.qingge.yangsong.factory.presenter.BaseContract;

/**
 * Created by White paper on 2019/10/18
 * Describe :
 */
public interface SearchPostContact {
    interface View extends BaseContract.RecyclerView<Presenter, Post>{

    }

    interface Presenter extends BaseContract.Presenter{
        void search(String name);
    }
}
