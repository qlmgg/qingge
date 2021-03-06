package com.qingge.yangsong.qingge.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingge.yangsong.common.app.PresenterActivity;
import com.qingge.yangsong.common.widget.PortraitView;
import com.qingge.yangsong.common.widget.adapter.TextWatcherAdapter;
import com.qingge.yangsong.common.widget.recycler.RecyclerAdapter;
import com.qingge.yangsong.face.Face;
import com.qingge.yangsong.factory.model.Author;
import com.qingge.yangsong.factory.model.db.Group;
import com.qingge.yangsong.factory.model.db.Message;
import com.qingge.yangsong.factory.model.db.Session;
import com.qingge.yangsong.factory.model.db.User;
import com.qingge.yangsong.factory.presenter.Account;
import com.qingge.yangsong.factory.presenter.message.ChatContract;
import com.qingge.yangsong.factory.presenter.message.ChatGroupPresenter;
import com.qingge.yangsong.qingge.R;
import com.qingge.yangsong.qingge.fragments.panel.PanelFragment;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.genius.ui.widget.Loading;
import net.qiujuer.widget.airpanel.AirPanel;
import net.qiujuer.widget.airpanel.Util;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

import static com.qingge.yangsong.qingge.activity.ChatUserActivity.KEY_RECEIVER_ID;

