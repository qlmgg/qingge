package com.qingge.yangsong.qingge;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.qingge.yangsong.common.Common;
import com.qingge.yangsong.common.app.Application;
import com.qingge.yangsong.common.widget.RoundImageView;
import com.qingge.yangsong.common.widget.recycler.RecyclerAdapter;
import com.qingge.yangsong.factory.model.RspModel;
import com.qingge.yangsong.factory.model.card.GroupCard;
import com.qingge.yangsong.factory.model.db.Group;
import com.qingge.yangsong.factory.model.db.Post;
import com.qingge.yangsong.factory.net.Network;
import com.qingge.yangsong.factory.net.RemoteService;
import com.qingge.yangsong.factory.presenter.Account;
import com.qingge.yangsong.factory.presenter.BaseContract;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupDialog extends DialogFragment
        implements RecyclerAdapter.AdapterListener<GroupCard> {

    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    private List<GroupCard> groupCards = new ArrayList<>();
    private RecyclerAdapter<GroupCard> adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Theme_AppCompat_DayNight_DialogWhenLarge);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog, container, false);
        ButterKnife.bind(this, view);
        Dialog dialog = getDialog();
        dialog.setCanceledOnTouchOutside(true);

        Window window = getDialog().getWindow();
        assert window != null;
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.y = 192;
        lp.gravity = Gravity.TOP;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;

        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        initWidget();
        return view;
    }

    //初始化控件
    private void initWidget() {

        for (int i = 0; i < 9; i++) {
            groupCards.add(new GroupCard());
        }

//        RemoteService service = Network.remote();
//        Call<RspModel<List<GroupCard>>> call = service.searchGroups(Account.getUser().getSchoolId());
//        call.enqueue(new Callback<RspModel<List<GroupCard>>>() {
//            @Override
//            public void onResponse(Call<RspModel<List<GroupCard>>> call, Response<RspModel<List<GroupCard>>> response) {
//                RspModel<List<GroupCard>> rspModel = response.body();
//                if (rspModel.success()) {
//                    groupCards = rspModel.getResult();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<RspModel<List<GroupCard>>> call, Throwable t) {
//                Toast.makeText(getContext(), "网络错误", Toast.LENGTH_SHORT).show();
//            }
//        });


        mRecycler.setLayoutManager(new GridLayoutManager(getContext(), 4));
        mRecycler.setAdapter(adapter = new RecyclerAdapter<GroupCard>(this, groupCards) {
            @Override
            protected int getItemViewType(int position, GroupCard card) {
                return R.layout.group_item_list;
            }

            @Override
            protected ViewHolder<GroupCard> onCreateViewHolder(View view, int viewType) {
                return new GroupDialog.ViewHolder(view);
            }
        });

    }

    @Override
    public void onItemClick(RecyclerAdapter.ViewHolder holder, GroupCard card) {
        Application.showToast("待完成");
    }

    @Override
    public void onItemLongClick(RecyclerAdapter.ViewHolder holder, GroupCard card) {
        Application.showToast("长按待完成");
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<GroupCard> {
        @BindView(R.id.group_name)
        TextView mGroupName;
        @BindView(R.id.group_pic)
        RoundImageView mGroupPic;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(GroupCard card) {
            mGroupName.setText(card.getName());
            Glide.with(getContext())
                    .load(card.getPicture())
                    .placeholder(R.drawable.default_portrait)
                    .centerCrop()
                    .dontAnimate()
                    .into(new ViewTarget<View, GlideDrawable>(mGroupPic) {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            this.view.setBackground(resource.getCurrent());
                        }
                    });
        }
    }

}
