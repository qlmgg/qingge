package com.qingge.yangsong.qingge.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.qingge.yangsong.qingge.R;

import java.io.File;
import java.util.List;

/**
 * Created by White paper on 2019/10/19
 * Describe :
 */
public class SelectPictureAdapter extends BaseAdapter {
    private List<String> picturePaths;
    private LayoutInflater layoutInflater;

    public SelectPictureAdapter(Context context, List<String> albumCards) {
        albumCards.add("");
        this.picturePaths = albumCards;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return picturePaths.size();
    }

    @Override
    public Object getItem(int position) {
        return picturePaths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (position == picturePaths.size() - 1)
            return layoutInflater.inflate(R.layout.lay_add_picture, null);

        convertView = layoutInflater.inflate(R.layout.cell_album, null);
        ImageView imageView = convertView.findViewById(R.id.im_album);
        String path = picturePaths.get(position);

        Uri uri = Uri.fromFile(new File(path));

        Glide.with(parent.getContext())
                .load(uri)
                .placeholder(R.drawable.default_portrait)
                .error(R.drawable.default_portrait)
                .into(imageView);

        return convertView;
    }

}
