package com.hardygtw.travelmemories.fragments.Nearby;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.hardygtw.travelmemories.R;
import com.hardygtw.travelmemories.fragments.Places.PlaceListFragment;

public class NearbyPlacesFragment extends Fragment {

    public NearbyPlacesFragment(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_nearby_places, container, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater){
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.menu_nearby_places, menu);
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

                fragment = new MapFragment();
                fragment.setArguments(bundle);

                ft.replace(R.id.frame_container, fragment,"MAP_FRAGMENT");
                ft.addToBackStack(null);
                ft.commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {

    }


}