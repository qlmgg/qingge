package com.qingge.yangsong.factory.presenter.post;

import com.qingge.yangsong.factory.presenter.BaseContract;

import java.util.List;

/**
 * Created by White paper on 2019/10/20
 * Describe :
 */
public interface SendPostContract {
    interface View extends BaseContract.View<Presenter> {
        void success();
    }

    interface Presenter extends BaseContract.Presenter {
        void send(String content, List<String> picturePaths);
    }
}
