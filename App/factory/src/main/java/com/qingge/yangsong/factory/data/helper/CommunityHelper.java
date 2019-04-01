package com.qingge.yangsong.factory.data.helper;

import com.qingge.yangsong.factory.Factory;
import com.qingge.yangsong.factory.R;
import com.qingge.yangsong.factory.data.DataSource;
import com.qingge.yangsong.factory.model.RspModel;
import com.qingge.yangsong.factory.model.SchoolInfoModel;
import com.qingge.yangsong.factory.model.account.AccountRspModel;
import com.qingge.yangsong.factory.model.db.User;
import com.qingge.yangsong.factory.net.Network;
import com.qingge.yangsong.factory.net.RemoteService;
import com.qingge.yangsong.factory.presenter.Account;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//社区的helper
public class CommunityHelper {

    //范围切换
    public static Call rangeSwitching(String range, final DataSource.Callback<List<SchoolInfoModel>> callback) {
        RemoteService service = Network.remote();
        Call<RspModel<List<SchoolInfoModel>>> call = service.schoolsInRange(range);
        call.enqueue(new Callback<RspModel<List<SchoolInfoModel>>>() {
            @Override
            public void onResponse(Call<RspModel<List<SchoolInfoModel>>> call, Response<RspModel<List<SchoolInfoModel>>> response) {
                RspModel<List<SchoolInfoModel>> rspModel = response.body();
                if (rspModel.success()) {
                    callback.onDataLoaded(rspModel.getResult());
                } else {
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<SchoolInfoModel>>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
        return call;
    }
}
