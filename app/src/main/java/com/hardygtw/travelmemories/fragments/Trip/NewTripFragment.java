package com.hardygtw.travelmemories.fragments.Trip;


import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.support.v4.app.FragmentTabHost;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hardygtw.travelmemories.DatePickerFragment;
import com.hardygtw.travelmemories.SQLDatabaseSingleton;
import com.hardygtw.travelmemories.activity.MainActivity;
import com.hardygtw.travelmemories.R;

import java.sql.SQLData;
import java.util.Calendar;


public class NewTripFragment extends Fragment {

    private ActionBar actionBar;
    private TextView tripTitle;
    private Button startButton;
    private Button endButton;
    private TextView tripNotes;


    public NewTripFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment let me watch the video
        View rootView = inflater.inflate(R.layout.new_trip_details,container, false);
        tripTitle = (TextView) rootView.findViewById(R.id.trip_name);
        startButton = (Button) rootView.findViewById(R.id.startDate);
        endButton = (Button) rootView.findViewById(R.id.endDate);
        tripNotes = (TextView) rootView.findViewById(R.id.trip_notes);

        setStartDate(rootView);
        setEndDate(rootView);
        addButtonListener(rootView);

        String title = getArguments().getString("NEW_TRIP");
        actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.removeAllTabs();
        ((MainActivity)getActivity()).getDrawerToggle().setDrawerIndicatorEnabled(false);

        if (!title.equals("")) {
            actionBar.setTitle(title);
        }

        return rootView;
    }


    private View getTabIndicator(Context context, int title) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab_layout, null);
        ImageView iv = (ImageView) view.findViewById(R.id.imageView);
        TextView tv = (TextView) view.findViewById(R.id.textView);
        tv.setText(title);
        return view;
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
        menuInflater.inflate(R.menu.menu_add_trip, menu);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_cancel:
                ((MainActivity)getActivity()).goBackFragment();
               return true;
            case R.id.add_accept:

                    if(tripTitle.getText().toString().equals("") || startButton.getText().toString().equals("") || endButton.toString().equals("")){
                        Toast.makeText(getActivity(),"You must fill in all the fields",Toast.LENGTH_SHORT).show();
                        if( tripTitle.getText().toString().length() == 0 )
                            tripTitle.setError( "Trip name is required!" );
                    } else {
                        SQLDatabaseSingleton.getInstance(getActivity()).createTrip(tripTitle.getText().toString(), startButton.getText().toString(), endButton.getText().toString(), tripNotes.getText().toString());
                        ((MainActivity)getActivity()).goBackFragment();
                        Toast.makeText(getActivity(),"Trip Created",Toast.LENGTH_SHORT).show();
                    }

            return true;

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
        startButton.setText(new StringBuilder()
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
        endButton.setText(new StringBuilder()
                // Month is 0 based, so you have to add 1
                .append(endDay).append("-")
                .append(endMonth + 1).append("-")
                .append(endYear).append(" "));


    }


    public void addButtonListener(View v) {

        startButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                DialogFragment picker = new DatePickerFragment(startButton);
                picker.show(getFragmentManager(), "datePicker1");


            }

        });

        endButton = (Button) v.findViewById(R.id.endDate);

        endButton.setOnClickListener(new View.OnClickListener() {

            @Override
             public void onClick(View v) {


                DialogFragment picker = new DatePickerFragment(endButton);
                picker.show(getFragmentManager(), "datePicker2");

            }

        });

    }
}
