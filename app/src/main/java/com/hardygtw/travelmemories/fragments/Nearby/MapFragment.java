package com.hardygtw.travelmemories.fragments.Nearby;

import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.hardygtw.travelmemories.GPSTracker;
import com.hardygtw.travelmemories.SQLDatabaseSingleton;
import com.hardygtw.travelmemories.activity.MainActivity;
import com.hardygtw.travelmemories.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hardygtw.travelmemories.model.PlaceVisited;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.app.ActionBar;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MapFragment extends Fragment implements OnMapReadyCallback{

    public static LatLng LOCATION = null;
    private SupportMapFragment fragment;
    private ArrayList<PlaceVisited> placesVisited;
    private HashMap<Marker, PlaceVisited> markers;
    private TextView textView;
    private GPSTracker gps;


    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        markers = new HashMap<Marker, PlaceVisited>();

        ((MainActivity)getActivity()).getDrawerToggle().setDrawerIndicatorEnabled(false);
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setDisplayShowCustomEnabled(false);
        actionBar.setTitle("View Map");
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.removeAllTabs();

        placesVisited =  SQLDatabaseSingleton.getInstance(getActivity()).getAllPlaceVisits();
        gps = new GPSTracker(getActivity());

        if(gps.canGetLocation()) {
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    LOCATION = new LatLng(latitude, longitude);
         } else {
                    gps.showSettingsAlert();
         }

        textView = (TextView) rootView.findViewById(R.id.mapLoadingText);

        return rootView;
    }

    public void onViewCreated (View view, Bundle savedInstanceState) {

        if(LOCATION == null){
            Toast.makeText(getActivity(), "Unable to retrieve GPS location", Toast.LENGTH_LONG).show();

            //Default location set to London
            LOCATION = new LatLng(51.5008, 0.1247);
        }
        new LongOperation(getActivity(), this).execute("");
    }

    private class LongOperation extends AsyncTask<String, Void, Boolean> {

        //ProgressDialog progressDialog;
        MapFragment mapFragment;


        public LongOperation(Context context, MapFragment mapFragment) {
            //progressDialog = new ProgressDialog(context);
            this.mapFragment = mapFragment;
        }

        @Override
        protected Boolean doInBackground(String... params) {

            try {
                FragmentManager fm = getChildFragmentManager();
                fragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
                if (fragment == null) {
                    fragment = SupportMapFragment.newInstance(new GoogleMapOptions().zOrderOnTop(true));
                    fm.beginTransaction().replace(R.id.map, fragment).commit();
                    fragment.getMap().setMyLocationEnabled(true);
                    fragment.getMap().setIndoorEnabled(true);
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
            // Update UI here
            //progressDialog.dismiss();
            textView.setVisibility(View.GONE);
            fragment.getMapAsync(mapFragment);
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        for (PlaceVisited place : placesVisited){
            Marker marker = map.addMarker(new MarkerOptions().position(place.getLocation()));
            markers.put(marker, place);
        }

        // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
       /** CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(MapFragment.LOCATION)      // Sets the center of the map to COLOSSEUM
                .zoom(10)                   // Sets the zoom
                        //.bearing(90)                // Sets the orientation of the camera to east
                        //.tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition)); **/


        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker marker) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                View view = getActivity().getLayoutInflater().inflate(R.layout.marker_window, null);
                TextView locationView = (TextView) view.findViewById(R.id.place_name);
                TextView addressView = (TextView) view.findViewById(R.id.place_address);
                TextView dateView = (TextView) view.findViewById(R.id.date_visited);

                String formatLocator = "<b>Place Name:</b> " + markers.get(marker).getPlaceName();
                String formatAddress = "<b>Address:</b> " + markers.get(marker).getAddress();
                String formatDate = "<b>Visit date:</b> " + markers.get(marker).getDateVisited();

                //Likewise you can introduce some HTML formatting to the content

                locationView.setText(Html.fromHtml(formatLocator));
                addressView.setText(Html.fromHtml(formatAddress));
                dateView.setText(Html.fromHtml(formatDate));

                return view;
            }
        });

        centerIncidentRouteOnMap(map);
    }

    public void centerIncidentRouteOnMap(GoogleMap map) {
        double minLat = Integer.MAX_VALUE;
        double maxLat = Integer.MIN_VALUE;
        double minLon = Integer.MAX_VALUE;
        double maxLon = Integer.MIN_VALUE;
        for (PlaceVisited placeVisited: markers.values()) {
            maxLat = Math.max(placeVisited.getLocation().latitude, maxLat);
            minLat = Math.min(placeVisited.getLocation().latitude, minLat);
            maxLon = Math.max(placeVisited.getLocation().longitude, maxLon);
            minLon = Math.min(placeVisited.getLocation().longitude, minLon);
        }
        final LatLngBounds bounds = new LatLngBounds.Builder().include(new LatLng(maxLat, maxLon)).include(new LatLng(minLat, minLon)).build();
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
    }


}
