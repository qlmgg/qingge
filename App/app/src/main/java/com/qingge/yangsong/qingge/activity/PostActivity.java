package com.qingge.yangsong.qingge.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingge.yangsong.common.app.Activity;

import com.qingge.yangsong.common.app.Application;
import com.qingge.yangsong.common.app.PresenterToolbarActivity;
import com.qingge.yangsong.common.widget.EmptyView;
import com.qingge.yangsong.common.widget.PortraitView;
import com.qingge.yangsong.common.widget.recycler.RecyclerAdapter;
import com.qingge.yangsong.factory.model.card.CommentCard;
import com.qingge.yangsong.factory.model.db.Post;
import com.qingge.yangsong.factory.presenter.post.PostContract;
import com.qingge.yangsong.factory.presenter.post.PostPresenter;
import com.qingge.yangsong.qingge.App;
import com.qingge.yangsong.qingge.R;
import com.qingge.yangsong.qingge.fragments.post.CommentFragment;
import com.qingge.yangsong.utils.DateTimeUtil;

import net.qiujuer.genius.kit.handler.Run;

import butterknife.BindView;
import butterknife.OnClick;

public class PostActivity extends PresenterToolbarActivity<PostContract.Presenter>
        implements PostContract.View {

    public static final String POST_ID = "POST_ID";
    public static final String POST_CONTENT = "POST_CONTENT";
    private static final String POST_SENDER_NAME = "POST_SENDER_NAME";
    private static final String POST_PORTRAIT = "POST_PORTRAIT";
    public static final String POST_SENDER_ID = "POST_SENDER_ID";
    @BindView(R.id.frame_layout)
    FrameLayout layout;
    @BindView(R.id.portrait)
    PortraitView mPortrait;
    @BindView(R.id.user_name)
    TextView mPostUserName;
    @BindView(R.id.txt_time)
    TextView mTime;
    @BindView(R.id.content)
    TextView mPostContent;
    @BindView(R.id.txt_fabulous)
    TextView mFabulousNumber;
    @BindView(R.id.txt_comment)
    TextView mCommentNumber;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    @BindView(R.id.empty)
    EmptyView mEmptyView;
    private Adapter mAdapter;

    private String name;//发送者名字
    private String content;//内容
    private String portrait;//帖子的发送者头像
    private String postId;//帖子的id
    private String postSenderId;//帖子的发送者id

    public static void show(Context context, Post post) {
        Intent intent = new Intent(context, PostActivity.class);
        intent.putExtra(POST_ID, post.getId());
        intent.putExtra(POST_SENDER_ID, post.getSenderId());
        intent.putExtra(POST_CONTENT, post.getContent());
        intent.putExtra(POST_SENDER_NAME, post.getSenderName());
        intent.putExtra(POST_PORTRAIT, post.getSenderPortrait());
        context.startActivity(intent);
    }

    @Override
    protected boolean initArgs(Bundle bundle) {

        name = bundle.getString(POST_SENDER_NAME, "");
        postId = bundle.getString(POST_ID, "");
        portrait = bundle.getString(POST_PORTRAIT, "");
        content = bundle.getString(POST_CONTENT, "");
        postSenderId = bundle.getString(POST_SENDER_ID, "");

        return !TextUtils.isEmpty(name)
                && !TextUtils.isEmpty(postId)
                && !TextUtils.isEmpty(portrait)
                && !TextUtils.isEmpty(postSenderId)
                && !TextUtils.isEmpty(content);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_post;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        mPortrait.setup(Glide.with(this), portrait);
        mPostContent.setText(content);
        mPostUserName.setText(name);

        setTitle("");
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setAdapter(mAdapter = new Adapter());

        mEmptyView.bind(layout);
        setPlaceHolderView(mEmptyView);
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.start();
    }

    @Override
    protected PostContract.Presenter initPresenter() {
        return new PostPresenter(this, postId);
    }

    @Override
    public RecyclerAdapter<CommentCard> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {
        hideLoading();
    }

    @Override
    public void showError(int str) {
        mEmptyView.triggerNetError();
    }

    @Override
    protected void initTitleNeedBack() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_back_grey));
        }
        super.initTitleNeedBack();
    }

    class Adapter extends RecyclerAdapter<CommentCard> {

        @Override
        protected int getItemViewType(int position, CommentCard post) {
            return R.layout.cell_post_comment;
        }

        @Override
        protected ViewHolder<CommentCard> onCreateViewHolder(View view, int viewType) {
            return new PostActivity.ViewHolder(view);
        }
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<CommentCard> {
        @BindView(R.id.portrait)
        PortraitView mPortrait;
        @BindView(R.id.txt_comment_name)
        TextView mPostUserName;
        @BindView(R.id.txt_comment_time)
        TextView mTime;
        @BindView(R.id.txt_comment_content)
        TextView mPostContent;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(CommentCard card) {
            Run.onUiSync(() -> {
                mPortrait.setup(Glide.with(PostActivity.this), card.getCommenterPortrait());
                mPostUserName.setText(card.getSenderName());
                mTime.setText(DateTimeUtil.getSampleDate(card.getCreateAt()));
                mPostContent.setText(card.getContent());
            });
        }
    }

    @OnClick(R.id.write_comment)
    void onWriteComment() {
        CommentFragment fragment = new CommentFragment();
        Bundle bundle = new Bundle();

        bundle.putString(POST_SENDER_ID, postSenderId);
        bundle.putString(POST_CONTENT, content);
        bundle.putString(POST_ID, postId);

        fragment.setArguments(bundle);
        //开始
        fragment.show(getSupportFragmentManager(), CommentFragment.class.getName());

    }
}

