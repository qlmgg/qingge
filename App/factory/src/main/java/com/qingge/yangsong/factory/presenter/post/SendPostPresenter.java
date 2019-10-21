package com.qingge.yangsong.factory.presenter.post;

import android.text.TextUtils;

import com.qingge.yangsong.factory.Factory;
import com.qingge.yangsong.factory.R;
import com.qingge.yangsong.factory.data.DataSource;
import com.qingge.yangsong.factory.data.helper.PostHelper;
import com.qingge.yangsong.factory.model.db.Post;
import com.qingge.yangsong.factory.model.post.CreatePostModel;
import com.qingge.yangsong.factory.net.UploadHelper;
import com.qingge.yangsong.factory.presenter.Account;
import com.qingge.yangsong.factory.presenter.BasePresenter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by White paper on 2019/10/20
 * Describe :
 */
public class SendPostPresenter extends BasePresenter<SendPostContract.View>
        implements SendPostContract.Presenter, DataSource.Callback<Post> {
    public SendPostPresenter(SendPostContract.View view) {
        super(view);
    }

    @Override
    public void send(String content, List<String> picturePaths) {
        start();
        SendPostContract.View view = getView();
        //上传图片
        Factory.runOnAsync(() -> {
            List<String> uploadedPath = new ArrayList<>();
            for (String path : picturePaths) {
                String url = UploadHelper.uploadImage(path);
                if (TextUtils.isEmpty(url)) {
                    Run.onUiAsync(() -> {
                        // 上传失败
                        view.showError(R.string.data_upload_error);
                    });
                } else {
                    uploadedPath.add(url);
                }
            }

            CreatePostModel model = new CreatePostModel(content, Account.getUserId(), Account.getUser().getName()
                    , Account.getUser().getPortrait(), Account.getUser().getSchoolId(), uploadedPath);
            //发送
            PostHelper.sendPost(model, this);
        });


    }

    @Override
    public void onDataLoaded(Post post) {
        Run.onUiSync(() -> {
            SendPostContract.View view = getView();
            if (view!=null)
                view.success();
        });
    }

    @Override
    public void onDataNotAvailable(int strRes) {
        Run.onUiSync(() -> {
            SendPostContract.View view = getView();
            if (view!=null)
                view.showError(strRes);
        });
    }
}
