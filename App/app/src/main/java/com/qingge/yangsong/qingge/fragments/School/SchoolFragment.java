package com.qingge.yangsong.qingge.fragments.school;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingge.yangsong.common.app.Application;
import com.qingge.yangsong.common.app.PresenterFragment;
import com.qingge.yangsong.common.widget.EmptyView;
import com.qingge.yangsong.common.widget.PortraitView;
import com.qingge.yangsong.common.widget.recycler.RecyclerAdapter;
import com.qingge.yangsong.factory.data.helper.SchoolHelper;
import com.qingge.yangsong.factory.model.db.Group;
import com.qingge.yangsong.factory.model.db.Post;
import com.qingge.yangsong.factory.net.RemoteService;
import com.qingge.yangsong.factory.presenter.school.SchoolContract;
import com.qingge.yangsong.factory.presenter.school.SchoolPresenter;
import com.qingge.yangsong.factory.utils.DiffUiDataCallback;
import com.qingge.yangsong.qingge.R;
import com.qingge.yangsong.qingge.activity.PersonalActivity;
import com.qingge.yangsong.qingge.fragments.main.CommunityFragment;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class SchoolFragment extends PresenterFragment<SchoolContract.Presenter>
        implements SchoolContract.View, RecyclerAdapter.AdapterListener<Post> {

    @BindView(R.id.empty)
    EmptyView mEmptyView;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    @BindView(R.id.group_recycler)
    RecyclerView mRecyclerGroup;
    @BindView(R.id.smart_refresh)
    RefreshLayout mRefreshLayout;
    @BindView(R.id.show_groups)
    ImageView mShowGroups;
    private String mSchoolId = CommunityFragment.getSchoolId();
    private RecyclerAdapter<Post> mAdapter;//这是帖子的适配器
    private RecyclerAdapter<Group> mGroupAdapter;//这是群的适配器
    public int pageCount = 2;// 总页数 每次请求返回后的会得到
    private boolean isShow;//是否显示群列表
    public SchoolFragment() {
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.loading(mSchoolId);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_university;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        isShow = false; //初始化的时候默认不显示
        //设置群列表控件隐藏
        mRecyclerGroup.setVisibility(View.GONE);
        //设箭头向下
        mShowGroups.setBackground(Objects.requireNonNull(getContext()).getResources().getDrawable(R.drawable.ic_down));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerGroup.setLayoutManager(layoutManager);
        mRecyclerGroup.setAdapter(mGroupAdapter = new RecyclerAdapter<Group>() {
            @Override
            protected int getItemViewType(int position, Group group) {
                return R.layout.cell_group_list;
            }

            @Override
            protected ViewHolder<Group> onCreateViewHolder(View view, int viewType) {
                return new GroupListViewHolder(view);
            }
        });


        //设置头部为官方样式
        mRefreshLayout.setRefreshHeader(new MaterialHeader(Objects.requireNonNull(getContext())));
        //设置底部样式
        mRefreshLayout.setRefreshFooter(new ClassicsFooter(getContext()));
        //底部高度
        mRefreshLayout.setFooterHeight(30);
        //底部拉取拉升长度(高度的几倍)
        mRefreshLayout.setFooterMaxDragRate(1);
        //下拉监听
        mRefreshLayout.setOnRefreshListener(refreshLayout -> {
            mPresenter.loadingNoStart(mSchoolId, true);
            //下拉刷新完成
            mRefreshLayout.finishRefresh();
            //刷新群列表
            SchoolHelper.findGroupList(mSchoolId,this);
        });

        mRefreshLayout.setOnLoadMoreListener(refreshLayout -> {
            //上拉加载
            mPresenter.loadingNoStart(mSchoolId, false);
            mRefreshLayout.finishLoadMore();
        });


        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.setFocusableInTouchMode(false);
        mRecycler.setAdapter(mAdapter = new RecyclerAdapter<Post>(this) {
            @Override
            protected int getItemViewType(int position, Post post) {
                return R.layout.item_school_post;
            }

            @Override
            protected ViewHolder<Post> onCreateViewHolder(View view, int viewType) {
                return new SchoolFragment.ViewHolder(view);
            }
        });

        mEmptyView.bind(mRecycler);
        setPlaceHolderView(mEmptyView);

    }

    @Override
    protected SchoolContract.Presenter initPresenter() {
        return new SchoolPresenter(this);
    }

    @Override
    public void loadGroupList(List<Group> groups) {
        Run.onUiAsync(() -> {
            List<Group> oldGroups = mGroupAdapter.getItems();
            //进行数据对比
            DiffUtil.Callback callback = new DiffUiDataCallback<>(oldGroups, groups);
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);

            // 改变数据集合并不通知界面刷新
            mGroupAdapter.getItems().clear();
            mGroupAdapter.getItems().addAll(groups);

            // 进行增量更新
            result.dispatchUpdatesTo(mGroupAdapter);
        });

    }

    @Override
    public void loadingResult(int pageCount) {
        this.pageCount = pageCount;

        // 数据成功的情况下返回数据
//        mAdapter.replace(posts);
//         如果有数据，则是OK，没有数据就显示空布局
//        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }

    @Override
    public int getPageCount() {
        return pageCount;
    }

    @Override
    public RecyclerAdapter<Post> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {
        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }


    @Override
    public void onItemClick(RecyclerAdapter.ViewHolder holder, Post post) {
        //TODO 点击帖子后的处理
        Application.showToast("待完成");
    }

    @Override
    public void onItemLongClick(RecyclerAdapter.ViewHolder holder, Post post) {
        //TODO 点击帖子后的处理
        Application.showToast("长按:待完成");
    }


    //这儿记得别写private  要有包访问权限
    class ViewHolder extends RecyclerAdapter.ViewHolder<Post> {
        @BindView(R.id.portrait_view)
        PortraitView mPortraitView;

        @BindView(R.id.tv_name)
        TextView mName;

        @BindView(R.id.content)
        TextView mContent;

        ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Post post) {
            Run.onUiAsync(() -> {
                mPortraitView.setup(Glide.with(SchoolFragment.this), post.getSenderPortrait());
                mName.setText(post.getSenderName());
                mContent.setText(post.getContent());
            });

        }

        @OnClick(R.id.portrait_view)
        void onPortraitClick() {
            // 显示信息
            PersonalActivity.show(getContext(), mData.getSenderId());
        }
    }

    //群列表的Holder
    class GroupListViewHolder extends RecyclerAdapter.ViewHolder<Group> {
        @BindView(R.id.im_portrait)
        PortraitView mGroupPortrait;
        @BindView(R.id.txt_name)
        TextView mGroupName;
        @BindView(R.id.txt_desc)
        TextView mGroupDesc;

        GroupListViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Group group) {
            mGroupPortrait.setup(Glide.with(SchoolFragment.this),group.getPicture());
            mGroupName.setText(group.getName());
            mGroupDesc.setText(group.getDesc());
        }
    }


    //点击后显示或者隐藏群列表
    @OnClick(R.id.all_show)
    public void setGroupsIsShow() {
        if (isShow) {
            mRecyclerGroup.setVisibility(View.GONE);
            mShowGroups.setBackground(Objects.requireNonNull(getContext()).getResources().getDrawable(R.drawable.ic_down));
            isShow = false;
        } else {
            mRecyclerGroup.setVisibility(View.VISIBLE);
            mShowGroups.setBackground(Objects.requireNonNull(getContext()).getResources().getDrawable(R.drawable.ic_up));
            isShow = true;
        }
    }

}
