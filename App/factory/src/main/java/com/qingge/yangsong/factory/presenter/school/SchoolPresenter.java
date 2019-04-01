package com.qingge.yangsong.factory.presenter.school;

import android.support.v7.util.DiffUtil;
import android.util.Log;

import com.qingge.yangsong.common.widget.recycler.RecyclerAdapter;
import com.qingge.yangsong.factory.data.DataSource;
import com.qingge.yangsong.factory.data.helper.CommunityHelper;
import com.qingge.yangsong.factory.data.helper.SchoolHelper;
import com.qingge.yangsong.factory.data.post.PostRepository;
import com.qingge.yangsong.factory.model.SchoolInfoModel;
import com.qingge.yangsong.factory.model.card.PostCard;
import com.qingge.yangsong.factory.model.db.Post;
import com.qingge.yangsong.factory.presenter.BasePresenter;
import com.qingge.yangsong.factory.presenter.BaseSourcePresenter;
import com.qingge.yangsong.factory.presenter.community.CommunityContract;
import com.qingge.yangsong.factory.utils.DiffUiDataCallback;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.ArrayList;
import java.util.List;


public class SchoolPresenter extends BaseSourcePresenter<Post,Post,PostRepository,SchoolContract.View>
        implements SchoolContract.Presenter, DataSource.Callback<List<Post>> {

    public SchoolPresenter(SchoolContract.View view) {
        super(new PostRepository(),view);
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
        SchoolHelper.loadingPost(schoolId);
    }

    @Override
    public void onDataLoaded(final List<Post> posts) {
        final SchoolContract.View view = getView();
        if (view != null){
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
        if (view != null){
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.showError(strRes);
                }
            });
        }
    }


}
