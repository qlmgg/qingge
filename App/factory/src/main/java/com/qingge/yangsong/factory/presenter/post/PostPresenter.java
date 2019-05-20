package com.qingge.yangsong.factory.presenter.post;

import android.support.v7.util.DiffUtil;

import com.qingge.yangsong.common.widget.recycler.RecyclerAdapter;
import com.qingge.yangsong.factory.data.DataSource;
import com.qingge.yangsong.factory.data.helper.PostHelper;
import com.qingge.yangsong.factory.model.card.CommentCard;
import com.qingge.yangsong.factory.presenter.BaseRecyclerPresenter;
import com.qingge.yangsong.factory.utils.DiffUiDataCallback;

import java.util.List;

public class PostPresenter extends BaseRecyclerPresenter<CommentCard,PostContract.View>
implements PostContract.Presenter, DataSource.Callback<List<CommentCard>> {
  private String postId;
    public PostPresenter(PostContract.View view,String postId) {
        super(view);
        this.postId = postId;
    }

    @Override
    public void start() {
        super.start();
        PostHelper.loadComment(postId,this);
    }

    @Override
    public void onDataLoaded(List<CommentCard> cards) {

        final PostContract.View view = getView();
        if (view!=null){
            view.onAdapterDataChanged();
            RecyclerAdapter<CommentCard> adapter = view.getRecyclerAdapter();
            List<CommentCard> oldCards = adapter.getItems();
            //数据对比
            DiffUtil.Callback callback = new DiffUiDataCallback<>(oldCards,cards);
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);

            refreshData(result,cards);
        }
    }

    @Override
    public void onDataNotAvailable(int strRes) {
        final PostContract.View view = getView();
        view.showError(strRes);
    }
}
