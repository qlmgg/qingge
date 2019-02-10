package com.qingge.post.service;


import com.google.common.base.Strings;
import com.qingge.post.bean.api.account.AccountRspModel;
import com.qingge.post.bean.api.account.LoginModel;
import com.qingge.post.bean.api.account.RegisterModel;
import com.qingge.post.bean.base.ResponseModel;
import com.qingge.post.bean.db.User;
import com.qingge.post.factory.UserFactory;

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
                        System.out.print("测试是否进入这儿");
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

        // 绑定设备Id
        @POST
        @Path("/bind/{pushId}")
        // 指定请求与返回的相应体为JSON
        @Consumes(MediaType.APPLICATION_JSON)
        @Produces(MediaType.APPLICATION_JSON)
        // 从请求头中获取token字段
        // pushId从url地址中获取
        public ResponseModel<AccountRspModel> bind(@HeaderParam("token") String token,
                                                   @PathParam("pushId") String pushId) {
                if (Strings.isNullOrEmpty(token) ||
                        Strings.isNullOrEmpty(pushId)) {
                        // 返回参数异常
                        return ResponseModel.buildParameterError();
                }

                // 拿到自己的个人信息
                 User user = UserFactory.findByToken(token);
//                User self = getSelf();
                return bind(user, pushId);
        }

}
