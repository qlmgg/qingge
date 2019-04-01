package com.qingge.yangsong.qingge.fragments.main;

import android.view.View;
import android.widget.EditText;

import com.qingge.yangsong.common.app.Application;
import com.qingge.yangsong.qingge.R;

import butterknife.BindView;
import butterknife.OnClick;

public class CateringFragment extends com.qingge.yangsong.common.app.Fragment {

    @BindView(R.id.search_edit_shop)
    EditText mSearchShop;

    public CateringFragment() {

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_catering;
    }

    @OnClick(R.id.search_edit_shop)
    public void search(){
        Application.showToast("测试点击事件");
    }

}
