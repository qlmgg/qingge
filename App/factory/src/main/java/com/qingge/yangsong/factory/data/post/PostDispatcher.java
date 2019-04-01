package com.qingge.yangsong.factory.data.post;

import android.text.TextUtils;

import com.qingge.yangsong.factory.data.helper.DbHelper;
import com.qingge.yangsong.factory.data.helper.SchoolHelper;
import com.qingge.yangsong.factory.data.helper.UserHelper;
import com.qingge.yangsong.factory.data.user.UserCenter;
import com.qingge.yangsong.factory.model.card.PostCard;
import com.qingge.yangsong.factory.model.card.UserCard;
import com.qingge.yangsong.factory.model.db.Post;
import com.qingge.yangsong.factory.model.db.University;
import com.qingge.yangsong.factory.model.db.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PostDispatcher implements PostCenter {
    private static PostCenter instance;
    // 单线程池；处理卡片一个个的消息进行处理
    private final Executor executor = Executors.newSingleThreadExecutor();

    public static PostCenter instance() {
        if (instance == null) {
            synchronized (PostDispatcher.class) {
                if (instance == null)
                    instance = new PostDispatcher();
            }
        }
        return instance;
    }

    @Override
    public void dispatch(PostCard... cards) {
        if (cards == null || cards.length == 0)
            return;
        // 丢到单线程池中
        executor.execute(new PostCardHandler(cards));
    }

    /**
     * 线程调度的时候会触发run方法
     */
    private class PostCardHandler implements Runnable {
        private final PostCard[] cards;

        PostCardHandler(PostCard[] cards) {
            this.cards = cards;
        }

        @Override
        public void run() {
            // 单被线程调度的时候触发
            List<Post> posts = new ArrayList<>();

            for (PostCard card : cards) {
                // 进行过滤操作
                if (card == null || TextUtils.isEmpty(card.getId()))
                    continue;
                // 添加操作
                posts.add(card.build());
            }
            // 进行数据库存储，并分发通知, 异步的操作
            DbHelper.save(Post.class, posts.toArray(new Post[0]));
        }
    }
}