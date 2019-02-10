package com.qingge.yangsong.factory.presenter.account;

import com.qingge.yangsong.factory.presenter.BaseContract;
/**
 * 注册的契约
 * */
public interface RegisterContract {
    interface View extends BaseContract.View<Presenter>{
        void registerSuccess();
    }
    interface Presenter extends BaseContract.Presenter{
        void register(String phone,String password,String name);
    }
}
