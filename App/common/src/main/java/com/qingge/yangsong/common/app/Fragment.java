package com.qingge.yangsong.common.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qingge.yangsong.common.widget.convention.PlaceHolderView;

import butterknife.ButterKnife;

public abstract class Fragment extends android.support.v4.app.Fragment {
    protected View mRoot;
    protected PlaceHolderView mPlaceHolderView;
    protected Activity mActivity;
    protected Bundle mSaveState;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        initWindows();
        //初始化参数
        initArgs(getArguments());
        this.mActivity = (Activity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mSaveState = savedInstanceState;
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRoot == null) {
            int layoutId = getContentLayoutId();
            //这儿参数2是用于存储view的容器,参数三是问要不要在创建view的时候就添加到父容器中,这
            //儿选择false,当在结束逻辑return  View时,会自己添加
            View root = inflater.inflate(layoutId, container, false);
            initWidget(root);
            mRoot = root;
        } else {
            // view已经创建过了   每次切换时不会销毁fragment,但会把旧的view从父布局中除掉
            // 因为每一个View只能拥有一个Parent，如果不移除，将会重复加载而导致程序崩溃。
            if (mRoot.getParent() != null) {
                ((ViewGroup) mRoot.getParent()).removeView(mRoot);//有就清除,因为return时会把此mRoot存到父容器中,避免重复
            }
        }

        return mRoot;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("TABB","c测试执行" + getClass().getName());
        initData();
    }


    //从实现类拿到布局id
    protected abstract int getContentLayoutId();

    //初始化窗口
    protected void initWindows() {
    }

    //初始化数据
    protected void initData() {
    }

    //初始化控件
    protected void initWidget(View root) {
        //当在某个类实现这个方法时,这个类中的使用@BindView注解会与root进行绑定
        ButterKnife.bind(this, root);
    }

    //初始化相关参数
    protected void initArgs(Bundle bundle) {
    }

    //返回按键  返回true代表fragment自己处理返回逻辑,不用activity自己finish,false就让activity自己处理
    public boolean onBackPressed() {
        return false;
    }

    /**
     * 设置占位布局
     *
     * @param placeHolderView 继承了占位布局规范的view
     */
    public void setPlaceHolderView(PlaceHolderView placeHolderView) {
        this.mPlaceHolderView = placeHolderView;
    }

}
