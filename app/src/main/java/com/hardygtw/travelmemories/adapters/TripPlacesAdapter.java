package com.hardygtw.travelmemories.adapters;

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
import com.hardygtw.travelmemories.fragments.Places.ViewPlaceFragment;
import com.hardygtw.travelmemories.fragments.Trip.ViewTripFragment;
import com.hardygtw.travelmemories.model.PlaceVisited;

import java.util.ArrayList;

public class TripPlacesAdapter extends RecyclerView.Adapter<TripPlacesAdapter.PlaceViewHolder> {
    private ArrayList<PlaceVisited> myDataset;
    private FragmentManager fm;

    // Provide a suitable constructor (depends on the kind of dataset)
    public TripPlacesAdapter(ArrayList<PlaceVisited> myDataset, FragmentManager fm) {

        this.myDataset = myDataset;
        this.fm = fm;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TripPlacesAdapter.PlaceViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_trip_places, parent, false);
        // set the view's size, margins, paddings and layout parameters
        PlaceViewHolder vh = new PlaceViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(PlaceViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextViewPlace.setText(myDataset.get(position).getPlaceName());
        holder.mTextViewLocation.setText(myDataset.get(position).getAddress());
        holder.mTextViewDate.setText(myDataset.get(position).getDateVisited());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = null;

                FragmentTransaction ft = fm.beginTransaction();

                fragment = new ViewPlaceFragment();

                Bundle bundle = new Bundle(1);
                bundle.putInt("PLACE_VISIT_ID", myDataset.get(position).getPlaceVisitId());
                fragment.setArguments(bundle);

                ft.replace(R.id.frame_container, fragment, "VIEW_PLACES_FRAGMENT");
                ft.addToBackStack(null);
                ft.commit();

            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return myDataset.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class PlaceViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextViewPlace;
        public TextView mTextViewLocation;
        public TextView mTextViewDate;
        public ImageView mImageView;

        public PlaceViewHolder(View v) {
            super(v);
            mTextViewPlace = (TextView) v.findViewById(R.id.place_name);
            mTextViewLocation = (TextView) v.findViewById(R.id.location);
            mTextViewDate = (TextView) v.findViewById(R.id.dateVisited);
            mImageView = (ImageView) v.findViewById(R.id.place_image);
        }
    }
}
