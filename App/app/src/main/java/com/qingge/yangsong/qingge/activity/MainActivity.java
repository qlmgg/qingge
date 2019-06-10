package com.qingge.yangsong.qingge.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.qingge.yangsong.common.app.Activity;
import com.qingge.yangsong.common.app.Fragment;
import com.qingge.yangsong.qingge.R;
import com.qingge.yangsong.qingge.fragments.main.CateringFragment;
import com.qingge.yangsong.qingge.fragments.main.DailyFragment;
import com.qingge.yangsong.qingge.fragments.main.CommunityFragment;
import com.qingge.yangsong.qingge.fragments.main.MyFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends Activity
        implements BottomNavigationView.OnNavigationItemSelectedListener {
    private CateringFragment cateringFragment;
    private CommunityFragment communityFragment;
    private DailyFragment dailyFragment;
    private MyFragment myFragment;
    private Fragment mCurrent; //用户保存内存回收 MainActivity销毁后保存当前显示的fragment,用于重启后的找回
    private List<Fragment> mFragments = new ArrayList<>(); //保存当前活动下的f,用于配合mCurrent的恢复
    private static final String SAVE_CURRENT_FRAGMENT_TAG = "TAG";
    @BindView(R.id.navigation)
    BottomNavigationView mNavigationView;

    public static void show(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        if (mSaveInstanceState != null) {
            //拿到操作事物
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            //找到f并进行赋值,然后拿到回收前的frag
            Fragment saveFragment = findFragments();

            for (Fragment fragment : mFragments) {
                if (fragment == saveFragment) {
                    transaction = transaction.show(fragment);//表示这个f与保存的相同,那么就显示这个,否则隐藏
                } else
                    transaction = transaction.hide(fragment);
            }
            transaction.commit();
        } else {
            initFragments();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.lay_container, cateringFragment, CateringFragment.class.getSimpleName())
                    .add(R.id.lay_container, communityFragment, CommunityFragment.class.getSimpleName())
                    .add(R.id.lay_container, dailyFragment, DailyFragment.class.getSimpleName())
                    .add(R.id.lay_container, myFragment, MyFragment.class.getSimpleName())
                    .show(cateringFragment)
                    .hide(communityFragment)
                    .hide(dailyFragment)
                    .hide(myFragment)
                    .commit();
        }

        mNavigationView.setOnNavigationItemSelectedListener(this);

    }

    //如果程序被内存回收  就找回fragment并添加进去
    private Fragment findFragments() {
        //当内存回收后重启,拿到保存的回收前的frag的tag
        String tag = mSaveInstanceState.getString(SAVE_CURRENT_FRAGMENT_TAG);
        Fragment getSaveFragment = (Fragment) getSupportFragmentManager().findFragmentByTag(tag);

        cateringFragment = (CateringFragment) getSupportFragmentManager().findFragmentByTag(CateringFragment.class.getSimpleName());
        communityFragment = (CommunityFragment) getSupportFragmentManager().findFragmentByTag(CommunityFragment.class.getSimpleName());
        dailyFragment = (DailyFragment) getSupportFragmentManager().findFragmentByTag(DailyFragment.class.getSimpleName());
        myFragment = (MyFragment) getSupportFragmentManager().findFragmentByTag(MyFragment.class.getSimpleName());
        //保存到数组
        mFragments.add(cateringFragment);
        mFragments.add(communityFragment);
        mFragments.add(dailyFragment);
        mFragments.add(myFragment);

        return getSaveFragment;
    }

    //初始化首页的四个tabFragm
    private void initFragments() {

        cateringFragment = (CateringFragment) CateringFragment.instantiate(MainActivity.this, CateringFragment.class.getName(), null);
        communityFragment = (CommunityFragment) CateringFragment.instantiate(MainActivity.this, CommunityFragment.class.getName(), null);
        dailyFragment = (DailyFragment) CateringFragment.instantiate(MainActivity.this, DailyFragment.class.getName(), null);
        myFragment = (MyFragment) CateringFragment.instantiate(MainActivity.this, MyFragment.class.getName(), null);
        //保存
        mFragments.add(cateringFragment);
        mFragments.add(communityFragment);
        mFragments.add(dailyFragment);
        mFragments.add(myFragment);

        //每次显示都会把显示的fragment存到mCurrent中去
        mCurrent = cateringFragment;

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.tab_catering:
                mCurrent = cateringFragment;
                getSupportFragmentManager().beginTransaction()
                        .show(cateringFragment)
                        .hide(communityFragment)
                        .hide(dailyFragment)
                        .hide(myFragment)
                        .commit();
                break;
            case R.id.tab_message:
                mCurrent = communityFragment;
                getSupportFragmentManager().beginTransaction()
                        .hide(cateringFragment)
                        .show(communityFragment)
                        .hide(dailyFragment)
                        .hide(myFragment)
                        .commit();
                break;
            case R.id.tab_daily:
                mCurrent = dailyFragment;
                getSupportFragmentManager().beginTransaction()
                        .hide(cateringFragment)
                        .hide(communityFragment)
                        .show(dailyFragment)
                        .hide(myFragment)
                        .commit();
                break;
            case R.id.tab_my:
                mCurrent = myFragment;
                getSupportFragmentManager().beginTransaction()
                        .hide(cateringFragment)
                        .hide(communityFragment)
                        .hide(dailyFragment)
                        .show(myFragment)
                        .commit();
                break;
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //保存回收前的tab
        outState.putString(SAVE_CURRENT_FRAGMENT_TAG, mCurrent.getClass().getSimpleName());
        super.onSaveInstanceState(outState);

    }
}
