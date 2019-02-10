package com.yangsong.web.qingge.push.service;

import com.google.common.base.Strings;
import com.yangsong.web.qingge.push.bean.api.account.AccountRspModel;
import com.yangsong.web.qingge.push.bean.base.LoginModel;
import com.yangsong.web.qingge.push.bean.base.RegisterModel;
import com.yangsong.web.qingge.push.bean.base.ResponseModel;
import com.yangsong.web.qingge.push.bean.db.User;
import com.yangsong.web.qingge.push.factory.UserFactory;

import javax.jws.soap.SOAPBinding;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/account")
public class AccountService {
    @GET
    @Path("/test")
    public String test() {
        return "测试成功";
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<AccountRspModel> register(RegisterModel model) {
        if (!RegisterModel.check(model)) {
            return ResponseModel.buildParameterError();
        }
        //先进行数据库查找此账号是否注册
        User user = UserFactory.findByPhone(model.getPhone().trim());
        if (user != null) {
            return ResponseModel.buildHaveAccountError();
        }
        //检查此名字是否存在
        user = UserFactory.findByName(model.getName().trim());
        if (user != null) {
            return ResponseModel.buildHaveNameError();
        }
        //注册操作   完成得到一个用户
        user = UserFactory.register(model.getPhone(), model.getPassword(), model.getName());

        if (user != null) {
            if (!Strings.isNullOrEmpty(model.getPushId())) {
                //进行绑定推送id
                return bind(user, model.getPushId());
            }
            //没有pushId的话就不绑定了,
            AccountRspModel accountRspModel = new AccountRspModel(user);
            return ResponseModel.buildOk(accountRspModel);
        } else
            return ResponseModel.buildRegisterError();
    }

    /**
     *绑定操作
     */
    private ResponseModel<AccountRspModel> bind(User self, String pushId) {
        User user = UserFactory.bindPushId(self, pushId);
        if (user != null)
            return ResponseModel.buildOk(new AccountRspModel(user, true));
        else
            return ResponseModel.buildServiceError();
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<AccountRspModel> login(LoginModel model) {
        //检查参数
        if (!LoginModel.check(model)) {
            return ResponseModel.buildParameterError();
        }
        User user = UserFactory.login(model.getPhone(), model.getPassword());
        //是否登陆成功
        if (user != null) {
            if (user.getPushId() != null) {
                //登陆后绑定设备id
                return bind(user, user.getPushId());
            }
            AccountRspModel accountRspModel = new AccountRspModel(user);
            return ResponseModel.buildOk(accountRspModel);

        }else
            return ResponseModel.buildLoginError(); //登陆错误
    }
}
