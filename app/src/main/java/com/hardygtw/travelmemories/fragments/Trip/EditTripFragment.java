package com.hardygtw.travelmemories.fragments.Trip;


import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.support.v4.app.FragmentTabHost;
import android.widget.ImageView;
import android.widget.TextView;

import com.hardygtw.travelmemories.MainActivity;
import com.hardygtw.travelmemories.R;


public class EditTripFragment extends Fragment {

    private ActionBar actionBar;
    private FragmentTabHost mTabHost;

    public EditTripFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment let me watch the video
        View rootView = inflater.inflate(R.layout.tab_list,container, false);
        String title = getArguments().getString("EDIT_TRIP");
        actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.removeAllTabs();
        ((MainActivity)getActivity()).getDrawerToggle().setDrawerIndicatorEnabled(false);
        mTabHost = (FragmentTabHost)rootView.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.tabFrameLayout);

        mTabHost.addTab(
                mTabHost.newTabSpec("tab1").setIndicator(getTabIndicator(mTabHost.getContext(), R.string.trip_details)),
                EditTripDetailsFragment.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("tab2").setIndicator(getTabIndicator(mTabHost.getContext(), R.string.trip_gallery)),
                EditTripGalleryFragment.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("tab3").setIndicator(getTabIndicator(mTabHost.getContext(), R.string.view_places)),
                EditTripPlacesFragment.class, null);

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
        menuInflater.inflate(R.menu.menu_edit_trip, menu);
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
                Fragment fragment = null;
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                Bundle bundle = new Bundle();
                bundle.putString("VIEW_TRIP", "Trip");

                fragment = new TripListFragment();
                fragment.setArguments(bundle);

                ft.replace(R.id.frame_container, fragment,"TRIP_LIST_FRAGMENT");
                ft.addToBackStack(null);
                ft.commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
