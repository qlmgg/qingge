package com.qingge.yangsong.factory.presenter.community;
import com.qingge.yangsong.common.widget.recycler.RecyclerAdapter;
import com.qingge.yangsong.factory.model.SchoolInfoModel;
import com.qingge.yangsong.factory.model.db.Group;
import com.qingge.yangsong.factory.model.db.Post;
import com.qingge.yangsong.factory.presenter.BaseContract;

import java.util.List;

//社区的契约
public interface CommunityContract {
    interface View extends BaseContract.RecyclerView<Presenter, Post>{

        RecyclerAdapter<Group> getGroupAdapter();

    }

    interface Presenter extends BaseContract.Presenter{
        //根据自身范围拿到学校
//        void rangeSwitching(String range);
        //拿到总页数
        void getPageCount(int pageCount);
        //加载完成的群集合
       void haveLoadedGroups(List<Group> groups);
        //加载完成的帖子集合
        void haveLoadedPosts(List<Post> posts);
        //根据id拿到学校信息
        void getSchoolById(String schoolId);
        //刷新的加载 isRefresh: true代表下拉,false代表上拉加载
        void loadingNoStart(String schoolId,boolean isRefresh);
    }
}
