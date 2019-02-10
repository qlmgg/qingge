package com.qingge.yangsong.factory.presenter.Community;

import com.qingge.yangsong.factory.presenter.BaseContract;
import com.qingge.yangsong.factory.presenter.BasePresenter;

public class CommunityPresenter extends BasePresenter<CommunityContract.View>
        implements BaseContract.Presenter {


    public CommunityPresenter(CommunityContract.View view) {
        super(view);
    }
}
