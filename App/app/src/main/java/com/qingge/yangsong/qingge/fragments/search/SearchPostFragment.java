package com.qingge.yangsong.qingge.fragments.search;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qingge.yangsong.common.app.PresenterFragment;
import com.qingge.yangsong.common.widget.recycler.RecyclerAdapter;
import com.qingge.yangsong.factory.model.db.Post;
import com.qingge.yangsong.factory.model.db.User;
import com.qingge.yangsong.factory.presenter.BaseContract;
import com.qingge.yangsong.factory.presenter.search.SearchPostContact;
import com.qingge.yangsong.factory.presenter.search.SearchPostPresenter;
import com.qingge.yangsong.qingge.R;
import com.qingge.yangsong.qingge.activity.SearchActivity;

import butterknife.BindView;

/**
 * Created by White paper on 2019/10/18
 * Describe :
 */
public class SearchPostFragment extends PresenterFragment<SearchPostContact.Presenter>
        implements SearchActivity.SearchFragment, SearchPostContact.View {
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    private RecyclerAdapter<Post> adapter;

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapter = new RecyclerAdapter<Post>() {
            @Override
            protected int getItemViewType(int position, Post post) {
                return R.layout.cell_search_post;
            }

            @Override
            protected ViewHolder<Post> onCreateViewHolder(View view, int viewType) {
                return new SearchPostFragment.ViewHolder(view);
            }
        });
    }

    @Override
    protected SearchPostContact.Presenter initPresenter() {
        return new SearchPostPresenter(this);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_search;
    }

    @Override
    public void search(String content) {
        mPresenter.search(content);
    }

    @Override
    public RecyclerAdapter<Post> getRecyclerAdapter() {
        return adapter;
    }

    @Override
    public void onAdapterDataChanged() {

    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<Post> {

        ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Post post) {

        }
    }
}
