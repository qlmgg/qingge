package com.qingge.yangsong.factory.presenter.contact;

import android.widget.Toast;

import com.qingge.yangsong.factory.Factory;
import com.qingge.yangsong.factory.R;
import com.qingge.yangsong.factory.data.DataSource;
import com.qingge.yangsong.factory.data.helper.UserHelper;
import com.qingge.yangsong.factory.model.card.UserCard;
import com.qingge.yangsong.factory.model.db.User;
import com.qingge.yangsong.factory.presenter.Account;
import com.qingge.yangsong.factory.presenter.BasePresenter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;


/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public class PersonalPresenter extends BasePresenter<PersonalContract.View>
        implements PersonalContract.Presenter,DataSource.Callback<UserCard> {

    private User user;

    public PersonalPresenter(PersonalContract.View view) {
        super(view);
    }


    @Override
    public void start() {
        super.start();

        // 个人界面用户数据优先从网络拉取
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                PersonalContract.View view = getView();
                if (view != null) {
                    String id = view.getUserId();
                    User user = UserHelper.findFromNet(id);
                    if (user == null) {
                        getView().showError(R.string.app_name);
                    } else
                        onLoaded(user);
                }
            }
        });

    }

    /**
     * 进行界面的设置
     *
     * @param user 用户信息
     */
    private void onLoaded(final User user) {
        this.user = user;
        // 是否就是我自己
        final boolean isSelf = user.getId().equalsIgnoreCase(Account.getUserId());
        // 是否已经关注
        final boolean isFollow = isSelf || user.isFollow();
        // 已经关注同时不是自己才能聊天
        final boolean allowSayHello = isFollow && !isSelf;

        // 切换到Ui线程
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                final PersonalContract.View view = getView();
                if (view == null)
                    return;
                view.onLoadDone(user);
                view.setFollowStatus(isFollow);
                view.allowSayHello(allowSayHello);
            }
        });
    }


    @Override
    public User getUserPersonal() {
        return user;
    }

    //用户关注
    @Override
    public void userFollow(String id) {
        UserHelper.userFollow(id,this);
    }
    //取消关注
    @Override
    public void userCancelFollow(String id) {
        UserHelper.userCancelFollow(id,this);
    }

    @Override
    public void onDataLoaded(UserCard userCard) {
        PersonalContract.View view = getView();
        if (view!=null){
            view.setFollowStatus(userCard.isFollow());
        }
    }

    @Override
    public void onDataNotAvailable(int strRes) {
        PersonalContract.View view = getView();
        if (view!=null){
            view.showError(strRes);
        }
    }
}
