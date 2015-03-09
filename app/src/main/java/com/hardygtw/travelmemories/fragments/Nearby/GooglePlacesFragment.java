package com.hardygtw.travelmemories.fragments.Nearby;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.hardygtw.travelmemories.GPSTracker;
import com.hardygtw.travelmemories.R;
import com.hardygtw.travelmemories.activity.MainActivity;
import com.hardygtw.travelmemories.adapters.CustomListSearchPlacesAdapter;
import com.hardygtw.travelmemories.model.GooglePlace;
import com.hardygtw.travelmemories.views.AutoCompleteView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class GooglePlacesFragment extends Fragment implements AdapterView.OnItemClickListener {

    // Log tag
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String GOOGLE_KEY = "AIzaSyBJbwhMjJjpOi60KSPXAIfTjJiBH591g2Q";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    private ListView listView;
    private CustomListSearchPlacesAdapter adapter;
    private ArrayList<GooglePlace> placeList;
    private AutoCompleteView autoCompView;
    private ImageView searchIcon;
    private GPSTracker gps;
    private double latitude;
    private double longitude;

    private ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + GOOGLE_KEY);
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    private class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected Filter.FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    }
                    else {
                        notifyDataSetInvalidated();
                    }
                }};
            return filter;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_search_places, container, false);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_USE_LOGO | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP); // what's mainly important here is DISPLAY_SHOW_CUSTOM. the rest is optional
        actionBar.setDisplayShowTitleEnabled(true);

        LayoutInflater inflaterActionBar = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // inflate the view that we created before
        View v = inflaterActionBar.inflate(R.layout.search_actionbar, null);

        actionBar.setCustomView(v);

        // the view that contains the search "magnifier" icon
        searchIcon = (ImageView) v.findViewById(R.id.search_icon);

        searchIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                toggleSearch(false);
            }
        });

        autoCompView = (AutoCompleteView) v.findViewById(R.id.placesAutoCompleteTextView);
        autoCompView.setAdapter(new PlacesAutoCompleteAdapter(getActivity(), android.R.layout.simple_list_item_1));
        autoCompView.setOnItemClickListener(this);
        autoCompView.setVisibility(View.INVISIBLE);
        autoCompView.setThreshold(1);

        autoCompView.setOnClearListener(new AutoCompleteView.OnClearListener() {

            @Override
            public void onClear() {
                toggleSearch(true);
            }
        });

        autoCompView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    GooglePlacesSearch googlePlacesSearch;
                    googlePlacesSearch = new GooglePlacesSearch(autoCompView.getText().toString());
                    googlePlacesSearch.execute();
                    return true;
                }
                return false;
            }
        });

        listView = (ListView) rootView.findViewById(R.id.list_search_places);
        //mSearchTerm = (EditText) rootView.findViewById(R.id.search_term);
        placeList = new ArrayList<GooglePlace>();

        // start the AsyncTask that makes the call for the venus search.
        new GooglePlacesNearbySearch().execute();

        return rootView;
    }

    /** this toggles between the visibility of the search icon and the search box
     // to show search icon - reset = true
     // to show search box - reset = false **/
    protected void toggleSearch(boolean reset) {

        if (reset) {
            // hide search box and show search icon
            autoCompView.setText("");
            autoCompView.setVisibility(View.GONE);
            searchIcon.setVisibility(View.VISIBLE);
            // hide the keyboard
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(autoCompView.getWindowToken(), 0);
        } else {
            // hide search icon and show search box
            searchIcon.setVisibility(View.GONE);
            autoCompView.setVisibility(View.VISIBLE);
            autoCompView.requestFocus();
            // show the keyboard
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(autoCompView, InputMethodManager.SHOW_IMPLICIT);
        }
    }


    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
        autoCompView.setText(str);
    }

    /* The click listener for ListView in the navigation drawer */
    private class PlaceItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {

        /**Fragment fragment = null;
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        fragment = new VIEW PLACE();
        ft.replace(R.id.frame_container, fragment, "VIEW_PLACE");

        ft.addToBackStack(null);
        ft.commit();**/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.map:
                Fragment fragment = null;
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                Bundle bundle = new Bundle();
                bundle.putString("MAP", "Map");

                fragment = new NearbyFragment();
                fragment.setArguments(bundle);

                ft.replace(R.id.frame_container, fragment,"MAP_FRAGMENT");
                ft.addToBackStack(null);
                ft.commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private class GooglePlacesNearbySearch extends AsyncTask<View, Void, String> {

        String temp;
        ProgressDialog dialog;
        boolean mFailLocation;

        @Override
        protected void onPreExecute() {
            dialog= ProgressDialog.show(getActivity(), "Loading", "Loading nearby places...");
            GPSTracker gps = new GPSTracker(getActivity());

            if(gps.canGetLocation()) {
                mFailLocation = false;
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();

            } else {
                mFailLocation = true;
                gps.showSettingsAlert();
            }
        }

        @Override
        protected String doInBackground(View... urls) {            // make Call to the url
            if (mFailLocation) {
                return "FAIL";
            } else {
                temp = makeCall("https://maps.googleapis.com/maps/api/place/search/json?location="+ latitude + "," + longitude + "&radius=100&sensor=true&key=" + GOOGLE_KEY );
                return "";
            }
        }

        @Override
        protected void onPostExecute(String result) {

            if (result.equals("FAIL")) {
                dialog.dismiss();
                Toast.makeText(getActivity(), "Failed to retrieve location", Toast.LENGTH_SHORT).show();

            } else {

                if (temp.equals("")) {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "Failed to connect to Google Places", Toast.LENGTH_SHORT).show();
                } else {

                    try {
                        placeList = (ArrayList) parseGooglePlaces(temp);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (placeList.size() == 0) {
                        Toast.makeText(getActivity(), "Failed to find any venues.", Toast.LENGTH_SHORT).show();
                    }

                    adapter = new CustomListSearchPlacesAdapter(getActivity(), placeList);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new PlaceItemClickListener());
                    dialog.dismiss();

                }

            }
        }
    }

    private class GooglePlacesSearch extends AsyncTask<String, Void, String> {

        String temp;
        String searchTerm;
        ProgressDialog dialog;

        public GooglePlacesSearch(String searchTerm) {
            try {
                this.searchTerm = URLEncoder.encode(searchTerm, "UTF-8");

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
            dialog= ProgressDialog.show(getActivity(), "Loading", "Searching Venues...");
        }

        @Override
        protected String doInBackground(String... text) {
            // make Call to the url
            temp = makeCall("https://maps.googleapis.com/maps/api/place/textsearch/json?&query="+ searchTerm + "&radius=100&sensor=true&key=" + GOOGLE_KEY);

            return "";
        }

        @Override
        protected void onPostExecute(String result) {

            if (temp.equals("")) {
                dialog.dismiss();
                Toast.makeText(getActivity(), "Failed to connect to Google Places", Toast.LENGTH_SHORT).show();
            } else {

                try {
                    placeList = (ArrayList) parseGooglePlaces(temp);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (placeList.size() == 0) {
                    Toast.makeText(getActivity(), "Failed to find any venues.", Toast.LENGTH_SHORT).show();
                }

                adapter = new CustomListSearchPlacesAdapter(getActivity(), placeList);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new PlaceItemClickListener());
                dialog.dismiss();

            }

        }

    }

    public static String makeCall(String url) {

        // string buffers the url
        StringBuffer buffer_string = new StringBuffer(url);
        String replyString = "";

        // instantiate an HttpClient
        HttpClient httpclient = new DefaultHttpClient();
        // instantiate an HttpGet
        HttpGet httpget = new HttpGet(buffer_string.toString());

        try {
            // get the response of the HttpClient execution of the url
            HttpResponse response = httpclient.execute(httpget);
            InputStream is = response.getEntity().getContent();

            // buffer input stream the result
            BufferedInputStream bis = new BufferedInputStream(is);
            ByteArrayBuffer baf = new ByteArrayBuffer(20);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }
            // the result as a string is ready for parsing
            replyString = new String(baf.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        // trim the whitespaces
        return replyString.trim();
    }

    private static ArrayList parseGooglePlaces(final String response) throws JSONException {

        ArrayList temp = new ArrayList();
        try {

            // make an jsonObject in order to parse the response
            JSONObject jsonObject = new JSONObject(response);

            if (jsonObject.has("results")) {

                JSONArray venues = jsonObject.getJSONArray("results");

                for (int i = 0; i < venues.length(); i++) {
                      GooglePlace place = new GooglePlace();
                    if (venues.getJSONObject(i).has("name")) {
                        place.setName(venues.getJSONObject(i).optString("name"));
                        //poi.setRating(jsonArray.getJSONObject(i).optString("rating", " "));

                        if (venues.getJSONObject(i).has("vicinity")) {
                            place.setFormatted_address(venues.getJSONObject(i).optString("vicinity"));
                        }

                        if (venues.getJSONObject(i).has("types")) {
                            JSONArray typesArray = venues.getJSONObject(i).getJSONArray("types");

                            for (int j = 0; j < typesArray.length(); j++) {
                                if (j == typesArray.length() - 1) {

                                } else {
                                    place.setCategories(typesArray.getString(j) + ", " + place.getCategories());
                                }
                            }
                        }


                    }
                    temp.add(place);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList();
        }
        return temp;

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    //@Override
    /**public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater){
     super.onCreateOptionsMenu(menu, menuInflater);
     menuInflater.inflate(R.menu.search_places_menu, menu);

     MenuItem searchItem = menu.findItem(R.id.search_icon);
     searchItem.setEnabled(false);
     }**/

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem settingsItem = menu.findItem(R.id.action_settings);
        settingsItem.setVisible(false);
    }

}