package com.qingge.yangsong.qingge.fragments.search;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingge.yangsong.common.app.PresenterFragment;
import com.qingge.yangsong.common.widget.PortraitView;
import com.qingge.yangsong.common.widget.recycler.RecyclerAdapter;
import com.qingge.yangsong.factory.model.db.User;
import com.qingge.yangsong.factory.presenter.search.SearchPeopleContract;
import com.qingge.yangsong.factory.presenter.search.SearchPeoplePresenter;
import com.qingge.yangsong.qingge.R;
import com.qingge.yangsong.qingge.activity.SearchActivity;

import butterknife.BindView;

/**
 * Created by White paper on 2019/10/18
 * Describe :
 */
public class SearchPeopleFragment extends PresenterFragment<SearchPeopleContract.Presenter>
        implements SearchActivity.SearchFragment, SearchPeopleContract.View {
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    private RecyclerAdapter<User> adapter;
    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapter = new RecyclerAdapter<User>() {
            @Override
            protected int getItemViewType(int position, User user) {
                return R.layout.cell_contacts;
            }

            @Override
            protected ViewHolder<User> onCreateViewHolder(View view, int viewType) {
                return new SearchPeopleFragment.ViewHolder(view);
            }
        });
    }

    @Override
    protected SearchPeopleContract.Presenter initPresenter() {
        return new SearchPeoplePresenter(this);
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
    public RecyclerAdapter<User> getRecyclerAdapter() {
        return adapter;
    }

    @Override
    public void onAdapterDataChanged() {

    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<User> {
        @BindView(R.id.im_portrait)
        PortraitView mPortraitView;
        @BindView(R.id.txt_contacts_name)
        TextView mName;
        @BindView(R.id.txt_contacts_desc)
        TextView mDesc;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(User user) {
            mPortraitView.setup(Glide.with(getContext()), user);
            mName.setText(user.getName());
            mDesc.setText(user.getDesc());
        }
    }
}
