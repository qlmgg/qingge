package com.qingge.yangsong.common.app;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.qingge.yangsong.factory.presenter.BaseContract;

/**
 * Activity的基础契约视图层
*/
public abstract class PresenterActivity<Presenter extends BaseContract.Presenter> extends Activity
implements BaseContract.View<Presenter> {

    protected Presenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
    }

    protected abstract void initPresenter();

    @Override
    public void showError(int str) {

    }

    @Override
    public void showLoading() {
        
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.mPresenter = presenter;
    }

}
