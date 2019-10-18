package com.qingge.yangsong.qingge.fragments.main;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingge.yangsong.common.app.PresenterFragment;
import com.qingge.yangsong.common.widget.PortraitView;
import com.qingge.yangsong.common.widget.recycler.RecyclerAdapter;
import com.qingge.yangsong.factory.model.db.User;
import com.qingge.yangsong.factory.presenter.contact.ContactContract;
import com.qingge.yangsong.factory.presenter.contact.ContactPresenter;
import com.qingge.yangsong.qingge.R;

import butterknife.BindView;

/**
 * Created by White paper on 2019/10/16
 * Describe :]
 */
public class ContactFragment extends PresenterFragment<ContactContract.Presenter>
        implements ContactContract.View,RecyclerAdapter.AdapterListener<User> {
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;

    private RecyclerAdapter<User> adapter;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_contacts;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecyclerAdapter<User>(this) {
            @Override
            protected int getItemViewType(int position, User user) {
                return R.layout.cell_contacts;
            }

            @Override
            protected ViewHolder<User> onCreateViewHolder(View view, int viewType) {
                return new ContactFragment.ViewHolder(view);
            }
        };
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.start();
    }



    @Override
    protected ContactPresenter initPresenter() {
        return new ContactPresenter(this);
    }


    @Override
    public RecyclerAdapter<User> getRecyclerAdapter() {
        return adapter;
    }

    @Override
    public void onAdapterDataChanged() {

    }

    @Override
    public void onItemClick(RecyclerAdapter.ViewHolder holder, User user) {

    }

    @Override
    public void onItemLongClick(RecyclerAdapter.ViewHolder holder, User user) {

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
