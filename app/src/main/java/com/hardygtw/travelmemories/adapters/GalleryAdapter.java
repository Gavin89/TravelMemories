package com.hardygtw.travelmemories.adapters;

/**
 * Created by gavin on 21/03/2015.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hardygtw.travelmemories.R;
import com.hardygtw.travelmemories.activity.FullScreenImageActivity;
import com.hardygtw.travelmemories.fragments.Gallery.GalleryFragment;
import com.hardygtw.travelmemories.fragments.Trip.ViewTripFragment;
import com.hardygtw.travelmemories.model.Photo;
import com.hardygtw.travelmemories.model.Trip;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import java.util.ArrayList;



public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ImageViewHolder> {
    private ArrayList<Photo> mDataset;
    private FragmentManager fm;
    private Activity _activity;

    // Provide a suitable constructor (depends on the kind of dataset)
    public GalleryAdapter(ArrayList<Photo> myDataset, FragmentManager fm, Activity _activity) {
        mDataset = myDataset;
        this.fm = fm;
        this._activity = _activity;
    }


    public Photo getItem(int location) {
        return mDataset.get(location);
    }


    // Create new views (invoked by the layout manager)
    @Override
    public  GalleryAdapter.ImageViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_view_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ImageViewHolder vh = new ImageViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ImageViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mDataset.get(position).getTags());
        Photo item = mDataset.get(position);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        ImageSize targetSize = new ImageSize(160,0150);
        Bitmap loadedImage = ImageLoader.getInstance().loadImageSync(item.getPath(), targetSize, options);
        holder.mImageView.setImageBitmap(loadedImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
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



    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ImageView mImageView;
        public TextView mTextViewDate;

        public ImageViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.text);
            mImageView = (ImageView) v.findViewById(R.id.image);
            mTextViewDate = (TextView) v.findViewById(R.id.start_date);
        }
    }
}
