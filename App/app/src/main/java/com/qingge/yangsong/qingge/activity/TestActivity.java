package com.qingge.yangsong.qingge.activity;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.qingge.yangsong.common.app.Activity;
import com.qingge.yangsong.common.app.Application;
import com.qingge.yangsong.factory.presenter.group.GroupCreateContract;
import com.qingge.yangsong.qingge.R;

import butterknife.BindView;

public class TestActivity extends Activity {
@BindView(R.id.toolbar)
    Toolbar mToolbar;
    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_test;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(v -> finish());

        // sample code snippet to set the text content on the ExpandableTextView
        ExpandableTextView expTv1 =
                findViewById(R.id.expand_text_view);

// IMPORTANT - call setText on the ExpandableTextView to set the text content to display
        expTv1.setText(getString(R.string.dummy_text1));

        ExpandableTextView expTv2 =findViewById(R.id.test1).
                findViewById(R.id.expand_text_view);
        expTv2.setText(getString(R.string.dummy_text1));

        ExpandableTextView expTv3 =findViewById(R.id.test2).
                findViewById(R.id.expand_text_view);
        expTv3.setText(getString(R.string.dummy_text1));
    }

}
