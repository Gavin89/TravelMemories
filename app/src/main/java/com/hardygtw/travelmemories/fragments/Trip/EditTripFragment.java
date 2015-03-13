package com.hardygtw.travelmemories.fragments.Trip;


import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.support.v4.app.FragmentTabHost;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hardygtw.travelmemories.DatePickerFragment;
import com.hardygtw.travelmemories.SQLDatabaseSingleton;
import com.hardygtw.travelmemories.activity.MainActivity;
import com.hardygtw.travelmemories.R;
import com.hardygtw.travelmemories.model.Trip;

import java.util.Calendar;


public class EditTripFragment extends Fragment {

    private ActionBar actionBar;
    private FragmentTabHost mTabHost;
    private int trip_id;
    private EditText tripName;
    private Button startDate;
    private Button endDate;
    private EditText editTripNotes;

    public EditTripFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment let me watch the video
        View rootView = inflater.inflate(R.layout.edit_trip_details,container, false);
        String title = getArguments().getString("EDIT_TRIP");
        trip_id = getArguments().getInt("TRIP_ID");
        actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.removeAllTabs();
        ((MainActivity)getActivity()).getDrawerToggle().setDrawerIndicatorEnabled(false);

        Trip trip = SQLDatabaseSingleton.getInstance(getActivity()).getTripDetails(trip_id);

        tripName = (EditText) rootView.findViewById(R.id.trip_name);
        startDate = (Button) rootView.findViewById(R.id.startDate);
        endDate = (Button) rootView.findViewById(R.id.endDate);
        editTripNotes = (EditText) rootView.findViewById(R.id.edit_trip_notes);

        setStartDate(rootView);
        setEndDate(rootView);
        addButtonListener(rootView);

        tripName.setText(trip.getTripName());
        startDate.setText(trip.getStartDate());
        endDate.setText(trip.getEndDate());
        editTripNotes.setText(trip.getNotes());

        if (!title.equals("")) {
            actionBar.setTitle(title);
        }

        return rootView;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu){
        MenuItem settings = menu.findItem(R.id.action_settings);
        settings.setVisible(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater){
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.menu_edit_trip, menu);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_accept:
                if (tripName.getText().toString().equals("") || startDate.getText().toString().equals("") || endDate.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Fields must not be empty", Toast.LENGTH_SHORT).show();
                } else {
                    SQLDatabaseSingleton.getInstance(getActivity()).updateTrip(trip_id, tripName.getText().toString(), startDate.getText().toString(), endDate.getText().toString(), editTripNotes.getText().toString());
                    Toast.makeText(getActivity(), "Trip Updated", Toast.LENGTH_SHORT).show();
                    ((MainActivity) getActivity()).goBackFragment();
                }
                break;
            case R.id.add_cancel:
                ((MainActivity)getActivity()).goBackFragment();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // display current date both on the text view and the Date Picker when the application starts.
    public void setStartDate(View v) {

        final Calendar calendar = Calendar.getInstance();

        int startDay = calendar.get(Calendar.DAY_OF_MONTH);
        int startMonth = calendar.get(Calendar.MONTH);
        int startYear = calendar.get(Calendar.YEAR);

        // set current date into textview
        startDate.setText(new StringBuilder()
                // Month is 0 based, so you have to add 1
                .append(startDay).append("-")
                .append(startMonth + 1).append("-")
                .append(startYear).append(" "));
    }


    // display current date both on the text view and the Date Picker when the application starts.
    public void setEndDate(View v) {

        final Calendar calendar = Calendar.getInstance();

        int endDay = calendar.get(Calendar.DAY_OF_MONTH);
        int endMonth = calendar.get(Calendar.MONTH);
        int endYear = calendar.get(Calendar.YEAR);

        // set current date into textview
        endDate.setText(new StringBuilder()
                // Month is 0 based, so you have to add 1
                .append(endDay).append("-")
                .append(endMonth + 1).append("-")
                .append(endYear).append(" "));


    }


    public void addButtonListener(View v) {

        startDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                DialogFragment picker = new DatePickerFragment(startDate);
                picker.show(getFragmentManager(), "datePicker1");


            }

        });

        endDate = (Button) v.findViewById(R.id.endDate);

        endDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                DialogFragment picker = new DatePickerFragment(endDate);
                picker.show(getFragmentManager(), "datePicker2");

            }

        });

    }

}