public class ChatGroupActivity extends PresenterActivity<ChatContract.Presenter>
        implements ChatContract.GroupView,PanelFragment.PanelCallback {
    @BindView(R.id.tv_title_name)
    TextView mTitleName;
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.edit_content)
    EditText mContent;
    @BindView(R.id.btn_submit)
    View mSubmit;

    protected Adapter mAdapter = new Adapter();

    private String mReceiverId;
    //???????????????????????????????????????Boss??????
    private AirPanel.Boss mPanelBoss;
    private PanelFragment mPanelFragment;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_chat_group;
    }

    @Override
    public void onInit(Group group) {
        mTitleName.setText(group.getName());
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        mReceiverId = bundle.getString(KEY_RECEIVER_ID);
        return !TextUtils.isEmpty(mReceiverId);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mPanelBoss = findViewById(R.id.lay_content);
        mPanelBoss.setup(() -> {
            //?????????????????????
            Util.hideKeyboard(mContent);
        });
        initEditContent();

        mPanelFragment = (PanelFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_panel);
        mPanelFragment.setup(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(v -> finish());

    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.start();
    }

    /**
     * ??????Session????????????
     *
     * @param context ?????????
     * @param session Session
     */
    public static void show(Context context, Session session) {
        if (session == null || context == null || TextUtils.isEmpty(session.getId()))
            return;
        Intent intent = new Intent(context, ChatGroupActivity.class);
        intent.putExtra(KEY_RECEIVER_ID, session.getId());
//        intent.putExtra(KEY_RECEIVER_IS_GROUP, session.getReceiverType() == Message.RECEIVER_TYPE_GROUP);
        context.startActivity(intent);
    }


    public static void show(Context context, Group group) {
        if (group == null || context == null || TextUtils.isEmpty(group.getId()))
            return;
        Intent intent = new Intent(context, ChatGroupActivity.class);
        intent.putExtra(KEY_RECEIVER_ID, group.getId());
        context.startActivity(intent);
    }

    @Override
    protected ChatContract.Presenter initPresenter() {
        return new ChatGroupPresenter(this, mReceiverId);
    }


    @Override
    public void showGroupMember(boolean isGroupMember) {

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
            // ??????
            String content = mContent.getText().toString();
            mContent.setText("");
            mPresenter.pushText(content);
        } else {
            mPanelBoss.openPanel();
            mPanelFragment.showGallery();
        }
    }

    @OnClick(R.id.btn_face)
    void onFaceClick() {
        mPanelBoss.openPanel();
        mPanelFragment.showFace();
    }

    @OnClick(R.id.btn_record)
    void onRecordClick() {
        mPanelBoss.openPanel();
        mPanelFragment.showRecord();
    }

    // ????????????????????????
    private void initEditContent() {
        mContent.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                String content = s.toString().trim();
                boolean needSendMsg = !TextUtils.isEmpty(content);
                // ??????????????????????????????Icon
                mSubmit.setActivated(needSendMsg);
            }
        });
    }

    @Override
    public EditText getInputEditText() {
        return mContent;
    }

    // ??????????????????
    private class Adapter extends RecyclerAdapter<Message> {

        @Override
        protected int getItemViewType(int position, Message message) {
            // ??????????????????????????????????????????
            boolean isRight = Objects.equals(message.getSender().getId(), Account.getUserId());

            switch (message.getType()) {
                // ????????????
                case Message.TYPE_STR:
                    return isRight ? R.layout.cell_chat_text_right : R.layout.cell_chat_text_left;

                // ????????????
                case Message.TYPE_AUDIO:
                    return isRight ? R.layout.cell_chat_audio_right : R.layout.cell_chat_audio_left;

                // ????????????
                case Message.TYPE_PIC:
                    return isRight ? R.layout.cell_chat_pic_right : R.layout.cell_chat_pic_left;

                // ?????????????????????
                default:
                    return isRight ? R.layout.cell_chat_text_right : R.layout.cell_chat_text_left;
            }
        }

        @Override
        protected ViewHolder<Message> onCreateViewHolder(View root, int viewType) {
            switch (viewType) {
                // ?????????????????????
                case R.layout.cell_chat_text_right:
                case R.layout.cell_chat_text_left:
                    return new ChatGroupActivity.TextHolder(root);

                case R.layout.cell_chat_audio_right:
                case R.layout.cell_chat_audio_left:
                    return new ChatGroupActivity.AudioHolder(root);

                case R.layout.cell_chat_pic_right:
                case R.layout.cell_chat_pic_left:
                    return new ChatGroupActivity.PicHolder(root);

                // ?????????????????????????????????Text?????????Holder????????????
                // ?????????????????????
                default:
                    return new ChatGroupActivity.TextHolder(root);
            }
        }
    }

    // Holder?????????
    class BaseHolder extends RecyclerAdapter.ViewHolder<Message> {
        @BindView(R.id.im_portrait)
        PortraitView mPortrait;

        // ???????????????????????????????????????
        @Nullable
        @BindView(R.id.loading)
        Loading mLoading;


        BaseHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            User sender = message.getSender();
            // ??????????????????
            sender.load();
            // ????????????
            mPortrait.setup(Glide.with(ChatGroupActivity.this), sender);

            if (mLoading != null) {
                // ??????????????????????????????
                int status = message.getStatus();
                if (status == Message.STATUS_DONE) {
                    // ????????????, ??????Loading
                    mLoading.stop();
                    mLoading.setVisibility(View.GONE);
                } else if (status == Message.STATUS_CREATED) {
                    // ????????????????????????
                    mLoading.setVisibility(View.VISIBLE);
                    mLoading.setProgress(0);
                    mLoading.setForegroundColor(UiCompat.getColor(getResources(), R.color.colorAccent));
                    mLoading.start();
                } else if (status == Message.STATUS_FAILED) {
                    // ??????????????????, ??????????????????
                    mLoading.setVisibility(View.VISIBLE);
                    mLoading.stop();
                    mLoading.setProgress(1);
                    mLoading.setForegroundColor(UiCompat.getColor(getResources(), R.color.alertImportant));
                }

                // ??????????????????????????????????????????
                mPortrait.setEnabled(status == Message.STATUS_FAILED);
            }
        }

        @OnClick(R.id.im_portrait)
        void onRePushClick() {
            // ????????????

            if (mLoading != null && mPresenter.rePush(mData)) {
                // ????????????????????????????????????????????????
                // ???????????????????????????????????????????????????
                updateData(mData);
            }

        }
    }


    // ?????????Holder
    class TextHolder extends ChatGroupActivity.BaseHolder {
        @BindView(R.id.txt_content)
        TextView mContent;

        TextHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            super.onBind(message);

            Spannable spannable = new SpannableString(message.getContent());
            // ????????????
            Face.decode(mContent, spannable, (int) Ui.dipToPx(getResources(), 20));

            // ???????????????????????????
            mContent.setText(spannable);

        }
    }

    // ?????????Holder
    class AudioHolder extends ChatGroupActivity.BaseHolder {

        AudioHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            super.onBind(message);
            // TODO
        }
    }

    // ?????????Holder
    class PicHolder extends ChatGroupActivity.BaseHolder {

        PicHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            super.onBind(message);
            // TODO
        }
    }
}
