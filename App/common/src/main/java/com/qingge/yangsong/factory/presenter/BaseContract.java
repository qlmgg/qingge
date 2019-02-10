package com.qingge.yangsong.factory.presenter;
import android.support.annotation.StringRes;

import com.qingge.yangsong.common.widget.recycler.RecyclerAdapter;

/**
 * MVP模式中公共的基础契约
 */
public interface BaseContract {
    //基本契约中的v层接口
    interface View<T extends Presenter> {
        // 公共的：显示一个字符串错误
        void showError(@StringRes int str);

        // 公共的：显示进度条
        void showLoading();

        // 支持设置一个Presenter
        void setPresenter(T presenter);
    }
    //基本契约中的p层接口
    interface Presenter {
        // 共用的开始触发
        void start();

        // 共用的销毁触发
        void destroy();
    }
    //基本的一个列表的view的职责
    interface RecyclerView<T extends Presenter,ViewMode> extends View<T>{
        //界面端只能刷新整个数据集合,不能精确到每一条数据更新
        //void onDone(List<User> users)
        //拿到一个适配器,然后自己自主的进行刷新
        RecyclerAdapter<ViewMode> getRecyclerAdapter();
        //当适配器更改了的时候触发
        void onAdapterDataChanged();
    }
}
