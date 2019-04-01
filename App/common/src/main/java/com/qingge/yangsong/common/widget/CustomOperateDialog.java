package com.qingge.yangsong.common.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.qingge.yangsong.common.R;
import com.xujiaji.happybubble.BubbleDialog;

public class CustomOperateDialog extends BubbleDialog implements View.OnClickListener
{
    private ViewHolder mViewHolder;
    private OnClickCustomButtonListener mListener;

    public CustomOperateDialog(Context context)
    {
        super(context);
        calBar(true);
        setTransParentBackground();
        setPosition(Position.TOP);
        View rootView = LayoutInflater.from(context).inflate(R.layout.dialog_view, null);
        mViewHolder = new ViewHolder(rootView);
        addContentView(rootView);
        mViewHolder.tv_1.setOnClickListener(this);
        mViewHolder.tv_2.setOnClickListener(this);
        mViewHolder.tv_3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mListener != null)
        {
            mListener.onClick(((TextView)v).getText().toString());
        }
    }

    private static class ViewHolder {
        TextView tv_1, tv_2, tv_3;
        ViewHolder(View rootView) {
            tv_1 = rootView.findViewById(R.id.tv_01);
            tv_2 = rootView.findViewById(R.id.tv_02);
            tv_3 = rootView.findViewById(R.id.tv_03);
        }
    }

    public void setClickListener(OnClickCustomButtonListener l) {
        this.mListener = l;
    }

    public interface OnClickCustomButtonListener {
        void onClick(String str);
    }
}

