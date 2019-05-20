package com.qingge.yangsong.qingge.fragments.message;


import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.qingge.yangsong.factory.model.db.User;
import com.qingge.yangsong.factory.presenter.message.ChatContract;
import com.qingge.yangsong.factory.presenter.message.ChatUserPresenter;
import com.qingge.yangsong.qingge.R;

//TODO  等待删除
/**
 * 用户聊天界面    // 这个界面被ChatUserActivity代替了   以后项目完成后再删除
 */
public class ChatUserFragment extends ChatFragment<User>
        implements ChatContract.UserView, Toolbar.OnMenuItemClickListener {

//    private MenuItem mUserInfoMenuItem;

    public ChatUserFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_chat_user;
    }


    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        Toast.makeText(getContext(),"当前界面是fragment:" ,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();

        Toolbar toolbar = mToolbar;
        toolbar.inflateMenu(R.menu.chat_user);
        toolbar.setOnMenuItemClickListener(this);

    }


    @Override
    protected ChatContract.Presenter initPresenter() {
        // 初始化Presenter
        return new ChatUserPresenter(this, mReceiverId);
    }

    @Override
    public void onInit(User user) {
        // 对和你聊天的朋友的信息进行初始化操作
//        mCollapsingLayout.setTitle(user.getName());
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        Toast.makeText(getActivity(),"个人详细信息界面:" + menuItem.getItemId(),Toast.LENGTH_SHORT).show();
//        if (menuItem.getItemId() == R.id.action_person) {
//            PersonalActivity.show(getContext(), mReceiverId);
//        }
        return false;
    }

}
