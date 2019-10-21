package com.qingge.yangsong.qingge.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.qingge.yangsong.qingge.R;

import java.util.List;

/**
 * Created by White paper on 2019/10/19
 * Describe :
 */
public class AlbumAdapter extends BaseAdapter {
    private List<String> albumPaths;
    private LayoutInflater layoutInflater;
    public AlbumAdapter(Context context, List<String> albumCards) {
        this.albumPaths = albumCards;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return albumPaths.size();
    }

    @Override
    public Object getItem(int position) {
        return albumPaths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.cell_album, null);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.im_album);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String path = albumPaths.get(position);

        Glide.with(parent.getContext())
                .load(path)
                .placeholder(R.drawable.default_portrait)
                .error(R.drawable.default_portrait)
                .into(holder.imageView);
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
    }
}
