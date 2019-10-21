package com.qingge.yangsong.factory.presenter.search;

import com.qingge.yangsong.factory.data.DataSource;
import com.qingge.yangsong.factory.data.helper.SearchHelper;
import com.qingge.yangsong.factory.model.db.Post;
import com.qingge.yangsong.factory.presenter.BaseRecyclerPresenter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.List;

import retrofit2.Call;

/**
 * Created by White paper on 2019/10/18
 * Describe :
 */
public class SearchPostPresenter extends BaseRecyclerPresenter<Post, SearchPostContact.View>
        implements SearchPostContact.Presenter, DataSource.Callback<List<Post>> {
    private Call searchCall;

    public SearchPostPresenter(SearchPostContact.View view) {
        super(view);
    }

    @Override
    public void onDataLoaded(List<Post> posts) {
        Run.onUiSync(() -> refreshData(posts));
    }

    @Override
    public void onDataNotAvailable(int strRes) {
        Run.onUiAsync(() -> getView().showError(strRes));
    }

    @Override
    public void search(String name) {
        Call call = searchCall;
        if (call != null && !call.isCanceled()) {
            call.cancel();
        }
        searchCall = SearchHelper.searchPost(name, this);
    }
}
