package com.qingge.yangsong.qingge.fragments.main;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingge.yangsong.common.app.Application;
import com.qingge.yangsong.common.app.PresenterFragment;
import com.qingge.yangsong.common.widget.PortraitView;
import com.qingge.yangsong.factory.data.helper.SchoolHelper;
import com.qingge.yangsong.factory.model.SchoolInfoModel;
import com.qingge.yangsong.factory.model.db.University;
import com.qingge.yangsong.factory.presenter.Account;
import com.qingge.yangsong.factory.presenter.community.CommunityContract;
import com.qingge.yangsong.factory.presenter.community.CommunityPresenter;

import com.qingge.yangsong.qingge.R;
import com.qingge.yangsong.qingge.activity.GroupCreateActivity;
import com.qingge.yangsong.qingge.activity.LaunchActivity;
import com.qingge.yangsong.qingge.activity.SendPostActivity;
import com.qingge.yangsong.qingge.activity.TestActivity;
import com.qingge.yangsong.qingge.fragments.school.SchoolFragment;


import net.qiujuer.genius.ui.widget.FloatActionButton;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class CommunityFragment extends PresenterFragment<CommunityContract.Presenter>
        implements CommunityContract.View {
    @BindView(R.id.tv_range)
    TextView mRange;
    @BindView(R.id.iv_portrait)
    PortraitView mPortrait;

    @BindView(R.id.write_post)
    FloatActionButton mWritePost;

    private static String mSchoolId;
    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_community;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        if (Account.isLogin()) {
            //登陆情况下就进行页面加载和头像的显示
            //得到登陆人的学校id
            mSchoolId = Objects.requireNonNull(Account.getSelf()).getSchoolId();
            //根据学校id查询查询学校  优先本地
            University university = SchoolHelper.findUniversity(mSchoolId);
            assert university != null;
            //设置学校头像
            mPortrait.setup(Glide.with(CommunityFragment.this), university.getPicture());
            //设置学校名字
            mRange.setText(university.getName());

            assert getFragmentManager() != null;
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new SchoolFragment())
                    .commit();

//            mPresenter.rangeSwitching("我的大学");
        } else {
            //TODO 如果没登陆,就加载一个请登陆的提示页面或者...
            Application.showToast("请先登陆");
        }

    }

    @Override
    protected CommunityContract.Presenter initPresenter() {

        return new CommunityPresenter(this);
    }

    //切换完成后返回的关于当前范围的学校的信息
    @Override
    public void schoolInfo(List<SchoolInfoModel> models) {
        //TODO 这儿当点击左上角头像时切换范围加载所选范围的所有学校   当学校多起来的时候实现
    }

    public static String getSchoolId() {
        return mSchoolId;
    }

    @OnClick(R.id.write_post)
    public void writePost(){
        SendPostActivity.show(getActivity());
    }


    /**
     * 弹出框一个  可以创建群 ..
     * */
    @OnClick(R.id.iv_menu)
    public void createGroup(){
        GroupCreateActivity.show(Objects.requireNonNull(getContext()));
            //TODO  退出登陆的操作,已经实现,待应用
//        Account.signOut(getContext(),new Intent(getContext(), LaunchActivity.class));
    }
}
