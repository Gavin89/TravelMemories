//package com.hardygtw.travelmemories.fragments.Nearby;
//
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//
//import com.hardygtw.travelmemories.GPSTracker;
//import com.hardygtw.travelmemories.R;
//import com.hardygtw.travelmemories.activity.MapActivity;
//import com.hardygtw.travelmemories.model.GooglePlaceList;
//import com.hardygtw.travelmemories.model.GooglePlacesUtility;
//import com.hardygtw.travelmemories.model.NearbyPlaceItem;
//import com.hardygtw.travelmemories.model.GooglePlace;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.StatusLine;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.DefaultHttpClient;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.URLEncoder;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//
///**
//* Created by beaumoaj on 03/02/15.
//*/
//public class RssMapFragment extends SupportMapFragment implements GoogleMap.OnMarkerClickListener {
//    private static final String ITEM = "NearbyPlaceITEM";
//
//    private GoogleMap googleMap;
//    private NearbyPlaceItem item;
//    private Marker itemMarker;
//    private HashMap<NearbyPlaceItem, Marker> markers;
//    private HashMap<Marker, GooglePlace> nearby;
//    // see http://volcanoes.usgs.gov/rss/vhpcaprss.xml
//
//    public static RssMapFragment newInstance(GPSTracker item) {
//        Bundle args = new Bundle();
//       // args.putSerializable(ITEM, item);
//        RssMapFragment mf = new RssMapFragment();
//        //mf.setItem(item);
//        mf.setArguments(args);
//        return mf;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        getActivity().setTitle(TheNews.getInstance().getFeed().getTitle());
//        this.setHasOptionsMenu(true);
//        // check for a location
//        Bundle args = getArguments();
//        /* */
//        Intent i = getActivity().getIntent();
//        item = (NearbyPlaceItem) i.getSerializableExtra(MapActivity.ITEM);
//    }
//
//    public void setItem(NearbyPlaceItem item) {
//        this.item = item;
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
//        View v = super.onCreateView(inflater, parent, savedInstanceState);
//
//        googleMap = getMap();
//        googleMap.setOnMarkerClickListener(this);
//        placePin(item, true);
//        googleMap.setMyLocationEnabled(true);
//        googleMap.getUiSettings().setZoomControlsEnabled(true);
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(item.getLocation().getLatLon(), 9));
//        return v;
//    }
//
//    private Marker createMarker(LatLng ll, String title, String description, float hue) {
//        /*
//        CircleOptions circleOptions = new CircleOptions()
//                .center(ll)
//                .radius(1000); // In meters
//        Circle circle = googleMap.addCircle(circleOptions);
//        */
//        Marker marker = googleMap.addMarker(new MarkerOptions().position(ll).icon(BitmapDescriptorFactory.defaultMarker(hue)));
//        marker.setTitle(title);
//        marker.setSnippet(description);
//
//        return marker;
//    }
//
//    private void placePin(NearbyPlaceItem item, boolean isItemMarker) {
//        if (isItemMarker) {
//            this.itemMarker = createMarker(item.getLocation().getLatLon(), item.getTitle(), item.getDescription(), BitmapDescriptorFactory.HUE_RED);
//        } else {
//            if (this.markers == null) {
//                this.markers = new HashMap<>();
//                this.markers.put(item, createMarker(item.getLocation().getLatLon(), item.getTitle(), item.getDescription(), BitmapDescriptorFactory.HUE_RED));
//            } else if (this.markers.containsKey(item)) {
//                this.markers.get(item).setVisible(true);
//            } else {
//                this.markers.put(item, createMarker(item.getLocation().getLatLon(), item.getTitle(), item.getDescription(), BitmapDescriptorFactory.HUE_RED));
//            }
//        }
//    }
//
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.map_add_remove, menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.menu_add_others:
//
//                for (NearbyPlaceItem i : TheNews.getInstance().getFeed().getItems()) {
//                    if (!i.equals(this.item)) {
//                        placePin(i, false);
//                    }
//                }
//                return true;
//            case R.id.menu_remove_others:
//                if (this.markers != null) {
//                    for (NearbyPlaceItem i : markers.keySet()) {
//                        markers.get(i).setVisible(false);
//                    }
//                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(this.item.getLocation().getLatLon(), 9));
//                }
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    public boolean onMarkerClick(Marker marker) {
//       // Log.i(RssItemActivity.TAG, "Clicked on marker at " + marker.getPosition());
//        if (marker.equals(this.itemMarker) && nearby == null) {
//            String placesKey = getActivity().getResources().getString(R.string.places_key);;
//            double lat = marker.getPosition().latitude;
//            double lng = marker.getPosition().longitude;
//            String type = URLEncoder.encode("train_station|bus_station");
//            String placesRequest = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" +
//                    lat + "," + lng + "&radius=500&key=" + placesKey;
//            PlacesReadFeed process = new PlacesReadFeed();
//            process.execute(placesRequest);
//        }
//        if (marker.equals(this.itemMarker) && nearby != null) {
//            for (Marker placeMarker : nearby.keySet()) {
//                placeMarker.setVisible(!placeMarker.isVisible());
//            }
//        } else if (nearby != null && nearby.containsKey(marker)) {
//            Intent intent = new Intent(getActivity(), PlaceDetailActivity.class);
//            intent.putExtra("PLACE", nearby.get(marker));
//            startActivity(intent);
//            return true;
//        } else {
//           // Log.i(RssItemActivity.TAG, "Not the item marker so not fetching places");
//        }
//        return false;
//    }
//
//
//    private class PlacesReadFeed extends AsyncTask<String, Void, GooglePlaceList> {
//        private final ProgressDialog dialog = new ProgressDialog(getActivity());
//
//        @Override
//        protected GooglePlaceList doInBackground(String... urls) {
//            try {
//                //dialog.setMessage("Fetching Places Data");
//                String input = GooglePlacesUtility.readGooglePlaces(urls[0]);
//                Gson gson = new Gson();
//                GooglePlaceList places = gson.fromJson(input, GooglePlaceList.class);
//             //   Log.i(RssItemActivity.TAG, "Number of places found is " + places.getResults().size());
//                return places;
//            } catch (Exception e) {
//                e.printStackTrace();
//               // Log.i(RssItemActivity.TAG, e.getMessage());
//                return null;
//            }
//        }
//
//        @Override
//        protected void onPreExecute() {
//            this.dialog.setMessage("Getting nearby places...");
//            this.dialog.show();
//        }
//
//        @Override
//        protected void onPostExecute(GooglePlaceList places) {
//            nearby = new HashMap<>();
//            for (GooglePlace place : places.getResults()) {
//                String name = place.getName();
//                List<String> types = place.getTypes();
//              //  Log.i(RssItemActivity.TAG, "Found a place called: " + name);
//                GooglePlace.Geometry geometry = place.getGeometry();
//                if (geometry != null) {
//                    GooglePlace.Geometry.Location location = geometry.getLocation();
//                    if (location != null) {
//
//                        nearby.put(createMarker(new LatLng(location.getLat(), location.getLng()),
//                                        types.toString(), name, BitmapDescriptorFactory.HUE_BLUE),
//                                place);
//                    }
//                }
//            }
//            this.dialog.dismiss();
//        }
//
//
//    }
//
//
//}