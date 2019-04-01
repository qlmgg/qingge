package com.qingge.yangsong.factory.presenter.community;

import retrofit2.Call;

import com.qingge.yangsong.factory.data.DataSource;
import com.qingge.yangsong.factory.data.helper.CommunityHelper;
import com.qingge.yangsong.factory.model.SchoolInfoModel;
import com.qingge.yangsong.factory.presenter.BasePresenter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.List;

public class CommunityPresenter extends BasePresenter<CommunityContract.View>
        implements CommunityContract.Presenter, DataSource.Callback<List<SchoolInfoModel>> {

    private Call queryCall;

    public CommunityPresenter(CommunityContract.View view) {
        super(view);
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void rangeSwitching(String range) {
        start();

        Call call = queryCall;
        if (call != null && !call.isCanceled()) {
            // 当正在网络请求的时候如果再次请求,就会先关闭上个请求
            call.cancel();
        }
        //开始切换   注意  因为前期只拿两个或三个学校试点   所有就不弄跨校了,这儿的请求方式与返回并
        //未做改变   返回的只能是一个  那就是本校的信息
        queryCall = CommunityHelper.rangeSwitching(range, this);
    }


    @Override
    public void onDataLoaded(final List<SchoolInfoModel> models) {
        final CommunityContract.View view = getView();
        if (view != null) {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.schoolInfo(models);
                }
            });
        }
    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        final CommunityContract.View view = getView();
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
