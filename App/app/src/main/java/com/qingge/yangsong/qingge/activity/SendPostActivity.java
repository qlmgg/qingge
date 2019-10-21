package com.qingge.yangsong.qingge.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.opengl.Visibility;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.qingge.yangsong.common.app.Activity;
import com.qingge.yangsong.common.app.Application;
import com.qingge.yangsong.common.app.PresenterActivity;
import com.qingge.yangsong.factory.presenter.post.SendPostContract;
import com.qingge.yangsong.factory.presenter.post.SendPostPresenter;
import com.qingge.yangsong.qingge.R;
import com.qingge.yangsong.qingge.adapter.AlbumAdapter;
import com.qingge.yangsong.qingge.adapter.SelectPictureAdapter;
import com.qingge.yangsong.qingge.fragments.media.GalleryFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SendPostActivity extends PresenterActivity<SendPostContract.Presenter> implements
        Toolbar.OnMenuItemClickListener, SendPostContract.View {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.grid_view)
    GridView mGridView;
    @BindView(R.id.edit_post_content)
    EditText mContent;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    private SelectPictureAdapter adapter;
    private List<String> mPathList = new ArrayList<>();

    public static void show(Context context) {
        Intent intent = new Intent(context, SendPostActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void initWidget() {
        super.initWidget();
        mToolbar.setNavigationOnClickListener(v -> finish());
        mToolbar.inflateMenu(R.menu.send_post_menu);
        mToolbar.setOnMenuItemClickListener(this);

        adapter = new SelectPictureAdapter(this, mPathList);
        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener((parent, view, position, id) -> {
            if (position == adapter.getCount() - 1) {
                new GalleryFragment()
                        .setListener(paths -> {
                            if (mPathList.get(0).equals("")) {
                                mPathList.remove(0);
                            } else if (mPathList.get(mPathList.size() - 1).equals("")) {
                                mPathList.remove(mPathList.size() - 1);
                            }
                            mPathList.addAll(Arrays.asList(paths));
                            mPathList.add("");

                            adapter.notifyDataSetChanged();
                        }).show(getSupportFragmentManager(), GalleryFragment.class.getName());
            } else {
                Application.showToast("点击了图片 ：" + position);
            }
        });
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_send_post;
    }


    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        if (mContent.getText().toString().isEmpty()){
            Application.showToast("内容不能为空");
            return false;
        }
        if (mPathList.size() > 0 && mPathList.get(mPathList.size() - 1).equals(""))
            mPathList.remove(mPathList.size() - 1);

        mPresenter.send(mContent.getText().toString(), mPathList);
        showLoading();
        return false;
    }

    @Override
    protected SendPostContract.Presenter initPresenter() {
        return new SendPostPresenter(this);
    }

    @Override
    public void showLoading() {
        super.showLoading();

        mGridView.setEnabled(false);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void success() {
        mProgressBar.setVisibility(View.GONE);
        Application.showToast("发送完成");
        finish();
    }

    @Override
    public void showError(int str) {
        super.showError(str);
        mGridView.setEnabled(true);
        if (mPathList.size() > 0 && !mPathList.get(mPathList.size() - 1).equals(""))
            mPathList.add("");
        adapter.notifyDataSetChanged();
        mProgressBar.setVisibility(View.GONE);
    }
}
