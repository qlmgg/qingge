package com.qingge.yangsong.factory.data.helper;

import com.qingge.yangsong.factory.Factory;
import com.qingge.yangsong.factory.R;
import com.qingge.yangsong.factory.data.DataSource;
import com.qingge.yangsong.factory.model.RspModel;
import com.qingge.yangsong.factory.model.SchoolInfoModel;
import com.qingge.yangsong.factory.model.account.AccountRspModel;
import com.qingge.yangsong.factory.model.card.GroupCard;
import com.qingge.yangsong.factory.model.card.LoadPostCard;
import com.qingge.yangsong.factory.model.card.PostCard;
import com.qingge.yangsong.factory.model.db.Group;
import com.qingge.yangsong.factory.model.db.Post;
import com.qingge.yangsong.factory.model.db.User;
import com.qingge.yangsong.factory.net.Network;
import com.qingge.yangsong.factory.net.RemoteService;
import com.qingge.yangsong.factory.presenter.Account;
import com.qingge.yangsong.factory.presenter.community.CommunityContract;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//社区的helper
public class CommunityHelper {

    //范围切换
    public static Call rangeSwitching(String range, final DataSource.Callback<SchoolInfoModel> callback) {
        RemoteService service = Network.remote();
        Call<RspModel<SchoolInfoModel>> call = service.schoolsInRange(range);
        call.enqueue(new Callback<RspModel<SchoolInfoModel>>() {
            @Override
            public void onResponse(Call<RspModel<SchoolInfoModel>> call, Response<RspModel<SchoolInfoModel>> response) {
                RspModel<SchoolInfoModel> rspModel = response.body();
                if (rspModel.success()) {
                    callback.onDataLoaded(rspModel.getResult());
                } else {
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<SchoolInfoModel>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
        return call;
    }

    //用id去拿学校信息(含 帖子 群)
    public static void getSchoolInfo(String schoolId, final DataSource.Callback<SchoolInfoModel> callback) {
        RemoteService service = Network.remote();
        Call<RspModel<SchoolInfoModel>> call = service.getSchoolInfo(schoolId);
        call.enqueue(new Callback<RspModel<SchoolInfoModel>>() {
            @Override
            public void onResponse(Call<RspModel<SchoolInfoModel>> call, Response<RspModel<SchoolInfoModel>> response) {
                RspModel<SchoolInfoModel> rspModel = response.body();
                if (rspModel.success()) {

                    SchoolInfoModel model = rspModel.getResult();

                    //对群进行保存
                    Factory.getGroupCenter().dispatch(model.getGroupCards().toArray(new GroupCard[0]));
                    //对帖子进行保存
                    Factory.getPostCenter().dispatch(model.getPostCards().toArray(new PostCard[0]));

                    callback.onDataLoaded(model);
                } else {
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<SchoolInfoModel>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }

    //根据学校id查询帖子
    public static void loadPostList(String id, int page, final CommunityContract.Presenter presenter) {
        RemoteService service = Network.remote();
        Call<RspModel<LoadPostCard>> call = service.getPostsByPage(id, page);
        call.enqueue(new Callback<RspModel<LoadPostCard>>() {
            @Override
            public void onResponse(Call<RspModel<LoadPostCard>> call, Response<RspModel<LoadPostCard>> response) {
                RspModel<LoadPostCard> rspModel = response.body();
                if (rspModel.success()) {
                    LoadPostCard loadPostCard = rspModel.getResult();
                    List<PostCard> cards = loadPostCard.getCards();

                    if (presenter != null && loadPostCard.getPageCount() >= 0) {
                        //返回总页数
                        presenter.getPageCount(loadPostCard.getPageCount());
                        //回调回去的数据集合
                        List<Post> posts = new ArrayList<>();
                        for (PostCard card : cards) {
                            //转换
                            posts.add(card.build());
                        }
                        //回调
                        presenter.haveLoadedPosts(posts);
                    }

                    if (cards == null || cards.size() == 0)
                        return;
                    PostCard[] cards1 = cards.toArray(new PostCard[0]);
                    //进行储存并分发
                    Factory.getPostCenter().dispatch(cards1);
                } else {
                    Factory.decodeRspCode(rspModel, null);
                }
            }

            @Override
            public void onFailure(Call<RspModel<LoadPostCard>> call, Throwable t) {
                //TODO    失败的处理
            }
        });

    }


    //拿到当前学校下的群集合
    public static void loadGroupList(String schoolId, final CommunityContract.Presenter presenter) {
        //TODO 进行优化,只需要name id pic desc  服务端对应
        RemoteService service = Network.remote();
        Call<RspModel<List<GroupCard>>> call = service.searchGroups(schoolId);
        call.enqueue(new Callback<RspModel<List<GroupCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<GroupCard>>> call, Response<RspModel<List<GroupCard>>> response) {
                RspModel<List<GroupCard>> rspModel = response.body();
                if (rspModel.success()) {
                    //要返回的集合
                    final List<Group> groups = new ArrayList<>();
                    List<GroupCard> cards = rspModel.getResult();
                    //遍历成group并添加到集合中
                    for (GroupCard card : cards) {
                        //拿到创建者
                        User owner = UserHelper.search(card.getOwnerId());
                        //添加到集合
                        groups.add(card.build(owner));
                    }
                    //回调
                    presenter.haveLoadedGroups(groups);
                    //保存
                    Factory.getGroupCenter().dispatch(cards.toArray(new GroupCard[0]));

                }
            }

            @Override
            public void onFailure(Call<RspModel<List<GroupCard>>> call, Throwable t) {
                //TODO 失败的处理
            }
        });

    }
}
