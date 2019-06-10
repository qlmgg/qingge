package com.qingge.yangsong.qingge.fragments.post;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.qingge.yangsong.common.app.Application;
import com.qingge.yangsong.common.widget.recycler.RecyclerAdapter;
import com.qingge.yangsong.factory.data.helper.PostHelper;
import com.qingge.yangsong.factory.model.card.AlbumCard;
import com.qingge.yangsong.factory.model.comment.CommentModel;
import com.qingge.yangsong.factory.model.db.User;
import com.qingge.yangsong.factory.presenter.Account;
import com.qingge.yangsong.qingge.R;
import com.qingge.yangsong.qingge.activity.PostActivity;

import net.qiujuer.widget.airpanel.Util;

import java.util.Objects;

/**
 * 帖子评论的界面   逻辑和界面写到一起了,这儿优先实现功能,后续优化
 **/
public class CommentFragment extends BottomSheetDialogFragment
        implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    private EditText mContent;
    private ImageView mFace, mPic, mAit;
    private Button mSubmit;

    private String receiverId;
    private String postId;


    public CommentFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle arguments = getArguments();
        if (arguments != null) {
            receiverId = arguments.getString(PostActivity.POST_SENDER_ID);
            postId = arguments.getString(PostActivity.POST_ID);
        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new BottomSheetDialog(Objects.requireNonNull(getContext()));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //设置这句话可以让输入栏把布局顶上去
        Objects.requireNonNull(getDialog().getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        View root = inflater.inflate(R.layout.fragment_comment, container, false);
        //初始化
        initWidget(root);

        return root;
    }

    //初始化控件
    private void initWidget(View root) {
        mRecyclerView = root.findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new Adapter());

        mContent = root.findViewById(R.id.edit_content);
        mFace = root.findViewById(R.id.btn_face);
        mPic = root.findViewById(R.id.btn_pic);
        mAit = root.findViewById(R.id.btn_ait);
        mSubmit = root.findViewById(R.id.btn_submit);
        mSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_submit) {
            String content = mContent.getText().toString();
            if (TextUtils.isEmpty(content)) {
                //TODO  仅图片评论
                Application.showToast("内容不能空");
                return;
            }
            //拿到自己的信息
            User self = Account.getUser();
            //提交
            //先构建model
            CommentModel model = new CommentModel.Builder()
                    .setSenderIdAndReceiverId(self.getId(), receiverId)
                    .setSenderNameAndPortrait(self.getName(), self.getPortrait())
                    .setContentAndPostId(content, postId)
                    .build();

            // 构建完成  这儿不知道model的参数是否正常, 服务端进行判断
            PostHelper.writeComment(model);
            //提交后清空
            mContent.getText().clear();
            //关闭软键盘
            Util.hideKeyboard(mContent);

        }
    }

    class Adapter extends RecyclerAdapter<AlbumCard> {

        @Override
        protected int getItemViewType(int position, AlbumCard albumCard) {
            return R.layout.cell_pic;
        }

        @Override
        protected ViewHolder<AlbumCard> onCreateViewHolder(View view, int viewType) {
            return new CommentFragment.ViewHolder(view);
        }
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<AlbumCard> {
        private ImageView mPic;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            mPic = itemView.findViewById(R.id.im_pic);
        }

        @Override
        protected void onBind(AlbumCard albumCard) {
            Glide.with(getContext())
                    .load(albumCard.getAddress())
                    .centerCrop()
                    .into(mPic);
        }
    }

}
