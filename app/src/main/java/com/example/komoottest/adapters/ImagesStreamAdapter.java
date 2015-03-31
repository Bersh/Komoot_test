package com.example.komoottest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.example.komoottest.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * @author <a href="mailto:iBersh20@gmail.com">Iliya Bershadskiy</a>
 * @since 30.03.2015
 */
public class ImagesStreamAdapter extends ArrayAdapter<String> {
    private LayoutInflater inflater;

    public ImagesStreamAdapter(Context context, List<String> fileNames) {
        super(context, R.layout.image_list_item);
        addAll(fileNames);
        inflater = LayoutInflater.from(context);
    }

    public void setFileNames(List<String> fileNames) {
        clear();
        addAll(fileNames);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.image_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Picasso.with(getContext()).load("file:" + getItem(position)).into(viewHolder.image);
        return convertView;
    }


    private class ViewHolder {
        ImageView image;
    }
}
