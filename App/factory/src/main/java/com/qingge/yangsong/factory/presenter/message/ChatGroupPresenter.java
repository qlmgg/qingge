package com.qingge.yangsong.factory.presenter.message;

import com.qingge.yangsong.factory.data.helper.GroupHelper;
import com.qingge.yangsong.factory.data.message.MessageDataSource;
import com.qingge.yangsong.factory.data.message.MessageGroupRepository;
import com.qingge.yangsong.factory.model.db.Group;
import com.qingge.yangsong.factory.model.db.Message;


public class ChatGroupPresenter extends ChatPresenter<ChatContract.GroupView>
implements ChatContract.Presenter{

    public ChatGroupPresenter(ChatContract.GroupView view, String receiverId) {
        super(new MessageGroupRepository(receiverId), view, receiverId, Message.RECEIVER_TYPE_GROUP);
    }

    @Override
    public void start() {
        super.start();
       Group group = GroupHelper.findFromLocal(mReceiverId);

       if (group!=null){
           ChatContract.GroupView view = getView();
           view.onInit(group);

       }
    }
}
