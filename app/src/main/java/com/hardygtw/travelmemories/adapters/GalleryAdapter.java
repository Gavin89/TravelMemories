package com.hardygtw.travelmemories.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hardygtw.travelmemories.R;
import com.hardygtw.travelmemories.model.ImageItem;
import com.hardygtw.travelmemories.model.Photo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by gavin on 09/02/2015.
 */
public class GalleryAdapter extends ArrayAdapter<Photo> {
    private Context context;
    private int layoutResourceId;
    private ArrayList<Photo> data = new ArrayList<Photo>();

    public GalleryAdapter(Context context, int layoutResourceId,
                          ArrayList<Photo> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageTitle = (TextView) row.findViewById(R.id.text);
            holder.image = (ImageView) row.findViewById(R.id.image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Photo item = data.get(position);
        holder.imageTitle.setText(item.getTitle());

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        ImageSize targetSize = new ImageSize(160,150);
        Bitmap loadedImage = ImageLoader.getInstance().loadImageSync("file:///" + item.getPath(), targetSize, options);
        holder.image.setImageBitmap(loadedImage);

        return row;
    }

    static class ViewHolder {
        TextView imageTitle;
        ImageView image;
    }
}