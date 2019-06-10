package com.qingge.yangsong.qingge.fragments.panel;

import android.view.View;


import com.qingge.yangsong.common.widget.recycler.RecyclerAdapter;
import com.qingge.yangsong.face.Face;
import com.qingge.yangsong.qingge.R;

import java.util.List;


public class FaceAdapter extends RecyclerAdapter<Face.Bean> {

    public FaceAdapter(List<Face.Bean> beans, AdapterListener<Face.Bean> listener) {
        super(listener,beans);
    }

    @Override
    protected int getItemViewType(int position, Face.Bean bean) {
        return R.layout.cell_face;
    }

    @Override
    protected ViewHolder<Face.Bean> onCreateViewHolder(View root, int viewType) {
        return new FaceHolder(root);
    }
}
