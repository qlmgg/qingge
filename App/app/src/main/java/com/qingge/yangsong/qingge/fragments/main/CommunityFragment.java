package com.qingge.yangsong.qingge.fragments.main;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.qingge.yangsong.factory.data.helper.UserHelper;
import com.qingge.yangsong.factory.model.card.AlbumCard;
import com.qingge.yangsong.factory.model.db.Group;
import com.qingge.yangsong.factory.model.db.Post;
import com.qingge.yangsong.factory.model.db.University;
import com.qingge.yangsong.factory.model.db.User;
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
    private boolean isShow;//?????????????????????
    public int pageCount = 2;// ????????? ?????????????????????????????????
    private RecyclerAdapter<Post> mAdapter;//????????????????????????
    private RecyclerAdapter<Group> mGroupAdapter;//?????????????????????

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_community;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        if (Account.isLogin()) {
            //??????????????????????????????????????????????????????
            //????????????????????????id
            mSchoolId = Objects.requireNonNull(Account.getSelf()).getSchoolId();
            //????????????id??????????????????  ????????????
            University university = SchoolHelper.findUniversity(mSchoolId);
            assert university != null;
            //??????????????????
            mPortrait.setup(Glide.with(CommunityFragment.this), university.getPicture());
            //??????????????????
            mRange.setText(university.getName());

            isShow = false; //?????????????????????????????????
            //???????????????????????????
            mRecyclerGroup.setVisibility(View.GONE);
            //???????????????
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

            //???????????????????????????
            mRefreshLayout.setRefreshHeader(new MaterialHeader(Objects.requireNonNull(getContext())));
            //??????????????????
            mRefreshLayout.setRefreshFooter(new ClassicsFooter(getContext()));
            //????????????
            mRefreshLayout.setFooterHeight(30);
            //????????????????????????(???????????????)
            mRefreshLayout.setFooterMaxDragRate(1);
            //????????????
            mRefreshLayout.setOnRefreshListener(refreshLayout -> {
                mPresenter.loadingNoStart(mSchoolId, true);
                //??????????????????
                mRefreshLayout.finishRefresh();
                //???????????????
                CommunityHelper.loadGroupList(mSchoolId, mPresenter);
            });

            mRefreshLayout.setOnLoadMoreListener(refreshLayout -> {
                //????????????
                mPresenter.loadingNoStart(mSchoolId, false);
                mRefreshLayout.finishLoadMore();
            });

            //?????????recycler
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

//            mPresenter.rangeSwitching("????????????");
        } else {
            //TODO ???????????????,?????????????????????????????????????????????...
            Application.showToast("????????????");
        }

    }

    @Override
    protected void initData() {
        super.initData();
        //?????????????????????????????????,??????????????????????????????
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

    @OnClick(R.id.btn_send_post)
    public void sendPost() {
        SendPostActivity.show(mActivity);
    }


    @OnClick(R.id.iv_menu)
    public void createGroup() {
        GroupCreateActivity.show(Objects.requireNonNull(getContext()));
        //TODO  ?????????????????????,????????????,?????????
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
        //TODO ????????????????????????
        Application.showToast("??????:?????????");
    }

    private int imageNumber;

    class ViewHolder extends RecyclerAdapter.ViewHolder<Post>
            implements RecyclerAdapter.AdapterListener<AlbumCard> {
        @BindView(R.id.portrait_view)
        PortraitView mPortraitView;

        @BindView(R.id.tv_name)
        TextView mName;

        @BindView(R.id.content)
        TextView mContent;
        @BindView(R.id.recycler)
        RecyclerView mRecyclerView;

        private Post post;

        ViewHolder(View itemView) {
            super(itemView);
        }

        //?????????????????????????????????, ?????????????????????,????????????????????????,??????????????????????????????.
        @Override
        protected void onBind(Post post) {
            this.post = post;


            imageNumber = post.getImages().size();
            Run.onUiAsync(() -> {
                mPortraitView.setup(Glide.with(getContext()), post.getSenderPortrait());
                mName.setText(post.getSenderName());
                mContent.setText(post.getContent());

                List<AlbumCard> postImages = post.getImages();
                int imageSize = postImages.size();

                mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), getCountColumns(imageSize) == 0 ? 1 : getCountColumns(imageSize)));
                mRecyclerView.setAdapter(new RecyclerAdapter<AlbumCard>(this, postImages) {
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

        //??????????????????????????????????????????
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
            // ????????????
            PersonalActivity.show(getContext(), mData.getSenderId());
        }

        @Override
        public void onItemClick(RecyclerAdapter.ViewHolder holder, AlbumCard albumCard) {
            PostActivity.show(getContext(), post);
        }

        @Override
        public void onItemLongClick(RecyclerAdapter.ViewHolder holder, AlbumCard albumCard) {

        }
    }

    //????????????Holder
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


    //????????????????????????????????????
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
