package com.qingge.yangsong.common.app;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ProgressBar;

import com.qingge.yangsong.common.R;
import com.qingge.yangsong.factory.presenter.BaseContract;

import net.qiujuer.genius.ui.widget.Loading;

import static android.view.View.GONE;

public abstract class PresenterToolbarActivity<Presenter extends BaseContract.Presenter>
        extends ToolbarActivity implements BaseContract.View<Presenter> {
    protected Presenter mPresenter;
    protected ProgressBar mLoadingDialog;
    @Override
    protected void initBefore() {
        super.initBefore();
        // 初始化Presenter
        initPresenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 界面关闭时进行销毁的操作
        if (mPresenter != null) {
            mPresenter.destroy();
        }
    }

    /**
     * 初始化Presenter
     *
     * @return Presenter
     */
    protected abstract Presenter initPresenter();

    @Override
    public void showError(int str) {
        hideLoading();
        // 显示错误, 优先使用占位布局
        if (mPlaceHolderView != null) {
            mPlaceHolderView.triggerError(str);
        } else {
            Application.showToast(str);
        }
    }

    @Override
    public void showLoading() {
        if (mPlaceHolderView != null) {
            mPlaceHolderView.triggerLoading();
        } else {
            ProgressBar loading = mLoadingDialog;
            if (loading == null) {
                loading = new ProgressBar(this,null,R.attr.progressBarStyle);
                // 不可触摸取消
                // 强制取消关闭界面

                mLoadingDialog = loading;
            }
            loading.setVisibility(View.VISIBLE);

        }
    }

    protected void hideDialogLoading() {
        ProgressBar dialog = mLoadingDialog;
        if (dialog != null) {
            mLoadingDialog = null;
            dialog.setVisibility(GONE);
        }
    }


    protected void hideLoading() {
        // 不管你怎么样，我先隐藏我
        hideDialogLoading();

        if (mPlaceHolderView != null) {
            mPlaceHolderView.triggerOk();
        }
    }

    @Override
    public void setPresenter(Presenter presenter) {
        // View中赋值Presenter
        mPresenter = presenter;
    }

}
