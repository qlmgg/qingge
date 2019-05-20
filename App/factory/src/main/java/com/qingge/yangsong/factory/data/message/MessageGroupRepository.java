package com.qingge.yangsong.factory.data.message;

import com.qingge.yangsong.factory.data.BaseDbRepository;
import com.qingge.yangsong.factory.model.db.Message;
import com.qingge.yangsong.factory.model.db.Message_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

public class MessageGroupRepository extends BaseDbRepository<Message>
        implements MessageDataSource {
    //群id
    private String receiverId;

    public MessageGroupRepository(String receiverId) {
        super();
        this.receiverId = receiverId;
    }

    @Override
    public void load(SucceedCallback<List<Message>> callback) {
        super.load(callback);
        //进行本地数据的查询
        SQLite.select()
                .from(Message.class)
                .where(Message_Table.group_id.eq(receiverId))
                .orderBy(Message_Table.createAt,false)
                .limit(50)
                .async()
                .queryListResultCallback(this)
                .execute();
    }

    @Override
    protected boolean isRequired(Message message) {
        // 如果消息的Group不为空，则一定是发送到一个群的
        // 如果群Id等于我们需要的，那就是通过
        return message.getGroup() != null
                && receiverId.equalsIgnoreCase(message.getGroup().getId());
    }

//    @Override
//    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Message> tResult) {
//        // 反转返回的集合
//        Collections.reverse(tResult);
//        // 然后再调度
//        super.onListQueryResult(transaction, tResult);
//    }
}
