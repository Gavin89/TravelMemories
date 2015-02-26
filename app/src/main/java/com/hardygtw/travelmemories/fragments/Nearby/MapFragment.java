package com.hardygtw.travelmemories.fragments.Nearby;

import com.hardygtw.travelmemories.GPSTracker;
import com.hardygtw.travelmemories.MainActivity;
import com.hardygtw.travelmemories.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.app.ActionBar;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MapFragment extends Fragment implements OnMapReadyCallback{

    public static LatLng LOCATION = null;
    private SupportMapFragment fragment;
    private TextView textView;

    private Button btnShowLocation;
    private GPSTracker gps;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        ((MainActivity)getActivity()).getDrawerToggle().setDrawerIndicatorEnabled(false);
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setDisplayShowCustomEnabled(false);
        actionBar.setTitle("View Map");
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.removeAllTabs();

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
        protected void onPreExecute() {
            //progressDialog.setMessage("Map Loading...");
            //progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {

            try {
                FragmentManager fm = getChildFragmentManager();
                fragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
                if (fragment == null) {
                    fragment = SupportMapFragment.newInstance(new GoogleMapOptions().zOrderOnTop(true));
                    fm.beginTransaction().replace(R.id.map, fragment).commit();
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

        map.addMarker(new MarkerOptions().position(MapFragment.LOCATION));

        // Move the camera instantly to COLOSSEUM with a zoom of 15.
        //map.moveCamera(CameraUpdateFactory.newLatLngZoom(COLOSSEUM, 10));

        // Zoom in, animating the camera.
        //map.animateCamera(CameraUpdateFactory.zoomIn());

        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        //map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

        // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(MapFragment.LOCATION)      // Sets the center of the map to COLOSSEUM
                .zoom(10)                   // Sets the zoom
                        //.bearing(90)                // Sets the orientation of the camera to east
                        //.tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

}
