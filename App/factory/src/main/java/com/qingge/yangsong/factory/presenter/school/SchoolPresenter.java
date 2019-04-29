package com.qingge.yangsong.factory.presenter.school;

import android.support.v7.util.DiffUtil;
import android.util.Log;

import com.qingge.yangsong.common.widget.recycler.RecyclerAdapter;
import com.qingge.yangsong.factory.data.DataSource;
import com.qingge.yangsong.factory.data.helper.CommunityHelper;
import com.qingge.yangsong.factory.data.helper.DbHelper;
import com.qingge.yangsong.factory.data.helper.SchoolHelper;
import com.qingge.yangsong.factory.data.post.PostRepository;
import com.qingge.yangsong.factory.model.SchoolInfoModel;
import com.qingge.yangsong.factory.model.card.PostCard;
import com.qingge.yangsong.factory.model.db.Group;
import com.qingge.yangsong.factory.model.db.Post;
import com.qingge.yangsong.factory.presenter.BasePresenter;
import com.qingge.yangsong.factory.presenter.BaseSourcePresenter;
import com.qingge.yangsong.factory.presenter.community.CommunityContract;
import com.qingge.yangsong.factory.utils.DiffUiDataCallback;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class SchoolPresenter extends BaseSourcePresenter<Post, Post, PostRepository, SchoolContract.View>
        implements SchoolContract.Presenter, DataSource.Callback<List<Post>> {
    private int pageCount = getView().getPageCount();
    private int currentPage = 1;//当前页  因为开始时会先请求一次,所以1开

    public SchoolPresenter(SchoolContract.View view) {
        super(new PostRepository(), view);
    }

    @Override
    public void start() {
        super.start();
    }


    //用学校id去加载学校里面的帖子
    @Override
    public void loading(String schoolId) {
        start();
        //开始加载
        SchoolHelper.loadingPost(schoolId, 0, getView());
        //根据学校id拿到下面的群(社团兴趣群)
        SchoolHelper.findGroupList(schoolId,getView());

        pageCount = getView().getPageCount();
    }

    @Override
    public void loadingNoStart(String schoolId, boolean isRefresh) {
        if (isRefresh) {
            //下拉始终加载第0页的数据  可以保持每次下拉都能得到最新数据
            mSource.clearDatas();//下拉把开始加载的数据进行清楚
            SchoolHelper.loadingPost(schoolId, currentPage = 0, getView());
            currentPage++;//拉完当前页还是加1 用于上拉加载
            pageCount = getView().getPageCount();
        } else {
            currentPage = currentPage <= pageCount -1 ? currentPage : 0;//如果当前页比总页数大  那么就回到0页
            SchoolHelper.loadingPost(schoolId, currentPage, getView());
            currentPage++;
            pageCount = getView().getPageCount();
        }
    }


    @Override
    public void onDataLoaded(final List<Post> posts) {
        final SchoolContract.View view = getView();
        if (view != null) {
//            Run.onUiAsync(new Action() {
//                @Override
//                public void call() {
//                    view.loadingResult(posts);
//                }
//            });
            RecyclerAdapter<Post> adapter = view.getRecyclerAdapter();
            List<Post> old = adapter.getItems();

            //进行数据对比
            DiffUtil.Callback callback = new DiffUiDataCallback<>(old, posts);
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);

            // 调用基类方法进行界面刷新
            refreshData(result, posts);
        }
    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        final SchoolContract.View view = getView();

        if (view != null) {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.showError(strRes);
                }
            });
        }
    }

}
