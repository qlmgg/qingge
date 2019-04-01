package com.qingge.yangsong.factory.data.helper;

import android.util.Log;

import com.qingge.yangsong.factory.Factory;
import com.qingge.yangsong.factory.R;
import com.qingge.yangsong.factory.data.DataSource;
import com.qingge.yangsong.factory.model.RspModel;
import com.qingge.yangsong.factory.model.account.AccountRspModel;
import com.qingge.yangsong.factory.model.account.LoginModel;
import com.qingge.yangsong.factory.model.account.RegisterModel;
import com.qingge.yangsong.factory.model.db.University;
import com.qingge.yangsong.factory.model.db.User;
import com.qingge.yangsong.factory.net.Network;
import com.qingge.yangsong.factory.net.RemoteService;
import com.qingge.yangsong.factory.presenter.Account;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountHelper {

    //登陆
    public static void login(LoginModel model, DataSource.Callback<User> callback) {
        // 调用Retrofit对我们的网络请求接口做代理
        RemoteService service = Network.remote();
        service.accountLogin(model).enqueue(new AccountRspCallback(callback));
    }

    //注册
    public static void register(RegisterModel model, DataSource.Callback<User> callback){
        Network.remote().accountRegister(model).enqueue(new AccountRspCallback(callback));
    }

    /**
     * 对设备进行绑定操作
     */
    public static void bindPush(DataSource.Callback<User> callback) {
        String pushId = Account.getPushId();
        if (pushId != null) {
            Network.remote().accountBind(pushId).enqueue(new AccountRspCallback(callback));
        }
    }

    private static class AccountRspCallback implements Callback<RspModel<AccountRspModel>> {
        final DataSource.Callback<User> callback;

        AccountRspCallback(DataSource.Callback<User> callback) {
            this.callback = callback;
        }

        @Override
        public void onResponse(Call<RspModel<AccountRspModel>> call,
                               Response<RspModel<AccountRspModel>> response) {

            RspModel<AccountRspModel> rspModel = response.body();
            if (rspModel.success()) {
                final AccountRspModel model = rspModel.getResult(); //映射

                //构建user
                User user = model.getUserCard().build();
                //保存
                DbHelper.save(User.class,user);
                //进行一次学校的查询并保存
                SchoolHelper.findUniversity(user.getSchoolId());
                // 同步到XML持久化中
                Account.login(model);
                //判断是否绑定
                if (model.isBind()) {
                    //设置绑定ok
                    Account.setBind(true);
                    //然后返回
                    if (callback!=null)
                        callback.onDataLoaded(user);
                } else {
                    //唤起绑定
                    bindPush(callback);
                }
            } else {//错误解析
                Factory.decodeRspCode(rspModel, callback);
            }
        }


        @Override
        public void onFailure(Call<RspModel<AccountRspModel>> call, Throwable t) {
            if (callback != null)
                callback.onDataNotAvailable(R.string.data_network_error);
        }
    }


}
