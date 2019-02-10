package com.qingge.yangsong.factory.presenter;

public class BaseActivityPresenter<View extends BaseContract.View>
        implements BaseContract.Presenter {
    private View mView;


    public BaseActivityPresenter(View view) {
        setView(view);
    }

    @SuppressWarnings("unchecked")
    public void setView(View mView) {
        this.mView = mView;
        this.mView.setPresenter(this);
    }

    //子类拿取view
    protected View getView() {
        return mView;
    }

    @Override
    public void start() {
        if (mView!=null){
            mView.showLoading();
        }
    }

    @Override
    public void destroy() {

    }


}
