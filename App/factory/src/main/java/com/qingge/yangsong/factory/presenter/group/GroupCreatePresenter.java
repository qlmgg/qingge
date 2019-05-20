package com.qingge.yangsong.factory.presenter.group;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.qingge.yangsong.common.app.Application;
import com.qingge.yangsong.factory.Factory;
import com.qingge.yangsong.factory.R;
import com.qingge.yangsong.factory.data.DataSource;
import com.qingge.yangsong.factory.data.helper.GroupHelper;
import com.qingge.yangsong.factory.data.helper.UserHelper;
import com.qingge.yangsong.factory.model.card.GroupCard;
import com.qingge.yangsong.factory.model.db.view.UserSampleModel;
import com.qingge.yangsong.factory.model.group.GroupCreateModel;
import com.qingge.yangsong.factory.net.UploadHelper;
import com.qingge.yangsong.factory.presenter.BaseRecyclerPresenter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GroupCreatePresenter extends BaseRecyclerPresenter<GroupCreateContract.ViewModel, GroupCreateContract.View>
        implements GroupCreateContract.Presenter, DataSource.Callback<GroupCard> {
    private Set<String> users = new HashSet<>();//要添加的群成员

    @Override
    public void start() {
        super.start();
        //开始加载联系人
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                List<UserSampleModel> sampleModels = UserHelper.getSampleContact();
                List<GroupCreateContract.ViewModel> models = new ArrayList<>();
                for (UserSampleModel sampleModel : sampleModels) {
                    GroupCreateContract.ViewModel viewModel = new GroupCreateContract.ViewModel();
                    viewModel.author = sampleModel;
                    models.add(viewModel);
                }

                refreshData(models);
            }
        });

    }

    public GroupCreatePresenter(GroupCreateContract.View view) {
        super(view);
    }

    @Override
    public void create(final String name, final String desc, final String picture) {
        final GroupCreateContract.View view = getView();
        view.showLoading();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(picture)) {
            view.showError(R.string.parameter_must_not_null);
            return;
        }

        // 上传图片
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                //上传到阿里云oss
                String url = UploadHelper.uploadPortrait(picture);
                if (TextUtils.isEmpty(url)) {
                    // 切换到UI线程 提示信息
                    Run.onUiAsync(new Action() {
                        @Override
                        public void call() {
                            view.showError(R.string.data_upload_error);
                        }
                    });
                }

                if (TextUtils.isEmpty(url))
                    return;
                // 进行网络请求
                GroupCreateModel model = new GroupCreateModel(name, desc, url, users);
                GroupHelper.create(model, GroupCreatePresenter.this);
            }
        });

    }


    @Override
    public void changeSelect(GroupCreateContract.ViewModel model, boolean isSelected) {
        if (isSelected)
            users.add(model.author.getId());
        else
            users.remove(model.author.getId());

    }


    @Override
    public void onDataLoaded(GroupCard groupCard) {
        getView().onCreateSucceed();
        Application.showToast(""+groupCard);
    }

    @Override
    public void onDataNotAvailable(int strRes) {
        //创建失败
        getView().showError(strRes);
    }
}
