package com.qingge.yangsong.qingge.fragments.main;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingge.yangsong.common.app.Fragment;
import com.qingge.yangsong.common.app.PresenterFragment;

import com.qingge.yangsong.common.widget.MyViewPagerAdapter;
import com.qingge.yangsong.common.widget.PortraitView;
import com.qingge.yangsong.factory.presenter.Account;
import com.qingge.yangsong.factory.presenter.account.LoginContract;
import com.qingge.yangsong.factory.presenter.account.LoginPresenter;
import com.qingge.yangsong.qingge.R;
import com.qingge.yangsong.qingge.activity.LoginActivity;
import com.qingge.yangsong.qingge.fragments.mytab.CollectionFragment;
import com.qingge.yangsong.qingge.fragments.mytab.DynamicFragment;
import com.qingge.yangsong.qingge.fragments.mytab.OrderFragment;
import com.qingge.yangsong.qingge.fragments.mytab.ShopFragment;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class MyFragment extends Fragment implements LoginActivity.initMyData {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.CollapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.appbar)
    AppBarLayout mAppBar;

    @BindView(R.id.FrameLayout)
    FrameLayout mFrameLayout;

    @BindView(R.id.tab_layout)
    TabLayout mTbaLayout;

    @BindView(R.id.PortraitView)
    PortraitView mPortraitView;

    @BindView(R.id.ViewPager)
    ViewPager mViewPager;

    @BindView(R.id.tv_name)
    TextView mTextView;

    @Override
    protected void initData() {
        super.initData();
        mPortraitView.setup(Glide.with(getActivity()), Account.getUser());
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_my;
    }


    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        List<Fragment> mFragments = new ArrayList<>();
        //因为系统原因  需要把tab的名字传过去才能设定到TabLayout上
        List<String> mTitles = new ArrayList<>();

        mFragments.add(new OrderFragment());
        mFragments.add(new CollectionFragment());
        mFragments.add(new ShopFragment());
        mFragments.add(new DynamicFragment());
        mTitles.add("订单");
        mTitles.add("收藏");
        mTitles.add("店铺");
        mTitles.add("动态");

        //设置标题为的账户名
        collapsingToolbarLayout.setTitle(Account.getUser().getName());
        //设置标题位置
        collapsingToolbarLayout.setCollapsedTitleGravity(Gravity.TOP);
        mToolbar.setTitleMarginTop(25);

        if (Account.getUser() != null)
            mTextView.setText(Account.getUser().getName());

        MyViewPagerAdapter adapter = new MyViewPagerAdapter(Objects.requireNonNull(getActivity()).getSupportFragmentManager(),
                mFragments,
                mTitles);
        mViewPager.setAdapter(adapter);
        //关联
        mTbaLayout.setupWithViewPager(mViewPager);
        //折叠布局折叠时的监听
        mAppBar.addOnOffsetChangedListener((appBarLayout, i) -> {

            //i这个值就是当完全展开就是0       收完就是x
            int verticalOffset = Math.abs(i);
            //这个就是拿到最大值   就是完全收拢时的值
            float totalScrollRange = appBarLayout.getTotalScrollRange();
            //进度
            float progress = verticalOffset / totalScrollRange;

            if (progress == 1) {
                collapsingToolbarLayout.setTitleEnabled(true);
            } else
                collapsingToolbarLayout.setTitleEnabled(false);

        });
    }

    @OnClick(R.id.PortraitView)
    public void startLogin() {
        if (!Account.isLogin()) {//如果没登陆才跳转到登陆
            LoginActivity.show(Objects.requireNonNull(getActivity()), this);
        }

    }

    //当登陆完成后的调用,用于加载头像等数据
    @Override
    public void init() {
        //加载头像
        mPortraitView.setup(Glide.with(MyFragment.this), Account.getUser());

    }
}
