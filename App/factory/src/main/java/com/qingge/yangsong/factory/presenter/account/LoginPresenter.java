package com.qingge.yangsong.factory.presenter.account;

import android.text.TextUtils;
import android.util.Log;

import com.qingge.yangsong.factory.Factory;
import com.qingge.yangsong.factory.R;
import com.qingge.yangsong.factory.data.DataSource;
import com.qingge.yangsong.factory.data.helper.AccountHelper;
import com.qingge.yangsong.factory.model.account.LoginModel;
import com.qingge.yangsong.factory.model.db.User;
import com.qingge.yangsong.factory.presenter.Account;
import com.qingge.yangsong.factory.presenter.BaseActivityPresenter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

public class LoginPresenter extends BaseActivityPresenter<LoginContract.View>
        implements LoginContract.Presenter, DataSource.Callback<User> {


    public LoginPresenter(LoginContract.View view) {
        super(view);
    }


    @Override
    public void login(String phone, String password) {
        //加载
        start();
        final LoginContract.View view = getView();
            //判断账号密码
        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {

            view.showError(R.string.data_account_login_invalid_parameter);

        } else {
             LoginModel model = new LoginModel(phone, password, Account.getPushId());
            //登陆
                    AccountHelper.login(model, LoginPresenter.this);

        }

    }

    @Override
    public void onDataLoaded(User user) {
        final LoginContract.View view = getView();
        if (view != null) {
            //回到主线程
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.loginSuccess();
                }
            });
        }
    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        final LoginContract.View view = getView();
        if (view != null) {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.showError(strRes);
                }
            });
        }
    }
}
