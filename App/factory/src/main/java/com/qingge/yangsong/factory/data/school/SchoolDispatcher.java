package com.qingge.yangsong.factory.data.school;

import android.text.TextUtils;

import com.qingge.yangsong.factory.data.helper.DbHelper;
import com.qingge.yangsong.factory.data.user.UserCenter;
import com.qingge.yangsong.factory.model.card.UniversityCard;
import com.qingge.yangsong.factory.model.card.UserCard;
import com.qingge.yangsong.factory.model.db.University;
import com.qingge.yangsong.factory.model.db.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SchoolDispatcher implements SchoolCenter {
    private static SchoolCenter instance;
    // 单线程池；处理卡片一个个的消息进行处理
    private final Executor executor = Executors.newSingleThreadExecutor();

    public static SchoolCenter instance() {
        if (instance == null) {
            synchronized (SchoolDispatcher.class) {
                if (instance == null)
                    instance = new SchoolDispatcher();
            }
        }
        return instance;
    }

    @Override
    public void dispatch(UniversityCard... cards) {
        if (cards == null || cards.length == 0)
            return;
        // 丢到单线程池中
        executor.execute(new SchoolCardHandler(cards));
    }

    /**
     * 线程调度的时候会触发run方法
     */
    private class SchoolCardHandler implements Runnable {
        private final UniversityCard[] cards;

        SchoolCardHandler(UniversityCard[] cards) {
            this.cards = cards;
        }

        @Override
        public void run() {
            // 单被线程调度的时候触发
            List<University> universities = new ArrayList<>();
            for (UniversityCard card : cards) {
                // 进行过滤操作
                if (card == null || TextUtils.isEmpty(card.getId()))
                    continue;
                // 添加操作
                universities.add(card.build());
            }

            // 进行数据库存储，并分发通知, 异步的操作
            DbHelper.save(University.class, universities.toArray(new University[0]));
        }
    }
}