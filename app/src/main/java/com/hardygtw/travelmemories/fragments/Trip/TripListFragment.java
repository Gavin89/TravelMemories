package com.hardygtw.travelmemories.fragments.Trip;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionItemTarget;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;
import com.hardygtw.travelmemories.R;
import com.hardygtw.travelmemories.SQLDatabaseSingleton;
import com.hardygtw.travelmemories.adapters.TripAdapter;
import com.hardygtw.travelmemories.model.Trip;

import java.util.ArrayList;

public class TripListFragment extends Fragment implements GestureDetector.OnGestureListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private ShowcaseView sv;
    private RecyclerView.LayoutManager mLayoutManager;
    private static final String DEBUG_TAG = "Gestures";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        ArrayList<Trip> trips = SQLDatabaseSingleton.getInstance(getActivity()).getAllTrips();

        View rootView = inflater.inflate(R.layout.recycler_view, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);


        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // ... Respond to touch events
                return true;
            }
        });

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new TripAdapter(trips,getFragmentManager());
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
        menuInflater.inflate(R.menu.menu_trips, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_icon:
                Fragment fragment = null;
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                Bundle bundle = new Bundle();
                bundle.putString("NEW_TRIP", "Create New Trip");

                fragment = new NewTripFragment();
                fragment.setArguments(bundle);

                ft.replace(R.id.frame_container, fragment,"NEW_TRIP_FRAGMENT");
                ft.addToBackStack(null);
                ft.commit();
                break;
            case R.id.help_icon:
                         sv =  new ShowcaseView.Builder(getActivity())
                        .setTarget(new ActionViewTarget(getActivity(), ActionViewTarget.Type.HOME))
                        .setContentTitle("Navigation Bar")
                        .setContentText("Navigation bar allows you to navigate through travel memories")
                        .setStyle(R.style.CustomShowcaseTheme)
                        .hideOnTouchOutside()
                        .setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                sv.hide();
                                new ShowcaseView.Builder(getActivity())
                                .setTarget(new ActionItemTarget(getActivity(), R.id.add_icon))
                                .setContentTitle("Add Trip")
                                .setContentText("Use this to add a trip")
                                .hideOnTouchOutside()
                                .setStyle(R.style.CustomShowcaseTheme2)
                                .build();
                            }
                        })
                        .build();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.d(DEBUG_TAG, "onLongPress: " + e.toString());
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}