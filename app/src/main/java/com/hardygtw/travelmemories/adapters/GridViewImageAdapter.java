package com.hardygtw.travelmemories.adapters;

/**
 * Created by G on 12/03/2015.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hardygtw.travelmemories.R;
import com.hardygtw.travelmemories.activity.FullScreenImageActivity;
import com.hardygtw.travelmemories.model.Photo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;

public class GridViewImageAdapter extends ArrayAdapter<Photo> {

    private Activity _activity;
    private int imageWidth;
    private int layoutResourceId;
    private ArrayList<Photo> data = new ArrayList<Photo>();

    public GridViewImageAdapter(Activity activity, int layoutResourceId,
                                ArrayList<Photo> data) {
        super(activity, layoutResourceId, data);
        this._activity = activity;
        this.layoutResourceId = layoutResourceId;

        this.data = data;
    }

    @Override
    public int getCount() {
        return this.data.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) _activity).getLayoutInflater();
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

        ImageSize targetSize = new ImageSize(160,0150);
        Bitmap loadedImage = ImageLoader.getInstance().loadImageSync("file:///" + item.getPath(), targetSize, options);
        holder.image.setImageBitmap(loadedImage);

        row.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(_activity, FullScreenImageActivity.class);
                //i.putExtra("PLACE_VISIT_ID", item.getPhotoPlaceVisitId());
                //i.putExtra("TRIP_ID", item.getPhotoTripId());
                i.putExtra("PHOTO_POSITION", position);
                i.putExtra("PHOTO_DISPLAY", 0);
                _activity.startActivityForResult(i, 2);
            }
        });

        return row;
    }

    static class ViewHolder {
        TextView imageTitle;
        ImageView image;
    }

    /*
     * Resizing image size
     */
    public static Bitmap decodeFile(String filePath, int WIDTH, int HIGHT) {
        try {

            File f = new File(filePath);

            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            final int REQUIRED_WIDTH = WIDTH;
            final int REQUIRED_HIGHT = HIGHT;
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_WIDTH
                    && o.outHeight / scale / 2 >= REQUIRED_HIGHT)
                scale *= 2;

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}