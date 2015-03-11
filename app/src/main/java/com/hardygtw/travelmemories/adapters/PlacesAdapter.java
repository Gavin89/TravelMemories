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
import com.hardygtw.travelmemories.fragments.Places.ViewPlaceFragment;
import com.hardygtw.travelmemories.fragments.Trip.ViewTripFragment;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlaceViewHolder> {
    private String[] mDataset;
    private FragmentManager fm;

    // Provide a suitable constructor (depends on the kind of dataset)
    public PlacesAdapter(String[] myDataset, FragmentManager fm) {

        mDataset = myDataset;
        this.fm = fm;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PlacesAdapter.PlaceViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.places_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        PlaceViewHolder vh = new PlaceViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(PlaceViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mDataset[position]);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = null;

                FragmentTransaction ft = fm.beginTransaction();

                fragment = new ViewPlaceFragment();

                ft.replace(R.id.frame_container, fragment, "VIEW_PLACES_FRAGMENT");
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
    public static class PlaceViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ImageView mImageView;

        public PlaceViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.info_text);
            mImageView = (ImageView) v.findViewById(R.id.place_image);
        }
    }
}
