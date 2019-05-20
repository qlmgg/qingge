package com.qingge.yangsong.factory.data.helper;

import com.qingge.yangsong.factory.Factory;
import com.qingge.yangsong.factory.R;
import com.qingge.yangsong.factory.data.DataSource;
import com.qingge.yangsong.factory.model.RspModel;
import com.qingge.yangsong.factory.model.card.GroupCard;
import com.qingge.yangsong.factory.model.db.Group;
import com.qingge.yangsong.factory.model.db.Group_Table;
import com.qingge.yangsong.factory.model.group.GroupCreateModel;
import com.qingge.yangsong.factory.net.Network;
import com.qingge.yangsong.factory.net.RemoteService;
import com.qingge.yangsong.factory.presenter.group.GroupCreatePresenter;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 对群的一个简单的辅助工具类
 */
public class GroupHelper {

    public static Group find(String groupId) {
        // TODO 查询群的信息，先本地，后网络
        return findFromLocal(groupId);
    }

    public static Group findFromLocal(String groupId) {
        return SQLite.select()
                .from(Group.class)
                .where(Group_Table.id.eq(groupId))
                .querySingle();
    }

    //创建群
    // 群的创建
    public static void create(GroupCreateModel model, final DataSource.Callback<GroupCard> callback) {
        RemoteService service = Network.remote();
        service.groupCreate(model)
                .enqueue(new Callback<RspModel<GroupCard>>() {
                    @Override
                    public void onResponse(Call<RspModel<GroupCard>> call, Response<RspModel<GroupCard>> response) {
                        RspModel<GroupCard> rspModel = response.body();
                        if (rspModel.success()) {
                            GroupCard groupCard = rspModel.getResult();
                            // 唤起进行保存的操作
                            Factory.getGroupCenter().dispatch(groupCard);
                            // 返回数据
                            callback.onDataLoaded(groupCard);
                        } else {
                            Factory.decodeRspCode(rspModel, callback);
                        }
                    }

                    @Override
                    public void onFailure(Call<RspModel<GroupCard>> call, Throwable t) {
                        callback.onDataNotAvailable(R.string.data_network_error);
                    }
                });
    }
}
