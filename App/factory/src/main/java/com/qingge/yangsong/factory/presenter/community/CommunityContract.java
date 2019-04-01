package com.qingge.yangsong.factory.presenter.community;
import com.qingge.yangsong.factory.model.SchoolInfoModel;
import com.qingge.yangsong.factory.presenter.BaseContract;

import java.util.List;

//社区的契约
public interface CommunityContract {
    interface View extends BaseContract.View<Presenter>{
        void schoolInfo(List<SchoolInfoModel> models);
    }

    interface Presenter extends BaseContract.Presenter{
        void rangeSwitching(String range);
    }
}
