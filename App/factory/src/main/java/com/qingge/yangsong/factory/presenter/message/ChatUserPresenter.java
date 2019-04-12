package com.qingge.yangsong.factory.presenter.message;


import com.qingge.yangsong.factory.data.helper.UserHelper;
import com.qingge.yangsong.factory.data.message.MessageRepository;
import com.qingge.yangsong.factory.model.db.Message;
import com.qingge.yangsong.factory.model.db.User;

/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public class ChatUserPresenter extends ChatPresenter<ChatContract.UserView>
        implements ChatContract.Presenter {

    public ChatUserPresenter(ChatContract.UserView view, String receiverId) {
        // 数据源，View，接收者，接收者的类型
        super(new MessageRepository(receiverId), view, receiverId, Message.RECEIVER_TYPE_NONE);

    }

    @Override
    public void start() {
        super.start();

        // 从本地拿这个人的信息
        User receiver = UserHelper.search(mReceiverId);
        getView().onInit(receiver);
    }
}
