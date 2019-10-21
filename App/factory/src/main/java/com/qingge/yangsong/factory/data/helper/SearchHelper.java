package com.qingge.yangsong.factory.data.helper;

import com.qingge.yangsong.factory.Factory;
import com.qingge.yangsong.factory.R;
import com.qingge.yangsong.factory.data.DataSource;
import com.qingge.yangsong.factory.model.RspModel;
import com.qingge.yangsong.factory.model.card.PostCard;
import com.qingge.yangsong.factory.model.card.UserCard;
import com.qingge.yangsong.factory.model.db.Post;
import com.qingge.yangsong.factory.model.db.User;
import com.qingge.yangsong.factory.net.Network;
import com.qingge.yangsong.factory.net.RemoteService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by White paper on 2019/10/18
 * Describe :
 */
public class SearchHelper {
    public static Call searchPeople(String name, DataSource.Callback<List<User>> callback) {
        RemoteService service = Network.remote();
        Call<RspModel<List<UserCard>>> call = service.searchByName(name);
        call.enqueue(new Callback<RspModel<List<UserCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<UserCard>>> call, Response<RspModel<List<UserCard>>> response) {
                RspModel<List<UserCard>> model = response.body();
                if (model == null) {
                    callback.onDataNotAvailable(R.string.data_network_error);
                    return;
                }
                if (model.success()) {
                    List<UserCard> cardList = model.getResult();
                    List<User> users = new ArrayList<>();
                    for (UserCard card : cardList) {
                        users.add(card.build());
                    }
                    if (callback != null) {
                        callback.onDataLoaded(users);
                    }
                } else {
                    if (callback != null)
                        Factory.decodeRspCode(model, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<UserCard>>> call, Throwable t) {
                if (callback != null)
                    callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
        return call;
    }

    public static Call searchPost(String name, DataSource.Callback<List<Post>> callback) {
        RemoteService service = Network.remote();
        Call<RspModel<List<PostCard>>> call = service.searchPostByName(name);
        call.enqueue(new Callback<RspModel<List<PostCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<PostCard>>> call, Response<RspModel<List<PostCard>>> response) {
                RspModel<List<PostCard>> model = response.body();
                if (model == null) {
                    callback.onDataNotAvailable(R.string.data_network_error);
                    return;
                }
                if (model.success()) {
                    List<PostCard> cardList = model.getResult();
                    List<Post> posts = new ArrayList<>();
                    for (PostCard card : cardList) {
                        posts.add(card.build());
                    }
                    if (callback != null) {
                        callback.onDataLoaded(posts);
                    }
                } else {
                    if (callback != null)
                        Factory.decodeRspCode(model, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<PostCard>>> call, Throwable t) {
                if (callback != null)
                    callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
        return call;
    }
}
