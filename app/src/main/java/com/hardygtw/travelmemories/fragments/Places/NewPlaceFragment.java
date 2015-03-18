package com.hardygtw.travelmemories.fragments.Places;


import android.app.ActionBar;
import android.content.Context;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
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
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.wallet.Address;
import com.hardygtw.travelmemories.DatePickerFragment;
import com.hardygtw.travelmemories.GPSTracker;
import com.hardygtw.travelmemories.SQLDatabaseSingleton;
import com.hardygtw.travelmemories.activity.MainActivity;
import com.hardygtw.travelmemories.R;
import com.hardygtw.travelmemories.fragments.Nearby.NearbyFragment;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;


public class NewPlaceFragment extends Fragment implements OnMapReadyCallback {

    private ActionBar actionBar;
    private TextView placeVisited;
    private Button dateVisited;
    private TextView companions;
    private TextView textView;
    private GPSTracker gps;
    private TextView placeNotes;
    private String address;
    private TextView location;
    private SupportMapFragment fragment;
    public static LatLng LOCATION = null;

    public NewPlaceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.new_place_details,container, false);

        address = "";
        placeVisited = (TextView) rootView.findViewById(R.id.place_name);
        dateVisited = (Button) rootView.findViewById(R.id.dateVisited);
        companions = (TextView)rootView.findViewById(R.id.companion_name);
        placeNotes = (TextView) rootView.findViewById(R.id.place_notes);
        location = (TextView) rootView.findViewById(R.id.location);
        setDateVisited(rootView);
        addButtonListener(rootView);
        gps = new GPSTracker(getActivity());

        if(gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            LOCATION = new LatLng(latitude, longitude);
        } else {
            gps.showSettingsAlert();
        }
        String title = getArguments().getString("NEW_PLACE");
        actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.removeAllTabs();
        ((MainActivity)getActivity()).getDrawerToggle().setDrawerIndicatorEnabled(false);
        if (!title.equals("")) {
            actionBar.setTitle(title);
        }
      //  new LongOperation(this).execute("");
        return rootView;
    }

    private View getTabIndicator(Context context, int title) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab_layout, null);
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

                if (LOCATION == null || address.equals("") || placeVisited.getText().toString().equals("") || dateVisited.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "You must fill in all the fields", Toast.LENGTH_SHORT).show();

                    if( placeVisited.getText().toString().length() == 0 )
                        placeVisited.setError( "Place name is required!" );
                } else {
                    SQLDatabaseSingleton.getInstance(getActivity()).createPlaceVisit(placeVisited.getText().toString(), dateVisited.getText().toString(), placeNotes.getText().toString(), companions.getText().toString(), NewPlaceFragment.LOCATION, address, 0);
                    ((MainActivity)getActivity()).goBackFragment();
                    Toast.makeText(getActivity(),"Place Created",Toast.LENGTH_SHORT).show();
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
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

    public void onViewCreated (View view, Bundle savedInstanceState) {

        if(LOCATION == null){
            Toast.makeText(getActivity(), "Unable to retrieve GPS location", Toast.LENGTH_LONG).show();

            //Default location set to London
            LOCATION = new LatLng(51.5008, 0.1247);
        }
        new LongOperation(this).execute("");
    }


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

    private class LongOperation extends AsyncTask<String, Void, Boolean> {

        //ProgressDialog progressDialog;
        NewPlaceFragment placeFragment;


        public LongOperation(NewPlaceFragment placeFragment) {

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

        if (NewPlaceFragment.LOCATION == null) {
            NewPlaceFragment.LOCATION = new LatLng(0,0);
        }

        map.addMarker(new MarkerOptions().position(NewPlaceFragment.LOCATION));

        Geocoder geoCoder = new Geocoder(getActivity());
        List<android.location.Address> matches = null;
        try {
            matches = geoCoder.getFromLocation(NewPlaceFragment.LOCATION.latitude, NewPlaceFragment.LOCATION.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        android.location.Address bestMatch = (matches.isEmpty() ? null : matches.get(0));
        if (bestMatch != null) {
            location.setText(bestMatch.getAddressLine(0) + ", " + bestMatch.getLocality() + ", " + bestMatch.getCountryName());
            address = bestMatch.getAddressLine(0) + ", " + bestMatch.getLocality() + ", " + bestMatch.getCountryName();
        }

        // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(NewPlaceFragment.LOCATION)      // Sets the center of the map to COLOSSEUM
                .zoom(10)                   // Sets the zoom
                        //.bearing(90)                // Sets the orientation of the camera to east
                        //.tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                map.clear();
                map.addMarker(new MarkerOptions().position(latLng));
                NewPlaceFragment.LOCATION = latLng;

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
