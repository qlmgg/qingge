package com.qingge.yangsong.factory.net;

import com.qingge.yangsong.factory.model.RspModel;
import com.qingge.yangsong.factory.model.SchoolInfoModel;
import com.qingge.yangsong.factory.model.account.AccountRspModel;
import com.qingge.yangsong.factory.model.account.LoginModel;
import com.qingge.yangsong.factory.model.account.RegisterModel;
import com.qingge.yangsong.factory.model.card.CommentCard;
import com.qingge.yangsong.factory.model.card.GroupCard;
import com.qingge.yangsong.factory.model.card.LoadPostCard;
import com.qingge.yangsong.factory.model.card.MessageCard;
import com.qingge.yangsong.factory.model.card.PostCard;

import com.qingge.yangsong.factory.model.card.UniversityCard;
import com.qingge.yangsong.factory.model.card.UserCard;
import com.qingge.yangsong.factory.model.comment.CommentModel;
import com.qingge.yangsong.factory.model.db.University;
import com.qingge.yangsong.factory.model.group.GroupCreateModel;
import com.qingge.yangsong.factory.model.message.MsgCreateModel;
import com.qingge.yangsong.factory.model.post.CreatePostModel;


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

    @GET("university/query/{range}")
    Call<RspModel<SchoolInfoModel>> schoolsInRange(@Path("range") String range);

    //用id拿到学校的帖子,群  完善用户信息的时候保存所选学校信息到本地
    @GET("university/query/{schoolId}")
    Call<RspModel<SchoolInfoModel>> getSchoolInfo(@Path("schoolId") String schoolId);
    /**
     * 根据学校id拿帖子
     */
    @GET("post/{id}")
    Call<RspModel<List<PostCard>>> getPosts(@Path("id") String id);

    /**
     * 根据学校id和页数拿帖子
     * 得到总页数和帖子集合
     */
    @GET("post/{id}|{page}")
    Call<RspModel<LoadPostCard>> getPostsByPage(@Path("id") String id, @Path("page") int page);

    /**
     * 根据id拿学校
     */
    @GET("university/{id}")
    Call<RspModel<UniversityCard>> findUniversity(@Path("id") String id);

    // 拉取消息接口
//    @PUT("user")
//    Call<RspModel<List<MessageCard>>> userUpdate(@Path() UserUpdateModel model);

    //    //用户搜索接口
//    @GET("user/search/{name}")
//    Call<RspModel<List<UserCard>>> userSearch(@Path("name") String name);
//
//    //用户关注接口
    @PUT("user/follow/{userId}")
    Call<RspModel<UserCard>> userFollow(@Path("userId") String userId);

    //取消用户关注接口
    @PUT("user/un_follow/{userId}")
    Call<RspModel<UserCard>> userCancelFollow(@Path("userId") String userId);


    //刷新联系人
    @GET("user/contact")
    Call<RspModel<List<UserCard>>> userContacts();
//   用name搜人
    @GET("user/search/{name}")
    Call<RspModel<List<UserCard>>> searchByName(@Path("name") String name);
//   用帖子名字搜帖子
    @GET("user/search/post/{name}")
    Call<RspModel<List<PostCard>>> searchPostByName(@Path("name") String name);
    
    //获取某人详细信息
    @GET("user/{userId}")
    Call<RspModel<UserCard>> userFind(@Path("userId") String userId);

    // 发送消息的接口
    @POST("msg")
    Call<RspModel<MessageCard>> msgPush(@Body MsgCreateModel model);

    //搜素群列表
    @GET("group/search/{schoolId}")
    Call<RspModel<List<GroupCard>>> searchGroups(@Path("schoolId") String schoolId);

    //创建群
    @POST("group")
    Call<RspModel<GroupCard>> groupCreate(@Body GroupCreateModel model);

    @GET("post/comment/{postId}")
    Call<RspModel<List<CommentCard>>> loadComments(@Path("postId") String postId);

    @PUT("post/comment/write")
    Call<RspModel<CommentCard>> writeComment(@Body CommentModel model);

    @PUT("post/write")
    Call<RspModel<PostCard>> sendPost(@Body CreatePostModel model);

}
