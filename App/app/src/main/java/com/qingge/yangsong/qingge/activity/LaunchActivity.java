package com.qingge.yangsong.qingge.activity;

import com.qingge.yangsong.common.app.Activity;
import com.qingge.yangsong.qingge.R;

public class LaunchActivity extends Activity {

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        MainActivity.show(this);
        finish();
    }
}
