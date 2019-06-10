package com.qingge.yangsong.factory.data.post;

import com.qingge.yangsong.factory.data.BaseDbRepository;
import com.qingge.yangsong.factory.data.user.ContactDataSource;
import com.qingge.yangsong.factory.model.card.PostCard;
import com.qingge.yangsong.factory.model.db.Post;
import com.qingge.yangsong.factory.model.db.Post_Table;
import com.qingge.yangsong.factory.model.db.User;
import com.qingge.yangsong.factory.model.db.User_Table;
import com.qingge.yangsong.factory.presenter.Account;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;
import java.util.Objects;

/**
 * 学校里的帖子仓库
 *
 */
public class PostRepository extends BaseDbRepository<Post> implements PostDataSource {

    public  PostRepository() {
    }

    public void clearDatas(){
        getDataList().clear();
    }

    @Override
    public void load(SucceedCallback<List<Post>> callback) {
        super.load(callback);

        // 加载本地数据库数据
//        SQLite.select()
//                .from(User.class)
//                .where(Post_Table.isFollow.eq(true))
//                .and(User_Table.id.notEq(Account.getUserId()))
//                .orderBy(User_Table.name, true)
//                .limit(100)
//                .async()
//                .queryListResultCallback(this)
//                .execute();
    }

    //过滤器
    @Override
    protected boolean isRequired(Post post) {
        return true;
    }




}
