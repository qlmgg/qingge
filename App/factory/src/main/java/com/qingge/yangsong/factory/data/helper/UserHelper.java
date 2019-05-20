package com.qingge.yangsong.factory.data.helper;

import android.util.Log;

import com.qingge.yangsong.factory.Factory;
import com.qingge.yangsong.factory.R;
import com.qingge.yangsong.factory.data.DataSource;
import com.qingge.yangsong.factory.model.RspModel;
import com.qingge.yangsong.factory.model.card.UserCard;
import com.qingge.yangsong.factory.model.db.User;
import com.qingge.yangsong.factory.model.db.User_Table;
import com.qingge.yangsong.factory.model.db.view.UserSampleModel;
import com.qingge.yangsong.factory.net.Network;
import com.qingge.yangsong.factory.net.RemoteService;
import com.qingge.yangsong.factory.presenter.Account;
import com.qingge.yangsong.factory.presenter.contact.PersonalPresenter;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserHelper {

    // 从网络查询某用户的信息
    public static User findFromNet(String id) {
        RemoteService remoteService = Network.remote();
        try {
            Response<RspModel<UserCard>> response = remoteService.userFind(id).execute();
            UserCard card = response.body().getResult();

            if (card != null) {
                return card.build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    // 从本地查询一个用户的信息
    public static User findFromLocal(String id) {
        return SQLite.select()
                .from(User.class)
                .where(User_Table.id.eq(id))
                .querySingle();
    }

    /**
     * 搜索一个用户，优先本地缓存，
     * 没有用然后再从网络拉取
     */
    public static User search(String id) {
        User user = findFromLocal(id);
        if (user == null) {
            return findFromNet(id);
        }
        return user;
    }

    // 关注的网络请求
    public static void userFollow(String userId, final DataSource.Callback<UserCard> callback) {
        RemoteService service = Network.remote();
        Call<RspModel<UserCard>> call = service.userFollow(userId);
        call.enqueue(new Callback<RspModel<UserCard>>() {
            @Override
            public void onResponse(Call<RspModel<UserCard>> call, Response<RspModel<UserCard>> response) {
                RspModel<UserCard> rspModel = response.body();
                if (rspModel.success()) {
                    UserCard userCard = rspModel.getResult();
                    // 唤起进行保存的操作
                    Factory.getUserCenter().dispatch(userCard);
                    // 返回数据
                    callback.onDataLoaded(userCard);
                } else {
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<UserCard>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }
    //取消关注
    public static void userCancelFollow(String userId, final DataSource.Callback<UserCard> callback) {
        RemoteService service = Network.remote();
        Call<RspModel<UserCard>> call = service.userCancelFollow(userId);
        call.enqueue(new Callback<RspModel<UserCard>>() {
            @Override
            public void onResponse(Call<RspModel<UserCard>> call, Response<RspModel<UserCard>> response) {
                RspModel<UserCard> rspModel = response.body();
                if (rspModel.success()) {
                    UserCard userCard = rspModel.getResult();
                    // 唤起进行保存的操作
//                    Factory.getUserCenter().dispatch(userCard);
                    //TODO 取消关注,那么也应该清除本地数据库
                    // 返回数据
                    callback.onDataLoaded(userCard);
                } else {
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<UserCard>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }


    // 刷新联系人的操作，不需要Callback，直接存储到数据库，
    // 并通过数据库观察者进行通知界面更新，
    // 界面更新的时候进行对比，然后差异更新
    //登陆成功后就进行一次刷新
    public static void refreshContacts() {
        RemoteService service = Network.remote();
        service.userContacts()
                .enqueue(new Callback<RspModel<List<UserCard>>>() {
                    @Override
                    public void onResponse(Call<RspModel<List<UserCard>>> call, Response<RspModel<List<UserCard>>> response) {
                        RspModel<List<UserCard>> rspModel = response.body();
                        if (rspModel.success()) {
                            // 拿到集合
                            List<UserCard> cards = rspModel.getResult();
                            if (cards == null || cards.size() == 0)
                                return;

                            UserCard[] cards1 = cards.toArray(new UserCard[0]);
                            // CollectionUtil.toArray(cards, UserCard.class);

                            Factory.getUserCenter().dispatch(cards1);

                        } else {
                            Factory.decodeRspCode(rspModel, null);
                        }
                    }

                    @Override
                    public void onFailure(Call<RspModel<List<UserCard>>> call, Throwable t) {
                        // nothing
                    }
                });
    }



    //查询联系人的简单信息
    public static List<UserSampleModel> getSampleContact() {

        //"select id = ??";
        //"select User_id = ??";
        return SQLite.select(User_Table.id.withTable().as("id"),
                User_Table.name.withTable().as("name"),
                User_Table.portrait.withTable().as("portrait"))
                .from(User.class)
                .where(User_Table.isFollow.eq(true))
                .and(User_Table.id.notEq(Account.getUserId()))
                .orderBy(User_Table.name, true)
                .queryCustomList(UserSampleModel.class);

    }
}
