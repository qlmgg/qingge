package com.qingge.yangsong.factory.presenter.community;

import android.support.v7.util.DiffUtil;
import com.qingge.yangsong.common.widget.recycler.RecyclerAdapter;
import com.qingge.yangsong.factory.data.DataSource;
import com.qingge.yangsong.factory.data.helper.CommunityHelper;
import com.qingge.yangsong.factory.data.helper.UserHelper;
import com.qingge.yangsong.factory.model.SchoolInfoModel;
import com.qingge.yangsong.factory.model.card.GroupCard;
import com.qingge.yangsong.factory.model.card.PostCard;
import com.qingge.yangsong.factory.model.db.Group;
import com.qingge.yangsong.factory.model.db.Post;
import com.qingge.yangsong.factory.model.db.User;
import com.qingge.yangsong.factory.presenter.BasePresenter;
import com.qingge.yangsong.factory.utils.DiffUiDataCallback;
import net.qiujuer.genius.kit.handler.Run;
import java.util.ArrayList;
import java.util.List;

public class CommunityPresenter extends BasePresenter<CommunityContract.View>
        implements CommunityContract.Presenter, DataSource.Callback<SchoolInfoModel> {

    private int currentPage = 1;//当前页  因为开始时会先请求一次,所以1开
    private int pageCount;//总页数
    private RecyclerAdapter<Post> postAdapter;
    private RecyclerAdapter<Group> groupAdapter;

    private List<Post> posts = new ArrayList<>();  //缓存帖子

    public CommunityPresenter(CommunityContract.View view) {
        super(view);
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void loadingNoStart(String schoolId, boolean isRefresh) {
        if (isRefresh) {
            //下拉始终加载第0页的数据  可以保持每次下拉都能得到最新数据

            posts.clear(); //清除缓存
            CommunityHelper.loadPostList(schoolId, currentPage = 0, this);
            currentPage++;//拉完当前页还是加1 用于上拉加载
        } else {
            currentPage = currentPage <= pageCount - 1 ? currentPage : 0;//如果当前页比总页数大  那么就回到0页
            CommunityHelper.loadPostList(schoolId, currentPage, this);
            currentPage++;
        }

    }


    @Override
    public void onDataLoaded(final SchoolInfoModel models) {
        if (models == null)
            return;
        //拿到总页数
        this.pageCount = models.getPageCount();
        //转换成帖子集合
        List<Post> posts = new ArrayList<>();
        for (PostCard postCard : models.getPostCards()) {
            posts.add(postCard.build());
        }
        for (Post post : posts) {
            insertOrUpdate(post);
        }

        //转换成群集合
        List<Group> groups = new ArrayList<>();
        for (GroupCard groupCard : models.getGroupCards()) {
            //拿到创建者
            User owner = UserHelper.search(groupCard.getOwnerId());
            groups.add(groupCard.build(owner));
        }

        final CommunityContract.View view = getView();
        //拿到帖子的适配器
        postAdapter = view.getRecyclerAdapter();
        //拿到群的适配器
        groupAdapter = view.getGroupAdapter();

        Run.onUiAsync(() -> {
            //群的增量更新
            List<Group> oldGroups = groupAdapter.getItems();
            //进行数据对比
            DiffUtil.Callback callback = new DiffUiDataCallback<>(oldGroups, groups);
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
            // 改变数据集合并不通知界面刷新
            groupAdapter.getItems().clear();
            groupAdapter.getItems().addAll(groups);
            // 进行增量更新
            result.dispatchUpdatesTo(groupAdapter);

            //帖子的增量更新
            List<Post> oldPosts = postAdapter.getItems();
            //进行数据对比
            DiffUtil.Callback callback1 = new DiffUiDataCallback<>(oldPosts, posts);
            DiffUtil.DiffResult result1 = DiffUtil.calculateDiff(callback1);
            // 改变数据集合并不通知界面刷新
            postAdapter.getItems().clear();
            postAdapter.getItems().addAll(posts);
            // 进行增量更新
            result1.dispatchUpdatesTo(postAdapter);
        });

    }


    @Override
    public void onDataNotAvailable(final int strRes) {
        final CommunityContract.View view = getView();
        if (view != null) {
            Run.onUiAsync(() -> view.showError(strRes));
        }
    }

    @Override
    public void getPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    //刷新加载完成后的群列表
    @Override
    public void haveLoadedGroups(List<Group> groups) {
        //群的增量更新
        List<Group> oldGroups = groupAdapter.getItems();
        //进行数据对比
        DiffUtil.Callback callback = new DiffUiDataCallback<>(oldGroups, groups);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        // 改变数据集合并不通知界面刷新
        groupAdapter.getItems().clear();
        groupAdapter.getItems().addAll(groups);
        // 进行增量更新
        result.dispatchUpdatesTo(groupAdapter);
    }

    //刷新加载完成后的帖子列表
    @Override
    public void haveLoadedPosts(List<Post> posts) {

        for (Post post : posts) {
            insertOrUpdate(post);
        }
        posts = this.posts;

        //帖子的增量更新
        List<Post> oldPosts = postAdapter.getItems();
        //进行数据对比
        DiffUtil.Callback callback1 = new DiffUiDataCallback<>(oldPosts, posts);
        DiffUtil.DiffResult result1 = DiffUtil.calculateDiff(callback1);
        // 改变数据集合并不通知界面刷新
        postAdapter.getItems().clear();
        postAdapter.getItems().addAll(posts);
        // 进行增量更新
        result1.dispatchUpdatesTo(postAdapter);
    }

    @Override
    public void getSchoolById(String schoolId) {
        if (schoolId == null)
            return;
        CommunityHelper.getSchoolInfo(schoolId, this);
    }

    // 插入或者更新
    private void insertOrUpdate(Post data) {
        int index = indexOf(data);
        if (index >= 0) {
            replace(index, data);
        } else {
            insert(data);
        }
    }

    // 更新操作，更新某个坐标下的数据
    private void replace(int index, Post data) {
        posts.remove(index);
        posts.add(index, data);
    }

    // 添加方法
    private void insert(Post data) {
        posts.add(data);
    }


    // 查询一个数据是否在当前的缓存数据中，如果在则返回坐标
    private int indexOf(Post newData) {
        int index = -1;
        for (Post data : posts) {
            index++;
            if (data.isSame(newData)) {
                return index;
            }
        }
        return -1;
    }

}
