package com.qingge.yangsong.qingge.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.qingge.yangsong.common.app.Application;
import com.qingge.yangsong.common.app.ToolbarActivity;
import com.qingge.yangsong.common.widget.PortraitView;
import com.qingge.yangsong.qingge.R;

import java.io.File;

import butterknife.OnClick;

public class GroupCreateActivity extends ToolbarActivity {
private PortraitView mPortrait;
private EditText mGroupName;
private EditText mGroupDesc;
private RecyclerView recyclerView;
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
        setTitle("创建");
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

            Application.showToast("点击了创建");
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.portrait)
    void onPortraitClick() {
    }
}
