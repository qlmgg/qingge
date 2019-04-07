package com.qingge.yangsong.factory.data.helper;

import com.qingge.yangsong.factory.Factory;
import com.qingge.yangsong.factory.data.DataSource;
import com.qingge.yangsong.factory.model.RspModel;

import com.qingge.yangsong.factory.model.card.LoadPostCard;
import com.qingge.yangsong.factory.model.card.PostCard;
import com.qingge.yangsong.factory.model.card.UniversityCard;
import com.qingge.yangsong.factory.model.db.Post;
import com.qingge.yangsong.factory.model.db.University;
import com.qingge.yangsong.factory.model.db.University_Table;
import com.qingge.yangsong.factory.net.Network;
import com.qingge.yangsong.factory.net.RemoteService;
import com.qingge.yangsong.factory.presenter.school.SchoolContract;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SchoolHelper {
    //根据学校id查询帖子
    //这儿没有用call  当刷新数据时 拿到新的数据进行对
    public static void loadingPost(String id, int page, final SchoolContract.View view) {
        RemoteService service = Network.remote();
//        Call<RspModel<List<PostCard>>> call = service.getPostsByPage(id,page);
        Call<RspModel<LoadPostCard>> call = service.getPostsByPage(id, page);
        call.enqueue(new Callback<RspModel<LoadPostCard>>() {
            @Override
            public void onResponse(Call<RspModel<LoadPostCard>> call, Response<RspModel<LoadPostCard>> response) {
                RspModel<LoadPostCard> rspModel = response.body();
                if (rspModel.success()) {
                    LoadPostCard loadPostCard = rspModel.getResult();
                    if (view != null && loadPostCard.getPageCount() >= 0)
                        view.loadingResult(loadPostCard.getPageCount());//每次刷新后就把总页数返回回去

                    List<PostCard> cards = loadPostCard.getCards();
                    if (cards == null || cards.size() == 0)
                        return;
                    PostCard[] cards1 = cards.toArray(new PostCard[0]);
//                    //进行储存并分发
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
//        call.enqueue(new Callback<RspModel<List<PostCard>>>() {
//            @Override
//            public void onResponse(Call<RspModel<List<PostCard>>> call, Response<RspModel<List<PostCard>>> response) {
//
//                RspModel<List<PostCard>> rspModel = response.body();
//                if (rspModel.success()) {
//                    List<PostCard> cards = rspModel.getResult();
//                    if (cards == null || cards.size() == 0)
//                        return;
//                    PostCard[] cards1 = cards.toArray(new PostCard[0]);
//                    //进行储存并分发
//                    Factory.getPostCenter().dispatch(cards1);
//
//                } else {
//                    Factory.decodeRspCode(rspModel, null);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<RspModel<List<PostCard>>> call, Throwable t) {
//                //TODO    失败的处理
//            }
//        });
    }

    //根据学校id查询学校  远程
    private static University findUniversityFormNet(String id) {
        RemoteService service = Network.remote();
        try {
            Response<RspModel<UniversityCard>> response = service.findUniversity(id).execute();
            UniversityCard card = response.body().getResult();
            //保存的操作
            Factory.getSchoolCenter().dispatch(card);

            return card.build();
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
        return null;
    }

    //根据学校id查询学校  从本地
    private static University findUniversityFormLocal(String id) {
        return SQLite.select()
                .from(University.class)
                .where(University_Table.id.eq(id))
                .querySingle();

    }

    //根据学校id查询学校   先本地,没有的话在远程
    public static University findUniversity(final String id) {
        final University[] university = {findUniversityFormLocal(id)};
        if (university[0] == null) { //本地无  走远程
            Factory.runOnAsync(new Runnable() {
                @Override
                public void run() {
                    University university1 = findUniversityFormNet(id);
                    if (university1 != null)
                        university[0] = university1;
                    else
                        university[0] = null;
                }
            });
            return university[0];
        }
        return university[0];
    }
}
