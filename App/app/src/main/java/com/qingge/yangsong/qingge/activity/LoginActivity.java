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
import android.widget.Toast;

import com.qingge.yangsong.common.app.PresenterActivity;
import com.qingge.yangsong.factory.presenter.account.LoginContract;
import com.qingge.yangsong.factory.presenter.account.LoginPresenter;
import com.qingge.yangsong.qingge.R;

import net.qiujuer.genius.ui.widget.Loading;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends PresenterActivity<LoginContract.Presenter>
        implements LoginContract.View {

    @BindView(R.id.account)
    EditText mAccount;
    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.loading)
    Loading mLoading;

    @BindView(R.id.btn_login)
    Button mButton;

    private static initMyData mInitData;

    public static void show(Context context , initMyData initMyData) {
        mInitData = initMyData;
        context.startActivity(new Intent(context, LoginActivity.class));

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initWindows() {
        super.initWindows();
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

    @OnClick(R.id.btn_login)
    void onSubmitClick() {
        String phone = mAccount.getText().toString();
        String password = mPassword.getText().toString();

        //登陆
        mPresenter.login(phone, password);
    }

    @OnClick(R.id.tv_register)
    public void startRegister() {
        RegisterActivity.show(this);
    }

    //登陆成功后的调用
    @Override
    public void loginSuccess() {
        mInitData.init();
        finish();
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
        mButton.setEnabled(true);
    }

    @Override
    protected LoginContract.Presenter initPresenter() {

        return new LoginPresenter(this);
    }

    @Override
    public void showLoading() {
        super.showLoading();
        // 开始Loading
        mLoading.start();
        //停用控件
        mAccount.setEnabled(false);
        mPassword.setEnabled(false);
        mButton.setEnabled(false);

    }

    //让my界面加载数据
    public interface initMyData{
        void init();
    }
}
