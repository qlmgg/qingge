package com.qingge.yangsong.qingge.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.qingge.yangsong.common.app.Activity;
import com.qingge.yangsong.common.app.Fragment;
import com.qingge.yangsong.qingge.R;
import com.qingge.yangsong.qingge.fragments.search.SearchPeopleFragment;
import com.qingge.yangsong.qingge.fragments.search.SearchPostFragment;

import butterknife.BindView;

/**
 * Created by White paper on 2019/10/18
 * Describe :
 */
public class SearchActivity extends Activity {
    private static final String SEARCH_TYPE = "SEARCH_TYPE";
    public static final int TYPE_PEOPLE = 0;//搜索人
    public static final int TYPE_POST = 1;//搜索帖子
    private SearchFragment mSearchFragment;
    private int type;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.edt_search)
    EditText mSearch;

    public static void show(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(SEARCH_TYPE, TYPE_PEOPLE);
        context.startActivity(intent);
    }

//    public static void show(Context context) {
//        Intent intent = new Intent(context, SearchActivity.class);
//        intent.putExtra(SEARCH_TYPE, TYPE_POST);
//        context.startActivity(intent);
//    }


    @Override
    protected boolean initArgs(Bundle bundle) {
        int type = this.type = bundle.getInt(SEARCH_TYPE);
        return type == TYPE_PEOPLE || type == TYPE_POST;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mToolbar.inflateMenu(R.menu.search_menu);
        Fragment fragment;
        if (type == TYPE_PEOPLE) {
            SearchPeopleFragment searchPeopleFragment = new SearchPeopleFragment();
            fragment = searchPeopleFragment;
            mSearchFragment = searchPeopleFragment;
        } else {
            SearchPostFragment searchPostFragment = new SearchPostFragment();
            fragment = searchPostFragment;
            mSearchFragment = searchPostFragment;
        }

        getSupportFragmentManager().beginTransaction()
                .add(R.id.lay_container, fragment)
                .commit();
        mToolbar.setNavigationOnClickListener(v -> finish());
        mToolbar.setOnMenuItemClickListener(menuItem -> {
            mSearchFragment.search(mSearch.getText().toString().trim());
            return false;
        });
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_search;
    }

    public interface SearchFragment {
        void search(String content);
    }
}
