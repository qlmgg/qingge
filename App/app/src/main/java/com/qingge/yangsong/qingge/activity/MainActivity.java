package com.qingge.yangsong.qingge.activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.qingge.yangsong.common.app.Activity;
import com.qingge.yangsong.qingge.R;
import com.qingge.yangsong.qingge.fragments.main.CateringFragment;
import com.qingge.yangsong.qingge.fragments.main.DailyFragment;
import com.qingge.yangsong.qingge.fragments.main.CommunityFragment;
import com.qingge.yangsong.qingge.fragments.main.MyFragment;
import com.qingge.yangsong.qingge.helper.NavHelper;

import butterknife.BindView;

public class MainActivity extends Activity
        implements NavHelper.OnTabChangedListener<Integer>
        , BottomNavigationView.OnNavigationItemSelectedListener {

//    @BindView(R.id.tv_title)
//    TextView mTitle;
//@BindView(R.id.appbar)
//    AppBarLayout appBar;
    @BindView(R.id.navigation)
    BottomNavigationView mNavigationView;

//    @BindView(R.id.im_search)
//    ImageView mSearch;
//    @BindView(R.id.im_menu)
//    ImageView mMenu;

    NavHelper<Integer> mNavHelper;

    public static void show(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected void initWindows() {
        super.initWindows();

        //设置状态栏全透明
        if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.M){
            //如果是6.0以上将状态栏文字改为黑色，并设置状态栏颜色
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        //初始化工具类
        mNavHelper = new NavHelper<>(this, R.id.lay_container,
                getSupportFragmentManager(), this);
        //添加fragment进去
        mNavHelper.add(R.id.tab_catering, new NavHelper.Tab<>(CateringFragment.class, R.string.tab_catering))
                .add(R.id.tab_message, new NavHelper.Tab<>(CommunityFragment.class, R.string.tab_message))
                .add(R.id.tab_daily,new NavHelper.Tab<>(DailyFragment.class,R.string.tab_daily))
                .add(R.id.tab_my, new NavHelper.Tab<>(MyFragment.class, R.string.tab_my));
        mNavigationView.setOnNavigationItemSelectedListener(this);

    }

    @Override
    protected void initData() {
        super.initData();
        //拿到容器中menu
        Menu menu = mNavigationView.getMenu();
        //首次选中messageFrag...
        menu.performIdentifierAction(R.id.tab_catering, 0);
    }


    @Override
    public void onTabChanged(NavHelper.Tab<Integer> newTab, NavHelper.Tab<Integer> oldTab) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        //让工具类来处理点击的menuItem
        return mNavHelper.performClickMenu(menuItem.getItemId());
    }

}
