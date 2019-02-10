package com.qingge.yangsong.common.app;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import butterknife.ButterKnife;

public abstract class Activity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在界面未初始化之前初始化窗口
        initWindows();
        if (initArgs(getIntent().getExtras())){
            int layoutId = getContentLayoutId();
            setContentView(layoutId);
            initWidget();
            initData();
        }

    }
    //初始化窗口
    protected void initWindows(){}
    //初始化相关参数
    protected boolean initArgs(Bundle bundle){
        return true;
    }
    //从实现类拿到id
    protected abstract int getContentLayoutId();
    //初始化数据
    protected void initData(){}
    //初始化控件
    protected void initWidget(){
        //直接是activity中的view  ,不用像fragment那样需要view
        ButterKnife.bind(this);
    }
    //点击导航栏的返回键
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
    //点击实体键的返回键

    @Override
    public void onBackPressed() {
        //得到当前activity下的所以fragment
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments.size() > 0) {
            for (Fragment fragment : fragments) {
                if (fragment instanceof com.qingge.yangsong.common.app.Fragment){
                    //判断是否拦截了返回按钮   就是实现...recycler.Fragment类的fragment是否重写了onBackPressed方法
                    //重写了就执行其方法体然后返回true,这儿收到就直接return
                    if (((com.qingge.yangsong.common.app.Fragment)fragment).onBackPressed()){
                        return;
                    }
                }
            }
        }
        //TODO   测试把super放第一行
        super.onBackPressed();
        finish();

    }
}
