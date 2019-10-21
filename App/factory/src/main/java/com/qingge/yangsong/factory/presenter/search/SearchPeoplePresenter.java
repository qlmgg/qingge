package com.qingge.yangsong.factory.presenter.search;

import com.qingge.yangsong.factory.data.DataSource;
import com.qingge.yangsong.factory.data.helper.SearchHelper;
import com.qingge.yangsong.factory.model.db.User;
import com.qingge.yangsong.factory.presenter.BaseRecyclerPresenter;

import net.qiujuer.genius.kit.handler.Run;

import java.util.List;

import retrofit2.Call;

/**
 * Created by White paper on 2019/10/18
 * Describe :
 */
public class SearchPeoplePresenter extends BaseRecyclerPresenter<User, SearchPeopleContract.View>
        implements SearchPeopleContract.Presenter, DataSource.Callback<List<User>> {
    private Call searchCall;

    public SearchPeoplePresenter(SearchPeopleContract.View view) {
        super(view);
    }

    @Override
    public void search(String content) {
        Call call = searchCall;
        if (call != null && !call.isCanceled()) {
            call.cancel();
        }
        searchCall = SearchHelper.searchPeople(content, this);
    }

    @Override
    public void onDataLoaded(List<User> user) {
        Run.onUiAsync(() -> refreshData(user));
    }

    @Override
    public void onDataNotAvailable(int strRes) {
        Run.onUiAsync(() -> getView().showError(strRes));
    }
}
