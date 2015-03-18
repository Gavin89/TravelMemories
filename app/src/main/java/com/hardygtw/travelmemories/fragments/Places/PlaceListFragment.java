package com.hardygtw.travelmemories.fragments.Places;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionItemTarget;
import com.google.android.gms.maps.MapFragment;
import com.hardygtw.travelmemories.R;
import com.hardygtw.travelmemories.SQLDatabaseSingleton;
import com.hardygtw.travelmemories.adapters.PlacesAdapter;
import com.hardygtw.travelmemories.adapters.TripAdapter;
import com.hardygtw.travelmemories.fragments.Places.NewPlaceFragment;

public class PlaceListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ShowcaseView sv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_places_visited, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new PlacesAdapter(SQLDatabaseSingleton.getInstance(getActivity()).getAllPlaceVisits(),getFragmentManager());
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu){
        MenuItem settings = menu.findItem(R.id.action_settings);
        settings.setVisible(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater){
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.menu_places, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Fragment fragment = null;
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        Bundle bundle = new Bundle();

        switch (item.getItemId()) {

            case R.id.map:

                bundle.putString("NEW_PLACE", "Create New Place");

                fragment = new com.hardygtw.travelmemories.fragments.Nearby.MapFragment();
                fragment.setArguments(bundle);

                ft.replace(R.id.frame_container, fragment,"VIEW_PLACES_MAP_FRAGMENT");
                ft.addToBackStack(null);
                ft.commit();
                break;
            case R.id.add_icon:

                bundle.putString("NEW_PLACE", "Create New Place");

                fragment = new NewPlaceFragment();
                fragment.setArguments(bundle);

                ft.replace(R.id.frame_container, fragment,"NEW_PLACE_FRAGMENT");
                ft.addToBackStack(null);
                ft.commit();
                break;
            case R.id.help_icon:
                sv =  new ShowcaseView.Builder(getActivity())
                        .setTarget(new ActionItemTarget(getActivity(), R.id.add_icon))
                        .setContentTitle("Add new place")
                        .setContentText("Select this icon when to add a new place visit")
                        .setStyle(R.style.CustomShowcaseTheme)
                        .hideOnTouchOutside()
                        .setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                sv.hide();
                                new ShowcaseView.Builder(getActivity())
                                        .setTarget(new ActionItemTarget(getActivity(), R.id.map))
                                        .setContentTitle("Places Visited Map")
                                        .setContentText("Select this icon if you wish to see all your places visited on a map")
                                        .hideOnTouchOutside()
                                        .setStyle(R.style.CustomShowcaseTheme2)
                                        .build();
                            }
                        })
                        .build();
        }
        return super.onOptionsItemSelected(item);
    }
}