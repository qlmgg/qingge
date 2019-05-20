package com.qingge.yangsong.qingge.fragments.message;


import com.qingge.yangsong.factory.model.db.Group;
import com.qingge.yangsong.factory.presenter.message.ChatContract;
import com.qingge.yangsong.qingge.R;
//TODO  等待删除
/**
 * 群聊天界面实现
 */
public class ChatGroupFragment extends ChatFragment<Group>
        implements ChatContract.GroupView {


    public ChatGroupFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_chat_group;
    }



    @Override
    protected ChatContract.Presenter initPresenter() {
        return null;
    }
    @Override
    public void onInit(Group group) {

    }

    @Override
    public void showGroupMember(boolean isGroupMember) {

    }
}
