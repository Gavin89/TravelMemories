package com.hardygtw.travelmemories.fragments.Places;


import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hardygtw.travelmemories.DatePickerFragment;
import com.hardygtw.travelmemories.GPSTracker;
import com.hardygtw.travelmemories.SQLDatabaseSingleton;
import com.hardygtw.travelmemories.activity.MainActivity;
import com.hardygtw.travelmemories.R;
import com.hardygtw.travelmemories.model.PlaceVisited;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;


public class EditPlaceFragment extends Fragment implements OnMapReadyCallback {

    private ActionBar actionBar;
    private int place_id;
    private EditText placeName;
    private Button dateVisited;
    private EditText companions;
    private TextView location;
    private EditText editPlaceNotes;
    private String address;
    private SupportMapFragment fragment;
    public static LatLng LOCATION = null;
    private GPSTracker gps;

    public EditPlaceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.edit_place_details,container, false);

        place_id = getArguments().getInt("PLACE_VISIT_ID");
        actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.removeAllTabs();
        PlaceVisited place = SQLDatabaseSingleton.getInstance(getActivity()).getPlaceDetails(place_id);

        String title = place.getPlaceName();

        placeName = (EditText) rootView.findViewById(R.id.place_name);
        dateVisited = (Button) rootView.findViewById(R.id.dateVisited);
        companions = (EditText) rootView.findViewById(R.id.companion_name);
        editPlaceNotes = (EditText) rootView.findViewById(R.id.place_notes);
        location = (TextView) rootView.findViewById(R.id.location);

        setDateVisited(rootView);
        addButtonListener(rootView);

        placeName.setText(place.getPlaceName());
        dateVisited.setText(place.getDateVisited());
        companions.setText(place.getTravelCompanions());
        editPlaceNotes.setText(place.getTravellerNotes());
        location.setText(place.getAddress());
        LOCATION = place.getLocation();

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
        menuInflater.inflate(R.menu.menu_add_place, menu);
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
                break;
            case R.id.add_accept:
                new AlertDialog.Builder(getActivity())
                        .setTitle("Edit Place")
                        .setMessage("Are you sure you want to edit this place?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (placeName.getText().toString().equals("")) {
                                    Toast.makeText(getActivity(), "Fields must not be empty", Toast.LENGTH_SHORT).show();
                                } else {
                                    SQLDatabaseSingleton.getInstance(getActivity()).createPlaceVisit(placeName.getText().toString(), dateVisited.getText().toString(), editPlaceNotes.getText().toString(), companions.getText().toString(), EditPlaceFragment.LOCATION, address, 0);

                                    Toast.makeText(getActivity(), "Place Updated", Toast.LENGTH_SHORT).show();
                                    ((MainActivity) getActivity()).goBackFragment();
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void onViewCreated (View view, Bundle savedInstanceState) {

        if(LOCATION == null){
            Toast.makeText(getActivity(), "Unable to retrieve GPS location", Toast.LENGTH_LONG).show();

            LOCATION = new LatLng(51.5008, 0.1247);
        }
        new LongOperation(this).execute("");
    }

    // display current date both on the text view and the Date Picker when the application starts.
    public void setDateVisited(View v) {

        final Calendar calendar = Calendar.getInstance();

        int endDay = calendar.get(Calendar.DAY_OF_MONTH);
        int endMonth = calendar.get(Calendar.MONTH);
        int endYear = calendar.get(Calendar.YEAR);

        // set current date into textview
        dateVisited.setText(new StringBuilder()
                // Month is 0 based, so you have to add 1
                .append(endDay).append("-")
                .append(endMonth + 1).append("-")
                .append(endYear).append(" "));


    }


    public void addButtonListener(View v) {

        dateVisited.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                DialogFragment picker = new DatePickerFragment(dateVisited);
                picker.show(getFragmentManager(), "datePicker1");


            }

        });
    }

    private class LongOperation extends AsyncTask<String, Void, Boolean> {

        EditPlaceFragment placeFragment;


        public LongOperation(EditPlaceFragment placeFragment) {

            this.placeFragment = placeFragment;
        }

        @Override
        protected Boolean doInBackground(String... params) {

            try {
                FragmentManager fm = getChildFragmentManager();
                fragment = (SupportMapFragment) fm.findFragmentById(R.id.place_map);
                if (fragment == null) {
                    fragment = SupportMapFragment.newInstance(new GoogleMapOptions().zOrderOnTop(true));
                    fm.beginTransaction().replace(R.id.place_map, fragment).commit();
                }

                return true;

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            fragment.getMapAsync(placeFragment);
        }
    }

    @Override
    public void onMapReady(final GoogleMap map) {

        map.setMyLocationEnabled(true);
        map.setIndoorEnabled(true);

        if (EditPlaceFragment.LOCATION == null) {
            EditPlaceFragment.LOCATION = new LatLng(0,0);
        }

        map.addMarker(new MarkerOptions().position(EditPlaceFragment.LOCATION));

        Geocoder geoCoder = new Geocoder(getActivity());
        List<Address> matches = null;
        try {
            matches = geoCoder.getFromLocation(EditPlaceFragment.LOCATION.latitude, EditPlaceFragment.LOCATION.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        android.location.Address bestMatch = (matches.isEmpty() ? null : matches.get(0));
        if (bestMatch != null) {
            location.setText(bestMatch.getAddressLine(0) + ", " + bestMatch.getLocality() + ", " + bestMatch.getCountryName());
            address = bestMatch.getAddressLine(0) + ", " + bestMatch.getLocality() + ", " + bestMatch.getCountryName();
        }

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(EditPlaceFragment.LOCATION)
                .zoom(10)
                .build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                map.clear();
                map.addMarker(new MarkerOptions().position(latLng));
                EditPlaceFragment.LOCATION = latLng;

                Geocoder geoCoder = new Geocoder(getActivity());
                List<android.location.Address> matches = null;
                try {
                    matches = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                android.location.Address bestMatch = (matches.isEmpty() ? null : matches.get(0));
                location.setText(bestMatch.getAddressLine(0)+", " + bestMatch.getLocality() + ", " + bestMatch.getCountryName());
                address = bestMatch.getAddressLine(0)+", " + bestMatch.getLocality() + ", " + bestMatch.getCountryName();
            }
        });

    }
}
