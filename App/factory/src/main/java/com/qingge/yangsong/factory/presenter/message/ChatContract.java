package com.qingge.yangsong.factory.presenter.message;



import com.qingge.yangsong.factory.model.db.Group;
import com.qingge.yangsong.factory.model.db.Message;
import com.qingge.yangsong.factory.model.db.User;
import com.qingge.yangsong.factory.presenter.BaseContract;


/**
 * 聊天契约
 *
 */
public interface ChatContract {
    interface Presenter extends BaseContract.Presenter {
        // 发送文字
        void pushText(String content);

        // 发送语音
        void pushAudio(String path);

        // 发送图片
        void pushImages(String[] paths);

        // 重新发送一个消息，返回是否调度成功
        boolean rePush(Message message);
    }

    // 界面的基类
    interface View<InitModel> extends BaseContract.RecyclerView<Presenter, Message> {
        // 初始化的Model
        void onInit(InitModel model);
    }

    // 人聊天的界面
    interface UserView extends View<User> {

    }

    // 群聊天的界面
    interface GroupView extends View<Group> {
        //当已经是群成员的时候显示的界面
        void showGroupMember(boolean isGroupMember);
    }
}
