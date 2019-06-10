package com.qingge.yangsong.qingge.fragments.main;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingge.yangsong.common.app.Application;
import com.qingge.yangsong.common.app.PresenterFragment;
import com.qingge.yangsong.common.widget.PortraitView;
import com.qingge.yangsong.common.widget.recycler.RecyclerAdapter;
import com.qingge.yangsong.factory.data.helper.CommunityHelper;
import com.qingge.yangsong.factory.data.helper.SchoolHelper;
import com.qingge.yangsong.factory.model.SchoolInfoModel;
import com.qingge.yangsong.factory.model.card.AlbumCard;
import com.qingge.yangsong.factory.model.db.Group;
import com.qingge.yangsong.factory.model.db.Post;
import com.qingge.yangsong.factory.model.db.University;
import com.qingge.yangsong.factory.presenter.Account;
import com.qingge.yangsong.factory.presenter.community.CommunityContract;
import com.qingge.yangsong.factory.presenter.community.CommunityPresenter;

import com.qingge.yangsong.factory.utils.DiffUiDataCallback;
import com.qingge.yangsong.qingge.R;
import com.qingge.yangsong.qingge.activity.ChatGroupActivity;
import com.qingge.yangsong.qingge.activity.GroupCreateActivity;
import com.qingge.yangsong.qingge.activity.LaunchActivity;
import com.qingge.yangsong.qingge.activity.PersonalActivity;
import com.qingge.yangsong.qingge.activity.PostActivity;
import com.qingge.yangsong.qingge.activity.SendPostActivity;
import com.qingge.yangsong.qingge.activity.TestActivity;

import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;


import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.ui.widget.FloatActionButton;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class CommunityFragment extends PresenterFragment<CommunityContract.Presenter>
        implements CommunityContract.View, RecyclerAdapter.AdapterListener<Post> {
    @BindView(R.id.tv_range)
    TextView mRange;
    @BindView(R.id.iv_portrait)
    PortraitView mPortrait;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    @BindView(R.id.group_recycler)
    RecyclerView mRecyclerGroup;
    @BindView(R.id.smart_refresh)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.show_groups)
    ImageView mShowGroups;

    private static String mSchoolId;
    private boolean isShow;//是否显示群列表
    public int pageCount = 2;// 总页数 每次请求返回后的会得到
    private RecyclerAdapter<Post> mAdapter;//这是帖子的适配器
    private RecyclerAdapter<Group> mGroupAdapter;//这是群的适配器

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_community;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        if (Account.isLogin()) {
            //登陆情况下就进行页面加载和头像的显示
            //得到登陆人的学校id
            mSchoolId = Objects.requireNonNull(Account.getSelf()).getSchoolId();
            //根据学校id查询查询学校  优先本地
            University university = SchoolHelper.findUniversity(mSchoolId);
            assert university != null;
            //设置学校头像
            mPortrait.setup(Glide.with(CommunityFragment.this), university.getPicture());
            //设置学校名字
            mRange.setText(university.getName());

            isShow = false; //初始化的时候默认不显示
            //设置群列表控件隐藏
            mRecyclerGroup.setVisibility(View.GONE);
            //设箭头向下
            mShowGroups.setBackground(Objects.requireNonNull(getContext()).getResources().getDrawable(R.drawable.ic_down));

            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            mRecyclerGroup.setLayoutManager(layoutManager);

            mRecyclerGroup.setAdapter(mGroupAdapter = new RecyclerAdapter<Group>(new RecyclerAdapter.AdapterListener<Group>() {
                @Override
                public void onItemClick(RecyclerAdapter.ViewHolder holder, Group group) {
                    ChatGroupActivity.show(getContext(), group);
                }

                @Override
                public void onItemLongClick(RecyclerAdapter.ViewHolder holder, Group group) {

                }
            }) {
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
                CommunityHelper.loadGroupList(mSchoolId, mPresenter);
            });

            mRefreshLayout.setOnLoadMoreListener(refreshLayout -> {
                //上拉加载
                mPresenter.loadingNoStart(mSchoolId, false);
                mRefreshLayout.finishLoadMore();
            });

            //帖子的recycler
            mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecycler.setFocusableInTouchMode(false);
            mRecycler.setAdapter(mAdapter = new RecyclerAdapter<Post>(this) {
                @Override
                protected int getItemViewType(int position, Post post) {
                    return R.layout.item_school_post;
                }

                @Override
                protected ViewHolder<Post> onCreateViewHolder(View view, int viewType) {
                    return new CommunityFragment.ViewHolder(view);
                }
            });

//            mPresenter.rangeSwitching("我的大学");
        } else {
            //TODO 如果没登陆,就加载一个请登陆的提示页面或者...
            Application.showToast("请先登陆");
        }

    }

    @Override
    protected void initData() {
        super.initData();
        //加载这个页面就执行一次,后面刷新不会执行这个
        mPresenter.getSchoolById(Account.getUser().getSchoolId());
    }

    @Override
    protected CommunityContract.Presenter initPresenter() {
        return new CommunityPresenter(this);
    }

    @Override
    public RecyclerAdapter<Group> getGroupAdapter() {
        return mGroupAdapter;
    }

    public static String getSchoolId() {
        return mSchoolId;
    }

