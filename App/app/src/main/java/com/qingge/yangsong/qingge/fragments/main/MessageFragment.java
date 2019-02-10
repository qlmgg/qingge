package com.qingge.yangsong.qingge.fragments.main;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qingge.yangsong.common.app.Fragment;
import com.qingge.yangsong.qingge.R;

import butterknife.BindView;

public class MessageFragment extends Fragment {
    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

    }
}
