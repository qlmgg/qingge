package com.qingge.yangsong.factory.presenter.account;

import com.qingge.yangsong.factory.presenter.BaseContract;

public interface LoginContract {
    interface View extends BaseContract.View<Presenter>{
        //登陆成功
        void loginSuccess();
    }

    interface Presenter extends BaseContract.Presenter{
        //发起登陆
        void login(String phone,String password);
    }
}
