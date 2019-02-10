package com.qingge.yangsong.qingge.fragments.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

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


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class MyFragment extends Fragment implements LoginActivity.initMyData {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.appbar)
    AppBarLayout mAppBar;

    @BindView(R.id.FrameLayout)
    FrameLayout mFrameLayout;

    @BindView(R.id.tab_layout)
    TabLayout mTbaLayout;

    @BindView(R.id.PortraitView)
    PortraitView mPortraitView;


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

        mAppBar.addOnOffsetChangedListener((appBarLayout, i) -> {
            //i这个值就是当完全展开就是0       收完就是x
            int verticalOffset = Math.abs(i);
            //这个就是拿到最大值   就是完全收拢时的值
            float totalScrollRange = appBarLayout.getTotalScrollRange();
            //进度
            float progress = verticalOffset / totalScrollRange;

            if (progress == 1) {
                mToolbar.setTitleMarginBottom(55);
            } else {
                mToolbar.setTitleMarginBottom((int) (55 * progress));
            }
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
