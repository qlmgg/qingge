package com.qingge.yangsong.qingge.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.qingge.yangsong.common.app.Activity;
import com.qingge.yangsong.common.app.PresenterActivity;
import com.qingge.yangsong.factory.presenter.account.RegisterContract;
import com.qingge.yangsong.factory.presenter.account.RegisterPresenter;
import com.qingge.yangsong.qingge.R;

import net.qiujuer.genius.ui.widget.Loading;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterActivity extends PresenterActivity<RegisterContract.Presenter>
        implements RegisterContract.View {
    @BindView(R.id.account)
    EditText mAccount;
    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.name)
    EditText mName;
    @BindView(R.id.btn_register)
    Button mRegister;
    @BindView(R.id.school_name)
    EditText mSchoolName;
    @BindView(R.id.loading2)
    Loading mLoading;

    public static void show(Context context) {
        context.startActivity(new Intent(context, RegisterActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        //设置状态栏全透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }


    }

    @OnClick(R.id.btn_register)
    public void register() {
        mPresenter.register(mAccount.getText().toString(), mPassword.getText().toString(), mName.getText().toString(),mSchoolName.getText().toString());
    }

    @Override
    public void showError(int str) {
        super.showError(str);
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
        // 停止Loading
        mLoading.stop();
        // 让控件可以输入
        mAccount.setEnabled(true);
        mPassword.setEnabled(true);
        // 提交按钮可以继续点击
        mRegister.setEnabled(true);
    }

    @Override
    protected void initPresenter() {
        new RegisterPresenter(this);
    }

    @Override
    public void registerSuccess() {
        finish();
    }

    @Override
    public void showLoading() {
        super.showLoading();
        mLoading.start();
        mAccount.setEnabled(false);
        mPassword.setEnabled(false);
        mRegister.setEnabled(false);
    }
}
