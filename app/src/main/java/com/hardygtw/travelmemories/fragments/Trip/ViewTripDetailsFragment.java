package com.hardygtw.travelmemories.fragments.Trip;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hardygtw.travelmemories.R;
import com.hardygtw.travelmemories.SQLDatabaseSingleton;
import com.hardygtw.travelmemories.model.Trip;

/**
 * Created by gavin on 12/02/2015.
 */
public class ViewTripDetailsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.view_trip_details, container, false);

        int trip_id = getArguments().getInt("TRIP_ID");

        TextView tripName = (TextView) v.findViewById(R.id.trip_name);
        TextView departureDate = (TextView) v.findViewById(R.id.startDate);
        TextView returnDate = (TextView) v.findViewById(R.id.endDate);
        TextView notes = (TextView) v.findViewById(R.id.trip_notes);

        Trip trip = SQLDatabaseSingleton.getInstance(getActivity()).getTripDetails(trip_id);

        tripName.setText(trip.getTripName());
        departureDate.setText(trip.getStartDate());
        returnDate.setText(trip.getEndDate());
        notes.setText(trip.getNotes());

        return v;
    }
}

