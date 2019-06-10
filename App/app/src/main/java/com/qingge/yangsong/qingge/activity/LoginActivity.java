package com.qingge.yangsong.qingge.activity;

import android.content.Context;
import android.content.Intent;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.qingge.yangsong.common.app.PresenterActivity;
import com.qingge.yangsong.factory.data.helper.UserHelper;
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

    public static void show(Context context, initMyData initMyData) {
        mInitData = initMyData;
        context.startActivity(new Intent(context, LoginActivity.class));

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_login;
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
        //登陆成功后就进行我关注的人的刷新,并保存到本地
        UserHelper.refreshContacts();
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
    public interface initMyData {
        void init();
    }
}
