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

import com.qingge.yangsong.qingge.fragments.main.ContactFragment;
import com.qingge.yangsong.qingge.fragments.main.MessageFragment;
import com.qingge.yangsong.qingge.fragments.main.CommunityFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends Activity
        implements BottomNavigationView.OnNavigationItemSelectedListener {
    private CommunityFragment mCommunityFragment;
    private MessageFragment mMessageFragment;
    private ContactFragment mContactsFragment;
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
                    .add(R.id.lay_container, mCommunityFragment, CommunityFragment.class.getSimpleName())
                    .add(R.id.lay_container, mMessageFragment, MessageFragment.class.getSimpleName())
                    .add(R.id.lay_container, mContactsFragment, ContactFragment.class.getSimpleName())
                    .show(mCommunityFragment)
                    .hide(mMessageFragment)
                    .hide(mContactsFragment)
                    .commit();
        }

        mNavigationView.setOnNavigationItemSelectedListener(this);

    }

    //如果程序被内存回收  就找回fragment并添加进去
    private Fragment findFragments() {
        //当内存回收后重启,拿到保存的回收前的frag的tag
        String tag = mSaveInstanceState.getString(SAVE_CURRENT_FRAGMENT_TAG);
        Fragment getSaveFragment = (Fragment) getSupportFragmentManager().findFragmentByTag(tag);

        mCommunityFragment = (CommunityFragment) getSupportFragmentManager().findFragmentByTag(CommunityFragment.class.getSimpleName());
        mMessageFragment = (MessageFragment) getSupportFragmentManager().findFragmentByTag(MessageFragment.class.getSimpleName());
        mContactsFragment = (ContactFragment) getSupportFragmentManager().findFragmentByTag(ContactFragment.class.getSimpleName());
        //保存到数组
        mFragments.add(mCommunityFragment);
        mFragments.add(mMessageFragment);
        mFragments.add(mContactsFragment);

        return getSaveFragment;
    }

    private void initFragments() {
        mCommunityFragment = (CommunityFragment) CommunityFragment.instantiate(MainActivity.this, CommunityFragment.class.getName(), null);
        mMessageFragment = (MessageFragment) MessageFragment.instantiate(MainActivity.this, MessageFragment.class.getName(), null);
        mContactsFragment = (ContactFragment) ContactFragment.instantiate(MainActivity.this, ContactFragment.class.getName(), null);
        //保存
        mFragments.add(mCommunityFragment);
        mFragments.add(mMessageFragment);
        mFragments.add(mContactsFragment);

        //每次显示都会把显示的fragment存到mCurrent中去
        mCurrent = mCommunityFragment;

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.tab_community:
                mCurrent = mCommunityFragment;
                getSupportFragmentManager().beginTransaction()
                        .show(mCommunityFragment)
                        .hide(mMessageFragment)
                        .hide(mContactsFragment)
                        .commit();
                break;
            case R.id.tab_message:
                mCurrent = mMessageFragment;
                getSupportFragmentManager().beginTransaction()
                        .hide(mCommunityFragment)
                        .show(mMessageFragment)
                        .hide(mContactsFragment)
                        .commit();
                break;
            case R.id.tab_contact:
                mCurrent = mContactsFragment;
                getSupportFragmentManager().beginTransaction()
                        .hide(mCommunityFragment)
                        .hide(mMessageFragment)
                        .show(mContactsFragment)
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
