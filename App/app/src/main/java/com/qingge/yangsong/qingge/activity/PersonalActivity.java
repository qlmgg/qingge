package com.qingge.yangsong.qingge.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingge.yangsong.common.app.PresenterToolbarActivity;
import com.qingge.yangsong.common.widget.EmptyView;
import com.qingge.yangsong.common.widget.PortraitView;
import com.qingge.yangsong.factory.data.helper.UserHelper;
import com.qingge.yangsong.factory.model.db.User;
import com.qingge.yangsong.factory.presenter.Account;
import com.qingge.yangsong.factory.presenter.contact.PersonalContract;
import com.qingge.yangsong.factory.presenter.contact.PersonalPresenter;
import com.qingge.yangsong.qingge.R;

import net.qiujuer.genius.res.Resource;


import butterknife.BindView;
import butterknife.OnClick;

public class PersonalActivity extends PresenterToolbarActivity<PersonalContract.Presenter>
        implements PersonalContract.View {
    private static final String BOUND_KEY_ID = "BOUND_KEY_ID";
    private String userId;
    @BindView(R.id.personal_appbar)
    AppBarLayout mAppBar;
    @BindView(R.id.portrait)
    PortraitView mPortrait;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.describe)
    TextView mDescribe;
    @BindView(R.id.follows)
    TextView mFollows;
    @BindView(R.id.tv_title_name)
    TextView mTitleName;
    @BindView(R.id.following)
    TextView mFollowing;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.btn_send_message)
    Button mSayHello;
    @BindView(R.id.btn_follow)
    Button mFollow;
    @BindView(R.id.empty_view)
    EmptyView mEmptyView;
    @BindView(R.id.layout)
    CoordinatorLayout layout;
    // 关注
    private MenuItem mFollowItem;
    private boolean mIsFollowUser = false;

    //当前用户的缓存
    private User user;
    //当前是否关注
    private Boolean isFollow;
    public static void show(Context context, String userId) {
        Intent intent = new Intent(context, PersonalActivity.class);
        intent.putExtra(BOUND_KEY_ID, userId);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_personal;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        userId = bundle.getString(BOUND_KEY_ID);
        return !TextUtils.isEmpty(userId);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        setTitle("");
        //设置状态栏全透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //如果是6.0以上将状态栏文字改为黑色，并设置状态栏颜色
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }


        //折叠布局折叠时的监听
        mAppBar.addOnOffsetChangedListener((appBarLayout, i) -> {

            //i这个值就是当完全展开就是0       收完就是x
            int verticalOffset = Math.abs(i);
            //这个就是拿到最大值   就是完全收拢时的值
            float totalScrollRange = appBarLayout.getTotalScrollRange();
            //进度
            float progress = verticalOffset / totalScrollRange;

            if (progress >= 0.5) {
                mTitleName.setText(user.getName());
            } else {
                mTitleName.setText("");

            }
        });


        mEmptyView.bind(layout);
        setPlaceHolderView(mEmptyView);
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.start();
    }

    @OnClick(R.id.btn_follow)
    void onFollow() {
        if(!isFollow)
            mPresenter.userFollow(user.getId());
        else
            mPresenter.userCancelFollow(user.getId());
//        UserHelper.userFollow(user.getId(),);
    }

    @OnClick(R.id.btn_send_message)
    void onSayHelloClick() {
        // 发起聊天的点击
        User user = mPresenter.getUserPersonal();
        if (user == null)
            return;
//        MessageActivity.show(this, user);
        ChatUserActivity.show(this,user);
    }


    @Override
    public String getUserId() {
        return userId;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onLoadDone(User user) {
        if (user == null)
            return;
        this.user = user;
        mEmptyView.triggerOk();
        mPortrait.setup(Glide.with(this), user);
        mTvName.setText(user.getName());
        mDescribe.setText(user.getDesc());
        mFollows.setText("关注 : " + user.getFollows());
        mFollowing.setText("粉丝 : " + user.getFollowing());
        hideLoading();
    }

    @Override
    public void allowSayHello(boolean isAllow) {
//        mSayHello.setVisibility(isAllow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setFollowStatus(boolean isFollow) {
        this.isFollow = isFollow;
        if (isFollow) {
            mFollow.setText("已关注");
            mFollow.setBackgroundColor(0x7f040027);
        } else {
            mFollow.setText("关注");
            mFollow.setBackgroundColor(0x7f040063);
        }
    }


    @Override
    public void showError(int str) {
        mEmptyView.triggerNetError();
    }

    @Override
    protected PersonalContract.Presenter initPresenter() {
        return new PersonalPresenter(this);
    }
}
