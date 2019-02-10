package com.qingge.yangsong.factory.net;

import com.qingge.yangsong.factory.model.RspModel;
import com.qingge.yangsong.factory.model.account.AccountRspModel;
import com.qingge.yangsong.factory.model.account.LoginModel;
import com.qingge.yangsong.factory.model.account.RegisterModel;
import com.qingge.yangsong.factory.model.card.MessageCard;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * 网络请求的所有的接口
 *
 * @version 1.0.0
 */
public interface RemoteService {

    /**
     * 注册接口
     *
     * @param model 传入的是RegisterModel
     * @return 返回的是RspModel<AccountRspModel>
     */
    @POST("account/register")
    Call<RspModel<AccountRspModel>> accountRegister(@Body RegisterModel model);

    /**
     * 登录接口
     *
     * @param model LoginModel
     * @return RspModel<AccountRspModel>
     */
    @POST("account/login")
    Call<RspModel<AccountRspModel>> accountLogin(@Body LoginModel model);

    /**
     * 绑定设备Id
     *
     * @param pushId 设备Id
     * @return RspModel<AccountRspModel>
     */
    @POST("account/bind/{pushId}")
    Call<RspModel<AccountRspModel>> accountBind(@Path(encoded = true, value = "pushId") String pushId);

    // 拉取消息接口
//    @PUT("user")
//    Call<RspModel<List<MessageCard>>> userUpdate(@Path() UserUpdateModel model);

//    //用户搜索接口
//    @GET("user/search/{name}")
//    Call<RspModel<List<UserCard>>> userSearch(@Path("name") String name);
//
//    //用户关注接口
//    @PUT("user/follow/{userId}")
//    Call<RspModel<UserCard>> userFollw(@Path("userId") String userId);
//
//    //刷新联系人
//    @GET("user/contact")
//    Call<RspModel<List<UserCard>>> userContacts();
//
//    //获取某人详细信息
//    @GET("user/{userId}")
//    Call<RspModel<UserCard>> userFind(@Path("userId") String userId);
}
