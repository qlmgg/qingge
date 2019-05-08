package com.qingge.yangsong.qingge.fragments.mytab;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qingge.yangsong.common.app.Fragment;
import com.qingge.yangsong.common.widget.recycler.RecyclerAdapter;
import com.qingge.yangsong.qingge.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class DynamicFragment extends Fragment {
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;

    @Override
    protected int getContentLayoutId() {
        Log.e("MMM","c测试数据");
        return R.layout.fragment_dynamic_my;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        Log.e("MMM","c测试数据int");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            data.add("我是数据:"+ i);
            Log.e("MMM","c测试数据intdata");
        }
        mRecyclerView.setAdapter(new RecyclerAdapter<String>(null,data) {
            @Override
            protected int getItemViewType(int position, String o) {
                return R.layout.cell_test;
            }

            @Override
            protected ViewHolder<String> onCreateViewHolder(View view, int viewType) {
                return new DynamicFragment.ViewHolder(view);
            }


        });

    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<String> {
        @BindView(R.id.text)
        TextView mText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(String s) {
            Log.e("MMM","c测试数据"+s);
            mText.setText(s);
        }
    }
}
