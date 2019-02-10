package com.qingge.yangsong.factory.presenter.account;

import com.qingge.yangsong.factory.data.DataSource;
import com.qingge.yangsong.factory.data.helper.AccountHelper;
import com.qingge.yangsong.factory.model.account.RegisterModel;
import com.qingge.yangsong.factory.model.db.User;
import com.qingge.yangsong.factory.presenter.Account;
import com.qingge.yangsong.factory.presenter.BaseActivityPresenter;
import com.qingge.yangsong.factory.presenter.BaseContract;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

public class RegisterPresenter extends BaseActivityPresenter<RegisterContract.View>
        implements RegisterContract.Presenter, DataSource.Callback<User> {

    public RegisterPresenter(RegisterContract.View view) {
        super(view);
    }

    @Override
    public void register(String phone, String password, String name) {
        start();

        RegisterModel registerModel = new RegisterModel(phone, password, name, Account.getPushId());
        AccountHelper.register(registerModel, this);
    }

    @Override
    public void onDataLoaded(User user) {
        final RegisterContract.View view = getView();
        if (view != null) {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.registerSuccess();
                }
            });
        }
    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        final RegisterContract.View view = getView();
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