//    @OnClick(R.id.write_post)
//    public void writePost() {
//        SendPostActivity.show(mActivity);
//    }

    /**
     * 弹出框一个  可以创建群 ..
     */
    @OnClick(R.id.iv_menu)
    public void createGroup() {
        GroupCreateActivity.show(Objects.requireNonNull(getContext()));
        //TODO  退出登陆的操作,已经实现,待应用
//        Account.signOut(getContext(),new Intent(getContext(), LaunchActivity.class));
    }

    @Override
    public RecyclerAdapter<Post> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {

    }

    @Override
    public void onItemClick(RecyclerAdapter.ViewHolder holder, Post post) {
        PostActivity.show(getContext(), post);
    }

    @Override
    public void onItemLongClick(RecyclerAdapter.ViewHolder holder, Post post) {
        //TODO 点击帖子后的处理
        Application.showToast("长按:待完成");
    }

    private int imageNumber;

    //这儿记得别写private  要有包访问权限
    class ViewHolder extends RecyclerAdapter.ViewHolder<Post> {
        @BindView(R.id.portrait_view)
        PortraitView mPortraitView;

        @BindView(R.id.tv_name)
        TextView mName;

        @BindView(R.id.content)
        TextView mContent;

        @BindView(R.id.recycler)
        RecyclerView mRecyclerView;

        ViewHolder(View itemView) {
            super(itemView);

        }

        //每个数据都会过这儿绑定, 存一个照片集合,这儿有个表格布局,根据集合大小显示列数.
        @Override
        protected void onBind(Post post) {
            imageNumber = post.getImages().size();

            Run.onUiAsync(() -> {
                mPortraitView.setup(Glide.with(getContext()), post.getSenderPortrait());
                mName.setText(post.getSenderName());
                mContent.setText(post.getContent());

                List<AlbumCard> postImages = post.getImages();
                int imageSize = postImages.size();

                mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), getCountColumns(imageSize) == 0 ? 1 : getCountColumns(imageSize)));
                mRecyclerView.setAdapter(new RecyclerAdapter<AlbumCard>(null, postImages) {
                    @Override
                    protected int getItemViewType(int position, AlbumCard albumCard) {
                        return R.layout.cell_test;
                    }

                    @Override
                    protected ViewHolder<AlbumCard> onCreateViewHolder(View view, int viewType) {
                        return new CommunityFragment.PostImagesViewHolder(view);
                    }
                });

            });

        }
        //根据图片数量去判断作几列显示
        private int getCountColumns(int size) {
            if (size == 1) {
                return 1;
            } else if (size == 2 || size == 4) {
                return 2;
            } else if (size >= 3) {
                return 3;
            }

            return 0;
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

            mGroupPortrait.setup(Glide.with(getContext()), group.getPicture());
            mGroupName.setText(group.getName());
            mGroupDesc.setText(group.getDesc());
        }
    }


    class PostImagesViewHolder extends RecyclerAdapter.ViewHolder<AlbumCard> {
        @BindView(R.id.image)
        ImageView imageView;

        PostImagesViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(AlbumCard albumCard) {

            if (imageNumber == 1) {
                ViewGroup.LayoutParams lp = imageView.getLayoutParams();
                lp.height = 975;
                imageView.setLayoutParams(lp);
            } else if (imageNumber == 2 || imageNumber == 4) {
                ViewGroup.LayoutParams lp = imageView.getLayoutParams();
                lp.height = 487;
                imageView.setLayoutParams(lp);
            } else if (imageNumber == 3 || imageNumber >= 5) {
                ViewGroup.LayoutParams lp = imageView.getLayoutParams();
                lp.height = 325;
                imageView.setLayoutParams(lp);
            }


            Glide.with(getContext())
                    .load(albumCard.getAddress())
                    .centerCrop()
                    .into(imageView);
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
