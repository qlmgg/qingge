package com.qingge.yangsong.factory.presenter.school;
import com.qingge.yangsong.factory.model.SchoolInfoModel;
import com.qingge.yangsong.factory.model.card.PostCard;
import com.qingge.yangsong.factory.model.db.Post;
import com.qingge.yangsong.factory.presenter.BaseContract;

import java.util.List;

//学校的契约
public interface SchoolContract {
    interface View extends BaseContract.RecyclerView<Presenter,Post>{
        void loadingResult(int pageCount);
        int getPageCount();
    }

    interface Presenter extends BaseContract.Presenter{
        void loading(String schoolId);
        void loadingNoStart(String schoolId,boolean isRefresh);
    }
}
