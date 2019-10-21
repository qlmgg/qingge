package com.qingge.yangsong.common.widget.recycler;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qingge.yangsong.common.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class RecyclerAdapter<Data> extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder<Data>>
        implements View.OnClickListener, View.OnLongClickListener, AdapterCallback<Data> {
    private final List<Data> mDataList;
    private AdapterListener<Data> mListener;

    public RecyclerAdapter() {
        this(null);
    }

    protected RecyclerAdapter(AdapterListener<Data> listener) {
        this(listener, new ArrayList<Data>());
    }

    protected RecyclerAdapter(AdapterListener<Data> listener, List<Data> mDataList) {
        this.mListener = listener;
        this.mDataList = mDataList;
    }

    /**
     * 通过实现类得到布局id(viewType)
     */
    @LayoutRes
    protected abstract int getItemViewType(int position, Data data);

    /**
     * 通过实现类得到一个ViewHolder
     */
    protected abstract ViewHolder<Data> onCreateViewHolder(View view, int viewType);

    /**
     * 复写默认的布局类型
     */
    @Override
    public int getItemViewType(int position) {
        return getItemViewType(position, mDataList.get(position));
    }

    @NonNull
    @Override
    public ViewHolder<Data> onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        //拿到LayoutInflater
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        //把xml id viewType转化为view
        View view = inflater.inflate(viewType, viewGroup, false);
        //通过实现类拿到viewHolder
        ViewHolder<Data> holder = onCreateViewHolder(view, viewType);
        // 设置View的Tag为ViewHolder，进行双向绑定
        view.setTag(R.id.tag_recycler_holder, holder);
        //设置点击事件
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        //界面绑定
        holder.unbinder = ButterKnife.bind(holder, view);
        //绑定callback
        holder.callback = this;
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder<Data> dataViewHolder, int position) {
        //拿到需要绑定的数据
        Data data = mDataList.get(position);
        //绑定
        dataViewHolder.bind(data);
    }

    /**
     * 得到集合大小
     */
    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    /**
     * 返回整个集合
     *
     * @return 类型，其实复写后返回的都是XML文件的ID
     */
    public List<Data> getItems() {
        return mDataList;
    }

    @Override
    public void onClick(View v) {
        ViewHolder viewHolder = (ViewHolder) v.getTag(R.id.tag_recycler_holder);
        if (this.mListener != null) {
            //得到当前viewHolder对应适配器中的坐标
            int position = viewHolder.getAdapterPosition();
            this.mListener.onItemClick(viewHolder, mDataList.get(position));
        }
    }

    @Override
    public boolean onLongClick(View v) {
        ViewHolder viewHolder = (ViewHolder) v.getTag(R.id.tag_recycler_holder);
        if (this.mListener != null) {
            // 得到当前viewHolder对应适配器中的坐标
            int position = viewHolder.getAdapterPosition();
            this.mListener.onItemLongClick(viewHolder, mDataList.get(position));
            return true;
        }
        return false;
    }

    @Override
    public void update(Data data, ViewHolder<Data> holder) {
        int position = holder.getAdapterPosition();
        if (position >= 0) {
            mDataList.remove(position);
            mDataList.add(position, data);
            //通知当前位置更新
            notifyItemChanged(position);
        }
    }


    public static abstract class ViewHolder<Data> extends RecyclerView.ViewHolder {

        private Unbinder unbinder;
        protected Data mData;
        private AdapterCallback<Data> callback;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        /**
         * 用于绑定数据的触发
         *
         * @param data 绑定的数据
         */
        void bind(Data data) {
            this.mData = data;
            onBind(data);
        }

        /**
         * 绑定数据的的回掉,必须复写
         *
         * @param data 绑定的数据
         */
        protected abstract void onBind(Data data);

        /**
         * 更新数据
         */
        public void updateData(Data data) {

            if (this.callback != null) {
                this.callback.update(data, this);
            }
        }
    }


    public void setListener(AdapterListener<Data> listener) {
        this.mListener = listener;
    }

    /**
     * 自定义的监听器
     */
    public interface AdapterListener<Data> {
        //当点击itme的时候
        void onItemClick(RecyclerAdapter.ViewHolder holder, Data data);

        //当长按item的时候
        void onItemLongClick(RecyclerAdapter.ViewHolder holder, Data data);
    }

    /**
     * 插入一条数据并通知插入
     *
     * @param data Data
     */
    public void add(Data data) {
        mDataList.add(data);
        notifyItemInserted(mDataList.size() - 1);
    }

    /**
     * 插入一堆数据，并通知这段集合更新
     * 以数组形式添加
     *
     * @param dataList Data..
     */
    public void add(Data... dataList) {
        if (dataList != null && dataList.length > 0) {
            int startPos = mDataList.size();
            Collections.addAll(mDataList, dataList);
            notifyItemRangeInserted(startPos, dataList.length);
        }
    }

    /**
     * 插入一堆数据，并通知这段集合更新
     * 以集合形式添加
     *
     * @param dataList Data
     */
    public void add(Collection<Data> dataList) {
        if (dataList != null && dataList.size() > 0) {
            int startPos = mDataList.size();
            mDataList.addAll(dataList);
            notifyItemRangeInserted(startPos, dataList.size());
        }
    }

    /**
     * 删除操作
     */
    public void clear() {
        mDataList.clear();
        notifyDataSetChanged();
    }

    /**
     * 替换为一个新的集合，其中包括了清空
     *
     * @param dataList 一个新的集合
     */
    public void replace(Collection<Data> dataList) {
        mDataList.clear();
        if (dataList == null || dataList.size() == 0)
            return;
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public static abstract class AdapterListenerImpl<Data> implements AdapterListener<Data> {
        @Override
        public void onItemClick(ViewHolder holder, Data data) {

        }

        @Override
        public void onItemLongClick(ViewHolder holder, Data data) {

        }
    }
}
