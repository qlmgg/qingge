package com.qingge.yangsong.qingge.fragments.main;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.qingge.yangsong.common.app.Fragment;
import com.qingge.yangsong.common.widget.MyViewPagerAdapter;
import com.qingge.yangsong.qingge.R;
import com.qingge.yangsong.qingge.fragments.daily.EverydayFragment;
import com.qingge.yangsong.qingge.fragments.daily.NewsFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;

public class DailyFragment extends com.qingge.yangsong.common.app.Fragment {
@BindView(R.id.tab_layout_daily)
    TabLayout mTabLayout;
@BindView(R.id.viewpager)
    ViewPager mViewPager;
    public DailyFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_daily;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        List<Fragment> mFragments = new ArrayList<>();
        //因为系统原因  需要把tab的名字传过去才能设定到TabLayout上
        List<String> mTitles = new ArrayList<>();
        mFragments.add(new NewsFragment());
        mFragments.add(new EverydayFragment());
        mTitles.add("消息");
        mTitles.add("日常");

        MyViewPagerAdapter adapter = new MyViewPagerAdapter(Objects.requireNonNull(getActivity()).getSupportFragmentManager(),
                mFragments,
                mTitles);
        mViewPager.setAdapter(adapter);
        //关联
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
