package com.qingge.yangsong.qingge.fragments.main;

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
import com.qingge.yangsong.qingge.activity.SendPostActivity;
import com.qingge.yangsong.qingge.fragments.School.SchoolFragment;


import net.qiujuer.genius.ui.widget.FloatActionButton;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class CommunityFragment extends PresenterFragment<CommunityContract.Presenter>
        implements CommunityContract.View {
    @BindView(R.id.tv_range)
    TextView mRange;
    //    @BindView(R.id.tab_layout)
//    TabLayout mTabLayout;
//    @BindView(R.id.iv_more)
//    ImageView mMore;
    @BindView(R.id.iv_portrait)
    PortraitView mPortrait;
    @BindView(R.id.iv_menu)
    ImageView mSearch;
    @BindView(R.id.write_post)
    FloatActionButton mWritePost;
//    @BindView(R.id.viewpager)
//    ViewPager mViewPager;

//    //返回的学校名字
//    private List<String> schoolName = new ArrayList<>();
//
//    //返回的学校id
//    private List<Fragment> mFragments = new ArrayList<>();

    //    private static boolean isFirst = true;
//    @OnClick(R.id.iv_more)
//    public void removeAllTabs(){
//        Application.showToast("点击more");
//    }
    private static String mSchoolId;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_message;
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
            Application.showToast("请先登陆");
        }

//        mPortrait.setOnClickListener(v -> {      考虑前期学校不多   就不整其它范围了
//            BubbleLayout layout = new BubbleLayout(Objects.requireNonNull(getActivity()));
//            layout.setLookLength(25);
//            layout.setLookWidth(36);

//            final CustomOperateDialog codDialog = new CustomOperateDialog(getActivity())
//                    .setPosition(BubbleDialog.Position.BOTTOM)
//                    .setBubbleLayout(layout)
//                    .setClickedView(mPortrait);
//            codDialog.setClickListener(str -> {
//
//                switch (str) {
//                    case "我的大学":
//                    case "市内大学":
//                    case "省内大学":
//                        mPresenter.rangeSwitching("我的大学");
//                        break;

//                }
//                mRange.setText(str);
//                codDialog.cancel();
//            });
//            codDialog.show();
//        });
    }

//    @Override
////    protected void initData() {
////        super.initData();
////        //初始化数据
////        if (isFirst)//新进入时进行第一次加载,
////            mPresenter.rangeSwitching(mRange.getText().toString());
////        isFirst = false;
////    }

    @Override
    protected CommunityContract.Presenter initPresenter() {

        return new CommunityPresenter(this);
    }

    //切换完成后返回的关于当前范围的学校的信息
    @Override
    public void schoolInfo(List<SchoolInfoModel> models) {
//        mSchoolId = models.get(0).getSchoolId();
//        assert getFragmentManager() != null;
//        getFragmentManager().beginTransaction()
//                .add(R.id.container, new SchoolFragment())
//                .commit();
//        mRange.setText(models.get(0).getSchoolName());
//        //TODO  后面把这个SchoolInfoModel改一下  把头像也返回来  这儿就先用自己头像
//        mPortrait.setup(Glide.with(CommunityFragment.this), Account.getUser());

//        mFragments.removeAll(mFragments);
//        schoolName.removeAll(schoolName);
        //遍历到集合并创建tab
//        for (SchoolInfoModel model : models) {
//            schoolName.add(model.getSchoolName());
//            mTabLayout.addTab(mTabLayout.newTab().setText(model.getSchoolName()));
//            //TODO 实例化的时候传参这儿看以后需不需要改一下
//            mFragments.add(new SchoolFragment(model.getSchoolId()));
//        }
//
//        MyViewPagerAdapter adapter = new MyViewPagerAdapter(Objects.requireNonNull(getActivity()).getSupportFragmentManager(),
//                mFragments,
//                schoolName);
//        mViewPager.setAdapter(adapter);
//        //关联
//        mTabLayout.setupWithViewPager(mViewPager);
    }

    public static String getSchoolId() {
        return mSchoolId;
    }

    @OnClick(R.id.write_post)
    public void writePost(){
        SendPostActivity.show(getActivity());
    }
}
