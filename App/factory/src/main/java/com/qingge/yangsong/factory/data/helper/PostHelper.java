package com.qingge.yangsong.factory.data.helper;

import com.qingge.yangsong.common.app.Application;
import com.qingge.yangsong.factory.Factory;
import com.qingge.yangsong.factory.R;
import com.qingge.yangsong.factory.data.DataSource;
import com.qingge.yangsong.factory.model.RspModel;
import com.qingge.yangsong.factory.model.card.CommentCard;
import com.qingge.yangsong.factory.model.comment.CommentModel;
import com.qingge.yangsong.factory.net.Network;
import com.qingge.yangsong.factory.net.RemoteService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostHelper {
    //加载评论
    public static void loadComment(String postId, final DataSource.Callback<List<CommentCard>> callback) {
        RemoteService service = Network.remote();
        service.loadComments(postId)
                .enqueue(new Callback<RspModel<List<CommentCard>>>() {
                    @Override
                    public void onResponse(Call<RspModel<List<CommentCard>>> call, Response<RspModel<List<CommentCard>>> response) {
                        RspModel<List<CommentCard>> rspModel = response.body();
                        if (rspModel.success()) {
                            List<CommentCard> cards = rspModel.getResult();
                            callback.onDataLoaded(cards);
                        } else
                            Factory.decodeRspCode(rspModel, callback);
                    }

                    @Override
                    public void onFailure(Call<RspModel<List<CommentCard>>> call, Throwable t) {
                        callback.onDataNotAvailable(R.string.data_network_error);
                    }
                });
    }


    //写评论
    public static void writeComment(CommentModel model) {
        RemoteService service = Network.remote();
        service.writeComment(model)
                .enqueue(new Callback<RspModel<CommentCard>>() {
                    @Override
                    public void onResponse(Call<RspModel<CommentCard>> call, Response<RspModel<CommentCard>> response) {
                        RspModel<CommentCard> rspModel = response.body();
                        if (rspModel.success()) {
                            CommentCard card = rspModel.getResult();
                            if (card != null)
                                Application.showToast("评论成功!");
                        } else
                            Application.showToast("错误代码 :" + rspModel.getCode());

                    }

                    @Override
                    public void onFailure(Call<RspModel<CommentCard>> call, Throwable t) {
                        // 网络错误
                        Application.showToast(R.string.data_network_error);
                    }
                });
    }
}
