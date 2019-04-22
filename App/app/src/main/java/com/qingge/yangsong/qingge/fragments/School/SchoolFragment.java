package com.qingge.yangsong.qingge.fragments.school;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingge.yangsong.common.app.Application;
import com.qingge.yangsong.common.app.PresenterFragment;
import com.qingge.yangsong.common.widget.EmptyView;
import com.qingge.yangsong.common.widget.PortraitView;
import com.qingge.yangsong.common.widget.recycler.RecyclerAdapter;
import com.qingge.yangsong.factory.model.db.Post;
import com.qingge.yangsong.factory.presenter.school.SchoolContract;
import com.qingge.yangsong.factory.presenter.school.SchoolPresenter;
import com.qingge.yangsong.qingge.R;
import com.qingge.yangsong.qingge.activity.PersonalActivity;
import com.qingge.yangsong.qingge.fragments.main.CommunityFragment;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;

import net.qiujuer.genius.kit.handler.Run;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class SchoolFragment extends PresenterFragment<SchoolContract.Presenter>
        implements SchoolContract.View, RecyclerAdapter.AdapterListener<Post> {

    @BindView(R.id.empty)
    EmptyView mEmptyView;

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    @BindView(R.id.smart_refresh)
    RefreshLayout mRefreshLayout;

    private String mSchoolId = CommunityFragment.getSchoolId();
    private RecyclerAdapter<Post> mAdapter;
    public int pageCount = 2;// 总页数 每次请求返回后的会得到

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
            mPresenter.loadingNoStart(mSchoolId,true);
            //下拉刷新完成
            mRefreshLayout.finishRefresh();
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
        new SchoolPresenter(this);
        return null;
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


}
