package com.hardygtw.travelmemories.adapters;

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
import com.hardygtw.travelmemories.fragments.Trip.ViewTripFragment;
import com.hardygtw.travelmemories.model.Photo;
import com.hardygtw.travelmemories.model.Trip;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import java.util.ArrayList;
import java.util.Random;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {
    private ArrayList<Trip> mDataset;
    private FragmentManager fm;

    // Provide a suitable constructor (depends on the kind of dataset)
    public TripAdapter(ArrayList<Trip> myDataset, FragmentManager fm) {
        mDataset = myDataset;
        this.fm = fm;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TripAdapter.TripViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trip_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        TripViewHolder vh = new TripViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(TripViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mDataset.get(position).getTripName());
        holder.mTextViewDate.setText(mDataset.get(position).getStartDate());


        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        Trip item = mDataset.get(position);
        ImageSize targetSize = new ImageSize(160,150);

        if (item.getTripPhotos().size() > 0) {
            Random r = new Random();

            Photo photo =  item.getTripPhotos().get(r.nextInt(item.getTripPhotos().size()));
            Bitmap loadedImage = ImageLoader.getInstance().loadImageSync(photo.getPath(), targetSize, options);
                holder.mImageView.setImageBitmap(loadedImage);

        } else {
              holder.mImageView.setImageResource(R.drawable.no_image);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        Fragment fragment = null;

                        FragmentTransaction ft = fm.beginTransaction();
                        Bundle bundle = new Bundle(1);

                        bundle.putInt("TRIP_ID",mDataset.get(position).getTripId());

                        fragment = new ViewTripFragment();
                        fragment.setArguments(bundle);

                        ft.replace(R.id.frame_container, fragment, "VIEW_TRIP_FRAGMENT");
                        ft.addToBackStack(null);
                        ft.commit();

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
    public static class TripViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ImageView mImageView;
        public TextView mTextViewDate;

        public TripViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.info_text);
            mImageView = (ImageView) v.findViewById(R.id.trip_image);
            mTextViewDate = (TextView) v.findViewById(R.id.start_date);
        }
    }
}
