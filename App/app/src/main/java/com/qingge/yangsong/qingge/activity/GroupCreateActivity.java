package com.qingge.yangsong.qingge.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.guoxiaoxing.phoenix.core.PhoenixOption;
import com.guoxiaoxing.phoenix.core.model.MediaEntity;
import com.guoxiaoxing.phoenix.core.model.MimeType;
import com.guoxiaoxing.phoenix.picker.Phoenix;
import com.qingge.yangsong.common.app.Application;
import com.qingge.yangsong.common.app.PresenterToolbarActivity;
import com.qingge.yangsong.common.app.ToolbarActivity;
import com.qingge.yangsong.common.widget.PortraitView;
import com.qingge.yangsong.common.widget.recycler.RecyclerAdapter;
import com.qingge.yangsong.factory.net.UploadHelper;
import com.qingge.yangsong.factory.presenter.BaseContract;
import com.qingge.yangsong.factory.presenter.group.GroupCreateContract;
import com.qingge.yangsong.factory.presenter.group.GroupCreatePresenter;
import com.qingge.yangsong.qingge.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class GroupCreateActivity extends PresenterToolbarActivity<GroupCreateContract.Presenter>
        implements GroupCreateContract.View {
    private static final int REQUEST_CODE = 1001;

    @BindView(R.id.portrait)
    PortraitView mPortrait;
    @BindView(R.id.et_group_name)
    EditText mGroupName;
    @BindView(R.id.et_group_desc)
    EditText mGroupDesc;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    private Adapter mAdapter;
    private String mPortraitPath = "";
    List<MediaEntity> lists = new ArrayList<>();
    public static void show(Context context) {
        context.startActivity(new Intent(context, GroupCreateActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_group_create;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        setTitle("??????");
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setAdapter(mAdapter = new Adapter());
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_group_finish, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.create_group_finish) {
            String name = mGroupName.getText().toString().trim();
            String desc = mGroupDesc.getText().toString().trim();
            mPresenter.create(name,desc,mPortraitPath);
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.portrait)
    void onPortraitClick() {
        Phoenix.with()
                .theme(PhoenixOption.THEME_DEFAULT)// ??????
                .fileType(MimeType.ofAll())//??????????????????????????????????????????????????????
                .maxPickNumber(4)// ??????????????????
                .minPickNumber(1)// ??????????????????
                .spanCount(4)// ??????????????????
                .enablePreview(true)// ??????????????????
                .enableCamera(true)// ??????????????????
                .enableAnimation(true)// ??????????????????????????????
                .enableCompress(false)// ??????????????????
                .compressPictureFilterSize(1024)//??????kb????????????????????????
                .compressVideoFilterSize(2018)//??????kb????????????????????????
                .thumbnailHeight(160)// ????????????????????????
                .thumbnailWidth(160)// ????????????????????????
                .enableClickSound(false)// ????????????????????????
                .pickedMediaList(lists)// ??????????????????
                .videoFilterTime(0)//??????????????????????????????
                .mediaFilterSize(10000)//????????????kb???????????????/??????????????????0??????????????????
                //????????????Activity???????????????Activity???????????????Fragment???????????????Fragment
                .start(GroupCreateActivity.this, PhoenixOption.TYPE_PICK_MEDIA, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            //???????????????
            List<MediaEntity> result = Phoenix.result(data);
//            mMediaAdapter.setData(result);
            if (result == null)
                return;
            mPortraitPath = result.get(0).getLocalPath();
            Glide.with(this)
                    .load(mPortraitPath)
                    .asBitmap()
                    .centerCrop()
                    .into(mPortrait);
        }
    }


    @Override
    protected GroupCreateContract.Presenter initPresenter() {
        return new GroupCreatePresenter(this);
    }

    //????????????
    @Override
    public void onCreateSucceed() {
        //TODO ???????????????????????????load,??????????????????  ?????????????????????

        hideLoading();
        finish();
    }

    @Override
    public RecyclerAdapter<GroupCreateContract.ViewModel> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {
        hideLoading();
    }


    class Adapter extends RecyclerAdapter<GroupCreateContract.ViewModel> {

        @Override
        protected int getItemViewType(int position, GroupCreateContract.ViewModel viewModel) {
            return R.layout.cell_group_create_contact;
        }

        @Override
        protected ViewHolder<GroupCreateContract.ViewModel> onCreateViewHolder(View view, int viewType) {
            return new GroupCreateActivity.ViewHolder(view);
        }
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<GroupCreateContract.ViewModel> {
        @BindView(R.id.im_portrait)
        PortraitView mPortrait;
        @BindView(R.id.txt_name)
        TextView mName;
        @BindView(R.id.cb_select)
        CheckBox mSelect;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @OnCheckedChanged(R.id.cb_select)
        void onCheckedChanged(boolean checked) {
            mPresenter.changeSelect(mData, checked);
        }

        @Override
        protected void onBind(GroupCreateContract.ViewModel viewModel) {
            mPortrait.setup(Glide.with(GroupCreateActivity.this), viewModel.author);
            mName.setText(viewModel.author.getName());
            mSelect.setChecked(viewModel.isSelected);
        }
    }
}
