package com.hardygtw.travelmemories.adapters;

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

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {
    private String[] mDataset;
    private FragmentManager fm;

    // Provide a suitable constructor (depends on the kind of dataset)
    public TripAdapter(String[] myDataset, FragmentManager fm) {
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
    public void onBindViewHolder(TripViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mDataset[position]);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                        Fragment fragment = null;


                        FragmentTransaction ft = fm.beginTransaction();

                        fragment = new ViewTripFragment();

                        ft.replace(R.id.frame_container, fragment, "VIEW_TRIP_FRAGMENT");
                        ft.addToBackStack(null);
                        ft.commit();

            }
        });


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class TripViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ImageView mImageView;

        public TripViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.info_text);
            mImageView = (ImageView) v.findViewById(R.id.trip_image);
        }
    }
}
