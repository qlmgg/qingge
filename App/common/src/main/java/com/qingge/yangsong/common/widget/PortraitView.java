package com.qingge.yangsong.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import com.bumptech.glide.RequestManager;
import com.qingge.yangsong.common.R;
import com.qingge.yangsong.factory.model.Author;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 头像控件
 */
public class PortraitView extends CircleImageView {
    public PortraitView(Context context) {
        super(context);
    }

    public PortraitView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PortraitView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setup(RequestManager manager, Author author){
        if (author==null)
            return;
            setup(manager,author.getPortrait());
    }

    public void setup(RequestManager manager,String url){
        setup(manager, R.drawable.default_portrait,url);
    }

    public void setup(RequestManager manager,int resourceId,String url){
        if (url == null)
            url = "";
        manager.load(url)
                .placeholder(resourceId)
                .centerCrop()
                .dontAnimate()////空间中不能使用动画
                .into(this);
    }
}
