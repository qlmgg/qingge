package com.qingge.yangsong.qingge.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.qingge.yangsong.common.app.PresenterActivity;
import com.qingge.yangsong.common.widget.PortraitView;
import com.qingge.yangsong.common.widget.adapter.TextWatcherAdapter;
import com.qingge.yangsong.common.widget.recycler.RecyclerAdapter;
import com.qingge.yangsong.factory.model.Author;
import com.qingge.yangsong.factory.model.db.Message;
import com.qingge.yangsong.factory.model.db.Session;
import com.qingge.yangsong.factory.model.db.User;
import com.qingge.yangsong.factory.presenter.Account;
import com.qingge.yangsong.factory.presenter.message.ChatContract;
import com.qingge.yangsong.factory.presenter.message.ChatUserPresenter;
import com.qingge.yangsong.qingge.R;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;
import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.genius.ui.widget.Loading;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class ChatUserActivity extends PresenterActivity<ChatContract.Presenter>
        implements Toolbar.OnMenuItemClickListener,ChatContract.UserView {
    // 接收者Id，可以是群，也可以是人的Id
    public static final String KEY_RECEIVER_ID = "KEY_RECEIVER_ID";
    // 是否是群
    private static final String KEY_RECEIVER_IS_GROUP = "KEY_RECEIVER_IS_GROUP";
    private String mReceiverId;

    protected Adapter mAdapter;
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_title_name)
    TextView mTitleName;
    @BindView(R.id.edit_content)
    EditText mContent;
    @BindView(R.id.btn_submit)
    View mSubmit;
    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_chat_user;
    }


    /**
     * 通过Session发起聊天
     *
     * @param context 上下文
     * @param session Session
     */
    public static void show(Context context, Session session) {
        if (session == null || context == null || TextUtils.isEmpty(session.getId()))
            return;
        Intent intent = new Intent(context, ChatUserActivity.class);
        intent.putExtra(KEY_RECEIVER_ID, session.getId());
        intent.putExtra(KEY_RECEIVER_IS_GROUP, session.getReceiverType() == Message.RECEIVER_TYPE_GROUP);
        context.startActivity(intent);
    }


    public static void show(Context context, Author author) {
        if (author == null || context == null || TextUtils.isEmpty(author.getId()))
            return;
        Intent intent = new Intent(context, ChatUserActivity.class);
        intent.putExtra(KEY_RECEIVER_ID, author.getId());
        context.startActivity(intent);
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        mReceiverId = bundle.getString(KEY_RECEIVER_ID);
        return !TextUtils.isEmpty(mReceiverId);
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        initEditContent();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new Adapter();
        mRecyclerView.setAdapter(mAdapter);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(v -> {
            finish();
        });
        mToolbar.inflateMenu(R.menu.chat_user);
        mToolbar.setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        Toast.makeText(ChatUserActivity.this,"个人详细信息界面:" + menuItem.getItemId(),Toast.LENGTH_SHORT).show();
        return false;
    }


    @Override
    protected void initData() {
        super.initData();
        mPresenter.start();
    }

    @Override
    protected ChatContract.Presenter initPresenter() {
        return new ChatUserPresenter(this, mReceiverId);
    }
    //用户信息初始化
    @Override
    public void onInit(User user) {
        //TODO bug
        Run.onUiAsync(() -> mTitleName.setText(user.getName()));

    }


    @Override
    public RecyclerAdapter<Message> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {

    }

    @OnClick(R.id.btn_submit)
    void onSubmitClick() {
        if (mSubmit.isActivated()) {
            // 发送
            String content = mContent.getText().toString();
            mContent.setText("");
            mPresenter.pushText(content);
        } else {
//            onMoreClick();
        }
    }

    // 初始化输入框监听
    private void initEditContent() {
        mContent.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                String content = s.toString().trim();
                boolean needSendMsg = !TextUtils.isEmpty(content);
                // 设置状态，改变对应的Icon
                mSubmit.setActivated(needSendMsg);
            }
        });
    }

    // 内容的适配器
    private class Adapter extends RecyclerAdapter<Message> {

        @Override
        protected int getItemViewType(int position, Message message) {
            // 我发送的在右边，收到的在左边
            boolean isRight = Objects.equals(message.getSender().getId(), Account.getUserId());

            switch (message.getType()) {
                // 文字内容
                case Message.TYPE_STR:
                    return isRight ? R.layout.cell_chat_text_right : R.layout.cell_chat_text_left;

                // 语音内容
                case Message.TYPE_AUDIO:
                    return isRight ? R.layout.cell_chat_audio_right : R.layout.cell_chat_audio_left;

                // 图片内容
                case Message.TYPE_PIC:
                    return isRight ? R.layout.cell_chat_pic_right : R.layout.cell_chat_pic_left;

                // 其他内容：文件
                default:
                    return isRight ? R.layout.cell_chat_text_right : R.layout.cell_chat_text_left;
            }
        }

        @Override
        protected ViewHolder<Message> onCreateViewHolder(View root, int viewType) {
            switch (viewType) {
                // 左右都是同一个
                case R.layout.cell_chat_text_right:
                case R.layout.cell_chat_text_left:
                    return new TextHolder(root);

                case R.layout.cell_chat_audio_right:
                case R.layout.cell_chat_audio_left:
                    return new AudioHolder(root);

                case R.layout.cell_chat_pic_right:
                case R.layout.cell_chat_pic_left:
                    return new PicHolder(root);

                // 默认情况下，返回的就是Text类型的Holder进行处理
                // 文件的一些实现
                default:
                    return new TextHolder(root);
            }
        }
    }

    // Holder的基类
    class BaseHolder extends RecyclerAdapter.ViewHolder<Message> {
        @BindView(R.id.im_portrait)
        PortraitView mPortrait;

        // 允许为空，左边没有，右边有
        @Nullable
        @BindView(R.id.loading)
        Loading mLoading;


        public BaseHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            User sender = message.getSender();
            // 进行数据加载
            sender.load();
            // 头像加载
            mPortrait.setup(Glide.with(ChatUserActivity.this), sender);

            if (mLoading != null) {
                // 当前布局应该是在右边
                int status = message.getStatus();
                if (status == Message.STATUS_DONE) {
                    // 正常状态, 隐藏Loading
                    mLoading.stop();
                    mLoading.setVisibility(View.GONE);
                } else if (status == Message.STATUS_CREATED) {
                    // 正在发送中的状态
                    mLoading.setVisibility(View.VISIBLE);
                    mLoading.setProgress(0);
                    mLoading.setForegroundColor(UiCompat.getColor(getResources(), R.color.colorAccent));
                    mLoading.start();
                } else if (status == Message.STATUS_FAILED) {
                    // 发送失败状态, 允许重新发送
                    mLoading.setVisibility(View.VISIBLE);
                    mLoading.stop();
                    mLoading.setProgress(1);
                    mLoading.setForegroundColor(UiCompat.getColor(getResources(), R.color.alertImportant));
                }

                // 当状态是错误状态时才允许点击
                mPortrait.setEnabled(status == Message.STATUS_FAILED);
            }
        }

        @OnClick(R.id.im_portrait)
        void onRePushClick() {
            // 重新发送

            if (mLoading != null && mPresenter.rePush(mData)) {
                // 必须是右边的才有可能需要重新发送
                // 状态改变需要重新刷新界面当前的信息
                updateData(mData);
            }

        }
    }

    // 文字的Holder
    class TextHolder extends BaseHolder {
        @BindView(R.id.txt_content)
        TextView mContent;

        public TextHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            super.onBind(message);

            // 把内容设置到布局上
            mContent.setText(message.getContent());

        }
    }

    // 语音的Holder
    class AudioHolder extends BaseHolder {

        public AudioHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            super.onBind(message);
            // TODO
        }
    }

    // 图片的Holder
    class PicHolder extends BaseHolder {

        public PicHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            super.onBind(message);
            // TODO
        }
    }
}
